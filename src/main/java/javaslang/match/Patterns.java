/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import java.lang.invoke.MethodType;
import java.util.function.Function;
import java.util.function.Predicate;

import javaslang.Tuples;
import javaslang.Tuples.Tuple1;
import javaslang.Tuples.Tuple2;
import javaslang.lambda.Lambdas;
import javaslang.lambda.SerializableFunction;
import javaslang.lambda.SerializablePredicate;

public final class Patterns {

	/**
	 * The Pattern which macthes any object (including null).
	 */
	public static final Pattern<Object, Tuple1<Object>, Tuple1<Object>> ANY = Pattern.of(o -> Tuples.of(o),
			Tuples.of(new Object() {
				@Override
				public boolean equals(Object o) {
					return true;
				}
			}));

	/**
	 * This class is not intended to be instantiated.
	 */
	private Patterns() {
		throw new AssertionError(Patterns.class.getName() + " is not intended to be instantiated.");
	}

	// -- patterns for functions

	public static <T> Pattern<Predicate<T>, Tuple1<Class<T>>, Tuple1<Class<T>>> Predicate(Class<T> paramType) {
		final Decomposition<Predicate<T>, Tuple1<Class<T>>> decomposition = predicate -> {
			final SerializablePredicate<T> lambda = t -> predicate.test(t);
			@SuppressWarnings("unchecked")
			final Class<T> currentParamType = (Class<T>) Lambdas.getLambdaSignature(lambda).get().parameterType(0);
			return Tuples.of(currentParamType);
		};
		return Pattern.of(decomposition, Tuples.of(paramType));
	}

	@SuppressWarnings("unchecked")
	public static <T, R> Pattern<Function<T, R>, Tuple2<Class<T>, Class<R>>, Tuple2<Class<T>, Class<R>>> Function(
			Class<T> paramType, Class<R> returnType) {
		final Decomposition<Function<T, R>, Tuple2<Class<T>, Class<R>>> decomposition = function -> {

			// TODO: BUG: lambda actually has signature (Function, Object) -> Object but should habe (Function) -> Tuple2
			final SerializableFunction<T, R> lambda = t -> function.apply(t);
			final MethodType methodType = Lambdas.getLambdaSignature(lambda).get();
			final Class<T> currentParamType = (Class<T>) methodType.parameterType(0);
			final Class<R> currentReturnType = (Class<R>) methodType.returnType();
			return Tuples.of(currentParamType, currentReturnType);
		};
		return Pattern.of(decomposition, Tuples.of(paramType, returnType));
	}
}
