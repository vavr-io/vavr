/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Algebra.Monoid;
import javaslang.Manifest;
import javaslang.Require;
import javaslang.Strings;
import javaslang.Tuple.Tuple2;
import javaslang.monad.Option;

import java.util.function.*;

/**
 * An interface for inherently recursive data structures. The order of elements is determined by {@link java.lang.Iterable#iterator()}, which may vary each time it is called.
 * <p/>
 * <p>Conversion:</p>
 * <ul>
 * <li>{@link #toJavaArray(Class)}</li>
 * <li>{@link #toJavaList()}</li>
 * <li>{@link #toJavaMap(java.util.function.Function)}</li>
 * <li>{@link #toJavaSet()}</li>
 * <li>{@link #toJavaStream()}</li>
 * </ul>
 * <p/>
 * <p>Basic operations:</p>
 * <ul>
 * <li>{@link #clear()}</li>
 * <li>{@link #contains(Object)}</li>
 * <li>{@link #containsAll(Iterable)}</li>
 * <li>{@link #forEach(Consumer)}</li>
 * <li>{@link #head()}</li>
 * <li>{@link #init()}</li>
 * <li>{@link #isEmpty()}</li>
 * <li>{@link #last()}</li>
 * <li>{@link #length()}</li>
 * <li>{@link #tail()}</li>
 * </ul>
 * <p/>
 * <p>Filtering</p>
 * <ul>
 * <li>{@link #filter(java.util.function.Predicate)}</li>
 * <li>{@link #remove(Object)}</li>
 * <li>{@link #removeAll(Object)}</li>
 * <li>{@link #removeAll(Iterable)}</li>
 * <li>{@link #retainAll(Iterable)}</li>
 * </ul>
 * <p/>
 * <p>Reduction:</p>
 * <ul>
 * <li>{@link #foldLeft(Object, java.util.function.BiFunction)}</li>
 * <li>{@link #foldRight(Object, java.util.function.BiFunction)}</li>
 * <li>{@link #foldMap(javaslang.Algebra.Monoid, java.util.function.Function)}</li>
 * <li>{@link #join()}</li>
 * <li>{@link #join(CharSequence)}</li>
 * <li>{@link #join(CharSequence, CharSequence, CharSequence)}</li>
 * <li>{@link #reduceLeft(java.util.function.BiFunction)}</li>
 * <li>{@link #reduceRight(java.util.function.BiFunction)}</li>
 * </ul>
 * <p/>
 * <p>Selection:</p>
 * <ul>
 * <li>{@link #drop(int)}</li>
 * <li>{@link #dropRight(int)}</li>
 * <li>{@link #dropWhile(java.util.function.Predicate)}</li>
 * <li>{@link #findAll(java.util.function.Predicate)}</li>
 * <li>{@link #findFirst(java.util.function.Predicate)}</li>
 * <li>{@link #findLast(java.util.function.Predicate)}</li>
 * <li>{@link #take(int)}</li>
 * <li>{@link #takeRight(int)}</li>
 * <li>{@link #takeWhile(java.util.function.Predicate)}</li>
 * </ul>
 * <p/>
 * <p>Transformation:</p>
 * <ul>
 * <li>{@link #distinct()}</li>
 * <li>{@link #flatMap(java.util.function.Function)}</li>
 * <li>TODO: groupBy</li>
 * <li>{@link #intersperse(Object)}</li>
 * <li>{@link #map(java.util.function.Function)}</li>
 * <li>{@link #replace(Object, Object)}</li>
 * <li>{@link #replaceAll(Object, Object)}</li>
 * <li>{@link #replaceAll(java.util.function.UnaryOperator)}</li>
 * <li>{@link #reverse()}</li>
 * <li>{@link #splitAt(int)}</li>
 * <li>{@link #span(java.util.function.Predicate)}</li>
 * <li>{@link #unzip(java.util.function.Function)}</li>
 * <li>{@link #zip(Iterable)}</li>
 * <li>{@link #zipAll(Iterable, Object, Object)}</li>
 * <li>{@link #zipWithIndex()}</li>
 * </ul>
 *
 * @param <T> Component type.
 */
public interface Traversable<T> extends Iterable<T>, Manifest<T, Traversable<?>> {

    /**
     * Returns an empty version of this traversable, i.e. {@code this.clear().isEmpty() == true}.
     *
     * @return An empty Traversable.
     */
    Traversable<T> clear();

