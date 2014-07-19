/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import javaslang.Lang.UnsatisfiedRequirementException;

import org.junit.Test;

public class ListTest {

	// -- head

	@Test
	public void shouldThrowWhenHeadOnEmptyList() {
		assertThat(() -> EmptyList.instance().head()).isThrowing(
				UnsupportedOperationException.class, "head of empty list");
	}

	@Test
	public void shouldReturnHeadOfNonEmptyList() {
		final Integer actual = List.of(1, 2, 3).head();
		assertThat(actual).isEqualTo(1);
	}

	// -- tail

	@Test
	public void shouldThrowWhenTailOnEmptyList() {
		assertThat(() -> EmptyList.instance().tail()).isThrowing(
				UnsupportedOperationException.class, "tail of empty list");
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

	// -- appendAll

	@Test
	public void shouldThrowOnAppendAllOfNull() {
		assertThat(() -> List.empty().appendAll(null)).isThrowing(
				UnsatisfiedRequirementException.class, "elements is null");
	}

	@Test
	public void shouldAppendAllEmptyListToEmptyList() {
		final List<Object> actual = List.empty().appendAll(List.empty());
		final List<Object> expected = List.empty();
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldAppendAllNonEmptyListToEmptyList() {
		final List<Integer> actual = List.<Integer> empty().appendAll(List.of(1, 2, 3));
		final List<Integer> expected = List.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldAppendAllEmptyListToNonEmptyList() {
		final List<Integer> actual = List.of(1, 2, 3).appendAll(List.empty());
		final List<Integer> expected = List.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldAppendAllNonEmptyListToNonEmptyList() {
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

	// -- prependAll

	@Test
	public void shouldThrowOnPrependAllOfNull() {
		assertThat(() -> List.empty().prependAll(null)).isThrowing(
				UnsatisfiedRequirementException.class, "elements is null");
	}

	@Test
	public void shouldPrependAllEmptyListToEmptyList() {
		final List<Integer> actual = List.<Integer> empty().prependAll(List.empty());
		final List<Integer> expected = List.empty();
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPrependAllEmptyListToNonEmptyList() {
		final List<Integer> actual = List.of(1, 2, 3).prependAll(List.empty());
		final List<Integer> expected = List.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPrependAllNonEmptyListToEmptyList() {
		final List<Integer> actual = List.<Integer> empty().prependAll(List.of(1, 2, 3));
		final List<Integer> expected = List.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPrependAllNonEmptyListToNonEmptyList() {
		final List<Integer> actual = List.of(4, 5, 6).prependAll(List.of(1, 2, 3));
		final List<Integer> expected = List.of(1, 2, 3, 4, 5, 6);
		assertThat(actual).isEqualTo(expected);
	}

	// -- insert

	@Test
	public void shouldInsertIntoEmptyList() {
		final List<Integer> actual = List.<Integer> empty().insert(0, 1);
		final List<Integer> expected = List.of(1);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldInsertInFrontOfElement() {
		final List<Integer> actual = List.of(4).insert(0, 1);
		final List<Integer> expected = List.of(1, 4);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldInsertBehindOfElement() {
		final List<Integer> actual = List.of(4).insert(1, 1);
		final List<Integer> expected = List.of(4, 1);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldInsertIntoList() {
		final List<Integer> actual = List.of(1, 2, 3).insert(2, 4);
		final List<Integer> expected = List.of(1, 2, 4, 3);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldThrowOnInsertWithNegativeIndex() {
		assertThat(() -> List.empty().insert(-1, null)).isThrowing(IndexOutOfBoundsException.class,
				"insert(-1, e)");
	}

	@Test
	public void shouldThrowOnInsertWhenExceedingUpperBound() {
		assertThat(() -> List.empty().insert(1, null)).isThrowing(IndexOutOfBoundsException.class,
				"insert(1, e) on list of size 0");
	}

	// -- insertAll

	@Test
	public void shouldInserAlltIntoEmptyList() {
		final List<Integer> actual = List.<Integer> empty().insertAll(0, List.of(1, 2, 3));
		final List<Integer> expected = List.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldInsertAllInFrontOfElement() {
		final List<Integer> actual = List.of(4).insertAll(0, List.of(1, 2, 3));
		final List<Integer> expected = List.of(1, 2, 3, 4);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldInsertAllBehindOfElement() {
		final List<Integer> actual = List.of(4).insertAll(1, List.of(1, 2, 3));
		final List<Integer> expected = List.of(4, 1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldInsertAllIntoList() {
		final List<Integer> actual = List.of(1, 2, 3).insertAll(2, List.of(4, 5));
		final List<Integer> expected = List.of(1, 2, 4, 5, 3);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldThrowOnInsertAllWithEmptyList() {
		assertThat(() -> List.empty().insertAll(0, null)).isThrowing(UnsatisfiedRequirementException.class,
				"elements is null");
	}

	@Test
	public void shouldThrowOnInsertAllWithNegativeIndex() {
		assertThat(() -> List.empty().insertAll(-1, List.empty())).isThrowing(IndexOutOfBoundsException.class,
				"insertAll(-1, elements)");
	}

	@Test
	public void shouldThrowOnInsertAllWhenExceedingUpperBound() {
		assertThat(() -> List.empty().insertAll(1, List.empty())).isThrowing(IndexOutOfBoundsException.class,
				"insertAll(1, elements) on list of size 0");
	}

	// -- contains

	@Test
	public void shouldRecognizeEmptyListContainsNoElement() {
		final boolean actual = List.empty().contains(null);
		assertThat(actual).isFalse();
	}

	@Test
	public void shouldRecognizeNonEmptyListDoesNotContainElement() {
		final boolean actual = List.of(1, 2, 3).contains(0);
		assertThat(actual).isFalse();
	}

	@Test
	public void shouldRecognizeNonEmptyListDoesContainElement() {
		final boolean actual = List.of(1, 2, 3).contains(2);
		assertThat(actual).isTrue();
	}

	// -- containsAll

	@Test
	public void shouldRecognizeEmptyListNotContainsAllElements() {
		final boolean actual = List.empty().containsAll(List.of(1, 2, 3));
		assertThat(actual).isFalse();
	}

	@Test
	public void shouldRecognizeNonEmptyListNotContainsAllOverlappingElements() {
		final boolean actual = List.of(1, 2, 3).containsAll(List.of(2, 3, 4));
		assertThat(actual).isFalse();
	}

	@Test
	public void shouldRecognizeNonEmptyListContainsAllOnSelf() {
		final boolean actual = List.of(1, 2, 3).containsAll(List.of(1, 2, 3));
		assertThat(actual).isTrue();
	}
	
	// TODO: shouldThrow..., see insert(index, element)

	// -- indexOf

	// TODO

	// -- lastIndexOf

	// TODO

	// -- get

	@Test
	public void shouldThrowWhenGetWithNegativeIndexOnEmptyList() {
		assertThat(() -> List.empty().get(-1)).isThrowing(IndexOutOfBoundsException.class,
				"get(-1) on empty list");
	}

	@Test
	public void shouldThrowWhenGetWithNegativeIndexOnNonEmptyList() {
		assertThat(() -> List.of(1).get(-1)).isThrowing(IndexOutOfBoundsException.class, "get(-1)");
	}

	@Test
	public void shouldThrowWhenGetOnEmptyList() {
		assertThat(() -> List.empty().get(0)).isThrowing(IndexOutOfBoundsException.class,
				"get(0) on empty list");
	}

	@Test
	public void shouldThrowWhenGetWithTooBigIndexOnNonEmptyList() {
		assertThat(() -> List.of(1).get(1)).isThrowing(IndexOutOfBoundsException.class,
				"get(1) on list of size 1");
	}

	@Test
	public void shouldGetFirstElement() {
		assertThat(List.of(1).get(0)).isEqualTo(1);
	}

	// -- set

	// TODO

	// -- sublist(beginIndex)

	@Test
	public void shouldReturnEmptyListWhenSublistFrom0OnEmptyList() {
		final List<Integer> actual = List.<Integer> empty().sublist(0);
		assertThat(actual).isEqualTo(List.empty());
	}

	@Test
	public void shouldReturnIdentityWhenSublistFrom0OnNonEmptyList() {
		final List<Integer> actual = List.of(1).sublist(0);
		assertThat(actual).isEqualTo(List.of(1));
	}

	@Test
	public void shouldReturnEmptyListWhenSublistFrom1OnListOf1() {
		final List<Integer> actual = List.of(1).sublist(1);
		assertThat(actual).isEqualTo(List.empty());
	}

	@Test
	public void shouldReturnSublistWhenIndexIsWithinRange() {
		final List<Integer> actual = List.of(1, 2, 3).sublist(1);
		assertThat(actual).isEqualTo(List.of(2, 3));
	}

	@Test
	public void shouldReturnEmptyListWhenSublistBeginningWithSize() {
		final List<Integer> actual = List.of(1, 2, 3).sublist(3);
		assertThat(actual).isEqualTo(List.empty());
	}

	@Test
	public void shouldThrowWhenSublist0OnEmptyList() {
		assertThat(() -> List.<Integer> empty().sublist(1)).isThrowing(
				IndexOutOfBoundsException.class, "sublist(1) on list of size 0");
	}

	@Test
	public void shouldThrowWhenSublistWithOutOfLowerBound() {
		assertThat(() -> List.of(1, 2, 3).sublist(-1)).isThrowing(IndexOutOfBoundsException.class,
				"sublist(-1)");
	}

	@Test
	public void shouldThrowWhenSublistWithOutOfUpperBound() {
		assertThat(() -> List.of(1, 2, 3).sublist(4)).isThrowing(IndexOutOfBoundsException.class,
				"sublist(4) on list of size 3");
	}

	// -- sublist(beginIndex, endIndex)

	@Test
	public void shouldReturnEmptyListWhenSublistFrom0To0OnEmptyList() {
		final List<Integer> actual = List.<Integer> empty().sublist(0, 0);
		assertThat(actual).isEqualTo(List.empty());
	}

	@Test
	public void shouldReturnEmptyListWhenSublistFrom0To0OnNonEmptyList() {
		final List<Integer> actual = List.of(1).sublist(0, 0);
		assertThat(actual).isEqualTo(List.empty());
	}

	@Test
	public void shouldReturnListWithFirstElementWhenSublistFrom0To1OnNonEmptyList() {
		final List<Integer> actual = List.of(1).sublist(0, 1);
		assertThat(actual).isEqualTo(List.of(1));
	}

	@Test
	public void shouldReturnEmptyListWhenSublistFrom1To1OnNonEmptyList() {
		final List<Integer> actual = List.of(1).sublist(1, 1);
		assertThat(actual).isEqualTo(List.empty());
	}

	@Test
	public void shouldReturnSublistWhenIndicesAreWithinRange() {
		final List<Integer> actual = List.of(1, 2, 3).sublist(1, 3);
		assertThat(actual).isEqualTo(List.of(2, 3));
	}

	@Test
	public void shouldReturnEmptyListWhenIndicesBothAreUpperBound() {
		final List<Integer> actual = List.of(1, 2, 3).sublist(3, 3);
		assertThat(actual).isEqualTo(List.empty());
	}

	@Test
	public void shouldThrowOnSublistWhenEndIndexIsGreaterThanBeginIndex() {
		assertThat(() -> List.of(1, 2, 3).sublist(1, 0)).isThrowing(
				IndexOutOfBoundsException.class, "sublist(1, 0) on list of size 3");
	}

	@Test
	public void shouldThrowOnSublistWhenBeginIndexExceedsLowerBound() {
		assertThat(() -> List.of(1, 2, 3).sublist(-1, 2)).isThrowing(
				IndexOutOfBoundsException.class, "sublist(-1, 2) on list of size 3");
	}

	@Test
	public void shouldThrowOnSublistWhenEndIndexExceedsUpperBound() {
		assertThat(() -> List.of(1, 2, 3).sublist(1, 4)).isThrowing(
				IndexOutOfBoundsException.class, "sublist(1, 4) on list of size 3");
	}

	// -- drop

	// TODO

	// -- take

	// TODO

	// -- toArray

	// TODO

	// -- toArrayList

	// TODO

	// -- sort

	// TODO

	// -- sort(Comparator)

	// TODO

	// -- stream

	// TODO

	// -- parallelStream

	// TODO

	// -- spliterator

	// TODO

	// -- iterator

	// TODO

	// -- equals

	// TODO

	// -- hashCode

	// TODO

	// -- toString

	// TODO

	// -- List.empty

	// TODO

	// -- List.of

	// TODO

	// -- List.collector

	// TODO

}
