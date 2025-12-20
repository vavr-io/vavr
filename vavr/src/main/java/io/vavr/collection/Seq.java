/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
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

import io.vavr.*;
import io.vavr.control.Option;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;
import org.jspecify.annotations.NonNull;

/**
 * Base interface for immutable, sequential collections.
 * Implementations preserve element order and return new instances on each
 * structural modification, ensuring that all operations are side-effect free.
 *
 * <p>Sequences built on this interface support a broad set of operations,
 * including element insertion and removal, indexed access, slicing, filtering,
 * sorting, zipping, and various combinatorial transformations such as
 * permutations, combinations, and cross-products. Most operations return a new
 * sequence while retaining the original ordering semantics.</p>
 *
 * <p>The interface also provides mechanisms for traversal, conversion to Java
 * collection views (both read-only and mutable), and treating the sequence as an
 * index-based partial function.</p>
 *
 * Views:
 *
 * <ul>
 * <li>{@link #asJava()}</li>
 * <li>{@link #asJava(Consumer)}</li>
 * <li>{@link #asJavaMutable()}</li>
 * <li>{@link #asJavaMutable(Consumer)}</li>
 * </ul>
 *
 * @param <T> the element type
 * @author Daniel Dietrich, Grzegorz Piwowarek
 */
public interface Seq<T> extends Traversable<T>, PartialFunction<Integer, T>, Serializable {

    /**
     * The serial version UID for serialization.
     */
    long serialVersionUID = 1L;

    /**
     * Narrows a {@code Seq<? extends T>} to {@code Seq<T>} via a safe unchecked cast.
     * This is valid because {@code Seq} is immutable and thus covariant in its element type.
     *
     * @param seq the sequence to narrow
     * @param <T> the element type of the resulting sequence
     * @return the given sequence viewed as {@code Seq<T>}
     */
    @SuppressWarnings("unchecked")
    static <T> Seq<T> narrow(Seq<? extends T> seq) {
        return (Seq<T>) seq;
    }


    /**
     * Returns a new sequence with the given element appended at the end.
     *
     * @param element the element to append
     * @return a new {@code Seq} containing all elements of this sequence
     *         followed by the given element
     */
    Seq<T> append(T element);

    /**
     * Returns a new sequence with all elements from the given {@code Iterable} appended
     * at the end of this sequence.
     *
     * @param elements the elements to append; must not be {@code null}
     * @return a new {@code Seq} containing all elements of this sequence followed by
     *         the given elements
     * @throws NullPointerException if {@code elements} is {@code null}
     */
    Seq<T> appendAll(@NonNull Iterable<? extends T> elements);


    /**
     * A {@code Seq} is a partial function which returns the element at the specified index by calling
     * {@linkplain #get(int)}.
     *
     * @param index an index
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     * @deprecated Will be removed
     */
    @Deprecated
    @Override
    default T apply(Integer index) {
        return get(index);
    }

    /**
     * Returns an <strong>immutable</strong> {@link java.util.List} view of this {@code Seq}.
     * Any attempt to modify the view (e.g., via mutator methods) will throw
     * {@link UnsupportedOperationException} at runtime.
     *
     * <p>This is a <em>view</em>, not a copy. Compared to conversion methods like {@code toJava*()}:
     * <ul>
     *     <li>Creating the view is O(1) (constant time), whereas conversions take O(n), with n = collection size.</li>
     *     <li>Operations on the view have the same performance characteristics as the underlying persistent Vavr collection,
     *         while converted collections behave like standard Java collections.</li>
     * </ul>
     *
     * <p>Note: the immutable Java list view throws {@code UnsupportedOperationException} before checking method arguments,
     * which may differ from standard Java behavior.</p>
     *
     * @return an immutable {@link java.util.List} view of this sequence
     */
    @GwtIncompatible
    java.util.List<T> asJava();


    /**
     * Creates an <strong>immutable</strong> {@link java.util.List} view of this {@code Seq}
     * and passes it to the given {@code action}.
     *
     * <p>The view is immutable: any attempt to modify it will throw
     * {@link UnsupportedOperationException} at runtime.</p>
     *
     * @param action a side-effecting operation that receives an immutable {@code java.util.List} view
     * @return this sequence
     * @see Seq#asJava()
     */
    @GwtIncompatible
    Seq<T> asJava(@NonNull Consumer<? super java.util.List<T>> action);

    /**
     * Returns a <strong>mutable</strong> {@link java.util.List} view of this {@code Seq}.
     * All standard mutator methods of the {@code List} interface are supported and
     * modify the underlying view.
     *
     * <p>Unlike {@link #asJava()}, this view allows modifications, but the performance
     * characteristics correspond to the underlying persistent Vavr collection.</p>
     *
     * @return a mutable {@link java.util.List} view of this sequence
     * @see Seq#asJava()
     */
    @GwtIncompatible
    java.util.List<T> asJavaMutable();

    /**
     * Creates a <strong>mutable</strong> {@link java.util.List} view of this {@code Seq}
     * and passes it to the given {@code action}.
     *
     * <p>The view supports all standard mutator methods. The result of the action
     * determines what is returned:</p>
     * <ul>
     *     <li>If only read operations are performed, this instance is returned.</li>
     *     <li>If any write operations are performed, a new {@code Seq} reflecting those changes is returned.</li>
     * </ul>
     *
     * @param action a side-effecting operation that receives a mutable {@code java.util.List} view
     * @return this sequence or a new sequence reflecting modifications made through the view
     * @see Seq#asJavaMutable()
     */
    @GwtIncompatible
    Seq<T> asJavaMutable(@NonNull Consumer<? super java.util.List<T>> action);

    /**
     * Returns a {@link PartialFunction} view of this {@code Seq}, where the function
     * is defined at an index if this sequence contains at least {@code index + 1} elements.
     * Applying the partial function to a defined index returns the element at that index.
     *
     * @return a {@link PartialFunction} mapping indices to elements
     * @throws IndexOutOfBoundsException if the sequence is empty, or if
     *         {@code index < 0} or {@code index >= length()}
     */
    PartialFunction<Integer, T> asPartialFunction() throws IndexOutOfBoundsException;

