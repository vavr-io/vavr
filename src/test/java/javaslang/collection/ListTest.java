/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static org.fest.assertions.api.Assertions.assertThat;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

public class ListTest {
	
	// -- head
	
	@Test(expected = UnsupportedOperationException.class)
	public void shouldThrowWhenHeadOnEmptyList() {
		EmptyList.instance().head();
	}
	
	@Test
	public void shouldReturnHeadOfNonEmptyList() {
		final Integer actual = List.of(1,2,3).head();
		assertThat(actual).isEqualTo(1);
	}
	
	// -- tail

	@Test(expected = UnsupportedOperationException.class)
	public void shouldThrowWhenTailOnEmptyList() {
		EmptyList.instance().tail();
	}

	@Test
	public void shouldReturnTailOfNonEmptyList() {
		final List<Integer> actual = List.of(1,2,3).tail();
		final List<Integer> expected = List.of(2,3);
		assertThat(actual).isEqualTo(expected);
	}
	
	// -- isEmpty
	
	@Test
	public void shouldRecognizeEmptyList() {
		assertThat(List.empty().isEmpty()).isTrue();
	}

	@Test
	public void shouldRecognizeNonEmptyList() {
		assertThat(List.of(1).isEmpty()).isFalse();
	}

	// -- get
	
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
	
	// -- append
	
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

	// -- prepend

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
