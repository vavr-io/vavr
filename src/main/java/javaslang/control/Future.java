/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.*;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javaslang.collection.List;
import javaslang.control.Try.CheckedSupplier;

/**
 * Futures represent asynchronous computations that will result in a value at some unknown point in the future.
 * Methods on {@link javaslang.control.Future} return new Futures that will perform their operations or call backs when the value is available.
 *
 * @see {@link javaslang.control.Promise} if you want Futures that can be completed by some other method.
 *
 * @param <T> The type of this Future's eventual value.
 * @since 1.3.0
 * @author LordBlackhole
 */
public class Future<T> {

    private final Executor ex;
    private final CompletableFuture<T> inner;
    private Option<Try<T>> value = Option.none();


    /**
     * Create a new Future based on a {@link java.util.concurrent.CompletableFuture}.
     * @param source A {@link java.util.concurrent.CompletableFuture} that will be wrapped up in this Future
     * @param ex An {@link java.util.concurrent.Executor} that will be used as the default Executor for all asynchronous methods on this Future.
     */
    public Future(CompletableFuture<T> source, Executor ex){
        this.inner = source;
        this.ex = ex;

        onCompletedTry(r -> value = new Some<>(r));
    }

    /**
     * Create a new Future based on a {@link java.util.concurrent.CompletableFuture}.
     * @param source A {@link java.util.concurrent.CompletableFuture} that will be wrapped up in this Future
     */
    public Future(CompletableFuture<T> source){
        this(source, ForkJoinPool.commonPool());
    }

