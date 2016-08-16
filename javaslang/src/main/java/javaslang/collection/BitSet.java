/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Function1;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Collector;

import static javaslang.collection.Comparators.naturalComparator;

/**
 * An immutable {@code BitSet} implementation.
 *
 * @author Ruslan Sennov
 * @since 2.1.0
 */
public interface BitSet<T> extends SortedSet<T> {

    long serialVersionUID = 1L;

    class Builder<T> {

        final static Builder<Integer> DEFAULT = new Builder<>(i -> i, i -> i);

        final Function1<Integer, T> fromInt;
        final Function1<T, Integer> toInt;

        Builder(Function1<Integer, T> fromInt, Function1<T, Integer> toInt) {
            this.fromInt = fromInt;
            this.toInt = toInt;
        }

        public Collector<T, ArrayList<T>, BitSet<T>> collector() {
            final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
                left.addAll(right);
                return left;
            };
            return Collector.of(ArrayList::new, ArrayList::add, combiner, this::ofAll);
        }

        public BitSet<T> empty() {
            return new BitSetModule.BitSet1<>(fromInt, toInt, 0L);
        }

        public BitSet<T> of(T t) {
            final int value = toInt.apply(t);
            if (value < BitSetModule.BITS_PER_WORD) {
                return new BitSetModule.BitSet1<>(fromInt, toInt, 1L << value);
            } else if (value < 2 * BitSetModule.BITS_PER_WORD) {
                return new BitSetModule.BitSet2<>(fromInt, toInt, 0L, 1L << value);
            } else {
                return empty().add(t);
            }
        }

        @SuppressWarnings("varargs")
        @SafeVarargs
        public final BitSet<T> of(T... values) {
            return empty().addAll(Array.wrap(values));
        }

        public BitSet<T> ofAll(Iterable<? extends T> values) {
            Objects.requireNonNull(values, "values is null");
            return empty().addAll(values);
        }

        public BitSet<T> ofAll(java.util.stream.Stream<? extends T> javaStream) {
            Objects.requireNonNull(javaStream, "javaStream is null");
            return empty().addAll(Iterator.ofAll(javaStream.iterator()));
        }

