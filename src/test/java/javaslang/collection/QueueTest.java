/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.ArrayList;
import java.util.stream.Collector;

public class QueueTest extends AbstractSeqTest {

    // -- construction

    @Override
    protected <T> Collector<T, ArrayList<T>, Queue<T>> collector() {
        return Queue.collector();
    }

    @Override
    protected <T> Queue<T> empty() {
        return Queue.empty();
    }

    @Override
    protected <T> Queue<T> of(T element) {
        return Queue.of(element);
    }

    @SafeVarargs
    @SuppressWarnings({ "unchecked", "varargs" })
    @Override
    protected final <T> Queue<T> of(T... elements) {
        return Queue.of(elements);
    }

    @Override
    protected <T> Queue<T> ofAll(Iterable<? extends T> elements) {
        return Queue.ofAll(elements);
    }

    @Override
    protected Queue<Boolean> ofAll(boolean[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Byte> ofAll(byte[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Character> ofAll(char[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Double> ofAll(double[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Float> ofAll(float[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Integer> ofAll(int[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Long> ofAll(long[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Short> ofAll(short[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Integer> range(int from, int toExclusive) {
        return Queue.range(from, toExclusive);
    }

    @Override
    protected Queue<Integer> rangeBy(int from, int toExclusive, int step) {
        return Queue.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Queue<Long> range(long from, long toExclusive) {
        return Queue.range(from, toExclusive);
    }

    @Override
    protected Queue<Long> rangeBy(long from, long toExclusive, long step) {
        return Queue.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Queue<Integer> rangeClosed(int from, int toInclusive) {
        return Queue.rangeClosed(from, toInclusive);
    }

    @Override
    protected Queue<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return Queue.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Queue<Long> rangeClosed(long from, long toInclusive) {
        return Queue.rangeClosed(from, toInclusive);
    }

    @Override
    protected Queue<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return Queue.rangeClosedBy(from, toInclusive, step);
    }

    // -- other

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 1;
    }
}
