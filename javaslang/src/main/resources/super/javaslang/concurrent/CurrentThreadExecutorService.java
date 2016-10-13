/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class CurrentThreadExecutorService implements ExecutorService {

    private static class CurrentThreadFuture<T> implements Future<T> {

        private T result;

        private Exception exception;

        public CurrentThreadFuture(T result, Exception exception) {
            this.result = result;
            this.exception = exception;
        }

        @Override public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }
        @Override public boolean isCancelled() {
            return false;
        }
        @Override public boolean isDone() {
            return true;
        }
        @Override public T get() throws InterruptedException, ExecutionException {
            if (exception != null) {
                throw new ExecutionException(exception);
            }

            return result;
        }
        @Override public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
                                                                   TimeoutException {
            return get();
        }
    }

    private boolean shutdown = false;

    @Override public void shutdown() { shutdown = true; }

    @Override public List<Runnable> shutdownNow() { return new ArrayList<>(); }

    @Override public boolean isShutdown() { return shutdown; }

    @Override public boolean isTerminated() { return shutdown; }

    @Override public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException { return true; }

    @Override public <T> Future<T> submit(Callable<T> task) {
        return call(task);
    }

    @Override public <T> Future<T> submit(Runnable task, T result) {
        task.run();
        return new CurrentThreadFuture(result, null);
    }

    @Override public Future<?> submit(Runnable task) {
        task.run();
        return new CurrentThreadFuture<>(null, null);
    }

    @Override public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws
                                                                                            InterruptedException {
        return tasks.stream().map(task -> call(task)).collect(Collectors.toList());
    }

    @Override public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return invokeAll(tasks);
    }

    @Override public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        List<Exception> errors = new ArrayList<>();
        Optional<T> result = tasks.stream()
                                  .map(task -> {
                                      try {
                                          return call(task).get();
                                      } catch (Exception e) {
                                          errors.add(e);
                                          return null;
                                      }
                                  })
                                  .filter(value -> value != null)
                                  .findFirst();

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ExecutionException("No task returned a valid result", errors.size() > 0 ? errors.get(0)
                                                                                              : new Exception());
        }
    }

    @Override public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout,
            TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return invokeAny(tasks);
    }

    @Override public void execute(Runnable command) {
        command.run();
    }

    private <T> Future<T> call(Callable<T> task) {
        T result = null;
        Exception exception = null;

        try {
            result = task.call();
        } catch (Exception e) {
            exception = e;
        }

        return new CurrentThreadFuture(result, exception);
    }
}