    /**
     * Tests if this Traversable contains a given value.
     *
     * @param element An Object of type A, may be null.
     * @return true, if element is in this Traversable, false otherwise.
     */
    default boolean contains(T element) {
        return findFirst(e -> java.util.Objects.equals(e, element)).isPresent();
    }

    /**
     * Tests if this Traversable contains all given elements.
     * <p/>
     * The result is equivalent to
     * {@code elements.isEmpty() ? true : contains(elements.head()) && containsAll(elements.tail())} but implemented
     * without recursion.
     *
     * @param elements A List of values of type E.
     * @return true, if this List contains all given elements, false otherwise.
     * @throws javaslang.Require.UnsatisfiedRequirementException if elements is null
     */
    default boolean containsAll(Iterable<? extends T> elements) {
        Require.nonNull(elements, "elements is null");
        return List.of(elements)
                .distinct()
                .findFirst(e -> !this.contains(e))
                .isNotPresent();
    }

    Traversable<T> distinct();

    /**
     * Drops the first n elements of this Traversable or the all elements, if this length &lt; n.
     *
     * @param n The number of elements to drop.
     * @return A Traversable consisting of all elements of this Traversable except the first n ones, or else an empty Traversable, if this
     * Traversable has less than n elements.
     */
    default Traversable<T> drop(int n) {
        Traversable<T> traversable = this;
        for (int i = n; i > 0 && !traversable.isEmpty(); i--) {
            traversable = traversable.tail();
        }
        return traversable;
    }

    default Traversable<T> dropRight(int n) {
        return reverse().drop(n).reverse();
    }

    default Traversable<T> dropWhile(Predicate<? super T> predicate) {
        Require.nonNull(predicate, "predicate is null");
        Traversable<T> traversable = this;
        while (!traversable.isEmpty() && predicate.test(traversable.head())) {
            traversable = traversable.tail();
        }
        return traversable;
    }

    Traversable<T> filter(Predicate<? super T> predicate);

    /**
     * Essentially the same as {@link #filter(java.util.function.Predicate)} but the result type may differ,
     * i.e. tree.findAll() may be a List.
     *
     * @param predicate A predicate.
     * @return All elements of this which satisfy the given predicate.
     */
    default Traversable<T> findAll(Predicate<? super T> predicate) {
        return filter(predicate);
    }