    /**
     * Create a new Future who's value will be calculated asynchronously from the given {@link java.util.function.Supplier}
     * @param source A {@link java.util.function.Supplier} that will asynchronously provide the final value.
     * @param ex An {@link java.util.concurrent.Executor} that will be used to calculate the value of the supplier and will serve
     *           as the default Executor for all asynchronous methods on this Future.
     */
    public Future(CheckedSupplier<T> source, Executor ex){
        this(CompletableFuture.supplyAsync(() -> {
            try {
                return source.get();
            } catch (RuntimeException | Error e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }, ex));

    }

    /**
     * Create a new Future who's value will be calculated asynchronously from the given {@link java.util.function.Supplier}
     * @param source A {@link java.util.function.Supplier} that will asynchronously provide the final value.
     */
    public Future(CheckedSupplier<T> source){
        this(source, ForkJoinPool.commonPool());
    }

    /**
     * A static form of {@link javaslang.control.Future #new(CompletableFuture<T> source, Executor ex} and is identical.
     * @see {@link javaslang.control.Future #new(CompletableFuture<T> source, Executor ex}
     */
    public static <T> Future<T> of(CompletableFuture<T> source, Executor ex){
        return new Future<>(source, ex);
    }

    /**
     * A static form of {@link javaslang.control.Future #new(CompletableFuture<T> source} and is identical.
     * @see {@link javaslang.control.Future #new(CompletableFuture<T> source}
     */
    public static <T> Future<T> of(CompletableFuture<T> source){
        return new Future<>(source);
    }

    /**
     * Create a new Future based on a {@link java.util.concurrent.Future}.
     * Not recommended, as a thread must be used poll for a response.
     * Only use when necessary to bridge legacy apis that use {@link java.util.concurrent.Future}.
     * If there is any way to use a {@link javaslang.control.Promise} instead, do that.
     * @param source A {@link java.util.concurrent.Future} to fulfil this Future.
     * @param <T> The Type of both Futures.
     * @return A new Future that will poll and wait for the result of the given {@link java.util.concurrent.Future}.
     */
    public static <T> Future<T> of(java.util.concurrent.Future<T> source){
        Promise<T> result = new Promise<>();

        FutureConverter.instance().addFuture(source, result);

        return result.future();
    }

    /**
     * Create a new Future that is already finished with the given value.
     * @param obj A value to finish this new Future with.
     * @param <T> The type of obj.
     * @return A new pre-completed Future.
     */
    public static <T> Future<T> completed(T obj){
        return new Future<>(CompletableFuture.completedFuture(obj));
    }

    /**
     * Create a new Future that is already finished with the given exception.
     * @param t An exception to hold in this Future.
     * @param <T> The type of the Future, always empty.
     * @return A new pre-failed Future.
     */
    public static <T> Future<T> failed(Throwable t){
        CompletableFuture<T> fail = new CompletableFuture<>();
        fail.completeExceptionally(t);
        return of(fail);
    }

    /**
     * Creates a new Future by applying a function to the successful result of this Future.
     * If this Future fails with an exception the resulting Future will contain the same exception instead.
     * @param func Function to apply to the result
     * @param ex Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @param <R> The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> map(Function1<T, R> func, Executor ex){
        Objects.requireNonNull(func, "func is null");
        Promise<R> result = new Promise<>();

        onCompleted(t -> result.success(func.apply(t)), result::failure, ex);

        return result.future();
    }

    /**
     * Creates a new Future by applying a function to the successful result of this Future.
     * If this Future fails with an exception the resulting Future will contain the same exception instead.
     * @param func Function to apply to the result
     * @param <R> The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> map(Function1<T, R> func){
        return map(func, ex);
    }

    /**
     * Creates a new Future by applying a function to the successful result of this Future and returning the value of that Future.
     * If this Future fails with an exception the resulting Future will contain the same exception instead.
     * @param func Function to apply to the result
     * @param ex Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @param <R> The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> flatMap(Function1<T, Future<R>> func, Executor ex){
        Objects.requireNonNull(func, "func is null");

        Promise<R> result = new Promise<>();

        onCompleted(t -> result.completeWith(func.apply(t)), result::failure, ex);

        return result.future();
    }

    /**
     * Creates a new Future by applying a function to the successful result of this Future and returning the value of that Future.
     * If this Future fails with an exception the resulting Future will contain the same exception instead.
     * @param func Function to apply to the result
     * @param <R> The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> flatMap(Function1<T, Future<R>> func){
        return flatMap(func, ex);
    }

    /**
     * Creates a new Future by applying a function to the result of this Future.
     * Both input and output Try can contain success or failure, so it's possible to map success to failure or any other combination.
     * @param func Function to apply to the result
     * @param ex Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @param <R> The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> mapTry(Function1<Try<T>, Try<R>> func, Executor ex){
        Objects.requireNonNull(func, "func is null");
        Promise<R> result = new Promise<>();

        onCompletedTry(t -> result.complete(func.apply(t)), ex);

        return result.future();
    }

    /**
     * Creates a new Future by applying a function to the result of this Future.
     * Both input and output Try can contain success or failure, so it's possible to map success to failure or any other combination.
     * @param func Function to apply to the result
     * @param <R> The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> mapTry(Function1<Try<T>, Try<R>> func){
        return mapTry(func, ex);
    }

    /**
     * Creates a new Future by applying a function to the result of this Future.
     * Both input and output Try can contain success or failure, so it's possible to map success to failure or any other combination.
     * @param func Function to apply to the result
     * @param ex Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @param <R> The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> flatMapTry(Function1<Try<T>, Future<Try<R>>> func, Executor ex){
        Objects.requireNonNull(func, "func is null");
        Promise<R> result = new Promise<>();

        onCompletedTry(t -> {
            func.apply(t).onCompletedTry(maybe -> {
                maybe.forEach(result::complete);
                maybe.onFailure(result::failure);
            }, ex);
        }, ex);

        return result.future();
    }

    /**
     * Creates a new Future by applying a function to the result of this Future.
     * Both input and output Try can contain success or failure, so it's possible to map success to failure or any other combination.
     * @param func Function to apply to the result
     * @param <R> The result type of the Function
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public <R> Future<R> flatMapTry(Function1<Try<T>, Future<Try<R>>> func){
        return flatMapTry(func, ex);
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Will not be called if the future fails.
     * Identical to {@link javaslang.control.Future #onSuccess(Consumer<T> func, Executor ex)}.
     * @param func Function to be applied when the Future completes.
     * @param ex Executor to use for this operation.
     */
    public void forEach(Consumer<T> func, Executor ex){
        onSuccess(func, ex);
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Will not be called if the future fails.
     * Identical to {@link javaslang.control.Future #onSuccess(Consumer<T> func)}.
     * @param func Function to be applied when the Future completes.
     */
    public void forEach(Consumer<T> func){
        onSuccess(func);
    }

    /**
     * Returns a failed projection of this future.
     * The failed projection is a future holding a value of type Throwable.
     * It is completed with a value which is the throwable of the original future in case the original future is failed.
     * It is failed with a NoSuchElementException if the original future is completed successfully.
     * @param ex Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @return A failed projection of this future.
     */
    public Future<Throwable> failed(Executor ex){
        Promise<Throwable> result = new Promise<>();

        onCompleted(t -> result.failure(new NoSuchElementException("Future was successful.")), result::success, ex);

        return result.future();
    }

    /**
     * Returns a failed projection of this future.
     * The failed projection is a future holding a value of type Throwable.
     * It is completed with a value which is the throwable of the original future in case the original future is failed.
     * It is failed with a NoSuchElementException if the original future is completed successfully.
     * @return A failed projection of this future.
     */
    public Future<Throwable> failed(){
        return failed(ex);
    }

    /**
     * Creates a new future that will handle any matching throwable that this future might contain.
     * @param func A Function to handle any exception and return some value.
     * @param ex Executor to use for this operation. Becomes the default Executor for the resulting Future.
     */
    public Future<T> recover(Function1<Throwable, ? extends T> func, Executor ex){
        Objects.requireNonNull(func, "func is null");

        Promise<T> result = new Promise<>();

        onCompleted(result::success, e -> result.success(func.apply(e)), ex);

        return result.future();
    }

    /**
     * Creates a new future that will handle any matching throwable that this future might contain.
     * @param func A Function to handle any exception and return some value.
     */
    public Future<T> recover(Function1<Throwable, ? extends T> func){
        return recover(func, ex);
    }

    /**
     * Creates a new future that will handle any matching throwable that this future might contain.
     * @param func A Function to handle any exception and return some value.
     * @param ex Executor to use for this operation. Becomes the default Executor for the resulting Future.
     */
    public Future<T> recoverWith(Function1<Throwable, Future<T>> func, Executor ex){
        Objects.requireNonNull(func, "func is null");

        Promise<T> result = new Promise<>();

        //If this future succeeds, complete result
        onSuccess(result::success);

        //If it fails, run the func and complete result with that future's success or failure
        onFailure(e -> result.completeWith(func.apply(e)), ex);

        return result.future();
    }

    /**
     * Creates a new future that will handle any matching throwable that this future might contain.
     * @param func A Function to handle any exception and return some value.
     */
    public Future<T> recoverWith(Function1<Throwable, Future<T>> func){
        return recoverWith(func, ex);
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Will not be called if the future fails.
     * @param func Function to be applied when the Future completes.
     * @param ex Executor to use for this operation.
     */
    public void onSuccess(Consumer<T> func, Executor ex){
        Objects.requireNonNull(func, "func is null");
        onCompletedTry(r -> r.forEach(func), ex);
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Will not be called if the future fails.
     * @param func Function to be applied when the Future completes.
     */
    public void onSuccess(Consumer<T> func){
        onSuccess(func, ex);
    }

    /**
     * Asynchronously processes the exception in the future once the value becomes available.
     * Will not be called if the future does not throw an exception.
     * @param func Function to be applied if the Future fails.
     * @param ex Executor to use for this operation.
     */
    public void onFailure(Consumer<Throwable> func, Executor ex){
        Objects.requireNonNull(func, "func is null");
        onCompletedTry(r -> r.onFailure(func), ex);
    }

    /**
     * Asynchronously processes the exception in the future once the value becomes available.
     * Will not be called if the future does not throw an exception.
     * @param func Function to be applied if the Future fails.
     */
    public void onFailure(Consumer<Throwable> func){
        onFailure(func, ex);
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Will be called for success or failure.
     * @param func Function to be applied when the Future completes.
     * @param ex Executor to use for this operation.
     */
    public void onCompletedTry(Consumer<Try<T>> func, Executor ex){
        Objects.requireNonNull(func, "func is null");

        inner.whenCompleteAsync((t, e) -> {
            if(t != null)
                func.accept(new Success<>(t));
            else
                func.accept(new Failure<>(e));
        });
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Will be called for success or failure.
     * @param func Function to be applied when the Future completes.
     */
    public void onCompletedTry(Consumer<Try<T>> func){
        onCompletedTry(func, ex);
    }

    /**
     * Asynchronously processes the value in the future once the value becomes available.
     * Either success or failure will be called.
     * @param success Function to be applied if the Future completes successfully.
     * @param failure Function to be applied if the Future completes with an exception.
     * @param ex Executor to use for this operation.
     */
    public void onCompleted(Consumer<T> success, Consumer<Throwable> failure, Executor ex){
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
     * @param success Function to be applied if the Future completes successfully.
     * @param failure Function to be applied if the Future completes with an exception.
     */
    public void onCompleted(Consumer<T> success, Consumer<Throwable> failure){
        onCompleted(success, failure, ex);
    }

    /**
     * Creates a new Future by applying a predicate to the successful result of this Future.
     * If the value fails the test the resulting Future will contain a NoSuchElementException. Otherwise it will be the same value.
     * If this Future fails with an exception the resulting Future will contain the same exception instead.
     * @param func Function to apply to the result
     * @param ex Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public Future<T> filter(Predicate<T> func, Executor ex){
        Objects.requireNonNull(func, "func is null");

        Promise<T> result = new Promise<>();

        onCompleted(t -> {
            if (func.test(t))
                result.success(t);
            else
                result.failure(new NoSuchElementException("Future failed predicate test."));
        }, result::failure, ex);

        return result.future();
    }

    /**
     * Creates a new Future by applying a predicate to the successful result of this Future.
     * If the value fails the test the resulting Future will contain a NoSuchElementException. Otherwise it will be the same value.
     * If this Future fails with an exception the resulting Future will contain the same exception instead.
     * @param func Function to apply to the result
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public Future<T> filter(Predicate<T> func){
        return filter(func, ex);
    }

    /**
     * Applies the side-effecting function to the result of this future, and returns a new future with the result of this future.
     * This method allows one to enforce that the callbacks are executed in a specified order.
     * Note that if one of the chained andThen callbacks throws an exception, that exception is not propagated to the subsequent andThen callbacks.
     * Instead, the subsequent andThen callbacks are given the original value of this future.
     * @param func Function to apply to the result
     * @param ex Executor to use for this operation. Becomes the default Executor for the resulting Future.
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public Future<T> andThen(Consumer<Try<T>> func, Executor ex){
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
     * @param func Function to apply to the result
     * @return A new Future who's value will be calculated with the supplied function once this Future's result is evaluated.
     */
    public Future<T> andThen(Consumer<Try<T>> func){
        return andThen(func, ex);
    }

    /**
     * @return If this Future has finished.
     */
    public boolean isCompleted(){
        return value.isDefined();
    }

    /**
     * @return If this Future has finished AND contains a Success.
     */
    public boolean isSuccess(){
        return value.isDefined() && value.get().isSuccess();
    }

    /**
     * @return If this Future has finished AND contains a Failure.
     */
    public boolean isFailure(){
        return value.isDefined() && value.get().isFailure();
    }

    /**
     * @return The resulting value of this Future. Will be None until the Future finishes. After finishing, it will be a Some of either Success or Failure.
     */
    public Option<Try<T>> value(){
        return value;
    }

    /**
     * Block and wait for this Future to finish. Not recommended.
     * @param timeOut Time to wait
     * @param unit TimeUnit that timeOut is in.
     * @return this, but after blocking until completed.
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public Future<T> await(long timeOut, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        inner.get(timeOut, unit);
        return this;
    }

    /**
     * Block and wait for this Future to finish. Not recommended.
     * @param timeOut Time to wait
     * @param unit TimeUnit that timeOut is in.
     * @return The result value of this Future after blocking.
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public Try<T> ready(long timeOut, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        await(timeOut, unit);
        return value().get();
    }

    /**
     * Returns a new Future which will take the result of #that if and only if this Future fails.
     * @param that An alternetive value.
     */
    public Future<T> fallbackTo(Future<T> that){
        return recoverWith(t -> that);
    }

    private static class FutureConverter {

        protected static final Lazy<FutureConverter> instance = Lazy.of(FutureConverter::new);

        private final Executor ex = Executors.newSingleThreadExecutor();

        protected static FutureConverter instance(){
            return instance.get();
        }

        protected <T> void addFuture(java.util.concurrent.Future<T> in, Promise<T> out){
            ex.execute(() -> {
                if(in.isDone())
                    try {
                        out.success(in.get());
                    } catch (Throwable e){
                        out.failure(e);
                    }
                else {
                    addFuture(in, out);
                }
            });
        }

    }




}
