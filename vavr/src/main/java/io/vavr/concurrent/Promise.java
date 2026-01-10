/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.concurrent;

import io.vavr.control.Try;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import org.jspecify.annotations.NonNull;

import static io.vavr.concurrent.Future.DEFAULT_EXECUTOR;

/**
 * A {@code Promise} is a write-once container for a read-only {@code Future}, allowing the underlying {@code Future}
 * to be completed with a value or an exception.
 * <p>
 * The associated {@code Executor} is used to run asynchronous handlers, for example, via
 * {@code promise.future().onComplete(...)}.
 *
 * <h2>Creation</h2>
 * <p>
 * {@code Promise} provides static factory methods to create new promises:
 * <ul>
 *   <li>Unfulfilled promises: {@link Promise#make()}</li>
 *   <li>Already completed promises:
 *     <ul>
 *       <li>{@link #failed(Throwable)}</li>
 *       <li>{@link #fromTry(Try)}</li>
 *       <li>{@link #successful(Object)}</li>
 *     </ul>
 *   </li>
 * </ul>
 * All factory methods also have variants that accept an {@link Executor}, allowing finer control over
 * thread usage and thread pool configuration.
 *
 * <h3>One-shot API</h3>
 * <p>
 * When a single {@code Thread} is responsible for completing the {@code Promise}, use one of the following methods.
 * Calls will throw an exception if the {@code Promise} has already been completed:
 * <ul>
 *   <li>{@link #complete(Try)}</li>
 *   <li>{@link #completeWith(Future)}</li>
 *   <li>{@link #failure(Throwable)}</li>
 *   <li>{@link #success(Object)}</li>
 * </ul>
 *
 * <h3>API for concurrent completion</h3>
 * <p>
 * When multiple {@code Thread}s may attempt to complete the {@code Promise}, use one of the following "try" methods.
 * Calls will return {@code false} if the {@code Promise} is already completed:
 * <ul>
 *   <li>{@link #tryComplete(Try)}</li>
 *   <li>{@link #tryCompleteWith(Future)}</li>
 *   <li>{@link #tryFailure(Throwable)}</li>
 *   <li>{@link #trySuccess(Object)}</li>
 * </ul>
 *
 * @param <T> the type of the value that completes the underlying {@code Future}
 * @author Daniel Dietrich
 */
public interface Promise<T> {

    /**
     * Creates a {@code Promise} that is already completed with a failure, using the 
     * {@link Future#DEFAULT_EXECUTOR} for asynchronous operations.
     *
     * @param exception the cause of the failure
     * @param <T>       the type of the value that would have been returned on success
     * @return a {@code Promise} completed with the given failure
     * @throws NullPointerException if {@code exception} is null
     */
    static <T> Promise<T> failed(@NonNull Throwable exception) {
        Objects.requireNonNull(exception, "exception is null");
        return failed(DEFAULT_EXECUTOR, exception);
    }

