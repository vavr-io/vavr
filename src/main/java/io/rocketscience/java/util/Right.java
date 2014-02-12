package io.rocketscience.java.util;

import java.util.Objects;

public class Right<L, R> implements Either<L, R> {

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
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Right)) {
			return false;
		}
		final Right<?, ?> other = (Right<?, ?>) obj;
		return Objects.equals(right, other.right);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(right);
	}

	@Override
	public String toString() {
		return String.format("Right[%s]", right);
	}

}
