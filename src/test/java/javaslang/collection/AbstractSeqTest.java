/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import org.junit.Test;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests all methods defined in {@link javaslang.collection.Seq}.
 */
public abstract class AbstractSeqTest extends AbstractTraversableTest {

    // -- construction

    @Override
    abstract protected <T> Seq<T> empty();

    @SuppressWarnings("unchecked")
    @Override
    abstract protected <T> Seq<T> of(T... elements);

    // -- range

    abstract protected Seq<Integer> range(int from, int toExclusive);

    abstract protected Seq<Integer> rangeBy(int from, int toExclusive, int step);

    abstract protected Seq<Long> range(long from, long toExclusive);

    abstract protected Seq<Long> rangeBy(long from, long toExclusive, long step);

    abstract protected Seq<Integer> rangeClosed(int from, int toInclusive);

    abstract protected Seq<Integer> rangeClosedBy(int from, int toInclusive, int step);

    abstract protected Seq<Long> rangeClosed(long from, long toInclusive);

    abstract protected Seq<Long> rangeClosedBy(long from, long toInclusive, long step);

    // -- append

    @Test
    public void shouldAppendElementToNil() {
        final Seq<Integer> actual = this.<Integer>empty().append(1);
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

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnAppendAllOfNull() {
        this.empty().appendAll(null);
    }

    @Test
    public void shouldAppendAllNilToNil() {
        final Seq<Object> actual = this.empty().appendAll(this.empty());
        final Seq<Object> expected = this.empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNonNilToNil() {
        final Seq<Integer> actual = this.<Integer>empty().appendAll(this.of(1, 2, 3));
        final Seq<Integer> expected = this.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNilToNonNil() {
        final Seq<Integer> actual = this.of(1, 2, 3).appendAll(this.empty());
        final Seq<Integer> expected = this.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNonNilToNonNil() {
        final Seq<Integer> actual = this.of(1, 2, 3).appendAll(this.of(4, 5, 6));
        final Seq<Integer> expected = this.of(1, 2, 3, 4, 5, 6);
        assertThat(actual).isEqualTo(expected);
    }

    // -- apply

    @Test
    public void shouldUseSeqAsPartialFunction() {
        assertThat(of(1, 2, 3).apply(1)).isEqualTo(2);
    }

    // -- get

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetWithNegativeIndexOnNil() {
        this.empty().get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetWithNegativeIndexOnNonNil() {
        this.of(1).get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetOnNil() {
        this.empty().get(0);
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
    public void shouldNotFindIndexOfElementWhenSeqIsEmpty() {
        assertThat(this.empty().indexOf(1)).isEqualTo(-1);
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

    // -- insert

    @Test
    public void shouldInsertIntoNil() {
        final Seq<Integer> actual = this.<Integer>empty().insert(0, 1);
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
    public void shouldInsertIntoSeq() {
        final Seq<Integer> actual = this.of(1, 2, 3).insert(2, 4);
        final Seq<Integer> expected = this.of(1, 2, 4, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnInsertWithNegativeIndex() {
        this.empty().insert(-1, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnInsertWhenExceedingUpperBound() {
        this.empty().insert(1, null);
    }

    // -- insertAll

    @Test
    public void shouldInserAlltIntoNil() {
        final Seq<Integer> actual = this.<Integer>empty().insertAll(0, this.of(1, 2, 3));
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
    public void shouldInsertAllIntoSeq() {
        final Seq<Integer> actual = this.of(1, 2, 3).insertAll(2, this.of(4, 5));
        final Seq<Integer> expected = this.of(1, 2, 4, 5, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnInsertAllWithNil() {
        this.empty().insertAll(0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnInsertAllWithNegativeIndex() {
        this.empty().insertAll(-1, this.empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnInsertAllWhenExceedingUpperBound() {
        this.empty().insertAll(1, this.empty());
    }

    // -- iterator(int)

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenNilIteratorStartingAtIndex() {
        this.empty().iterator(1);
    }

    @Test
    public void shouldIterateFirstElementOfNonNilStartingAtIndex() {
        assertThat(this.of(1, 2, 3).iterator(1).next()).isEqualTo(2);
    }

    @Test
    public void shouldFullyIterateNonNilStartingAtIndex() {
        int actual = -1;
        for (Iterator<Integer> iter = this.of(1, 2, 3).iterator(1); iter.hasNext(); ) {
            actual = iter.next();
        }
        assertThat(actual).isEqualTo(3);
    }

    // -- lastIndexOf

    @Test
    public void shouldNotFindLastIndexOfElementWhenSeqIsEmpty() {
        assertThat(this.empty().lastIndexOf(1)).isEqualTo(-1);
    }

    @Test
    public void shouldFindLastIndexOfElement() {
        assertThat(this.of(1, 2, 3, 1, 2, 3).lastIndexOf(1)).isEqualTo(3);
    }

    // -- prepend

    @Test
    public void shouldPrependElementToNil() {
        final Seq<Integer> actual = this.<Integer>empty().prepend(1);
        final Seq<Integer> expected = this.of(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependElementToNonNil() {
        final Seq<Integer> actual = this.of(2, 3).prepend(1);
        final Seq<Integer> expected = this.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    // -- prependAll

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnPrependAllOfNull() {
        this.empty().prependAll(null);
    }

    @Test
    public void shouldPrependAllNilToNil() {
        final Seq<Integer> actual = this.<Integer>empty().prependAll(this.empty());
        final Seq<Integer> expected = this.empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNilToNonNil() {
        final Seq<Integer> actual = this.of(1, 2, 3).prependAll(this.empty());
        final Seq<Integer> expected = this.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNonNilToNil() {
        final Seq<Integer> actual = this.<Integer>empty().prependAll(this.of(1, 2, 3));
        final Seq<Integer> expected = this.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNonNilToNonNil() {
        final Seq<Integer> actual = this.of(4, 5, 6).prependAll(this.of(1, 2, 3));
        final Seq<Integer> expected = this.of(1, 2, 3, 4, 5, 6);
        assertThat(actual).isEqualTo(expected);
    }

    // -- set

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithNegativeIndexOnNil() {
        this.empty().set(-1, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithNegativeIndexOnNonNil() {
        this.of(1).set(-1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetOnNil() {
        this.empty().set(0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithIndexExceedingByOneOnNonNil() {
        this.of(1).set(1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithIndexExceedingByTwoOnNonNil() {
        this.of(1).set(2, 2);
    }

    @Test
    public void shouldSetFirstElement() {
        assertThat(this.of(1, 2, 3).set(0, 4)).isEqualTo(this.of(4, 2, 3));
    }

    @Test
    public void shouldSetLastElement() {
        assertThat(this.of(1, 2, 3).set(2, 4)).isEqualTo(this.of(1, 2, 4));
    }

    // -- sort()

    @Test
    public void shouldSortNil() {
        assertThat(this.empty().sort()).isEqualTo(this.empty());
    }

    @Test
    public void shouldSortNonNil() {
        assertThat(this.of(3, 4, 1, 2).sort()).isEqualTo(this.of(1, 2, 3, 4));
    }

    // -- sort(Comparator)

    @Test
    public void shouldSortNilUsingComparator() {
        assertThat(this.<Integer>empty().sort((i, j) -> j - i)).isEqualTo(this.empty());
    }

    @Test
    public void shouldSortNonNilUsingComparator() {
        assertThat(this.of(3, 4, 1, 2).sort((i, j) -> j - i)).isEqualTo(this.of(4, 3, 2, 1));
    }

    // -- splitAt(index)

    @Test
    public void shouldSplitAtNil() {
        assertThat(this.empty().splitAt(1)).isEqualTo(Tuple.of(this.empty(), this.empty()));
    }

    @Test
    public void shouldSplitAtNonNil() {
        assertThat(this.of(1, 2, 3).splitAt(1)).isEqualTo(Tuple.of(this.of(1), this.of(2, 3)));
    }

    // -- subsequence(beginIndex)

    @Test
    public void shouldReturnNilWhenSubsequenceFrom0OnNil() {
        final Seq<Integer> actual = this.<Integer>empty().subsequence(0);
        assertThat(actual).isEqualTo(this.empty());
    }

    @Test
    public void shouldReturnIdentityWhenSubsequenceFrom0OnNonNil() {
        final Seq<Integer> actual = this.of(1).subsequence(0);
        assertThat(actual).isEqualTo(this.of(1));
    }

    @Test
    public void shouldReturnNilWhenSubsequenceFrom1OnSeqOf1() {
        final Seq<Integer> actual = this.of(1).subsequence(1);
        assertThat(actual).isEqualTo(this.empty());
    }

    @Test
    public void shouldReturnSubsequenceWhenIndexIsWithinRange() {
        final Seq<Integer> actual = this.of(1, 2, 3).subsequence(1);
        assertThat(actual).isEqualTo(this.of(2, 3));
    }

    @Test
    public void shouldReturnNilWhenSubsequenceBeginningWithSize() {
        final Seq<Integer> actual = this.of(1, 2, 3).subsequence(3);
        assertThat(actual).isEqualTo(this.empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubsequence0OnNil() {
        this.<Integer>empty().subsequence(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubsequenceWithOutOfLowerBound() {
        this.of(1, 2, 3).subsequence(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubsequenceWithOutOfUpperBound() {
        this.of(1, 2, 3).subsequence(4);
    }

    // -- subsequence(beginIndex, endIndex)

    @Test
    public void shouldReturnNilWhenSubsequenceFrom0To0OnNil() {
        final Seq<Integer> actual = this.<Integer>empty().subsequence(0, 0);
        assertThat(actual).isEqualTo(this.empty());
    }

    @Test
    public void shouldReturnNilWhenSubsequenceFrom0To0OnNonNil() {
        final Seq<Integer> actual = this.of(1).subsequence(0, 0);
        assertThat(actual).isEqualTo(this.empty());
    }

    @Test
    public void shouldReturnSeqWithFirstElementWhenSubsequenceFrom0To1OnNonNil() {
        final Seq<Integer> actual = this.of(1).subsequence(0, 1);
        assertThat(actual).isEqualTo(this.of(1));
    }

    @Test
    public void shouldReturnNilWhenSubsequenceFrom1To1OnNonNil() {
        final Seq<Integer> actual = this.of(1).subsequence(1, 1);
        assertThat(actual).isEqualTo(this.empty());
    }

    @Test
    public void shouldReturnSubsequenceWhenIndicesAreWithinRange() {
        final Seq<Integer> actual = this.of(1, 2, 3).subsequence(1, 3);
        assertThat(actual).isEqualTo(this.of(2, 3));
    }

    @Test
    public void shouldReturnNilWhenIndicesBothAreUpperBound() {
        final Seq<Integer> actual = this.of(1, 2, 3).subsequence(3, 3);
        assertThat(actual).isEqualTo(this.empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubsequenceWhenEndIndexIsGreaterThanBeginIndex() {
        this.of(1, 2, 3).subsequence(1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubsequenceWhenBeginIndexExceedsLowerBound() {
        this.of(1, 2, 3).subsequence(-1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubsequenceWhenEndIndexExceedsUpperBound() {
        this.of(1, 2, 3).subsequence(1, 4).join(); // force computation of last element, e.g. because Stream is lazy
    }

    // -- static rangeClosed(int, int)

    @Test
    public void shouldCreateRangeWhereFromIsGreaterThanTo() {
        assertThat(rangeClosed(1, 0)).isEqualTo(empty());
        assertThat(rangeClosed(1L, 0L)).isEqualTo(empty());
    }

    @Test
    public void shouldCreateRangeWhereFromEqualsTo() {
        assertThat(rangeClosed(0, 0)).isEqualTo(of(0));
        assertThat(rangeClosed(0L, 0L)).isEqualTo(of(0L));
    }

    @Test
    public void shouldCreateRangeWhereFromIsLessThanTo() {
        assertThat(rangeClosed(1, 3)).isEqualTo(of(1, 2, 3));
        assertThat(rangeClosed(1L, 3L)).isEqualTo(of(1L, 2L, 3L));
    }

    @Test
    public void shouldCreateRangeWhereFromEqualsToEquals_MIN_VALUE() {
        assertThat(rangeClosed(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(of(Integer.MIN_VALUE));
        assertThat(rangeClosed(Long.MIN_VALUE, Long.MIN_VALUE)).isEqualTo(of(Long.MIN_VALUE));
    }

    @Test
    public void shouldCreateRangeWhereFromEqualsToEquals_MAX_VALUE() {
        assertThat(rangeClosed(Integer.MAX_VALUE, Integer.MAX_VALUE)).isEqualTo(of(Integer.MAX_VALUE));
        assertThat(rangeClosed(Long.MAX_VALUE, Long.MAX_VALUE)).isEqualTo(of(Long.MAX_VALUE));
    }

    // -- static rangeClosedBy(int, int, int), rangeClosedBy(long, long, long)

    @Test
    public void shouldCreateRangeByWhereFromIsGreaterThanTo() {
        assertThat(rangeClosedBy(1, 0, 1)).isEqualTo(empty());
        assertThat(rangeClosedBy(1, 0, 3)).isEqualTo(empty());
        assertThat(rangeClosedBy(0, 1, -1)).isEqualTo(empty());
        assertThat(rangeClosedBy(0, 1, -3)).isEqualTo(empty());
        assertThat(rangeClosedBy(1L, 0L, 1)).isEqualTo(empty());
        assertThat(rangeClosedBy(1L, 0L, 3)).isEqualTo(empty());
        assertThat(rangeClosedBy(0L, 1L, -1)).isEqualTo(empty());
        assertThat(rangeClosedBy(0L, 1L, -3)).isEqualTo(empty());
    }

    @Test
    public void shouldCreateRangeByWhereFromEqualsTo() {
        assertThat(rangeClosedBy(0, 0, 1)).isEqualTo(of(0));
        assertThat(rangeClosedBy(0, 0, 3)).isEqualTo(of(0));
        assertThat(rangeClosedBy(0, 0, -1)).isEqualTo(of(0));
        assertThat(rangeClosedBy(0, 0, -3)).isEqualTo(of(0));
        assertThat(rangeClosedBy(0L, 0L, 1)).isEqualTo(of(0L));
        assertThat(rangeClosedBy(0L, 0L, 3)).isEqualTo(of(0L));
        assertThat(rangeClosedBy(0L, 0L, -1)).isEqualTo(of(0L));
        assertThat(rangeClosedBy(0L, 0L, -3)).isEqualTo(of(0L));
    }

    @Test
    public void shouldCreateRangeByWhereFromIsLessThanTo() {
        assertThat(rangeClosedBy(1, 3, 1)).isEqualTo(of(1, 2, 3));
        assertThat(rangeClosedBy(1, 5, 2)).isEqualTo(of(1, 3, 5));
        assertThat(rangeClosedBy(1, 6, 2)).isEqualTo(of(1, 3, 5));
        assertThat(rangeClosedBy(Integer.MAX_VALUE - 2, Integer.MAX_VALUE, 3)).isEqualTo(of(Integer.MAX_VALUE - 2));
        assertThat(rangeClosedBy(Integer.MAX_VALUE - 3, Integer.MAX_VALUE, 3)).isEqualTo(of(Integer.MAX_VALUE - 3, Integer.MAX_VALUE));
        assertThat(rangeClosedBy(3, 1, -1)).isEqualTo(of(3, 2, 1));
        assertThat(rangeClosedBy(5, 1, -2)).isEqualTo(of(5, 3, 1));
        assertThat(rangeClosedBy(5, 0, -2)).isEqualTo(of(5, 3, 1));
        assertThat(rangeClosedBy(Integer.MIN_VALUE + 2, Integer.MIN_VALUE, -3)).isEqualTo(of(Integer.MIN_VALUE + 2));
        assertThat(rangeClosedBy(Integer.MIN_VALUE + 3, Integer.MIN_VALUE, -3)).isEqualTo(of(Integer.MIN_VALUE + 3, Integer.MIN_VALUE));
        assertThat(rangeClosedBy(1L, 3L, 1)).isEqualTo(of(1L, 2L, 3L));
        assertThat(rangeClosedBy(1L, 5L, 2)).isEqualTo(of(1L, 3L, 5L));
        assertThat(rangeClosedBy(1L, 6L, 2)).isEqualTo(of(1L, 3L, 5L));
        assertThat(rangeClosedBy(Long.MAX_VALUE - 2, Long.MAX_VALUE, 3)).isEqualTo(of(Long.MAX_VALUE - 2));
        assertThat(rangeClosedBy(Long.MAX_VALUE - 3, Long.MAX_VALUE, 3)).isEqualTo(of(Long.MAX_VALUE - 3, Long.MAX_VALUE));
        assertThat(rangeClosedBy(3L, 1L, -1)).isEqualTo(of(3L, 2L, 1L));
        assertThat(rangeClosedBy(5L, 1L, -2)).isEqualTo(of(5L, 3L, 1L));
        assertThat(rangeClosedBy(5L, 0L, -2)).isEqualTo(of(5L, 3L, 1L));
        assertThat(rangeClosedBy(Long.MIN_VALUE + 2, Long.MIN_VALUE, -3)).isEqualTo(of(Long.MIN_VALUE + 2));
        assertThat(rangeClosedBy(Long.MIN_VALUE + 3, Long.MIN_VALUE, -3)).isEqualTo(of(Long.MIN_VALUE + 3, Long.MIN_VALUE));
    }

    @Test
    public void shouldCreateRangeByWhereFromEqualsToEquals_MIN_VALUE() {
        assertThat(rangeClosedBy(Integer.MIN_VALUE, Integer.MIN_VALUE, 1)).isEqualTo(of(Integer.MIN_VALUE));
        assertThat(rangeClosedBy(Integer.MIN_VALUE, Integer.MIN_VALUE, 3)).isEqualTo(of(Integer.MIN_VALUE));
        assertThat(rangeClosedBy(Integer.MIN_VALUE, Integer.MIN_VALUE, -1)).isEqualTo(of(Integer.MIN_VALUE));
        assertThat(rangeClosedBy(Integer.MIN_VALUE, Integer.MIN_VALUE, -3)).isEqualTo(of(Integer.MIN_VALUE));
        assertThat(rangeClosedBy(Long.MIN_VALUE, Long.MIN_VALUE, 1)).isEqualTo(of(Long.MIN_VALUE));
        assertThat(rangeClosedBy(Long.MIN_VALUE, Long.MIN_VALUE, 3)).isEqualTo(of(Long.MIN_VALUE));
        assertThat(rangeClosedBy(Long.MIN_VALUE, Long.MIN_VALUE, -1)).isEqualTo(of(Long.MIN_VALUE));
        assertThat(rangeClosedBy(Long.MIN_VALUE, Long.MIN_VALUE, -3)).isEqualTo(of(Long.MIN_VALUE));
    }

    @Test
    public void shouldCreateRangeByWhereFromEqualsToEquals_MAX_VALUE() {
        assertThat(rangeClosedBy(Integer.MAX_VALUE, Integer.MAX_VALUE, 1)).isEqualTo(of(Integer.MAX_VALUE));
        assertThat(rangeClosedBy(Integer.MAX_VALUE, Integer.MAX_VALUE, 3)).isEqualTo(of(Integer.MAX_VALUE));
        assertThat(rangeClosedBy(Integer.MAX_VALUE, Integer.MAX_VALUE, -1)).isEqualTo(of(Integer.MAX_VALUE));
        assertThat(rangeClosedBy(Integer.MAX_VALUE, Integer.MAX_VALUE, -3)).isEqualTo(of(Integer.MAX_VALUE));
        assertThat(rangeClosedBy(Long.MAX_VALUE, Long.MAX_VALUE, 1)).isEqualTo(of(Long.MAX_VALUE));
        assertThat(rangeClosedBy(Long.MAX_VALUE, Long.MAX_VALUE, 3)).isEqualTo(of(Long.MAX_VALUE));
        assertThat(rangeClosedBy(Long.MAX_VALUE, Long.MAX_VALUE, -1)).isEqualTo(of(Long.MAX_VALUE));
        assertThat(rangeClosedBy(Long.MAX_VALUE, Long.MAX_VALUE, -3)).isEqualTo(of(Long.MAX_VALUE));
    }

    // -- static range(int, int), range(long, long)

    @Test
    public void shouldCreateStreamOfUntilWhereFromIsGreaterThanTo() {
        assertThat(range(1, 0)).isEqualTo(empty());
        assertThat(range(1L, 0L)).isEqualTo(empty());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsTo() {
        assertThat(range(0, 0)).isEqualTo(empty());
        assertThat(range(0L, 0L)).isEqualTo(empty());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromIsLessThanTo() {
        assertThat(range(1, 3)).isEqualTo(of(1, 2));
        assertThat(range(1L, 3L)).isEqualTo(of(1L, 2L));
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsToEquals_MIN_VALUE() {
        assertThat(range(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(empty());
        assertThat(range(Long.MIN_VALUE, Long.MIN_VALUE)).isEqualTo(empty());
    }

    @Test
    public void shouldCreateStreamOfUntilWhereFromEqualsToEquals_MAX_VALUE() {
        assertThat(range(Integer.MAX_VALUE, Integer.MAX_VALUE)).isEqualTo(empty());
        assertThat(range(Long.MAX_VALUE, Long.MAX_VALUE)).isEqualTo(empty());
    }

    // -- static rangeBy(int, int, int), rangeBy(long, long, long)

    @Test
    public void shouldCreateStreamOfUntilByWhereFromIsGreaterThanTo() {
        assertThat(rangeBy(1, 0, 1)).isEqualTo(empty());
        assertThat(rangeBy(1, 0, 3)).isEqualTo(empty());
        assertThat(rangeBy(0, 1, -1)).isEqualTo(empty());
        assertThat(rangeBy(0, 1, -3)).isEqualTo(empty());
        assertThat(rangeBy(1L, 0L, 1L)).isEqualTo(empty());
        assertThat(rangeBy(1L, 0L, 3L)).isEqualTo(empty());
        assertThat(rangeBy(0L, 1L, -1L)).isEqualTo(empty());
        assertThat(rangeBy(0L, 1L, -3L)).isEqualTo(empty());
    }

    @Test
    public void shouldCreateStreamOfUntilByWhereFromEqualsTo() {
        assertThat(rangeBy(0, 0, 1)).isEqualTo(empty());
        assertThat(rangeBy(0, 0, 3)).isEqualTo(empty());
        assertThat(rangeBy(0, 0, -1)).isEqualTo(empty());
        assertThat(rangeBy(0, 0, -3)).isEqualTo(empty());
        assertThat(rangeBy(0L, 0L, 1L)).isEqualTo(empty());
        assertThat(rangeBy(0L, 0L, 3L)).isEqualTo(empty());
        assertThat(rangeBy(0L, 0L, -1L)).isEqualTo(empty());
        assertThat(rangeBy(0L, 0L, -3L)).isEqualTo(empty());
    }

    @Test
    public void shouldCreateStreamOfUntilByWhereFromIsLessThanTo() {
        assertThat(rangeBy(1, 3, 1)).isEqualTo(of(1, 2));
        assertThat(rangeBy(1, 4, 2)).isEqualTo(of(1, 3));
        assertThat(rangeBy(3, 1, -1)).isEqualTo(of(3, 2));
        assertThat(rangeBy(4, 1, -2)).isEqualTo(of(4, 2));
        assertThat(rangeBy(1L, 3L, 1L)).isEqualTo(of(1L, 2L));
        assertThat(rangeBy(1L, 4L, 2L)).isEqualTo(of(1L, 3L));
        assertThat(rangeBy(3L, 1L, -1L)).isEqualTo(of(3L, 2L));
        assertThat(rangeBy(4L, 1L, -2L)).isEqualTo(of(4L, 2L));
    }

    @Test
    public void shouldCreateStreamOfUntilByWhereFromEqualsToEquals_MIN_VALUE() {
        assertThat(rangeBy(Integer.MIN_VALUE, Integer.MIN_VALUE, 1)).isEqualTo(empty());
        assertThat(rangeBy(Integer.MIN_VALUE, Integer.MIN_VALUE, 3)).isEqualTo(empty());
        assertThat(rangeBy(Integer.MIN_VALUE, Integer.MIN_VALUE, -1)).isEqualTo(empty());
        assertThat(rangeBy(Integer.MIN_VALUE, Integer.MIN_VALUE, -3)).isEqualTo(empty());
        assertThat(rangeBy(Long.MIN_VALUE, Long.MIN_VALUE, 1L)).isEqualTo(empty());
        assertThat(rangeBy(Long.MIN_VALUE, Long.MIN_VALUE, 3L)).isEqualTo(empty());
        assertThat(rangeBy(Long.MIN_VALUE, Long.MIN_VALUE, -1L)).isEqualTo(empty());
        assertThat(rangeBy(Long.MIN_VALUE, Long.MIN_VALUE, -3L)).isEqualTo(empty());
    }

    @Test
    public void shouldCreateStreamOfUntilByWhereFromEqualsToEquals_MAX_VALUE() {
        assertThat(rangeBy(Integer.MAX_VALUE, Integer.MAX_VALUE, 1)).isEqualTo(empty());
        assertThat(rangeBy(Integer.MAX_VALUE, Integer.MAX_VALUE, 3)).isEqualTo(empty());
        assertThat(rangeBy(Integer.MAX_VALUE, Integer.MAX_VALUE, -1)).isEqualTo(empty());
        assertThat(rangeBy(Integer.MAX_VALUE, Integer.MAX_VALUE, -3)).isEqualTo(empty());
        assertThat(rangeBy(Long.MAX_VALUE, Long.MAX_VALUE, 1L)).isEqualTo(empty());
        assertThat(rangeBy(Long.MAX_VALUE, Long.MAX_VALUE, 3L)).isEqualTo(empty());
        assertThat(rangeBy(Long.MAX_VALUE, Long.MAX_VALUE, -1L)).isEqualTo(empty());
        assertThat(rangeBy(Long.MAX_VALUE, Long.MAX_VALUE, -3L)).isEqualTo(empty());
    }
}