    /**
     * Creates a {@code Promise} that is already completed with a failure, using the specified {@link Executor}
     * for asynchronous operations.
     *
     * @param executor  the {@code Executor} used by the underlying {@link Future}
     * @param exception the cause of the failure
     * @param <T>       the type of the value that would have been returned on success
     * @return a {@code Promise} completed with the given failure
     * @throws NullPointerException if {@code executor} or {@code exception} is null
     */
    static <T> Promise<T> failed(@NonNull Executor executor, @NonNull Throwable exception) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(exception, "exception is null");
        return Promise.<T> make(executor).failure(exception);
    }

    /**
     * Creates a {@code Promise} from the given {@link Try}, using the {@link Future#DEFAULT_EXECUTOR}
     * for asynchronous operations.
     *
     * @param result the {@code Try} representing a success or failure
     * @param <T>    the type of the value in case of success
     * @return a {@code Promise} already completed with the given {@code Try} result
     * @throws NullPointerException if {@code result} is null
     */
    static <T> Promise<T> fromTry(@NonNull Try<? extends T> result) {
        return fromTry(DEFAULT_EXECUTOR, result);
    }

    /**
     * Creates a {@code Promise} from the given {@link Try}, using the specified {@link Executor}
     * for asynchronous operations.
     *
     * @param executor the {@code Executor} used by the underlying {@link Future}
     * @param result   the {@code Try} representing a success or failure
     * @param <T>      the type of the value in case of success
     * @return a {@code Promise} already completed with the given {@code Try} result
     * @throws NullPointerException if {@code executor} or {@code result} is null
     */
    static <T> Promise<T> fromTry(@NonNull Executor executor, @NonNull Try<? extends T> result) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(result, "result is null");
        return Promise.<T> make(executor).complete(result);
    }

    /**
     * Creates a new {@code Promise} that is not yet completed, using Virtual Threads for asynchronous operations.
     *
     * @param <T> the type of the value that will complete the {@code Promise}
     * @return a new, uncompleted {@code Promise}
     */
    static <T> Promise<T> make() {
        return make(DEFAULT_EXECUTOR);
    }

    /**
     * Creates a new {@code Promise} that is not yet completed, using the specified {@link Executor}
     * for asynchronous operations.
     *
     * @param executor the {@code Executor} used by the underlying {@link Future}
     * @param <T>      the type of the value that will complete the {@code Promise}
     * @return a new, uncompleted {@code Promise}
     * @throws NullPointerException if {@code executor} is null
     */
    static <T> Promise<T> make(@NonNull Executor executor) {
        Objects.requireNonNull(executor, "executor is null");
        return new PromiseImpl<>(FutureImpl.of(executor));
    }

    /**
     * Narrows a {@code Promise<? extends T>} to {@code Promise<T>} through a type-safe cast. 
     * This is safe because immutable or read-only collections are covariant.
     *
     * @param promise the {@code Promise} to narrow
     * @param <T>     the component type of the {@code Promise}
     * @return the same {@code promise} instance, cast to {@code Promise<T>}
     */
    @SuppressWarnings("unchecked")
    static <T> Promise<T> narrow(Promise<? extends T> promise) {
        return (Promise<T>) promise;
    }

    /**
     * Creates a {@code Promise} that is already completed successfully, using the 
     * {@link Future#DEFAULT_EXECUTOR} for asynchronous operations.
     *
     * @param result the value of the successful result
     * @param <T>    the type of the value
     * @return a {@code Promise} already completed with the given result
     */
    static <T> Promise<T> successful(T result) {
        return successful(DEFAULT_EXECUTOR, result);
    }

    /**
     * Creates a {@code Promise} that is already completed successfully, using the specified {@link Executor}
     * for asynchronous operations.
     *
     * @param executor the {@code Executor} used by the underlying {@link Future}
     * @param result   the value of the successful result
     * @param <T>      the type of the value
     * @return a {@code Promise} already completed with the given result
     * @throws NullPointerException if {@code executor} is null
     */
    static <T> Promise<T> successful(@NonNull Executor executor, T result) {
        Objects.requireNonNull(executor, "executor is null");
        return Promise.<T> make(executor).success(result);
    }

    /**
     * Returns the {@link Executor} used by the underlying {@link Future} of this {@code Promise}.
     *
     * @return the {@code Executor} associated with this {@code Promise}
     */
    default Executor executor() {
        return executorService();
    }

    /**
     * This method is deprecated.
     * <p>
     * THE DEFAULT IMPLEMENTATION (obtained by one of the {@link Promise} factory methods) MIGHT THROW AN
     * {@link UnsupportedOperationException} AT RUNTIME, DEPENDING ON WHAT {@link Future#executorService()}
     * returns.
     *
     * @return (never)
     * @throws UnsupportedOperationException if the underlying {@link Executor} isn't an {@link ExecutorService}.
     * @deprecated Removed starting with Vavr 0.10.0, use {@link #executor()} instead.
     */
    @Deprecated
    ExecutorService executorService();

    /**
     * Returns the underlying {@link Future} associated with this {@code Promise}.
     *
     * @return the underlying {@code Future}
     */
    Future<T> future();

    /**
     * Checks whether this {@code Promise} has been completed, either successfully or with a failure.
     *
     * @return {@code true} if the {@code Promise} is completed, {@code false} otherwise
     */
    default boolean isCompleted() {
        return future().isCompleted();
    }

    /**
     * Completes this {@code Promise} with the given {@code value}.
     *
     * @param value a {@link Try.Success} containing the result or a {@link Try.Failure} containing an exception
     * @return this {@code Promise}
     * @throws IllegalStateException if this {@code Promise} has already been completed
     */
    default Promise<T> complete(@NonNull Try<? extends T> value) {
        if (tryComplete(value)) {
            return this;
        } else {
            throw new IllegalStateException("Promise already completed.");
        }
    }

    /**
     * Attempts to complete this {@code Promise} with the given {@code value}.
     *
     * @param value a {@link Try.Success} containing the result or a {@link Try.Failure} containing an exception
     * @return {@code true} if the {@code Promise} was completed successfully, 
     *         {@code false} if it was already completed
     */
    boolean tryComplete(@NonNull Try<? extends T> value);

    /**
     * Completes this {@code Promise} with the result of the given {@code Future} once it is completed.
     *
     * @param other the {@code Future} whose result or failure will complete this {@code Promise}
     * @return this {@code Promise}
     */
    default Promise<T> completeWith(@NonNull Future<? extends T> other) {
        return tryCompleteWith(other);
    }

    /**
     * Attempts to complete this {@code Promise} with the result of the given {@code Future} once it is completed.
     *
     * @param other the {@code Future} whose result or failure may complete this {@code Promise}
     * @return {@code true} if this {@code Promise} was completed by {@code other}, 
     *         {@code false} if it was already completed
     */
    default Promise<T> tryCompleteWith(@NonNull Future<? extends T> other) {
        other.onComplete(this::tryComplete);
        return this;
    }

    /**
     * Completes this {@code Promise} with the given value.
     *
     * @param value the value to complete this {@code Promise} with
     * @return this {@code Promise}
     * @throws IllegalStateException if this {@code Promise} has already been completed
     */
    default Promise<T> success(T value) {
        return complete(Try.success(value));
    }

    /**
     * Attempts to complete this {@code Promise} with the given value.
     *
     * @param value the value to complete this {@code Promise} with
     * @return {@code true} if the {@code Promise} was completed successfully, 
     *         {@code false} if it was already completed
     */
    default boolean trySuccess(T value) {
        return tryComplete(Try.success(value));
    }

    /**
     * Completes this {@code Promise} with the given exception.
     *
     * @param exception the exception to complete this {@code Promise} with
     * @return this {@code Promise}
     * @throws IllegalStateException if this {@code Promise} has already been completed
     */
    default Promise<T> failure(@NonNull Throwable exception) {
        return complete(Try.failure(exception));
    }

    /**
     * Attempts to complete this {@code Promise} with the given exception.
     *
     * @param exception the exception to complete this {@code Promise} with
     * @return {@code true} if the {@code Promise} was completed successfully, 
     *         {@code false} if it was already completed
     */
    default boolean tryFailure(@NonNull Throwable exception) {
        return tryComplete(Try.failure(exception));
    }
}

/**
 * Internal {@code Promise} implementation.
 *
 * @param <T> result type
 * @author Daniel Dietrich
 */
final class PromiseImpl<T> implements Promise<T> {

    private final FutureImpl<T> future;

    PromiseImpl(FutureImpl<T> future) {
        this.future = future;
    }

    @Override
    public Executor executor() {
        return future.executor();
    }

    @Deprecated
    @Override
    public ExecutorService executorService() {
        return future.executorService();
    }

    @Override
    public Future<T> future() {
        return future;
    }

    @Override
    public boolean tryComplete(@NonNull Try<? extends T> value) {
        return future.tryComplete(value);
    }

    // The underlying FutureImpl is MUTABLE and therefore we CANNOT CHANGE DEFAULT equals() and hashCode() behavior.
    // See http://stackoverflow.com/questions/4718009/mutable-objects-and-hashcode

    @Override
    public String toString() {
        return "Promise(" + future.getValue().map(String::valueOf).getOrElse("?") + ")";
    }
}
