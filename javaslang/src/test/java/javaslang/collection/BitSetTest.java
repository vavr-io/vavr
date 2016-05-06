package javaslang.collection;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.IterableAssert;
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

    @Override
    protected <T> IterableAssert<T> assertThat(Iterable<T> actual) {
        return new IterableAssert<T>(actual) {
            @Override
            @SuppressWarnings("unchecked")
            public IterableAssert<T> isEqualTo(Object obj) {
                if(obj instanceof BitSet || actual instanceof BitSet) {
                    Assertions.assertThat(List.ofAll(actual)).isEqualTo(List.ofAll((Iterable<T>) obj));
                } else {
                    super.isEqualTo(obj);
                }
                return this;
            }
        };
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
    protected boolean emptyShouldBeSingleton() {
        return false;
    }

    @Override
    protected <T> BitSet<T> of(T element) {
        return this.<T>bsBuilder().of(element);
    }

    @Override
    protected <T> BitSet<T> of(T... elements) {
        return this.<T>bsBuilder().of(elements);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Override
    protected <T> BitSet<T> ofAll(Iterable<? extends T> elements) {
        return this.<T>bsBuilder().ofAll(elements);
    }

    @Override
    protected BitSet<Boolean> ofAll(boolean[] array) {
        return BitSet.ofAll(array);
    }

    @Override
    protected BitSet<Byte> ofAll(byte[] array) {
        return BitSet.ofAll(array);
    }

    @Override
    protected BitSet<Character> ofAll(char[] array) {
        return BitSet.ofAll(array);
    }

    @Override
    protected BitSet<Double> ofAll(double[] array) {
        return null;
    }

    @Override
    protected BitSet<Float> ofAll(float[] array) {
        return null;
    }

    @Override
    protected BitSet<Integer> ofAll(int[] array) {
        return BitSet.ofAll(array);
    }

    @Override
    protected BitSet<Long> ofAll(long[] array) {
        return BitSet.ofAll(array);
    }

    @Override
    protected BitSet<Short> ofAll(short[] array) {
        return BitSet.ofAll(array);
    }

    @Override
    protected <T> BitSet<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        return this.<T>bsBuilder().tabulate(n, f);
    }

    @Override
    protected <T> BitSet<T> fill(int n, Supplier<? extends T> s) {
        return this.<T>bsBuilder().fill(n, s);
    }

    @Override
    protected BitSet<Character> range(char from, char toExclusive) {
        return null;
    }

    @Override
    protected BitSet<Character> rangeBy(char from, char toExclusive, int step) {
        return null;
    }

    @Override
    protected BitSet<Double> rangeBy(double from, double toExclusive, double step) {
        return null;
    }

    @Override
    protected BitSet<Integer> range(int from, int toExclusive) {
        return BitSet.range(from, toExclusive);
    }

    @Override
    protected BitSet<Integer> rangeBy(int from, int toExclusive, int step) {
        return BitSet.rangeBy(from, toExclusive, step);
    }

    @Override
    protected BitSet<Long> range(long from, long toExclusive) {
        return null;
    }

    @Override
    protected BitSet<Long> rangeBy(long from, long toExclusive, long step) {
        return null;
    }

    @Override
    protected BitSet<Character> rangeClosed(char from, char toInclusive) {
        return BitSet.rangeClosed(from, toInclusive);
    }

    @Override
    protected BitSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return BitSet.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected BitSet<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return null;
    }

    @Override
    protected BitSet<Integer> rangeClosed(int from, int toInclusive) {
        return null;//BitSet.rangeClosed(from, toInclusive);
    }

    @Override
    protected BitSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return BitSet.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected BitSet<Long> rangeClosed(long from, long toInclusive) {
        return null;
    }

    @Override
    protected BitSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
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
