/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.lang.invoke.MethodType;

import javaslang.Functions.*;
import javaslang.Tuple.Tuple3;

import org.junit.Test;

public class FunctionTest {

	// -- Function0

	@Test
	public void shouldGetMethodTypeOfFunction0() {
		final Function0 lambda = () -> null;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", getClass()
				.getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction0() {
		final Function0 lambda = () -> null;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Void;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction0() {
		final Function0 lambda = () -> null;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple0;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function1

	@Test
	public void shouldGetMethodTypeOfFunction1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final Function1<?, ?> lambda = o -> o;
		final Function1 lambda = o -> o;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final Function1<?, ?> lambda = o -> o;
		final Function1 lambda = o -> o;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final Function1<?, ?> lambda = o -> o;
		final Function1 lambda = o -> o;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple1;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function2

	@Test
	public void shouldGetMethodTypeOfFunction2() {
		final Function2 lambda = (o1, o2) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction2() {
		final Function2 lambda = (o1, o2) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Functions$Function1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction2() {
		final Function2 lambda = (o1, o2) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple2;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function3

	@Test
	public void shouldGetMethodTypeOfFunction3() {
		final Function3 lambda = (o1, o2, o3) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", getClass()
						.getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction3() {
		final Function3 lambda = (o1, o2, o3) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Functions$Function1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction3() {
		final Function3 lambda = (o1, o2, o3) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple3;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function4

	@Test
	public void shouldGetMethodTypeOfFunction4() {
		final Function4 lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction4() {
		final Function4 lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Functions$Function1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction4() {
		final Function4 lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple4;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function5

	@Test
	public void shouldGetMethodTypeOfFunction5() {
		final Function5 lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction5() {
		final Function5 lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Functions$Function1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction5() {
		final Function5 lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple5;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function6

	@Test
	public void shouldGetMethodTypeOfFunction6() {
		final Function6 lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction6() {
		final Function6 lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Functions$Function1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction6() {
		final Function6 lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple6;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function7

	@Test
	public void shouldGetMethodTypeOfFunction7() {
		final Function7 lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction7() {
		final Function7 lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Functions$Function1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction7() {
		final Function7 lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple7;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function8

	@Test
	public void shouldGetMethodTypeOfFunction8() {
		final Function8 lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction8() {
		final Function8 lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Functions$Function1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction8() {
		final Function8 lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple8;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function9

	@Test
	public void shouldGetMethodTypeOfFunction9() {
		final Function9 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction9() {
		final Function9 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Functions$Function1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction9() {
		final Function9 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple9;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function10

	@Test
	public void shouldGetMethodTypeOfFunction10() {
		final Function10 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction10() {
		final Function10 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Functions$Function1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction10() {
		final Function10 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple10;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function11

	@Test
	public void shouldGetMethodTypeOfFunction11() {
		final Function11 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction11() {
		final Function11 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Functions$Function1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction11() {
		final Function11 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple11;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function12

	@Test
	public void shouldGetMethodTypeOfFunction12() {
		final Function12 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction12() {
		final Function12 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Functions$Function1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction12() {
		final Function12 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple12;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function13

	@Test
	public void shouldGetMethodTypeOfFunction13() {
		final Function13 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11,
				o12, o13) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction13() {
		final Function13 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11,
				o12, o13) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Functions$Function1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction13() {
		final Function13 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11,
				o12, o13) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple13;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- lambda reflection tests

	@Test
	public void shouldParseReturnTypeVoid() {
		final ReturnTypeVoid lambda = () -> {
		};
		final Class<?> actual = Functions.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("void");
	}

	@Test
	public void shouldParseReturnTypeBoolean() {
		final ReturnTypeBoolean lambda = () -> true;
		final Class<?> actual = Functions.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("boolean");
	}

	@Test
	public void shouldParseReturnTypeByte() {
		final ReturnTypeByte lambda = () -> (byte) 1;
		final Class<?> actual = Functions.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("byte");
	}

	@Test
	public void shouldParseReturnTypeChar() {
		final ReturnTypeChar lambda = () -> '@';
		final Class<?> actual = Functions.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("char");
	}

	@Test
	public void shouldParseReturnTypeDouble() {
		final ReturnTypeDouble lambda = () -> 1.0d;
		final Class<?> actual = Functions.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("double");
	}

	@Test
	public void shouldParseReturnTypeFloat() {
		final ReturnTypeFloat lambda = () -> 1.0f;
		final Class<?> actual = Functions.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("float");
	}

	@Test
	public void shouldParseReturnTypeInt() {
		final ReturnTypeInt lambda = () -> 1;
		final Class<?> actual = Functions.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("int");
	}

	@Test
	public void shouldParseReturnTypeLong() {
		final ReturnTypeLong lambda = () -> 1L;
		final Class<?> actual = Functions.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("long");
	}

	@Test
	public void shouldParseReturnTypeShort() {
		final ReturnTypeShort lambda = () -> (short) 1;
		final Class<?> actual = Functions.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("short");
	}

	@Test
	public void shouldParseReturnTypeArrayOfInt() {
		final ReturnTypeArrayOfInt lambda = () -> new int[] {};
		final Class<?> actual = Functions.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("[I");
	}

	@Test
	public void shouldParseParameterTypeArrayOfBoolean() {
		final ParameterTypeArrayOfBoolean lambda = (boolean[] b) -> {};
		final Class<?> actual = Functions.getLambdaSignature(lambda).parameterType(0);
		assertThat(actual.getName()).isEqualTo("[Z");
	}

	@Test
	public void shouldParseReturnTypeArrayOfArrayReference() {
		final ReturnTypeArrayOfArrayOfString lambda = () -> new String[][] {};
		final Class<?> actual = Functions.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("[[Ljava.lang.String;");
	}

	@Test
	public void shouldParseReturnTypeClassReference() {
		final ReturnTypeClassReference lambda = () -> "hi";
		final Class<?> actual = Functions.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("java.lang.String");
	}

	@Test
	public void shouldParseNoParameterTypes() {
		final NoParameterTypes lambda = () -> {
		};
		final Class<?>[] actual = Functions.getLambdaSignature(lambda).parameterArray();
		assertThat(actual).isEmpty();
	}

	@Test
	public void shouldParseOneParameterType() {
		final OneParameterType lambda = (int i) -> {
		};
		final Class<?>[] actual = Functions.getLambdaSignature(lambda).parameterArray();
		assertThat(actual).containsExactly(int.class);
	}

	@Test
	public void shouldParseTwoParameterTypes() throws ClassNotFoundException {
		final TwoParameterTypes lambda = (String s, byte[][] bytes) -> {
		};
		final Class<?>[] actual = Functions.getLambdaSignature(lambda).parameterArray();
		assertThat(actual).containsExactly(String.class, Class.forName("[[B"));
	}

	@Test
	public void shouldConvertUnitLambdaSignatureToString() {
		final Unit lambda = () -> {
		};
		final String actual = Functions.getLambdaSignature(lambda).toString();
		assertThat(actual).isEqualTo("()void");
	}

	@Test
	public void shouldConvertNonTrivialLambdaSignatureToString() {
		final StringIntegerArrayDoubleArrayToTuple3 lambda = (s, i, d) -> Tuple.of(s, i, d);
		final String actual = Functions.getLambdaSignature(lambda).toString();
		assertThat(actual).isEqualTo("(String,Integer[][],double[][])Tuple3");
	}

	@Test
	public void shouldRecognizeTrivialEqualLambdaSignatures() {
		final Unit lambda1 = () -> {
		};
		final Unit lambda2 = () -> {
		};
		assertThat(Functions.getLambdaSignature(lambda1)).isEqualTo(Functions.getLambdaSignature(lambda2));
	}

	@Test
	public void shouldRecognizeNonTrivialEqualLambdaSignatures() {
		final StringIntegerArrayDoubleArrayToTuple3 lambda1 = (s, i, d) -> Tuple.of(s, i, d);
		final StringIntegerArrayDoubleArrayToTuple3 lambda2 = (s, i, d) -> Tuple.of(s, i, d);
		assertThat(Functions.getLambdaSignature(lambda1)).isEqualTo(Functions.getLambdaSignature(lambda2));
	}

	@Test
	public void shouldRecognizeNonTrivialNonEqualLambdaSignatures() {
		final StringIntegerArrayDoubleArrayToTuple3 lambda1 = (s, i, d) -> Tuple.of(s, i, d);
		final StringIntArrayDoubleArrayToTuple3 lambda2 = (s, i, d) -> Tuple.of(s, i, d);
		assertThat(Functions.getLambdaSignature(lambda1)).isNotEqualTo(Functions.getLambdaSignature(lambda2));
	}

	@FunctionalInterface
	static interface Unit extends Serializable {
		void go();
	}

	@FunctionalInterface
	static interface StringIntegerArrayDoubleArrayToTuple3 extends Serializable {
		Tuple3<String, Integer[][], double[][]> go(String s, Integer[][] i, double[][] d);
	}

	@FunctionalInterface
	static interface StringIntArrayDoubleArrayToTuple3 extends Serializable {
		Tuple3<String, int[][], double[][]> go(String s, int[][] i, double[][] d);
	}

	@FunctionalInterface
	static interface ReturnTypeVoid extends Serializable {
		void go();
	}

	@FunctionalInterface
	static interface ReturnTypeBoolean extends Serializable {
		boolean go();
	}

	@FunctionalInterface
	static interface ReturnTypeByte extends Serializable {
		byte go();
	}

	@FunctionalInterface
	static interface ReturnTypeChar extends Serializable {
		char go();
	}

	@FunctionalInterface
	static interface ReturnTypeFloat extends Serializable {
		float go();
	}

	@FunctionalInterface
	static interface ReturnTypeDouble extends Serializable {
		double go();
	}

	@FunctionalInterface
	static interface ReturnTypeInt extends Serializable {
		int go();
	}

	@FunctionalInterface
	static interface ReturnTypeLong extends Serializable {
		long go();
	}

	@FunctionalInterface
	static interface ReturnTypeShort extends Serializable {
		short go();
	}

	@FunctionalInterface
	static interface ReturnTypeArrayOfInt extends Serializable {
		int[] go();
	}

	@FunctionalInterface
	static interface ReturnTypeArrayOfArrayOfString extends Serializable {
		String[][] go();
	}

	@FunctionalInterface
	static interface ReturnTypeClassReference extends Serializable {
		String go();
	}

	@FunctionalInterface
	static interface NoParameterTypes extends Serializable {
		void go();
	}

	@FunctionalInterface
	static interface OneParameterType extends Serializable {
		void go(int i);
	}

	@FunctionalInterface
	static interface TwoParameterTypes extends Serializable {
		void go(String s, byte[][] bytes);
	}

	@FunctionalInterface
	static interface ParameterTypeArrayOfBoolean extends Serializable {
		void go(boolean[] b);
	}
}
