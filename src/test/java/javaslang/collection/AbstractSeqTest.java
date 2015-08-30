/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collector;

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
    abstract protected <T> Seq<T> ofAll(java.lang.Iterable<? extends T> elements);

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

    // -- apply

    @Test
    public void shouldUseSeqAsPartialFunction() {
        assertThat(of(1, 2, 3).apply(1)).isEqualTo(2);
    }

    // -- combinations

    @Test
    public void shouldComputeCombinationsOfEmptyList() {
        assertThat(empty().combinations()).isEqualTo(of(empty()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldComputeCombinationsOfNonEmptyList() {
        assertThat(of(1, 2, 3).combinations()).isEqualTo(of(empty(), of(1), of(2), of(3), of(1, 2), of(1, 3), of(2, 3), of(1, 2, 3)));
    }

    // -- combinations(k)

    @Test
    public void shouldComputeKCombinationsOfEmptyList() {
        assertThat(empty().combinations(1)).isEqualTo(empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldComputeKCombinationsOfNonEmptyList() {
        assertThat(of(1, 2, 3).combinations(2)).isEqualTo(of(of(1, 2), of(1, 3), of(2, 3)));
    }

    @Test
    public void shouldComputeKCombinationsOfNegativeK() {
        assertThat(of(1).combinations(-1)).isEqualTo(of(empty()));
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

    // -- crossProduct()

    @Test
    public void shouldCalculateCrossProductOfNil() {
        final Traversable<Tuple2<Object, Object>> actual = empty().crossProduct();
        assertThat(actual).isEqualTo(empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCalculateCrossProductOfNonNil() {
        final Traversable<Tuple2<Integer, Integer>> actual = of(1, 2, 3).crossProduct();
        final Traversable<Tuple2<Integer, Integer>> expected = of(
                Tuple.of(1, 1), Tuple.of(1, 2), Tuple.of(1, 3),
                Tuple.of(2, 1), Tuple.of(2, 2), Tuple.of(2, 3),
                Tuple.of(3, 1), Tuple.of(3, 2), Tuple.of(3, 3));
        assertThat(actual).isEqualTo(expected);
    }

    // -- crossProduct(Iterable)

    @Test
    public void shouldCalculateCrossProductOfNilAndNil() {
        final Traversable<Tuple2<Object, Object>> actual = empty().crossProduct(empty());
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldCalculateCrossProductOfNilAndNonNil() {
        final Traversable<Tuple2<Object, Object>> actual = empty().crossProduct(of(1, 2, 3));
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldCalculateCrossProductOfNonNilAndNil() {
        final Traversable<Tuple2<Integer, Integer>> actual = of(1, 2, 3).crossProduct(empty());
        assertThat(actual).isEqualTo(empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCalculateCrossProductOfNonNilAndNonNil() {
        final Traversable<Tuple2<Integer, Character>> actual = of(1, 2, 3).crossProduct(of('a', 'b'));
        final Traversable<Tuple2<Integer, Character>> expected = of(
                Tuple.of(1, 'a'), Tuple.of(1, 'b'),
                Tuple.of(2, 'a'), Tuple.of(2, 'b'),
                Tuple.of(3, 'a'), Tuple.of(3, 'b'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenCalculatingCrossProductAndThatIsNull() {
        empty().crossProduct(null);
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

    // -- grouped

    @Test
    public void shouldGroupedNil() {
        assertThat(empty().grouped(1).isEmpty()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenGroupedWithSizeZero() {
        empty().grouped(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenGroupedWithNegativeSize() {
        empty().grouped(-1);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldGroupedTraversableWithEqualSizedBlocks() {
        assertThat(of(1, 2, 3, 4).grouped(2).toList()).isEqualTo(List.of(Vector.of(1, 2), Vector.of(3, 4)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldGroupedTraversableWithRemainder() {
        assertThat(of(1, 2, 3, 4, 5).grouped(2).toList()).isEqualTo(List.of(Vector.of(1, 2), Vector.of(3, 4), Vector.of(5)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldGroupedWhenTraversableLengthIsSmallerThanBlockSize() {
        assertThat(of(1, 2, 3, 4).grouped(5).toList()).isEqualTo(List.of(Vector.of(1, 2, 3, 4)));
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


    // -- intersperse

    @Test
    public void shouldIntersperseNil() {
        assertThat(this.<Character> empty().intersperse(',')).isEqualTo(empty());
    }

    @Test
    public void shouldIntersperseSingleton() {
        assertThat(of('a').intersperse(',')).isEqualTo(of('a'));
    }

    @Test
    public void shouldIntersperseMultipleElements() {
        assertThat(of('a', 'b').intersperse(',')).isEqualTo(of('a', ',', 'b'));
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

    // -- padTo

    @Test
    public void shouldPadEmptyToEmpty() {
        assertThat(empty().padTo(0, 1)).isSameAs(empty());
    }

    @Test
    public void shouldPadEmptyToNonEmpty() {
        assertThat(empty().padTo(2, 1)).isEqualTo(of(1, 1));
    }

    @Test
    public void shouldPadNonEmptyZeroLen() {
        Seq<Integer> seq = of(1);
        assertThat(seq.padTo(0, 2)).isSameAs(seq);
    }

    @Test
    public void shouldPadNonEmpty() {
        assertThat(of(1).padTo(2, 1)).isEqualTo(of(1, 1));
        assertThat(of(1).padTo(2, 2)).isEqualTo(of(1, 2));
        assertThat(of(1).padTo(3, 2)).isEqualTo(of(1, 2, 2));
    }

    // -- permutations

    @Test
    public void shouldComputePermutationsOfEmptyList() {
        assertThat(empty().permutations()).isEqualTo(empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldComputePermutationsOfNonEmptyList() {
        assertThat(of(1, 2, 3).permutations()).isEqualTo(ofAll(of(of(1, 2, 3), of(1, 3, 2), of(2, 1, 3), of(2, 3, 1), of(3, 1, 2), of(3, 2, 1))));
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

    // -- remove

    @Test
    public void shouldRemoveElementFromNil() {
        assertThat(empty().remove(null)).isEqualTo(empty());
    }

    @Test
    public void shouldRemoveFirstElement() {
        assertThat(of(1, 2, 3).remove(1)).isEqualTo(of(2, 3));
    }

    @Test
    public void shouldRemoveLastElement() {
        assertThat(of(1, 2, 3).remove(3)).isEqualTo(of(1, 2));
    }

    @Test
    public void shouldRemoveInnerElement() {
        assertThat(of(1, 2, 3).remove(2)).isEqualTo(of(1, 3));
    }

    @Test
    public void shouldRemoveNonExistingElement() {
        final Seq<Integer> t = of(1, 2, 3);
        if (isThisLazyCollection()) {
            assertThat(t.remove(4)).isEqualTo(t);
        } else {
            assertThat(t.remove(4)).isSameAs(t);
        }
    }

    // -- removeFirst(Predicate)

    @Test
    public void shouldRemoveFirstElementByPredicateFromNil() {
        assertThat(empty().removeFirst(v -> true)).isEqualTo(empty());
    }

    @Test
    public void shouldRemoveFirstElementByPredicateBegin() {
        assertThat(of(1, 2, 3).removeFirst(v -> v == 1)).isEqualTo(of(2, 3));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateBeginM() {
        assertThat(of(1, 2, 1, 3).removeFirst(v -> v == 1)).isEqualTo(of(2, 1, 3));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateEnd() {
        assertThat(of(1, 2, 3).removeFirst(v -> v == 3)).isEqualTo(of(1, 2));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateInner() {
        assertThat(of(1, 2, 3, 4, 5).removeFirst(v -> v == 3)).isEqualTo(of(1, 2, 4, 5));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateInnerM() {
        assertThat(of(1, 2, 3, 2, 5).removeFirst(v -> v == 2)).isEqualTo(of(1, 3, 2, 5));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateNonExisting() {
        final Seq<Integer> t = of(1, 2, 3);
        if (isThisLazyCollection()) {
            assertThat(t.removeFirst(v -> v == 4)).isEqualTo(t);
        } else {
            assertThat(t.removeFirst(v -> v == 4)).isSameAs(t);
        }
    }

    // -- removeLast(Predicate)

    @Test
    public void shouldRemoveLastElementByPredicateFromNil() {
        assertThat(empty().removeLast(v -> true)).isEqualTo(empty());
    }

    @Test
    public void shouldRemoveLastElementByPredicateBegin() {
        assertThat(of(1, 2, 3).removeLast(v -> v == 1)).isEqualTo(of(2, 3));
    }

    @Test
    public void shouldRemoveLastElementByPredicateEnd() {
        assertThat(of(1, 2, 3).removeLast(v -> v == 3)).isEqualTo(of(1, 2));
    }

    @Test
    public void shouldRemoveLastElementByPredicateEndM() {
        assertThat(of(1, 3, 2, 3).removeLast(v -> v == 3)).isEqualTo(of(1, 3, 2));
    }

    @Test
    public void shouldRemoveLastElementByPredicateInner() {
        assertThat(of(1, 2, 3, 4, 5).removeLast(v -> v == 3)).isEqualTo(of(1, 2, 4, 5));
    }

    @Test
    public void shouldRemoveLastElementByPredicateInnerM() {
        assertThat(of(1, 2, 3, 2, 5).removeLast(v -> v == 2)).isEqualTo(of(1, 2, 3, 5));
    }

    @Test
    public void shouldRemoveLastElementByPredicateNonExisting() {
        final Seq<Integer> t = of(1, 2, 3);
        assertThat(t.removeLast(v -> v == 4)).isSameAs(t);
    }

    // -- removeAll(Iterable)

    @Test
    public void shouldRemoveAllElementsFromNil() {
        assertThat(empty().removeAll(of(1, 2, 3))).isEqualTo(empty());
    }

    @Test
    public void shouldRemoveAllExistingElementsFromNonNil() {
        assertThat(of(1, 2, 3, 1, 2, 3).removeAll(of(1, 2))).isEqualTo(of(3, 3));
    }

    @Test
    public void shouldNotRemoveAllNonExistingElementsFromNonNil() {
        final Seq<Integer> t = of(1, 2, 3);
        if (isThisLazyCollection()) {
            assertThat(t.removeAll(of(4, 5))).isEqualTo(t);
        } else {
            assertThat(t.removeAll(of(4, 5))).isSameAs(t);
        }
    }

    // -- removeAll(Object)

    @Test
    public void shouldRemoveAllObjectsFromNil() {
        assertThat(empty().removeAll(1)).isEqualTo(empty());
    }

    @Test
    public void shouldRemoveAllExistingObjectsFromNonNil() {
        assertThat(of(1, 2, 3, 1, 2, 3).removeAll(1)).isEqualTo(of(2, 3, 2, 3));
    }

    @Test
    public void shouldNotRemoveAllNonObjectsElementsFromNonNil() {
        final Seq<Integer> t = of(1, 2, 3);
        if (isThisLazyCollection()) {
            assertThat(t.removeAll(4)).isEqualTo(t);
        } else {
            assertThat(t.removeAll(4)).isSameAs(t);
        }
    }

    // -- reverse

    @Test
    public void shouldReverseNil() {
        assertThat(empty().reverse()).isEqualTo(empty());
    }

    @Test
    public void shouldReverseNonNil() {
        assertThat(of(1, 2, 3).reverse()).isEqualTo(of(3, 2, 1));
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

    @Test
    public void shouldSplitAtBegin() {
        assertThat(of(1, 2, 3).splitAt(0)).isEqualTo(Tuple.of(empty(), of(1, 2, 3)));
    }

    @Test
    public void shouldSplitAtEnd() {
        assertThat(of(1, 2, 3).splitAt(3)).isEqualTo(Tuple.of(of(1, 2, 3), empty()));
    }

    @Test
    public void shouldSplitAtOutOfBounds() {
        assertThat(of(1, 2, 3).splitAt(5)).isEqualTo(Tuple.of(of(1, 2, 3), empty()));
        assertThat(of(1, 2, 3).splitAt(-1)).isEqualTo(Tuple.of(empty(), of(1, 2, 3)));
    }

    // -- splitAt(predicate)

    @Test
    public void shouldSplitPredicateAtNil() {
        assertThat(empty().splitAt(e -> true)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSplitPredicateAtNonNil() {
        assertThat(of(1, 2, 3).splitAt(e -> e == 2)).isEqualTo(Tuple.of(of(1), of(2, 3)));
    }

    @Test
    public void shouldSplitAtPredicateBegin() {
        assertThat(of(1, 2, 3).splitAt(e -> e == 1)).isEqualTo(Tuple.of(empty(), of(1, 2, 3)));
    }

    @Test
    public void shouldSplitAtPredicateEnd() {
        assertThat(of(1, 2, 3).splitAt(e -> e == 3)).isEqualTo(Tuple.of(of(1, 2), of(3)));
    }

    @Test
    public void shouldSplitAtPredicateNotFound() {
        assertThat(of(1, 2, 3).splitAt(e -> e == 5)).isEqualTo(Tuple.of(of(1, 2, 3), empty()));
    }

    // -- splitAtInclusive(predicate)

    @Test
    public void shouldSplitInclusivePredicateAtNil() {
        assertThat(empty().splitAtInclusive(e -> true)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSplitInclusivePredicateAtNonNil() {
        assertThat(of(1, 2, 3).splitAtInclusive(e -> e == 2)).isEqualTo(Tuple.of(of(1, 2), of(3)));
    }

    @Test
    public void shouldSplitAtInclusivePredicateBegin() {
        assertThat(of(1, 2, 3).splitAtInclusive(e -> e == 1)).isEqualTo(Tuple.of(of(1), of(2, 3)));
    }

    @Test
    public void shouldSplitAtInclusivePredicateEnd() {
        assertThat(of(1, 2, 3).splitAtInclusive(e -> e == 3)).isEqualTo(Tuple.of(of(1, 2, 3), empty()));
    }

    @Test
    public void shouldSplitAtInclusivePredicateNotFound() {
        assertThat(of(1, 2, 3).splitAtInclusive(e -> e == 5)).isEqualTo(Tuple.of(of(1, 2, 3), empty()));
    }

    // -- startsWith

    @Test
    public void shouldStartsNilOfNilCalculate() {
        assertThat(empty().startsWith(empty())).isTrue();
    }

    @Test
    public void shouldStartsNilOfNonNilCalculate() {
        assertThat(empty().startsWith(of(1))).isFalse();
    }

    @Test
    public void shouldStartsNilOfNilWithOffsetCalculate() {
        assertThat(empty().startsWith(empty(), 1)).isFalse();
    }

    @Test
    public void shouldStartsNilOfNonNilWithOffsetCalculate() {
        assertThat(empty().startsWith(of(1), 1)).isFalse();
    }

    @Test
    public void shouldStartsNonNilOfNilCalculate() {
        assertThat(of(1, 2, 3).startsWith(empty())).isTrue();
    }

    @Test
    public void shouldStartsNonNilOfNonNilCalculate() {
        assertThat(of(1, 2, 3).startsWith(of(1, 2))).isTrue();
        assertThat(of(1, 2, 3).startsWith(of(1, 2, 3))).isTrue();
        assertThat(of(1, 2, 3).startsWith(of(1, 2, 3, 4))).isFalse();
        assertThat(of(1, 2, 3).startsWith(of(1, 3))).isFalse();
    }

    @Test
    public void shouldStartsNonNilOfNilWithOffsetCalculate() {
        assertThat(of(1, 2, 3).startsWith(empty(), 1)).isTrue();
    }

    @Test
    public void shouldStartsNonNilOfNonNilWithOffsetCalculate() {
        assertThat(of(1, 2, 3).startsWith(of(2, 3), 1)).isTrue();
        assertThat(of(1, 2, 3).startsWith(of(2, 3, 4), 1)).isFalse();
        assertThat(of(1, 2, 3).startsWith(of(2, 4), 1)).isFalse();
    }

    // -- removeAt(index)

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldRemoveIndxAtNil() {
        assertThat(empty().removeAt(1)).isEqualTo(empty());
    }

    @Test
    public void shouldRemoveIndxAtNonNil() {
        assertThat(of(1, 2, 3).removeAt(1)).isEqualTo(of(1, 3));
    }

    @Test
    public void shouldRemoveIndxAtBegin() {
        assertThat(of(1, 2, 3).removeAt(0)).isEqualTo(of(2, 3));
    }

    @Test
    public void shouldRemoveIndxAtEnd() {
        assertThat(of(1, 2, 3).removeAt(2)).isEqualTo(of(1, 2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldRemoveIndxOutOfBoundsLeft() {
        assertThat(of(1, 2, 3).removeAt(-1)).isEqualTo(of(1, 2, 3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldRemoveIndxOutOfBoundsRight() {
        assertThat(of(1, 2, 3).removeAt(5)).isEqualTo(of(1, 2, 3));
    }

    // -- slice(beginIndex)

    @Test
    public void shouldReturnNilWhenSliceFrom0OnNil() {
        final Seq<Integer> actual = this.<Integer> empty().slice(0);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnIdentityWhenSliceFrom0OnNonNil() {
        final Seq<Integer> actual = of(1).slice(0);
        assertThat(actual).isEqualTo(of(1));
    }

    @Test
    public void shouldReturnNilWhenSliceFrom1OnSeqOf1() {
        final Seq<Integer> actual = of(1).slice(1);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnSliceWhenIndexIsWithinRange() {
        final Seq<Integer> actual = of(1, 2, 3).slice(1);
        assertThat(actual).isEqualTo(of(2, 3));
    }

    @Test
    public void shouldReturnNilWhenSliceBeginningWithSize() {
        final Seq<Integer> actual = of(1, 2, 3).slice(3);
        assertThat(actual).isEqualTo(empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSliceOnNil() {
        empty().slice(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSliceWithOutOfLowerBound() {
        of(1, 2, 3).slice(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSliceWithOutOfUpperBound() {
        of(1, 2, 3).slice(4);
    }

    // -- slice(beginIndex, endIndex)

    @Test
    public void shouldReturnNilWhenSliceFrom0To0OnNil() {
        final Seq<Integer> actual = this.<Integer> empty().slice(0, 0);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnNilWhenSliceFrom0To0OnNonNil() {
        final Seq<Integer> actual = of(1).slice(0, 0);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnSeqWithFirstElementWhenSliceFrom0To1OnNonNil() {
        final Seq<Integer> actual = of(1).slice(0, 1);
        assertThat(actual).isEqualTo(of(1));
    }

    @Test
    public void shouldReturnNilWhenSliceFrom1To1OnNonNil() {
        final Seq<Integer> actual = of(1).slice(1, 1);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnSliceWhenIndicesAreWithinRange() {
        final Seq<Integer> actual = of(1, 2, 3).slice(1, 3);
        assertThat(actual).isEqualTo(of(2, 3));
    }

    @Test
    public void shouldReturnNilWhenIndicesBothAreUpperBound() {
        final Seq<Integer> actual = of(1, 2, 3).slice(3, 3);
        assertThat(actual).isEqualTo(empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSliceOnNonNilWhenBeginIndexIsGreaterThanEndIndex() {
        of(1, 2, 3).slice(1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSliceOnNilWhenBeginIndexIsGreaterThanEndIndex() {
        empty().slice(1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSliceOnNonNilWhenBeginIndexExceedsLowerBound() {
        of(1, 2, 3).slice(-1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSliceOnNilWhenBeginIndexExceedsLowerBound() {
        empty().slice(-1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSlice2OnNil() {
        empty().slice(0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSliceWhenEndIndexExceedsUpperBound() {
        of(1, 2, 3).slice(1, 4).mkString(); // force computation of last element, e.g. because Stream is lazy
    }

    // -- unzip

    @Test
    public void shouldUnzipNil() {
        assertThat(empty().unzip(x -> Tuple.of(x, x))).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldUnzipNonNil() {
        final Tuple actual = of(0, 1).unzip(i -> Tuple.of(i, (char) ((short) 'a' + i)));
        final Tuple expected = Tuple.of(of(0, 1), this.<Character> of('a', 'b'));
        assertThat(actual).isEqualTo(expected);
    }

    // -- zip

    @Test
    public void shouldZipNils() {
        final Seq<?> actual = empty().zip(empty());
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldZipEmptyAndNonNil() {
        final Seq<?> actual = empty().zip(of(1));
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldZipNonEmptyAndNil() {
        final Seq<?> actual = of(1).zip(empty());
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldZipNonNilsIfThisIsSmaller() {
        final Seq<Tuple2<Integer, String>> actual = of(1, 2).zip(of("a", "b", "c"));
        @SuppressWarnings("unchecked")
        final Seq<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonNilsIfThatIsSmaller() {
        final Seq<Tuple2<Integer, String>> actual = of(1, 2, 3).zip(of("a", "b"));
        @SuppressWarnings("unchecked")
        final Seq<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonNilsOfSameSize() {
        final Seq<Tuple2<Integer, String>> actual = of(1, 2, 3).zip(of("a", "b", "c"));
        @SuppressWarnings("unchecked")
        final Seq<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowIfZipWithThatIsNull() {
        empty().zip(null);
    }

    // -- zipAll

    @Test
    public void shouldZipAllNils() {
        final Seq<?> actual = empty().zipAll(empty(), null, null);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldZipAllEmptyAndNonNil() {
        final Seq<?> actual = empty().zipAll(of(1), null, null);
        @SuppressWarnings("unchecked")
        final Seq<Tuple2<Object, Integer>> expected = of(Tuple.of(null, 1));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonEmptyAndNil() {
        final Seq<?> actual = of(1).zipAll(empty(), null, null);
        @SuppressWarnings("unchecked")
        final Seq<Tuple2<Integer, Object>> expected = of(Tuple.of(1, null));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThisIsSmaller() {
        final Seq<Tuple2<Integer, String>> actual = of(1, 2).zipAll(of("a", "b", "c"), 9, "z");
        @SuppressWarnings("unchecked")
        final Seq<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(9, "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThatIsSmaller() {
        final Seq<Tuple2<Integer, String>> actual = of(1, 2, 3).zipAll(of("a", "b"), 9, "z");
        @SuppressWarnings("unchecked")
        final Seq<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "z"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsOfSameSize() {
        final Seq<Tuple2<Integer, String>> actual = of(1, 2, 3).zipAll(of("a", "b", "c"), 9, "z");
        @SuppressWarnings("unchecked")
        final Seq<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowIfZipAllWithThatIsNull() {
        empty().zipAll(null, null, null);
    }

    // -- zipWithIndex

    @Test
    public void shouldZipNilWithIndex() {
        assertThat(this.<String> empty().zipWithIndex()).isEqualTo(this.<Tuple2<String, Integer>> empty());
    }

    @Test
    public void shouldZipNonNilWithIndex() {
        final Seq<Tuple2<String, Integer>> actual = of("a", "b", "c").zipWithIndex();
        @SuppressWarnings("unchecked")
        final Seq<Tuple2<String, Integer>> expected = of(Tuple.of("a", 0), Tuple.of("b", 1), Tuple.of("c", 2));
        assertThat(actual).isEqualTo(expected);
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
    public void  shouldCreateRangeByWhereFromIsLessThanTo() {
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
