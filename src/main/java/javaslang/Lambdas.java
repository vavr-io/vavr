/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static java.util.stream.Collectors.joining;
import static javaslang.Lang.requireNotInstantiable;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javaslang.Tuples.Tuple2;

/**
 * Extension methods for java.lang.invoke.*
 * 
 * @see <a
 *      href="http://stackoverflow.com/questions/21860875/printing-debug-info-on-errors-with-java-8-lambda-expressions">printing
 *      debug info on errors with java 8 lambda expressions</a>
 */
public final class Lambdas {

	private static final Pattern JVM_FIELD_TYPE = Pattern
			.compile("\\[*(B|C|D|F|I|J|(L.*?;)|S|V|Z)");

	/**
	 * This class is not intended to be instantiated.
	 */
	private Lambdas() {
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
		final Tuple2<String, String> signature = split(getSerializedLambda(lambda)
				.getImplMethodSignature());
		final Class<?>[] parameterTypes = Lang
				.stream(JVM_FIELD_TYPE.matcher(signature._1))
				.map(Lambdas::getJavaType)
				.toArray(Class<?>[]::new);
		final Class<?> returnType = getJavaType(signature._2);
		return new LambdaSignature(parameterTypes, returnType);
	}

	/**
	 * Splits a lambda signature string into the parts parameters and return string.
	 * 
	 * @param signature '(' + paramsSignatureString + ')' + returnSignatureString
	 * @return Tuple2(paramsSignatureString, returnSignatureString)
	 */
	private static Tuple2<String, String> split(String signature) {
		final int index = signature.lastIndexOf(')');
		return Tuples.of(signature.substring(1, index), signature.substring(index + 1));
	}

	/**
	 * Returns the Class<?> according to the given JVM field type.
	 * 
	 * @param jvmFieldType
	 * @return The Class instance corresponding to the given jvmFieldType.
	 * @throws IllegalStateException if the class referenced by a JVM field type 'L ClassName ;'
	 *             cannot be found.
	 * @see <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html">JVM field
	 *      types</a>.
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
				final String javaType = jvmFieldType
						.substring(1, jvmFieldType.length() - 1)
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
		private final Class<?>[] parameterTypes;
		private final Class<?> returnType;
		private int hashCode;

		public LambdaSignature(Class<?>[] parameterTypes, Class<?> returnType) {
			this.parameterTypes = parameterTypes;
			this.returnType = returnType;
		}

		public Class<?>[] getParameterTypes() {
			return parameterTypes;
		}

		public Class<?> getParameterType(int index) {
			if (index < 0 || index >= parameterTypes.length) {
				throw new IndexOutOfBoundsException("getParameterType(" + index + ")");
			}
			return parameterTypes[index];
		}

		public Class<?> getReturnType() {
			return returnType;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof LambdaSignature)) {
				return false;
			} else {
				final LambdaSignature that = (LambdaSignature) o;
				return this.returnType.equals(that.returnType)
						&& Arrays.equals(this.parameterTypes, that.parameterTypes);
			}
		}

		@Override
		public int hashCode() {
			// no synchronization, implementation identical to String.hashCode
			if (hashCode == 0) {
				// toString() cannot be empty => hashCode > 0
				hashCode = toString().hashCode();
			}
			return hashCode;
		}

		@Override
		public String toString() {
			return Stream//
					.of(parameterTypes)
					.map(Strings::toString)
					.collect(joining(", ", "(", ") -> ")) + returnType.getName();
		}
	}

}
