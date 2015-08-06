/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Kind;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;
import javaslang.control.Some;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.*;

/**
 * Interface for immutable sequential data structures.
 * <p>
 * Mutation:
 *
 * <ul>
 * <li>{@link #append(Object)}</li>
 * <li>{@link #appendAll(Iterable)}</li>
 * <li>{@link #insert(int, Object)}</li>
 * <li>{@link #insertAll(int, Iterable)}</li>
 * <li>{@link #prepend(Object)}</li>
 * <li>{@link #prependAll(Iterable)}</li>
 * <li>{@link #set(int, Object)}</li>
 * </ul>
 *
 * Selection:
 *
 * <ul>
 * <li>{@link #get(int)}</li>
 * <li>{@link #indexOf(Object)}</li>
 * <li>{@link #indexOf(Object, int)}</li>
 * <li>{@link #lastIndexOf(Object)}</li>
 * <li>{@link #lastIndexOf(Object, int)}</li>
 * <li>{@link #subsequence(int)}</li>
 * <li>{@link #subsequence(int, int)}</li>
 * </ul>
 *
 * Transformation:
 *
 * <ul>
 * <li>{@link #combinations()}</li>
 * <li>{@link #combinations(int)}</li>
 * <li>{@link #permutations()}</li>
 * <li>{@link #sort()}</li>
 * <li>{@link #sort(Comparator)}</li>
 * <li>{@link #splitAt(int)}</li>
 * </ul>
 *
 * Traversal:
 *
 * <ul>
 * <li>{@link #iterator(int)}</li>
 * </ul>
 *
 * @param <T> Component type
 * @since 1.1.0
 */
public interface Seq<T> extends Traversable<T>, IntFunction<T> {

    /**
     * A {@code Seq} is a partial function which returns the element at the specified index by calling
     * {@linkplain #get(int)}.
     *
     * @param index an index
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    @Override
    default T apply(int index) {
        return get(index);
    }

    /**
     * Appends an element to this.
     *
     * @param element An element
     * @return A new Seq containing the given element appended to this elements
     */
    Seq<T> append(T element);

    /**
     * Appends all given elements to this.
     *
     * @param elements An Iterable of elements
     * @return A new Seq containing the given elements appended to this elements
     * @throws NullPointerException if {@code elements} is null
     */
    Seq<T> appendAll(Iterable<? extends T> elements);

    /**
     * Returns the union of all combinations from k = 0 to length().
     * <p>
     * Examples:
     * <pre>
     * <code>
     * [].combinations() = [[]]
     *
     * [1,2,3].combinations() = [
     *   [],                  // k = 0
     *   [1], [2], [3],       // k = 1
     *   [1,2], [1,3], [2,3], // k = 2
     *   [1,2,3]              // k = 3
     * ]
     * </code>
     * </pre>
     *
     * @return the combinations of this
     */
    Seq<? extends Seq<T>> combinations();

    /**
     * Returns the k-combination of this traversable, i.e. all subset of this of k distinct elements.
     *
     * @param k Size of subsets
     * @return the k-combination of this elements
     * @see <a href="http://en.wikipedia.org/wiki/Combination">Combination</a>
     */
    Seq<? extends Seq<T>> combinations(int k);

    /**
     * Tests whether this sequence contains a given sequence as a slice.
     * <p>
     * Note: may not terminate for infinite-sized collections.
     *
     * @param that the sequence to test
     * @return true if this sequence contains a slice with the same elements as that, otherwise false.
     * @throws NullPointerException if {@code that} is null.
     */
    default boolean containsSlice(Iterable<? extends T> that) {
        Objects.requireNonNull(that, "that is null");
        return indexOfSlice(that) >= 0;
    }

    /**
     * Returns the element at the specified index.
     *
     * @param index an index
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    T get(int index);

    /**
     * Returns the index of the first occurrence of the given element or -1 if this does not contain the given element.
     *
     * @param element an element
     * @return the index of the first occurrence of the given element
     */
    default int indexOf(T element) {
        return indexOf(element, 0);
    }

    /**
     * Returns the index of the first occurrence of the given element after or at some start index
     * or -1 if this does not contain the given element.
     *
     * @param element an element
     * @param from    start index
     * @return the index of the first occurrence of the given element
     */
    int indexOf(T element, int from);

