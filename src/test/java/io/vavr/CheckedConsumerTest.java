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
            Assertions.fail("Did not expect an exception but received: " + x.getMessage());
        }
    }

    @Test
    public void shouldApplyAnUncheckedFunctionThatThrows() {
        final Consumer<Object> consumer = CheckedConsumer.of(obj -> { throw new Error(); }).unchecked();
        try {
            consumer.accept(null);
            Assertions.fail("Did expect an exception.");
        } catch(Error x) {
            // ok!
        }
    }
}
