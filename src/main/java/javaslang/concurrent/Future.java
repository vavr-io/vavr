/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import javaslang.*;
import javaslang.collection.List;
import javaslang.control.*;
import javaslang.control.Try.CheckedSupplier;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Futures represent asynchronous computations that will result in a value at some unknown point in the future.
 * Methods on {@link Future} return new Futures that will perform their operations or call backs when the value is available.
 * <p>
 * See {@link Promise} if you want Futures that can be success by some other method.
 *
 * @param <T> The type of this Future's eventual value.
 * @since 2.0.0
 */
public class Future<T> {

    private final ConcurrentLinkedQueue<Tuple2<Executor, Consumer<Try<T>>>> callBacks = new ConcurrentLinkedQueue<>();
    private volatile Option<Try<T>> value = Option.none();


    /**
     * Create a new Future based on a {@link java.util.concurrent.CompletableFuture}.
     *
     * @param source A {@link java.util.concurrent.CompletableFuture} that will be wrapped up in this Future
     */
    public Future(CompletableFuture<? extends T> source) {
        source.whenComplete((s, f) -> {
            if (s != null)
                complete(new Success<>(s));
            else
                complete(new Failure<>(f));
        });
    }

    /**
     * Create a new Future who's value will be calculated asynchronously from the given {@link java.util.function.Supplier}
     *
     * @param source A {@link java.util.function.Supplier} that will asynchronously provide the final value.
     * @param ex     An {@link java.util.concurrent.Executor} that will be used to calculate the value of the supplier and will serve
     *               as the default Executor for all asynchronous methods on this Future.
     */
    public Future(CheckedSupplier<T> source, Executor ex) {
        ex.execute(() -> complete(Try.of(source)));
    }

    /**
     * Create a new Future who's value will be calculated asynchronously from the given {@link java.util.function.Supplier}
     *
     * @param source A {@link java.util.function.Supplier} that will asynchronously provide the final value.
     */
    public Future(CheckedSupplier<T> source) {
        this(source, ForkJoinPool.commonPool());
    }

	/**
	 * Should only be called from Promise.
	 */
    Future() {

    }

	/**
	 * Create a new Future who's value will be calculated asynchronously from the given {@link java.util.function.Supplier}
	 *
     * @param <T> value type
	 * @param source A {@link java.util.function.Supplier} that will asynchronously provide the final value.
	 * @param ex     An {@link java.util.concurrent.Executor} that will be used to calculate the value of the supplier and will serve
	 *               as the default Executor for all asynchronous methods on this Future.
     * @return a new Future
	 */
	public static <T> Future<T> of(CheckedSupplier<T> source, Executor ex) {
		return new Future<>(source, ex);
	}

	/**
	 * Create a new Future who's value will be calculated asynchronously from the given {@link java.util.function.Supplier}
	 *
     * @param <T> value type
	 * @param source A {@link java.util.function.Supplier} that will asynchronously provide the final value.
     * @return a new Future
	 */
	public static <T> Future<T> of(CheckedSupplier<T> source) {
		return new Future<>(source);
	}

    /**
     * A static form of {@link #Future(CompletableFuture)} and is identical.
     * <p>
     * See {@link #Future(CompletableFuture)}.
     *
     * @param source a source CompletableFuture
     * @param <T> The type of this Future's eventual value.
     * @return a new Future
     */
    public static <T> Future<T> of(CompletableFuture<? extends T> source) {
        return new Future<>(source);
    }

    /**
     * Create a new Future based on a {@link java.util.concurrent.Future}.
     * Not recommended, as a thread must be used poll for a response.
     * Only use when necessary to bridge legacy apis that use {@link java.util.concurrent.Future}.
     * If there is any way to use a {@link Promise} instead, do that.
     *
     * @param source A {@link java.util.concurrent.Future} to fulfil this Future.
     * @param <T>    The Type of both Futures.
     * @return A new Future that will poll and wait for the result of the given {@link java.util.concurrent.Future}.
     */
    @SuppressWarnings("unchecked")
    public static <T> Future<T> of(java.util.concurrent.Future<? extends T> source) {
	    if(source instanceof   CompletableFuture)
		    return new Future<>((CompletableFuture<T>) source);
	    else {
		    Promise<T> result = new Promise<>();

		    FutureConverter.instance().addFuture(source, result);

		    return result.future();
	    }
    }

