/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static javaslang.Requirements.require;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import javaslang.Tuples.Tuple2;

/**
 * Extension methods for {@link java.util.stream.Stream}.
 */
public final class Streamz {

	/**
	 * This class is not intended to be instantiated.
	 */
	private Streamz() {
		throw new AssertionError(Streamz.class.getName() + " is not intended to be instantiated.");
	}

	/**
	 * Zips the elements of a given Stream to pairs {@code (element, index)}, where index is the occurrence within the
	 * original Stream, starting with 0.
	 * 
	 * @param <T> the type of the stream elements
	 * @param stream A {@linkplain java.util.stream.Stream}.
	 * @return A zipped stream containing elements of type {@link javaslang.Tuples.Tuple2} and values
	 *         {@code (index, element)}. The resulting Stream is parallel if, and only if, the given stream is parallel.
	 */
	public static <T> Stream<Tuple2<T, Integer>> zipWithIndex(Stream<T> stream) {
		require(!stream.isParallel(), "stream is parallel");
		final AtomicInteger index = new AtomicInteger();
		return stream.map(e -> Tuples.of(e, index.getAndIncrement()));
	}

	/**
	 * Convenience method for {@code Streamz.zipWithIndex(stream.boxed())}.
	 * 
	 * @param stream A {@linkplain java.util.stream.DoubleStream}.
	 * @return A zipped stream with index. The original stream is boxed.
	 * @see Streamz#zipWithIndex(Stream)
	 */
	public static Stream<Tuple2<Double, Integer>> zipWithIndex(DoubleStream stream) {
		require(!stream.isParallel(), "stream is parallel");
		return zipWithIndex(stream.boxed());
	}

	/**
	 * Convenience method for {@code Streamz.zipWithIndex(stream.boxed())}.
	 * 
	 * @param stream A {@linkplain java.util.stream.IntStream}.
	 * @return A zipped stream with index. The original stream is boxed.
	 * @see Streamz#zipWithIndex(Stream)
	 */
	public static Stream<Tuple2<Integer, Integer>> zipWithIndex(IntStream stream) {
		require(!stream.isParallel(), "stream is parallel");
		return zipWithIndex(stream.boxed());
	}

	/**
	 * Convenience method for {@code Streamz.zipWithIndex(stream.boxed())}.
	 * 
	 * @param stream A {@linkplain java.util.stream.LongStream}.
	 * @return A zipped stream with index. The original stream is boxed.
	 * @see Streamz#zipWithIndex(Stream)
	 */
	public static Stream<Tuple2<Long, Integer>> zipWithIndex(LongStream stream) {
		require(!stream.isParallel(), "stream is parallel");
		return zipWithIndex(stream.boxed());
	}

}
