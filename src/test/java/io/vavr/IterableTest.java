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

import io.vavr.collection.Queue;
import io.vavr.collection.Stream;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

// Specific tests. For general tests, see AbstractIterableTest.
public class IterableTest {

    // -- eq

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
