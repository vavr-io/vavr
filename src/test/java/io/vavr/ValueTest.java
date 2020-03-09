/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2020 Vavr, http://vavr.io
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
package io.vavr;

import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
}
