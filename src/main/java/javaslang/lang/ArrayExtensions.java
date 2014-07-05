/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lang;

import static javaslang.lang.Lang.requireNonNull;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javaslang.match.Match;

/**
 * Additions to {@link java.util.Arrays}.
 */
public final class ArrayExtensions {

	private static final Match<Stream<?>> ARRAY_TO_STREAM_MATCHER = new Match<Stream<?>>()
			.caze((boolean[] a) -> stream(a))
			.caze((byte[] a) -> stream(a))
			.caze((char[] a) -> stream(a))
			.caze((double[] a) -> stream(a))
			.caze((float[] a) -> stream(a))
			.caze((int[] a) -> stream(a))
			.caze((long[] a) -> stream(a))
			.caze((short[] a) -> stream(a))
			.caze((Object[] a) -> java.util.Arrays.stream(a));

	private static final Match<Stream<?>> ARRAY_TO_PARALLEL_STREAM_MATCHER = new Match<Stream<?>>()
			.caze((boolean[] a) -> parallelStream(a))
			.caze((byte[] a) -> parallelStream(a))
			.caze((char[] a) -> parallelStream(a))
			.caze((double[] a) -> parallelStream(a))
			.caze((float[] a) -> parallelStream(a))
			.caze((int[] a) -> parallelStream(a))
			.caze((long[] a) -> parallelStream(a))
			.caze((short[] a) -> parallelStream(a))
			.caze((Object[] a) -> java.util.Arrays.stream(a).parallel());

	/**
	 * This class is not intendet to be instantiated.
	 */
	private ArrayExtensions() {
		throw new AssertionError(ArrayExtensions.class.getName() + " cannot be instantiated.");
	}

	// -- asList

	public static List<Boolean> asList(boolean... array) {
		requireNonNull(array, "array is null");
		return stream(array).collect(Collectors.toList());
	}

	public static List<Byte> asList(byte... array) {
		requireNonNull(array, "array is null");
		return createList(array.length, i -> array[i]);
	}

	public static List<Character> asList(char... array) {
		requireNonNull(array, "array is null");
		return createList(array.length, i -> array[i]);
	}

	public static List<Double> asList(double... array) {
		requireNonNull(array, "array is null");
		return createList(array.length, i -> array[i]);
	}

	public static List<Float> asList(float... array) {
		requireNonNull(array, "array is null");
		return createList(array.length, i -> array[i]);
	}

	public static List<Integer> asList(int... array) {
		requireNonNull(array, "array is null");
		return createList(array.length, i -> array[i]);
	}

	public static List<Long> asList(long... array) {
		requireNonNull(array, "array is null");
		return createList(array.length, i -> array[i]);
	}

	public static List<Short> asList(short... array) {
		requireNonNull(array, "array is null");
		return createList(array.length, i -> array[i]);
	}

	// -- forEach

