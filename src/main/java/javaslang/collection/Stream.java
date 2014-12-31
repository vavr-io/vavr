/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.Require.UnsatisfiedRequirementException;
import javaslang.Tuple.Tuple2;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.StreamSupport;

/**
 * An immutable Stream implementation, suitable for concurrent programming.
 * <p/>
 * A Stream is composed of a {@code head()} element and a {@code tail()} Stream.
 * <p/>
 * There are two implementations of the interface Stream:
 * <ul>
 * <li>{@link Nil}, which represents a Stream containing no elements.</li>
 * <li>{@link Cons}, which represents a Stream containing elements.</li>
 * </ul>
 * <p/>
 * Use {@code Stream.of(1, 2, 3)} instead of {@code new Cons(1, new Cons(2, new Cons(3, Nil.instance())))}.
 * <p/>
 * Use {@code Stream.nil()} instead of {@code Nil.instance()}.
 * <p/>
 * In contrast to the mutable Stream variant {@link java.util.ArrayList}, it does not make sense for immutable Streams to
 * implement the interface {@link java.lang.Cloneable} because of the following conclusion: <blockquote>
 * "[...] , it doesn’t make sense for immutable classes to support object copying, because copies would be virtually indistinguishable from the original."
 * </blockquote> <em>(see Effective Java, 2nd ed., p. 61)</em>.
 *
 * @param <E> Component type of the Stream.
 */
public interface Stream<E> extends Foldable<E, Stream<?>, Stream<E>>, Algebra.Monad<E, Stream<?>>, Algebra.Monoid<Stream<E>> {

    // -- Core Stream API

    /**
     * Returns the first element of this Stream in O(1).
     *
     * @return The head of this Stream.
     * @throws UnsupportedOperationException if this is Nil.
     */
    E head();

    /**
     * Returns all elements except the first element of this Stream in O(1).
     *
     * @return The tail of this Stream.
     * @throws UnsupportedOperationException if this is Nil.
     */
    Stream<E> tail();

    /**
     * Tests whether this Stream is empty in O(1).
     *
     * @return true, if this Stream is Nil, false otherwise.
     */
    @Override
    boolean isEmpty();

    /**
     * Appends an element to this Stream in O(2n).
     * <p/>
     * The result is equivalent to {@code reverse().prepend(element).reverse()}.
     *
     * @param element An element.
     * @return A new Stream containing the elements of this stream, appended the given element.
     */
    default Stream<E> append(E element) {
        return isEmpty() ? Stream.of(element) : new Cons<>(head(), () -> tail().append(element));
    }

    /**
     * Appends all elements of a given Stream to this Stream in O(2n). This implementation returns
     * {@code elements.prependAll(this)}.
     * <p/>
     * Example: {@code Stream.of(1, 2, 3).appendAll(Stream.of(4, 5, 6))} equals {@code Stream.of(1, 2, 3, 4, 5, 6)} .
     *
     * @param elements Elements to be appended.
     * @return A new Stream containing the given elements appended to this Stream.
     * @throws javaslang.Require.UnsatisfiedRequirementException if elements is null
     */
    @SuppressWarnings("unchecked")
    default Stream<E> appendAll(Iterable<? extends E> elements) {
        Require.nonNull(elements, "elements is null");
        return isEmpty() ? Stream.of(elements) : new Cons<>(head(), () -> tail().appendAll(elements));
    }

    /**
     * Convenience method, well known from java.util collections. It has no effect on the original Stream, it just returns
     * Nil.instance().
     *
     * @return Nil.instance()
     */
    default Stream<E> clear() {
        return Nil.instance();
    }

    /**
     * Checks, if this stream contains the given element.
     * @param element An element.
     * @return true, if this stream contains the given element, false otherwise.
     */
    default boolean contains(E element) {
        return indexOf(element) != -1;
    }

