/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.*;
import javaslang.collection.Iterator;
import javaslang.collection.Seq;
import javaslang.collection.Vector;

import java.io.Serializable;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static javaslang.API.Match;

/**
 * An implementation similar to Scala's Try control.
 *
 * @param <T> Value type in the case of success.
 * @author Daniel Dietrich
 * @since 1.0.0
 */
public interface Try<T> extends Value<T>, Serializable {

    long serialVersionUID = 1L;

    /**
     * Creates a Try of a CheckedSupplier.
     *
     * @param supplier A checked supplier
     * @param <T>      Component type
     * @return {@code Success(supplier.get())} if no exception occurs, otherwise {@code Failure(throwable)} if an
     * exception occurs calling {@code supplier.get()}.
     */
    static <T> Try<T> of(CheckedSupplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        try {
            return new Success<>(supplier.get());
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }


    /**
     * Creates a Try of a Supplier.
     *
     * @param supplier A supplier
     * @param <T>      Component type
     * @return {@code Success(supplier.get())} if no exception occurs, otherwise {@code Failure(throwable)} if an
     * exception occurs calling {@code supplier.get()}.
     */
    static <T> Try<T> ofSupplier(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return of(supplier::get);
    }

    /**
     * Creates a Try of a Callable.
     *
     * @param callable A callable
     * @param <T>      Component type
     * @return {@code Success(callable.call())} if no exception occurs, otherwise {@code Failure(throwable)} if an
     * exception occurs calling {@code callable.call()}.
     */
    static <T> Try<T> ofCallable(Callable<? extends T> callable) {
        Objects.requireNonNull(callable, "callable is null");
        return of(callable::call);
    }

    /**
     * Creates a Try of a CheckedRunnable.
     *
     * @param runnable A checked runnable
     * @return {@code Success(null)} if no exception occurs, otherwise {@code Failure(throwable)} if an exception occurs
     * calling {@code runnable.run()}.
     */
    static Try<Void> run(CheckedRunnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        try {
            runnable.run();
            return new Success<>(null); // null represents the absence of an value, i.e. Void
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    /**
     * Creates a Try of a Runnable.
     *
     * @param runnable A runnable
     * @return {@code Success(null)} if no exception occurs, otherwise {@code Failure(throwable)} if an exception occurs
     * calling {@code runnable.run()}.
     */
    static Try<Void> runRunnable(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        return run(runnable::run);
    }

    /**
     * Reduces many {@code Try}s into a single {@code Try} by transforming an
     * {@code Iterable<Try<? extends T>>} into a {@code Try<Seq<T>>}. If any of
     * the {@code Try}s are {@link Try.Failure}, then this returns a {@link Try.Failure}.
     *
     * @param values An {@link Iterable} of {@code Try}s
     * @param <T>    type of the Trys
     * @return A {@code Try} of a {@link Seq} of results
     * @throws NullPointerException if {@code values} is null
     */
    static <T> Try<Seq<T>> sequence(Iterable<? extends Try<? extends T>> values) {
        Objects.requireNonNull(values, "values is null");
        Vector<T> vector = Vector.empty();
        for (Try<? extends T> value : values) {
            if (value.isFailure()) {
                return Try.failure(value.getCause());
            }
            vector = vector.append(value.get());
        }
        return Try.success(vector);
    }

    /**
     * Creates a {@link Success} that contains the given {@code value}. Shortcut for {@code new Success<>(value)}.
     *
     * @param value A value.
     * @param <T>   Type of the given {@code value}.
     * @return A new {@code Success}.
     */
    static <T> Try<T> success(T value) {
        return new Success<>(value);
    }

    /**
     * Creates a {@link Failure} that contains the given {@code exception}. Shortcut for {@code new Failure<>(exception)}.
     *
     * @param exception An exception.
     * @param <T>       Component type of the {@code Try}.
     * @return A new {@code Failure}.
     */
    static <T> Try<T> failure(Throwable exception) {
        return new Failure<>(exception);
    }

    /**
     * Narrows a widened {@code Try<? extends T>} to {@code Try<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param t   A {@code Try}.
     * @param <T> Component type of the {@code Try}.
     * @return the given {@code t} instance as narrowed type {@code Try<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> Try<T> narrow(Try<? extends T> t) {
        return (Try<T>) t;
    }

    /**
     * Shortcut for {@code andThenTry(consumer::accept)}, see {@link #andThenTry(CheckedConsumer)}.
     *
     * @param consumer A consumer
     * @return this {@code Try} if this is a {@code Failure} or the consumer succeeded, otherwise the
     * {@code Failure} of the consumption.
     * @throws NullPointerException if {@code consumer} is null
     */
    default Try<T> andThen(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer, "consumer is null");
        return andThenTry(consumer::accept);
    }

    /**
     * Passes the result to the given {@code consumer} if this is a {@code Success}.
     * <p>
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
     * @param consumer A checked consumer
     * @return this {@code Try} if this is a {@code Failure} or the consumer succeeded, otherwise the
     * {@code Failure} of the consumption.
     * @throws NullPointerException if {@code consumer} is null
     */
    default Try<T> andThenTry(CheckedConsumer<? super T> consumer) {
        Objects.requireNonNull(consumer, "consumer is null");
        if (isFailure()) {
            return this;
        } else {
            try {
                consumer.accept(get());
                return this;
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
    }

    /**
     * Shortcut for {@code andThenTry(runnable::run)}, see {@link #andThenTry(CheckedRunnable)}.
     *
     * @param runnable A runnable
     * @return this {@code Try} if this is a {@code Failure} or the runnable succeeded, otherwise the
     * {@code Failure} of the run.
     * @throws NullPointerException if {@code runnable} is null
     */
    default Try<T> andThen(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        return andThenTry(runnable::run);
    }

    /**
     * Runs the given runnable if this is a {@code Success}, otherwise returns this {@code Failure}.
     * <p>
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
     * Try.run(this::doStuff)
     *    .andThen(this::doMoreStuff)
     *    .andThen(this::doEvenMoreStuff);
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
     * @return this {@code Try} if this is a {@code Failure} or the runnable succeeded, otherwise the
     * {@code Failure} of the run.
     * @throws NullPointerException if {@code runnable} is null
     */
    default Try<T> andThenTry(CheckedRunnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        if (isFailure()) {
            return this;
        } else {
            try {
                runnable.run();
                return this;
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
    }

    /**
     * Returns {@code Success(throwable)} if this is a {@code Failure(throwable)}, otherwise
     * a {@code Failure(new NoSuchElementException("Success.failed()"))} if this is a Success.
     *
     * @return a new Try
     */
    default Try<Throwable> failed() {
        if (isFailure()) {
            return new Success<>(getCause());
        } else {
            return new Failure<>(new NoSuchElementException("Success.failed()"));
        }
    }

    /**
     * Shortcut for {@code filterTry(predicate::test, throwableSupplier)}, see
     * {@link #filterTry(CheckedPredicate, Supplier)}}.
     *
     * @param predicate         A predicate
     * @param throwableSupplier A supplier of a throwable
     * @return a {@code Try} instance
     * @throws NullPointerException if {@code predicate} or {@code throwableSupplier} is null
     */
    default Try<T> filter(Predicate<? super T> predicate, Supplier<? extends Throwable> throwableSupplier) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(throwableSupplier, "throwableSupplier is null");
        return filterTry(predicate::test, throwableSupplier);
    }

    /**
     * Shortcut for {@code filterTry(predicate::test, errorProvider::apply)}, see
     * {@link #filterTry(CheckedPredicate, CheckedFunction1)}}.
     *
     * @param predicate A predicate
     * @param errorProvider A function that provides some kind of Throwable for T
     * @return a {@code Try} instance
     * @throws NullPointerException if {@code predicate} or {@code errorProvider} is null
     */
    default Try<T> filter(Predicate<? super T> predicate, Function<? super T, ? extends Throwable> errorProvider) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(errorProvider, "errorProvider is null");
        return filterTry(predicate::test, errorProvider::apply);
    }

    /**
     * Shortcut for {@code filterTry(predicate::test)}, see {@link #filterTry(CheckedPredicate)}}.
     *
     * @param predicate A predicate
     * @return a {@code Try} instance
     * @throws NullPointerException if {@code predicate} is null
     */
    default Try<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filterTry(predicate::test);
    }

    /**
     * Returns {@code this} if this is a Failure or this is a Success and the value satisfies the predicate.
     * <p>
     * Returns a new Failure, if this is a Success and the value does not satisfy the Predicate or an exception
     * occurs testing the predicate. The returned Failure wraps a Throwable instance provided by the given
     * {@code throwableSupplier}.
     *
     * @param predicate         A checked predicate
     * @param throwableSupplier A supplier of a throwable
     * @return a {@code Try} instance
     * @throws NullPointerException if {@code predicate} or {@code throwableSupplier} is null
     */
    default Try<T> filterTry(CheckedPredicate<? super T> predicate, Supplier<? extends Throwable> throwableSupplier) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(throwableSupplier, "throwableSupplier is null");

        if (isFailure()) {
            return this;
        } else {
            try {
                if (predicate.test(get())) {
                    return this;
                } else {
                    return new Failure<>(throwableSupplier.get());
                }
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
    }

    /**
     * Returns {@code this} if this is a Failure or this is a Success and the value satisfies the predicate.
     * <p>
     * Returns a new Failure, if this is a Success and the value does not satisfy the Predicate or an exception
     * occurs testing the predicate. The returned Failure wraps a Throwable instance provided by the given
     * {@code errorProvider}.
     *
     * @param predicate         A checked predicate
     * @param errorProvider     A provider of a throwable
     * @return a {@code Try} instance
     * @throws NullPointerException if {@code predicate} or {@code errorProvider} is null
     */
    default Try<T> filterTry(CheckedPredicate<? super T> predicate, CheckedFunction1<? super T, ? extends Throwable> errorProvider) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(errorProvider, "errorProvider is null");
        return flatMapTry(t -> predicate.test(t) ? this : failure(errorProvider.apply(t)));
    }

    /**
     * Returns {@code this} if this is a Failure or this is a Success and the value satisfies the predicate.
     * <p>
     * Returns a new Failure, if this is a Success and the value does not satisfy the Predicate or an exception
     * occurs testing the predicate. The returned Failure wraps a {@link NoSuchElementException} instance.
     *
     * @param predicate A checked predicate
     * @return a {@code Try} instance
     * @throws NullPointerException if {@code predicate} is null
     */
    default Try<T> filterTry(CheckedPredicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filterTry(predicate, () -> new NoSuchElementException("Predicate does not hold for " + get()));
    }

    /**
     * Shortcut for {@code flatMapTry(mapper::apply)}, see {@link #flatMapTry(CheckedFunction)}.
     *
     * @param mapper A mapper
     * @param <U>    The new component type
     * @return a {@code Try}
     * @throws NullPointerException if {@code mapper} is null
     */
    default <U> Try<U> flatMap(Function<? super T, ? extends Try<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return flatMapTry((CheckedFunction<T, Try<? extends U>>) mapper::apply);
    }

    /**
     * FlatMaps the value of a Success or returns a Failure.
     *
     * @param mapper A mapper
     * @param <U>    The new component type
     * @return a {@code Try}
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    default <U> Try<U> flatMapTry(CheckedFunction<? super T, ? extends Try<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isFailure()) {
            return (Failure<U>) this;
        } else {
            try {
                return (Try<U>) mapper.apply(get());
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
    }

    /**
     * Gets the result of this Try if this is a Success or throws if this is a Failure.
     *
     * @return The result of this Try.
     * @throws NonFatalException if this is a Failure
     */
    @Override
    T get();

    /**
     * Gets the cause if this is a Failure or throws if this is a Success.
     *
     * @return The cause if this is a Failure
     * @throws UnsupportedOperationException if this is a Success
     */
    Throwable getCause();

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
     * A {@code Try} is a single-valued.
     *
     * @return {@code true}
     */
    @Override
    default boolean isSingleValued() {
        return true;
    }

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
     * Shortcut for {@code mapTry(mapper::apply)}, see {@link #mapTry(CheckedFunction)}.
     *
     * @param <U>    The new component type
     * @param mapper A checked function
     * @return a {@code Try}
     * @throws NullPointerException if {@code mapper} is null
     */
    @Override
    default <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return mapTry(mapper::apply);
    }

    /**
     * Maps the cause to a new exception if this is a {@code Failure} or returns this instance if this is a {@code Success}.
     * <p>
     * If none of the given cases matches the cause, the same {@code Failure} is returned.
     *
     * @param cases A not necessarily exhaustive sequence of cases that will be matched against a cause.
     * @return A new {@code Try} if this is a {@code Failure}, otherwise this.
     */
    @SuppressWarnings({ "unchecked", "varargs" })
    default Try<T> mapFailure(Match.Case<? extends Throwable, ? extends Throwable>... cases) {
        if (isSuccess()) {
            return this;
        } else {
            final Option<Throwable> x = Match(getCause()).option(cases);
            return x.isEmpty() ? this : failure(x.get());
        }
    }

    /**
     * Runs the given checked function if this is a {@code Success},
     * passing the result of the current expression to it.
     * If this expression is a {@code Failure} then it'll return a new
     * {@code Failure} of type R with the original exception.
     * <p>
     * The main use case is chaining checked functions using method references:
     *
     * <pre>
     * <code>
     * Try.of(() -&gt; 0)
     *    .map(x -&gt; 1 / x); // division by zero
     * </code>
     * </pre>
     *
     * @param <U>    The new component type
     * @param mapper A checked function
     * @return a {@code Try}
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    default <U> Try<U> mapTry(CheckedFunction<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
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
     * Consumes the throwable if this is a Failure.
     *
     * @param action An exception consumer
     * @return this
     * @throws NullPointerException if {@code action} is null
     */
    default Try<T> onFailure(Consumer<? super Throwable> action) {
        Objects.requireNonNull(action, "action is null");
        if (isFailure()) {
            action.accept(getCause());
        }
        return this;
    }

    /**
     * Consumes the value if this is a Success.
     *
     * @param action A value consumer
     * @return this
     * @throws NullPointerException if {@code action} is null
     */
    default Try<T> onSuccess(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (isSuccess()) {
            action.accept(get());
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    default Try<T> orElse(Try<? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        return isSuccess() ? this : (Try<T>) other;
    }

    @SuppressWarnings("unchecked")
    default Try<T> orElse(Supplier<? extends Try<? extends T>> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isSuccess() ? this : (Try<T>) supplier.get();
    }

    default T getOrElseGet(Function<? super Throwable, ? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        if (isFailure()) {
            return other.apply(getCause());
        } else {
            return get();
        }
    }

    default void orElseRun(Consumer<? super Throwable> action) {
        Objects.requireNonNull(action, "action is null");
        if (isFailure()) {
            action.accept(getCause());
        }
    }

    default <X extends Throwable> T getOrElseThrow(Function<? super Throwable, X> exceptionProvider) throws X {
        Objects.requireNonNull(exceptionProvider, "exceptionProvider is null");
        if (isFailure()) {
            throw exceptionProvider.apply(getCause());
        } else {
            return get();
        }
    }

    /**
     * Applies the action to the value of a Success or does nothing in the case of a Failure.
     *
     * @param action A Consumer
     * @return this {@code Try}
     * @throws NullPointerException if {@code action} is null
     */
    @Override
    default Try<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (isSuccess()) {
            action.accept(get());
        }
        return this;
    }

    /**
     * Returns {@code this}, if this is a {@code Success} or this is a {@code Failure} and the cause is not assignable
     * from {@code cause.getClass()}. Otherwise tries to recover the exception of the failure with {@code f},
     * i.e. calling {@code Try.of(() -> f.apply((X) getCause())}.
     *
     * @param <X>       Exception type
     * @param exception The specific exception type that should be handled
     * @param f         A recovery function taking an exception of type {@code X}
     * @return a {@code Try}
     * @throws NullPointerException if {@code exception} is null or {@code f} is null
     */
    @GwtIncompatible
    @SuppressWarnings("unchecked")
    default <X extends Throwable> Try<T> recover(Class<X> exception, Function<? super X, ? extends T> f) {
        Objects.requireNonNull(exception, "exception is null");
        Objects.requireNonNull(f, "f is null");
        if (isFailure()) {
            final Throwable cause = getCause();
            if (exception.isAssignableFrom(cause.getClass())) {
                return Try.of(() -> f.apply((X) cause));
            }
        }
        return this;
    }

    /**
     * Returns {@code this}, if this is a {@code Success} or this is a {@code Failure} and the cause is not assignable
     * from {@code cause.getClass()}. Otherwise tries to recover the exception of the failure with {@code f} <b>which returns Try</b>.
     * If {@link Try#isFailure()} returned by {@code f} function is <code>true</code> it means that recovery cannot take place due to some circumstances.
     *
     * @param <X>       Exception type
     * @param exception The specific exception type that should be handled
     * @param f         A recovery function taking an exception of type {@code X} and returning Try as a result of recovery.
     *                  If Try is {@link Try#isSuccess()} then recovery ends up successfully. Otherwise the function was not able to recover.
     * @return a {@code Try}
     */
    @GwtIncompatible
    @SuppressWarnings("unchecked")
    default <X extends Throwable> Try<T> recoverWith(Class<X> exception, Function<? super X, Try<? extends T>> f){
        Objects.requireNonNull(exception, "exception is null");
        Objects.requireNonNull(f, "f is null");
        if(isFailure()){
            final Throwable cause = getCause();
            if (exception.isAssignableFrom(cause.getClass())) {
                try {
                    return narrow(f.apply((X) cause));
                } catch (Throwable t) {
                    return new Failure<>(t);
                }
            }
        }
        return this;
    }

    @GwtIncompatible
    default <X extends Throwable> Try<T> recoverWith(Class<X> exception,  Try<? extends T> recovered){
        return (isFailure() && exception.isAssignableFrom(getCause().getClass()))
                ? narrow(recovered)
                : this;
    }


    /**
     * Returns {@code this}, if this is a {@code Success} or this is a {@code Failure} and the cause is not assignable
     * from {@code cause.getClass()}. Otherwise returns a {@code Success} containing the given {@code value}.
     *
     * @param <X>       Exception type
     * @param exception The specific exception type that should be handled
     * @param value     A value that is used in case of a recovery
     * @return a {@code Try}
     * @throws NullPointerException if {@code exception} is null
     */
    @GwtIncompatible
    default <X extends Throwable> Try<T> recover(Class<X> exception, T value) {
        Objects.requireNonNull(exception, "exception is null");
        return (isFailure() && exception.isAssignableFrom(getCause().getClass()))
               ? Try.success(value)
               : this;
    }



    /**
     * Returns {@code this}, if this is a {@code Success}, otherwise tries to recover the exception of the failure with {@code f},
     * i.e. calling {@code Try.of(() -> f.apply(throwable))}.
     *
     * @param f A recovery function taking a Throwable
     * @return a {@code Try}
     * @throws NullPointerException if {@code f} is null
     */
    default Try<T> recover(Function<? super Throwable, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        if (isFailure()) {
            return Try.of(() -> f.apply(getCause()));
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
     * @return a {@code Try}
     * @throws NullPointerException if {@code f} is null
     */
    @SuppressWarnings("unchecked")
    default Try<T> recoverWith(Function<? super Throwable, ? extends Try<? extends T>> f) {
        Objects.requireNonNull(f, "f is null");
        if (isFailure()) {
            try {
                return (Try<T>) f.apply(getCause());
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        } else {
            return this;
        }
    }

    /**
     * Converts this {@code Try} to an {@link Either}.
     *
     * @return A new {@code Either}
     */
    default Either<Throwable, T> toEither() {
        if (isFailure()) {
            return Either.left(getCause());
        } else {
            return Either.right(get());
        }
    }

    /**
     * Transforms this {@code Try}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(Function<? super Try<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    /**
     * Provides try's finally behavior no matter what the result of the operation is.
     *
     * @param runnable A runnable
     * @return this {@code Try}.
     * @throws NullPointerException if {@code runnable} is null
     */
    default Try<T> andFinally(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        return andFinallyTry(runnable::run);
    }

    /**
     * Provides try's finally behavior no matter what the result of the operation is.
     *
     * @param runnable A runnable
     * @return this {@code Try}.
     * @throws NullPointerException if {@code runnable} is null
     */
    default Try<T> andFinallyTry(CheckedRunnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        try {
            runnable.run();
            return this;
        } catch (Throwable t) {
            return new Failure<>(t);
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

        /**
         * Negates this predicate.
         *
         * @return A new CheckedPredicate.
         */
        default CheckedPredicate<T> negate() {
            return t -> !test(t);
        }
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

    /**
     * A succeeded Try.
     *
     * @param <T> component type of this Success
     * @author Daniel Dietrich
     * @since 1.0.0
     */
    final class Success<T> implements Try<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private final T value;

        /**
         * Constructs a Success.
         *
         * @param value The value of this Success.
         */
        private Success(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public Throwable getCause() {
            throw new UnsupportedOperationException("getCause on Success");
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Success && Objects.equals(value, ((Success<?>) obj).value));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String stringPrefix() {
            return "Success";
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + value + ")";
        }
    }

    /**
     * A failed Try.
     *
     * @param <T> component type of this Failure
     * @author Daniel Dietrich
     * @since 1.0.0
     */
    final class Failure<T> implements Try<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private final NonFatalException cause;

        /**
         * Constructs a Failure.
         *
         * @param exception A cause of type Throwable, may not be null.
         * @throws NullPointerException if exception is null
         * @throws Error                if the given exception if fatal, i.e. non-recoverable
         */
        private Failure(Throwable exception) {
            Objects.requireNonNull(exception, "exception is null");
            cause = NonFatalException.of(exception);
        }

        // Throws NonFatal instead of Throwable because it is a RuntimeException which does not need to be checked.
        @Override
        public T get() throws NonFatalException {
            throw cause;
        }

        @Override
        public Throwable getCause() {
            return cause.getCause();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Failure && Objects.equals(cause, ((Failure<?>) obj).cause));
        }

        @Override
        public String stringPrefix() {
            return "Failure";
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(cause.getCause());
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + cause.getCause() + ")";
        }

    }

    // -- try with resources

    /**
     * Creates a {@code Try}-with-resources builder that operates on one {@link AutoCloseable} resource.
     *
     * @param t1Supplier The supplier of the first resource.
     * @param <T1> Type of the 1st resource.
     * @return a new {@link WithResources1} instance.
     */
    static <T1 extends AutoCloseable> WithResources1<T1> withResources(CheckedSupplier<? extends T1> t1Supplier) {
        return new WithResources1<>(t1Supplier);
    }

    /**
     * Creates a {@code Try}-with-resources builder that operates on two {@link AutoCloseable} resources.
     *
     * @param t1Supplier The supplier of the 1st resource.
     * @param t2Supplier The supplier of the 2nd resource.
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @return a new {@link WithResources2} instance.
     */
    static <T1 extends AutoCloseable, T2 extends AutoCloseable> WithResources2<T1, T2> withResources(CheckedSupplier<? extends T1> t1Supplier, CheckedSupplier<? extends T2> t2Supplier) {
        return new WithResources2<>(t1Supplier, t2Supplier);
    }

    /**
     * Creates a {@code Try}-with-resources builder that operates on three {@link AutoCloseable} resources.
     *
     * @param t1Supplier The supplier of the 1st resource.
     * @param t2Supplier The supplier of the 2nd resource.
     * @param t3Supplier The supplier of the 3rd resource.
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @return a new {@link WithResources3} instance.
     */
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable> WithResources3<T1, T2, T3> withResources(CheckedSupplier<? extends T1> t1Supplier, CheckedSupplier<? extends T2> t2Supplier, CheckedSupplier<? extends T3> t3Supplier) {
        return new WithResources3<>(t1Supplier, t2Supplier, t3Supplier);
    }

    /**
     * Creates a {@code Try}-with-resources builder that operates on four {@link AutoCloseable} resources.
     *
     * @param t1Supplier The supplier of the 1st resource.
     * @param t2Supplier The supplier of the 2nd resource.
     * @param t3Supplier The supplier of the 3rd resource.
     * @param t4Supplier The supplier of the 4th resource.
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @return a new {@link WithResources4} instance.
     */
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable> WithResources4<T1, T2, T3, T4> withResources(CheckedSupplier<? extends T1> t1Supplier, CheckedSupplier<? extends T2> t2Supplier, CheckedSupplier<? extends T3> t3Supplier, CheckedSupplier<? extends T4> t4Supplier) {
        return new WithResources4<>(t1Supplier, t2Supplier, t3Supplier, t4Supplier);
    }

    /**
     * Creates a {@code Try}-with-resources builder that operates on five {@link AutoCloseable} resources.
     *
     * @param t1Supplier The supplier of the 1st resource.
     * @param t2Supplier The supplier of the 2nd resource.
     * @param t3Supplier The supplier of the 3rd resource.
     * @param t4Supplier The supplier of the 4th resource.
     * @param t5Supplier The supplier of the 5th resource.
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     * @return a new {@link WithResources5} instance.
     */
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable> WithResources5<T1, T2, T3, T4, T5> withResources(CheckedSupplier<? extends T1> t1Supplier, CheckedSupplier<? extends T2> t2Supplier, CheckedSupplier<? extends T3> t3Supplier, CheckedSupplier<? extends T4> t4Supplier, CheckedSupplier<? extends T5> t5Supplier) {
        return new WithResources5<>(t1Supplier, t2Supplier, t3Supplier, t4Supplier, t5Supplier);
    }

    /**
     * Creates a {@code Try}-with-resources builder that operates on six {@link AutoCloseable} resources.
     *
     * @param t1Supplier The supplier of the 1st resource.
     * @param t2Supplier The supplier of the 2nd resource.
     * @param t3Supplier The supplier of the 3rd resource.
     * @param t4Supplier The supplier of the 4th resource.
     * @param t5Supplier The supplier of the 5th resource.
     * @param t6Supplier The supplier of the 6th resource.
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     * @param <T6> Type of the 6th resource.
     * @return a new {@link WithResources6} instance.
     */
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable> WithResources6<T1, T2, T3, T4, T5, T6> withResources(CheckedSupplier<? extends T1> t1Supplier, CheckedSupplier<? extends T2> t2Supplier, CheckedSupplier<? extends T3> t3Supplier, CheckedSupplier<? extends T4> t4Supplier, CheckedSupplier<? extends T5> t5Supplier, CheckedSupplier<? extends T6> t6Supplier) {
        return new WithResources6<>(t1Supplier, t2Supplier, t3Supplier, t4Supplier, t5Supplier, t6Supplier);
    }

    /**
     * Creates a {@code Try}-with-resources builder that operates on seven {@link AutoCloseable} resources.
     *
     * @param t1Supplier The supplier of the 1st resource.
     * @param t2Supplier The supplier of the 2nd resource.
     * @param t3Supplier The supplier of the 3rd resource.
     * @param t4Supplier The supplier of the 4th resource.
     * @param t5Supplier The supplier of the 5th resource.
     * @param t6Supplier The supplier of the 6th resource.
     * @param t7Supplier The supplier of the 7th resource.
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     * @param <T6> Type of the 6th resource.
     * @param <T7> Type of the 7th resource.
     * @return a new {@link WithResources7} instance.
     */
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable, T7 extends AutoCloseable> WithResources7<T1, T2, T3, T4, T5, T6, T7> withResources(CheckedSupplier<? extends T1> t1Supplier, CheckedSupplier<? extends T2> t2Supplier, CheckedSupplier<? extends T3> t3Supplier, CheckedSupplier<? extends T4> t4Supplier, CheckedSupplier<? extends T5> t5Supplier, CheckedSupplier<? extends T6> t6Supplier, CheckedSupplier<? extends T7> t7Supplier) {
        return new WithResources7<>(t1Supplier, t2Supplier, t3Supplier, t4Supplier, t5Supplier, t6Supplier, t7Supplier);
    }

    /**
     * Creates a {@code Try}-with-resources builder that operates on eight {@link AutoCloseable} resources.
     *
     * @param t1Supplier The supplier of the 1st resource.
     * @param t2Supplier The supplier of the 2nd resource.
     * @param t3Supplier The supplier of the 3rd resource.
     * @param t4Supplier The supplier of the 4th resource.
     * @param t5Supplier The supplier of the 5th resource.
     * @param t6Supplier The supplier of the 6th resource.
     * @param t7Supplier The supplier of the 7th resource.
     * @param t8Supplier The supplier of the 8th resource.
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     * @param <T6> Type of the 6th resource.
     * @param <T7> Type of the 7th resource.
     * @param <T8> Type of the 8th resource.
     * @return a new {@link WithResources8} instance.
     */
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable, T7 extends AutoCloseable, T8 extends AutoCloseable> WithResources8<T1, T2, T3, T4, T5, T6, T7, T8> withResources(CheckedSupplier<? extends T1> t1Supplier, CheckedSupplier<? extends T2> t2Supplier, CheckedSupplier<? extends T3> t3Supplier, CheckedSupplier<? extends T4> t4Supplier, CheckedSupplier<? extends T5> t5Supplier, CheckedSupplier<? extends T6> t6Supplier, CheckedSupplier<? extends T7> t7Supplier, CheckedSupplier<? extends T8> t8Supplier) {
        return new WithResources8<>(t1Supplier, t2Supplier, t3Supplier, t4Supplier, t5Supplier, t6Supplier, t7Supplier, t8Supplier);
    }

    /**
     * A {@code Try}-with-resources builder that operates on one {@link AutoCloseable} resource.
     * 
     * @param <T1> Type of the 1st resource.
     */
    final class WithResources1<T1 extends AutoCloseable> {

        private final CheckedSupplier<? extends T1> t1Supplier;

        private WithResources1(CheckedSupplier<? extends T1> t1Supplier) {
            this.t1Supplier = t1Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes one {@code AutoClosable} resource.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
        public <R> Try<R> of(CheckedFunction1<? super T1, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.get()) {
                    return f.apply(t1);
                }
            });
        }
    }

    /**
     * A {@code Try}-with-resources builder that operates on two {@link AutoCloseable} resources.
     *
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     */
    final class WithResources2<T1 extends AutoCloseable, T2 extends AutoCloseable> {

        private final CheckedSupplier<? extends T1> t1Supplier;
        private final CheckedSupplier<? extends T2> t2Supplier;

        private WithResources2(CheckedSupplier<? extends T1> t1Supplier, CheckedSupplier<? extends T2> t2Supplier) {
            this.t1Supplier = t1Supplier;
            this.t2Supplier = t2Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes two {@code AutoClosable} resources.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
        public <R> Try<R> of(CheckedFunction2<? super T1, ? super T2, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.get(); T2 t2 = t2Supplier.get()) {
                    return f.apply(t1, t2);
                }
            });
        }
    }

    /**
     * A {@code Try}-with-resources builder that operates on three {@link AutoCloseable} resources.
     *
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     */
    final class WithResources3<T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable> {

        private final CheckedSupplier<? extends T1> t1Supplier;
        private final CheckedSupplier<? extends T2> t2Supplier;
        private final CheckedSupplier<? extends T3> t3Supplier;

        private WithResources3(CheckedSupplier<? extends T1> t1Supplier, CheckedSupplier<? extends T2> t2Supplier, CheckedSupplier<? extends T3> t3Supplier) {
            this.t1Supplier = t1Supplier;
            this.t2Supplier = t2Supplier;
            this.t3Supplier = t3Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes three {@code AutoClosable} resources.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
        public <R> Try<R> of(CheckedFunction3<? super T1, ? super T2, ? super T3, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.get(); T2 t2 = t2Supplier.get(); T3 t3 = t3Supplier.get()) {
                    return f.apply(t1, t2, t3);
                }
            });
        }
    }

    /**
     * A {@code Try}-with-resources builder that operates on four {@link AutoCloseable} resources.
     *
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     */
    final class WithResources4<T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable> {

        private final CheckedSupplier<? extends T1> t1Supplier;
        private final CheckedSupplier<? extends T2> t2Supplier;
        private final CheckedSupplier<? extends T3> t3Supplier;
        private final CheckedSupplier<? extends T4> t4Supplier;

        private WithResources4(CheckedSupplier<? extends T1> t1Supplier, CheckedSupplier<? extends T2> t2Supplier, CheckedSupplier<? extends T3> t3Supplier, CheckedSupplier<? extends T4> t4Supplier) {
            this.t1Supplier = t1Supplier;
            this.t2Supplier = t2Supplier;
            this.t3Supplier = t3Supplier;
            this.t4Supplier = t4Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes four {@code AutoClosable} resources.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
        public <R> Try<R> of(CheckedFunction4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.get(); T2 t2 = t2Supplier.get(); T3 t3 = t3Supplier.get(); T4 t4 = t4Supplier.get()) {
                    return f.apply(t1, t2, t3, t4);
                }
            });
        }
    }

    /**
     * A {@code Try}-with-resources builder that operates on five {@link AutoCloseable} resources.
     *
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     */
    final class WithResources5<T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable> {

        private final CheckedSupplier<? extends T1> t1Supplier;
        private final CheckedSupplier<? extends T2> t2Supplier;
        private final CheckedSupplier<? extends T3> t3Supplier;
        private final CheckedSupplier<? extends T4> t4Supplier;
        private final CheckedSupplier<? extends T5> t5Supplier;

        private WithResources5(CheckedSupplier<? extends T1> t1Supplier, CheckedSupplier<? extends T2> t2Supplier, CheckedSupplier<? extends T3> t3Supplier, CheckedSupplier<? extends T4> t4Supplier, CheckedSupplier<? extends T5> t5Supplier) {
            this.t1Supplier = t1Supplier;
            this.t2Supplier = t2Supplier;
            this.t3Supplier = t3Supplier;
            this.t4Supplier = t4Supplier;
            this.t5Supplier = t5Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes five {@code AutoClosable} resources.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
        public <R> Try<R> of(CheckedFunction5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.get(); T2 t2 = t2Supplier.get(); T3 t3 = t3Supplier.get(); T4 t4 = t4Supplier.get(); T5 t5 = t5Supplier.get()) {
                    return f.apply(t1, t2, t3, t4, t5);
                }
            });
        }
    }

    /**
     * A {@code Try}-with-resources builder that operates on six {@link AutoCloseable} resources.
     *
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     * @param <T6> Type of the 6th resource.
     */
    final class WithResources6<T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable> {

        private final CheckedSupplier<? extends T1> t1Supplier;
        private final CheckedSupplier<? extends T2> t2Supplier;
        private final CheckedSupplier<? extends T3> t3Supplier;
        private final CheckedSupplier<? extends T4> t4Supplier;
        private final CheckedSupplier<? extends T5> t5Supplier;
        private final CheckedSupplier<? extends T6> t6Supplier;

        private WithResources6(CheckedSupplier<? extends T1> t1Supplier, CheckedSupplier<? extends T2> t2Supplier, CheckedSupplier<? extends T3> t3Supplier, CheckedSupplier<? extends T4> t4Supplier, CheckedSupplier<? extends T5> t5Supplier, CheckedSupplier<? extends T6> t6Supplier) {
            this.t1Supplier = t1Supplier;
            this.t2Supplier = t2Supplier;
            this.t3Supplier = t3Supplier;
            this.t4Supplier = t4Supplier;
            this.t5Supplier = t5Supplier;
            this.t6Supplier = t6Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes six {@code AutoClosable} resources.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
        public <R> Try<R> of(CheckedFunction6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.get(); T2 t2 = t2Supplier.get(); T3 t3 = t3Supplier.get(); T4 t4 = t4Supplier.get(); T5 t5 = t5Supplier.get(); T6 t6 = t6Supplier.get()) {
                    return f.apply(t1, t2, t3, t4, t5, t6);
                }
            });
        }
    }

    /**
     * A {@code Try}-with-resources builder that operates on seven {@link AutoCloseable} resources.
     *
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     * @param <T6> Type of the 6th resource.
     * @param <T7> Type of the 7th resource.
     */
    final class WithResources7<T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable, T7 extends AutoCloseable> {

        private final CheckedSupplier<? extends T1> t1Supplier;
        private final CheckedSupplier<? extends T2> t2Supplier;
        private final CheckedSupplier<? extends T3> t3Supplier;
        private final CheckedSupplier<? extends T4> t4Supplier;
        private final CheckedSupplier<? extends T5> t5Supplier;
        private final CheckedSupplier<? extends T6> t6Supplier;
        private final CheckedSupplier<? extends T7> t7Supplier;

        private WithResources7(CheckedSupplier<? extends T1> t1Supplier, CheckedSupplier<? extends T2> t2Supplier, CheckedSupplier<? extends T3> t3Supplier, CheckedSupplier<? extends T4> t4Supplier, CheckedSupplier<? extends T5> t5Supplier, CheckedSupplier<? extends T6> t6Supplier, CheckedSupplier<? extends T7> t7Supplier) {
            this.t1Supplier = t1Supplier;
            this.t2Supplier = t2Supplier;
            this.t3Supplier = t3Supplier;
            this.t4Supplier = t4Supplier;
            this.t5Supplier = t5Supplier;
            this.t6Supplier = t6Supplier;
            this.t7Supplier = t7Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes seven {@code AutoClosable} resources.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
        public <R> Try<R> of(CheckedFunction7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.get(); T2 t2 = t2Supplier.get(); T3 t3 = t3Supplier.get(); T4 t4 = t4Supplier.get(); T5 t5 = t5Supplier.get(); T6 t6 = t6Supplier.get(); T7 t7 = t7Supplier.get()) {
                    return f.apply(t1, t2, t3, t4, t5, t6, t7);
                }
            });
        }
    }

    /**
     * A {@code Try}-with-resources builder that operates on eight {@link AutoCloseable} resources.
     *
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     * @param <T6> Type of the 6th resource.
     * @param <T7> Type of the 7th resource.
     * @param <T8> Type of the 8th resource.
     */
    final class WithResources8<T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable, T7 extends AutoCloseable, T8 extends AutoCloseable> {

        private final CheckedSupplier<? extends T1> t1Supplier;
        private final CheckedSupplier<? extends T2> t2Supplier;
        private final CheckedSupplier<? extends T3> t3Supplier;
        private final CheckedSupplier<? extends T4> t4Supplier;
        private final CheckedSupplier<? extends T5> t5Supplier;
        private final CheckedSupplier<? extends T6> t6Supplier;
        private final CheckedSupplier<? extends T7> t7Supplier;
        private final CheckedSupplier<? extends T8> t8Supplier;

        private WithResources8(CheckedSupplier<? extends T1> t1Supplier, CheckedSupplier<? extends T2> t2Supplier, CheckedSupplier<? extends T3> t3Supplier, CheckedSupplier<? extends T4> t4Supplier, CheckedSupplier<? extends T5> t5Supplier, CheckedSupplier<? extends T6> t6Supplier, CheckedSupplier<? extends T7> t7Supplier, CheckedSupplier<? extends T8> t8Supplier) {
            this.t1Supplier = t1Supplier;
            this.t2Supplier = t2Supplier;
            this.t3Supplier = t3Supplier;
            this.t4Supplier = t4Supplier;
            this.t5Supplier = t5Supplier;
            this.t6Supplier = t6Supplier;
            this.t7Supplier = t7Supplier;
            this.t8Supplier = t8Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes eight {@code AutoClosable} resources.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try"/* https://bugs.openjdk.java.net/browse/JDK-8155591 */)
        public <R> Try<R> of(CheckedFunction8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.get(); T2 t2 = t2Supplier.get(); T3 t3 = t3Supplier.get(); T4 t4 = t4Supplier.get(); T5 t5 = t5Supplier.get(); T6 t6 = t6Supplier.get(); T7 t7 = t7Supplier.get(); T8 t8 = t8Supplier.get()) {
                    return f.apply(t1, t2, t3, t4, t5, t6, t7, t8);
                }
            });
        }
    }

    // -- exception wrappers

    /**
     * An unchecked wrapper for Fatal exceptions.
     * <p>
     * See {@link NonFatalException}.
     * @deprecated Will be removed in 3.0.0. Instead we throw sneaky.
     */
    @Deprecated
    final class FatalException extends RuntimeException implements Serializable {

        private static final long serialVersionUID = 1L;

        private FatalException(Throwable exception) {
            super(exception);
        }

        /**
         * Two Fatal exceptions are equal, if they have the same stack trace.
         *
         * @param o An object
         * @return true, if o equals this, false otherwise.
         */
        @Override
        public boolean equals(Object o) {
            return (o == this) || (o instanceof FatalException
                    && Arrays.deepEquals(getCause().getStackTrace(), ((FatalException) o).getCause().getStackTrace()));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getCause());
        }

        @Override
        public String toString() {
            return "Fatal(" + getCause() + ")";
        }
    }

    /**
     * An unchecked wrapper for non-fatal/recoverable exceptions. The underlying exception can
     * be accessed via {@link #getCause()}.
     * <p>
     * The following exceptions are considered to be fatal/non-recoverable:
     * <ul>
     * <li>{@linkplain InterruptedException}</li>
     * <li>{@linkplain LinkageError}</li>
     * <li>{@linkplain ThreadDeath}</li>
     * <li>{@linkplain VirtualMachineError} (i.e. {@linkplain OutOfMemoryError} or {@linkplain StackOverflowError})</li>
     * </ul>
     * @deprecated Will be removed in 3.0.0. Instead we throw sneaky.
     */
    @Deprecated
    final class NonFatalException extends RuntimeException implements Serializable {

        private static final long serialVersionUID = 1L;

        private NonFatalException(Throwable exception) {
            super(exception);
        }

        /**
         * Wraps the given exception in a {@code NonFatal} or throws an {@link Error} if the given exception is fatal.
         * <p>
         * Note: InterruptedException is not considered to be fatal. It should be handled explicitly but we cannot
         * throw it directly because it is not an Error. If we would wrap it in an Error, we couldn't handle it
         * directly. Therefore it is not thrown as fatal exception.
         *
         * @param exception A Throwable
         * @return A new {@code NonFatal} if the given exception is recoverable
         * @throws Error                if the given exception is fatal, i.e. not recoverable
         * @throws NullPointerException if exception is null
         */
        static NonFatalException of(Throwable exception) {
            Objects.requireNonNull(exception, "exception is null");
            if (exception instanceof NonFatalException) {
                return (NonFatalException) exception;
            } else if (exception instanceof FatalException) {
                throw (FatalException) exception;
            } else {
                final boolean isFatal = exception instanceof InterruptedException
                        || exception instanceof LinkageError
                        || exception instanceof ThreadDeath
                        || exception instanceof VirtualMachineError;
                if (isFatal) {
                    throw new FatalException(exception);
                } else {
                    return new NonFatalException(exception);
                }
            }
        }

        /**
         * Two NonFatal exceptions are equal, if they have the same stack trace.
         *
         * @param o An object
         * @return true, if o equals this, false otherwise.
         */
        @Override
        public boolean equals(Object o) {
            return (o == this) || (o instanceof NonFatalException
                    && Arrays.deepEquals(getCause().getStackTrace(), ((NonFatalException) o).getCause().getStackTrace()));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getCause());
        }

        @Override
        public String toString() {
            return "NonFatal(" + getCause() + ")";
        }
    }
}
