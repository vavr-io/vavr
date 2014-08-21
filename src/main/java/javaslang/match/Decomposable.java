/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import java.io.Serializable;

import javaslang.Tuples.Tuple;

/**
 * An object which implements this interface signals that it is decomposable, i.e. can be decomposed to the parts it is
 * composed of.
 *
 * @param <T> Type of object to be decomposed.
 * @param <R> Container type for the parts an object is composed of.
 */
// DEV-NOTE: Serializable & @FunctionalInterface to be compatible with {@link javaslang.Lambdas#getLambdaSignature(Serializable)} 
@FunctionalInterface
public interface Decomposable<T, R extends Tuple> extends Serializable {

	/**
	 * Returns a {@link Decomposition} that is able to decompose objects of type T.
	 * 
	 * @return A Decomposition for object of type T.
	 */
	Decomposition<T, R> decomposition();
}
