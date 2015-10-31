/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import javaslang.control.Failure;
import javaslang.control.Option;
import javaslang.control.Success;
import javaslang.control.Try;
import javaslang.control.Try.CheckedRunnable;
import javaslang.control.Try.CheckedSupplier;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

/**
 * A Future is a computation result that becomes available at some point.
 * <p>
 * The underlying {@code ExecutorService} is used to execute asynchronous handlers, e.g. via
 * {@code onComplete(...)}.
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
     * The default executor service is {@link ForkJoinPool#commonPool()}.
     */
    ExecutorService DEFAULT_EXECUTOR_SERVICE = ForkJoinPool.commonPool();

    /**
     * Creates a failed {@code Future} with the given {@code exception}, backed by the {@link #DEFAULT_EXECUTOR_SERVICE}.
     *
     * @param exception The reason why it failed.
     * @param <T>       The value type of a successful result.
     * @return A failed {@code Future}.
     * @throws NullPointerException if exception is null
     */
    static <T> Future<T> failed(Throwable exception) {
        Objects.requireNonNull(exception, "exception is null");
        return failed(DEFAULT_EXECUTOR_SERVICE, exception);
    }

    /**
     * Creates a failed {@code Future} with the given {@code exception}, backed by the given {@link ExecutorService}.
     *
     * @param executorService An executor service.
     * @param exception       The reason why it failed.
     * @param <T>             The value type of a successful result.
     * @return A failed {@code Future}.
     * @throws NullPointerException if executorService or exception is null
     */
    static <T> Future<T> failed(ExecutorService executorService, Throwable exception) {
        Objects.requireNonNull(executorService, "executorService is null");
        Objects.requireNonNull(exception, "exception is null");
        return Promise.<T> failed(executorService, exception).future();
    }

    // TODO: find

    /**
     * Returns a new {@code Future} that will contain the result of the first of the given futures that is completed,
     * backed by the {@link #DEFAULT_EXECUTOR_SERVICE}.
     *
     * @param futures An iterable of futures.
     * @param <T>     The result type.
     * @return A new {@code Future}.
     */
    static <T> Future<T> firstCompletedOf(Iterable<Future<? extends T>> futures) {
        return firstCompletedOf(DEFAULT_EXECUTOR_SERVICE, futures);
    }

    /**
     * Returns a new {@code Future} that will contain the result of the first of the given futures that is completed,
     * backed by the given {@link ExecutorService}.
     *
     * @param executorService An executor service.
     * @param futures         An iterable of futures.
     * @param <T>             The result type.
     * @return A new {@code Future}.
     */
    static <T> Future<T> firstCompletedOf(ExecutorService executorService, Iterable<Future<? extends T>> futures) {
        final Promise<T> promise = Promise.make(executorService);
        final Consumer<Try<? extends T>> completeFirst = promise::tryComplete;
        futures.forEach(future -> future.onComplete(completeFirst));
        return promise.future();
    }

    // TODO: fold

    /**
     * Creates a {@code Future} from a {@link Try}, backed by the {@link #DEFAULT_EXECUTOR_SERVICE}.
     *
     * @param result The result.
     * @param <T>    The value type of a successful result.
     * @return A completed {@code Future} which contains either a {@code Success} or a {@code Failure}.
     * @throws NullPointerException if result is null
     */
    static <T> Future<T> fromTry(Try<T> result) {
        return fromTry(DEFAULT_EXECUTOR_SERVICE, result);
    }

    /**
     * Creates a {@code Future} from a {@link Try}, backed by the given {@link ExecutorService}.
     *
     * @param executorService An {@code ExecutorService}.
     * @param result          The result.
     * @param <T>             The value type of a successful result.
     * @return A completed {@code Future} which contains either a {@code Success} or a {@code Failure}.
     * @throws NullPointerException if executorService or result is null
     */
    static <T> Future<T> fromTry(ExecutorService executorService, Try<T> result) {
        Objects.requireNonNull(executorService, "executorService is null");
        Objects.requireNonNull(result, "result is null");
        return Promise.fromTry(executorService, result).future();
    }

    /**
     * Starts an asynchronous computation, backed by the {@link #DEFAULT_EXECUTOR_SERVICE}.
     *
     * @param computation A computation.
     * @param <T>         Type of the computation result.
     * @return A new Future instance.
     * @throws NullPointerException if computation is null.
     */
    static <T> Future<T> of(CheckedSupplier<? extends T> computation) {
        return Future.of(DEFAULT_EXECUTOR_SERVICE, computation);
    }

    /**
     * Starts an asynchronous computation, backed by the given {@link ExecutorService}.
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

    // TODO: reduce

    /**
     * Runs an asynchronous computation, backed by the {@link #DEFAULT_EXECUTOR_SERVICE}.
     *
     * @param unit A unit of work.
     * @return A new Future instance which results in nothing.
     * @throws NullPointerException if unit is null.
     */
    static Future<Void> run(CheckedRunnable unit) {
        Objects.requireNonNull(unit, "unit is null");
        return Future.of(DEFAULT_EXECUTOR_SERVICE, () -> {
            unit.run();
            return null;
        });
    }

    /**
     * Starts an asynchronous computation, backed by the given {@link ExecutorService}.
     *
     * @param executorService An executor service.
     * @param unit            A unit of work.
     * @return A new Future instance which results in nothing.
     * @throws NullPointerException if one of executorService of unit is null.
     */
    static Future<Void> run(ExecutorService executorService, CheckedRunnable unit) {
        Objects.requireNonNull(executorService, "executorService is null");
        Objects.requireNonNull(unit, "unit is null");
        return Future.of(executorService, () -> {
            unit.run();
            return null;
        });
    }

    // TODO: sequence

    /**
     * Creates a succeeded {@code Future}, backed by the {@link #DEFAULT_EXECUTOR_SERVICE}.
     *
     * @param result The result.
     * @param <T>    The value type of a successful result.
     * @return A succeeded {@code Future}.
     */
    static <T> Future<T> successful(T result) {
        return successful(DEFAULT_EXECUTOR_SERVICE, result);
    }

    /**
     * Creates a succeeded {@code Future}, backed by the given {@link ExecutorService}.
     *
     * @param executorService An {@code ExecutorService}.
     * @param result          The result.
     * @param <T>             The value type of a successful result.
     * @return A succeeded {@code Future}.
     * @throws NullPointerException if executorService is null
     */
    static <T> Future<T> successful(ExecutorService executorService, T result) {
        Objects.requireNonNull(executorService, "executorService is null");
        return Promise.successful(executorService, result).future();
    }

    // TODO: traverse

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

    /**
     * Performs the action once the Future is complete and the result is a {@link Failure}.
     * Does nothing if the Future is cancelled.
     *
     * @param action An action to be performed when this future failed.
     * @throws NullPointerException if {@code action} is null.
     */
    default void onFailure(Consumer<? super Throwable> action) {
        Objects.requireNonNull(action, "action is null");
        onComplete(result -> result.onFailure(action));
    }

    /**
     * Performs the action once the Future is complete and the result is a {@link Success}.
     * Does nothing if the Future is cancelled.
     *
     * @param action An action to be performed when this future failed.
     * @throws NullPointerException if {@code action} is null.
     */
    default void onSuccess(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        onComplete(result -> result.onSuccess(action));
    }

}