    @Override
    <R> Seq<R> collect(@NonNull PartialFunction<? super T, ? extends R> partialFunction);

    /**
     * Returns a sequence containing all combinations of elements from this sequence,
     * for all sizes from {@code 0} to {@code length()}.
     * <p>
     * Examples:
     * <pre>
     * {@code
     * [].combinations() = [[]]
     *
     * [1,2,3].combinations() = [
     *   [],                  // k = 0
     *   [1], [2], [3],       // k = 1
     *   [1,2], [1,3], [2,3], // k = 2
     *   [1,2,3]              // k = 3
     * ]
     * }
     * </pre>
     *
     * @return a sequence of sequences representing all combinations of this sequence's elements
     */
    Seq<? extends Seq<T>> combinations();

    /**
     * Returns all subsets of this sequence containing exactly {@code k} distinct elements,
     * i.e., the k-combinations of this sequence.
     *
     * @param k the size of each subset
     * @return a sequence of sequences representing all k-element combinations
     * @see <a href="http://en.wikipedia.org/wiki/Combination">Combination</a>
     */
    Seq<? extends Seq<T>> combinations(int k);

    /**
     * Checks whether this sequence contains the given sequence as a contiguous slice.
     *
     * <p><strong>Note:</strong> This method may not terminate for infinite sequences.</p>
     *
     * @param that the sequence to search for; must not be {@code null}
     * @return {@code true} if this sequence contains a slice equal to {@code that}, {@code false} otherwise
     * @throws NullPointerException if {@code that} is {@code null}
     */
    default boolean containsSlice(@NonNull Iterable<? extends T> that) {
        Objects.requireNonNull(that, "that is null");
        return indexOfSlice(that) >= 0;
    }

    /**
     * Computes the Cartesian product of this sequence with itself,
     * producing all pairs of elements ({@code this × this}).
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Result: [(1, 1), (1, 2), (1, 3), (2, 1), (2, 2), (2, 3), (3, 1), (3, 2), (3, 3)]
     * List.of(1, 2, 3).crossProduct();
     * }</pre>
     *
     * @return an {@link Iterator} over all pairs in the Cartesian square of this sequence
     */
    default Iterator<Tuple2<T, T>> crossProduct() {
        return crossProduct(this);
    }

    /**
     * Returns the n-ary Cartesian power (cross product) of this sequence.
     * Each element of the resulting iterator is a sequence of length {@code power},
     * containing all possible combinations of elements from this sequence.
     *
     * <p>Example for power = 2:</p>
     * <pre>{@code
     * // Result: [(A,A), (A,B), (A,C), ..., (B,A), (B,B), ..., (Z,Y), (Z,Z)]
     * CharSeq.rangeClosed('A', 'Z').crossProduct(2);
     * }</pre>
     *
     * <p>If {@code power} is negative, the result is an empty iterator:</p>
     * <pre>{@code
     * // Result: ()
     * CharSeq.rangeClosed('A', 'Z').crossProduct(-1);
     * }</pre>
     *
     * @param power the number of Cartesian multiplications
     * @return an {@link Iterator} over sequences representing the Cartesian power of this sequence
     */
    Iterator<? extends Seq<T>> crossProduct(int power);


    /**
     * Computes the Cartesian product of this sequence with another iterable,
     * producing pairs of elements ({@code this × that}).
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Result: [(1, 'a'), (1, 'b'), (2, 'a'), (2, 'b'), (3, 'a'), (3, 'b')]
     * List.of(1, 2, 3).crossProduct(List.of('a', 'b'));
     * }</pre>
     *
     * @param that another iterable; must not be {@code null}
     * @param <U> the element type of the other iterable
     * @return an {@link Iterator} over all pairs from this sequence and {@code that}
     * @throws NullPointerException if {@code that} is {@code null}
     */
    default <U> Iterator<Tuple2<T, U>> crossProduct(@NonNull Iterable<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        final Stream<U> other = Stream.ofAll(that);
        return Iterator.ofAll(this).flatMap(a -> other.map(b -> Tuple.of(a, b)));
    }

    /**
     * Checks whether this sequence ends with the given sequence.
     *
     * <p><strong>Note:</strong> If both this sequence and {@code that} are infinite,
     * this method may not terminate.</p>
     *
     * @param that the sequence to check as a suffix; must not be {@code null}
     * @return {@code true} if this sequence ends with {@code that}, {@code false} otherwise
     * @throws NullPointerException if {@code that} is {@code null}
     */
    default boolean endsWith(@NonNull Seq<? extends T> that) {
        Objects.requireNonNull(that, "that is null");
        final Iterator<T> i = this.iterator().drop(length() - that.length());
        final Iterator<? extends T> j = that.iterator();
        while (i.hasNext() && j.hasNext()) {
            if (!Objects.equals(i.next(), j.next())) {
                return false;
            }
        }
        return !j.hasNext();
    }

    /**
     * Returns the element at the specified index.
     *
     * @param index the position of the element to retrieve
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if the sequence is empty, or if
     *         {@code index < 0} or {@code index >= length()}
     */
    T get(int index);

    /**
     * Returns the index of the first occurrence of the given element,
     * or {@code -1} if this sequence does not contain the element.
     *
     * @param element the element to search for
     * @return the index of the first occurrence, or {@code -1} if not found
     */
    default int indexOf(T element) {
        return indexOf(element, 0);
    }

    /**
     * Returns the index of the first occurrence of the given element as an {@link Option}.
     *
     * @param element the element to search for
     * @return {@code Some(index)} if the element is found, or {@code None} if not found
     */
    default Option<Integer> indexOfOption(T element) {
        return Collections.indexOption(indexOf(element));
    }

    /**
     * Returns the index of the first occurrence of the given element, starting at
     * the specified index, or {@code -1} if this sequence does not contain the element.
     *
     * @param element the element to search for
     * @param from    the starting index for the search
     * @return the index of the first occurrence at or after {@code from}, or {@code -1} if not found
     */
    int indexOf(T element, int from);

