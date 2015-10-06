/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Serializables;
import javaslang.control.Success;
import javaslang.control.Try;
import org.junit.Test;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.stream.Collector;

public class StreamTest extends AbstractSeqTest {

    // -- construction

    @Override
    protected <T> Collector<T, ArrayList<T>, Stream<T>> collector() {
        return Stream.collector();
    }

    @Override
    protected <T> Stream<T> empty() {
        return Stream.empty();
    }

    @Override
    protected <T> Stream<T> of(T element) {
        return Stream.of(element);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Stream<T> of(T... elements) {
        return Stream.of(elements);
    }

    @Override
    protected <T> Stream<T> ofAll(java.lang.Iterable<? extends T> elements) {
        return Stream.ofAll(elements);
    }

    @Override
    protected Stream<Boolean> ofAll(boolean[] array) {
        return Stream.ofAll(array);
    }

    @Override
    protected Stream<Byte> ofAll(byte[] array) {
        return Stream.ofAll(array);
    }

    @Override
    protected Stream<Character> ofAll(char[] array) {
        return Stream.ofAll(array);
    }

    @Override
    protected Stream<Double> ofAll(double[] array) {
        return Stream.ofAll(array);
    }

    @Override
    protected Stream<Float> ofAll(float[] array) {
        return Stream.ofAll(array);
    }

    @Override
    protected Stream<Integer> ofAll(int[] array) {
        return Stream.ofAll(array);
    }

    @Override
    protected Stream<Long> ofAll(long[] array) {
        return Stream.ofAll(array);
    }

    @Override
    protected Stream<Short> ofAll(short[] array) {
        return Stream.ofAll(array);
    }

    @Override
    protected Stream<Character> range(char from, char toExclusive) {
        return Stream.range(from, toExclusive);
    }

    @Override
    protected Stream<Character> rangeBy(char from, char toExclusive, int step) {
        return Stream.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Stream<Double> rangeBy(double from, double toExclusive, double step) {
        return Stream.rangeBy(from, toExclusive, step);
    }

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
    protected Stream<Character> rangeClosed(char from, char toInclusive) {
        return Stream.rangeClosed(from, toInclusive);
    }

    @Override
    protected Stream<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return Stream.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Stream<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return Stream.rangeClosedBy(from, toInclusive, step);
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

    // -- static from(long)

    @Test
    public void shouldGenerateLongStream() {
        assertThat(Stream.from(-1L).take(3)).isEqualTo(Stream.of(-1L, 0L, 1L));
    }

    @Test
    public void shouldGenerateTerminatingLongStream() {
        //noinspection NumericOverflow
        assertThat(Stream.from(Long.MAX_VALUE).take(2)).isEqualTo(Stream.of(Long.MAX_VALUE, Long.MAX_VALUE + 1));
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

    // -- flatMap

    @Test
    public void shouldFlatMapInfiniteTraversable() {
        assertThat(Stream.gen(1, i -> i + 1).flatMap(i -> List.of(i, 2 * i)).take(7)).isEqualTo(Stream.of(1, 2, 2, 4, 3, 6, 4));
    }

    // -- peek

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 3;
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
    public void shouldRecurrentlyCalculatePrimes() {
        assertThat(Stream.of(2).appendSelf(self -> Stream.gen(3, i -> i + 2).filter(i -> self.takeWhile(j -> j * j <= i).forAll(k -> i % k > 0))).take(10)).isEqualTo(Stream.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29));
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

    // -- containsSlice

    @Test
    public void shouldRecognizeInfiniteDoesContainSlice() {
        final boolean actual = Stream.gen(1, i -> i + 1).containsSlice(of(12, 13, 14));
        assertThat(actual).isTrue();
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
        Serializables.callReadObject(Stream.cons(1, Stream::empty));
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

    @Override
    boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Test // See #327, #594
    public void shouldNotEvaluateHeadOfTailWhenCallingIteratorHasNext() {

        final StringBuilder result1 = new StringBuilder();
        final List<Integer> vals1 = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        flatTryWithJavaslangStream(vals1, i -> doStuff(i, result1));

        final StringBuilder result2 = new StringBuilder();
        final Integer[] vals2 = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        flatTryWithJavaStream(vals2, i -> doStuff(i, result2));

        assertThat(result1.toString()).isEqualTo(result2.toString());
    }

    private <T> Try<Void> flatTryWithJavaslangStream(List<T> vals, Try.CheckedConsumer<T> func) {
        return vals.toStream()
                .map(v -> Try.run(() -> func.accept(v)))
                .findFirst(Try::isFailure)
                .orElseGet(() -> new Success<>(null));
    }

    private <T> Try<Void> flatTryWithJavaStream(Integer[] vals, Try.CheckedConsumer<Integer> func) {
        return java.util.stream.Stream.of(vals)
                .map(v -> Try.run(() -> func.accept(v)))
                .filter(Try::isFailure)
                .findFirst()
                .orElseGet(() -> new Success<>(null));
    }

    private String doStuff(int i, StringBuilder builder) throws Exception {
        builder.append(i);
        if (i == 5) {
            throw new Exception("Some error !!!");
        }
        return i + " Value";
    }
}
