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
package io.vavr.concurrent;

import io.vavr.control.Try;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PromiseTest {

    @AfterAll
    public static void gracefullyFinishThreads() throws TimeoutException {
        Concurrent.gracefullyFinishThreads();
    }

    @Test
    public void shouldReturnExecutorService() {
        final Promise<Integer> promise = Promise.successful(42);
        assertThat(promise.executor()).isNotNull();
    }

    @Test
    public void shouldReturnSuccessfulPromise() {
        final Promise<Integer> promise = Promise.successful(42);
        assertThat(promise.isCompleted()).isTrue();
        assertThat(promise.future().isSuccess()).isTrue();
    }

    @Test
    public void shouldReturnFailedPromise() {
        final Promise<Integer> promise = Promise.failed(new RuntimeException());
        assertThat(promise.isCompleted()).isTrue();
        assertThat(promise.future().isFailure()).isTrue();
    }

    @Test
    public void shouldReturnPromiseFromTry() {
        final Promise<Integer> promise = Promise.fromTry(Try.of(() -> 42));
        assertThat(promise.isCompleted()).isTrue();
        assertThat(promise.future().isSuccess()).isTrue();
    }

    @Test
    public void shouldFailWhenCompleteAgain() {
        assertThrows(IllegalStateException.class, () -> Promise.successful(42).complete(Try.success(0)));
    }

    @Test
    public void shouldTrySuccess() {
        final Promise<Integer> promise = Promise.make();
        assertThat(promise.trySuccess(42)).isTrue();
        assertThat(promise.trySuccess(42)).isFalse();
        assertThat(promise.future().get()).isEqualTo(42);
    }

    @Test
    public void shouldTryFailure() {
        final Promise<Integer> promise = Promise.make();
        assertThat(promise.tryFailure(new RuntimeException())).isTrue();
        assertThat(promise.tryFailure(new RuntimeException())).isFalse();
        assertThat(promise.future().isFailure()).isTrue();
    }

    @Test
    public void shouldConvertToString() {
        assertThat(Promise.successful("vavr").toString().contains("vavr")).isTrue();
    }

    @Test
    public void shouldCompletePromiseWithItsOwnFuture() {
        final Promise<String> promise = Promise.make(FutureTest.TRIVIAL_EXECUTOR);
        promise.completeWith(promise.future());
        assertThat(promise.isCompleted()).isFalse();
        assertThat(promise.success("ok").isCompleted()).isTrue();
    }

    @Test
    public void shouldMediateProducerConsumerViaPromise() {

        final String product = "Coffee";

        class Context {

            private String produceSomething() {
                Concurrent.zZz();
                System.out.println("Making " + product);
                return product;
            }

            private void continueDoingSomethingUnrelated() {
                System.out.println("Unreleated stuff");
            }

            private void startDoingSomething() {
                System.out.println("Something else");
            }
        }

        final Context ctx = new Context();
        final Promise<String> producerResult = Promise.make();
        final Promise<String> consumerResult = Promise.make();

        // producer
        Future.run(() -> {
            producerResult.success(ctx.produceSomething());
            ctx.continueDoingSomethingUnrelated();
        });

        // consumer
        Future.run(() -> {
            ctx.startDoingSomething();
            consumerResult.completeWith(producerResult.future());
        });

        final String actual = consumerResult.future().get();
        assertThat(actual).isEqualTo(product);
    }
}
