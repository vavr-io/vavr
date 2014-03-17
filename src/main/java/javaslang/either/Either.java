package javaslang.either;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javaslang.option.None;
import javaslang.option.Option;
import javaslang.option.Some;

public interface Either<L, R> {

	default LeftProjection<L, R> left() {
		return new LeftProjection<>(this);
	}

	default RightProjection<L, R> right() {
		return new RightProjection<>(this);
	}

	boolean isLeft();

	boolean isRight();
	
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

		public Option<Either<L, R>> filter(Predicate<? super L> predicate) {
			Objects.requireNonNull(predicate);
			if (either.isLeft() && predicate.test(asLeft())) {
				return new Some<>(either);
			} else {
				return None.instance();
			}
		}

		public <U> Either<U, R> flatMap(Function<? super L, Either<U, R>> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isLeft())
				return mapper.apply(asLeft());
			else {
				return new Right<>(asRight());
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

		public Option<Either<L, R>> filter(Predicate<? super R> predicate) {
			Objects.requireNonNull(predicate);
			if (either.isRight() && predicate.test(asRight())) {
				return new Some<>(either);
			} else {
				return None.instance();
			}
		}

		public <U> Either<L, U> flatMap(Function<? super R, Either<L, U>> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isRight())
				return mapper.apply(asRight());
			else {
				return new Left<>(asLeft());
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
