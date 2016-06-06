/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.function.*;
import java.util.stream.*;
import java.util.stream.Stream;

public class VectorTest extends AbstractIndexedSeqTest {
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
    protected final <T> Vector<T> of(T... elements) {
        return Vector.of(elements);
    }

    @Override
    protected <T> Vector<T> ofAll(Iterable<? extends T> elements) {
        return Vector.ofAll(elements);
    }

    @Override
    protected <T> Vector<T> ofJavaStream(Stream<? extends T> javaStream) {
        return Vector.ofAll(javaStream);
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
    protected <T> Vector<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        return Vector.tabulate(n, f);
    }

    @Override
    protected <T> Vector<T> fill(int n, Supplier<? extends T> s) {
        return Vector.fill(n, s);
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

    // -- static narrow

    @Test
    public void shouldNarrowVector() {
        final Vector<Double> doubles = of(1.0d);
        final Vector<Number> numbers = Vector.narrow(doubles);
        final int actual = numbers.append(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    // -- delegate

    @Test
    public void shouldPeriodicallyTrimExcess() {
        for (Vector<Integer> values = range(0, 100); !values.isEmpty(); values = values.tail()) {
            final int length = values.length(), delegateLength = values.delegate.size();
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
        assertThat(empty().toString()).isEqualTo("Vector()");
    }

    @Test
    public void shouldStringifyNonNil() {
        assertThat(of(null, 1, 2, 3).toString()).isEqualTo("Vector(null, 1, 2, 3)");
    }
}
