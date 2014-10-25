/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.ByteArrayInputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javaslang.AssertionsExtensions;
import javaslang.Serializables;

import org.junit.Test;

public class ConsTest {

	@Test
	public void shouldNotSerializeEnclosingClass() throws Exception {
		AssertionsExtensions.assertThat(() -> callReadObject(List.of(1))).isThrowing(InvalidObjectException.class,
				"Proxy required");
	}

	@Test
	public void shouldNotDeserializeListWithSizeLessThanOne() {
		AssertionsExtensions.assertThat(() -> {
			try {
				/*
				 * This implementation is stable regarding jvm impl changes of object serialization The index of the
				 * number of List elements is gathered dynamically.
				 */
				final byte[] listWithOneElement = Serializables.serialize(List.of(0));
				final byte[] listWithTwoElements = Serializables.serialize(List.of(0, 0));
				int index = -1;
				for (int i = 0; i < listWithOneElement.length && index == -1; i++) {
					final byte b1 = listWithOneElement[i];
					final byte b2 = listWithTwoElements[i];
					if (b1 != b2) {
						if (b1 != 1 || b2 != 2) {
							throw new IllegalStateException("Difference does not indicate number of elements.");
						} else {
							index = i;
						}
					}
				}
				/*
				 * Hack the serialized data and fake zero elements.
				 */
				listWithOneElement[index] = 0;
				Serializables.deserialize(listWithOneElement);
			} catch (IllegalStateException x) {
				throw (x.getCause() != null) ? x.getCause() : x;
			}
		}).isThrowing(InvalidObjectException.class, "No elements");
	}

	private void callReadObject(Object o) throws Throwable {
		final byte[] objectData = Serializables.serialize(o);
		try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(objectData))) {
			final Method method = o.getClass().getDeclaredMethod("readObject", ObjectInputStream.class);
			method.setAccessible(true);
			try {
				method.invoke(o, stream);
			} catch (InvocationTargetException x) {
				throw (x.getCause() != null) ? x.getCause() : x;
			}
		}
	}
}
