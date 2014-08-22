/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class Serializables {

	/**
	 * This class is not intended to be instantiated.
	 */
	private Serializables() {
		throw new AssertionError(Serializables.class.getName() + " is not intended to be instantiated.");
	}

	public static byte[] serialize(Object obj) throws IOException {
		try (ByteArrayOutputStream buf = new ByteArrayOutputStream();
				ObjectOutputStream stream = new ObjectOutputStream(buf)) {
			stream.writeObject(obj);
			return buf.toByteArray();
		}
	}

	public static Object deserialize(byte[] objectData) throws ClassNotFoundException, IOException {
		try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(objectData))) {
			return stream.readObject();
		}
	}

}
