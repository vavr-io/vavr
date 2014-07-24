/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;

import javaslang.Requirements.UnsatisfiedRequirementException;

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
		assertThat(() -> List.empty().insertAll(0, null)).isThrowing(
				UnsatisfiedRequirementException.class, "elements is null");
	}

	@Test
	public void shouldThrowOnInsertAllWithNegativeIndex() {
		assertThat(() -> List.empty().insertAll(-1, List.empty())).isThrowing(
				IndexOutOfBoundsException.class, "insertAll(-1, elements)");
	}

	@Test
	public void shouldThrowOnInsertAllWhenExceedingUpperBound() {
		assertThat(() -> List.empty().insertAll(1, List.empty())).isThrowing(
				IndexOutOfBoundsException.class, "insertAll(1, elements) on list of size 0");
	}

	// -- remove

	@Test
	public void shouldRemoveElementFromEmptyList() {
		assertThat(List.empty().remove(null)).isEqualTo(List.empty());
	}

	@Test
	public void shouldRemoveFirstElement() {
		assertThat(List.of(1, 2, 3).remove(1)).isEqualTo(List.of(2, 3));
	}

	@Test
	public void shouldRemoveLastElement() {
		assertThat(List.of(1, 2, 3).remove(3)).isEqualTo(List.of(1, 2));
	}

	@Test
	public void shouldRemoveInnerElement() {
		assertThat(List.of(1, 2, 3).remove(2)).isEqualTo(List.of(1, 3));
	}

	@Test
	public void shouldRemoveNonExistingElement() {
		assertThat(List.of(1, 2, 3).remove(4)).isEqualTo(List.of(1, 2, 3));
	}

	// -- removeAll

	@Test
	public void shouldRemoveAllElementsFromEmptyList() {
		assertThat(List.empty().removeAll(List.of(1, 2, 3))).isEqualTo(List.empty());
	}

	@Test
	public void shouldRemoveAllExistingElementsFromNonEmptyList() {
		assertThat(List.of(1, 2, 3, 1, 2, 3).removeAll(List.of(1, 2))).isEqualTo(List.of(3, 3));
	}

	@Test
	public void shouldNotRemoveAllNonExistingElementsFromNonEmptyList() {
		assertThat(List.of(1, 2, 3).removeAll(List.of(4, 5))).isEqualTo(List.of(1, 2, 3));
	}

	// -- retainAll

	@Test
	public void shouldRetainAllElementsFromEmptyList() {
		assertThat(List.empty().retainAll(List.of(1, 2, 3))).isEqualTo(List.empty());
	}

	@Test
	public void shouldRetainAllExistingElementsFromNonEmptyList() {
		assertThat(List.of(1, 2, 3, 1, 2, 3).retainAll(List.of(1, 2))).isEqualTo(
				List.of(1, 2, 1, 2));
	}

	@Test
	public void shouldNotRetainAllNonExistingElementsFromNonEmptyList() {
		assertThat(List.of(1, 2, 3).retainAll(List.of(4, 5))).isEqualTo(List.empty());
	}

	// -- replaceAll

	@Test
	public void shouldReplaceAllElementsOfEmptyList() {
		assertThat(List.<Integer> empty().replaceAll(i -> i + 1)).isEqualTo(List.empty());
	}

	@Test
	public void shouldReplaceAllElementsOfNonEmptyList() {
		assertThat(List.of(1, 2, 3).replaceAll(i -> i + 1)).isEqualTo(List.of(2, 3, 4));
	}

	// -- clear

	@Test
	public void shouldClearEmptyList() {
		assertThat(List.empty().clear()).isEqualTo(List.empty());
	}

	@Test
	public void shouldClearNonEmptyList() {
		assertThat(List.of(1, 2, 3).clear()).isEqualTo(List.empty());
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

	// -- indexOf

	@Test
	public void shouldNotFindIndexOfElementWhenListIsEmpty() {
		assertThat(List.empty().indexOf(1)).isEqualTo(-1);
	}

	@Test
	public void shouldFindIndexOfFirstElement() {
		assertThat(List.of(1, 2, 3).indexOf(1)).isEqualTo(0);
	}

	@Test
	public void shouldFindIndexOfInnerElement() {
		assertThat(List.of(1, 2, 3).indexOf(2)).isEqualTo(1);
	}

	@Test
	public void shouldFindIndexOfLastElement() {
		assertThat(List.of(1, 2, 3).indexOf(3)).isEqualTo(2);
	}

	// -- lastIndexOf

	@Test
	public void shouldNotFindLastIndexOfElementWhenListIsEmpty() {
		assertThat(List.empty().lastIndexOf(1)).isEqualTo(-1);
	}

	@Test
	public void shouldFindLastIndexOfElement() {
		assertThat(List.of(1, 2, 3, 1, 2, 3).lastIndexOf(1)).isEqualTo(3);
	}

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
		assertThat(List.of(1, 2, 3).get(0)).isEqualTo(1);
	}

	@Test
	public void shouldGetLastElement() {
		assertThat(List.of(1, 2, 3).get(2)).isEqualTo(3);
	}

	// -- set

	@Test
	public void shouldThrowWhenSetWithNegativeIndexOnEmptyList() {
		assertThat(() -> List.empty().set(-1, null)).isThrowing(IndexOutOfBoundsException.class,
				"set(-1, e) on empty list");
	}

	@Test
	public void shouldThrowWhenSetWithNegativeIndexOnNonEmptyList() {
		assertThat(() -> List.of(1).set(-1, 2)).isThrowing(IndexOutOfBoundsException.class,
				"set(-1, e)");
	}

	@Test
	public void shouldThrowWhenSetOnEmptyList() {
		assertThat(() -> List.empty().set(0, null)).isThrowing(IndexOutOfBoundsException.class,
				"set(0, e) on empty list");
	}

	@Test
	public void shouldThrowWhenSetWithTooBigIndexOnNonEmptyList() {
		assertThat(() -> List.of(1).set(1, 2)).isThrowing(IndexOutOfBoundsException.class,
				"set(1, e) on list of size 1");
	}

	@Test
	public void shouldSetFirstElement() {
		assertThat(List.of(1, 2, 3).set(0, 4)).isEqualTo(List.of(4, 2, 3));
	}

	@Test
	public void shouldSetLastElement() {
		assertThat(List.of(1, 2, 3).set(2, 4)).isEqualTo(List.of(1, 2, 4));
	}

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

	@Test
	public void shouldDropNoneOnEmptyList() {
		assertThat(List.empty().drop(1)).isEqualTo(List.empty());
	}

	@Test
	public void shouldDropNoneIfCountIsNegative() {
		assertThat(List.of(1, 2, 3).drop(-1)).isEqualTo(List.of(1, 2, 3));
	}

	@Test
	public void shouldDropAsExpectedIfCountIsLessThanSize() {
		assertThat(List.of(1, 2, 3).drop(2)).isEqualTo(List.of(3));
	}

	@Test
	public void shouldDropAllIfCountExceedsSize() {
		assertThat(List.of(1, 2, 3).drop(4)).isEqualTo(List.empty());
	}

	// -- take

	@Test
	public void shouldTakeNoneOnEmptyList() {
		assertThat(List.empty().take(1)).isEqualTo(List.empty());
	}

	@Test
	public void shouldTakeNoneIfCountIsNegative() {
		assertThat(List.of(1, 2, 3).take(-1)).isEqualTo(List.empty());
	}

	@Test
	public void shouldTakeAsExpectedIfCountIsLessThanSize() {
		assertThat(List.of(1, 2, 3).take(2)).isEqualTo(List.of(1, 2));
	}

	@Test
	public void shouldTakeAllIfCountExceedsSize() {
		assertThat(List.of(1, 2, 3).take(4)).isEqualTo(List.of(1, 2, 3));
	}

	// -- toArray

	@Test
	public void shouldConvertEmptyListToArray() {
		assertThat(List.<Integer> empty().toArray()).isEqualTo(new Integer[] {});
	}

	@Test
	public void shouldConvertNonEmptyListToArray() {
		assertThat(List.of(1, 2, 3).toArray()).isEqualTo(new Integer[] { 1, 2, 3 });
	}

	// -- toArray(E[])

	@Test
	public void shouldConvertEmptyListGivenEmptyArray() {
		final Integer[] actual = List.<Integer> empty().toArray(new Integer[] {});
		final Integer[] expected = new Integer[] {};
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldConvertEmptyListGivenNonEmptyArray() {
		final Integer[] array = List.<Integer> empty().toArray(new Integer[] { 9, 9, 9 });
		final Integer[] expected = new Integer[] { null, 9, 9 };
		assertThat(array).isEqualTo(expected);
	}

	@Test
	public void shouldConvertNonEmptyListToGivenArrayIfSizeIsSmaller() {
		final Integer[] array = List.of(1, 2).toArray(new Integer[] { 9, 9, 9 });
		final Integer[] expected = new Integer[] { 1, 2, null };
		assertThat(array).isEqualTo(expected);
	}

	@Test
	public void shouldConvertNonEmptyListToGivenArrayIfSizeIsEqual() {
		final Integer[] actual = List.of(1, 2, 3).toArray(new Integer[] { 9, 9, 9 });
		final Integer[] expected = new Integer[] { 1, 2, 3 };
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldConvertNonEmptyListToGivenArrayIfSizeIsBigger() {
		final Integer[] array = List.of(1, 2, 3, 4).toArray(new Integer[] { 9, 9, 9 });
		final Integer[] expected = new Integer[] { 1, 2, 3, 4 };
		assertThat(array).isEqualTo(expected);
	}

	// -- toArrayList

	@Test
	public void shouldConvertEmptyListToArrayList() {
		assertThat(List.<Integer> empty().toArrayList()).isEqualTo(new ArrayList<Integer>());
	}

	@Test
	public void shouldConvertNonEmptyListToArrayList() {
		assertThat(List.of(1, 2, 3).toArrayList()).isEqualTo(Arrays.asList(1, 2, 3));
	}

	// -- sort

	@Test
	public void shouldSortEmptyList() {
		assertThat(List.empty().sort()).isEqualTo(List.empty());
	}

	@Test
	public void shouldSortNonEmptyList() {
		assertThat(List.of(3, 4, 1, 2).sort()).isEqualTo(List.of(1, 2, 3, 4));
	}

	// -- sort(Comparator)

	@Test
	public void shouldSortEmptyListUsingComparator() {
		assertThat(List.<Integer> empty().sort((i, j) -> j - i)).isEqualTo(List.empty());
	}

	@Test
	public void shouldSortNonEmptyListUsingComparator() {
		assertThat(List.of(3, 4, 1, 2).sort((i, j) -> j - i)).isEqualTo(List.of(4, 3, 2, 1));
	}

	// -- stream

	@Test
	public void shouldStreamAndCollectEmptyList() {
		assertThat(List.empty().stream().collect(List.collector())).isEqualTo(List.empty());
	}

	@Test
	public void shouldStreamAndCollectNonEmptyList() {
		assertThat(List.of(1, 2, 3).stream().collect(List.collector())).isEqualTo(List.of(1, 2, 3));
	}

	// -- parallelStream

	@Test
	public void shouldParallelStreamAndCollectEmptyList() {
		assertThat(List.empty().parallelStream().collect(List.collector())).isEqualTo(List.empty());
	}

	@Test
	public void shouldParallelStreamAndCollectNonEmptyList() {
		assertThat(List.of(1, 2, 3).parallelStream().collect(List.collector())).isEqualTo(
				List.of(1, 2, 3));
	}

	// -- spliterator

	@Test
	public void shouldSplitEmptyList() {
		final java.util.List<Integer> actual = new java.util.ArrayList<>();
		List.<Integer> empty().spliterator().forEachRemaining(i -> actual.add(i));
		assertThat(actual).isEqualTo(Arrays.asList());
	}

	@Test
	public void shouldSplitNonEmptyList() {
		final java.util.List<Integer> actual = new java.util.ArrayList<>();
		List.of(1, 2, 3).spliterator().forEachRemaining(i -> actual.add(i));
		assertThat(actual).isEqualTo(Arrays.asList(1, 2, 3));
	}
	
	@Test
	public void shouldHaveImmutableSpliterator() {
		assertThat(List.of(1, 2, 3).spliterator().characteristics() & Spliterator.IMMUTABLE).isNotZero();
	}

	@Test
	public void shouldHaveOrderedSpliterator() {
		assertThat(List.of(1, 2, 3).spliterator().characteristics() & Spliterator.ORDERED).isNotZero();
	}

	@Test
	public void shouldHaveSizedSpliterator() {
		assertThat(List.of(1, 2, 3).spliterator().characteristics() & Spliterator.SIZED).isNotZero();
	}

	@Test
	public void shouldReturnSizeWhenSpliterator() {
		assertThat(List.of(1, 2, 3).spliterator().getExactSizeIfKnown()).isEqualTo(3);
	}

	// -- iterator

	@Test
	public void shouldNotHasNextWhenEmptyListIterator() {
		assertThat(List.empty().iterator().hasNext()).isFalse();
	}

	@Test
	public void shouldThrowOnNextWhenEmptyListIterator() {
		assertThat(() -> List.empty().iterator().next()).isThrowing(NoSuchElementException.class,
				null);
	}

	@Test
	public void shouldIterateFirstElementOfNonEmptyList() {
		assertThat(List.of(1, 2, 3).iterator().next()).isEqualTo(1);
	}

	@Test
	public void shouldFullyIterateNonEmptyList() {
		int actual = -1;
		for (Iterator<Integer> iter = List.of(1, 2, 3).iterator(); iter.hasNext(); actual = iter
				.next())
			;
		assertThat(actual).isEqualTo(3);
	}

	// -- iterator(int)

	@Test
	public void shouldThrowWhenEmptyListIteratorStartingAtIndex() {
		assertThat(() -> { List.empty().iterator(1); }).isThrowing(IndexOutOfBoundsException.class,
				"sublist(1) on list of size 0");
	}

	@Test
	public void shouldIterateFirstElementOfNonEmptyListStartingAtIndex() {
		assertThat(List.of(1, 2, 3).iterator(1).next()).isEqualTo(2);
	}

	@Test
	public void shouldFullyIterateNonEmptyListStartingAtIndex() {
		int actual = -1;
		for (Iterator<Integer> iter = List.of(1, 2, 3).iterator(1); iter.hasNext(); actual = iter
				.next())
			;
		assertThat(actual).isEqualTo(3);
	}

	// -- equals

	@Test
	public void shouldRecognizeEqualityOfEmptyLists() {
		assertThat(List.empty().equals(List.empty())).isTrue();
	}

	@Test
	public void shouldRecognizeEqualityOfNonEmptyLists() {
		assertThat(List.of(1, 2, 3).equals(List.of(1, 2, 3))).isTrue();
	}

	@Test
	public void shouldRecognizeNonEqualityOfListsOfSameSize() {
		assertThat(List.of(1, 2, 3).equals(List.of(1, 2, 4))).isFalse();
	}

	@Test
	public void shouldRecognizeNonEqualityOfListsOfDifferentSize() {
		assertThat(List.of(1, 2, 3).equals(List.of(1, 2))).isFalse();
	}

	// -- hashCode

	@Test
	public void shouldCalculateHashCodeOfEmptyList() {
		assertThat(List.empty().hashCode()).isEqualTo(1);
	}

	@Test
	public void shouldCalculateHashCodeOfNonEmptyList() {
		assertThat(List.of(1, 2, 3).hashCode()).isEqualTo(31 * (31 * (31 * 1 + 1) + 2) + 3);
	}

	// -- toString

	@Test
	public void shouldStringifyEmptyList() {
		assertThat(List.empty().toString()).isEqualTo("()");
	}

	@Test
	public void shouldStringifyNonEmptyList() {
		assertThat(List.of(1, 2, 3).toString()).isEqualTo("(1, 2, 3)");
	}

	// -- List.empty()

	@Test
	public void shouldCreateEmptyList() {
		assertThat(List.empty()).isEqualTo(EmptyList.instance());
	}

	// -- List.of(T, T...)

	@Test
	public void shouldCreateListOfElements() {
		final List<Object> actual = List.of(1, 2);
		final List<Object> expected = new LinearList<>(1, new LinearList<>(2, EmptyList.instance()));
		assertThat(actual).isEqualTo(expected);
	}

	// -- List.of(Iterable)

	@Test
	public void shouldCreateListOfIterable() {
		final java.util.List<Integer> arrayList = Arrays.asList(1, 2, 3);
		assertThat(List.of(arrayList)).isEqualTo(List.of(1, 2, 3));
	}

}
