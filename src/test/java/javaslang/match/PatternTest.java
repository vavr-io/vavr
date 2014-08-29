/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import static javaslang.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.function.Function;

import javaslang.Tuples;
import javaslang.Tuples.Tuple2;
import javaslang.lambda.SerializableFunction;

import org.junit.Test;

public class PatternTest {

	@Test
	public void shouldNotInstantiable() {
		assertThat(Patterns.class).isNotInstantiable();
	}

	@Test
	public void shouldMatchFunctionWithoutCapturedArgsBySignature() {
		final SerializableFunction<Integer, String> function = i -> String.valueOf(i);
		final Tuple2<SerializableFunction<Integer, String>, Tuple2<Class<Integer>, Class<String>>> match = Patterns
				.Function(Integer.class, String.class)
				.apply(function)
				.get();
		final Tuple2<Class<Integer>, Class<String>> decomposition = match._2;
		assertThat(decomposition).isEqualTo(Tuples.of(Integer.class, String.class));
	}

	@Test
	public void shouldMatchFunctionWithCapturedArgsBySignature() {
		final Function<Integer, String> f = i -> String.valueOf(i);
		final SerializableFunction<Integer, String> function = i -> f.apply(i);
		final Tuple2<SerializableFunction<Integer, String>, Tuple2<Class<Integer>, Class<String>>> match = Patterns
				.Function(Integer.class, String.class)
				.apply(function)
				.get();
		final Tuple2<Class<Integer>, Class<String>> decomposition = match._2;
		assertThat(decomposition).isEqualTo(Tuples.of(Integer.class, String.class));
	}
}
