/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.monad;

import static javaslang.Requirements.requireNonNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javaslang.Requirements.UnsatisfiedRequirementException;

public final class Failure<T> implements Try<T>, Serializable {

	private static final long serialVersionUID = 2836756728630414146L;

	private final NonFatal cause;

	/**
	 * Constructs a Failure.
	 * 
	 * @param t A cause of type Throwable, may not be null.
	 * @throws UnsatisfiedRequirementException if t is null.
	 */
	public Failure(Throwable t) {
		requireNonNull(t, "Throwable is null");
		final Cause cause = Cause.of(t);
		if (cause.isFatal()) {
			throw cause;
		} else {
			this.cause = (NonFatal) cause;
		}
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
	public T get() throws NonFatal {
		throw cause;
	}

	@Override
	public T orElse(T other) {
		return other;
	}

	@Override
	public T orElseGet(Function<Throwable, ? extends T> other) {
		return other.apply(cause.getCause());
	}

	@Override
	public <X extends Throwable> T orElseThrow(Function<Throwable, X> exceptionProvider) throws X {
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
	public Option<T> toOption() {
		return None.instance();
	}

	@Override
	public Try<Throwable> failed() {
		return new Success<>(cause.getCause());
	}

	@Override
	public Try<T> filter(Predicate<? super T> predicate) {
		return this;
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		// nothing to do
	}

	@Override
	public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
		@SuppressWarnings("unchecked")
		final Try<U> result = (Try<U>) this;
		return result;
	}

	@Override
	public <U> Try<U> flatMap(Function<? super T, ? extends Try<U>> mapper) {
		@SuppressWarnings("unchecked")
		final Try<U> result = (Try<U>) this;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Failure)) {
			return false;
		}
		final Failure<?> failure = (Failure<?>) obj;
		return equals(cause.getCause(), failure.cause.getCause());
	}

	// DEV-NOTE: no cycle-detection, intentionally
	private boolean equals(Throwable thisCause, Throwable thatCause) {
		return (thisCause == thatCause)
				|| (thisCause != null
						&& thatCause != null
						&& Objects.equals(thisCause.getClass(), thatCause.getClass())
						&& Objects.equals(thisCause.getMessage(), thatCause.getMessage()) && equals(
							thisCause.getCause(), thatCause.getCause()));
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(cause.getCause());
	}

	@Override
	public String toString() {
		return String.format("Failure(%s)", cause.getCause());
	}

	/**
	 * Causes wrap Throwables. They are unchecked, i.e. RuntimeExceptions, which are either fatal (represented by the
	 * subclass {@link Fatal}) or non-fatal (represented by the subclass {@link NonFatal}). Fatal causes are considered
	 * to be non-recoverable.
	 * <p>
	 * Use {@link Cause#of(Throwable)} to get an instance of Cause. The instance returned is either of type
	 * {@link Fatal} or {@link NonFatal}.
	 * <p>
	 * {@link #isFatal()} states, if this Cause is considered to be non-recoverable.
	 */
	public static abstract class Cause extends RuntimeException {

		private static final long serialVersionUID = 1905549717320100279L;

		Cause(Throwable cause) {
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
		 * Wraps t in a Cause which is either a {@link Fatal} or a {@link NonFatal}. The given Throwable t is wrapped in
		 * a Fatal, i.e. considered as a non-recoverable, if t is an instance of one of the following classes:
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
		 * @throws UnsatisfiedRequirementException if t is null.
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
	public static final class Fatal extends Cause implements Serializable {

		private static final long serialVersionUID = 7927552082244515502L;

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
	public static final class NonFatal extends Cause implements Serializable {

		private static final long serialVersionUID = -1643015386682564223L;

		NonFatal(Throwable cause) {
			super(cause);
		}

		@Override
		public boolean isFatal() {
			return false;
		}
	}
}
