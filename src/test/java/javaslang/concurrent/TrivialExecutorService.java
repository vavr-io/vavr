/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * A submitted Callable is immediately done (without running in a different thread).
 */
final class TrivialExecutorService implements ExecutorService {

    private static final TrivialExecutorService INSTANCE = new TrivialExecutorService();

    private TrivialExecutorService() {
    }

    public static TrivialExecutorService instance() {
        return INSTANCE;
    }

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
