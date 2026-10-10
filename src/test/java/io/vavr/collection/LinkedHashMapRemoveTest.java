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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkedHashMapRemoveTest {

    // -- performance: remove(key) must not scan the insertion-order structure

    @Test
    public void shouldRemoveInReverseOrderInSubQuadraticTime() {
        final int n = 50_000;
        LinkedHashMap<Integer, Integer> map = LinkedHashMap.empty();
        for (int i = 0; i < n; i++) {
            map = map.put(i, i);
        }
        final long start = System.nanoTime();
        LinkedHashMap<Integer, Integer> result = map;
        for (int i = n - 1; i >= 0; i--) {
            result = result.remove(i);
        }
        final long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        assertThat(result.isEmpty()).isTrue();
        // O(n) per remove takes seconds here; O(log n) takes tens of milliseconds.
        // The bound is deliberately loose to stay robust on slow CI machines.
        assertThat(elapsedMs).isLessThan(2_000);
    }

    // -- semantics: random interleaving must match java.util.LinkedHashMap

    @Test
    public void shouldMatchJavaLinkedHashMapUnderRandomPutRemoveInterleaving() {
        for (long seed = 0; seed < 5; seed++) {
            final Random random = new Random(seed);
            LinkedHashMap<Integer, Integer> actual = LinkedHashMap.empty();
            final java.util.LinkedHashMap<Integer, Integer> expected = new java.util.LinkedHashMap<>();
            for (int op = 0; op < 5_000; op++) {
                final int key = random.nextInt(200);
                if (random.nextInt(3) == 0) {
                    actual = actual.remove(key);
                    expected.remove(key);
                } else {
                    actual = actual.put(key, op);
                    expected.put(key, op);
                }
            }
            assertThat(actual.size()).isEqualTo(expected.size());
            final java.util.Iterator<java.util.Map.Entry<Integer, Integer>> expectedIterator = expected.entrySet().iterator();
            for (Tuple2<Integer, Integer> entry : actual) {
                final java.util.Map.Entry<Integer, Integer> expectedEntry = expectedIterator.next();
                assertThat(entry._1).isEqualTo(expectedEntry.getKey());
                assertThat(entry._2).isEqualTo(expectedEntry.getValue());
            }
            assertThat(expectedIterator.hasNext()).isFalse();
        }
    }

    @Test
    public void shouldKeepHeadAndLastConsistentWhileRemovingFromBothEnds() {
        final int n = 1_001;
        LinkedHashMap<Integer, Integer> map = LinkedHashMap.empty();
        for (int i = 0; i < n; i++) {
            map = map.put(i, i);
        }
        int lo = 0, hi = n - 1;
        while (lo < hi) {
            assertThat(map.head()).isEqualTo(Tuple.of(lo, lo));
            assertThat(map.last()).isEqualTo(Tuple.of(hi, hi));
            map = map.remove(lo++).remove(hi--);
        }
        assertThat(map.size()).isEqualTo(1);
        assertThat(map.head()).isEqualTo(map.last());
    }

    @Test
    public void shouldPreserveOrderAfterRemovingEveryOtherKey() {
        final int n = 1_000;
        LinkedHashMap<Integer, Integer> map = LinkedHashMap.empty();
        for (int i = 0; i < n; i++) {
            map = map.put(i, i);
        }
        for (int i = 0; i < n; i += 2) {
            map = map.remove(i);
        }
        assertThat(map.keysIterator().toJavaList())
                .isEqualTo(Iterator.range(0, n).filter(i -> i % 2 == 1).toJavaList());
        assertThat(map.head()).isEqualTo(Tuple.of(1, 1));
        assertThat(map.last()).isEqualTo(Tuple.of(n - 1, n - 1));
    }

    @Test
    public void shouldPreserveInsertionPointWhenRemovedKeyIsReinserted() {
        LinkedHashMap<String, Integer> map = LinkedHashMap.of("a", 1, "b", 2, "c", 3)
                .remove("b")
                .put("b", 4);
        assertThat(map.keysIterator().toJavaList()).containsExactly("a", "c", "b");
    }

    @Test
    public void shouldSupportTailAndInitAfterInteriorRemovals() {
        LinkedHashMap<Integer, Integer> map = LinkedHashMap.empty();
        for (int i = 0; i < 100; i++) {
            map = map.put(i, i);
        }
        for (int i = 10; i < 90; i += 3) {
            map = map.remove(i);
        }
        final java.util.List<Integer> keys = map.keysIterator().toJavaList();
        assertThat(map.tail().keysIterator().toJavaList()).isEqualTo(keys.subList(1, keys.size()));
        assertThat(map.init().keysIterator().toJavaList()).isEqualTo(keys.subList(0, keys.size() - 1));
    }

    // -- serialization must survive interior removals and preserve order

    @Test
    public void shouldSerializeAndDeserializePreservingOrderAfterRemovals() throws Exception {
        LinkedHashMap<Integer, Integer> map = LinkedHashMap.empty();
        for (int i = 0; i < 100; i++) {
            map = map.put(i, i);
        }
        for (int i = 0; i < 100; i += 4) {
            map = map.remove(i);
        }
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(map);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            @SuppressWarnings("unchecked")
            final LinkedHashMap<Integer, Integer> deserialized = (LinkedHashMap<Integer, Integer>) in.readObject();
            assertThat(deserialized.keysIterator().toJavaList()).isEqualTo(map.keysIterator().toJavaList());
            assertThat(deserialized).isEqualTo(map);
        }
    }
}
