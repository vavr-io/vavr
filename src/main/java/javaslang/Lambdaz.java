/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static javaslang.Lang.requireNotInstantiable;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extension methods for java.lang.invoke.*
 * 
 * @see <a
 *      href="http://stackoverflow.com/questions/21860875/printing-debug-info-on-errors-with-java-8-lambda-expressions">printing
 *      debug info on errors with java 8 lambda expressions</a>
 */
public final class Lambdaz {

	private static final Pattern JVM_FIELD_TYPE = Pattern.compile("\\[*(B|C|D|F|I|J|(L.*;)|S|V|Z)");

	/**
	 * This class is not intended to be instantiated.
	 */
	private Lambdaz() {
		requireNotInstantiable();
	}

	/**
	 * Serializes the given Serializable lambda and returns the corresponding
	 * {@link java.lang.invoke.SerializedLambda}.
	 * 
	 * @param lambda An instance of a lambda.
	 * @return The serialized lambda.
	 */
	public static SerializedLambda getSerializedLambda(Serializable lambda) {
		try {
			final Method method = lambda.getClass().getDeclaredMethod("writeReplace");
			method.setAccessible(true);
			return (SerializedLambda) method.invoke(lambda);
		} catch (Throwable x) {
			throw new IllegalStateException("Error serializing lamda expression.", x);
		}
	}

	/**
	 * Gets the runtime method signature of the given lambda instance. Especially this function is
	 * handy when the functional interface is generic and the parameter and/or return types cannot
	 * be determined directly.
	 * <p>
	 * Uses internally the {@link java.lang.invoke.SerializedLambda#getImplMethodSignature()} by
	 * parsing the JVM field types of the method signature. The result is a {@link LambdaSignature}
	 * which contains the return type and the parameter types of the given lambda.
	 * 
	 * @param lambda A serializable lambda.
	 * @return The signature of the lambda.
	 */
	public static LambdaSignature getLambdaSignature(Serializable lambda) {
		final String signature = getSerializedLambda(lambda).getImplMethodSignature();
		final int index = signature.lastIndexOf(')');
		final Class<?> returnType = getJavaType(signature.substring(index + 1));
		final List<Class<?>> parameterTypes = new ArrayList<>();
		for (Matcher matcher = JVM_FIELD_TYPE.matcher(signature.substring(1, index)); matcher.find(); parameterTypes.add(getJavaType(matcher.group())))
			;
		return new LambdaSignature(returnType,
				parameterTypes.toArray(new Class<?>[parameterTypes.size()]));
	}

	/**
	 * Returns the Class<?> according to the given JVM field type.
	 * 
	 * @param jvmFieldType
	 * @return The Class instance corresponding to the given jvmFieldType.
	 * @throws IllegalStateException if the class referenced by a JVM field type 'L ClassName ;'
	 *             cannot be found.
	 * @see <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html">JVM field
	 *      types</a>
	 */
	private static Class<?> getJavaType(String jvmFieldType) {
		final char firstChar = jvmFieldType.charAt(0);
		switch (firstChar) {
			case 'B':
				return byte.class;
			case 'C':
				return char.class;
			case 'D':
				return double.class;
			case 'F':
				return float.class;
			case 'I':
				return int.class;
			case 'J':
				return long.class;
			case 'L': {
				final String javaType = jvmFieldType.substring(1, jvmFieldType.length() - 1)
						.replaceAll("/", ".");
				try {
					return Class.forName(javaType);
				} catch (ClassNotFoundException x) {
					throw new IllegalStateException("Error loading class of JVM field type "
							+ jvmFieldType, x);
				}
			}
			case 'S':
				return short.class;
			case 'V':
				return void.class;
			case 'Z':
				return boolean.class;
			case '[': {
				final int index = jvmFieldType.lastIndexOf('[') + 1;
				final Class<?> componentType = getJavaType(jvmFieldType.substring(index));
				final int[] dimensions = new int[index]; // initialized with zeros '0'
				return Array.newInstance(componentType, dimensions).getClass();
			}
			default:
				throw new IllegalArgumentException("Unknown JVM field type: " + jvmFieldType);
		}
	}

	/**
	 * Represents a Lambda signature having a return type and parameter types but no name, similar
	 * to a {@link java.lang.reflect.Method}.
	 */
	public static class LambdaSignature {
		private final Class<?> returnType;
		private final Class<?>[] parameterTypes;

		public LambdaSignature(Class<?> returnType, Class<?>[] parameterTypes) {
			this.returnType = returnType;
			this.parameterTypes = parameterTypes;
		}

		public Class<?> getReturnType() {
			return returnType;
		}

		public Class<?>[] getParameterTypes() {
			return parameterTypes;
		}
	}

}
