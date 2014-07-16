/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArrayzTest {
	
	@Test
	public void shoudRecognizeNullArray() {
		final byte[] array = null;
		final boolean actual = Arrayz.isNullOrEmpty(array);
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shoudRecognizeEmptyArray() {
		final byte[] array = new byte[] { };
		final boolean actual = Arrayz.isNullOrEmpty(array);
		assertThat(actual).isEqualTo(true);
	}
	
	@Test
	public void shoudRecognizeNoneEmptyArray() {
		final byte[] array = new byte[] { 1, 2, 3 };
		final boolean actual = Arrayz.isNullOrEmpty(array);
		assertThat(actual).isEqualTo(false);
	}
	
	@Test
	public void shoudConvertIntArrayToList() {
		final List<Integer> actual = Arrayz.asList(1, 2, 3);
		final List<Integer> expected = Arrays.asList(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

}
