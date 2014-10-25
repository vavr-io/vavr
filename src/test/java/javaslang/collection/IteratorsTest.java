/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javaslang.AssertionsExtensions;

import org.junit.Test;

public class IteratorsTest {

	@Test
	public void shouldNotBeInstantiable() {
		AssertionsExtensions.assertThat(Iterators.class).isNotInstantiable();
	}

	// -- of(iterator, while condition)

	@Test
	public void shouldDetectHasNext() {
		final Iterator<Integer> iterator = Iterators.of(Arrays.asList(1).iterator(), i -> i >= 1);
		assertThat(iterator.hasNext()).isTrue();
	}

	@Test
	public void shouldDetectNotHasNext() {
		final Iterator<Integer> iterator = Iterators.of(Arrays.asList(1).iterator(), i -> i < 1);
		assertThat(iterator.hasNext()).isFalse();
	}

	@Test
	public void shouldCreateIteratorByHasNextAndNext() {
		final Iterator<Integer> iterator = Iterators.of(Arrays.asList(1, 2).iterator(), i -> i < 3);
		iterator.next();
		iterator.next();
		assertThat(iterator.hasNext()).isFalse();
	}

	@Test(expected = NoSuchElementException.class)
	public void shouldThrowGivenIteratorByHasNextAndNextWhenNextAndNotHasNext() {
		final Iterator<Integer> iterator = Iterators.of(Arrays.asList(1, 2).iterator(), i -> i < 3);
		iterator.next();
		iterator.next();
		iterator.next();
	}
}
