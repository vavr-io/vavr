/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lambda;

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

import javaslang.exception.Try;

/**
 * Extension methods for {@code java.lang.invoke.*}.
 * 
 * @see <a
 *      href="http://stackoverflow.com/questions/21860875/printing-debug-info-on-errors-with-java-8-lambda-expressions">printing
 *      debug info on errors with java 8 lambda expressions</a>
 * @see <a href="http://www.slideshare.net/hendersk/method-handles-in-java">Method Handles in Java</a>
 */
public final class Lambdas {

	/**
	 * This class is not intended to be instantiated.
	 */
	private Lambdas() {
		throw new AssertionError(Lambdas.class.getName() + " is not intended to be instantiated.");
	}

	/**
	 * Serializes the given Serializable lambda and returns the corresponding {@link java.lang.invoke.SerializedLambda}.
	 * 
	 * @param lambda An instance of a lambda.
	 * @return The serialized lambda wrapped in a {@link javaslang.exception.Success}, or a
	 *         {@link javaslang.exception.Failure} if an exception occurred.
	 */
	public static Try<SerializedLambda> getSerializedLambda(Serializable lambda) {
		return Try.of(() -> {
			final Method method = lambda.getClass().getDeclaredMethod("writeReplace");
			method.setAccessible(true);
			return (SerializedLambda) method.invoke(lambda);
		});
	}

	/**
	 * Gets the runtime method signature of the given lambda instance. Especially this function is handy when the
	 * functional interface is generic and the parameter and/or return types cannot be determined directly.
	 * <p>
	 * Uses internally the {@link java.lang.invoke.SerializedLambda#getImplMethodSignature()} by parsing the JVM field
	 * types of the method signature. The result is a {@link LambdaSignature} which contains the return type and the
	 * parameter types of the given lambda.
	 * 
	 * @param lambda A serializable lambda.
	 * @return The signature of the lambda wrapped in a {@link javaslang.exception.Success}, or a
	 *         {@link javaslang.exception.Failure} if an exception occurred.
	 */
	public static Try<MethodType> getLambdaSignature(Serializable lambda) {
		return getSerializedLambda(lambda).map(serializedLambda -> {
			final String signature = serializedLambda.getImplMethodSignature();
			return MethodType.fromMethodDescriptorString(signature, lambda.getClass().getClassLoader());
		});
	}
}