	public static <T> Future<T> completed(Try<T> tri){
		Future<T> result = new Future<>();

		result.complete(tri);

		return result;
	}

    /**
     * Create a new Future that is already finished with the given value.
     *
     * @param obj A value to finish this new Future with.
     * @param <T> The type of obj.
     * @return A new pre-success Future.
     */
    public static <T> Future<T> success(T obj) {
        Future<T> result = new Future<>();

        result.complete(new Success<>(obj));

        return result;
    }

    /**
     * Create a new Future that is already finished with the given exception.
     *
     * @param t   An exception to hold in this Future.
     * @param <T> The type of the Future, always empty.
     * @return A new pre-failed Future.
     */
    public static <T> Future<T> failed(Throwable t) {
        Future<T> result = new Future<>();

        result.complete(new Failure<>(t));

        return result;
    }

    synchronized void complete(Try<T> result) {
        if (value.isEmpty()) {
            value = new Some<>(result);

            while (!callBacks.isEmpty()) {
                Tuple2<Executor, Consumer<Try<T>>> pair = callBacks.poll();
                pair._1.execute(() -> pair._2.accept(result));
            }

	        notifyAll();

            //These lines were using in testing to ensure that no callBacks were slipping through.
//            try {
//                Thread.sleep(100);
//            } catch (Throwable t){
//                throw new RuntimeException(t);
//            }
//
//            assert callBacks.isEmpty();
        }
	    else
	        throw new IllegalStateException("This Future has already been completed!");
        //TODO: else throw exception maybe?
    }