    /**
     * Returns the index of the first occurrence of the given element at or after
     * the specified start index, as an {@link Option}.
     *
     * @param element the element to search for
     * @param from    the starting index for the search
     * @return {@code Some(index)} if the element is found, or {@code None} if not found
     */
    default Option<Integer> indexOfOption(T element, int from) {
        return Collections.indexOption(indexOf(element, from));
    }

    /**
     * Returns the first index at which this sequence contains the given sequence
     * as a contiguous slice, or {@code -1} if no such slice exists.
     *
     * <p><strong>Note:</strong> This method may not terminate for infinite sequences.</p>
     *
     * @param that the sequence to search for; must not be {@code null}
     * @return the starting index of the first matching slice, or {@code -1} if not found
     * @throws NullPointerException if {@code that} is {@code null}
     */
    default int indexOfSlice(@NonNull Iterable<? extends T> that) {
        return indexOfSlice(that, 0);
    }

    /**
     * Returns the first index at which this sequence contains the given sequence
     * as a contiguous slice, wrapped in an {@link Option}.
     *
     * <p><strong>Note:</strong> This method may not terminate for infinite sequences.</p>
     *
     * @param that the sequence to search for; must not be {@code null}
     * @return {@code Some(index)} if a matching slice is found, or {@code None} if not found
     */
    default Option<Integer> indexOfSliceOption(@NonNull Iterable<? extends T> that) {
        return Collections.indexOption(indexOfSlice(that));
    }

    /**
     * Returns the first index at or after the specified start index where this sequence
     * contains the given sequence as a contiguous slice, or {@code -1} if no such slice exists.
     *
     * <p><strong>Note:</strong> This method may not terminate for infinite sequences.</p>
     *
     * @param that the sequence to search for; must not be {@code null}
     * @param from the starting index for the search
     * @return the starting index of the first matching slice at or after {@code from}, or {@code -1} if not found
     * @throws NullPointerException if {@code that} is {@code null}
     */
    int indexOfSlice(@NonNull Iterable<? extends T> that, int from);

    /**
     * Returns the first index at or after the specified start index where this sequence
     * contains the given sequence as a contiguous slice, wrapped in an {@link Option}.
     *
     * <p><strong>Note:</strong> This method may not terminate for infinite sequences.</p>
     *
     * @param that the sequence to search for; must not be {@code null}
     * @param from the starting index for the search
     * @return {@code Some(index)} if a matching slice is found, or {@code None} if not found
     */
    default Option<Integer> indexOfSliceOption(@NonNull Iterable<? extends T> that, int from) {
        return Collections.indexOption(indexOfSlice(that, from));
    }

    /**
     * Returns the index of the first element in this sequence that satisfies the given predicate,
     * or {@code -1} if no such element exists.
     *
     * @param predicate the predicate used to test elements; must not be {@code null}
     * @return the index of the first matching element, or {@code -1} if none exists
     */
    default int indexWhere(@NonNull Predicate<? super T> predicate) {
        return indexWhere(predicate, 0);
    }

    /**
     * Returns the index of the first element satisfying the given predicate as an {@link Option}.
     *
     * @param predicate the predicate used to test elements; must not be {@code null}
     * @return {@code Some(index)} if a matching element exists, or {@code None} if not found
     */
    default Option<Integer> indexWhereOption(@NonNull Predicate<? super T> predicate) {
        return Collections.indexOption(indexWhere(predicate));
    }

    /**
     * Returns the index of the first element at or after the specified start index
     * that satisfies the given predicate, or {@code -1} if no such element exists.
     *
     * @param predicate the predicate used to test elements; must not be {@code null}
     * @param from      the starting index for the search
     * @return the index {@code >= from} of the first matching element, or {@code -1} if none exists
     */
    int indexWhere(@NonNull Predicate<? super T> predicate, int from);

    /**
     * Returns the index of the first element at or after the specified start index
     * that satisfies the given predicate as an {@link Option}.
     *
     * @param predicate the predicate used to test elements; must not be {@code null}
     * @param from      the starting index for the search
     * @return {@code Some(index)} if a matching element exists, or {@code None} if not found
     */
    default Option<Integer> indexWhereOption(@NonNull Predicate<? super T> predicate, int from) {
        return Collections.indexOption(indexWhere(predicate, from));
    }

    /**
     * Returns a new sequence with the given element inserted at the specified index.
     *
     * @param index   the position at which to insert the element
     * @param element the element to insert
     * @return a new {@code Seq} with the element inserted
     * @throws IndexOutOfBoundsException if the sequence is empty, or if
     *         {@code index < 0} or {@code index >= length()}
     */
    Seq<T> insert(int index, T element);

    /**
     * Returns a new sequence with the given elements inserted at the specified index.
     *
     * @param index    the position at which to insert the elements
     * @param elements the elements to insert; must not be {@code null}
     * @return a new {@code Seq} with the elements inserted
     * @throws IndexOutOfBoundsException if the sequence is empty, or if
     *         {@code index < 0} or {@code index >= length()}
     */
    Seq<T> insertAll(int index, @NonNull Iterable<? extends T> elements);

    /**
     * Returns a new sequence where the given element is inserted between all elements
     * of this sequence.
     *
     * @param element the element to intersperse
     * @return a new {@code Seq} with the element interspersed
     */
    Seq<T> intersperse(T element);

    /**
     * Returns an iterator over the elements of this sequence starting at the specified index.
     * Equivalent to {@code this.subSequence(index).iterator()}.
     *
     * @param index the starting index
     * @return an {@link Iterator} beginning at the given index, or empty if {@code index == length()}
     * @throws IndexOutOfBoundsException if {@code index < 0} or {@code index > length()}
     */
    default Iterator<T> iterator(int index) {
        return subSequence(index).iterator();
    }

    /**
     * Returns the index of the last occurrence of the given element,
     * or {@code -1} if this sequence does not contain the element.
     *
     * @param element the element to search for
     * @return the index of the last occurrence, or {@code -1} if not found
     */
    default int lastIndexOf(T element) {
        return lastIndexOf(element, Integer.MAX_VALUE);
    }

