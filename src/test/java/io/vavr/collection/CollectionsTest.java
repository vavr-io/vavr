/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2020 Vavr, http://vavr.io
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
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionsTest {

    @Test
    public void shouldBeEqualSets() throws Exception {
        forAll(List.of(TreeSet.ofAll(1, 2, 3),
                HashSet.ofAll(1, 2, 3),
                LinkedHashSet.ofAll(1, 2, 3),
                BitSet.ofAll(1, 2, 3)), true);
    }

    @Test
    public void shouldNotBeEqualSets() throws Exception {
        forAll(List.of(HashSet.ofAll(1, 2, 3),
                HashSet.ofAll('a', 'b', 'c')), false);
    }

    @Test
    public void shouldBeEqualSeqs() throws Exception {
        forAll(List.of(Array.ofAll(1, 2, 3),
                Stream.ofAll(1, 2, 3),
                Vector.ofAll(1, 2, 3),
                List.ofAll(1, 2, 3),
                Queue.ofAll(1, 2, 3)), true);
    }

    @Test
    public void shouldNotBeEqualSeqs() throws Exception {
        forAll(List.of(Array.ofAll(1, 2, 3),
                Array.ofAll('a', 'b', 'c')), false);
    }

    @Test
    public void shouldBeEqualMaps() throws Exception {
        forAll(List.of(TreeMap.of(1, 2, 2, 3, 3, 4),
                HashMap.of(1, 2, 2, 3, 3, 4),
                LinkedHashMap.of(1, 2, 2, 3, 3, 4)), true);
    }

    @Test
    public void shouldNotBeEqualMaps() throws Exception {
        forAll(List.of(HashMap.of(1, 2, 2, 3, 3, 4),
                HashMap.of('a', 'b', 'c', 'd', 'e', 'f')), false);
    }

    @Test
    public void shouldBeEqualMultimaps() throws Exception {
        forAll(List.of(TreeMultimap.withSeq().<Integer, Integer>empty().put(1, 1).put(1, 1).put(2, 2),
                HashMultimap.withSeq().<Integer, Integer>empty().put(1, 1).put(1, 1).put(2, 2),
                LinkedHashMultimap.withSeq().<Integer, Integer>empty().put(1, 1).put(1, 1).put(2, 2)), true);
    }

    @Test
    public void shouldNotBeEqualMultimaps() throws Exception {
        forAll(List.of(TreeMultimap.withSeq().<Integer, Integer>empty().put(1, 1).put(1, 1).put(2, 2),
                HashMultimap.withSeq().<Character, Character>empty().put('a', 'b').put('c', 'd').put('e', 'f')),
                false);
    }

    @Test
    public void shouldNotBeEqualSeqAndSet() throws Exception {
        forAll(List.of(Array.ofAll(1, 2, 3),
                TreeSet.ofAll(1, 2, 3)), false);
    }

    @Test
    public void shouldNotBeEqualMapAndSet() throws Exception {
        forAll(List.of(HashSet.of(Tuple.of(1, 2), Tuple.of(2, 3)),
                TreeMap.of(1, 2, 2, 3)), false);
    }

    @Test
    public void shouldUseMergeFunctionToHandleKeysCollisions() {
        Seq<Tuple2<String, Integer>> s = Vector.of(Tuple.of("a",2), Tuple.of("a",55), Tuple.of("a",3), Tuple.of("b",2));

        Map<String, Seq<Integer>> m = s.toMap(
                Tuple2::_1,
                t -> Vector.of(t._2),
                (l1, l2) -> l1.appendAll(l2));

        assertThat(m.containsKey("a")).isTrue();
        assertThat(m.get("a").get().size()).isEqualTo(3);
        assertThat(m.containsKey("b")).isTrue();
        assertThat(m.get("b").get().size()).isEqualTo(1);

        Map<String, Integer> m1 = s.toSet().toMap(
                Tuple2::_1,
                Tuple2::_2,
                (a, b) -> a + b);

        assertThat(m1.containsKey("a")).isTrue();
        assertThat(m1.get("a").get()).isEqualTo(60);
        assertThat(m1.containsKey("b")).isTrue();
        assertThat(m1.get("b").get()).isEqualTo(2);
    }

    private void forAll(List<Traversable<?>> traversables, boolean value) {
        for (Traversable<?> traversable1 : traversables) {
            for (Traversable<?> traversable2 : traversables) {
                if (traversable1 != traversable2) {
                    assertThat(traversable1.equals(traversable2)).isEqualTo(value);
                    if (value) {
                        assertThat(traversable1.hashCode() == traversable2.hashCode()).isTrue();
                    }
                }
            }
        }
    }

}
