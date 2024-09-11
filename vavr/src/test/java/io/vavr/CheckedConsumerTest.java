/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2024 Vavr, http://vavr.io
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

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

    // -- accept

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
        final CheckedConsumer<?> f = t -> { throw new Error(); };
        try {
            f.accept(null);
            fail("should have thrown");
        } catch(Throwable x) {
            // ok
        }
    }

    // -- andThen

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
        final CheckedConsumer<?> f = t -> { throw new Error(); };
        try {
            f.andThen(ignored -> result.set(true)).accept(null);
            fail("should have thrown");
        } catch(Throwable x) {
            assertThat(result.get()).isFalse();
        }
    }

    // -- unchecked

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
        final Consumer<Object> consumer = CheckedConsumer.of(obj -> { throw new Error(); }).unchecked();
        try {
            consumer.accept(null);
            Assertions.fail("Did excepect an exception.");
        } catch(Error x) {
            // ok!
        }
    }
}
