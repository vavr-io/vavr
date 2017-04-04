/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Comparator.nullsFirst;
import static javaslang.collection.Comparators.naturalComparator;

public class TreeSetTest extends AbstractSortedSetTest {

    @Override
    protected <T> Collector<T, ArrayList<T>, ? extends TreeSet<T>> collector() {
        return TreeSet.collector(naturalComparator());
    }

    @Override
    protected <T> TreeSet<T> empty() {
        return TreeSet.empty(naturalComparator());
    }

    @Override
    protected <T> TreeSet<T> emptyWithNull() {
        return TreeSet.empty(nullsFirst(naturalComparator()));
    }

    @Override
    protected boolean emptyShouldBeSingleton() {
        return false;
    }

    @Override
    protected <T> TreeSet<T> of(T element) {
        return TreeSet.of(naturalComparator(), element);
    }

    @Override
    protected <T> TreeSet<T> of(Comparator<? super T> comparator, T element) {
        return TreeSet.of(comparator, element);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> TreeSet<T> of(T... elements) {
        return TreeSet.<T> of(naturalComparator(), elements);
    }

    @Override
    protected <T> TreeSet<T> ofAll(Iterable<? extends T> elements) {
        return TreeSet.ofAll(naturalComparator(), elements);
    }

    @Override
    protected TreeSet<Boolean> ofAll(boolean[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected TreeSet<Byte> ofAll(byte[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected TreeSet<Character> ofAll(char[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected TreeSet<Double> ofAll(double[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected TreeSet<Float> ofAll(float[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected TreeSet<Integer> ofAll(int[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected TreeSet<Long> ofAll(long[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected TreeSet<Short> ofAll(short[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected <T> TreeSet<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        return TreeSet.tabulate(naturalComparator(), n, f);
    }

    @Override
    protected <T> TreeSet<T> fill(int n, Supplier<? extends T> s) {
        return TreeSet.fill(naturalComparator(), n, s);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    // -- static narrow

    @Test
    public void shouldNarrowTreeSet() {
        final TreeSet<Double> doubles = TreeSet.of(toStringComparator(), 1.0d);
        final TreeSet<Number> numbers = TreeSet.narrow(doubles);
        final int actual = numbers.add(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    // -- addAll

    @Test
    public void shouldKeepComparator() {
        final List<?> list = TreeSet.empty(inverseIntComparator()).addAll(TreeSet.of(1, 2, 3)).toList();
        assertThat(list).isEqualTo(List.of(3, 2, 1));
    }

    // -- transform()

    @Test
    public void shouldTransform() {
        final String transformed = of(42).transform(v -> String.valueOf(v.get()));
        assertThat(transformed).isEqualTo("42");
    }

    // -- helpers

    private static Comparator<Integer> inverseIntComparator() {
        return (i1, i2) -> Integer.compare(i2, i1);
    }

    private static Comparator<Object> toStringComparator() {
        return (Comparator<Object> & Serializable) (o1, o2) -> String.valueOf(o1).compareTo(String.valueOf(o2));
    }

    @Override
    protected TreeSet<Character> range(char from, char toExclusive) {
        return TreeSet.range(from, toExclusive);
    }

    @Override
    protected TreeSet<Character> rangeBy(char from, char toExclusive, int step) {
        return TreeSet.rangeBy(from, toExclusive, step);
    }

    @Override
    protected TreeSet<Double> rangeBy(double from, double toExclusive, double step) {
        return TreeSet.rangeBy(from, toExclusive, step);
    }

    @Override
    protected TreeSet<Integer> range(int from, int toExclusive) {
        return TreeSet.range(from, toExclusive);
    }

    @Override
    protected TreeSet<Integer> rangeBy(int from, int toExclusive, int step) {
        return TreeSet.rangeBy(from, toExclusive, step);
    }

    @Override
    protected TreeSet<Long> range(long from, long toExclusive) {
        return TreeSet.range(from, toExclusive);
    }

    @Override
    protected TreeSet<Long> rangeBy(long from, long toExclusive, long step) {
        return TreeSet.rangeBy(from, toExclusive, step);
    }

    @Override
    protected TreeSet<Character> rangeClosed(char from, char toInclusive) {
        return TreeSet.rangeClosed(from, toInclusive);
    }

    @Override
    protected TreeSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return TreeSet.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected TreeSet<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return TreeSet.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected TreeSet<Integer> rangeClosed(int from, int toInclusive) {
        return TreeSet.rangeClosed(from, toInclusive);
    }

    @Override
    protected TreeSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return TreeSet.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected TreeSet<Long> rangeClosed(long from, long toInclusive) {
        return TreeSet.rangeClosed(from, toInclusive);
    }

    @Override
    protected TreeSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return TreeSet.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    @Test
    public void shouldScanWithNonComparable() {
    }

    @Override
    @Test
    public void shouldNarrowTraversable() {
    }

    @Override
    @Test
    public void shouldNarrowSet() {
    }
}
