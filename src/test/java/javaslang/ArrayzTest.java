/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static javaslang.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

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
	public void shouldCombineTwoArrays() {
		final String[] array1 = { "1", "2" };
		final String[] array2 = { "3", "4" };
		final String[] expected = { "1", "2", "3", "4" };
		assertThat(Arrayz.combine(array1, array2)).isEqualTo(expected);
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

	@Test
	public void shoudRecognizeNullArray() {
		final byte[] array = null;
		final boolean actual = Arrayz.isNullOrEmpty(array);
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shoudRecognizeEmptyArray() {
		final byte[] array = new byte[] {};
		final boolean actual = Arrayz.isNullOrEmpty(array);
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shoudRecognizeNoneEmptyArray() {
		final byte[] array = new byte[] { 1, 2, 3 };
		final boolean actual = Arrayz.isNullOrEmpty(array);
		assertThat(actual).isEqualTo(false);
	}

	// TODO

	// -- stream

	// TODO

	// -- parallelStream

	// TODO

}
