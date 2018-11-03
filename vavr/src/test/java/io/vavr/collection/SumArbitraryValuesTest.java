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

// REFACTOR Move this onto Traversable?
public class SumArbitraryValuesTest {
    @Test
    public void empty() throws Exception {
        Assertions.assertThat(sum(List.empty()))
                .isEqualTo(ExampleSummableValue.with(0));
    }

    private ExampleSummableValue sum(List<ExampleSummableValue> empty) {
        return ExampleSummableValue.with(0);
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
}
