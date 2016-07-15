/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Serializables;
import javaslang.Value;
import javaslang.control.Option;
import javaslang.control.Try;
import javaslang.Tuple2;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InvalidObjectException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class StreamTest extends AbstractLinearSeqTest {

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

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> Stream<T> of(T... elements) {
        return Stream.of(elements);
    }

    @Override
    protected <T> Stream<T> ofAll(Iterable<? extends T> elements) {
        return Stream.ofAll(elements);
    }

    @Override
    protected <T> Stream<T> ofJavaStream(java.util.stream.Stream<? extends T> javaStream) {
        return Stream.ofAll(javaStream);
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
    protected <T> Stream<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        return Stream.tabulate(n, f);
    }

    @Override
    protected <T> Stream<T> fill(int n, Supplier<? extends T> s) {
        return Stream.fill(n, s);
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
    public void shouldGenerateOverflowingIntStream() {
        //noinspection NumericOverflow
        assertThat(Stream.from(Integer.MAX_VALUE).take(2))
                .isEqualTo(Stream.of(Integer.MAX_VALUE, Integer.MAX_VALUE + 1));
    }

    // -- static from(int, int)

    @Test
    public void shouldGenerateIntStreamWithStep() {
        assertThat(Stream.from(-1, 6).take(3)).isEqualTo(Stream.of(-1, 5, 11));
    }

    @Test
    public void shouldGenerateOverflowingIntStreamWithStep() {
        //noinspection NumericOverflow
        assertThat(Stream.from(Integer.MAX_VALUE, 2).take(2))
                .isEqualTo(Stream.of(Integer.MAX_VALUE, Integer.MAX_VALUE + 2));
    }

    // -- static from(long)

    @Test
    public void shouldGenerateLongStream() {
        assertThat(Stream.from(-1L).take(3)).isEqualTo(Stream.of(-1L, 0L, 1L));
    }

    @Test
    public void shouldGenerateOverflowingLongStream() {
        //noinspection NumericOverflow
        assertThat(Stream.from(Long.MAX_VALUE).take(2))
                .isEqualTo(Stream.of(Long.MAX_VALUE, Long.MAX_VALUE + 1));
    }

    // -- static from(long, long)

    @Test
    public void shouldGenerateLongStreamWithStep() {
        assertThat(Stream.from(-1L, 5L).take(3)).isEqualTo(Stream.of(-1L, 4L, 9L));
    }

    @Test
    public void shouldGenerateOverflowingLongStreamWithStep() {
        //noinspection NumericOverflow
        assertThat(Stream.from(Long.MAX_VALUE, 2).take(2))
                .isEqualTo(Stream.of(Long.MAX_VALUE, Long.MAX_VALUE + 2));
    }
    // -- static continually(Supplier)

    @Test
    public void shouldGenerateInfiniteStreamBasedOnSupplier() {
        assertThat(Stream.continually(() -> 1).take(13).reduce((i, j) -> i + j)).isEqualTo(13);
    }

    // -- static iterate(T, Function)

    @Test
    public void shouldGenerateInfiniteStreamBasedOnSupplierWithAccessToPreviousValue() {
        assertThat(Stream.iterate(2, (i) -> i + 2).take(3).reduce((i, j) -> i + j)).isEqualTo(12);
    }

    // -- static continually (T)

    @Test
    public void shouldGenerateInfiniteStreamBasedOnRepeatedElement() {
        assertThat(Stream.continually(2).take(3).reduce((i, j) -> i + j)).isEqualTo(6);
    }

    // -- static cons(T, Supplier)

    @Test
    public void shouldBuildStreamBasedOnHeadAndTailSupplierWithAccessToHead() {
        assertThat(Stream.cons(1, () -> Stream.cons(2, Stream::empty))).isEqualTo(Stream.of(1, 2));
    }

    // -- static narrow

    @Test
    public void shouldNarrowStream() {
        final Stream<Double> doubles = of(1.0d);
        final Stream<Number> numbers = Stream.narrow(doubles);
        final int actual = numbers.append(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    // -- append

    @Test
    public void shouldAppendMillionTimes() {
        final int bigNum = 1_000_000;
        assertThat(Stream.range(0, bigNum).foldLeft(Stream.empty(), Stream::append).length()).isEqualTo(bigNum);
    }

    // -- appendAll

    @Test
    public void shouldAppendAll() {
        assertThat(of(1, 2, 3).appendAll(of(4, 5, 6))).isEqualTo(of(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void shouldAppendAllIfThisIsEmpty() {
        assertThat(empty().appendAll(of(4, 5, 6))).isEqualTo(of(4, 5, 6));
    }

    @Test
    public void shouldAppendAllIfThatIsInfinite() {
        assertThat(of(1, 2, 3).appendAll(Stream.from(4)).take(6)).isEqualTo(of(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void shouldAppendAllToInfiniteStream() {
        assertThat(Stream.from(1).appendAll(Stream.continually(() -> -1)).take(6)).isEqualTo(of(1, 2, 3, 4, 5, 6));
    }

    // -- combinations

    @Test
    public void shouldComputeCombinationsOfEmptyStream() {
        assertThat(Stream.empty().combinations()).isEqualTo(Stream.of(Stream.empty()));
    }

    @Test
    public void shouldComputeCombinationsOfNonEmptyStream() {
        assertThat(Stream.of(1, 2, 3).combinations()).isEqualTo(Stream.of(Stream.empty(), Stream.of(1), Stream.of(2),
                Stream.of(3), Stream.of(1, 2), Stream.of(1, 3), Stream.of(2, 3), Stream.of(1, 2, 3)));
    }

    // -- combinations(k)

    @Test
    public void shouldComputeKCombinationsOfEmptyStream() {
        assertThat(Stream.empty().combinations(1)).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldComputeKCombinationsOfNonEmptyStream() {
        assertThat(Stream.of(1, 2, 3).combinations(2))
                .isEqualTo(Stream.of(Stream.of(1, 2), Stream.of(1, 3), Stream.of(2, 3)));
    }

    // -- flatMap

    @Test
    public void shouldFlatMapInfiniteTraversable() {
        assertThat(Stream.iterate(1, i -> i + 1).flatMap(i -> List.of(i, 2 * i)).take(7))
                .isEqualTo(Stream.of(1, 2, 2, 4, 3, 6, 4));
    }

    // -- peek

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 3;
    }

    // -- permutations

    @Test
    public void shouldComputePermutationsOfEmptyStream() {
        assertThat(Stream.empty().permutations()).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldComputePermutationsOfNonEmptyStream() {
        assertThat(Stream.of(1, 2, 3).permutations()).isEqualTo(Stream.ofAll(Stream.of(Stream.of(1, 2, 3),
                Stream.of(1, 3, 2), Stream.of(2, 1, 3), Stream.of(2, 3, 1), Stream.of(3, 1, 2), Stream.of(3, 2, 1))));
    }

    // -- appendSelf

    @Test
    public void shouldRecurrentlyCalculateFibonacci() {
        assertThat(Stream.of(1, 1).appendSelf(self -> self.zip(self.tail()).map(t -> t._1 + t._2)).take(10))
                .isEqualTo(Stream.of(1, 1, 2, 3, 5, 8, 13, 21, 34, 55));
    }

    @Test
    public void shouldRecurrentlyCalculatePrimes() {
        assertThat(Stream
                .of(2)
                .appendSelf(self -> Stream
                        .iterate(3, i -> i + 2)
                        .filter(i -> self.takeWhile(j -> j * j <= i).forAll(k -> i % k > 0)))
                .take(10)).isEqualTo(Stream.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29));
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
        final boolean actual = Stream.iterate(1, i -> i + 1).containsSlice(of(12, 13, 14));
        assertThat(actual).isTrue();
    }

    // -- lazy dropRight

    @Test
    public void shouldLazyDropRight() {
        assertThat(Stream.from(1).takeUntil(i -> i == 18).dropRight(7)).isEqualTo(Stream.range(1, 11));
    }

    // -- cycle

    @Test
    public void shouldCycleEmptyStream() {
        assertThat(empty().cycle()).isEqualTo(empty());
    }

    @Test
    public void shouldCycleNonEmptyStream() {
        assertThat(of(1, 2, 3).cycle().take(9)).isEqualTo(of(1, 2, 3, 1, 2, 3, 1, 2, 3));
    }

    // -- cycle(int)

    @Test
    public void shouldCycleTimesEmptyStream() {
        assertThat(empty().cycle(3)).isEqualTo(empty());
    }

    @Test
    public void shouldCycleTimesNonEmptyStream() {
        assertThat(of(1, 2, 3).cycle(-1)).isEqualTo(empty());
        assertThat(of(1, 2, 3).cycle(0)).isEqualTo(empty());
        assertThat(of(1, 2, 3).cycle(1)).isEqualTo(of(1, 2, 3));
        assertThat(of(1, 2, 3).cycle(3)).isEqualTo(of(1, 2, 3, 1, 2, 3, 1, 2, 3));
    }

    // -- transform()

    @Test
    public void shouldTransform() {
        String transformed = of(42).transform(v -> String.valueOf(v.get()));
        assertThat(transformed).isEqualTo("42");
    }

    @Test
    public void shouldExtendStreamWithConstantValue() {
        assertThat(Stream.of(1, 2, 3).extend(42).take(6)).isEqualTo(of(1, 2, 3, 42, 42, 42));
    }

    @Test
    public void shouldExtendStreamWithSupplier() {
        assertThat(Stream.of(1, 2, 3).extend(() -> 42).take(6)).isEqualTo(of(1, 2, 3, 42, 42, 42));
    }

    @Test
    public void shouldExtendStreamWithFunction() {
        assertThat(Stream.of(1, 2, 3).extend(i -> i + 1).take(6)).isEqualTo(of(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void shouldExtendEmptyStreamWithConstantValue() {
        assertThat(Stream.of().extend(42).take(6)).isEqualTo(of(42, 42, 42, 42, 42, 42));
    }

    @Test
    public void shouldExtendEmptyStreamWithSupplier() {
        assertThat(Stream.of().extend(() -> 42).take(6)).isEqualTo(of(42, 42, 42, 42, 42, 42));
    }

    @Test
    public void shouldReturnAnEmptyStreamWhenExtendingAnEmptyStreamWithFunction() {
        assertThat(Stream.<Integer> of().extend(i -> i + 1)).isEqualTo(of());
    }

    @Test
    public void shouldReturnTheOriginalStreamWhenTryingToExtendInfiniteStreamWithConstantValue() {
        assertThat(Stream.continually(1).extend(42).take(6)).isEqualTo(of(1, 1, 1, 1, 1, 1));
    }

    @Test
    public void shouldReturnTheOriginalStreamWhenTryingToExtendInfiniteStreamWithSupplier() {
        assertThat(Stream.continually(1).extend(() -> 42).take(6)).isEqualTo(of(1, 1, 1, 1, 1, 1));
    }

    @Test
    public void shouldReturnTheOriginalStreamWhenTryingToExtendInfiniteStreamWithFunction() {
        assertThat(Stream.continually(1).extend(i -> i + 1).take(6)).isEqualTo(of(1, 1, 1, 1, 1, 1));
    }

    // -- unfold

    @Test
    public void shouldUnfoldRightToEmpty() {
        assertThat(Stream.unfoldRight(0, x -> Option.none())).isEqualTo(empty());
    }

    @Test
    public void shouldUnfoldRightSimpleStream() {
        assertThat(
            Stream.unfoldRight(10, x -> x == 0
                              ? Option.none()
                              : Option.of(new Tuple2<>(x, x-1))))
            .isEqualTo(of(10, 9, 8, 7, 6, 5, 4, 3, 2, 1));
    }

    @Test
    public void shouldUnfoldLeftToEmpty() {
        assertThat(Stream.unfoldLeft(0, x -> Option.none())).isEqualTo(empty());
    }

    @Test
    public void shouldUnfoldLeftSimpleStream() {
        assertThat(
            Stream.unfoldLeft(10, x -> x == 0
                             ? Option.none()
                             : Option.of(new Tuple2<>(x-1, x))))
            .isEqualTo(of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    @Test
    public void shouldUnfoldToEmpty() {
        assertThat(Stream.unfold(0, x -> Option.none())).isEqualTo(empty());
    }

    @Test
    public void shouldUnfoldSimpleStream() {
        assertThat(
            Stream.unfold(10, x -> x == 0
                          ? Option.none()
                          : Option.of(new Tuple2<>(x-1, x))))
            .isEqualTo(of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
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

    @Test
    public void shouldStringifyNonNilAndNilTail() {
        final Stream<Integer> stream = this.of(1);
        stream.tail(); // evaluates empty tail
        assertThat(stream.toString()).isEqualTo("Stream(1)");
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
             * This implementation is stable regarding jvm impl changes of object serialization. The index of the number
             * of Stream elements is gathered dynamically.
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
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Test
    public void shouldEvaluateTailAtMostOnce() {
        final int[] counter = { 0 };
        final Stream<Integer> stream = Stream.continually(() -> counter[0]++);
        // this test ensures that the `tail.append(100)` does not modify the tail elements
        final Stream<Integer> tail = stream.tail().append(100);
        final String expected = stream.drop(1).take(3).mkString(",");
        final String actual = tail.take(3).mkString(",");
        assertThat(expected).isEqualTo("1,2,3");
        assertThat(actual).isEqualTo(expected);
    }

    @Ignore
    @Test
    public void shouldNotProduceStackOverflow() {
        Stream.range(0, 1_000_000)
                .map(String::valueOf)
                .foldLeft(Stream.<String> empty(), Stream::append)
                .mkString();
    }

    @Test // See #327, #594
    public void shouldNotEvaluateHeadOfTailWhenCallingIteratorHasNext() {

        final Integer[] vals = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        final StringBuilder actual = new StringBuilder();
        flatTryWithJavaslangStream(vals, i -> doStuff(i, actual));

        final StringBuilder expected = new StringBuilder();
        flatTryWithJavaStream(vals, i -> doStuff(i, expected));

        assertThat(actual.toString()).isEqualTo(expected.toString());
    }

    @Test
    public void shouldNotEvaluateNplusOneWhenTakeN() {
        assertThat(Stream.from(0).filter(this::hiddenThrow).take(1).sum().intValue()).isEqualTo(0);

    }

    private boolean hiddenThrow(final int i) {
        if (i == 0) {
            return true;
        }
        throw new IllegalArgumentException();
    }

    private Try<Void> flatTryWithJavaslangStream(Integer[] vals, Try.CheckedConsumer<Integer> func) {
        return Stream.of(vals)
                .map(v -> Try.run(() -> func.accept(v)))
                .find(Try::isFailure)
                .getOrElse(() -> Try.success(null));
    }

    private Try<Void> flatTryWithJavaStream(Integer[] vals, Try.CheckedConsumer<Integer> func) {
        return java.util.stream.Stream.of(vals)
                .map(v -> Try.run(() -> func.accept(v)))
                .filter(Try::isFailure)
                .findFirst()
                .orElseGet(() -> Try.success(null));
    }

    private String doStuff(int i, StringBuilder builder) throws Exception {
        builder.append(i);
        if (i == 5) {
            throw new Exception("Some error !!!");
        }
        return i + " Value";
    }

    // -- toStream

    @Test
    public void shouldReturnSelfOnConvertToStream() {
        Value<Integer> value = of(1, 2, 3);
        assertThat(value.toStream()).isSameAs(value);
    }
}
