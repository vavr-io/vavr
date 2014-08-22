/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lambda;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * A predicate which implements Serializable in order to obtain runtime type information about the parameter type via
 * {@link javaslang.lambda.Lambdas#getLambdaSignature(Serializable)}.
 *
 * @param <T> The parameter type of the predicate.
 */
@FunctionalInterface
public interface SerializablePredicate<T> extends Predicate<T>, Serializable {

	static <T, U, R> SerializablePredicate<T> of(Predicate<T> f) {
		return t -> f.test(t);
	}
}
