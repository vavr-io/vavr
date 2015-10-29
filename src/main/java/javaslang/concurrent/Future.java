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
 * A Future has two states: complete and not complete.
 * <p>
 * If a Future is <em>not complete</em>, {@code getValue()} returns {@code None}
 * and {@code onComplete()} will then enqueue the given {@code action}.
 * <p>
 * If a Future is <em>complete</em>, {@code getValue()} returns {@code Some(value)}
 * and {@code onComplete()} will immediately perform the given {@code action}.
 * <p>
 * <strong>Execution</strong>
 * <p>
 * Both the given {@code computation} and the given {@code actions} are executed using the underlying {@code Executor}.
 * If no {@code Executor} is specified, {@link ForkJoinPool#commonPool()} is used.
 *
 * @param <T> Type of the computation result.
 */
public interface Future<T> {

    /**
     * Starts an asynchronous computation, using the {@link ExecutorService} {@link ForkJoinPool#commonPool()}.
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
     * Starts an asynchronous computation, using the given {@link ExecutorService}.
     *
     * @param computation A computation.
     * @param <T>         Type of the computation result.
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
     * Returns the value of the Future.
     *
     * @return {@code None}, if the Future not yet completed, otherwise {@code Some(Try)}.
     */
    Option<Try<T>> getValue();

    /**
     * Checks if the Future is complete, i.e. has a value.
     *
     * @return true, if the computation finished, false otherwise.
     */
    boolean isComplete();

    /**
     * Performs the action once the Future is complete.
     *
     * @param action An action to be performed when this future is complete.
     * @throws NullPointerException if computation is null.
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
 * </ul>
 * The Future is *not* complete and does *not* process a computation. New actions may be added.
 * <p>
 * 2) Run
 * <ul>
 * <li>{@code value = None}</li>
 * <li>{@code actions = Queue(...)}</li>
 * <li>{@code job = java.util.concurrent.Future}</li>
 * </ul>
 * The Future is *not* complete and *does* process a computation. New actions may be added.
 * <p>
 * 3) Complete
 * <ul>
 * <li>{@code value = Some(result)}</li>
 * <li>{@code actions = null}</li>
 * <li>{@code job = null}</li>
 * </ul>
 * The Future *is* complete. New actions are performed immediately.
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
     */
    private final Object lock = new Object();

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
    public Option<Try<T>> getValue() {
        return value;
    }

    @Override
    public boolean isComplete() {
        return value.isDefined();
    }

    @Override
    public void onComplete(Consumer<? super Try<T>> action) {
        Objects.requireNonNull(action, "action is null");
        if (isComplete()) {
            perform(action);
        } else synchronized (lock) {
            if (isComplete()) {
                perform(action);
            } else {
                actions = actions.enqueue(action);
            }
        }
    }

    /**
     * Running this Future.
     * <p>
     * Notes: Running outside of the constructor to ensure 'this' is fully initialized.
     * This comes handy for Promise, a writable Future.
     *
     * @throws IllegalStateException if {@code run()} is called more than once.
     * @throws NullPointerException  if computation is null.
     */
    void run(CheckedSupplier<T> computation) {
        Objects.requireNonNull(computation, "computation is null");
        synchronized (lock) {

            if (job != null || isComplete()) {
                throw new IllegalStateException("A Future can be run only once.");
            }

            // the current lock ensure that the job is assigned before the computation completes
            job = executorService.submit(() -> {

                final Try<T> value = Try.of(computation);
                final Queue<Consumer<? super Try<T>>> actions;

                synchronized (lock) {
                    this.value = new Some<>(value);
                    actions = this.actions;
                    this.actions = null;
                    this.job = null;
                }

                actions.forEach(this::perform);

                return value;
            });
        }
    }

    private void perform(Consumer<? super Try<T>> action) {
        executorService.execute(() -> action.accept(value.get()));
    }
}
