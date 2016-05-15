package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.IterableAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class BitSetTest extends AbstractSortedSetTest {

    private final static int MAX_BIT = 1_000_000;

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
                i = nextValue++;
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
                    Assertions.assertThat(HashSet.ofAll(actual)).isEqualTo(HashSet.ofAll((Iterable<T>) obj));
                } else {
                    super.isEqualTo(obj);
                }
                return this;
            }
        };
    }

    @Override
    protected <T> ObjectAssert<T> assertThat(T actual) {
        return new ObjectAssert<T>(actual) {
            @Override
            public ObjectAssert<T> isEqualTo(Object expected) {
                if (actual instanceof Tuple2) {
                    final Tuple2<?, ?> t1 = (Tuple2<?, ?>) actual;
                    final Tuple2<?, ?> t2 = (Tuple2<?, ?>) expected;
                    assertThat((Iterable<?>) t1._1).isEqualTo(t2._1);
                    assertThat((Iterable<?>) t1._2).isEqualTo(t2._2);
                    return this;
                } else if (actual instanceof Tuple3) {
                    final Tuple3<?, ?, ?> t1 = (Tuple3<?, ?, ?>) actual;
                    final Tuple3<?, ?, ?> t2 = (Tuple3<?, ?, ?>) expected;
                    assertThat((Iterable<?>) t1._1).isEqualTo(t2._1);
                    assertThat((Iterable<?>) t1._2).isEqualTo(t2._2);
                    return this;
                } else {
                    return super.isEqualTo(expected);
                }
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

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> BitSet<T> of(T... elements) {
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
        return this.<Double>bsBuilder().ofAll(Iterator.ofAll(array));
    }

    @Override
    protected BitSet<Float> ofAll(float[] array) {
        return this.<Float>bsBuilder().ofAll(Iterator.ofAll(array));
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
        return BitSet.range(from, toExclusive);
    }

    @Override
    protected BitSet<Character> rangeBy(char from, char toExclusive, int step) {
        return BitSet.rangeBy(from, toExclusive, step);
    }

    @Override
    protected BitSet<Double> rangeBy(double from, double toExclusive, double step) {
        return this.<Double>bsBuilder().ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    private static boolean badRange(int a, int b) {
        return a < 0 || b < 0 || a > MAX_BIT || b > MAX_BIT;
    }

    private static boolean badRange(long a, long b) {
        return a < 0 || b < 0 || a > MAX_BIT || b > MAX_BIT;
    }

    @Override
    protected BitSet<Integer> range(int from, int toExclusive) {
        if (badRange(from, toExclusive)) {
            return this.<Integer>bsBuilder().ofAll(Iterator.range(from, toExclusive));
        } else {
            return BitSet.range(from, toExclusive);
        }
    }

    @Override
    protected BitSet<Integer> rangeBy(int from, int toExclusive, int step) {
        if (badRange(from, toExclusive)) {
            return this.<Integer>bsBuilder().ofAll(Iterator.rangeBy(from, toExclusive, step));
        } else {
            return BitSet.rangeBy(from, toExclusive, step);
        }
    }

    @Override
    protected BitSet<Long> range(long from, long toExclusive) {
        if (badRange(from, toExclusive)) {
            return this.<Long>bsBuilder().ofAll(Iterator.range(from, toExclusive));
        } else {
            return BitSet.range(from, toExclusive);
        }
    }

    @Override
    protected BitSet<Long> rangeBy(long from, long toExclusive, long step) {
        if (badRange(from, toExclusive)) {
            return this.<Long>bsBuilder().ofAll(Iterator.rangeBy(from, toExclusive, step));
        } else {
            return BitSet.rangeBy(from, toExclusive, step);
        }
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
        return this.<Double>bsBuilder().ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected BitSet<Integer> rangeClosed(int from, int toInclusive) {
        if (badRange(from, toInclusive)) {
            return this.<Integer>bsBuilder().ofAll(Iterator.rangeClosed(from, toInclusive));
        } else {
            return BitSet.rangeClosed(from, toInclusive);
        }
    }

    @Override
    protected BitSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        if (badRange(from, toInclusive)) {
            return this.<Integer>bsBuilder().ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
        } else {
            return BitSet.rangeClosedBy(from, toInclusive, step);
        }
    }

    @Override
    protected BitSet<Long> rangeClosed(long from, long toInclusive) {
        if (badRange(from, toInclusive)) {
            return this.<Long>bsBuilder().ofAll(Iterator.rangeClosed(from, toInclusive));
        } else {
            return BitSet.rangeClosed(from, toInclusive);
        }
    }

    @Override
    protected BitSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        if (badRange(from, toInclusive)) {
            return this.<Long>bsBuilder().ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
        } else {
            return BitSet.rangeClosedBy(from, toInclusive, step);
        }
    }

    // BitSet specific

    @Test
    public void testBitSet1() {
        BitSet<Integer> bs = BitSet.empty();

        bs = bs.add(2);
        assertThat(bs.head()).isEqualTo(2);

        bs = bs.add(4);
        assertThat(bs.head()).isEqualTo(2);

        bs = bs.remove(2);
        assertThat(bs.head()).isEqualTo(4);
        assertThat(bs.contains(2)).isFalse();

        bs = bs.remove(4);
        assertThat(bs.isEmpty()).isTrue();
    }

    @Test
    public void testBitSet2() {
        BitSet<Integer> bs = BitSet.empty();

        bs = bs.add(2);
        assertThat(bs.head()).isEqualTo(2);

        bs = bs.add(70);
        assertThat(bs.head()).isEqualTo(2);

        bs = bs.remove(2);
        assertThat(bs.head()).isEqualTo(70);
        assertThat(bs.contains(2)).isFalse();

        bs = bs.remove(70);
        assertThat(bs.isEmpty()).isTrue();
    }

    @Test
    public void testBitSetN() {
        BitSet<Integer> bs = BitSet.empty();

        bs = bs.add(2);
        assertThat(bs.head()).isEqualTo(2);

        bs = bs.add(700);
        assertThat(bs.head()).isEqualTo(2);

        bs = bs.remove(2);
        assertThat(bs.head()).isEqualTo(700);
        assertThat(bs.contains(2)).isFalse();

        bs = bs.remove(700);
        assertThat(bs.isEmpty()).isTrue();
    }

    @Test
    public void testFactories() {
        assertThat(BitSet.of(7).contains(7)).isTrue();     // BitSet1
        assertThat(BitSet.of(77).contains(77)).isTrue();   // BitSet2
        assertThat(BitSet.of(777).contains(777)).isTrue(); // BitSetN
    }

    @Test
    public void testEnums() {
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

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAddNegativeElement() {
        BitSet.empty().add(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAddNegativeElements() {
        BitSet.empty().addAll(List.of(-1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowContainsNegativeElements() {
        BitSet.empty().contains(-1);
    }
}
