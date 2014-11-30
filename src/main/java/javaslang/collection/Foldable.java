/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Algebra;
import javaslang.Require;
import javaslang.Tuple;
import javaslang.Tuple.Tuple2;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An interface for inherently recursive data structures.
 *
 * @param <A> A type.
 */
public interface Foldable<A, CLASS extends Foldable<?, CLASS, ?>, SELF extends Foldable<A, ?, SELF>> extends Iterable<A>, Algebra.Monad<A, CLASS>, Algebra.Monoid<SELF> {

    // -- reducing operations

    default <B> B foldLeft(B zero, BiFunction<B, ? super A, B> f) {
        Require.nonNull(f, "function is null");
        B result = zero;
        for (A a : this) {
            result = f.apply(result, a);
        }
        return result;
    }

    default <B> B foldRight(B zero, BiFunction<? super A, B, B> f) {
        Require.nonNull(f, "function is null");
        final Function<A, Function<B, B>> curried = a -> b -> f.apply(a, b);
        return foldMap(Algebra.Monoid.endoMonoid(), curried).apply(zero);
    }

    default <B> B foldMap(Algebra.Monoid<B> monoid, Function<A, B> mapper) {
        Require.nonNull(monoid, "monoid is null");
        Require.nonNull(mapper, "mapper is null");
        return foldLeft(monoid.zero(), (b, a) -> monoid.combine(b, mapper.apply(a)));
    }

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op} from the left.
     *
     * @return The reduced value.
     * @throws UnsupportedOperationException if this Foldable is empty
     */
    default A reduceLeft(BinaryOperator<A> op) {
        Require.nonNull(op, "operator is null");
        if (isEmpty()) {
            throw new UnsupportedOperationException("reduceLeft on empty " + getClass().getSimpleName());
        } else {
            return tail().foldLeft(head(), op);
        }
    }

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op} from the right.
     *
     * @return The reduced value.
     * @throws UnsupportedOperationException if this Foldable is empty
     */
    default A reduceRight(BinaryOperator<A> op) {
        Require.nonNull(op, "operator is null");
        if (isEmpty()) {
            throw new UnsupportedOperationException("reduceRight on empty " + getClass().getSimpleName());
        } else {
            return tail().foldRight(head(), op);
        }
    }

    // -- creational operations

    // @see Algebra.Monad.unit()
    @Override
    <B> Foldable<B, CLASS, ?> unit(B b);

    // @see Algebra.Monoid.zero()
    @Override
    SELF zero();

    // @see Algebra.Monoid.combine()
    @Override
    SELF combine(SELF a1, SELF a2);

    default SELF concat(Foldable<A, ?, SELF> other) {
        Require.nonNull(other, "other is null");
        //noinspection unchecked
        return combine((SELF) this, (SELF) other);
    }

    // -- basic operations

    /**
     * Checks if this Foldable is empty.
     *
     * @return true, if this Foldable contains no elements, falso otherwise.
     */
    default boolean isEmpty() {
        return !iterator().hasNext();
    }

    /**
     * Computes the number of elements in this Foldable.
     *
     * @return The number of elements in this Foldable.
     */
    default int length() {
        // jdk compiler bug: foldLeft(0, (n, ignored) -> n + 1)
        int length = 0;
        for (A ignored : this) {
            length++;
        }
        return length;
    }

    /**
     * Computes the size of this Foldable by calling {@code #length}.
     *
     * @return The number of elements in this Foldable.
     */
    default int size() {
        return length();
    }

    /**
     * Returns the first element of a non-empty Foldable.
     *
     * @return The first element of this Foldable.
     * @throws UnsupportedOperationException if this Foldable is empty
     */
    default A head() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("head on empty " + getClass().getSimpleName());
        } else {
            return iterator().next();
        }
    }

    // dual of tail
    default SELF init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init on empty " + getClass().getSimpleName());
        } else {
            return dropRight(1);
        }
    }

    // dual of head
    default A last() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("last on empty " + getClass().getSimpleName());
        } else {
            return reverse().head();
        }
    }

    /**
     * Drops the first element of a non-empty Foldable.
     *
     * @return A new instance of Foldable containing all elements except the first.
     * @throws UnsupportedOperationException if this Foldable is empty
     */
    @SuppressWarnings("unchecked")
    default SELF tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail on empty " + getClass().getSimpleName());
        } else {
            Foldable xs = zero();
            final Iterator<A> iter = iterator();
            iter.next(); // this is not empty
            while (iter.hasNext()) {
                xs = xs.concat(unit(iter.next()));
            }
            return (SELF) xs;
        }
    }

    // -- filtering & transformations

    default SELF filter(Predicate<A> predicate) {
        //noinspection unchecked
        return foldLeft(zero(), (xs, x) -> predicate.test(x) ? xs.concat((SELF) unit(x)) : xs);
    }

    // @see Algebra.Monad.flatMap()
    @SuppressWarnings("unchecked")
    @Override
    default <B, FOLDABLE extends Algebra.Monad<B, CLASS>> Foldable<B, CLASS, ?> flatMap(Function<? super A, FOLDABLE> mapper) {
        // jdk compiler error: foldLeft((Foldable) zero(), (xs, x) -> xs.concat((Foldable) mapper.apply(x)))
        Foldable xs = zero();
        for (A a : this) {
            final Foldable ys = (Foldable) mapper.apply(a);
            xs = xs.concat(ys);
        }
        return xs;
    }

    // @see Algebra.Monad.map()
    @Override
    default <B> Foldable<B, CLASS, ?> map(Function<? super A, ? extends B> mapper) {
        // jdk compiler error: foldLeft((Foldable) zero(), (xs, x) -> xs.concat(unit(mapper.apply(x))))
        //noinspection RedundantCast
        return (Foldable<B, CLASS, ?>) flatMap(a -> (Foldable<B, CLASS, ?>) unit(mapper.apply(a)));
    }

    /**
     * Inserts a between all elements.
     *
     * @param a An element.
     * @return An 'interspersed' version of this Foldable.
     */
    default SELF intersperse(A a) {
        //noinspection unchecked
        return foldLeft(zero(), (xs, x) -> xs.isEmpty() ? (SELF) unit(x) : xs.concat((SELF) unit(a)).concat((SELF) unit(x)));
    }

    /**
     * Reverses the order of elements.
     *
     * @return The reversed elements.
     */
    default SELF reverse() {
        //noinspection unchecked
        return foldLeft(zero(), (xs, x) -> ((SELF) unit(x)).concat(xs));
    }

    /**
     * Returns a tuple where the first element is the longest prefix of elements that satisfy p and the second element is the remainder.
     *
     * @param p A predicate.
     * @return A Tuple containing the longest prefix of elements that satisfy p and the remainder.
     */
    default Tuple2<SELF, SELF> span(Predicate<A> p) {
        return Tuple.of(takeWhile(p), dropWhile(p));
    }

    /**
     * Splits a Foldable at the specified intex.
     *
     * @param n An index.
     * @return A Tuple containing the first n and the remaining elements.
     */
    default Tuple2<SELF, SELF> splitAt(int n) {
        return Tuple.of(take(n), drop(n));
    }

    @SuppressWarnings("unchecked")
    default <B> Foldable<Tuple2<A, B>, CLASS, ?> zip(Iterable<B> that) {
        Require.nonNull(that, "that is null");
        Foldable xs = zero(); // DEV-NOTE: dirty trick to make concat(xs) work
        Iterator<A> iter1 = this.iterator();
        Iterator<B> iter2 = that.iterator();
        while (iter1.hasNext() && iter2.hasNext()) {
            final Tuple2<A, B> x = Tuple.of(iter1.next(), iter2.next());
            xs = xs.concat(unit(x));
        }
        return xs;
    }

    @SuppressWarnings("unchecked")
    default <B> Foldable<Tuple2<A, B>, CLASS, ?> zipAll(Iterable<B> that, A thisElem, B thatElem) {
        Require.nonNull(that, "that is null");
        Foldable xs = zero(); // DEV-NOTE: dirty trick to make concat(xs) work
        Iterator<A> iter1 = this.iterator();
        Iterator<B> iter2 = that.iterator();
        while (iter1.hasNext() || iter2.hasNext()) {
            final A elem1 = iter1.hasNext() ? iter1.next() : thisElem;
            final B elem2 = iter2.hasNext() ? iter2.next() : thatElem;
            final Tuple2<A, B> x = Tuple.of(elem1, elem2);
            xs = xs.concat(unit(x));
        }
        return xs;
    }

    @SuppressWarnings("unchecked")
    default Foldable<Tuple2<A, Integer>, CLASS, ?> zipWithIndex() {
        Foldable xs = zero(); // DEV-NOTE: dirty trick to make concat(xs) work
        int i = 0;
        for (A a : this) {
            final Tuple2<A, Integer> x = Tuple.of(a, i++);
            xs = xs.concat(unit(x));
        }
        return xs;
    }

    // DEV-NOTE: this is a non-static method because of the generic CLASS parameter
    @SuppressWarnings("unchecked")
    default <A1, A2> Tuple2<Foldable<A1, CLASS, ?>, Foldable<A2, CLASS, ?>> unzip(Foldable<Tuple2<A1, A2>, CLASS, ?> foldable) {
        Foldable xs = zero();
        Foldable ys = zero();
        for (Tuple2<A1, A2> t : foldable) {
            xs = xs.concat(unit(t._1));
            ys = ys.concat(unit(t._2));
        }
        return Tuple.of(xs, ys);
    }

    // -- selection operations

    default SELF drop(int n) {
        if (n <= 0 || isEmpty()) {
            //noinspection unchecked
            return (SELF) this;
        } else {
            return tail().drop(n - 1);
        }
    }

    default SELF dropRight(int n) {
        return take(Math.max(0, length() - n));
    }

    default SELF dropWhile(Predicate<A> predicate) {
        if (isEmpty() || !predicate.test(head())) {
            //noinspection unchecked
            return (SELF) this;
        } else {
            return tail().dropWhile(predicate);
        }
    }

    default SELF take(int n) {
        if (isEmpty()) {
            //noinspection unchecked
            return (SELF) this;
        } else if (n <= 0) {
            return zero();
        } else {
            //noinspection unchecked
            return ((SELF) unit(head())).concat(tail().take(n - 1));
        }
    }

    default SELF takeRight(int n) {
        return reverse().take(n).reverse();
    }

    default SELF takeWhile(Predicate<A> predicate) {
        if (isEmpty()) {
            //noinspection unchecked
            return (SELF) this;
        } else {
            final A head = head();
            if (!predicate.test(head)) {
                return zero();
            } else {
                //noinspection unchecked
                return ((SELF) unit(head)).concat(tail().takeWhile(predicate));
            }
        }
    }
}
