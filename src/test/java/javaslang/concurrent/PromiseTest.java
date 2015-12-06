/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import org.junit.Test;

import static javaslang.concurrent.Concurrent.waitUntil;
import static javaslang.concurrent.Concurrent.zZz;
import static org.assertj.core.api.Assertions.assertThat;

public class PromiseTest {

    @Test
    public void shouldCompletePromiseWithItsOwnFuture() {
        final Promise<String> promise = Promise.make(TrivialExecutorService.instance());
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
