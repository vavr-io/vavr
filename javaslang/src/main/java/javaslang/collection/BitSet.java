/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.control.Option;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * An immutable {@code BitSet} implementation.
 *
 * @author Ruslan Sennov
 * @since 2.1.0
 */
public interface BitSet extends SortedSet<Integer> {

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.BitSet}.
     * <p>
     * The natural comparator is used to compare TreeSet elements.
     *
     * @return A javaslang.collection.List Collector.
     */
    static Collector<Integer, ArrayList<Integer>, BitSet> collector() {
        final BinaryOperator<ArrayList<Integer>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        return Collector.of(ArrayList::new, ArrayList::add, combiner, BitSet::ofAll);
    }

    static BitSet empty() {
        return BitSetModule.EMPTY;
    }

    static BitSet of(Integer value) {
        if(value < Long.SIZE) {
            return new BitSetModule.BitSet1(value);
        }
        if(value < 2 * Long.SIZE) {
            return new BitSetModule.BitSet2(0L, value);
        }
        return empty().add(value);
    }

    static BitSet of(Integer... values) {
        if(values.length == 0) {
            return empty();
        }
        if(values.length == 1) {
            return of(values[0]);
        }
        return empty().addAll(Iterator.of(values));
    }

    /**
     * Returns a BitSet containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param n          The number of elements in the BitSet
     * @param f          The Function computing element values
     * @return A BitSet consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} are null
     */
    static BitSet tabulate(int n, Function<Integer, Integer> f) {
        Objects.requireNonNull(f, "f is null");
        return Collections.tabulate(n, f, BitSet.empty(), BitSet::of);
    }

    /**
     * Returns a BitSet containing {@code n} values supplied by a given Supplier {@code s}.
     *
     * @param n          The number of elements in the BitSet
     * @param s          The Supplier computing element values
     * @return A BitSet of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} are null
     */
    static BitSet fill(int n, Supplier<Integer> s) {
        Objects.requireNonNull(s, "s is null");
        return Collections.fill(n, s, BitSet.empty(), BitSet::of);
    }

    static BitSet ofAll(Iterable<Integer> values) {
        Objects.requireNonNull(values, "values is null");
        return Iterator.ofAll(values).foldLeft(BitSet.empty(), BitSet::add);
    }

    /**
     * Creates a BitSet based on the elements of an int array.
     *
     * @param array an int array
     * @return A new BitSet of Integer values
     */
    static BitSet ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return BitSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a BitSet of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or the empty range if {@code from >= toExclusive}
     */
    static BitSet range(int from, int toExclusive) {
        return BitSet.ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a BitSet of int numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or the empty range if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static BitSet rangeBy(int from, int toExclusive, int step) {
        return BitSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a BitSet of int numbers starting from {@code from}, extending to {@code toInclusive}.
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or the empty range if {@code from > toInclusive}
     */
    static BitSet rangeClosed(int from, int toInclusive) {
        return BitSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a BitSet of int numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or the empty range if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static BitSet rangeClosedBy(int from, int toInclusive, int step) {
        return BitSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    BitSet add(Integer element);

    @Override
    default BitSet addAll(Iterable<? extends Integer> elements) {
        BitSet result = this;
        for (Integer element : elements) {
            result = result.add(element);
        }
        return result;
    }

    @Override
    BitSet diff(Set<? extends Integer> elements);

    @Override
    BitSet distinct();

    @Override
    BitSet distinctBy(Comparator<? super Integer> comparator);

    @Override
    <U> BitSet distinctBy(Function<? super Integer, ? extends U> keyExtractor);

    @Override
    BitSet drop(long n);

    @Override
    BitSet dropRight(long n);

    @Override
    BitSet dropUntil(Predicate<? super Integer> predicate);

    @Override
    BitSet dropWhile(Predicate<? super Integer> predicate);

    @Override
    BitSet filter(Predicate<? super Integer> predicate);

    @Override
    <U> SortedSet<U> flatMap(Function<? super Integer, ? extends Iterable<? extends U>> mapper);

    @Override
    <U> U foldRight(U zero, BiFunction<? super Integer, ? super U, ? extends U> f);

    @Override
    <C> Map<C, BitSet> groupBy(Function<? super Integer, ? extends C> classifier);

    @Override
    Iterator<BitSet> grouped(long size);

    @Override
    default boolean hasDefiniteSize() {
        return true;
    }

    @Override
    Integer head();

    @Override
    BitSet init();

    @Override
    Option<BitSet> initOption();

    @Override
    default boolean isTraversableAgain() {
        return true;
    }

    @Override
    Iterator<Integer> iterator();

    @Override
    int length();

    @Override
    BitSet intersect(Set<? extends Integer> elements);

    @Override
    <U> SortedSet<U> map(Function<? super Integer, ? extends U> mapper);

    @Override
    Tuple2<BitSet, BitSet> partition(Predicate<? super Integer> predicate);

    @Override
    BitSet peek(Consumer<? super Integer> action);

    @Override
    default String stringPrefix() {
        return "BitSet";
    }

    @Override
    default Comparator<Integer> comparator() {
        return Integer::compare;
    }

    @Override
    <U> SortedSet<U> flatMap(Comparator<? super U> comparator, Function<? super Integer, ? extends Iterable<? extends U>> mapper);

    @Override
    <U> SortedSet<U> map(Comparator<? super U> comparator, Function<? super Integer, ? extends U> mapper);

    @Override
    BitSet remove(Integer element);

    @Override
    default BitSet removeAll(Iterable<? extends Integer> elements) {
        BitSet result = this;
        for (Integer element : elements) {
            result = result.remove(element);
        }
        return result;
    }

    @Override
    BitSet replace(Integer currentElement, Integer newElement);

    @Override
    BitSet replaceAll(Integer currentElement, Integer newElement);

    @Override
    BitSet retainAll(Iterable<? extends Integer> elements);

    @Override
    BitSet scan(Integer zero, BiFunction<? super Integer, ? super Integer, ? extends Integer> operation);

    @Override
    <U> Set<U> scanLeft(U zero, BiFunction<? super U, ? super Integer, ? extends U> operation);

    @Override
    <U> Set<U> scanRight(U zero, BiFunction<? super Integer, ? super U, ? extends U> operation);

    @Override
    Iterator<BitSet> sliding(long size);

    @Override
    Iterator<BitSet> sliding(long size, long step);

    @Override
    Tuple2<BitSet, BitSet> span(Predicate<? super Integer> predicate);

    @Override
    BitSet tail();

    @Override
    Option<BitSet> tailOption();

    @Override
    BitSet take(long n);

    @Override
    BitSet takeRight(long n);

    @Override
    BitSet takeUntil(Predicate<? super Integer> predicate);

    @Override
    BitSet takeWhile(Predicate<? super Integer> predicate);

    @Override
    java.util.SortedSet<Integer> toJavaSet();

    @Override
    BitSet union(Set<? extends Integer> elements);

    @Override
    <T1, T2> Tuple2<? extends SortedSet<T1>, ? extends SortedSet<T2>> unzip(Function<? super Integer, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <T1, T2, T3> Tuple3<? extends SortedSet<T1>, ? extends SortedSet<T2>, ? extends SortedSet<T3>> unzip3(Function<? super Integer, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper);

    @Override
    <U> SortedSet<Tuple2<Integer, U>> zip(Iterable<? extends U> that);

    @Override
    <U> SortedSet<Tuple2<Integer, U>> zipAll(Iterable<? extends U> that, Integer thisElem, U thatElem);

    @Override
    SortedSet<Tuple2<Integer, Long>> zipWithIndex();
}

interface BitSetModule {

    BitSet EMPTY = new BitSet1(0);

    abstract class AbstractBitSet implements BitSet {

        abstract BitSet updateWord(int index, long word);

        abstract int nWords();

        abstract long word(int index);

        BitSet fromBitMaskNoCopy(long[] elements) {
            final int len = elements.length;
            if (len == 0) {
                return EMPTY;
            }
            if (len == 1) {
                return new BitSet1(elements[0]);
            }
            if (len == 2) {
                return new BitSet2(elements[0], elements[1]);
            }
            return new BitSetN(elements);
        }

        long[] updateArray(long[] elements, int index, long word) {
            int len = elements.length;
            while (len > 0 && (elements[len - 1] == 0L || word == 0L && index == len - 1)) {
                len--;
            }
            int newlen = len;
            if (index >= newlen && word != 0L) {
                newlen = index + 1;
            }
            long[] newelems = new long[newlen];
            System.arraycopy(elements, 0, newelems, 0, len);
            if (index < newlen) {
                newelems[index] = word;
            } else {
                if (word != 0L) {
                    throw new IndexOutOfBoundsException();
                }
            }
            return newelems;
        }

        @Override
        public BitSet add(Integer element) {
            if (element < 0) {
                throw new IllegalArgumentException("bitset element must be >= 0");
            }
            if (contains(element)) {
                return this;
            } else {
                final int index = element >> 6;
                return updateWord(index, word(index) | (1L << element));
            }
        }

        @Override
        public <U> SortedSet<U> flatMap(Comparator<? super U> comparator, Function<? super Integer, ? extends Iterable<? extends U>> mapper) {
            return null;
        }

        @Override
        public <U> SortedSet<U> map(Comparator<? super U> comparator, Function<? super Integer, ? extends U> mapper) {
            return null;
        }

        @Override
        public boolean contains(Integer element) {
            if (element < 0) {
                throw new IllegalArgumentException("bitset element must be >= 0");
            }
            final int index = element >> 6;
            return (word(index) & (1L << element)) != 0;
        }

        @Override
        public BitSet diff(Set<? extends Integer> elements) {
            return null;
        }

        @Override
        public BitSet distinct() {
            return null;
        }

        @Override
        public BitSet distinctBy(Comparator<? super Integer> comparator) {
            return null;
        }

        @Override
        public <U> BitSet distinctBy(Function<? super Integer, ? extends U> keyExtractor) {
            return null;
        }

        @Override
        public BitSet drop(long n) {
            return null;
        }

        @Override
        public BitSet dropRight(long n) {
            return null;
        }

        @Override
        public BitSet dropUntil(Predicate<? super Integer> predicate) {
            return null;
        }

        @Override
        public BitSet dropWhile(Predicate<? super Integer> predicate) {
            return null;
        }

        @Override
        public BitSet filter(Predicate<? super Integer> predicate) {
            return null;
        }

        @Override
        public <U> SortedSet<U> flatMap(Function<? super Integer, ? extends Iterable<? extends U>> mapper) {
            return null;
        }

        @Override
        public <U> U foldRight(U zero, BiFunction<? super Integer, ? super U, ? extends U> f) {
            return null;
        }

        @Override
        public <C> Map<C, BitSet> groupBy(Function<? super Integer, ? extends C> classifier) {
            return null;
        }

        @Override
        public Iterator<BitSet> grouped(long size) {
            return null;
        }

        @Override
        public Integer head() {
            return null;
        }

        @Override
        public BitSet init() {
            return null;
        }

        @Override
        public Option<BitSet> initOption() {
            return null;
        }

        @Override
        public Iterator<Integer> iterator() {
            return null;
        }

        @Override
        public int length() {
            return 0;
        }

        @Override
        public BitSet intersect(Set<? extends Integer> elements) {
            return null;
        }

        @Override
        public <U> SortedSet<U> map(Function<? super Integer, ? extends U> mapper) {
            return null;
        }

        @Override
        public Tuple2<BitSet, BitSet> partition(Predicate<? super Integer> predicate) {
            return null;
        }

        @Override
        public BitSet peek(Consumer<? super Integer> action) {
            return null;
        }

        @Override
        public BitSet remove(Integer element) {
            if (element < 0) {
                throw new IllegalArgumentException("bitset element must be >= 0");
            }
            if (contains(element)) {
                final int idx = element >> 6;
                return updateWord(idx, word(idx) & ~(1L << element));
            } else {
                return this;
            }
        }

        @Override
        public BitSet replace(Integer currentElement, Integer newElement) {
            return null;
        }

        @Override
        public BitSet replaceAll(Integer currentElement, Integer newElement) {
            return null;
        }

        @Override
        public BitSet retainAll(Iterable<? extends Integer> elements) {
            return null;
        }

        @Override
        public BitSet scan(Integer zero, BiFunction<? super Integer, ? super Integer, ? extends Integer> operation) {
            return null;
        }

        @Override
        public <U> Set<U> scanLeft(U zero, BiFunction<? super U, ? super Integer, ? extends U> operation) {
            return null;
        }

        @Override
        public <U> Set<U> scanRight(U zero, BiFunction<? super Integer, ? super U, ? extends U> operation) {
            return null;
        }

        @Override
        public Iterator<BitSet> sliding(long size) {
            return null;
        }

        @Override
        public Iterator<BitSet> sliding(long size, long step) {
            return null;
        }

        @Override
        public Tuple2<BitSet, BitSet> span(Predicate<? super Integer> predicate) {
            return null;
        }

        @Override
        public BitSet tail() {
            return null;
        }

        @Override
        public Option<BitSet> tailOption() {
            return null;
        }

        @Override
        public BitSet take(long n) {
            return null;
        }

        @Override
        public BitSet takeRight(long n) {
            return null;
        }

        @Override
        public BitSet takeUntil(Predicate<? super Integer> predicate) {
            return null;
        }

        @Override
        public BitSet takeWhile(Predicate<? super Integer> predicate) {
            return null;
        }

        @Override
        public java.util.SortedSet<Integer> toJavaSet() {
            return null;
        }

        @Override
        public BitSet union(Set<? extends Integer> elements) {
            return null;
        }

        @Override
        public <T1, T2> Tuple2<? extends SortedSet<T1>, ? extends SortedSet<T2>> unzip(Function<? super Integer, Tuple2<? extends T1, ? extends T2>> unzipper) {
            return null;
        }

        @Override
        public <T1, T2, T3> Tuple3<? extends SortedSet<T1>, ? extends SortedSet<T2>, ? extends SortedSet<T3>> unzip3(Function<? super Integer, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
            return null;
        }

        @Override
        public <U> SortedSet<Tuple2<Integer, U>> zip(Iterable<? extends U> that) {
            return null;
        }

        @Override
        public <U> SortedSet<Tuple2<Integer, U>> zipAll(Iterable<? extends U> that, Integer thisElem, U thatElem) {
            return null;
        }

        @Override
        public SortedSet<Tuple2<Integer, Long>> zipWithIndex() {
            return null;
        }
    }

    class BitSet1 extends AbstractBitSet {

        private final long elements;

        BitSet1(long elements) {
            this.elements = elements;
        }

        @Override
        BitSet updateWord(int index, long word) {
            if (index == 0) {
                return new BitSet1(word);
            }
            if (index == 1) {
                return new BitSet2(elements, word);
            }
            return fromBitMaskNoCopy(updateArray(new long[] { elements }, index, word));
        }

        @Override
        int nWords() {
            return 1;
        }

        @Override
        long word(int index) {
            return index < 1 ? elements : 0L;
        }
    }

    class BitSet2 extends AbstractBitSet {

        private final long elements1, elements2;

        BitSet2(long elements1, long elements2) {
            this.elements1 = elements1;
            this.elements2 = elements2;
        }

        @Override
        BitSet updateWord(int index, long word) {
            if (index == 0) {
                return new BitSet2(word, elements1);
            }
            if (index == 1) {
                return new BitSet2(elements1, word);
            }
            return fromBitMaskNoCopy(updateArray(new long[] { elements1, elements2 }, index, word));
        }

        @Override
        int nWords() {
            return 2;
        }

        @Override
        long word(int index) {
            return index == 0 ? elements1 : index == 1 ? elements2 : 0L;
        }
    }

    class BitSetN extends AbstractBitSet {

        private final long[] elements;

        BitSetN(long[] elements) {
            this.elements = elements;
        }

        @Override
        BitSet updateWord(int index, long word) {
            return fromBitMaskNoCopy(updateArray(elements, index, word));
        }

        @Override
        int nWords() {
            return elements.length;
        }

        @Override
        long word(int index) {
            return index < elements.length ? elements[index] : 0L;
        }
    }
}