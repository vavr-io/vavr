/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.CheckedFunction1;
import javaslang.Value;
import javaslang.collection.Iterator;
import javaslang.control.Failure.NonFatal;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An implementation similar to Scala's Try control.
 *
 * @param <T> Value type in the case of success.
 * @author Daniel Dietrich
 * @since 1.0.0
 */
public interface Try<T> extends Value<T> {

    /**
     * Creates a Try of a CheckedSupplier.
     *
     * @param supplier A checked supplier
     * @param <T>      Component type
     * @return {@code Success(supplier.get())} if no exception occurs, otherwise {@code Failure(throwable)} if an
     * exception occurs calling {@code supplier.get()}.
     */
    static <T> Try<T> of(CheckedSupplier<T> supplier) {
        try {
            return new Success<>(supplier.get());
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    /**
     * Creates a Try of a CheckedRunnable.
     *
     * @param runnable A checked runnable
     * @return {@code Success(null)} if no exception occurs, otherwise {@code Failure(throwable)} if an exception occurs
     * calling {@code runnable.run()}.
     */
    static Try<Void> run(CheckedRunnable runnable) {
        try {
            runnable.run();
            return new Success<>(null); // null represents the absence of an value, i.e. Void
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    /**
     * Runs the given checked consumer if this is a {@code Success},
     * passing the result of the current expression to it.
     * If this expression is a {@code Failure} then it'll return a new
     * {@code Failure} of type T with the original exception.
     *
     * The main use case is chaining checked functions using method references:
     *
     * <pre>
     * <code>
     * Try.of(() -&gt; 100)
     *    .andThen(i -&gt; System.out.println(i));
     *
     * </code>
     * </pre>
     *
     * @param consumer A checked consumer taking a single argument.
     * @return a new {@code Try}
     */
    @SuppressWarnings("unchecked")
    default Try<T> andThen(CheckedConsumer<? super T> consumer) {
        if (isFailure()) {
            return this;
        } else {
            return Try.run(() -> consumer.accept(get())).flatMap(ignored -> this);
        }
    }

    /**
     * Runs the given runnable if this is a {@code Success}, otherwise returns this {@code Failure}.
     * Shorthand for {@code flatMap(ignored -> Try.run(runnable))}.
     * The main use case is chaining runnables using method references:
     *
     * <pre>
     * <code>
     * Try.run(A::methodRef).andThen(B::methodRef).andThen(C::methodRef);
     * </code>
     * </pre>
     *
     * Please note that these lines are semantically the same:
     *
     * <pre>
     * <code>
     * Try.run(() -&gt; { doStuff(); })
     *    .andThen(() -&gt; { doMoreStuff(); })
     *    .andThen(() -&gt; { doEvenMoreStuff(); });
     *
     * Try.run(() -&gt; {
     *     doStuff();
     *     doMoreStuff();
     *     doEvenMoreStuff();
     * });
     * </code>
     * </pre>
     *
     * @param runnable A checked runnable
     * @return a new {@code Try}
     */
    default Try<Void> andThen(CheckedRunnable runnable) {
        return flatMap(ignored -> Try.run(runnable));
    }

    /**
     * Returns {@code Success(throwable)} if this is a {@code Failure(throwable)}, otherwise
     * a {@code Failure(new UnsupportedOperationException("Success.failed()"))} if this is a Success.
     *
     * @return a new Try
     */
    default Try<Throwable> failed() {
        if (isSuccess()) {
            return new Failure<>(new UnsupportedOperationException("Success.failed()"));
        } else {
            return new Success<>(getCause().getCause());
        }
    }

    /**
     * Returns {@code this} if this is a Failure or this is a Success and the value satisfies the predicate.
     * <p>
     * Returns a new Failure, if this is a Success and the value does not satisfy the Predicate or an exception
     * occurs testing the predicate.
     *
     * @param predicate A predicate
     * @return a new Try
     */
    default Try<T> filter(Predicate<? super T> predicate) {
        if (isFailure()) {
            return this;
        } else {
            try {
                if (predicate.test(get())) {
                    return this;
                } else {
                    return new Failure<>(new NoSuchElementException("Predicate does not hold for " + get()));
                }
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
    }

    default Try<T> filterTry(CheckedPredicate<? super T> predicate) {
        if (isFailure()) {
            return this;
        } else {
            return Try.of(() -> predicate.test(get())).flatMap(b -> filter(ignored -> b));
        }
    }

    /**
     * FlatMaps the value of a Success or returns a Failure.
     *
     * @param mapper A mapper
     * @param <U>    The new component type
     * @return a new Try
     */
    @SuppressWarnings("unchecked")
    @Override
    default <U> Try<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
        if (isFailure()) {
            return (Failure<U>) this;
        } else {
            return flatMapTry((CheckedFunction<T, java.lang.Iterable<? extends U>>) mapper::apply);
        }
    }

    @SuppressWarnings("unchecked")
    default <U> Try<U> flatMapTry(CheckedFunction<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
        if (isFailure()) {
            return (Failure<U>) this;
        } else {
            try {
                final Iterable<? extends U> iterable = mapper.apply(get());
                if (iterable instanceof Value) {
                    return ((Value<U>) iterable).toTry();
                } else {
                    return Try.of(() -> Value.get(iterable));
                }
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    default Try<Object> flatten() {
        if (isFailure()) {
            return (Failure<Object>) this;
        } else {
            return flatMap(value -> (value instanceof Try) ? ((Try<?>) get()).flatten() : this);
        }
    }

    /**
     * Gets the result of this Try if this is a Success or throws if this is a Failure.
     *
     * @return The result of this Try.
     * @throws NonFatal if this is a Failure
     */
    @Override
    T get();

    /**
     * Gets the cause if this is a Failure or throws if this is a Success.
     *
     * @return The cause if this is a Failure
     * @throws UnsupportedOperationException if this is a Success
     */
    NonFatal getCause();

    /**
     * Checks whether this Try has no result, i.e. is a Failure.
     *
     * @return true if this is a Failure, returns false if this is a Success.
     */
    @Override
    boolean isEmpty();

    /**
     * Checks if this is a Failure.
     *
     * @return true, if this is a Failure, otherwise false, if this is a Success
     */
    boolean isFailure();

    /**
     * Checks if this is a Success.
     *
     * @return true, if this is a Success, otherwise false, if this is a Failure
     */
    boolean isSuccess();

    @Override
    default Iterator<T> iterator() {
        return isSuccess() ? Iterator.of(get()) : Iterator.empty();
    }

    /**
     * Maps the value of a Success or returns a Failure.
     *
     * @param <U>    The new component type
     * @param mapper A mapper
     * @return a new Try
     */
    @SuppressWarnings("unchecked")
    @Override
    default <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        if (isFailure()) {
            return (Failure<U>) this;
        } else {
            try {
                return new Success<>(mapper.apply(get()));
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
    }

    /**
     * Runs the given checked function if this is a {@code Success},
     * passing the result of the current expression to it.
     * If this expression is a {@code Failure} then it'll return a new
     * {@code Failure} of type R with the original exception.
     *
     * The main use case is chaining checked functions using method references:
     *
     * <pre>
     * <code>
     * Try.of(() -&gt; 0)
     *    .mapTry(x -&gt; 1 / x); // division by zero
     * </code>
     * </pre>
     *
     * @param <U>    The new component type
     * @param mapper A checked function
     * @return a new {@code Try}
     */
    @SuppressWarnings("unchecked")
    default <U> Try<U> mapTry(CheckedFunction1<? super T, ? extends U> mapper) {
        if (isFailure()) {
            return (Failure<U>) this;
        } else {
            return Try.of(() -> mapper.apply(get()));
        }
    }

    /**
     * Consumes the throwable if this is a Failure, otherwise returns this Success.
     *
     * @param f A Consumer
     * @return a new Failure, if this is a Failure and the consumer throws, otherwise this, which may be a Success or
     * a Failure.
     */
    default Try<T> onFailure(Consumer<Throwable> f) {
        if (isFailure()) {
            try {
                f.accept(getCause().getCause());
                return this;
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        } else {
            return this;
        }
    }

    default T orElseGet(Function<? super Throwable, ? extends T> other) {
        if (isFailure()) {
            return other.apply(getCause().getCause());
        } else {
            return get();
        }
    }

    default void orElseRun(Consumer<? super Throwable> action) {
        if (isFailure()) {
            action.accept(getCause().getCause());
        }
    }

    default <X extends Throwable> T orElseThrow(Function<? super Throwable, X> exceptionProvider) throws X {
        if (isFailure()) {
            throw exceptionProvider.apply(getCause().getCause());
        } else {
            return get();
        }
    }

    /**
     * Applies the action to the value of a Success or does nothing in the case of a Failure.
     *
     * @param action A Consumer
     * @return this Try
     */
    @Override
    default Try<T> peek(Consumer<? super T> action) {
        if (isSuccess()) {
            action.accept(get());
        }
        return this;
    }

    /**
     * Returns {@code this}, if this is a {@code Success}, otherwise tries to recover the exception of the failure with {@code f},
     * i.e. calling {@code Try.of(() -> f.apply(throwable))}.
     *
     * @param f A recovery function taking a Throwable
     * @return a new Try
     */
    default Try<T> recover(Function<Throwable, ? extends T> f) {
        if (isFailure()) {
            return Try.of(() -> f.apply(getCause().getCause()));
        } else {
            return this;
        }
    }

    /**
     * Returns {@code this}, if this is a Success, otherwise tries to recover the exception of the failure with {@code f},
     * i.e. calling {@code f.apply(cause.getCause())}. If an error occurs recovering a Failure, then the new Failure is
     * returned.
     *
     * @param f A recovery function taking a Throwable
     * @return a new Try
     */
    default Try<T> recoverWith(Function<Throwable, Try<T>> f) {
        if (isFailure()) {
            try {
                return f.apply(getCause().getCause());
            } catch (Throwable t) {
                return new Failure<>(t);
            }

        } else {
            return this;
        }
    }

    default Either<Throwable, T> toEither() {
        if (isFailure()) {
            return new Left<>(getCause().getCause());
        } else {
            return new Right<>(get());
        }
    }

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    /**
     * A {@linkplain java.util.function.Consumer} which may throw.
     *
     * @param <T> the type of value supplied to this consumer.
     */
    @FunctionalInterface
    interface CheckedConsumer<T> {

        /**
         * Performs side-effects.
         *
         * @param value a value
         * @throws Throwable if an error occurs
         */
        void accept(T value) throws Throwable;
    }

    /**
     * A {@linkplain java.util.function.Function} which may throw.
     *
     * @param <T> the type of the input to the function
     * @param <R> the result type of the function
     */
    @FunctionalInterface
    interface CheckedFunction<T, R> {

        /**
         * Applies this function to the given argument.
         *
         * @param t the function argument
         * @return the function result
         * @throws Throwable if an error occurs
         */
        R apply(T t) throws Throwable;
    }

    /**
     * A {@linkplain java.util.function.Predicate} which may throw.
     *
     * @param <T> the type of the input to the predicate
     */
    @FunctionalInterface
    interface CheckedPredicate<T> {

        /**
         * Evaluates this predicate on the given argument.
         *
         * @param t the input argument
         * @return {@code true} if the input argument matches the predicate, otherwise {@code false}
         * @throws Throwable if an error occurs
         */
        boolean test(T t) throws Throwable;
    }

    /**
     * A {@linkplain java.lang.Runnable} which may throw.
     */
    @FunctionalInterface
    interface CheckedRunnable {

        /**
         * Performs side-effects.
         *
         * @throws Throwable if an error occurs
         */
        void run() throws Throwable;
    }

    /**
     * A {@linkplain java.util.function.Supplier} which may throw.
     *
     * @param <R> the type of results supplied by this supplier
     */
    @FunctionalInterface
    interface CheckedSupplier<R> {

        /**
         * Gets a result.
         *
         * @return a result
         * @throws Throwable if an error occurs
         */
        R get() throws Throwable;
    }
}
