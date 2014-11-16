/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

public class StreamTest {

	// TODO
	// private static final String[] I_WILL_BE_BACK = { "I", "will", "be", "back!" };

	// -- stream of Iterable

	@Test
	public void shouldCreateSequentialStreamOfIterable() {
		assertThat(Stream.of(Arrays.asList()).isParallel()).isFalse();
	}

	// -- zipWithIndex

	@Test
	@Ignore
	public void shouldZipObjectStreamWithIndex() {
		// TODO
		//		final java.util.stream.Stream<String> stream = java.util.stream.Stream.of(I_WILL_BE_BACK);
		//		final List<Tuple2<String, Integer>> actual = Stream.of(stream).zipWithIndex().collect(Collectors.toList());
		//		final List<Tuple2<String, Integer>> expected = Arrays.asList(Tuple.of("I", 0), Tuple.of("will", 1),
		//				Tuple.of("be", 2), Tuple.of("back!", 3));
		//		assertThat(actual).isEqualTo(expected);
	}

	// zip vs. privitive types

	@Test
	@Ignore
	public void shouldZipDoubleStreamWithIndex() {
		// TODO
		//		final DoubleStream stream = DoubleStream.of(1.4142, 2.7182, 3.1415);
		//		final List<Tuple2<Double, Integer>> actual = Stream.of(stream).zipWithIndex().collect(toList());
		//		final List<Tuple2<Double, Integer>> expected = Arrays.asList(Tuple.of(1.4142, 0), Tuple.of(2.7182, 1),
		//				Tuple.of(3.1415, 2));
		//		assertThat(actual).isEqualTo(expected);
	}

	@Test
	@Ignore
	public void shouldZipIntStreamWithIndex() {
		// TODO
		//		final IntStream stream = IntStream.of(1, 2, 3);
		//		final List<Tuple2<Integer, Integer>> actual = Stream.of(stream).zipWithIndex().collect(toList());
		//		final List<Tuple2<Integer, Integer>> expected = Arrays.asList(Tuple.of(1, 0), Tuple.of(2, 1), Tuple.of(3, 2));
		//		assertThat(actual).isEqualTo(expected);
	}

	@Test
	@Ignore
	public void shouldZipLongStreamWithIndex() {
		// TODO
		//		final LongStream stream = LongStream.of(1, 2, 3);
		//		final List<Tuple2<Long, Integer>> actual = Stream.of(stream).zipWithIndex().collect(toList());
		//		final List<Tuple2<Long, Integer>> expected = Arrays.asList(Tuple.of(1L, 0), Tuple.of(2L, 1), Tuple.of(3L, 2));
		//		assertThat(actual).isEqualTo(expected);
	}

	// zip vs. parallel/sequential

	@Test
	@Ignore
	public void shouldZipSequentialStream() {
		// TODO
		//		final java.util.stream.Stream<String> stream = java.util.stream.Stream.of(I_WILL_BE_BACK);
		//		assertThat(Stream.of(stream).zipWithIndex().isParallel()).isFalse();
	}
}
