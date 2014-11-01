/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lambda;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.invoke.MethodType;

import javaslang.lambda.Functions.Function0;
import javaslang.lambda.Functions.Function1;
import javaslang.lambda.Functions.Function10;
import javaslang.lambda.Functions.Function11;
import javaslang.lambda.Functions.Function12;
import javaslang.lambda.Functions.Function13;
import javaslang.lambda.Functions.Function2;
import javaslang.lambda.Functions.Function3;
import javaslang.lambda.Functions.Function4;
import javaslang.lambda.Functions.Function5;
import javaslang.lambda.Functions.Function6;
import javaslang.lambda.Functions.Function7;
import javaslang.lambda.Functions.Function8;
import javaslang.lambda.Functions.Function9;

import org.junit.Test;

public class FunctionsTest {

	// -- Function0

	@Test
	public void shouldGetMethodTypeOfFunction0() {
		final Function0<?> lambda = () -> null;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", getClass()
				.getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction0() {
		final Function0<?> lambda = () -> null;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", getClass()
				.getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction0() {
		final Function0<?> lambda = () -> null;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", getClass()
				.getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function1

	@Test
	public void shouldGetMethodTypeOfFunction1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final Function1<?, ?> lambda = o -> o;
		final Function1<Object, Object> lambda = o -> o;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final Function1<?, ?> lambda = o -> o;
		final Function1<Object, Object> lambda = o -> o;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final Function1<?, ?> lambda = o -> o;
		final Function1<Object, Object> lambda = o -> o;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple1;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function2

	@Test
	public void shouldGetMethodTypeOfFunction2() {
		final Function2<?, ?, ?> lambda = (o1, o2) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction2() {
		final Function2<?, ?, ?> lambda = (o1, o2) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$Function1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction2() {
		final Function2<?, ?, ?> lambda = (o1, o2) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple2;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function3

	@Test
	public void shouldGetMethodTypeOfFunction3() {
		final Function3<?, ?, ?, ?> lambda = (o1, o2, o3) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", getClass()
						.getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction3() {
		final Function3<?, ?, ?, ?> lambda = (o1, o2, o3) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$Function1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction3() {
		final Function3<?, ?, ?, ?> lambda = (o1, o2, o3) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple3;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function4

	@Test
	public void shouldGetMethodTypeOfFunction4() {
		final Function4<?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction4() {
		final Function4<?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$Function1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction4() {
		final Function4<?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple4;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function5

	@Test
	public void shouldGetMethodTypeOfFunction5() {
		final Function5<?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction5() {
		final Function5<?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$Function1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction5() {
		final Function5<?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple5;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function6

	@Test
	public void shouldGetMethodTypeOfFunction6() {
		final Function6<?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction6() {
		final Function6<?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$Function1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction6() {
		final Function6<?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple6;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function7

	@Test
	public void shouldGetMethodTypeOfFunction7() {
		final Function7<?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction7() {
		final Function7<?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$Function1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction7() {
		final Function7<?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple7;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function8

	@Test
	public void shouldGetMethodTypeOfFunction8() {
		final Function8<?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction8() {
		final Function8<?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$Function1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction8() {
		final Function8<?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple8;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function9

	@Test
	public void shouldGetMethodTypeOfFunction9() {
		final Function9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction9() {
		final Function9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$Function1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction9() {
		final Function9<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple9;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function10

	@Test
	public void shouldGetMethodTypeOfFunction10() {
		final Function10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction10() {
		final Function10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$Function1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction10() {
		final Function10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple10;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function11

	@Test
	public void shouldGetMethodTypeOfFunction11() {
		final Function11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction11() {
		final Function11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$Function1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction11() {
		final Function11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple11;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function12

	@Test
	public void shouldGetMethodTypeOfFunction12() {
		final Function12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11,
				o12) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction12() {
		final Function12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11,
				o12) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$Function1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction12() {
		final Function12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11,
				o12) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple12;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	// -- Function13

	@Test
	public void shouldGetMethodTypeOfFunction13() {
		final Function13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10,
				o11, o12, o13) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda).get();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedFunction13() {
		final Function13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10,
				o11, o12, o13) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.curried()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;)Ljavaslang/lambda/Functions$Function1;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledFunction13() {
		final Function13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10,
				o11, o12, o13) -> o1;
		final MethodType actual = Lambdas.getLambdaSignature(lambda.tupled()).get();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/collection/Tuple$Tuple13;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}
}
