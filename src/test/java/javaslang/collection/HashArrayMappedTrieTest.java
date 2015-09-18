/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.None;
import javaslang.control.Some;
import org.junit.Test;

import java.util.Random;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class HashArrayMappedTrieTest {

    @Test
    public void testGetExistingKey() {
        Map<Integer, Integer> hamt = HashMap.empty();
        hamt = hamt.put(1, 2).put(4, 5);
        assertThat(hamt.get(1)).isEqualTo(new Some<>(2));
        assertThat(hamt.get(4)).isEqualTo(new Some<>(5));
    }

    @Test
    public void testGetUnknownKey() {
        Map<Integer, Integer> hamt = HashMap.empty();
        hamt = hamt.put(1, 2).put(4, 5);
        assertThat(hamt.get(2)).isEqualTo(None.instance());
    }

    @Test
    public void testRemoveFromEmpty() {
        Map<Integer, Integer> hamt = HashMap.empty();
        hamt = hamt.remove(1);
        assertThat(hamt.size()).isEqualTo(0);
    }

    @Test
    public void testRemoveUnknownKey() {
        Map<Integer, Integer> hamt = HashMap.empty();
        hamt = hamt.put(1, 2).remove(3);
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

    private <K extends Comparable<K>, V> void testBigData(int count, Function<Tuple2<Integer, Integer>, Tuple2<K, V>> mapper) {
        Comparator<K, V> cmp = new Comparator<>();
        java.util.Map<K, V> rnd = rnd(count, mapper);
        for (java.util.Map.Entry<K, V> e : rnd.entrySet()) {
            cmp.set(e.getKey(), e.getValue());
        }
        cmp.test();
        for (K key : new java.util.TreeSet<>(rnd.keySet())) {
            rnd.remove(key);
            cmp.remove(key);
        }
        cmp.test();
    }

    private class WeakInteger implements Comparable<WeakInteger> {
        final int value;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WeakInteger that = (WeakInteger) o;
            return value == that.value;
        }

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
        private final java.util.Map<K, V> classic = new java.util.HashMap<>();
        private Map<K, V> hamt = HashMap.empty();

        void test() {
            assertThat(hamt.size()).isEqualTo(classic.size());
            hamt.iterator().forEachRemaining(e -> assertThat(classic.get(e.key)).isEqualTo(e.value));
            classic.forEach((k, v) -> assertThat(hamt.get(k).get()).isEqualTo(v));
        }

        void set(K key, V value) {
            classic.put(key, value);
            hamt = hamt.put(key, value);
        }

        void remove(K key) {
            classic.remove(key);
            hamt = hamt.remove(key);
        }
    }

    private <K, V> java.util.Map<K, V> rnd(int count, Function<Tuple2<Integer, Integer>, Tuple2<K, V>> mapper) {
        Random r = new Random();
        java.util.HashMap<K, V> mp = new java.util.HashMap<>();
        for (int i = 0; i < count; i++) {
            Tuple2<K, V> entry = mapper.apply(Tuple.of(r.nextInt(), r.nextInt()));
            mp.put(entry._1, entry._2);
        }
        return mp;
    }
}
