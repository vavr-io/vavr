package javaslang.util;

import static javaslang.lang.Lang.require;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Additions to {@link java.util.Arrays}.
 */
public final class Arrays {

	private Arrays() {
		throw new AssertionError(Arrays.class.getName() + " cannot be instantiated.");
	}

	// -- asList

	/** Convenience method, calls {@link java.util.Arrays#asList(Object...)} */
	public static <T> List<T> asList(T[] array) {
		require(array != null, "array is null");
		// performs better than createList(array.length, i -> array[i])
		return java.util.Arrays.asList(array);
	}

	public static List<Boolean> asList(boolean[] array) {
		require(array != null, "array is null");
		return createList(array.length, i -> array[i]);
	}

	public static List<Byte> asList(byte[] array) {
		require(array != null, "array is null");
		return createList(array.length, i -> array[i]);
	}

	public static List<Character> asList(char[] array) {
		require(array != null, "array is null");
		return createList(array.length, i -> array[i]);
	}

	public static List<Double> asList(double[] array) {
		require(array != null, "array is null");
		return createList(array.length, i -> array[i]);
	}

	public static List<Float> asList(float[] array) {
		require(array != null, "array is null");
		return createList(array.length, i -> array[i]);
	}

	public static List<Integer> asList(int[] array) {
		require(array != null, "array is null");
		return createList(array.length, i -> array[i]);
	}

	public static List<Long> asList(long[] array) {
		require(array != null, "array is null");
		return createList(array.length, i -> array[i]);
	}

	public static List<Short> asList(short[] array) {
		require(array != null, "array is null");
		return createList(array.length, i -> array[i]);
	}

	// -- forEach

