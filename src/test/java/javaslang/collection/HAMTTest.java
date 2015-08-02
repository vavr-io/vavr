package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class HAMTTest {

    @Test
    public void testGetExistingKey() {
        HashArrayMappedTrie<Integer,Integer> hamt = HashArrayMappedTrie.empty();
        hamt = hamt.add(1, 2).add(4, 5);
        Option<Integer> o1 = hamt.get(1);
        assertThat(o1.isDefined()).isTrue();
        assertThat(o1.get()).isEqualTo(2);
        Option<Integer> o2 = hamt.get(4);
        assertThat(o2.isDefined()).isTrue();
        assertThat(o2.get()).isEqualTo(5);
    }

    @Test
    public void testGetUnknownKey() {
        HashArrayMappedTrie<Integer,Integer> hamt = HashArrayMappedTrie.empty();
        hamt = hamt.add(1, 2).add(4, 5);
        assertThat(hamt.get(2).isDefined()).isFalse();
    }

    @Test
    public void testRemoveFromEmpty() {
        HashArrayMappedTrie<Integer,Integer> hamt = HashArrayMappedTrie.empty();
        hamt = hamt.remove(1);
        assertThat(hamt.size()).isEqualTo(0);
    }

    @Test
    public void testRemoveUnknownKey() {
        HashArrayMappedTrie<Integer,Integer> hamt = HashArrayMappedTrie.empty();
        hamt = hamt.add(1, 2).remove(3);
        assertThat(hamt.size()).isEqualTo(1);
        hamt = hamt.remove(1);
        assertThat(hamt.size()).isEqualTo(0);
    }

    @Test
    public void testBigData() {
        testBigData(5000, t -> t);
    }

    @Test
    public void testBigDataWeakHashCode() {
        testBigData(5000, t -> Tuple.of(new WeakInteger(t._1), t._2));
    }

    private <K extends Comparable<K>, V> void testBigData(int count, Function<Tuple2<Integer,Integer>, Tuple2<K, V>> mapper) {
        Comparator<K, V> cmp = new Comparator<>();
        Map<K, V> rnd = rnd(count, mapper);
        for (Map.Entry<K, V> e : rnd.entrySet()) {
            cmp.set(e.getKey(), e.getValue());
        }
        cmp.test();
        for (K key: new TreeSet<>(rnd.keySet())) {
            rnd.remove(key);
            cmp.remove(key);
        }
        cmp.test();
    }

    private class WeakInteger implements Comparable<WeakInteger> {
        final int value;

        WeakInteger(int value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return Math.abs(value) % 10;
        }

        @Override
        public int compareTo(WeakInteger other) {
            return Integer.compare(value, other.value);
        }
    }

    private class Comparator<K, V> {
        private final Map<K, V> classic = new HashMap<>();
        private HashArrayMappedTrie<K, V> hamt = HashArrayMappedTrie.empty();

        void test() {
            assertThat(hamt.size()).isEqualTo(classic.size());
            for (Map.Entry<K, V> e : classic.entrySet()) {
                Option<V> f = hamt.get(e.getKey());
                assertThat(f.isDefined()).isTrue();
                assertThat(f.get()).isEqualTo(e.getValue());
            }
        }

        void set(K key, V value) {
            classic.put(key, value);
            hamt = hamt.add(key, value);
        }

        void remove(K key) {
            classic.remove(key);
            hamt = hamt.remove(key);
        }
    }

    private <K, V> Map<K, V> rnd(int count, Function<Tuple2<Integer,Integer>, Tuple2<K, V>> mapper) {
        Random r = new Random();
        HashMap<K, V> mp = new HashMap<>();
        for (int i = 0; i < count; i++) {
            Tuple2<K, V> entry = mapper.apply(Tuple.of(r.nextInt(), r.nextInt()));
            mp.put(entry._1, entry._2);
        }
        return mp;
    }
}
