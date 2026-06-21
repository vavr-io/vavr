/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
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
import java.util.ArrayList;
import java.util.Random;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Behavioral spec for the experimental CHAMP-based {@link HashArrayMappedTrie} implementation.
 *
 * The strongest oracle is the existing, battle-tested HAMT ({@link HashArrayMappedTrie#empty()}):
 * for any sequence of operations, CHAMP must agree with it on size, lookups and iteration content.
 */
public class ChampTrieTest {

    private static <K, V> HashArrayMappedTrie<K, V> champ() {
        return CompressedHashArrayMappedPrefixTrie.empty();
    }

    private static <K, V> HashArrayMappedTrie<K, V> hamt() {
        return HashArrayMappedTrie.empty();
    }

    @Test
    public void emptyHasNoElements() {
        final HashArrayMappedTrie<Integer, Integer> t = champ();
        assertThat(t.isEmpty()).isTrue();
        assertThat(t.size()).isEqualTo(0);
        assertThat(t.get(1)).isEqualTo(Option.none());
        assertThat(t.getOrElse(1, 42)).isEqualTo(42);
        assertThat(t.containsKey(1)).isFalse();
        assertThat(t.iterator().hasNext()).isFalse();
    }

    @Test
    public void putThenGetSingle() {
        final HashArrayMappedTrie<Integer, Integer> t = CompressedHashArrayMappedPrefixTrie.<Integer, Integer>empty().put(1, 2);
        assertThat(t.isEmpty()).isFalse();
        assertThat(t.size()).isEqualTo(1);
        assertThat(t.get(1)).isEqualTo(Option.some(2));
        assertThat(t.getEntry(1)).isEqualTo(Option.some(Tuple.of(1, 2)));
        assertThat(t.getOrElse(1, 42)).isEqualTo(2);
        assertThat(t.containsKey(1)).isTrue();
    }

    @Test
    public void putOverwritesExistingKey() {
        final HashArrayMappedTrie<Integer, Integer> t = CompressedHashArrayMappedPrefixTrie.<Integer, Integer>empty().put(1, 2).put(1, 3);
        assertThat(t.size()).isEqualTo(1);
        assertThat(t.get(1)).isEqualTo(Option.some(3));
    }

    @Test
    public void putAdoptsTheReplacingKeyInstance() {
        // Two equal-but-distinct keys; vavr's HashArrayMappedTrie surfaces the *replacing*
        // key instance after an overwrite, even when the value is equal.
        final WeakInteger first = new WeakInteger(1);
        final WeakInteger replacingNewValue = new WeakInteger(1);
        final WeakInteger replacingEqualValue = new WeakInteger(1);

        HashArrayMappedTrie<WeakInteger, String> t = CompressedHashArrayMappedPrefixTrie.empty();
        t = t.put(first, "a").put(replacingNewValue, "b");
        assertThat(t.size()).isEqualTo(1);
        assertThat(t.getEntry(first).get()._1).isSameAs(replacingNewValue);
        assertThat(t.getEntry(first).get()._2).isEqualTo("b");

        t = t.put(replacingEqualValue, "b"); // equal value must still adopt the new key instance
        assertThat(t.getEntry(first).get()._1).isSameAs(replacingEqualValue);
    }

    @Test
    public void putIsImmutable() {
        final HashArrayMappedTrie<Integer, Integer> a = CompressedHashArrayMappedPrefixTrie.<Integer, Integer>empty().put(1, 1);
        final HashArrayMappedTrie<Integer, Integer> b = a.put(2, 2);
        assertThat(a.containsKey(2)).isFalse();
        assertThat(b.containsKey(2)).isTrue();
        assertThat(a.size()).isEqualTo(1);
        assertThat(b.size()).isEqualTo(2);
    }

    @Test
    public void supportsNullKeyAndValue() {
        final HashArrayMappedTrie<Integer, Integer> t = CompressedHashArrayMappedPrefixTrie.<Integer, Integer>empty().put(null, 7).put(3, null);
        assertThat(t.get(null)).isEqualTo(Option.some(7));
        assertThat(t.get(3)).isEqualTo(Option.some(null));
        assertThat(t.containsKey(3)).isTrue();
        assertThat(t.size()).isEqualTo(2);
    }

    @Test
    public void removeShrinksBackToEmpty() {
        HashArrayMappedTrie<Integer, Integer> t = CompressedHashArrayMappedPrefixTrie.<Integer, Integer>empty().put(1, 1).put(2, 2);
        t = t.remove(1);
        assertThat(t.size()).isEqualTo(1);
        assertThat(t.containsKey(1)).isFalse();
        assertThat(t.get(2)).isEqualTo(Option.some(2));
        t = t.remove(2);
        assertThat(t.isEmpty()).isTrue();
        assertThat(t.size()).isEqualTo(0);
    }

    @Test
    public void removeUnknownKeyIsNoOp() {
        final HashArrayMappedTrie<Integer, Integer> t = CompressedHashArrayMappedPrefixTrie.<Integer, Integer>empty().put(1, 1);
        assertThat(t.remove(2).size()).isEqualTo(1);
        assertThat(CompressedHashArrayMappedPrefixTrie.<Integer, Integer>empty().remove(99).size()).isEqualTo(0);
    }

    @Test
    public void handlesHashCollisions() {
        // WeakInteger hashes to value%10, so 1, 11, 21, 31 all collide.
        HashArrayMappedTrie<WeakInteger, Integer> t = champ();
        final int[] keys = {1, 11, 21, 31};
        for (int k : keys) {
            t = t.put(new WeakInteger(k), k);
        }
        assertThat(t.size()).isEqualTo(4);
        for (int k : keys) {
            assertThat(t.get(new WeakInteger(k))).isEqualTo(Option.some(k));
        }
        t = t.remove(new WeakInteger(21));
        assertThat(t.size()).isEqualTo(3);
        assertThat(t.get(new WeakInteger(21))).isEqualTo(Option.none());
        assertThat(t.get(new WeakInteger(11))).isEqualTo(Option.some(11));
    }

    @Test
    public void buildsDeepestTree() {
        // Keys 1<<i share low bits, forcing maximum depth.
        HashArrayMappedTrie<Integer, Integer> t = champ();
        final List<Integer> ints = List.tabulate(Integer.SIZE, i -> 1 << i);
        t = ints.foldLeft(t, (h, i) -> h.put(i, i));
        assertThat(t.size()).isEqualTo(Integer.SIZE);
        assertThat(List.ofAll(t.keysIterator()).sorted()).isEqualTo(ints.sorted());
        for (int i : ints) {
            assertThat(t.get(i)).isEqualTo(Option.some(i));
        }
    }

    @Test
    public void iteratorYieldsAllEntries() {
        HashArrayMappedTrie<Integer, Integer> t = champ();
        for (int i = 0; i < 1000; i++) {
            t = t.put(i, i * 2);
        }
        final java.util.TreeMap<Integer, Integer> seen = new java.util.TreeMap<>();
        t.iterator().forEachRemaining(e -> seen.put(e._1, e._2));
        assertThat(seen.size()).isEqualTo(1000);
        for (int i = 0; i < 1000; i++) {
            assertThat(seen.get(i)).isEqualTo(i * 2);
        }
        assertThat(List.ofAll(t.keysIterator()).sorted()).isEqualTo(List.range(0, 1000));
        assertThat(List.ofAll(t.valuesIterator()).sorted())
                .isEqualTo(List.range(0, 1000).map(i -> i * 2));
    }

    /**
     * The workhorse: drive CHAMP and the trusted HAMT oracle through the same random
     * sequence of puts and removes, asserting they stay observationally identical.
     */
    @Test
    public void differentialAgainstHamtOracle() {
        final Random r = new Random(42);
        for (int trial = 0; trial < 50; trial++) {
            HashArrayMappedTrie<Integer, Integer> ch = champ();
            HashArrayMappedTrie<Integer, Integer> or = hamt();
            final int keySpace = 1 + r.nextInt(2000); // sometimes tiny → lots of churn
            for (int op = 0; op < 4000; op++) {
                final int key = r.nextInt(keySpace);
                if (r.nextInt(3) == 0) {
                    ch = ch.remove(key);
                    or = or.remove(key);
                } else {
                    final int value = r.nextInt();
                    ch = ch.put(key, value);
                    or = or.put(key, value);
                }
                if (op % 250 == 0) {
                    assertSameContent(ch, or);
                }
            }
            assertSameContent(ch, or);
        }
    }

    @Test
    public void differentialWithWeakHashes() {
        final Random r = new Random(7);
        HashArrayMappedTrie<WeakInteger, Integer> ch = champ();
        HashArrayMappedTrie<WeakInteger, Integer> or = hamt();
        for (int op = 0; op < 20000; op++) {
            final WeakInteger key = new WeakInteger(r.nextInt(500));
            if (r.nextInt(3) == 0) {
                ch = ch.remove(key);
                or = or.remove(key);
            } else {
                final int value = r.nextInt();
                ch = ch.put(key, value);
                or = or.put(key, value);
            }
        }
        assertSameContent(ch, or);
    }

    private static <K, V> void assertSameContent(HashArrayMappedTrie<K, V> actual, HashArrayMappedTrie<K, V> oracle) {
        assertThat(actual.size()).isEqualTo(oracle.size());
        assertThat(actual.isEmpty()).isEqualTo(oracle.isEmpty());
        // every oracle entry is present and equal in actual
        oracle.iterator().forEachRemaining(e -> {
            assertThat(actual.containsKey(e._1)).as("containsKey %s", e._1).isTrue();
            assertThat(actual.get(e._1)).as("get %s", e._1).isEqualTo(Option.some(e._2));
        });
        // actual carries no extra entries
        final java.util.List<Tuple2<K, V>> actualEntries = new ArrayList<>();
        actual.iterator().forEachRemaining(actualEntries::add);
        assertThat(actualEntries.size()).isEqualTo(oracle.size());
        actualEntries.forEach(e -> assertThat(oracle.get(e._1)).isEqualTo(Option.some(e._2)));
    }

    private static final class WeakInteger implements Comparable<WeakInteger> {
        final int value;

        WeakInteger(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }
            return value == ((WeakInteger) o).value;
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
}
