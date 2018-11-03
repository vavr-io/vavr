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

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import java.util.Collection;
import java.util.function.BiFunction;

import static org.junit.runners.Parameterized.Parameters;

// REFACTOR Move this onto Traversable?
@RunWith(Parameterized.class)
public class SumArbitraryValuesTest {
    @Parameter(0)
    public List<ExampleSummableValue> itemsAsExampleSummableValues;

    @Parameter(1)
    public ExampleSummableValue expectedSumOfExampleSummableValues;

    @Parameters(name = "case {index}: sum({0}) = {1}")
    public static Collection<Object[]> data() {
        return List.of(
                exampleSummableValueCase(List.empty(), 0),
                exampleSummableValueCase(List.of(45), 45),
                exampleSummableValueCase(List.of(2, 8, -5), 5)
        ).map(Array::toJavaArray).toJavaList();
    }

    private static Array<Object> exampleSummableValueCase(List<Integer> itemsAsIntegers, int expectedSumAsInt) {
        return Array.of(
                itemsAsIntegers.map(ExampleSummableValue::with),
                ExampleSummableValue.with(expectedSumAsInt));
    }

    @Test
    public void checkSum() throws Exception {
        Assertions.assertThat(
                sum(
                        itemsAsExampleSummableValues,
                        ExampleSummableValue.with(0),
                        ExampleSummableValue::add))
                .isEqualTo(expectedSumOfExampleSummableValues);
    }

    private ExampleSummableValue sum(List<ExampleSummableValue> items, ExampleSummableValue identityElement, BiFunction<ExampleSummableValue, ExampleSummableValue, ExampleSummableValue> addFunction) {
        return items.foldLeft(identityElement, addFunction);
    }

    // REFACTOR Should this implement Value?
    public static class ExampleSummableValue {
        private final int integerValue;

        public ExampleSummableValue(int integerValue) {
            this.integerValue = integerValue;
        }

        public static ExampleSummableValue with(int integerValue) {
            return new ExampleSummableValue(integerValue);
        }

        public ExampleSummableValue add(ExampleSummableValue that) {
            return new ExampleSummableValue(this.integerValue + that.integerValue);
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof ExampleSummableValue) {
                ExampleSummableValue that = (ExampleSummableValue) other;
                return this.integerValue == that.integerValue;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return integerValue;
        }

        @Override
        public String toString() {
            return String.format("ExampleSummableValue[integerValue=%d]", integerValue);
        }
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
                }
                else if (this.text == null || that.text == null) {
                    return false;
                }
                else {
                    return this.text.equals(that.text);
                }
            }
            else {
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
