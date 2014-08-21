/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import java.io.Serializable;

import javaslang.Tuples.Tuple;

/**
 * Interface for object decomposition, simmilar to Scala's {@code unapply()}. A Tuple serves as container type for the
 * parts, an object is composed of.
 *
 * @param <T> Type of object to be decomposed.
 * @param <R> Container type for the parts an object is composed of.
 */
// DEV-NOTE: extending Decomposable reduces interface Pattern by 50%
//DEV-NOTE: Serializable & @FunctionalInterface to be compatible with {@link javaslang.Lambdas#getLambdaSignature(Serializable)}
@FunctionalInterface
public interface Decomposition<T, R extends Tuple> extends Decomposable<T, R>, Serializable {

	/**
	 * Performs the decomposition.
	 * 
	 * @param t The object to be decomposed.
	 * @return The tuple containing the parts of the given object t.
	 */
	R apply(T t);

	/**
	 * Returning this.
	 * 
	 * @return A Decomposition for object of type T.
	 */
	@Override
	default Decomposition<T, R> decomposition() {
		return this;
	}
}
