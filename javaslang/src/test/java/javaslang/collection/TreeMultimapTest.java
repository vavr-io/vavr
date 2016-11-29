package javaslang.collection;

import javaslang.Tuple2;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static javaslang.collection.Comparators.naturalComparator;

public class TreeMultimapTest extends AbstractMultimapTest {

    @Override
    protected String className() {
        return "TreeMultimap[" + containerName() + "]";
    }

    @Override
    <T1, T2> java.util.Map<T1, T2> javaEmptyMap() {
        return new java.util.TreeMap<>();
    }

    @Override
    protected <T1 extends Comparable<T1>, T2> Multimap<T1, T2> emptyMap() {
        switch (containerType) {
            case SEQ:
                return TreeMultimap.withSeq().empty();
            case SET:
                return TreeMultimap.withSet().empty();
            case SORTED_SET:
                return TreeMultimap.withSortedSet(naturalComparator()).empty();
        }
        throw new RuntimeException();
    }

    @Override
    protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Multimap<Integer, T>> mapCollector() {
        switch (containerType) {
            case SEQ:
                return TreeMultimap.withSeq().collector();
            case SET:
                return TreeMultimap.withSet().collector();
            case SORTED_SET:
                return TreeMultimap.withSortedSet(naturalComparator()).collector();
        }
        throw new RuntimeException();
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K extends Comparable<K>, V> Multimap<K, V> mapOfTuples(Tuple2<? extends K, ? extends V>... entries) {
        switch (containerType) {
            case SEQ:
                return TreeMultimap.withSeq().ofEntries(entries);
            case SET:
                return TreeMultimap.withSet().ofEntries(entries);
            case SORTED_SET:
                return TreeMultimap.withSortedSet(naturalComparator()).ofEntries(entries);
        }
        throw new RuntimeException();
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K extends Comparable<K>, V> Multimap<K, V> mapOfEntries(java.util.Map.Entry<? extends K, ? extends V>... entries) {
        switch (containerType) {
            case SEQ:
                return TreeMultimap.withSeq().ofEntries(entries);
            case SET:
                return TreeMultimap.withSet().ofEntries(entries);
            case SORTED_SET:
                return TreeMultimap.withSortedSet(naturalComparator()).ofEntries(entries);
        }
        throw new RuntimeException();
    }

    @Override
    protected <K extends Comparable<K>, V> Multimap<K, V> mapOfPairs(K k1, V v1, K k2, V v2, K k3, V v3) {
        switch (containerType) {
            case SEQ:
                return TreeMultimap.withSeq().of(k1, v1, k2, v2, k3, v3);
            case SET:
                return TreeMultimap.withSet().of(k1, v1, k2, v2, k3, v3);
            case SORTED_SET:
                return TreeMultimap.withSortedSet(naturalComparator()).of(k1, v1, k2, v2, k3, v3);
        }
        throw new RuntimeException();
    }

    @Override
    protected <K extends Comparable<K>, V> Multimap<K, V> mapOf(K key, V value) {
        return TreeMultimap.withSeq().of(key, value);
    }

    @Override
    protected <K extends Comparable<K>, V> Multimap<K, V> mapTabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        switch (containerType) {
            case SEQ:
                return TreeMultimap.withSeq().tabulate(n, f);
            case SET:
                return TreeMultimap.withSet().tabulate(n, f);
            case SORTED_SET:
                return TreeMultimap.withSortedSet(naturalComparator()).tabulate(n, f);
        }
        throw new RuntimeException();
    }

    @Override
    protected <K extends Comparable<K>, V> Multimap<K, V> mapFill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        switch (containerType) {
            case SEQ:
                return TreeMultimap.withSeq().fill(n, s);
            case SET:
                return TreeMultimap.withSet().fill(n, s);
            case SORTED_SET:
                return TreeMultimap.withSortedSet(naturalComparator()).fill(n, s);
        }
        throw new RuntimeException();
    }

    @Test
    public void shouldCreateSortedMapFrom2Pairs() {
        final Multimap<Integer, Integer> map = TreeMultimap.withSeq().of(1, 2, 2, 4);
        Assertions.assertThat(map.apply(1)).isEqualTo(List.of(2));
        Assertions.assertThat(map.apply(2)).isEqualTo(List.of(4));
    }

    @Test
    public void shouldCreateSortedMapFrom3Pairs() {
        final Multimap<Integer, Integer> map = TreeMultimap.withSeq().of(1, 2, 2, 4, 3, 6);
        Assertions.assertThat(map.apply(1)).isEqualTo(List.of(2));
        Assertions.assertThat(map.apply(2)).isEqualTo(List.of(4));
        Assertions.assertThat(map.apply(3)).isEqualTo(List.of(6));
    }

    @Test
    public void shouldCreateSortedMapFrom4Pairs() {
        final Multimap<Integer, Integer> map = TreeMultimap.withSeq().of(1, 2, 2, 4, 3, 6, 4, 8);
        Assertions.assertThat(map.apply(1)).isEqualTo(List.of(2));
        Assertions.assertThat(map.apply(2)).isEqualTo(List.of(4));
        Assertions.assertThat(map.apply(3)).isEqualTo(List.of(6));
        Assertions.assertThat(map.apply(4)).isEqualTo(List.of(8));
    }

