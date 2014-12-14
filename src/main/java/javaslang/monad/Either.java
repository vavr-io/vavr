/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.monad;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javaslang.Algebra;
import javaslang.Manifest;
import javaslang.monad.Option.None;
import javaslang.monad.Option.Some;

/**
 * Either represents a value of two possible types. An Either is either a {@link javaslang.monad.Either.Left} or a
 * {@link javaslang.monad.Either.Right}.
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
// DEV-NOTE: Either is no Monad and no Functor in the sense of javaslang.Algebra.*
public interface Either<L, R> {

	boolean isLeft();

	boolean isRight();

	default LeftProjection<L, R> left() {
		return new LeftProjection<>(this);
	}

	default RightProjection<L, R> right() {
		return new RightProjection<>(this);
	}

	// -- Object.*

	@Override
	boolean equals(Object o);

	@Override
	int hashCode();

	@Override
	String toString();

	// -- Either implementations

	static final class Left<L, R> implements Either<L, R>, Serializable {

		private static final long serialVersionUID = 3297057402720487673L;

		final L left;

		public Left(L left) {
			this.left = left;
		}

		@Override
		public boolean isLeft() {
			return true;
		}

		@Override
		public boolean isRight() {
			return false;
		}

		@Override
		public boolean equals(Object obj) {
			return (obj == this) || (obj instanceof Left && Objects.equals(left, ((Left<?, ?>) obj).left));
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(left);
		}

		@Override
		public String toString() {
			return String.format("Left(%s)", left);
		}
	}

	static final class Right<L, R> implements Either<L, R>, Serializable {

		private static final long serialVersionUID = 6037923230455552437L;

		final R right;

		public Right(R right) {
			this.right = right;
		}

		@Override
		public boolean isLeft() {
			return false;
		}

		@Override
		public boolean isRight() {
			return true;
		}

		@Override
		public boolean equals(Object obj) {
			return (obj == this) || (obj instanceof Right && Objects.equals(right, ((Right<?, ?>) obj).right));
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(right);
		}

		@Override
		public String toString() {
			return String.format("Right(%s)", right);
		}
	}

	// -- Left/Right projections

	static final class LeftProjection<L, R> {

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

		@SuppressWarnings("unchecked")
		public <U> Either<U, R> map(Function<? super L, ? extends U> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isLeft())
				return new Left<>(mapper.apply(asLeft()));
			else {
				return (Either<U,R>) either;
			}
		}

		@SuppressWarnings("unchecked")
		public <U> Either<U, R> flatMap(Function<? super L, ? extends Either<U, R>> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isLeft()) {
				return mapper.apply(asLeft());
			} else {
				return (Either<U,R>) either;
			}
		}

		@Override
		public boolean equals(Object obj) {
			return (obj == this)|| (obj instanceof LeftProjection && Objects.equals(either, ((LeftProjection<?, ?>) obj).either));
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

	static final class RightProjection<L, R> {

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

		@SuppressWarnings("unchecked")
		public <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isRight())
				return new Right<>(mapper.apply(asRight()));
			else {
				return (Either<L, U>) either;
			}
		}

		@SuppressWarnings("unchecked")
		public <U> Either<L, U> flatMap(Function<? super R, ? extends Either<L, U>> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isRight())
				return mapper.apply(asRight());
			else {
				return (Either<L, U>) either;
			}
		}

		@Override
		public boolean equals(Object obj) {
			return (obj == this)|| (obj instanceof RightProjection && Objects.equals(either, ((RightProjection<?, ?>) obj).either));
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
