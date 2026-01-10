/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import io.vavr.PartialFunction;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.Value;
import io.vavr.control.Option;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.*;
import java.util.stream.DoubleStream;
import org.jspecify.annotations.NonNull;

/**
 * Represents a recursive, multi-valued data structure whose elements can be traversed in order.
 * The iteration order is determined by {@link Iterable#iterator()} and may vary across calls.
 *
 * <p>This interface provides operations for:
 * <ul>
 *   <li><b>Basic access:</b> querying elements, length, head/tail, and emptiness.</li>
 *   <li><b>Iteration:</b> indexed traversal, sliding windows, and grouping.</li>
 *   <li><b>Numeric computations:</b> sum, product, min/max, and averages.</li>
 *   <li><b>Reduction and folding:</b> folding, reducing, and string representation.</li>
 *   <li><b>Selection and slicing:</b> take/drop, filtering, partitioning, and sub-sequencing.</li>
 *   <li><b>Testing:</b> uniqueness, order, distinctness, and sequence properties.</li>
 *   <li><b>Transformation:</b> mapping, flat-mapping, scanning, zipping, and deduplication.</li>
 * </ul>
 *
 * <p>Implementations may be lazy or strict and may support infinite sequences.
 *
 * @param <T> the type of elements contained in this Traversable
 * @author Daniel Dietrich, Grzegorz Piwowarek
 */
public interface Traversable<T> extends Foldable<T>, Value<T> {

    /**
     * Narrows a {@code Traversable<? extends T>} to {@code Traversable<T>} with a type-safe cast.
     * <p>
     * This is safe because immutable or read-only collections are covariant in their element type.
     *
     * @param traversable the {@code Traversable} instance to narrow
     * @param <T>         the element type of the resulting {@code Traversable}
     * @return the same {@code traversable} instance with type {@code Traversable<T>}
     */
    @SuppressWarnings("unchecked")
    static <T> Traversable<T> narrow(Traversable<? extends T> traversable) {
        return (Traversable<T>) traversable;
    }

    /**
     * Groups elements by a unique key extracted from each element.
     * <p>
     * Returns {@code None} if any key occurs more than once; otherwise, returns a {@code Map}
     * where each key is associated with its corresponding element.
     *
     * @param getKey a function to extract a key from each element
     * @param <K>    the type of keys
     * @return an {@code Option} containing the {@code Map} of elements by key, or {@code None} if keys are not unique
     * @throws NullPointerException if {@code getKey} is null
     * @see #groupBy(Function)
     */
    default <K> Option<Map<K, T>> arrangeBy(@NonNull Function<? super T, ? extends K> getKey) {
        Objects.requireNonNull(getKey, "getKey is null");
        return Option.of(groupBy(getKey).mapValues(Traversable<T>::singleOption))
          .filter(map -> !map.exists(kv -> kv._2.isEmpty()))
          .map(map -> Map.narrow(map.mapValues(Option::get)));
    }


    /**
     * Computes the average of the elements, assuming they are of type {@link Number}.
     * <p>
     * If the elements are not numeric, an {@link UnsupportedOperationException} is thrown.
     * <p>
     * Examples:
     *
     * <pre>{@code
     * List.empty().average()                       // = None
     * List.of(1, 2, 3).average()                   // = Some(2.0)
     * List.of(1.0, 1e100, 2.0, -1e100).average()  // = Some(0.75)
     * List.of(1.0, Double.NaN).average()           // = NaN
     * List.of("apple", "pear").average()           // throws
     * }</pre>
     *
     * <p>
     * Unlike Java's {@link DoubleStream#average()} which uses the Kahan summation algorithm,
     * Vavr uses Neumaier's modification of Kahan's algorithm for improved numerical accuracy.
     *
     * @return {@code Some(average)} if the sequence has elements, otherwise {@code None}
     * @throws UnsupportedOperationException if any element is not numeric
     */
    default Option<Double> average() {
        try {
            final double[] sum = TraversableModule.neumaierSum(this, t -> ((Number) t).doubleValue());
            final double count = sum[1];
            return (count == 0) ? Option.none() : Option.some(sum[0] / count);
        } catch (ClassCastException x) {
            throw new UnsupportedOperationException("Elements are not numeric", x);
        }
    }

    /**
     * Applies a {@link PartialFunction} to all elements that are defined for it and collects the results.
     * <p>
     * For each element in iteration order, the function is first tested:
     *
     * <pre>{@code
     * partialFunction.isDefinedAt(element)
     * }</pre>
     *
     * If {@code true}, the element is mapped to type {@code R}:
     *
     * <pre>{@code
     * R newElement = partialFunction.apply(element)
     * }</pre>
     *
     * <p><strong>Note:</strong> If this {@code Traversable} is ordered (i.e., extends {@link Ordered}),
     * the caller must ensure that the resulting elements are comparable (i.e., implement {@link Comparable}).
     *
     * @param partialFunction a function that may not be defined for all elements of this traversable
     * @param <R> the type of elements in the resulting {@code Traversable}
     * @return a new {@code Traversable} containing the results of applying the partial function
     * @throws NullPointerException if {@code partialFunction} is null
     */
    <R> Traversable<R> collect(@NonNull PartialFunction<? super T, ? extends R> partialFunction);

