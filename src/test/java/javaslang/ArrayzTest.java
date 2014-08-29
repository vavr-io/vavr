/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static javaslang.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;

public class ArrayzTest {

	@Test
	public void shouldNotInstantiable() {
		assertThat(Arrayz.class).isNotInstantiable();
	}

	// -- array creation

	@Test
	public void shouldCreateArrayOfVarargs() {
		final String[] expected = { "" };
		assertThat(Arrayz.of("")).isEqualTo(expected);
	}

	@Test
	public void shouldCreateNewArrayOfSpecificComponentType() {
		assertThat(Arrayz.newArray(String.class, 1).getClass().getComponentType().getName()).isEqualTo(
				"java.lang.String");
	}

	@Test
	public void shouldCreateNewTypedArrayOfSpecificLength() {
		assertThat(Arrayz.newArray(String.class, 3).length).isEqualTo(3);
	}

	@Test
	public void shouldGetComponentTypeOfDeclaredArray() {
		final String[] array = { "test" };
		assertThat(Arrayz.getComponentType(array).getName()).isEqualTo("java.lang.String");
	}

	@Test
	public void shouldGetComponentTypeOfLazilyCreatedArray() {
		assertThat(Arrayz.getComponentType(Arrayz.of("test")).getName()).isEqualTo("java.lang.String");
	}

	@Test
	public void shouldCombineTwoNonEmptyArrays() {
		final String[] array1 = { "1", "2" };
		final String[] array2 = { "3", "4" };
		final String[] expected = { "1", "2", "3", "4" };
		assertThat(Arrayz.combine(array1, array2)).isEqualTo(expected);
	}

	@Test
	public void shouldCombineWhenArray1IsEmpty() {
		final String[] array1 = {};
		final String[] array2 = { "1", "2" };
		assertThat(Arrayz.combine(array1, array2)).isEqualTo(array2);
	}

	@Test
	public void shouldCombineWhenArray2IsEmpty() {
		final String[] array1 = { "1", "2" };
		final String[] array2 = {};
		assertThat(Arrayz.combine(array1, array2)).isEqualTo(array1);
	}

	@Test
	public void shouldAppendElementToArray() {
		final String[] array = { "1", "2" };
		final String[] expected = { "1", "2", "3" };
		assertThat(Arrayz.append(array, "3")).isEqualTo(expected);
	}

	@Test
	public void shouldPrependElementToArray() {
		final String[] array = { "2", "3" };
		final String[] expected = { "1", "2", "3" };
		assertThat(Arrayz.prepend(array, "1")).isEqualTo(expected);
	}

	// -- isNullOrEmpty

	// Object

