/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

public class CheckedConsumerTest {

    // -- of

    @Test
    public void shouldCreateCheckedConsumerUsingLambda() {
        final CheckedConsumer<Object> consumer = CheckedConsumer.of(obj -> {});
        assertThat(consumer).isNotNull();
    }

    @Test
    public void shouldCreateCheckedConsumerUsingMethodReference() {
        final CheckedConsumer<Object> consumer = CheckedConsumer.of(CheckedConsumerTest::accept);
        assertThat(consumer).isNotNull();
    }

    private static void accept(Object obj) {
    }

    @Nested
    class AcceptTests {
        @Test
        public void shouldApplyNonThrowingCheckedConsumer() {
            final CheckedConsumer<?> f = t -> {};
            try {
                f.accept(null);
            } catch(Throwable x) {
                fail("should not have thrown", x);
            }
        }

        @Test
        public void shouldApplyThrowingCheckedConsumer() {
            final Error error = new Error();
            final CheckedConsumer<?> f = t -> { throw error; };
            assertThatThrownBy(() -> f.accept(null)).isSameAs(error);
        }
    }

    @Nested
    class AndthenTests {
        @Test
        public void shouldThrowWhenComposingCheckedConsumerUsingAndThenWithNullParameter() {
            final CheckedConsumer<?> f = t -> {};
            assertThatThrownBy(() -> f.andThen(null)).isInstanceOf(NullPointerException.class);
        }

        @Test
        public void shouldComposeCheckedConsumerUsingAndThenWhenFirstOneSucceeds() {
            final AtomicBoolean result = new AtomicBoolean(false);
            final CheckedConsumer<?> f = t -> {};
            try {
                f.andThen(ignored -> result.set(true)).accept(null);
                assertThat(result.get()).isTrue();
            } catch(Throwable x) {
                fail("should not have thrown", x);
            }
        }

        @Test
        public void shouldComposeCheckedConsumerUsingAndThenWhenFirstOneFails() {
            final AtomicBoolean result = new AtomicBoolean(false);
            final Error error = new Error();
            final CheckedConsumer<?> f = t -> { throw error; };
            assertThatThrownBy(() -> f.andThen(ignored -> result.set(true)).accept(null)).isSameAs(error);
            assertThat(result.get()).isFalse();
        }
    }

    @Nested
    class UncheckedTests {
        @Test
        public void shouldApplyAnUncheckedFunctionThatDoesNotThrow() {
            final Consumer<Object> consumer = CheckedConsumer.of(obj -> {}).unchecked();
            try {
                consumer.accept(null);
            } catch(Throwable x) {
                Assertions.fail("Did not excepect an exception but received: " + x.getMessage());
            }
        }

        @Test
        public void shouldApplyAnUncheckedFunctionThatThrows() {
            final Error error = new Error();
            final Consumer<Object> consumer = CheckedConsumer.of(obj -> { throw error; }).unchecked();
            assertThatThrownBy(() -> consumer.accept(null)).isSameAs(error);
        }

        @Test
        public void shouldApplyAnUncheckedFunctionThatThrowsCheckedException() {
            final Exception exception = new Exception();
            final Consumer<Object> consumer = CheckedConsumer.of(obj -> { throw exception; }).unchecked();
            assertThatThrownBy(() -> consumer.accept(null)).isSameAs(exception);
        }
    }
}