    /**
     * Returns the index of the last occurrence of the given element as an {@link Option}.
     *
     * @param element the element to search for
     * @return {@code Some(index)} if found, or {@code None} if not found
     */
    default Option<Integer> lastIndexOfOption(T element) {
        return Collections.indexOption(lastIndexOf(element));
    }

    /**
     * Returns the index of the last element in this sequence that satisfies the given predicate,
     * or {@code -1} if no such element exists.
     *
     * @param predicate the predicate used to test elements; must not be {@code null}
     * @return the index of the last matching element, or {@code -1} if none exists
     */
    default int lastIndexWhere(@NonNull Predicate<? super T> predicate) {
        return lastIndexWhere(predicate, length() - 1);
    }

    /**
     * Returns the index of the last element satisfying the given predicate as an {@link Option}.
     *
     * @param predicate the predicate used to test elements; must not be {@code null}
     * @return {@code Some(index)} if a matching element exists, or {@code None} if not found
     */
    default Option<Integer> lastIndexWhereOption(@NonNull Predicate<? super T> predicate) {
        return Collections.indexOption(lastIndexWhere(predicate));
    }

    /**
     * Returns the index of the last element at or before the specified end index
     * that satisfies the given predicate, or {@code -1} if no such element exists.
     *
     * @param predicate the predicate used to test elements; must not be {@code null}
     * @param end       the maximum index to consider
     * @return the index {@code <= end} of the last matching element, or {@code -1} if none exists
     */
    int lastIndexWhere(@NonNull Predicate<? super T> predicate, int end);

    /**
     * Returns the index of the last element at or before the specified end index
     * that satisfies the given predicate, wrapped in an {@link Option}.
     *
     * @param predicate the predicate used to test elements; must not be {@code null}
     * @param end       the maximum index to consider
     * @return {@code Some(index)} if a matching element exists, or {@code None} if not found
     */
    default Option<Integer> lastIndexWhereOption(@NonNull Predicate<? super T> predicate, int end) {
        return Collections.indexOption(lastIndexWhere(predicate, end));
    }

    /**
     * Turns this sequence into a plain function returning an Option result.
     *
     * @return a function that takes an index i and returns the value of
     * this sequence in a Some if the index is within bounds, otherwise a None.
     * @deprecated Will be removed
     */
    @Deprecated
    default Function1<Integer, Option<T>> lift() {
        return i -> (i >= 0 && i < length()) ? Option.some(apply(i)) : Option.none();
    }

    /**
     * Returns the index of the last occurrence of the given element at or before
     * the specified end index, or {@code -1} if this sequence does not contain the element.
     *
     * @param element the element to search for
     * @param end     the maximum index to consider
     * @return the index of the last occurrence at or before {@code end}, or {@code -1} if not found
     */
    int lastIndexOf(T element, int end);

    /**
     * Returns the index of the last occurrence of the given element at or before
     * the specified end index as an {@link Option}.
     *
     * @param element the element to search for
     * @param end     the maximum index to consider
     * @return {@code Some(index)} if found, or {@code None} if not found
     */
    default Option<Integer> lastIndexOfOption(T element, int end) {
        return Collections.indexOption(lastIndexOf(element, end));
    }

    /**
     * Returns the last index where this sequence contains the given sequence as a contiguous slice,
     * or {@code -1} if no such slice exists.
     *
     * <p><strong>Note:</strong> This method will not terminate for infinite sequences.</p>
     *
     * @param that the sequence to search for; must not be {@code null}
     * @return the starting index of the last matching slice, or {@code -1} if not found
     * @throws NullPointerException if {@code that} is {@code null}
     */
    default int lastIndexOfSlice(@NonNull Iterable<? extends T> that) {
        return lastIndexOfSlice(that, Integer.MAX_VALUE);
    }

    /**
     * Returns the last index where this sequence contains the given sequence as a contiguous slice,
     * wrapped in an {@link Option}.
     *
     * @param that the sequence to search for; must not be {@code null}
     * @return {@code Some(index)} if a matching slice exists, or {@code None} if not found
     */
    default Option<Integer> lastIndexOfSliceOption(@NonNull Iterable<? extends T> that) {
        return Collections.indexOption(lastIndexOfSlice(that));
    }

    /**
     * Returns the last index at or before the specified end index where this sequence
     * contains the given sequence as a contiguous slice, or {@code -1} if no such slice exists.
     *
     * @param that the sequence to search for; must not be {@code null}
     * @param end  the maximum index to consider
     * @return the last index {@code <= end} where the slice starts, or {@code -1} if not found
     * @throws NullPointerException if {@code that} is {@code null}
     */
    int lastIndexOfSlice(@NonNull Iterable<? extends T> that, int end);

    /**
     * Returns the last index at or before the specified end index where this sequence
     * contains the given sequence as a contiguous slice, wrapped in an {@link Option}.
     *
     * @param that the sequence to search for; must not be {@code null}
     * @param end  the maximum index to consider
     * @return {@code Some(index)} if a matching slice exists, or {@code None} if not found
     */
    default Option<Integer> lastIndexOfSliceOption(@NonNull Iterable<? extends T> that, int end) {
        return Collections.indexOption(lastIndexOfSlice(that, end));
    }

    /**
     * Returns a new sequence with this sequence padded on the right with the given element
     * until the specified target length is reached.
     *
     * <p><strong>Note:</strong> Lazily-evaluated sequences may need to process all elements
     * to determine the overall length.</p>
     *
     * @param length  the target length of the resulting sequence
     * @param element the element to append as padding
     * @return a new {@code Seq} consisting of this sequence followed by the minimal number
     *         of occurrences of {@code element} to reach at least {@code length}
     */
    Seq<T> padTo(int length, T element);

