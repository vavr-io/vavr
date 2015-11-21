/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.control.Option;

/**
 * Interface for immutable sequential data structures.
 * <p>
 * Creation:
 *
 * <ul>
 * <li>{@link #unit(java.lang.Iterable)}</li>
 * </ul>
 *
 * Filtering:
 *
 * <ul>
 * <li>{@link #remove(Object)}</li>
 * <li>{@link #removeAll(Object)}</li>
 * <li>{@link #removeAll(java.lang.Iterable)}</li>
 * <li>{@link #removeAt(int)}</li>
 * <li>{@link #removeFirst(Predicate)}</li>
 * <li>{@link #removeLast(Predicate)}</li>
 * </ul>
 *
 * Mutation:
 *
 * <ul>
 * <li>{@link #append(Object)}</li>
 * <li>{@link #appendAll(java.lang.Iterable)}</li>
 * <li>{@link #insert(int, Object)}</li>
 * <li>{@link #insertAll(int, java.lang.Iterable)}</li>
 * <li>{@link #prepend(Object)}</li>
 * <li>{@link #prependAll(java.lang.Iterable)}</li>
 * <li>{@link #update(int, Object)}</li>
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
 * <li>{@link #slice(int, int)}</li>
 * <li>{@link #subSequence(int)}</li>
 * <li>{@link #subSequence(int, int)}</li>
 * </ul>
 *
 * Transformation:
 *
 * <ul>
 * <li>{@link #crossProduct()}</li>
 * <li>{@link #crossProduct(int)}</li>
 * <li>{@link #crossProduct(java.lang.Iterable)}</li>
 * <li>{@link #combinations()}</li>
 * <li>{@link #combinations(int)}</li>
 * <li>{@link #grouped(int)}</li>
 * <li>{@link #intersperse(Object)}</li>
 * <li>{@link #padTo(int, Object)}</li>
 * <li>{@link #permutations()}</li>
 * <li>{@link #reverse()}</li>
 * <li>{@link #sort()}</li>
 * <li>{@link #sort(Comparator)}</li>
 * <li>{@link #splitAt(int)}</li>
 * <li>{@link #unzip(Function)}</li>
 * <li>{@link #zip(java.lang.Iterable)}</li>
 * <li>{@link #zipAll(java.lang.Iterable, Object, Object)}</li>
 * <li>{@link #zipWithIndex()}</li>
 * </ul>
 *
 * Traversal:
 *
 * <ul>
 * <li>{@link #iterator(int)}</li>
 * </ul>
 *
 * @param <T> Component type
 * @author Daniel Dietrich
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
     * @param elements An java.lang.Iterable of elements
     * @return A new Seq containing the given elements appended to this elements
     * @throws NullPointerException if {@code elements} is null
     */
    Seq<T> appendAll(java.lang.Iterable<? extends T> elements);

    /**
     * Calculates the cross product (, i.e. square) of {@code this x this}.
     * <p>
     * Example:
     * <pre>
     * <code>
     * // = List of Tuples (1, 1), (1, 2), (1, 3), (2, 1), (2, 2), (2, 3), (3, 1), (3, 2), (3, 3)
     * List.of(1, 2, 3).crossProduct();
     * </code>
     * </pre>
     *
     * @return a new Seq containing the square of {@code this}
     */
    Seq<Tuple2<T, T>> crossProduct();

    /**
     * Calculates the n-ary cartesian power (or <em>cross product</em> or simply <em>product</em>) of this.
     * <p>
     * Example:
     * <pre>
     * <code>
     * // = ((A,A), (A,B), (A,C), ..., (B,A), (B,B), ..., (Z,Y), (Z,Z))
     * CharSeq.rangeClosed('A', 'Z').crossProduct(2);
     * </code>
     * </pre>
     *
     * @param power the number of cartesian multiplications
     * @return A new Seq representing the n-ary cartesian power of this
     */
    Seq<IndexedSeq<T>> crossProduct(int power);

    /**
     * Calculates the cross product {@code this x that}.
     * <p>
     * Example:
     * <pre>
     * <code>
     * // = List of Tuples (1, 'a'), (1, 'b'), (2, 'a'), (2, 'b'), (3, 'a'), (3, 'b')
     * List.of(1, 2, 3).crossProduct(List.of('a', 'b');
     * </code>
     * </pre>
     *
     * @param that Another iterable
     * @param <U>  Component type
     * @return a new Seq containing the cross product {@code this x that}
     * @throws NullPointerException if that is null
     */
    <U> Seq<Tuple2<T, U>> crossProduct(java.lang.Iterable<? extends U> that);

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
    default boolean containsSlice(java.lang.Iterable<? extends T> that) {
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
     * the elements of sequence that, or -1 of no such slice exists.
     * @throws NullPointerException if {@code that} is null.
     */
    default int indexOfSlice(java.lang.Iterable<? extends T> that) {
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
     * the elements of sequence that, or -1 of no such slice exists.
     * @throws NullPointerException if {@code that} is null.
     */
    default int indexOfSlice(java.lang.Iterable<? extends T> that, int from) {
        Objects.requireNonNull(that, "that is null");
        class Util {
            int indexOfSlice(Seq<T> t, Seq<T> slice, int from) {
                if (t.isEmpty()) {
                    return from == 0 && slice.isEmpty() ? 0 : -1;
                }
                if (from <= 0 && checkPrefix(t, slice)) {
                    return 0;
                }
                int idx = indexOfSlice(t.tail(), slice, from - 1);
                return idx >= 0 ? idx + 1 : -1;
            }

            private boolean checkPrefix(Seq<T> t, Seq<T> prefix) {
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
     * Finds index of first element satisfying some predicate.
     *
     * @param p
     *            the predicate used to test elements.
     * @return the index of the first element of this $coll that satisfies the
     *         predicate `p`, or `-1`, if none exists.
     */
    default int indexWhere(Predicate<? super T> p) {
        return indexWhere(p, 0);
    }

    /**
     * Finds index of the first element satisfying some predicate after or at
     * some start index.
     *
     * @param predicate
     *            the predicate used to test elements.
     * @param from
     *            the start index
     * @return the index `>= from` of the first element of this Seq that
     *         satisfies the predicate `p`, or `-1`, if none exists.
     */
    default int indexWhere(Predicate<? super T> predicate, int from) {
        Objects.requireNonNull(predicate, "predicate is null");
        int i = from;
        Iterator<T> it = iterator().drop(from);
        while(it.hasNext()) {
            if(predicate.test(it.next())) {
                return i;
            }
            else {
                i++;
            }
        }
        return -1;
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
     * @param elements An java.lang.Iterable of elements
     * @return a new Seq, where the given elements are inserted into this at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    Seq<T> insertAll(int index, java.lang.Iterable<? extends T> elements);

    /**
     * Inserts an element between all elements of this Traversable.
     *
     * @param element An element.
     * @return an interspersed version of this
     */
    Seq<T> intersperse(T element);

    /**
     * Returns an iterator of this elements starting at the given index.
     * The result is equivalent to {@code this.subSequence(index).iterator()}.
     *
     * @param index an index
     * @return a new Iterator, starting with the element at the given index or the empty Iterator, if index = length()
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; length()
     */
    default Iterator<T> iterator(int index) {
        return subSequence(index).iterator();
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
     * Finds index of last element satisfying some predicate.
     *
     * @param p the predicate used to test elements.
     * @return the index of the last element of this Seq that satisfies the
     *         predicate `p`, or `-1`, if none exists.
     */
    default int lastIndexWhere(Predicate<? super T> p){
        return lastIndexWhere(p, length() - 1);
    }

    /**
     * Finds index of last element satisfying some predicate before or at given
     * end index.
     *
     * @param predicate the predicate used to test elements.
     * @return the index `<= end` of the last element of this Seq that
     *         satisfies the predicate `p`, or `-1`, if none exists.
     */
    default int lastIndexWhere(Predicate<? super T> predicate, int end) {
        Objects.requireNonNull(predicate, "predicate is null");
        int i = length() - 1;
        Iterator<T> it = reverseIterator();
        while(it.hasNext()) {
            T elem = it.next();
            if(i > end || !predicate.test(elem)) {
                i--;
            }
            else {
                break;
            }
        }
        return i;
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
     * of sequence that, or -1 of no such slice exists.
     * @throws NullPointerException if {@code that} is null.
     */
    default int lastIndexOfSlice(java.lang.Iterable<? extends T> that) {
        Objects.requireNonNull(that, "that is null");
        return lastIndexOfSlice(that, Integer.MAX_VALUE);
    }

    /**
     * Finds last index before or at a given end index where this sequence contains a given sequence as a slice.
     *
     * @param that the sequence to test
     * @param end  the end index
     * @return the last index &lt;= end such that the elements of this sequence starting at this index match
     * the elements of sequence that, or -1 of no such slice exists.
     * @throws NullPointerException if {@code that} is null.
     */
    default int lastIndexOfSlice(java.lang.Iterable<? extends T> that, int end) {
        Objects.requireNonNull(that, "that is null");
        class Util {
            int lastIndexOfSlice(Seq<T> t, Seq<T> slice, int end) {
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
                Tuple2<Seq<T>, Integer> r = findSlice(t, slice);
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

            private Tuple2<Seq<T>, Integer> findSlice(Seq<T> t, Seq<T> slice) {
                if (t.isEmpty()) {
                    return slice.isEmpty() ? Tuple.of(t, 0) : null;
                }
                if (checkPrefix(t, slice)) {
                    return Tuple.of(t, 0);
                }
                Tuple2<Seq<T>, Integer> idx = findSlice(t.tail(), slice);
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
     * A copy of this sequence with an element appended until a given target length is reached.
     *
     * @param length  the target length
     * @param element the padding element
     * @return a new sequence consisting of all elements of this sequence followed by the minimal number
     * of occurrences of <code>element</code> so that the resulting sequence has a length of at least <code>length</code>.
     */
    Seq<T> padTo(int length, T element);

    /**
     * Produces a new list where a slice of elements in this list is replaced by another sequence.
     *
     * @param from     the index of the first replaced element
     * @param that     sequence for replacement
     * @param replaced the number of elements to drop in the original list
     * @return a new sequence.
     */
    Seq<T> patch(int from, java.lang.Iterable<? extends T> that, int replaced);

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
     * @param elements An java.lang.Iterable of elements
     * @return A new Seq containing the given elements prepended to this elements
     */
    Seq<T> prependAll(java.lang.Iterable<? extends T> elements);

    /**
     * Removes the first occurrence of the given element.
     *
     * @param element An element to be removed from this Seq.
     * @return a Seq containing all elements of this without the first occurrence of the given element.
     */
    Seq<T> remove(T element);

    /**
     * Removes all occurrences of the given element.
     *
     * @param element An element to be removed from this Seq.
     * @return a Seq containing all elements of this but not the given element.
     */
    Seq<T> removeAll(T element);

    /**
     * Removes all occurrences of the given elements.
     *
     * @param elements Elements to be removed from this Seq.
     * @return a Seq containing all elements of this but none of the given elements.
     * @throws NullPointerException if {@code elements} is null
     */
    Seq<T> removeAll(java.lang.Iterable<? extends T> elements);

    /**
     * Removes the element at the specified position in this sequence. Shifts any subsequent elements to the left
     * (subtracts one from their indices).
     *
     * @param index position of element to remove
     * @return a sequence containing all elements of this without the element at the specified position.
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    Seq<T> removeAt(int index);

    /**
     * Removes the first occurrence that satisfy predicate
     *
     * @param predicate an predicate
     * @return a new Seq
     */
    Seq<T> removeFirst(Predicate<T> predicate);

    /**
     * Removes the last occurrence that satisfy predicate
     *
     * @param predicate an predicate
     * @return a new Seq
     */
    Seq<T> removeLast(Predicate<T> predicate);


    /**
     * Reverses the order of elements.
     *
     * @return the reversed elements.
     */
    Seq<T> reverse();
    
    /**
     * An iterator yielding elements in reversed order.
     *
     * Note: `xs.reverseIterator` is the same as `xs.reverse().iterator()` but might
     * be more efficient.
     *
     * @return an iterator yielding the elements of this Seq in reversed order
     */
    Iterator<T> reverseIterator();

    /**
     * Returns a Seq that is a <em>slice</em> of this. The slice begins with the element at the specified
     * {@code beginIndex} and extends to the element at index {@code endIndex - 1}.
     * <p>
     * Examples:
     *
     * <pre>
     * <code>
     * List.of(1, 2, 3, 4).slice(1, 3); // = (2, 3)
     * List.of(1, 2, 3, 4).slice(0, 4); // = (1, 2, 3, 4)
     * List.of(1, 2, 3, 4).slice(2, 2); // = ()
     * List.of(1, 2).slice(1, 0);       // = ()
     * List.of(1, 2).slice(-10, 10);    // = (1, 2)
     * </code>
     * </pre>
     *
     * See also {@link #subSequence(int, int)} which throws in some cases instead of returning a sequence.
     *
     * @param beginIndex the beginning index, inclusive
     * @param endIndex   the end index, exclusive
     * @return the specified slice
     */
    Seq<T> slice(int beginIndex, int endIndex);

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
     * Sorts this elements by comparing the elements in a different domain, using the given {@code mapper}.
     *
     * @param mapper A mapper
     * @param <U>    The domain where elements are compared
     * @return a sorted version of this
     */
    <U extends Comparable<? super U>> Seq<T> sortBy(Function<? super T, ? extends U> mapper);

    /**
     * Sorts this elements by comparing the elements in a different domain, using the given {@code mapper}.
     *
     * @param comparator A comparator
     * @param mapper     A mapper
     * @param <U>        The domain where elements are compared
     * @return a sorted version of this
     */
    <U> Seq<T> sortBy(Comparator<? super U> comparator, Function<? super T, ? extends U> mapper);

    /**
     * Splits a Seq at the specified index. The result of {@code splitAt(n)} is equivalent to
     * {@code Tuple.of(take(n), drop(n))}.
     *
     * @param n An index.
     * @return A Tuple containing the first n and the remaining elements.
     */
    Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAt(int n);

    /**
     * Splits a sequence at the first element which satisfies the {@link Predicate}, e.g. Tuple(init, element+tail).
     *
     * @param predicate An predicate
     * @return A {@link Tuple} containing divided sequences
     */
    Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAt(Predicate<? super T> predicate);

    /**
     * Splits a sequence at the first element which satisfies the {@link Predicate}, e.g. Tuple(init+element, tail).
     *
     * @param predicate An predicate
     * @return A {@link Tuple} containing divided sequences
     */
    Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAtInclusive(Predicate<? super T> predicate);

    /**
     * Tests whether this list starts with the given sequence.
     *
     * @param that the sequence to test
     * @return true if that is empty or that is prefix of this collection, false otherwise.
     */
    default boolean startsWith(java.lang.Iterable<? extends T> that) {
        return startsWith(that, 0);
    }

    /**
     * Tests whether this list contains the given sequence at a given index.
     * <p>
     * Note: If the both the receiver object this and the argument that are infinite sequences this method may not terminate.
     *
     * @param that   the sequence to test
     * @param offset the index where the sequence is searched.
     * @return true if that is empty or that is prefix of this collection starting from the given offset, false otherwise.
     */
    default boolean startsWith(Iterable<? extends T> that, int offset) {
        Objects.requireNonNull(that, "that is null");
        if(offset < 0) return false;
        Iterator<T> i = this.iterator().drop(offset);
        java.util.Iterator<? extends T> j = that.iterator();
        while(i.hasNext() && j.hasNext()) {
            if(!Objects.equals(i.next(), j.next())) {
                return false;
            }
        }
        return !j.hasNext();
    }

    
    /**
     * Tests whether this sequence ends with the given sequence.
     * <p>
     * Note: If the both the receiver object this and the argument that are infinite sequences this method may not terminate.
     *
     * @param that   the sequence to test
     * @return true if this sequence has that as a suffix, false otherwise..
     */
    default boolean endsWith(Seq<? extends T> that) {
        Objects.requireNonNull(that, "that is null");
        Iterator<T> i = this.iterator().drop(length() - that.length());
        Iterator<? extends T> j = that.iterator();
        while(i.hasNext() && j.hasNext()) {
            if(!Objects.equals(i.next(), j.next())) {
                return false;
            }
        }
        return !j.hasNext();
    }
    
    /**
     * Returns a Seq that is a subsequence of this. The subsequence begins with the element at the specified
     * {@code beginIndex} and extends to the end of this Seq.
     * <p>
     * Examples:
     *
     * <pre>
     * <code>
     * List.of(1, 2).subSequence(0);     // = (1, 2)
     * List.of(1, 2).subSequence(1);     // = (2)
     * List.of(1, 2).subSequence(2);     // = ()
     * List.of(1, 2).subSequence(10);    // throws IndexOutOfBoundsException
     * List.of(1, 2).subSequence(-10);   // throws IndexOutOfBoundsException
     * </code>
     * </pre>
     *
     * See also {@link #drop(int)} which is similar but does not throw.
     *
     * @param beginIndex the beginning index, inclusive
     * @return the specified subsequence
     * @throws IndexOutOfBoundsException if {@code beginIndex} is negative or larger than the length of this
     *                                   {@code String} object.
     */
    Seq<T> subSequence(int beginIndex);

    /**
     * Returns a Seq that is a subsequence of this. The subsequence begins with the element at the specified
     * {@code beginIndex} and extends to the element at index {@code endIndex - 1}.
     * <p>
     * Examples:
     *
     * <pre>
     * <code>
     * List.of(1, 2, 3, 4).subSequence(1, 3); // = (2, 3)
     * List.of(1, 2, 3, 4).subSequence(0, 4); // = (1, 2, 3, 4)
     * List.of(1, 2, 3, 4).subSequence(2, 2); // = ()
     * List.of(1, 2).subSequence(1, 0);       // throws IndexOutOfBoundsException
     * List.of(1, 2).subSequence(-10, 1);     // throws IndexOutOfBoundsException
     * List.of(1, 2).subSequence(0, 10);      // throws IndexOutOfBoundsException
     * </code>
     * </pre>
     *
     * See also {@link #slice(int, int)} which returns an empty sequence instead of throwing.
     *
     * @param beginIndex the beginning index, inclusive
     * @param endIndex   the end index, exclusive
     * @return the specified subsequence
     * @throws IndexOutOfBoundsException if {@code beginIndex} or {@code endIndex} is negative,
     *                                   if {@code endIndex} is greater than {@code length()},
     *                                   or if {@code beginIndex} is greater than {@code endIndex}
     */
    Seq<T> subSequence(int beginIndex, int endIndex);

    /**
     * Creates an instance of this type of an {@code java.lang.Iterable}.
     *
     * @param <U>      Component type
     * @param iterable an {@code java.lang.Iterable}
     * @return A new instance of this collection containing the elements of the given {@code iterable}.
     */
    <U> Seq<U> unit(java.lang.Iterable<? extends U> iterable);

    /**
     * Updates the given element at the specified index.
     *
     * @param index   an index
     * @param element an element
     * @return a new Seq consisting of this elements and the given element is set at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    Seq<T> update(int index, T element);
    
    /**
     * Computes length of longest segment whose elements all satisfy some predicate.
     * 
     * Note: may not terminate for infinite-sized collections.
     * 
     * @param predicate the predicate used to test elements.
     * @param from the index where the search starts.
     * @return the length of the longest segment of this sequence starting from index 
     *  from such that every element of the segment satisfies the predicate p.
     */
    int segmentLength(Predicate<? super T> predicate, int from);

    // -- Adjusted return types of Traversable methods

    @Override
    Seq<T> clear();

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
    <U> Seq<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper);

    @Override
    <U> Seq<U> flatten();

    @Override
    default <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return reverse().foldLeft(zero, (xs, x) -> f.apply(x, xs));
    }

    @Override
    <C> Map<C, ? extends Seq<T>> groupBy(Function<? super T, ? extends C> classifier);

    @Override
    Seq<T> init();

    @Override
    Option<? extends Seq<T>> initOption();

    @Override
    <U> Seq<U> map(Function<? super T, ? extends U> mapper);

    @Override
    Tuple2<? extends Seq<T>, ? extends Seq<T>> partition(Predicate<? super T> predicate);

    @Override
    Seq<T> peek(Consumer<? super T> action);

    @Override
    Seq<T> replace(T currentElement, T newElement);

    @Override
    Seq<T> replaceAll(T currentElement, T newElement);

    @Override
    Seq<T> retainAll(java.lang.Iterable<? extends T> elements);

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
    Seq<T> takeUntil(Predicate<? super T> predicate);

    @Override
    Seq<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <T1, T2> Tuple2<? extends Seq<T1>, ? extends Seq<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);
    
    @Override
    <T1, T2, T3> Tuple3<? extends Seq<T1>, ? extends Seq<T2>, ? extends Seq<T3>> unzip3(Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper);

    @Override
    <U> Seq<Tuple2<T, U>> zip(java.lang.Iterable<U> that);

    @Override
    <U> Seq<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem);

    @Override
    Seq<Tuple2<T, Integer>> zipWithIndex();

}
