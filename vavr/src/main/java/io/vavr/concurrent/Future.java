/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.concurrent;

import io.vavr.*;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;
import org.jspecify.annotations.NonNull;

/**
 * Represents the result of an asynchronous computation that becomes available at some point in the future.
 * All operations provided by this {@code Future} are non-blocking.
 *
 * <p>The underlying {@link java.util.concurrent.Executor} is used to execute asynchronous handlers, for example via
 * {@code onComplete(...)}.</p>
 *
 * <p>A {@code Future} has two states:</p>
 * <ul>
 *     <li><strong>Pending:</strong> The computation is ongoing. Only a pending future may be completed or cancelled.</li>
 *     <li><strong>Completed:</strong> The computation has finished, either successfully with a result, with an exception, or via cancellation.</li>
 * </ul>
 *
 * <p>Callbacks may be registered on a {@code Future} at any time. These callbacks are executed as soon as the
 * {@code Future} completes. If a callback is registered on a completed {@code Future}, it is executed immediately.
 * Execution may occur on a separate thread, depending on the underlying {@code Executor}. Callbacks registered
 * on a cancelled {@code Future} are executed with the failed result.</p>
 *
 * @param <T> the type of the computation result
 * @author Daniel Dietrich, Grzegorz Piwowarek
 */
public interface Future<T> extends Value<T> {

    Executor DEFAULT_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Creates a failed {@code Future} with the given {@code exception}, using the {@link #DEFAULT_EXECUTOR}
     * to execute callbacks.
     *
     * @param exception the exception that caused the failure
     * @param <T>       the type of successful result
     * @return a failed {@code Future} containing the given exception
     * @throws NullPointerException if {@code exception} is null
     */
    static <T> Future<T> failed(@NonNull Throwable exception) {
        Objects.requireNonNull(exception, "exception is null");
        return failed(DEFAULT_EXECUTOR, exception);
    }

    /**
     * Creates a failed {@code Future} with the given {@code exception}, executing callbacks using the specified {@link Executor}.
     *
     * @param executor  the {@link Executor} to run asynchronous handlers
     * @param exception the exception that caused the failure
     * @param <T>       the type of successful result
     * @return a failed {@code Future} containing the given exception
     * @throws NullPointerException if {@code executor} or {@code exception} is null
     */
    static <T> Future<T> failed(@NonNull Executor executor, @NonNull Throwable exception) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(exception, "exception is null");
        return FutureImpl.of(executor, Try.failure(exception));
    }

    /**
     * Returns a {@code Future} that completes with the first result of the given {@code futures}
     * that satisfies the specified {@code predicate}. If no result matches, the {@code Future} will contain {@link Option.None}.
     *
     * <p>The returned {@code Future} uses the {@link #DEFAULT_EXECUTOR} to execute callbacks.</p>
     *
     * @param futures   an iterable of futures to inspect
     * @param predicate a predicate to test successful future results
     * @param <T>       the type of the future results
     * @return a {@code Future} of an {@link Option} containing the first matching result, or {@link Option.None} if none match
     * @throws NullPointerException if {@code futures} or {@code predicate} is null
     */
    static <T> Future<Option<T>> find(@NonNull Iterable<? extends Future<? extends T>> futures, @NonNull Predicate<? super T> predicate) {
        return find(DEFAULT_EXECUTOR, futures, predicate);
    }

    /**
     * Returns a {@code Future} that completes with the first result of the given {@code futures}
     * that satisfies the specified {@code predicate}. If no result matches, the {@code Future} will contain {@link Option.None}.
     *
     * <p>The returned {@code Future} executes using the provided {@link Executor}.</p>
     *
     * @param executor  the {@link Executor} to run asynchronous handlers
     * @param futures   an iterable of futures to inspect
     * @param predicate a predicate to test successful future results
     * @param <T>       the type of the future results
     * @return a {@code Future} of an {@link Option} containing the first matching result, or {@link Option.None} if none match
     * @throws NullPointerException if any argument is null
     */
    static <T> Future<Option<T>> find(@NonNull Executor executor, @NonNull Iterable<? extends Future<? extends T>> futures, @NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(futures, "futures is null");
        Objects.requireNonNull(predicate, "predicate is null");
        final List<Future<? extends T>> list = List.ofAll(futures);
        if (list.isEmpty()) {
            return successful(executor, Option.none());
        } else {
            return run(executor, complete -> {
                final AtomicBoolean completed = new AtomicBoolean(false);
                final AtomicInteger count = new AtomicInteger(list.length());
                final Lock lock = new ReentrantLock();
                list.forEach(future -> future.onComplete(result -> {
                    lock.lock();
                    try {
                        // if the future is already completed we already found our result and there is nothing more to do.
                        if (!completed.get()) {
                            // when there are no more results we return a None
                            final boolean wasLast = count.decrementAndGet() == 0;
                            // when result is a Failure or predicate is false then we check in onFailure for finish
                            result.filter(predicate)
                                    .onSuccess(value -> completed.set(complete.with(Try.success(Option.some(value)))))
                                    .onFailure(ignored -> {
                                        if (wasLast) {
                                            completed.set(complete.with(Try.success(Option.none())));
                                        }
                                    });
                        }
                    } finally {
                        lock.unlock();
                    }
                }));
            });
        }
    }

    /**
     * Returns a new {@code Future} that completes with the result of the first future in the given iterable
     * that completes, using the {@link #DEFAULT_EXECUTOR}.
     *
     * @param futures an iterable of futures to observe
     * @param <T>     the type of the future results
     * @return a new {@code Future} containing the result of the first completed future
     * @throws NullPointerException if {@code futures} is null
     */
    static <T> Future<T> firstCompletedOf(@NonNull Iterable<? extends Future<? extends T>> futures) {
        return firstCompletedOf(DEFAULT_EXECUTOR, futures);
    }

