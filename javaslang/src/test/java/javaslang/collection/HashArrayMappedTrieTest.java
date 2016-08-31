/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;
import org.junit.Test;

import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class HashArrayMappedTrieTest {

    @Test
    public void testGetExistingKey() {
        HashArrayMappedTrie<Integer, Integer> hamt = empty();
        hamt = hamt.put(1, 2).put(4, 5).put(null, 7);
        assertThat(hamt.containsKey(1)).isTrue();
        assertThat(hamt.get(1)).isEqualTo(Option.some(2));
        assertThat(hamt.getOrElse(1, 42)).isEqualTo(2);
        assertThat(hamt.containsKey(4)).isTrue();
        assertThat(hamt.get(4)).isEqualTo(Option.some(5));
        assertThat(hamt.containsKey(null)).isTrue();
        assertThat(hamt.get(null)).isEqualTo(Option.some(7));
    }

    @Test
    public void testGetUnknownKey() {
        HashArrayMappedTrie<Integer, Integer> hamt = empty();
        hamt = hamt.put(1, 2).put(4, 5);
        assertThat(hamt.containsKey(2)).isFalse();
        assertThat(hamt.get(2)).isEqualTo(Option.none());
        assertThat(hamt.getOrElse(2, 42)).isEqualTo(42);
        assertThat(hamt.containsKey(null)).isFalse();
        assertThat(hamt.get(null)).isEqualTo(Option.none());
    }

    @Test
    public void testRemoveFromEmpty() {
        HashArrayMappedTrie<Integer, Integer> hamt = empty();
        hamt = hamt.remove(1);
        assertThat(hamt.size()).isEqualTo(0);
    }

    @Test
    public void testRemoveUnknownKey() {
        HashArrayMappedTrie<Integer, Integer> hamt = empty();
        hamt = hamt.put(1, 2).remove(3);
        assertThat(hamt.size()).isEqualTo(1);
        hamt = hamt.remove(1);
        assertThat(hamt.size()).isEqualTo(0);
    }

    @Test
    public void testDeepestTree() {
        HashArrayMappedTrie<Integer, Integer> hamt = empty();
        List<Integer> ints = List.tabulate(Integer.SIZE, i -> 1 << i).sorted();
        hamt = ints.foldLeft(hamt, (h, i) -> h.put(i, i));
        assertThat(List.ofAll(hamt.keysIterator()).sorted()).isEqualTo(ints);
    }

    @Test
    public void testBigData() {
        testBigData(5000, t -> t);
    }

    @Test
    public void testBigDataWeakHashCode() {
        testBigData(5000, t -> Tuple.of(new WeakInteger(t._1), t._2));
    }

    private <K extends Comparable<? super K>, V> void testBigData(int count, Function<Tuple2<Integer, Integer>, Tuple2<K, V>> mapper) {
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
        HashArrayMappedTrie<Integer, Integer> trie = empty();
        // should contain all node types
        for (int i = 0; i < 5000; i++) {
            trie = trie.put(i, i);
        }
        trie = trie.put(null, 2);
        assertThat(trie.get(0).get()).isEqualTo(0);     // key.hashCode = 0
        assertThat(trie.get(null).get()).isEqualTo(2);  // key.hashCode = 0
    }

    // -- equals

    @Test
    public void shouldEqualSameHAMTInstance() {
        HashArrayMappedTrie<Integer, Integer> trie = empty();
        assertThat(trie.equals(trie)).isTrue();
    }

    @Test
    public void shouldEmptyNotEqualsDifferentType() {
        assertThat(empty().equals("")).isFalse();
    }

    @Test
    public void shouldNonEmptyNotEqualsDifferentType() {
        assertThat(of(1).equals("")).isFalse();
    }

    @Test
    public void shouldRecognizeEqualityOfNils() {
        assertThat(empty().equals(empty())).isTrue();
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
        assertThat(of(1, 2).equals(of(1, 2, 3))).isFalse();
    }

    // -- hashCode

    @Test
    public void shouldCheckHashCodeInLeafList() {
        HashArrayMappedTrie<Integer, Integer> trie = empty();
        trie = trie.put(0, 1).put(null, 2);       // LeafList.hash == 0
        Option<Integer> none = trie.get(1 << 6);  // (key.hash & BUCKET_BITS) == 0
        assertThat(none).isEqualTo(Option.none());
    }

    @Test
    public void shouldCalculateHashCodeOfNil() {
        assertThat(empty().hashCode()).isEqualTo(0);
    }

    @Test
    public void shouldCalculateHashCodeOfCollision() {
        assertThat(empty().put(null, 1).put(0, 2).hashCode()).isEqualTo(empty().put(null, 1).put(0, 2).hashCode());
    }

    @Test
    public void shouldCalculateDifferentHashCodesForDifferentHAMT() {
        assertThat(of(1, 2).hashCode()).isNotEqualTo(of(2, 3).hashCode());
    }

    @Test
    public void test() {
        System.out.printf("#(0): %d%n", Objects.hash(0));
        System.out.printf("#(0, 1): %d%n", Objects.hash(0, 1));
        System.out.printf("#(1): %d%n", Objects.hash(1));
        System.out.printf("#(1, 2): %d%n", Objects.hash(1, 2));
        System.out.printf("#(2): %d%n", Objects.hash(2));
        System.out.printf("#(2, 3): %d%n", Objects.hash(2, 3));
        System.out.printf("0 ^ #(1, 2): %d%n", (0 ^ Objects.hash(1, 2)));
        System.out.printf("0 ^ #(2, 3): %d%n", (0 ^ Objects.hash(2, 3)));

    }

    @Test
    public void shouldCalculateBigHashCode() {
        HashArrayMappedTrie<Integer, Integer> h1 = empty();
        HashArrayMappedTrie<Integer, Integer> h2 = empty();
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
        assertThat(empty().toString()).isEqualTo("HashArrayMappedTrie()");
        assertThat(empty().put(1, 2).toString()).isEqualTo("HashArrayMappedTrie(1 -> 2)");
    }

    // -- helpers

    private HashArrayMappedTrie<Integer, Integer> of(int... ints) {
        HashArrayMappedTrie<Integer, Integer> h = empty();
        for (int i : ints) {
            h = h.put(h.size(), i);
        }
        return h;
    }

    private <K, V> HashArrayMappedTrie<K, V> empty() {
        return HashArrayMappedTrie.empty();
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
