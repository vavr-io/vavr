/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

public class ListTest {

	@Test(expected = IndexOutOfBoundsException.class)
	public void shouldThrowOnGetOnEmptyList() {
		List.empty().get(0);
	}

	@Test
	public void shouldGetFirstElement() {
		List.of((Object) null).get(0);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void shouldThrowOnGetExceedingUpperBound() {
		List.of((Object) null).get(1);
	}
	
	@Test
	public void shouldAppendElementToEmptyList() {
		final List<Integer> actual = List.<Integer>empty().append(1);
		final List<Integer> expected = List.of(1);
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldAppendElementToNonEmptyList() {
		final List<Integer> actual = List.of(1).append(2);
		final List<Integer> expected = List.of(1, 2);
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldAppendList() {
		final List<Integer> actual = List.of(1, 2, 3).appendAll(List.of(4, 5, 6));
		final List<Integer> expected = List.of(1, 2, 3, 4, 5, 6);
		Assertions.assertThat(actual).isEqualTo(expected);
	}


	@Test
	public void shouldPrependElementToEmptyList() {
		final List<Integer> actual = List.<Integer>empty().prepend(1);
		final List<Integer> expected = List.of(1);
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPrependElementToNonEmptyList() {
		final List<Integer> actual = List.of(2).prepend(1);
		final List<Integer> expected = List.of(1, 2);
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPrependList() {
		final List<Integer> actual = List.of(4, 5, 6).prependAll(List.of(1, 2, 3));
		final List<Integer> expected = List.of(1, 2, 3, 4, 5, 6);
		Assertions.assertThat(actual).isEqualTo(expected);
	}

}
