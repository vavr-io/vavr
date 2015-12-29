/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class StackTest extends AbstractSeqTest {

    // -- construction

    @Override
    protected <T> Collector<T, ArrayList<T>, Stack<T>> collector() {
        return Stack.collector();
    }

    @Override
    protected <T> Stack<T> empty() {
        return Stack.empty();
    }

    @Override
    protected <T> Stack<T> of(T element) {
        return Stack.of(element);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> Stack<T> of(T... elements) {
        return Stack.of(elements);
    }

    @Override
    protected <T> Stack<T> ofAll(Iterable<? extends T> elements) {
        return Stack.ofAll(elements);
    }

    @Override
    protected Stack<Boolean> ofAll(boolean[] array) {
        return Stack.ofAll(array);
    }

    @Override
    protected Stack<Byte> ofAll(byte[] array) {
        return Stack.ofAll(array);
    }

    @Override
    protected Stack<Character> ofAll(char[] array) {
        return Stack.ofAll(array);
    }

    @Override
    protected Stack<Double> ofAll(double[] array) {
        return Stack.ofAll(array);
    }

    @Override
    protected Stack<Float> ofAll(float[] array) {
        return Stack.ofAll(array);
    }

    @Override
    protected Stack<Integer> ofAll(int[] array) {
        return Stack.ofAll(array);
    }

    @Override
    protected Stack<Long> ofAll(long[] array) {
        return Stack.ofAll(array);
    }

    @Override
    protected Stack<Short> ofAll(short[] array) {
        return Stack.ofAll(array);
    }

    @Override
    protected <T> Stack<T> tabulate(Integer n, Function<Integer, ? extends T> f) {
        return Stack.tabulate(n, f);
    }

    @Override
    protected <T> Stack<T> fill(Integer n, Supplier<? extends T> s) {
        return Stack.fill(n, s);
    }

    @Override
    protected Stack<Character> range(char from, char toExclusive) {
        return Stack.range(from, toExclusive);
    }

    @Override
    protected Stack<Character> rangeBy(char from, char toExclusive, int step) {
        return Stack.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Stack<Double> rangeBy(double from, double toExclusive, double step) {
        return Stack.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Stack<Integer> range(int from, int toExclusive) {
        return Stack.range(from, toExclusive);
    }

    @Override
    protected Stack<Integer> rangeBy(int from, int toExclusive, int step) {
        return Stack.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Stack<Long> range(long from, long toExclusive) {
        return Stack.range(from, toExclusive);
    }

    @Override
    protected Stack<Long> rangeBy(long from, long toExclusive, long step) {
        return Stack.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Stack<Character> rangeClosed(char from, char toInclusive) {
        return Stack.rangeClosed(from, toInclusive);
    }

    @Override
    protected Stack<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return Stack.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Stack<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return Stack.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Stack<Integer> rangeClosed(int from, int toInclusive) {
        return Stack.rangeClosed(from, toInclusive);
    }

    @Override
    protected Stack<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return Stack.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Stack<Long> rangeClosed(long from, long toInclusive) {
        return Stack.rangeClosed(from, toInclusive);
    }

    @Override
    protected Stack<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return Stack.rangeClosedBy(from, toInclusive, step);
    }

    // -- other

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return false;
    }

}
