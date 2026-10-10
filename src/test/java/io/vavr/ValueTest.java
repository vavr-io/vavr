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
package io.vavr;

import io.vavr.collection.List;
import io.vavr.collection.PriorityQueue;
import io.vavr.collection.TreeMap;
import io.vavr.collection.TreeMultimap;
import io.vavr.collection.TreeSet;
import io.vavr.control.Option;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValueTest {

    @Test
    public void shouldNarrowValue() {
        final Value<Double> doubles = List.of(1.0d);
        final Value<Number> numbers = Value.narrow(doubles);
        assertThat(numbers.get()).isEqualTo(1.0d);
    }

    @Test
    public void collectWorkAsExpectedMultiValue() {
        final Value<Double> doubles = List.of(1.0d, 2.0d);
        final java.util.List<Double> result = doubles.collect(Collectors.toList());
        assertThat(result).contains(1.0d, 2.0d);
    }

    @Test
    public void verboseCollectWorkAsExpectedMultiValue() {
        final Value<Double> doubles = List.of(1.0d, 2.0d);
        final java.util.List<Double> result = doubles.collect(ArrayList<Double>::new, ArrayList::add, ArrayList::addAll);
        assertThat(result).contains(1.0d, 2.0d);
    }

    @Test
    public void collectWorkAsExpectedSingleValue() {
        final Value<Double> doubles = Option.of(1.0d);
        assertThat(doubles.collect(Collectors.toList()).get(0)).isEqualTo(1.0d);
    }

    @Test
    public void verboseCollectWorkAsExpectedSingleValue() {
        final Value<Double> doubles = Option.of(1.0d);
        assertThat(doubles.collect(ArrayList<Double>::new,
                ArrayList::add, ArrayList::addAll).get(0)).isEqualTo(1.0d);
    }

    // -- toSortedSet() / toPriorityQueue() on key-ordered maps (Ordered<K> but Value<Tuple2<K, V>>)

    @Test
    public void shouldConvertSortedMapWithKeyComparatorToSortedSetUsingNaturalOrderOfEntries() {
        final Comparator<Integer> keyComparator = Comparator.comparingInt(Integer::intValue); // not applicable to Tuple2
        final Value<Tuple2<Integer, String>> map = TreeMap.of(keyComparator.reversed(), 1, "a", 2, "b");
        assertThat(map.toSortedSet()).isEqualTo(TreeSet.of(Tuple.of(1, "a"), Tuple.of(2, "b")));
    }

    @Test
    public void shouldConvertSortedMapWithKeyComparatorToPriorityQueueUsingNaturalOrderOfEntries() {
        final Comparator<Integer> keyComparator = Comparator.comparingInt(Integer::intValue); // not applicable to Tuple2
        final Value<Tuple2<Integer, String>> map = TreeMap.of(keyComparator.reversed(), 1, "a", 2, "b");
        assertThat(map.toPriorityQueue()).isEqualTo(PriorityQueue.of(Tuple.of(1, "a"), Tuple.of(2, "b")));
    }

    @Test
    public void shouldConvertSortedMultimapWithKeyComparatorToSortedSetUsingNaturalOrderOfEntries() {
        final Comparator<Integer> keyComparator = Comparator.comparingInt(Integer::intValue); // not applicable to Tuple2
        final Value<Tuple2<Integer, String>> multimap = TreeMultimap.withSeq().of(keyComparator.reversed(), 1, "a", 1, "b");
        assertThat(multimap.toSortedSet()).isEqualTo(TreeSet.of(Tuple.of(1, "a"), Tuple.of(1, "b")));
    }
}
