/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import javaslang.AssertionsExtensions;
import javaslang.Tuple;
import javaslang.Lambda.λ1;
import javaslang.Tuple.Tuple2;

import org.junit.Test;

public class PatternTest {

	@Test
	public void shouldNotBeInstantiable() {
		AssertionsExtensions.assertThat(Patterns.class).isNotInstantiable();
	}

	// -- pattern creation

	/*
	 * @Test public void shouldCreatePatternOfArity1() { assertThat(Pattern.of(t -> null, Tuples.of(1))).isNotNull(); }
	 * @Test public void shouldCreatePatternOfArity2() { assertThat(Pattern.of(t -> null, Tuples.of(1, 2))).isNotNull();
	 * }
	 * @Test public void shouldCreatePatternOfArity3() { assertThat(Pattern.of(t -> null, Tuples.of(1, 2,
	 * 3))).isNotNull(); }
	 * @Test public void shouldCreatePatternOfArity4() { assertThat(Pattern.of(t -> null, Tuples.of(1, 2, 3,
	 * 4))).isNotNull(); }
	 * @Test public void shouldCreatePatternOfArity5() { assertThat(Pattern.of(t -> null, Tuples.of(1, 2, 3, 4,
	 * 5))).isNotNull(); }
	 * @Test public void shouldCreatePatternOfArity6() { assertThat(Pattern.of(t -> null, Tuples.of(1, 2, 3, 4, 5,
	 * 6))).isNotNull(); }
	 * @Test public void shouldCreatePatternOfArity7() { assertThat(Pattern.of(t -> null, Tuples.of(1, 2, 3, 4, 5, 6,
	 * 7))).isNotNull(); }
	 * @Test public void shouldCreatePatternOfArity8() { assertThat(Pattern.of(t -> null, Tuples.of(1, 2, 3, 4, 5, 6, 7,
	 * 8))).isNotNull(); }
	 * @Test public void shouldCreatePatternOfArity9() { assertThat(Pattern.of(t -> null, Tuples.of(1, 2, 3, 4, 5, 6, 7,
	 * 8, 9))).isNotNull(); }
	 * @Test public void shouldCreatePatternOfArity10() { assertThat(Pattern.of(t -> null, Tuples.of(1, 2, 3, 4, 5, 6,
	 * 7, 8, 9, 10))).isNotNull(); }
	 * @Test public void shouldCreatePatternOfArity11() { assertThat(Pattern.of(t -> null, Tuples.of(1, 2, 3, 4, 5, 6,
	 * 7, 8, 9, 10, 11))).isNotNull(); }
	 * @Test public void shouldCreatePatternOfArity12() { assertThat(Pattern.of(t -> null, Tuples.of(1, 2, 3, 4, 5, 6,
	 * 7, 8, 9, 10, 11, 12))).isNotNull(); }
	 * @Test public void shouldCreatePatternOfArity13() { assertThat(Pattern.of(t -> null, Tuples.of(1, 2, 3, 4, 5, 6,
	 * 7, 8, 9, 10, 11, 12, 13))).isNotNull(); }
	 */

	// -- pattern matching

	@Test
	// DEV-NOTE: implemented to satisfy code coverage
	public void shouldComparePrototype() {
		assertThat(new Patterns.UnaryPrototype(o -> true).equals(new Object())).isTrue();
	}

	@Test
	public void shouldMatchFunctionWithoutCapturedArgsBySignature() {
		final λ1<Integer, String> function = i -> String.valueOf(i);
		final Tuple2<λ1<Integer, String>, Tuple2<Class<Integer>, Class<String>>> match = Patterns
				.Function(Integer.class, String.class)
				.apply(function)
				.get();
		final Tuple2<Class<Integer>, Class<String>> decomposition = match._2;
		assertThat(decomposition).isEqualTo(Tuple.of(Integer.class, String.class));
	}

	@Test
	public void shouldMatchFunctionWithCapturedArgsBySignature() {
		final Function<Integer, String> f = i -> String.valueOf(i);
		final λ1<Integer, String> function = i -> f.apply(i);
		final Tuple2<λ1<Integer, String>, Tuple2<Class<Integer>, Class<String>>> match = Patterns
				.Function(Integer.class, String.class)
				.apply(function)
				.get();
		final Tuple2<Class<Integer>, Class<String>> decomposition = match._2;
		assertThat(decomposition).isEqualTo(Tuple.of(Integer.class, String.class));
	}
}
