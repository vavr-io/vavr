/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import javaslang.collection.Tuple;
import javaslang.lambda.Reflective;

/**
 * Interface for object decomposition, simmilar to Scala's {@code unapply()}. A Tuple serves as container type for the
 * parts, an object is composed of.
 *
 * @param <T> Type of objects to be decomposed.
 * @param <R> Container type for the parts an object is composed of.
 */
// DEV-NOTE: Serializable & @FunctionalInterface to be compatible with {@link javaslang.Lambdas#getLambdaSignature(Serializable)}
@FunctionalInterface
public interface Decomposition<T, R extends Tuple> extends Reflective {

	/**
	 * Performs the decomposition.
	 * 
	 * @param t The object to be decomposed.
	 * @return The tuple containing the parts of the given object t.
	 */
	R unapply(T t);
}
