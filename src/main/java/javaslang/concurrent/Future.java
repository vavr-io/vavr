/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import javaslang.collection.Queue;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;
import javaslang.control.Try;
import javaslang.control.Try.CheckedSupplier;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

/**
 * A Future is a computation result that becomes available at some point.
 * <p>
 * A Future has three disjoint states: pending, completed, cancelled.
 * <ul>
 * <li>Pending: The computation is ongoing. Only a pending future may be cancelled.</li>
 * <li>Completed: The computation finished successfully with a result or failed with an exception.</li>
 * <li>Cancelled: A pending computation was cancelled. A cancelled Future will never reach the completed state.</li>
 * </ul>
 * Callbacks man be registered on a Future at each point of time. These actions are performed as soon as the Future
 * is completed. An action which is registered on a completed Future is immediately performed. The action may run on
 * a separate Thread, depending on the underlying ExecutionService. Actions which are registered on a cancelled
 * Future are never performed.
 *
 * @param <T> Type of the computation result.
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Future<T> {

    /**
     * Starts an asynchronous computation using the default {@link ExecutorService} {@link ForkJoinPool#commonPool()}.
     *
     * @param computation A computation.
     * @param <T>         Type of the computation result.
     * @return A new Future instance.
     * @throws NullPointerException if computation is null.
     */
    static <T> Future<T> of(CheckedSupplier<T> computation) {
        Objects.requireNonNull(computation, "computation is null");
        return Future.of(ForkJoinPool.commonPool(), computation);
    }

    /**
     * Starts an asynchronous computation using the given {@link ExecutorService}.
     *
     * @param executorService An executor service.
     * @param computation     A computation.
     * @param <T>             Type of the computation result.
     * @return A new Future instance.
     * @throws NullPointerException if one of executorService of computation is null.
     */
    static <T> Future<T> of(ExecutorService executorService, CheckedSupplier<T> computation) {
        Objects.requireNonNull(executorService, "executorService is null");
        Objects.requireNonNull(computation, "computation is null");
        final FutureImpl<T> future = new FutureImpl<>(executorService);
        future.run(computation);
        return future;
    }

    /**
     * Cancels the Future. A pending Future may be interrupted, depending on the underlying ExecutionService.
     *
     * @return false, if the Future is already completed or cancelled, otherwise true.
     */
    boolean cancel();

    /**
     * Returns the value of the Future.
     *
     * @return {@code None}, if the Future is not yet completed or was cancelled, otherwise {@code Some(Try)}.
     */
    Option<Try<T>> getValue();

    /**
     * Checks if the Future was cancelled.
     *
     * @return true, if the Future was cancelled, false otherwise.
     */
    boolean isCancelled();

    /**
     * Checks if the Future is completed, i.e. has a value.
     *
     * @return true, if the computation successfully finished or failed, false otherwise.
     */
    boolean isCompleted();

    /**
     * Performs the action once the Future is complete. Does nothing if the Future is cancelled.
     *
     * @param action An action to be performed when this future is complete.
     * @throws NullPointerException if {@code action} is null.
     */
    void onComplete(Consumer<? super Try<T>> action);

}

/**
 * {@link Future} implementation, for internal use only.
 * <p>
 * <strong>Lifecycle of a {@code FutureImpl}:</strong>
 * <p>
 * 1) Creation
 * <ul>
 * <li>{@code value = None}</li>
 * <li>{@code actions = Queue.empty()}</li>
 * <li>{@code job = null}</li>
 * <li>{@code cancelled = false}</li>
 * </ul>
 * 2) Run
 * <ul>
 * <li>{@code value = None}</li>
 * <li>{@code actions = Queue(...)}</li>
 * <li>{@code job = java.util.concurrent.Future}</li>
 * <li>{@code cancelled = false}</li>
 * </ul>
 * 3) Complete
 * <ul>
 * <li>{@code value = Some(Try)}</li>
 * <li>{@code actions = null}</li>
 * <li>{@code job = null}</li>
 * <li>{@code cancelled = false}</li>
 * </ul>
 * 4) Cancel
 * <ul>
 * <li>{@code value = None}</li>
 * <li>{@code actions = null}</li>
 * <li>{@code job = null}</li>
 * <li>{@code cancelled = true}</li>
 * </ul>
 *
 * @param <T> Result of the computation.
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
     * Reflects the cancelled state of the Future.
     *
     * @@GuardedBy("lock")
     */
    private volatile boolean cancelled = false;

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
    public boolean cancel() {
        synchronized (lock) {
            if (isCompleted() || isCancelled()) {
                return false;
            } else {
                if (job != null) {
                    Try.run(() -> job.cancel(true));
                }
                // we know that value is None because we hold the lock and checked it already
                cancelled = true;
                actions = null;
                job = null;
                // by definition cancellation succeeded
                return true;
            }
        }
    }

    @Override
    public Option<Try<T>> getValue() {
        return value;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
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
                } else if (!isCancelled()) {
                    actions = actions.enqueue(action);
                }
            }
        }
    }

    /**
     * Runs a computation using the underlying ExecutorService.
     * If the Future already has been cancelled nothing happens.
     *
     * @throws IllegalStateException if the Future is pending or completed
     * @throws NullPointerException if {@code computation} is null.
     */
    void run(CheckedSupplier<T> computation) {
        Objects.requireNonNull(computation, "computation is null");
        synchronized (lock) {

            // guards
            if (isCancelled()) {
                return;
            }
            if (job != null) {
                throw new IllegalStateException("The Future is already running.");
            }
            if (isCompleted()) {
                throw new IllegalStateException("The Future is completed.");
            }

            // The current lock ensures that the job is assigned before the computation completes.
            // If submit() throws, actions may be in a dirty state, which should not matter.
            job = executorService.submit(() -> {

                final Try<T> value = Try.of(computation);
                final Queue<Consumer<? super Try<T>>> actions;

                // Depending on the underlying ExecutorService implementation, cancel() may have been called without
                // doing anything at all. In other words this thread might still run. Then the Future is 'cancelled'
                // (by definition), because there is no way to figure out the semantics of our ExecutorService.
                // To deal with threads that still run despite of cancellation, we make additional isCancelled() checks
                // to ensure 1) not to overwrite the cancelled state and 2) not to perform any actions.

                synchronized (lock) {
                    actions = this.actions;
                    if (!isCancelled()) {
                        this.value = new Some<>(value);
                        this.actions = null;
                        this.job = null;
                    }
                }

                if (!isCancelled()) {
                    actions.forEach(this::perform);
                }

                return value;
            });
        }
    }

    private void perform(Consumer<? super Try<T>> action) {
        if (!isCancelled()) {
            Try.run(() -> executorService.execute(() -> action.accept(value.get())));
        }
    }
}
