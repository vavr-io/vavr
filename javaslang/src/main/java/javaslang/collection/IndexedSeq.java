/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Function1;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.control.Match;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Interface for immutable, indexed sequences.
 * <p>
 * Efficient random access is characteristic for indexed sequences.
 *
 * @param <T> component type
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface IndexedSeq<T> extends Seq<T> {

    long serialVersionUID = 1L;

    /**
     * Narrows a widened {@code IndexedSeq<? extends T>} to {@code IndexedSeq<T>}
     * by performing a type safe-cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param indexedSeq An {@code IndexedSeq}.
     * @param <T>        Component type of the {@code IndexedSeq}.
     * @return the given {@code indexedSeq} instance as narrowed type {@code IndexedSeq<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> IndexedSeq<T> narrow(IndexedSeq<? extends T> indexedSeq) {
        return (IndexedSeq<T>) indexedSeq;
    }

    // -- Adjusted return types of Seq methods

    @Override
    IndexedSeq<T> append(T element);

    @Override
    IndexedSeq<T> appendAll(Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> clear();

    @Override
    IndexedSeq<? extends IndexedSeq<T>> combinations();

    @Override
    IndexedSeq<? extends IndexedSeq<T>> combinations(int k);

    @Override
    IndexedSeq<Tuple2<T, T>> crossProduct();

    @Override
    IndexedSeq<? extends IndexedSeq<T>> crossProduct(int power);

    @Override
    <U> IndexedSeq<Tuple2<T, U>> crossProduct(Iterable<? extends U> that);

    @Override
    IndexedSeq<T> distinct();

    @Override
    IndexedSeq<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U> IndexedSeq<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    IndexedSeq<T> drop(long n);

    @Override
    IndexedSeq<T> dropRight(long n);

    @Override
    IndexedSeq<T> dropUntil(Predicate<? super T> predicate);

    @Override
    IndexedSeq<T> dropWhile(Predicate<? super T> predicate);

    @Override
    default boolean endsWith(Seq<? extends T> that) {
        Objects.requireNonNull(that, "that is null");
        if (that instanceof IndexedSeq) {
            int i = length() - 1;
            int j = that.length() - 1;
            if (j > i) {
                return false;
            } else {
                while (j >= 0) {
                    if (!Objects.equals(this.get(i), that.get(j))) {
                        return false;
                    }
                    i--;
                    j--;
                }
                return true;
            }
        } else {
            return Seq.super.endsWith(that);
        }
    }

    @Override
    IndexedSeq<T> filter(Predicate<? super T> predicate);

    @Override
    <U> IndexedSeq<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper);

    @Override
    <C> Map<C, ? extends IndexedSeq<T>> groupBy(Function<? super T, ? extends C> classifier);

    @Override
    default int indexWhere(Predicate<? super T> predicate, int from) {
        Objects.requireNonNull(predicate, "predicate is null");
        int start = Math.max(from, 0);
        int n = start + segmentLength(predicate.negate(), start);
        return (n >= length()) ? -1 : n;
    }

    @Override
    Iterator<? extends IndexedSeq<T>> grouped(long size);

    @Override
    IndexedSeq<T> init();

    @Override
    Option<? extends IndexedSeq<T>> initOption();

    @Override
    IndexedSeq<T> insert(int index, T element);

    @Override
    IndexedSeq<T> insertAll(int index, Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> intersperse(T element);

    @Override
    default T last() {
        if (isEmpty()) {
            throw new NoSuchElementException("last of empty IndexedSeq");
        } else {
            return get(length() - 1);
        }
    }

    @Override
    default int lastIndexOfSlice(Iterable<? extends T> that, int end) {
        return IndexedSeqModule.LastIndexOfSlice.lastIndexOfSlice(this, unit(that), end);
    }

    @Override
    default int lastIndexWhere(Predicate<? super T> predicate, int end) {
        Objects.requireNonNull(predicate, "predicate is null");
        int i = Math.min(end, length() - 1);
        while (i >= 0 && !predicate.test(this.get(i))) {
            i--;
        }
        return i;
    }

    @Override
    <U> IndexedSeq<U> map(Function<? super T, ? extends U> mapper);

    @Override
    Match.MatchValue.Of<? extends IndexedSeq<T>> match();

    @Override
    IndexedSeq<T> padTo(int length, T element);

    @Override
    IndexedSeq<T> patch(int from, Iterable<? extends T> that, int replaced);

    @Override
    Tuple2<? extends IndexedSeq<T>, ? extends IndexedSeq<T>> partition(Predicate<? super T> predicate);

    @Override
    IndexedSeq<T> peek(Consumer<? super T> action);

    @Override
    IndexedSeq<? extends IndexedSeq<T>> permutations();

    @Override
    IndexedSeq<T> prepend(T element);

    @Override
    IndexedSeq<T> prependAll(Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> remove(T element);

    @Override
    IndexedSeq<T> removeFirst(Predicate<T> predicate);

    @Override
    IndexedSeq<T> removeLast(Predicate<T> predicate);

    @Override
    IndexedSeq<T> removeAt(int index);

    @Override
    IndexedSeq<T> removeAll(T element);

    @Override
    IndexedSeq<T> removeAll(Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> replace(T currentElement, T newElement);

    @Override
    IndexedSeq<T> replaceAll(T currentElement, T newElement);

    @Override
    IndexedSeq<T> retainAll(Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> reverse();

    @Override
    default Iterator<T> reverseIterator() {
        return new AbstractIterator<T>() {
            private int i = IndexedSeq.this.length();

            @Override
            public boolean hasNext() {
                return i > 0;
            }

            @Override
            public T getNext() {
                return IndexedSeq.this.get(--i);
            }
        };
    }

    @Override
    IndexedSeq<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation);

    @Override
    <U> IndexedSeq<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation);

    @Override
    <U> IndexedSeq<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation);

    @Override
    default int segmentLength(Predicate<? super T> predicate, int from) {
        Objects.requireNonNull(predicate, "predicate is null");
        int len = length();
        int i = from;
        while (i < len && predicate.test(this.get(i))) {
            i++;
        }
        return i - from;
    }

    @Override
    IndexedSeq<T> slice(long beginIndex, long endIndex);

    @Override
    Iterator<? extends IndexedSeq<T>> sliding(long size);

    @Override
    Iterator<? extends IndexedSeq<T>> sliding(long size, long step);

    @Override
    IndexedSeq<T> sorted();

    @Override
    IndexedSeq<T> sorted(Comparator<? super T> comparator);

    @Override
    <U extends Comparable<? super U>> IndexedSeq<T> sortBy(Function<? super T, ? extends U> mapper);

    @Override
    <U> IndexedSeq<T> sortBy(Comparator<? super U> comparator, Function<? super T, ? extends U> mapper);

    @Override
    Tuple2<? extends IndexedSeq<T>, ? extends IndexedSeq<T>> span(Predicate<? super T> predicate);

    @Override
    default boolean startsWith(Iterable<? extends T> that, int offset) {
        Objects.requireNonNull(that, "that is null");
        if (offset < 0) return false;
        if (that instanceof IndexedSeq) {
            IndexedSeq<? extends T> dhat = (IndexedSeq<? extends T>) that;
            int i = offset;
            int j = 0;
            int thisLen = length();
            int thatLen = dhat.length();
            while (i < thisLen && j < thatLen && Objects.equals(this.get(i), dhat.get(j))) {
                i++;
                j++;
            }
            return j == thatLen;
        } else {
            int i = offset;
            int thisLen = length();
            java.util.Iterator<? extends T> thatElems = that.iterator();
            while (i < thisLen && thatElems.hasNext()) {
                if (!Objects.equals(this.get(i), thatElems.next())) {
                    return false;
                }
                i++;
            }
            return !thatElems.hasNext();
        }
    }

    @Override
    IndexedSeq<T> subSequence(int beginIndex);

    @Override
    IndexedSeq<T> subSequence(int beginIndex, int endIndex);

    @Override
    IndexedSeq<T> tail();

    @Override
    Option<? extends IndexedSeq<T>> tailOption();

    @Override
    IndexedSeq<T> take(long n);

    @Override
    IndexedSeq<T> takeRight(long n);

    @Override
    IndexedSeq<T> takeUntil(Predicate<? super T> predicate);

    @Override
    IndexedSeq<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <U> IndexedSeq<U> unit(Iterable<? extends U> iterable);

    @Override
    <T1, T2> Tuple2<? extends IndexedSeq<T1>, ? extends IndexedSeq<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <T1, T2, T3> Tuple3<? extends IndexedSeq<T1>, ? extends IndexedSeq<T2>, ? extends IndexedSeq<T3>> unzip3(Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper);

    @Override
    IndexedSeq<T> update(int index, T element);

    @Override
    <U> IndexedSeq<Tuple2<T, U>> zip(Iterable<U> that);

    @Override
    <U> IndexedSeq<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem);

    @Override
    IndexedSeq<Tuple2<T, Long>> zipWithIndex();

    @SuppressWarnings("unchecked")
    default int search(T key) {
        Function1<Integer, Integer> midToCmp = mid -> {
            Comparable<? super T> midVal = (Comparable<? super T>) get(mid);
            return midVal.compareTo(key);
        };
        return IndexedSeqModule.Search.binarySearch(this, midToCmp);
    }

    default int search(T key, Comparator<? super T> comparator) {
        Function1<Integer, Integer> midToCmp = mid -> {
            T midVal = get(mid);
            return comparator.compare(midVal, key);
        };
        return IndexedSeqModule.Search.binarySearch(this, midToCmp);
    }

}

interface IndexedSeqModule {
    interface LastIndexOfSlice {
        static <T> int lastIndexOfSlice(IndexedSeq<T> t, IndexedSeq<T> slice, int end) {
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
            int p = 0;
            int result = -1;
            final int maxPtr = t.length() - slice.length();
            while (p <= maxPtr) {
                int r = findSlice(t, p, maxPtr, slice);
                if (r < 0) {
                    return result;
                }
                if (r <= end) {
                    result = r;
                    p = r + 1;
                } else {
                    return result;
                }
            }
            return result;
        }

        static <T> int findSlice(IndexedSeq<T> t, int p, int maxPtr, IndexedSeq<T> slice) {
            while (p <= maxPtr) {
                if (t.startsWith(slice, p)) {
                    return p;
                }
                p++;
            }
            return -1;
        }
    }
    interface Search {
        static <T> int binarySearch(IndexedSeq<T> t, Function1<Integer, Integer> midToCmp) {
            int low = 0;
            int high = t.size() - 1;

            while (low <= high) {
                int mid = (low + high) >>> 1;
                int cmp = midToCmp.apply(mid);

                if (cmp < 0)
                    low = mid + 1;
                else if (cmp > 0)
                    high = mid - 1;
                else
                    return mid; // key found
            }
            return -(low + 1);  // key not found
        }
    }
}