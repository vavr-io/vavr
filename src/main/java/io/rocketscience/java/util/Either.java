package io.rocketscience.java.util;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Either<L, R> {

	default LeftProjection<L, R> left() {
		return new LeftProjection<L, R>(this);
	}

	default RightProjection<L, R> right() {
		return new RightProjection<L, R>(this);
	}

	boolean isLeft();

	default boolean isRight() {
		return !isLeft();
	}
	
	static <L, R> Left<L, R> leftOf(L left) {
		return new Left<L,R>(left);
	}

	static <L, R> Right<L, R> rightOf(R right) {
		return new Right<L,R>(right);
	}
	
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

		public <X extends Throwable> L orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
			if (either.isLeft()) {
				return asLeft();
			} else {
				throw exceptionSupplier.get();
			}
		}
		
		public Optional<Either<L, R>> filter(Predicate<? super L> predicate) {
			Objects.requireNonNull(predicate);
			if (either.isLeft() && predicate.test(asLeft())) {
				return Optional.of(either);
			} else {
				return Optional.empty();
			}
		}

		public <U> Either<U, R> flatMap(Function<? super L, Either<U, R>> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isLeft())
				return mapper.apply(asLeft());
			else {
				return new Right<U, R>(asRight());
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
	            return new Left<U,R>(mapper.apply(asLeft()));
	        else {
	            return new Right<U,R>(asRight());
	        }
	    }

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
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
			return String.format("LeftProjection[%s]", either);
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

		public <X extends Throwable> R orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
			if (either.isRight()) {
				return asRight();
			} else {
				throw exceptionSupplier.get();
			}
		}

		public Optional<Either<L, R>> filter(Predicate<? super R> predicate) {
			Objects.requireNonNull(predicate);
			if (either.isRight() && predicate.test(asRight())) {
				return Optional.of(either);
			} else {
				return Optional.empty();
			}
		}

		public <U> Either<L, U> flatMap(Function<? super R, Either<L, U>> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isRight())
				return mapper.apply(asRight());
			else {
				return new Left<L, U>(asLeft());
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
				return new Right<L, U>(mapper.apply(asRight()));
			else {
				return new Left<L, U>(asLeft());
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
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
			return String.format("RightProjection[%s]", either);
		}

		private L asLeft() {
			return ((Left<L, R>) either).left;
		}

		private R asRight() {
			return ((Right<L, R>) either).right;
		}
	}

}
