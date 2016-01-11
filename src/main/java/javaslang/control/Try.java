/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.CheckedFunction1;
import javaslang.Value;
import javaslang.algebra.Monad;
import javaslang.collection.Iterator;
import javaslang.collection.List;
import javaslang.collection.Seq;

import java.io.Serializable;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
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
public interface Try<T> extends Monad<T>, Value<T> {

    /**
     * Creates a Try of a CheckedSupplier.
     *
     * @param supplier A checked supplier
     * @param <T>      Component type
     * @return {@code Success(supplier.get())} if no exception occurs, otherwise {@code Failure(throwable)} if an
     * exception occurs calling {@code supplier.get()}.
     */
    static <T> Try<T> of(CheckedSupplier<? extends T> supplier) {
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
     * Reduces many {@code Try}s into a single {@code Try} by transforming an
     * {@code Iterable<Try<? extends T>>} into a {@code Try<Seq<T>>}. If any of
     * the {@code Try}s are {@link Try.Failure}, then this returns a {@link Try.Failure}.
     *
     * @param values An {@link Iterable} of {@code Try}s
     * @param <T> type of the Trys
     * @return A {@code Try} of a {@link Seq} of results
     * @throws NullPointerException if {@code values} is null
     */
    static <T> Try<Seq<T>> sequence(Iterable<? extends Try<? extends T>> values) {
        Objects.requireNonNull(values, "values is null");
        List<T> list = List.empty();
        for (Try<? extends T> value : values) {
            if(value.isFailure()) {
                return Try.failure(value.getCause());
            }
            list = list.prepend(value.get());
        }

        return Try.success(list.reverse());
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
     * Returns {@code this} if this is a Failure or this is a Success and the value satisfies the predicate.
     * <p>
     * Returns a new Failure, if this is a Success and the value does not satisfy the Predicate or an exception
     * occurs testing the predicate.
     *
     * @param predicate A predicate
     * @return a new Try
     */
    @Override
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

    @Override
    default Try<T> filterNot(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }

    default Try<T> filterTry(CheckedPredicate<? super T> predicate) {
        if (isFailure()) {
            return this;
        } else {
            return Try.of(() -> predicate.test(get())).flatMap(b -> filter(ignored -> b));
        }
    }

    default Try<T> filterNotTry(CheckedPredicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filterTry(predicate.negate());
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
    default <U> Try<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        if (isFailure()) {
            return (Failure<U>) this;
        } else {
            return flatMapTry((CheckedFunction<T, Iterable<? extends U>>) mapper::apply);
        }
    }

    @SuppressWarnings("unchecked")
    default <U> Try<U> flatMapTry(CheckedFunction<? super T, ? extends Iterable<? extends U>> mapper) {
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
     * A try is a singleton type.
     *
     * @return {@code true}
     */
    @Override
    default boolean isSingletonType() {
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
     * Maps the value of a Success or returns a Failure.
     *
     * @param <U>    The new component type
     * @param mapper A mapper
     * @return a new Try
     */
    @Override
    default <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        return mapTry(mapper::apply);
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

    @Override
    default Match.MatchMonad.Of<Try<T>> match() {
        return Match.of(this);
    }

    /**
     * Consumes the throwable if this is a Failure.
     *
     * @param action An exception consumer
     * @return a new Failure, if this is a Failure and the consumer throws, otherwise this, which may be a Success or
     * a Failure.
     */
    default Try<T> onFailure(Consumer<? super Throwable> action) {
        if (isFailure()) {
            try {
                action.accept(getCause());
                return this;
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        } else {
            return this;
        }
    }

    /**
     * Consumes the value if this is a Success.
     *
     * @param action A value consumer
     * @return a new Failure, if this is a Success and the consumer throws, otherwise this, which may be a Success or
     * a Failure.
     */
    default Try<T> onSuccess(Consumer<? super T> action) {
        if (isSuccess()) {
            try {
                action.accept(get());
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
            return other.apply(getCause());
        } else {
            return get();
        }
    }

    default void orElseRun(Consumer<? super Throwable> action) {
        if (isFailure()) {
            action.accept(getCause());
        }
    }

    default <X extends Throwable> T orElseThrow(Function<? super Throwable, X> exceptionProvider) throws X {
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
    default Try<T> recover(Function<? super Throwable, ? extends T> f) {
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
     * @return a new Try
     */
    @SuppressWarnings("unchecked")
    default Try<T> recoverWith(Function<? super Throwable, ? extends Try<? extends T>> f) {
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
    default <U> U transform(Function<? super Try<? super T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
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

    /**
     * An unchecked wrapper for Fatal exceptions.
     * <p>
     * See {@link NonFatalException}.
     */
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
     */
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