    /**
     * Returns the first element of this which satisfies the given predicate.
     *
     * @param predicate A predicate.
     * @return Some(element) or None, where element may be null (i.e. {@code List.of(null).findFirst(e -> e == null)}).
     */
    default Option<T> findFirst(Predicate<? super T> predicate) {
        for (T a : this) {
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
    default Option<T> findLast(Predicate<? super T> predicate) {
        return reverse().findFirst(predicate);
    }

    <U, TRAVERSABLE extends Manifest<U, Traversable<?>>> Traversable<U> flatMap(Function<? super T, TRAVERSABLE> mapper);

    /**
     * Accumulates the elements of this Traversable by successively calling the given function {@code f} from the left,
     * starting with a value {@code zero} of type B.
     *
     * @param zero Value to start the accumulation with.
     * @param f    The accumulator function.
     * @param <U>  Result type of the accumulator.
     * @return An accumulated version of this.
     */
    default <U> U foldLeft(U zero, BiFunction<? super U, ? super T, ? extends U> f) {
        Require.nonNull(f, "function is null");
        U result = zero;
        for (T a : this) {
            result = f.apply(result, a);
        }
        return result;
    }

    /**
     * Maps this elements to a Monoid and applies foldLeft, starting with monoid.zero().
     *
     * @param monoid A Monoid
     * @param mapper A mapper
     * @param <U>    Component type of the given monoid.
     * @return The folded monoid value.
     * @throws javaslang.Require.UnsatisfiedRequirementException if monoid or mapper is null
     */
    default <U> U foldMap(Monoid<U> monoid, Function<T, U> mapper) {
        Require.nonNull(monoid, "monoid is null");
        Require.nonNull(mapper, "mapper is null");
        return foldLeft(monoid.zero(), (b, a) -> monoid.combine(b, mapper.apply(a)));
    }

    /**
     * Accumulates the elements of this Traversable by successively calling the given function {@code f} from the right,
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
     * @param <U>  Result type of the accumulator.
     * @return An accumulated version of this.
     * @throws javaslang.Require.UnsatisfiedRequirementException if f is null
     */
    default <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        Require.nonNull(f, "function is null");
        return reverse().foldLeft(zero, (b, a) -> f.apply(a, b));
    }

    default void forEach(Consumer<? super T> action) {
        for (T t : this) {
            action.accept(t);
        }
    }

    /**
     * Returns the first element of a non-empty Traversable.
     *
     * @return The first element of this Traversable.
     * @throws UnsupportedOperationException if this Traversable is empty
     */
    T head();

    // dual of tail regarding reversed order
    Traversable<T> init();

    /**
     * Inserts an element between all elements of this Traversable.
     *
     * @param element An element.
     * @return An 'interspersed' version of this Traversable.
     */
    Traversable<T> intersperse(T element);

    /**
     * Checks if this Traversable is empty.
     *
     * @return true, if this Traversable contains no elements, falso otherwise.
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
    default T last() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("last of empty Traversable");
        } else {
            Traversable<T> traversable = this;
            { // don't let escape tail
                Traversable<T> tail;
                while (!(tail = traversable.tail()).isEmpty()) {
                    traversable = tail;
                }
            }
            return traversable.head();
        }
    }

    /**
     * Computes the number of elements in this Traversable.
     *
     * @return The number of elements in this Traversable.
     */
    @SuppressWarnings("RedundantCast")
    default int length() {
        // cast because of jdk 1.8.0_25 compiler error
        return (int) foldLeft(0, (n, ignored) -> n + 1);
    }

    /**
     * Maps the elements of this traversable to elements of a new type preserving their order, if any.
     *
     * @param mapper A mapper.
     * @param <U>    Component type of the target Traversable
     * @return A mapped Traversable
     * @see javaslang.Algebra.Monad#map(Function)
     */
    <U> Traversable<U> map(Function<? super T, ? extends U> mapper);

    /**
     * Removes the first occurrence of the given element.
     *
     * @param element An element to be removed from this Traversable.
     * @return A Traversable containing all elements of this without the first occurrence of the given element.
     */
    Traversable<T> remove(T element);


    /**
     * Removes all occurrences of the given element.
     *
     * @param element An element to be removed from this Traversable.
     * @return A Traversable containing all elements of this but not the given element.
     */
    Traversable<T> removeAll(T element);

    /**
     * Removes all occurrences of the given elements.
     *
     * @param elements Elements to be removed from this Traversable.
     * @return A Traversable containing all elements of this but none of the given elements.
     */
    Traversable<T> removeAll(Iterable<? extends T> elements);

    /**
     * Accumulates the elements of this Traversable by successively calling the given operation {@code op} from the left.
     *
     * @return The reduced value.
     * @throws UnsupportedOperationException                     if this Traversable is empty
     * @throws javaslang.Require.UnsatisfiedRequirementException if op is null
     */
    T reduceLeft(BiFunction<? super T, ? super T, ? extends T> op);

    /**
     * Accumulates the elements of this Traversable by successively calling the given operation {@code op} from the right.
     *
     * @return The reduced value.
     * @throws UnsupportedOperationException                     if this Traversable is empty
     * @throws javaslang.Require.UnsatisfiedRequirementException if op is null
     */
    T reduceRight(BiFunction<? super T, ? super T, ? extends T> op);

    /**
     * Replaces the first occurrence (if exists) of the given currentElement with newElement.
     *
     * @param currentElement An element to be substituted.
     * @param newElement     A replacement for currentElement.
     * @return A Traversable containing all elements of this where the first occurrence of currentElement is replaced with newELement.
     */
    Traversable<T> replace(T currentElement, T newElement);

    /**
     * Replaces all occurrences of the given currentElement with newElement.
     *
     * @param currentElement An element to be substituted.
     * @param newElement     A replacement for currentElement.
     * @return A Traversable containing all elements of this where all occurrences of currentElement are replaced with newELement.
     */
    Traversable<T> replaceAll(T currentElement, T newElement);

    /**
     * Replaces all occurrences of this Traversable by applying the given operator to the elements, which is
     * essentially a special case of {@link #map(java.util.function.Function)}.
     *
     * @param operator An operator.
     * @return A Traversable containing all elements of this transformed within the same domain.
     */
    Traversable<T> replaceAll(UnaryOperator<T> operator);

    /**
     * Keeps all occurrences of the given elements from this.
     *
     * @param elements Elements to be kept.
     * @return A Traversable containing all occurreces of the given elements.
     */
    Traversable<T> retainAll(Iterable<? extends T> elements);

    /**
     * Reverses the order of elements.
     *
     * @return The reversed elements.
     */
    Traversable<T> reverse();

    /**
     * Returns a tuple where the first element is the longest prefix of elements that satisfy p and the second element is the remainder.
     *
     * @param predicate A predicate.
     * @return A Tuple containing the longest prefix of elements that satisfy p and the remainder.
     */
    Tuple2<? extends Traversable<T>, ? extends Traversable<T>> span(Predicate<? super T> predicate);

    /**
     * Splits a Traversable at the specified intex.
     *
     * @param n An index.
     * @return A Tuple containing the first n and the remaining elements.
     */
    Tuple2<? extends Traversable<T>, ? extends Traversable<T>> splitAt(int n);

    /**
     * Drops the first element of a non-empty Traversable.
     *
     * @return A new instance of Traversable containing all elements except the first.
     * @throws UnsupportedOperationException if this Traversable is empty
     */
    Traversable<T> tail();

    /**
     * Takes the first n elements of this Traversable or all elements, if this length &lt; n.
     * <p/>
     * The result is equivalent to {@code sublist(0, n)} but does not throw if n &lt; 0 or n &gt; length(). In the case of
     * n &lt; 0 the empty Traversable is returned, in the case of n &gt; length() this Traversable is returned.
     *
     * @param n The number of elements to take.
     * @return A Traversable consisting of the first n elements of this Traversable or all elements, if it has less than n elements.
     */
    Traversable<T> take(int n);

    default Traversable<T> takeRight(int n) {
        return reverse().take(n).reverse();
    }

    Traversable<T> takeWhile(Predicate<? super T> predicate);

    default T[] toJavaArray(Class<T> componentType) {
        final java.util.List<T> list = toJavaList();
        @SuppressWarnings("unchecked")
        final T[] array = list.toArray((T[]) java.lang.reflect.Array.newInstance(componentType, list.size()));
        return array;
    }

    default java.util.List<T> toJavaList() {
        final java.util.List<T> result = new java.util.ArrayList<>();
        for (T a : this) {
            result.add(a);
        }
        return result;
    }

    default <K, V> java.util.Map<K, V> toJavaMap(Function<? super T, Tuple2<K, V>> f) {
        final java.util.Map<K, V> map = new java.util.HashMap<>();
        for (T a : this) {
            final Tuple2<K, V> entry = f.apply(a);
            map.put(entry._1, entry._2);
        }
        return map;
    }

    default java.util.Set<T> toJavaSet() {
        final java.util.Set<T> result = new java.util.HashSet<>();
        for (T a : this) {
            result.add(a);
        }
        return result;
    }

    default java.util.stream.Stream<T> toJavaStream() {
        return toJavaList().stream();
    }

    <T1, T2> Tuple2<? extends Traversable<T1>, ? extends Traversable<T2>> unzip(Function<? super T, Tuple2<T1, T2>> unzipper);

    /**
     * Returns a Traversable formed from this Traversable and another Iterable collection by combining corresponding elements
     * in pairs. If one of the two Traversables is longer than the other, its remaining elements are ignored.
     *
     * @param <U>  The type of the second half of the returned pairs.
     * @param that The Iterable providing the second half of each result pair.
     * @return a new Traversable containing pairs consisting of corresponding elements of this list and that.
     * The length of the returned collection is the minimum of the lengths of this Traversable and that.
     * @throws javaslang.Require.UnsatisfiedRequirementException if that is null.
     */
    <U> Traversable<Tuple2<T, U>> zip(Iterable<U> that);

    /**
     * Returns a Traversable formed from this Traversable and another Iterable by combining corresponding elements in
     * pairs. If one of the two collections is shorter than the other, placeholder elements are used to extend the
     * shorter collection to the length of the longer.
     *
     * @param <U>      The type of the second half of the returned pairs.
     * @param that     The Iterable providing the second half of each result pair.
     * @param thisElem The element to be used to fill up the result if this Traversable is shorter than that.
     * @param thatElem The element to be used to fill up the result if that is shorter than this Traversable.
     * @return A new Traversable containing pairs consisting of corresponding elements of this Traversable and that.
     * The length of the returned Traversable is the maximum of the lengths of this Traversable and that.
     * If this Traversable is shorter than that, thisElem values are used to fill the result.
     * If that is shorter than this Traversable, thatElem values are used to fill the result.
     * @throws javaslang.Require.UnsatisfiedRequirementException if that is null.
     */
    <U> Traversable<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem);

    /**
     * Zips this List with its indices.
     *
     * @return A new List containing all elements of this List paired with their index, starting with 0.
     */
    Traversable<Tuple2<T, Integer>> zipWithIndex();
}
