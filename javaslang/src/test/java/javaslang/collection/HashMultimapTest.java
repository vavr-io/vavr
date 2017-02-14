package javaslang.collection;

import javaslang.Tuple2;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.*;
import java.util.stream.Stream;

import static javaslang.collection.Comparators.naturalComparator;

public class HashMultimapTest extends AbstractMultimapTest {

    @Override
    protected String className() {
        return "HashMultimap[" + containerName() + "]";
    }

    @Override
    <T1, T2> java.util.Map<T1, T2> javaEmptyMap() {
        return new java.util.HashMap<>();
    }

    @Override
    protected <T1 extends Comparable<T1>, T2> HashMultimap<T1, T2> emptyMap(Comparator<? super T2> comparator) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().empty();
            case SET:
                return HashMultimap.withSet().empty();
            case SORTED_SET:
                return HashMultimap.withSortedSet(comparator).empty();
            default:
                throw new RuntimeException();
        }
    }

    @Override
    protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Multimap<Integer, T>> mapCollector() {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().collector();
            case SET:
                return HashMultimap.withSet().collector();
            case SORTED_SET:
                return HashMultimap.withSortedSet(naturalComparator()).collector();
            default:
                throw new RuntimeException();
        }
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K extends Comparable<K>, V> HashMultimap<K, V> mapOfTuples(Tuple2<? extends K, ? extends V>... entries) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().ofEntries(entries);
            case SET:
                return HashMultimap.withSet().ofEntries(entries);
            case SORTED_SET:
                return HashMultimap.withSortedSet(naturalComparator()).ofEntries(entries);
            default:
                throw new RuntimeException();
        }
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K extends Comparable<K>, V> HashMultimap<K, V> mapOfEntries(java.util.Map.Entry<? extends K, ? extends V>... entries) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().ofEntries(entries);
            case SET:
                return HashMultimap.withSet().ofEntries(entries);
            case SORTED_SET:
                return HashMultimap.withSortedSet(naturalComparator()).ofEntries(entries);
            default:
                throw new RuntimeException();
        }
    }

    @Override
    protected <K extends Comparable<K>, V> HashMultimap<K, V> mapOfPairs(K k1, V v1, K k2, V v2, K k3, V v3) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().of(k1, v1, k2, v2, k3, v3);
            case SET:
                return HashMultimap.withSet().of(k1, v1, k2, v2, k3, v3);
            case SORTED_SET:
                return HashMultimap.withSortedSet(naturalComparator()).of(k1, v1, k2, v2, k3, v3);
            default:
                throw new RuntimeException();
        }
    }

    @Override
    protected <K extends Comparable<K>, V> HashMultimap<K, V> mapOf(K key, V value) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().of(key, value);
            case SET:
                return HashMultimap.withSet().of(key, value);
            case SORTED_SET:
                return HashMultimap.withSortedSet(naturalComparator()).of(key, value);
            default:
                throw new RuntimeException();
        }
    }

    @Override
    protected <K extends Comparable<? super K>, V> HashMultimap<K, V> mapOf(Map<? extends K, ? extends V> map) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().ofAll(map);
            case SET:
                return HashMultimap.withSet().ofAll(map);
            case SORTED_SET:
                return HashMultimap.withSortedSet(naturalComparator()).ofAll(map);
            default:
                throw new RuntimeException();
        }
    }

    @Override
    protected <T, K extends Comparable<? super K>, V> HashMultimap<K, V> mapOf(Stream<? extends T> stream, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().ofAll(stream, keyMapper, valueMapper);
            case SET:
                return HashMultimap.withSet().ofAll(stream, keyMapper, valueMapper);
            case SORTED_SET:
                return HashMultimap.withSortedSet(naturalComparator()).ofAll(stream, keyMapper, valueMapper);
            default:
                throw new RuntimeException();
        }
    }

    @Override
    protected <T, K extends Comparable<? super K>, V> HashMultimap<K, V> mapOf(Stream<? extends T> stream, Function<? super T, Tuple2<? extends K, ? extends V>> f) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().ofAll(stream, f);
            case SET:
                return HashMultimap.withSet().ofAll(stream, f);
            case SORTED_SET:
                return HashMultimap.withSortedSet(naturalComparator()).ofAll(stream, f);
            default:
                throw new RuntimeException();
        }
    }

    @Override
    protected <K extends Comparable<K>, V> HashMultimap<K, V> mapTabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().tabulate(n, f);
            case SET:
                return HashMultimap.withSet().tabulate(n, f);
            case SORTED_SET:
                return HashMultimap.withSortedSet(naturalComparator()).tabulate(n, f);
            default:
                throw new RuntimeException();
        }
    }

    @Override
    protected <K extends Comparable<K>, V> HashMultimap<K, V> mapFill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().fill(n, s);
            case SET:
                return HashMultimap.withSet().fill(n, s);
            case SORTED_SET:
                return HashMultimap.withSortedSet(naturalComparator()).fill(n, s);
            default:
                throw new RuntimeException();
        }
    }

    @Test
    public void shouldCreateSortedMapFrom2Pairs() {
        final Multimap<Integer, Integer> map = HashMultimap.withSeq().of(1, 2, 2, 4);
        Assertions.assertThat(map.apply(1)).isEqualTo(List.of(2));
        Assertions.assertThat(map.apply(2)).isEqualTo(List.of(4));
    }

    @Test
    public void shouldCreateSortedMapFrom3Pairs() {
        final Multimap<Integer, Integer> map = HashMultimap.withSeq().of(1, 2, 2, 4, 3, 6);
        Assertions.assertThat(map.apply(1)).isEqualTo(List.of(2));
        Assertions.assertThat(map.apply(2)).isEqualTo(List.of(4));
        Assertions.assertThat(map.apply(3)).isEqualTo(List.of(6));
    }

    @Test
    public void shouldCreateSortedMapFrom4Pairs() {
        final Multimap<Integer, Integer> map = HashMultimap.withSeq().of(1, 2, 2, 4, 3, 6, 4, 8);
        Assertions.assertThat(map.apply(1)).isEqualTo(List.of(2));
        Assertions.assertThat(map.apply(2)).isEqualTo(List.of(4));
        Assertions.assertThat(map.apply(3)).isEqualTo(List.of(6));
        Assertions.assertThat(map.apply(4)).isEqualTo(List.of(8));
    }

    @Test
    public void shouldCreateSortedMapFrom5Pairs() {
        final Multimap<Integer, Integer> map = HashMultimap.withSeq().of(1, 2, 2, 4, 3, 6, 4, 8, 5, 10);
        Assertions.assertThat(map.apply(1)).isEqualTo(List.of(2));
        Assertions.assertThat(map.apply(2)).isEqualTo(List.of(4));
        Assertions.assertThat(map.apply(3)).isEqualTo(List.of(6));
        Assertions.assertThat(map.apply(4)).isEqualTo(List.of(8));
        Assertions.assertThat(map.apply(5)).isEqualTo(List.of(10));
    }

    @Test
    public void shouldCreateSortedMapFrom6Pairs() {
        final Multimap<Integer, Integer> map = HashMultimap.withSeq().of(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12);
        Assertions.assertThat(map.apply(1)).isEqualTo(List.of(2));
        Assertions.assertThat(map.apply(2)).isEqualTo(List.of(4));
        Assertions.assertThat(map.apply(3)).isEqualTo(List.of(6));
        Assertions.assertThat(map.apply(4)).isEqualTo(List.of(8));
        Assertions.assertThat(map.apply(5)).isEqualTo(List.of(10));
        Assertions.assertThat(map.apply(6)).isEqualTo(List.of(12));
    }

    @Test
    public void shouldCreateSortedMapFrom7Pairs() {
        final Multimap<Integer, Integer> map = HashMultimap.withSeq().of(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14);
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
        final Multimap<Integer, Integer> map = HashMultimap.withSeq().of(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16);
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
        final Multimap<Integer, Integer> map = HashMultimap.withSeq().of(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16, 9, 18);
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
        final Multimap<Integer, Integer> map = HashMultimap.withSeq().of(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16, 9, 18, 10, 20);
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

    // -- static narrow

    @Test
    public void shouldNarrowMap() {
        final HashMultimap<Integer, Number> int2doubleMap = (HashMultimap<Integer, Number>) this.<Integer, Number> emptyMap().put(1, 1.0d);
        final HashMultimap<Number, Number> number2numberMap = HashMultimap.narrow(int2doubleMap);
        final int actual = number2numberMap.put(new BigDecimal("2"), new BigDecimal("2.0")).values().sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

}