        public BitSet<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
            Objects.requireNonNull(f, "f is null");
            return empty().addAll(Collections.tabulate(n, f));
        }

        public BitSet<T> fill(int n, Supplier<? extends T> s) {
            Objects.requireNonNull(s, "s is null");
            return empty().addAll(Collections.fill(n, s));
        }
    }

    static <T> Builder<T> withRelations(Function1<Integer, T> fromInt, Function1<T, Integer> toInt) {
        return new Builder<>(fromInt, toInt);
    }

    @SuppressWarnings("RedundantTypeArguments")
    static <T extends Enum<T>> Builder<T> withEnum(Class<T> enumClass) {
        return new Builder<>(i -> enumClass.getEnumConstants()[i], Enum<T> ::ordinal);
    }

    static Builder<Character> withCharacters() {
        return new Builder<>(i -> (char) i.intValue(), c -> (int) c);
    }

    static Builder<Byte> withBytes() {
        return new Builder<>(Integer::byteValue, Byte::intValue);
    }

    static Builder<Long> withLongs() {
        return new Builder<>(Integer::longValue, Long::intValue);
    }

    static Builder<Short> withShorts() {
        return new Builder<>(Integer::shortValue, Short::intValue);
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.BitSet}.
     *
     * @return A {@link javaslang.collection.BitSet} Collector.
     */
    static Collector<Integer, ArrayList<Integer>, BitSet<Integer>> collector() {
        return Builder.DEFAULT.collector();
    }

    static BitSet<Integer> empty() {
        return Builder.DEFAULT.empty();
    }

    static BitSet<Integer> of(Integer value) {
        return Builder.DEFAULT.of(value);
    }

    static BitSet<Integer> of(Integer... values) {
        return Builder.DEFAULT.of(values);
    }

    /**
     * Returns a BitSet containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param n The number of elements in the BitSet
     * @param f The Function computing element values
     * @return A BitSet consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    static BitSet<Integer> tabulate(int n, Function<Integer, Integer> f) {
        return Builder.DEFAULT.tabulate(n, f);
    }

    /**
     * Returns a BitSet containing {@code n} values supplied by a given Supplier {@code s}.
     *
     * @param n The number of elements in the BitSet
     * @param s The Supplier computing element values
     * @return A BitSet of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    static BitSet<Integer> fill(int n, Supplier<Integer> s) {
        return Builder.DEFAULT.fill(n, s);
    }

    static BitSet<Integer> ofAll(Iterable<Integer> values) {
        return Builder.DEFAULT.ofAll(values);
    }

    static BitSet<Integer> ofAll(java.util.stream.Stream<Integer> javaStream) {
        return Builder.DEFAULT.ofAll(javaStream);
    }

    /**
     * Creates a BitSet based on the elements of a boolean array.
     *
     * @param array a boolean array
     * @return A new BitSet of boolean values
     */
    static BitSet<Boolean> ofAll(boolean[] array) {
        Objects.requireNonNull(array, "array is null");
        return BitSet.withRelations(i -> i != 0, b -> b ? 1 : 0).ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a BitSet based on the elements of a byte array.
     *
     * @param array a byte array
     * @return A new BitSet of byte values
     */
    static BitSet<Byte> ofAll(byte[] array) {
        Objects.requireNonNull(array, "array is null");
        return BitSet.withBytes().ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a BitSet based on the elements of a char array.
     *
     * @param array a char array
     * @return A new BitSet of char values
     */
    static BitSet<Character> ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return BitSet.withCharacters().ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a BitSet based on the elements of an int array.
     *
     * @param array an int array
     * @return A new BitSet of int values
     */
    static BitSet<Integer> ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return BitSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a BitSet based on the elements of a long array.
     *
     * @param array a long array
     * @return A new BitSet of long values
     */
    static BitSet<Long> ofAll(long[] array) {
        Objects.requireNonNull(array, "array is null");
        return BitSet.withLongs().ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a BitSet based on the elements of a short array.
     *
     * @param array a short array
     * @return A new BitSet of short values
     */
    static BitSet<Short> ofAll(short[] array) {
        Objects.requireNonNull(array, "array is null");
        return BitSet.withShorts().ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a BitSet of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or the empty range if {@code from >= toExclusive}
     */
    static BitSet<Integer> range(int from, int toExclusive) {
        return BitSet.ofAll(Iterator.range(from, toExclusive));
    }

    static BitSet<Character> range(char from, char toExclusive) {
        return BitSet.withCharacters().ofAll(Iterator.range(from, toExclusive));
    }

    static BitSet<Long> range(long from, long toExclusive) {
        return BitSet.withLongs().ofAll(Iterator.range(from, toExclusive));
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
    static BitSet<Integer> rangeBy(int from, int toExclusive, int step) {
        return BitSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    static BitSet<Character> rangeBy(char from, char toExclusive, int step) {
        return BitSet.withCharacters().ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    static BitSet<Long> rangeBy(long from, long toExclusive, long step) {
        return BitSet.withLongs().ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a BitSet of int numbers starting from {@code from}, extending to {@code toInclusive}.
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or the empty range if {@code from > toInclusive}
     */
    static BitSet<Integer> rangeClosed(int from, int toInclusive) {
        return BitSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    static BitSet<Character> rangeClosed(char from, char toInclusive) {
        return BitSet.withCharacters().ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    static BitSet<Long> rangeClosed(long from, long toInclusive) {
        return BitSet.withLongs().ofAll(Iterator.rangeClosed(from, toInclusive));
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
    static BitSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return BitSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    static BitSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return BitSet.withCharacters().ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    static BitSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return BitSet.withLongs().ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    BitSet<T> add(T element);

    @Override
    BitSet<T> addAll(Iterable<? extends T> elements);

    @Override
    default BitSet<T> diff(Set<? extends T> elements) {
        return removeAll(elements);
    }

    @Override
    default BitSet<T> distinct() {
        return this;
    }

    @Override
    BitSet<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U> BitSet<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    BitSet<T> drop(int n);

    @Override
    BitSet<T> dropRight(int n);

    @Override
    default BitSet<T> dropUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @Override
    BitSet<T> dropWhile(Predicate<? super T> predicate);

    @Override
    BitSet<T> filter(Predicate<? super T> predicate);

    @Override
    default <U> SortedSet<U> flatMap(Comparator<? super U> comparator, Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return TreeSet.ofAll(comparator, iterator().flatMap(mapper));
    }

    @Override
    default <U> SortedSet<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return flatMap(naturalComparator(), mapper);
    }

    @Override
    default <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return iterator().foldRight(zero, f);
    }

    @Override
    <C> Map<C, BitSet<T>> groupBy(Function<? super T, ? extends C> classifier);

    @Override
    default Iterator<BitSet<T>> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    default boolean hasDefiniteSize() {
        return true;
    }

    @Override
    BitSet<T> init();

    @Override
    default Option<BitSet<T>> initOption() {
        return isEmpty() ? Option.none() : Option.some(init());
    }

    @Override
    default boolean isTraversableAgain() {
        return true;
    }

    @Override
    BitSet<T> intersect(Set<? extends T> elements);

    @Override
    Tuple2<BitSet<T>, BitSet<T>> partition(Predicate<? super T> predicate);

    @Override
    default BitSet<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(head());
        }
        return this;
    }

    @Override
    default String stringPrefix() {
        return "BitSet";
    }

    @Override
    default <U> SortedSet<U> map(Comparator<? super U> comparator, Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return TreeSet.ofAll(comparator, iterator().map(mapper));
    }

    @Override
    default <U> SortedSet<U> map(Function<? super T, ? extends U> mapper) {
        return map(naturalComparator(), mapper);
    }

    @Override
    BitSet<T> remove(T element);

    @Override
    BitSet<T> removeAll(Iterable<? extends T> elements);

    @Override
    default BitSet<T> replace(T currentElement, T newElement) {
        if (contains(currentElement)) {
            return remove(currentElement).add(newElement);
        } else {
            return this;
        }
    }

    @Override
    default BitSet<T> replaceAll(T currentElement, T newElement) {
        // a set has only one occurrence
        return replace(currentElement, newElement);
    }

    @Override
    default BitSet<T> retainAll(Iterable<? extends T> elements) {
        return Collections.retainAll(this, elements);
    }

    @Override
    BitSet<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation);

    @Override
    default <U> Set<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation) {
        return Collections.scanLeft(this, zero, operation, new java.util.ArrayList<>(), (c, u) -> {
            c.add(u);
            return c;
        }, HashSet::ofAll);
    }

    @Override
    default <U> Set<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
        return Collections.scanRight(this, zero, operation, new java.util.ArrayList<>(), (c, u) -> {
            c.add(u);
            return c;
        }, HashSet::ofAll);
    }

    @Override
    default Iterator<BitSet<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    Iterator<BitSet<T>> sliding(int size, int step);

    @Override
    Tuple2<BitSet<T>, BitSet<T>> span(Predicate<? super T> predicate);

    @Override
    default BitSet<T> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty BitSet");
        } else {
            return drop(1);
        }
    }

    @Override
    default Option<BitSet<T>> tailOption() {
        return isEmpty() ? Option.none() : Option.some(tail());
    }

    @Override
    BitSet<T> take(int n);

    @Override
    BitSet<T> takeRight(int n);

    @Override
    default BitSet<T> takeUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @Override
    BitSet<T> takeWhile(Predicate<? super T> predicate);

    @Override
    default java.util.SortedSet<T> toJavaSet() {
        return toJavaSet(ignore -> new java.util.TreeSet<>(comparator()));
    }

    @Override
    default BitSet<T> union(Set<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return addAll(elements);
    }

    @Override
    default <T1, T2> Tuple2<TreeSet<T1>, TreeSet<T2>> unzip(
            Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip(unzipper).map(i1 -> TreeSet.ofAll(naturalComparator(), i1),
                i2 -> TreeSet.ofAll(naturalComparator(), i2));
    }

    @Override
    default <T1, T2, T3> Tuple3<TreeSet<T1>, TreeSet<T2>, TreeSet<T3>> unzip3(
            Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip3(unzipper).map(
                i1 -> TreeSet.ofAll(naturalComparator(), i1),
                i2 -> TreeSet.ofAll(naturalComparator(), i2),
                i3 -> TreeSet.ofAll(naturalComparator(), i3));
    }

    @Override
    default <U> TreeSet<Tuple2<T, U>> zip(Iterable<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        final Comparator<Tuple2<T, U>> tuple2Comparator = Tuple2.comparator(comparator(), naturalComparator());
        return TreeSet.ofAll(tuple2Comparator, iterator().zip(that));
    }

    @Override
    default <U, R> TreeSet<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return TreeSet.ofAll(naturalComparator(), iterator().zipWith(that, mapper));
    }

    @Override
    default <U> TreeSet<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        final Comparator<Tuple2<T, U>> tuple2Comparator = Tuple2.comparator(comparator(), naturalComparator());
        return TreeSet.ofAll(tuple2Comparator, iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    default TreeSet<Tuple2<T, Integer>> zipWithIndex() {
        final Comparator<? super T> component1Comparator = comparator();
        final Comparator<Tuple2<T, Integer>> tuple2Comparator = (t1, t2) -> component1Comparator.compare(t1._1, t2._1);
        return TreeSet.ofAll(tuple2Comparator, iterator().zipWithIndex());
    }

    @Override
    default <U> TreeSet<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return TreeSet.ofAll(naturalComparator(), iterator().zipWithIndex(mapper));
    }
}

