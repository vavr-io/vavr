/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2018 Vavr, http://vavr.io
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
package io.vavr.control;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckedConsumerTest {

    // -- .accept(Object)

    @Test
    void shouldBeAbleToThrowCheckedException() {
        final CheckedConsumer<?> f = ignored -> { throw new Exception(); };
        assertThrows(Exception.class, () -> f.accept(null));
    }

    // -- .andThen(CheckedConsumer)

    @Test
    void shouldCallOneCheckedConsumerAndThenAnotherCheckedConsumer() throws Exception {
        final List<String> list = new ArrayList<>();
        final CheckedConsumer<String> f1 = s -> list.add("before:" + s);
        final CheckedConsumer<String> f2 = s -> list.add("andThen:" + s);
        f1.andThen(f2).accept("ok");
        assertEquals(List.of("before:ok", "andThen:ok"), list);
    }

    @Test
    void shouldNotCallAfterWhenBeforeThrowsWhenCallingAndThen() {
        final CheckedConsumer<String> f1 = ignored -> { throw new Exception("before"); };
        final CheckedConsumer<String> f2 = ignored -> { throw new AssertionError("after called"); };
        assertEquals(
                "before",
                assertThrows(Exception.class, () -> f1.andThen(f2).accept(null)).getMessage()
        );
    }

    @Test
    void shouldCallBeforeWhenAfterThrowsWhenCallingAndThen() {
        final CheckedConsumer<String> f1 = ignored -> {};
        final CheckedConsumer<String> f2 = ignored -> { throw new Exception("after"); };
        assertEquals(
                "after",
                assertThrows(Exception.class, () -> f1.andThen(f2).accept(null)).getMessage()
        );
    }

    @Test
    void shouldThrowNPEIfAndThenReceivesNullParam() {
        final CheckedConsumer<String> f = ignored -> {};
        assertEquals(
                "after is null",
                assertThrows(NullPointerException.class, () -> f.andThen(null)).getMessage()
        );
    }
}
