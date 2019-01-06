/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
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
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.collection.List;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;

/**
 * A Future is a computation result that becomes available at some point. All operations provided are non-blocking.
 * <p>
 * The underlying {@code Executor} is used to execute asynchronous handlers, e.g. via
 * {@code onComplete(...)}.
 * <p>
 * A Future has two states: pending and completed.
 * <ul>
 * <li>Pending: The computation is ongoing. Only a pending future may be completed or cancelled.</li>
 * <li>Completed: The computation finished successfully with a result, failed with an exception or was cancelled.</li>
 * </ul>
 * Callbacks may be registered on a Future at each point of time. These actions are performed as soon as the Future
 * is completed. An action which is registered on a completed Future is immediately performed. The action may run on
 * a separate Thread, depending on the underlying Executor. Actions which are registered on a cancelled
 * Future are performed with the failed result.
 *
 * @param <T> Type of the computation result.
 * @author Daniel Dietrich
 */
public interface Future<T> extends Value<T> {

    /**
     * The default executor service is {@link ForkJoinPool#commonPool()}.
     * <p>
     * Facts about ForkJoinPool:
     *
     * <ul>
     * <li>It is work-stealing, i.e. all threads in the pool attempt to find work submitted to the pool.
     * Especially this is efficient under heavy load (many small tasks), e.g. when tasks create subtasks
     * (recursive threads).</li>
     * <li>The ForkJoinPool is dynamic, it has a maximum of 32767 running threads. Compared to fixed-size pools,
     * this reduces the risk of dead-locks.</li>
     * <li>The commonPool() is shared across the entire VM. Keep this in mind when also using
     * {@link java.util.stream.Stream#parallel()} and {@link java.util.concurrent.CompletableFuture}}</li>
     * </ul>
     *
     * The ForkJoinPool creates daemon threads but its run state is unaffected by attempts to shutdown() or shutdownNow().
     * However, all running tasks are immediately terminated upon program System.exit(int).
     * <p>
     * IMPORTANT: Invoke {@code ForkJoinPool.commonPool().awaitQuiescence(long, TimeUnit)} before exit in order to
     * ensure that all running async tasks complete before program termination.
     *
     * @see ForkJoinPool#awaitQuiescence(long, TimeUnit)
     * @deprecated Will be removed in Vavr 1.0. Use {@link #DEFAULT_EXECUTOR instead}.
     */
    @Deprecated
    // See https://zeroturnaround.com/rebellabs/fixedthreadpool-cachedthreadpool-or-forkjoinpool-picking-correct-java-executors-for-background-tasks
    ExecutorService DEFAULT_EXECUTOR_SERVICE = ForkJoinPool.commonPool();

    /**
     * The default executor is {@link ForkJoinPool#commonPool()}.
     * <p>
     * Facts about ForkJoinPool:
     *
     * <ul>
     * <li>It is work-stealing, i.e. all threads in the pool attempt to find work submitted to the pool.
     * Especially this is efficient under heavy load (many small tasks), e.g. when tasks create subtasks
     * (recursive threads).</li>
     * <li>The ForkJoinPool is dynamic, it has a maximum of 32767 running threads. Compared to fixed-size pools,
     * this reduces the risk of dead-locks.</li>
     * <li>The commonPool() is shared across the entire VM. Keep this in mind when also using
     * {@link java.util.stream.Stream#parallel()} and {@link java.util.concurrent.CompletableFuture}}</li>
     * </ul>
     *
     * The ForkJoinPool creates daemon threads but its run state is unaffected by attempts to shutdown() or shutdownNow().
     * However, all running tasks are immediately terminated upon program System.exit(int).
     * <p>
     * IMPORTANT: Invoke {@code ForkJoinPool.commonPool().awaitQuiescence(long, TimeUnit)} before exit in order to
     * ensure that all running async tasks complete before program termination.
     *
     * @see ForkJoinPool#awaitQuiescence(long, TimeUnit)
     */
    // See https://zeroturnaround.com/rebellabs/fixedthreadpool-cachedthreadpool-or-forkjoinpool-picking-correct-java-executors-for-background-tasks
    Executor DEFAULT_EXECUTOR = DEFAULT_EXECUTOR_SERVICE;

    /**
     * Creates a failed {@code Future} with the given {@code exception}, backed by the {@link #DEFAULT_EXECUTOR}.
     *
     * @param exception The reason why it failed.
     * @param <T>       The value type of a successful result.
     * @return A failed {@code Future}.
     * @throws NullPointerException if exception is null
     */
    static <T> Future<T> failed(Throwable exception) {
        Objects.requireNonNull(exception, "exception is null");
        return failed(DEFAULT_EXECUTOR, exception);
    }

