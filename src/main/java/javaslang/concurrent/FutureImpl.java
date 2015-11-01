/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import javaslang.collection.Queue;
import javaslang.control.*;

import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * <strong>INTERNAL API - This class is subject to change.</strong>
 *
 * {@link Future} implementation, for internal use only.
 * <p>
 * <strong>Lifecycle of a {@code FutureImpl}:</strong>
 * <p>
 * 1) Creation
 * <ul>
 * <li>{@code value = None}</li>
 * <li>{@code actions = Queue.empty()}</li>
 * <li>{@code job = null}</li>
 * </ul>
 * 2) Run
 * <ul>
 * <li>{@code value = None}</li>
 * <li>{@code actions = Queue(...)}</li>
 * <li>{@code job = java.util.concurrent.Future}</li>
 * </ul>
 * 3) Complete
 * <ul>
 * <li>{@code value = Some(Try)}</li>
 * <li>{@code actions = null}</li>
 * <li>{@code job = null}</li>
 * </ul>
 * 4) Cancel
 * <ul>
 * <li>{@code value = Some(Failure)}</li>
 * <li>{@code actions = null}</li>
 * <li>{@code job = null}</li>
 * </ul>
 *
 * @param <T> Result of the computation.
 * @author Daniel Dietrich
 * @since 2.0.0
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
     *
     * @@GuardedBy("lock")
     */
    private volatile Option<Try<T>> value = None.instance();

    /**
     * The queue of actions is filled when calling onComplete() before the Future is completed or cancelled.
     * Otherwise actions = null.
     *
     * @@GuardedBy("lock")
     */
    private Queue<Consumer<? super Try<T>>> actions = Queue.empty();

    /**
     * Once a computation is started via run(), job is defined and used to control the lifecycle of the computation.
     *
     * @@GuardedBy("lock")
     */
    private java.util.concurrent.Future<Try<T>> job = null;

    /**
     * Creates a Future, {@link #run(Try.CheckedSupplier)} has to be called separately.
     *
     * @param executorService An {@link ExecutorService} to run and control the computation and to perform the actions.
     */
    FutureImpl(ExecutorService executorService) {
        Objects.requireNonNull(executorService, "executorService is null");
        this.executorService = executorService;
    }

    @Override
    public boolean cancel() {
        synchronized (lock) {
            if (isCompleted()) {
                return false;
            } else {
                if (job != null) {
                    Try.run(() -> job.cancel(true));
                }
                value = new Some<>(new Failure<>(new CancellationException()));
                actions = null;
                job = null;
                return true;
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
    public void onComplete(Consumer<? super Try<T>> action) {
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
    }

    /**
     * Runs a computation using the underlying ExecutorService.
     * <p>
     * DEV-NOTE: Internally this method is called by the static {@code Future} factory methods.
     *
     * @throws IllegalStateException if the Future is pending, completed or cancelled
     * @throws NullPointerException  if {@code computation} is null.
     */
    void run(Try.CheckedSupplier<? extends T> computation) {
        Objects.requireNonNull(computation, "computation is null");
        synchronized (lock) {
            if (job != null) {
                throw new IllegalStateException("The Future is already running.");
            }
            if (isCompleted()) {
                throw new IllegalStateException("The Future is completed.");
            }
            // The current lock ensures that the job is assigned before the computation completes.
            job = executorService.submit(() -> complete(Try.of(computation)));
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
    @SuppressWarnings("unchecked")
    Try<T> complete(Try<? extends T> value) {
        Objects.requireNonNull(value, "value is null");
        synchronized (lock) {
            if (isCompleted()) {
                throw new IllegalStateException("The Future is completed.");
            }
            final Try<T> that = (Try<T>) value;
            final Queue<Consumer<? super Try<T>>> actions;
            synchronized (lock) {
                actions = this.actions;
                this.value = new Some<>(that);
                this.actions = null;
                this.job = null;
            }
            actions.forEach(this::perform);
            return that;
        }
    }

    private void perform(Consumer<? super Try<T>> action) {
        Try.run(() -> executorService.execute(() -> action.accept(value.get())));
    }
}
