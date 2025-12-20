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

import io.vavr.Function1;
import io.vavr.PartialFunction;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.control.Option;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import org.jspecify.annotations.NonNull;

/**
 * An immutable {@code BitSet} implementation.
 *
 * @param <T> Component type
 * @author Ruslan Sennov
 */
public interface BitSet<T> extends SortedSet<T> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Builder of the BitSet. Encapsulates the conversion functions for T.
     *
     * @param <T> Component type
     */
    class Builder<T> {

        final static Builder<Integer> DEFAULT = new Builder<>(i -> i, i -> i);

        final Function1<Integer, T> fromInt;
        final Function1<T, Integer> toInt;

        Builder(Function1<Integer, T> fromInt, Function1<T, Integer> toInt) {
            this.fromInt = fromInt;
            this.toInt = toInt;
        }

        /**
         * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
         * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain an {@link ArrayList}.
         *
         * @return A {@link ArrayList} Collector.
         */
        public Collector<T, ArrayList<T>, BitSet<T>> collector() {
            final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
                left.addAll(right);
                return left;
            };
            return Collector.of(ArrayList::new, ArrayList::add, combiner, this::ofAll);
        }

        /**
         * Returns a new empty BitSet
         *
         * @return new empty BitSet
         */
        public BitSet<T> empty() {
            return new BitSetModule.BitSet1<>(fromInt, toInt, 0L);
        }

        /**
         * Builds a new BitSet from a specific value t
         *
         * @param t value to build the BitSet from
         * @return new BitSet
         */
        public BitSet<T> of(T t) {
            final int value = toInt.apply(t);
            final int index = value >> BitSetModule.ADDRESS_BITS_PER_WORD;
            final int bit = value & (BitSetModule.BITS_PER_WORD - 1);

            switch (index) {
                case 0:
                    return new BitSetModule.BitSet1<>(fromInt, toInt, 1L << bit);
                case 1:
                    return new BitSetModule.BitSet2<>(fromInt, toInt, 0L, 1L << bit);
                default:
                    return empty().add(t);
            }
        }

        /**
         * Builds a new BitSet from a list of values
         *
         * @param values values to build the BitSet from
         * @return new BitSet
         */
        @SuppressWarnings("varargs")
        @SafeVarargs
        public final BitSet<T> of(T @NonNull ... values) {
            return empty().addAll(Array.wrap(values));
        }

        /**
         * Builds a new BitSet from an {@link Iterable}
         *
         * @param values iterable to build the BitSet from
         * @return new BitSet
         */
        public BitSet<T> ofAll(@NonNull Iterable<? extends T> values) {
            Objects.requireNonNull(values, "values is null");
            return empty().addAll(values);
        }

        /**
         * Builds a new BitSet from a {@link java.util.stream.Stream} of values
         *
         * @param javaStream stream of values to build the BitSet from
         * @return new BitSet
         */
        public BitSet<T> ofAll(java.util.stream.Stream<? extends T> javaStream) {
            Objects.requireNonNull(javaStream, "javaStream is null");
            return empty().addAll(Iterator.ofAll(javaStream.iterator()));
        }

        /**
         * Builds a new BitSet tabulating a value n times
         *
         * @param n number of times to tabulate
         * @param f function to tabulate
         * @return new BitSet
         */
        public BitSet<T> tabulate(int n, @NonNull Function<? super Integer, ? extends T> f) {
            Objects.requireNonNull(f, "f is null");
            return empty().addAll(Collections.tabulate(n, f));
        }

        /**
         * Builds a new BitSet containing n copies of the same value
         *
         * @param n number of times to copy the value
         * @param s value supplier
         * @return new BitSet
         */
        public BitSet<T> fill(int n, @NonNull Supplier<? extends T> s) {
            Objects.requireNonNull(s, "s is null");
            return empty().addAll(Collections.fill(n, s));
        }
    }

    /**
     * Returns a new {@link BitSet} Builder with custom conversion functions between Integer and type T
     *
     * @param <T> Component type
     * @param fromInt function to convert from Integer to T
     * @param toInt function to convert from T to Integer
     * @return a new Builder
     */
    static <T> Builder<T> withRelations(Function1<Integer, T> fromInt, Function1<T, Integer> toInt) {
        return new Builder<>(fromInt, toInt);
    }

    /**
     * Returns a new {@link BitSet} Builder for an enum type
     *
     * @param <T> Enum type
     * @param enumClass the enum class
     * @return a new Builder
     */
    @SuppressWarnings("RedundantTypeArguments")
    static <T extends Enum<T>> Builder<T> withEnum(Class<T> enumClass) {
        return new Builder<>(i -> enumClass.getEnumConstants()[i], Enum<T>::ordinal);
    }

    /**
     * Returns a new {@link BitSet} Builder for type {@link Character}
     *
     * @return a new Builder
     */
    static Builder<Character> withCharacters() {
        return new Builder<>(i -> (char) i.intValue(), c -> (int) c);
    }

    /**
     * Returns new {@link BitSet} Builder for type {@link Byte}
     *
     * @return new Builder
     */
    static Builder<Byte> withBytes() {
        return new Builder<>(Integer::byteValue, Byte::intValue);
    }

    /**
     * Returns new {@link BitSet} Builder for type {@link Long}
     *
     * @return new Builder
     */
    static Builder<Long> withLongs() {
        return new Builder<>(Integer::longValue, Long::intValue);
    }

    /**
     * Returns new {@link BitSet} Builder for type {@link Short}
     *
     * @return new Builder
     */
    static Builder<Short> withShorts() {
        return new Builder<>(Integer::shortValue, Short::intValue);
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link BitSet}.
     *
     * @return A {@link BitSet} Collector.
     */
    static Collector<Integer, ArrayList<Integer>, BitSet<Integer>> collector() {
        return Builder.DEFAULT.collector();
    }

    /**
     * Returns a BitSet containing no elements.
     *
     * @return an empty BitSet
     */
    static BitSet<Integer> empty() {
        return Builder.DEFAULT.empty();
    }

    /**
     * Returns a BitSet containing a single value.
     *
     * @param value A single value
     * @return A BitSet containing the given value
     */
    static BitSet<Integer> of(Integer value) {
        return Builder.DEFAULT.of(value);
    }

    /**
     * Creates a BitSet of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * @param values int values
     * @return A new BitSet of int values
     */
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

    /**
     * Creates a BitSet of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * @param values int values
     * @return A new BitSet of int values
     */
    static BitSet<Integer> ofAll(Iterable<Integer> values) {
        return Builder.DEFAULT.ofAll(values);
    }

    /**
     * Creates a BitSet of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * @param javaStream A java.util.stream.Stream of int values
     * @return A new BitSet of int values
     */
    static BitSet<Integer> ofAll(java.util.stream.Stream<Integer> javaStream) {
        return Builder.DEFAULT.ofAll(javaStream);
    }

    /**
     * Creates a BitSet from boolean values.
     *
     * @param elements boolean values
     * @return A new BitSet of boolean values
     * @throws NullPointerException if elements is null
     */
    static BitSet<Boolean> ofAll(boolean... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return BitSet.withRelations(i -> i != 0, b -> b ? 1 : 0).ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a BitSet from byte values.
     *
     * @param elements byte values
     * @return A new BitSet of byte values
     * @throws NullPointerException if elements is null
     */
    static BitSet<Byte> ofAll(byte... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return BitSet.withBytes().ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a BitSet from char values.
     *
     * @param elements char values
     * @return A new BitSet of char values
     * @throws NullPointerException if elements is null
     */
    static BitSet<Character> ofAll(char... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return BitSet.withCharacters().ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a BitSet from int values.
     *
     * @param elements int values
     * @return A new BitSet of int values
     * @throws NullPointerException if elements is null
     */
    static BitSet<Integer> ofAll(int... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return BitSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a BitSet from long values.
     *
     * @param elements long values
     * @return A new BitSet of long values
     * @throws NullPointerException if elements is null
     */
    static BitSet<Long> ofAll(long... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return BitSet.withLongs().ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a BitSet from short values.
     *
     * @param elements short values
     * @return A new BitSet of short values
     * @throws NullPointerException if elements is null
     */
    static BitSet<Short> ofAll(short... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return BitSet.withShorts().ofAll(Iterator.ofAll(elements));
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

    /**
     * Creates a BitSet of char numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of char values as specified or the empty range if {@code from >= toExclusive}
     */
    static BitSet<Character> range(char from, char toExclusive) {
        return BitSet.withCharacters().ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a BitSet of long numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of long values as specified or the empty range if {@code from >= toExclusive}
     */
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

    /** Creates a BitSet of char numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of char values as specified or the empty range if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static BitSet<Character> rangeBy(char from, char toExclusive, int step) {
        return BitSet.withCharacters().ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /** Creates a BitSet of long numbers starting from {@code from}, extending to {@code toExclusive - 1},
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

    /**
     * Creates a BitSet of char numbers starting from {@code from}, extending to {@code toInclusive}.
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of char values as specified or the empty range if {@code from > toInclusive}
     */
    static BitSet<Character> rangeClosed(char from, char toInclusive) {
        return BitSet.withCharacters().ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a BitSet of long numbers starting from {@code from}, extending to {@code toInclusive}.
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of long values as specified or the empty range if {@code from > toInclusive}
     */
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

    /**
     * Creates a BitSet of char numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of char values as specified or the empty range if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static BitSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return BitSet.withCharacters().ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a BitSet of long numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of long values as specified or the empty range if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static BitSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return BitSet.withLongs().ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    BitSet<T> add(T element);

    @Override
    BitSet<T> addAll(@NonNull Iterable<? extends T> elements);

    @Override
    default <R> SortedSet<R> collect(@NonNull PartialFunction<? super T, ? extends R> partialFunction) {
        Objects.requireNonNull(partialFunction, "partialFunction is null");
        return TreeSet.ofAll(Comparators.naturalComparator(), iterator().collect(partialFunction));
    }

    @Override
    default BitSet<T> diff(@NonNull Set<? extends T> elements) {
        return removeAll(elements);
    }

    @Override
    default BitSet<T> distinct() {
        return this;
    }

    @Override
    BitSet<T> distinctBy(@NonNull Comparator<? super T> comparator);

    @Override
    <U> BitSet<T> distinctBy(@NonNull Function<? super T, ? extends U> keyExtractor);

    @Override
    BitSet<T> drop(int n);

    @Override
    BitSet<T> dropRight(int n);

    @Override
    default BitSet<T> dropUntil(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @Override
    BitSet<T> dropWhile(@NonNull Predicate<? super T> predicate);

    @Override
    BitSet<T> filter(@NonNull Predicate<? super T> predicate);

    @Override
    BitSet<T> reject(@NonNull Predicate<? super T> predicate);

    @Override
    default <U> SortedSet<U> flatMap(@NonNull Comparator<? super U> comparator, Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return TreeSet.ofAll(comparator, iterator().flatMap(mapper));
    }

    @Override
    default <U> SortedSet<U> flatMap(@NonNull Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return flatMap(Comparators.naturalComparator(), mapper);
    }

    @Override
    default <U> U foldRight(U zero, @NonNull BiFunction<? super T, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return iterator().foldRight(zero, f);
    }

    @Override
    <C> Map<C, BitSet<T>> groupBy(@NonNull Function<? super T, ? extends C> classifier);

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

    /**
     * An {@code BitSet}'s value is computed synchronously.
     *
     * @return false
     */
    @Override
    default boolean isAsync() {
        return false;
    }

    @Override
    default boolean isTraversableAgain() {
        return true;
    }

    /**
     * An {@code BitSet}'s value is computed eagerly.
     *
     * @return false
     */
    @Override
    default boolean isLazy() {
        return false;
    }

    @Override
    BitSet<T> intersect(@NonNull Set<? extends T> elements);

    @Override
    default T last() {
        return Collections.last(this);
    }

    @Override
    Tuple2<BitSet<T>, BitSet<T>> partition(@NonNull Predicate<? super T> predicate);

    @Override
    default BitSet<T> peek(@NonNull Consumer<? super T> action) {
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
    default <U> SortedSet<U> map(@NonNull Comparator<? super U> comparator, Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return TreeSet.ofAll(comparator, iterator().map(mapper));
    }

    @Override
    default <U> SortedSet<U> map(@NonNull Function<? super T, ? extends U> mapper) {
        return map(Comparators.naturalComparator(), mapper);
    }

    @Override
    default <U> SortedSet<U> mapTo(U value) {
        return map(ignored -> value);
    }

    @Override
    default SortedSet<Void> mapToVoid() {
        return map(ignored -> null);
    }

    @Override
    BitSet<T> remove(T element);

    @Override
    BitSet<T> removeAll(@NonNull Iterable<? extends T> elements);

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
    default BitSet<T> retainAll(@NonNull Iterable<? extends T> elements) {
        return Collections.retainAll(this, elements);
    }

    @Override
    BitSet<T> scan(T zero, @NonNull BiFunction<? super T, ? super T, ? extends T> operation);

    @Override
    default <U> Set<U> scanLeft(U zero, @NonNull BiFunction<? super U, ? super T, ? extends U> operation) {
        return Collections.scanLeft(this, zero, operation, HashSet::ofAll);
    }

    @Override
    default <U> Set<U> scanRight(U zero, @NonNull BiFunction<? super T, ? super U, ? extends U> operation) {
        return Collections.scanRight(this, zero, operation, HashSet::ofAll);
    }

    @Override
    Iterator<BitSet<T>> slideBy(@NonNull Function<? super T, ?> classifier);

    @Override
    default Iterator<BitSet<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    Iterator<BitSet<T>> sliding(int size, int step);

    @Override
    Tuple2<BitSet<T>, BitSet<T>> span(@NonNull Predicate<? super T> predicate);

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
    default BitSet<T> takeUntil(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @Override
    BitSet<T> takeWhile(@NonNull Predicate<? super T> predicate);

    @Override
    default java.util.SortedSet<T> toJavaSet() {
        return toJavaSet(ignore -> new java.util.TreeSet<>(comparator()));
    }

    @Override
    default BitSet<T> union(@NonNull Set<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return elements.isEmpty() ? this : addAll(elements);
    }

    @Override
    default <T1, T2> Tuple2<TreeSet<T1>, TreeSet<T2>> unzip(
      @NonNull Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip(unzipper).map(i1 -> TreeSet.ofAll(Comparators.naturalComparator(), i1),
                i2 -> TreeSet.ofAll(Comparators.naturalComparator(), i2));
    }

    @Override
    default <T1, T2, T3> Tuple3<TreeSet<T1>, TreeSet<T2>, TreeSet<T3>> unzip3(
      @NonNull Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip3(unzipper).map(
                i1 -> TreeSet.ofAll(Comparators.naturalComparator(), i1),
                i2 -> TreeSet.ofAll(Comparators.naturalComparator(), i2),
                i3 -> TreeSet.ofAll(Comparators.naturalComparator(), i3));
    }

    @Override
    default <U> TreeSet<Tuple2<T, U>> zip(@NonNull Iterable<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        final Comparator<Tuple2<T, U>> tuple2Comparator = Tuple2.comparator(comparator(), Comparators.naturalComparator());
        return TreeSet.ofAll(tuple2Comparator, iterator().zip(that));
    }

    @Override
    default <U, R> TreeSet<R> zipWith(@NonNull Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return TreeSet.ofAll(Comparators.naturalComparator(), iterator().zipWith(that, mapper));
    }

    @Override
    default <U> TreeSet<Tuple2<T, U>> zipAll(@NonNull Iterable<? extends U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        final Comparator<Tuple2<T, U>> tuple2Comparator = Tuple2.comparator(comparator(), Comparators.naturalComparator());
        return TreeSet.ofAll(tuple2Comparator, iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    default TreeSet<Tuple2<T, Integer>> zipWithIndex() {
        final Comparator<? super T> component1Comparator = comparator();
        final Comparator<Tuple2<T, Integer>> tuple2Comparator = (t1, t2) -> component1Comparator.compare(t1._1, t2._1);
        return TreeSet.ofAll(tuple2Comparator, iterator().zipWithIndex());
    }

    @Override
    default <U> TreeSet<U> zipWithIndex(@NonNull BiFunction<? super T, ? super Integer, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return TreeSet.ofAll(Comparators.naturalComparator(), iterator().zipWithIndex(mapper));
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

        @SuppressWarnings("unchecked")
        BitSet<T> createFromAll(Iterable<? extends T> values) {
            return values instanceof BitSet ? (BitSet<T>) values : createEmpty().addAll(values);
        }

        BitSet<T> fromBitMaskNoCopy(long[] elements) {
            switch (elements.length) {
                case 0:
                    return createEmpty();
                case 1:
                    return new BitSet1<>(fromInt, toInt, elements[0]);
                case 2:
                    return new BitSet2<>(fromInt, toInt, elements[0], elements[1]);
                default:
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
        public BitSet<T> distinctBy(@NonNull Comparator<? super T> comparator) {
            Objects.requireNonNull(comparator, "comparator is null");
            return isEmpty() ? this : createFromAll(iterator().distinctBy(comparator));
        }

        @Override
        public <U> BitSet<T> distinctBy(@NonNull Function<? super T, ? extends U> keyExtractor) {
            Objects.requireNonNull(keyExtractor, "keyExtractor is null");
            return isEmpty() ? this : createFromAll(iterator().distinctBy(keyExtractor));
        }

        @Override
        public BitSet<T> drop(int n) {
            if (n <= 0 || isEmpty()) {
                return this;
            } else if (n >= length()) {
                return createEmpty();
            } else {
                return createFromAll(iterator().drop(n));
            }
        }

        @Override
        public BitSet<T> dropRight(int n) {
            if (n <= 0 || isEmpty()) {
                return this;
            } else if (n >= length()) {
                return createEmpty();
            } else {
                return createFromAll(iterator().dropRight(n));
            }
        }

        @Override
        public BitSet<T> dropWhile(@NonNull Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            final BitSet<T> bitSet = createFromAll(iterator().dropWhile(predicate));
            return (bitSet.length() == length()) ? this : bitSet;
        }

        @Override
        public BitSet<T> intersect(@NonNull Set<? extends T> elements) {
            Objects.requireNonNull(elements, "elements is null");
            if (isEmpty()) {
                return this;
            } else if (elements.isEmpty()) {
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

        /**
         * Returns this {@code BitSet} if it is nonempty,
         * otherwise {@code BitSet} created from iterable, using existing bitset properties.
         *
         * @param other An alternative {@code Traversable}
         * @return this {@code BitSet} if it is nonempty,
         * otherwise {@code BitSet} created from iterable, using existing bitset properties.
         */
        @Override
        public BitSet<T> orElse(@NonNull Iterable<? extends T> other) {
            return isEmpty() ? createFromAll(other) : this;
        }

        /**
         * Returns this {@code BitSet} if it is nonempty,
         * otherwise {@code BitSet} created from result of evaluating supplier, using existing bitset properties.
         *
         * @param supplier An alternative {@code Traversable}
         * @return this {@code BitSet} if it is nonempty,
         * otherwise {@code BitSet} created from result of evaluating supplier, using existing bitset properties.
         */
        @Override
        public BitSet<T> orElse(@NonNull Supplier<? extends Iterable<? extends T>> supplier) {
            return isEmpty() ? createFromAll(supplier.get()) : this;
        }

        @Override
        public Iterator<BitSet<T>> slideBy(@NonNull Function<? super T, ?> classifier) {
            return iterator().slideBy(classifier).map(this::createFromAll);
        }

        @Override
        public Iterator<BitSet<T>> sliding(int size, int step) {
            return iterator().sliding(size, step).map(this::createFromAll);
        }

        @Override
        public Tuple2<BitSet<T>, BitSet<T>> span(@NonNull Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return iterator().span(predicate).map(this::createFromAll, this::createFromAll);
        }

        @Override
        public BitSet<T> scan(T zero, @NonNull BiFunction<? super T, ? super T, ? extends T> operation) {
            return Collections.scanLeft(this, zero, operation, this::createFromAll);
        }

        @Override
        public Tuple2<BitSet<T>, BitSet<T>> partition(@NonNull Predicate<? super T> predicate) {
            return Collections.partition(this, this::createFromAll, predicate);
        }

        @Override
        public BitSet<T> filter(@NonNull Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            final BitSet<T> bitSet = createFromAll(iterator().filter(predicate));
            return (bitSet.length() == length()) ? this : bitSet;
        }

        @Override
        public BitSet<T> reject(@NonNull Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            final BitSet<T> bitSet = createFromAll(iterator().reject(predicate));
            return (bitSet.length() == length()) ? this : bitSet;
        }

        @Override
        public <C> Map<C, BitSet<T>> groupBy(@NonNull Function<? super T, ? extends C> classifier) {
            return Collections.groupBy(this, classifier, this::createFromAll);
        }

        @Override
        public Comparator<T> comparator() {
            return Comparator.comparing(toInt);
        }

        @Override
        public BitSet<T> takeWhile(@NonNull Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            final BitSet<T> result = createFromAll(iterator().takeWhile(predicate));
            return (result.length() == length()) ? this : result;
        }

        @Override
        @SuppressWarnings("unchecked")
        public BitSet<T> addAll(@NonNull Iterable<? extends T> elements) {
            final Stream<Integer> source = Stream.ofAll(elements).map(toInt);
            if (source.isEmpty()) {
                return this;
            } else {
                final long[] copy = copyExpand(1 + (source.max().getOrElse(0) >> ADDRESS_BITS_PER_WORD));
                source.forEach(element -> {
                    if (element < 0) {
                        throw new IllegalArgumentException("bitset element must be >= 0");
                    }
                    setElement(copy, element);
                });
                final BitSet<T> bitSet = fromBitMaskNoCopy(copy);
                return (bitSet.length() == length()) ? this : bitSet;
            }
        }

        @Override
        public boolean contains(T t) {
            final int element = toInt.apply(t);
            if (element < 0) {
                throw new IllegalArgumentException("bitset element must be >= 0");
            }
            final int index = element >> ADDRESS_BITS_PER_WORD;
            final int bit = element & (BitSetModule.BITS_PER_WORD - 1);
            return index < getWordsNum() && (getWord(index) & (1L << bit)) != 0;
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
        public @NonNull Iterator<T> iterator() {
            return new BitSetIterator<>(this);
        }

        @Override
        public BitSet<T> take(int n) {
            if (isEmpty() || n >= length()) {
                return this;
            } else if (n <= 0) {
                return createEmpty();
            } else {
                return createFromAll(iterator().take(n));
            }
        }

        @Override
        public BitSet<T> takeRight(int n) {
            if (isEmpty() || n >= length()) {
                return this;
            } else if (n <= 0) {
                return createEmpty();
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
        public BitSet<T> removeAll(@NonNull Iterable<? extends T> elements) {
            if (isEmpty()) {
                return this;
            } else {
                final Stream<Integer> source = Stream.ofAll(elements).map(toInt);
                if (source.isEmpty()) {
                    return this;
                } else {
                    final long[] copy = copyExpand(getWordsNum());
                    source.forEach(element -> unsetElement(copy, element));
                    return fromBitMaskNoCopy(shrink(copy));
                }
            }
        }

        @Override
        public String toString() {
            return mkString(stringPrefix() + "(", ", ", ")");
        }

        @Override
        public boolean equals(Object o) {
            return Collections.equals(this, o);
        }

        @Override
        public int hashCode() {
            return Collections.hashUnordered(this);
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