    /**
     * Finds first index where this sequence contains a given sequence as a slice.
     * <p>
     * Note: may not terminate for infinite-sized collections.
     *
     * @param that the sequence to test
     * @return the first index such that the elements of this sequence starting at this index match
     * the elements of sequence that, or -1 of no such subsequence exists.
     * @throws NullPointerException if {@code that} is null.
     */
    default int indexOfSlice(Iterable<? extends T> that) {
        Objects.requireNonNull(that, "that is null");
        return indexOfSlice(that, 0);
    }

    /**
     * Finds first index after or at a start index where this sequence contains a given sequence as a slice.
     * <p>
     * Note: may not terminate for infinite-sized collections.
     *
     * @param that the sequence to test
     * @param from the start index
     * @return the first index &gt;= from such that the elements of this sequence starting at this index match
     * the elements of sequence that, or -1 of no such subsequence exists.
     * @throws NullPointerException if {@code that} is null.
     */
    default int indexOfSlice(Iterable<? extends T> that, int from) {
        Objects.requireNonNull(that, "that is null");
        class Util {
            int indexOfSlice(Traversable<T> t, Traversable<T> slice, int from) {
                if (t.isEmpty()) {
                    return from == 0 && slice.isEmpty() ? 0 : -1;
                }
                if (from <= 0 && checkPrefix(t, slice)) {
                    return 0;
                }
                int idx = indexOfSlice(t.tail(), slice, from - 1);
                return idx >= 0 ? idx + 1 : -1;
            }

            private boolean checkPrefix(Traversable<T> t, Traversable<T> prefix) {
                if (prefix.isEmpty()) {
                    return true;
                } else {
                    return !t.isEmpty() && java.util.Objects.equals(t.head(), prefix.head())
                            && checkPrefix(t.tail(), prefix.tail());
                }
            }
        }

        return new Util().indexOfSlice(this, unit(that), from);
    }

    /**
     * Inserts the given element at the specified index.
     *
     * @param index   an index
     * @param element an element
     * @return a new Seq, where the given element is inserted into this at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    Seq<T> insert(int index, T element);

    /**
     * Inserts the given elements at the specified index.
     *
     * @param index    an index
     * @param elements An Iterable of elements
     * @return a new Seq, where the given elements are inserted into this at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    Seq<T> insertAll(int index, Iterable<? extends T> elements);

    /**
     * Returns an iterator of this elements starting at the given index.
     * The result is equivalent to {@code this.subsequence(index).iterator()}.
     *
     * @param index an index
     * @return a new Iterator, starting with the element at the given index or the empty Iterator, if index = length()
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; length()
     */
    default Iterator<T> iterator(int index) {
        return subsequence(index).iterator();
    }

    /**
     * Returns the index of the last occurrence of the given element or -1 if this does not contain the given element.
     *
     * @param element an element
     * @return the index of the last occurrence of the given element
     */
    default int lastIndexOf(T element) {
        return lastIndexOf(element, Integer.MAX_VALUE);
    }

    /**
     * Returns the index of the last occurrence of the given element before or at a given end index
     * or -1 if this does not contain the given element.
     *
     * @param element an element
     * @param end     the end index
     * @return the index of the last occurrence of the given element
     */
    int lastIndexOf(T element, int end);

    /**
     * Finds last index where this sequence contains a given sequence as a slice.
     * <p>
     * Note: will not terminate for infinite-sized collections.
     *
     * @param that the sequence to test
     * @return the last index such that the elements of this sequence starting a this index match the elements
     * of sequence that, or -1 of no such subsequence exists.
     * @throws NullPointerException if {@code that} is null.
     */
    default int lastIndexOfSlice(Iterable<? extends T> that) {
        Objects.requireNonNull(that, "that is null");
        return lastIndexOfSlice(that, Integer.MAX_VALUE);
    }

