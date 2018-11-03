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

import java.util.function.BiFunction;

public abstract class SumArbitraryValueObjectsTest<ValueType> {
    @Test
    public void checkSum() throws Exception {
        Assertions.assertThat(
                sum(items(), identityElement(), addFunction()))
                .isEqualTo(expectedSum());
    }

    protected abstract ValueType expectedSum();
    protected abstract BiFunction<ValueType, ValueType, ValueType> addFunction();
    protected abstract Monoid<ValueType> monoid();
    protected abstract ValueType identityElement();
    protected abstract List<ValueType> items();

    // REFACTOR Production code. Not sure where it goes yet. Traversable?
    private <T> T sum(List<T> items, T identityElement, BiFunction<T, T, T> addFunction) {
        return items.foldLeft(identityElement, addFunction);
    }
}