    /**
     * Creates a new Future by applying a function to the successful result of this Future.
     * If this Future fails with an exception the resulting Future will contain the same exception instead.
     *
     * @param func Function to apply to the result
     * @param ex   Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @param <R>  The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> map(Function<? super T, ? extends R> func, Executor ex) {
        Objects.requireNonNull(func, "func is null");
        Promise<R> result = new Promise<>();

        onCompleted(t -> result.success(func.apply(t)), result::failure, ex);

        return result.future();
    }

    /**
     * Creates a new Future by applying a function to the successful result of this Future.
     * If this Future fails with an exception the resulting Future will contain the same exception instead.
     *
     * @param func Function to apply to the result
     * @param <R>  The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> map(Function<? super T, ? extends R> func) {
        return map(func, ForkJoinPool.commonPool());
    }

    /**
     * Creates a new Future by applying a function to the successful result of this Future and returning the value of that Future.
     * If this Future fails with an exception the resulting Future will contain the same exception instead.
     *
     * @param func Function to apply to the result
     * @param ex   Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @param <R>  The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> flatMap(Function<? super T, Future<R>> func, Executor ex) {
        Objects.requireNonNull(func, "func is null");

        Promise<R> result = new Promise<>();

        onCompleted(t -> result.completeWith(func.apply(t)), result::failure, ex);

        return result.future();
    }

    /**
     * Creates a new Future by applying a function to the successful result of this Future and returning the value of that Future.
     * If this Future fails with an exception the resulting Future will contain the same exception instead.
     *
     * @param func Function to apply to the result
     * @param <R>  The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> flatMap(Function<? super T, Future<R>> func) {
        return flatMap(func, ForkJoinPool.commonPool());
    }

    /**
     * Creates a new Future by applying a function to the result of this Future.
     * Both input and output Try can contain success or failure, so it's possible to map success to failure or any other combination.
     *
     * @param func Function to apply to the result
     * @param ex   Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @param <R>  The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> mapTry(Function<Try<T>, Try<R>> func, Executor ex) {
        Objects.requireNonNull(func, "func is null");
        Promise<R> result = new Promise<>();

        onCompletedTry(t -> result.complete(func.apply(t)), ex);

        return result.future();
    }

    /**
     * Creates a new Future by applying a function to the result of this Future.
     * Both input and output Try can contain success or failure, so it's possible to map success to failure or any other combination.
     *
     * @param func Function to apply to the result
     * @param <R>  The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> mapTry(Function<Try<T>, Try<R>> func) {
        return mapTry(func, ForkJoinPool.commonPool());
    }

    /**
     * Creates a new Future by applying a function to the result of this Future.
     * Both input and output Try can contain success or failure, so it's possible to map success to failure or any other combination.
     *
     * @param func Function to apply to the result
     * @param ex   Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @param <R>  The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> flatMapTry(Function<Try<T>, Future<Try<R>>> func, Executor ex) {
        Objects.requireNonNull(func, "func is null");
        Promise<R> result = new Promise<>();

        onCompletedTry(t -> func.apply(t).onCompletedTry(maybe -> {
	        maybe.forEach(result::complete);
	        maybe.onFailure(result::failure);
        }, ex), ex);

        return result.future();
    }

    /**
     * Creates a new Future by applying a function to the result of this Future.
     * Both input and output Try can contain success or failure, so it's possible to map success to failure or any other combination.
     *
     * @param func Function to apply to the result
     * @param <R>  The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> flatMapTry(Function<Try<T>, Future<Try<R>>> func) {
        return flatMapTry(func, ForkJoinPool.commonPool());
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Will not be called if the future fails.
     * Identical to {@link #onSuccess(Consumer, Executor)}.
     *
     * @param func Function to be applied when the Future completes.
     * @param ex   Executor to use for this operation.
     */
    public void forEach(Consumer<? super T> func, Executor ex) {
        onSuccess(func, ex);
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Will not be called if the future fails.
     * Identical to {@link #onSuccess(Consumer)}.
     *
     * @param func Function to be applied when the Future completes.
     */
    public void forEach(Consumer<? super T> func) {
        onSuccess(func);
    }

    /**
     * Returns a failed projection of this future.
     * The failed projection is a future holding a value of type Throwable.
     * It is success with a value which is the throwable of the original future in case the original future is failed.
     * It is failed with a NoSuchElementException if the original future is success successfully.
     *
     * @param ex Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @return A failed projection of this future.
     */
    public Future<Throwable> failed(Executor ex) {
	    return mapTry(Try::failed, ex);
    }

    /**
     * Returns a failed projection of this future.
     * The failed projection is a future holding a value of type Throwable.
     * It is success with a value which is the throwable of the original future in case the original future is failed.
     * It is failed with a NoSuchElementException if the original future is success successfully.
     *
     * @return A failed projection of this future.
     */
    public Future<Throwable> failed() {
        return failed(ForkJoinPool.commonPool());
    }

    /**
     * Creates a new future that will handle any matching throwable that this future might contain.
     *
     * @param func A Function to handle any exception and return some value.
     * @param ex   Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @return a new Future
     */
    public Future<T> recover(Function<Throwable, ? extends T> func, Executor ex) {
        Objects.requireNonNull(func, "func is null");

	    return mapTry(tri -> tri.recover(func), ex);
    }

    /**
     * Creates a new future that will handle any matching throwable that this future might contain.
     *
     * @param func A Function to handle any exception and return some value.
     * @return a new Future
     */
    public Future<T> recover(Function<Throwable, ? extends T> func) {
        return recover(func, ForkJoinPool.commonPool());
    }

    /**
     * Creates a new future that will handle any matching throwable that this future might contain.
     *
     * @param func A Function to handle any exception and return some value.
     * @param ex   Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @return a new Future
     */
    public Future<T> recoverWith(Function<Throwable, Future<T>> func, Executor ex) {
        Objects.requireNonNull(func, "func is null");

        Promise<T> result = new Promise<>();

        //If it fails, run the func and complete result with that future's success or failure
	    onCompleted(result::success, e -> result.completeWith(func.apply(e)), ex);

        return result.future();
    }

    /**
     * Creates a new future that will handle any matching throwable that this future might contain.
     *
     * @param func A Function to handle any exception and return some value.
     * @return a new Future
     */
    public Future<T> recoverWith(Function<Throwable, Future<T>> func) {
        return recoverWith(func, ForkJoinPool.commonPool());
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Will not be called if the future fails.
     *
     * @param func Function to be applied when the Future completes.
     * @param ex   Executor to use for this operation.
     */
    public void onSuccess(Consumer<? super T> func, Executor ex) {
        Objects.requireNonNull(func, "func is null");
        onCompletedTry(r -> r.forEach(func), ex);
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Will not be called if the future fails.
     *
     * @param func Function to be applied when the Future completes.
     */
    public void onSuccess(Consumer<? super T> func) {
        onSuccess(func, ForkJoinPool.commonPool());
    }

    /**
     * Asynchronously processes the exception in the future once the value becomes available.
     * Will not be called if the future does not throw an exception.
     *
     * @param func Function to be applied if the Future fails.
     * @param ex   Executor to use for this operation.
     */
    public void onFailure(Consumer<Throwable> func, Executor ex) {
        Objects.requireNonNull(func, "func is null");
        onCompletedTry(r -> r.onFailure(func), ex);
    }

    /**
     * Asynchronously processes the exception in the future once the value becomes available.
     * Will not be called if the future does not throw an exception.
     *
     * @param func Function to be applied if the Future fails.
     */
    public void onFailure(Consumer<Throwable> func) {
        onFailure(func, ForkJoinPool.commonPool());
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Will be called for success or failure.
     *
     * @param func Function to be applied when the Future completes.
     * @param ex   Executor to use for this operation.
     */
    public void onCompletedTry(Consumer<Try<T>> func, Executor ex) {
        Objects.requireNonNull(func, "func is null");
        //TODO: Test the heck out this to make sure that the synchronization works properly.
        if (value.isEmpty()) {
            synchronized (callBacks) {
                if (value.isEmpty()) {
                    callBacks.add(Tuple.of(ex, func));
                    return;
                }
            }

        }

        value.forEach(func);
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Will be called for success or failure.
     *
     * @param func Function to be applied when the Future completes.
     */
    public void onCompletedTry(Consumer<Try<T>> func) {
        onCompletedTry(func, ForkJoinPool.commonPool());
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Either success or failure will be called.
     *
     * @param success Function to be applied if the Future completes successfully.
     * @param failure Function to be applied if the Future completes with an exception.
     * @param ex      Executor to use for this operation.
     */
    public void onCompleted(Consumer<? super T> success, Consumer<Throwable> failure, Executor ex) {
        Objects.requireNonNull(success, "success is null");
        Objects.requireNonNull(failure, "failure is null");
        onCompletedTry(r -> {
            r.forEach(success);
            r.onFailure(failure);
        }, ex);
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Either success or failure will be called.
     *
     * @param success Function to be applied if the Future completes successfully.
     * @param failure Function to be applied if the Future completes with an exception.
     */
    public void onCompleted(Consumer<? super T> success, Consumer<Throwable> failure) {
        onCompleted(success, failure, ForkJoinPool.commonPool());
    }

    /**
     * Creates a new Future by applying a predicate to the successful result of this Future.
     * If the value fails the test the resulting Future will contain a NoSuchElementException. Otherwise it will be the same value.
     * If this Future fails with an exception the resulting Future will contain the same exception instead.
     *
     * @param func Function to apply to the result
     * @param ex   Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public Future<T> filter(Predicate<? super T> func, Executor ex) {
        Objects.requireNonNull(func, "func is null");
	    return mapTry(in -> in.filter(func), ex);
    }

    /**
     * Creates a new Future by applying a predicate to the successful result of this Future.
     * If the value fails the test the resulting Future will contain a NoSuchElementException. Otherwise it will be the same value.
     * If this Future fails with an exception the resulting Future will contain the same exception instead.
     *
     * @param func Function to apply to the result
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public Future<T> filter(Predicate<? super T> func) {
        return filter(func, ForkJoinPool.commonPool());
    }

    /**
     * Applies the side-effecting function to the result of this future, and returns a new future with the result of this future.
     * This method allows one to enforce that the callbacks are executed in a specified order.
     * Note that if one of the chained andThen callbacks throws an exception, that exception is not propagated to the subsequent andThen callbacks.
     * Instead, the subsequent andThen callbacks are given the original value of this future.
     *
     * @param func Function to apply to the result
     * @param ex   Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public Future<T> andThen(Consumer<? super Try<T>> func, Executor ex) {
        Objects.requireNonNull(func, "func is null");

        Promise<T> result = new Promise<>();

        onCompletedTry(r -> {
            func.accept(r);
            result.complete(r);
        }, ex);

        return result.future();
    }

    /**
     * Applies the side-effecting function to the result of this future, and returns a new future with the result of this future.
     * This method allows one to enforce that the callbacks are executed in a specified order.
     * Note that if one of the chained andThen callbacks throws an exception, that exception is not propagated to the subsequent andThen callbacks.
     * Instead, the subsequent andThen callbacks are given the original value of this future.
     *
     * @param func Function to apply to the result
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public Future<T> andThen(Consumer<? super Try<T>> func) {
        return andThen(func, ForkJoinPool.commonPool());
    }

    /**
     * @return If this Future has finished.
     */
    public boolean isCompleted() {
        return value.isDefined();
    }

    /**
     * @return If this Future has finished AND contains a Success.
     */
    public boolean isSuccess() {
        return value.isDefined() && value.get().isSuccess();
    }

    /**
     * @return If this Future has finished AND contains a Failure.
     */
    public boolean isFailure() {
        return value.isDefined() && value.get().isFailure();
    }

    /**
     * @return The resulting value of this Future. Will be None until the Future finishes. After finishing, it will be a Some of either Success or Failure.
     */
    public Option<Try<T>> value() {
        return value;
    }

    /**
     * Block and wait for this Future to finish. Not recommended.
     *
     * @param timeOut Time to wait
     * @param unit    TimeUnit that timeOut is in.
     * @return this, but after blocking until success.
     * @throws InterruptedException if {@code unit.timedWait} gets interrupted
     * @throws TimeoutException if a timeout occurs
     */
    public Future<T> await(long timeOut, TimeUnit unit) throws InterruptedException, TimeoutException {
	    if(isCompleted())
		    return this;

        //TODO: Test more to make sure this works all the time.
        synchronized (this) {
            unit.timedWait(this, timeOut);
        }
        if (!isCompleted())
            throw new TimeoutException("Future did not finish within given time limit.");
        return this;
    }

    /**
     * Block and wait for this Future to finish. Not recommended.
     *
     * @param timeOut Time to wait
     * @param unit    TimeUnit that timeOut is in.
     * @return The result value of this Future after blocking.
     * @throws InterruptedException if {@code await} gets interrupted
     * @throws TimeoutException if a timeout occurs
     */
    public Try<T> ready(long timeOut, TimeUnit unit) throws InterruptedException, TimeoutException {
        await(timeOut, unit);
        return value().get();
    }

    /**
     * Returns a new Future which will take the result of #that if and only if this Future fails.
     *
     * @param that An alternative value.
     * @return a new Future
     */
    public Future<T> fallbackTo(Future<T> that) {
        return recoverWith(t -> that);
    }

    public static <T> Future<List<T>> sequence(java.lang.Iterable<Future<T>> source, Executor ex) {
        return List.ofAll(source)
		        .foldRight(Future.success(List.empty()), (next, accumulator) -> accumulator.flatMap(list -> next.map(list::prepend, ex), ex));
    }

    public static <T> Future<List<T>> sequence(java.lang.Iterable<Future<T>> source) {
        return sequence(source, ForkJoinPool.commonPool());
    }

    public static <T> Future<List<T>> traverse(java.lang.Iterable<T> source, Function1<T, Future<T>> func, Executor ex) {
        Objects.requireNonNull(func, "func is null");
        return sequence(List.ofAll(source).map(func), ex);
    }

    public static <T> Future<List<T>> traverse(java.lang.Iterable<T> source, Function1<T, Future<T>> func) {
        return traverse(source, func, ForkJoinPool.commonPool());
    }

    public static <T1> Future<Tuple1<T1>> sequence(Tuple1<Future<T1>> source) {
        return source._1.map(Tuple1::new);
    }

    public static <T1, T2> Future<Tuple2<T1, T2>> sequence(Tuple2<Future<T1>, Future<T2>> source) {
        return source._1.flatMap(v1 -> source._2.map(v2 -> new Tuple2<>(v1, v2)));
    }

    public static <T1, T2, T3> Future<Tuple3<T1, T2, T3>> sequence(Tuple3<Future<T1>, Future<T2>, Future<T3>> source) {
        return sequence(Tuple.of(sequence(Tuple.of(source._1, source._2)), source._3))
		        .map(nested -> Tuple.of(nested._1._1, nested._1._2, nested._2));

    }

    public static <T1, T2, T3, T4> Future<Tuple4<T1, T2, T3, T4>> sequence(Tuple4<Future<T1>, Future<T2>, Future<T3>, Future<T4>> source) {
        return sequence(Tuple.of(sequence(Tuple.of(source._1, source._2, source._3)), source._4))
		        .map(nested -> Tuple.of(nested._1._1, nested._1._2, nested._1._3, nested._2));

    }

    public static <T1, T2, T3, T4, T5> Future<Tuple5<T1, T2, T3, T4, T5>> sequence(Tuple5<Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>> source) {
        return sequence(Tuple.of(sequence(Tuple.of(source._1, source._2, source._3, source._4)), source._5))
		        .map(nested -> Tuple.of(nested._1._1, nested._1._2, nested._1._3, nested._1._4, nested._2));

    }

    public static <T1, T2, T3, T4, T5, T6> Future<Tuple6<T1, T2, T3, T4, T5, T6>> sequence(Tuple6<Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>> source) {
        return sequence(Tuple.of(sequence(Tuple.of(source._1, source._2, source._3, source._4, source._5)), source._6))
		        .map(nested -> Tuple.of(nested._1._1, nested._1._2, nested._1._3, nested._1._4, nested._1._5, nested._2));

    }

    public static <T1, T2, T3, T4, T5, T6, T7> Future<Tuple7<T1, T2, T3, T4, T5, T6, T7>> sequence(Tuple7<Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, Future<T7>> source) {
        return sequence(Tuple.of(sequence(Tuple.of(source._1, source._2, source._3, source._4, source._5, source._6)), source._7))
		        .map(nested -> Tuple.of(nested._1._1, nested._1._2, nested._1._3, nested._1._4, nested._1._5, nested._1._6, nested._2));
    }


    public static <T1, T2, T3, T4, T5, T6, T7, T8> Future<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> sequence(Tuple8<Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, Future<T7>, Future<T8>> source) {
        return sequence(Tuple.of(sequence(Tuple.of(source._1, source._2, source._3, source._4, source._5, source._6, source._7)), source._8))
		        .map(nested -> Tuple.of(nested._1._1, nested._1._2, nested._1._3, nested._1._4, nested._1._5, nested._1._6, nested._1._7, nested._2));
    }

    private static class FutureConverter {

        protected static final Lazy<FutureConverter> instance = Lazy.of(FutureConverter::new);

        private final Executor ex = Executors.newSingleThreadExecutor();

        protected static FutureConverter instance() {
            return instance.get();
        }

        protected <T> void addFuture(java.util.concurrent.Future<? extends T> in, Promise<T> out) {
            ex.execute(() -> {
                if (in.isDone()) out.complete(Try.of(in::get));
                else addFuture(in, out);
            });
        }

    }


}