    /**
     * Returns a new sequence with this sequence padded on the left with the given element
     * until the specified target length is reached.
     *
     * <p><strong>Note:</strong> Lazily-evaluated sequences may need to process all elements
     * to determine the overall length.</p>
     *
     * @param length  the target length of the resulting sequence
     * @param element the element to prepend as padding
     * @return a new {@code Seq} consisting of this sequence prepended by the minimal number
     *         of occurrences of {@code element} to reach at least {@code length}
     */
    Seq<T> leftPadTo(int length, T element);

    /**
     * Returns a new sequence in which a slice of elements in this sequence is replaced
     * by the elements of another sequence.
     *
     * @param from     the starting index of the slice to be replaced
     * @param that     the sequence of elements to insert; must not be {@code null}
     * @param replaced the number of elements to remove from this sequence starting at {@code from}
     * @return a new {@code Seq} with the specified slice replaced
     */
    Seq<T> patch(int from, @NonNull Iterable<? extends T> that, int replaced);

    /**
     * Returns all unique permutations of this sequence.
     *
     * <p>Example:</p>
     * <pre>{@code
     * [].permutations() = []
     *
     * [1, 2, 3].permutations() = [
     *   [1, 2, 3],
     *   [1, 3, 2],
     *   [2, 1, 3],
     *   [2, 3, 1],
     *   [3, 1, 2],
     *   [3, 2, 1]
     * ]
     * }</pre>
     *
     * @return a sequence of all unique permutations of this sequence
     */
    Seq<? extends Seq<T>> permutations();


    /**
     * Returns the length of the longest prefix of this sequence whose elements
     * all satisfy the given predicate.
     *
     * <p><strong>Note:</strong> This method may not terminate for infinite sequences.</p>
     *
     * @param predicate the predicate to test elements; must not be {@code null}
     * @return the length of the longest prefix in which every element satisfies {@code predicate}
     */
    default int prefixLength(@NonNull Predicate<? super T> predicate) {
        return segmentLength(predicate, 0);
    }

    /**
     * Returns a new sequence with the given element prepended to this sequence.
     *
     * @param element the element to prepend
     * @return a new {@code Seq} with the element added at the front
     */
    Seq<T> prepend(T element);

    /**
     * Returns a new sequence with all given elements prepended to this sequence.
     *
     * @param elements the elements to prepend; must not be {@code null}
     * @return a new {@code Seq} with the elements added at the front
     */
    Seq<T> prependAll(@NonNull Iterable<? extends T> elements);

    /**
     * Returns a new sequence with the first occurrence of the given element removed.
     *
     * @param element the element to remove
     * @return a new {@code Seq} without the first occurrence of the element
     */
    Seq<T> remove(T element);

    /**
     * Returns a new sequence with all occurrences of the given element removed.
     *
     * @param element the element to remove
     * @return a new {@code Seq} without any occurrences of the element
     */
    Seq<T> removeAll(T element);

    /**
     * Returns a new sequence with all occurrences of the given elements removed.
     *
     * @param elements the elements to remove; must not be {@code null}
     * @return a new {@code Seq} without any of the given elements
     * @throws NullPointerException if {@code elements} is {@code null}
     */
    Seq<T> removeAll(@NonNull Iterable<? extends T> elements);

    /**
     * Returns a new Seq consisting of all elements which do not satisfy the given predicate.
     *
     * @deprecated Please use {@link #reject(Predicate)}
     * @param predicate the predicate used to test elements
     * @return a new Seq
     * @throws NullPointerException if {@code predicate} is null
     */
    @Deprecated
    Seq<T> removeAll(@NonNull Predicate<? super T> predicate);

    /**
     * Returns a new sequence with the element at the specified position removed.
     * Subsequent elements are shifted to the left (indices decreased by one).
     *
     * @param index the position of the element to remove
     * @return a new {@code Seq} without the element at the specified index
     * @throws IndexOutOfBoundsException if the sequence is empty, or if {@code index < 0} or {@code index >= length()}
     */
    Seq<T> removeAt(int index);

    /**
     * Returns a new sequence with the first element that satisfies the given predicate removed.
     *
     * @param predicate the predicate used to identify the element to remove; must not be {@code null}
     * @return a new {@code Seq} without the first matching element
     */
    Seq<T> removeFirst(@NonNull Predicate<T> predicate);

    /**
     * Returns a new sequence with the last element that satisfies the given predicate removed.
     *
     * @param predicate the predicate used to identify the element to remove; must not be {@code null}
     * @return a new {@code Seq} without the last matching element
     */
    Seq<T> removeLast(@NonNull Predicate<T> predicate);

    /**
     * Returns a new sequence with the order of elements reversed.
     *
     * @return a new {@code Seq} with elements in reversed order
     */
    Seq<T> reverse();

    /**
     * Returns an iterator that yields elements of this sequence in reversed order.
     *
     * <p><strong>Note:</strong> {@code xs.reverseIterator()} is equivalent to
     * {@code xs.reverse().iterator()} but may be more efficient.</p>
     *
     * @return an {@link Iterator} over the elements in reversed order
     */
    Iterator<T> reverseIterator();

    /**
     * Returns a new sequence with the elements circularly rotated to the left by the specified distance.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Result: List(3, 4, 5, 1, 2)
     * List.of(1, 2, 3, 4, 5).rotateLeft(2);
     * }</pre>
     *
     * @param n the number of positions to rotate left
     * @return a new {@code Seq} with elements rotated left
     */
    Seq<T> rotateLeft(int n);

    /**
     * Returns a new sequence with the elements circularly rotated to the right by the specified distance.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // Result: List(4, 5, 1, 2, 3)
     * List.of(1, 2, 3, 4, 5).rotateRight(2);
     * }</pre>
     *
     * @param n the number of positions to rotate right
     * @return a new {@code Seq} with elements rotated right
     */
    Seq<T> rotateRight(int n);

    /**
     * Returns the length of the longest contiguous segment, starting from the specified index,
     * in which all elements satisfy the given predicate.
     *
     * <p><strong>Note:</strong> This method may not terminate for infinite sequences.</p>
     *
     * @param predicate the predicate used to test elements; must not be {@code null}
     * @param from      the index at which to start the search
     * @return the length of the longest segment starting at {@code from} where every element satisfies {@code predicate}
     */
    int segmentLength(@NonNull Predicate<? super T> predicate, int from);

