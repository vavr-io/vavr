/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import java.util.Random;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HashArrayMappedTrieTest {

    @Test
    public void testLeafSingleton() {
        HashArrayMappedTrie<WeakInteger, Integer> hamt = empty();
        hamt = hamt.put(new WeakInteger(1), 1);
        assertThat(hamt.get(new WeakInteger(1))).isEqualTo(Option.some(1));
        assertThat(hamt.get(new WeakInteger(11))).isEqualTo(Option.none());
        assertThat(hamt.getOrElse(new WeakInteger(1), 2)).isEqualTo(1);
        assertThat(hamt.getOrElse(new WeakInteger(11), 2)).isEqualTo(2);
        assertThat(hamt.get(new WeakInteger(2))).isEqualTo(Option.none());
        assertThat(hamt.getOrElse(new WeakInteger(2), 2)).isEqualTo(2);
    }

    @Test
    public void testLeafList() {
        HashArrayMappedTrie<WeakInteger, Integer> hamt = empty();
        hamt = hamt.put(new WeakInteger(1), 1).put(new WeakInteger(31), 31);
        assertThat(hamt.get(new WeakInteger(1))).isEqualTo(Option.some(1));
        assertThat(hamt.get(new WeakInteger(11))).isEqualTo(Option.none());
        assertThat(hamt.get(new WeakInteger(31))).isEqualTo(Option.some(31));
        assertThat(hamt.getOrElse(new WeakInteger(1), 2)).isEqualTo(1);
        assertThat(hamt.getOrElse(new WeakInteger(11), 2)).isEqualTo(2);
        assertThat(hamt.getOrElse(new WeakInteger(31), 2)).isEqualTo(31);
        assertThat(hamt.get(new WeakInteger(2))).isEqualTo(Option.none());
        assertThat(hamt.getOrElse(new WeakInteger(2), 2)).isEqualTo(2);
    }

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
        assertThat(hamt.get(2)).isEqualTo(Option.none());
        assertThat(hamt.getOrElse(2, 42)).isEqualTo(42);
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
        final List<Integer> ints = List.tabulate(Integer.SIZE, i -> 1 << i).sorted();
        HashArrayMappedTrie<Integer, Integer> hamt = empty();
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
        final Comparator<K, V> cmp = new Comparator<>();
        final java.util.Map<K, V> rnd = rnd(count, mapper);
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
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }
            final WeakInteger that = (WeakInteger) o;
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

    private final class Comparator<K, V> {
        private final java.util.Map<K, V> classic = new java.util.HashMap<>();
        private Map<K, V> hamt = HashMap.empty();

        void test() {
            assertThat(hamt.size()).isEqualTo(classic.size());
            hamt.iterator().forEachRemaining(e -> assertThat(classic.get(e._1)).isEqualTo(e._2));
            classic.forEach((k, v) -> {
                assertThat(hamt.get(k).get()).isEqualTo(v);
                assertThat(hamt.getOrElse(k, null)).isEqualTo(v);
            });
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
        final Random r = new Random();
        final java.util.HashMap<K, V> mp = new java.util.HashMap<>();
        for (int i = 0; i < count; i++) {
            final Tuple2<K, V> entry = mapper.apply(Tuple.of(r.nextInt(), r.nextInt()));
            mp.put(entry._1, entry._2);
        }
        return mp;
    }
}
