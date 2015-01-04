/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import javaslang.*;
import javaslang.Algebra.Monoid;
import javaslang.collection.List.Cons;
import javaslang.collection.List.Nil;

import org.junit.Test;

public class ListTest extends AbstractSeqTest {

	@Override
	protected <T> Seq<T> nil() {
		return List.nil();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> Seq<T> of(T... elements) {
		return List.of(elements);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T, U extends Traversable<T>> Monoid<U> zero() {
		return (Monoid<U>) (Monoid) List.nil().zero();
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

	// -- static range(int, int)

	@Test
	public void shouldCreateListOfRangeWhereFromIsGreaterThanTo() {
		assertThat(List.range(1, 0)).isEqualTo(List.nil());
	}

	@Test
	public void shouldCreateListOfRangeWhereFromEqualsTo() {
		assertThat(List.range(0, 0)).isEqualTo(List.of(0));
	}

	@Test
	public void shouldCreateListOfRangeWhereFromIsLessThanTo() {
		assertThat(List.range(1, 3)).isEqualTo(List.of(1, 2, 3));
	}

	@Test
	public void shouldCreateListOfRangeWhereFromEqualsToEqualsInteger_MIN_VALUE() {
		assertThat(List.range(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(List.of(Integer.MIN_VALUE));
	}

	// -- static until(int, int)

	@Test
	public void shouldCreateListOfUntilWhereFromIsGreaterThanTo() {
		assertThat(List.until(1, 0)).isEqualTo(List.nil());
	}

	@Test
	public void shouldCreateListOfUntilWhereFromEqualsTo() {
		assertThat(List.until(0, 0)).isEqualTo(List.nil());
	}

	@Test
	public void shouldCreateListOfUntilWhereFromIsLessThanTo() {
		assertThat(List.until(1, 3)).isEqualTo(List.of(1, 2));
	}

	@Test
	public void shouldCreateListOfUntilWhereFromEqualsToEqualsInteger_MIN_VALUE() {
		assertThat(List.until(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(List.nil());
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