    /**
     * Finds last index before or at a given end index where this sequence contains a given sequence as a slice.
     *
     * @param that the sequence to test
     * @param end  the end index
     * @return the last index &lt;= end such that the elements of this sequence starting at this index match
     * the elements of sequence that, or -1 of no such subsequence exists.
     * @throws NullPointerException if {@code that} is null.
     */
    default int lastIndexOfSlice(Iterable<? extends T> that, int end) {
        Objects.requireNonNull(that, "that is null");
        class Util {
            int lastIndexOfSlice(Traversable<T> t, Traversable<T> slice, int end) {
                if (end < 0) {
                    return -1;
                }
                if (t.isEmpty()) {
                    return slice.isEmpty() ? 0 : -1;
                }
                if (slice.isEmpty()) {
                    int len = t.length();
                    return len < end ? len : end;
                }
                Tuple2<Traversable<T>, Integer> r = findSlice(t, slice);
                if (r == null) {
                    return -1;
                }
                if (r._2 <= end) {
                    int idx = lastIndexOfSlice(r._1.tail(), slice, end - r._2);
                    return idx >= 0 ? idx + 1 + r._2 : r._2;
                } else {
                    return -1;
                }

            }

            private Tuple2<Traversable<T>, Integer> findSlice(Traversable<T> t, Traversable<T> slice) {
                if (t.isEmpty()) {
                    return slice.isEmpty() ? Tuple.of(t, 0) : null;
                }
                if (checkPrefix(t, slice)) {
                    return Tuple.of(t, 0);
                }
                Tuple2<Traversable<T>, Integer> idx = findSlice(t.tail(), slice);
                return idx != null ? Tuple.of(idx._1, idx._2 + 1) : null;
            }

            private boolean checkPrefix(Traversable<T> t, Traversable<T> prefix) {
                if (prefix.isEmpty()) {
                    return true;
                } else {
                    return !t.isEmpty() && java.util.Objects.equals(t.head(), prefix.head())
                            && checkPrefix(t.tail(), prefix.tail());
                }
            }
        }

        return new Util().lastIndexOfSlice(this, unit(that), end);
    }

    /**
     * Computes all unique permutations.
     * <p>
     * Example:
     * <pre>
     * <code>
     * [].permutations() = []
     *
     * [1,2,3].permutations() = [
     *   [1,2,3],
     *   [1,3,2],
     *   [2,1,3],
     *   [2,3,1],
     *   [3,1,2],
     *   [3,2,1]
     * ]
     * </code>
     * </pre>
     *
     * @return this unique permutations
     */
    Seq<? extends Seq<T>> permutations();

    /**
     * Prepends an element to this.
     *
     * @param element An element
     * @return A new Seq containing the given element prepended to this elements
     */
    Seq<T> prepend(T element);

    /**
     * Prepends all given elements to this.
     *
     * @param elements An Iterable of elements
     * @return A new Seq containing the given elements prepended to this elements
     */
    Seq<T> prependAll(Iterable<? extends T> elements);

    /**
     * Sets the given element at the specified index.
     *
     * @param index   an index
     * @param element an element
     * @return a new Seq consisting of this elements and the given element is set at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    Seq<T> set(int index, T element);

    /**
     * Sorts this elements according to their natural order. If this elements are not
     * {@code Comparable}, a {@code java.lang.ClassCastException} may be thrown.
     *
     * @return A sorted version of this
     * @throws ClassCastException if this elements are not {@code Comparable}
     */
    Seq<T> sort();

    /**
     * Sorts this elements according to the provided {@code Comparator}. If this elements are not
     * {@code Comparable}, a {@code java.lang.ClassCastException} may be thrown.
     *
     * @param comparator A comparator
     * @return a sorted version of this
     */
    Seq<T> sort(Comparator<? super T> comparator);

    /**
     * Splits a Seq at the specified index. The result of {@code splitAt(n)} is equivalent to
     * {@code Tuple.of(take(n), drop(n))}.
     *
     * @param n An index.
     * @return A Tuple containing the first n and the remaining elements.
     */
    Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAt(int n);

    /**
     * <p>Returns a Seq that is a subsequence of this. The subsequence begins with the element at the specified index
     * and extends to the end of this Seq.</p>
     * Examples:
     * <pre>
     * <code>
     * List.of(1, 2).substring(0) = List.of(1, 2)
     * List.of(1, 2).substring(1) = List.of(2)
     * List.of(1, 2).substring(2) = List.empty()
     * </code>
     * </pre>
     *
     * @param beginIndex the beginning index, inclusive
     * @return the specified subsequence
     * @throws IndexOutOfBoundsException if {@code beginIndex} is negative or larger than the length of this
     *                                   {@code String} object.
     */
    Seq<T> subsequence(int beginIndex);

