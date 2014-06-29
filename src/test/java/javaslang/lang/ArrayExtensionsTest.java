package javaslang.lang;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javaslang.lang.ArrayExtensions;

import org.junit.Test;

public class ArrayExtensionsTest {
	
	@Test
	public void shoudRecognizeNullArray() {
		final byte[] array = null;
		final boolean actual = ArrayExtensions.isNullOrEmpty(array);
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shoudRecognizeEmptyArray() {
		final byte[] array = new byte[] { };
		final boolean actual = ArrayExtensions.isNullOrEmpty(array);
		assertThat(actual).isEqualTo(true);
	}
	
	@Test
	public void shoudRecognizeNoneEmptyArray() {
		final byte[] array = new byte[] { 1, 2, 3 };
		final boolean actual = ArrayExtensions.isNullOrEmpty(array);
		assertThat(actual).isEqualTo(false);
	}
	
	@Test
	public void shoudConvertIntArrayToList() {
		final int[] array = new int[] { 1, 2, 3 };
		final List<Integer> actual = ArrayExtensions.asList(array);
		final List<Integer> expected = new ArrayList<Integer>();
		expected.add(1);
		expected.add(2);
		expected.add(3);
		assertThat(actual).isEqualTo(expected);
	}

}
