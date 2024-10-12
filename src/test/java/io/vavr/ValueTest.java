/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2024 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.vavr;

import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;

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