    /**
     * <p>Returns a Seq that is a subsequence of this. The subsequence begins with the element at the specified index
     * and extends to the element at index {@code endIndex - 1}.</p>
     * Examples:
     * <pre>
     * <code>
     * List.of(1, 2, 3, 4).substring(1, 3) = List.of(2, 3)
     * List.of(1, 2, 3, 4).substring(0, 4) = List.of(1, 2, 3, 4)
     * List.of(1, 2, 3, 4).substring(2, 2) = List.empty()
     * </code>
     * </pre>
     *
     * @param beginIndex the beginning index, inclusive
     * @param endIndex   the end index, exclusive
     * @return the specified subsequence
     * @throws IndexOutOfBoundsException if the
     *                                   {@code beginIndex} is negative, or
     *                                   {@code endIndex} is larger than the length of
     *                                   this {@code String} object, or
     *                                   {@code beginIndex} is larger than
     *                                   {@code endIndex}.
     */
    Seq<T> subsequence(int beginIndex, int endIndex);

    // -- Adjusted return types of Traversable methods

    @Override
    Seq<T> clear();

    @Override
    Seq<Tuple2<T, T>> cartesianProduct();

    @Override
    <U> Seq<Tuple2<T, U>> cartesianProduct(Iterable<? extends U> that);

    @Override
    Seq<T> distinct();

    @Override
    Seq<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U> Seq<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    Seq<T> drop(int n);

    @Override
    Seq<T> dropRight(int n);

    @Override
    Seq<T> dropWhile(Predicate<? super T> predicate);

    @Override
    Seq<T> filter(Predicate<? super T> predicate);

    @Override
    Seq<Some<T>> filterOption(Predicate<? super T> predicate);

    @Override
    Seq<T> findAll(Predicate<? super T> predicate);

    @Override
    <U> Seq<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper);

    @Override
    <U> Seq<U> flatMapM(Function<? super T, ? extends Kind<? extends IterableKind<?>, ? extends U>> mapper);

    @Override
    Seq<Object> flatten();

    @Override
    Seq<? extends Seq<T>> grouped(int size);

    @Override
    Seq<T> init();

    @Override
    Option<? extends Seq<T>> initOption();

    @Override
    Seq<T> intersperse(T element);

    @Override
    <U> Seq<U> map(Function<? super T, ? extends U> mapper);

    @Override
    Tuple2<? extends Seq<T>, ? extends Seq<T>> partition(Predicate<? super T> predicate);

    @Override
    Seq<T> peek(Consumer<? super T> action);

    @Override
    Seq<T> remove(T element);

    @Override
    Seq<T> removeAll(T element);

    @Override
    Seq<T> removeAll(Iterable<? extends T> elements);

    @Override
    Seq<T> replace(T currentElement, T newElement);

    @Override
    Seq<T> replaceAll(T currentElement, T newElement);

    @Override
    Seq<T> replaceAll(UnaryOperator<T> operator);

    @Override
    Seq<T> retainAll(Iterable<? extends T> elements);

    @Override
    Seq<T> reverse();

    @Override
    Seq<? extends Seq<T>> sliding(int size);

    @Override
    Seq<? extends Seq<T>> sliding(int size, int step);

    @Override
    Tuple2<? extends Seq<T>, ? extends Seq<T>> span(Predicate<? super T> predicate);

    @Override
    Seq<T> tail();

    @Override
    Option<? extends Seq<T>> tailOption();

    @Override
    Seq<T> take(int n);

    @Override
    Seq<T> takeRight(int n);

    @Override
    Seq<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <U> Seq<U> unit(Iterable<? extends U> iterable);

    @Override
    <T1, T2> Tuple2<? extends Seq<T1>, ? extends Seq<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <U> Seq<Tuple2<T, U>> zip(Iterable<U> that);

    @Override
    <U> Seq<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem);

    @Override
    Seq<Tuple2<T, Integer>> zipWithIndex();
}
