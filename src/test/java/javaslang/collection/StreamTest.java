/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Serializables;
import javaslang.collection.Stream.Cons;
import javaslang.collection.Stream.Nil;
import org.junit.Test;

import java.io.InvalidObjectException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class StreamTest extends AbstractSeqTest {

    // -- construction

    @Override
    protected <T> Stream<T> empty() {
        return Stream.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Stream<T> of(T... elements) {
        return Stream.of(elements);
    }

    // -- range

    @Override
    protected Stream<Integer> range(int from, int toExclusive) {
        return Stream.range(from, toExclusive);
    }

    @Override
    protected Stream<Integer> rangeBy(int from, int toExclusive, int step) {
        return Stream.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Stream<Long> range(long from, long toExclusive) {
        return Stream.range(from, toExclusive);
    }

    @Override
    protected Stream<Long> rangeBy(long from, long toExclusive, long step) {
        return Stream.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Stream<Integer> rangeClosed(int from, int toInclusive) {
        return Stream.rangeClosed(from, toInclusive);
    }

    @Override
    protected Stream<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return Stream.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Stream<Long> rangeClosed(long from, long toInclusive) {
        return Stream.rangeClosed(from, toInclusive);
    }

    @Override
    protected Stream<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return Stream.rangeClosedBy(from, toInclusive, step);
    }

    // -- static collector()

    @Test
    public void shouldStreamAndCollectNil() {
        final Stream<?> actual = Stream.empty().toJavaStream().collect(Stream.collector());
        assertThat(actual).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldStreamAndCollectNonNil() {
        final Stream<?> actual = Stream.of(1, 2, 3).toJavaStream().collect(Stream.collector());
        assertThat(actual).isEqualTo(Stream.of(1, 2, 3));
    }

    @Test
    public void shouldParallelStreamAndCollectNil() {
        final Stream<?> actual = Stream.empty().toJavaStream().parallel().collect(Stream.collector());
        assertThat(actual).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldParallelStreamAndCollectNonNil() {
        final Stream<?> actual = Stream.of(1, 2, 3).toJavaStream().parallel().collect(Stream.collector());
        assertThat(actual).isEqualTo(Stream.of(1, 2, 3));
    }

    // -- static from(int)

    @Test
    public void shouldGenerateIntStream() {
        assertThat(Stream.from(-1).take(3)).isEqualTo(Stream.of(-1, 0, 1));
    }

    @Test
    public void shouldGenerateTerminatingIntStream() {
        //noinspection NumericOverflow
        assertThat(Stream.from(Integer.MAX_VALUE).take(2)).isEqualTo(Stream.of(Integer.MAX_VALUE, Integer.MAX_VALUE + 1));
    }

    // -- static gen(Supplier)

    @Test
    public void shouldGenerateInfiniteStreamBasedOnSupplier() {
        assertThat(Stream.gen(() -> 1).take(13).reduce((i, j) -> i + j)).isEqualTo(13);
    }

    // -- static gen(T, Function)

    @Test
    public void shouldGenerateInfiniteStreamBasedOnSupplierWithAccessToPreviousValue() {
        assertThat(Stream.gen(2, (i) -> i + 2).take(3).reduce((i, j) -> i + j)).isEqualTo(12);
    }

    // -- static cons(T, Supplier)

    @Test
    public void shouldBuildStreamBasedOnHeadAndTailSupplierWithAccessToHead() {
        assertThat(Stream.cons(1, () -> Stream.cons(2, Stream::empty))).isEqualTo(Stream.of(1, 2));
    }

    // -- static empty()

    @Test
    public void shouldCreateNil() {
        assertThat(Stream.empty()).isEqualTo(Nil.instance());
    }

    // -- static of()

    @Test
    public void shouldCreateStreamOfStreamUsingCons() {
        assertThat(Stream.of(Stream.empty()).toString()).isEqualTo("Stream(Stream(), ?)");
    }

    // -- static of(T...)

    @Test
    public void shouldCreateStreamOfElements() {
        final Stream<Integer> actual = Stream.of(1, 2);
        final Stream<Integer> expected = new Cons<>(() -> 1, () -> new Cons<>(() -> 2, Nil::instance));
        assertThat(actual).isEqualTo(expected);
    }

    // -- static ofAll(Iterable)

    @Test
    public void shouldCreateStreamOfIterable() {
        final java.util.List<Integer> arrayList = Arrays.asList(1, 2, 3);
        assertThat(Stream.ofAll(arrayList)).isEqualTo(Stream.of(1, 2, 3));
    }

    // -- static ofAll(<primitive array>)

    @Test
    public void shouldCreateStreamOfPrimitiveBooleanArray() {
        final Stream<Boolean> actual = Stream.ofAll(new boolean[] {true, false});
        final Stream<Boolean> expected = Stream.of(true, false);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateStreamOfPrimitiveByteArray() {
        final Stream<Byte> actual = Stream.ofAll(new byte[] {1, 2, 3});
        final Stream<Byte> expected = Stream.of((byte) 1, (byte) 2, (byte) 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateStreamOfPrimitiveCharArray() {
        final Stream<Character> actual = Stream.ofAll(new char[] {'a', 'b', 'c'});
        final Stream<Character> expected = Stream.of('a', 'b', 'c');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateStreamOfPrimitiveDoubleArray() {
        final Stream<Double> actual = Stream.ofAll(new double[] {1d, 2d, 3d});
        final Stream<Double> expected = Stream.of(1d, 2d, 3d);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateStreamOfPrimitiveFloatArray() {
        final Stream<Float> actual = Stream.ofAll(new float[] {1f, 2f, 3f});
        final Stream<Float> expected = Stream.of(1f, 2f, 3f);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateStreamOfPrimitiveIntArray() {
        final Stream<Integer> actual = Stream.ofAll(new int[] {1, 2, 3});
        final Stream<Integer> expected = Stream.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateStreamOfPrimitiveLongArray() {
        final Stream<Long> actual = Stream.ofAll(new long[] {1L, 2L, 3L});
        final Stream<Long> expected = Stream.of(1L, 2L, 3L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateStreamOfPrimitiveShortArray() {
        final Stream<Short> actual = Stream.ofAll(new short[] {(short) 1, (short) 2, (short) 3});
        final Stream<Short> expected = Stream.of((short) 1, (short) 2, (short) 3);
        assertThat(actual).isEqualTo(expected);
    }

    // -- static rangeClosed(int, int)

    @Test
    public void shouldCreateStreamOfRangeWhereFromIsGreaterThanTo() {
        assertThat(Stream.rangeClosed(1, 0)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeClosed(1L, 0L)).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromEqualsTo() {
        assertThat(Stream.rangeClosed(0, 0)).isEqualTo(Stream.of(0));
        assertThat(Stream.rangeClosed(0L, 0L)).isEqualTo(Stream.of(0L));
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromIsLessThanTo() {
        assertThat(Stream.rangeClosed(1, 3)).isEqualTo(Stream.of(1, 2, 3));
        assertThat(Stream.rangeClosed(1L, 3L)).isEqualTo(Stream.of(1L, 2L, 3L));
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromEqualsToEquals_MIN_VALUE() {
        assertThat(Stream.rangeClosed(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(Stream.of(Integer.MIN_VALUE));
        assertThat(Stream.rangeClosed(Long.MIN_VALUE, Long.MIN_VALUE)).isEqualTo(Stream.of(Long.MIN_VALUE));
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromEqualsToEquals_MAX_VALUE() {
        assertThat(Stream.rangeClosed(Integer.MAX_VALUE, Integer.MAX_VALUE)).isEqualTo(Stream.of(Integer.MAX_VALUE));
        assertThat(Stream.rangeClosed(Long.MAX_VALUE, Long.MAX_VALUE)).isEqualTo(Stream.of(Long.MAX_VALUE));
    }

    // -- static rangeClosedBy(int, int, int), rangeClosedBy(long, long, long)

    @Test
    public void shouldCreateStreamOfRangeByWhereFromIsGreaterThanTo() {
        assertThat(Stream.rangeClosedBy(1, 0, 1)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeClosedBy(1, 0, 3)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeClosedBy(0, 1, -1)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeClosedBy(0, 1, -3)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeClosedBy(1L, 0L, 1)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeClosedBy(1L, 0L, 3)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeClosedBy(0L, 1L, -1)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeClosedBy(0L, 1L, -3)).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldCreateStreamOfRangeByWhereFromEqualsTo() {
        assertThat(Stream.rangeClosedBy(0, 0, 1)).isEqualTo(Stream.of(0));
        assertThat(Stream.rangeClosedBy(0, 0, 3)).isEqualTo(Stream.of(0));
        assertThat(Stream.rangeClosedBy(0, 0, -1)).isEqualTo(Stream.of(0));
        assertThat(Stream.rangeClosedBy(0, 0, -3)).isEqualTo(Stream.of(0));
        assertThat(Stream.rangeClosedBy(0L, 0L, 1)).isEqualTo(Stream.of(0L));
        assertThat(Stream.rangeClosedBy(0L, 0L, 3)).isEqualTo(Stream.of(0L));
        assertThat(Stream.rangeClosedBy(0L, 0L, -1)).isEqualTo(Stream.of(0L));
        assertThat(Stream.rangeClosedBy(0L, 0L, -3)).isEqualTo(Stream.of(0L));
    }

    @Test
    public void shouldCreateStreamOfRangeByWhereFromIsLessThanTo() {
        assertThat(Stream.rangeClosedBy(1, 3, 1)).isEqualTo(Stream.of(1, 2, 3));
        assertThat(Stream.rangeClosedBy(1, 5, 2)).isEqualTo(Stream.of(1, 3, 5));
        assertThat(Stream.rangeClosedBy(1, 6, 2)).isEqualTo(Stream.of(1, 3, 5));
        assertThat(Stream.rangeClosedBy(Integer.MAX_VALUE - 2, Integer.MAX_VALUE, 3)).isEqualTo(Stream.of(Integer.MAX_VALUE - 2));
        assertThat(Stream.rangeClosedBy(Integer.MAX_VALUE - 3, Integer.MAX_VALUE, 3)).isEqualTo(Stream.of(Integer.MAX_VALUE - 3, Integer.MAX_VALUE));
        assertThat(Stream.rangeClosedBy(3, 1, -1)).isEqualTo(Stream.of(3, 2, 1));
        assertThat(Stream.rangeClosedBy(5, 1, -2)).isEqualTo(Stream.of(5, 3, 1));
        assertThat(Stream.rangeClosedBy(5, 0, -2)).isEqualTo(Stream.of(5, 3, 1));
        assertThat(Stream.rangeClosedBy(Integer.MIN_VALUE + 2, Integer.MIN_VALUE, -3)).isEqualTo(Stream.of(Integer.MIN_VALUE + 2));
        assertThat(Stream.rangeClosedBy(Integer.MIN_VALUE + 3, Integer.MIN_VALUE, -3)).isEqualTo(Stream.of(Integer.MIN_VALUE + 3, Integer.MIN_VALUE));
        assertThat(Stream.rangeClosedBy(1L, 3L, 1)).isEqualTo(Stream.of(1L, 2L, 3L));
        assertThat(Stream.rangeClosedBy(1L, 5L, 2)).isEqualTo(Stream.of(1L, 3L, 5L));
        assertThat(Stream.rangeClosedBy(1L, 6L, 2)).isEqualTo(Stream.of(1L, 3L, 5L));
        assertThat(Stream.rangeClosedBy(Long.MAX_VALUE - 2, Long.MAX_VALUE, 3)).isEqualTo(Stream.of(Long.MAX_VALUE - 2));
        assertThat(Stream.rangeClosedBy(Long.MAX_VALUE - 3, Long.MAX_VALUE, 3)).isEqualTo(Stream.of(Long.MAX_VALUE - 3, Long.MAX_VALUE));
        assertThat(Stream.rangeClosedBy(3L, 1L, -1)).isEqualTo(Stream.of(3L, 2L, 1L));
        assertThat(Stream.rangeClosedBy(5L, 1L, -2)).isEqualTo(Stream.of(5L, 3L, 1L));
        assertThat(Stream.rangeClosedBy(5L, 0L, -2)).isEqualTo(Stream.of(5L, 3L, 1L));
        assertThat(Stream.rangeClosedBy(Long.MIN_VALUE + 2, Long.MIN_VALUE, -3)).isEqualTo(Stream.of(Long.MIN_VALUE + 2));
        assertThat(Stream.rangeClosedBy(Long.MIN_VALUE + 3, Long.MIN_VALUE, -3)).isEqualTo(Stream.of(Long.MIN_VALUE + 3, Long.MIN_VALUE));
    }

    @Test
    public void shouldCreateStreamOfRangeByWhereFromEqualsToEquals_MIN_VALUE() {
        assertThat(Stream.rangeClosedBy(Integer.MIN_VALUE, Integer.MIN_VALUE, 1)).isEqualTo(Stream.of(Integer.MIN_VALUE));
        assertThat(Stream.rangeClosedBy(Integer.MIN_VALUE, Integer.MIN_VALUE, 3)).isEqualTo(Stream.of(Integer.MIN_VALUE));
        assertThat(Stream.rangeClosedBy(Integer.MIN_VALUE, Integer.MIN_VALUE, -1)).isEqualTo(Stream.of(Integer.MIN_VALUE));
        assertThat(Stream.rangeClosedBy(Integer.MIN_VALUE, Integer.MIN_VALUE, -3)).isEqualTo(Stream.of(Integer.MIN_VALUE));
        assertThat(Stream.rangeClosedBy(Long.MIN_VALUE, Long.MIN_VALUE, 1)).isEqualTo(Stream.of(Long.MIN_VALUE));
        assertThat(Stream.rangeClosedBy(Long.MIN_VALUE, Long.MIN_VALUE, 3)).isEqualTo(Stream.of(Long.MIN_VALUE));
        assertThat(Stream.rangeClosedBy(Long.MIN_VALUE, Long.MIN_VALUE, -1)).isEqualTo(Stream.of(Long.MIN_VALUE));
        assertThat(Stream.rangeClosedBy(Long.MIN_VALUE, Long.MIN_VALUE, -3)).isEqualTo(Stream.of(Long.MIN_VALUE));
    }

    @Test
    public void shouldCreateStreamOfRangeByWhereFromEqualsToEquals_MAX_VALUE() {
        assertThat(Stream.rangeClosedBy(Integer.MAX_VALUE, Integer.MAX_VALUE, 1)).isEqualTo(Stream.of(Integer.MAX_VALUE));
        assertThat(Stream.rangeClosedBy(Integer.MAX_VALUE, Integer.MAX_VALUE, 3)).isEqualTo(Stream.of(Integer.MAX_VALUE));
        assertThat(Stream.rangeClosedBy(Integer.MAX_VALUE, Integer.MAX_VALUE, -1)).isEqualTo(Stream.of(Integer.MAX_VALUE));
        assertThat(Stream.rangeClosedBy(Integer.MAX_VALUE, Integer.MAX_VALUE, -3)).isEqualTo(Stream.of(Integer.MAX_VALUE));
        assertThat(Stream.rangeClosedBy(Long.MAX_VALUE, Long.MAX_VALUE, 1)).isEqualTo(Stream.of(Long.MAX_VALUE));
        assertThat(Stream.rangeClosedBy(Long.MAX_VALUE, Long.MAX_VALUE, 3)).isEqualTo(Stream.of(Long.MAX_VALUE));
        assertThat(Stream.rangeClosedBy(Long.MAX_VALUE, Long.MAX_VALUE, -1)).isEqualTo(Stream.of(Long.MAX_VALUE));
        assertThat(Stream.rangeClosedBy(Long.MAX_VALUE, Long.MAX_VALUE, -3)).isEqualTo(Stream.of(Long.MAX_VALUE));
    }

    // -- static range(int, int), range(long, long)

    @Test
    public void shouldCreateStreamOfUntilWhereFromIsGreaterThanTo() {
        assertThat(Stream.range(1, 0)).isEqualTo(Stream.empty());
        assertThat(Stream.range(1L, 0L)).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsTo() {
        assertThat(Stream.range(0, 0)).isEqualTo(Stream.empty());
        assertThat(Stream.range(0L, 0L)).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromIsLessThanTo() {
        assertThat(Stream.range(1, 3)).isEqualTo(Stream.of(1, 2));
        assertThat(Stream.range(1L, 3L)).isEqualTo(Stream.of(1L, 2L));
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsToEquals_MIN_VALUE() {
        assertThat(Stream.range(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(Stream.empty());
        assertThat(Stream.range(Long.MIN_VALUE, Long.MIN_VALUE)).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsToEquals_MAX_VALUE() {
        assertThat(Stream.range(Integer.MAX_VALUE, Integer.MAX_VALUE)).isEqualTo(Stream.empty());
        assertThat(Stream.range(Long.MAX_VALUE, Long.MAX_VALUE)).isEqualTo(Stream.empty());
    }

    // -- static rangeBy(int, int, int), rangeBy(long, long, long)

    @Test
    public void shouldCreateStreamOfUntilByWhereFromIsGreaterThanTo() {
        assertThat(Stream.rangeBy(1, 0, 1)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(1, 0, 3)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(0, 1, -1)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(0, 1, -3)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(1L, 0L, 1L)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(1L, 0L, 3L)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(0L, 1L, -1L)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(0L, 1L, -3L)).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldCreateStreamOfUntilByWhereFromEqualsTo() {
        assertThat(Stream.rangeBy(0, 0, 1)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(0, 0, 3)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(0, 0, -1)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(0, 0, -3)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(0L, 0L, 1L)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(0L, 0L, 3L)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(0L, 0L, -1L)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(0L, 0L, -3L)).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldCreateStreamOfUntilByWhereFromIsLessThanTo() {
        assertThat(Stream.rangeBy(1, 3, 1)).isEqualTo(Stream.of(1, 2));
        assertThat(Stream.rangeBy(1, 4, 2)).isEqualTo(Stream.of(1, 3));
        assertThat(Stream.rangeBy(3, 1, -1)).isEqualTo(Stream.of(3, 2));
        assertThat(Stream.rangeBy(4, 1, -2)).isEqualTo(Stream.of(4, 2));
        assertThat(Stream.rangeBy(1L, 3L, 1L)).isEqualTo(Stream.of(1L, 2L));
        assertThat(Stream.rangeBy(1L, 4L, 2L)).isEqualTo(Stream.of(1L, 3L));
        assertThat(Stream.rangeBy(3L, 1L, -1L)).isEqualTo(Stream.of(3L, 2L));
        assertThat(Stream.rangeBy(4L, 1L, -2L)).isEqualTo(Stream.of(4L, 2L));
    }

    @Test
    public void shouldCreateStreamOfUntilByWhereFromEqualsToEquals_MIN_VALUE() {
        assertThat(Stream.rangeBy(Integer.MIN_VALUE, Integer.MIN_VALUE, 1)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(Integer.MIN_VALUE, Integer.MIN_VALUE, 3)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(Integer.MIN_VALUE, Integer.MIN_VALUE, -1)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(Integer.MIN_VALUE, Integer.MIN_VALUE, -3)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(Long.MIN_VALUE, Long.MIN_VALUE, 1L)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(Long.MIN_VALUE, Long.MIN_VALUE, 3L)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(Long.MIN_VALUE, Long.MIN_VALUE, -1L)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(Long.MIN_VALUE, Long.MIN_VALUE, -3L)).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldCreateStreamOfUntilByWhereFromEqualsToEquals_MAX_VALUE() {
        assertThat(Stream.rangeBy(Integer.MAX_VALUE, Integer.MAX_VALUE, 1)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(Integer.MAX_VALUE, Integer.MAX_VALUE, 3)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(Integer.MAX_VALUE, Integer.MAX_VALUE, -1)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(Integer.MAX_VALUE, Integer.MAX_VALUE, -3)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(Long.MAX_VALUE, Long.MAX_VALUE, 1L)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(Long.MAX_VALUE, Long.MAX_VALUE, 3L)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(Long.MAX_VALUE, Long.MAX_VALUE, -1L)).isEqualTo(Stream.empty());
        assertThat(Stream.rangeBy(Long.MAX_VALUE, Long.MAX_VALUE, -3L)).isEqualTo(Stream.empty());
    }

    // -- combinations

    @Test
    public void shouldComputeCombinationsOfEmptyStream() {
        assertThat(Stream.empty().combinations()).isEqualTo(Stream.of(Stream.empty()));
    }

    @Test
    public void shouldComputeCombinationsOfNonEmptyStream() {
        assertThat(Stream.of(1, 2, 3).combinations()).isEqualTo(Stream.of(Stream.empty(), Stream.of(1), Stream.of(2), Stream.of(3), Stream.of(1, 2), Stream.of(1, 3), Stream.of(2, 3), Stream.of(1, 2, 3)));
    }

    // -- combinations(k)

    @Test
    public void shouldComputeKCombinationsOfEmptyStream() {
        assertThat(Stream.empty().combinations(1)).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldComputeKCombinationsOfNonEmptyStream() {
        assertThat(Stream.of(1, 2, 3).combinations(2)).isEqualTo(Stream.of(Stream.of(1, 2), Stream.of(1, 3), Stream.of(2, 3)));
    }

    // -- peek

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 3;
    }

    @Test
    public void shouldComputeKCombinationsOfNegativeK() {
        assertThat(Stream.of(1).combinations(-1)).isEqualTo(Stream.of(Stream.empty()));
    }

    // -- permutations

    @Test
    public void shouldComputePermutationsOfEmptyStream() {
        assertThat(Stream.empty().permutations()).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldComputePermutationsOfNonEmptyStream() {
        assertThat(Stream.of(1, 2, 3).permutations()).isEqualTo(Stream.ofAll(Stream.of(Stream.of(1, 2, 3), Stream.of(1, 3, 2), Stream.of(2, 1, 3), Stream.of(2, 3, 1), Stream.of(3, 1, 2), Stream.of(3, 2, 1))));
    }

    // -- addSelf

    @Test
    public void shouldRecurrentlyCalculateFibonacci() {
        assertThat(Stream.of(1, 1).appendSelf(self -> self.zip(self.tail()).map(t -> t._1 + t._2)).take(10)).isEqualTo(Stream.of(1, 1, 2, 3, 5, 8, 13, 21, 34, 55));
    }

    @Test
    public void shouldDoNothingOnNil() {
        assertThat(Stream.empty().appendSelf(self -> self)).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldRecurrentlyCalculateArithmeticProgression() {
        assertThat(Stream.of(1).appendSelf(self -> self.map(t -> t + 1)).take(4)).isEqualTo(Stream.of(1, 2, 3, 4));
    }

    @Test
    public void shouldRecurrentlyCalculateGeometricProgression() {
        assertThat(Stream.of(1).appendSelf(self -> self.map(t -> t * 2)).take(4)).isEqualTo(Stream.of(1, 2, 4, 8));
    }

    @Test
    public void shouldCanStopRecursion() {
        assertThat(Stream.of(1).appendSelf(self -> self.head() < 8 ? self.map(t -> t * 2) : Stream.empty())).isEqualTo(Stream.of(1, 2, 4, 8));
    }

    // -- toString

    @Test
    public void shouldStringifyNil() {
        assertThat(empty().toString()).isEqualTo("Stream()");
    }

    @Test
    public void shouldStringifyNonNil() {
        assertThat(of(1, 2, 3).toString()).isEqualTo("Stream(1, ?)");
    }

    @Test
    public void shouldStringifyNonNilEvaluatingFirstTail() {
        final Stream<Integer> stream = this.of(1, 2, 3);
        stream.tail(); // evaluates second head element
        assertThat(stream.toString()).isEqualTo("Stream(1, 2, ?)");
    }

    // -- Serializable

    @Test(expected = InvalidObjectException.class)
    public void shouldNotSerializeEnclosingClassOfCons() throws Throwable {
        Serializables.callReadObject(new Cons<>(() -> 1, Nil::instance));
    }

    @Test(expected = InvalidObjectException.class)
    public void shouldNotDeserializeStreamWithSizeLessThanOne() throws Throwable {
        try {
            /*
             * This implementation is stable regarding jvm impl changes of object serialization. The index of the
             * number of Stream elements is gathered dynamically.
             */
            final byte[] listWithOneElement = Serializables.serialize(Stream.of(0));
            final byte[] listWithTwoElements = Serializables.serialize(Stream.of(0, 0));
            int index = -1;
            for (int i = 0; i < listWithOneElement.length && index == -1; i++) {
                final byte b1 = listWithOneElement[i];
                final byte b2 = listWithTwoElements[i];
                if (b1 != b2) {
                    if (b1 != 1 || b2 != 2) {
                        throw new IllegalStateException("Difference does not indicate number of elements.");
                    } else {
                        index = i;
                    }
                }
            }
            if (index == -1) {
                throw new IllegalStateException("Hack incomplete - index not found");
            }
            /*
             * Hack the serialized data and fake zero elements.
             */
            listWithOneElement[index] = 0;
            Serializables.deserialize(listWithOneElement);
        } catch (IllegalStateException x) {
            throw (x.getCause() != null) ? x.getCause() : x;
        }
    }
}
