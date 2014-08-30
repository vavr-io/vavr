/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static javaslang.Assertions.assertThat;
import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.Test;

public class SerializablesTest {

	private static final String MAGIC_NUMBER = "0xCAFEBABE";

	@Test
	public void shouldNotInstantiable() {
		assertThat(Serializables.class).isNotInstantiable();
	}

	@Test
	public void shouldSerializeDeserialize() {
		assertThat(deserialize(serialize(MAGIC_NUMBER))).isEqualTo(MAGIC_NUMBER);
	}

	@Test
	public void shouldThrowIllegalStateExceptionOnSerializationError() {
		final Object o = new Object() {
			private void writeObject(ObjectOutputStream o) throws IOException {
				throw new IOException();
			}
		};
		assertThat(() -> serialize(o)).isThrowing(IllegalStateException.class, "Error serializing object");
	}
}
