/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import static org.fest.assertions.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.function.Function;

import javaslang.option.Some;

import org.junit.Test;

public class MatchTest {

	@Test
	public void shouldMatchNullAsPrototype() {
		final int actual = Matchs.caze((String s) -> s.length()).caze(null, o -> 1).apply(null);
		assertThat(actual).isEqualTo(1);
	}

	@Test(expected = MatchError.class)
	public void shouldNotMatchNullAsType() {
		Matchs.caze((int i) -> false).caze((Integer i) -> true).apply((Integer) null);
	}

	@Test
	public void shouldMatchByValuesUsingFunction() {
		final int actual = Matchs.caze("1", (String s) -> 1).apply("1");
		assertThat(actual).isEqualTo(1);
	}

	@Test(expected = MatchError.class)
	public void shouldThrowOnNoMatchByValue() {
		Matchs.caze("1", o -> 1).apply("2");
	}

	@Test
	public void shouldMatchByValueOnMultipleCases() {
		final int actual = Matchs.caze("1", o -> 1).caze("2", o -> 2).caze("3", o -> 3).apply("2");
		assertThat(actual).isEqualTo(2);
	}

	@Test
	public void shouldMatchByDoubleOnMultipleCasesUsingTypedParameter() {
		final int actual = Matchs
				.caze((Byte b) -> 1)
				.caze((Double d) -> 2)
				.caze((Integer i) -> 3)
				.apply(1.0d);
		assertThat(actual).isEqualTo(2);
	}

	@Test
	public void shouldMatchByIntOnMultipleCasesUsingTypedParameter() {
		final int actual = Matchs
				.caze((Byte b) -> (int) b)
				.caze((Double d) -> d.intValue())
				.caze((Integer i) -> i)
				.apply(Integer.MAX_VALUE);
		assertThat(actual).isEqualTo(Integer.MAX_VALUE);
	}

	@Test
	public void shouldMatchByAssignableTypeOnMultipleCases() {
		final int actual = Matchs
				.caze(1, o -> 'a')
				.caze((Number n) -> 'b')
				.caze((Object o) -> 'c')
				.apply(2.0d);
		assertThat(actual).isEqualTo('b');
	}

	@Test
	public void shouldMatchDefaultCase() {
		final int actual = Matchs.caze(null, o -> 1).orElse(() -> 2).apply("default");
		assertThat(actual).isEqualTo(2);
	}

	@Test
	public void shouldClarifyHereThatTypeErasureIsPresent() {
		final int actual = Matchs
				.caze((Some<Integer> some) -> 1)
				.caze((Some<String> some) -> Integer.parseInt(some.get()))
				.apply(new Some<>("123"));
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldMatchPrimitiveInt() {
		final boolean actual = Matchs.caze((int i) -> true).caze((Integer i) -> false).apply(1);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchBoxedPrimitiveIntAsInteger() {
		final boolean actual = Matchs.caze((Integer i) -> true).caze((int i) -> false).apply(1);
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchIntegerAsPrimitiveInt() {
		final boolean actual = Matchs
				.caze((int i) -> true)
				.caze((Integer i) -> false)
				.apply(new Integer(1));
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchInteger() {
		final boolean actual = Matchs
				.caze((Integer i) -> true)
				.caze((int i) -> false)
				.apply(new Integer(1));
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldMatchPrimitiveBooleanValueAndApplyBooleanFunction() {
		final int actual = Matchs.caze(true, b -> 1).caze(Boolean.TRUE, b -> 2).apply(true);
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldMatchPrimitiveBooleanValueAsBooleanAndApplyBooleanFunction() {
		final int actual = Matchs.caze(Boolean.TRUE, b -> 1).caze(true, b -> 2).apply(true);
		assertThat(actual).isEqualTo(1);
	}

	// TODO: add tests for all primitive types + new Object()

	@Test
	public void shouldCompileObjectIntegerPrototypeCase() {
		// This does *not* compile: new Match.Builder<>().caze(1, (int i) -> i);
		// Use this instead: Match.Builder<>().caze(1, i -> i);
		new Match.Builder<>().caze(1, (Integer i) -> i);
	}

	@Test
	public void shouldCompileUnqualifiedIntegerPrototypeCase() {
		new Match.Builder<>().caze(1, i -> i);
	}

	@Test
	public void shouldMatchBooleanArray() {
		final int actual = new Match.Builder<Integer>()
				.caze((boolean[] b) -> 1)
				.build()
				.apply(new boolean[] { true });
		assertThat(actual).isEqualTo(1);
	}

	@Test
	public void shouldAllowCommonReturnTypeUsingBuilder() {
		final Match<Number> toNumber = new Match.Builder<Number>()
				.caze((Integer i) -> i)
				.caze((String s) -> new BigDecimal(s))
				.build();
		final Number number = toNumber.apply("1.0E10");
		assertThat(number).isEqualTo(new BigDecimal("1.0E10"));
	}

	@Test
	public void shouldAllowCommonReturnTypeUsingBuilderAndPrototype() {
		final Match<Number> toNumber = new Match.Builder<Number>()
				.caze(1, (Integer i) -> i)
				.caze("1", (String s) -> new BigDecimal(s))
				.build();
		final Number number = toNumber.apply("1");
		assertThat(number).isEqualTo(new BigDecimal("1"));
	}
	
	@Test
	public void shouldAllowCommonReturnTypeUsingMatchs() {
		final Match<Number> toNumber = Matchs
				.<Number> caze((Integer i) -> i)
				.caze((String s) -> new BigDecimal(s))
				.build();
		final Number number = toNumber.apply("1");
		assertThat(number).isEqualTo(new BigDecimal("1"));
	}
	
	@Test
	public void shouldAllowCommonReturnTypeUsingMatchsWithPrototype() {
		final Match<Number> toNumber = Matchs
				.<Integer, Number> caze(1, (Integer i) -> i)
				.caze("1", (String s) -> new BigDecimal(s))
				.build();
		final Number number = toNumber.apply("1");
		assertThat(number).isEqualTo(new BigDecimal("1"));
	}
	
	@Test
	public void shouldMatchLambdaConsideringTypeHierarchy() {
		final SpecialFunction lambda = i -> String.valueOf(i);
		final String actual = Matchs
				.caze((SameSignatureAsSpecialFunction f) -> f.apply(1))
				.caze((Function<Integer, String> f) -> f.apply(2))
				.apply(lambda);
		assertThat(actual).isEqualTo("2");
	}

	@FunctionalInterface
	static interface SpecialFunction extends Function<Integer, String> {
		String apply(Integer i);
	}

	@FunctionalInterface
	static interface SameSignatureAsSpecialFunction extends Function<Integer, String> {
		String apply(Integer i);
	}

}