    @Test
    public void shouldCreateSortedMapFrom5Pairs() {
        final Multimap<Integer, Integer> map = TreeMultimap.withSeq().of(1, 2, 2, 4, 3, 6, 4, 8, 5, 10);
        Assertions.assertThat(map.apply(1)).isEqualTo(List.of(2));
        Assertions.assertThat(map.apply(2)).isEqualTo(List.of(4));
        Assertions.assertThat(map.apply(3)).isEqualTo(List.of(6));
        Assertions.assertThat(map.apply(4)).isEqualTo(List.of(8));
        Assertions.assertThat(map.apply(5)).isEqualTo(List.of(10));
    }

    @Test
    public void shouldCreateSortedMapFrom6Pairs() {
        final Multimap<Integer, Integer> map = TreeMultimap.withSeq().of(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12);
        Assertions.assertThat(map.apply(1)).isEqualTo(List.of(2));
        Assertions.assertThat(map.apply(2)).isEqualTo(List.of(4));
        Assertions.assertThat(map.apply(3)).isEqualTo(List.of(6));
        Assertions.assertThat(map.apply(4)).isEqualTo(List.of(8));
        Assertions.assertThat(map.apply(5)).isEqualTo(List.of(10));
        Assertions.assertThat(map.apply(6)).isEqualTo(List.of(12));
    }

    @Test
    public void shouldCreateSortedMapFrom7Pairs() {
        final Multimap<Integer, Integer> map = TreeMultimap.withSeq().of(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14);
        Assertions.assertThat(map.apply(1)).isEqualTo(List.of(2));
        Assertions.assertThat(map.apply(2)).isEqualTo(List.of(4));
        Assertions.assertThat(map.apply(3)).isEqualTo(List.of(6));
        Assertions.assertThat(map.apply(4)).isEqualTo(List.of(8));
        Assertions.assertThat(map.apply(5)).isEqualTo(List.of(10));
        Assertions.assertThat(map.apply(6)).isEqualTo(List.of(12));
        Assertions.assertThat(map.apply(7)).isEqualTo(List.of(14));
    }

    @Test
    public void shouldCreateSortedMapFrom8Pairs() {
        final Multimap<Integer, Integer> map = TreeMultimap.withSeq().of(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16);
        Assertions.assertThat(map.apply(1)).isEqualTo(List.of(2));
        Assertions.assertThat(map.apply(2)).isEqualTo(List.of(4));
        Assertions.assertThat(map.apply(3)).isEqualTo(List.of(6));
        Assertions.assertThat(map.apply(4)).isEqualTo(List.of(8));
        Assertions.assertThat(map.apply(5)).isEqualTo(List.of(10));
        Assertions.assertThat(map.apply(6)).isEqualTo(List.of(12));
        Assertions.assertThat(map.apply(7)).isEqualTo(List.of(14));
        Assertions.assertThat(map.apply(8)).isEqualTo(List.of(16));
    }

    @Test
    public void shouldCreateSortedMapFrom9Pairs() {
        final Multimap<Integer, Integer> map = TreeMultimap.withSeq().of(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16, 9, 18);
        Assertions.assertThat(map.apply(1)).isEqualTo(List.of(2));
        Assertions.assertThat(map.apply(2)).isEqualTo(List.of(4));
        Assertions.assertThat(map.apply(3)).isEqualTo(List.of(6));
        Assertions.assertThat(map.apply(4)).isEqualTo(List.of(8));
        Assertions.assertThat(map.apply(5)).isEqualTo(List.of(10));
        Assertions.assertThat(map.apply(6)).isEqualTo(List.of(12));
        Assertions.assertThat(map.apply(7)).isEqualTo(List.of(14));
        Assertions.assertThat(map.apply(8)).isEqualTo(List.of(16));
        Assertions.assertThat(map.apply(9)).isEqualTo(List.of(18));
    }

    @Test
    public void shouldCreateSortedMapFrom10Pairs() {
        final Multimap<Integer, Integer> map = TreeMultimap.withSeq().of(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16, 9, 18, 10, 20);
        Assertions.assertThat(map.apply(1)).isEqualTo(List.of(2));
        Assertions.assertThat(map.apply(2)).isEqualTo(List.of(4));
        Assertions.assertThat(map.apply(3)).isEqualTo(List.of(6));
        Assertions.assertThat(map.apply(4)).isEqualTo(List.of(8));
        Assertions.assertThat(map.apply(5)).isEqualTo(List.of(10));
        Assertions.assertThat(map.apply(6)).isEqualTo(List.of(12));
        Assertions.assertThat(map.apply(7)).isEqualTo(List.of(14));
        Assertions.assertThat(map.apply(8)).isEqualTo(List.of(16));
        Assertions.assertThat(map.apply(9)).isEqualTo(List.of(18));
        Assertions.assertThat(map.apply(10)).isEqualTo(List.of(20));
    }

    // -- narrow

    @Test
    public void shouldNarrowMap() {
        final TreeMultimap<Integer, Number> int2doubleMap = (TreeMultimap<Integer, Number>) this.<Integer, Number>emptyMap().put(1, 1.0d);
        final TreeMultimap<Number, Number> number2numberMap = TreeMultimap.narrow(int2doubleMap);
        final int actual = number2numberMap.put(2, new BigDecimal("2.0")).values().sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    @Override
    protected boolean isDistinctElements() {
        return true;
    }
}
