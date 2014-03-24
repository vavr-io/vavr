/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
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
