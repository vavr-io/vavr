/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.collection;

import io.vavr.PartialFunction;
import io.vavr.Tuple3;
import io.vavr.Tuple2;
import io.vavr.control.Option;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;

/**
 * Interface for immutable, indexed sequences.
 * <p>
 * Efficient random access is characteristic for indexed sequences.
 *
 * @param <T> component type
 * @author Daniel Dietrich
 */
public interface IndexedSeq<T> extends Seq<T> {

    long serialVersionUID = 1L;

    /**
     * Narrows a widened {@code IndexedSeq<? extends T>} to {@code IndexedSeq<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
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

    @GwtIncompatible
    @Override
    IndexedSeq<T> asJava(Consumer<? super java.util.List<T>> action);

    @GwtIncompatible
    @Override
    IndexedSeq<T> asJavaMutable(Consumer<? super java.util.List<T>> action);

    @Override
    <R> IndexedSeq<R> collect(PartialFunction<? super T, ? extends R> partialFunction);

    @Override
    IndexedSeq<? extends IndexedSeq<T>> combinations();

    @Override
    IndexedSeq<? extends IndexedSeq<T>> combinations(int k);

    @Override
    Iterator<? extends IndexedSeq<T>> crossProduct(int power);

    @Override
    IndexedSeq<T> distinct();

    @Override
    IndexedSeq<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U> IndexedSeq<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    IndexedSeq<T> drop(int n);

    @Override
    IndexedSeq<T> dropUntil(Predicate<? super T> predicate);

    @Override
    IndexedSeq<T> dropWhile(Predicate<? super T> predicate);

    @Override
    IndexedSeq<T> dropRight(int n);

    @Override
    IndexedSeq<T> dropRightUntil(Predicate<? super T> predicate);

    @Override
    IndexedSeq<T> dropRightWhile(Predicate<? super T> predicate);

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
    Iterator<? extends IndexedSeq<T>> grouped(int size);

    @Override
    default int indexOfSlice(Iterable<? extends T> that, int from) {
        Objects.requireNonNull(that, "that is null");
        return IndexedSeqModule.Slice.indexOfSlice(this, that, from);
    }

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
        Objects.requireNonNull(that, "that is null");
        return IndexedSeqModule.Slice.lastIndexOfSlice(this, that, end);
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
    IndexedSeq<T> orElse(Iterable<? extends T> other);

    @Override
    IndexedSeq<T> orElse(Supplier<? extends Iterable<? extends T>> supplier);

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
    IndexedSeq<T> removeAll(Predicate<? super T> predicate);

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
    IndexedSeq<T> shuffle();

    @Override
    IndexedSeq<T> slice(int beginIndex, int endIndex);

    @Override
    Iterator<? extends IndexedSeq<T>> slideBy(Function<? super T, ?> classifier);

    @Override
    Iterator<? extends IndexedSeq<T>> sliding(int size);

    @Override
    Iterator<? extends IndexedSeq<T>> sliding(int size, int step);

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
        if (offset < 0) { return false; }
        if (that instanceof IndexedSeq) {
            IndexedSeq<? extends T> thatIndexedSeq = (IndexedSeq<? extends T>) that;
            int i = offset;
            int j = 0;
            int thisLen = length();
            int thatLen = thatIndexedSeq.length();
            while (i < thisLen && j < thatLen && Objects.equals(this.get(i), thatIndexedSeq.get(j))) {
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
    IndexedSeq<T> take(int n);

    @Override
    IndexedSeq<T> takeRight(int n);

    @Override
    IndexedSeq<T> takeUntil(Predicate<? super T> predicate);

    @Override
    IndexedSeq<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <T1, T2> Tuple2<? extends IndexedSeq<T1>, ? extends IndexedSeq<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <T1, T2, T3> Tuple3<? extends IndexedSeq<T1>, ? extends IndexedSeq<T2>, ? extends IndexedSeq<T3>> unzip3(Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper);

    @Override
    IndexedSeq<T> update(int index, T element);

    @Override
    IndexedSeq<T> update(int index, Function<? super T, ? extends T> updater);

    @Override
    <U> IndexedSeq<Tuple2<T, U>> zip(Iterable<? extends U> that);

    @Override
    <U, R> IndexedSeq<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper);

    @Override
    <U> IndexedSeq<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem);

    @Override
    IndexedSeq<Tuple2<T, Integer>> zipWithIndex();

    @Override
    <U> IndexedSeq<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper);

    /**
     * Searches this sequence for a specific element using a binary search. The sequence must already be sorted into
     * ascending natural order. If it is not sorted, the results are undefined.
     *
     * @param element the element to find
     * @return the index of the search element, if it is contained in the sequence;
     * otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The
     * <i>insertion point</i> is defined as the point at which the
     * element would be inserted into the sequence. Note that this guarantees that
     * the return value will be &gt;= 0 if and only if the element is found.
     * @throws ClassCastException if T cannot be cast to {@code Comparable<? super T>}
     */
    @Override
    @SuppressWarnings("unchecked")
    default int search(T element) {
        IntUnaryOperator comparison = midIndex -> {
            Comparable<? super T> midVal = (Comparable<? super T>) get(midIndex);
            return midVal.compareTo(element);
        };
        return IndexedSeqModule.Search.binarySearch(this, comparison);
    }

