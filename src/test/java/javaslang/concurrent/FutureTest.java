/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import javaslang.control.None;
import javaslang.control.Some;
import javaslang.control.Success;
import javaslang.control.Try;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.fail;

public class FutureTest {

    static final TrivialExecutorService TRIVIAL_EXECUTOR_SERVICE = new TrivialExecutorService();

    // Max wait time = WAIT_MILLIS * WAIT_COUNT (however, most probably it will take only WAIT_MILLIS * 1)
    static final long WAIT_MILLIS = 50;
    static final int WAIT_COUNT = 100;

    @Test
    public void shouldCreateAndCompleteAFutureUsingTrivialExecutorService() {
        final Future<Integer> future = Future.of(TRIVIAL_EXECUTOR_SERVICE, () -> 1);
        assertCompleted(future, 1);
    }

    @Test
    public void shouldNotCancelCompletedFutureUsingTrivialExecutorService() {
        final Future<Integer> future = Future.of(TRIVIAL_EXECUTOR_SERVICE, () -> 1);
        assertThat(future.cancel()).isFalse();
        assertCompleted(future, 1);
    }

    @Test
    public void shouldRegisterCallbackBeforeFutureCompletes() {

        // instead of delaying we wait/notify
        final Object lock = new Object();
        final int[] actual = new int[] { -1 };
        final int expected = 1;

        // create a future and put it to sleep
        final Future<Integer> future = Future.of(() -> {
            synchronized (lock) {
                lock.wait();
            }
            return expected;
        });

        // the future now is on hold and we have time to register a callback
        future.onComplete(result -> actual[0] = result.get());
        assertThat(future.isCompleted()).isFalse();
        assertThat(future.isCancelled()).isFalse();
        assertThat(actual[0]).isEqualTo(-1);

        // now awake the future
        synchronized (lock) {
            lock.notify();
        }

        // give the future thread some time to complete
        waitUntil(future::isCompleted);

        // the callback is also executed on its own thread - we have to wait for it to complete.
        waitUntil(() -> actual[0] == expected);
    }

    @Test
    public void shouldPerformActionAfterFutureCompleted() {
        final int[] actual = new int[] { -1 };
        final Future<Integer> future = Future.of(TRIVIAL_EXECUTOR_SERVICE, () -> 1);
        future.onComplete(result -> actual[0] = result.get());
        assertThat(actual[0]).isEqualTo(1);
    }

    @Test
    public void shouldInterruptLockedFuture() {

        final Future<?> future = Future.of(() -> {
            final Object lock = new Object();
            synchronized (lock) {
                lock.wait();
            }
            return null;
        });

        future.onComplete(r -> Assertions.fail("future should lock forever"));

        int count = 0;
        while (!future.isCompleted() && !future.isCancelled()) {
            Try.run(() -> Thread.sleep(100));
            if (++count > 3) {
                future.cancel();
            }
        }

        assertCancelled(future);
    }

    // checks the invariant for cancelled state
    static void assertCancelled(Future<?> future) {
        assertThat(future.isCancelled()).isTrue();
        assertThat(future.isCompleted()).isFalse();
        assertThat(future.getValue()).isEqualTo(None.instance());
    }

    // checks the invariant for cancelled state
    static <T> void assertCompleted(Future<?> future, T value) {
        assertThat(future.isCancelled()).isFalse();
        assertThat(future.isCompleted()).isTrue();
        assertThat(future.getValue()).isEqualTo(new Some<>(new Success<>(value)));
    }

    static void waitUntil(Supplier<Boolean> condition) {
        int count = 0;
        while(!condition.get()) {
            Try.run(() -> Thread.sleep(WAIT_MILLIS));
            if (++count > WAIT_COUNT) {
                fail("Condition not met.");
            }
        }
    }

    /**
     * A submitted Callable is immediately done (without running in a different thread).
     */
    static class TrivialExecutorService implements ExecutorService {

        // -- relevant methods

        @Override
        public <T> Done<T> submit(Callable<T> task) {
            try {
                return new Done<>(task.call());
            } catch (Exception x) {
                throw new IllegalStateException("Error calling task.", x);
            }
        }


        @Override
        public void execute(Runnable command) {
            command.run();
        }

        // -- not needed

        @Override
        public void shutdown() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Runnable> shutdownNow() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isShutdown() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isTerminated() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> java.util.concurrent.Future<T> submit(Runnable task, T result) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.concurrent.Future<?> submit(Runnable task) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            throw new UnsupportedOperationException();
        }

        // -- immediately done future

        static class Done<T> implements java.util.concurrent.Future<T> {

            final T value;

            Done(T value) {
                this.value = value;
            }

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return true;
            }

            @Override
            public T get() {
                return value;
            }

            @Override
            public T get(long timeout, TimeUnit unit) {
                return value;
            }
        }
    }
}
