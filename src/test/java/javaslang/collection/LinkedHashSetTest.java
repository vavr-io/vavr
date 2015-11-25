package javaslang.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collector;

public class LinkedHashSetTest extends AbstractSetTest {

    @Override
    protected <T> Collector<T, ArrayList<T>, LinkedHashSet<T>> collector() {
        return LinkedHashSet.collector();
    }

    @Override
    protected <T> LinkedHashSet<T> empty() {
        return LinkedHashSet.empty();
    }

    @Override
    protected <T> LinkedHashSet<T> of(T element) {
        return LinkedHashSet.of(element);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> LinkedHashSet<T> ofAll(T... elements) {
        return LinkedHashSet.ofAll(elements);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return false;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Override
    protected <T> LinkedHashSet<T> ofAll(Iterable<? extends T> elements) {
        return LinkedHashSet.ofAll(elements);
    }

    @Override
    protected LinkedHashSet<Boolean> ofAll(boolean[] array) {
        return LinkedHashSet.ofAll(array);
    }

    @Override
    protected LinkedHashSet<Byte> ofAll(byte[] array) {
        return LinkedHashSet.ofAll(array);
    }

    @Override
    protected LinkedHashSet<Character> ofAll(char[] array) {
        return LinkedHashSet.ofAll(array);
    }

    @Override
    protected LinkedHashSet<Double> ofAll(double[] array) {
        return LinkedHashSet.ofAll(array);
    }

    @Override
    protected LinkedHashSet<Float> ofAll(float[] array) {
        return LinkedHashSet.ofAll(array);
    }

    @Override
    protected LinkedHashSet<Integer> ofAll(int[] array) {
        return LinkedHashSet.ofAll(array);
    }

    @Override
    protected LinkedHashSet<Long> ofAll(long[] array) {
        return LinkedHashSet.ofAll(array);
    }

    @Override
    protected LinkedHashSet<Short> ofAll(short[] array) {
        return LinkedHashSet.ofAll(array);
    }

    @Override
    protected LinkedHashSet<Character> range(char from, char toExclusive) {
        return LinkedHashSet.range(from, toExclusive);
    }

    @Override
    protected LinkedHashSet<Character> rangeBy(char from, char toExclusive, int step) {
        return LinkedHashSet.rangeBy(from, toExclusive, step);
    }

    @Override
    protected LinkedHashSet<Double> rangeBy(double from, double toExclusive, double step) {
        return LinkedHashSet.rangeBy(from, toExclusive, step);
    }

    @Override
    protected LinkedHashSet<Integer> range(int from, int toExclusive) {
        return LinkedHashSet.range(from, toExclusive);
    }

    @Override
    protected LinkedHashSet<Integer> rangeBy(int from, int toExclusive, int step) {
        return LinkedHashSet.rangeBy(from, toExclusive, step);
    }

    @Override
    protected LinkedHashSet<Long> range(long from, long toExclusive) {
        return LinkedHashSet.range(from, toExclusive);
    }

    @Override
    protected LinkedHashSet<Long> rangeBy(long from, long toExclusive, long step) {
        return LinkedHashSet.rangeBy(from, toExclusive, step);
    }

    @Override
    protected LinkedHashSet<Character> rangeClosed(char from, char toInclusive) {
        return LinkedHashSet.rangeClosed(from, toInclusive);
    }

    @Override
    protected LinkedHashSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return LinkedHashSet.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected LinkedHashSet<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return LinkedHashSet.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected LinkedHashSet<Integer> rangeClosed(int from, int toInclusive) {
        return LinkedHashSet.rangeClosed(from, toInclusive);
    }

    @Override
    protected LinkedHashSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return LinkedHashSet.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected LinkedHashSet<Long> rangeClosed(long from, long toInclusive) {
        return LinkedHashSet.rangeClosed(from, toInclusive);
    }

    @Override
    protected LinkedHashSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return LinkedHashSet.rangeClosedBy(from, toInclusive, step);
    }

    @Test
    public void shouldKeepOrder() {
        List<Integer> actual = LinkedHashSet.<Integer> empty().add(3).add(2).add(1).toList();
        assertThat(actual).isEqualTo(List.ofAll(3, 2, 1));
    }
}
