/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

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

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Vector<T> of(T... elements) {
        return Vector.of(elements);
    }

    @Override
    protected <T> Vector<T> ofAll(Iterable<? extends T> elements) {
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
    int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Override
    boolean isThisLazyCollection() {
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
}
