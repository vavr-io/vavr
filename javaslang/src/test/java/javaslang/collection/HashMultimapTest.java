package javaslang.collection;

import javaslang.Tuple2;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class HashMultimapTest extends AbstractMultimapTest {

    @Override
    protected String className() {
        return "Multimap[HashMap," + containerType.empty().stringPrefix() + "]";
    }

    @Override
    protected <T1, T2> Multimap<T1, T2> emptyMap() {
        return HashMultimap.empty(containerType);
    }

    @Override
    protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Multimap<Integer, T>> mapCollector() {
        return HashMultimap.collector(containerType);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Multimap<K, V> mapOfTuples(Tuple2<? extends K, ? extends V>... entries) {
        return HashMultimap.ofEntries(containerType, entries);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Multimap<K, V> mapOfEntries(Map.Entry<? extends K, ? extends V>... entries) {
        return HashMultimap.ofEntries(containerType, entries);
    }

    @Override
    protected <K, V> Multimap<K, V> mapOfPairs(Object... pairs) {
        return HashMultimap.of(containerType, pairs);
    }

    @Override
    protected <K, V> Multimap<K, V> mapOf(K key, V value) {
        final Multimap<K, V> map = HashMultimap.empty(containerType);
        return map.put(key, value);
    }

    @Override
    protected <K, V> Multimap<K, V> mapTabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        return HashMultimap.tabulate(containerType, n, f);
    }

    @Override
    protected <K, V> Multimap<K, V> mapFill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        return HashMultimap.fill(containerType, n, s);
    }

    /////////////////////////////////////////////////////////////////

    @Test
    public void test1() {
        Multimap<Integer, String> multimap = emptyMap();
        multimap = multimap.put(1, "a").put(1, "b").put(1, "b");
        if(containerType == Multimap.ContainerType.SEQ) {
            assertThat(multimap.toString()).isEqualTo(className() + "((1, a), (1, b), (1, b))");
        } else {
            assertThat(multimap.toString()).isEqualTo(className() + "((1, a), (1, b))");
        }
    }

    // -- narrow

    @Test
    public void shouldNarrowMap() {
        final HashMultimap<Integer, Number> int2doubleMap = HashMultimap.<Integer, Number>empty(containerType).put(1, 1.0d);
        final HashMultimap<Number, Number> number2numberMap = HashMultimap.narrow(int2doubleMap);
        final int actual = number2numberMap.put(new BigDecimal("2"), new BigDecimal("2.0")).values().sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

}