interface BitSetModule {

    int ADDRESS_BITS_PER_WORD = 6;
    int BITS_PER_WORD = 64;

    abstract class AbstractBitSet<T> implements BitSet<T>, Serializable {

        private static final long serialVersionUID = 1L;

        final Function1<Integer, T> fromInt;
        final Function1<T, Integer> toInt;

        AbstractBitSet(Function1<Integer, T> fromInt, Function1<T, Integer> toInt) {
            this.fromInt = fromInt;
            this.toInt = toInt;
        }

        abstract int getWordsNum();

        abstract long[] copyExpand(int wordsNum);

        abstract long getWord(int index);

        BitSet<T> createEmpty() {
            return new BitSet1<>(fromInt, toInt, 0L);
        }

        BitSet<T> createFromAll(Iterable<? extends T> values) {
            return createEmpty().addAll(values);
        }

        BitSet<T> fromBitMaskNoCopy(long[] elements) {
            final int len = elements.length;
            if (len == 0) {
                return createEmpty();
            } else if (len == 1) {
                return new BitSet1<>(fromInt, toInt, elements[0]);
            } else if (len == 2) {
                return new BitSet2<>(fromInt, toInt, elements[0], elements[1]);
            } else {
                return new BitSetN<>(fromInt, toInt, elements);
            }
        }

