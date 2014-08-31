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
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import javaslang.Requirements.UnsatisfiedRequirementException;
import javaslang.Tuples.Tuple2;

import org.junit.Test;

public class StreamzTest {

	private static final String[] I_WILL_BE_BACK = { "I", "will", "be", "back!" };

	@Test
	public void shouldNotInstantiable() {
		AssertionsExtensions.assertThat(Streamz.class).isNotInstantiable();
	}

	// -- stream of Iterable

	@Test
	public void shouldCreateSequentialStreamOfIterable() {
		assertThat(Streamz.stream(Arrays.asList()).isParallel()).isFalse();
	}

	@Test
	public void shouldCreateParallelStreamOfIterable() {
		assertThat(Streamz.parallelStream(Arrays.asList()).isParallel()).isTrue();
	}

	// -- zipWithIndex

	@Test
	public void shouldZipObjectStreamWithIndex() {
		final Stream<String> stream = Stream.of(I_WILL_BE_BACK);
		final List<Tuple2<String, Integer>> actual = Streamz.zipWithIndex(stream).collect(toList());
		final List<Tuple2<String, Integer>> expected = Arrays.asList(Tuples.of("I", 0), Tuples.of("will", 1),
				Tuples.of("be", 2), Tuples.of("back!", 3));
		assertThat(actual).isEqualTo(expected);
	}

	// zip vs. privitive types

	@Test
	public void shouldZipDoubleStreamWithIndex() {
		final DoubleStream stream = DoubleStream.of(1.4142, 2.7182, 3.1415);
		final List<Tuple2<Double, Integer>> actual = Streamz.zipWithIndex(stream).collect(toList());
		final List<Tuple2<Double, Integer>> expected = Arrays.asList(Tuples.of(1.4142, 0), Tuples.of(2.7182, 1),
				Tuples.of(3.1415, 2));
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldZipIntStreamWithIndex() {
		final IntStream stream = IntStream.of(1, 2, 3);
		final List<Tuple2<Integer, Integer>> actual = Streamz.zipWithIndex(stream).collect(toList());
		final List<Tuple2<Integer, Integer>> expected = Arrays
				.asList(Tuples.of(1, 0), Tuples.of(2, 1), Tuples.of(3, 2));
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldZipLongStreamWithIndex() {
		final LongStream stream = LongStream.of(1, 2, 3);
		final List<Tuple2<Long, Integer>> actual = Streamz.zipWithIndex(stream).collect(toList());
		final List<Tuple2<Long, Integer>> expected = Arrays
				.asList(Tuples.of(1L, 0), Tuples.of(2L, 1), Tuples.of(3L, 2));
		assertThat(actual).isEqualTo(expected);
	}

	// zip vs. parallel/sequential

	@Test
	public void shouldZipSequentialStream() {
		final Stream<String> stream = Stream.of(I_WILL_BE_BACK);
		assertThat(Streamz.zipWithIndex(stream).isParallel()).isFalse();
	}

	@Test
	public void shouldThrowWhenZipWithParallelStream() {
		final Stream<String> stream = Stream.of(I_WILL_BE_BACK).parallel();
		AssertionsExtensions.assertThat(() -> Streamz.zipWithIndex(stream)).isThrowing(
				UnsatisfiedRequirementException.class, "stream is parallel");
	}
}
