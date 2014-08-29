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

import org.junit.Test;

public class SerializablesTest {

	private static final String MAGIC_NUMBER = "0xCAFEBABE";

	@Test
	public void shouldNotInstantiable() {
		assertThat(Serializables.class).isNotInstantiable();
	}

	@Test
	public void shouldSerializeDeserialize() throws Exception {
		assertThat(deserialize(serialize(MAGIC_NUMBER))).isEqualTo(MAGIC_NUMBER);
	}
}
