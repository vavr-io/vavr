/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.monad;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Either represents a value of two possible types. An Either is either a {@link javaslang.monad.Left} or a
 * {@link javaslang.monad.Right}.
 * <p>
 * It is possible to project an Either to a Left or a Right. Both cases can be further processed with monad operations
 * map, flatMap, filter.
 * <p>
 * If the given Either is a Right and projected to a Left, the Left operations have no effect on the Right value.<br>
 * If the given Either is a Left and projected to a Right, the Right operations have no effect on the Left value.<br>
 * If a Left is projected to a Left or a Right is projected to a Right, the operations have an effect.
 * <p>
 * <strong>Example:</strong> A compute() function, which results either in an Integer value (in the case of success) or
 * in an error message of type String (in the case of failure). By convention the success case is Right and the failure
 * is Left.
 * 
 * <pre>
 * <code>
 * Either&lt;String,Integer&gt; value = compute().right().map(i -&gt; i * 2);
 * </code>
 * </pre>
 * 
 * If the result of compute() is Right(1), the value is Right(2).<br>
 * If the result of compute() is Left("error), the value is Left("error").
 *
 * @param <L> The type of a Left value of the Either.
 * @param <R> The type of a Right value of the Either.
 */
public interface Either<L, R> {

	boolean isLeft();

	boolean isRight();

	default LeftProjection<L, R> left() {
		return new LeftProjection<>(this);
	}

	default RightProjection<L, R> right() {
		return new RightProjection<>(this);
	}

	// -- delegate to RightProjection by default

	default R get() {
		return right().get();
	}

	default R orElse(R other) {
		return right().orElse(other);
	}

	default R orElseGet(Supplier<? extends R> other) {
		return right().orElseGet(other);
	}

	default <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
		return right().orElseThrow(exceptionSupplier);
	}

	default <X extends Throwable> R orElseThrow(Function<L, X> exceptionFunction) throws X {
		return right().orElseThrow(exceptionFunction);
	}

	default Option<Either<L, R>> filter(Predicate<? super R> predicate) {
		return right().filter(predicate);
	}

	default void forEach(Consumer<? super R> action) {
		right().forEach(action);
	}

	default <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
		return right().map(mapper);
	}

	default <U> Either<L, U> flatMap(Function<? super R, ? extends Either<L, U>> mapper) {
		return right().flatMap(mapper);
	}

	// -- Object.*

	@Override
	boolean equals(Object o);

	@Override
	int hashCode();

	@Override
	String toString();

	// -- projections

	static class LeftProjection<L, R> {

		private final Either<L, R> either;

		LeftProjection(Either<L, R> either) {
			this.either = either;
		}

		public L get() {
			if (either.isLeft()) {
				return asLeft();
			} else {
				throw new NoSuchElementException("Either.left().get() on Right");
			}
		}

		public L orElse(L other) {
			return either.isLeft() ? asLeft() : other;
		}

		public L orElseGet(Supplier<? extends L> other) {
			return either.isLeft() ? asLeft() : other.get();
		}

		public <X extends Throwable> L orElseThrow(Supplier<X> exceptionSupplier) throws X {
			if (either.isLeft()) {
				return asLeft();
			} else {
				throw exceptionSupplier.get();
			}
		}

		public <X extends Throwable> L orElseThrow(Function<R, X> exceptionFunction) throws X {
			if (either.isLeft()) {
				return asLeft();
			} else {
				throw exceptionFunction.apply(asRight());
			}
		}

		public Option<Either<L, R>> filter(Predicate<? super L> predicate) {
			Objects.requireNonNull(predicate);
			if (either.isLeft() && predicate.test(asLeft())) {
				return new Some<>(either);
			} else {
				return None.instance();
			}
		}

		public void forEach(Consumer<? super L> action) {
			Objects.requireNonNull(action);
			if (either.isLeft()) {
				action.accept(asLeft());
			}
		}

		public <U> Either<U, R> map(Function<? super L, ? extends U> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isLeft())
				return new Left<>(mapper.apply(asLeft()));
			else {
				return new Right<>(asRight());
			}
		}

		public <U> Either<U, R> flatMap(Function<? super L, ? extends Either<U, R>> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isLeft()) {
				return mapper.apply(asLeft());
			} else {
				return new Right<>(asRight());
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof LeftProjection)) {
				return false;
			}
			final LeftProjection<?, ?> other = (LeftProjection<?, ?>) obj;
			return Objects.equals(either, other.either);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(either);
		}

		@Override
		public String toString() {
			return String.format("LeftProjection(%s)", either);
		}

		private L asLeft() {
			return ((Left<L, R>) either).left;
		}

		private R asRight() {
			return ((Right<L, R>) either).right;
		}
	}

	static class RightProjection<L, R> {

		private final Either<L, R> either;

		RightProjection(Either<L, R> either) {
			this.either = either;
		}

		public R get() {
			if (either.isRight()) {
				return asRight();
			} else {
				throw new NoSuchElementException("Either.right().get() on Left");
			}
		}

		public R orElse(R other) {
			return either.isRight() ? asRight() : other;
		}

		public R orElseGet(Supplier<? extends R> other) {
			return either.isRight() ? asRight() : other.get();
		}

		public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
			if (either.isRight()) {
				return asRight();
			} else {
				throw exceptionSupplier.get();
			}
		}

		public <X extends Throwable> R orElseThrow(Function<L, X> exceptionFunction) throws X {
			if (either.isRight()) {
				return asRight();
			} else {
				throw exceptionFunction.apply(asLeft());
			}
		}

		public Option<Either<L, R>> filter(Predicate<? super R> predicate) {
			Objects.requireNonNull(predicate);
			if (either.isRight() && predicate.test(asRight())) {
				return new Some<>(either);
			} else {
				return None.instance();
			}
		}

		public void forEach(Consumer<? super R> action) {
			Objects.requireNonNull(action);
			if (either.isRight()) {
				action.accept(asRight());
			}
		}

		public <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isRight())
				return new Right<>(mapper.apply(asRight()));
			else {
				return new Left<>(asLeft());
			}
		}

		public <U> Either<L, U> flatMap(Function<? super R, ? extends Either<L, U>> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isRight())
				return mapper.apply(asRight());
			else {
				return new Left<>(asLeft());
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof RightProjection)) {
				return false;
			}
			final RightProjection<?, ?> other = (RightProjection<?, ?>) obj;
			return Objects.equals(either, other.either);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(either);
		}

		@Override
		public String toString() {
			return String.format("RightProjection(%s)", either);
		}

		private L asLeft() {
			return ((Left<L, R>) either).left;
		}

		private R asRight() {
			return ((Right<L, R>) either).right;
		}
	}
}