    /**
     * Returns the element of this Stream at the specified index in O(n).
     * <p/>
     * The result is roughly equivalent to {@code (index == 0) ? head() : tail().get(index - 1)} but implemented without
     * recursion.
     *
     * @param index An index, where 0 &lt;= index &lt; size()
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException if this Stream is empty, index &lt; 0 or index &gt;= size of this Stream.
     */
    default E get(int index) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("get(" + index + ") on empty stream");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("get(" + index + ")");
        }
        Stream<E> stream = this;
        for (int i = index - 1; i >= 0; i--) {
            stream = stream.tail();
            if (stream.isEmpty()) {
                throw new IndexOutOfBoundsException(String.format("get(%s) on stream of size %s", index, index - i));
            }
        }
        return stream.head();
    }

    /**
     * Returns the index of the given element in O(n). The result is -1, if the element is not contained.
     * <p/>
     * The result is equivalent to {@code head().equals(element) ? 0 : 1 + tail().indexOf(element)} but implemented
     * without recursion.
     *
     * @param element An Object of type E, may be null.
     * @return The index of element or -1.
     */
    default int indexOf(E element) {
        int index = 0;
        for (Stream<E> stream = this; !stream.isEmpty(); stream = stream.tail(), index++) {
            if (Objects.equals(stream.head(), element)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Inserts the given element at the specified index into this Stream in O(n).
     * <p/>
     * Examples:
     * <p/>
     * <pre>
     * <code>().insert(0, 1) = (1)
     * (4).insert(0, 1) = (1,4)
     * (4).insert(1, 1) = (4,1)
     * (1,2,3).insert(2, 4) = (1,2,4,3)</code>
     * </pre>
     *
     * @param index   The insertion index.
     * @param element An element to be inserted.
     * @return This Stream with the given element inserted at the given index.
     * @throws IndexOutOfBoundsException if the index &lt; 0 or index &gt; size()
     */
    default Stream<E> insert(int index, E element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        }
        if (index > 0 && isEmpty()) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e) on empty stream");
        }
        if (index == 0) {
            return new Cons<>(element, () -> this);
        } else {
            return new Cons<>(head(), () -> this.insert(index - 1, element));
        }
    }

    /**
     * Inserts all of the given elements at the specified index into this Stream in O(n).
     * <p/>
     * Examples:
     * <p/>
     * <pre>
     * <code>().insertAll(0, (1,2,3)) = (1,2,3)
     * (4).insertAll(0, (1,2,3)) = (1,2,3,4)
     * (4).insertAll(1, (1,2,3)) = (4,1,2,3)
     * (1,2,3).insertAll(2, (4,5)) = (1,2,4,5,3)</code>
     * </pre>
     * <p/>
     * The result is roughly (without bounds check) equivalent to
     * <p/>
     * <pre>
     * <code>if (isEmpty()) {
     *     return elements;
     * } else if (index == 0) {
     *     if (elements.isEmpty()) {
     *         return this;
     *     } else {
     *         return new Cons(elements.head(), insertAll(0, elements.tail()));
     *     }
     * } else {
     *     return new Cons(head(), tail().insertAll(index - 1, elements));
     * }</code>
     * </pre>
     *
     * @param index    The insertion index.
     * @param elements The elements to be inserted.
     * @return This Stream with the given elements inserted at the given index.
     * @throws IndexOutOfBoundsException if the index &lt; 0 or index &gt; size()
     */
    default Stream<E> insertAll(int index, Iterable<? extends E> elements) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insertAll(" + index + ", elements)");
        }
        if (index > 0 && isEmpty()) {
            throw new IndexOutOfBoundsException("insertAll(" + index + ", elements) on empty stream");
        }
        if (index == 0) {
            return Stream.of(elements).appendAll(this);
        } else {
            return new Cons<>(head(), () -> this.insertAll(index - 1, elements));
        }
    }

    /**
     * Shortcut for {@code substream(index).iterator()}.
     *
     * @param index The start index of the iterator.
     * @return An iterator, starting at the given index.
     */
    default Iterator<E> iterator(int index) {
        return substream(index).iterator();
    }

    /**
     * Returns the last index of the given element in O(n). The result is -1, if the element is not contained.
     * <p/>
     * The result is equivalent to {@code (reverse().indexOf(element) == -1) ? -1 : size() - reverse().indexOf(element)}
     * but implemented without recursion.
     *
     * @param element An Object of type E, may be null.
     * @return The index of element or -1.
     */
    default int lastIndexOf(E element) {
        int result = -1, index = 0;
        for (Stream<E> stream = this; !stream.isEmpty(); stream = stream.tail(), index++) {
            if (Objects.equals(stream.head(), element)) {
                result = index;
            }
        }
        return result;
    }

    /**
     * Prepends an element to this Stream in O(1).
     * <p/>
     * The result is equivalent to {@code new Cons<>(element, this)}.
     *
     * @param element An element.
     * @return A new Stream containing the elements of this stream, prepended the given element.
     */
    default Stream<E> prepend(E element) {
        return new Cons<>(element, () -> this);
    }

    /**
     * Prepends all elements of a given Stream to this Stream in O(2n).
     * <p/>
     * If this.isEmpty(), elements is returned. If elements.isEmpty(), this is returned. Otherwise elements are
     * prepended to this.
     * <p/>
     * Example: {@code Stream.of(4, 5, 6).prependAll(Stream.of(1, 2, 3))} equals {@code Stream.of(1, 2, 3, 4, 5, 6)}.
     * <p/>
     * The result is equivalent to
     * {@code elements.isEmpty() ? this : prependAll(elements.tail()).prepend(elements.head())} but implemented without
     * recursion.
     *
     * @param elements Elements to be prepended.
     * @return A new Stream containing the given elements prepended to this Stream.
     * @throws javaslang.Require.UnsatisfiedRequirementException if elements is null
     */
    default Stream<E> prependAll(Iterable<? extends E> elements) {
        Require.nonNull(elements, "elements is null");
        return Stream.of(elements).appendAll(this);
    }

    /**
     * Removes the first occurrence of the given element from this stream if it is present in O(n).
     * <p/>
     * Example: {@code Stream.of(1, 2, 3).remove(2)} equals {@code Stream.of(1, 3)}.
     * <p/>
     * The result is equivalent to
     * <p/>
     * <pre>
     * <code>if (isEmpty()) {
     *     return this;
     * } else if (head().equals(element)) {
     *     return tail();
     * } else {
     *     return new Cons(head(), tail().remove(element));
     * }</code>
     * </pre>
     * <p/>
     * but implemented without recursion.
     *
     * @param element An element to be removed from this Stream.
     * @return A new stream where the first occurrence of the element is removed or the same stream, if the given element is
     * not part of the stream.
     */
    default Stream<E> remove(E element) {
        if (isEmpty()) {
            return this;
        } else {
            final E head = head();
            return Objects.equals(head, element) ? tail() : new Cons<>(head, () -> tail().remove(element));
        }
    }

    /**
     * Removes all occurrences of the given elements from this Stream in O(n^2).
     * <p/>
     * Example: {@code Stream.of(1, 2, 3, 1, 2, 3).removeAll(Stream.of(1, 2))} is equal to {@code Stream.of(3, 3)}.
     * <p/>
     * The result is equivalent to
     * <p/>
     * <pre>
     * <code>if (isEmpty())
     *     return this;
     * } else if (elements.contains(head())) {
     *     return tail().removeAll(elements);
     * } else {
     *     return new Cons(head(), tail().removeAll(elements));
     * }</code>
     * </pre>
     *
     * @param elements Elements to be removed.
     * @return A Stream containing all of this elements except the given elements.
     */
    default Stream<E> removeAll(Iterable<? extends E> elements) {
        final Stream<E> distinct = Stream.of(elements).distinct();
        return filter(distinct::contains);
    }

    default Stream<E> removeAll(E removed) {
        return filter(e -> !Objects.equals(e, removed));
    }

    /**
     * Replaces the first occurrence (if exists) of the given currentElement with newElement in O(2n).
     * <p/>
     * Example: {@code Stream.of(1, 2, 3, 2).replace(2, 4)} equals {Stream.of(1,4,3,2)}.
     * <p/>
     * The result is equivalent to:
     * {@code isEmpty() ? this : Objects.equals(head(), currentElement) ? new Cons(newElement, tail()) : new Cons(head(), tail().replace(currentElement, newElement))}.
     *
     * @param currentElement The element to be replaced.
     * @param newElement     The replacement for currentElement.
     * @return A Stream of elements, where the first occurrence (if exists) of currentElement is replaced with newElement.
     */
    default Stream<E> replace(E currentElement, E newElement) {
        if (isEmpty()) {
            return this;
        } else {
            final E head = head();
            if (Objects.equals(head, currentElement)) {
                return new Cons<>(newElement, this::tail);
            } else {
                return new Cons<>(head, () -> tail().replace(currentElement, newElement));
            }
        }
    }

    /**
     * Replaces all occurrences (if any) of the given currentElement with newElement in O(2n).
     * <p/>
     * Example: {@code Stream.of(1, 2, 3, 2).replaceAll(2, 4)} equals {Stream.of(1,4,3,4)}.
     * <p/>
     * The result is equivalent to:
     * {@code isEmpty() ? this : new Cons(Objects.equals(head(), currentElement) ? newElement : head(), tail().replaceAll(currentElement, newElement))}.
     *
     * @param currentElement The element to be replaced.
     * @param newElement     The replacement for currentElement.
     * @return A Stream of elements, where all occurrences (if any) of currentElement are replaced with newElement.
     */

    default Stream<E> replaceAll(E currentElement, E newElement) {
        if (isEmpty()) {
            return this;
        } else {
            final E head = head();
            final E newHead = Objects.equals(head, currentElement) ? newElement : head;
            return new Cons<>(newHead, () -> tail().replace(currentElement, newElement));
        }
    }

    /**
     * Applies an {@link java.util.function.UnaryOperator} to all elements of this Stream and returns the result as new
     * Stream (of same order) in O(2n).
     * <p/>
     * Example: {@code Stream.of(1, 2, 3).replaceAll(i -> i + 1)} equals {Stream.of(2,3,4)}.
     * <p/>
     * The result is equivalent to:
     * {@code isEmpty() ? this : new Cons(operator.apply(head()), tail().replaceAll(operator))}.
     *
     * @param operator An unary operator.
     * @return A Stream of elements transformed by the given operator.
     */
    default Stream<E> replaceAll(UnaryOperator<E> operator) {
        if (isEmpty()) {
            return this;
        } else {
            return new Cons<>(operator.apply(head()), () -> tail().replaceAll(operator));
        }
    }

    /**
     * Keeps all occurrences of the given elements from this Stream in O(n^2).
     * <p/>
     * Example: {@code Stream.of(1, 2, 3, 1, 2, 3).retainAll(Stream.of(1, 2))} is equal to {@code Stream.of(1, 2, 1, 2)}.
     * <p/>
     * The result is equivalent to
     * <p/>
     * <pre>
     * <code>if (isEmpty())
     *     return this;
     * } else if (elements.contains(head())) {
     *     return new Cons(head(), tail().retainAll(elements));
     * } else {
     *     return tail().retainAll(elements);
     * }</code>
     * </pre>
     *
     * @param elements Elements to be retained.
     * @return A Stream containing all of this elements which are also in the given elements.
     */
    default Stream<E> retainAll(Iterable<? extends E> elements) {
        final Stream<E> keeped = Stream.of(elements).distinct();
        return filter(keeped::contains);
    }

    /**
     * Replaces the element at the specified index in O(n).
     * <p/>
     * The result is roughly equivalent to
     * {@code (index == 0) ? tail().prepend(element) : new Cons(head(), tail().set(index - 1, element))} but implemented
     * without recursion.
     *
     * @param index   An index, where 0 &lt;= index &lt; size()
     * @param element A new element.
     * @return A stream containing all of the elements of this Stream but the given element at the given index.
     * @throws IndexOutOfBoundsException if this Stream is empty, index &lt; 0 or index &gt;= size of this Stream.
     */
    default Stream<E> set(int index, E element) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("set(" + index + ", e) on empty stream");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("set(" + index + ", e)");
        }
        Stream<E> preceding = Nil.instance();
        Stream<E> tail = this;
        for (int i = index; i > 0; i--, tail = tail.tail()) {
            if (tail.isEmpty()) {
                throw new IndexOutOfBoundsException("set(" + index + ", e) on stream of size " + size());
            }
            preceding = preceding.prepend(tail.head());
        }
        if (tail.isEmpty()) {
            throw new IndexOutOfBoundsException("set(" + index + ", e) on stream of size " + size());
        }
        // skip the current head element because it is replaced
        return preceding.appendAll(tail.tail().prepend(element));
    }

    /**
     * Returns a new Stream which contains all elements starting at beginIndex (inclusive). The substream is computed in
     * O(n).
     * <p/>
     * Examples:
     * <ul>
     * <li>{@code Stream.empty().substream(0)} returns {@code Stream.empty()}</li>
     * <li>{@code Stream.of(1).substream(0)} returns {@code Stream.of(1)}</li>
     * <li>{@code Stream.of(1).substream(1)} returns {@code Stream.empty()}</li>
     * <li>{@code Stream.of(1, 2, 3).substream(1)} returns {@code Stream.of(2, 3)}</li>
     * <li>{@code Stream.of(1, 2, 3).substream(3)} returns {@code Stream.empty()}</li>
     * </ul>
     * <p/>
     * The following calls are illegal:
     * <ul>
     * <li>{@code Stream.empty().substream(1)} throws</li>
     * <li>{@code Stream.of(1, 2, 3).substream(-1)} throws}</li>
     * <li>{@code Stream.of(1, 2, 3).substream(4)} throws}</li>
     * </ul>
     * <p/>
     * The result is equivalent to {@code (index == 0) ? this : tail().substream(index - 1)} but implemented without
     * recursion.
     * <p/>
     * If you do not want the bounds to be checked, use the fail-safe variant {@code drop(beginIndex)} instead.
     *
     * @param beginIndex Start index of the substream, where 0 &lt;= beginIndex &lt;= size()
     * @return The substream of the Stream, starting at beginIndex (inclusive).
     * @see #drop(int)
     * @see #take(int)
     */
    default Stream<E> substream(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("substream(" + beginIndex + ")");
        }
        Stream<E> result = this;
        for (int i = 0; i < beginIndex; i++, result = result.tail()) {
            if (result.isEmpty()) {
                throw new IndexOutOfBoundsException(String.format("substream(%s) on stream of size %s", beginIndex, i));
            }
        }
        return result;
    }

    /**
     * Returns a new Stream which contains the elements from beginIndex (inclusive) to endIndex (exclusive) of this Stream.
     * The substream is computed in O(2n).
     * <p/>
     * Examples:
     * <ul>
     * <li>{@code Stream.empty().substream(0, 0)} returns {@code Stream.empty()}</li>
     * <li>{@code Stream.of(1).substream(0, 0)} returns {@code Stream.empty()}</li>
     * <li>{@code Stream.of(1).substream(0, 1)} returns {@code Stream.of(1)}</li>
     * <li>{@code Stream.of(1).substream(1, 1)} returns {@code Stream.empty()}</li>
     * <li>{@code Stream.of(1, 2, 3).substream(1, 3)} returns {@code Stream.of(2, 3)}</li>
     * <li>{@code Stream.of(1, 2, 3).substream(3, 3)} returns {@code Stream.empty()}</li>
     * </ul>
     * <p/>
     * The following calls are illegal:
     * <ul>
     * <li>{@code Stream.of(1, 2, 3).substream(1, 0)} throws}</li>
     * <li>{@code Stream.of(1, 2, 3).substream(-1, 2)} throws}</li>
     * <li>{@code Stream.of(1, 2, 3).substream(1, 4)} throws}</li>
     * </ul>
     * <p/>
     * The result is equivalent to
     * {@code (beginIndex == 0) ? reverse().substream(size() - endIndex).reverse() : tail().substream(beginIndex - 1, endIndex)}
     * but implemented without recursion.
     * <p/>
     * If you do not want the bounds to be checked, use the fail-safe variant
     * {@code drop(beginIndex).take(endIndex - beginIndex)} instead.
     *
     * @param beginIndex Start index of the substream, where 0 &lt;= beginIndex &lt;= size()
     * @param endIndex   End index of the substream, where beginIndex &lt;= endIndex &lt;= size()
     * @return The substream of the Stream, starting at beginIndex (inclusive) and ending at endIndex (exclusive).
     * @see #drop(int)
     * @see #take(int)
     */
    default Stream<E> substream(int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex - beginIndex < 0) {
            throw new IndexOutOfBoundsException(String.format("substream(%s, %s)", beginIndex, endIndex));
        }
        if (endIndex - beginIndex == 0) {
            return Nil.instance();
        }
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("substream of empty stream");
        }
        if (beginIndex == 0) {
            return new Cons<>(head(), () -> tail().substream(0, endIndex - 1));
        } else {
            return tail().substream(beginIndex - 1, endIndex - 1);
        }
    }

    /**
     * Sorts the elements of this Stream according to their natural order.
     * <p/>
     * This call is equivalent to {@code stream().sorted().collect(Stream.collector())}.
     *
     * @return An ordered Stream.
     */
    default Stream<E> sort() {
        return stream().sorted().collect(collector());
    }

    /**
     * Sorts the elements of this Stream according to the provided {@link java.util.Comparator}.
     * <p/>
     * This call is equivalent to {@code stream().sorted(c).collect(Stream.collector())}.
     *
     * @param c An element Comparator.
     * @return An ordered Stream.
     */
    default Stream<E> sort(Comparator<? super E> c) {
        return stream().sorted(c).collect(collector());
    }

    // -- Stream conversion

    /**
     * Returns an array containing all elements of this Stream in the same order. The array is created in O(2n).
     *
     * @return The elements of this Stream as array.
     */
    default Object[] toArray() {
        final Object[] result = new Object[size()];
        int i = 0;
        for (Stream<E> stream = this; !stream.isEmpty(); stream = stream.tail(), i++) {
            result[i] = stream.head();
        }
        return result;
    }

    /**
     * Returns the given array filled with this elements in the same order or a new Array containing this elements, if
     * array.length &lt; size(). This takes O(2n).
     * <p/>
     * According to {@link java.util.ArrayList#toArray(Object[])}, the element in the array immediately following the
     * end of the Stream is set to null.
     *
     * @param array An Array to be filled with this elements.
     * @return The given array containing this elements or a new one if array.length &lt; size().
     */
    default E[] toArray(E[] array) {
        return toArrayList().toArray(array);
    }

    /**
     * Converts this Stream into an {@link java.util.ArrayList} which is mutable.
     *
     * @return An ArrayStream of the same size, containing this elements.
     */
    default java.util.ArrayList<E> toArrayList() {
        final java.util.ArrayList<E> result = new java.util.ArrayList<>();
        for (E element : this) {
            result.add(element);
        }
        return result;
    }

    /**
     * Returns a sequential {@link java.util.stream.Stream} representation of this Stream.
     * <p/>
     * This call is equivalent to {@code StreamSupport.stream(spliterator(), false)}.
     *
     * @return A sequential Stream of elements of this Stream.
     */
    default java.util.stream.Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Returns a parallel {@link java.util.stream.Stream} representation of this Stream.
     * <p/>
     * This call is equivalent to {@code StreamSupport.stream(spliterator(), true)}.
     *
     * @return A parallel Stream of elements of this Stream.
     */
    default java.util.stream.Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    // -- Implementation of interface Foldable

    @Override
    default <T> Stream<T> unit(T element) {
        return Stream.of(element);
    }

    @Override
    default Stream<E> zero() {
        return Stream.nil();
    }

    @Override
    default Stream<E> combine(Stream<E> l1, Stream<E> l2) {
        return l2.prependAll(l1);
    }

    // -- Stream specific optimization of default Foldable interface methods

    @Override
    default Stream<E> distinct() {
        return Stream.of(List.of(this).distinct());
    }

    @Override
    default Stream<E> filter(Predicate<? super E> predicate) {
        final E head = head();
        if (predicate.test(head)) {
            return new Cons<>(head, () -> tail().filter(predicate));
        } else {
            return tail().filter(predicate);
        }
    }

    // @see Algebra.Monad.flatMap()
    @Override
    default <T, STREAM extends Manifest<T, Stream<?>>> Stream<T> flatMap(Function<? super E, STREAM> mapper) {
        if (isEmpty()) {
            return Nil.instance();
        } else {
            @SuppressWarnings("unchecked")
            final Stream<T> stream = (Stream<T>) mapper.apply(head());
            return stream.appendAll(tail().flatMap(mapper));
        }
    }

    // @see Algebra.Monad.map()
    @Override
    default <T> Stream<T> map(Function<? super E, ? extends T> mapper) {
        if (isEmpty()) {
            return Nil.instance();
        } else {
            return new Cons<>(mapper.apply(head()), () -> tail().map(mapper));
        }
    }

    @Override
    default Stream<E> intersperse(E element) {
        if (isEmpty()) {
            return Nil.instance();
        } else {
            return new Cons<>(head(), () -> {
                final Stream<E> tail = tail();
                return tail.isEmpty() ? tail : new Cons<>(element, () -> tail.intersperse(element));
            });
        }
    }

    /**
     * Reverses this Stream and returns a new Stream in O(n).
     * <p/>
     * The result is equivalent to
     * <p/>
     * <pre>
     * <code>Stream&lt;E&gt; reverse(Stream&lt;E&gt; reversed, Stream&lt;E&gt; remaining) {
     *     if (remaining.isEmpty()) {
     *        return reversed;
     *     } else {
     *        return reverse(reversed.prepend(remaining.head()), remaining.tail());
     *     }
     * }
     * reverse(Nil.instance(), this);</code>
     * </pre>
     * <p/>
     * but implemented without recursion.
     *
     * @return A new Stream containing the elements of this Stream in reverse order.
     */
    @Override
    default Stream<E> reverse() {
        return foldLeft(nil(), Stream::prepend);
    }

    /**
     * Returns a Stream formed from this Stream and another Iterable collection by combining corresponding elements in
     * pairs. If one of the two collections is longer than the other, its remaining elements are ignored.
     *
     * @param <T>  The type of the second half of the returned pairs.
     * @param that The Iterable providing the second half of each result pair.
     * @return a new Stream containing pairs consisting of corresponding elements of this stream and that. The length of the
     * returned collection is the minimum of the lengths of this Stream and that.
     * @throws UnsatisfiedRequirementException if that is null.
     */
    @Override
    default <T> Stream<Tuple2<E, T>> zip(Iterable<T> that) {
        Require.nonNull(that, "that is null");
        Stream<Tuple2<E, T>> result = Nil.instance();
        Stream<E> stream1 = this;
        Iterator<T> stream2 = that.iterator();
        while (!stream1.isEmpty() && stream2.hasNext()) {
            result = result.prepend(Tuple.of(stream1.head(), stream2.next()));
            stream1 = stream1.tail();
        }
        return result.reverse();
    }

    /**
     * Returns a Stream formed from this Stream and another Iterable collection by combining corresponding elements in
     * pairs. If one of the two collections is shorter than the other, placeholder elements are used to extend the
     * shorter collection to the length of the longer.
     *
     * @param <T>      The type of the second half of the returned pairs.
     * @param that     The Iterable providing the second half of each result pair.
     * @param thisElem The element to be used to fill up the result if this Stream is shorter than that.
     * @param thatElem The element to be used to fill up the result if that is shorter than this Stream.
     * @return A new Stream containing pairs consisting of corresponding elements of this Stream and that. The length of the
     * returned collection is the maximum of the lengths of this Stream and that. If this Stream is shorter than
     * that, thisElem values are used to pad the result. If that is shorter than this Stream, thatElem values are
     * used to pad the result.
     * @throws UnsatisfiedRequirementException if that is null.
     */
    @Override
    default <T> Stream<Tuple2<E, T>> zipAll(Iterable<T> that, E thisElem, T thatElem) {
        Require.nonNull(that, "that is null");
        Stream<Tuple2<E, T>> result = Nil.instance();
        Iterator<E> stream1 = this.iterator();
        Iterator<T> stream2 = that.iterator();
        while (stream1.hasNext() || stream2.hasNext()) {
            final E elem1 = stream1.hasNext() ? stream1.next() : thisElem;
            final T elem2 = stream2.hasNext() ? stream2.next() : thatElem;
            result = result.prepend(Tuple.of(elem1, elem2));
        }
        return result.reverse();
    }

    /**
     * Zips this Stream with its indices.
     *
     * @return A new Stream containing all elements of this Stream paired with their index, starting with 0.
     */
    @Override
    default Stream<Tuple2<E, Integer>> zipWithIndex() {
        Stream<Tuple2<E, Integer>> result = Nil.instance();
        int index = 0;
        for (Stream<E> stream = this; !stream.isEmpty(); stream = stream.tail()) {
            result = result.prepend(Tuple.of(stream.head(), index++));
        }
        return result.reverse();
    }

    @Override
    default <E1, E2> Tuple2<Stream<E1>, Stream<E2>> unzip(Function<? super E, Tuple2<E1, E2>> unzipper) {
        Require.nonNull(unzipper, "unzipper is null");
        Stream<E1> xs = nil();
        Stream<E2> ys = nil();
        for (E element : this) {
            final Tuple2<E1, E2> t = unzipper.apply(element);
            xs = xs.prepend(t._1);
            ys = ys.prepend(t._2);
        }
        return Tuple.of(xs.reverse(), ys.reverse());
    }

    /**
     * Takes the first n elements of this stream or the whole stream, if this size &lt; n. The elements are taken in O(n).
     * <p/>
     * The result is equivalent to {@code substream(0, n)} but does not throw if n &lt; 0 or n &gt; size(). In the case of
     * n &lt; 0 the Nil is returned, in the case of n &gt; size() this Stream is returned.
     *
     * @param n The number of elements to take.
     * @return A stream consisting of the first n elements of this stream or the whole stream, if it has less than n elements.
     */
    @Override
    default Stream<E> take(int n) {
        Stream<E> result = Nil.instance();
        Stream<E> stream = this;
        for (int i = 0; i < n && !stream.isEmpty(); i++, stream = stream.tail()) {
            result = result.prepend(stream.head());
        }
        return result.reverse();
    }

    @Override
    default Stream<E> takeWhile(Predicate<? super E> predicate) {
        Stream<E> result = Nil.instance();
        for (Stream<E> stream = this; !stream.isEmpty() && predicate.test(stream.head()); stream = stream.tail()) {
            result = result.prepend(stream.head());
        }
        return result.reverse();
    }

    // -- Implementation of interface Iterable

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    default Iterator<E> iterator() {

        final class StreamIterator implements Iterator<E> {

            Stream<E> stream = Stream.this;

            @Override
            public boolean hasNext() {
                return !stream.isEmpty();
            }

            @Override
            public E next() {
                if (stream.isEmpty()) {
                    throw new NoSuchElementException();
                } else {
                    final E result = stream.head();
                    stream = stream.tail();
                    return result;
                }
            }
        }

        return new StreamIterator();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#spliterator()
     */
    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    // -- Object equals, hashCode, toString

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    /**
     * Returns a String representation of this Stream.
     * <p/>
     * If this is Nil, {@code "()"} is returned.
     * <p/>
     * If this is an Cons containing the elements e1, ..., en, then {@code "(" + Strings.toString(e1)
     * + ", " + ... + ", " + Strings.toString(en) + ")"} is returned.
     *
     * @return This Stream as String.
     */
    @Override
    String toString();


    // -- factory methods

    /**
     * Returns the single instance of Nil. Convenience method for {@code Nil.instance()} .
     *
     * @param <T> Component type of Nil, determined by type inference in the particular context.
     * @return The empty stream.
     */
    static <T> Stream<T> nil() {
        return Nil.instance();
    }

    /**
     * Creates a Stream of the given elements.
     * <p/>
     * <pre>
     * <code>  Stream.of(1, 2, 3, 4)
     * = Nil.instance().prepend(4).prepend(3).prepend(2).prepend(1)
     * = new Cons(1, new Cons(2, new Cons(3, new Cons(4, Nil.instance()))))</code>
     * </pre>
     *
     * @param <T>      Component type of the Stream.
     * @param elements Zero or more elements.
     * @return A stream containing the given elements in the same order.
     */
    @SafeVarargs
    static <T> Stream<T> of(T... elements) {
        Require.nonNull(elements, "elements is null");
        return Stream.of(List.of(elements));
    }

    /**
     * Creates a Stream of the given elements.
     *
     * @param <T>      Component type of the Stream.
     * @param elements An Iterable of elements.
     * @return A stream containing the given elements in the same order.
     */
    static <T> Stream<T> of(Iterable<? extends T> elements) {
        Require.nonNull(elements, "elements is null");
        if (elements instanceof Stream) {
            @SuppressWarnings("unchecked")
            final Stream<T> stream = (Stream<T>) elements;
            return stream;
        } else {
            @SuppressWarnings("unchecked")
            final Iterator<T> iterator = (Iterator<T>) elements.iterator();
            if (iterator.hasNext()) {
                return new Cons<>(iterator.next(), () -> Stream.of(() -> iterator));
            } else {
                return Nil.instance();
            }
        }
    }

    // -- Stream providers

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(Collector)} to obtain a {@link javaslang.collection.Stream}.
     *
     * @param <T> Component type of the Stream.
     * @return A Stream Collector.
     */
    static <T> Collector<T, ArrayList<T>, Stream<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, Stream<T>> finisher = Stream::of;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    // -- Stream implementations

    /**
     * Non-empty Stream.
     *
     * @param <E> Component type of the Stream.
     */
    // DEV NOTE: class declared final because of serialization proxy pattern.
    // (see Effective Java, 2nd ed., p. 315)
    static final class Cons<E> extends AbstractStream<E> implements Serializable {

        private static final long serialVersionUID = 53595355464228669L;

        private final E head;
        private final Supplier<Stream<E>> tail;

        public Cons(E head, Supplier<Stream<E>> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public E head() {
            return head;
        }

        @Override
        public Stream<E> tail() {
            return tail.get();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        // -- Serializable implementation

        /**
         * {@code writeReplace} method for the serialization proxy pattern.
         * <p/>
         * The presence of this method causes the serialization system to emit a SerializationProxy instance instead of
         * an instance of the enclosing class.
         *
         * @return A SerialiationProxy for this enclosing class.
         */
        private Object writeReplace() {
            return new SerializationProxy<>(this);
        }

        /**
         * {@code readObject} method for the serialization proxy pattern.
         * <p/>
         * Guarantees that the serialization system will never generate a serialized instance of the enclosing class.
         *
         * @param stream An object serialization stream.
         * @throws InvalidObjectException This method will throw with the message "Proxy required".
         */
        private void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        /**
         * A serialization proxy which, in this context, is used to deserialize immutable, linked Streams with final
         * instance fields.
         *
         * @param <E> The component type of the underlying stream.
         */
        // DEV NOTE: The serialization proxy pattern is not compatible with non-final, i.e. extendable,
        // classes. Also, it may not be compatible with circular object graphs.
        private static final class SerializationProxy<E> implements Serializable {

            private static final long serialVersionUID = 3851894487210781138L;

            // the instance to be serialized/deserialized
            private transient Cons<E> stream;

            /**
             * Constructor for the case of serialization, called by {@link Cons#writeReplace()}.
             * <p/>
             * The constructor of a SerializationProxy takes an argument that concisely represents the logical state of
             * an instance of the enclosing class.
             *
             * @param stream a Cons
             */
            SerializationProxy(Cons<E> stream) {
                this.stream = stream;
            }

            /**
             * Write an object to a serialization stream.
             *
             * @param s An object serialization stream.
             * @throws IOException If an error occurs writing to the stream.
             */
            private void writeObject(ObjectOutputStream s) throws IOException {
                s.defaultWriteObject();
                s.writeInt(stream.size());
                for (Stream<E> l = stream; !l.isEmpty(); l = l.tail()) {
                    s.writeObject(l.head());
                }
            }

            /**
             * Read an object from a deserialization stream.
             *
             * @param s An object deserialization stream.
             * @throws ClassNotFoundException If the object's class read from the stream cannot be found.
             * @throws InvalidObjectException If the stream contains no stream elements.
             * @throws IOException            If an error occurs reading from the stream.
             */
            @SuppressWarnings("ConstantConditions")
            private void readObject(ObjectInputStream s) throws ClassNotFoundException, InvalidObjectException,
                    IOException {
                s.defaultReadObject();
                final int size = s.readInt();
                if (size <= 0) {
                    throw new InvalidObjectException("No elements");
                }
                Stream<E> temp = Nil.instance();
                for (int i = 0; i < size; i++) {
                    @SuppressWarnings("unchecked")
                    final E element = (E) s.readObject();
                    temp = temp.append(element);
                }
                // DEV-NOTE: Cons is deserialized
                stream = (Cons<E>) temp;
            }

            /**
             * {@code readResolve} method for the serialization proxy pattern.
             * <p/>
             * Returns a logically equivalent instance of the enclosing class. The presence of this method causes the
             * serialization system to translate the serialization proxy back into an instance of the enclosing class
             * upon deserialization.
             *
             * @return A deserialized instance of the enclosing class.
             */
            private Object readResolve() {
                return stream;
            }
        }
    }

    /**
     * The empty Stream.
     * <p/>
     * This is a singleton, i.e. not Cloneable.
     *
     * @param <E> Component type of the Stream.
     */
    static final class Nil<E> extends AbstractStream<E> implements Serializable {

        private static final long serialVersionUID = 809473773619488283L;

        private static final Nil<?> INSTANCE = new Nil<>();

        // hidden
        private Nil() {
        }

        public static <T> Nil<T> instance() {
            @SuppressWarnings("unchecked")
            final Nil<T> instance = (Nil<T>) INSTANCE;
            return instance;
        }

        @Override
        public E head() {
            throw new UnsupportedOperationException("head of empty stream");
        }

        @Override
        public Stream<E> tail() {
            throw new UnsupportedOperationException("tail of empty stream");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        // -- Serializable implementation

        /**
         * Instance control for object serialization.
         *
         * @return The singleton instance of Nil.
         * @see java.io.Serializable
         */
        private Object readResolve() {
            return INSTANCE;
        }
    }

    // -- Stream API shared by implementations Cons and Nil

    /**
     * This class is needed because the interface {@link Stream} cannot use default methods to override Object's non-final
     * methods equals, hashCode and toString.
     * <p/>
     * See <a href="http://mail.openjdk.java.net/pipermail/lambda-dev/2013-March/008435.html">Allow default methods to
     * override Object's methods</a>.
     *
     * @param <E> Component type of the Stream.
     */
    static abstract class AbstractStream<E> implements Stream<E> {

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof Stream)) {
                return false;
            } else {
                Stream<?> stream1 = this;
                Stream<?> stream2 = (Stream<?>) o;
                while (!stream1.isEmpty() && !stream2.isEmpty()) {
                    final boolean isEqual = Objects.equals(stream1.head(), stream2.head());
                    if (!isEqual) {
                        return false;
                    }
                    stream1 = stream1.tail();
                    stream2 = stream2.tail();
                }
                return stream1.isEmpty() && stream2.isEmpty();
            }
        }

        @Override
        public int hashCode() {
            int hashCode = 1;
            for (Stream<E> stream = this; !stream.isEmpty(); stream = stream.tail()) {
                final E element = stream.head();
                hashCode = 31 * hashCode + Objects.hashCode(element);
            }
            return hashCode;
        }

        @Override
        public String toString() {
            return map(Strings::toString).join(", ", "Stream(", ")");
        }
    }
}
