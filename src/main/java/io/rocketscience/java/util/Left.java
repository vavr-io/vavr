package io.rocketscience.java.util;

import java.util.Objects;

public class Left<L, R> implements Either<L, R> {

	final L left;

	public Left(L left) {
		this.left = left;
	}

	@Override
	public boolean isLeft() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Left)) {
			return false;
		}
		final Left<?, ?> other = (Left<?, ?>) obj;
		return Objects.equals(left, other.left);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(left);
	}

	@Override
	public String toString() {
		return String.format("Left[%s]", left);
	}

}
