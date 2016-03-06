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
        HashMultimap<Integer, String, Set<String>> hms = HashMultimap.emptyWithSet();
        hms = hms.put(1, "a").put(1, "b").put(1, "b");
        assertThat(hms.toString()).isEqualTo("HashMultimap[Set]((1, a), (1, b))");
    }

    @Test
    public void test2() {
        HashMultimap<Integer, String, Seq<String>> hms = HashMultimap.emptyWithSeq();
        hms = hms.put(1, "a").put(1, "b").put(1, "b");
        assertThat(hms.toString()).isEqualTo("HashMultimap[Seq]((1, a), (1, b), (1, b))");
    }

    @Override
    protected String className() {
        return "HashMultimap[Set]";
    }

    @Override
    protected <T1, T2> Multimap<T1, T2, Set<T2>> emptyMap() {
        return HashMultimap.emptyWithSet();
    }

    @Override
    protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Multimap<Integer, T, Set<T>>> mapCollector() {
        return HashMultimap.collector();
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Multimap<K, V, Set<V>> mapOfTuples(Tuple2<? extends K, ? extends V>... entries) {
        return HashMultimap.ofEntries(entries);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Multimap<K, V, Set<V>> mapOfEntries(Map.Entry<? extends K, ? extends V>... entries) {
        return HashMultimap.ofEntries(entries);
    }

    @Override
    protected <K, V> Multimap<K, V, Set<V>> mapOfPairs(Object... pairs) {
        return HashMultimap.of(pairs);
    }

    @Override
    protected <K extends Comparable<? super K>, V> Multimap<K, V, Set<V>> mapOf(K key, V value) {
        return HashMultimap.<K, V>emptyWithSet().put(key, value);
    }

    @Override
    protected <K, V> Multimap<K, V, Set<V>> mapTabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        return HashMultimap.tabulate(n, f);
    }

    @Override
    protected <K, V> Multimap<K, V, Set<V>> mapFill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        return HashMultimap.fill(n, s);
    }
}
