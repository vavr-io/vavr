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
    // Byte
    private static Byte byteFrom = Byte.valueOf((byte)1);
    private static Byte byteTo = Byte.valueOf((byte)3);
    private static Byte byteStep = Byte.valueOf((byte)1);
    // Short
    private static Short shortFrom = Short.valueOf((short)1);
    private static Short shortTo = Short.valueOf((short)3);
    private static Short shortStep = Short.valueOf((short)1);
    // Integer
    private static Integer integerFrom = 1;
    private static Integer integerTo = 3;
    private static Integer integerStep = 1;
    // Long
    private static Long longFrom = 1L;
    private static Long longTo = 3L;
    private static Long longStep = 1L;
    // Character
    private static Character characterFrom = 'a';
    private static Character characterTo = 'c';
    private static Character characterStep = 1;
    // Float
    private static Float floatFrom = 1.0f;
    private static Float floatTo = 3.0f;
    private static Float floatStep = 1.0f;
    // Double
    private static Double doubleFrom = 1.0d;
    private static Double doubleTo = 3.0d;
    private static Double doubleStep = 1.0d;
    // BigDecimal
    private static BigDecimal decimalFrom = BigDecimal.valueOf(1);
    private static BigDecimal decimalTo = BigDecimal.valueOf(3);
    private static BigDecimal decimalStep = BigDecimal.valueOf(1);

    @Test
    public void shallWorkWithForLoop() {
        // This test will pass if it compiles without errors
        for (int i : Range.inclusive(0, 2)) {
            System.out.println(i);
        }
    }

    @Test
    public void shallWorkWithAllNumericTypes() {
        // This test will pass of it compiles without errors.
        // Numeric types to support: Byte, Short, Integer, Long, Character, Float, Double, BigDecimal.

        // .inclusive()
        Range.inclusive(integerFrom, integerTo);
        // TODO: add tests for all other supported numeric types

        // .inclusiveBy()
        Range.inclusiveBy(integerFrom, integerTo, integerStep);
        // TODO: add tests for all other supported numeric types

        // .exclusive()
        Range.exclusive(integerFrom, integerTo);
        // TODO: add tests for all other supported numeric types

        // . exclusiveBy()
        Range.exclusiveBy(integerFrom, integerTo, integerStep);
        // TODO: add tests for all other supported numeric types
    }

    @Test
    public void shallGenerateIncusiveRanges() {
        assertThat(Array.ofAll(Range.inclusive(integerFrom, integerTo))).isEqualTo(Array.of(1,2,3));
        // TODO: add tests for all other supported numeric types
    }

    @Test
    public void shallGenerateExclusiveRanges() {
        assertThat(Array.ofAll(Range.exclusive(integerFrom, integerTo))).isEqualTo(Array.of(1,2));
        // TODO: add tests for all other supported numeric types
    }

    @Test
    public void shallGenerateInclusiveRangesByIncrement() {
        assertThat(Array.ofAll(Range.inclusiveBy(integerFrom, integerTo, integerStep))).isEqualTo(Array.of(1,2,3));
        // TODO: add tests for all other supported numeric types
    }

    @Test
    public void shallGenerateExclusiveRangesByIncrement() {
        assertThat(Array.ofAll(Range.exclusiveBy(integerFrom, integerTo, integerStep))).isEqualTo(Array.of(1,2));
        // TODO: add tests for all other supported numeric types
    }
}