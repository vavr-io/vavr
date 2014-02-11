package io.rocketscience.java.util;

import java.util.List;
import java.util.function.Function;


public final class Arrays {

	private Arrays() {
        throw new AssertionError(Arrays.class.getName() + " cannot be instantiated.");
    }
	
	/**
	 * Tests if given Array is null or empty.
	 * 
	 * @param array An Array
	 * @return true, if arr is null or empty, false otherwise
	 */
	public static <T> boolean isNullOrEmpty(T[] array) {
		return array == null || array.length == 0;
	}
	
	public static List<Boolean> asList(boolean[] array) {
		return createList(Boolean.class, array.length, i -> array[i]);
	}

	public static List<Byte> asList(byte[] array) {
		return createList(Byte.class, array.length, i -> array[i]);
	}

	public static List<Character> asList(char[] array) {
		return createList(Character.class, array.length, i -> array[i]);
	}

	public static List<Double> asList(double[] array) {
		return createList(Double.class, array.length, i -> array[i]);
	}

	public static List<Float> asList(float[] array) {
		return createList(Float.class, array.length, i -> array[i]);
	}

	public static List<Integer> asList(int[] array) {
		return createList(Integer.class, array.length, i -> array[i]);
	}

	public static List<Long> asList(long[] array) {
		return createList(Long.class, array.length, i -> array[i]);
	}

	public static List<Short> asList(short[] array) {
		return createList(Short.class, array.length, i -> array[i]);
	}

	@SuppressWarnings("unchecked")
	private static <T> List<T> createList(Class<T> componentType, int size, Function<Integer, T> generator) {
		final Object[] array = new Object[size];
		for (int i = 0; i < size; i++) {
			array[i] = generator.apply(i);
		}
		return java.util.Arrays.asList((T[]) array); // the cast is correct
	}
	
}
