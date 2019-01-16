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
package io.vavr.concurrent;

import io.vavr.control.Try;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

public class PromiseTest {

    @AfterClass
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

    @Test(expected = IllegalStateException.class)
    public void shouldFailWhenCompleteAgain() {
        Promise.successful(42).complete(Try.success(0));
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
