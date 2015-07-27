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

    @Override
    protected <T> Stream<T> nil() {
        return Stream.nil();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Stream<T> of(T... elements) {
        return Stream.of(elements);
    }

    // -- static collector()

    @Test
    public void shouldStreamAndCollectNil() {
        final Stream<?> actual = Stream.nil().toJavaStream().collect(Stream.collector());
        assertThat(actual).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldStreamAndCollectNonNil() {
        final Stream<?> actual = Stream.of(1, 2, 3).toJavaStream().collect(Stream.collector());
        assertThat(actual).isEqualTo(Stream.of(1, 2, 3));
    }

    @Test
    public void shouldParallelStreamAndCollectNil() {
        final Stream<?> actual = Stream.nil().toJavaStream().parallel().collect(Stream.collector());
        assertThat(actual).isEqualTo(Stream.nil());
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
        assertThat(Stream.cons(1, () -> Stream.cons(2, Stream::nil))).isEqualTo(Stream.of(1, 2));
    }

    // -- static nil()

    @Test
    public void shouldCreateNil() {
        assertThat(Stream.nil()).isEqualTo(Nil.instance());
    }

    // -- static of()

    @Test
    public void shouldCreateStreamOfStreamUsingCons() {
        assertThat(Stream.of(Stream.nil()).toString()).isEqualTo("Stream(Stream(), ?)");
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
        assertThat(Stream.rangeClosed(1, 0)).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromEqualsTo() {
        assertThat(Stream.rangeClosed(0, 0)).isEqualTo(Stream.of(0));
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromIsLessThanTo() {
        assertThat(Stream.rangeClosed(1, 3)).isEqualTo(Stream.of(1, 2, 3));
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromEqualsToEqualsInteger_MIN_VALUE() {
        assertThat(Stream.rangeClosed(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(Stream.of(Integer.MIN_VALUE));
    }

    @Test
    public void shouldCreateStreamOfRangeWhereFromEqualsToEqualsInteger_MAX_VALUE() {
        assertThat(Stream.rangeClosed(Integer.MAX_VALUE, Integer.MAX_VALUE)).isEqualTo(Stream.of(Integer.MAX_VALUE));
    }

    // -- static range(int, int)

    @Test
    public void shouldCreateStreamOfUntilWhereFromIsGreaterThanTo() {
        assertThat(Stream.range(1, 0)).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsTo() {
        assertThat(Stream.range(0, 0)).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromIsLessThanTo() {
        assertThat(Stream.range(1, 3)).isEqualTo(Stream.of(1, 2));
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsToEqualsInteger_MIN_VALUE() {
        assertThat(Stream.range(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsToEqualsInteger_MAX_VALUE() {
        assertThat(Stream.range(Integer.MAX_VALUE, Integer.MAX_VALUE)).isEqualTo(Stream.nil());
    }

    // -- combinations

    @Test
    public void shouldComputeCombinationsOfEmptyStream() {
        assertThat(Stream.nil().combinations()).isEqualTo(Stream.of(Stream.nil()));
    }

    @Test
    public void shouldComputeCombinationsOfNonEmptyStream() {
        assertThat(Stream.of(1, 2, 3).combinations()).isEqualTo(Stream.of(Stream.nil(), Stream.of(1), Stream.of(2), Stream.of(3), Stream.of(1, 2), Stream.of(1, 3), Stream.of(2, 3), Stream.of(1, 2, 3)));
    }

    // -- combinations(k)

    @Test
    public void shouldComputeKCombinationsOfEmptyStream() {
        assertThat(Stream.nil().combinations(1)).isEqualTo(Stream.nil());
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
        assertThat(Stream.of(1).combinations(-1)).isEqualTo(Stream.of(Stream.nil()));
    }

    // -- permutations

    @Test
    public void shouldComputePermutationsOfEmptyStream() {
        assertThat(Stream.nil().permutations()).isEqualTo(Stream.nil());
    }

    @Test
    public void shouldComputePermutationsOfNonEmptyStream() {
        assertThat(Stream.of(1, 2, 3).permutations()).isEqualTo(Stream.ofAll(Stream.of(Stream.of(1, 2, 3), Stream.of(1, 3, 2), Stream.of(2, 1, 3), Stream.of(2, 3, 1), Stream.of(3, 1, 2), Stream.of(3, 2, 1))));
    }

    // -- toString

    @Test
    public void shouldStringifyNil() {
        assertThat(this.nil().toString()).isEqualTo("Stream()");
    }

    @Test
    public void shouldStringifyNonNil() {
        assertThat(this.of(1, 2, 3).toString()).isEqualTo("Stream(1, ?)");
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