    /**
     * Creates a failed {@code Future} with the given {@code exception}, backed by the given {@link Executor}.
     *
     * @param executor  An {@link Executor}.
     * @param exception The reason why it failed.
     * @param <T>       The value type of a successful result.
     * @return A failed {@code Future}.
     * @throws NullPointerException if executor or exception is null
     */
    static <T> Future<T> failed(Executor executor, Throwable exception) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(exception, "exception is null");
        return FutureImpl.of(executor, Try.failure(exception));
    }

    /**
     * Returns a {@code Future} that eventually succeeds with the first result of the given {@code Future}s which
     * matches the given {@code predicate}. If no result matches, the {@code Future} will contain {@link Option.None}.
     * <p>
     * The returned {@code Future} is backed by the {@link #DEFAULT_EXECUTOR}.
     *
     * @param futures   An iterable of futures.
     * @param predicate A predicate that tests successful future results.
     * @param <T>       Result type of the futures.
     * @return A Future of an {@link Option} of the first result of the given {@code futures} that satisfies the given {@code predicate}.
     * @throws NullPointerException if one of the arguments is null
     */
    static <T> Future<Option<T>> find(Iterable<? extends Future<? extends T>> futures, Predicate<? super T> predicate) {
        return find(DEFAULT_EXECUTOR, futures, predicate);
    }

    /**
     * Returns a {@code Future} that eventually succeeds with the first result of the given {@code Future}s which
     * matches the given {@code predicate}. If no result matches, the {@code Future} will contain {@link Option.None}.
     * <p>
     * The returned {@code Future} is backed by the given {@link Executor}.
     *
     * @param executor  An {@link Executor}.
     * @param futures   An iterable of futures.
     * @param predicate A predicate that tests successful future results.
     * @param <T>       Result type of the futures.
     * @return A Future of an {@link Option} of the first result of the given {@code futures} that satisfies the given {@code predicate}.
     * @throws NullPointerException if one of the arguments is null
     */
    static <T> Future<Option<T>> find(Executor executor, Iterable<? extends Future<? extends T>> futures, Predicate<? super T> predicate) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(futures, "futures is null");
        Objects.requireNonNull(predicate, "predicate is null");
        final List<Future<? extends T>> list = List.ofAll(futures);
        if (list.isEmpty()) {
            return successful(executor, Option.none());
        } else {
            return join(executor, tryComplete -> {
                final AtomicBoolean completed = new AtomicBoolean(false);
                final AtomicInteger count = new AtomicInteger(list.length());
                list.forEach(future -> future.onComplete(result -> {
                    synchronized (count) {
                        // if the future is already completed we already found our result and there is nothing more to do.
                        if (!completed.get()) {
                            // when there are no more results we return a None
                            final boolean wasLast = count.decrementAndGet() == 0;
                            // when result is a Failure or predicate is false then we check in onFailure for finish
                            result.filter(predicate)
                                    .onSuccess(value -> completed.set(tryComplete.test(Try.success(Option.some(value)))))
                                    .onFailure(ignored -> {
                                        if (wasLast) {
                                            completed.set(tryComplete.test(Try.success(Option.none())));
                                        }
                                    });
                        }
                    }
                }));
            });
        }
    }

    /**
     * Returns a new {@code Future} that will contain the result of the first of the given futures that is completed,
     * backed by the {@link #DEFAULT_EXECUTOR}.
     *
     * @param futures An iterable of futures.
     * @param <T>     The result type.
     * @return A new {@code Future}.
     * @throws NullPointerException if futures is null
     */
    static <T> Future<T> firstCompletedOf(Iterable<? extends Future<? extends T>> futures) {
        return firstCompletedOf(DEFAULT_EXECUTOR, futures);
    }

    /**
     * Returns a new {@code Future} that will contain the result of the first of the given futures that is completed,
     * backed by the given {@link Executor}.
     *
     * @param executor An {@link Executor}.
     * @param futures  An iterable of futures.
     * @param <T>      The result type.
     * @return A new {@code Future}.
     * @throws NullPointerException if executor or futures is null
     */
    static <T> Future<T> firstCompletedOf(Executor executor, Iterable<? extends Future<? extends T>> futures) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(futures, "futures is null");
        return join(executor, tryComplete -> futures.forEach(future -> future.onComplete(tryComplete::test)));
    }

    /**
     * Returns a Future which contains the result of the fold of the given future values. If any future or the fold
     * fail, the result is a failure.
     * <p>
     * The resulting {@code Future} is backed by the {@link #DEFAULT_EXECUTOR}.
     *
     * @param futures An iterable of futures.
     * @param zero    The zero element of the fold.
     * @param f       The fold operation.
     * @param <T>     The result type of the given {@code Futures}.
     * @param <U>     The fold result type.
     * @return A new {@code Future} that will contain the fold result.
     * @throws NullPointerException if futures or f is null.
     */
    static <T, U> Future<U> fold(Iterable<? extends Future<? extends T>> futures, U zero, BiFunction<? super U, ? super T, ? extends U> f) {
        return fold(DEFAULT_EXECUTOR, futures, zero, f);
    }

    /**
     * Returns a Future which contains the result of the fold of the given future values. If any future or the fold
     * fail, the result is a failure.
     * <p>
     * The resulting {@code Future} is backed by the given {@link Executor}.
     *
     * @param executor An {@link Executor}.
     * @param futures  An iterable of futures.
     * @param zero     The zero element of the fold.
     * @param f        The fold operation.
     * @param <T>      The result type of the given {@code Futures}.
     * @param <U>      The fold result type.
     * @return A new {@code Future} that will contain the fold result.
     * @throws NullPointerException if executor, futures or f is null.
     */
    static <T, U> Future<U> fold(Executor executor, Iterable<? extends Future<? extends T>> futures, U zero, BiFunction<? super U, ? super T, ? extends U> f) {
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
     * Creates a {@code Future} with the given java.util.concurrent.Future, backed by the {@link #DEFAULT_EXECUTOR}
     *
     * @param future A {@link java.util.concurrent.Future}
     * @param <T>    Result type of the Future
     * @return A new {@code Future} wrapping the result of the Java future
     * @throws NullPointerException if future is null
     */
    static <T> Future<T> fromJavaFuture(java.util.concurrent.Future<T> future) {
        Objects.requireNonNull(future, "future is null");
        return of(DEFAULT_EXECUTOR, future::get);
    }

    /**
     * Creates a {@code Future} with the given java.util.concurrent.Future, backed by given {@link Executor}
     *
     * @param executor An {@link Executor}.
     * @param future   A {@link java.util.concurrent.Future}.
     * @param <T>      Result type of the Future
     * @return A new {@code Future} wrapping the result of the Java future
     * @throws NullPointerException if executor or future is null
     */
    static <T> Future<T> fromJavaFuture(Executor executor, java.util.concurrent.Future<T> future) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(future, "future is null");
        return of(executor, future::get);
    }

    /**
     * Creates a {@code Future} with the given {@link java.util.concurrent.CompletableFuture}, backed by the {@link #DEFAULT_EXECUTOR}
     *
     * @param future A {@link java.util.concurrent.CompletableFuture}
     * @param <T>    Result type of the Future
     * @return A new {@code Future} wrapping the result of the {@link java.util.concurrent.CompletableFuture}
     * @throws NullPointerException if future is null
     */
    @GwtIncompatible
    static <T> Future<T> fromCompletableFuture(CompletableFuture<T> future) {
        return fromCompletableFuture(DEFAULT_EXECUTOR, future);
    }

    /**
     * Creates a {@code Future} with the given {@link java.util.concurrent.CompletableFuture}, backed by given {@link Executor}
     *
     * @param executor An {@link Executor}.
     * @param future   A {@link java.util.concurrent.CompletableFuture}.
     * @param <T>      Result type of the Future
     * @return A new {@code Future} wrapping the result of the {@link java.util.concurrent.CompletableFuture}
     * @throws NullPointerException if executor or future is null
     */
    @GwtIncompatible
    static <T> Future<T> fromCompletableFuture(Executor executor, CompletableFuture<T> future) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(future, "future is null");
        if (future.isDone() || future.isCompletedExceptionally() || future.isCancelled()) {
            return fromTry(Try.of(future::get).recoverWith(error -> Try.failure(error.getCause())));
        } else {
            return join(executor, tryComplete ->
                    future.handle((t, err) -> tryComplete.test((err == null) ? Try.success(t) : Try.failure(err)))
            );
        }
    }

    /**
     * Creates a {@code Future} from a {@link Try}, backed by the {@link #DEFAULT_EXECUTOR}.
     *
     * @param result The result.
     * @param <T>    The value type of a successful result.
     * @return A completed {@code Future} which contains either a {@code Success} or a {@code Failure}.
     * @throws NullPointerException if result is null
     */
    static <T> Future<T> fromTry(Try<? extends T> result) {
        return fromTry(DEFAULT_EXECUTOR, result);
    }

    /**
     * Creates a {@code Future} from a {@link Try}, backed by the given {@link Executor}.
     *
     * @param executor An {@link Executor}.
     * @param result   The result.
     * @param <T>      The value type of a successful result.
     * @return A completed {@code Future} which contains either a {@code Success} or a {@code Failure}.
     * @throws NullPointerException if executor or result is null
     */
    static <T> Future<T> fromTry(Executor executor, Try<? extends T> result) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(result, "result is null");
        return FutureImpl.of(executor, result);
    }

    /**
     * Narrows a widened {@code Future<? extends T>} to {@code Future<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param future A {@code Future}.
     * @param <T>    Component type of the {@code Future}.
     * @return the given {@code future} instance as narrowed type {@code Future<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> Future<T> narrow(Future<? extends T> future) {
        return (Future<T>) future;
    }

    /**
     * Starts an asynchronous computation, backed by the {@link #DEFAULT_EXECUTOR}.
     *
     * @param computation A computation.
     * @param <T>      Type of the computation result.
     * @return A new Future instance
     * @throws NullPointerException if computation is null.
     * @deprecated Will be removed. Use {@code Future.of(supplier::get)} instead of {@code Future.ofSupplier(supplier)}.
     */
    @Deprecated
    static <T> Future<T> ofSupplier(Supplier<? extends T> computation) {
        Objects.requireNonNull(computation, "computation is null");
        return of(DEFAULT_EXECUTOR, computation::get);
    }

    /**
     * Starts an asynchronous computation, backed by the given {@link Executor}.
     *
     * @param executor An executor service.
     * @param computation A computation.
     * @param <T>      Type of the computation result.
     * @return A new Future instance
     * @throws NullPointerException if one of executor or computation is null.
     * @deprecated Will be removed. Use {@code Future.of(executor, supplier::get)} instead of {@code Future.ofSupplier(executor, supplier)}.
     */
    @Deprecated
    static <T> Future<T> ofSupplier(Executor executor, Supplier<? extends T> computation) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(computation, "computation is null");
        return of(executor, computation::get);
    }

    /**
     * Starts an asynchronous computation, backed by the {@link #DEFAULT_EXECUTOR}.
     *
     * @param computation A computation
     * @param <T>      Type of the computation result.
     * @return A new Future instance
     * @throws NullPointerException if computation is null.
     * @deprecated Will be removed. Use {@code Future.of(callable::call)} instead of {@code Future.ofCallable(callable)}.
     */
    @Deprecated
    static <T> Future<T> ofCallable(Callable<? extends T> computation) {
        Objects.requireNonNull(computation, "computation is null");
        return of(DEFAULT_EXECUTOR, computation::call);
    }

    /**
     * Starts an asynchronous computation, backed by the given {@link Executor}.
     *
     * @param executor An executor service.
     * @param computation A computation.
     * @param <T>      Type of the computation result.
     * @return A new Future instance
     * @throws NullPointerException if one of executor or computation is null.
     * @deprecated Will be removed. Use {@code Future.of(executor, callable::call)} instead of {@code Future.ofCallable(executor, callable)}.
     */
    @Deprecated
    static <T> Future<T> ofCallable(Executor executor, Callable<? extends T> computation) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(computation, "computation is null");
        return of(executor, computation::call);
    }

    /**
     * Starts an asynchronous computation, backed by the {@link #DEFAULT_EXECUTOR}.
     *
     * @param computation A computation
     * @return A new Future instance
     * @throws NullPointerException if computation is null.
     * @deprecated Will be removed. Use {@code Future.of(runnable::run)} instead of {@code Future.runRunnable(runnable)}.
     */
    @Deprecated
    static Future<Void> runRunnable(Runnable computation) {
        Objects.requireNonNull(computation, "computation is null");
        return run(DEFAULT_EXECUTOR, computation::run);
    }

    /**
     * Starts an asynchronous computation, backed by the given {@link Executor}.
     *
     * @param executor An executor service.
     * @param computation A computation.
     * @return A new Future instance
     * @throws NullPointerException if one of executor or computation is null.
     * @deprecated Will be removed. Use {@code Future.of(executor, runnable::run)} instead of {@code Future.runRunnable(executor, runnable)}.
     */
    @Deprecated
    static Future<Void> runRunnable(Executor executor, Runnable computation) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(computation, "computation is null");
        return run(executor, computation::run);
    }

    /**
     * Starts an asynchronous computation, backed by the {@link #DEFAULT_EXECUTOR}.
     *
     * @param computation A computation.
     * @param <T>         Type of the computation result.
     * @return A new Future instance.
     * @throws NullPointerException if computation is null.
     */
    static <T> Future<T> of(CheckedFunction0<? extends T> computation) {
        return of(DEFAULT_EXECUTOR, computation);
    }

    /**
     * Starts an asynchronous computation, backed by the given {@link Executor}.
     *
     * @param executor    An {@link Executor}.
     * @param computation A computation.
     * @param <T>         Type of the computation result.
     * @return A new Future instance.
     * @throws NullPointerException if one of executor or computation is null.
     */
    static <T> Future<T> of(Executor executor, CheckedFunction0<? extends T> computation) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(computation, "computation is null");
        return FutureImpl.async(executor, tryComplete -> tryComplete.test(Try.of(computation)));
    }

    /**
     * Creates a (possibly blocking) Future that joins the results of the given {@code computation}
     * using a completion handler:
     *
     * <pre>{@code
     * CheckedConsumer<Predicate<Try<T>>> computation = tryComplete -> {
     *     // computation
     * };
     * }</pre>
     *
     * The {@code computation} is executed synchronously. It requires to complete the returned Future.
     * A common use-case is to hand over the {@code tryComplete} predicate to another {@code Future}
     * in order to prevent blocking:
     *
     * <pre>{@code
     * Future<String> greeting(Future<String> nameFuture) {
     *     return Future.join(tryComplete -> {
     *         nameFuture.onComplete(name -> tryComplete.test("Hi " + name));
     *     });
     * }}</pre>
     *
     * The computation receives a {@link Predicate}, named {@code tryComplete} by convention,
     * that takes a result of type {@code Try<T>} and returns a boolean that states whether the
     * Future was completed.
     * <p>
     * Future completion is an idempotent operation in the way that the first call of {@code tryComplete}
     * will return true, successive calls will return false.
     *
     * @param computation A computational task
     * @param <T>         Type of the result
     * @return a new {@code Future} instance
     * @deprecated Experimental API
     */
    @Deprecated
    static <T> Future<T> join(CheckedConsumer<Predicate<Try<? extends T>>> computation) {
        return join(DEFAULT_EXECUTOR, computation);
    }

    /**
     * Creates a (possibly blocking) Future that joins the results of the given {@code computation}
     * using a completion handler:
     *
     * <pre>{@code
     * CheckedConsumer<Predicate<Try<T>>> computation = tryComplete -> {
     *     // computation
     * };
     * }</pre>
     *
     * The {@code computation} is executed synchronously. It requires to complete the returned Future.
     * A common use-case is to hand over the {@code tryComplete} predicate to another {@code Future}
     * in order to prevent blocking:
     *
     * <pre>{@code
     * Future<String> greeting(Future<String> nameFuture) {
     *     return Future.join(tryComplete -> {
     *         nameFuture.onComplete(name -> tryComplete.test("Hi " + name));
     *     });
     * }}</pre>
     *
     * The computation receives a {@link Predicate}, named {@code tryComplete} by convention,
     * that takes a result of type {@code Try<T>} and returns a boolean that states whether the
     * Future was completed.
     * <p>
     * Future completion is an idempotent operation in the way that the first call of {@code tryComplete}
     * will return true, successive calls will return false.
     *
     * @param executor    An {@link Executor} that runs the given {@code computation}
     * @param computation A computational task
     * @param <T>         Type of the result
     * @return a new {@code Future} instance
     * @deprecated Experimental API
     */
    @Deprecated
    static <T> Future<T> join(Executor executor, CheckedConsumer<Predicate<Try<? extends T>>> computation) {
        return FutureImpl.sync(executor, computation);
    }

    /**
     * Returns a Future which contains the reduce result of the given future values. The zero is the result of the
     * first future that completes. If any future or the reduce operation fail, the result is a failure.
     * <p>
     * The resulting {@code Future} is backed by the {@link #DEFAULT_EXECUTOR}.
     *
     * @param futures An iterable of futures.
     * @param f       The reduce operation.
     * @param <T>     The result type of the given {@code Futures}.
     * @return A new {@code Future} that will contain the reduce result.
     * @throws NullPointerException if executor, futures or f is null.
     */
    static <T> Future<T> reduce(Iterable<? extends Future<? extends T>> futures, BiFunction<? super T, ? super T, ? extends T> f) {
        return reduce(DEFAULT_EXECUTOR, futures, f);
    }

    /**
     * Returns a Future which contains the reduce result of the given future values. The zero is the result of the
     * first future that completes. If any future or the reduce operation fail, the result is a failure.
     * <p>
     * The resulting {@code Future} is backed by the given {@link Executor}.
     *
     * @param executor An {@link Executor}.
     * @param futures  An iterable of futures.
     * @param f        The reduce operation.
     * @param <T>      The result type of the given {@code Futures}.
     * @return A new {@code Future} that will contain the reduce result.
     * @throws NullPointerException if executor, futures or f is null.
     */
    static <T> Future<T> reduce(Executor executor, Iterable<? extends Future<? extends T>> futures, BiFunction<? super T, ? super T, ? extends T> f) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(futures, "futures is null");
        Objects.requireNonNull(f, "f is null");
        if (!futures.iterator().hasNext()) {
            throw new NoSuchElementException("Future.reduce on empty futures");
        } else {
            return Future.<T> sequence(futures).map(seq -> seq.reduceLeft(f));
        }
    }

    /**
     * Runs an asynchronous computation, backed by the {@link #DEFAULT_EXECUTOR}.
     *
     * @param unit A unit of work.
     * @return A new Future instance which results in nothing.
     * @throws NullPointerException if unit is null.
     */
    static Future<Void> run(CheckedRunnable unit) {
        return run(DEFAULT_EXECUTOR, unit);
    }

    /**
     * Starts an asynchronous computation, backed by the given {@link Executor}.
     *
     * @param executor An {@link Executor}.
     * @param unit     A unit of work.
     * @return A new Future instance which results in nothing.
     * @throws NullPointerException if one of executor or unit is null.
     */
    static Future<Void> run(Executor executor, CheckedRunnable unit) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(unit, "unit is null");
        return of(executor, () -> {
            unit.run();
            return null;
        });
    }

    /**
     * Reduces many {@code Future}s into a single {@code Future} by transforming an
     * {@code Iterable<Future<? extends T>>} into a {@code Future<Seq<T>>}.
     * <p>
     * The resulting {@code Future} is backed by the {@link #DEFAULT_EXECUTOR}.
     *
     * <ul>
     * <li>
     * If all of the given Futures succeed, sequence() succeeds too:
     * <pre><code>// = Future(Success(Seq(1, 2)))
     * sequence(
     *     List.of(
     *         Future.of(() -&gt; 1),
     *         Future.of(() -&gt; 2)
     *     )
     * );</code></pre>
     * </li>
     * <li>
     * If a given Future fails, sequence() fails too:
     * <pre><code>// = Future(Failure(Error)))
     * sequence(
     *     List.of(
     *         Future.of(() -&gt; 1),
     *         Future.of(() -&gt; { throw new Error(); }
     *     )
     * );</code></pre>
     * </li>
     * </ul>
     *
     * @param futures An {@code Iterable} of {@code Future}s.
     * @param <T>     Result type of the futures.
     * @return A {@code Future} of a {@link Seq} of results.
     * @throws NullPointerException if futures is null.
     */
    static <T> Future<Seq<T>> sequence(Iterable<? extends Future<? extends T>> futures) {
        return sequence(DEFAULT_EXECUTOR, futures);
    }

    /**
     * Reduces many {@code Future}s into a single {@code Future} by transforming an
     * {@code Iterable<Future<? extends T>>} into a {@code Future<Seq<T>>}.
     * <p>
     * The resulting {@code Future} is backed by the given {@link Executor}.
     *
     * @param executor An {@link Executor}.
     * @param futures  An {@code Iterable} of {@code Future}s.
     * @param <T>      Result type of the futures.
     * @return A {@code Future} of a {@link Seq} of results.
     * @throws NullPointerException if executor or futures is null.
     */
    static <T> Future<Seq<T>> sequence(Executor executor, Iterable<? extends Future<? extends T>> futures) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(futures, "futures is null");
        final Future<Seq<T>> zero = successful(executor, Stream.empty());
        final BiFunction<Future<Seq<T>>, Future<? extends T>, Future<Seq<T>>> f =
                (result, future) -> result.flatMap(seq -> future.map(seq::append));
        return Iterator.ofAll(futures).foldLeft(zero, f);
    }

    /**
     * Creates a succeeded {@code Future}, backed by the {@link #DEFAULT_EXECUTOR}.
     *
     * @param result The result.
     * @param <T>    The value type of a successful result.
     * @return A succeeded {@code Future}.
     */
    static <T> Future<T> successful(T result) {
        return successful(DEFAULT_EXECUTOR, result);
    }

    /**
     * Creates a succeeded {@code Future}, backed by the given {@link Executor}.
     *
     * @param executor An {@link Executor}.
     * @param result   The result.
     * @param <T>      The value type of a successful result.
     * @return A succeeded {@code Future}.
     * @throws NullPointerException if executor is null
     */
    static <T> Future<T> successful(Executor executor, T result) {
        Objects.requireNonNull(executor, "executor is null");
        return FutureImpl.of(executor, Try.success(result));
    }

    @Override
    @GwtIncompatible
    default CompletableFuture<T> toCompletableFuture() {
        final CompletableFuture<T> future = new CompletableFuture<>();
        onSuccess(future::complete);
        onFailure(future::completeExceptionally);
        return future;
    }

    /**
     * Maps the values of an iterable in parallel to a sequence of mapped values into a single {@code Future} by
     * transforming an {@code Iterable<? extends T>} into a {@code Future<Seq<U>>}.
     * <p>
     * The resulting {@code Future} is backed by the {@link #DEFAULT_EXECUTOR}.
     *
     * @param values An {@code Iterable} of {@code Future}s.
     * @param mapper A mapper of values to Futures
     * @param <T>    The type of the given values.
     * @param <U>    The mapped value type.
     * @return A {@code Future} of a {@link Seq} of results.
     * @throws NullPointerException if values or f is null.
     */
    static <T, U> Future<Seq<U>> traverse(Iterable<? extends T> values, Function<? super T, ? extends Future<? extends U>> mapper) {
        return traverse(DEFAULT_EXECUTOR, values, mapper);
    }

    /**
     * Maps the values of an iterable in parallel to a sequence of mapped values into a single {@code Future} by
     * transforming an {@code Iterable<? extends T>} into a {@code Future<Seq<U>>}.
     * <p>
     * The resulting {@code Future} is backed by the given {@link Executor}.
     *
     * @param executor An {@link Executor}.
     * @param values   An {@code Iterable} of values.
     * @param mapper   A mapper of values to Futures
     * @param <T>      The type of the given values.
     * @param <U>      The mapped value type.
     * @return A {@code Future} of a {@link Seq} of results.
     * @throws NullPointerException if executor, values or f is null.
     */
    static <T, U> Future<Seq<U>> traverse(Executor executor, Iterable<? extends T> values, Function<? super T, ? extends Future<? extends U>> mapper) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(values, "values is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return sequence(executor, Iterator.ofAll(values).map(mapper));
    }

    // -- non-static Future API

    /**
     * Support for chaining of callbacks that are guaranteed to be executed in a specific order.
     * <p>
     * An exception, which occurs when performing the given {@code action}, is not propagated to the outside.
     * In other words, subsequent actions are performed based on the value of the original Future.
     * <p>
     * Example:
     * <pre><code>
     * // prints Success(1)
     * Future.of(() -&gt; 1)
     *       .andThen(t -&gt; { throw new Error(""); })
     *       .andThen(System.out::println);
     * </code></pre>
     *
     * @param action A side-effecting action.
     * @return A new Future that contains this result and which is completed after the given action was performed.
     * @throws NullPointerException if action is null
     */
    default Future<T> andThen(Consumer<? super Try<T>> action) {
        Objects.requireNonNull(action, "action is null");
        return join(executor(), tryComplete ->
                onComplete(t -> {
                    Try.run(() -> action.accept(t));
                    tryComplete.test(t);
                })
        );
    }

    /**
     * Blocks the current Thread until this Future completed or returns immediately if this Future is already completed.
     * <p>
     * In the case the current thread was interrupted while waiting, a failed {@code Future} is returned containing
     * the corresponding {@link InterruptedException}.
     *
     * @return this {@code Future} instance
     */
    Future<T> await();

    /**
     * Blocks the current Thread until this Future completed or returns immediately if this Future is already completed.
     * <p>
     * In the case the current thread was interrupted while waiting, a failed {@code Future} is returned containing
     * the corresponding {@link InterruptedException}.
     * <p>
     * If the deadline wasn't met, a failed {@code Future} is returned containing a {@link TimeoutException}.
     *
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     * @return this {@code Future} instance
     * @throws IllegalArgumentException if {@code timeout} is negative
     * @throws NullPointerException     if {@code unit} is null
     */
    Future<T> await(long timeout, TimeUnit unit);

    /**
     * Cancels the Future. A running thread is interrupted.
     * <p>
     * If the Future was successfully cancelled, the result is a {@code Failure(CancellationException)}.
     *
     * @return {@code false}, if this {@code Future} is already completed or could not be cancelled, otherwise {@code true}.
     * @throws SecurityException if the current thread cannot modify the Future's thread
     * @see Future#isCancelled()
     */
    default boolean cancel() {
        return cancel(true);
    }

    /**
     * Cancels the Future. A pending Future may be interrupted, depending on the underlying {@code Executor}.
     * <p>
     * If the Future was successfully cancelled, the result is a {@code Failure(CancellationException)}.
     *
     * @param mayInterruptIfRunning {@code true} if a running thread should be interrupted, otherwise a running thread
     *                              is allowed to complete its computation.
     * @return {@code false}, if this {@code Future} is already completed or could not be cancelled, otherwise {@code true}.
     * @throws SecurityException if the current thread cannot modify the Future's thread
     * @see Future#isCancelled()
     * @see java.util.concurrent.Future#cancel(boolean)
     */
    boolean cancel(boolean mayInterruptIfRunning);

    /**
     * Collects value that is in the domain of the given {@code partialFunction} by mapping the value to type {@code R}.
     *
     * <pre>{@code
     * partialFunction.isDefinedAt(value)
     * }</pre>
     *
     * If the element makes it through that filter, the mapped instance is wrapped in {@code Future}
     *
     * <pre>{@code
     * R newValue = partialFunction.apply(value)
     * }</pre>
     *
     * @param partialFunction A function that is not necessarily defined on value of this future.
     * @param <R>             The new value type
     * @return A new {@code Future} instance containing value of type {@code R}
     * @throws NullPointerException if {@code partialFunction} is null
     */
    default <R> Future<R> collect(PartialFunction<? super T, ? extends R> partialFunction) {
        Objects.requireNonNull(partialFunction, "partialFunction is null");
        return join(executor(), tryComplete ->
            onComplete(result -> tryComplete.test(result.collect(partialFunction)))
        );
    }

    /**
     * Returns the {@link Executor} used by this {@code Future}.
     *
     * @return The underlying {@code Executor}.
     */
    @SuppressWarnings("deprecation")
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
     * A projection that inverses the result of this Future.
     * <p>
     * If this Future succeeds, the failed projection returns a failure containing a {@code NoSuchElementException}.
     * <p>
     * If this Future fails, the failed projection returns a success containing the exception.
     *
     * @return A new Future which contains an exception at a point of time.
     */
    default Future<Throwable> failed() {
        return join(executor(), tryComplete ->
            onComplete(result -> {
                if (result.isFailure()) {
                    tryComplete.test(Try.success(result.getCause()));
                } else {
                    tryComplete.test(Try.failure(new NoSuchElementException("Future.failed completed without a throwable")));
                }
            })
        );
    }

    /**
     * Returns a Future that returns the result of this Future, if it is a success. If the value of this Future is a
     * failure, the result of {@code that} Future is returned, if that is a success. If both Futures fail, the failure
     * of this Future is returned.
     * <p>
     * Example:
     * <pre><code>
     * Future&lt;Integer&gt; future = Future.of(() -&gt; { throw new Error(); });
     * Future&lt;Integer&gt; that = Future.of(() -&gt; 1);
     * Future&lt;Integer&gt; result = future.fallbackTo(that);
     *
     * // prints Some(1)
     * result.onComplete(System.out::println);
     * </code></pre>
     *
     * @param that A fallback future computation
     * @return A new Future
     * @throws NullPointerException if that is null
     */
    default Future<T> fallbackTo(Future<? extends T> that) {
        Objects.requireNonNull(that, "that is null");
        return join(executor(), tryComplete ->
            onComplete(t -> {
                if (t.isSuccess()) {
                    tryComplete.test(t);
                } else {
                    that.onComplete(alt -> tryComplete.test(alt.isSuccess() ? alt : t));
                }
            })
        );
    }

    /**
     * Shortcut for {@code filterTry(predicate::test}.
     *
     * @param predicate A predicate
     * @return A new {@code Future}
     * @throws NullPointerException if {@code predicate} is null
     */
    default Future<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filterTry(predicate::test);
    }

    /**
     * Filters the result of this {@code Future} by calling {@link Try#filterTry(CheckedPredicate)}.
     *
     * @param predicate A checked predicate
     * @return A new {@code Future}
     * @throws NullPointerException if {@code predicate} is null
     */
    default Future<T> filterTry(CheckedPredicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return join(executor(), tryComplete -> onComplete(result -> tryComplete.test(result.filterTry(predicate))));
    }

    /**
     * Returns the underlying exception of this Future, syntactic sugar for {@code future.getValue().map(Try::getCause)}.
     *
     * @return None if the Future is not completed yet. Returns Some(Throwable) if the Future was completed with a failure.
     * @throws UnsupportedOperationException if the Future was successfully completed with a value
     */
    default Option<Throwable> getCause() {
        return getValue().map(Try::getCause);
    }

    /**
     * Returns the value of the Future.
     *
     * @return {@code None}, if the Future is not yet completed or was cancelled, otherwise {@code Some(Try)}.
     */
    Option<Try<T>> getValue();

    /**
     * Checks if this Future is cancelled, i.e. the thread was forced to stop before completion.
     *
     * @return true, if the computation was cancelled, false otherwise
     */
    boolean isCancelled();

    /**
     * Checks if this Future is completed, i.e. has a value.
     *
     * @return true, if the computation successfully finished, failed or was cancelled, false otherwise.
     */
    boolean isCompleted();

    /**
     * Checks if this Future completed with a success.
     *
     * @return true, if this Future completed and is a Success, false otherwise.
     */
    default boolean isSuccess() {
        return isCompleted() && getValue().get().isSuccess();
    }

    /**
     * Checks if this Future completed with a failure.
     *
     * @return true, if this Future completed and is a Failure, false otherwise.
     */
    default boolean isFailure() {
        return isCompleted() && getValue().get().isFailure();
    }

    /**
     * Performs the action once the Future is complete.
     *
     * @param action An action to be performed when this future is complete.
     * @return this Future
     * @throws NullPointerException if {@code action} is null.
     */
    Future<T> onComplete(Consumer<? super Try<T>> action);

    /**
     * Performs the action once the Future is complete and the result is a {@link Try.Failure}. Please note that the
     * future is also a failure when it was cancelled.
     *
     * @param action An action to be performed when this future failed.
     * @return this Future
     * @throws NullPointerException if {@code action} is null.
     */
    default Future<T> onFailure(Consumer<? super Throwable> action) {
        Objects.requireNonNull(action, "action is null");
        return onComplete(result -> result.onFailure(action));
    }

    /**
     * Performs the action once the Future is complete and the result is a {@link Try.Success}.
     *
     * @param action An action to be performed when this future succeeded.
     * @return this Future
     * @throws NullPointerException if {@code action} is null.
     */
    default Future<T> onSuccess(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        return onComplete(result -> result.onSuccess(action));
    }

    /**
     * Handles a failure of this Future by returning another result.
     * <p>
     * Example:
     * <pre><code>
     * // = "oh!"
     * Future.of(() -&gt; new Error("oh!")).recover(Throwable::getMessage);
     * </code></pre>
     *
     * @param f A function which takes the exception of a failure and returns a new value.
     * @return A new Future.
     * @throws NullPointerException if {@code f} is null
     */
    default Future<T> recover(Function<? super Throwable, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        return transformValue(t -> t.recover(f));
    }

    /**
     * Handles a failure of this Future by returning the result of another Future.
     * <p>
     * Example:
     * <pre><code>
     * // = "oh!"
     * Future.of(() -&gt; { throw new Error("oh!"); }).recoverWith(x -&gt; Future.of(x::getMessage));
     * </code></pre>
     *
     * @param f A function which takes the exception of a failure and returns a new future.
     * @return A new Future.
     * @throws NullPointerException if {@code f} is null
     */
    default Future<T> recoverWith(Function<? super Throwable, ? extends Future<? extends T>> f) {
        Objects.requireNonNull(f, "f is null");
        return join(executor(), tryComplete ->
            onComplete(t -> {
                if (t.isFailure()) {
                    Try.run(() -> f.apply(t.getCause()).onComplete(tryComplete::test))
                            .onFailure(x -> tryComplete.test(Try.failure(x)));
                } else {
                    tryComplete.test(t);
                }
            })
        );
    }

    /**
     * Transforms this {@code Future}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(Function<? super Future<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    /**
     * Transforms the value of this {@code Future}, whether it is a success or a failure.
     *
     * @param f   A transformation
     * @param <U> Generic type of transformation {@code Try} result
     * @return A {@code Future} of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> Future<U> transformValue(Function<? super Try<T>, ? extends Try<? extends U>> f) {
        Objects.requireNonNull(f, "f is null");
        return join(executor(), tryComplete ->
            onComplete(t -> Try.run(() -> tryComplete.test(f.apply(t)))
                    .onFailure(x -> tryComplete.test(Try.failure(x)))
            )
        );
    }

    /**
     * Returns a tuple of this and that Future result.
     * <p>
     * If this Future failed the result contains this failure. Otherwise the result contains that failure or
     * a tuple of both successful Future results.
     *
     * @param that Another Future
     * @param <U>  Result type of {@code that}
     * @return A new Future that returns both Future results.
     * @throws NullPointerException if {@code that} is null
     */
    default <U> Future<Tuple2<T, U>> zip(Future<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        return zipWith(that, Tuple::of);
    }

    /**
     * Returns a this and that Future result combined using a given combinator function.
     * <p>
     * If this Future failed the result contains this failure. Otherwise the result contains that failure or
     * a combination of both successful Future results.
     *
     * @param that       Another Future
     * @param combinator The combinator function
     * @param <U>        Result type of {@code that}
     * @param <R>        Result type of {@code f}
     * @return A new Future that returns both Future results.
     * @throws NullPointerException if {@code that} is null
     */
    @SuppressWarnings("unchecked")
    default <U, R> Future<R> zipWith(Future<? extends U> that, BiFunction<? super T, ? super U, ? extends R> combinator) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(combinator, "combinator is null");
        return join(executor(), tryComplete ->
            onComplete(res1 -> {
                if (res1.isFailure()) {
                    tryComplete.test((Try.Failure<R>) res1);
                } else {
                    that.onComplete(res2 -> {
                        final Try<R> result = res1.flatMap(t -> res2.map(u -> combinator.apply(t, u)));
                        tryComplete.test(result);
                    });
                }
            })
        );
    }

    // -- Value & Monad implementation

    default <U> Future<U> flatMap(Function<? super T, ? extends Future<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return flatMapTry(mapper::apply);
    }

    default <U> Future<U> flatMapTry(CheckedFunction1<? super T, ? extends Future<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return join(executor(), tryComplete ->
            onComplete(result -> result.mapTry(mapper)
                    .onSuccess(future -> future.onComplete(tryComplete::test))
                    .onFailure(x -> tryComplete.test(Try.failure(x)))
            )
        );
    }

    /**
     * Performs the given {@code action} asynchronously hence this Future result becomes available.
     * The {@code action} is not performed, if the result is a failure.
     *
     * @param action A {@code Consumer}
     */
    @Override
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        onComplete(result -> result.forEach(action));
    }

    /**
     * Gets the value if the computation result is a {@code Success} or throws if it was a {@code Failure}.
     * Waits for the result if necessary by blocking the current thread.
     * <p>
     * <strong>IMPORTANT! If the computation result is a {@link Try.Failure}, the underlying {@code cause} of type {@link Throwable} is thrown.</strong>
     *
     * @return The value of this {@code Future}.
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
    default Iterator<T> iterator() {
        return isEmpty() ? Iterator.empty() : Iterator.of(get());
    }

    @Override
    default <U> Future<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return transformValue(t -> t.map(mapper));
    }

    default <U> Future<U> mapTry(CheckedFunction1<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return transformValue(t -> t.mapTry(mapper));
    }

    default Future<T> orElse(Future<? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        return join(executor(), tryComplete ->
            onComplete(result -> {
                if (result.isSuccess()) {
                    tryComplete.test(result);
                } else {
                    other.onComplete(tryComplete::test);
                }
            })
        );
    }

    default Future<T> orElse(Supplier<? extends Future<? extends T>> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return join(executor(), tryComplete ->
            onComplete(result -> {
                if (result.isSuccess()) {
                    tryComplete.test(result);
                } else {
                    supplier.get().onComplete(tryComplete::test);
                }
            })
        );
    }

    @Override
    default Future<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        onSuccess(action);
        return this;
    }

    @Override
    default String stringPrefix() {
        return "Future";
    }
}
