/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import java.lang.invoke.MethodType;

import javaslang.Tuples;
import javaslang.Tuples.Tuple1;
import javaslang.Tuples.Tuple2;
import javaslang.lambda.Lambdas;
import javaslang.lambda.SerializableFunction;

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

	/**
	 * This Pattern matches by the signature of a given function. The signature consists of a param type and a return
	 * type.
	 * 
	 * @param <P> Function's argument type.
	 * @param <R> Function's result type.
	 * @param paramType Argument type of the function.
	 * @param returnType Return type of the function.
	 * @return A Pattern which matches functions by argument type and result type.
	 */
	@SuppressWarnings("unchecked")
	public static <T, R> Pattern<SerializableFunction<T, R>, Tuple2<Class<?>, Class<?>>, Tuple2<Class<T>, Class<R>>> Function(
			Class<T> paramType, Class<R> returnType) {
		return Pattern.of((SerializableFunction<T, R> f) -> {
			final MethodType methodType = Lambdas.getLambdaSignature(f).get();
			/* if lambda has captured argument, the last parameter is the method argument */
			final int paramIndex = methodType.parameterCount() - 1;
			final Class<T> currentParamType = (Class<T>) methodType.parameterType(paramIndex);
			final Class<R> currentReturnType = (Class<R>) methodType.returnType();
			return Tuples.of(currentParamType, currentReturnType);
		}, Tuples.of(Integer.class, String.class));
	}
}
