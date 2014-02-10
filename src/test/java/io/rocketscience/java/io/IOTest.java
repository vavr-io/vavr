package io.rocketscience.java.io;

import static io.rocketscience.java.io.IO.UTF8;
import static org.fest.assertions.api.Assertions.assertThat;

import java.io.InputStream;

import org.junit.Test;

public class IOTest {
	
	@Test
	public void shouldConvertInputStreamToBytes() throws Exception {
		final byte[] expected = "hello".getBytes();
		final InputStream in = IO.toInputStream(expected);
		final byte[] actual = IO.toBytes(in).orElse(null);
		assertThat(actual).isEqualTo(expected);
	}
	
	@Test
	public void shouldConvertInputStreamToString() throws Exception {
		final String expected = "hello";
		final InputStream in = IO.toInputStream(expected, UTF8);
		final String actual = IO.toString(in, UTF8).orElse(null);
		assertThat(actual).isEqualTo(expected);
	}
	
}
