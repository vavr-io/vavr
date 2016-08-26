/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import javaslang.control.Try;
import org.junit.Test;

import static javaslang.concurrent.Concurrent.zZz;
import static javaslang.concurrent.ExecutorServices.trivialExecutorService;
import static org.assertj.core.api.Assertions.assertThat;

public class PromiseTest {

    @Test
    public void shouldReturnExecutorService() {
        final Promise<Integer> promise = Promise.successful(42);
        assertThat(promise.executorService()).isNotNull();
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
        assertThat(Promise.successful("javaslang").toString().contains("javaslang")).isTrue();
    }

    @Test
    public void shouldCompletePromiseWithItsOwnFuture() {
        final Promise<String> promise = Promise.make(trivialExecutorService());
        promise.completeWith(promise.future());
        assertThat(promise.isCompleted()).isFalse();
        assertThat(promise.success("ok").isCompleted()).isTrue();
    }

    @Test
    public void shouldMediateProducerConsumerViaPromise() {

        final String product = "Coffee";

        class Context {

            String produceSomething() {
                zZz();
                System.out.println("Making " + product);
                return product;
            }

            void continueDoingSomethingUnrelated() {
                System.out.println("Unreleated stuff");
            }

            void startDoingSomething() {
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
