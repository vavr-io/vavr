package javaslang.collection;

import javaslang.*;
import javaslang.WrappedString;
import org.assertj.core.api.*;
import org.junit.Test;

import java.util.*;

// TODO AbstractTraversableTest, AbstractValueTest
public class StringTest {

    protected <T> IterableAssert<T> assertThat(Iterable<T> actual) {
        return new IterableAssert<T>(actual) {
        };
    }

    protected <T> ObjectAssert<T> assertThat(T actual) {
        return new ObjectAssert<T>(actual) {
        };
    }

    protected BooleanAssert assertThat(Boolean actual) {
        return new BooleanAssert(actual) {
        };
    }

    protected DoubleAssert assertThat(Double actual) {
        return new DoubleAssert(actual) {
        };
    }

    protected IntegerAssert assertThat(Integer actual) {
        return new IntegerAssert(actual) {
        };
    }

    protected LongAssert assertThat(Long actual) {
        return new LongAssert(actual) {
        };
    }

    protected StringAssert assertThat(java.lang.String actual) {
        return new StringAssert(actual) {
        };
    }
    
    private WrappedString empty() {
        return WrappedString.empty();
    }

    // -- append

    @Test
    public void shouldAppendElementToNil() {
        final WrappedString actual = empty().append('1');
        final WrappedString expected = WrappedString.of('1');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendElementToNonNil() {
        final WrappedString actual = WrappedString.of('1', '2').append('3');
        final WrappedString expected = WrappedString.of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    // -- appendAll

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnAppendAllOfNull() {
        empty().appendAll(null);
    }

    @Test
    public void shouldAppendAllNilToNil() {
        final WrappedString actual = empty().appendAll(empty());
        final WrappedString expected = empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNonNilToNil() {
        final WrappedString actual = this.<Integer> empty().appendAll(WrappedString.of('1', '2', '3'));
        final WrappedString expected = WrappedString.of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNilToNonNil() {
        final WrappedString actual = WrappedString.of('1', '2', '3').appendAll(empty());
        final WrappedString expected = WrappedString.of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNonNilToNonNil() {
        final WrappedString actual = WrappedString.of('1', '2', '3').appendAll(WrappedString.of('4', '5', '6'));
        final WrappedString expected = WrappedString.of('1', '2', '3', '4', '5', '6');
        assertThat(actual).isEqualTo(expected);
    }

    // -- apply

    @Test
    public void shouldUseSeqAsPartialFunction() {
        assertThat(WrappedString.of('1', '2', '3').apply(1)).isEqualTo('2');
    }

    // -- containsSlice

    @Test
    public void shouldRecognizeNilNotContainsSlice() {
        final boolean actual = empty().containsSlice(WrappedString.of('1', '2', '3'));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesContainSlice() {
        final boolean actual = WrappedString.of('1', '2', '3', '4', '5').containsSlice(WrappedString.of('2', '3'));
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldRecognizeNonNilDoesNotContainSlice() {
        final boolean actual = WrappedString.of('1', '2', '3', '4', '5').containsSlice(WrappedString.of('2', '1', '4'));
        assertThat(actual).isFalse();
    }

    // -- crossProduct()

    @Test
    public void shouldCalculateCrossProductOfNil() {
        final Vector<Tuple2<Character, Character>> actual = empty().crossProduct();
        assertThat(actual).isEqualTo(empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCalculateCrossProductOfNonNil() {
        final Vector<Tuple2<Character, Character>> actual = WrappedString.of('1', '2', '3').crossProduct();
        final Vector<Tuple2<Character, Character>> expected = Vector.of(
                Tuple.of('1', '1'), Tuple.of('1', '2'), Tuple.of('1', '3'),
                Tuple.of('2', '1'), Tuple.of('2', '2'), Tuple.of('2', '3'),
                Tuple.of('3', '1'), Tuple.of('3', '2'), Tuple.of('3', '3'));
        assertThat(actual).isEqualTo(expected);
    }

    // -- crossProduct(Iterable)

    @Test
    public void shouldCalculateCrossProductOfNilAndNil() {
        final Traversable<Tuple2<Character, Object>> actual = empty().crossProduct(empty());
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldCalculateCrossProductOfNilAndNonNil() {
        final Traversable<Tuple2<Character, Object>> actual = empty().crossProduct(WrappedString.of('1', '2', '3'));
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldCalculateCrossProductOfNonNilAndNil() {
        final Traversable<Tuple2<Character, Character>> actual = WrappedString.of('1', '2', '3').crossProduct(WrappedString.empty());
        assertThat(actual).isEqualTo(empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCalculateCrossProductOfNonNilAndNonNil() {
        final Vector<Tuple2<Character, Character>> actual = WrappedString.of('1', '2', '3').crossProduct(WrappedString.of('a', 'b'));
        final Vector<Tuple2<Character, Character>> expected = Vector.of(
                Tuple.of('1', 'a'), Tuple.of('1', 'b'),
                Tuple.of('2', 'a'), Tuple.of('2', 'b'),
                Tuple.of('3', 'a'), Tuple.of('3', 'b'));
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
        WrappedString.of('1').get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetOnNil() {
        empty().get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetWithTooBigIndexOnNonNil() {
        WrappedString.of('1').get(1);
    }

    @Test
    public void shouldGetFirstElement() {
        assertThat(WrappedString.of('1', '2', '3').get(0)).isEqualTo('1');
    }

    @Test
    public void shouldGetLastElement() {
        assertThat(WrappedString.of('1', '2', '3').get(2)).isEqualTo('3');
    }

    // -- grouped

    @Test
    public void shouldGroupedNil() {
        assertThat(empty().grouped(1)).isEqualTo(empty());
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
        assertThat(WrappedString.of('1', '2', '3', '4').grouped(2)).isEqualTo(Vector.of(WrappedString.of('1', '2'), WrappedString.of('3', '4')));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldGroupedTraversableWithRemainder() {
        assertThat(WrappedString.of('1', '2', '3', '4', '5').grouped(2)).isEqualTo(Vector.of(WrappedString.of('1', '2'), WrappedString.of('3', '4'), WrappedString.of('5')));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldGroupedWhenTraversableLengthIsSmallerThanBlockSize() {
        assertThat(WrappedString.of('1', '2', '3', '4').grouped(5)).isEqualTo(Vector.of(WrappedString.of('1', '2', '3', '4')));
    }

    // -- indexOf

    @Test
    public void shouldNotFindIndexOfElementWhenSeqIsEmpty() {
        assertThat(empty().indexOf(1)).isEqualTo(-1);
    }

    @Test
    public void shouldNotFindIndexOfElementWhenStartIsGreater() {
        assertThat(WrappedString.of('1', '2', '3', '4').indexOf(2, 2)).isEqualTo(-1);
    }

    @Test
    public void shouldFindIndexOfFirstElement() {
        assertThat(WrappedString.of('1', '2', '3').indexOf('1')).isEqualTo(0);
    }

    @Test
    public void shouldFindIndexOfInnerElement() {
        assertThat(WrappedString.of('1', '2', '3').indexOf('2')).isEqualTo(1);
    }

    @Test
    public void shouldFindIndexOfLastElement() {
        assertThat(WrappedString.of('1', '2', '3').indexOf('3')).isEqualTo(2);
    }

    // -- indexOfSlice

    @Test
    public void shouldNotFindIndexOfSliceWhenSeqIsEmpty() {
        assertThat(empty().indexOfSlice(WrappedString.of('2', '3'))).isEqualTo(-1);
    }

    @Test
    public void shouldNotFindIndexOfSliceWhenStartIsGreater() {
        assertThat(WrappedString.of('1', '2', '3', '4').indexOfSlice(WrappedString.of('2', '3'), 2)).isEqualTo(-1);
    }

    @Test
    public void shouldFindIndexOfFirstSlice() {
        assertThat(WrappedString.of('1', '2', '3', '4').indexOfSlice(WrappedString.of('1', '2'))).isEqualTo(0);
    }

    @Test
    public void shouldFindIndexOfInnerSlice() {
        assertThat(WrappedString.of('1', '2', '3', '4').indexOfSlice(WrappedString.of('2', '3'))).isEqualTo(1);
    }

    @Test
    public void shouldFindIndexOfLastSlice() {
        assertThat(WrappedString.of('1', '2', '3').indexOfSlice(WrappedString.of('2', '3'))).isEqualTo(1);
    }

    // -- lastIndexOf

    @Test
    public void shouldNotFindLastIndexOfElementWhenSeqIsEmpty() {
        assertThat(empty().lastIndexOf(1)).isEqualTo(-1);
    }

    @Test
    public void shouldNotFindLastIndexOfElementWhenEndIdLess() {
        assertThat(WrappedString.of('1', '2', '3', '4').lastIndexOf(3, 1)).isEqualTo(-1);
    }

    @Test
    public void shouldFindLastIndexOfElement() {
        assertThat(WrappedString.of('1', '2', '3', '1', '2', '3').lastIndexOf('1')).isEqualTo(3);
    }

    @Test
    public void shouldFindLastIndexOfElementWithEnd() {
        assertThat(WrappedString.of('1', '2', '3', '1', '2', '3').lastIndexOf('1', 1)).isEqualTo(0);
    }

    // -- lastIndexOfSlice

    @Test
    public void shouldNotFindLastIndexOfSliceWhenSeqIsEmpty() {
        assertThat(empty().lastIndexOfSlice(WrappedString.of('2', '3'))).isEqualTo(-1);
    }

    @Test
    public void shouldNotFindLastIndexOfSliceWhenEndIdLess() {
        assertThat(WrappedString.of('1', '2', '3', '4', '5').lastIndexOfSlice(WrappedString.of('3', '4'), 1)).isEqualTo(-1);
    }

    @Test
    public void shouldFindLastIndexOfSlice() {
        assertThat(WrappedString.of('1', '2', '3', '1', '2').lastIndexOfSlice(empty())).isEqualTo(5);
        assertThat(WrappedString.of('1', '2', '3', '1', '2').lastIndexOfSlice(WrappedString.of('2'))).isEqualTo(4);
        assertThat(WrappedString.of('1', '2', '3', '1', '2', '3', '4').lastIndexOfSlice(WrappedString.of('2', '3'))).isEqualTo(4);
    }

    @Test
    public void shouldFindLastIndexOfSliceWithEnd() {
        assertThat(WrappedString.of('1', '2', '3', '1', '2', '3').lastIndexOfSlice(empty(), 2)).isEqualTo(2);
        assertThat(WrappedString.of('1', '2', '3', '1', '2', '3').lastIndexOfSlice(WrappedString.of('2'), 2)).isEqualTo(1);
        assertThat(WrappedString.of('1', '2', '3', '1', '2', '3').lastIndexOfSlice(WrappedString.of('2', '3'), 2)).isEqualTo(1);
        assertThat(WrappedString.of('1', '2', '3', '1', '2', '3', '4').lastIndexOfSlice(WrappedString.of('2', '3'), 2)).isEqualTo(1);
    }

    // -- insert

    @Test
    public void shouldInsertIntoNil() {
        final WrappedString actual = this.<Integer> empty().insert(0, '1');
        final WrappedString expected = WrappedString.of('1');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertInFrontOfElement() {
        final WrappedString actual = WrappedString.of('4').insert(0, '1');
        final WrappedString expected = WrappedString.of('1', '4');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertBehindOfElement() {
        final WrappedString actual = WrappedString.of('4').insert(1, '1');
        final WrappedString expected = WrappedString.of('4', '1');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertIntoSeq() {
        final WrappedString actual = WrappedString.of('1', '2', '3').insert(2, '4');
        final WrappedString expected = WrappedString.of('1', '2', '4', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenInsertOnNonNilWithNegativeIndex() {
        WrappedString.of('1').insert(-1, null);
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
        final WrappedString actual = this.<Integer> empty().insertAll(0, WrappedString.of('1', '2', '3'));
        final WrappedString expected = WrappedString.of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllInFrontOfElement() {
        final WrappedString actual = WrappedString.of('4').insertAll(0, WrappedString.of('1', '2', '3'));
        final WrappedString expected = WrappedString.of('1', '2', '3', '4');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllBehindOfElement() {
        final WrappedString actual = WrappedString.of('4').insertAll(1, WrappedString.of('1', '2', '3'));
        final WrappedString expected = WrappedString.of('4', '1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllIntoSeq() {
        final WrappedString actual = WrappedString.of('1', '2', '3').insertAll(2, WrappedString.of('4', '5'));
        final WrappedString expected = WrappedString.of('1', '2', '4', '5', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnInsertAllWithNil() {
        empty().insertAll(0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenInsertOnNonNilAllWithNegativeIndex() {
        WrappedString.of('1').insertAll(-1, empty());
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
        assertThat(WrappedString.of('a').intersperse(',')).isEqualTo(WrappedString.of('a'));
    }

    @Test
    public void shouldIntersperseMultipleElements() {
        assertThat(WrappedString.of('a', 'b').intersperse(',')).isEqualTo(WrappedString.of('a', ',', 'b'));
    }

    // -- iterator(int)

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenNilIteratorStartingAtIndex() {
        empty().iterator(1);
    }

    @Test
    public void shouldIterateFirstElementOfNonNilStartingAtIndex() {
        assertThat(WrappedString.of('1', '2', '3').iterator(1).next()).isEqualTo('2');
    }

    @Test
    public void shouldFullyIterateNonNilStartingAtIndex() {
        int actual = -1;
        for (java.util.Iterator<Character> iter = WrappedString.of('1', '2', '3').iterator(1); iter.hasNext(); ) {
            actual = iter.next();
        }
        assertThat(actual).isEqualTo('3');
    }

    // -- prepend

    @Test
    public void shouldPrependElementToNil() {
        final WrappedString actual = this.<Integer> empty().prepend('1');
        final WrappedString expected = WrappedString.of('1');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependElementToNonNil() {
        final WrappedString actual = WrappedString.of('2', '3').prepend('1');
        final WrappedString expected = WrappedString.of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    // -- prependAll

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnPrependAllOfNull() {
        empty().prependAll(null);
    }

    @Test
    public void shouldPrependAllNilToNil() {
        final WrappedString actual = this.<Integer> empty().prependAll(empty());
        final WrappedString expected = empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNilToNonNil() {
        final WrappedString actual = WrappedString.of('1', '2', '3').prependAll(empty());
        final WrappedString expected = WrappedString.of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNonNilToNil() {
        final WrappedString actual = this.<Integer> empty().prependAll(WrappedString.of('1', '2', '3'));
        final WrappedString expected = WrappedString.of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNonNilToNonNil() {
        final WrappedString actual = WrappedString.of('4', '5', '6').prependAll(WrappedString.of('1', '2', '3'));
        final WrappedString expected = WrappedString.of('1', '2', '3', '4', '5', '6');
        assertThat(actual).isEqualTo(expected);
    }

    // -- remove

    @Test
    public void shouldRemoveElementFromNil() {
        assertThat(empty().remove(null)).isEqualTo(empty());
    }

    @Test
    public void shouldRemoveFirstElement() {
        assertThat(WrappedString.of('1', '2', '3').remove('1')).isEqualTo(WrappedString.of('2', '3'));
    }

    @Test
    public void shouldRemoveLastElement() {
        assertThat(WrappedString.of('1', '2', '3').remove('3')).isEqualTo(WrappedString.of('1', '2'));
    }

    @Test
    public void shouldRemoveInnerElement() {
        assertThat(WrappedString.of('1', '2', '3').remove('2')).isEqualTo(WrappedString.of('1', '3'));
    }

    @Test
    public void shouldRemoveNonExistingElement() {
        final WrappedString t = WrappedString.of('1', '2', '3');
        if (isThisLazyCollection()) {
            assertThat(t.remove('4')).isEqualTo(t);
        } else {
            assertThat(t.remove('4')).isSameAs(t);
        }
    }

    boolean isThisLazyCollection() {
        return false;
    }

    // -- removeFirst(Predicate)

    @Test
    public void shouldRemoveFirstElementByPredicateFromNil() {
        assertThat(empty().removeFirst(v -> true)).isEqualTo(empty());
    }

    @Test
    public void shouldRemoveFirstElementByPredicateBegin() {
        assertThat(WrappedString.of('1', '2', '3').removeFirst(v -> v == '1')).isEqualTo(WrappedString.of('2', '3'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateBeginM() {
        assertThat(WrappedString.of('1', '2', '1', '3').removeFirst(v -> v == '1')).isEqualTo(WrappedString.of('2', '1', '3'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateEnd() {
        assertThat(WrappedString.of('1', '2', '3').removeFirst(v -> v == '3')).isEqualTo(WrappedString.of('1', '2'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateInner() {
        assertThat(WrappedString.of('1', '2', '3', '4', '5').removeFirst(v -> v == '3')).isEqualTo(WrappedString.of('1', '2', '4', '5'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateInnerM() {
        assertThat(WrappedString.of('1', '2', '3', '2', '5').removeFirst(v -> v == '2')).isEqualTo(WrappedString.of('1', '3', '2', '5'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateNonExisting() {
        final WrappedString t = WrappedString.of('1', '2', '3');
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
        assertThat(WrappedString.of('1', '2', '3').removeLast(v -> v == '1')).isEqualTo(WrappedString.of('2', '3'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateEnd() {
        assertThat(WrappedString.of('1', '2', '3').removeLast(v -> v == '3')).isEqualTo(WrappedString.of('1', '2'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateEndM() {
        assertThat(WrappedString.of('1', '3', '2', '3').removeLast(v -> v == '3')).isEqualTo(WrappedString.of('1', '3', '2'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateInner() {
        assertThat(WrappedString.of('1', '2', '3', '4', '5').removeLast(v -> v == '3')).isEqualTo(WrappedString.of('1', '2', '4', '5'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateInnerM() {
        assertThat(WrappedString.of('1', '2', '3', '2', '5').removeLast(v -> v == '2')).isEqualTo(WrappedString.of('1', '2', '3', '5'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateNonExisting() {
        final WrappedString t = WrappedString.of('1', '2', '3');
        assertThat(t.removeLast(v -> v == 4)).isSameAs(t);
    }

    // -- removeAll(Iterable)

    @Test
    public void shouldRemoveAllElementsFromNil() {
        assertThat(empty().removeAll(WrappedString.of('1', '2', '3'))).isEqualTo(empty());
    }

    @Test
    public void shouldRemoveAllExistingElementsFromNonNil() {
        assertThat(WrappedString.of('1', '2', '3', '1', '2', '3').removeAll(WrappedString.of('1', '2'))).isEqualTo(WrappedString.of('3', '3'));
    }

    @Test
    public void shouldNotRemoveAllNonExistingElementsFromNonNil() {
        final WrappedString t = WrappedString.of('1', '2', '3');
        if (isThisLazyCollection()) {
            assertThat(t.removeAll(WrappedString.of('4', '5'))).isEqualTo(t);
        } else {
            assertThat(t.removeAll(WrappedString.of('4', '5'))).isSameAs(t);
        }
    }

    // -- removeAll(Object)

    @Test
    public void shouldRemoveAllObjectsFromNil() {
        assertThat(empty().removeAll('1')).isEqualTo(empty());
    }

    @Test
    public void shouldRemoveAllExistingObjectsFromNonNil() {
        assertThat(WrappedString.of('1', '2', '3', '1', '2', '3').removeAll('1')).isEqualTo(WrappedString.of('2', '3', '2', '3'));
    }

    @Test
    public void shouldNotRemoveAllNonObjectsElementsFromNonNil() {
        final WrappedString t = WrappedString.of('1', '2', '3');
        if (isThisLazyCollection()) {
            assertThat(t.removeAll('4')).isEqualTo(t);
        } else {
            assertThat(t.removeAll('4')).isSameAs(t);
        }
    }

    // -- reverse

    @Test
    public void shouldReverseNil() {
        assertThat(empty().reverse()).isEqualTo(empty());
    }

    @Test
    public void shouldReverseNonNil() {
        assertThat(WrappedString.of('1', '2', '3').reverse()).isEqualTo(WrappedString.of('3', '2', '1'));
    }

    // -- set

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithNegativeIndexOnNil() {
        empty().set(-1, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithNegativeIndexOnNonNil() {
        WrappedString.of('1').set(-1, '2');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetOnNil() {
        empty().set(0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithIndexExceedingByOneOnNonNil() {
        WrappedString.of('1').set(1, '2');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithIndexExceedingByTwoOnNonNil() {
        WrappedString.of('1').set(2, '2');
    }

    @Test
    public void shouldSetFirstElement() {
        assertThat(WrappedString.of('1', '2', '3').set(0, '4')).isEqualTo(WrappedString.of('4', '2', '3'));
    }

    @Test
    public void shouldSetLastElement() {
        assertThat(WrappedString.of('1', '2', '3').set(2, '4')).isEqualTo(WrappedString.of('1', '2', '4'));
    }

    // -- sort()

    @Test
    public void shouldSortNil() {
        assertThat(empty().sort()).isEqualTo(empty());
    }

    @Test
    public void shouldSortNonNil() {
        assertThat(WrappedString.of('3', '4', '1', '2').sort()).isEqualTo(WrappedString.of('1', '2', '3', '4'));
    }

    // -- sort(Comparator)

    @Test
    public void shouldSortNilUsingComparator() {
        assertThat(this.<Integer> empty().sort((i, j) -> j - i)).isEqualTo(empty());
    }

    @Test
    public void shouldSortNonNilUsingComparator() {
        assertThat(WrappedString.of('3', '4', '1', '2').sort((i, j) -> j - i)).isEqualTo(WrappedString.of('4', '3', '2', '1'));
    }

    // -- splitAt(index)

    @Test
    public void shouldSplitAtNil() {
        assertThat(empty().splitAt(1)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSplitAtNonNil() {
        assertThat(WrappedString.of('1', '2', '3').splitAt(1)).isEqualTo(Tuple.of(WrappedString.of('1'), WrappedString.of('2', '3')));
    }

    @Test
    public void shouldSplitAtBegin() {
        assertThat(WrappedString.of('1', '2', '3').splitAt(0)).isEqualTo(Tuple.of(empty(), WrappedString.of('1', '2', '3')));
    }

    @Test
    public void shouldSplitAtEnd() {
        assertThat(WrappedString.of('1', '2', '3').splitAt(3)).isEqualTo(Tuple.of(WrappedString.of('1', '2', '3'), empty()));
    }

    @Test
    public void shouldSplitAtOutOfBounds() {
        assertThat(WrappedString.of('1', '2', '3').splitAt(5)).isEqualTo(Tuple.of(WrappedString.of('1', '2', '3'), empty()));
        assertThat(WrappedString.of('1', '2', '3').splitAt(-1)).isEqualTo(Tuple.of(empty(), WrappedString.of('1', '2', '3')));
    }

    // -- splitAt(predicate)

    @Test
    public void shouldSplitPredicateAtNil() {
        assertThat(empty().splitAt(e -> true)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSplitPredicateAtNonNil() {
        assertThat(WrappedString.of('1', '2', '3').splitAt(e -> e == '2')).isEqualTo(Tuple.of(WrappedString.of('1'), WrappedString.of('2', '3')));
    }

    @Test
    public void shouldSplitAtPredicateBegin() {
        assertThat(WrappedString.of('1', '2', '3').splitAt(e -> e == '1')).isEqualTo(Tuple.of(empty(), WrappedString.of('1', '2', '3')));
    }

    @Test
    public void shouldSplitAtPredicateEnd() {
        assertThat(WrappedString.of('1', '2', '3').splitAt(e -> e == '3')).isEqualTo(Tuple.of(WrappedString.of('1', '2'), WrappedString.of('3')));
    }

    @Test
    public void shouldSplitAtPredicateNotFound() {
        assertThat(WrappedString.of('1', '2', '3').splitAt(e -> e == '5')).isEqualTo(Tuple.of(WrappedString.of('1', '2', '3'), empty()));
    }

    // -- splitAtInclusive(predicate)

    @Test
    public void shouldSplitInclusivePredicateAtNil() {
        assertThat(empty().splitAtInclusive(e -> true)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSplitInclusivePredicateAtNonNil() {
        assertThat(WrappedString.of('1', '2', '3').splitAtInclusive(e -> e == '2')).isEqualTo(Tuple.of(WrappedString.of('1', '2'), WrappedString.of('3')));
    }

    @Test
    public void shouldSplitAtInclusivePredicateBegin() {
        assertThat(WrappedString.of('1', '2', '3').splitAtInclusive(e -> e == '1')).isEqualTo(Tuple.of(WrappedString.of('1'), WrappedString.of('2', '3')));
    }

    @Test
    public void shouldSplitAtInclusivePredicateEnd() {
        assertThat(WrappedString.of('1', '2', '3').splitAtInclusive(e -> e == '3')).isEqualTo(Tuple.of(WrappedString.of('1', '2', '3'), empty()));
    }

    @Test
    public void shouldSplitAtInclusivePredicateNotFound() {
        assertThat(WrappedString.of('1', '2', '3').splitAtInclusive(e -> e == '5')).isEqualTo(Tuple.of(WrappedString.of('1', '2', '3'), empty()));
    }

    // -- removeAt(index)

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldRemoveIndxAtNil() {
        assertThat(empty().removeAt(1)).isEqualTo(empty());
    }

    @Test
    public void shouldRemoveIndxAtNonNil() {
        assertThat(WrappedString.of('1', '2', '3').removeAt(1)).isEqualTo(WrappedString.of('1', '3'));
    }

    @Test
    public void shouldRemoveIndxAtBegin() {
        assertThat(WrappedString.of('1', '2', '3').removeAt(0)).isEqualTo(WrappedString.of('2', '3'));
    }

    @Test
    public void shouldRemoveIndxAtEnd() {
        assertThat(WrappedString.of('1', '2', '3').removeAt(2)).isEqualTo(WrappedString.of('1', '2'));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldRemoveIndxOutOfBoundsLeft() {
        assertThat(WrappedString.of('1', '2', '3').removeAt(-1)).isEqualTo(WrappedString.of('1', '2', '3'));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldRemoveIndxOutOfBoundsRight() {
        assertThat(WrappedString.of('1', '2', '3').removeAt(5)).isEqualTo(WrappedString.of('1', '2', '3'));
    }

    // -- subsequence(beginIndex)

    @Test
    public void shouldReturnNilWhenSubsequenceFrom0OnNil() {
        final WrappedString actual = this.<Integer> empty().subsequence(0);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnIdentityWhenSubsequenceFrom0OnNonNil() {
        final WrappedString actual = WrappedString.of('1').subsequence(0);
        assertThat(actual).isEqualTo(WrappedString.of('1'));
    }

    @Test
    public void shouldReturnNilWhenSubsequenceFrom1OnSeqOf1() {
        final WrappedString actual = WrappedString.of('1').subsequence(1);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnSubsequenceWhenIndexIsWithinRange() {
        final WrappedString actual = WrappedString.of('1', '2', '3').subsequence(1);
        assertThat(actual).isEqualTo(WrappedString.of('2', '3'));
    }

    @Test
    public void shouldReturnNilWhenSubsequenceBeginningWithSize() {
        final WrappedString actual = WrappedString.of('1', '2', '3').subsequence(3);
        assertThat(actual).isEqualTo(empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubsequenceOnNil() {
        empty().subsequence(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubsequenceWithOutOfLowerBound() {
        WrappedString.of('1', '2', '3').subsequence(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubsequenceWithOutOfUpperBound() {
        WrappedString.of('1', '2', '3').subsequence(4);
    }

    // -- subsequence(beginIndex, endIndex)

    @Test
    public void shouldReturnNilWhenSubsequenceFrom0To0OnNil() {
        final WrappedString actual = this.<Integer> empty().subsequence(0, 0);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnNilWhenSubsequenceFrom0To0OnNonNil() {
        final WrappedString actual = WrappedString.of('1').subsequence(0, 0);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnSeqWithFirstElementWhenSubsequenceFrom0To1OnNonNil() {
        final WrappedString actual = WrappedString.of('1').subsequence(0, 1);
        assertThat(actual).isEqualTo(WrappedString.of('1'));
    }

    @Test
    public void shouldReturnNilWhenSubsequenceFrom1To1OnNonNil() {
        final WrappedString actual = WrappedString.of('1').subsequence(1, 1);
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldReturnSubsequenceWhenIndicesAreWithinRange() {
        final WrappedString actual = WrappedString.of('1', '2', '3').subsequence(1, 3);
        assertThat(actual).isEqualTo(WrappedString.of('2', '3'));
    }

    @Test
    public void shouldReturnNilWhenIndicesBothAreUpperBound() {
        final WrappedString actual = WrappedString.of('1', '2', '3').subsequence(3, 3);
        assertThat(actual).isEqualTo(empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubsequenceOnNonNilWhenBeginIndexIsGreaterThanEndIndex() {
        WrappedString.of('1', '2', '3').subsequence(1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubsequenceOnNilWhenBeginIndexIsGreaterThanEndIndex() {
        empty().subsequence(1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubsequenceOnNonNilWhenBeginIndexExceedsLowerBound() {
        WrappedString.of('1', '2', '3').subsequence(-'1', '2');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubsequenceOnNilWhenBeginIndexExceedsLowerBound() {
        empty().subsequence(-'1', '2');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubsequence2OnNil() {
        empty().subsequence(0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubsequenceWhenEndIndexExceedsUpperBound() {
        WrappedString.of('1', '2', '3').subsequence(1, 4).mkString(); // force computation of last element, e.g. because Stream is lazy
    }

    // -- unzip

    @Test
    public void shouldUnzipNil() {
        assertThat(empty().unzip(x -> Tuple.of(x, x))).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldUnzipNonNil() {
        final Tuple actual = WrappedString.of('0', '1').unzip(i -> Tuple.of(i, i == '0' ? 'a' : 'b'));
        final Tuple expected = Tuple.of(Vector.of('0', '1'), Vector.of('a', 'b'));
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
        final Seq<?> actual = empty().zip(WrappedString.of('1'));
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldZipNonEmptyAndNil() {
        final Seq<?> actual = WrappedString.of('1').zip(empty());
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldZipNonNilsIfThisIsSmaller() {
        final Vector<Tuple2<Character, Character>> actual = WrappedString.of('1', '2').zip(WrappedString.of('a', 'b', 'c'));
        @SuppressWarnings("unchecked")
        final Vector<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonNilsIfThatIsSmaller() {
        final Vector<Tuple2<Character, Character>> actual = WrappedString.of('1', '2', '3').zip(WrappedString.of('a', 'b'));
        @SuppressWarnings("unchecked")
        final Vector<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonNilsOfSameSize() {
        final Vector<Tuple2<Character, Character>> actual = WrappedString.of('1', '2', '3').zip(WrappedString.of('a', 'b', 'c'));
        @SuppressWarnings("unchecked")
        final Vector<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'), Tuple.of('3', 'c'));
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
        final Vector<?> actual = empty().zipAll(WrappedString.of('1'), null, null);
        @SuppressWarnings("unchecked")
        final Vector<Tuple2<Object, Character>> expected = Vector.of(Tuple.of(null, '1'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonEmptyAndNil() {
        final Vector<?> actual = WrappedString.of('1').zipAll(empty(), null, null);
        @SuppressWarnings("unchecked")
        final Vector<Tuple2<Character, Object>> expected = Vector.of(Tuple.of('1', null));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThisIsSmaller() {
        final Vector<Tuple2<Character, Character>> actual = WrappedString.of('1', '2').zipAll(WrappedString.of('a', 'b', 'c'), '9', 'z');
        @SuppressWarnings("unchecked")
        final Vector<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'), Tuple.of('9', 'c'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThatIsSmaller() {
        final Vector<Tuple2<Character, Character>> actual = WrappedString.of('1', '2', '3').zipAll(WrappedString.of('a', 'b'), '9', 'z');
        @SuppressWarnings("unchecked")
        final Vector<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'), Tuple.of('3', 'z'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsOfSameSize() {
        final Vector<Tuple2<Character, Character>> actual = WrappedString.of('1', '2', '3').zipAll(WrappedString.of('a', 'b', 'c'), '9', 'z');
        @SuppressWarnings("unchecked")
        final Vector<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'), Tuple.of('3', 'c'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowIfZipAllWithThatIsNull() {
        empty().zipAll(null, null, null);
    }

    // -- zipWithIndex

    @Test
    public void shouldZipNilWithIndex() {
        assertThat(this.<WrappedString> empty().zipWithIndex()).isEqualTo(this.<Tuple2<WrappedString, Integer>> empty());
    }

    @Test
    public void shouldZipNonNilWithIndex() {
        final Vector<Tuple2<Character, Integer>> actual = WrappedString.of("abc").zipWithIndex();
        @SuppressWarnings("unchecked")
        final Vector<Tuple2<Character, Integer>> expected = Vector.of(Tuple.of('a', 0), Tuple.of('b', 1), Tuple.of('c', 2));
        assertThat(actual).isEqualTo(expected);
    }

    // -- static collector()

    @Test
    public void shouldStreamAndCollectNil() {
        final Seq<?> actual = java.util.stream.Stream.<Character>empty().collect(WrappedString.collector());
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldStreamAndCollectNonNil() {
        final Seq<?> actual = java.util.stream.Stream.of('1', '2', '3').collect(WrappedString.collector());
        assertThat(actual).isEqualTo(WrappedString.of('1', '2', '3'));
    }

    @Test
    public void shouldParallelStreamAndCollectNil() {
        final Seq<?> actual = java.util.stream.Stream.<Character>empty().parallel().collect(WrappedString.collector());
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldParallelStreamAndCollectNonNil() {
        final Seq<?> actual = java.util.stream.Stream.of('1', '2', '3').parallel().collect(WrappedString.collector());
        assertThat(actual).isEqualTo(WrappedString.of('1', '2', '3'));
    }

    // -- static empty()

    @Test
    public void shouldCreateNil() {
        final Seq<?> actual = empty();
        assertThat(actual.length()).isEqualTo(0);
    }

    // -- static javaslang.String.of(T...)

    @Test
    public void shouldCreateSeqOfElements() {
        final WrappedString actual = WrappedString.of('1', '2');
        assertThat(actual.length()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualTo('1');
        assertThat(actual.get(1)).isEqualTo('2');
    }

    // -- static ofAll(Iterable)

    @Test
    public void shouldCreateListOfIterable() {
        final java.util.List<Character> arrayList = Arrays.asList('1', '2');
        final WrappedString actual = WrappedString.ofAll(arrayList);
        assertThat(actual.length()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualTo('1');
        assertThat(actual.get(1)).isEqualTo('2');
    }

}
