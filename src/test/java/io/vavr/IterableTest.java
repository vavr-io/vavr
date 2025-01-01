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

import io.vavr.collection.Queue;
import io.vavr.collection.Stream;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

// Specific tests. For general tests, see AbstractIterableTest.
public class IterableTest {

    @Nested
    @DisplayName("eq")
    class EqTest {

        @Test
        public void shouldEqNoneAndEmptyList() {
            assertThat(Option.none().eq(List.empty())).isTrue();
            assertThat(Option.none().eq(List.of(1))).isFalse();
        }

        @Test
        public void shouldEqSomeAndNonEmptyList() {
            assertThat(Option.some(1).eq(List.of(1))).isTrue();
            assertThat(Option.some(1).eq(List.of(2))).isFalse();
            assertThat(Option.some(1).eq(List.empty())).isFalse();
        }

        @Test
        public void shouldEqIterableAndJavaIterable() {
            assertThat(List.of(1, 2, 3).eq(Arrays.asList(1, 2, 3))).isTrue();
        }

        @Test
        public void shouldEqNestedIterables() {
            // ((1, 2), ((3)))
            final Value<?> i1 = List.of(List.of(1, 2), Collections.singletonList(List.of(3)));
            final Value<?> i2 = Queue.of(Stream.of(1, 2), List.of(Lazy.of(() -> 3)));
            final Value<?> i3 = Queue.of(Stream.of(1, 2), List.of(List.of()));
            assertThat(i1.eq(i2)).isTrue();
            assertThat(i1.eq(i3)).isFalse();
        }
    }
}
