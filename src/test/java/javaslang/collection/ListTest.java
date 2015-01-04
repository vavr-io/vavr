/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;

import javaslang.*;
import javaslang.Algebra.Monoid;
import javaslang.Require.UnsatisfiedRequirementException;
import javaslang.Tuple.Tuple2;
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
	protected <T> Monoid<Seq<T>> zero() {
		return (Monoid<Seq<T>>) (Monoid) List.nil();
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

	// -- List.nil()

	@Test
	public void shouldCreateNil() {
		assertThat(List.nil()).isEqualTo(Nil.instance());
	}

	// -- List.of(T...)

	@Test
	public void shouldCreateListOfElements() {
		final List<Integer> actual = List.of(1, 2);
		final List<Integer> expected = new Cons<>(1, new Cons<>(2, Nil.instance()));
		assertThat(actual).isEqualTo(expected);
	}

	// -- List.of(Iterable)

	@Test
	public void shouldCreateListOfIterable() {
		final java.util.List<Integer> arrayList = Arrays.asList(1, 2, 3);
		assertThat(List.of(arrayList)).isEqualTo(List.of(1, 2, 3));
	}

	// -- List.range(int, int)

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

	// -- List.until(int, int)

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









	// -- head

	@Test
	public void shouldThrowWhenHeadOnNil() {
		AssertionsExtensions.assertThat(() -> List.nil().head()).isThrowing(UnsupportedOperationException.class,
				"head of empty list");
	}

	@Test
	public void shouldReturnHeadOfNonNil() {
		final Integer actual = List.of(1, 2, 3).head();
		assertThat(actual).isEqualTo(1);
	}

	// -- tail

	@Test
	public void shouldThrowWhenTailOnNil() {
		AssertionsExtensions.assertThat(() -> List.nil().tail()).isThrowing(UnsupportedOperationException.class,
				"tail of empty list");
	}

	@Test
	public void shouldReturnTailOfNonNil() {
		final List<Integer> actual = List.of(1, 2, 3).tail();
		final List<Integer> expected = List.of(2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	// -- isEmpty

	@Test
	public void shouldRecognizeNil() {
		assertThat(List.nil().isEmpty()).isTrue();
	}

	@Test
	public void shouldRecognizeNonNil() {
		assertThat(List.of(1).isEmpty()).isFalse();
	}

	// -- reverse

	@Test
	public void shouldReverseNil() {
		assertThat(List.nil().reverse()).isEqualTo(List.nil());
	}

	@Test
	public void shouldReverseNonNil() {
		assertThat(List.of(1, 2, 3).reverse()).isEqualTo(List.of(3, 2, 1));
	}

	// -- length

	@Test
	public void shouldComputeLengthOfNil() {
		assertThat(List.nil().length()).isEqualTo(0);
	}

	@Test
	public void shouldComputeLengthOfNonNil() {
		assertThat(List.of(1, 2, 3).length()).isEqualTo(3);
	}

	// -- prepend

	@Test
	public void shouldPrependElementToNil() {
		final List<Integer> actual = List.<Integer> nil().prepend(1);
		final List<Integer> expected = List.of(1);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPrependElementToNonNil() {
		final List<Integer> actual = List.of(2, 3).prepend(1);
		final List<Integer> expected = List.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	// -- prependAll

	@Test
	public void shouldThrowOnPrependAllOfNull() {
		AssertionsExtensions.assertThat(() -> List.nil().prependAll(null)).isThrowing(
				UnsatisfiedRequirementException.class, "elements is null");
	}

	@Test
	public void shouldPrependAllNilToNil() {
		final List<Integer> actual = List.<Integer> nil().prependAll(List.nil());
		final List<Integer> expected = List.nil();
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPrependAllNilToNonNil() {
		final List<Integer> actual = List.of(1, 2, 3).prependAll(List.nil());
		final List<Integer> expected = List.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPrependAllNonNilToNil() {
		final List<Integer> actual = List.<Integer> nil().prependAll(List.of(1, 2, 3));
		final List<Integer> expected = List.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPrependAllNonNilToNonNil() {
		final List<Integer> actual = List.of(4, 5, 6).prependAll(List.of(1, 2, 3));
		final List<Integer> expected = List.of(1, 2, 3, 4, 5, 6);
		assertThat(actual).isEqualTo(expected);
	}



	// -- remove

	@Test
	public void shouldRemoveElementFromNil() {
		assertThat(List.nil().remove(null)).isEqualTo(List.nil());
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
	public void shouldRemoveAllElementsFromNil() {
		assertThat(List.nil().removeAll(List.of(1, 2, 3))).isEqualTo(List.nil());
	}

	@Test
	public void shouldRemoveAllExistingElementsFromNonNil() {
		assertThat(List.of(1, 2, 3, 1, 2, 3).removeAll(List.of(1, 2))).isEqualTo(List.of(3, 3));
	}

	@Test
	public void shouldNotRemoveAllNonExistingElementsFromNonNil() {
		assertThat(List.of(1, 2, 3).removeAll(List.of(4, 5))).isEqualTo(List.of(1, 2, 3));
	}

	// -- retainAll

	@Test
	public void shouldRetainAllElementsFromNil() {
		assertThat(List.nil().retainAll(List.of(1, 2, 3))).isEqualTo(List.nil());
	}

	@Test
	public void shouldRetainAllExistingElementsFromNonNil() {
		assertThat(List.of(1, 2, 3, 1, 2, 3).retainAll(List.of(1, 2))).isEqualTo(List.of(1, 2, 1, 2));
	}

	@Test
	public void shouldNotRetainAllNonExistingElementsFromNonNil() {
		assertThat(List.of(1, 2, 3).retainAll(List.of(4, 5))).isEqualTo(List.nil());
	}

	// -- replace(curr, new)

	@Test
	public void shouldReplaceElementOfNilUsingCurrNew() {
		assertThat(List.<Integer> nil().replace(1, 2)).isEqualTo(List.nil());
	}

	@Test
	public void shouldReplaceElementOfNonNilUsingCurrNew() {
		assertThat(List.of(0, 1, 2, 1).replace(1, 3)).isEqualTo(List.of(0, 3, 2, 1));
	}

	// -- replaceAll(curr, new)

	@Test
	public void shouldReplaceAllElementsOfNilUsingCurrNew() {
		assertThat(List.<Integer> nil().replaceAll(1, 2)).isEqualTo(List.nil());
	}

	@Test
	public void shouldReplaceAllElementsOfNonNilUsingCurrNew() {
		assertThat(List.of(0, 1, 2, 1).replaceAll(1, 3)).isEqualTo(List.of(0, 3, 2, 3));
	}

	// -- replaceAll(UnaryOp)

	@Test
	public void shouldReplaceAllElementsOfNilUsingUnaryOp() {
		assertThat(List.<Integer> nil().replaceAll(i -> i + 1)).isEqualTo(List.nil());
	}

	@Test
	public void shouldReplaceAllElementsOfNonNilUsingUnaryOp() {
		assertThat(List.of(1, 2, 3).replaceAll(i -> i + 1)).isEqualTo(List.of(2, 3, 4));
	}

	// -- lastIndexOf

	@Test
	public void shouldNotFindLastIndexOfElementWhenListIsEmpty() {
		assertThat(List.nil().lastIndexOf(1)).isEqualTo(-1);
	}

	@Test
	public void shouldFindLastIndexOfElement() {
		assertThat(List.of(1, 2, 3, 1, 2, 3).lastIndexOf(1)).isEqualTo(3);
	}

	// -- set

	@Test
	public void shouldThrowWhenSetWithNegativeIndexOnNil() {
		AssertionsExtensions.assertThat(() -> List.nil().set(-1, null)).isThrowing(IndexOutOfBoundsException.class,
				"set(-1, e) on empty list");
	}

	@Test
	public void shouldThrowWhenSetWithNegativeIndexOnNonNil() {
		AssertionsExtensions.assertThat(() -> List.of(1).set(-1, 2)).isThrowing(IndexOutOfBoundsException.class,
				"set(-1, e)");
	}

	@Test
	public void shouldThrowWhenSetOnNil() {
		AssertionsExtensions.assertThat(() -> List.nil().set(0, null)).isThrowing(IndexOutOfBoundsException.class,
				"set(0, e) on empty list");
	}

	@Test
	public void shouldThrowWhenSetWithIndexExceedingByOneOnNonNil() {
		AssertionsExtensions.assertThat(() -> List.of(1).set(1, 2)).isThrowing(IndexOutOfBoundsException.class,
				"set(1, e) on list of length 1");
	}

	@Test
	public void shouldThrowWhenSetWithIndexExceedingByTwoOnNonNil() {
		AssertionsExtensions.assertThat(() -> List.of(1).set(2, 2)).isThrowing(IndexOutOfBoundsException.class,
				"set(2, e) on list of length 1");
	}

	@Test
	public void shouldSetFirstElement() {
		assertThat(List.of(1, 2, 3).set(0, 4)).isEqualTo(List.of(4, 2, 3));
	}

	@Test
	public void shouldSetLastElement() {
		assertThat(List.of(1, 2, 3).set(2, 4)).isEqualTo(List.of(1, 2, 4));
	}

	// -- subsequence(beginIndex)

	@Test
	public void shouldReturnNilWhenSublistFrom0OnNil() {
		final List<Integer> actual = List.<Integer> nil().subsequence(0);
		assertThat(actual).isEqualTo(List.nil());
	}

	@Test
	public void shouldReturnIdentityWhenSublistFrom0OnNonNil() {
		final List<Integer> actual = List.of(1).subsequence(0);
		assertThat(actual).isEqualTo(List.of(1));
	}

	@Test
	public void shouldReturnNilWhenSublistFrom1OnListOf1() {
		final List<Integer> actual = List.of(1).subsequence(1);
		assertThat(actual).isEqualTo(List.nil());
	}

	@Test
	public void shouldReturnSublistWhenIndexIsWithinRange() {
		final List<Integer> actual = List.of(1, 2, 3).subsequence(1);
		assertThat(actual).isEqualTo(List.of(2, 3));
	}

	@Test
	public void shouldReturnNilWhenSublistBeginningWithSize() {
		final List<Integer> actual = List.of(1, 2, 3).subsequence(3);
		assertThat(actual).isEqualTo(List.nil());
	}

	@Test
	public void shouldThrowWhenSublist0OnNil() {
		AssertionsExtensions.assertThat(() -> List.<Integer> nil().subsequence(1)).isThrowing(
				IndexOutOfBoundsException.class, "subsequence(1) on list of length 0");
	}

	@Test
	public void shouldThrowWhenSublistWithOutOfLowerBound() {
		AssertionsExtensions.assertThat(() -> List.of(1, 2, 3).subsequence(-1)).isThrowing(IndexOutOfBoundsException.class,
				"subsequence(-1)");
	}

	@Test
	public void shouldThrowWhenSublistWithOutOfUpperBound() {
		AssertionsExtensions.assertThat(() -> List.of(1, 2, 3).subsequence(4)).isThrowing(IndexOutOfBoundsException.class,
				"subsequence(4) on list of length 3");
	}

	// -- subsequence(beginIndex, endIndex)

	@Test
	public void shouldReturnNilWhenSublistFrom0To0OnNil() {
		final List<Integer> actual = List.<Integer> nil().subsequence(0, 0);
		assertThat(actual).isEqualTo(List.nil());
	}

	@Test
	public void shouldReturnNilWhenSublistFrom0To0OnNonNil() {
		final List<Integer> actual = List.of(1).subsequence(0, 0);
		assertThat(actual).isEqualTo(List.nil());
	}

	@Test
	public void shouldReturnListWithFirstElementWhenSublistFrom0To1OnNonNil() {
		final List<Integer> actual = List.of(1).subsequence(0, 1);
		assertThat(actual).isEqualTo(List.of(1));
	}

	@Test
	public void shouldReturnNilWhenSublistFrom1To1OnNonNil() {
		final List<Integer> actual = List.of(1).subsequence(1, 1);
		assertThat(actual).isEqualTo(List.nil());
	}

	@Test
	public void shouldReturnSublistWhenIndicesAreWithinRange() {
		final List<Integer> actual = List.of(1, 2, 3).subsequence(1, 3);
		assertThat(actual).isEqualTo(List.of(2, 3));
	}

	@Test
	public void shouldReturnNilWhenIndicesBothAreUpperBound() {
		final List<Integer> actual = List.of(1, 2, 3).subsequence(3, 3);
		assertThat(actual).isEqualTo(List.nil());
	}

	@Test
	public void shouldThrowOnSublistWhenEndIndexIsGreaterThanBeginIndex() {
		AssertionsExtensions.assertThat(() -> List.of(1, 2, 3).subsequence(1, 0)).isThrowing(
				IndexOutOfBoundsException.class, "subsequence(1, 0) on list of length 3");
	}

	@Test
	public void shouldThrowOnSublistWhenBeginIndexExceedsLowerBound() {
		AssertionsExtensions.assertThat(() -> List.of(1, 2, 3).subsequence(-1, 2)).isThrowing(
				IndexOutOfBoundsException.class, "subsequence(-1, 2) on list of length 3");
	}

	@Test
	public void shouldThrowOnSublistWhenEndIndexExceedsUpperBound() {
		AssertionsExtensions.assertThat(() -> List.of(1, 2, 3).subsequence(1, 4)).isThrowing(
				IndexOutOfBoundsException.class, "subsequence(1, 4) on list of length 3");
	}

	// -- take

	@Test
	public void shouldTakeNoneOnNil() {
		assertThat(List.nil().take(1)).isEqualTo(List.nil());
	}

	@Test
	public void shouldTakeNoneIfCountIsNegative() {
		assertThat(List.of(1, 2, 3).take(-1)).isEqualTo(List.nil());
	}

	@Test
	public void shouldTakeAsExpectedIfCountIsLessThanSize() {
		assertThat(List.of(1, 2, 3).take(2)).isEqualTo(List.of(1, 2));
	}

	@Test
	public void shouldTakeAllIfCountExceedsSize() {
		assertThat(List.of(1, 2, 3).take(4)).isEqualTo(List.of(1, 2, 3));
	}

	// -- takeWhile

	@Test
	public void shouldTakeWhileNoneOnNil() {
		assertThat(List.nil().takeWhile(x -> true)).isEqualTo(List.nil());
	}

	@Test
	public void shouldTakeWhileAllOnFalseCondition() {
		assertThat(List.of(1, 2, 3).takeWhile(x -> false)).isEqualTo(List.nil());
	}

	@Test
	public void shouldTakeWhileAllOnTrueCondition() {
		assertThat(List.of(1, 2, 3).takeWhile(x -> true)).isEqualTo(List.of(1, 2, 3));
	}

	@Test
	public void shouldTakeWhileAsExpected() {
		assertThat(List.of(2, 4, 5, 6).takeWhile(x -> x % 2 == 0)).isEqualTo(List.of(2, 4));
	}

	// -- zip

	@Test
	public void shouldZipNils() {
		final List<?> actual = List.nil().zip(List.nil());
		assertThat(actual).isEqualTo(List.nil());
	}

	@Test
	public void shouldZipEmptyAndNonNil() {
		final List<?> actual = List.nil().zip(List.of(1));
		assertThat(actual).isEqualTo(List.nil());
	}

	@Test
	public void shouldZipNonEmptyAndNil() {
		final List<?> actual = List.of(1).zip(List.nil());
		assertThat(actual).isEqualTo(List.nil());
	}

	@Test
	public void shouldZipNonNilsIfThisIsSmaller() {
		final List<Tuple2<Integer, String>> actual = List.of(1, 2).zip(List.of("a", "b", "c"));
		final List<Tuple2<Integer, String>> expected = List.of(Tuple.of(1, "a"), Tuple.of(2, "b"));
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldZipNonNilsIfThatIsSmaller() {
		final List<Tuple2<Integer, String>> actual = List.of(1, 2, 3).zip(List.of("a", "b"));
		final List<Tuple2<Integer, String>> expected = List.of(Tuple.of(1, "a"), Tuple.of(2, "b"));
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldZipNonNilsOfSameSize() {
		final List<Tuple2<Integer, String>> actual = List.of(1, 2, 3).zip(List.of("a", "b", "c"));
		final List<Tuple2<Integer, String>> expected = List.of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "c"));
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldThrowIfZipWithThatIsNull() {
		AssertionsExtensions.assertThat(() -> List.nil().zip(null)).isThrowing(UnsatisfiedRequirementException.class,
				"that is null");
	}

	// -- zipAll

	@Test
	public void shouldZipAllNils() {
		final List<?> actual = List.nil().zipAll(List.nil(), null, null);
		assertThat(actual).isEqualTo(List.nil());
	}

	@Test
	public void shouldZipAllEmptyAndNonNil() {
		final List<?> actual = List.nil().zipAll(List.of(1), null, null);
		assertThat(actual).isEqualTo(List.of(Tuple.of(null, 1)));
	}

	@Test
	public void shouldZipAllNonEmptyAndNil() {
		final List<?> actual = List.of(1).zipAll(List.nil(), null, null);
		assertThat(actual).isEqualTo(List.of(Tuple.of(1, null)));
	}

	@Test
	public void shouldZipAllNonNilsIfThisIsSmaller() {
		final List<Tuple2<Integer, String>> actual = List.of(1, 2).zipAll(List.of("a", "b", "c"), 9, "z");
		final List<Tuple2<Integer, String>> expected = List.of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(9, "c"));
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldZipAllNonNilsIfThatIsSmaller() {
		final List<Tuple2<Integer, String>> actual = List.of(1, 2, 3).zipAll(List.of("a", "b"), 9, "z");
		final List<Tuple2<Integer, String>> expected = List.of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "z"));
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldZipAllNonNilsOfSameSize() {
		final List<Tuple2<Integer, String>> actual = List.of(1, 2, 3).zipAll(List.of("a", "b", "c"), 9, "z");
		final List<Tuple2<Integer, String>> expected = List.of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "c"));
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldThrowIfZipAllWithThatIsNull() {
		AssertionsExtensions.assertThat(() -> List.nil().zipAll(null, null, null)).isThrowing(
				UnsatisfiedRequirementException.class, "that is null");
	}

	// -- zipWithIndex

	@Test
	public void shouldZipNilWithIndex() {
		assertThat(List.<String> nil().zipWithIndex()).isEqualTo(List.<Tuple2<String, Integer>> nil());
	}

	@Test
	public void shouldZipNonNilWithIndex() {
		final List<Tuple2<String, Integer>> actual = List.of("a", "b", "c").zipWithIndex();
		final List<Tuple2<String, Integer>> expected = List.of(Tuple.of("a", 0), Tuple.of("b", 1), Tuple.of("c", 2));
		assertThat(actual).isEqualTo(expected);
	}

	// -- toArray(Class)

	@Test
	public void shouldConvertNilToJavaArray() {
		final Integer[] actual = List.<Integer> nil().toJavaArray(Integer.class);
		final Integer[] expected = new Integer[] {};
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldConvertNonNilToJavaArray() {
		final Integer[] array = List.of(1, 2).toJavaArray(Integer.class);
		final Integer[] expected = new Integer[] { 1, 2 };
		assertThat(array).isEqualTo(expected);
	}

	// -- toArrayList

	@Test
	public void shouldConvertNilToArrayList() {
		assertThat(List.<Integer> nil().toJavaList()).isEqualTo(new ArrayList<Integer>());
	}

	@Test
	public void shouldConvertNonNilToArrayList() {
		assertThat(List.of(1, 2, 3).toJavaList()).isEqualTo(Arrays.asList(1, 2, 3));
	}

	// -- sort

	@Test
	public void shouldSortNil() {
		assertThat(List.nil().sort()).isEqualTo(List.nil());
	}

	@Test
	public void shouldSortNonNil() {
		assertThat(List.of(3, 4, 1, 2).sort()).isEqualTo(List.of(1, 2, 3, 4));
	}

	// -- sort(Comparator)

	@Test
	public void shouldSortNilUsingComparator() {
		assertThat(List.<Integer> nil().sort((i, j) -> j - i)).isEqualTo(List.nil());
	}

	@Test
	public void shouldSortNonNilUsingComparator() {
		assertThat(List.of(3, 4, 1, 2).sort((i, j) -> j - i)).isEqualTo(List.of(4, 3, 2, 1));
	}

	// -- spliterator

	@Test
	public void shouldSplitNil() {
		final java.util.List<Integer> actual = new java.util.ArrayList<>();
		List.<Integer> nil().spliterator().forEachRemaining(actual::add);
		assertThat(actual).isEqualTo(Arrays.asList());
	}

	@Test
	public void shouldSplitNonNil() {
		final java.util.List<Integer> actual = new java.util.ArrayList<>();
		List.of(1, 2, 3).spliterator().forEachRemaining(actual::add);
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

	// -- forEach

	// TODO: forEach

	// -- iterator

	@Test
	public void shouldNotHasNextWhenNilIterator() {
		assertThat(List.nil().iterator().hasNext()).isFalse();
	}

	@Test
	public void shouldThrowOnNextWhenNilIterator() {
		AssertionsExtensions.assertThat(() -> List.nil().iterator().next()).isThrowing(NoSuchElementException.class,
				null);
	}

	@Test
	public void shouldIterateFirstElementOfNonNil() {
		assertThat(List.of(1, 2, 3).iterator().next()).isEqualTo(1);
	}

	@Test
	public void shouldFullyIterateNonNil() {
		int actual = -1;
		for (Integer i : List.of(1, 2, 3)) {
			actual = i;
		}
		assertThat(actual).isEqualTo(3);
	}

	// -- iterator(int)

	@Test
	public void shouldThrowWhenNilIteratorStartingAtIndex() {
		AssertionsExtensions.assertThat(() ->  List.nil().iterator(1))
				.isThrowing(IndexOutOfBoundsException.class, "subsequence(1) on list of length 0");
	}

	@Test
	public void shouldIterateFirstElementOfNonNilStartingAtIndex() {
		assertThat(List.of(1, 2, 3).iterator(1).next()).isEqualTo(2);
	}

	@Test
	public void shouldFullyIterateNonNilStartingAtIndex() {
		int actual = -1;
		for (Iterator<Integer> iter = List.of(1, 2, 3).iterator(1); iter.hasNext(); ) {
			actual = iter.next();
		}
		assertThat(actual).isEqualTo(3);
	}

	// -- equals

	@Test
	public void shouldEqualSameListInstance() {
		final List<?> list = List.nil();
		assertThat(list).isEqualTo(list);
	}

	@Test
	public void shouldNilNotEqualsNull() {
		assertThat(List.nil()).isNotNull();
	}

	@Test
	public void shouldNonNilNotEqualsNull() {
		assertThat(List.of(1)).isNotNull();
	}

	@Test
	public void shouldEmptyNotEqualsDifferentType() {
		assertThat(List.nil()).isNotEqualTo("");
	}

	@Test
	public void shouldNonEmptyNotEqualsDifferentType() {
		assertThat(List.of(1)).isNotEqualTo("");
	}

	@Test
	public void shouldRecognizeEqualityOfNils() {
		assertThat(List.nil()).isEqualTo(List.nil());
	}

	@Test
	public void shouldRecognizeEqualityOfNonNils() {
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
	public void shouldCalculateHashCodeOfNil() {
		assertThat(List.nil().hashCode() == List.nil().hashCode()).isTrue();
	}

	@Test
	public void shouldCalculateHashCodeOfNonNil() {
		assertThat(List.of(1, 2).hashCode() == List.of(1, 2).hashCode()).isTrue();
	}

	@Test
	public void shouldCalculateDifferentHashCodesForDifferentLists() {
		assertThat(List.of(1, 2).hashCode() != List.of(2, 3).hashCode()).isTrue();
	}

	// -- toString

	@Test
	public void shouldStringifyNil() {
		assertThat(List.nil().toString()).isEqualTo("List()");
	}

	@Test
	public void shouldStringifyNonNil() {
		assertThat(List.of(1, 2, 3).toString()).isEqualTo("List(1, 2, 3)");
	}

	// -- Serializable interface

	@Test
	public void shouldSerializeDeserializeNil() {
		final Object actual = deserialize(serialize(List.nil()));
		final Object expected = List.nil();
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPreserveSingletonInstanceOnDeserialization() {
		final boolean actual = deserialize(serialize(List.nil())) == List.nil();
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldSerializeDeserializeNonNil() {
		final Object actual = deserialize(serialize(List.of(1, 2, 3)));
		final Object expected = List.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
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
