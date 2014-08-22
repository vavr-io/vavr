/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * A function with one argument which implements Serializable in order to obtain runtime type information about the
 * lambda via {@link javaslang.lambda.Lambdas#getLambdaSignature(Serializable)}.
 *
 * @param <T> The parameter type of the function.
 * @param <R> The return type of the function.
 */
@FunctionalInterface
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {

	static <T, U, R> SerializableFunction<T, R> of(Function<T, R> f) {
		return t -> f.apply(t);
	}
}