    /**
     * Returns a new sequence with the elements randomly shuffled.
     *
     * @return a new {@code Seq} containing the same elements in a random order
     */
    Seq<T> shuffle();

    /**
     * Returns a subsequence (slice) of this sequence, starting at {@code beginIndex} (inclusive)
     * and ending at {@code endIndex} (exclusive).
     *
     * <p>Examples:</p>
     * <pre>{@code
     * List.of(1, 2, 3, 4).slice(1, 3); // = (2, 3)
     * List.of(1, 2, 3, 4).slice(0, 4); // = (1, 2, 3, 4)
     * List.of(1, 2, 3, 4).slice(2, 2); // = ()
     * List.of(1, 2).slice(1, 0);       // = ()
     * List.of(1, 2).slice(-10, 10);    // = (1, 2)
     * }</pre>
     *
     * <p>See also {@link #subSequence(int, int)}, which may throw an exception instead of returning a sequence in some cases.</p>
     *
     * @param beginIndex the starting index (inclusive)
     * @param endIndex   the ending index (exclusive)
     * @return a new {@code Seq} representing the specified slice
     */
    Seq<T> slice(int beginIndex, int endIndex);

    /**
     * Returns a new sequence with elements sorted according to their natural order.
     *
     * @return a new {@code Seq} with elements in natural order
     * @throws ClassCastException if elements are not {@link Comparable}
     */
    Seq<T> sorted();

    /**
     * Returns a new sequence with elements sorted according to the given {@code Comparator}.
     *
     * @param comparator the comparator used to order elements; must not be {@code null}
     * @return a new {@code Seq} with elements sorted according to the comparator
     */
    Seq<T> sorted(@NonNull Comparator<? super T> comparator);

    /**
     * Returns a new sequence sorted by comparing elements in a different domain defined by the given {@code mapper}.
     *
     * @param mapper a function mapping elements to a {@link Comparable} domain; must not be {@code null}
     * @param <U>    the type used for comparison
     * @return a new {@code Seq} sorted according to the mapped values
     * @throws NullPointerException if {@code mapper} is {@code null}
     */
    <U extends Comparable<? super U>> Seq<T> sortBy(@NonNull Function<? super T, ? extends U> mapper);
    /**
     * Returns a new sequence sorted by comparing elements in a different domain defined by the given {@code mapper},
     * using the provided {@code comparator}.
     *
     * @param comparator the comparator used to compare mapped values; must not be {@code null}
     * @param mapper     a function mapping elements to the domain for comparison; must not be {@code null}
     * @param <U>        the type used for comparison
     * @return a new {@code Seq} sorted according to the mapped values and comparator
     * @throws NullPointerException if {@code comparator} or {@code mapper} is {@code null}
     */
    <U> Seq<T> sortBy(@NonNull Comparator<? super U> comparator, Function<? super T, ? extends U> mapper);

    /**
     * Splits this sequence at the specified index.
     *
     * <p>The result of {@code splitAt(n)} is equivalent to {@code Tuple.of(take(n), drop(n))}.</p>
     *
     * @param n the index at which to split
     * @return a {@link Tuple2} containing the first {@code n} elements and the remaining elements
     */
    Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAt(int n);

    /**
     * Splits this sequence at the first element satisfying the given predicate.
     *
     * @param predicate the predicate used to determine the split point; must not be {@code null}
     * @return a {@link Tuple2} containing the sequence before the first matching element and the remaining sequence
     */
    Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAt(@NonNull Predicate<? super T> predicate);

    /**
     * Splits this sequence at the first element satisfying the given predicate, including the element in the first part.
     *
     * @param predicate the predicate used to determine the split point; must not be {@code null}
     * @return a {@link Tuple2} containing the sequence up to and including the first matching element and the remaining sequence
     */
    Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAtInclusive(@NonNull Predicate<? super T> predicate);

    /**
     * Tests whether this sequence starts with the given sequence.
     *
     * @param that the sequence to test; must not be {@code null}
     * @return {@code true} if {@code that} is empty or is a prefix of this sequence, {@code false} otherwise
     */
    default boolean startsWith(@NonNull Iterable<? extends T> that) {
        return startsWith(that, 0);
    }

    /**
     * Tests whether this sequence contains the given sequence starting at the specified index.
     *
     * <p><strong>Note:</strong> If both this sequence and the argument sequence are infinite, this method may not terminate.</p>
     *
     * @param that   the sequence to test; must not be {@code null}
     * @param offset the index at which to start checking for the prefix
     * @return {@code true} if {@code that} is empty or matches a subsequence of this sequence starting at {@code offset}, {@code false} otherwise
     */
    default boolean startsWith(@NonNull Iterable<? extends T> that, int offset) {
        Objects.requireNonNull(that, "that is null");
        if (offset < 0) {
            return false;
        }
        final Iterator<T> i = this.iterator().drop(offset);
        final java.util.Iterator<? extends T> j = that.iterator();
        while (i.hasNext() && j.hasNext()) {
            if (!Objects.equals(i.next(), j.next())) {
                return false;
            }
        }
        return !j.hasNext();
    }


    /**
     * Returns a {@code Seq} that is a subsequence of this sequence, starting from the specified {@code beginIndex}
     * and extending to the end of this sequence.
     *
     * <p>Examples:</p>
     * <pre>{@code
     * List.of(1, 2).subSequence(0);   // = (1, 2)
     * List.of(1, 2).subSequence(1);   // = (2)
     * List.of(1, 2).subSequence(2);   // = ()
     * List.of(1, 2).subSequence(10);  // throws IndexOutOfBoundsException
     * List.of(1, 2).subSequence(-10); // throws IndexOutOfBoundsException
     * }</pre>
     *
     * <p>See also {@link #drop(int)}, which provides similar functionality but does not throw an exception
     * for out-of-bounds indices.</p>
     *
     * @param beginIndex the starting index (inclusive) of the subsequence
     * @return a new {@code Seq} representing the subsequence from {@code beginIndex} to the end
     * @throws IndexOutOfBoundsException if {@code beginIndex} is negative or greater than the length of this sequence
     */
    Seq<T> subSequence(int beginIndex);

