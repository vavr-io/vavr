/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

final class ExecutorServices {

    private static final ExecutorService TRIVIAL_EXECUTOR_SERVICE = new TrivialExecutorService();

    private static final ExecutorService REJECTING_EXECUTOR_SERVICE = new RejectingExecutorService();

    private ExecutorServices() {
    }

    static ExecutorService trivialExecutorService() {
        return TRIVIAL_EXECUTOR_SERVICE;
    }

    static ExecutorService rejectingExecutorService() {
        return REJECTING_EXECUTOR_SERVICE;
    }

    private static final class TrivialExecutorService extends AbstractExecutorService {

        @Override
        public <T> java.util.concurrent.Future<T> submit(Callable<T> task) {
            try {
                return new ImmediatelyDoneFuture<>(task.call());
            } catch (Exception x) {
                throw new IllegalStateException("Error calling task.", x);
            }
        }

        private static class ImmediatelyDoneFuture<T> implements java.util.concurrent.Future<T> {

            final T value;

            ImmediatelyDoneFuture(T value) {
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

    private static final class RejectingExecutorService extends AbstractExecutorService {

        @Override
        public <T> java.util.concurrent.Future<T> submit(Callable<T> task) {
            throw new RejectedExecutionException();
        }
    }

    private static abstract class AbstractExecutorService implements ExecutorService {

        private boolean shutdown = false;

        @Override
        public abstract <T> java.util.concurrent.Future<T> submit(Callable<T> task);

        @Override
        public java.util.concurrent.Future<?> submit(Runnable task) {
            return submit(task, null);
        }

        @Override
        public <T> java.util.concurrent.Future<T> submit(Runnable task, T result) {
            return submit(() -> {
                task.run();
                return result;
            });
        }

        @Override
        public void execute(Runnable command) {
            command.run();
        }

        @Override
        public void shutdown() { shutdown = true; }

        @Override
        public List<Runnable> shutdownNow() {
            shutdown();
            return Collections.emptyList();
        }

        @Override
        public boolean isShutdown() {
            return shutdown;
        }

        @Override
        public boolean isTerminated() {
            return isShutdown();
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return true;
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
    }
}
