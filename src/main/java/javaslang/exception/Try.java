/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.exception;

import static javaslang.Requirements.requireNonNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javaslang.option.Option;

/**
 * An implementation similar to Scala's Try monad.
 *
 * @param <T> Value type in the case of success.
 */
public interface Try<T> {

	static <T> Try<T> of(Try.CheckedSupplier<T> supplier) {
		try {
			return new Success<>(supplier.get());
		} catch (Throwable t) {
			return new Failure<>(t);
		}
	}

	boolean isFailure();

	boolean isSuccess();

	T get() throws NonFatal;

	T orElse(T other);

	T orElseGet(Function<Throwable, ? extends T> other);

	<X extends Throwable> T orElseThrow(Function<Throwable, ? extends X> exceptionProvider)
			throws X;

	Try<T> recover(Function<? super Throwable, ? extends T> f);

	Try<T> recoverWith(Function<? super Throwable, Try<T>> f);

	Option<T> toOption();

	Try<T> filter(Predicate<? super T> predicate);

	<U> Try<U> flatMap(Function<? super T, Try<U>> mapper);

	void forEach(Consumer<? super T> action);

	<U> Try<U> map(Function<? super T, ? extends U> mapper);

	Try<Throwable> failed();

	/**
	 * Used to initialize a Try calling {@link Try#of(Try.CheckedSupplier)}.
	 *
	 * @param <T> Type of supplied object.
	 */
	@FunctionalInterface
	static interface CheckedSupplier<T> {

		/**
		 * Gets a result or throws a Throwable.
		 *
		 * @return a result
		 * @throws java.lang.Throwable if an error occurs.
		 */
		T get() throws Throwable;

	}

	/**
	 * Causes wrap Throwables. They are unchecked, i.e. RuntimeExceptions, which are either fatal
	 * (represented by the subclass {@link Fatal}) or non-fatal (represented by the subclass
	 * {@link NonFatal}). Fatal causes are considered to be non-recoverable.
	 * <p>
	 * Use {@link Cause#of(Throwable)} to get an instance of Cause. The instance returned is either
	 * of type {@link Fatal} or {@link NonFatal}.
	 * <p>
	 * Use {@link #get()}, which is a convenient method and essentially the same as
	 * {@link #getCause()}, to get the wrapped Throwable. {@link #isFatal()} states, if this Cause
	 * is considered to be non-recoverable.
	 */
	static abstract class Cause extends RuntimeException {

		private static final long serialVersionUID = 1L;

		Cause(Throwable cause) {
			super(cause);
		}

		/**
		 * Convenience method, returns the Throwable of this Cause which is considered either as
		 * fatal or non-fatal.
		 * 
		 * @return The Throwable of this Cause.
		 */
		public Throwable get() {
			return getCause();
		}

		/**
		 * Returns true, if this is Fatal, i.e. if the cause is fatal. See
		 * {@link Cause#of(Throwable)} for the definition on when a Throwable is fatal.
		 * 
		 * @return true, if this instance is Fatal, false otherwise.
		 */
		public abstract boolean isFatal();

		/**
		 * Wraps t in a Cause which is either a {@link Fatal} or a {@link NonFatal}. The given
		 * Throwable t is wrapped in a Fatal, i.e. considered as a non-recoverable, if t is an
		 * instance of one of the following classes:
		 * 
		 * <ul>
		 * <li>InterruptedException</li>
		 * <li>LinkageError</li>
		 * <li>ThreadDeath</li>
		 * <li>VirtualMachineError (i.e. OutOfMemoryError)</li>
		 * </ul>
		 * 
		 * However, StackOverflowError is considered as a non-fatal.
		 * 
		 * @param t A Throwable
		 * @return A {@link Fatal}, if t is fatal, a {@link NonFatal} otherwise.
		 * @throws IllegalStateException if t is null.
		 */
		public static Cause of(Throwable t) {
			requireNonNull(t, "Throwable is null");
			final boolean isFatal = (t instanceof VirtualMachineError && !(t instanceof StackOverflowError))//
					|| t instanceof ThreadDeath//
					|| t instanceof InterruptedException//
					|| t instanceof LinkageError;
			return isFatal ? new Fatal(t) : new NonFatal(t);
		}

	}

	/**
	 * Use {@link Cause#of(Throwable)} to create instances of {@link Fatal} and {@link NonFatal}.
	 */
	static final class Fatal extends Cause {

		private static final long serialVersionUID = 1L;

		Fatal(Throwable cause) {
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
	static final class NonFatal extends Cause {

		private static final long serialVersionUID = 1L;

		NonFatal(Throwable cause) {
			super(cause);
		}

		@Override
		public boolean isFatal() {
			return false;
		}

	}

}
