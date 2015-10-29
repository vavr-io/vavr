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
 * <strong>State</strong>
 * <p>
 * A Future has four states: complete, cancelled, running and not running.
 * <p>
 * If a Future is <em>not complete</em> and <em>not cancelled</em>, {@code getValue()} returns {@code None}
 * and {@code onComplete()} will then enqueue the given {@code action}.
 * <p>
 * If a Future is <em>complete</em>, {@code getValue()} returns {@code Some(value)}
 * and {@code onComplete()} will immediately perform the given {@code action}.
 * A complete Future cannot be cancelled any more.
 * <p>
 * If a Future is <em>cancelled</em>, {@code getValue()} returns {@code None} and {@code onComplete()} will not
 * perform the given {@code action}.
 * <p>
 * <strong>Execution</strong>
 * <p>
 * Both the given {@code computation} and the given {@code actions} are executed using the underlying {@code Executor}.
 * If no {@code Executor} is specified, {@link ForkJoinPool#commonPool()} is used.
 *
 * @param <T> Type of the computation result.
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Future<T> {

    /**
     * Starts an asynchronous computation using the {@link ExecutorService} {@link ForkJoinPool#commonPool()}.
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
     * Cancels the Future.
     * <p>
     * It is ensured that a future that hasn't run yet will never run after this call.
     * If the Future is running, it may be interrupted.
     * <p>
     * If the Future is already completed or cancelled, nothing happens and the result is false.
     * Otherwise the state will be changed to cancelled and the result is true.
     * <p>
     * Please note that (different to {@link java.util.concurrent.Future#cancel(boolean)}) {@code isCompleted()}
     * (= isDone) and {@code isCancelled()} are mutually exclusive. If a Future {@code isCompleted()}, it is implied
     * that the Future value is defined, i.e. {@code Some}.
     *
     * @return true, if the Future hasn't run, isn't completed and wasn't cancelled yet, otherwise false.
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
     * Checks if the Future is complete, i.e. has a value.
     *
     * @return true, if the computation finished, false otherwise.
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
 * The Future is *not* complete and *not* cancelled and does *not* process a computation. New actions may be added.
 * <p>
 * 2) Run
 * <ul>
 * <li>{@code value = None}</li>
 * <li>{@code actions = Queue(...)}</li>
 * <li>{@code job = java.util.concurrent.Future}</li>
 * <li>{@code cancelled = false}</li>
 * </ul>
 * The Future is *not* complete and *not* cancelled and *does* process a computation. New actions may be added.
 * <p>
 * 3) Complete
 * <ul>
 * <li>{@code value = Some(result)}</li>
 * <li>{@code actions = null}</li>
 * <li>{@code job = null}</li>
 * <li>{@code cancelled = false}</li>
 * </ul>
 * The Future *is* complete and *not* cancelled. New actions are performed immediately.
 * <p>
 * 4) Cancel
 * <ul>
 * <li>{@code value = None}</li>
 * <li>{@code actions = null}</li>
 * <li>{@code job = null}</li>
 * <li>{@code cancelled = true}</li>
 * </ul>
 * The Future *is* cancelled and *not* complete. New actions are not performed at all.
 *
 * @param <T> Result of the computation.
 */
final class FutureImpl<T> implements Future<T> {

    /**
     * Used to start new threads.
     */
    private final ExecutorService executorService;

    /**
     * Ensures validity of the {@code Future} invariants reflected by the state of the {@code FutureImpl}
     * ({@code value}, {@code actions} and {@code job}).
     * <p>
     * {@code cancelled} and {@code value} are also accessed for reading without lock and therefore {@code volatile}
     */
    private final Object lock = new Object();

    /**
     * Reflects the cancelled state of the Future.
     *
     * @@GuardedBy("lock")
     */
    private volatile boolean cancelled = false;

    /**
     * Once the Future is completed, the value is defined and the actions are performed and set to null.
     * The fields 'value', 'actions' and 'job' are guarded by 'lock'.
     * Value represents the completed state.
     *
     * @@GuardedBy("lock")
     */
    private volatile Option<Try<T>> value = None.instance();

    /**
     * The queue of actions is built when calling onComplete() before the Future is completed.
     * After the Future is completed, the queue is set to null and actions are performed immediately.
     *
     * @@GuardedBy("lock")
     */
    private Queue<Consumer<? super Try<T>>> actions = Queue.empty();

    /**
     * Once a computation is started via run(), the job can be used to control the lifecycle of the computation
     * and to wait for the result.
     *
     * @@GuardedBy("lock")
     */
    private java.util.concurrent.Future<Try<T>> job = null;

    /**
     * Creates a Future, {@link #run(CheckedSupplier)} has to be called separately.
     *
     * @param executorService An {@link ExecutorService} to start and control a computation.
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
                    // may return false
                    job.cancel(true);
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
     * Running this Future.
     * <p>
     * If the Future already has been cancelled nothing happens.
     * <p>
     * Notes: Running outside of the constructor to ensure 'this' is fully initialized.
     * This comes handy for Promise, a writable Future.
     *
     * @throws IllegalStateException if the future is running or completed
     * @throws NullPointerException  if computation is null.
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
            // the current lock ensure that the job is assigned before the computation completes
            job = executorService.submit(() -> {

                final Try<T> value = Try.of(computation);
                final Queue<Consumer<? super Try<T>>> actions;

                // Depending on the underlying ExecutorService implementation cancel() may have been called without
                // doing anything at all. In this case the state of this Future is 'cancelled' (by definition), because
                // there is no way to figure out the semantics of our ExecutorService.
                // To deal with this, we make additional isCancelled() checks to ensure not to perform actions once
                // this Future is cancelled.

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
            executorService.execute(() -> action.accept(value.get()));
        }
    }
}