    /**
     * Searches this sequence for a specific element using a binary search. The sequence must already be sorted into
     * ascending order according to the specified comparator. If it is not sorted, the results are undefined.
     *
     * @param element    the element to find
     * @param comparator the comparator by which this sequence is ordered
     * @return the index of the search element, if it is contained in the sequence;
     * otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The
     * <i>insertion point</i> is defined as the point at which the
     * element would be inserted into the sequence. Note that this guarantees that
     * the return value will be &gt;= 0 if and only if the element is found.
     */
    @Override
    default int search(T element, Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        IntUnaryOperator comparison = midIndex -> {
            T midVal = get(midIndex);
            return comparator.compare(midVal, element);
        };
        return IndexedSeqModule.Search.binarySearch(this, comparison);
    }

}

interface IndexedSeqModule {

    class Slice {

        static <T> int indexOfSlice(IndexedSeq<T> source, Iterable<? extends T> slice, int from) {
            if (source.isEmpty()) {
                return from == 0 && Collections.isEmpty(slice) ? 0 : -1;
            }
            final IndexedSeq<T> _slice = toIndexedSeq(slice);
            final int maxIndex = source.length() - _slice.length();
            return findSlice(source, _slice, Math.max(from, 0), maxIndex);
        }

        static <T> int lastIndexOfSlice(IndexedSeq<T> source, Iterable<? extends T> slice, int end) {
            if (end < 0) {
                return -1;
            } else if (source.isEmpty()) {
                return Collections.isEmpty(slice) ? 0 : -1;
            } else if (Collections.isEmpty(slice)) {
                final int len = source.length();
                return len < end ? len : end;
            }
            int index = 0;
            int result = -1;
            final IndexedSeq<T> _slice = toIndexedSeq(slice);
            final int maxIndex = source.length() - _slice.length();
            while (index <= maxIndex) {
                int indexOfSlice = findSlice(source, _slice, index, maxIndex);
                if (indexOfSlice < 0) {
                    return result;
                }
                if (indexOfSlice <= end) {
                    result = indexOfSlice;
                    index = indexOfSlice + 1;
                } else {
                    return result;
                }
            }
            return result;
        }

        private static <T> int findSlice(IndexedSeq<T> source, IndexedSeq<T> slice, int index, int maxIndex) {
            while (index <= maxIndex) {
                if (source.startsWith(slice, index)) {
                    return index;
                }
                index++;
            }
            return -1;
        }

        @SuppressWarnings("unchecked")
        private static <T> IndexedSeq<T> toIndexedSeq(Iterable<? extends T> iterable) {
            return (iterable instanceof IndexedSeq) ? (IndexedSeq<T>) iterable : Vector.ofAll(iterable);
        }
    }

    interface Search {
        
        static <T> int binarySearch(IndexedSeq<T> seq, IntUnaryOperator comparison) {
            int low = 0;
            int high = seq.size() - 1;
            while (low <= high) {
                final int mid = (low + high) >>> 1;
                final int cmp = comparison.applyAsInt(mid);
                if (cmp < 0) {
                    low = mid + 1;
                } else if (cmp > 0) {
                    high = mid - 1;
                } else {
                    return mid;
                }
            }
            return -(low + 1);
        }
    }
}
