/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import javaslang.control.Failure;
import javaslang.control.Success;
import javaslang.control.Try;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import static javaslang.concurrent.Future.DEFAULT_EXECUTOR_SERVICE;

/**
 * A Promise is a write-once wrapper around a read-only Future which can complete the underlying Future with a value
 * or an exception.
 * <p>
 * The underlying {@code ExecutorService} is used to execute asynchronous handlers, e.g. via
 * {@code promise.future().onComplete(...)}.
 *
 * @param <T> The result type of the underlying {@code Future}.
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Promise<T> {

    /**
     * Creates a failed {@code Promise}, backed by the {@link Future#DEFAULT_EXECUTOR_SERVICE}.
     *
     * @param exception The reason why it failed.
     * @param <T>       The value type of a successful result.
     * @return A failed {@code Promise}.
     * @throws NullPointerException if exception is null
     */
    static <T> Promise<T> failed(Throwable exception) {
        Objects.requireNonNull(exception, "exception is null");
        return failed(DEFAULT_EXECUTOR_SERVICE, exception);
    }

    /**
     * Creates a failed {@code Promise}, backed by the given {@link ExecutorService}.
     *
     * @param executorService An {@code ExecutorService} passed to the underlying {@link Future}.
     * @param exception       The reason why it failed.
     * @param <T>             The value type of a successful result.
     * @return A failed {@code Promise}.
     * @throws NullPointerException if executorService or exception is null
     */
    static <T> Promise<T> failed(ExecutorService executorService, Throwable exception) {
        Objects.requireNonNull(executorService, "executorService is null");
        Objects.requireNonNull(exception, "exception is null");
        return Promise.<T> make(executorService).failure(exception);
    }

    /**
     * Creates a {@code Promise} from a {@link Try}, backed by the {@link Future#DEFAULT_EXECUTOR_SERVICE}.
     *
     * @param result The result.
     * @param <T>    The value type of a successful result.
     * @return A completed {@code Promise} which contains either a {@code Success} or a {@code Failure}.
     * @throws NullPointerException if result is null
     */
    static <T> Promise<T> fromTry(Try<? extends T> result) {
        return fromTry(DEFAULT_EXECUTOR_SERVICE, result);
    }

    /**
     * Creates a {@code Promise} from a {@link Try}, backed by the given {@link ExecutorService}.
     *
     * @param executorService An {@code ExecutorService} passed to the underlying {@link Future}.
     * @param result          The result.
     * @param <T>             The value type of a successful result.
     * @return A completed {@code Promise} which contains either a {@code Success} or a {@code Failure}.
     * @throws NullPointerException if executorService or result is null
     */
    static <T> Promise<T> fromTry(ExecutorService executorService, Try<? extends T> result) {
        Objects.requireNonNull(executorService, "executorService is null");
        Objects.requireNonNull(result, "result is null");
        return Promise.<T> make(executorService).complete(result);
    }

    /**
     * Makes a {@code Promise} that isn't fulfilled yet, backed by the {@link Future#DEFAULT_EXECUTOR_SERVICE}.
     * {@link ForkJoinPool#commonPool()}.
     *
     * @param <T> Result type of the {@code Promise}.
     * @return A new {@code Promise}.
     */
    static <T> Promise<T> make() {
        return make(DEFAULT_EXECUTOR_SERVICE);
    }

    /**
     * Makes a {@code Promise} that isn't fulfilled yet, backed by the given {@link ExecutorService}.
     *
     * @param executorService An {@code ExecutorService} passed to the underlying {@link Future}.
     * @param <T>             Result type of the {@code Promise}.
     * @return A new {@code Promise}.
     * @throws NullPointerException if executorService is null
     */
    static <T> Promise<T> make(ExecutorService executorService) {
        Objects.requireNonNull(executorService, "executorService is null");
        return new PromiseImpl<>(new FutureImpl<>(executorService));
    }

    /**
     * Creates a succeeded {@code Promise}, backed by the {@link Future#DEFAULT_EXECUTOR_SERVICE}.
     *
     * @param result The result.
     * @param <T>    The value type of a successful result.
     * @return A succeeded {@code Promise}.
     */
    static <T> Promise<T> successful(T result) {
        return successful(DEFAULT_EXECUTOR_SERVICE, result);
    }

    /**
     * Creates a succeeded {@code Promise}, backed by the given {@link ExecutorService}.
     *
     * @param executorService An {@code ExecutorService} passed to the underlying {@link Future}.
     * @param result          The result.
     * @param <T>             The value type of a successful result.
     * @return A succeeded {@code Promise}.
     * @throws NullPointerException if executorService is null
     */
    static <T> Promise<T> successful(ExecutorService executorService, T result) {
        Objects.requireNonNull(executorService, "executorService is null");
        return Promise.<T> make(executorService).success(result);
    }

    /**
     * Returns the {@link ExecutorService} used by this {@code Future}.
     *
     * @return The underlying {@code ExecutorService}.
     */
    ExecutorService executorService();

    /**
     * Returns the underlying {@link Future} of this {@code Promise}.
     *
     * @return The {@code Future}.
     */
    Future<T> future();

    /**
     * Checks if this {@code Promise} is completed, i.e. has a value.
     *
     * @return true, if the computation successfully finished or failed, false otherwise.
     */
    default boolean isCompleted() {
        return future().isCompleted();
    }

    /**
     * Completes this {@code Promise} with the given {@code value}.
     *
     * @param value Either a {@link Success} containing the result or a {@link Failure} containing an exception.
     * @return This {@code Promise}.
     * @throws IllegalStateException if this {@code Promise} has already been completed.
     */
    default Promise<T> complete(Try<? extends T> value) {
        if (tryComplete(value)) {
            return this;
        } else {
            throw new IllegalStateException("Promise already completed.");
        }
    }

    /**
     * Attempts to completes this {@code Promise} with the given {@code value}.
     *
     * @param value Either a {@link Success} containing the result or a {@link Failure} containing an exception.
     * @return {@code false} if this {@code Promise} has already been completed, {@code true} otherwise.
     * @throws IllegalStateException if this {@code Promise} has already been completed.
     */
    boolean tryComplete(Try<? extends T> value);

    /**
     * Completes this {@code Promise} with the given {@code Future}, once that {@code Future} is completed.
     *
     * @param other Another {@code Future} to react on.
     * @return This {@code Promise}.
     */
    default Promise<T> completeWith(Future<? extends T> other) {
        return tryCompleteWith(other);
    }

    /**
     * Attempts to complete this {@code Promise} with the specified {@code Future}, once that {@code Future} is completed.
     *
     * @param other Another {@code Future} to react on.
     * @return This {@code Promise}.
     */
    default Promise<T> tryCompleteWith(Future<? extends T> other) {
        other.onComplete(this::tryComplete);
        return this;
    }

    /**
     * Completes this {@code Promise} with the given {@code value}.
     *
     * @param value A value.
     * @return This {@code Promise}.
     * @throws IllegalStateException if this {@code Promise} has already been completed.
     */
    default Promise<T> success(T value) {
        return complete(new Success<>(value));
    }

    /**
     * Completes this {@code Promise} with the given {@code value}.
     *
     * @param value A value.
     * @return {@code false} if this {@code Promise} has already been completed, {@code true} otherwise.
     */
    default boolean trySuccess(T value) {
        return tryComplete(new Success<>(value));
    }

    /**
     * Completes this {@code Promise} with the given {@code exception}.
     *
     * @param exception An exception.
     * @return This {@code Promise}.
     * @throws IllegalStateException if this {@code Promise} has already been completed.
     */
    default Promise<T> failure(Throwable exception) {
        return complete(new Failure<>(exception));
    }

    /**
     * Completes this {@code Promise} with the given {@code exception}.
     *
     * @param exception An exception.
     * @return {@code false} if this {@code Promise} has already been completed, {@code true} otherwise.
     */
    default boolean tryFailure(Throwable exception) {
        return tryComplete(new Failure<>(exception));
    }
}

/**
 * Internal {@code Promise} implementation.
 *
 * @param <T> result type
 * @author Daniel Dietrich
 * @since 2.0.0
 */
final class PromiseImpl<T> implements Promise<T> {

    final FutureImpl<T> future;

    PromiseImpl(FutureImpl<T> future) {
        this.future = future;
    }

    @Override
    public ExecutorService executorService() {
        return future.executorService();
    }

    @Override
    public Future<T> future() {
        return future;
    }

    @Override
    public boolean tryComplete(Try<? extends T> value) {
        return Try.of(() -> future.complete(value)).isSuccess();
    }

    // The underlying FutureImpl is MUTABLE and therefore we CANNOT CHANGE DEFAULT equals() and hashCode() behavior.
    // See http://stackoverflow.com/questions/4718009/mutable-objects-and-hashcode

    @Override
    public String toString() {
        return "Promise(" + future + ")";
    }
}
