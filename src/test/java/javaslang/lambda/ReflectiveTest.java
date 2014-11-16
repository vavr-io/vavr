/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lambda;

import static org.assertj.core.api.Assertions.assertThat;
import javaslang.collection.Tuple;
import javaslang.collection.Tuple.Tuple3;

import org.junit.Test;

public class ReflectiveTest {

	@Test
	public void shouldParseReturnTypeVoid() {
		final ReturnTypeVoid lambda = () -> {
		};
		final Class<?> actual = lambda.getLambdaSignature().returnType();
		assertThat(actual.getName()).isEqualTo("void");
	}

	@Test
	public void shouldParseReturnTypeBoolean() {
		final ReturnTypeBoolean lambda = () -> true;
		final Class<?> actual = lambda.getLambdaSignature().returnType();
		assertThat(actual.getName()).isEqualTo("boolean");
	}

	@Test
	public void shouldParseReturnTypeByte() {
		final ReturnTypeByte lambda = () -> (byte) 1;
		final Class<?> actual = lambda.getLambdaSignature().returnType();
		assertThat(actual.getName()).isEqualTo("byte");
	}

	@Test
	public void shouldParseReturnTypeChar() {
		final ReturnTypeChar lambda = () -> '@';
		final Class<?> actual = lambda.getLambdaSignature().returnType();
		assertThat(actual.getName()).isEqualTo("char");
	}

	@Test
	public void shouldParseReturnTypeDouble() {
		final ReturnTypeDouble lambda = () -> 1.0d;
		final Class<?> actual = lambda.getLambdaSignature().returnType();
		assertThat(actual.getName()).isEqualTo("double");
	}

	@Test
	public void shouldParseReturnTypeFloat() {
		final ReturnTypeFloat lambda = () -> 1.0f;
		final Class<?> actual = lambda.getLambdaSignature().returnType();
		assertThat(actual.getName()).isEqualTo("float");
	}

	@Test
	public void shouldParseReturnTypeInt() {
		final ReturnTypeInt lambda = () -> 1;
		final Class<?> actual = lambda.getLambdaSignature().returnType();
		assertThat(actual.getName()).isEqualTo("int");
	}

	@Test
	public void shouldParseReturnTypeLong() {
		final ReturnTypeLong lambda = () -> 1L;
		final Class<?> actual = lambda.getLambdaSignature().returnType();
		assertThat(actual.getName()).isEqualTo("long");
	}

	@Test
	public void shouldParseReturnTypeShort() {
		final ReturnTypeShort lambda = () -> (short) 1;
		final Class<?> actual = lambda.getLambdaSignature().returnType();
		assertThat(actual.getName()).isEqualTo("short");
	}

	@Test
	public void shouldParseReturnTypeArrayOfInt() {
		final ReturnTypeArrayOfInt lambda = () -> new int[] {};
		final Class<?> actual = lambda.getLambdaSignature().returnType();
		assertThat(actual.getName()).isEqualTo("[I");
	}

	@Test
	public void shouldParseParameterTypeArrayOfBoolean() {
		final ParameterTypeArrayOfBoolean lambda = (boolean[] b) -> {
			return;
		};
		final Class<?> actual = lambda.getLambdaSignature().parameterType(0);
		assertThat(actual.getName()).isEqualTo("[Z");
	}

	@Test
	public void shouldParseReturnTypeArrayOfArrayReference() {
		final ReturnTypeArrayOfArrayOfString lambda = () -> new String[][] {};
		final Class<?> actual = lambda.getLambdaSignature().returnType();
		assertThat(actual.getName()).isEqualTo("[[Ljava.lang.String;");
	}

	@Test
	public void shouldParseReturnTypeClassReference() {
		final ReturnTypeClassReference lambda = () -> "hi";
		final Class<?> actual = lambda.getLambdaSignature().returnType();
		assertThat(actual.getName()).isEqualTo("java.lang.String");
	}

	@Test
	public void shouldParseNoParameterTypes() {
		final NoParameterTypes lambda = () -> {
		};
		final Class<?>[] actual = lambda.getLambdaSignature().parameterArray();
		assertThat(actual).isEmpty();
	}

	@Test
	public void shouldParseOneParameterType() {
		final OneParameterType lambda = (int i) -> {
		};
		final Class<?>[] actual = lambda.getLambdaSignature().parameterArray();
		assertThat(actual).containsExactly(int.class);
	}