	@Test
	public void shoudRecognizeNullObjectArray() {
		final Object[] array = null;
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeEmptyObjectArray() {
		final Object[] array = {};
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeNoneEmptyObjectArray() {
		final Object[] array = { new Object() };
		assertThat(Arrayz.isNullOrEmpty(array)).isFalse();
	}

	// boolean

	@Test
	public void shoudRecognizeNullBooleanArray() {
		final boolean[] array = null;
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeEmptyBooleanArray() {
		final boolean[] array = {};
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeNoneEmptyBooleanArray() {
		final boolean[] array = { true };
		assertThat(Arrayz.isNullOrEmpty(array)).isFalse();
	}

	// byte

	@Test
	public void shoudRecognizeNullByteArray() {
		final byte[] array = null;
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeEmptyByteArray() {
		final byte[] array = {};
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeNoneEmptyByteArray() {
		final byte[] array = { 0 };
		assertThat(Arrayz.isNullOrEmpty(array)).isFalse();
	}

	// char

	@Test
	public void shoudRecognizeNullCharArray() {
		final char[] array = null;
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeEmptyCharArray() {
		final char[] array = {};
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeNoneEmptyCharArray() {
		final char[] array = { ' ' };
		assertThat(Arrayz.isNullOrEmpty(array)).isFalse();
	}

	// double

	@Test
	public void shoudRecognizeNullDoubleArray() {
		final double[] array = null;
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeEmptyDoubleArray() {
		final double[] array = {};
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeNoneEmptyDoubleArray() {
		final double[] array = { 0 };
		assertThat(Arrayz.isNullOrEmpty(array)).isFalse();
	}

	// float

	@Test
	public void shoudRecognizeNullFloatArray() {
		final float[] array = null;
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeEmptyFloatArray() {
		final float[] array = {};
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeNoneEmptyFloatArray() {
		final float[] array = { 0 };
		assertThat(Arrayz.isNullOrEmpty(array)).isFalse();
	}

	// int

	@Test
	public void shoudRecognizeNullIntArray() {
		final int[] array = null;
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeEmptyIntArray() {
		final int[] array = {};
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeNoneEmptyIntArray() {
		final int[] array = { 0 };
		assertThat(Arrayz.isNullOrEmpty(array)).isFalse();
	}

	// long

	@Test
	public void shoudRecognizeNullLongArray() {
		final long[] array = null;
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeEmptyLongArray() {
		final long[] array = {};
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeNoneEmptyLongArray() {
		final long[] array = { 0 };
		assertThat(Arrayz.isNullOrEmpty(array)).isFalse();
	}

	// short

	@Test
	public void shoudRecognizeNullShortArray() {
		final short[] array = null;
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeEmptyShortArray() {
		final short[] array = {};
		assertThat(Arrayz.isNullOrEmpty(array)).isTrue();
	}

	@Test
	public void shoudRecognizeNoneShortFloatArray() {
		final short[] array = { 0 };
		assertThat(Arrayz.isNullOrEmpty(array)).isFalse();
	}

	// -- stream

	@Test
	public void shouldStreamBoolean() {
		final boolean[] array = { true, false };
		assertThat(Arrayz.stream(array).collect(Collectors.toList())).isEqualTo(Arrays.asList(true, false));
	}

	@Test
	public void shouldStreamByte() {
		final byte[] array = { 1, 2, 3 };
		assertThat(Arrayz.stream(array).collect(Collectors.toList())).isEqualTo(
				Arrays.asList((byte) 1, (byte) 2, (byte) 3));
	}

	@Test
	public void shouldStreamChar() {
		final char[] array = { 1, 2, 3 };
		assertThat(Arrayz.stream(array).collect(Collectors.toList())).isEqualTo(
				Arrays.asList((char) 1, (char) 2, (char) 3));
	}

	@Test
	public void shouldStreamDouble() {
		final double[] array = { 1, 2, 3 };
		assertThat(Arrayz.stream(array).collect(Collectors.toList())).isEqualTo(
				Arrays.asList((double) 1, (double) 2, (double) 3));
	}

	@Test
	public void shouldStreamFloat() {
		final float[] array = { 1, 2, 3 };
		assertThat(Arrayz.stream(array).collect(Collectors.toList())).isEqualTo(
				Arrays.asList((float) 1, (float) 2, (float) 3));
	}

	@Test
	public void shouldStreamInt() {
		final int[] array = { 1, 2, 3 };
		assertThat(Arrayz.stream(array).collect(Collectors.toList())).isEqualTo(Arrays.asList(1, 2, 3));
	}

	@Test
	public void shouldStreamLong() {
		final long[] array = { 1, 2, 3 };
		assertThat(Arrayz.stream(array).collect(Collectors.toList())).isEqualTo(
				Arrays.asList((long) 1, (long) 2, (long) 3));
	}

	@Test
	public void shouldStreamShort() {
		final short[] array = { 1, 2, 3 };
		assertThat(Arrayz.stream(array).collect(Collectors.toList())).isEqualTo(
				Arrays.asList((short) 1, (short) 2, (short) 3));
	}

	@Test
	public void shouldStreamObject() {
		final Object array = new byte[] { 1, 2, 3 };
		assertThat(Arrayz.toStream(array).map(Object::toString).collect(Collectors.joining())).isEqualTo("123");
	}

	// -- parallelStream

	@Test
	public void shouldParallelStreamBoolean() {
		final boolean[] array = { true, false };
		assertThat(Arrayz.parallelStream(array).collect(Collectors.toList())).isEqualTo(Arrays.asList(true, false));
	}

	@Test
	public void shouldParallelStreamByte() {
		final byte[] array = { 1, 2, 3 };
		assertThat(Arrayz.parallelStream(array).collect(Collectors.toList())).isEqualTo(
				Arrays.asList((byte) 1, (byte) 2, (byte) 3));
	}

	@Test
	public void shouldParallelStreamChar() {
		final char[] array = { 1, 2, 3 };
		assertThat(Arrayz.parallelStream(array).collect(Collectors.toList())).isEqualTo(
				Arrays.asList((char) 1, (char) 2, (char) 3));
	}

	@Test
	public void shouldParallelStreamDouble() {
		final double[] array = { 1, 2, 3 };
		assertThat(Arrayz.parallelStream(array).collect(Collectors.toList())).isEqualTo(
				Arrays.asList((double) 1, (double) 2, (double) 3));
	}

	@Test
	public void shouldParallelStreamFloat() {
		final float[] array = { 1, 2, 3 };
		assertThat(Arrayz.parallelStream(array).collect(Collectors.toList())).isEqualTo(
				Arrays.asList((float) 1, (float) 2, (float) 3));
	}

	@Test
	public void shouldParallelStreamInt() {
		final int[] array = { 1, 2, 3 };
		assertThat(Arrayz.parallelStream(array).collect(Collectors.toList())).isEqualTo(Arrays.asList(1, 2, 3));
	}

	@Test
	public void shouldParallelStreamLong() {
		final long[] array = { 1, 2, 3 };
		assertThat(Arrayz.parallelStream(array).collect(Collectors.toList())).isEqualTo(
				Arrays.asList((long) 1, (long) 2, (long) 3));
	}

	@Test
	public void shouldParallelStreamShort() {
		final short[] array = { 1, 2, 3 };
		assertThat(Arrayz.parallelStream(array).collect(Collectors.toList())).isEqualTo(
				Arrays.asList((short) 1, (short) 2, (short) 3));
	}

	@Test
	public void shouldParallelStreamObject() {
		final Object array = new byte[] { 1, 2, 3 };
		assertThat(Arrayz.toParallelStream(array).map(Object::toString).collect(Collectors.joining())).isEqualTo("123");
	}
}
