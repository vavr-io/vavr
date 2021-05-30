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

public class RangeTest {
    @Test
    public void shallWorkWithForLoop() {
        // This test will pass of it compiles without errors
        for (int i : Range.inclusive(0, 2)) {
            System.out.println(i);
        }
    }

    @Test
    public void shallWorkWithAllNumericTypes() {
        // This test will pass of it compiles without errors

        // Numeric types to support:
        //   Byte
        //   Short
        //   Integer
        //   Long
        //   Char
        //   Float
        //   Double
        //   BigDecimal

        // TODO: Range.inclusive(Byte.valueOf(0b), Byte.valueOf(10b));
        // TODO: Range.inclusive(Short.valueOf((byte)0), Short.valueOf((byte)10));
        Range.inclusive(Integer.valueOf(0), Integer.valueOf(10));
        // TODO: Range.inclusive(Long.valueOf(0), Long.valueOf(10));
        // TODO: Range.inclusive(Character.valueOf(0), Character.valueOf(10));
        // TODO: Range.inclusive(Float.valueOf(0), Float.valueOf(10));
        // TODO: Range.inclusive(Double.valueOf(0), Double.valueOf(10));
        // TODO: Range.inclusive(BigDecimal.valueOf(0), BigDecimal.valueOf(10));
    }
}
