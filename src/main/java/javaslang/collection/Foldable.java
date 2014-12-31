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
import javaslang.monad.Option;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
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
 * <p/>
 * Implementations of Foldable need to provide implementations for the following methods:
 * <ul>
 *     <li>{@link #iterator()}</li>
 *     <li>{@link #unit(Object)}</li>
 *     <li>{@link #zero()}</li>
 *     <li>{@link #combine(Foldable, Foldable)}</li>
 * </ul>
 * <p/>
 * Implementations of Foldable should overwrite the following methods in order to provide appropriate return types.
 * A super-call in conjunction with a cast does the job.
 * <ul>
 *     <li>{@link #flatMap(java.util.function.Function)}</li>
 *     <li>{@link #map(java.util.function.Function)}</li>
 *     <li>{@link #zip(Iterable)}</li>
 *     <li>{@link #zipAll(Iterable, Object, Object)}</li>
 *     <li>{@link #zipWithIndex()}</li>
 *     <li>{@link #unzip(java.util.function.Function)}</li>
 * </ul>
 * <p/>
 * Additionally it makes sense to provide more efficient implementations for methods where applicable.
 * I.e. javaslang.collection.List overrides head() and tail() because these are natural operations of a linked list.
 *
 * @param <A> A type.
 */
public interface Foldable<A, CLASS extends Foldable<?, CLASS, ?>, SELF extends Foldable<A, ?, SELF>> extends Iterable<A>, Manifest<A, CLASS> {

    // -- reducing operations

    /**
     * Accumulates the elements of this Foldable by successively calling the given function {@code f} from the left,
     * starting with a value {@code zero} of type B.
     *
     * @param zero Value to start the accumulation with.
     * @param f The accumulator function.
     * @param <B> Result type of the accumulator.
     * @return An accumulated version of this.
     */
    default <B> B foldLeft(B zero, BiFunction<? super B, ? super A, ? extends B> f) {
        Require.nonNull(f, "function is null");
        B result = zero;
        for (A a : this) {
            result = f.apply(result, a);
        }
        return result;
    }

    /**
     * Accumulates the elements of this Foldable by successively calling the given function {@code f} from the right,
     * starting with a value {@code zero} of type B.
     * <p>
     * In order to prevent recursive calls, foldRight is implemented based on reverse and foldLeft. A recursive variant
     * is based on foldMap, using the monoid of function composition (endo monoid).
     * <pre>
     * <code>
     * foldRight = reverse().foldLeft(zero, (b, a) -> f.apply(a, b));
     * foldRight = foldMap(Algebra.Monoid.endoMonoid(), a -> b -> f.apply(a, b)).apply(zero);
     * </code>
     * </pre>
     *
     * @param zero Value to start the accumulation with.
     * @param f The accumulator function.
     * @param <B> Result type of the accumulator.
     * @return An accumulated version of this.
     */
    default <B> B foldRight(B zero, BiFunction<? super A, ? super B, ? extends B> f) {
        Require.nonNull(f, "function is null");
        return reverse().foldLeft(zero, (b, a) -> f.apply(a, b));
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
    default A reduceLeft(BiFunction<? super A, ? super A, ? extends A> op) {
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
    default A reduceRight(BiFunction<? super A, ? super A, ? extends A> op) {
        Require.nonNull(op, "operator is null");
        if (isEmpty()) {
            throw new UnsupportedOperationException("reduceRight on empty " + getClass().getSimpleName());
        } else {
            return tail().foldRight(head(), op);
        }
    }

    default String join() {
        return join("", "", "");
    }

    default String join(CharSequence delimiter) {
        return join(delimiter, "", "");
    }

    default String join(CharSequence delimiter,
                        CharSequence prefix,
                        CharSequence suffix) {
        final Foldable<String, ?, ?> mapped = map(Objects::toString);
        return prefix + (mapped.isEmpty() ? "" : mapped.reduceLeft((s1,s2) -> s1 + delimiter + s2)) + suffix;
    }

    // -- creational operations

    // @see Algebra.Monad.unit()
    <B> Foldable<B, CLASS, ?> unit(B b);

    // @see Algebra.Monoid.zero()
    SELF zero();

    // @see Algebra.Monoid.combine()
    SELF combine(SELF a1, SELF a2);

    @SuppressWarnings("unchecked")
    default SELF concat(Foldable<? super A, ?, ? extends SELF> other) {
        Require.nonNull(other, "other is null");
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
        // need cast because of jdk 1.8.0_25 compiler error
        //noinspection RedundantCast
        return (int) foldLeft(0, (n, ignored) -> n + 1);
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

    @SuppressWarnings("unchecked")
    default SELF distinct() {
        return foldLeft(zero(), (xs, x) -> xs.contains(x) ? xs : xs.concat((SELF) unit(x)));
    }

    @SuppressWarnings("unchecked")
    default SELF filter(Predicate<? super A> predicate) {
        Require.nonNull(predicate, "predicate is null");
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
    @SuppressWarnings("unchecked")
    default SELF intersperse(A element) {
        return foldLeft(zero(), (xs, x) -> xs.isEmpty() ? (SELF) unit(x) : xs.concat((SELF) unit(element)).concat((SELF) unit(x)));
    }

    /**
     * Reverses the order of elements.
     *
     * @return The reversed elements.
     */
    @SuppressWarnings("unchecked")
    default SELF reverse() {
        return foldLeft(zero(), (xs, x) -> ((SELF) unit(x)).concat(xs));
    }

    /**
     * Returns a tuple where the first element is the longest prefix of elements that satisfy p and the second element is the remainder.
     *
     * @param predicate A predicate.
     * @return A Tuple containing the longest prefix of elements that satisfy p and the remainder.
     */
    default Tuple2<SELF, SELF> span(Predicate<? super A> predicate) {
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
        Foldable xs = zero(); // DEV-NOTE: untyped to make concat(xs) work
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
        Foldable xs = zero(); // DEV-NOTE: untyped to make concat(xs) work
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
        Foldable xs = zero(); // DEV-NOTE: untyped to make concat(xs) work
        int i = 0;
        for (A a : this) {
            final Tuple2<A, Integer> x = Tuple.of(a, i++);
            xs = xs.concat(unit(x));
        }
        return xs;
    }

    // DEV-NOTE: Need Manifest here in order to overwrite return type of unzip method by sub-classes.
    @SuppressWarnings("unchecked")
    default <A1, A2> Tuple2<? extends Manifest<A1,CLASS>, ? extends Manifest<A2,CLASS>> unzip(Function<? super A, Tuple2<A1, A2>> unzipper) {
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
     * Tests if this Foldable contains a given value as an element in O(n).
     *
     * @param element An Object of type A, may be null.
     * @return true, if element is in this Foldable, false otherwise.
     */
    default boolean contains(A element) {
        for (A a : this) {
            if (Objects.equals(a, element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests if this Foldable contains all given elements in O(n*m), where n = length of this and m = length of given elements.
     * <p/>
     * The result is equivalent to
     * {@code elements.isEmpty() ? true : contains(elements.head()) && containsAll(elements.tail())} but implemented
     * without recursion.
     *
     * @param elements A List of values of type E.
     * @return true, if this List contains all given elements, false otherwise.
     * @throws javaslang.Require.UnsatisfiedRequirementException if elements is null
     */
    default boolean containsAll(Iterable<? extends A> elements) {
        Require.nonNull(elements, "elements is null");
        return List.of(elements)
                .distinct()
                .findFirst(e -> !this.contains(e))
                .isNotPresent();
    }

    /**
     * Alias for {@link #filter(java.util.function.Predicate)}.
     *
     * @param predicate A predicate.
     * @return All elements of this which satisfy the given predicate.
     */
    default SELF findAll(Predicate<? super A> predicate) {
        return filter(predicate);
    }

    /**
     * Returns the first element of this which satisfies the given predicate.
     *
     * @param predicate A predicate.
     * @return Some(element) or None, where element may be null (i.e. {@code List.of(null).findFirst(e -> e == null)}).
     */
    default Option<A> findFirst(Predicate<? super A> predicate) {
        for (A a : this) {
            if (predicate.test(a)) {
                return new Option.Some<>(a); // may be Some(null)
            }
        }
        return Option.none();
    }

    /**
     * Returns the last element of this which satisfies the given predicate.
     * <p/>
     * Same as {@code reverse().findFirst(predicate)}.
     *
     * @param predicate A predicate.
     * @return Some(element) or None, where element may be null (i.e. {@code List.of(null).findFirst(e -> e == null)}).
     */
    default Option<A> findLast(Predicate<? super A> predicate) {
        return reverse().findFirst(predicate);
    }

    /**
     * Drops the first n elements of this Foldable or the all elements, if this size &lt; n. The elements are dropped in O(n).
     *
     * @param n The number of elements to drop.
     * @return A Foldable consisting of all elements of this Foldable except the first n ones, or else an empty Foldable, if this
     * Foldable has less than n elements.
     */
    default SELF drop(int n) {
        @SuppressWarnings("unchecked")
        SELF foldable = (SELF) this;
        for (int i = n; i > 0 && !foldable.isEmpty(); i--) {
            foldable = foldable.tail();
        }
        return foldable;
    }

    default SELF dropRight(int n) {
        return reverse().drop(n).reverse();
    }

    default SELF dropWhile(Predicate<? super A> predicate) {
        Require.nonNull(predicate, "predicate is null");
        @SuppressWarnings("unchecked")
        SELF foldable = (SELF) this;
        while (!foldable.isEmpty() && predicate.test(foldable.head())) {
            foldable = foldable.tail();
        }
        return foldable;
    }

    @SuppressWarnings("unchecked")
    default SELF take(int n) {
        SELF result = zero();
        SELF foldable = (SELF) this;
        for (int i = n; i > 0 && !foldable.isEmpty(); i--) {
            result = result.concat((SELF) unit(foldable.head()));
            foldable = foldable.tail();
        }
        return result;
    }

    default SELF takeRight(int n) {
        return reverse().take(n).reverse();
    }

    @SuppressWarnings("unchecked")
    default SELF takeWhile(Predicate<? super A> predicate) {
        Require.nonNull(predicate, "predicate is null");
        SELF result = zero();
        SELF foldable = (SELF) this;
        while (!foldable.isEmpty() && predicate.test(foldable.head())) {
            result = result.concat((SELF) unit(foldable.head()));
            foldable = foldable.tail();
        }
        return result;
    }
}
