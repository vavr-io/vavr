/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import javaslang.option.Option;

import org.junit.Test;

public class ListzTest {

	@Test
	public void shouldReturnNoneOnFirstElementOfNullList() {
		final List<?> list = null;
		final Option<?> actual = Listz.firstElement(list);
		assertThat(actual.isPresent()).isEqualTo(false);
	}
	
	@Test
	public void shouldReturnNoneOnFirstElementOfEmptyList() {
		final List<?> list = java.util.Collections.emptyList();
		final Option<?> actual = Listz.firstElement(list);
		assertThat(actual.isPresent()).isEqualTo(false);
	}
	
	@Test
	public void shouldReturnFirstElementOfNoneEmptyList() {
		final List<Integer> list = java.util.Arrays.asList(1, 2, 3);
		final Option<Integer> actual = Listz.firstElement(list);
		assertThat(actual.get()).isEqualTo(1);
	}
	
	@Test
	public void shouldReturnNoneOnLastElementOfNullList() {
		final List<?> list = null;
		final Option<?> actual = Listz.lastElement(list);
		assertThat(actual.isPresent()).isEqualTo(false);
	}
	
	@Test
	public void shouldReturnNoneOnLastElementOfEmptyList() {
		final List<?> list = java.util.Collections.emptyList();
		final Option<?> actual = Listz.lastElement(list);
		assertThat(actual.isPresent()).isEqualTo(false);
	}
	
	@Test
	public void shouldReturnLastElementOfNoneEmptyList() {
		final List<Integer> list = java.util.Arrays.asList(1, 2, 3);
		final Option<Integer> actual = Listz.lastElement(list);
		assertThat(actual.get()).isEqualTo(3);
	}

}