    /**
     * Checks whether this {@code Traversable} contains all elements from the given iterable.
     * <p>
     * Equivalent to testing each element individually:
     * <pre>{@code
     * elements.isEmpty() ? true : contains(elements.head()) && containsAll(elements.tail())
     * }</pre>
     * but implemented efficiently without recursion.
     *
     * @param elements an {@code Iterable} of elements to check for containment
     * @return {@code true} if all elements are present, {@code false} otherwise
     * @throws NullPointerException if {@code elements} is null
     */
    default boolean containsAll(@NonNull Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        for (T element : elements) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Counts the number of elements that satisfy the given predicate.
     *
     * @param predicate a condition to test each element
     * @return the number of elements matching the predicate (always >= 0)
     * @throws NullPointerException if {@code predicate} is null
     */
    default int count(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return foldLeft(0, (i, t) -> predicate.test(t) ? i + 1 : i);
    }

    /**
     * Returns a new {@code Traversable} containing the elements of this instance
     * with all duplicates removed. Element equality is determined using {@code equals}.
     *
     * @return a new {@code Traversable} without duplicate elements
     */
    Traversable<T> distinct();

    /**
     * Returns a new {@code Traversable} containing the elements of this instance
     * without duplicates, as determined by the given {@code comparator}.
     *
     * @param comparator a comparator used to determine equality of elements
     * @return a new {@code Traversable} with duplicates removed
     * @throws NullPointerException if {@code comparator} is null
     */
    Traversable<T> distinctBy(@NonNull Comparator<? super T> comparator);

    /**
     * Returns a new {@code Traversable} containing the elements of this instance
     * without duplicates, based on keys extracted from elements using {@code keyExtractor}.
     * <p>
     * The first occurrence of each key is retained in the resulting sequence.
     *
     * @param keyExtractor a function to extract keys for determining uniqueness
     * @param <U>          the type of key
     * @return a new {@code Traversable} with duplicates removed based on keys
     * @throws NullPointerException if {@code keyExtractor} is null
     */
    <U> Traversable<T> distinctBy(@NonNull Function<? super T, ? extends U> keyExtractor);

    /**
     * Returns a new {@code Traversable} without the first {@code n} elements,
     * or an empty instance if this contains fewer than {@code n} elements.
     *
     * @param n the number of elements to drop
     * @return a new instance excluding the first {@code n} elements
     */
    Traversable<T> drop(int n);

    /**
     * Returns a new {@code Traversable} without the last {@code n} elements,
     * or an empty instance if this contains fewer than {@code n} elements.
     *
     * @param n the number of elements to drop from the end
     * @return a new instance excluding the last {@code n} elements
     */
    Traversable<T> dropRight(int n);

    /**
     * Returns a new {@code Traversable} starting from the first element
     * that satisfies the given {@code predicate}, dropping all preceding elements.
     *
     * @param predicate a condition tested on each element
     * @return a new instance starting from the first element matching the predicate
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> dropUntil(@NonNull Predicate<? super T> predicate);

    /**
     * Returns a new {@code Traversable} starting from the first element
     * that does not satisfy the given {@code predicate}, dropping all preceding elements.
     * <p>
     * This is equivalent to {@code dropUntil(predicate.negate())}, which is useful
     * for method references that cannot be negated directly.
     *
     * @param predicate a condition tested on each element
     * @return a new instance starting from the first element not matching the predicate
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> dropWhile(@NonNull Predicate<? super T> predicate);


    /**
     * Determines whether this collection is equal to the given object.
     * <p>
     * In Vavr, there are four basic collection types:
     * <ul>
     *     <li>{@code Seq} – sequential elements</li>
     *     <li>{@code Set} – distinct elements</li>
     *     <li>{@code Map} – key-value pairs</li>
     *     <li>{@code Multimap} – keys mapped to multiple values</li>
     * </ul>
     * Two collections are considered equal if and only if:
     * <ul>
     *     <li>They are of the same collection type (Seq, Set, Map, Multimap)</li>
     *     <li>They contain the same elements</li>
     *     <li>For {@code Seq}, the element order is the same</li>
     * </ul>
     * <p>
     * For {@code Map} and {@code Multimap}, two entries {@code (key1, value1)} and {@code (key2, value2)}
     * are equal if both their keys and values are equal.
     * <p>
     * <strong>Additional notes:</strong>
     * <ul>
     *     <li>No collection equals {@code null} (e.g., {@code Queue(1) != null})</li>
     *     <li>Null elements are allowed and treated as expected
     *         (e.g., {@code List(null, 1) == Stream(null, 1)}, {@code HashMap((null,1)) == LinkedHashMap((null,1))})</li>
     *     <li>Element order matters only for {@code Seq}</li>
     *     <li>Other collection classes are equal if their types and elements (in iteration order) are equal</li>
     *     <li>Iterators are compared by reference only</li>
     * </ul>
     *
     * @param obj the object to compare with, may be {@code null}
     * @return {@code true} if the collections are equal according to the rules above, {@code false} otherwise
     */
    boolean equals(Object obj);

    /**
     * Checks whether there is exactly one element in this traversable for which the given predicate holds.
     *
     * @param predicate the condition to test elements
     * @return {@code true} if exactly one element satisfies the predicate, {@code false} otherwise
     * @throws NullPointerException if {@code predicate} is {@code null}
     */
    default boolean existsUnique(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        boolean exists = false;
        for (T t : this) {
            if (predicate.test(t)) {
                if (exists) {
                    return false; // more than one found
                } else {
                    exists = true; // first match found
                }
            }
        }
        return exists;
    }

    /**
     * Returns a new traversable containing only the elements that satisfy the given predicate.
     *
     * @param predicate the condition to test elements
     * @return a traversable with elements matching the predicate
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> filter(@NonNull Predicate<? super T> predicate);

    /**
     * Returns a new traversable containing only the elements that do not satisfy the given predicate.
     * <p>
     * This is equivalent to {@code filter(predicate.negate())}.
     *
     * @param predicate the condition to test elements
     * @return a traversable with elements not matching the predicate
     * @throws NullPointerException if {@code predicate} is null
     */
    default Traversable<T> reject(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }
    /**
     * Returns the first element that satisfies the given predicate.
     *
     * @param predicate the condition to test elements
     * @return {@code Some(element)} if a matching element is found, otherwise {@code None};
     *         the element may be {@code null}
     * @throws NullPointerException if {@code predicate} is null
     */
    default Option<T> find(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (T a : this) {
            if (predicate.test(a)) {
                return Option.some(a); // may be Some(null)
            }
        }
        return Option.none();
    }

    /**
     * Returns the last element that satisfies the given predicate.
     * <p>
     * Equivalent to {@code reverse().find(predicate)} but potentially more efficient.
     *
     * @param predicate the condition to test elements
     * @return {@code Some(element)} if a matching element is found, otherwise {@code None};
     *         the element may be {@code null}
     * @throws NullPointerException if {@code predicate} is null
     */
    default Option<T> findLast(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return iterator().findLast(predicate);
    }

    /**
     * Transforms each element of this Traversable into an {@code Iterable} of elements and
     * flattens the resulting iterables into a single Traversable.
     *
     * @param mapper a function mapping elements to iterables
     * @param <U> the type of elements in the resulting Traversable
     * @return a new Traversable containing all elements produced by applying {@code mapper} and flattening
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> Traversable<U> flatMap(@NonNull Function<? super T, ? extends Iterable<? extends U>> mapper);

    @Override
    default <U> U foldLeft(U zero, @NonNull BiFunction<? super U, ? super T, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        U xs = zero;
        for (T x : this) {
            xs = f.apply(xs, x);
        }
        return xs;
    }

    @Override
    <U> U foldRight(U zero, @NonNull BiFunction<? super T, ? super U, ? extends U> f);

    /**
     * Performs the given action on each element of this Traversable along with its index.
     *
     * <p>This method is more efficient than {@code iterator().zipWithIndex().forEach()} because
     * it avoids creating intermediate {@code Tuple2} objects and boxing integers.</p>
     *
     * <p>Note that the iteration order may vary between calls depending on the underlying
     * Traversable implementation. Also, if this Traversable is lazily evaluated (e.g., a {@link Stream}),
     * the method may loop indefinitely.</p>
     *
     * @param action an action to perform on each element and its index
     * @throws NullPointerException if {@code action} is null
     */
    default void forEachWithIndex(@NonNull ObjIntConsumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        int index = 0;
        for (T t : this) {
            action.accept(t, index++);
        }
    }

    /**
     * Returns the first element of this {@code Traversable} in iteration order.
     *
     * @return the first element
     * @throws NoSuchElementException if this {@code Traversable} is empty
     */
    @Override
    default T get() {
        return head();
    }

    /**
     * Groups elements of this {@code Traversable} based on a classifier function.
     *
     * @param classifier A function that assigns each element to a group
     * @param <C>        The type of the group keys
     * @return A map where each key corresponds to a group of elements
     * @throws NullPointerException if {@code classifier} is null
     * @see #arrangeBy(Function)
     */
    <C> Map<C, ? extends Traversable<T>> groupBy(@NonNull Function<? super T, ? extends C> classifier);

    /**
     * Splits this {@code Traversable} into consecutive blocks of the given size.
     * <p>
     * Let {@code length} be the number of elements in this {@code Traversable}:
     * <ul>
     *     <li>If empty, the resulting {@code Iterator} is empty.</li>
     *     <li>If {@code size <= length}, the resulting {@code Iterator} contains
     *         {@code length / size} blocks of size {@code size} and possibly a final smaller block of size {@code length % size}.</li>
     *     <li>If {@code size > length}, the resulting {@code Iterator} contains a single block of size {@code length}.</li>
     * </ul>
     *
     * <p>Examples:</p>
     * <pre>
     * {@code
     * [].grouped(1) = []
     * [].grouped(0) throws
     * [].grouped(-1) throws
     * [1,2,3,4].grouped(2) = [[1,2],[3,4]]
     * [1,2,3,4,5].grouped(2) = [[1,2],[3,4],[5]]
     * [1,2,3,4].grouped(5) = [[1,2,3,4]]
     * }
     * </pre>
     *
     * <p>Note: {@code grouped(size)} is equivalent to {@code sliding(size, size)}.</p>
     *
     * @param size the block size; must be positive
     * @return an {@code Iterator} over blocks of elements
     * @throws IllegalArgumentException if {@code size} is zero or negative
     */
    Iterator<? extends Traversable<T>> grouped(int size);

    /**
     * Indicates whether this {@code Traversable} has a known finite size.
     * <p>
     * This should typically be implemented by concrete classes, not interfaces.
     *
     * @return {@code true} if the number of elements is finite and known, {@code false} otherwise.
     */
    boolean hasDefiniteSize();

    /**
     * Returns the first element of this non-empty {@code Traversable}.
     *
     * @return the first element
     * @throws NoSuchElementException if this {@code Traversable} is empty
     */
    T head();

    /**
     * Returns the first element of this {@code Traversable} as an {@link Option}.
     *
     * @return {@code Some(element)} if non-empty, otherwise {@code None}
     */
    default Option<T> headOption() {
        return isEmpty() ? Option.none() : Option.some(head());
    }

    /**
     * Returns the hash code of this collection.
     *
     * <p>Vavr distinguishes between collections with predictable iteration order (like {@code Seq}) and
     * collections with arbitrary iteration order (like {@code Set}, {@code Map}, and {@code Multimap}).
     * In all cases, the hash of an empty collection is defined as {@code 1}.</p>
     *
     * <p>For collections with predictable iteration order, the hash is computed as:</p>
     * <pre>{@code
     * int hash = 1;
     * for (T t : this) {
     *     hash = hash * 31 + Objects.hashCode(t);
     * }
     * }</pre>
     *
     * <p>For collections with arbitrary iteration order, the hash is computed to be independent of element order:</p>
     * <pre>{@code
     * int hash = 1;
     * for (T t : this) {
     *     hash += Objects.hashCode(t);
     * }
     * }</pre>
     *
     * <p>Note that these algorithms may change in future Vavr versions. Hash codes are generally <em>not</em> cached,
     * unlike size/length, because caching would increase memory usage due to persistent tree-based structures.
     * Computing the hash code is linear in time, O(n). For frequently reused collections (e.g., as {@code HashMap} keys),
     * caching can be done externally using a wrapper, for example:</p>
     *
     * <pre>{@code
     * public final class Hashed<K> {
     *     private final K key;
     *     private final Lazy<Integer> hashCode;
     *
     *     public Hashed(K key) {
     *         this.key = key;
     *         this.hashCode = Lazy.of(() -> Objects.hashCode(key));
     *     }
     *
     *     public K key() { return key; }
     *
     *     @Override
     *     public boolean equals(Object o) {
     *         if (o == key) return true;
     *         if (key != null && o instanceof Hashed) return key.equals(((Hashed<?>) o).key);
     *         return false;
     *     }
     *
     *     @Override
     *     public int hashCode() { return hashCode.get(); }
     *
     *     @Override
     *     public String toString() { return "Hashed(" + key + ")"; }
     * }
     * }</pre>
     *
     * @return the hash code of this collection
     */
    int hashCode();


    /**
     * Returns all elements of this Traversable except the last one.
     * <p>
     * This is the dual of {@link #tail()}.
     *
     * @return a new instance containing all elements except the last
     * @throws UnsupportedOperationException if this Traversable is empty
     */
    Traversable<T> init();

    /**
     * Returns all elements of this Traversable except the last one, wrapped in an {@code Option}.
     * <p>
     * This is the dual of {@link #tailOption()}.
     *
     * @return {@code Some(traversable)} if non-empty, or {@code None} if this Traversable is empty
     */
    default Option<? extends Traversable<T>> initOption() {
        return isEmpty() ? Option.none() : Option.some(init());
    }

    /**
     * Indicates whether this Traversable may contain only distinct elements.
     *
     * @return {@code true} if this Traversable may contain only distinct elements, {@code false} otherwise
     */
    default boolean isDistinct() {
        return false;
    }

    /**
     * Checks if this Traversable contains no elements.
     *
     * @return {@code true} if empty, {@code false} otherwise
     */
    @Override
    default boolean isEmpty() {
        return length() == 0;
    }

    /**
     * Indicates whether this Traversable is ordered according to its natural or specified order.
     *
     * @return {@code true} if this Traversable is ordered, {@code false} otherwise
     */
    default boolean isOrdered() {
        return false;
    }

    /**
     * Indicates whether the elements of this Traversable appear in encounter (insertion) order.
     *
     * @return {@code true} if insertion order is preserved, {@code false} otherwise
     */
    default boolean isSequential() {
        return false;
    }

    /**
     * Indicates that this Traversable may contain multiple elements.
     *
     * @return {@code false} since Traversable is multi-valued by design
     */
    @Override
    default boolean isSingleValued() {
        return false;
    }

    /**
     * Checks if this Traversable can be traversed multiple times without side effects.
     * <p>
     * Implementations should provide the correct behavior; this is not meant for interfaces alone.
     *
     * @return {@code true} if this Traversable is guaranteed to be repeatably traversable, {@code false} otherwise
     */
    boolean isTraversableAgain();

    /**
     * Returns an iterator over the elements of this Traversable, implemented via {@link #head()} and {@link #tail()}.
     * Subclasses may override for a more efficient implementation.
     *
     * @return a new {@link Iterator} over the elements of this Traversable
     */
    @Override
    default @NonNull Iterator<T> iterator() {
        final Traversable<T> that = this;
        return new AbstractIterator<T>() {

            Traversable<T> traversable = that;

            @Override
            public boolean hasNext() {
                return !traversable.isEmpty();
            }

            @Override
            public T getNext() {
                final T result = traversable.head();
                traversable = traversable.tail();
                return result;
            }
        };
    }

    /**
     * Returns the last element of this Traversable.
     *
     * @return the last element
     * @throws NoSuchElementException if this Traversable is empty
     */
    T last();

    /**
     * Returns the last element of this Traversable as an {@code Option}.
     *
     * @return {@code Some(element)} if not empty, otherwise {@code None}
     */
    default Option<T> lastOption() {
        return isEmpty() ? Option.none() : Option.some(last());
    }

    /**
     * Returns the number of elements in this Traversable.
     * <p>
     * Equivalent to {@link #size()}.
     *
     * @return the number of elements
     */
    int length();

    /**
     * Transforms the elements of this Traversable to a new type, preserving order if defined.
     *
     * @param mapper a mapping function
     * @param <U>   the target element type
     * @return a new Traversable containing the mapped elements
     * @throws NullPointerException if {@code mapper} is null
     */
    @Override
    <U> Traversable<U> map(@NonNull Function<? super T, ? extends U> mapper);


    @Override
    default <U> Traversable<U> mapTo(U value) {
        return map(ignored -> value);
    }

    @Override
    default Traversable<Void> mapToVoid() {
        return map(ignored -> null);
    }

    /**
     * Returns the maximum element of this Traversable according to the natural order of its elements.
     * <p>
     * Note that the underlying order of sorted collections is not considered—only the natural ordering of elements matters.
     * <p>
     * Examples:
     * <pre>{@code
     * List.empty().max()             // = None
     * List.of(1, 2, 3).max()         // = Some(3)
     * List.of("a", "b", "c").max()   // = Some("c")
     * List.of(1.0, Double.NaN).max() // = NaN
     * List.of(1, "a").max()          // throws ClassCastException
     * }</pre>
     *
     * @return {@code Some(maximum)} if this Traversable is not empty, otherwise {@code None}
     * @throws NullPointerException if any element is null
     * @throws ClassCastException if elements do not implement {@link Comparable}
     */
    default Option<T> max() {
        return maxBy(Comparators.naturalComparator());
    }

    /**
     * Returns the maximum element of this Traversable according to the given comparator.
     * <p>
     * If the Traversable is empty, {@code None} is returned.
     *
     * @param comparator a non-null {@link Comparator} to determine element ordering
     * @return {@code Some(maximum)} if this Traversable is not empty, otherwise {@code None}
     * @throws NullPointerException if {@code comparator} is null
     */
    default Option<T> maxBy(@NonNull Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        if (isEmpty()) {
            return Option.none();
        } else {
            final T value = reduce((t1, t2) -> comparator.compare(t1, t2) >= 0 ? t1 : t2);
            return Option.some(value);
        }
    }


    /**
     * Returns the element of this Traversable whose mapped value, according to the given function, is maximal.
     * <p>
     * The mapping function {@code f} transforms elements of type {@code T} to a comparable type {@code U},
     * and the element with the largest {@code U} value is returned.
     *
     * @param f   a non-null function mapping elements to a comparable type
     * @param <U> the type used for comparison, must implement {@link Comparable}
     * @return {@code Some(element)} whose mapped value is maximal, or {@code None} if this Traversable is empty
     * @throws NullPointerException if {@code f} is null
     */
    default <U extends Comparable<? super U>> Option<T> maxBy(@NonNull Function<? super T, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        if (isEmpty()) {
            return Option.none();
        } else {
            final Iterator<T> iter = iterator();
            T tm = iter.next();
            U um = f.apply(tm);
            while (iter.hasNext()) {
                final T t = iter.next();
                final U u = f.apply(t);
                if (u.compareTo(um) > 0) {
                    um = u;
                    tm = t;
                }
            }
            return Option.some(tm);
        }
    }

    /**
     * Returns the minimum element of this Traversable according to its natural order in O(n).
     * <p>
     * The underlying order of sorted collections is not considered. For numeric types {@link Double} and {@link Float},
     * if any element is {@code NaN}, the result is {@code NaN} instead of following the natural order.
     * <p>
     * Examples:
     * <pre>
     * {@code
     * List.empty().min()             // = None
     * List.of(1, 2, 3).min()         // = Some(1)
     * List.of("a", "b", "c").min()   // = Some("a")
     * List.of(1.0, Double.NaN).min() // = NaN
     * List.of(1, "a").min()          // throws
     * }
     * </pre>
     *
     * @return {@code Some(minimum)} of this elements, or {@code None} if this Traversable is empty
     * @throws NullPointerException if an element is null
     * @throws ClassCastException   if the elements do not have a natural order, i.e., do not implement {@link Comparable}
     */
    @SuppressWarnings("unchecked")
    default Option<T> min() {
        // DEV-NOTE: minBy(Comparators.naturalComparator()) does not handle (Double/Float) NaN correctly
        if (isEmpty()) {
            return Option.none();
        } else {
            final T head = head();
            final T min;
            if (head instanceof Double) {
                min = (T) ((Traversable<Double>) this).foldLeft((Double) head, Math::min);
            } else if (head instanceof Float) {
                min = (T) ((Traversable<Float>) this).foldLeft((Float) head, Math::min);
            } else {
                final Comparator<T> comparator = Comparators.naturalComparator();
                min = this.foldLeft(head, (t1, t2) -> comparator.compare(t1, t2) <= 0 ? t1 : t2);
            }
            return Option.some(min);
        }
    }


    /**
     * Returns the minimum element of this Traversable according to a given comparator.
     *
     * @param comparator a non-null comparator used to determine ordering
     * @return {@code Some(minimum)} of this elements, or {@code None} if this Traversable is empty
     * @throws NullPointerException if {@code comparator} is null
     */
    default Option<T> minBy(@NonNull Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        if (isEmpty()) {
            return Option.none();
        } else {
            final T value = reduce((t1, t2) -> comparator.compare(t1, t2) <= 0 ? t1 : t2);
            return Option.some(value);
        }
    }

    /**
     * Returns the element of this Traversable whose mapped value is minimal according to natural order.
     *
     * @param f   a function mapping elements to a comparable value
     * @param <U> the type of the comparable value
     * @return the element of type T whose mapped value is minimal, wrapped in {@code Some}, or {@code None} if empty
     * @throws NullPointerException if {@code f} is null
     */
    default <U extends Comparable<? super U>> Option<T> minBy(@NonNull Function<? super T, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        if (isEmpty()) {
            return Option.none();
        } else {
            final Iterator<T> iter = iterator();
            T tm = iter.next();
            U um = f.apply(tm);
            while (iter.hasNext()) {
                final T t = iter.next();
                final U u = f.apply(t);
                if (u.compareTo(um) < 0) {
                    um = u;
                    tm = t;
                }
            }
            return Option.some(tm);
        }
    }

    /**
     * Concatenates the string representations of all elements in this Traversable.
     * <p>
     * Equivalent to {@code mkCharSeq("", "", "")}.
     *
     * @return a new {@link CharSeq} containing all elements concatenated
     */
    default CharSeq mkCharSeq() {
        return mkCharSeq("", "", "");
    }

    /**
     * Concatenates the string representations of all elements in this Traversable, separated by a delimiter.
     * <p>
     * Equivalent to {@code mkCharSeq("", delimiter, "")}.
     *
     * @param delimiter a string placed between elements
     * @return a new {@link CharSeq} with elements separated by the delimiter
     */
    default CharSeq mkCharSeq(CharSequence delimiter) {
        return mkCharSeq("", delimiter, "");
    }

    /**
     * Concatenates the string representations of all elements in this Traversable with a prefix, delimiter, and suffix.
     * <p>
     * Example: {@code List.of("a", "b", "c").mkCharSeq("Chars(", ", ", ")") = CharSeq.of("Chars(a, b, c)")}
     *
     * @param prefix    a string prepended to the result
     * @param delimiter a string placed between elements
     * @param suffix    a string appended to the result
     * @return a new {@link CharSeq} containing the formatted concatenation of elements
     */
    default CharSeq mkCharSeq(CharSequence prefix, CharSequence delimiter, CharSequence suffix) {
        return CharSeq.of(mkString(prefix, delimiter, suffix));
    }

    /**
     * Concatenates the string representations of all elements in this Traversable.
     * <p>
     * Equivalent to {@code mkString("", "", "")}.
     *
     * @return a new {@link String} containing all elements concatenated
     */
    default String mkString() {
        return mkString("", "", "");
    }

    /**
     * Concatenates the string representations of all elements in this Traversable, separated by a delimiter.
     * <p>
     * Equivalent to {@code mkString("", delimiter, "")}.
     *
     * @param delimiter a string placed between elements
     * @return a new {@link String} containing the concatenated elements
     */
    default String mkString(CharSequence delimiter) {
        return mkString("", delimiter, "");
    }

    /**
     * Concatenates the string representations of all elements in this Traversable with a prefix, delimiter, and suffix.
     * <p>
     * Example: {@code List.of("a", "b", "c").mkString("Chars(", ", ", ")") = "Chars(a, b, c)"}
     *
     * @param prefix    a string prepended to the result
     * @param delimiter a string placed between elements
     * @param suffix    a string appended to the result
     * @return a new {@link String} containing the formatted concatenation of elements
     */
    default String mkString(CharSequence prefix, CharSequence delimiter, CharSequence suffix) {
        final StringBuilder builder = new StringBuilder(prefix);
        iterator().map(String::valueOf).intersperse(String.valueOf(delimiter)).forEach(builder::append);
        return builder.append(suffix).toString();
    }

    /**
     * Checks if this {@code Traversable} contains at least one element.
     * <p>
     * Equivalent to {@code !isEmpty()}.
     *
     * @return {@code true} if this Traversable is not empty, {@code false} otherwise
     */
    default boolean nonEmpty() {
        return !isEmpty();
    }

    /**
     * Returns this {@code Traversable} if it is non-empty; otherwise, returns the given alternative.
     *
     * @param other an alternative {@code Traversable} to return if this is empty
     * @return this {@code Traversable} if non-empty, otherwise {@code other}
     */
    Traversable<T> orElse(Iterable<? extends T> other);

    /**
     * Returns this {@code Traversable} if it is non-empty; otherwise, returns the result of evaluating the given supplier.
     *
     * @param supplier a supplier of an alternative {@code Traversable} if this is empty
     * @return this {@code Traversable} if non-empty, otherwise the result of {@code supplier.get()}
     * @throws NullPointerException if {@code supplier} is null
     */
    Traversable<T> orElse(@NonNull Supplier<? extends Iterable<? extends T>> supplier);

    /**
     * Splits this {@code Traversable} into two partitions according to a predicate.
     * <p>
     * The first partition contains all elements that satisfy the predicate, and the second contains all elements that do not.
     * The original iteration order is preserved.
     *
     * @param predicate a predicate used to classify elements
     * @return a {@link Tuple2} containing the two resulting {@code Traversable} instances
     * @throws NullPointerException if {@code predicate} is null
     */
    Tuple2<? extends Traversable<T>, ? extends Traversable<T>> partition(@NonNull Predicate<? super T> predicate);


    @Override
    Traversable<T> peek(@NonNull Consumer<? super T> action);

    /**
     * Calculates the product of the elements in this {@code Traversable}.
     * <p>
     * Supported element types are {@code Byte}, {@code Double}, {@code Float}, {@code Integer}, {@code Long},
     * {@code Short}, {@code BigInteger}, and {@code BigDecimal}.
     * <p>
     * Examples:
     * <pre>{@code
     * List.empty().product()              // = 1
     * List.of(1, 2, 3).product()          // = 6L
     * List.of(0.1, 0.2, 0.3).product()    // = 0.006
     * List.of("apple", "pear").product()  // throws
     * }</pre>
     * <p>
     * For type-safe multiplication of elements, consider using {@link #fold(Object, BiFunction)}.
     *
     * @return a {@code Number} representing the product of the elements
     * @throws UnsupportedOperationException if the elements are not numeric
     */
    @SuppressWarnings("unchecked")
    default Number product() {
        if (isEmpty()) {
            return 1;
        } else {
            try {
                final Iterator<?> iter = iterator();
                final Object o = iter.next();
                if (o instanceof Integer || o instanceof Long || o instanceof Byte || o instanceof Short) {
                    return ((Iterator<Number>) iter).foldLeft(((Number) o).longValue(), (product, number) -> product * number.longValue());
                } else if (o instanceof BigInteger) {
                    return ((Iterator<BigInteger>) iter).foldLeft(((BigInteger) o), BigInteger::multiply);
                } else if (o instanceof BigDecimal) {
                    return ((Iterator<BigDecimal>) iter).foldLeft(((BigDecimal) o), BigDecimal::multiply);
                } else {
                    return ((Iterator<Number>) iter).toJavaStream().mapToDouble(Number::doubleValue).reduce(((Number) o).doubleValue(), (d1, d2) -> d1 * d2);
                }
            } catch(ClassCastException x) {
                throw new UnsupportedOperationException("not numeric", x);
            }
        }
    }

    /**
     * Reduces the elements of this Traversable from the left using the given binary operation.
     *
     * @param op A binary operation combining two elements of type T
     * @return the result of the reduction
     * @throws NoSuchElementException if this Traversable is empty
     * @throws NullPointerException   if {@code op} is null
     */
    @Override
    default T reduceLeft(@NonNull BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return iterator().reduceLeft(op);
    }

    /**
     * Reduces the elements of this Traversable from the left using the given binary operation,
     * returning the result wrapped in an {@code Option}.
     *
     * @param op A binary operation combining two elements of type T
     * @return {@code Some(reduced value)} or {@code None} if this Traversable is empty
     * @throws NullPointerException if {@code op} is null
     */
    @Override
    default Option<T> reduceLeftOption(@NonNull BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return isEmpty() ? Option.none() : Option.some(reduceLeft(op));
    }

    /**
     * Reduces the elements of this Traversable from the right using the given binary operation.
     *
     * @param op A binary operation combining two elements of type T
     * @return the result of the reduction
     * @throws NoSuchElementException if this Traversable is empty
     * @throws NullPointerException   if {@code op} is null
     */
    @Override
    default T reduceRight(@NonNull BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        if (isEmpty()) {
            throw new NoSuchElementException("reduceRight on empty Traversable");
        } else {
            return iterator().reduceRight(op);
        }
    }

    /**
     * Reduces the elements of this Traversable from the right using the given binary operation,
     * returning the result wrapped in an {@code Option}.
     *
     * @param op A binary operation combining two elements of type T
     * @return {@code Some(reduced value)} or {@code None} if this Traversable is empty
     * @throws NullPointerException if {@code op} is null
     */
    @Override
    default Option<T> reduceRightOption(@NonNull BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return isEmpty() ? Option.none() : Option.some(reduceRight(op));
    }

    /**
     * Replaces the first occurrence of {@code currentElement} with {@code newElement}, if it exists.
     *
     * @param currentElement the element to be replaced
     * @param newElement     the replacement element
     * @return a new Traversable with the first occurrence of {@code currentElement} replaced by {@code newElement}
     */
    Traversable<T> replace(T currentElement, T newElement);

    /**
     * Replaces all occurrences of {@code currentElement} with {@code newElement}.
     *
     * @param currentElement the element to be replaced
     * @param newElement     the replacement element
     * @return a new Traversable with all occurrences of {@code currentElement} replaced by {@code newElement}
     */
    Traversable<T> replaceAll(T currentElement, T newElement);

    /**
     * Retains only the elements from this Traversable that are contained in the given {@code elements}.
     *
     * @param elements the elements to keep
     * @return a new Traversable containing only the elements present in {@code elements}, in their original order
     * @throws NullPointerException if {@code elements} is null
     */
    Traversable<T> retainAll(@NonNull Iterable<? extends T> elements);

    /**
     * Computes a prefix scan of the elements of this Traversable.
     * <p>
     * The neutral element {@code zero} may be applied more than once.
     *
     * @param zero      the neutral element for the operator
     * @param operation an associative binary operator
     * @return a new Traversable containing the prefix scan of the elements
     * @throws NullPointerException if {@code operation} is null
     */
    Traversable<T> scan(T zero, @NonNull BiFunction<? super T, ? super T, ? extends T> operation);

    /**
     * Produces a collection containing cumulative results of applying the operator from left to right.
     * <p>
     * Will not terminate for infinite collections. The results may vary across runs unless the collection is ordered.
     *
     * @param <U>       the type of the resulting elements
     * @param zero      the initial value
     * @param operation a binary operator applied to the intermediate result and each element
     * @return a new Traversable containing the cumulative results
     * @throws NullPointerException if {@code operation} is null
     */
    <U> Traversable<U> scanLeft(U zero, @NonNull BiFunction<? super U, ? super T, ? extends U> operation);

    /**
     * Produces a collection containing cumulative results of applying the operator from right to left.
     * <p>
     * The head of the resulting collection is the last cumulative result. Will not terminate for infinite collections.
     * Results may vary across runs unless the collection is ordered.
     *
     * @param <U>       the type of the resulting elements
     * @param zero      the initial value
     * @param operation a binary operator applied to each element and the intermediate result
     * @return a new Traversable containing the cumulative results
     * @throws NullPointerException if {@code operation} is null
     */
    <U> Traversable<U> scanRight(U zero, @NonNull BiFunction<? super T, ? super U, ? extends U> operation);

    /**
     * Returns the single element of this Traversable.
     *
     * @return the single element
     * @throws NoSuchElementException if the Traversable is empty or contains more than one element
     */
    default T single() {
        return singleOption().getOrElseThrow(() -> new NoSuchElementException("Does not contain a single value"));
    }

    /**
     * Returns the single element of this Traversable as an {@code Option}.
     *
     * @return {@code Some(element)} if the Traversable contains exactly one element,
     *         or {@code None} otherwise.
     */
    default Option<T> singleOption() {
        final Iterator<T> it = iterator();
        if (!it.hasNext()) {
            return Option.none();
        }
        final T first = it.next();
        return it.hasNext() ? Option.none() : Option.some(first);
    }

    /**
     * Returns the number of elements in this Traversable.
     * <p>
     * Alias for {@link #length()}.
     *
     * @return the number of elements
     */
    default int size() {
        return length();
    }

    /**
     * Partitions this {@code Traversable} into consecutive non-overlapping windows
     * according to a classification function.
     * <p>
     * Each window contains elements with the same class, as determined by {@code classifier}.
     * Two consecutive elements belong to the same window only if {@code classifier} returns equal values
     * for both. Otherwise, the current window ends and a new window begins with the next element.
     * <p>
     * Examples:
     * <pre>{@code
     * [].slideBy(Function.identity()) = []
     * [1,2,3,4,4,5].slideBy(Function.identity()) = [[1],[2],[3],[4,4],[5]]
     * [1,2,3,10,12,5,7,20,29].slideBy(x -> x / 10) = [[1,2,3],[10,12],[5,7],[20,29]]
     * }</pre>
     *
     * @param classifier A function classifying elements into groups
     * @return An {@code Iterator} of windows (grouped elements)
     * @throws NullPointerException if {@code classifier} is null
     */
    Iterator<? extends Traversable<T>> slideBy(@NonNull Function<? super T, ?> classifier);

    /**
     * Slides a window of a given {@code size} over this {@code Traversable} with a step size of 1.
     * <p>
     * This is equivalent to calling {@link #sliding(int, int)} with a step size of 1.
     *
     * @param size a positive window size
     * @return An {@code Iterator} of windows, each containing up to {@code size} elements
     * @throws IllegalArgumentException if {@code size} is zero or negative
     */
    Iterator<? extends Traversable<T>> sliding(int size);

    /**
     * Slides a window of a specific {@code size} with a given {@code step} over this {@code Traversable}.
     * <p>
     * Examples:
     * <pre>{@code
     * [].sliding(1, 1) = []
     * [1,2,3,4,5].sliding(2, 3) = [[1,2],[4,5]]
     * [1,2,3,4,5].sliding(2, 4) = [[1,2],[5]]
     * [1,2,3,4,5].sliding(2, 5) = [[1,2]]
     * [1,2,3,4].sliding(5, 3) = [[1,2,3,4],[4]]
     * }</pre>
     *
     * @param size a positive window size
     * @param step a positive step size
     * @return an {@code Iterator} of windows with the given size and step
     * @throws IllegalArgumentException if {@code size} or {@code step} are zero or negative
     */
    Iterator<? extends Traversable<T>> sliding(int size, int step);

    /**
     * Splits this {@code Traversable} into a prefix and remainder according to the given {@code predicate}.
     * <p>
     * The first element of the returned {@code Tuple} is the longest prefix of elements satisfying {@code predicate},
     * and the second element is the remaining elements.
     *
     * @param predicate a predicate used to determine the prefix
     * @return a {@code Tuple} containing the prefix and remainder
     * @throws NullPointerException if {@code predicate} is null
     */
    Tuple2<? extends Traversable<T>, ? extends Traversable<T>> span(@NonNull Predicate<? super T> predicate);

    @Override
    default Spliterator<T> spliterator() {
        int characteristics = Spliterator.IMMUTABLE;
        if (isDistinct()) {
            characteristics |= Spliterator.DISTINCT;
        }
        if (isOrdered()) {
            characteristics |= (Spliterator.SORTED | Spliterator.ORDERED);
        }
        if (isSequential()) {
            characteristics |= Spliterator.ORDERED;
        }
        if (hasDefiniteSize()) {
            characteristics |= (Spliterator.SIZED | Spliterator.SUBSIZED);
            return Spliterators.spliterator(iterator(), length(), characteristics);
        } else {
            return Spliterators.spliteratorUnknownSize(iterator(), characteristics);
        }
    }

    /**
     * Calculates the sum of the elements in this {@code Traversable}. Supported numeric types are
     * {@code Byte}, {@code Double}, {@code Float}, {@code Integer}, {@code Long}, {@code Short},
     * {@code BigInteger}, and {@code BigDecimal}.
     * <p>
     * Examples:
     * <pre>{@code
     * List.empty().sum()              // = 0
     * List.of(1, 2, 3).sum()          // = 6L
     * List.of(0.1, 0.2, 0.3).sum()    // = 0.6
     * List.of("apple", "pear").sum()  // throws
     * }</pre>
     * <p>
     * See also {@link #fold(Object, BiFunction)} for type-safe summation of elements.
     *
     * @return a {@code Number} representing the sum of the elements
     * @throws UnsupportedOperationException if elements are not numeric
     */
    @SuppressWarnings("unchecked")
    default Number sum() {
        if (isEmpty()) {
            return 0;
        } else {
            try {
                final Iterator<?> iter = iterator();
                final Object o = iter.next();
                if (o instanceof Integer || o instanceof Long || o instanceof Byte || o instanceof Short) {
                    return ((Iterator<Number>) iter)
                      .foldLeft(((Number) o).longValue(), (sum, number) -> sum + number.longValue());
                } else if (o instanceof BigInteger) {
                    return ((Iterator<BigInteger>) iter)
                      .foldLeft((BigInteger) o, BigInteger::add);
                } else if (o instanceof BigDecimal) {
                    return ((Iterator<BigDecimal>) iter)
                      .foldLeft((BigDecimal) o, BigDecimal::add);
                } else {
                    // fallback for other Number types using Neumaier summation
                    return TraversableModule.neumaierSum(Iterator.of(o).concat(iter), t -> ((Number) t).doubleValue())[0];
                }
            } catch (ClassCastException x) {
                throw new UnsupportedOperationException("Elements are not numeric", x);
            }
        }
    }

    /**
     * Returns a new {@code Traversable} without its first element.
     *
     * @return a new {@code Traversable} containing all elements except the first
     * @throws UnsupportedOperationException if this {@code Traversable} is empty
     */
    Traversable<T> tail();

    /**
     * Returns a new {@code Traversable} without its first element as an {@code Option}.
     *
     * @return {@code Some(traversable)} if non-empty, otherwise {@code None}
     */
    default Option<? extends Traversable<T>> tailOption() {
        return isEmpty() ? Option.none() : Option.some(tail());
    }

    /**
     * Returns the first {@code n} elements of this {@code Traversable}, or all elements if {@code n} exceeds the length.
     * <p>
     * Equivalent to {@code sublist(0, max(0, min(length(), n)))}, but safe for {@code n < 0} or {@code n > length()}.
     * <p>
     * If {@code n < 0}, an empty instance is returned. If {@code n > length()}, the full instance is returned.
     *
     * @param n the number of elements to take
     * @return a new {@code Traversable} containing the first {@code n} elements
     */
    Traversable<T> take(int n);

    /**
     * Returns the last {@code n} elements of this {@code Traversable}, or all elements if {@code n} exceeds the length.
     * <p>
     * Equivalent to {@code sublist(max(0, length() - n), length())}, but safe for {@code n < 0} or {@code n > length()}.
     * <p>
     * If {@code n < 0}, an empty instance is returned. If {@code n > length()}, the full instance is returned.
     *
     * @param n the number of elements to take from the end
     * @return a new {@code Traversable} containing the last {@code n} elements
     */
    Traversable<T> takeRight(int n);

    /**
     * Takes elements from this {@code Traversable} until the given predicate holds for an element.
     * <p>
     * Equivalent to {@code takeWhile(predicate.negate())}, but useful when using method references
     * that cannot be negated directly.
     *
     * @param predicate a condition tested sequentially on the elements
     * @return a new {@code Traversable} containing all elements before the first one that satisfies the predicate
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> takeUntil(@NonNull Predicate<? super T> predicate);

    /**
     * Takes elements from this {@code Traversable} while the given predicate holds.
     *
     * @param predicate a condition tested sequentially on the elements
     * @return a new {@code Traversable} containing all elements up to (but not including) the first one
     *         that does not satisfy the predicate
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> takeWhile(@NonNull Predicate<? super T> predicate);


    /**
     * Unzips the elements of this {@code Traversable} by mapping each element to a pair
     * and splitting them into two separate {@code Traversable} collections.
     *
     * @param unzipper a function that maps elements of this {@code Traversable} to pairs
     * @param <T1>     type of the first element in the resulting pairs
     * @param <T2>     type of the second element in the resulting pairs
     * @return a {@code Tuple2} containing two {@code Traversable} collections with the split elements
     * @throws NullPointerException if {@code unzipper} is null
     */
    <T1, T2> Tuple2<? extends Traversable<T1>, ? extends Traversable<T2>> unzip(
      @NonNull Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    /**
     * Unzips the elements of this {@code Traversable} by mapping each element to a triple
     * and splitting them into three separate {@code Traversable} collections.
     *
     * @param unzipper a function that maps elements of this {@code Traversable} to triples
     * @param <T1>     type of the first element in the resulting triples
     * @param <T2>     type of the second element in the resulting triples
     * @param <T3>     type of the third element in the resulting triples
     * @return a {@code Tuple3} containing three {@code Traversable} collections with the split elements
     * @throws NullPointerException if {@code unzipper} is null
     */
    <T1, T2, T3> Tuple3<? extends Traversable<T1>, ? extends Traversable<T2>, ? extends Traversable<T3>> unzip3(
      @NonNull Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper);

    /**
     * Returns a {@code Traversable} formed by pairing elements of this {@code Traversable} with elements of another
     * {@code Iterable}. Pairing stops when either collection runs out of elements; any remaining elements in the longer
     * collection are ignored.
     * <p>
     * The length of the resulting {@code Traversable} is the minimum of the lengths of this {@code Traversable} and
     * {@code that}.
     *
     * @param <U>  the type of elements in the second half of each pair
     * @param that an {@code Iterable} providing the second element of each pair
     * @return a new {@code Traversable} containing pairs of corresponding elements
     * @throws NullPointerException if {@code that} is null
     */
    <U> Traversable<Tuple2<T, U>> zip(@NonNull Iterable<? extends U> that);

    /**
     * Returns a {@code Traversable} formed by pairing elements of this {@code Traversable} with elements of another
     * {@code Iterable}, filling in placeholder elements when one collection is shorter than the other.
     * <p>
     * The length of the resulting {@code Traversable} is the maximum of the lengths of this {@code Traversable} and
     * {@code that}.
     * <p>
     * If this {@code Traversable} is shorter than {@code that}, {@code thisElem} is used as a filler. Conversely, if
     * {@code that} is shorter, {@code thatElem} is used.
     *
     * @param <U>      the type of elements in the second half of each pair
     * @param that     an {@code Iterable} providing the second element of each pair
     * @param thisElem the element used to fill missing values if this {@code Traversable} is shorter than {@code that}
     * @param thatElem the element used to fill missing values if {@code that} is shorter than this {@code Traversable}
     * @return a new {@code Traversable} containing pairs of elements, including fillers as needed
     * @throws NullPointerException if {@code that} is null
     */
    <U> Traversable<Tuple2<T, U>> zipAll(@NonNull Iterable<? extends U> that, T thisElem, U thatElem);

    /**
     * Returns a {@code Traversable} by combining elements of this {@code Traversable} with elements of another
     * {@code Iterable} using a mapping function. Pairing stops when either collection runs out of elements.
     * <p>
     * The length of the resulting {@code Traversable} is the minimum of the lengths of this {@code Traversable} and
     * {@code that}.
     *
     * @param <U>    the type of elements in the second parameter of the mapper
     * @param <R>    the type of elements in the resulting {@code Traversable}
     * @param that   an {@code Iterable} providing the second parameter of the mapper
     * @param mapper a function that combines elements from this and {@code that} into a new element
     * @return a new {@code Traversable} containing mapped elements
     * @throws NullPointerException if {@code that} or {@code mapper} is null
     */
    <U, R> Traversable<R> zipWith(@NonNull Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper);

    /**
     * Zips this {@code Traversable} with its indices, starting at 0.
     *
     * @return a new {@code Traversable} containing each element paired with its index
     */
    Traversable<Tuple2<T, Integer>> zipWithIndex();

    /**
     * Zips this {@code Traversable} with its indices and maps the resulting pairs using the provided mapper.
     *
     * @param <U>    the type of elements in the resulting {@code Traversable}
     * @param mapper a function mapping an element and its index to a new element
     * @return a new {@code Traversable} containing the mapped elements
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> Traversable<U> zipWithIndex(@NonNull BiFunction<? super T, ? super Integer, ? extends U> mapper);
}

interface TraversableModule {

    /**
     * Uses Neumaier's variant of the Kahan summation algorithm in order to sum double values.
     * <p>
     * See <a href="https://en.wikipedia.org/wiki/Kahan_summation_algorithm">Kahan summation algorithm</a>.
     *
     * @param <T> element type
     * @param ts the elements
     * @param toDouble function which maps elements to {@code double} values
     * @return A pair {@code [sum, size]}, where {@code sum} is the compensated sum and {@code size} is the number of elements which were summed.
     */
    static <T> double[] neumaierSum(@NonNull Iterable<T> ts, @NonNull ToDoubleFunction<T> toDouble) {
        double simpleSum = 0.0;
        double sum = 0.0;
        double compensation = 0.0;
        int size = 0;
        for (T t : ts) {
            final double d = toDouble.applyAsDouble(t);
            final double tmp = sum + d;
            compensation += (Math.abs(sum) >= Math.abs(d)) ? (sum - tmp) + d : (d - tmp) + sum;
            sum = tmp;
            simpleSum += d;
            size++;
        }
        sum += compensation;
        if (size > 0 && Double.isNaN(sum) && Double.isInfinite(simpleSum)) {
            sum = simpleSum;
        }
        return new double[] { sum, size };
    }
}
