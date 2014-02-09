package io.rocketscience.java.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class Streams {

	private Streams() {
		throw new AssertionError(Streams.class.getName() + " cannot be instantiated.");
	}

	/**
	 * Maps a source stream using the given mapper while the given condition holds. The elements are processed in the
	 * encounter order of the source stream if the stream has a defined encounter order.<br>
	 * <br>
	 * Please note that we use the name map<em>While</em> because the mapping is performed before <em>after</em> test.
	 * If the test is false, the mapped element is <em>not</em> part of the result stream.
	 * 
	 * @see java.util.Stream#forEachOrdered(java.util.function.Consumer)
	 * 
	 * @param source A source Stream.
	 * @param f A mapping function.
	 * @param condition A condition, tested with the source element.
	 * @return The mapped stream, which is parallel if the source stream is parallel.
	 */
	public static <T, R> Stream<R> mapWhile(Stream<T> source, Function<? super T, ? extends R> mapper,
			Predicate<? super T> condition) {
		final List<R> result = new ArrayList<>();
		try {
			source.forEachOrdered(elem -> {
				if (condition.test(elem)) {
					result.add(mapper.apply(elem));
				} else {
					// this is a constly operation. alternatives?
					throw new Break();
				}
			});
		} catch (Break x) {
			// ok
		}
		return source.isParallel() ? result.parallelStream() : result.stream();
	}

	/**
	 * Maps a source stream using the given mapper until the given condition holds for a mapping result. The elements
	 * are processed in the encounter order of the source stream if the stream has a defined encounter order.<br>
	 * <br>
	 * Please note that we use the name map<em>Until</em> because the mapping is performed <em>before</em> the test.
	 * However, if the test is false, the mapped element is <em>not</em> part of the result stream.
	 *
	 * @see java.util.Stream#forEachOrdered(java.util.function.Consumer)
	 * 
	 * @param source A source Stream.
	 * @param f A mapping function.
	 * @param condition A condition, tested with the mapped element.
	 * @return The mapped stream, which is parallel if the source stream is parallel.
	 */
	public static <T, R> Stream<R> mapUntil(Stream<T> source, Function<? super T, ? extends R> mapper,
			Predicate<? super R> condition) {
		final List<R> result = new ArrayList<>();
		try {
			source.forEachOrdered(elem -> {
				final R mapped = mapper.apply(elem);
				if (condition.test(mapped)) {
					result.add(mapped);
				} else {
					// this is a constly operation. alternatives?
					throw new Break();
				}
			});
		} catch (Break x) {
			// ok
		}
		return source.isParallel() ? result.parallelStream() : result.stream();
	}

	// Used as workaround to break an internal Stream loop
	private static class Break extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

}
