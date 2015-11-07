/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Serializables;
import org.junit.Test;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.stream.Collector;

public class VectorTest extends AbstractSeqTest {
    @Override
    protected <T> Collector<T, ArrayList<T>, Vector<T>> collector() {
        return Vector.collector();
    }

    @Override
    protected <T> Vector<T> empty() {
        return Vector.empty();
    }

    @Override
    protected <T> Vector<T> of(T element) {
        return Vector.of(element);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> Vector<T> ofAll(T... elements) {
        return Vector.ofAll(elements);
    }

    @Override
    protected <T> Vector<T> ofAll(java.lang.Iterable<? extends T> elements) {
        return Vector.ofAll(elements);
    }

    @Override
    protected Vector<Boolean> ofAll(boolean[] array) {
        return Vector.ofAll(array);
    }

    @Override
    protected Vector<Byte> ofAll(byte[] array) {
        return Vector.ofAll(array);
    }

    @Override
    protected Vector<Character> ofAll(char[] array) {
        return Vector.ofAll(array);
    }

    @Override
    protected Vector<Double> ofAll(double[] array) {
        return Vector.ofAll(array);
    }

    @Override
    protected Vector<Float> ofAll(float[] array) {
        return Vector.ofAll(array);
    }

    @Override
    protected Vector<Integer> ofAll(int[] array) {
        return Vector.ofAll(array);
    }

    @Override
    protected Vector<Long> ofAll(long[] array) {
        return Vector.ofAll(array);
    }

    @Override
    protected Vector<Short> ofAll(short[] array) {
        return Vector.ofAll(array);
    }

    @Override
    protected Vector<Character> range(char from, char toExclusive) {
        return Vector.range(from, toExclusive);
    }

    @Override
    protected Vector<Character> rangeBy(char from, char toExclusive, int step) {
        return Vector.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Vector<Double> rangeBy(double from, double toExclusive, double step) {
        return Vector.rangeBy(from, toExclusive, step);
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return false;
    }

    @Override
    protected Vector<Integer> range(int from, int toExclusive) {
        return Vector.range(from, toExclusive);
    }

    @Override
    protected Vector<Integer> rangeBy(int from, int toExclusive, int step) {
        return Vector.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Vector<Long> range(long from, long toExclusive) {
        return Vector.range(from, toExclusive);
    }

    @Override
    protected Vector<Long> rangeBy(long from, long toExclusive, long step) {
        return Vector.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Vector<Character> rangeClosed(char from, char toInclusive) {
        return Vector.rangeClosed(from, toInclusive);
    }

    @Override
    protected Vector<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return Vector.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Vector<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return Vector.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Vector<Integer> rangeClosed(int from, int toInclusive) {
        return Vector.rangeClosed(from, toInclusive);
    }

    @Override
    protected Vector<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return Vector.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Vector<Long> rangeClosed(long from, long toInclusive) {
        return Vector.rangeClosed(from, toInclusive);
    }

    @Override
    protected Vector<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return Vector.rangeClosedBy(from, toInclusive, step);
    }

    // -- toString

    @Test
    public void shouldStringifyNil() {
        assertThat(empty().toString()).isEqualTo("Vector()");
    }

    @Test
    public void shouldStringifyNonNil() {
        assertThat(ofAll(1, 2, 3).toString()).isEqualTo("Vector(1, 2, 3)");
    }

    // -- Cons test

    @Test(expected = InvalidObjectException.class)
    public void shouldNotSerializeEnclosingClass() throws Throwable {
        Serializables.callReadObject(List.of(1));
    }

    @Test(expected = InvalidObjectException.class)
    public void shouldNotDeserializeListWithSizeLessThanOne() throws Throwable {
        try {
            /*
			 * This implementation is stable regarding jvm impl changes ofAll object serialization. The index ofAll the number
			 * ofAll List elements is gathered dynamically.
			 */
            final byte[] listWithOneElement = Serializables.serialize(List.of(0));
            final byte[] listWithTwoElements = Serializables.serialize(List.ofAll(0, 0));
            int index = -1;
            for (int i = 0; i < listWithOneElement.length && index == -1; i++) {
                final byte b1 = listWithOneElement[i];
                final byte b2 = listWithTwoElements[i];
                if (b1 != b2) {
                    if (b1 != 1 || b2 != 2) {
                        throw new IllegalStateException("Difference does not indicate number ofAll elements.");
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