    /**
     * Returns a new {@code Future} that completes with the result of the first future in the given iterable
     * that completes, using the specified {@link Executor}.
     *
     * @param executor the {@link Executor} to run asynchronous handlers
     * @param futures  an iterable of futures to observe
     * @param <T>      the type of the future results
     * @return a new {@code Future} containing the result of the first completed future
     * @throws NullPointerException if {@code executor} or {@code futures} is null
     */
    static <T> Future<T> firstCompletedOf(@NonNull Executor executor, @NonNull Iterable<? extends Future<? extends T>> futures) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(futures, "futures is null");
        return run(executor, complete -> futures.forEach(future -> future.onComplete(complete::with)));
    }

    /**
     * Returns a {@code Future} that contains the result of folding the given future values.
     * If any future fails or the fold operation throws an exception, the resulting {@code Future} will also fail.
     *
     * <p>The resulting {@code Future} executes using the {@link #DEFAULT_EXECUTOR}.</p>
     *
     * @param futures an iterable of futures to fold
     * @param zero    the initial value for the fold
     * @param f       the fold operation
     * @param <T>     the type of the values in the given futures
     * @param <U>     the result type of the fold
     * @return a new {@code Future} containing the fold result
     * @throws NullPointerException if {@code futures} or {@code f} is null
     */
    static <T, U> Future<U> fold(@NonNull Iterable<? extends Future<? extends T>> futures, U zero, @NonNull BiFunction<? super U, ? super T, ? extends U> f) {
        return fold(DEFAULT_EXECUTOR, futures, zero, f);
    }

    /**
     * Returns a {@code Future} containing the result of folding the given future values.
     * If any future fails or the fold operation throws an exception, the resulting {@code Future} will also fail.
     *
     * <p>The resulting {@code Future} executes using the specified {@link Executor}.</p>
     *
     * @param executor the {@link Executor} to run asynchronous handlers
     * @param futures  an iterable of futures to fold
     * @param zero     the initial value for the fold
     * @param f        the fold operation
     * @param <T>      the type of the values in the given futures
     * @param <U>      the result type of the fold
     * @return a new {@code Future} containing the fold result
     * @throws NullPointerException if {@code executor}, {@code futures}, or {@code f} is null
     */
    static <T, U> Future<U> fold(@NonNull Executor executor, @NonNull Iterable<? extends Future<? extends T>> futures, U zero, @NonNull BiFunction<? super U, ? super T, ? extends U> f) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(futures, "futures is null");
        Objects.requireNonNull(f, "f is null");
        if (!futures.iterator().hasNext()) {
            return successful(executor, zero);
        } else {
            return sequence(executor, futures).map(seq -> seq.foldLeft(zero, f));
        }
    }

    /**
     * Creates a {@code Future} that wraps the given {@link java.util.concurrent.Future},
     * executing callbacks on the {@link #DEFAULT_EXECUTOR}.
     *
     * @param future the {@link java.util.concurrent.Future} to wrap
     * @param <T>    the result type of the future
     * @return a new {@code Future} containing the result of the given Java future
     * @throws NullPointerException if {@code future} is null
     */
    static <T> Future<T> fromJavaFuture(java.util.concurrent.@NonNull Future<T> future) {
        Objects.requireNonNull(future, "future is null");
        return of(DEFAULT_EXECUTOR, future::get);
    }

    /**
     * Creates a {@code Future} that wraps the given {@link java.util.concurrent.Future},
     * executing callbacks using the specified {@link Executor}.
     *
     * @param executor the {@link Executor} to run asynchronous handlers
     * @param future   the {@link java.util.concurrent.Future} to wrap
     * @param <T>      the result type of the future
     * @return a new {@code Future} containing the result of the given Java future
     * @throws NullPointerException if {@code executor} or {@code future} is null
     */
    static <T> Future<T> fromJavaFuture(@NonNull Executor executor, java.util.concurrent.@NonNull Future<T> future) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(future, "future is null");
        return of(executor, future::get);
    }

    /**
     * Creates a {@code Future} that wraps the given {@link java.util.concurrent.CompletableFuture},
     * using the {@link #DEFAULT_EXECUTOR} for executing callbacks.
     *
     * @param future the {@link java.util.concurrent.CompletableFuture} to wrap
     * @param <T>    the result type of the future
     * @return a new {@code Future} containing the result of the given {@link java.util.concurrent.CompletableFuture}
     * @throws NullPointerException if {@code future} is null
     */
    static <T> Future<T> fromCompletableFuture(@NonNull CompletableFuture<T> future) {
        return fromCompletableFuture(DEFAULT_EXECUTOR, future);
    }

    /**
     * Creates a {@code Future} that wraps the given {@link java.util.concurrent.CompletableFuture},
     * executing callbacks using the specified {@link Executor}.
     *
     * @param executor the {@link Executor} to run asynchronous handlers
     * @param future   the {@link java.util.concurrent.CompletableFuture} to wrap
     * @param <T>      the result type of the future
     * @return a new {@code Future} containing the result of the given {@link java.util.concurrent.CompletableFuture}
     * @throws NullPointerException if {@code executor} or {@code future} is null
     */
    static <T> Future<T> fromCompletableFuture(@NonNull Executor executor, @NonNull CompletableFuture<T> future) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(future, "future is null");
        if (future.isDone() || future.isCompletedExceptionally() || future.isCancelled()) {
            return fromTry(Try.of(future::get).recoverWith(error -> Try.failure(error.getCause())));
        } else {
            return run(executor, complete ->
                    future.handle((t, err) -> complete.with((err == null) ? Try.success(t) : Try.failure(err)))
            );
        }
    }

    /**
     * Creates a {@code Future} from a {@link Try}, using the {@link #DEFAULT_EXECUTOR}.
     *
     * @param result the {@link Try} result to wrap
     * @param <T>    the type of a successful result
     * @return a completed {@code Future} containing either a {@code Success} or a {@code Failure}
     * @throws NullPointerException if {@code result} is null
     */
    static <T> Future<T> fromTry(@NonNull Try<? extends T> result) {
        return fromTry(DEFAULT_EXECUTOR, result);
    }

    /**
     * Creates a {@code Future} from a {@link Try}, executing callbacks using the specified {@link Executor}.
     *
     * @param executor the {@link Executor} to run asynchronous handlers
     * @param result   the {@link Try} result to wrap
     * @param <T>      the type of successful result
     * @return a completed {@code Future} containing either a {@code Success} or a {@code Failure}
     * @throws NullPointerException if {@code executor} or {@code result} is null
     */
    static <T> Future<T> fromTry(@NonNull Executor executor, @NonNull Try<? extends T> result) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(result, "result is null");
        return FutureImpl.of(executor, result);
    }

    /**
     * Narrows a {@code Future<? extends T>} to {@code Future<T>} via a type-safe cast.
     * This is safe because immutable or read-only collections are covariant.
     *
     * @param future the {@code Future} to narrow
     * @param <T>    the type of the value contained in the future
     * @return the given {@code future} instance as a {@code Future<T>}
     */
    @SuppressWarnings("unchecked")
    static <T> Future<T> narrow(Future<? extends T> future) {
        return (Future<T>) future;
    }

    /**
     * Starts an asynchronous computation using the {@link #DEFAULT_EXECUTOR}.
     *
     * @param computation the computation to execute asynchronously
     * @param <T>         the type of the computation result
     * @return a new {@code Future} containing the result of the computation
     * @throws NullPointerException if {@code computation} is null
     */
    static <T> Future<T> of(@NonNull CheckedFunction0<? extends T> computation) {
        return of(DEFAULT_EXECUTOR, computation);
    }

    /**
     * Starts an asynchronous computation using the specified {@link Executor}.
     *
     * @param executor    the {@link Executor} to run asynchronous handlers
     * @param computation the computation to execute asynchronously
     * @param <T>         the type of the computation result
     * @return a new {@code Future} containing the result of the computation
     * @throws NullPointerException if {@code executor} or {@code computation} is null
     */
    static <T> Future<T> of(@NonNull Executor executor, @NonNull CheckedFunction0<? extends T> computation) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(computation, "computation is null");
        return FutureImpl.async(executor, complete -> complete.with(Try.of(computation)));
    }

    /**
     * Creates a (possibly blocking) Future that runs the results of the given {@code computation}
     * using a completion handler:
     *
     * <pre>{@code
     * CheckedConsumer<Predicate<Try<T>>> computation = complete -> {
     *     // computation
     * };
     * }</pre>
     *
     * The {@code computation} is executed synchronously. It requires to complete the returned Future.
     * A common use-case is to hand over the {@code complete} predicate to another {@code Future}
     * in order to prevent blocking:
     *
     * <pre>{@code
     * Future<String> greeting(Future<String> nameFuture) {
     *     return Future.run(complete -> {
     *         nameFuture.onComplete(name -> complete.test("Hi " + name));
     *     });
     * }}</pre>
     *
     * The computation receives a {@link Predicate}, named {@code complete} by convention,
     * that takes a result of type {@code Try<T>} and returns a boolean that states whether the
     * Future was completed.
     * <p>
     * Future completion is an idempotent operation in the way that the first call of {@code complete}
     * will return true, successive calls will return false.
     *
     * @param task A computational task
     * @param <T>  Type of the result
     * @return a new {@code Future} instance
     * @deprecated Experimental API
     */
    @Deprecated
    static <T> Future<T> run(@NonNull Task<? extends T> task) {
        return run(DEFAULT_EXECUTOR, task);
    }

    /**
     * Creates a (possibly blocking) Future that runs the results of the given {@code computation}
     * using a completion handler:
     *
     * <pre>{@code
     * CheckedConsumer<Predicate<Try<T>>> computation = complete -> {
     *     // computation
     * };
     * }</pre>
     *
     * The {@code computation} is executed synchronously. It requires to complete the returned Future.
     * A common use-case is to hand over the {@code complete} predicate to another {@code Future}
     * in order to prevent blocking:
     *
     * <pre>{@code
     * Future<String> greeting(Future<String> nameFuture) {
     *     return Future.run(complete -> {
     *         nameFuture.onComplete(name -> complete.with("Hi " + name));
     *     });
     * }}</pre>
     *
     * The computation receives a {@link Predicate}, named {@code complete} by convention,
     * that takes a result of type {@code Try<T>} and returns a boolean that states whether the
     * Future was completed.
     * <p>
     * Future completion is an idempotent operation in the way that the first call of {@code complete}
     * will return true, successive calls will return false.
     *
     * @param executor An {@link Executor} that runs the given {@code computation}
     * @param task     A computational task
     * @param <T>      Type of the result
     * @return a new {@code Future} instance
     * @deprecated Experimental API
     */
    @Deprecated
    static <T> Future<T> run(@NonNull Executor executor, @NonNull Task<? extends T> task) {
        return FutureImpl.sync(executor, task);
    }

    /**
     * Returns a {@code Future} containing the result of reducing the given future values.
     * The first completed future serves as the initial (zero) value.
     * If any future or the reduce operation fails, the resulting {@code Future} will also fail.
     *
     * <p>The resulting {@code Future} executes using the {@link #DEFAULT_EXECUTOR}.</p>
     *
     * @param futures an iterable of futures to reduce
     * @param f       the reduce operation
     * @param <T>     the type of the values in the given futures
     * @return a new {@code Future} containing the reduce result
     * @throws NullPointerException if {@code futures} or {@code f} is null
     */
    static <T> Future<T> reduce(@NonNull Iterable<? extends Future<? extends T>> futures, @NonNull BiFunction<? super T, ? super T, ? extends T> f) {
        return reduce(DEFAULT_EXECUTOR, futures, f);
    }

    /**
     * Returns a {@code Future} containing the result of reducing the given future values.
     * The first completed future serves as the initial (zero) value.
     * If any future or the reduce operation fails, the resulting {@code Future} will also fail.
     *
     * <p>The resulting {@code Future} executes using the specified {@link Executor}.</p>
     *
     * @param executor the {@link Executor} to run asynchronous handlers
     * @param futures  an iterable of futures to reduce
     * @param f        the reduce operation
     * @param <T>      the type of the values in the given futures
     * @return a new {@code Future} containing the reduce result
     * @throws NullPointerException if {@code executor}, {@code futures}, or {@code f} is null
     */
    static <T> Future<T> reduce(@NonNull Executor executor, @NonNull Iterable<? extends Future<? extends T>> futures, @NonNull BiFunction<? super T, ? super T, ? extends T> f) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(futures, "futures is null");
        Objects.requireNonNull(f, "f is null");
        if (!futures.iterator().hasNext()) {
            throw new NoSuchElementException("Future.reduce on empty futures");
        } else {
            return Future.sequence(executor, futures).map(seq -> seq.reduceLeft(f));
        }
    }

    /**
     * Runs an asynchronous computation using the {@link #DEFAULT_EXECUTOR}.
     *
     * @param unit a unit of work to execute asynchronously
     * @return a new {@code Future} representing the completion of the computation, with no result
     * @throws NullPointerException if {@code unit} is null
     */
    static Future<Void> run(@NonNull CheckedRunnable unit) {
        return run(DEFAULT_EXECUTOR, unit);
    }

    /**
     * Starts an asynchronous computation using the specified {@link Executor}.
     *
     * @param executor the {@link Executor} to run asynchronous handlers
     * @param unit     a unit of work to execute asynchronously
     * @return a new {@code Future} representing the completion of the computation, with no result
     * @throws NullPointerException if {@code executor} or {@code unit} is null
     */

    static Future<Void> run(@NonNull Executor executor, @NonNull CheckedRunnable unit) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(unit, "unit is null");
        return of(executor, () -> {
            unit.run();
            return null;
        });
    }

    /**
     * Reduces multiple {@code Future} instances into a single {@code Future} by transforming an
     * {@code Iterable<Future<? extends T>>} into a {@code Future<Seq<T>>}.
     *
     * <p>The resulting {@code Future} executes using the {@link #DEFAULT_EXECUTOR}.</p>
     *
     * <ul>
     *     <li>
     *         If all given futures succeed, the resulting future also succeeds:
     *         <pre>{@code
     * // = Future(Success(Seq(1, 2)))
     * sequence(
     *     List.of(
     *         Future.of(() -> 1),
     *         Future.of(() -> 2)
     *     )
     * );
     *         }</pre>
     *     </li>
     *     <li>
     *         If any given future fails, the resulting future fails as well:
     *         <pre>{@code
     * // = Future(Failure(Error))
     * sequence(
     *     List.of(
     *         Future.of(() -> 1),
     *         Future.of(() -> { throw new Error(); })
     *     )
     * );
     *         }</pre>
     *     </li>
     * </ul>
     *
     * @param futures an {@code Iterable} of {@code Future}s
     * @param <T>     the result type of the futures
     * @return a {@code Future} containing a {@link Seq} of results
     * @throws NullPointerException if {@code futures} is null
     */
    static <T> Future<Seq<T>> sequence(@NonNull Iterable<? extends Future<? extends T>> futures) {
        return sequence(DEFAULT_EXECUTOR, futures);
    }

    /**
     * Reduces multiple {@code Future} instances into a single {@code Future} by transforming an
     * {@code Iterable<Future<? extends T>>} into a {@code Future<Seq<T>>}.
     *
     * <p>The resulting {@code Future} executes using the specified {@link Executor}.</p>
     *
     * @param executor the {@link Executor} to run asynchronous handlers
     * @param futures  an {@code Iterable} of {@code Future}s to reduce
     * @param <T>      the result type of the futures
     * @return a {@code Future} containing a {@link Seq} of results
     * @throws NullPointerException if {@code executor} or {@code futures} is null
     */
    static <T> Future<Seq<T>> sequence(@NonNull Executor executor, @NonNull Iterable<? extends Future<? extends T>> futures) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(futures, "futures is null");
        final Future<Seq<T>> zero = successful(executor, Stream.empty());
        final BiFunction<Future<Seq<T>>, Future<? extends T>, Future<Seq<T>>> f =
                (result, future) -> result.flatMap(seq -> future.map(seq::append));
        return Iterator.ofAll(futures).foldLeft(zero, f);
    }

    /**
     * Creates a succeeded {@code Future} with the given result, using the {@link #DEFAULT_EXECUTOR}
     * to execute callbacks.
     *
     * @param result the successful result
     * @param <T>    the type of the result
     * @return a succeeded {@code Future} containing the given result
     */
    static <T> Future<T> successful(T result) {
        return successful(DEFAULT_EXECUTOR, result);
    }

    /**
     * Creates a succeeded {@code Future} with the given result, executing callbacks using the specified {@link Executor}.
     *
     * @param executor the {@link Executor} to run asynchronous handlers
     * @param result   the successful result
     * @param <T>      the type of the result
     * @return a succeeded {@code Future} containing the given result
     * @throws NullPointerException if {@code executor} is null
     */
    static <T> Future<T> successful(@NonNull Executor executor, T result) {
        Objects.requireNonNull(executor, "executor is null");
        return FutureImpl.of(executor, Try.success(result));
    }

    @Override
    default CompletableFuture<T> toCompletableFuture() {
        final CompletableFuture<T> future = new CompletableFuture<>();
        onSuccess(future::complete);
        onFailure(future::completeExceptionally);
        return future;
    }

    /**
     * Maps the values of an iterable in parallel to a sequence of mapped values, producing a single {@code Future}
     * by transforming an {@code Iterable<? extends T>} into a {@code Future<Seq<U>>}.
     *
     * <p>The resulting {@code Future} executes using the {@link #DEFAULT_EXECUTOR}.</p>
     *
     * @param values an {@code Iterable} of input values
     * @param mapper a function mapping values to {@code Future}s
     * @param <T>    the type of the input values
     * @param <U>    the type of the mapped values
     * @return a {@code Future} containing a {@link Seq} of mapped results
     * @throws NullPointerException if {@code values} or {@code mapper} is null
     */
    static <T, U> Future<Seq<U>> traverse(@NonNull Iterable<? extends T> values, @NonNull Function<? super T, ? extends Future<? extends U>> mapper) {
        return traverse(DEFAULT_EXECUTOR, values, mapper);
    }

    /**
     * Maps the values of an iterable in parallel to a sequence of mapped values, producing a single {@code Future}
     * by transforming an {@code Iterable<? extends T>} into a {@code Future<Seq<U>>}.
     *
     * <p>The resulting {@code Future} executes using the specified {@link Executor}.</p>
     *
     * @param executor the {@link Executor} to run asynchronous handlers
     * @param values   an {@code Iterable} of input values
     * @param mapper   a function mapping values to {@code Future}s
     * @param <T>      the type of the input values
     * @param <U>      the type of the mapped values
     * @return a {@code Future} containing a {@link Seq} of mapped results
     * @throws NullPointerException if {@code executor}, {@code values}, or {@code mapper} is null
     */
    static <T, U> Future<Seq<U>> traverse(@NonNull Executor executor, @NonNull Iterable<? extends T> values, @NonNull Function<? super T, ? extends Future<? extends U>> mapper) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(values, "values is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return sequence(executor, Iterator.ofAll(values).map(mapper));
    }

    // -- non-static Future API

    /**
     * Supports chaining of callbacks that are guaranteed to be executed in order.
     *
     * <p>Exceptions thrown by the given {@code action} are not propagated. Subsequent actions
     * are executed based on the value of the original {@code Future}.</p>
     *
     * <p>Example:</p>
     * <pre>{@code
     * // prints Success(1)
     * Future.of(() -> 1)
     *       .andThen(t -> { throw new Error(""); })
     *       .andThen(System.out::println);
     * }</pre>
     *
     * @param action a side-effecting action to perform after the future completes
     * @return a new {@code Future} containing the original result, completed after the action executes
     * @throws NullPointerException if {@code action} is null
     */
    default Future<T> andThen(@NonNull Consumer<? super Try<T>> action) {
        Objects.requireNonNull(action, "action is null");
        return run(executor(), complete ->
                onComplete(t -> {
                    Try.run(() -> action.accept(t));
                    complete.with(t);
                })
        );
    }

    /**
     * Blocks the current thread until this {@code Future} is completed, or returns immediately if it is already completed.
     *
     * <p>If the current thread is interrupted while waiting, a failed {@code Future} is returned containing
     * the corresponding {@link InterruptedException}.</p>
     *
     * @return this {@code Future} instance
     */
    Future<T> await();

    /**
     * Blocks the current thread until this {@code Future} is completed, or returns immediately if it is already completed.
     *
     * <p>If the current thread is interrupted while waiting, a failed {@code Future} is returned containing
     * the corresponding {@link InterruptedException}.</p>
     *
     * <p>If the specified timeout is reached before completion, a failed {@code Future} is returned containing
     * a {@link TimeoutException}.</p>
     *
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     * @return this {@code Future} instance
     * @throws IllegalArgumentException if {@code timeout} is negative
     * @throws NullPointerException     if {@code unit} is null
     */
    Future<T> await(long timeout, TimeUnit unit);

    /**
     * Cancels this {@code Future}. If it is running, the executing thread is interrupted.
     *
     * <p>If the future is successfully cancelled, its result becomes a {@code Failure(CancellationException)}.</p>
     *
     * @return {@code false} if this {@code Future} is already completed or could not be cancelled, {@code true} otherwise
     * @throws SecurityException if the current thread is not permitted to modify the Future's thread
     * @see Future#isCancelled()
     */
    default boolean cancel() {
        return cancel(true);
    }

    /**
     * Cancels this {@code Future}. A pending future may be interrupted depending on the underlying {@code Executor}.
     *
     * <p>If the future is successfully cancelled, its result becomes a {@code Failure(CancellationException)}.</p>
     *
     * @param mayInterruptIfRunning {@code true} if a running thread should be interrupted; {@code false} allows it to complete
     * @return {@code false} if this {@code Future} is already completed or could not be cancelled, {@code true} otherwise
     * @throws SecurityException if the current thread is not permitted to modify the Future's thread
     * @see Future#isCancelled()
     * @see java.util.concurrent.Future#cancel(boolean)
     */
    boolean cancel(boolean mayInterruptIfRunning);

    /**
     * Applies a {@code partialFunction} to the value of this {@code Future}, collecting results only for values
     * where the function is defined. The mapped result is wrapped in a new {@code Future}.
     *
     * <p>Example:</p>
     * <pre>{@code
     * if (partialFunction.isDefinedAt(value)) {
     *     R newValue = partialFunction.apply(value);
     * }
     * }</pre>
     *
     * @param partialFunction a function that may not be defined for every value of this future
     * @param <R>             the type of the mapped result
     * @return a new {@code Future} containing the mapped value
     * @throws NullPointerException if {@code partialFunction} is null
     */
    default <R> Future<R> collect(@NonNull PartialFunction<? super T, ? extends R> partialFunction) {
        Objects.requireNonNull(partialFunction, "partialFunction is null");
        return run(executor(), complete ->
            onComplete(result -> complete.with(result.collect(partialFunction)))
        );
    }

    /**
     * Returns the {@link Executor} that executes asynchronous handlers for this {@code Future}.
     *
     * @return the underlying {@code Executor}
     */
    default Executor executor() {
        return executorService();
    }

    /**
     * This method is deprecated.
     * <p>
     * THE DEFAULT IMPLEMENTATION (obtained by one of the {@link Future} factory methods) MIGHT THROW AN
     * {@link UnsupportedOperationException} AT RUNTIME.
     *
     * @return (never)
     * @throws UnsupportedOperationException if the underlying {@link Executor} isn't an {@link ExecutorService}.
     * @deprecated Removed starting with Vavr 0.10.0, use {@link #executor()} instead.
     */
    @Deprecated
    ExecutorService executorService() throws UnsupportedOperationException;

    /**
     * Returns a projection that inverts the result of this {@code Future}.
     *
     * <p>If this {@code Future} succeeds, the resulting failed projection contains a {@code NoSuchElementException}.</p>
     * <p>If this {@code Future} fails, the resulting failed projection succeeds with the exception as its value.</p>
     *
     * @return a new {@code Future} representing the inverted result
     */
    default Future<Throwable> failed() {
        return run(executor(), complete ->
            onComplete(result -> {
                if (result.isFailure()) {
                    complete.with(Try.success(result.getCause()));
                } else {
                    complete.with(Try.failure(new NoSuchElementException("Future.failed completed without a throwable")));
                }
            })
        );
    }

    /**
     * Returns a {@code Future} that yields the result of this {@code Future} if it succeeds.
     * If this {@code Future} fails, the result of the given {@code that} {@code Future} is returned if it succeeds.
     * If both {@code Future}s fail, the failure of this {@code Future} is returned.
     *
     * <p>Example:</p>
     * <pre>{@code
     * Future<Integer> future = Future.of(() -> { throw new Error(); });
     * Future&lt;Integer&gt; that = Future.of(() -> 1);
     * Future&lt;Integer&gt; result = future.fallbackTo(that);
     *
     * // prints Success(1)
     * result.onComplete(System.out::println);
     * }</pre>
     *
     * @param that a fallback {@code Future} to use if this one fails
     * @return a new {@code Future} representing the result or fallback
     * @throws NullPointerException if {@code that} is null
     */
    default Future<T> fallbackTo(@NonNull Future<? extends T> that) {
        Objects.requireNonNull(that, "that is null");
        return run(executor(), complete ->
            onComplete(t -> {
                if (t.isSuccess()) {
                    complete.with(t);
                } else {
                    that.onComplete(alt -> complete.with(alt.isSuccess() ? alt : t));
                }
            })
        );
    }

    /**
     * Shortcut for {@code filterTry(predicate::test)}, filtering the result of this {@code Future} using the given predicate.
     *
     * @param predicate a predicate to test the value of the future
     * @return a new {@code Future} containing the value if the predicate passes, or a failure otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default Future<T> filter(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filterTry(predicate::test);
    }

    /**
     * Filters the result of this {@code Future} using the given {@link CheckedPredicate}, delegating to
     * {@link Try#filterTry(CheckedPredicate)}.
     *
     * @param predicate a checked predicate to test the value of the future
     * @return a new {@code Future} containing the value if the predicate passes, or a failure otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default Future<T> filterTry(@NonNull CheckedPredicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return run(executor(), complete -> onComplete(result -> complete.with(result.filterTry(predicate))));
    }

    /**
     * Returns the underlying exception. This is syntactic sugar for
     * {@code future.getValue().map(Try::getCause)}.
     *
     * @return {@code None} if the {@code Future} is not yet completed, or {@code Some(Throwable)} if it completed with a failure
     * @throws UnsupportedOperationException if the {@code Future} completed successfully with a value
     */

    default Option<Throwable> getCause() {
        return getValue().map(Try::getCause);
    }

    /**
     * Returns the value of this {@code Future}.
     *
     * @return {@code None} if the {@code Future} is not yet completed or was cancelled; otherwise, {@code Some(Try)} containing the result or failure
     */
    Option<Try<T>> getValue();

    /**
     * Checks whether this {@code Future} was cancelled, i.e., its computation was interrupted before completion.
     *
     * @return {@code true} if the computation was cancelled, {@code false} otherwise
     */
    boolean isCancelled();

    /**
     * Checks whether this {@code Future} is completed, i.e., whether it has finished with a value, failed, or was cancelled.
     *
     * @return {@code true} if the computation has completed in any state, {@code false} otherwise
     */
    boolean isCompleted();

    /**
     * Checks whether this {@code Future} completed successfully.
     *
     * @return {@code true} if this {@code Future} has completed with a successful result, {@code false} otherwise
     */
    default boolean isSuccess() {
        return isCompleted() && getValue().get().isSuccess();
    }

    /**
     * Checks whether this {@code Future} completed with a failure.
     *
     * @return {@code true} if this {@code Future} has completed with a failure, {@code false} otherwise
     */
    default boolean isFailure() {
        return isCompleted() && getValue().get().isFailure();
    }

    /**
     * Performs the given action once this {@code Future} is complete.
     *
     * @param action an action to execute when the future completes
     * @return this {@code Future}
     * @throws NullPointerException if {@code action} is null
     */
    Future<T> onComplete(@NonNull Consumer<? super Try<T>> action);

    /**
     * Performs the given action once this {@code Future} is complete and its result is a {@link Try.Failure}.
     * <p>
     * Note that a cancelled {@code Future} is also considered a failure.
     *
     * @param action an action to execute when this future fails
     * @return this {@code Future}
     * @throws NullPointerException if {@code action} is null
     */
    default Future<T> onFailure(@NonNull Consumer<? super Throwable> action) {
        Objects.requireNonNull(action, "action is null");
        return onComplete(result -> result.onFailure(action));
    }

    /**
     * Performs the given action once this {@code Future} is complete and its result is a {@link Try.Success}.
     *
     * @param action an action to execute when this future succeeds
     * @return this {@code Future}
     * @throws NullPointerException if {@code action} is null
     */
    default Future<T> onSuccess(@NonNull Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        return onComplete(result -> result.onSuccess(action));
    }

    /**
     * Handles a failure of this {@code Future} by mapping the exception to a new result.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // = "oh!"
     * Future.of(() -> { throw new Error("oh!"); })
     *       .recover(Throwable::getMessage);
     * }</pre>
     *
     * @param f a function that maps the failure exception to a new value
     * @return a new {@code Future} containing either the original success or the recovered value
     * @throws NullPointerException if {@code f} is null
     */
    default Future<T> recover(@NonNull Function<? super Throwable, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        return transformValue(t -> t.recover(f));
    }

    /**
     * Handles a failure of this {@code Future} by returning the result of another {@code Future}.
     *
     * <p>Example:</p>
     * <pre>{@code
     * // = "oh!"
     * Future.of(() -> { throw new Error("oh!"); })
     *       .recoverWith(ex -> Future.of(() -> ex.getMessage()));
     * }</pre>
     *
     * @param f a function that maps the failure exception to a new {@code Future}
     * @return a new {@code Future} containing either the original success or the result of the recovered {@code Future}
     * @throws NullPointerException if {@code f} is null
     */
    default Future<T> recoverWith(@NonNull Function<? super Throwable, ? extends Future<? extends T>> f) {
        Objects.requireNonNull(f, "f is null");
        return run(executor(), complete ->
            onComplete(t -> {
                if (t.isFailure()) {
                    Try.run(() -> f.apply(t.getCause()).onComplete(complete::with))
                            .onFailure(x -> complete.with(Try.failure(x)));
                } else {
                    complete.with(t);
                }
            })
        );
    }

    /**
     * Transforms the result of this {@code Future} using the given function.
     *
     * @param f   a function to transform the result
     * @param <U> the type of the transformed result
     * @return a new {@code Future} containing the transformed result
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(@NonNull Function<? super Future<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    /**
     * Transforms the value of this {@code Future}, regardless of whether it completed successfully or with a failure.
     *
     * @param f   a function to transform the {@link Try} result
     * @param <U> the type of the transformed {@code Try} result
     * @return a new {@code Future} containing the transformed result
     * @throws NullPointerException if {@code f} is null
     */
    default <U> Future<U> transformValue(@NonNull Function<? super Try<T>, ? extends Try<? extends U>> f) {
        Objects.requireNonNull(f, "f is null");
        return run(executor(), complete ->
            onComplete(t -> Try.run(() -> complete.with(f.apply(t)))
                    .onFailure(x -> complete.with(Try.failure(x)))
            )
        );
    }

    /**
     * Combines this {@code Future} with another {@code Future}, returning a {@code Future} of a tuple of both results.
     * <p>
     * If this {@code Future} fails, the resulting {@code Future} contains this failure. Otherwise, it contains
     * the failure of {@code that} {@code Future}, or a tuple of both successful results if both succeed.
     *
     * @param that another {@code Future} to combine with
     * @param <U>  the result type of {@code that}
     * @return a new {@code Future} containing either a failure or a tuple of both results
     * @throws NullPointerException if {@code that} is null
     */
    default <U> Future<Tuple2<T, U>> zip(@NonNull Future<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        return zipWith(that, Tuple::of);
    }

    /**
     * Combines this {@code Future} with another {@code Future} using the given combinator function.
     * <p>
     * If this {@code Future} fails, the resulting {@code Future} contains this failure. Otherwise, it contains
     * the failure of {@code that} {@code Future}, or the result of applying the combinator function to both successful results.
     *
     * @param that       another {@code Future} to combine with
     * @param combinator a function to combine the successful results of both futures
     * @param <U>        the result type of {@code that}
     * @param <R>        the result type of the combined value
     * @return a new {@code Future} containing either a failure or the combined result
     * @throws NullPointerException if {@code that} or {@code combinator} is null
     */
    @SuppressWarnings({"unchecked"})
    default <U, R> Future<R> zipWith(@NonNull Future<? extends U> that, @NonNull BiFunction<? super T, ? super U, ? extends R> combinator) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(combinator, "combinator is null");
        return run(executor(), complete ->
            onComplete(res1 -> {
                if (res1.isFailure()) {
                    complete.with((Try.Failure<R>) res1);
                } else {
                    that.onComplete(res2 -> {
                        final Try<R> result = res1.flatMap(t -> res2.map(u -> combinator.apply(t, u)));
                        complete.with(result);
                    });
                }
            })
        );
    }

    // -- Value & Monad implementation

    /**
     * Transforms the value of this {@code Future} using the given {@link Function} if it completes successfully,
     * or returns a {@code Future} with the failure if this {@code Future} fails.
     * <p>
     * This is a shortcut for {@link #flatMapTry(CheckedFunction1)}.
     *
     * @param mapper a function mapping the value to another {@code Future}
     * @param <U>    the type of the resulting {@code Future}
     * @return a new {@code Future} resulting from applying the mapper, or a {@code Future} with the failure if this {@code Future} fails
     * @throws NullPointerException if {@code mapper} is {@code null}
     */
    default <U> Future<U> flatMap(@NonNull Function<? super T, ? extends Future<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return flatMapTry(mapper::apply);
    }

    /**
     * Transforms the value of this {@code Future} using the given {@link CheckedFunction1} if it completes successfully,
     * or returns a {@code Future} with the failure if this {@code Future} fails.
     * <p>
     * If applying the mapper throws an exception, a {@code Future} containing the exception is returned.
     *
     * @param mapper a checked function mapping the value to another {@code Future}
     * @param <U>    the type of the resulting {@code Future}
     * @return a new {@code Future} resulting from applying the mapper, or a {@code Future} with the failure if this {@code Future} fails
     * @throws NullPointerException if {@code mapper} is {@code null}
     */
    default <U> Future<U> flatMapTry(@NonNull CheckedFunction1<? super T, ? extends Future<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return run(executor(), complete ->
            onComplete(result -> result.mapTry(mapper)
                    .onSuccess(future -> future.onComplete(complete::with))
                    .onFailure(x -> complete.with(Try.failure(x)))
            )
        );
    }

    /**
     * Performs the given {@code action} asynchronously when this {@code Future} completes successfully.
     * <p>
     * The {@code action} is not executed if the {@code Future} completes with a failure.
     *
     * @param action a {@code Consumer} to be executed with the successful result
     * @throws NullPointerException if {@code action} is null
     */
    @Override
    default void forEach(@NonNull Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        onComplete(result -> result.forEach(action));
    }

    /**
     * Returns the value of this {@code Future} if it completed successfully, or throws the underlying exception
     * if it completed with a failure. Blocks the current thread if the computation is not yet finished.
     *
     * <p><strong>Note:</strong> If the computation failed, the underlying {@link Throwable} cause is thrown.</p>
     *
     * @return the successful result of this {@code Future}
     */
    @Override
    default T get() {
        return await().getValue().get().get();
    }

    /**
     * A {@code Futures}'s value is computed asynchronously.
     *
     * @return true
     */
    @Override
    default boolean isAsync() {
        return true;
    }

    /**
     * Checks, if this future has a value.
     *
     * @return true, if this future succeeded with a value, false otherwise.
     */
    @Override
    default boolean isEmpty() {
        return await().getValue().get().isEmpty();
    }

    /**
     * A {@code Future}'s value is computed eagerly.
     *
     * @return false
     */
    @Override
    default boolean isLazy() {
        return false;
    }

    /**
     * A {@code Future} is single-valued.
     *
     * @return {@code true}
     */
    @Override
    default boolean isSingleValued() {
        return true;
    }

    @Override
    default @NonNull Iterator<T> iterator() {
        return isEmpty() ? Iterator.empty() : Iterator.of(get());
    }

    @Override
    default <U> Future<U> map(@NonNull Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return transformValue(t -> t.map(mapper));
    }

    @Override
    default <U> Future<U> mapTo(U value) {
        return map(ignored -> value);
    }

    @Override
    default Future<Void> mapToVoid() {
        return map(ignored -> null);
    }

    /**
     * Maps the value of this {@code Future} to a new value using the given {@link CheckedFunction1} if it completes successfully.
     * <p>
     * If applying the mapper throws an exception, a {@code Future} containing the exception is returned.
     * <p>
     * Example:
     * <pre>{@code
     * Future.of(() -> 0)
     *       .mapTry(x -> 1 / x); // division by zero will result in a failed Future
     * }</pre>
     *
     * @param <U>    the type of the result
     * @param mapper a checked function to apply to the value
     * @return a new {@code Future} containing the mapped value if this {@code Future} completes successfully, otherwise a {@code Future} with the failure
     * @throws NullPointerException if {@code mapper} is {@code null}
     */
    default <U> Future<U> mapTry(@NonNull CheckedFunction1<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return transformValue(t -> t.mapTry(mapper));
    }

    /**
     * Returns this {@code Future} if it completes successfully, or the given alternative {@code Future} if this {@code Future} fails.
     *
     * @param other the alternative {@code Future} to return if this {@code Future} fails
     * @return this {@code Future} if it completes successfully, otherwise {@code other}
     * @throws NullPointerException if {@code other} is null
     */
    default Future<T> orElse(@NonNull Future<? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        return run(executor(), complete ->
            onComplete(result -> {
                if (result.isSuccess()) {
                    complete.with(result);
                } else {
                    other.onComplete(complete::with);
                }
            })
        );
    }

    /**
     * Returns this {@code Future} if it completes successfully, or a {@code Future} supplied by the given {@link Supplier} if this {@code Future} fails.
     * <p>
     * The supplier is only invoked if this {@code Future} fails.
     *
     * @param supplier a supplier of an alternative {@code Future}
     * @return this {@code Future} if it completes successfully, otherwise the {@code Future} returned by {@code supplier}
     * @throws NullPointerException if {@code supplier} is null
     */
    default Future<T> orElse(@NonNull Supplier<? extends Future<? extends T>> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return run(executor(), complete ->
            onComplete(result -> {
                if (result.isSuccess()) {
                    complete.with(result);
                } else {
                    supplier.get().onComplete(complete::with);
                }
            })
        );
    }

    @Override
    default Future<T> peek(@NonNull Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        onSuccess(action);
        return this;
    }

    @Override
    default String stringPrefix() {
        return "Future";
    }

}
