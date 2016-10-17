/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import javaslang.collection.Queue;
import javaslang.control.Option;
import javaslang.control.Try;
import javaslang.control.Try.CheckedSupplier;

import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * GWT emulated version of {@link FutureImpl} with removed uses of Object's wait and notify methods.
 */
final class FutureImpl<T> implements Future<T> {

    /**
     * Used to start new threads.
     */
    private final ExecutorService executorService;

    /**
     * Used to synchronize state changes.
     */
    private final Object lock = new Object();

    /**
     * Once the Future is completed, the value is defined.
     */
    @GuardedBy("lock")
    private volatile Option<Try<T>> value = Option.none();

    /**
     * The queue of actions is filled when calling onComplete() before the Future is completed or cancelled.
     * Otherwise actions = null.
     */
    @GuardedBy("lock")
    private Queue<Consumer<? super Try<T>>> actions = Queue.empty();

    /**
     * Once a computation is started via run(), job is defined and used to control the lifecycle of the computation.
     * <p>
     * The {@code java.util.concurrent.Future} is not intended to store the result of the computation, it is stored in
     * {@code value} instead.
     */
    @GuardedBy("lock")
    private java.util.concurrent.Future<?> job = null;

    /**
     * Creates a Future, {@link #run(CheckedSupplier)} has to be called separately.
     *
     * @param executorService An {@link ExecutorService} to run and control the computation and to perform the actions.
     */
    FutureImpl(ExecutorService executorService) {
        Objects.requireNonNull(executorService, "executorService is null");
        this.executorService = executorService;
    }

    @Override
    public Future<T> await() {
        return this;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        synchronized (lock) {
            if (isCompleted()) {
                return false;
            } else {
                return Try.of(() -> job == null || job.cancel(mayInterruptIfRunning)).onSuccess(cancelled -> {
                    if (cancelled) {
                        value = Option.some(Try.failure(new CancellationException()));
                        actions = null;
                        job = null;
                    }
                }).getOrElse(false);
            }
        }
    }

    @Override
    public ExecutorService executorService() {
        return executorService;
    }

    @Override
    public Option<Try<T>> getValue() {
        return value;
    }

    @Override
    public boolean isCompleted() {
        return value.isDefined();
    }

    @Override
    public Future<T> onComplete(Consumer<? super Try<T>> action) {
        Objects.requireNonNull(action, "action is null");
        if (isCompleted()) {
            perform(action);
        } else {
            synchronized (lock) {
                if (isCompleted()) {
                    perform(action);
                } else {
                    actions = actions.enqueue(action);
                }
            }
        }
        return this;
    }

    // This class is MUTABLE and therefore CANNOT CHANGE DEFAULT equals() and hashCode() behavior.
    // See http://stackoverflow.com/questions/4718009/mutable-objects-and-hashcode

    @Override
    public String toString() {
        return stringPrefix() + "(" + value.map(String::valueOf).getOrElse("?") + ")";
    }

    /**
     * Runs a computation using the underlying ExecutorService.
     * <p>
     * DEV-NOTE: Internally this method is called by the static {@code Future} factory methods.
     *
     * @throws IllegalStateException if the Future is pending, completed or cancelled
     * @throws NullPointerException  if {@code computation} is null.
     */
    void run(CheckedSupplier<? extends T> computation) {
        Objects.requireNonNull(computation, "computation is null");
        synchronized (lock) {
            if (job != null) {
                throw new IllegalStateException("The Future is already running.");
            }
            if (isCompleted()) {
                throw new IllegalStateException("The Future is completed.");
            }
            try {
                // if the ExecutorService runs the computation
                // - in a different thread, the lock ensures that the job is assigned before the computation completes
                // - in the current thread, the job is already completed and the `job` variable remains null
                final java.util.concurrent.Future<?> tmpJob = executorService.submit(() -> complete(Try.of(computation)));
                if (!isCompleted()) {
                    job = tmpJob;
                }
            } catch (Throwable t) {
                // ensures that the Future completes if the `executorService.submit()` method throws
                if (!isCompleted()) {
                    complete(Try.failure(t));
                }
            }
        }
    }

    boolean tryComplete(Try<? extends T> value) {
        Objects.requireNonNull(value, "value is null");
        synchronized (lock) {
            if (isCompleted()) {
                return false;
            } else {
                complete(value);
                return true;
            }
        }
    }

    /**
     * Completes this Future with a value.
     * <p>
     * DEV-NOTE: Internally this method is called by the {@code Future.run()} method and by {@code Promise}.
     *
     * @param value A Success containing a result or a Failure containing an Exception.
     * @return The given {@code value} for convenience purpose.
     * @throws IllegalStateException if the Future is already completed or cancelled.
     * @throws NullPointerException  if the given {@code value} is null.
     */
    private void complete(Try<? extends T> value) {
        Objects.requireNonNull(value, "value is null");
        final Queue<Consumer<? super Try<T>>> actions;
        // it is essential to make the completed state public *before* performing the actions
        synchronized (lock) {
            if (isCompleted()) {
                throw new IllegalStateException("The Future is completed.");
            }
            actions = this.actions;
            this.value = Option.some(Try.narrow(value));
            this.actions = null;
            this.job = null;
        }
        actions.forEach(this::perform);
    }

    private void perform(Consumer<? super Try<T>> action) {
        Try.run(() -> executorService.execute(() -> action.accept(value.get())));
    }
}
