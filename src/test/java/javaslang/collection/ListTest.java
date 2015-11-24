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

public class ListTest extends AbstractSeqTest {

    // -- construction

    @Override
    protected <T> Collector<T, ArrayList<T>, List<T>> collector() {
        return List.collector();
    }

    @Override
    protected <T> List<T> empty() {
        return List.empty();
    }

    @Override
    protected <T> List<T> of(T element) {
        return List.of(element);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> List<T> ofAll(T... elements) {
        return List.ofAll(elements);
    }

    @Override
    protected <T> List<T> ofAll(java.lang.Iterable<? extends T> elements) {
        return List.ofAll(elements);
    }

    @Override
    protected List<Boolean> ofAll(boolean[] array) {
        return List.ofAll(array);
    }

    @Override
    protected List<Byte> ofAll(byte[] array) {
        return List.ofAll(array);
    }

    @Override
    protected List<Character> ofAll(char[] array) {
        return List.ofAll(array);
    }

    @Override
    protected List<Double> ofAll(double[] array) {
        return List.ofAll(array);
    }

    @Override
    protected List<Float> ofAll(float[] array) {
        return List.ofAll(array);
    }

    @Override
    protected List<Integer> ofAll(int[] array) {
        return List.ofAll(array);
    }

    @Override
    protected List<Long> ofAll(long[] array) {
        return List.ofAll(array);
    }

    @Override
    protected List<Short> ofAll(short[] array) {
        return List.ofAll(array);
    }

    @Override
    protected List<Character> range(char from, char toExclusive) {
        return List.range(from, toExclusive);
    }

    @Override
    protected List<Character> rangeBy(char from, char toExclusive, int step) {
        return List.rangeBy(from, toExclusive, step);
    }

    @Override
    protected List<Double> rangeBy(double from, double toExclusive, double step) {
        return List.rangeBy(from, toExclusive, step);
    }

    @Override
    protected List<Integer> range(int from, int toExclusive) {
        return List.range(from, toExclusive);
    }

    @Override
    protected List<Integer> rangeBy(int from, int toExclusive, int step) {
        return List.rangeBy(from, toExclusive, step);
    }

    @Override
    protected List<Long> range(long from, long toExclusive) {
        return List.range(from, toExclusive);
    }

    @Override
    protected List<Long> rangeBy(long from, long toExclusive, long step) {
        return List.rangeBy(from, toExclusive, step);
    }

    @Override
    protected List<Character> rangeClosed(char from, char toInclusive) {
        return List.rangeClosed(from, toInclusive);
    }

    @Override
    protected List<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return List.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected List<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return List.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected List<Integer> rangeClosed(int from, int toInclusive) {
        return List.rangeClosed(from, toInclusive);
    }

    @Override
    protected List<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return List.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected List<Long> rangeClosed(long from, long toInclusive) {
        return List.rangeClosed(from, toInclusive);
    }

    @Override
    protected List<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return List.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    // -- toString

    @Test
    public void shouldStringifyNil() {
        assertThat(empty().toString()).isEqualTo("List()");
    }

    @Test
    public void shouldStringifyNonNil() {
        assertThat(ofAll(1, 2, 3).toString()).isEqualTo("List(1, 2, 3)");
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

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return false;
    }

}
