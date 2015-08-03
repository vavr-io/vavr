/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collector;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests all methods defined in {@link javaslang.collection.Seq}.
 */
public abstract class AbstractSeqTest extends AbstractTraversableTest {

    // -- construction

    @Override
    abstract protected <T> Collector<T, ArrayList<T>, ? extends Seq<T>> collector();

    @Override
    abstract protected <T> Seq<T> empty();

    abstract protected <T> Seq<T> of(T element);

    @SuppressWarnings("unchecked")
    @Override
    abstract protected <T> Seq<T> of(T... elements);

    @Override
    abstract protected <T> Seq<T> ofAll(Iterable<? extends T> elements);

    @Override
    abstract protected Seq<Boolean> ofAll(boolean[] array);

    @Override
    abstract protected Seq<Byte> ofAll(byte[] array);

    @Override
    abstract protected Seq<Character> ofAll(char[] array);

    @Override
    abstract protected Seq<Double> ofAll(double[] array);

    @Override
    abstract protected Seq<Float> ofAll(float[] array);

    @Override
    abstract protected Seq<Integer> ofAll(int[] array);

    @Override
    abstract protected Seq<Long> ofAll(long[] array);

    @Override
    abstract protected Seq<Short> ofAll(short[] array);

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
        final Seq<Integer> actual = this.<Integer> empty().append(1);
        final Seq<Integer> expected = of(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendElementToNonNil() {
        final Seq<Integer> actual = of(1, 2).append(3);
        final Seq<Integer> expected = of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    // -- appendAll

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnAppendAllOfNull() {
        empty().appendAll(null);
    }

    @Test
    public void shouldAppendAllNilToNil() {
        final Seq<Object> actual = empty().appendAll(empty());
        final Seq<Object> expected = empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNonNilToNil() {
        final Seq<Integer> actual = this.<Integer> empty().appendAll(of(1, 2, 3));
        final Seq<Integer> expected = of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNilToNonNil() {
        final Seq<Integer> actual = of(1, 2, 3).appendAll(empty());
        final Seq<Integer> expected = of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNonNilToNonNil() {
        final Seq<Integer> actual = of(1, 2, 3).appendAll(of(4, 5, 6));
        final Seq<Integer> expected = of(1, 2, 3, 4, 5, 6);
        assertThat(actual).isEqualTo(expected);
    }

    // -- containsSlice

    @Test
    public void shouldRecognizeNilNotContainsSlice() {
        final boolean actual = empty().containsSlice(of(1, 2, 3));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesContainSlice() {
        final boolean actual = of(1, 2, 3, 4, 5).containsSlice(of(2, 3));
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldRecognizeNonNilDoesNotContainSlice() {
        final boolean actual = of(1, 2, 3, 4, 5).containsSlice(of(2, 1, 4));
        assertThat(actual).isFalse();
    }

    // -- apply

    @Test
    public void shouldUseSeqAsPartialFunction() {
        assertThat(of(1, 2, 3).apply(1)).isEqualTo(2);
    }

    // -- get

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetWithNegativeIndexOnNil() {
        empty().get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetWithNegativeIndexOnNonNil() {
        of(1).get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetOnNil() {
        empty().get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetWithTooBigIndexOnNonNil() {
        of(1).get(1);
    }

    @Test
    public void shouldGetFirstElement() {
        assertThat(of(1, 2, 3).get(0)).isEqualTo(1);
    }

    @Test
    public void shouldGetLastElement() {
        assertThat(of(1, 2, 3).get(2)).isEqualTo(3);
    }

    // -- indexOf

    @Test
    public void shouldNotFindIndexOfElementWhenSeqIsEmpty() {
        assertThat(empty().indexOf(1)).isEqualTo(-1);
    }

    @Test
    public void shouldNotFindIndexOfElementWhenStartIsGreater() {
        assertThat(of(1, 2, 3, 4).indexOf(2, 2)).isEqualTo(-1);
    }

    @Test
    public void shouldFindIndexOfFirstElement() {
        assertThat(of(1, 2, 3).indexOf(1)).isEqualTo(0);
    }

    @Test
    public void shouldFindIndexOfInnerElement() {
        assertThat(of(1, 2, 3).indexOf(2)).isEqualTo(1);
    }

    @Test
    public void shouldFindIndexOfLastElement() {
        assertThat(of(1, 2, 3).indexOf(3)).isEqualTo(2);
    }

    // -- indexOfSlice

    @Test
    public void shouldNotFindIndexOfSliceWhenSeqIsEmpty() {
        assertThat(empty().indexOfSlice(of(2, 3))).isEqualTo(-1);
    }

    @Test
    public void shouldNotFindIndexOfSliceWhenStartIsGreater() {
        assertThat(of(1, 2, 3, 4).indexOfSlice(of(2, 3), 2)).isEqualTo(-1);
    }

    @Test
    public void shouldFindIndexOfFirstSlice() {
        assertThat(of(1, 2, 3, 4).indexOfSlice(of(1, 2))).isEqualTo(0);
    }

    @Test
    public void shouldFindIndexOfInnerSlice() {
        assertThat(of(1, 2, 3, 4).indexOfSlice(of(2, 3))).isEqualTo(1);
    }

    @Test
    public void shouldFindIndexOfLastSlice() {
        assertThat(of(1, 2, 3).indexOfSlice(of(2, 3))).isEqualTo(1);
    }

    // -- lastIndexOf

    @Test
    public void shouldNotFindLastIndexOfElementWhenSeqIsEmpty() {
        assertThat(empty().lastIndexOf(1)).isEqualTo(-1);
    }

    @Test
    public void shouldNotFindLastIndexOfElementWhenEndIdLess() {
        assertThat(of(1, 2, 3, 4).lastIndexOf(3, 1)).isEqualTo(-1);
    }

    @Test
    public void shouldFindLastIndexOfElement() {
        assertThat(of(1, 2, 3, 1, 2, 3).lastIndexOf(1)).isEqualTo(3);
    }

    @Test
    public void shouldFindLastIndexOfElementWithEnd() {
        assertThat(of(1, 2, 3, 1, 2, 3).lastIndexOf(1, 1)).isEqualTo(0);
    }

    // -- lastIndexOfSlice

    @Test
    public void shouldNotFindLastIndexOfSliceWhenSeqIsEmpty() {
        assertThat(empty().lastIndexOfSlice(of(2, 3))).isEqualTo(-1);
    }

    @Test
    public void shouldNotFindLastIndexOfSliceWhenEndIdLess() {
        assertThat(of(1, 2, 3, 4, 5).lastIndexOfSlice(of(3, 4), 1)).isEqualTo(-1);
    }

    @Test
    public void shouldFindLastIndexOfSlice() {
        assertThat(of(1, 2, 3, 1, 2).lastIndexOfSlice(empty())).isEqualTo(5);
        assertThat(of(1, 2, 3, 1, 2).lastIndexOfSlice(of(2))).isEqualTo(4);
        assertThat(of(1, 2, 3, 1, 2, 3, 4).lastIndexOfSlice(of(2, 3))).isEqualTo(4);
    }

    @Test
    public void shouldFindLastIndexOfSliceWithEnd() {
        assertThat(of(1, 2, 3, 1, 2, 3).lastIndexOfSlice(empty(), 2)).isEqualTo(2);
        assertThat(of(1, 2, 3, 1, 2, 3).lastIndexOfSlice(of(2), 2)).isEqualTo(1);
        assertThat(of(1, 2, 3, 1, 2, 3).lastIndexOfSlice(of(2, 3), 2)).isEqualTo(1);
        assertThat(of(1, 2, 3, 1, 2, 3, 4).lastIndexOfSlice(of(2, 3), 2)).isEqualTo(1);
    }

    // -- insert

    @Test
    public void shouldInsertIntoNil() {
        final Seq<Integer> actual = this.<Integer> empty().insert(0, 1);
        final Seq<Integer> expected = of(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertInFrontOfElement() {
        final Seq<Integer> actual = of(4).insert(0, 1);
        final Seq<Integer> expected = of(1, 4);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertBehindOfElement() {
        final Seq<Integer> actual = of(4).insert(1, 1);
        final Seq<Integer> expected = of(4, 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertIntoSeq() {
        final Seq<Integer> actual = of(1, 2, 3).insert(2, 4);
        final Seq<Integer> expected = of(1, 2, 4, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenInsertOnNonNilWithNegativeIndex() {
        of(1).insert(-1, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenInsertOnNilWithNegativeIndex() {
        empty().insert(-1, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnInsertWhenExceedingUpperBound() {
        empty().insert(1, null);
    }

    // -- insertAll

    @Test
    public void shouldInserAlltIntoNil() {
        final Seq<Integer> actual = this.<Integer> empty().insertAll(0, of(1, 2, 3));
        final Seq<Integer> expected = of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllInFrontOfElement() {
        final Seq<Integer> actual = of(4).insertAll(0, of(1, 2, 3));
        final Seq<Integer> expected = of(1, 2, 3, 4);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllBehindOfElement() {
        final Seq<Integer> actual = of(4).insertAll(1, of(1, 2, 3));
        final Seq<Integer> expected = of(4, 1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllIntoSeq() {
        final Seq<Integer> actual = of(1, 2, 3).insertAll(2, of(4, 5));
        final Seq<Integer> expected = of(1, 2, 4, 5, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnInsertAllWithNil() {
        empty().insertAll(0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenInsertOnNonNilAllWithNegativeIndex() {
        of(1).insertAll(-1, empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenInsertOnNilAllWithNegativeIndex() {
        empty().insertAll(-1, empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnInsertAllWhenExceedingUpperBound() {
        empty().insertAll(1, empty());
    }

    // -- iterator(int)

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenNilIteratorStartingAtIndex() {
        empty().iterator(1);
    }

    @Test
    public void shouldIterateFirstElementOfNonNilStartingAtIndex() {
        assertThat(of(1, 2, 3).iterator(1).next()).isEqualTo(2);
    }

    @Test
    public void shouldFullyIterateNonNilStartingAtIndex() {
        int actual = -1;
        for (Iterator<Integer> iter = of(1, 2, 3).iterator(1); iter.hasNext(); ) {
            actual = iter.next();
        }
        assertThat(actual).isEqualTo(3);
    }

    // -- prepend

    @Test
    public void shouldPrependElementToNil() {
        final Seq<Integer> actual = this.<Integer> empty().prepend(1);
        final Seq<Integer> expected = of(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependElementToNonNil() {
        final Seq<Integer> actual = of(2, 3).prepend(1);
        final Seq<Integer> expected = of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    // -- prependAll

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnPrependAllOfNull() {
        empty().prependAll(null);
    }

    @Test
    public void shouldPrependAllNilToNil() {
        final Seq<Integer> actual = this.<Integer> empty().prependAll(empty());
        final Seq<Integer> expected = empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNilToNonNil() {
        final Seq<Integer> actual = of(1, 2, 3).prependAll(empty());
        final Seq<Integer> expected = of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNonNilToNil() {
        final Seq<Integer> actual = this.<Integer> empty().prependAll(of(1, 2, 3));
        final Seq<Integer> expected = of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNonNilToNonNil() {
        final Seq<Integer> actual = of(4, 5, 6).prependAll(of(1, 2, 3));
        final Seq<Integer> expected = of(1, 2, 3, 4, 5, 6);
        assertThat(actual).isEqualTo(expected);
    }

    // -- set

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithNegativeIndexOnNil() {
        empty().set(-1, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithNegativeIndexOnNonNil() {
        of(1).set(-1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetOnNil() {
        empty().set(0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithIndexExceedingByOneOnNonNil() {
        of(1).set(1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithIndexExceedingByTwoOnNonNil() {
        of(1).set(2, 2);
    }

    @Test
    public void shouldSetFirstElement() {
        assertThat(of(1, 2, 3).set(0, 4)).isEqualTo(of(4, 2, 3));
    }

    @Test
    public void shouldSetLastElement() {
        assertThat(of(1, 2, 3).set(2, 4)).isEqualTo(of(1, 2, 4));
    }

    // -- sort()

    @Test
    public void shouldSortNil() {
        assertThat(empty().sort()).isEqualTo(empty());
    }

    @Test
    public void shouldSortNonNil() {
        assertThat(of(3, 4, 1, 2).sort()).isEqualTo(of(1, 2, 3, 4));
    }

    // -- sort(Comparator)

    @Test
    public void shouldSortNilUsingComparator() {
        assertThat(this.<Integer> empty().sort((i, j) -> j - i)).isEqualTo(empty());
    }

    @Test
    public void shouldSortNonNilUsingComparator() {
        assertThat(of(3, 4, 1, 2).sort((i, j) -> j - i)).isEqualTo(of(4, 3, 2, 1));
    }

    // -- splitAt(index)

    @Test
    public void shouldSplitAtNil() {
        assertThat(empty().splitAt(1)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSplitAtNonNil() {
        assertThat(of(1, 2, 3).splitAt(1)).isEqualTo(Tuple.of(of(1), of(2, 3)));
    }

    // -- subsequence(beginIndex)

    @Test
    public void shouldReturnNilWhenSubsequenceFrom0OnNil() {
        final Seq<Integer> actual = this.<Integer> empty().subsequence(0);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnIdentityWhenSubsequenceFrom0OnNonNil() {
        final Seq<Integer> actual = of(1).subsequence(0);
        assertThat(actual).isEqualTo(of(1));
    }

    @Test
    public void shouldReturnNilWhenSubsequenceFrom1OnSeqOf1() {
        final Seq<Integer> actual = of(1).subsequence(1);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnSubsequenceWhenIndexIsWithinRange() {
        final Seq<Integer> actual = of(1, 2, 3).subsequence(1);
        assertThat(actual).isEqualTo(of(2, 3));
    }

    @Test
    public void shouldReturnNilWhenSubsequenceBeginningWithSize() {
        final Seq<Integer> actual = of(1, 2, 3).subsequence(3);
        assertThat(actual).isEqualTo(empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubsequenceOnNil() {
        empty().subsequence(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubsequenceWithOutOfLowerBound() {
        of(1, 2, 3).subsequence(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubsequenceWithOutOfUpperBound() {
        of(1, 2, 3).subsequence(4);
    }

    // -- subsequence(beginIndex, endIndex)

    @Test
    public void shouldReturnNilWhenSubsequenceFrom0To0OnNil() {
        final Seq<Integer> actual = this.<Integer> empty().subsequence(0, 0);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnNilWhenSubsequenceFrom0To0OnNonNil() {
        final Seq<Integer> actual = of(1).subsequence(0, 0);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnSeqWithFirstElementWhenSubsequenceFrom0To1OnNonNil() {
        final Seq<Integer> actual = of(1).subsequence(0, 1);
        assertThat(actual).isEqualTo(of(1));
    }

    @Test
    public void shouldReturnNilWhenSubsequenceFrom1To1OnNonNil() {
        final Seq<Integer> actual = of(1).subsequence(1, 1);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnSubsequenceWhenIndicesAreWithinRange() {
        final Seq<Integer> actual = of(1, 2, 3).subsequence(1, 3);
        assertThat(actual).isEqualTo(of(2, 3));
    }

    @Test
    public void shouldReturnNilWhenIndicesBothAreUpperBound() {
        final Seq<Integer> actual = of(1, 2, 3).subsequence(3, 3);
        assertThat(actual).isEqualTo(empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubsequenceOnNonNilWhenBeginIndexIsGreaterThanEndIndex() {
        of(1, 2, 3).subsequence(1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubsequenceOnNilWhenBeginIndexIsGreaterThanEndIndex() {
        empty().subsequence(1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubsequenceOnNonNilWhenBeginIndexExceedsLowerBound() {
        of(1, 2, 3).subsequence(-1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubsequenceOnNilWhenBeginIndexExceedsLowerBound() {
        empty().subsequence(-1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubsequence2OnNil() {
        empty().subsequence(0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubsequenceWhenEndIndexExceedsUpperBound() {
        of(1, 2, 3).subsequence(1, 4).join(); // force computation of last element, e.g. because Stream is lazy
    }

    // -- static collector()

    @Test
    public void shouldStreamAndCollectNil() {
        final Seq<?> actual = java.util.stream.Stream.empty().collect(this.<Object> collector());
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldStreamAndCollectNonNil() {
        final Seq<?> actual = java.util.stream.Stream.of(1, 2, 3).collect(this.<Object> collector());
        assertThat(actual).isEqualTo(of(1, 2, 3));
    }

    @Test
    public void shouldParallelStreamAndCollectNil() {
        final Seq<?> actual = java.util.stream.Stream.empty().parallel().collect(this.<Object> collector());
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldParallelStreamAndCollectNonNil() {
        final Seq<?> actual = java.util.stream.Stream.of(1, 2, 3).parallel().collect(this.<Object> collector());
        assertThat(actual).isEqualTo(of(1, 2, 3));
    }

    // -- static empty()

    @Test
    public void shouldCreateNil() {
        final Seq<?> actual = empty();
        assertThat(actual.length()).isEqualTo(0);
    }

    // -- static of()

    @Test
    public void shouldCreateSeqOfSeqUsingCons() {
        final Seq<List<?>> actual = of(List.empty());
        assertThat(actual.length()).isEqualTo(1);
        assertThat(actual.contains(List.empty())).isTrue();
    }

    // -- static of(T...)

    @Test
    public void shouldCreateSeqOfElements() {
        final Seq<Integer> actual = of(1, 2);
        assertThat(actual.length()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualTo(1);
        assertThat(actual.get(1)).isEqualTo(2);
    }

    // -- static ofAll(Iterable)

    @Test
    public void shouldCreateListOfIterable() {
        final java.util.List<Integer> arrayList = Arrays.asList(1, 2);
        final Seq<Integer> actual = ofAll(arrayList);
        assertThat(actual.length()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualTo(1);
        assertThat(actual.get(1)).isEqualTo(2);
    }

    // -- static ofAll(<primitive array>)

    @Test
    public void shouldCreateListOfPrimitiveBooleanArray() {
        final Seq<Boolean> actual = ofAll(new boolean[] { true, false });
        final Seq<Boolean> expected = of(true, false);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateListOfPrimitiveByteArray() {
        final Seq<Byte> actual = ofAll(new byte[] { 1, 2, 3 });
        final Seq<Byte> expected = of((byte) 1, (byte) 2, (byte) 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateListOfPrimitiveCharArray() {
        final Seq<Character> actual = ofAll(new char[] { 'a', 'b', 'c' });
        final Seq<Character> expected = of('a', 'b', 'c');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateListOfPrimitiveDoubleArray() {
        final Seq<Double> actual = ofAll(new double[] { 1d, 2d, 3d });
        final Seq<Double> expected = of(1d, 2d, 3d);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateListOfPrimitiveFloatArray() {
        final Seq<Float> actual = ofAll(new float[] { 1f, 2f, 3f });
        final Seq<Float> expected = of(1f, 2f, 3f);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateListOfPrimitiveIntArray() {
        final Seq<Integer> actual = ofAll(new int[] { 1, 2, 3 });
        final Seq<Integer> expected = of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateListOfPrimitiveLongArray() {
        final Seq<Long> actual = ofAll(new long[] { 1L, 2L, 3L });
        final Seq<Long> expected = of(1L, 2L, 3L);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateListOfPrimitiveShortArray() {
        final Seq<Short> actual = ofAll(new short[] { (short) 1, (short) 2, (short) 3 });
        final Seq<Short> expected = of((short) 1, (short) 2, (short) 3);
        assertThat(actual).isEqualTo(expected);
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

    // -- static rangeBy and rangeClosedBy: step = 0

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitRangeByIntStepZero() {
        rangeBy(0, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitRangeByLongStepZero() {
        rangeBy(0L, 0L, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitRangeClosedByIntStepZero() {
        rangeClosedBy(0, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitRangeClosedByLongStepZero() {
        rangeClosedBy(0L, 0L, 0L);
    }
}
