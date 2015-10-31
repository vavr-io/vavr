/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import javaslang.control.Option;
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
    static <T> Future<T> of(CheckedSupplier<? extends T> computation) {
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
    static <T> Future<T> of(ExecutorService executorService, CheckedSupplier<? extends T> computation) {
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
     * Returns the {@link ExecutorService} used by this {@code Future}.
     *
     * @return The underlying {@code ExecutorService}.
     */
    ExecutorService executorService();

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
