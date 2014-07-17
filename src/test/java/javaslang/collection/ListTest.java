/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class ListTest {

	// -- head

	@Test(expected = UnsupportedOperationException.class)
	public void shouldThrowWhenHeadOnEmptyList() {
		EmptyList.instance().head();
	}

	@Test
	public void shouldReturnHeadOfNonEmptyList() {
		final Integer actual = List.of(1, 2, 3).head();
		assertThat(actual).isEqualTo(1);
	}

	// -- tail

	@Test(expected = UnsupportedOperationException.class)
	public void shouldThrowWhenTailOnEmptyList() {
		EmptyList.instance().tail();
	}

	@Test
	public void shouldReturnTailOfNonEmptyList() {
		final List<Integer> actual = List.of(1, 2, 3).tail();
		final List<Integer> expected = List.of(2, 3);
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

	// -- reverse

	@Test
	public void shouldReverseEmptyList() {
		assertThat(List.empty().reverse()).isEqualTo(List.empty());
	}

	@Test
	public void shouldReverseNonEmptyList() {
		assertThat(List.of(1, 2, 3).reverse()).isEqualTo(List.of(3, 2, 1));
	}

	// -- size

	@Test
	public void shouldComputeSizeOfEmptyList() {
		assertThat(List.empty().size()).isEqualTo(0);
	}

	@Test
	public void shouldComputeSizeOfNonEmptyList() {
		assertThat(List.of(1, 2, 3).size()).isEqualTo(3);
	}

	// -- append

	@Test
	public void shouldAppendElementToEmptyList() {
		final List<Integer> actual = List.<Integer> empty().append(1);
		final List<Integer> expected = List.of(1);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldAppendElementToNonEmptyList() {
		final List<Integer> actual = List.of(1).append(2);
		final List<Integer> expected = List.of(1, 2);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldAppendEmptyListToEmptyList() {
		final List<Object> actual = List.empty().appendAll(List.empty());
		final List<Object> expected = List.empty();
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldAppendNonEmptyListToEmptyList() {
		final List<Integer> actual = List.<Integer> empty().appendAll(List.of(1, 2, 3));
		final List<Integer> expected = List.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldAppendEmptyListToNonEmptyList() {
		final List<Integer> actual = List.of(1, 2, 3).appendAll(List.empty());
		final List<Integer> expected = List.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldAppendNonEmptyListToNonEmptyList() {
		final List<Integer> actual = List.of(1, 2, 3).appendAll(List.of(4, 5, 6));
		final List<Integer> expected = List.of(1, 2, 3, 4, 5, 6);
		assertThat(actual).isEqualTo(expected);
	}

	// -- prepend

	@Test
	public void shouldPrependElementToEmptyList() {
		final List<Integer> actual = List.<Integer> empty().prepend(1);
		final List<Integer> expected = List.of(1);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPrependElementToNonEmptyList() {
		final List<Integer> actual = List.of(2).prepend(1);
		final List<Integer> expected = List.of(1, 2);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPrependEmptyListToEmptyList() {
		final List<Integer> actual = List.<Integer> empty().prependAll(List.empty());
		final List<Integer> expected = List.empty();
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPrependEmptyListToNonEmptyList() {
		final List<Integer> actual = List.of(1, 2, 3).prependAll(List.empty());
		final List<Integer> expected = List.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPrependNonEmptyListToEmptyList() {
		final List<Integer> actual = List.<Integer> empty().prependAll(List.of(1, 2, 3));
		final List<Integer> expected = List.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPrependNonEmptyListToNonEmptyList() {
		final List<Integer> actual = List.of(4, 5, 6).prependAll(List.of(1, 2, 3));
		final List<Integer> expected = List.of(1, 2, 3, 4, 5, 6);
		assertThat(actual).isEqualTo(expected);
	}

	// -- contains

	@Test
	public void shouldRecognizeThatEmptyListContainsNoElement() {
		assertThat(List.empty().contains(null)).isFalse();
	}

	@Test
	public void shouldRecognizeThatNonEmptyListContainsAnElement() {
		assertThat(List.of(1).contains(1)).isTrue();
	}
	
	// -- get
	
	@Test
	public void shouldThrowWhenGetWithNegativeIndexOnEmptyList() {
		assertThat(() -> List.empty().get(-1)).isThrowing(IndexOutOfBoundsException.class, "get(-1) on empty list");
	}

	@Test
	public void shouldThrowWhenGetWithNegativeIndexOnNonEmptyList() {
		assertThat(() -> List.of(1).get(-1)).isThrowing(IndexOutOfBoundsException.class, "get(-1)");
	}

	@Test
	public void shouldThrowWhenGetOnEmptyList() {
		assertThat(() -> List.empty().get(0)).isThrowing(IndexOutOfBoundsException.class, "get(0) on empty list");
	}

	@Test
	public void shouldThrowWhenGetWithTooBigIndexOnNonEmptyList() {
		assertThat(() -> List.of(1).get(1)).isThrowing(IndexOutOfBoundsException.class, "get(1) on list of size 1");
	}

	@Test
	public void shouldGetFirstElement() {
		assertThat(List.of(1).get(0)).isEqualTo(1);
	}
	
}
