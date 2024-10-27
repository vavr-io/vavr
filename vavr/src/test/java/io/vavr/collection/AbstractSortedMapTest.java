/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2024 Vavr, https://vavr.io
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

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Spliterator;

public abstract class AbstractSortedMapTest extends AbstractMapTest {

    @Override
    abstract protected <K extends Comparable<? super K>, V> SortedMap<K, V> mapOf(K k1, V v1);

    // -- isOrdered

    @Test
    public void shouldReturnOrdered() {
        final Map<Integer, String> actual = mapOf(1, "1", 1, "2");
        assertThat(actual.isOrdered()).isTrue();
    }

    // -- narrow

    @Test
    public void shouldNarrowMap() {
        final SortedMap<Integer, Double> int2doubleMap = mapOf(1, 1.0d);
        final SortedMap<Integer, Number> number2numberMap = SortedMap.narrow(int2doubleMap);
        final int actual = number2numberMap.put(2, new BigDecimal("2.0")).values().sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    // -- spliterator

    @Test
    public void shouldHaveSortedSpliterator() {
        assertThat(of(1, 2, 3).spliterator().hasCharacteristics(Spliterator.SORTED)).isTrue();
    }

    @Test
    public void shouldHaveOrderedSpliterator() {
        assertThat(of(1, 2, 3).spliterator().hasCharacteristics(Spliterator.ORDERED)).isTrue();
    }

    // -- isSequential()

    @Test
    public void shouldReturnFalseWhenIsSequentialCalled() {
        assertThat(of(1, 2, 3).isSequential()).isFalse();
    }

}
