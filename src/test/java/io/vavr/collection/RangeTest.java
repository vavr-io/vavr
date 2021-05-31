/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2021 Vavr, https://vavr.io
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

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class RangeTest {
    @Test
    public void shallWorkWithForLoop() {
        // This test will pass if it compiles without errors

        for (int i : Range.<Integer>inclusive(1, 3)) {
            System.out.println(i);
        }

        for (long i : Range.<Long>inclusive(1L, 3L)) {
            System.out.println(i);
        }
    }

    @Test
    public void shallGenerateInclusiveRanges() {
        assertThat(Array.ofAll(Range.<Integer>inclusive(1, 3))).isEqualTo(Array.of(1, 2, 3));
        assertThat(Array.ofAll(Range.<Long>inclusive(1L, 3L))).isEqualTo(Array.of(1L, 2L, 3L));
        assertThat(Array.ofAll(Range.<Character>inclusive('a', 'c'))).isEqualTo(Array.of('a', 'b', 'c'));
        // TODO: add tests for all other supported numeric types
    }

    @Test
    public void shallGenerateExclusiveRanges() {
        assertThat(Array.ofAll(Range.<Integer>exclusive(1, 3))).isEqualTo(Array.of(1, 2));
        assertThat(Array.ofAll(Range.<Long>exclusive(1L, 3L))).isEqualTo(Array.of(1L, 2L));
        assertThat(Array.ofAll(Range.<Character>exclusive('a', 'c'))).isEqualTo(Array.of('a', 'b'));
        // TODO: add tests for all other supported numeric types
    }

    @Test
    public void shallGenerateInclusiveRangesByStep() {
        assertThat(Array.ofAll(Range.<Integer>inclusiveBy(1, 5, 2))).isEqualTo(Array.of(1, 3, 5));
        assertThat(Array.ofAll(Range.<Long>inclusiveBy(1L, 5L, 2L))).isEqualTo(Array.of(1L, 3L, 5L));
        assertThat(Array.ofAll(Range.<Character>inclusiveBy('a', 'e', 2))).isEqualTo(Array.of('a', 'c', 'e'));
        // TODO: add tests for all other supported numeric types
    }

    @Test
    public void shallGenerateExclusiveRangesByStep() {
        assertThat(Array.ofAll(Range.<Integer>exclusiveBy(1, 5, 2))).isEqualTo(Array.of(1, 3));
        assertThat(Array.ofAll(Range.<Long>exclusiveBy(1L, 5L, 2L))).isEqualTo(Array.of(1L, 3L));
        assertThat(Array.ofAll(Range.<Character>exclusiveBy('a', 'e', 2))).isEqualTo(Array.of('a', 'c'));
        // TODO: add tests for all other supported numeric types
    }
}