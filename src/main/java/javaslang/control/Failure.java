/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.CheckedFunction1;
import javaslang.Value;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A failed Try.
 *
 * @param <T> component type of this Failure
 * @since 1.0.0
 */
public final class Failure<T> implements Try<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private final NonFatal cause;

    /**
     * Constructs a Failure.
     *
     * @param t A cause of type Throwable, may not be null.
     * @throws NullPointerException if t is null
     */
    public Failure(Throwable t) {
        Objects.requireNonNull(t, "Throwable is null");
        final Cause cause = Cause.of(t);
        if (cause.isFatal()) {
            throw cause;
        } else {
            this.cause = (NonFatal) cause;
        }
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

    // Throws NonFatal instead of Throwable because it is a RuntimeException which does not need to be checked.
    @Override
    public T get() throws NonFatal {
        throw cause;
    }

    @Override
    public T orElse(T other) {
        return other;
    }

    @Override
    public T orElseGet(Function<? super Throwable, ? extends T> other) {
        return other.apply(cause.getCause());
    }

    @Override
    public void orElseRun(Consumer<? super Throwable> action) {
        action.accept(cause.getCause());
    }

    @Override
    public <X extends Throwable> T orElseThrow(Function<? super Throwable, X> exceptionProvider) throws X {
        throw exceptionProvider.apply(cause.getCause());
    }

    @Override
    public Try<T> recover(Function<Throwable, ? extends T> f) {
        return Try.of(() -> f.apply(cause.getCause()));
    }

    @Override
    public Try<T> recoverWith(Function<Throwable, Try<T>> f) {
        try {
            return f.apply(cause.getCause());
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Override
    public Failure<T> onFailure(Consumer<Throwable> f) {
        try {
            f.accept(cause.getCause());
            return this;
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Override
    public None<T> toOption() {
        return None.instance();
    }

    @Override
    public Optional<T> toJavaOptional() {
        return Optional.empty();
    }

    @Override
    public Left<Throwable, T> toEither() {
        return new Left<>(cause.getCause());
    }

    @Override
    public Success<Throwable> failed() {
        return new Success<>(cause.getCause());
    }

    @Override
    public Failure<T> filter(Predicate<? super T> predicate) {
        return this;
    }

    @Override
    public Failure<T> filterTry(CheckedPredicate<? super T> predicate) {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Failure<U> flatMap(Function<? super T, ? extends Value<? extends U>> mapper) {
        return (Failure<U>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Failure<U> flatMapTry(CheckedFunction<? super T, ? extends Value<? extends U>> mapper) {
        return (Failure<U>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Failure<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper) {
        return (Failure<U>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Failure<Object> flatten() {
        return (Failure<Object>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Failure<U> map(Function<? super T, ? extends U> mapper) {
        return (Failure<U>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Failure<U> mapTry(CheckedFunction1<? super T, ? extends U> f) {
        return (Failure<U>) this;
    }

    @Override
    public Failure<T> peek(Consumer<? super T> action) {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Failure<T> andThen(CheckedConsumer<? super T> f) {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof Failure && equals(cause.getCause(), ((Failure<?>) obj).cause.getCause()));
    }

    // DEV-NOTE: no cycle-detection, intentionally
    private boolean equals(Throwable thisCause, Throwable thatCause) {
        return (thisCause == thatCause) || (thisCause != null && thatCause != null
                && Objects.equals(thisCause.getClass(), thatCause.getClass())
                && Objects.equals(thisCause.getMessage(), thatCause.getMessage())
                && equals(thisCause.getCause(), thatCause.getCause()));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cause.getCause());
    }

    @Override
    public String toString() {
        return "Failure(" + cause.getCause() + ")";
    }

    /**
     * Causes wrap Throwables. They are unchecked, i.e. RuntimeExceptions, which are either fatal (represented by
     * the subclass {@link Fatal}) or non-fatal (represented by the subclass {@link NonFatal}). Fatal causes are
     * considered to be non-recoverable.
     * <p>
     * Use {@link Cause#of(Throwable)} to get an instance of Cause. The instance returned is either of type
     * {@link Fatal} or {@link NonFatal}.
     * <p>
     * {@link #isFatal()} states, if this Cause is considered to be non-recoverable.
     */
    public static abstract class Cause extends RuntimeException implements Serializable {

        private static final long serialVersionUID = 1L;

        // DEV-NOTE: need to be protected because of serialization
        protected Cause(Throwable cause) {
            super(cause);
        }

        /**
         * Returns true, if this is Fatal, i.e. if the cause is fatal. See {@link Cause#of(Throwable)} for the
         * definition on when a Throwable is fatal.
         *
         * @return true, if this instance is Fatal, false otherwise.
         */
        public abstract boolean isFatal();

        /**
         * Wraps t in a Cause which is either a {@link Fatal} or a {@link NonFatal}. The given Throwable t is
         * wrapped in a Fatal, i.e. considered as a non-recoverable, if t is an instance of one of the following
         * classes:
         *
         * <ul>
         * <li>InterruptedException</li>
         * <li>LinkageError</li>
         * <li>ThreadDeath</li>
         * <li>VirtualMachineError (i.e. OutOfMemoryError or StackOverflowError)</li>
         * </ul>
         *
         * However, StackOverflowError is considered as a non-fatal.
         *
         * @param t A Throwable
         * @return A {@link Fatal}, if t is fatal, a {@link NonFatal} otherwise.
         * @throws java.lang.NullPointerException if t is null
         */
        public static Cause of(Throwable t) {
            Objects.requireNonNull(t, "Throwable is null");
            if (t instanceof Cause) {
                return (Cause) t;
            }
            final boolean isFatal = t instanceof VirtualMachineError
                    || t instanceof ThreadDeath
                    || t instanceof InterruptedException
                    || t instanceof LinkageError;
            return isFatal ? new Fatal(t) : new NonFatal(t);
        }
    }

    /**
     * Use {@link Cause#of(Throwable)} to create instances of {@link Fatal} and {@link NonFatal}.
     */
    public static final class Fatal extends Cause {

        private static final long serialVersionUID = 1L;

        /**
         * Use {@link Cause#of(Throwable)} to get an instance of this class.
         *
         * @param cause A cause
         */
        private Fatal(Throwable cause) {
            super(cause);
        }

        @Override
        public boolean isFatal() {
            return true;
        }
    }

    /**
     * Use {@link Cause#of(Throwable)} to create instances of {@link Fatal} and {@link NonFatal}.
     */
    public static final class NonFatal extends Cause {

        private static final long serialVersionUID = 1L;

        /**
         * Use {@link Cause#of(Throwable)} to get an instance of this class.
         *
         * @param cause A cause
         */
        private NonFatal(Throwable cause) {
            super(cause);
        }

        @Override
        public boolean isFatal() {
            return false;
        }
    }
}
