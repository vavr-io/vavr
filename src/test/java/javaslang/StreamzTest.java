/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static java.util.stream.Collectors.toList;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javaslang.Tuples.Tuple2;

import org.junit.Test;

public class StreamzTest {

	private static final String[] I_WILL_BE_BACK = { "I", "will", "be", "back!" };

	@Test
	public void shouldZipStreamWithIndex() {
		final Stream<String> stream = Stream.of(I_WILL_BE_BACK);
		final List<Tuple2<Integer, String>> actual = Streamz.zipWithIndex(stream).collect(toList());
		final List<Tuple2<Integer, String>> expected = Arrays.asList(Tuples.of(0, "I"), Tuples.of(1, "will"),
				Tuples.of(2, "be"), Tuples.of(3, "back!"));
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldZipStreamWithIndexPreservingSequentialState() {
		final Stream<String> stream = Stream.of(I_WILL_BE_BACK);
		assertThat(Streamz.zipWithIndex(stream).isParallel()).isFalse();
	}

	@Test
	public void shouldZipStreamWithIndexPreservingParallelState() {
		final Stream<String> stream = Stream.of(I_WILL_BE_BACK).parallel();
		assertThat(Streamz.zipWithIndex(stream).isParallel()).isTrue();
	}

}
