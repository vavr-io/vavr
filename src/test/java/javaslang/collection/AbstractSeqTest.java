/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Algebra.Monoid;
import javaslang.AssertionsExtensions;
import javaslang.Require.UnsatisfiedRequirementException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractSeqTest {

    abstract protected <T> Seq<T> nil();

    @SuppressWarnings("unchecked")
    abstract protected <T> Seq<T> of(T... elements);

    abstract protected <T> Monoid<Seq<T>> zero();

    // -- append

    @Test
    public void shouldAppendElementToNil() {
        final Seq<Integer> actual = this.<Integer> nil().append(1);
        final Seq<Integer> expected = this.of(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendElementToNonNil() {
        final Seq<Integer> actual = this.of(1, 2).append(3);
        final Seq<Integer> expected = this.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    // -- appendAll

    @Test(expected = UnsatisfiedRequirementException.class)
    public void shouldThrowOnAppendAllOfNull() {
        this.nil().appendAll(null);
    }

    @Test
    public void shouldAppendAllNilToNil() {
        final Seq<Object> actual = this.nil().appendAll(this.nil());
        final Seq<Object> expected = this.nil();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNonNilToNil() {
        final Seq<Integer> actual = this.<Integer> nil().appendAll(this.of(1, 2, 3));
        final Seq<Integer> expected = this.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNilToNonNil() {
        final Seq<Integer> actual = this.of(1, 2, 3).appendAll(this.nil());
        final Seq<Integer> expected = this.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNonNilToNonNil() {
        final Seq<Integer> actual = this.of(1, 2, 3).appendAll(this.of(4, 5, 6));
        final Seq<Integer> expected = this.of(1, 2, 3, 4, 5, 6);
        assertThat(actual).isEqualTo(expected);
    }

    // -- clear

    @Test
    public void shouldClearNil() {
        assertThat(this.nil().clear()).isEqualTo(this.nil());
    }

    @Test
    public void shouldClearNonNil() {
        assertThat(this.of(1, 2, 3).clear()).isEqualTo(this.nil());
    }

    // -- contains

    @Test
    public void shouldRecognizeNilContainsNoElement() {
        final boolean actual = this.nil().contains(null);
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesNotContainElement() {
        final boolean actual = this.of(1, 2, 3).contains(0);
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesContainElement() {
        final boolean actual = this.of(1, 2, 3).contains(2);
        assertThat(actual).isTrue();
    }

    // -- containsAll

    @Test
    public void shouldRecognizeNilNotContainsAllElements() {
        final boolean actual = this.nil().containsAll(this.of(1, 2, 3));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilNotContainsAllOverlappingElements() {
        final boolean actual = this.of(1, 2, 3).containsAll(this.of(2, 3, 4));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilContainsAllOnSelf() {
        final boolean actual = this.of(1, 2, 3).containsAll(this.of(1, 2, 3));
        assertThat(actual).isTrue();
    }

    // -- combine // Monoid

    @Test
    public void shouldCombineTwoSeqs() {
        assertThat(this.zero().combine(this.of(1, 2), this.of(3, 4))).isEqualTo(this.of(1, 2, 3, 4));
    }

    @Test
    public void shouldCombineToRightIfLeftIsZero() {
        assertThat(this.zero().combine(this.nil(), this.of(1))).isEqualTo(this.of(1));
    }

    @Test
    public void shouldCombineToLeftIfRightIsZero() {
        assertThat(this.zero().combine(this.of(1), this.nil())).isEqualTo(this.of(1));
    }

    @Test(expected = UnsatisfiedRequirementException.class)
    public void shouldThrowWhenCombiningAndFirstArgIsNull() {
        this.zero().combine(null, this.nil());
    }

    @Test(expected = UnsatisfiedRequirementException.class)
    public void shouldThrowWhenCombiningAndSecondArgIsNull() {
        this.zero().combine(this.nil(), null);
    }

    // -- distinct

    @Test
    public void shouldComputeDistinctOfEmptySeq() {
        assertThat(this.nil().distinct()).isEqualTo(this.nil());
    }

    @Test
    public void shouldComputeDistinctOfNonEmptySeq() {
        assertThat(this.of(1, 1, 2, 2, 3, 3).distinct()).isEqualTo(this.of(1, 2, 3));
    }

    // -- drop

    @Test
    public void shouldDropNoneOnNil() {
        assertThat(this.nil().drop(1)).isEqualTo(this.nil());
    }

    @Test
    public void shouldDropNoneIfCountIsNegative() {
        assertThat(this.of(1, 2, 3).drop(-1)).isEqualTo(this.of(1, 2, 3));
    }

    @Test
    public void shouldDropAsExpectedIfCountIsLessThanSize() {
        assertThat(this.of(1, 2, 3).drop(2)).isEqualTo(this.of(3));
    }

    @Test
    public void shouldDropAllIfCountExceedsSize() {
        assertThat(this.of(1, 2, 3).drop(4)).isEqualTo(this.nil());
    }

    // -- dropRight

    @Test
    public void shouldDropRightNoneOnNil() {
        assertThat(this.nil().dropRight(1)).isEqualTo(this.nil());
    }

    @Test
    public void shouldDropRightNoneIfCountIsNegative() {
        assertThat(this.of(1, 2, 3).dropRight(-1)).isEqualTo(this.of(1, 2, 3));
    }

    @Test
    public void shouldDropRightAsExpectedIfCountIsLessThanSize() {
        assertThat(this.of(1, 2, 3).dropRight(2)).isEqualTo(this.of(1));
    }

    @Test
    public void shouldDropRightAllIfCountExceedsSize() {
        assertThat(this.of(1, 2, 3).dropRight(4)).isEqualTo(this.nil());
    }

    // -- dropWhile

    @Test
    public void shouldDropWhileNoneOnNil() {
        assertThat(this.nil().dropWhile(ignored -> true)).isEqualTo(this.nil());
    }

    @Test
    public void shouldDropWhileNoneIfPredicateIsFalse() {
        assertThat(this.of(1, 2, 3).dropWhile(ignored -> false)).isEqualTo(this.of(1, 2, 3));
    }

    @Test
    public void shouldDropWhileAllIfPredicateIsTrue() {
        assertThat(this.of(1, 2, 3).dropWhile(ignored -> true)).isEqualTo(this.nil());
    }

    @Test
    public void shouldDropWhileCorrect() {
        assertThat(this.of(1, 2, 3).dropWhile(i -> i < 2)).isEqualTo(this.of(2, 3));
    }

    // -- filter

    @Test
    public void shouldFilterEmptySeq() {
        assertThat(this.nil().filter(ignored -> true)).isEqualTo(this.nil());
    }

    @Test
    public void shouldFilterNonEmptySeq() {
        assertThat(this.of(1, 2, 3, 4).filter(i -> i % 2 == 0)).isEqualTo(this.of(2, 4));
    }

    // -- flatMap

    @Test
    public void shouldFlatMapEmptySeq() {
        assertThat(this.nil().flatMap(this::of)).isEqualTo(this.nil());
    }

    @Test
    public void shouldFlatMapNonEmptySeq() {
        assertThat(this.of(1, 2, 3).flatMap(this::of)).isEqualTo(this.of(1, 2, 3));
    }

    // -- get

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetWithNegativeIndexOnNil() {
        this.nil().get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetWithNegativeIndexOnNonNil() {
        this.of(1).get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetOnNil() {
        this.nil().get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetWithTooBigIndexOnNonNil() {
        this.of(1).get(1);
    }

    @Test
    public void shouldGetFirstElement() {
        assertThat(this.of(1, 2, 3).get(0)).isEqualTo(1);
    }

    @Test
    public void shouldGetLastElement() {
        assertThat(this.of(1, 2, 3).get(2)).isEqualTo(3);
    }

    // -- indexOf

    @Test
    public void shouldNotFindIndexOfElementWhenListIsEmpty() {
        assertThat(this.nil().indexOf(1)).isEqualTo(-1);
    }

    @Test
    public void shouldFindIndexOfFirstElement() {
        assertThat(this.of(1, 2, 3).indexOf(1)).isEqualTo(0);
    }

    @Test
    public void shouldFindIndexOfInnerElement() {
        assertThat(this.of(1, 2, 3).indexOf(2)).isEqualTo(1);
    }

    @Test
    public void shouldFindIndexOfLastElement() {
        assertThat(this.of(1, 2, 3).indexOf(3)).isEqualTo(2);
    }

    // -- init

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenInitOfNil() {
        this.nil().init();
    }

    @Test
    public void shouldGetInitOfNonNil() {
        assertThat(this.of(1, 2, 3).init()).isEqualTo(this.of(1, 2));
    }

    // -- insert

    @Test
    public void shouldInsertIntoNil() {
        final Seq<Integer> actual = this.<Integer> nil().insert(0, 1);
        final Seq<Integer> expected = this.of(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertInFrontOfElement() {
        final Seq<Integer> actual = this.of(4).insert(0, 1);
        final Seq<Integer> expected = this.of(1, 4);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertBehindOfElement() {
        final Seq<Integer> actual = this.of(4).insert(1, 1);
        final Seq<Integer> expected = this.of(4, 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertIntoList() {
        final Seq<Integer> actual = this.of(1, 2, 3).insert(2, 4);
        final Seq<Integer> expected = this.of(1, 2, 4, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnInsertWithNegativeIndex() {
        this.nil().insert(-1, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnInsertWhenExceedingUpperBound() {
        this.nil().insert(1, null);
    }

    // -- insertAll

    @Test
    public void shouldInserAlltIntoNil() {
        final Seq<Integer> actual = this.<Integer> nil().insertAll(0, this.of(1, 2, 3));
        final Seq<Integer> expected = this.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllInFrontOfElement() {
        final Seq<Integer> actual = this.of(4).insertAll(0, this.of(1, 2, 3));
        final Seq<Integer> expected = this.of(1, 2, 3, 4);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllBehindOfElement() {
        final Seq<Integer> actual = this.of(4).insertAll(1, this.of(1, 2, 3));
        final Seq<Integer> expected = this.of(4, 1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllIntoList() {
        final Seq<Integer> actual = this.of(1, 2, 3).insertAll(2, this.of(4, 5));
        final Seq<Integer> expected = this.of(1, 2, 4, 5, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = UnsatisfiedRequirementException.class)
    public void shouldThrowOnInsertAllWithNil() {
        this.nil().insertAll(0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnInsertAllWithNegativeIndex() {
        this.nil().insertAll(-1, this.nil());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnInsertAllWhenExceedingUpperBound() {
        this.nil().insertAll(1, this.nil());
    }

    // TODO: move tests from ListTest to here
}
