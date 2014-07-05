/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.io;

import static javaslang.io.IO.UTF8;
import static org.fest.assertions.api.Assertions.assertThat;

import java.io.InputStream;

import javaslang.io.IO;

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
