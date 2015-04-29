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

    @Override
    abstract protected <T> Seq<T> nil();

    @SuppressWarnings("unchecked")
    @Override
    abstract protected <T> Seq<T> of(T... elements);

    // -- append

    @Test
    public void shouldAppendElementToNil() {
        final Seq<Integer> actual = this.<Integer>nil().append(1);
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
        final Seq<Integer> actual = this.<Integer>nil().appendAll(this.of(1, 2, 3));
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

    // -- apply

    @Test
    public void shouldUseSeqAsPartialFunction() {
        assertThat(of(1, 2, 3).apply(1)).isEqualTo(2);
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
    public void shouldNotFindIndexOfElementWhenSeqIsEmpty() {
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

    // -- insert

    @Test
    public void shouldInsertIntoNil() {
        final Seq<Integer> actual = this.<Integer>nil().insert(0, 1);
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
        this.nil().insert(-1, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnInsertWhenExceedingUpperBound() {
        this.nil().insert(1, null);
    }

    // -- insertAll

    @Test
    public void shouldInserAlltIntoNil() {
        final Seq<Integer> actual = this.<Integer>nil().insertAll(0, this.of(1, 2, 3));
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

    // -- iterator(int)

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenNilIteratorStartingAtIndex() {
        this.nil().iterator(1);
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
        assertThat(this.nil().lastIndexOf(1)).isEqualTo(-1);
    }

    @Test
    public void shouldFindLastIndexOfElement() {
        assertThat(this.of(1, 2, 3, 1, 2, 3).lastIndexOf(1)).isEqualTo(3);
    }

    // -- prepend

    @Test
    public void shouldPrependElementToNil() {
        final Seq<Integer> actual = this.<Integer>nil().prepend(1);
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
        this.nil().prependAll(null);
    }

    @Test
    public void shouldPrependAllNilToNil() {
        final Seq<Integer> actual = this.<Integer>nil().prependAll(this.nil());
        final Seq<Integer> expected = this.nil();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNilToNonNil() {
        final Seq<Integer> actual = this.of(1, 2, 3).prependAll(this.nil());
        final Seq<Integer> expected = this.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNonNilToNil() {
        final Seq<Integer> actual = this.<Integer>nil().prependAll(this.of(1, 2, 3));
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
        this.nil().set(-1, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithNegativeIndexOnNonNil() {
        this.of(1).set(-1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetOnNil() {
        this.nil().set(0, null);
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
        assertThat(this.nil().sort()).isEqualTo(this.nil());
    }

    @Test
    public void shouldSortNonNil() {
        assertThat(this.of(3, 4, 1, 2).sort()).isEqualTo(this.of(1, 2, 3, 4));
    }

    // -- sort(Comparator)

    @Test
    public void shouldSortNilUsingComparator() {
        assertThat(this.<Integer>nil().sort((i, j) -> j - i)).isEqualTo(this.nil());
    }

    @Test
    public void shouldSortNonNilUsingComparator() {
        assertThat(this.of(3, 4, 1, 2).sort((i, j) -> j - i)).isEqualTo(this.of(4, 3, 2, 1));
    }

    // -- splitAt(index)

    @Test
    public void shouldSplitAtNil() {
        assertThat(this.nil().splitAt(1)).isEqualTo(Tuple.of(this.nil(), this.nil()));
    }

    @Test
    public void shouldSplitAtNonNil() {
        assertThat(this.of(1, 2, 3).splitAt(1)).isEqualTo(Tuple.of(this.of(1), this.of(2, 3)));
    }

    // -- subsequence(beginIndex)

    @Test
    public void shouldReturnNilWhenSubsequenceFrom0OnNil() {
        final Seq<Integer> actual = this.<Integer>nil().subsequence(0);
        assertThat(actual).isEqualTo(this.nil());
    }

    @Test
    public void shouldReturnIdentityWhenSubsequenceFrom0OnNonNil() {
        final Seq<Integer> actual = this.of(1).subsequence(0);
        assertThat(actual).isEqualTo(this.of(1));
    }

    @Test
    public void shouldReturnNilWhenSubsequenceFrom1OnSeqOf1() {
        final Seq<Integer> actual = this.of(1).subsequence(1);
        assertThat(actual).isEqualTo(this.nil());
    }

    @Test
    public void shouldReturnSubsequenceWhenIndexIsWithinRange() {
        final Seq<Integer> actual = this.of(1, 2, 3).subsequence(1);
        assertThat(actual).isEqualTo(this.of(2, 3));
    }

    @Test
    public void shouldReturnNilWhenSubsequenceBeginningWithSize() {
        final Seq<Integer> actual = this.of(1, 2, 3).subsequence(3);
        assertThat(actual).isEqualTo(this.nil());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubsequence0OnNil() {
        this.<Integer>nil().subsequence(1);
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
        final Seq<Integer> actual = this.<Integer>nil().subsequence(0, 0);
        assertThat(actual).isEqualTo(this.nil());
    }

    @Test
    public void shouldReturnNilWhenSubsequenceFrom0To0OnNonNil() {
        final Seq<Integer> actual = this.of(1).subsequence(0, 0);
        assertThat(actual).isEqualTo(this.nil());
    }

    @Test
    public void shouldReturnSeqWithFirstElementWhenSubsequenceFrom0To1OnNonNil() {
        final Seq<Integer> actual = this.of(1).subsequence(0, 1);
        assertThat(actual).isEqualTo(this.of(1));
    }

    @Test
    public void shouldReturnNilWhenSubsequenceFrom1To1OnNonNil() {
        final Seq<Integer> actual = this.of(1).subsequence(1, 1);
        assertThat(actual).isEqualTo(this.nil());
    }

    @Test
    public void shouldReturnSubsequenceWhenIndicesAreWithinRange() {
        final Seq<Integer> actual = this.of(1, 2, 3).subsequence(1, 3);
        assertThat(actual).isEqualTo(this.of(2, 3));
    }

    @Test
    public void shouldReturnNilWhenIndicesBothAreUpperBound() {
        final Seq<Integer> actual = this.of(1, 2, 3).subsequence(3, 3);
        assertThat(actual).isEqualTo(this.nil());
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
}
