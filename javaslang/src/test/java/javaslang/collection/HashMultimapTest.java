package javaslang.collection;

import javaslang.Tuple2;
import org.junit.Test;

import java.util.*;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static org.assertj.core.api.Assertions.assertThat;

public class HashMultimapTest extends AbstractMultimapTest {

    @Test
    public void test1() {
        Multimap<Integer, String> hms = emptyMap();
        hms = hms.put(1, "a").put(1, "b").put(1, "b");
        assertThat(hms.toString()).isEqualTo("Multimap[HashMap,HashSet]((1, a), (1, b))");
    }

    @Test
    public void test2() {
        Multimap<Integer, String> hms = Multimap.empty(Multimap.MapType.HASH_MAP, Multimap.ContainerType.SEQ);
        hms = hms.put(1, "a").put(1, "b").put(1, "b");
        assertThat(hms.toString()).isEqualTo("Multimap[HashMap,List]((1, a), (1, b), (1, b))");
    }

    @Override
    protected String className() {
        return "Multimap[HashMap,HashSet]";
    }

    @Override
    protected <T1, T2> Multimap<T1, T2> emptyMap() {
        return Multimap.empty(Multimap.MapType.HASH_MAP, Multimap.ContainerType.SET);
    }

    @Override
    protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Multimap<Integer, T>> mapCollector() {
        return Multimap.collector(Multimap.MapType.HASH_MAP, Multimap.ContainerType.SET);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Multimap<K, V> mapOfTuples(Tuple2<? extends K, ? extends V>... entries) {
        return Multimap.ofEntries(Multimap.MapType.HASH_MAP, Multimap.ContainerType.SET, entries);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Multimap<K, V> mapOfEntries(Map.Entry<? extends K, ? extends V>... entries) {
        return Multimap.ofEntries(Multimap.MapType.HASH_MAP, Multimap.ContainerType.SET, entries);
    }

    @Override
    protected <K, V> Multimap<K, V> mapOfPairs(Object... pairs) {
        return Multimap.of(Multimap.MapType.HASH_MAP, Multimap.ContainerType.SET, pairs);
    }

    @Override
    protected <K, V> Multimap<K, V> mapOf(K key, V value) {
        final Multimap<K, V> map = Multimap.empty(Multimap.MapType.HASH_MAP, Multimap.ContainerType.SET);
        return map.put(key, value);
    }

    @Override
    protected <K, V> Multimap<K, V> mapTabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        return Multimap.tabulate(Multimap.MapType.HASH_MAP, Multimap.ContainerType.SET, n, f);
    }

    @Override
    protected <K, V> Multimap<K, V> mapFill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        return Multimap.fill(Multimap.MapType.HASH_MAP, Multimap.ContainerType.SET, n, s);
    }

}