    /**
     * Returns a {@code Seq} that is a subsequence of this sequence, starting from the specified {@code beginIndex}
     * (inclusive) and ending at {@code endIndex} (exclusive).
     *
     * <p>Examples:</p>
     * <pre>{@code
     * List.of(1, 2, 3, 4).subSequence(1, 3); // = (2, 3)
     * List.of(1, 2, 3, 4).subSequence(0, 4); // = (1, 2, 3, 4)
     * List.of(1, 2, 3, 4).subSequence(2, 2); // = ()
     * List.of(1, 2).subSequence(1, 0);       // throws IndexOutOfBoundsException
     * List.of(1, 2).subSequence(-10, 1);     // throws IndexOutOfBoundsException
     * List.of(1, 2).subSequence(0, 10);      // throws IndexOutOfBoundsException
     * }</pre>
     *
     * <p>See also {@link #slice(int, int)}, which returns an empty sequence instead of throwing exceptions
     * when indices are out of range.</p>
     *
     * @param beginIndex the starting index (inclusive) of the subsequence
     * @param endIndex   the ending index (exclusive) of the subsequence
     * @return a new {@code Seq} representing the subsequence from {@code beginIndex} to {@code endIndex - 1}
     * @throws IndexOutOfBoundsException if {@code beginIndex} or {@code endIndex} is negative, or if
     *                                   {@code endIndex} is greater than {@code length()}
     * @throws IllegalArgumentException  if {@code beginIndex} is greater than {@code endIndex}
     */
    Seq<T> subSequence(int beginIndex, int endIndex);


    /**
     * Returns a new {@code Seq} with the element at the specified index replaced by the given value.
     *
     * @param index   the index of the element to update
     * @param element the new element to set at the specified index
     * @return a new {@code Seq} with the updated element
     * @throws IndexOutOfBoundsException if {@code index} is negative or greater than or equal to {@code length()}
     */
    Seq<T> update(int index, T element);

    /**
     * Returns a new {@code Seq} with the element at the specified index updated using the given function.
     *
     * @param index   the index of the element to update
     * @param updater a function that computes the new element from the existing element
     * @return a new {@code Seq} with the element at {@code index} transformed by {@code updater}
     * @throws IndexOutOfBoundsException if {@code index} is negative or greater than or equal to {@code length()}
     * @throws NullPointerException      if {@code updater} is null
     */
    Seq<T> update(int index, @NonNull Function<? super T, ? extends T> updater);

    /**
     * Searches for a specified element in this sequence, which must be sorted in ascending natural order.
     * <p>
     * If the sequence is an {@code IndexedSeq}, a binary search is used; otherwise, a linear search is performed.
     *
     * @param element the element to search for
     * @return the index of the element if found; otherwise, {@code -(insertion point) - 1}, 
     *         where the insertion point is the index at which the element would be inserted.
     *         A non-negative return value indicates the element is present.
     * @throws ClassCastException if the element cannot be compared using natural ordering
     */
    int search(T element);

    /**
     * Searches for a specified element in this sequence, which must be sorted according to the given comparator.
     * <p>
     * If the sequence is an {@code IndexedSeq}, a binary search is used; otherwise, a linear search is performed.
     *
     * @param element    the element to search for
     * @param comparator the comparator defining the order of the sequence
     * @return the index of the element if found; otherwise, {@code -(insertion point) - 1}, 
     *         where the insertion point is the index at which the element would be inserted.
     *         A non-negative return value indicates the element is present.
     * @throws NullPointerException if {@code comparator} is null
     */
    int search(T element, @NonNull Comparator<? super T> comparator);


    // -- Adjusted return types of Traversable methods

    @Override
    Seq<T> distinct();

    @Override
    Seq<T> distinctBy(@NonNull Comparator<? super T> comparator);

    @Override
    <U> Seq<T> distinctBy(@NonNull Function<? super T, ? extends U> keyExtractor);

    /**
     * Returns a sequence with duplicate elements removed, as determined by the provided comparator.
     * When duplicates are found, the **last occurrence** of each element is retained.
     *
     * @param comparator a comparator defining equality between elements
     * @return a new sequence with duplicates removed, keeping the last occurrence of each element
     */
    Seq<T> distinctByKeepLast(@NonNull Comparator<? super T> comparator);

    /**
     * Returns a sequence with duplicates removed based on a key extracted from each element.
     * The key is obtained via the provided {@code keyExtractor} function. When duplicates are found,
     * the **last occurrence** of each element for a given key is retained.
     *
     * @param <U> the type of the key used for determining uniqueness
     * @param keyExtractor a function extracting a key from each element for uniqueness comparison
     * @return a new sequence of elements distinct by the extracted key, keeping the last occurrence
     */
    <U> Seq<T> distinctByKeepLast(@NonNull Function<? super T, ? extends U> keyExtractor);

    @Override
    Seq<T> drop(int n);

    @Override
    Seq<T> dropUntil(@NonNull Predicate<? super T> predicate);

    @Override
    Seq<T> dropWhile(@NonNull Predicate<? super T> predicate);

    @Override
    Seq<T> dropRight(int n);

    /**
     * Drops elements from the end of the sequence until an element satisfies the given predicate.
     * The returned sequence includes the last element that satisfies the predicate.
     *
     * @param predicate a condition to test elements, starting from the end
     * @return a new sequence containing all elements up to and including the last element
     *         that satisfies the predicate
     * @throws NullPointerException if {@code predicate} is null
     */
    Seq<T> dropRightUntil(@NonNull Predicate<? super T> predicate);