        private void setElement(long[] words, int element) {
            final int index = element >> ADDRESS_BITS_PER_WORD;
            words[index] |= (1L << element);
        }

        private void unsetElement(long[] words, int element) {
            final int index = element >> ADDRESS_BITS_PER_WORD;
            words[index] &= ~(1L << element);
        }

        long[] shrink(long[] elements) {
            int newlen = elements.length;
            while (newlen > 0 && elements[newlen - 1] == 0) {
                newlen--;
            }
            final long[] newelems = new long[newlen];
            System.arraycopy(elements, 0, newelems, 0, newlen);
            return newelems;
        }

        BitSet<T> addElement(int element) {
            final long[] copy = copyExpand(1 + (element >> ADDRESS_BITS_PER_WORD));
            setElement(copy, element);
            return fromBitMaskNoCopy(copy);
        }

        @Override
        public BitSet<T> distinctBy(Comparator<? super T> comparator) {
            Objects.requireNonNull(comparator, "comparator is null");
            return createFromAll(iterator().distinctBy(comparator));
        }

        @Override
        public <U> BitSet<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
            Objects.requireNonNull(keyExtractor, "keyExtractor is null");
            return createFromAll(iterator().distinctBy(keyExtractor));
        }

        @Override
        public BitSet<T> drop(int n) {
            if (n <= 0) {
                return this;
            } else if (n >= length()) {
                return createEmpty();
            } else {
                return createFromAll(iterator().drop(n));
            }
        }