	public static <T> void forEach(T[] array, Consumer<? super T> action) {
		require(array != null, "array is null");
		require(action != null, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(boolean[] array, Consumer<Boolean> action) {
		require(array != null, "array is null");
		require(action != null, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(byte[] array, Consumer<Byte> action) {
		require(array != null, "array is null");
		require(action != null, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(char[] array, Consumer<Character> action) {
		require(array != null, "array is null");
		require(action != null, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(double[] array, Consumer<Double> action) {
		require(array != null, "array is null");
		require(action != null, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(float[] array, Consumer<Float> action) {
		require(array != null, "array is null");
		require(action != null, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(int[] array, Consumer<Integer> action) {
		require(array != null, "array is null");
		require(action != null, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(long[] array, Consumer<Long> action) {
		require(array != null, "array is null");
		require(action != null, "action is null");
		consume(array.length, i -> array[i], action);
	}

	public static void forEach(short[] array, Consumer<Short> action) {
		require(array != null, "array is null");
		require(action != null, "action is null");
		consume(array.length, i -> array[i], action);
	}

	// -- isNullOrEmpty

	/**
	 * Tests if given Array is null or empty.
	 * 
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
	 * <code>Arrays.stream(array).map(f).collect(Collectors.toList()).toArray(new R[array.length])</code>
	 * .
	 */
	public static <R, T> R[] map(T[] array, Function<? super T, ? extends R> f) {
		require(array != null, "array is null");
		require(f != null, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(boolean[] array, Function<Boolean, ? extends R> f) {
		require(array != null, "array is null");
		require(f != null, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(byte[] array, Function<Byte, ? extends R> f) {
		require(array != null, "array is null");
		require(f != null, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(char[] array, Function<Character, ? extends R> f) {
		require(array != null, "array is null");
		require(f != null, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(double[] array, Function<Double, ? extends R> f) {
		require(array != null, "array is null");
		require(f != null, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(float[] array, Function<Float, ? extends R> f) {
		require(array != null, "array is null");
		require(f != null, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(int[] array, Function<Integer, ? extends R> f) {
		require(array != null, "array is null");
		require(f != null, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(long[] array, Function<Long, ? extends R> f) {
		require(array != null, "array is null");
		require(f != null, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	public static <R> R[] map(short[] array, Function<Short, ? extends R> f) {
		require(array != null, "array is null");
		require(f != null, "function is null");
		return createArray(array.length, i -> array[i], f);
	}

	// -- stream

	public static <T> Stream<T> stream(T[] array) {
		require(array != null, "array is null");
		final Spliterator<T> spliterator = Spliterators.spliterator(array, Spliterator.ORDERED);
		return StreamSupport.stream(spliterator, false);
	}

	public static Stream<Boolean> stream(boolean[] array) {
		require(array != null, "array is null");
		return new StreamableList<Boolean>(array.length, i -> array[i]).stream();
	}

	public static Stream<Byte> stream(byte[] array) {
		require(array != null, "array is null");
		return new StreamableList<Byte>(array.length, i -> array[i]).stream();
	}

	public static Stream<Character> stream(char[] array) {
		require(array != null, "array is null");
		return new StreamableList<Character>(array.length, i -> array[i]).stream();
	}

	public static DoubleStream stream(double[] array) {
		require(array != null, "array is null");
		final Spliterator.OfDouble spliterator = Spliterators.spliterator(array,
				Spliterator.ORDERED);
		return StreamSupport.doubleStream(spliterator, false);
	}

	public static Stream<Float> stream(float[] array) {
		require(array != null, "array is null");
		return new StreamableList<Float>(array.length, i -> array[i]).stream();
	}

	public static IntStream stream(int[] array) {
		require(array != null, "array is null");
		final Spliterator.OfInt spliterator = Spliterators.spliterator(array, Spliterator.ORDERED);
		return StreamSupport.intStream(spliterator, false);
	}

	public static LongStream stream(long[] array) {
		require(array != null, "array is null");
		final Spliterator.OfLong spliterator = Spliterators.spliterator(array, Spliterator.ORDERED);
		return StreamSupport.longStream(spliterator, false);
	}

	public static Stream<Short> stream(short[] array) {
		require(array != null, "array is null");
		return new StreamableList<Short>(array.length, i -> array[i]).stream();
	}

	// -- parallelStream

	public static <T> Stream<T> parallelStream(T[] array) {
		require(array != null, "array is null");
		final Spliterator<T> spliterator = Spliterators.spliterator(array, Spliterator.ORDERED);
		return StreamSupport.stream(spliterator, true);
	}

	public static Stream<Boolean> parallelStream(boolean[] array) {
		require(array != null, "array is null");
		return new StreamableList<Boolean>(array.length, i -> array[i]).parallelStream();
	}

	public static Stream<Byte> parallelStream(byte[] array) {
		require(array != null, "array is null");
		return new StreamableList<Byte>(array.length, i -> array[i]).parallelStream();
	}

	public static Stream<Character> parallelStream(char[] array) {
		require(array != null, "array is null");
		return new StreamableList<Character>(array.length, i -> array[i]).parallelStream();
	}

	public static DoubleStream parallelStream(double[] array) {
		require(array != null, "array is null");
		final Spliterator.OfDouble spliterator = Spliterators.spliterator(array,
				Spliterator.ORDERED);
		return StreamSupport.doubleStream(spliterator, true);
	}

	public static Stream<Float> parallelStream(float[] array) {
		require(array != null, "array is null");
		return new StreamableList<Float>(array.length, i -> array[i]).parallelStream();
	}

	public static IntStream parallelStream(int[] array) {
		require(array != null, "array is null");
		final Spliterator.OfInt spliterator = Spliterators.spliterator(array, Spliterator.ORDERED);
		return StreamSupport.intStream(spliterator, true);
	}

	public static LongStream parallelStream(long[] array) {
		require(array != null, "array is null");
		final Spliterator.OfLong spliterator = Spliterators.spliterator(array, Spliterator.ORDERED);
		return StreamSupport.longStream(spliterator, true);
	}

	public static Stream<Short> parallelStream(short[] array) {
		require(array != null, "array is null");
		return new StreamableList<Short>(array.length, i -> array[i]).parallelStream();
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
			return Spliterators.spliterator(this, Spliterator.ORDERED);
		}
	}

}
