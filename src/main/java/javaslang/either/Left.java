/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.either;

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
	public boolean isRight() {
		return false;
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
