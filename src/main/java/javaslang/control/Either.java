/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;

import javaslang.Tuple;
import javaslang.Tuple.*;
import javaslang.ValueObject;
import javaslang.control.Option.None;
import javaslang.control.Option.Some;
import javaslang.control.Valences.Bivalent;

/**
 * Either represents a value of two possible types. An Either is either a {@link javaslang.control.Either.Left} or a
 * {@link javaslang.control.Either.Right}.
 * <p>
 * It is possible to project an Either to a Left or a Right. Both cases can be further processed with control operations
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
public interface Either<L, R> extends ValueObject {

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

	static final class Left<L, R> implements Either<L, R> {

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
		public Tuple1<L> unapply() {
			return Tuple.of(left);
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

	static final class Right<L, R> implements Either<L, R> {

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
		public Tuple1<R> unapply() {
			return Tuple.of(right);
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

	static final class LeftProjection<L, R> implements Bivalent<L, R> {

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

		@Override
		public L orElse(L other) {
			return either.isLeft() ? asLeft() : other;
		}

		@Override
		public L orElseGet(Function<? super R, ? extends L> other) {
			if (either.isLeft()) {
				return asLeft();
			} else {
				return other.apply(asRight());
			}
		}

		@Override
		public void orElseRun(Consumer<? super R> action) {
			if (either.isRight()) {
				action.accept(asRight());
			}
		}

		@Override
		public <X extends Throwable> L orElseThrow(Function<? super R, X> exceptionFunction) throws X {
			if (either.isLeft()) {
				return asLeft();
			} else {
				throw exceptionFunction.apply(asRight());
			}
		}

		@Override
		public Option<L> toOption() {
			if (either.isLeft()) {
				return new Some<>(asLeft());
			} else {
				return None.instance();
			}
		}

		@Override
		public Either<L, R> toEither() {
			return either;
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

	static final class RightProjection<L, R> implements Bivalent<R, L> {

		private final Either<L, R> either;

		RightProjection(Either<L, R> either) {
			this.either = either;
		}

		@Override
		public R get() {
			if (either.isRight()) {
				return asRight();
			} else {
				throw new NoSuchElementException("Either.right().get() on Left");
			}
		}

		@Override
		public R orElse(R other) {
			return either.isRight() ? asRight() : other;
		}

		@Override
		public R orElseGet(Function<? super L, ? extends R> other) {
			if (either.isRight()) {
				return asRight();
			} else {
				return other.apply(asLeft());
			}
		}

		@Override
		public void orElseRun(Consumer<? super L> action) {
			if (either.isLeft()) {
				action.accept(asLeft());
			}
		}

		@Override
		public <X extends Throwable> R orElseThrow(Function<? super L, X> exceptionFunction) throws X {
			if (either.isRight()) {
				return asRight();
			} else {
				throw exceptionFunction.apply(asLeft());
			}
		}

		@Override
		public Option<R> toOption() {
			if (either.isRight()) {
				return new Some<>(asRight());
			} else {
				return None.instance();
			}
		}

		@Override
		public Either<L, R> toEither() {
			return either;
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