	@Test
	public void shouldParseTwoParameterTypes() throws ClassNotFoundException {
		final TwoParameterTypes lambda = (String s, byte[][] bytes) -> {
		};
		final Class<?>[] actual = lambda.getLambdaSignature().parameterArray();
		assertThat(actual).containsExactly(String.class, Class.forName("[[B"));
	}

	@Test
	public void shouldConvertUnitLambdaSignatureToString() {
		final Unit lambda = () -> {
		};
		final String actual = lambda.getLambdaSignature().toString();
		assertThat(actual).isEqualTo("()void");
	}

	@Test
	public void shouldConvertNonTrivialLambdaSignatureToString() {
		final StringIntegerArrayDoubleArrayToTuple3 lambda = (s, i, d) -> Tuple.of(s, i, d);
		final String actual = lambda.getLambdaSignature().toString();
		assertThat(actual).isEqualTo("(String,Integer[][],double[][])Tuple3");
	}

	@Test
	public void shouldRecognizeTrivialEqualLambdaSignatures() {
		final Unit lambda1 = () -> {
		};
		final Unit lambda2 = () -> {
		};
		assertThat(lambda1.getLambdaSignature()).isEqualTo(lambda2.getLambdaSignature());
	}

	@Test
	public void shouldRecognizeNonTrivialEqualLambdaSignatures() {
		final StringIntegerArrayDoubleArrayToTuple3 lambda1 = (s, i, d) -> Tuple.of(s, i, d);
		final StringIntegerArrayDoubleArrayToTuple3 lambda2 = (s, i, d) -> Tuple.of(s, i, d);
		assertThat(lambda1.getLambdaSignature()).isEqualTo(lambda2.getLambdaSignature());
	}

	@Test
	public void shouldRecognizeNonTrivialNonEqualLambdaSignatures() {
		final StringIntegerArrayDoubleArrayToTuple3 lambda1 = (s, i, d) -> Tuple.of(s, i, d);
		final StringIntArrayDoubleArrayToTuple3 lambda2 = (s, i, d) -> Tuple.of(s, i, d);
		assertThat(lambda1.getLambdaSignature()).isNotEqualTo(lambda2.getLambdaSignature());
	}

	@FunctionalInterface
	static interface Unit extends Reflective {
		void go();
	}

	@FunctionalInterface
	static interface StringIntegerArrayDoubleArrayToTuple3 extends Reflective {
		Tuple3<String, Integer[][], double[][]> go(String s, Integer[][] i, double[][] d);
	}

	@FunctionalInterface
	static interface StringIntArrayDoubleArrayToTuple3 extends Reflective {
		Tuple3<String, int[][], double[][]> go(String s, int[][] i, double[][] d);
	}

	@FunctionalInterface
	static interface ReturnTypeVoid extends Reflective {
		void go();
	}

	@FunctionalInterface
	static interface ReturnTypeBoolean extends Reflective {
		boolean go();
	}

	@FunctionalInterface
	static interface ReturnTypeByte extends Reflective {
		byte go();
	}

	@FunctionalInterface
	static interface ReturnTypeChar extends Reflective {
		char go();
	}

	@FunctionalInterface
	static interface ReturnTypeFloat extends Reflective {
		float go();
	}

	@FunctionalInterface
	static interface ReturnTypeDouble extends Reflective {
		double go();
	}

	@FunctionalInterface
	static interface ReturnTypeInt extends Reflective {
		int go();
	}

	@FunctionalInterface
	static interface ReturnTypeLong extends Reflective {
		long go();
	}

	@FunctionalInterface
	static interface ReturnTypeShort extends Reflective {
		short go();
	}

	@FunctionalInterface
	static interface ReturnTypeArrayOfInt extends Reflective {
		int[] go();
	}

	@FunctionalInterface
	static interface ReturnTypeArrayOfArrayOfString extends Reflective {
		String[][] go();
	}

	@FunctionalInterface
	static interface ReturnTypeClassReference extends Reflective {
		String go();
	}

	@FunctionalInterface
	static interface NoParameterTypes extends Reflective {
		void go();
	}

	@FunctionalInterface
	static interface OneParameterType extends Reflective {
		void go(int i);
	}

	@FunctionalInterface
	static interface TwoParameterTypes extends Reflective {
		void go(String s, byte[][] bytes);
	}

	@FunctionalInterface
	static interface ParameterTypeArrayOfBoolean extends Reflective {
		void go(boolean[] b);
	}
}
