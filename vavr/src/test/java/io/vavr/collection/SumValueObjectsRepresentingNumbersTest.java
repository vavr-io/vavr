/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2017 Vavr, http://vavr.io
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

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.function.BiFunction;

import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SumValueObjectsRepresentingNumbersTest extends SumArbitraryValueObjectsTest<IntegerValue> {
    private List<IntegerValue> items;

    private IntegerValue expectedSum;

    public SumValueObjectsRepresentingNumbersTest(List<IntegerValue> items, IntegerValue expectedSum) {
        this.items = items;
        this.expectedSum = expectedSum;
    }

    @Parameters(name = "case {index}: sum({0}) = {1}")
    public static Collection<Object[]> data() {
        return List.of(
                specialCase(List.empty(), 0),
                specialCase(List.of(45), 45),
                specialCase(List.of(2, 8, -5), 5)
        ).toJavaList();
    }

    private static Object[] specialCase(List<Integer> itemsAsIntegers, int expectedSumAsInt) {
        return new Object[] {
                itemsAsIntegers.map(IntegerValue::with),
                IntegerValue.with(expectedSumAsInt) };
    }

    @Override
    protected IntegerValue expectedSum() {
        return expectedSum;
    }

    @Override
    protected Monoid<IntegerValue> monoid() {
        return new Monoid<IntegerValue>() {
            @Override
            public IntegerValue identityElement() {
                return IntegerValue.with(0);
            }

            @Override
            public BiFunction<IntegerValue, IntegerValue, IntegerValue> addFunction() {
                return IntegerValue::add;
            }
        };
    }

    @Override
    protected List<IntegerValue> items() {
        return items;
    }

}
