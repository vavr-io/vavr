/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Serializables;
import javaslang.Tuple;
import javaslang.collection.List.Cons;
import javaslang.collection.List.Nil;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ListTest extends AbstractSeqTest {

	@Override
	protected <T> List<T> nil() {
		return List.nil();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> List<T> of(T... elements) {
		return List.of(elements);
	}

	// -- static collector()

	@Test
	public void shouldStreamAndCollectNil() {
		final List<?> actual = List.nil().toJavaStream().collect(List.collector());
		assertThat(actual).isEqualTo(List.nil());
	}

	@Test
	public void shouldStreamAndCollectNonNil() {
		final List<?> actual = List.of(1, 2, 3).toJavaStream().collect(List.collector());
		assertThat(actual).isEqualTo(List.of(1, 2, 3));
	}

	@Test
	public void shouldParallelStreamAndCollectNil() {
		final List<?> actual = List.nil().toJavaStream().parallel().collect(List.collector());
		assertThat(actual).isEqualTo(List.nil());
	}

	@Test
	public void shouldParallelStreamAndCollectNonNil() {
		final List<?> actual = List.of(1, 2, 3).toJavaStream().parallel().collect(List.collector());
		assertThat(actual).isEqualTo(List.of(1, 2, 3));
	}

	// -- static nil()

	@Test
	public void shouldCreateNil() {
		assertThat(List.nil()).isEqualTo(Nil.instance());
	}

	// -- static of(T...)

	@Test
	public void shouldCreateListOfElements() {
		final List<Integer> actual = List.of(1, 2);
		final List<Integer> expected = new Cons<>(1, new Cons<>(2, Nil.instance()));
		assertThat(actual).isEqualTo(expected);
	}

	// -- static of(Iterable)

	@Test
	public void shouldCreateListOfIterable() {
		final java.util.List<Integer> arrayList = Arrays.asList(1, 2, 3);
		assertThat(List.of(arrayList)).isEqualTo(List.of(1, 2, 3));
	}

	// -- static rangeClosed(int, int)

	@Test
	public void shouldCreateListOfRangeWhereFromIsGreaterThanTo() {
		assertThat(List.rangeClosed(1, 0)).isEqualTo(List.nil());
	}

	@Test
	public void shouldCreateListOfRangeWhereFromEqualsTo() {
		assertThat(List.rangeClosed(0, 0)).isEqualTo(List.of(0));
	}

	@Test
	public void shouldCreateListOfRangeWhereFromIsLessThanTo() {
		assertThat(List.rangeClosed(1, 3)).isEqualTo(List.of(1, 2, 3));
	}

	@Test
	public void shouldCreateListOfRangeWhereFromEqualsToEqualsInteger_MIN_VALUE() {
		assertThat(List.rangeClosed(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(List.of(Integer.MIN_VALUE));
	}

	// -- static range(int, int)

	@Test
	public void shouldCreateListOfUntilWhereFromIsGreaterThanTo() {
		assertThat(List.range(1, 0)).isEqualTo(List.nil());
	}

	@Test
	public void shouldCreateListOfUntilWhereFromEqualsTo() {
		assertThat(List.range(0, 0)).isEqualTo(List.nil());
	}

	@Test
	public void shouldCreateListOfUntilWhereFromIsLessThanTo() {
		assertThat(List.range(1, 3)).isEqualTo(List.of(1, 2));
	}

	@Test
	public void shouldCreateListOfUntilWhereFromEqualsToEqualsInteger_MIN_VALUE() {
		assertThat(List.range(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(List.nil());
	}

	// -- unapply

	@Test
	public void shouldUnapplyNil() {
		assertThat(Nil.instance().unapply()).isEqualTo(Tuple.empty());
	}

	@Test
	public void shouldUnapplyCons() {
		assertThat(List.of(1, 2, 3).unapply()).isEqualTo(Tuple.of(1, List.of(2, 3)));
	}

	// -- toString

	@Test
	public void shouldStringifyNil() {
		assertThat(this.nil().toString()).isEqualTo("List()");
	}

	@Test
	public void shouldStringifyNonNil() {
		assertThat(this.of(1, 2, 3).toString()).isEqualTo("List(1, 2, 3)");
	}

	// -- Cons test

	@Test(expected = InvalidObjectException.class)
	public void shouldNotSerializeEnclosingClass() throws Throwable {
		callReadObject(List.of(1));
	}

	@Test(expected = InvalidObjectException.class)
	public void shouldNotDeserializeListWithSizeLessThanOne() throws Throwable {
			try {
				/*
				 * This implementation is stable regarding jvm impl changes of object serialization. The index of the
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
				if (index == -1) {
					throw new IllegalStateException("Hack incomplete - index not found");
				}
				/*
				 * Hack the serialized data and fake zero elements.
				 */
				listWithOneElement[index] = 0;
				Serializables.deserialize(listWithOneElement);
			} catch (IllegalStateException x) {
				throw (x.getCause() != null) ? x.getCause() : x;
			}
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