    /**
     * Drops elements from the end of the sequence while the given predicate holds.
     * <p>
     * This is equivalent to {@code dropRightUntil(predicate.negate())}. Useful when using
     * method references that cannot be negated directly.
     *
     * @param predicate a condition to test elements, starting from the end
     * @return a new sequence containing all elements up to and including the last element
     *         that does not satisfy the predicate
     * @throws NullPointerException if {@code predicate} is null
     */
    Seq<T> dropRightWhile(@NonNull Predicate<? super T> predicate);

    @Override
    Seq<T> filter(@NonNull Predicate<? super T> predicate);

    @Override
    Seq<T> reject(@NonNull Predicate<? super T> predicate);

    @Override
    <U> Seq<U> flatMap(@NonNull Function<? super T, ? extends Iterable<? extends U>> mapper);

    @Override
    default <U> U foldRight(U zero, @NonNull BiFunction<? super T, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return reverse().foldLeft(zero, (xs, x) -> f.apply(x, xs));
    }

    @Override
    <C> Map<C, ? extends Seq<T>> groupBy(@NonNull Function<? super T, ? extends C> classifier);

    @Override
    Iterator<? extends Seq<T>> grouped(int size);

    @Override
    Seq<T> init();

    @Override
    Option<? extends Seq<T>> initOption();

    @Override
    <U> Seq<U> map(@NonNull Function<? super T, ? extends U> mapper);

    @Override
    default <U> Seq<U> mapTo(U value) {
        return map(ignored -> value);
    }

    @Override
    default Seq<Void> mapToVoid() {
        return map(ignored -> null);
    }

    @Override
    Seq<T> orElse(Iterable<? extends T> other);

    @Override
    Seq<T> orElse(@NonNull Supplier<? extends Iterable<? extends T>> supplier);

    @Override
    Tuple2<? extends Seq<T>, ? extends Seq<T>> partition(@NonNull Predicate<? super T> predicate);

    @Override
    Seq<T> peek(@NonNull Consumer<? super T> action);

    @Override
    Seq<T> replace(T currentElement, T newElement);

    @Override
    Seq<T> replaceAll(T currentElement, T newElement);

    @Override
    Seq<T> retainAll(@NonNull Iterable<? extends T> elements);

    @Override
    Seq<T> scan(T zero, @NonNull BiFunction<? super T, ? super T, ? extends T> operation);

    @Override
    <U> Seq<U> scanLeft(U zero, @NonNull BiFunction<? super U, ? super T, ? extends U> operation);

    @Override
    <U> Seq<U> scanRight(U zero, @NonNull BiFunction<? super T, ? super U, ? extends U> operation);

    @Override
    Iterator<? extends Seq<T>> slideBy(@NonNull Function<? super T, ?> classifier);

    @Override
    Iterator<? extends Seq<T>> sliding(int size);

    @Override
    Iterator<? extends Seq<T>> sliding(int size, int step);

    @Override
    Tuple2<? extends Seq<T>, ? extends Seq<T>> span(@NonNull Predicate<? super T> predicate);

    @Override
    Seq<T> tail();

    @Override
    Option<? extends Seq<T>> tailOption();

    @Override
    Seq<T> take(int n);

    @Override
    Seq<T> takeUntil(@NonNull Predicate<? super T> predicate);

    @Override
    Seq<T> takeWhile(@NonNull Predicate<? super T> predicate);

    @Override
    Seq<T> takeRight(int n);

    /**
     * Takes elements from the end of the sequence until an element satisfies the given predicate.
     * The returned sequence starts after the last element that satisfies the predicate.
     *
     * @param predicate a condition to test elements, starting from the end
     * @return a new sequence containing all elements after the last element
     *         that satisfies the predicate
     * @throws NullPointerException if {@code predicate} is null
     */
    Seq<T> takeRightUntil(@NonNull Predicate<? super T> predicate);

    /**
     * Takes elements from the end of the sequence while the given predicate holds.
     * <p>
     * This is an equivalent to {@code takeRightUntil(predicate.negate())}. Useful when using
     * method references that cannot be negated directly.
     *
     * @param predicate a condition to test elements, starting from the end
     * @return a new sequence containing all elements after the last element
     *         that does not satisfy the predicate
     * @throws NullPointerException if {@code predicate} is null
     */
    Seq<T> takeRightWhile(@NonNull Predicate<? super T> predicate);

    @Override
    <T1, T2> Tuple2<? extends Seq<T1>, ? extends Seq<T2>> unzip(@NonNull Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <T1, T2, T3> Tuple3<? extends Seq<T1>, ? extends Seq<T2>, ? extends Seq<T3>> unzip3(@NonNull Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper);

    @Override
    <U> Seq<Tuple2<T, U>> zip(@NonNull Iterable<? extends U> that);

    @Override
    <U, R> Seq<R> zipWith(@NonNull Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper);

    @Override
    <U> Seq<Tuple2<T, U>> zipAll(@NonNull Iterable<? extends U> that, T thisElem, U thatElem);

    @Override
    Seq<Tuple2<T, Integer>> zipWithIndex();

    @Override
    <U> Seq<U> zipWithIndex(@NonNull BiFunction<? super T, ? super Integer, ? extends U> mapper);

    /**
     * Turns this sequence from a partial function into a total function that
     * returns defaultValue for all indexes that are out of bounds.
     *
     * @param defaultValue default value to return for out of bound indexes
     * @return a total function from index to T
     * @deprecated Will be removed
     */
    @Deprecated
    default Function1<Integer, T> withDefaultValue(T defaultValue) {
        return i -> (i >= 0 && i < length()) ? apply(i) : defaultValue;
    }

    /**
     * Turns this sequence from a partial function into a total function that
     * returns a value computed by defaultFunction for all indexes that are out of bounds.
     *
     * @param defaultFunction function to evaluate for all out-of-bounds indexes.
     * @return a total function from index to T
     * @deprecated Will be removed
     */
    @Deprecated
    default Function1<Integer, T> withDefault(@NonNull Function<? super Integer, ? extends T> defaultFunction) {
        return i -> (i >= 0 && i < length()) ? apply(i) : defaultFunction.apply(i);
    }

    @Override
    default boolean isSequential() {
        return true;
    }
}
