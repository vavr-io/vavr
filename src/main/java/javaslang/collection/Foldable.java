/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Algebra;
import javaslang.Manifest;
import javaslang.Require;
import javaslang.Tuple;
import javaslang.Tuple.Tuple2;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An interface for inherently recursive data structures. The order of elements is determined by {@link java.lang.Iterable#iterator()}, which may vary each time it is called.
 * <p/>
 * <ul>
 * <li>unit(a) - constructor, stating that the given element shares all functional properties of this Foldable</li>
 * <li>zero() - the neutral element of this Foldable, i.e. an empty collection</li>
 * <li>combine(a,b) - summarizes two elements in a general way, i.e. combines two tree branches to one tree instance</li>
 * </ul>
 *
 * @param <A> A type.
 */
public interface Foldable<A, CLASS extends Foldable<?, CLASS, ?>, SELF extends Foldable<A, ?, SELF>> extends Iterable<A>, Manifest<A, CLASS> {

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
    <B> Foldable<B, CLASS, ?> unit(B b);

    // @see Algebra.Monoid.zero()
    SELF zero();

    // @see Algebra.Monoid.combine()
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
        Require.nonNull(predicate, "predicate is null");
        //noinspection unchecked
        return foldLeft(zero(), (xs, x) -> predicate.test(x) ? xs.concat((SELF) unit(x)) : xs);
    }

    // @see Algebra.Monad.flatMap()
    @SuppressWarnings("unchecked")
    default <B, FOLDABLE extends Manifest<B, CLASS>> Foldable<B, CLASS, ?> flatMap(Function<? super A, FOLDABLE> mapper) {
        Require.nonNull(mapper, "mapper is null");
        // need cast because of jdk 1.8.0_25 compiler error
        return (Foldable<B, CLASS, ?>) foldLeft((Foldable) zero(), (xs, x) -> xs.concat((Foldable) mapper.apply(x)));
    }

    // @see Algebra.Monad.map()
    @SuppressWarnings("unchecked")
    default <B> Foldable<B, CLASS, ?> map(Function<? super A, ? extends B> mapper) {
        Require.nonNull(mapper, "mapper is null");
        // need cast because of jdk 1.8.0_25 compiler error
        return (Foldable<B, CLASS, ?>) foldLeft((Foldable) zero(), (xs, x) -> xs.concat(unit(mapper.apply(x))));
    }

    /**
     * Inserts an element between all elements of this Foldable.
     *
     * @param element An element.
     * @return An 'interspersed' version of this Foldable.
     */
    default SELF intersperse(A element) {
        //noinspection unchecked
        return foldLeft(zero(), (xs, x) -> xs.isEmpty() ? (SELF) unit(x) : xs.concat((SELF) unit(element)).concat((SELF) unit(x)));
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
     * @param predicate A predicate.
     * @return A Tuple containing the longest prefix of elements that satisfy p and the remainder.
     */
    default Tuple2<SELF, SELF> span(Predicate<A> predicate) {
        Require.nonNull(predicate, "predicate is null");
        return Tuple.of(takeWhile(predicate), dropWhile(predicate));
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

    // DEV-NOTE: returning raw type Tuple2 is the only possibility for implementations to return an appropriate type
    @SuppressWarnings("unchecked")
    default <A1, A2> Tuple2/*<Foldable<A1, CLASS, ?>, Foldable<A2, CLASS, ?>>*/ unzip(Function<A, Tuple2<A1, A2>> unzipper) {
        Require.nonNull(unzipper, "unzipper is null");
        Foldable xs = zero();
        Foldable ys = zero();
        for (A a : this) {
            final Tuple2<A1, A2> t = unzipper.apply(a);
            xs = xs.concat(unit(t._1));
            ys = ys.concat(unit(t._2));
        }
        return Tuple.of(xs, ys);
    }

    // -- selection operations

    /**
     * Drops the first n elements of this Foldable by successively applying {@link #tail}.
     * @param n The number of elements to be dropped.
     * @return A new Foldable based on this, whithout the first n elements.
     */
    default SELF drop(int n) {
        //noinspection unchecked
        SELF foldable = (SELF) this;
        for (int i = n; i > 0 && !foldable.isEmpty(); i--) {
            foldable = foldable.tail();
        }
        return foldable;
    }

    default SELF dropRight(int n) {
        return reverse().drop(n).reverse();
    }

    default SELF dropWhile(Predicate<A> predicate) {
        Require.nonNull(predicate, "predicate is null");
        //noinspection unchecked
        SELF foldable = (SELF) this;
        while (!foldable.isEmpty() && predicate.test(foldable.head())) {
            foldable = foldable.tail();
        }
        return foldable;
    }

    default SELF take(int n) {
        SELF result = zero();
        //noinspection unchecked
        SELF foldable = (SELF) this;
        for (int i = n; i > 0 && !foldable.isEmpty(); i--) {
            //noinspection unchecked
            result = result.concat((SELF) unit(foldable.head()));
            foldable = foldable.tail();
        }
        return result;
    }

    default SELF takeRight(int n) {
        return reverse().take(n).reverse();
    }

    default SELF takeWhile(Predicate<A> predicate) {
        Require.nonNull(predicate, "predicate is null");
        SELF result = zero();
        //noinspection unchecked
        SELF foldable = (SELF) this;
        while(!foldable.isEmpty() && predicate.test(foldable.head())) {
            //noinspection unchecked
            result = result.concat((SELF) unit(foldable.head()));
            foldable = foldable.tail();
        }
        return result;
    }
}
