package javaslang.collection;

import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class BitSetTest extends AbstractSortedSetTest {

    private enum E {
        V1, V2, V3
    }

    private static class Mapper<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final java.util.Map<Integer, T> fromIntMap = new java.util.HashMap<>();
        private final java.util.Map<T, Integer> toIntMap = new java.util.HashMap<>();
        private int nextValue = 0;

        synchronized T fromInt(Integer i) {
            if (i < nextValue) {
                return fromIntMap.get(i);
            } else {
                throw new RuntimeException();
            }
        }

        synchronized Integer toInt(T value) {
            Integer i = toIntMap.get(value);
            if (i == null) {
                if(value instanceof Integer) {
                    i = (Integer) value;
                    nextValue = Integer.MAX_VALUE;
                } else {
                    i = nextValue++;
                }
                toIntMap.put(value, i);
                fromIntMap.put(i, value);
            }
            return i;
        }
    }

    private <T> BitSet.Builder<T> bsBuilder() {
        Mapper<T> mapper = new Mapper<>();
        return BitSet.withRelations(mapper::fromInt, mapper::toInt);
    }

    @Override
    protected <T> Collector<T, ArrayList<T>, ? extends Traversable<T>> collector() {
        return this.<T>bsBuilder().collector();
    }

    @Override
    protected <T> BitSet<T> empty() {
        return this.<T>bsBuilder().empty();
    }

    @Override
    protected <T> BitSet<T> of(T element) {
        return this.<T>bsBuilder().of(element);
    }

    @Override
    protected <T> Set<T> of(T... elements) {
        return this.<T>bsBuilder().of(elements);
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
    protected <T> Traversable<T> ofAll(Iterable<? extends T> elements) {
        return this.<T>bsBuilder().ofAll(elements);
    }

    @Override
    protected Traversable<Boolean> ofAll(boolean[] array) {
        return null;
    }

    @Override
    protected Traversable<Byte> ofAll(byte[] array) {
        return null;
    }

    @Override
    protected Traversable<Character> ofAll(char[] array) {
        return null;
    }

    @Override
    protected Traversable<Double> ofAll(double[] array) {
        return null;
    }

    @Override
    protected Traversable<Float> ofAll(float[] array) {
        return null;
    }

    @Override
    protected Traversable<Integer> ofAll(int[] array) {
        return null;
    }

    @Override
    protected Traversable<Long> ofAll(long[] array) {
        return null;
    }

    @Override
    protected Traversable<Short> ofAll(short[] array) {
        return null;
    }

    @Override
    protected <T> Traversable<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        return this.<T>bsBuilder().tabulate(n, f);
    }

    @Override
    protected <T> Traversable<T> fill(int n, Supplier<? extends T> s) {
        return this.<T>bsBuilder().fill(n, s);
    }

    @Override
    protected Traversable<Character> range(char from, char toExclusive) {
        return null;
    }

    @Override
    protected Traversable<Character> rangeBy(char from, char toExclusive, int step) {
        return null;
    }

    @Override
    protected Traversable<Double> rangeBy(double from, double toExclusive, double step) {
        return null;
    }

    @Override
    protected Traversable<Integer> range(int from, int toExclusive) {
        return null;
    }

    @Override
    protected Traversable<Integer> rangeBy(int from, int toExclusive, int step) {
        return null;
    }

    @Override
    protected Traversable<Long> range(long from, long toExclusive) {
        return null;
    }

    @Override
    protected Traversable<Long> rangeBy(long from, long toExclusive, long step) {
        return null;
    }

    @Override
    protected Traversable<Character> rangeClosed(char from, char toInclusive) {
        return null;
    }

    @Override
    protected Traversable<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return null;
    }

    @Override
    protected Traversable<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return null;
    }

    @Override
    protected Traversable<Integer> rangeClosed(int from, int toInclusive) {
        return null;
    }

    @Override
    protected Traversable<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return null;
    }

    @Override
    protected Traversable<Long> rangeClosed(long from, long toInclusive) {
        return null;
    }

    @Override
    protected Traversable<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return null;
    }

    @Test
    public void test1() {
        BitSet<Integer> bs = BitSet.empty();

        bs = bs.add(2);
        assert bs.head() == 2;

        bs = bs.add(4);
        assert bs.head() == 2;

        bs = bs.add(70);
        assert bs.head() == 2;

        bs = bs.add(300);
        assert bs.head() == 2;

        bs = bs.remove(2);
        assert bs.head() == 4;
        assert !bs.contains(2);

        bs = bs.remove(4);
        assert bs.head() == 70;
        assert !bs.contains(4);

        bs = bs.remove(70);
        assert bs.head() == 300;
        assert !bs.contains(70);

        assert bs.contains(300);
    }

    @Test
    public void test2() {
        BitSet<E> bs = BitSet.withEnum(E.class).empty();
        bs = bs.add(E.V2);
        assert bs.head() == E.V2;
        bs = bs.add(E.V3);
        assert bs.head() == E.V2;
        bs = bs.remove(E.V2);
        assert bs.head() == E.V3;
        assert !bs.contains(E.V2);
        assert bs.contains(E.V3);
    }

    @Test
    public void test3() {
        BitSet<Integer> bs = BitSet.empty();
        assert bs.add(1).add(2).init().toList().equals(List.of(1));
        assert bs.add(1).add(70).init().toList().equals(List.of(1));
        assert bs.add(1).add(700).init().toList().equals(List.of(1));
    }
}
