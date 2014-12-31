/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Algebra.Monoid;
import javaslang.Require;
import javaslang.Strings;
import javaslang.Tuple;
import javaslang.Tuple.Tuple2;
import javaslang.monad.Option;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * An interface for inherently recursive data structures. The order of elements is determined by {@link java.lang.Iterable#iterator()}, which may vary each time it is called.
 *
 * <p>Conversion:</p>
 * <ul>
 *     <li>{@link #toJavaArray}</li>
 *     <li>{@link #toJavaList}</li>
 *     <li>{@link #toJavaSet}</li>
 *     <li>{@link #toJavaStream}</li>
 * </ul>
 *
 * <p>Basic operations:</p>
 * <ul>
 *     <li>{@link #clear()}</li>
 *     <li>{@link #contains(Object)}</li>
 *     <li>{@link #containsAll(Iterable)}</li>
 *     <li>{@link #head()}</li>
 *     <li>{@link #init()}</li>
 *     <li>{@link #isEmpty()}</li>
 *     <li>{@link #last()}</li>
 *     <li>{@link #length()}</li>
 *     <li>{@link #tail()}</li>
 * </ul>
 *
 * <p>Filtering</p>
 * <ul>
 *     <li>{@link #filter(java.util.function.Predicate)}</li>
 *     <li>{@link #remove(Object)}</li>
 *     <li>{@link #removeAll(Object)}</li>
 *     <li>{@link #removeAll(Iterable)}</li>
 *     <li>{@link #retainAll(Iterable)}</li>
 * </ul>
 *
 * <p>Reduction:</p>
 * <ul>
 *     <li>{@link #foldLeft(Object, java.util.function.BiFunction)}</li>
 *     <li>{@link #foldRight(Object, java.util.function.BiFunction)}</li>
 *     <li>{@link #foldMap(javaslang.Algebra.Monoid, java.util.function.Function)}</li>
 *     <li>{@link #join()}</li>
 *     <li>{@link #join(CharSequence)}</li>
 *     <li>{@link #join(CharSequence, CharSequence, CharSequence)}</li>
 *     <li>{@link #reduceLeft(java.util.function.BiFunction)}</li>
 *     <li>{@link #reduceRight(java.util.function.BiFunction)}</li>
 * </ul>
 *
 * <p>Selection:</p>
 * <ul>
 *     <li>{@link #drop(int)}</li>
 *     <li>{@link #dropRight(int)}</li>
 *     <li>{@link #dropWhile(java.util.function.Predicate)}</li>
 *     <li>{@link #findAll(java.util.function.Predicate)}</li>
 *     <li>{@link #findFirst(java.util.function.Predicate)}</li>
 *     <li>{@link #findLast(java.util.function.Predicate)}</li>
 *     <li>{@link #take(int)}</li>
 *     <li>{@link #takeRight(int)}</li>
 *     <li>{@link #takeWhile(java.util.function.Predicate)}</li>
 * </ul>
 *
 * <p>Transformation:</p>
 * <ul>
 *     <li>{@link #distinct()}</li>
 *     <li>{@link #flatten()}</li>
 *     <li>TODO: flatMap</li>
 *     <li>TODO: groupBy</li>
 *     <li>{@link #intersperse(Object)}</li>
 *     <li>{@link #map(java.util.function.Function)}</li>
 *     <li>{@link #replace(Object, Object)}</li>
 *     <li>{@link #replaceAll(Object, Object)}</li>
 *     <li>{@link #replaceAll(java.util.function.UnaryOperator)}</li>
 *     <li>{@link #reverse()}</li>
 *     <li>{@link #splitAt(int)}</li>
 *     <li>{@link #span(java.util.function.Predicate)}</li>
 *     <li>{@link #unzip(java.util.function.Function)}</li>
 *     <li>{@link #zip(Iterable)}</li>
 *     <li>{@link #zipAll(Iterable, Object, Object)}</li>
 *     <li>{@link #zipWithIndex()}</li>
 * </ul>
 *
 * @param <A> Component type.
 */
public interface Foldable<A> extends Iterable<A> {

    /**
     * Returns an empty version of this foldable, i.e. {@code this.clear().isEmpty() == true}.
     *
     * @return An empty Foldable.
     */
    Foldable<A> clear();

    /**
     * Tests if this Foldable contains a given value as an element in O(n).
     *
     * @param element An Object of type A, may be null.
     * @return true, if element is in this Foldable, false otherwise.
     */
    default boolean contains(A element) {
        return findFirst(e -> java.util.Objects.equals(e, element)).isPresent();
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

    Foldable<A> distinct();

    /**
     * Drops the first n elements of this Foldable or the all elements, if this size &lt; n. The elements are dropped in O(n).
     *
     * @param n The number of elements to drop.
     * @return A Foldable consisting of all elements of this Foldable except the first n ones, or else an empty Foldable, if this
     * Foldable has less than n elements.
     */
    default Foldable<A> drop(int n) {
        Foldable<A> foldable = this;
        for (int i = n; i > 0 && !foldable.isEmpty(); i--) {
            foldable = foldable.tail();
        }
        return foldable;
    }

    default Foldable<A> dropRight(int n) {
        return reverse().drop(n).reverse();
    }

    default Foldable<A> dropWhile(Predicate<? super A> predicate) {
        Require.nonNull(predicate, "predicate is null");
        Foldable<A> foldable = this;
        while (!foldable.isEmpty() && predicate.test(foldable.head())) {
            foldable = foldable.tail();
        }
        return foldable;
    }

    Foldable<A> filter(Predicate<? super A> predicate);

    /**
     * Essentially the same as {@link #filter(java.util.function.Predicate)} but the result type may differ,
     * i.e. tree.findAll() may be a List.
     *
     * @param predicate A predicate.
     * @return All elements of this which satisfy the given predicate.
     */
    default Foldable<A> findAll(Predicate<? super A> predicate) {
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

    // TODO: flatMap @see Algebra.Monad.flatMap()

    Foldable<A> flatten();

    /**
     * Accumulates the elements of this Foldable by successively calling the given function {@code f} from the left,
     * starting with a value {@code zero} of type B.
     *
     * @param zero Value to start the accumulation with.
     * @param f    The accumulator function.
     * @param <B>  Result type of the accumulator.
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
     * Maps this elements to a Monoid and applies foldLeft, starting with monoid.zero().
     *
     * @param monoid A Monoid
     * @param mapper A mapper
     * @param <B>    Component type of the given monoid.
     * @return The folded monoid value.
     * @throws javaslang.Require.UnsatisfiedRequirementException if monoid or mapper is null
     */
    default <B> B foldMap(Monoid<B> monoid, Function<A, B> mapper) {
        Require.nonNull(monoid, "monoid is null");
        Require.nonNull(mapper, "mapper is null");
        return foldLeft(monoid.zero(), (b, a) -> monoid.combine(b, mapper.apply(a)));
    }

    /**
     * Accumulates the elements of this Foldable by successively calling the given function {@code f} from the right,
     * starting with a value {@code zero} of type B.
     * <p/>
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
     * @param f    The accumulator function.
     * @param <B>  Result type of the accumulator.
     * @return An accumulated version of this.
     * @throws javaslang.Require.UnsatisfiedRequirementException if f is null
     */
    <B> B foldRight(B zero, BiFunction<? super A, ? super B, ? extends B> f);

    /**
     * Returns the first element of a non-empty Foldable.
     *
     * @return The first element of this Foldable.
     * @throws UnsupportedOperationException if this Foldable is empty
     */
    A head();

    // dual of tail regarding reversed order
    Foldable<A> init();

    /**
     * Inserts an element between all elements of this Foldable.
     *
     * @param element An element.
     * @return An 'interspersed' version of this Foldable.
     */
    Foldable<A> intersperse(A element);

    /**
     * Checks if this Foldable is empty.
     *
     * @return true, if this Foldable contains no elements, falso otherwise.
     */
    boolean isEmpty();

    default String join() {
        return join("", "", "");
    }

    default String join(CharSequence delimiter) {
        return join(delimiter, "", "");
    }

    default String join(CharSequence delimiter,
                        CharSequence prefix,
                        CharSequence suffix) {
        final StringBuilder builder = new StringBuilder(prefix);
        map(Strings::toString).intersperse(String.valueOf(delimiter)).forEach(builder::append);
        return builder.append(suffix).toString();
    }

    // dual of head regarding reversed order
    A last();

    /**
     * Computes the number of elements in this Foldable.
     *
     * @return The number of elements in this Foldable.
     */
    int length();

    // @see Algebra.Monad.map()

    /**
     * Maps the elements of this foldable to elements of a new type preserving their order, if any.
     * @param mapper A mapper.
     * @param <B> Component type of the target Foldable
     * @return A mapped Foldable
     */
    <B> Foldable<B> map(Function<? super A, ? extends B> mapper);

    /**
     * Removes the first occurrence of the given element.
     * @param element An element to be removed from this Foldable.
     * @return A Foldable containing all elements of this without the first occurrence of the given element.
     */
    Foldable<A> remove(A element);


    /**
     * Removes all occurrences of the given element.
     * @param element An element to be removed from this Foldable.
     * @return A Foldable containing all elements of this but not the given element.
     */
    Foldable<A> removeAll(A element);

    /**
     * Removes all occurrences of the given elements.
     * @param elements Elements to be removed from this Foldable.
     * @return A Foldable containing all elements of this but none of the given elements.
     */
    Foldable<A> removeAll(Iterable<? extends A> elements);

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op} from the left.
     *
     * @return The reduced value.
     * @throws UnsupportedOperationException                     if this Foldable is empty
     * @throws javaslang.Require.UnsatisfiedRequirementException if op is null
     */
    A reduceLeft(BiFunction<? super A, ? super A, ? extends A> op);

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op} from the right.
     *
     * @return The reduced value.
     * @throws UnsupportedOperationException                     if this Foldable is empty
     * @throws javaslang.Require.UnsatisfiedRequirementException if op is null
     */
    A reduceRight(BiFunction<? super A, ? super A, ? extends A> op);

    /**
     * Replaces the first occurrence (if exists) of the given currentElement with newElement.
     * @param currentElement An element to be substituted.
     * @param newElement A replacement for currentElement.
     * @return A Foldable containing all elements of this where the first occurrence of currentElement is replaced with newELement.
     */
    Foldable<A> replace(A currentElement, A newElement);

    /**
     * Replaces all occurrences of the given currentElement with newElement.
     * @param currentElement An element to be substituted.
     * @param newElement A replacement for currentElement.
     * @return A Foldable containing all elements of this where all occurrences of currentElement are replaced with newELement.
     */
    Foldable<A> replaceAll(A currentElement, A newElement);

    /**
     * Replaces all occurrences of this Foldable by applying the given operator to the elements, which is
     * essentially a special case of {@link #map(java.util.function.Function)}.
     * @param operator An operator.
     * @return A Foldable containing all elements of this transformed within the same domain.
     */
    Foldable<A> replaceAll(UnaryOperator<A> operator);

    /**
     * Keeps all occurrences of the given elements from this.
     * @param elements Elements to be kept.
     * @return A Foldable containing all occurreces of the given elements.
     */
    Foldable<A> retainAll(Iterable<? extends A> elements);

    /**
     * Reverses the order of elements.
     *
     * @return The reversed elements.
     */
    Foldable<A> reverse();

    /**
     * Returns a tuple where the first element is the longest prefix of elements that satisfy p and the second element is the remainder.
     *
     * @param predicate A predicate.
     * @return A Tuple containing the longest prefix of elements that satisfy p and the remainder.
     */
    Tuple2<? extends Foldable<A>, ? extends Foldable<A>> span(Predicate<? super A> predicate);

    /**
     * Splits a Foldable at the specified intex.
     *
     * @param n An index.
     * @return A Tuple containing the first n and the remaining elements.
     */
    default Tuple2<? extends Foldable<A>, ? extends Foldable<A>> splitAt(int n) {
        return Tuple.of(take(n), drop(n));
    }

    /**
     * Drops the first element of a non-empty Foldable.
     *
     * @return A new instance of Foldable containing all elements except the first.
     * @throws UnsupportedOperationException if this Foldable is empty
     */
    Foldable<A> tail();

    Foldable<A> take(int n);

    default Foldable<A> takeRight(int n) {
        return reverse().take(n).reverse();
    }

    Foldable<A> takeWhile(Predicate<? super A> predicate);

    default A[] toJavaArray(Class<A> componentType) {
        final java.util.List<A> list = toJavaList();
        @SuppressWarnings("unchecked")
        final A[] array = list.toArray((A[]) java.lang.reflect.Array.newInstance(componentType, list.size()));
        return array;
    }

    default java.util.List<A> toJavaList() {
        final java.util.List<A> result = new java.util.ArrayList<>();
        for (A a : this) {
            result.add(a);
        }
        return result;
    }

    default java.util.Set<A> toJavaSet() {
        final java.util.Set<A> result = new java.util.HashSet<>();
        for (A a : this) {
            result.add(a);
        }
        return result;
    }

    default java.util.stream.Stream<A> toJavaStream() {
        return toJavaList().stream();
    }

    // DEV-NOTE: Need Manifest here in order to overwrite return type of unzip method by sub-classes.
    <A1, A2> Tuple2<? extends Foldable<A1>, ? extends Foldable<A2>> unzip(Function<? super A, Tuple2<A1, A2>> unzipper);

    <B> Foldable<Tuple2<A, B>> zip(Iterable<B> that);

    <B> Foldable<Tuple2<A, B>> zipAll(Iterable<B> that, A thisElem, B thatElem);

    Foldable<Tuple2<A, Integer>> zipWithIndex();
}
