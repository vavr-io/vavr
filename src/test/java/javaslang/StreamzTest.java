/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javaslang.Requirements.UnsatisfiedRequirementException;
import javaslang.Tuples.Tuple2;

import org.junit.Test;

public class StreamzTest {

	private static final String[] I_WILL_BE_BACK = { "I", "will", "be", "back!" };

	@Test
	public void shouldNotInstantiable() {
		AssertJExtensions.assertThat(Streamz.class).isNotInstantiable();
	}

	@Test
	public void shouldZipStreamWithIndex() {
		final Stream<String> stream = Stream.of(I_WILL_BE_BACK);
		final List<Tuple2<String, Integer>> actual = Streamz.zipWithIndex(stream).collect(toList());
		final List<Tuple2<String, Integer>> expected = Arrays.asList(Tuples.of("I", 0), Tuples.of("will", 1),
				Tuples.of("be", 2), Tuples.of("back!", 3));
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldZipSequentialStream() {
		final Stream<String> stream = Stream.of(I_WILL_BE_BACK);
		assertThat(Streamz.zipWithIndex(stream).isParallel()).isFalse();
	}

	@Test
	public void shouldThrowWhenZipWithParallelStream() {
		final Stream<String> stream = Stream.of(I_WILL_BE_BACK).parallel();
		AssertJExtensions.assertThat(() -> Streamz.zipWithIndex(stream)).isThrowing(
				UnsatisfiedRequirementException.class, "stream is parallel");
	}
}