        @Override
        public BitSet<T> dropRight(int n) {
            if (n <= 0) {
                return this;
            } else if (n >= length()) {
                return createEmpty();
            } else {
                return createFromAll(iterator().dropRight(n));
            }
        }

        @Override
        public BitSet<T> dropWhile(Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            final BitSet<T> bitSet = createFromAll(iterator().dropWhile(predicate));
            return (bitSet.length() == length()) ? this : bitSet;
        }

        @Override
        public BitSet<T> intersect(Set<? extends T> elements) {
            Objects.requireNonNull(elements, "elements is null");
            if (isEmpty() || elements.isEmpty()) {
                return createEmpty();
            } else {
                final int size = size();
                if (size <= elements.size()) {
                    return retainAll(elements);
                } else {
                    final BitSet<T> results = createFromAll(elements).retainAll(this);
                    return (size == results.size()) ? this : results;
                }
            }
        }

        @Override
        public Iterator<BitSet<T>> sliding(int size, int step) {
            return iterator().sliding(size, step).map(this::createFromAll);
        }

        @Override
        public Tuple2<BitSet<T>, BitSet<T>> span(Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return iterator().span(predicate).map(this::createFromAll, this::createFromAll);
        }

        @Override
        public BitSet<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation) {
            Objects.requireNonNull(operation, "operation is null");
            return Collections.scanLeft(this, zero, operation, new java.util.ArrayList<>(), (arr, t) -> {
                arr.add(t);
                return arr;
            }, this::createFromAll);
        }

        @Override
        public Tuple2<BitSet<T>, BitSet<T>> partition(Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return iterator().partition(predicate).map(this::createFromAll, this::createFromAll);
        }

        @Override
        public BitSet<T> filter(Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            final BitSet<T> bitSet = createFromAll(iterator().filter(predicate));
            return (bitSet.length() == length()) ? this : bitSet;
        }

        @Override
        public <C> Map<C, BitSet<T>> groupBy(Function<? super T, ? extends C> classifier) {
            return Collections.groupBy(this, classifier, this::createFromAll);
        }

        @Override
        public Comparator<T> comparator() {
            return (Comparator<T> & Serializable) (t1, t2) -> Integer.compare(toInt.apply(t1), toInt.apply(t2));
        }

        @Override
        public BitSet<T> takeWhile(Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            final BitSet<T> result = createFromAll(iterator().takeWhile(predicate));
            return (result.length() == length()) ? this : result;
        }

        @Override
        @SuppressWarnings("unchecked")
        public BitSet<T> addAll(Iterable<? extends T> elements) {
            final Stream<Integer> source = Stream.ofAll(elements).map(toInt);
            final long[] copy = copyExpand(1 + (source.max().getOrElse(0) >> ADDRESS_BITS_PER_WORD));
            source.forEach(element -> {
                if (element < 0) {
                    throw new IllegalArgumentException("bitset element must be >= 0");
                }
                setElement(copy, element);
            });
            return fromBitMaskNoCopy(copy);
        }

        @Override
        public boolean contains(T t) {
            final int element = toInt.apply(t);
            if (element < 0) {
                throw new IllegalArgumentException("bitset element must be >= 0");
            }
            final int index = element >> ADDRESS_BITS_PER_WORD;
            return index < getWordsNum() && (getWord(index) & (1L << element)) != 0;
        }

        @Override
        public BitSet<T> init() {
            if (isEmpty()) {
                throw new UnsupportedOperationException("init of empty TreeSet");
            } else {
                final long last = getWord(getWordsNum() - 1);
                final int element = BITS_PER_WORD * (getWordsNum() - 1) + BITS_PER_WORD - Long.numberOfLeadingZeros(last) - 1;
                return remove(fromInt.apply(element));
            }
        }

        @Override
        public Iterator<T> iterator() {
            return new BitSetIterator<>(this);
        }

        @Override
        public BitSet<T> take(int n) {
            if (n <= 0) {
                return createEmpty();
            } else if (n >= length()) {
                return this;
            } else {
                return createFromAll(iterator().take(n));
            }
        }

        @Override
        public BitSet<T> takeRight(int n) {
            if (n <= 0) {
                return createEmpty();
            } else if (n >= length()) {
                return this;
            } else {
                return createFromAll(iterator().takeRight(n));
            }
        }

        @Override
        public BitSet<T> remove(T t) {
            if (contains(t)) {
                final int element = toInt.apply(t);
                final long[] copy = copyExpand(getWordsNum());
                unsetElement(copy, element);
                return fromBitMaskNoCopy(shrink(copy));
            } else {
                return this;
            }
        }

        @Override
        public BitSet<T> removeAll(Iterable<? extends T> elements) {
            final Stream<Integer> source = Stream.ofAll(elements).map(toInt);
            final long[] copy = copyExpand(getWordsNum());
            source.forEach(element -> unsetElement(copy, element));
            return fromBitMaskNoCopy(shrink(copy));
        }

        @Override
        public String toString() {
            return mkString(stringPrefix() + "(", ", ", ")");
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof BitSet) {
                final BitSet<?> that = (BitSet<?>) o;
                return Collections.areEqual(this, that);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Collections.hash(this);
        }
    }

    class BitSetIterator<T> extends AbstractIterator<T> {

        private final AbstractBitSet<T> bitSet;

        private long element;
        private int index;

        BitSetIterator(AbstractBitSet<T> bitSet) {
            this.bitSet = bitSet;
            this.element = bitSet.getWord(0);
            this.index = 0;
        }

        @Override
        protected T getNext() {
            final int pos = Long.numberOfTrailingZeros(element);
            element &= ~(1L << pos);
            return bitSet.fromInt.apply(pos + (index << ADDRESS_BITS_PER_WORD));
        }

        @Override
        public boolean hasNext() {
            if (element == 0) {
                while (element == 0 && index < bitSet.getWordsNum() - 1) {
                    element = bitSet.getWord(++index);
                }
                return element != 0;
            } else {
                return true;
            }
        }
    }

    class BitSet1<T> extends AbstractBitSet<T> {

        private static final long serialVersionUID = 1L;

        private final long elements;
        private final int len;

        BitSet1(Function1<Integer, T> fromInt, Function1<T, Integer> toInt, long elements) {
            super(fromInt, toInt);
            this.elements = elements;
            this.len = Long.bitCount(elements);
        }

        @Override
        int getWordsNum() {
            return 1;
        }

        @Override
        long[] copyExpand(int wordsNum) {
            if (wordsNum < 1) {
                wordsNum = 1;
            }
            final long[] arr = new long[wordsNum];
            arr[0] = elements;
            return arr;
        }

        @Override
        long getWord(int index) {
            return elements;
        }

        @Override
        public T head() {
            if (elements == 0) {
                throw new NoSuchElementException("head of empty BitSet");
            } else {
                return fromInt.apply(Long.numberOfTrailingZeros(elements));
            }
        }

        @Override
        public int length() {
            return len;
        }

        @Override
        public BitSet<T> add(T t) {
            final int element = toInt.apply(t);
            if (element < 0) {
                throw new IllegalArgumentException("bitset element must be >= 0");
            }
            if (element < BITS_PER_WORD) {
                final long mask = 1L << element;
                if ((elements & mask) != 0) {
                    return this;
                } else {
                    return new BitSet1<>(fromInt, toInt, elements | mask);
                }
            } else {
                return addElement(element);
            }
        }
    }

    class BitSet2<T> extends AbstractBitSet<T> {

        private static final long serialVersionUID = 1L;

        private final long elements1, elements2;
        private final int len;

        BitSet2(Function1<Integer, T> fromInt, Function1<T, Integer> toInt, long elements1, long elements2) {
            super(fromInt, toInt);
            this.elements1 = elements1;
            this.elements2 = elements2;
            this.len = Long.bitCount(elements1) + Long.bitCount(elements2);
        }

        @Override
        int getWordsNum() {
            return 2;
        }

        @Override
        long[] copyExpand(int wordsNum) {
            if (wordsNum < 2) {
                wordsNum = 2;
            }
            final long[] arr = new long[wordsNum];
            arr[0] = elements1;
            arr[1] = elements2;
            return arr;
        }

        @Override
        long getWord(int index) {
            if (index == 0) {
                return elements1;
            } else {
                return elements2;
            }
        }

        @Override
        public T head() {
            if (elements1 == 0) {
                return fromInt.apply(BITS_PER_WORD + Long.numberOfTrailingZeros(elements2));
            } else {
                return fromInt.apply(Long.numberOfTrailingZeros(elements1));
            }
        }

        @Override
        public int length() {
            return len;
        }

        @Override
        public BitSet<T> add(T t) {
            final int element = toInt.apply(t);
            if (element < 0) {
                throw new IllegalArgumentException("bitset element must be >= 0");
            }
            final long mask = 1L << element;
            if (element < BITS_PER_WORD) {
                if ((elements1 & mask) != 0) {
                    return this;
                } else {
                    return new BitSet2<>(fromInt, toInt, elements1 | mask, elements2);
                }
            } else if (element < 2 * BITS_PER_WORD) {
                if ((elements2 & mask) != 0) {
                    return this;
                } else {
                    return new BitSet2<>(fromInt, toInt, elements1, elements2 | mask);
                }
            } else {
                return addElement(element);
            }
        }
    }

    class BitSetN<T> extends AbstractBitSet<T> {

        private static final long serialVersionUID = 1L;

        private final long[] elements;
        private final int len;

        BitSetN(Function1<Integer, T> fromInt, Function1<T, Integer> toInt, long[] elements) {
            super(fromInt, toInt);
            this.elements = elements;
            this.len = calcLength(elements);
        }

        private static int calcLength(long[] elements) {
            int len = 0;
            for (long element : elements) {
                len += Long.bitCount(element);
            }
            return len;
        }

        @Override
        int getWordsNum() {
            return elements.length;
        }

        @Override
        long[] copyExpand(int wordsNum) {
            if (wordsNum < elements.length) {
                wordsNum = elements.length;
            }
            final long[] arr = new long[wordsNum];
            System.arraycopy(elements, 0, arr, 0, elements.length);
            return arr;
        }

        @Override
        long getWord(int index) {
            return elements[index];
        }

        @Override
        public T head() {
            int offset = 0;
            int element = 0;
            for (int i = 0; i < getWordsNum(); i++) {
                if (elements[i] == 0) {
                    offset += BITS_PER_WORD;
                } else {
                    element = offset + Long.numberOfTrailingZeros(elements[i]);
                    break;
                }
            }
            return fromInt.apply(element);
        }

        @Override
        public int length() {
            return len;
        }

        @Override
        public BitSet<T> add(T t) {
            if (contains(t)) {
                return this;
            } else {
                return addElement(toInt.apply(t));
            }
        }
    }
}
