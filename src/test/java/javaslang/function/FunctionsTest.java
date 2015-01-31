/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.lang.invoke.MethodType;

import javaslang.Tuple;
import javaslang.Tuple3;

import javaslang.control.Try;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class FunctionsTest {

	// -- Lambda0

	@Test
	public void shouldGetMethodTypeOfLambda0() {
		final Lambda0 lambda = () -> null;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("()Ljava/lang/Object;", getClass()
				.getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedLambda0() {
		final Lambda0 lambda = () -> null;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Void;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledLambda0() {
		final Lambda0 lambda = () -> null;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple0;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldEnsureThatX0ImplementsTheLambdaInterface() {
		final CheckedLambda0<Integer> x0 = () -> 1;
		Assertions.assertThat(Try.of(x0::apply).get()).isEqualTo(1);
		assertThat(x0.andThen(i -> i + 1).getType().toString()).isEqualTo("(Function)Object");
		assertThat(x0.arity()).isEqualTo(0);
		assertThat(x0.curried().getType().toString()).isEqualTo("(Void)Object");
		assertThat(x0.tupled().getType().toString()).isEqualTo("(Tuple0)Object");
	}

	@Test
	public void shouldEnsureThatLambda0ImplementsTheLambdaInterface() {
		final Lambda0<Integer> Lambda0 = () -> 1;
		assertThat(Lambda0.get()).isEqualTo(1);
	}

	// -- Lambda1

	@Test
	public void shouldGetMethodTypeOfLambda1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final Lambda1<?, ?> lambda = o -> o;
		final Lambda1 lambda = o -> o;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedLambda1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final Lambda1<?, ?> lambda = o -> o;
		final Lambda1 lambda = o -> o;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledLambda1() {
		// TODO: Does not compile with jdk1.8.0_20-b26/mac: final Lambda1<?, ?> lambda = o -> o;
		final Lambda1 lambda = o -> o;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple1;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldEnsureThatX1ImplementsTheLambdaInterface() {
		final CheckedLambda1<Integer, Integer> x1 = i -> i + 1;
		Assertions.assertThat(Try.of(() -> x1.apply(1)).get()).isEqualTo(2);
		assertThat(x1.andThen(i -> i + 1).getType().toString()).isEqualTo("(Function,Object)Object");
		assertThat(x1.arity()).isEqualTo(1);
		assertThat(x1.curried().getType().toString()).isEqualTo("(Object)Object");
		assertThat(x1.tupled().getType().toString()).isEqualTo("(Tuple1)Object");
		Assertions.assertThat(Try.of(() -> x1.compose(o -> Integer.parseInt(o.toString())).apply("1")).get()).isEqualTo(2);
		assertThat(Try.of(() -> CheckedLambda1.identity().apply(1)).get()).isEqualTo(1);
	}

	@Test
	public void shouldEnsureThatCheckedLambda1IdentityImplementsTheLambdaInterface() {
		assertThat(Try.of(() -> CheckedLambda1.identity().apply(1)).get()).isEqualTo(1);
	}

	@Test
	public void shouldEnsureThatLambda1IdentityImplementsTheLambdaInterface() {
		assertThat(Lambda1.identity().apply(1)).isEqualTo(1);
	}

    @Test
    public void shouldEnsureThatLambda1IdentityCompositionImplementsTheLambdaInterface() {
        assertThat(Lambda1.identity().andThen(Lambda1.identity()).apply(1)).isEqualTo(1);
    }

    // -- Lambda2

	@Test
	public void shouldGetMethodTypeOfLambda2() {
		final Lambda2 lambda = (o1, o2) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedLambda2() {
		final Lambda2 lambda = (o1, o2) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/function/Lambda1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledLambda2() {
		final Lambda2 lambda = (o1, o2) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple2;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldEnsureThatX2ImplementsTheLambdaInterface() {
		final CheckedLambda2<Integer, Integer, Integer> x2 = (i, j) -> i + j;
		Assertions.assertThat(Try.of(() -> x2.apply(1, 1)).get()).isEqualTo(2);
		assertThat(x2.andThen(i -> i + 1).getType().toString()).isEqualTo("(Function,Object,Object)Object");
		assertThat(x2.arity()).isEqualTo(2);
		assertThat(x2.curried().getType().toString()).isEqualTo("(Object)CheckedLambda1");
		assertThat(x2.tupled().getType().toString()).isEqualTo("(Tuple2)Object");
	}

	// -- Lambda3

	@Test
	public void shouldGetMethodTypeOfLambda3() {
		final Lambda3 lambda = (o1, o2, o3) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", getClass()
						.getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedLambda3() {
		final Lambda3 lambda = (o1, o2, o3) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/function/Lambda1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledLambda3() {
		final Lambda3 lambda = (o1, o2, o3) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple3;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldEnsureThatX3ImplementsTheLambdaInterface() {
		final CheckedLambda3<Integer, Integer, Integer, Integer> x3 = (i1, i2, i3) -> i1 + i2 + i3;
		Assertions.assertThat(Try.of(() -> x3.apply(1, 1, 1)).get()).isEqualTo(3);
		assertThat(x3.andThen(i -> i + 1).getType().toString()).isEqualTo("(Function,Object,Object,Object)Object");
		assertThat(x3.arity()).isEqualTo(3);
		assertThat(x3.curried().getType().toString()).isEqualTo("(Object)CheckedLambda1");
		assertThat(x3.tupled().getType().toString()).isEqualTo("(Tuple3)Object");
	}

	// -- Lambda4

	@Test
	public void shouldGetMethodTypeOfLambda4() {
		final Lambda4 lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedLambda4() {
		final Lambda4 lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/function/Lambda1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledLambda4() {
		final Lambda4 lambda = (o1, o2, o3, o4) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple4;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldEnsureThatX4ImplementsTheLambdaInterface() {
		final CheckedLambda4<Integer, Integer, Integer, Integer, Integer> x = (i1, i2, i3, i4) -> i1 + i2 + i3 + i4;
		Assertions.assertThat(Try.of(() -> x.apply(1, 1, 1, 1)).get()).isEqualTo(4);
		assertThat(x.andThen(i -> i + 1).getType().toString()).isEqualTo("(Function,Object,Object,Object,Object)Object");
		assertThat(x.arity()).isEqualTo(4);
		assertThat(x.curried().getType().toString()).isEqualTo("(Object)CheckedLambda1");
		assertThat(x.tupled().getType().toString()).isEqualTo("(Tuple4)Object");
	}

	// -- Lambda5

	@Test
	public void shouldGetMethodTypeOfLambda5() {
		final Lambda5 lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedLambda5() {
		final Lambda5 lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/function/Lambda1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledLambda5() {
		final Lambda5 lambda = (o1, o2, o3, o4, o5) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple5;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldEnsureThatX5ImplementsTheLambdaInterface() {
		final CheckedLambda5<Integer, Integer, Integer, Integer, Integer, Integer> x = (i1, i2, i3, i4, i5) -> i1 + i2 + i3 + i4 + i5;
		Assertions.assertThat(Try.of(() -> x.apply(1, 1, 1, 1, 1)).get()).isEqualTo(5);
		assertThat(x.andThen(i -> i + 1).getType().toString()).isEqualTo("(Function,Object,Object,Object,Object,Object)Object");
		assertThat(x.arity()).isEqualTo(5);
		assertThat(x.curried().getType().toString()).isEqualTo("(Object)CheckedLambda1");
		assertThat(x.tupled().getType().toString()).isEqualTo("(Tuple5)Object");
	}

	// -- Lambda6

	@Test
	public void shouldGetMethodTypeOfLambda6() {
		final Lambda6 lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedLambda6() {
		final Lambda6 lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/function/Lambda1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledLambda6() {
		final Lambda6 lambda = (o1, o2, o3, o4, o5, o6) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple6;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldEnsureThatX6ImplementsTheLambdaInterface() {
		final CheckedLambda6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> x = (i1, i2, i3, i4, i5, i6) -> i1 + i2 + i3 + i4 + i5 + i6;
		Assertions.assertThat(Try.of(() -> x.apply(1, 1, 1, 1, 1, 1)).get()).isEqualTo(6);
		assertThat(x.andThen(i -> i + 1).getType().toString()).isEqualTo("(Function,Object,Object,Object,Object,Object,Object)Object");
		assertThat(x.arity()).isEqualTo(6);
		assertThat(x.curried().getType().toString()).isEqualTo("(Object)CheckedLambda1");
		assertThat(x.tupled().getType().toString()).isEqualTo("(Tuple6)Object");
	}

	// -- Lambda7

	@Test
	public void shouldGetMethodTypeOfLambda7() {
		final Lambda7 lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedLambda7() {
		final Lambda7 lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/function/Lambda1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledLambda7() {
		final Lambda7 lambda = (o1, o2, o3, o4, o5, o6, o7) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple7;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldEnsureThatX7ImplementsTheLambdaInterface() {
		final CheckedLambda7<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> x =
				(i1, i2, i3, i4, i5, i6, i7) -> i1 + i2 + i3 + i4 + i5 + i6 + i7;
		Assertions.assertThat(Try.of(() -> x.apply(1, 1, 1, 1, 1, 1, 1)).get()).isEqualTo(7);
		assertThat(x.andThen(i -> i + 1).getType().toString()).isEqualTo("(Function,Object,Object,Object,Object,Object,Object,Object)Object");
		assertThat(x.arity()).isEqualTo(7);
		assertThat(x.curried().getType().toString()).isEqualTo("(Object)CheckedLambda1");
		assertThat(x.tupled().getType().toString()).isEqualTo("(Tuple7)Object");
	}

	// -- Lambda8

	@Test
	public void shouldGetMethodTypeOfLambda8() {
		final Lambda8 lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedLambda8() {
		final Lambda8 lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/function/Lambda1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledLambda8() {
		final Lambda8 lambda = (o1, o2, o3, o4, o5, o6, o7, o8) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple8;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldEnsureThatX8ImplementsTheLambdaInterface() {
		final CheckedLambda8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> x =
				(i1, i2, i3, i4, i5, i6, i7, i8) -> i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8;
		Assertions.assertThat(Try.of(() -> x.apply(1, 1, 1, 1, 1, 1, 1, 1)).get()).isEqualTo(8);
		assertThat(x.andThen(i -> i + 1).getType().toString()).isEqualTo("(Function,Object,Object,Object,Object,Object,Object,Object,Object)Object");
		assertThat(x.arity()).isEqualTo(8);
		assertThat(x.curried().getType().toString()).isEqualTo("(Object)CheckedLambda1");
		assertThat(x.tupled().getType().toString()).isEqualTo("(Tuple8)Object");
	}

	// -- Lambda9

	@Test
	public void shouldGetMethodTypeOfLambda9() {
		final Lambda9 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedLambda9() {
		final Lambda9 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/function/Lambda1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledLambda9() {
		final Lambda9 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple9;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldEnsureThatX9ImplementsTheLambdaInterface() {
		final CheckedLambda9<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> x =
				(i1, i2, i3, i4, i5, i6, i7, i8, i9) -> i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8 + i9;
		Assertions.assertThat(Try.of(() -> x.apply(1, 1, 1, 1, 1, 1, 1, 1, 1)).get()).isEqualTo(9);
		assertThat(x.andThen(i -> i + 1).getType().toString())
				.isEqualTo("(Function,Object,Object,Object,Object,Object,Object,Object,Object,Object)Object");
		assertThat(x.arity()).isEqualTo(9);
		assertThat(x.curried().getType().toString()).isEqualTo("(Object)CheckedLambda1");
		assertThat(x.tupled().getType().toString()).isEqualTo("(Tuple9)Object");
	}

	// -- Lambda10

	@Test
	public void shouldGetMethodTypeOfLambda10() {
		final Lambda10 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedLambda10() {
		final Lambda10 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/function/Lambda1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledLambda10() {
		final Lambda10 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple10;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldEnsureThatX10ImplementsTheLambdaInterface() {
		final CheckedLambda10<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> x =
				(i1, i2, i3, i4, i5, i6, i7, i8, i9, i10) -> i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8 + i9 + i10;
		Assertions.assertThat(Try.of(() -> x.apply(1, 1, 1, 1, 1, 1, 1, 1, 1, 1)).get()).isEqualTo(10);
		assertThat(x.andThen(i -> i + 1).getType().toString())
				.isEqualTo("(Function,Object,Object,Object,Object,Object,Object,Object,Object,Object,Object)Object");
		assertThat(x.arity()).isEqualTo(10);
		assertThat(x.curried().getType().toString()).isEqualTo("(Object)CheckedLambda1");
		assertThat(x.tupled().getType().toString()).isEqualTo("(Tuple10)Object");
	}

	// -- Lambda11

	@Test
	public void shouldGetMethodTypeOfLambda11() {
		final Lambda11 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedLambda11() {
		final Lambda11 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/function/Lambda1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledLambda11() {
		final Lambda11 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple11;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldEnsureThatX11ImplementsTheLambdaInterface() {
		final CheckedLambda11<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> x =
				(i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11) -> i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8 + i9 + i10 + i11;
		Assertions.assertThat(Try.of(() -> x.apply(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)).get()).isEqualTo(11);
		assertThat(x.andThen(i -> i + 1).getType().toString())
				.isEqualTo("(Function,Object,Object,Object,Object,Object,Object,Object,Object,Object,Object,Object)Object");
		assertThat(x.arity()).isEqualTo(11);
		assertThat(x.curried().getType().toString()).isEqualTo("(Object)CheckedLambda1");
		assertThat(x.tupled().getType().toString()).isEqualTo("(Tuple11)Object");
	}

	// -- Lambda12

	@Test
	public void shouldGetMethodTypeOfLambda12() {
		final Lambda12 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedLambda12() {
		final Lambda12 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/function/Lambda1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledLambda12() {
		final Lambda12 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple12;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldEnsureThatX12ImplementsTheLambdaInterface() {
		final CheckedLambda12<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> x =
				(i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12) -> i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8 + i9 + i10 + i11 + i12;
		Assertions.assertThat(Try.of(() -> x.apply(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)).get()).isEqualTo(12);
		assertThat(x.andThen(i -> i + 1).getType().toString())
				.isEqualTo("(Function,Object,Object,Object,Object,Object,Object,Object,Object,Object,Object,Object,Object)Object");
		assertThat(x.arity()).isEqualTo(12);
		assertThat(x.curried().getType().toString()).isEqualTo("(Object)CheckedLambda1");
		assertThat(x.tupled().getType().toString()).isEqualTo("(Tuple12)Object");
	}

	// -- Lambda13

	@Test
	public void shouldGetMethodTypeOfLambda13() {
		final Lambda13 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11,
				o12, o13) -> o1;
		final MethodType actual = lambda.getType();
		final MethodType expected = MethodType
				.fromMethodDescriptorString(
						"(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
						getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfCurriedLambda13() {
		final Lambda13 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11,
				o12, o13) -> o1;
		final MethodType actual = lambda.curried().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Ljavaslang/function/Lambda1;",
				getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldGetMethodTypeOfTupledLambda13() {
		final Lambda13 lambda = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11,
				o12, o13) -> o1;
		final MethodType actual = lambda.tupled().getType();
		final MethodType expected = MethodType.fromMethodDescriptorString(
				"(Ljavaslang/Tuple13;)Ljava/lang/Object;", getClass().getClassLoader());
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldEnsureThatX13ImplementsTheLambdaInterface() {
		final CheckedLambda13<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> x =
				(i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13) -> i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8 + i9 + i10 + i11 + i12 + i13;
		Assertions.assertThat(Try.of(() -> x.apply(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)).get()).isEqualTo(13);
		assertThat(x.andThen(i -> i + 1).getType().toString())
				.isEqualTo("(Function,Object,Object,Object,Object,Object,Object,Object,Object,Object,Object,Object,Object,Object)Object");
		assertThat(x.arity()).isEqualTo(13);
		assertThat(x.curried().getType().toString()).isEqualTo("(Object)CheckedLambda1");
		assertThat(x.tupled().getType().toString()).isEqualTo("(Tuple13)Object");
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
