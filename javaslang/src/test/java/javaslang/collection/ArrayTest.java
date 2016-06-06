/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.*;
import java.util.stream.Stream;

public class ArrayTest extends AbstractIndexedSeqTest {

    @Override
    protected <T> Collector<T, ArrayList<T>, ? extends Seq<T>> collector() {
        return Array.collector();
    }

    @Override
    protected <T> Array<T> empty() {
        return Array.empty();
    }

    @Override
    protected <T> Array<T> of(T element) {
        return Array.of(element);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> Array<T> of(T... elements) {
        return Array.of(elements);
    }

    @Override
    protected <T> Array<T> ofAll(Iterable<? extends T> elements) {
        return Array.ofAll(elements);
    }

    @Override
    protected <T> Array<T> ofJavaStream(Stream<? extends T> javaStream) {
        return Array.ofAll(javaStream);
    }

    @Override
    protected Array<Boolean> ofAll(boolean[] array) {
        return Array.ofAll(array);
    }

    @Override
    protected Array<Byte> ofAll(byte[] array) {
        return Array.ofAll(array);
    }

    @Override
    protected Array<Character> ofAll(char[] array) {
        return Array.ofAll(array);
    }

    @Override
    protected Array<Double> ofAll(double[] array) {
        return Array.ofAll(array);
    }

    @Override
    protected Array<Float> ofAll(float[] array) {
        return Array.ofAll(array);
    }

    @Override
    protected Array<Integer> ofAll(int[] array) {
        return Array.ofAll(array);
    }

    @Override
    protected Array<Long> ofAll(long[] array) {
        return Array.ofAll(array);
    }

    @Override
    protected Array<Short> ofAll(short[] array) {
        return Array.ofAll(array);
    }

    @Override
    protected <T> Array<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        return Array.tabulate(n, f);
    }

    @Override
    protected <T> Array<T> fill(int n, Supplier<? extends T> s) {
        return Array.fill(n, s);
    }

    @Override
    protected Array<Character> range(char from, char toExclusive) {
        return Array.range(from, toExclusive);
    }

    @Override
    protected Array<Character> rangeBy(char from, char toExclusive, int step) {
        return Array.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Array<Double> rangeBy(double from, double toExclusive, double step) {
        return Array.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Array<Integer> range(int from, int toExclusive) {
        return Array.range(from, toExclusive);
    }

    @Override
    protected Array<Integer> rangeBy(int from, int toExclusive, int step) {
        return Array.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Array<Long> range(long from, long toExclusive) {
        return Array.range(from, toExclusive);
    }

    @Override
    protected Array<Long> rangeBy(long from, long toExclusive, long step) {
        return Array.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Array<Character> rangeClosed(char from, char toInclusive) {
        return Array.rangeClosed(from, toInclusive);
    }

    @Override
    protected Array<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return Array.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Array<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return Array.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Array<Integer> rangeClosed(int from, int toInclusive) {
        return Array.rangeClosed(from, toInclusive);
    }

    @Override
    protected Array<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return Array.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Array<Long> rangeClosed(long from, long toInclusive) {
        return Array.rangeClosed(from, toInclusive);
    }

    @Override
    protected Array<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return Array.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return false;
    }

    // -- static narrow

    @Test
    public void shouldNarrowArray() {
        final Array<Double> doubles = of(1.0d);
        final Array<Number> numbers = Array.narrow(doubles);
        final int actual = numbers.append(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    // -- delegate

    @Test
    public void shouldPeriodicallyTrimExcess() {
        for (Array<Integer> values = range(0, 100); !values.isEmpty(); values = values.tail()) {
            final int length = values.length(), delegateLength = values.delegate.length;
            if (length != delegateLength) {
                assertThat(List.of(100, 50, 25, 13, 7, 4, 2)).contains(delegateLength);
            }
        }
    }

    // -- transform()

    @Test
    public void shouldTransform() {
        final String transformed = of(42).transform(v -> String.valueOf(v.get()));
        assertThat(transformed).isEqualTo("42");
    }

    // -- toString

    @Test
    public void shouldStringifyNil() {
        assertThat(empty().toString()).isEqualTo("Array()");
    }

    @Test
    public void shouldStringifyNonNil() {
        assertThat(of(1, 2, 3).toString()).isEqualTo("Array(1, 2, 3)");
    }

}
