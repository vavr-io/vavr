/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.function.Function;

import javaslang.Tuples;
import javaslang.Tuples.Tuple2;

import org.junit.Ignore;
import org.junit.Test;

public class PatternTest {

	// TODO: depends on Issue #11
	@Test
	@Ignore
	public void shouldMatchFunctionBySignature() {
		final Function<Integer, String> function = i -> String.valueOf(i);
		final Pattern<Function<Integer, String>, Tuple2<Class<Integer>, Class<String>>, Tuple2<Class<Integer>, Class<String>>> pattern = Patterns
				.Function(Integer.class, String.class);
		final Tuple2<Function<Integer, String>, Tuple2<Class<Integer>, Class<String>>> match = pattern
				.apply(function)
				.get();
		final Tuple2<Class<Integer>, Class<String>> decomposition = match._2;
		assertThat(decomposition).isEqualTo(Tuples.of(Integer.class, String.class));
	}
}