	public static <T> void forEach(T[] array, Consumer<? super T> action) {
		requireNonNull(array, "array is null");
		requireNonNull(action, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(boolean[] array, Consumer<Boolean> action) {
		requireNonNull(array, "array is null");
		requireNonNull(action, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(byte[] array, Consumer<Byte> action) {
		requireNonNull(array, "array is null");
		requireNonNull(action, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(char[] array, Consumer<Character> action) {
		requireNonNull(array, "array is null");
		requireNonNull(action, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(double[] array, Consumer<Double> action) {
		requireNonNull(array, "array is null");
		requireNonNull(action, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(float[] array, Consumer<Float> action) {
		requireNonNull(array, "array is null");
		requireNonNull(action, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(int[] array, Consumer<Integer> action) {
		requireNonNull(array, "array is null");
		requireNonNull(action, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(long[] array, Consumer<Long> action) {
		requireNonNull(array, "array is null");
		requireNonNull(action, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(short[] array, Consumer<Short> action) {
		requireNonNull(array, "array is null");
		requireNonNull(action, "action is null");
		consume(array.length, i -> array[i], action);
	}

	// -- isNullOrEmpty

	/**
	 * Tests if given Array is null or empty.
	 * 
	 * @param <T> type of array elements
	 * @param array An Array
	 * @return true, if arr is null or empty, false otherwise
	 */
	public static <T> boolean isNullOrEmpty(T[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isNullOrEmpty(boolean[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isNullOrEmpty(byte[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isNullOrEmpty(char[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isNullOrEmpty(double[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isNullOrEmpty(float[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isNullOrEmpty(int[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isNullOrEmpty(long[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isNullOrEmpty(short[] array) {
		return array == null || array.length == 0;
	}

	// -- map

	/**
	 * Convenience method for
	 * {@code Arrays.stream(array).map(f).collect(Collectors.toList()).toArray(new R[array.length])}
	 * .
	 *
	 * @param <R> Type of result array elements
	 * @param <T> Type of input array elements
	 * @param array An array
	 * @param f function which maps array elements
	 * @return An array of mapped elements
	 */
	public static <R, T> R[] map(T[] array, Function<? super T, ? extends R> f) {
		requireNonNull(array, "array is null");
		requireNonNull(f, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(boolean[] array, Function<Boolean, ? extends R> f) {
		requireNonNull(array, "array is null");
		requireNonNull(f, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(byte[] array, Function<Byte, ? extends R> f) {
		requireNonNull(array, "array is null");
		requireNonNull(f, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(char[] array, Function<Character, ? extends R> f) {
		requireNonNull(array, "array is null");
		requireNonNull(f, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(double[] array, Function<Double, ? extends R> f) {
		requireNonNull(array, "array is null");
		requireNonNull(f, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(float[] array, Function<Float, ? extends R> f) {
		requireNonNull(array, "array is null");
		requireNonNull(f, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(int[] array, Function<Integer, ? extends R> f) {
		requireNonNull(array, "array is null");
		requireNonNull(f, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(long[] array, Function<Long, ? extends R> f) {
		requireNonNull(array, "array is null");
		requireNonNull(f, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(short[] array, Function<Short, ? extends R> f) {
		requireNonNull(array, "array is null");
		requireNonNull(f, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	// -- stream

	public static Stream<Boolean> stream(boolean[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Boolean>(array.length, i -> array[i]).stream();
	}

	public static Stream<Byte> stream(byte[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Byte>(array.length, i -> array[i]).stream();
	}

	public static Stream<Character> stream(char[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Character>(array.length, i -> array[i]).stream();
	}

	public static Stream<Double> stream(double[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Double>(array.length, i -> array[i]).stream();
	}

	public static Stream<Float> stream(float[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Float>(array.length, i -> array[i]).stream();
	}

	public static Stream<Integer> stream(int[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Integer>(array.length, i -> array[i]).stream();
	}

	public static Stream<Long> stream(long[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Long>(array.length, i -> array[i]).stream();
	}

	public static Stream<Short> stream(short[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Short>(array.length, i -> array[i]).stream();
	}
	
	public static Stream<?> toStream(Object object) {
		return ARRAY_TO_STREAM_MATCHER.apply(object);
	}

	// -- parallelStream

	public static Stream<Boolean> parallelStream(boolean[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Boolean>(array.length, i -> array[i]).parallelStream();
	}

	public static Stream<Byte> parallelStream(byte[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Byte>(array.length, i -> array[i]).parallelStream();
	}

	public static Stream<Character> parallelStream(char[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Character>(array.length, i -> array[i]).parallelStream();
	}

	public static Stream<Double> parallelStream(double[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Double>(array.length, i -> array[i]).parallelStream();
	}

	public static Stream<Float> parallelStream(float[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Float>(array.length, i -> array[i]).parallelStream();
	}

	public static Stream<Integer> parallelStream(int[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Integer>(array.length, i -> array[i]).parallelStream();
	}

	public static Stream<Long> parallelStream(long[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Long>(array.length, i -> array[i]).parallelStream();
	}

	public static Stream<Short> parallelStream(short[] array) {
		requireNonNull(array, "array is null");
		return new StreamableList<Short>(array.length, i -> array[i]).parallelStream();
	}
	
	public static Stream<?> toParallelStream(Object object) {
		return ARRAY_TO_PARALLEL_STREAM_MATCHER.apply(object);
	}

	// -- internal helpers

	private static <T> void consume(int length, Function<Integer, T> generator,
			Consumer<? super T> action) {
		for (int i = 0; i < length; i++) {
			final T t = generator.apply(i);
			action.accept(t);
		}
	}

	private static <R, T> R[] createArray(int length, Function<Integer, T> generator,
			Function<? super T, ? extends R> f) {
		@SuppressWarnings("unchecked")
		final R[] result = (R[]) new Object[length];
		for (int i = 0; i < length; i++) {
			final T value = generator.apply(i);
			result[i] = f.apply(value);
		}
		return result;
	}

	private static <T> List<T> createList(int size, Function<Integer, T> generator) {
		final List<T> result = new ArrayList<T>(size);
		for (int i = 0; i < size; i++) {
			result.add(generator.apply(i));
		}
		return java.util.Collections.unmodifiableList(result);
	}

	private static class StreamableList<E> extends AbstractList<E> {

		final int size;
		final Function<Integer, E> getter;

		StreamableList(int size, Function<Integer, E> getter) {
			this.size = size;
			this.getter = getter;
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public E get(int index) {
			return getter.apply(index);
		}

		@Override
		public Spliterator<E> spliterator() {
			return Spliterators.spliterator(this, Spliterator.ORDERED | Spliterator.IMMUTABLE);
		}
	}

}
