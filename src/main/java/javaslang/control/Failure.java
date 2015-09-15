/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * A failed Try.
 *
 * @param <T> component type of this Failure
 * @author Daniel Dietrich
 * @since 1.0.0
 */
public final class Failure<T> implements Try<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private final NonFatal cause;

    /**
     * Constructs a Failure.
     *
     * @param exception A cause of type Throwable, may not be null.
     * @throws NullPointerException if exception is null
     * @throws Error                if the given exception if fatal, i.e. non-recoverable
     */
    public Failure(Throwable exception) {
        Objects.requireNonNull(exception, "exception is null");
        cause = NonFatal.of(exception);
    }

    // Throws NonFatal instead of Throwable because it is a RuntimeException which does not need to be checked.
    @Override
    public T get() throws NonFatal {
        throw cause;
    }

    @Override
    public NonFatal getCause() {
        return cause;
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
    public int hashCode() {
        return Objects.hashCode(cause.getCause());
    }

    @Override
    public String toString() {
        return "Failure(" + cause.getCause() + ")";
    }

    /**
     * An unchecked wrapper for non-fatal/recoverable exceptions. The underlying exception can
     * be accessed via {@link #getCause()}.
     * <p>
     * The following exceptions are considered to be fatal/non-recoverable:
     * <ul>
     * <li>{@linkplain LinkageError}</li>
     * <li>{@linkplain ThreadDeath}</li>
     * <li>{@linkplain VirtualMachineError} (i.e. {@linkplain OutOfMemoryError} or {@linkplain StackOverflowError})</li>
     * </ul>
     */
    public static final class NonFatal extends RuntimeException implements Serializable {

        private static final long serialVersionUID = 1L;

        private NonFatal(Throwable exception) {
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
         * @throws Error if the given exception is fatal, i.e. not recoverable
         * @throws NullPointerException if exception is null
         */
        static NonFatal of(Throwable exception) {
            Objects.requireNonNull(exception, "exception is null");
            if (exception instanceof NonFatal) {
                return (NonFatal) exception;
            }
            final boolean isFatal = exception instanceof VirtualMachineError
                    || exception instanceof ThreadDeath
                    || exception instanceof LinkageError;
            if (isFatal) {
                throw (Error) exception;
            } else {
                return new NonFatal(exception);
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
            return (o == this) || (o instanceof NonFatal
                    && Arrays.deepEquals(getCause().getStackTrace(), ((NonFatal) o).getCause().getStackTrace()));
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
