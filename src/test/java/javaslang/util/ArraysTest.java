package javaslang.util;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import javaslang.util.Arrays;
import javaslang.util.Objects;

import org.junit.Test;

public class ArraysTest {
	
	@Test
	public void shoudRecognizeNullArray() {
		final byte[] array = null;
		final boolean actual = Arrays.isNullOrEmpty(array);
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shoudRecognizeEmptyArray() {
		final byte[] array = new byte[] { };
		final boolean actual = Arrays.isNullOrEmpty(array);
		assertThat(actual).isEqualTo(true);
	}
	
	@Test
	public void shoudRecognizeNoneEmptyArray() {
		final byte[] array = new byte[] { 1, 2, 3 };
		final boolean actual = Arrays.isNullOrEmpty(array);
		assertThat(actual).isEqualTo(false);
	}
	
	@Test
	public void shoudConvertByteArrayToList() {
		final byte[] array = new byte[] { 1, 2, 3 };
		final List<Byte> list = Arrays.asList(array);
		assertThat(Objects.toString(list)).isEqualTo("[1, 2, 3]");
	}

}
