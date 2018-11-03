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

// REFACTOR Move this onto Traversable?
@RunWith(Parameterized.class)
public class SumValueObjectsRepresentingNumbersTest extends SumArbitraryValueObjectsTest {
    private List<ExampleSummableValue> itemsAsExampleSummableValues;

    private ExampleSummableValue expectedSumOfExampleSummableValues;

    public SumValueObjectsRepresentingNumbersTest(List<ExampleSummableValue> itemsAsExampleSummableValues, ExampleSummableValue expectedSumOfExampleSummableValues) {
        this.itemsAsExampleSummableValues = itemsAsExampleSummableValues;
        this.expectedSumOfExampleSummableValues = expectedSumOfExampleSummableValues;
    }

    @Parameters(name = "case {index}: sum({0}) = {1}")
    public static Collection<Object[]> data() {
        return List.of(
                exampleSummableValueCase(List.empty(), 0),
                exampleSummableValueCase(List.of(45), 45),
                exampleSummableValueCase(List.of(2, 8, -5), 5)
        ).toJavaList();
    }

    private static Object[] exampleSummableValueCase(List<Integer> itemsAsIntegers, int expectedSumAsInt) {
        return new Object[] {
                itemsAsIntegers.map(ExampleSummableValue::with),
                ExampleSummableValue.with(expectedSumAsInt) };
    }

    @Override
    protected ExampleSummableValue expectedSum() {
        return expectedSumOfExampleSummableValues;
    }

    @Override
    protected BiFunction<ExampleSummableValue, ExampleSummableValue, ExampleSummableValue> addFunction() {
        return ExampleSummableValue::add;
    }

    @Override
    protected ExampleSummableValue identityElement() {
        return ExampleSummableValue.with(0);
    }

    @Override
    protected List<ExampleSummableValue> items() {
        return itemsAsExampleSummableValues;
    }

    public static class AnotherExampleSummableValue {
        private final String text;

        public AnotherExampleSummableValue(String text) {
            this.text = text;
        }

        public static AnotherExampleSummableValue with(String text) {
            return new AnotherExampleSummableValue(text);
        }

        public AnotherExampleSummableValue append(AnotherExampleSummableValue that) {
            return AnotherExampleSummableValue.with(this.text + ":" + that.text);
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof AnotherExampleSummableValue) {
                AnotherExampleSummableValue that = (AnotherExampleSummableValue) other;
                if (this.text == that.text) {
                    return true;
                } else if (this.text == null || that.text == null) {
                    return false;
                } else {
                    return this.text.equals(that.text);
                }
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return text.hashCode();
        }

        @Override
        public String toString() {
            return String.format("AnotherExampleSummableValue[text=%s]", text);
        }
    }
}
