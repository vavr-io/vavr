/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
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
        hamt = hamt.put(1, 2).put(4, 5).put(null, 7);
        assertThat(hamt.containsKey(1)).isTrue();
        assertThat(hamt.get(1)).isEqualTo(new Some<>(2));
        assertThat(hamt.containsKey(4)).isTrue();
        assertThat(hamt.get(4)).isEqualTo(new Some<>(5));
        assertThat(hamt.containsKey(null)).isTrue();
        assertThat(hamt.get(null)).isEqualTo(new Some<>(7));
    }

    @Test
    public void testGetUnknownKey() {
        Map<Integer, Integer> hamt = HashMap.empty();
        hamt = hamt.put(1, 2).put(4, 5);
        assertThat(hamt.containsKey(2)).isFalse();
        assertThat(hamt.get(2)).isEqualTo(None.instance());
        assertThat(hamt.containsKey(null)).isFalse();
        assertThat(hamt.get(null)).isEqualTo(None.instance());
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

    @Test
    public void shouldLookupNullInZeroKey() {
        HashArrayMappedTrie<Integer, Integer> trie = HashArrayMappedTrie.empty();
        trie = trie.put(0, 1);    // key.hashCode = 0
        trie = trie.put(null, 2); // key.hashCode = 0
        assertThat(trie.get(0).get()).isEqualTo(1);
        assertThat(trie.get(null).get()).isEqualTo(2);
    }

    // -- equals

    @Test
    public void shouldEqualSameHAMTInstance() {
        final HashArrayMappedTrie<?, ?> trie = HashArrayMappedTrie.empty();
        assertThat(trie).isEqualTo(trie);
    }

    @Test
    public void shouldNilNotEqualsNull() {
        assertThat(HashArrayMappedTrie.empty()).isNotNull();
    }

    @Test
    public void shouldNonNilNotEqualsNull() {
        assertThat(of(1)).isNotNull();
    }

    @Test
    public void shouldEmptyNotEqualsDifferentType() {
        assertThat(HashArrayMappedTrie.empty()).isNotEqualTo("");
    }

    @Test
    public void shouldNonEmptyNotEqualsDifferentType() {
        assertThat(of(1)).isNotEqualTo("");
    }

    @Test
    public void shouldRecognizeEqualityOfNils() {
        assertThat(HashArrayMappedTrie.empty()).isEqualTo(HashArrayMappedTrie.empty());
    }

    @Test
    public void shouldRecognizeEqualityOfNonNils() {
        assertThat(of(1, 2, 3).equals(of(1, 2, 3))).isTrue();
    }

    @Test
    public void shouldRecognizeNonEqualityOfHAMTOfSameSize() {
        assertThat(of(1, 2, 3).equals(of(1, 2, 4))).isFalse();
    }

    @Test
    public void shouldRecognizeNonEqualityOfHAMTOfDifferentSize() {
        assertThat(of(1, 2, 3).equals(of(1, 2))).isFalse();
    }

    // -- hashCode

    @Test
    public void shouldCalculateHashCodeOfNil() {
        assertThat(HashArrayMappedTrie.empty().hashCode() == HashArrayMappedTrie.empty().hashCode()).isTrue();
    }

    @Test
    public void shouldCalculateHashCodeOfNonNil() {
        assertThat(of(1, 2).hashCode() == of(1, 2).hashCode()).isTrue();
    }

    @Test
    public void shouldCalculateDifferentHashCodesForDifferentHAMT() {
        assertThat(of(1, 2).hashCode() != of(2, 3).hashCode()).isTrue();
    }

    @Test
    public void shouldCalculateBigHashCode() {
        HashArrayMappedTrie<Integer, Integer> h1 = HashArrayMappedTrie.empty();
        HashArrayMappedTrie<Integer, Integer> h2 = HashArrayMappedTrie.empty();
        int count = 1234;
        for (int i = 0; i <= count; i++) {
            h1 = h1.put(i, i);
            h2 = h2.put(count - i, count - i);
        }
        assertThat(h1.hashCode() == h2.hashCode()).isTrue();
    }

    // - toString

    @Test
    public void shouldMakeString() {
        assertThat(HashArrayMappedTrie.empty().toString()).isEqualTo("HashArrayMappedTrie()");
        assertThat(HashArrayMappedTrie.empty().put(1, 2).toString()).isEqualTo("HashArrayMappedTrie(1 -> 2)");
    }

    // -- helpers

    private HashArrayMappedTrie<Integer, Integer> of(int... ints) {
        HashArrayMappedTrie<Integer, Integer> h = HashArrayMappedTrie.empty();
        for (int i : ints) {
            h = h.put(h.size(), i);
        }
        return h;
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
            hamt.iterator().forEachRemaining(e -> assertThat(classic.get(e._1)).isEqualTo(e._2));
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
