/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lambda;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.invoke.MethodType;

import javaslang.lambda.Functions.SerializableFunction1;
import javaslang.lambda.Functions.SerializableFunction10;
import javaslang.lambda.Functions.SerializableFunction11;
import javaslang.lambda.Functions.SerializableFunction12;
import javaslang.lambda.Functions.SerializableFunction13;
import javaslang.lambda.Functions.SerializableFunction2;
import javaslang.lambda.Functions.SerializableFunction3;
import javaslang.lambda.Functions.SerializableFunction4;
import javaslang.lambda.Functions.SerializableFunction5;
import javaslang.lambda.Functions.SerializableFunction6;
import javaslang.lambda.Functions.SerializableFunction7;
import javaslang.lambda.Functions.SerializableFunction8;
import javaslang.lambda.Functions.SerializableFunction9;

import org.junit.Test;

public class FunctionsTest {

	// -- Function1

	@Test
	public void shouldGetMethodTypeOfSerializableFunction1() {
		final SerializableFunction1<?, ?> lambda = o -> o;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedSerializableFunction1() {
		final SerializableFunction1<?, ?> lambda = o -> o;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledSerializableFunction1() {
		final SerializableFunction1<?, ?> lambda = o -> o;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuples$Tuple1;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function2

	@Test
	public void shouldGetMethodTypeOfSerializableFunction2() {
		final SerializableFunction2<?, ?, ?> lambda = (o1, o2) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedSerializableFunction2() {
		final SerializableFunction2<?, ?, ?> lambda = (o1, o2) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$SerializableFunction1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledSerializableFunction2() {
		final SerializableFunction2<?, ?, ?> lambda = (o1, o2) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuples$Tuple2;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function3

	@Test
	public void shouldGetMethodTypeOfSerializableFunction3() {
		final SerializableFunction3<?, ?, ?, ?> lambda = (o1, o2, o3) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", getClass()
						.getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedSerializableFunction3() {
		final SerializableFunction3<?, ?, ?, ?> lambda = (o1, o2, o3) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$SerializableFunction1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledSerializableFunction3() {
		final SerializableFunction3<?, ?, ?, ?> lambda = (o1, o2, o3) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuples$Tuple3;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function4

	@Test
	public void shouldGetMethodTypeOfSerializableFunction4() {
		final SerializableFunction4<?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedSerializableFunction4() {
		final SerializableFunction4<?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$SerializableFunction1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledSerializableFunction4() {
		final SerializableFunction4<?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuples$Tuple4;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function5

	@Test
	public void shouldGetMethodTypeOfSerializableFunction5() {
		final SerializableFunction5<?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedSerializableFunction5() {
		final SerializableFunction5<?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$SerializableFunction1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledSerializableFunction5() {
		final SerializableFunction5<?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuples$Tuple5;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function6

	@Test
	public void shouldGetMethodTypeOfSerializableFunction6() {
		final SerializableFunction6<?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedSerializableFunction6() {
		final SerializableFunction6<?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$SerializableFunction1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledSerializableFunction6() {
		final SerializableFunction6<?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuples$Tuple6;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function7

	@Test
	public void shouldGetMethodTypeOfSerializableFunction7() {
		final SerializableFunction7<?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedSerializableFunction7() {
		final SerializableFunction7<?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$SerializableFunction1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledSerializableFunction7() {
		final SerializableFunction7<?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuples$Tuple7;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function8

	@Test
	public void shouldGetMethodTypeOfSerializableFunction8() {
		final SerializableFunction8<?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedSerializableFunction8() {
		final SerializableFunction8<?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$SerializableFunction1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledSerializableFunction8() {
		final SerializableFunction8<?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuples$Tuple8;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function9

	@Test
	public void shouldGetMethodTypeOfSerializableFunction9() {
		final SerializableFunction9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedSerializableFunction9() {
		final SerializableFunction9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$SerializableFunction1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledSerializableFunction9() {
		final SerializableFunction9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuples$Tuple9;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function10

	@Test
	public void shouldGetMethodTypeOfSerializableFunction10() {
		final SerializableFunction10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedSerializableFunction10() {
		final SerializableFunction10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$SerializableFunction1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledSerializableFunction10() {
		final SerializableFunction10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuples$Tuple10;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function11

	@Test
	public void shouldGetMethodTypeOfSerializableFunction11() {
		final SerializableFunction11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9,
				o10, o11) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedSerializableFunction11() {
		final SerializableFunction11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9,
				o10, o11) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$SerializableFunction1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledSerializableFunction11() {
		final SerializableFunction11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9,
				o10, o11) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuples$Tuple11;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function12

	@Test
	public void shouldGetMethodTypeOfSerializableFunction12() {
		final SerializableFunction12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8,
				o9, o10, o11, o12) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedSerializableFunction12() {
		final SerializableFunction12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8,
				o9, o10, o11, o12) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$SerializableFunction1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledSerializableFunction12() {
		final SerializableFunction12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8,
				o9, o10, o11, o12) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuples$Tuple12;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function13

	@Test
	public void shouldGetMethodTypeOfSerializableFunction13() {
		final SerializableFunction13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7,
				o8, o9, o10, o11, o12, o13) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedSerializableFunction13() {
		final SerializableFunction13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7,
				o8, o9, o10, o11, o12, o13) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$SerializableFunction1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledSerializableFunction13() {
		final SerializableFunction13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7,
				o8, o9, o10, o11, o12, o13) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuples$Tuple13;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}
}
