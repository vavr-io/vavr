/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.lang.invoke.MethodType;

import javaslang.Lambda.λ0;
import javaslang.Lambda.λ1;
import javaslang.Lambda.λ10;
import javaslang.Lambda.λ11;
import javaslang.Lambda.λ12;
import javaslang.Lambda.λ13;
import javaslang.Lambda.λ2;
import javaslang.Lambda.λ3;
import javaslang.Lambda.λ4;
import javaslang.Lambda.λ5;
import javaslang.Lambda.λ6;
import javaslang.Lambda.λ7;
import javaslang.Lambda.λ8;
import javaslang.Lambda.λ9;
import javaslang.Tuple.Tuple3;

import org.junit.Test;

public class LambdaTest {

	// -- λ0

	@Test
	public void shouldGetMethodTypeOfλ0() {
		final λ0<?> lambda = () -> null;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", getClass()
				.getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ0() {
		final λ0<?> lambda = () -> null;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Void;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ0() {
		final λ0<?> lambda = () -> null;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple0;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ1

	@Test
	public void shouldGetMethodTypeOfλ1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final λ1<?, ?> lambda = o -> o;
		final λ1<Object, Object> lambda = o -> o;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final λ1<?, ?> lambda = o -> o;
		final λ1<Object, Object> lambda = o -> o;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final λ1<?, ?> lambda = o -> o;
		final λ1<Object, Object> lambda = o -> o;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple1;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ2

	@Test
	public void shouldGetMethodTypeOfλ2() {
		final λ2<Object, Object, Object> lambda = (o1, o2) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ2() {
		final λ2<Object, Object, Object> lambda = (o1, o2) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Lambda$λ1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ2() {
		final λ2<Object, Object, Object> lambda = (o1, o2) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple2;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ3

	@Test
	public void shouldGetMethodTypeOfλ3() {
		final λ3<?, ?, ?, ?> lambda = (o1, o2, o3) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", getClass()
						.getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ3() {
		final λ3<?, ?, ?, ?> lambda = (o1, o2, o3) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Lambda$λ1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ3() {
		final λ3<?, ?, ?, ?> lambda = (o1, o2, o3) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple3;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ4

	@Test
	public void shouldGetMethodTypeOfλ4() {
		final λ4<?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ4() {
		final λ4<?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Lambda$λ1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ4() {
		final λ4<?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple4;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ5

	@Test
	public void shouldGetMethodTypeOfλ5() {
		final λ5<?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ5() {
		final λ5<?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Lambda$λ1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ5() {
		final λ5<?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple5;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ6

	@Test
	public void shouldGetMethodTypeOfλ6() {
		final λ6<?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ6() {
		final λ6<?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Lambda$λ1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ6() {
		final λ6<?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple6;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ7

	@Test
	public void shouldGetMethodTypeOfλ7() {
		final λ7<?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ7() {
		final λ7<?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Lambda$λ1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ7() {
		final λ7<?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple7;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ8

	@Test
	public void shouldGetMethodTypeOfλ8() {
		final λ8<?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ8() {
		final λ8<?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Lambda$λ1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ8() {
		final λ8<?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple8;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ9

	@Test
	public void shouldGetMethodTypeOfλ9() {
		final λ9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ9() {
		final λ9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Lambda$λ1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ9() {
		final λ9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple9;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ10

	@Test
	public void shouldGetMethodTypeOfλ10() {
		final λ10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ10() {
		final λ10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Lambda$λ1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ10() {
		final λ10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple10;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ11

	@Test
	public void shouldGetMethodTypeOfλ11() {
		final λ11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ11() {
		final λ11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Lambda$λ1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ11() {
		final λ11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple11;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ12

	@Test
	public void shouldGetMethodTypeOfλ12() {
		final λ12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ12() {
		final λ12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Lambda$λ1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ12() {
		final λ12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple$Tuple12;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ13

	@Test
	public void shouldGetMethodTypeOfλ13() {
		final λ13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11,
				o12, o13) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ13() {
		final λ13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11,
				o12, o13) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/Lambda$λ1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ13() {
		final λ13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11,
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
		final Class<?> actual = Lambda.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("void");
	}

	@Test
	public void shouldParseReturnTypeBoolean() {
		final ReturnTypeBoolean lambda = () -> true;
		final Class<?> actual = Lambda.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("boolean");
	}

	@Test
	public void shouldParseReturnTypeByte() {
		final ReturnTypeByte lambda = () -> (byte) 1;
		final Class<?> actual = Lambda.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("byte");
	}

	@Test
	public void shouldParseReturnTypeChar() {
		final ReturnTypeChar lambda = () -> '@';
		final Class<?> actual = Lambda.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("char");
	}

	@Test
	public void shouldParseReturnTypeDouble() {
		final ReturnTypeDouble lambda = () -> 1.0d;
		final Class<?> actual = Lambda.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("double");
	}

	@Test
	public void shouldParseReturnTypeFloat() {
		final ReturnTypeFloat lambda = () -> 1.0f;
		final Class<?> actual = Lambda.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("float");
	}

	@Test
	public void shouldParseReturnTypeInt() {
		final ReturnTypeInt lambda = () -> 1;
		final Class<?> actual = Lambda.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("int");
	}

	@Test
	public void shouldParseReturnTypeLong() {
		final ReturnTypeLong lambda = () -> 1L;
		final Class<?> actual = Lambda.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("long");
	}

	@Test
	public void shouldParseReturnTypeShort() {
		final ReturnTypeShort lambda = () -> (short) 1;
		final Class<?> actual = Lambda.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("short");
	}

	@Test
	public void shouldParseReturnTypeArrayOfInt() {
		final ReturnTypeArrayOfInt lambda = () -> new int[] {};
		final Class<?> actual = Lambda.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("[I");
	}

	@Test
	public void shouldParseParameterTypeArrayOfBoolean() {
		final ParameterTypeArrayOfBoolean lambda = (boolean[] b) -> {};
		final Class<?> actual = Lambda.getLambdaSignature(lambda).parameterType(0);
		assertThat(actual.getName()).isEqualTo("[Z");
	}

	@Test
	public void shouldParseReturnTypeArrayOfArrayReference() {
		final ReturnTypeArrayOfArrayOfString lambda = () -> new String[][] {};
		final Class<?> actual = Lambda.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("[[Ljava.lang.String;");
	}

	@Test
	public void shouldParseReturnTypeClassReference() {
		final ReturnTypeClassReference lambda = () -> "hi";
		final Class<?> actual = Lambda.getLambdaSignature(lambda).returnType();
		assertThat(actual.getName()).isEqualTo("java.lang.String");
	}

	@Test
	public void shouldParseNoParameterTypes() {
		final NoParameterTypes lambda = () -> {
		};
		final Class<?>[] actual = Lambda.getLambdaSignature(lambda).parameterArray();
		assertThat(actual).isEmpty();
	}

	@Test
	public void shouldParseOneParameterType() {
		final OneParameterType lambda = (int i) -> {
		};
		final Class<?>[] actual = Lambda.getLambdaSignature(lambda).parameterArray();
		assertThat(actual).containsExactly(int.class);
	}

	@Test
	public void shouldParseTwoParameterTypes() throws ClassNotFoundException {
		final TwoParameterTypes lambda = (String s, byte[][] bytes) -> {
		};
		final Class<?>[] actual = Lambda.getLambdaSignature(lambda).parameterArray();
		assertThat(actual).containsExactly(String.class, Class.forName("[[B"));
	}

	@Test
	public void shouldConvertUnitLambdaSignatureToString() {
		final Unit lambda = () -> {
		};
		final String actual = Lambda.getLambdaSignature(lambda).toString();
		assertThat(actual).isEqualTo("()void");
	}

	@Test
	public void shouldConvertNonTrivialLambdaSignatureToString() {
		final StringIntegerArrayDoubleArrayToTuple3 lambda = (s, i, d) -> Tuple.of(s, i, d);
		final String actual = Lambda.getLambdaSignature(lambda).toString();
		assertThat(actual).isEqualTo("(String,Integer[][],double[][])Tuple3");
	}

	@Test
	public void shouldRecognizeTrivialEqualLambdaSignatures() {
		final Unit lambda1 = () -> {
		};
		final Unit lambda2 = () -> {
		};
		assertThat(Lambda.getLambdaSignature(lambda1)).isEqualTo(Lambda.getLambdaSignature(lambda2));
	}

	@Test
	public void shouldRecognizeNonTrivialEqualLambdaSignatures() {
		final StringIntegerArrayDoubleArrayToTuple3 lambda1 = (s, i, d) -> Tuple.of(s, i, d);
		final StringIntegerArrayDoubleArrayToTuple3 lambda2 = (s, i, d) -> Tuple.of(s, i, d);
		assertThat(Lambda.getLambdaSignature(lambda1)).isEqualTo(Lambda.getLambdaSignature(lambda2));
	}

	@Test
	public void shouldRecognizeNonTrivialNonEqualLambdaSignatures() {
		final StringIntegerArrayDoubleArrayToTuple3 lambda1 = (s, i, d) -> Tuple.of(s, i, d);
		final StringIntArrayDoubleArrayToTuple3 lambda2 = (s, i, d) -> Tuple.of(s, i, d);
		assertThat(Lambda.getLambdaSignature(lambda1)).isNotEqualTo(Lambda.getLambdaSignature(lambda2));
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
