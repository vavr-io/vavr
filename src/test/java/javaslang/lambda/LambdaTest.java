/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lambda;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.invoke.MethodType;

import javaslang.lambda.Lambda.λ0;
import javaslang.lambda.Lambda.λ1;
import javaslang.lambda.Lambda.λ10;
import javaslang.lambda.Lambda.λ11;
import javaslang.lambda.Lambda.λ12;
import javaslang.lambda.Lambda.λ13;
import javaslang.lambda.Lambda.λ2;
import javaslang.lambda.Lambda.λ3;
import javaslang.lambda.Lambda.λ4;
import javaslang.lambda.Lambda.λ5;
import javaslang.lambda.Lambda.λ6;
import javaslang.lambda.Lambda.λ7;
import javaslang.lambda.Lambda.λ8;
import javaslang.lambda.Lambda.λ9;

import org.junit.Test;

public class LambdaTest {

	// -- λ0

	@Test
	public void shouldGetMethodTypeOfλ0() {
		final λ0<?> lambda = () -> null;
		final MethodType actual = lambda.getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", getClass()
				.getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ0() {
		final λ0<?> lambda = () -> null;
		final MethodType actual = lambda.curried().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Void;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ0() {
		final λ0<?> lambda = () -> null;
		final MethodType actual = lambda.tupled().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple0;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ1

	@Test
	public void shouldGetMethodTypeOfλ1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final λ1<?, ?> lambda = o -> o;
		final λ1<Object, Object> lambda = o -> o;
		final MethodType actual = lambda.getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final λ1<?, ?> lambda = o -> o;
		final λ1<Object, Object> lambda = o -> o;
		final MethodType actual = lambda.curried().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final λ1<?, ?> lambda = o -> o;
		final λ1<Object, Object> lambda = o -> o;
		final MethodType actual = lambda.tupled().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple1;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ2

	@Test
	public void shouldGetMethodTypeOfλ2() {
		final λ2<Object, Object, Object> lambda = (o1, o2) -> o1;
		final MethodType actual = lambda.getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ2() {
		final λ2<Object, Object, Object> lambda = (o1, o2) -> o1;
		final MethodType actual = lambda.curried().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Lambda$λ1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ2() {
		final λ2<Object, Object, Object> lambda = (o1, o2) -> o1;
		final MethodType actual = lambda.tupled().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple2;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ3

	@Test
	public void shouldGetMethodTypeOfλ3() {
		final λ3<?, ?, ?, ?> lambda = (o1, o2, o3) -> o1;
		final MethodType actual = lambda.getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", getClass()
						.getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ3() {
		final λ3<?, ?, ?, ?> lambda = (o1, o2, o3) -> o1;
		final MethodType actual = lambda.curried().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Lambda$λ1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ3() {
		final λ3<?, ?, ?, ?> lambda = (o1, o2, o3) -> o1;
		final MethodType actual = lambda.tupled().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple3;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ4

	@Test
	public void shouldGetMethodTypeOfλ4() {
		final λ4<?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = lambda.getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ4() {
		final λ4<?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = lambda.curried().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Lambda$λ1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ4() {
		final λ4<?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = lambda.tupled().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple4;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ5

	@Test
	public void shouldGetMethodTypeOfλ5() {
		final λ5<?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = lambda.getLambdaSignature();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ5() {
		final λ5<?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = lambda.curried().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Lambda$λ1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ5() {
		final λ5<?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = lambda.tupled().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple5;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ6

	@Test
	public void shouldGetMethodTypeOfλ6() {
		final λ6<?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = lambda.getLambdaSignature();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ6() {
		final λ6<?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = lambda.curried().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Lambda$λ1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ6() {
		final λ6<?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = lambda.tupled().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple6;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ7

	@Test
	public void shouldGetMethodTypeOfλ7() {
		final λ7<?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = lambda.getLambdaSignature();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ7() {
		final λ7<?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = lambda.curried().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Lambda$λ1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ7() {
		final λ7<?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = lambda.tupled().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple7;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ8

	@Test
	public void shouldGetMethodTypeOfλ8() {
		final λ8<?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = lambda.getLambdaSignature();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ8() {
		final λ8<?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = lambda.curried().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Lambda$λ1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ8() {
		final λ8<?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = lambda.tupled().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple8;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ9

	@Test
	public void shouldGetMethodTypeOfλ9() {
		final λ9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = lambda.getLambdaSignature();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ9() {
		final λ9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = lambda.curried().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Lambda$λ1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ9() {
		final λ9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = lambda.tupled().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple9;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ10

	@Test
	public void shouldGetMethodTypeOfλ10() {
		final λ10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = lambda.getLambdaSignature();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ10() {
		final λ10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = lambda.curried().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Lambda$λ1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ10() {
		final λ10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = lambda.tupled().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple10;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ11

	@Test
	public void shouldGetMethodTypeOfλ11() {
		final λ11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = lambda.getLambdaSignature();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ11() {
		final λ11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = lambda.curried().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Lambda$λ1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ11() {
		final λ11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = lambda.tupled().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple11;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ12

	@Test
	public void shouldGetMethodTypeOfλ12() {
		final λ12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> o1;
		final MethodType actual = lambda.getLambdaSignature();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedλ12() {
		final λ12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> o1;
		final MethodType actual = lambda.curried().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Lambda$λ1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ12() {
		final λ12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> o1;
		final MethodType actual = lambda.tupled().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple12;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- λ13

	@Test
	public void shouldGetMethodTypeOfλ13() {
		final λ13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11,
				o12, o13) -> o1;
		final MethodType actual = lambda.getLambdaSignature();
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
		final MethodType actual = lambda.curried().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Lambda$λ1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledλ13() {
		final λ13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11,
				o12, o13) -> o1;
		final MethodType actual = lambda.tupled().getLambdaSignature();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple13;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}
}
