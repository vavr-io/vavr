/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;
import org.assertj.core.api.*;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
import static javaslang.collection.CharSeq.*;

public class CharSeqTest {

    protected <T> IterableAssert<T> assertThat(Iterable<T> actual) {
        return new IterableAssert<T>(actual) {};
    }

    protected <T> ObjectAssert<T> assertThat(T actual) {
        return new ObjectAssert<T>(actual) {};
    }

    protected BooleanAssert assertThat(Boolean actual) {
        return new BooleanAssert(actual) {};
    }

    protected DoubleAssert assertThat(Double actual) {
        return new DoubleAssert(actual) {};
    }

    protected IntegerAssert assertThat(Integer actual) {
        return new IntegerAssert(actual) {};
    }

    protected LongAssert assertThat(Long actual) {
        return new LongAssert(actual) {};
    }

    protected StringAssert assertThat(String actual) {
        return new StringAssert(actual) {};
    }

    // -- exists

    @Test
    public void shouldBeAwareOfExistingElement() {
        assertThat(of('1', '2').exists(i -> i == '2')).isTrue();
    }

    @Test
    public void shouldBeAwareOfNonExistingElement() {
        assertThat(empty().exists(i -> i == 1)).isFalse();
    }

    // -- forAll

    @Test
    public void shouldBeAwareOfPropertyThatHoldsForAll() {
        assertThat(of('2', '4').forAll(i -> i % 2 == 0)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsForAll() {
        assertThat(of('2', '3').forAll(i -> i % 2 == 0)).isFalse();
    }

    // -- padTo

    @Test
    public void shouldPadEmptyToEmpty() {
        assertThat(empty().padTo(0, 'a')).isSameAs(empty());
    }

    @Test
    public void shouldPadEmptyToNonEmpty() {
        assertThat(empty().padTo(2, 'a')).isEqualTo(of('a', 'a'));
    }

    @Test
    public void shouldPadNonEmptyZeroLen() {
        final CharSeq seq = of('a');
        assertThat(seq.padTo(0, 'b')).isSameAs(seq);
    }

    @Test
    public void shouldPadNonEmpty() {
        assertThat(of('a').padTo(2, 'a')).isEqualTo(of('a', 'a'));
        assertThat(of('a').padTo(2, 'b')).isEqualTo(of('a', 'b'));
        assertThat(of('a').padTo(3, 'b')).isEqualTo(of('a', 'b', 'b'));
    }

    // -- leftPadTo

    @Test
    public void shouldLeftPadEmptyToEmpty() {
        assertThat(empty().leftPadTo(0, 'a')).isSameAs(empty());
    }

    @Test
    public void shouldLeftPadEmptyToNonEmpty() {
        assertThat(empty().leftPadTo(2, 'a')).isEqualTo(of('a', 'a'));
    }

    @Test
    public void shouldLeftPadNonEmptyZeroLen() {
        final CharSeq seq = of('a');
        assertThat(seq.leftPadTo(0, 'b')).isSameAs(seq);
    }

    @Test
    public void shouldLeftPadNonEmpty() {
        assertThat(of('a').leftPadTo(2, 'a')).isEqualTo(of('a', 'a'));
        assertThat(of('a').leftPadTo(2, 'b')).isEqualTo(of('b', 'a'));
        assertThat(of('a').leftPadTo(3, 'b')).isEqualTo(of('b', 'b', 'a'));
    }

    // -- patch

    @Test
    public void shouldPatchEmptyByEmpty() {
        assertThat(empty().patch(0, empty(), 0)).isSameAs(empty());
        assertThat(empty().patch(-1, empty(), -1)).isSameAs(empty());
        assertThat(empty().patch(-1, empty(), 1)).isSameAs(empty());
        assertThat(empty().patch(1, empty(), -1)).isSameAs(empty());
        assertThat(empty().patch(1, empty(), 1)).isSameAs(empty());
    }

    @Test
    public void shouldPatchEmptyByNonEmpty() {
        final Seq<Character> s = of('1', '2', '3');
        assertThat(empty().patch(0, s, 0)).isEqualTo(s);
        assertThat(empty().patch(-1, s, -1)).isEqualTo(s);
        assertThat(empty().patch(-1, s, 1)).isEqualTo(s);
        assertThat(empty().patch(1, s, -1)).isEqualTo(s);
        assertThat(empty().patch(1, s, 1)).isEqualTo(s);
    }

    @Test
    public void shouldPatchNonEmptyByEmpty() {
        final Seq<Character> s = of('1', '2', '3');
        assertThat(s.patch(-1, empty(), -1)).isEqualTo(of('1', '2', '3'));
        assertThat(s.patch(-1, empty(), 0)).isEqualTo(of('1', '2', '3'));
        assertThat(s.patch(-1, empty(), 1)).isEqualTo(of('2', '3'));
        assertThat(s.patch(-1, empty(), 3)).isSameAs(empty());
        assertThat(s.patch(0, empty(), -1)).isEqualTo(of('1', '2', '3'));
        assertThat(s.patch(0, empty(), 0)).isEqualTo(of('1', '2', '3'));
        assertThat(s.patch(0, empty(), 1)).isEqualTo(of('2', '3'));
        assertThat(s.patch(0, empty(), 3)).isSameAs(empty());
        assertThat(s.patch(1, empty(), -1)).isEqualTo(of('1', '2', '3'));
        assertThat(s.patch(1, empty(), 0)).isEqualTo(of('1', '2', '3'));
        assertThat(s.patch(1, empty(), 1)).isEqualTo(of('1', '3'));
        assertThat(s.patch(1, empty(), 3)).isEqualTo(of('1'));
        assertThat(s.patch(4, empty(), -1)).isEqualTo(of('1', '2', '3'));
        assertThat(s.patch(4, empty(), 0)).isEqualTo(of('1', '2', '3'));
        assertThat(s.patch(4, empty(), 1)).isEqualTo(of('1', '2', '3'));
        assertThat(s.patch(4, empty(), 3)).isEqualTo(of('1', '2', '3'));
    }

    @Test
    public void shouldPatchNonEmptyByNonEmpty() {
        final Seq<Character> s = of('1', '2', '3');
        final Seq<Character> d = of('4', '5', '6');
        assertThat(s.patch(-1, d, -1)).isEqualTo(of('4', '5', '6', '1', '2', '3'));
        assertThat(s.patch(-1, d, 0)).isEqualTo(of('4', '5', '6', '1', '2', '3'));
        assertThat(s.patch(-1, d, 1)).isEqualTo(of('4', '5', '6', '2', '3'));
        assertThat(s.patch(-1, d, 3)).isEqualTo(of('4', '5', '6'));
        assertThat(s.patch(0, d, -1)).isEqualTo(of('4', '5', '6', '1', '2', '3'));
        assertThat(s.patch(0, d, 0)).isEqualTo(of('4', '5', '6', '1', '2', '3'));
        assertThat(s.patch(0, d, 1)).isEqualTo(of('4', '5', '6', '2', '3'));
        assertThat(s.patch(0, d, 3)).isEqualTo(of('4', '5', '6'));
        assertThat(s.patch(1, d, -1)).isEqualTo(of('1', '4', '5', '6', '2', '3'));
        assertThat(s.patch(1, d, 0)).isEqualTo(of('1', '4', '5', '6', '2', '3'));
        assertThat(s.patch(1, d, 1)).isEqualTo(of('1', '4', '5', '6', '3'));
        assertThat(s.patch(1, d, 3)).isEqualTo(of('1', '4', '5', '6'));
        assertThat(s.patch(4, d, -1)).isEqualTo(of('1', '2', '3', '4', '5', '6'));
        assertThat(s.patch(4, d, 0)).isEqualTo(of('1', '2', '3', '4', '5', '6'));
        assertThat(s.patch(4, d, 1)).isEqualTo(of('1', '2', '3', '4', '5', '6'));
        assertThat(s.patch(4, d, 3)).isEqualTo(of('1', '2', '3', '4', '5', '6'));
    }

    // -- peek

    @Test
    public void shouldPeekNil() {
        assertThat(empty().peek(t -> {})).isSameAs(empty());
    }

    @Test
    public void shouldPeekNonNilPerformingNoAction() {
        assertThat(of('1').peek(t -> {})).isEqualTo(of('1'));
    }

    @Test
    public void shouldPeekSingleValuePerformingAnAction() {
        final char[] effect = { 0 };
        final CharSeq actual = of('1').peek(i -> effect[0] = i);
        assertThat(actual).isEqualTo(of('1'));
        assertThat(effect[0]).isEqualTo('1');
    }

    // -- static rangeClosed()

    @Test
    public void shouldCreateRangeClosedWhereFromIsGreaterThanTo() {
        assertThat(rangeClosed('b', 'a')).isEmpty();
    }

    @Test
    public void shouldCreateRangeClosedWhereFromEqualsTo() {
        assertThat(rangeClosed('a', 'a')).isEqualTo(of('a'));
    }

    @Test
    public void shouldCreateRangeClosedWhereFromIsLessThanTo() {
        assertThat(rangeClosed('a', 'c')).isEqualTo(of('a', 'b', 'c'));
    }

    @Test
    public void shouldCreateRangeClosedWhereFromAndToEqualMIN_VALUE() {
        assertThat(rangeClosed(Character.MIN_VALUE, Character.MIN_VALUE)).isEqualTo(of(Character.MIN_VALUE));
    }

    @Test
    public void shouldCreateRangeClosedWhereFromAndToEqualMAX_VALUE() {
        assertThat(rangeClosed(Character.MAX_VALUE, Character.MAX_VALUE)).isEqualTo(of(Character.MAX_VALUE));
    }

    // -- static rangeClosedBy()

    @Test
    public void shouldCreateRangeClosedByWhereFromIsGreaterThanToAndStepWrongDirection() {
        assertThat(rangeClosedBy('b', 'a', 1)).isEmpty();
        assertThat(rangeClosedBy('b', 'a', 3)).isEmpty();
        assertThat(rangeClosedBy('a', 'b', -1)).isEmpty();
        assertThat(rangeClosedBy('a', 'b', -3)).isEmpty();
    }

    @Test
    public void shouldCreateRangeClosedByWhereFromEqualsTo() {
        assertThat(rangeClosedBy('a', 'a', 1)).isEqualTo(of('a'));
        assertThat(rangeClosedBy('a', 'a', 3)).isEqualTo(of('a'));
        assertThat(rangeClosedBy('a', 'a', -1)).isEqualTo(of('a'));
        assertThat(rangeClosedBy('a', 'a', -3)).isEqualTo(of('a'));
    }

    @Test
    public void shouldCreateRangeClosedByWhereFromIsLessThanToAndStepCorrectDirection() {
        assertThat(rangeClosedBy('a', 'c', 1)).isEqualTo(of('a', 'b', 'c'));
        assertThat(rangeClosedBy('a', 'e', 2)).isEqualTo(of('a', 'c', 'e'));
        assertThat(rangeClosedBy('a', 'f', 2)).isEqualTo(of('a', 'c', 'e'));
        assertThat(rangeClosedBy((char) (Character.MAX_VALUE - 2), Character.MAX_VALUE, 3)).isEqualTo(of((char) (Character.MAX_VALUE - 2)));
        assertThat(rangeClosedBy((char) (Character.MAX_VALUE - 3), Character.MAX_VALUE, 3)).isEqualTo(of((char) (Character.MAX_VALUE - 3), Character.MAX_VALUE));
        assertThat(rangeClosedBy('c', 'a', -1)).isEqualTo(of('c', 'b', 'a'));
        assertThat(rangeClosedBy('e', 'a', -2)).isEqualTo(of('e', 'c', 'a'));
        assertThat(rangeClosedBy('e', (char) ('a' - 1), -2)).isEqualTo(of('e', 'c', 'a'));
        assertThat(rangeClosedBy((char) (Character.MIN_VALUE + 2), Character.MIN_VALUE, -3)).isEqualTo(of((char) (Character.MIN_VALUE + 2)));
        assertThat(rangeClosedBy((char) (Character.MIN_VALUE + 3), Character.MIN_VALUE, -3)).isEqualTo(of((char) (Character.MIN_VALUE + 3), Character.MIN_VALUE));
    }

    @Test
    public void shouldCreateRangeClosedByWhereFromAndToEqualMIN_VALUE() {
        assertThat(rangeClosedBy(Character.MIN_VALUE, Character.MIN_VALUE, 1)).isEqualTo(of(Character.MIN_VALUE));
        assertThat(rangeClosedBy(Character.MIN_VALUE, Character.MIN_VALUE, 3)).isEqualTo(of(Character.MIN_VALUE));
        assertThat(rangeClosedBy(Character.MIN_VALUE, Character.MIN_VALUE, -1)).isEqualTo(of(Character.MIN_VALUE));
        assertThat(rangeClosedBy(Character.MIN_VALUE, Character.MIN_VALUE, -3)).isEqualTo(of(Character.MIN_VALUE));
    }

    @Test
    public void shouldCreateRangeClosedByWhereFromAndToEqualMAX_VALUE() {
        assertThat(rangeClosedBy(Character.MAX_VALUE, Character.MAX_VALUE, 1)).isEqualTo(of(Character.MAX_VALUE));
        assertThat(rangeClosedBy(Character.MAX_VALUE, Character.MAX_VALUE, 3)).isEqualTo(of(Character.MAX_VALUE));
        assertThat(rangeClosedBy(Character.MAX_VALUE, Character.MAX_VALUE, -1)).isEqualTo(of(Character.MAX_VALUE));
        assertThat(rangeClosedBy(Character.MAX_VALUE, Character.MAX_VALUE, -3)).isEqualTo(of(Character.MAX_VALUE));
    }

    // -- static range()

    @Test
    public void shouldCreateRangeWhereFromIsGreaterThanTo() {
        assertThat(range('b', 'a').isEmpty());
    }

    @Test
    public void shouldCreateRangeWhereFromEqualsTo() {
        assertThat(range('a', 'a')).isEmpty();
    }

    @Test
    public void shouldCreateRangeWhereFromIsLessThanTo() {
        assertThat(range('a', 'c')).isEqualTo(of('a', 'b'));
    }

    @Test
    public void shouldCreateRangeWhereFromAndToEqualMIN_VALUE() {
        assertThat(range(Character.MIN_VALUE, Character.MIN_VALUE)).isEmpty();
    }

    @Test
    public void shouldCreateRangeWhereFromAndToEqualMAX_VALUE() {
        assertThat(range(Character.MAX_VALUE, Character.MAX_VALUE)).isEmpty();
    }

    // -- static rangeBy()

    @Test
    public void shouldCreateRangeByWhereFromIsGreaterThanToAndStepWrongDirection() {
        assertThat(rangeBy('b', 'a', 1)).isEmpty();
        assertThat(rangeBy('b', 'a', 3)).isEmpty();
        assertThat(rangeBy('a', 'b', -1)).isEmpty();
        assertThat(rangeBy('a', 'b', -3)).isEmpty();
    }

    @Test
    public void shouldCreateRangeByWhereFromEqualsTo() {
        assertThat(rangeBy('a', 'a', 1)).isEmpty();
        assertThat(rangeBy('a', 'a', 3)).isEmpty();
        assertThat(rangeBy('a', 'a', -1)).isEmpty();
        assertThat(rangeBy('a', 'a', -3)).isEmpty();
    }

    @Test
    public void shouldCreateRangeByWhereFromIsLessThanToAndStepCorrectDirection() {
        assertThat(rangeBy('a', 'c', 1)).isEqualTo(of('a', 'b'));
        assertThat(rangeBy('a', 'd', 2)).isEqualTo(of('a', 'c'));
        assertThat(rangeBy('c', 'a', -1)).isEqualTo(of('c', 'b'));
        assertThat(rangeBy('d', 'a', -2)).isEqualTo(of('d', 'b'));
        assertThat(rangeBy((char) (Character.MAX_VALUE - 3), Character.MAX_VALUE, 3)).isEqualTo(of((char) (Character.MAX_VALUE - 3)));
        assertThat(rangeBy((char) (Character.MAX_VALUE - 4), Character.MAX_VALUE, 3)).isEqualTo(of((char) (Character.MAX_VALUE - 4), (char) (Character.MAX_VALUE - 1)));
        assertThat(rangeBy((char) (Character.MIN_VALUE + 3), Character.MIN_VALUE, -3)).isEqualTo(of((char) (Character.MIN_VALUE + 3)));
        assertThat(rangeBy((char) (Character.MIN_VALUE + 4), Character.MIN_VALUE, -3)).isEqualTo(of((char) (Character.MIN_VALUE + 4), (char) (Character.MIN_VALUE + 1)));
    }

    @Test
    public void shouldCreateRangeByWhereFromAndToEqualMIN_VALUE() {
        assertThat(rangeBy(Character.MIN_VALUE, Character.MIN_VALUE, 1)).isEmpty();
        assertThat(rangeBy(Character.MIN_VALUE, Character.MIN_VALUE, 3)).isEmpty();
        assertThat(rangeBy(Character.MIN_VALUE, Character.MIN_VALUE, -1)).isEmpty();
        assertThat(rangeBy(Character.MIN_VALUE, Character.MIN_VALUE, -3)).isEmpty();
    }

    @Test
    public void shouldCreateRangeByWhereFromAndToEqualMAX_VALUE() {
        assertThat(rangeBy(Character.MAX_VALUE, Character.MAX_VALUE, 1)).isEmpty();
        assertThat(rangeBy(Character.MAX_VALUE, Character.MAX_VALUE, 3)).isEmpty();
        assertThat(rangeBy(Character.MAX_VALUE, Character.MAX_VALUE, -1)).isEmpty();
        assertThat(rangeBy(Character.MAX_VALUE, Character.MAX_VALUE, -3)).isEmpty();
    }

    // step == 0

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitCharRangeByStepZero() {
        rangeBy('a', 'b', 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitCharRangeClosedByStepZero() {
        rangeClosedBy('a', 'b', 0);
    }

    // -- average

    @Test
    public void shouldReturnNoneWhenComputingAverageOfNil() {
        assertThat(empty().average()).isEqualTo(Option.none());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenComputingAverageOfStrings() {
        of('a', 'b', 'c').average();
    }

    // -- contains

    @Test
    public void shouldRecognizeNilContainsNoElement() {
        final boolean actual = empty().contains((Character) null);
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesNotContainElement() {
        final boolean actual = of('1', '2', '3').contains('0');
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesContainElement() {
        final boolean actual = of('1', '2', '3').contains('2');
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldRecognizeNonNilDoesNotContainCharSequence() {
        final boolean actual = of('1', '2', '3').contains("13");
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesContainCharSequence() {
        final boolean actual = of('1', '2', '3').contains("23");
        assertThat(actual).isTrue();
    }

    // -- containsAll

    @Test
    public void shouldRecognizeNilNotContainsAllElements() {
        final boolean actual = empty().containsAll(of('1', '2', '3'));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilNotContainsAllOverlappingElements() {
        final boolean actual = of('1', '2', '3').containsAll(of('2', '3', '4'));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilContainsAllOnSelf() {
        final boolean actual = of('1', '2', '3').containsAll(of('1', '2', '3'));
        assertThat(actual).isTrue();
    }

    // -- distinct

    @Test
    public void shouldComputeDistinctOfEmptyTraversable() {
        assertThat(empty().distinct()).isSameAs(empty());
    }

    @Test
    public void shouldComputeDistinctOfNonEmptyTraversable() {
        assertThat(of('1', '1', '2', '2', '3', '3').distinct()).isEqualTo(of('1', '2', '3'));
    }

    // -- distinct(Comparator)

    @Test
    public void shouldComputeDistinctByOfEmptyTraversableUsingComparator() {
        final Comparator<Character> comparator = (i1, i2) -> i1 - i2;
        assertThat(empty().distinctBy(comparator)).isSameAs(empty());
    }

    @Test
    public void shouldComputeDistinctByOfNonEmptyTraversableUsingComparator() {
        final Comparator<Character> comparator = (s1, s2) -> (s1 - s2);
        assertThat(of('1', '2', '3', '3', '4', '5').distinctBy(comparator))
                .isEqualTo(of('1', '2', '3', '4', '5'));
    }

    // -- distinct(Function)

    @Test
    public void shouldComputeDistinctByOfEmptyTraversableUsingKeyExtractor() {
        assertThat(empty().distinctBy(Function.identity())).isSameAs(empty());
    }

    @Test
    public void shouldComputeDistinctByOfNonEmptyTraversableUsingKeyExtractor() {
        assertThat(of('1', '2', '3', '3', '4', '5').distinctBy(c -> c))
                .isEqualTo(of('1', '2', '3', '4', '5'));
    }

    // -- drop

    @Test
    public void shouldDropNoneOnNil() {
        assertThat(empty().drop(1)).isSameAs(empty());
    }

    @Test
    public void shouldDropNoneIfCountIsNegative() {
        final CharSeq t = of('1', '2', '3');
        assertThat(t.drop(-1)).isSameAs(t);
    }

    @Test
    public void shouldDropAsExpectedIfCountIsLessThanSize() {
        assertThat(of('1', '2', '3').drop(2)).isEqualTo(of('3'));
    }

    @Test
    public void shouldDropAllIfCountExceedsSize() {
        assertThat(of('1', '2', '3').drop('4')).isSameAs(empty());
    }

    // -- dropRight

    @Test
    public void shouldDropRightNoneOnNil() {
        assertThat(empty().dropRight(1)).isSameAs(empty());
    }

    @Test
    public void shouldDropRightNoneIfCountIsNegative() {
        final CharSeq t = of('1', '2', '3');
        assertThat(t.dropRight(-1)).isSameAs(t);
    }

    @Test
    public void shouldDropRightAsExpectedIfCountIsLessThanSize() {
        assertThat(of('1', '2', '3').dropRight(2)).isEqualTo(of('1'));
    }

    @Test
    public void shouldDropRightAllIfCountExceedsSize() {
        assertThat(of('1', '2', '3').dropRight(4)).isSameAs(empty());
    }

    // -- dropUntil

    @Test
    public void shouldDropUntilNoneOnNil() {
        assertThat(empty().dropUntil(ignored -> true)).isSameAs(empty());
    }

    @Test
    public void shouldDropUntilNoneIfPredicateIsTrue() {
        assertThat(of('1', '2', '3').dropUntil(ignored -> true)).isEqualTo(of('1', '2', '3'));
    }

    @Test
    public void shouldDropUntilAllIfPredicateIsFalse() {
        assertThat(of('1', '2', '3').dropUntil(ignored -> false)).isSameAs(empty());
    }

    @Test
    public void shouldDropUntilCorrect() {
        assertThat(of('1', '2', '3').dropUntil(i -> i >= '2')).isEqualTo(of('2', '3'));
    }

    // -- dropWhile

    @Test
    public void shouldDropWhileNoneOnNil() {
        assertThat(empty().dropWhile(ignored -> true)).isSameAs(empty());
    }

    @Test
    public void shouldDropWhileNoneIfPredicateIsFalse() {
        final CharSeq t = of('1', '2', '3');
        assertThat(t.dropWhile(ignored -> false)).isSameAs(t);
    }

    @Test
    public void shouldDropWhileAllIfPredicateIsTrue() {
        assertThat(of('1', '2', '3').dropWhile(ignored -> true)).isSameAs(empty());
    }

    @Test
    public void shouldDropWhileCorrect() {
        assertThat(of('1', '2', '3').dropWhile(i -> i == '1')).isEqualTo(of('2', '3'));
    }

    // -- existsUnique

    @Test
    public void shouldBeAwareOfExistingUniqueElement() {
        assertThat(of('1', '2').existsUnique(i -> i == '1')).isTrue();
    }

    @Test
    public void shouldBeAwareOfNonExistingUniqueElement() {
        assertThat(empty().existsUnique(i -> i == '1')).isFalse();
    }

    @Test
    public void shouldBeAwareOfExistingNonUniqueElement() {
        assertThat(of('1', '1', '2').existsUnique(i -> i == '1')).isFalse();
    }

    // -- filter

    @Test
    public void shouldFilterEmptyTraversable() {
        assertThat(empty().filter(ignored -> true)).isSameAs(empty());
    }

    @Test
    public void shouldFilterNonEmptyTraversable() {
        assertThat(of('1', '2', '3', '4').filter(i -> i == '2' || i == '4')).isEqualTo(of('2', '4'));
    }

    @Test
    public void shouldFilterNonEmptyTraversableAllMatch() {
        final CharSeq t = of('1', '2', '3', '4');
        assertThat(t.filter(i -> true)).isSameAs(t);
    }

    // -- find

    @Test
    public void shouldFindFirstOfNil() {
        assertThat(empty().find(ignored -> true)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFindFirstOfNonNil() {
        assertThat(of('1', '2', '3', '4').find(i -> i % 2 == 0)).isEqualTo(Option.of('2'));
    }

    // -- findLast

    @Test
    public void shouldFindLastOfNil() {
        assertThat(empty().findLast(ignored -> true)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFindLastOfNonNil() {
        assertThat(of('1', '2', '3', '4').findLast(i -> i % 2 == 0)).isEqualTo(Option.of('4'));
    }

    // -- flatMap

    @Test
    public void shouldFlatMapEmptyTraversable() {
        assertThat(empty().flatMap(CharSeq::of)).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldFlatMapNonEmptyTraversable() {
        assertThat(of('1', '2', '3').flatMap(CharSeq::of)).isEqualTo(Vector.of('1', '2', '3'));
    }

    @Test
    public void shouldFlatMapTraversableByExpandingElements() {
        assertThat(of('1', '2', '3').flatMap(i -> {
            if (i == '1') {
                return of('1', '2', '3');
            } else if (i == '2') {
                return of('4', '5');
            } else {
                return of('6');
            }
        })).isEqualTo(Vector.of('1', '2', '3', '4', '5', '6'));
    }

    @Test
    public void shouldFlatMapElementsToSequentialValuesInTheRightOrder() {
        final AtomicInteger seq = new AtomicInteger('0');
        final IndexedSeq<Character> actualInts = of('0', '1', '2').flatMap(
                ignored -> Vector.of((char) seq.getAndIncrement(), (char) seq.getAndIncrement()));
        final IndexedSeq<Character> expectedInts = Vector.of('0', '1', '2', '3', '4', '5');
        assertThat(actualInts).isEqualTo(expectedInts);
    }

    // -- flatMapChars()

    @Test
    public void sholdFlatMapChars() {
        assertThat(empty().flatMapChars(c -> "X")).isEqualTo(empty());
        assertThat(of('1', '2', '3').flatMapChars(c -> c == '1' ? "*" : "-")).isEqualTo(of("*--"));
    }

    // -- fold

    @Test
    public void shouldFoldNil() {
        assertThat(empty().fold('0', (a, b) -> b)).isEqualTo('0');
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenFoldNullOperator() {
        empty().fold(null, null);
    }

    @Test
    public void shouldFoldNonNil() {
        assertThat(of('1', '2', '3').fold('0', (a, b) -> b)).isEqualTo('3');
    }

    // -- foldLeft

    @Test
    public void shouldFoldLeftNil() {
        assertThat(empty().foldLeft("", (xs, x) -> xs + x)).isEqualTo("");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenFoldLeftNullOperator() {
        empty().foldLeft(null, null);
    }

    @Test
    public void shouldFoldLeftNonNil() {
        assertThat(of('a', 'b', 'c').foldLeft("", (xs, x) -> xs + x)).isEqualTo("abc");
    }

    // -- foldRight

    @Test
    public void shouldFoldRightNil() {
        assertThat(empty().foldRight("", (x, xs) -> x + xs)).isEqualTo("");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenFoldRightNullOperator() {
        empty().foldRight(null, null);
    }

    @Test
    public void shouldFoldRightNonNil() {
        assertThat(of('a', 'b', 'c').foldRight("", (x, xs) -> x + xs)).isEqualTo("abc");
    }

    // -- head

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowWhenHeadOnNil() {
        empty().head();
    }

    @Test
    public void shouldReturnHeadOfNonNil() {
        assertThat(of('1', '2', '3').head()).isEqualTo('1');
    }

    // -- headOption

    @Test
    public void shouldReturnNoneWhenCallingHeadOptionOnNil() {
        assertThat(empty().headOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeHeadWhenCallingHeadOptionOnNonNil() {
        assertThat(of('1', '2', '3').headOption()).isEqualTo(Option.some('1'));
    }

    // -- hasDefiniteSize

    @Test
    public void shouldReturnSomethingOnHasDefiniteSize() {
        assertThat(empty().hasDefiniteSize()).isTrue();
    }

    // -- groupBy

    @Test
    public void shouldNilGroupBy() {
        assertThat(empty().groupBy(Function.identity())).isEqualTo(LinkedHashMap.empty());
    }

    @Test
    public void shouldNonNilGroupByIdentity() {
        final Map<?, ?> actual = of('a', 'b', 'c').groupBy(Function.identity());
        final Map<?, ?> expected = LinkedHashMap.empty().put('a', of('a')).put('b', of('b')).put('c', of('c'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldNonNilGroupByEqual() {
        final Map<?, ?> actual = of('a', 'b', 'c').groupBy(c -> 1);
        final Map<?, ?> expected = LinkedHashMap.empty().put(1, of('a', 'b', 'c'));
        assertThat(actual).isEqualTo(expected);
    }

    // -- init

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenInitOfNil() {
        empty().init();
    }

    @Test
    public void shouldGetInitOfNonNil() {
        assertThat(of('1', '2', '3').init()).isEqualTo(of('1', '2'));
    }

    // -- initOption

    @Test
    public void shouldReturnNoneWhenCallingInitOptionOnNil() {
        assertThat(empty().initOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeInitWhenCallingInitOptionOnNonNil() {
        assertThat(of('1', '2', '3').initOption()).isEqualTo(Option.some(of('1', '2')));
    }

    // -- isEmpty

    @Test
    public void shouldRecognizeNil() {
        assertThat(empty().isEmpty()).isTrue();
    }

    @Test
    public void shouldRecognizeNonNil() {
        assertThat(of('1').isEmpty()).isFalse();
    }

    // -- iterator

    @Test
    public void shouldNotHasNextWhenNilIterator() {
        assertThat(empty().iterator().hasNext()).isFalse();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowOnNextWhenNilIterator() {
        empty().iterator().next();
    }

    @Test
    public void shouldIterateFirstElementOfNonNil() {
        assertThat(of('1', '2', '3').iterator().next()).isEqualTo('1');
    }

    @Test
    public void shouldFullyIterateNonNil() {
        final Iterator<Character> iterator = of('1', '2', '3').iterator();
        int actual;
        for (int i = 1; i <= 3; i++) {
            actual = iterator.next();
            assertThat(actual).isEqualTo('0' + i);
        }
        assertThat(iterator.hasNext()).isFalse();
    }

    // -- mkString()

    @Test
    public void shouldMkStringNil() {
        assertThat(empty().mkString()).isEqualTo("");
    }

    @Test
    public void shouldMkStringNonNil() {
        assertThat(of('a', 'b', 'c').mkString()).isEqualTo("abc");
    }

    // -- mkString(delimiter)

    @Test
    public void shouldMkStringWithDelimiterNil() {
        assertThat(empty().mkString(",")).isEqualTo("");
    }

    @Test
    public void shouldMkStringWithDelimiterNonNil() {
        assertThat(of('a', 'b', 'c').mkString(",")).isEqualTo("a,b,c");
    }

    // -- mkString(delimiter, prefix, suffix)

    @Test
    public void shouldMkStringWithDelimiterAndPrefixAndSuffixNil() {
        assertThat(empty().mkString("[", ",", "]")).isEqualTo("[]");
    }

    @Test
    public void shouldMkStringWithDelimiterAndPrefixAndSuffixNonNil() {
        assertThat(of('a', 'b', 'c').mkString("[", ",", "]")).isEqualTo("[a,b,c]");
    }

    // -- last

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowWhenLastOnNil() {
        empty().last();
    }

    @Test
    public void shouldReturnLastOfNonNil() {
        assertThat(of('1', '2', '3').last()).isEqualTo('3');
    }

    // -- lastOption

    @Test
    public void shouldReturnNoneWhenCallingLastOptionOnNil() {
        assertThat(empty().lastOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeLastWhenCallingLastOptionOnNonNil() {
        assertThat(of('1', '2', '3').lastOption()).isEqualTo(Option.some('3'));
    }

    // -- length

    @Test
    public void shouldComputeLengthOfNil() {
        assertThat(empty().length()).isEqualTo(0);
    }

    @Test
    public void shouldComputeLengthOfNonNil() {
        assertThat(of('1', '2', '3').length()).isEqualTo(3);
    }

    // -- map

    @Test
    public void shouldMapNil() {
        assertThat(empty().map(i -> i + 1)).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldMapNonNil() {
        assertThat(of('1', '2', '3').map(i -> (char) (i + 1))).isEqualTo(Vector.of('2', '3', '4'));
    }

    @Test
    public void shouldMapElementsToSequentialValuesInTheRightOrder() {
        final AtomicInteger seq = new AtomicInteger('0');
        final Vector<Character> expectedInts = Vector.of('0', '1', '2', '3', '4');
        final Vector<Character> actualInts = expectedInts.map(ignored -> (char) seq.getAndIncrement());
        assertThat(actualInts).isEqualTo(expectedInts);
    }

    @Test
    public void shouldMapToVectorWhenMapIsUsed() {
        assertThat(of('a', 'b', 'c').map(Integer::valueOf)).isInstanceOf(Vector.class);
    }

    @Test
    public void shouldMapToCharSeqWhenMapCharsIsUsed() {
        assertThat(empty().mapChars(c -> (char) (c + 1))).isEqualTo(empty());
        assertThat(of('a', 'b', 'c').mapChars(c -> (char) (c + 1))).isEqualTo(of("bcd"));
    }

    // -- partition

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenPartitionNilAndPredicateIsNull() {
        empty().partition(null);
    }

    @Test
    public void shouldPartitionNil() {
        assertThat(empty().partition(e -> true)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldPartitionIntsInOddAndEvenHavingOddAndEventNumbers() {
        assertThat(of('1', '2', '3', '4').partition(i -> i % 2 != 0))
                .isEqualTo(Tuple.of(of('1', '3'), of('2', '4')));
    }

    @Test
    public void shouldPartitionIntsInOddAndEvenHavingOnlyOddNumbers() {
        assertThat(of('1', '3').partition(i -> i % 2 != 0)).isEqualTo(Tuple.of(of('1', '3'), empty()));
    }

    @Test
    public void shouldPartitionIntsInOddAndEvenHavingOnlyEvenNumbers() {
        assertThat(of('2', '4').partition(i -> i % 2 != 0)).isEqualTo(Tuple.of(empty(), of('2', '4')));
    }

    // -- permutations

    @Test
    public void shouldComputePermutationsOfEmptyList() {
        assertThat(empty().permutations()).isEmpty();
    }

    @Test
    public void shouldComputePermutationsOfNonEmpty() {
        assertThat(of("123").permutations())
                .isEqualTo(Vector.of(of("123"), of("132"), of("213"), of("231"), of("312"), of("321")));
    }

    // -- max

    @Test
    public void shouldReturnNoneWhenComputingMaxOfNil() {
        assertThat(empty().max()).isEqualTo(Option.none());
    }

    @Test
    public void shouldComputeMaxOfChar() {
        assertThat(of('a', 'b', 'c').max()).isEqualTo(Option.some('c'));
    }

    // -- maxBy(Comparator)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMaxByWithNullComparator() {
        of('1').maxBy((Comparator<Character>) null);
    }

    @Test
    public void shouldThrowWhenMaxByOfNil() {
        assertThat(empty().maxBy((o1, o2) -> 0)).isEqualTo(Option.none());
    }

    @Test
    public void shouldCalculateMaxByOfInts() {
        assertThat(of('1', '2', '3').maxBy((i1, i2) -> i1 - i2)).isEqualTo(Option.some('3'));
    }

    @Test
    public void shouldCalculateInverseMaxByOfInts() {
        assertThat(of('1', '2', '3').maxBy((i1, i2) -> i2 - i1)).isEqualTo(Option.some('1'));
    }

    // -- maxBy(Function)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMaxByWithNullFunction() {
        of('1').maxBy((Function<Character, Character>) null);
    }

    @Test
    public void shouldThrowWhenMaxByFunctionOfNil() {
        assertThat(empty().maxBy(i -> i)).isEqualTo(Option.none());
    }

    @Test
    public void shouldCalculateMaxByFunctionOfInts() {
        assertThat(of('1', '2', '3').maxBy(i -> i)).isEqualTo(Option.some('3'));
    }

    @Test
    public void shouldCalculateInverseMaxByFunctionOfInts() {
        assertThat(of('1', '2', '3').maxBy(i -> -i)).isEqualTo(Option.some('1'));
    }

    // -- min

    @Test
    public void shouldReturnNoneWhenComputingMinOfNil() {
        assertThat(empty().min()).isEqualTo(Option.none());
    }

    @Test
    public void shouldComputeMinOfChar() {
        assertThat(of('a', 'b', 'c').min()).isEqualTo(Option.some('a'));
    }

    // -- minBy(Comparator)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMinByWithNullComparator() {
        of('1').minBy((Comparator<Character>) null);
    }

    @Test
    public void shouldThrowWhenMinByOfNil() {
        assertThat(empty().minBy((o1, o2) -> 0)).isEqualTo(Option.none());
    }

    @Test
    public void shouldCalculateMinByOfInts() {
        assertThat(of('1', '2', '3').minBy((i1, i2) -> i1 - i2)).isEqualTo(Option.some('1'));
    }

    @Test
    public void shouldCalculateInverseMinByOfInts() {
        assertThat(of('1', '2', '3').minBy((i1, i2) -> i2 - i1)).isEqualTo(Option.some('3'));
    }

    // -- minBy(Function)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMinByWithNullFunction() {
        of('1').minBy((Function<Character, Character>) null);
    }

    @Test
    public void shouldThrowWhenMinByFunctionOfNil() {
        assertThat(empty().minBy(i -> i)).isEqualTo(Option.none());
    }

    @Test
    public void shouldCalculateMinByFunctionOfInts() {
        assertThat(of('1', '2', '3').minBy(i -> i)).isEqualTo(Option.some('1'));
    }

    @Test
    public void shouldCalculateInverseMinByFunctionOfInts() {
        assertThat(of('1', '2', '3').minBy(i -> -i)).isEqualTo(Option.some('3'));
    }

    // -- peek

    @Test
    public void shouldPeekNonNilPerformingAnAction() {
        final char[] effect = { 0 };
        final CharSeq actual = of('1', '2', '3').peek(i -> effect[0] = i);
        assertThat(actual).isEqualTo(of('1', '2', '3')); // traverses all elements in the lazy case
        assertThat(effect[0]).isEqualTo('1');
    }

    // -- product

    @Test
    public void shouldComputeProductOfNil() {
        assertThat(empty().product()).isEqualTo(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenComputingProductOfStrings() {
        of('1', '2', '3').product();
    }

    // -- reduce

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowWhenReduceNil() {
        empty().reduce((a, b) -> a);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenReduceNullOperator() {
        empty().reduce(null);
    }

    @Test
    public void shouldReduceNonNil() {
        assertThat(of('1', '2', '3').reduce((a, b) -> b)).isEqualTo('3');
    }

    // -- reduceLeft

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowWhenReduceLeftNil() {
        empty().reduceLeft((a, b) -> a);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenReduceLeftNullOperator() {
        empty().reduceLeft(null);
    }

    @Test
    public void shouldReduceLeftNonNil() {
        assertThat(of('a', 'b', 'c').reduceLeft((xs, x) -> x)).isEqualTo('c');
    }

    // -- reduceRight

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowWhenReduceRightNil() {
        empty().reduceRight((a, b) -> a);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenReduceRightNullOperator() {
        empty().reduceRight(null);
    }

    @Test
    public void shouldReduceRightNonNil() {
        assertThat(of('a', 'b', 'c').reduceRight((x, xs) -> x)).isEqualTo('a');
    }

    // -- replace(curr, new)

    @Test
    public void shouldReplaceElementOfNilUsingCurrNew() {
        assertThat(empty().replace('1', '2')).isSameAs(empty());
    }

    @Test
    public void shouldReplaceElementOfNonNilUsingCurrNew() {
        assertThat(of('0', '1', '2', '1').replace('1', '3')).isEqualTo(of('0', '3', '2', '1'));
    }

    // -- replaceAll(curr, new)

    @Test
    public void shouldReplaceAllElementsOfNilUsingCurrNew() {
        assertThat(empty().replaceAll('1', '2')).isSameAs(empty());
    }

    @Test
    public void shouldReplaceAllElementsOfNonNilUsingCurrNew() {
        assertThat(of('0', '1', '2', '1').replaceAll('1', '3')).isEqualTo(of('0', '3', '2', '3'));
    }

    // -- retainAll

    @Test
    public void shouldRetainAllElementsFromNil() {
        assertThat(empty().retainAll(of('1', '2', '3'))).isSameAs(empty());
    }

    @Test
    public void shouldRetainAllExistingElementsFromNonNil() {
        assertThat(of('1', '2', '3', '1', '2', '3').retainAll(of('1', '2')))
                .isEqualTo(of('1', '2', '1', '2'));
    }

    @Test
    public void shouldNotRetainAllNonExistingElementsFromNonNil() {
        assertThat(of('1', '2', '3').retainAll(of('4', '5'))).isSameAs(empty());
    }

    // -- sliding(size)

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenSlidingNilByZeroSize() {
        empty().sliding(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenSlidingNilByNegativeSize() {
        empty().sliding(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenSlidingNonNilByZeroSize() {
        of('1').sliding(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenSlidingNonNilByNegativeSize() {
        of('1').sliding(-1);
    }

    @Test
    public void shouldSlideNilBySize() {
        assertThat(empty().sliding(1).isEmpty()).isTrue();
    }

    @Test
    public void shouldSlideNonNilBySize1() {
        assertThat(of('1', '2', '3').sliding(1).toList())
                .isEqualTo(List.of(of('1'), of('2'), of('3')));
    }

    @Test
    public void shouldSlideNonNilBySize2() {
        assertThat(of('1', '2', '3', '4', '5').sliding(2).toList())
                .isEqualTo(List.of(of('1', '2'), of('2', '3'), of('3', '4'), of('4', '5')));
    }

    // -- sliding(size, step)

    @Test
    public void shouldSlideNilBySizeAndStep() {
        assertThat(empty().sliding(1, 1).isEmpty()).isTrue();
    }

    @Test
    public void shouldSlide5ElementsBySize2AndStep3() {
        assertThat(of('1', '2', '3', '4', '5').sliding(2, 3).toList())
                .isEqualTo(List.of(of('1', '2'), of('4', '5')));
    }

    @Test
    public void shouldSlide5ElementsBySize2AndStep4() {
        assertThat(of('1', '2', '3', '4', '5').sliding(2, 4).toList())
                .isEqualTo(List.of(of('1', '2'), of('5')));
    }

    @Test
    public void shouldSlide5ElementsBySize2AndStep5() {
        assertThat(of('1', '2', '3', '4', '5').sliding(2, 5).toList()).isEqualTo(List.of(of('1', '2')));
    }

    @Test
    public void shouldSlide4ElementsBySize5AndStep3() {
        assertThat(of('1', '2', '3', '4').sliding(5, 3).toList())
                .isEqualTo(List.of(of('1', '2', '3', '4')));
    }

    // -- span

    @Test
    public void shouldSpanNil() {
        assertThat(empty().span(i -> i < 2)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSpanNonNil() {
        final CharSeq cs = of('0', '1', '2', '3');
        assertThat(cs.span(i -> i == '0' || i == '1'))
                .isEqualTo(Tuple.of(of('0', '1'), of('2', '3')));
        assertThat(cs.span(i -> false))
                .isEqualTo(Tuple.of(empty(), cs));
        assertThat(cs.span(i -> true))
                .isEqualTo(Tuple.of(cs, empty()));
    }

    // -- spliterator

    @Test
    public void shouldSplitNil() {
        final java.util.List<Character> actual = new java.util.ArrayList<>();
        empty().spliterator().forEachRemaining(actual::add);
        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldSplitNonNil() {
        final java.util.List<Character> actual = new java.util.ArrayList<>();
        of('1', '2', '3').spliterator().forEachRemaining(actual::add);
        assertThat(actual).isEqualTo(asList('1', '2', '3'));
    }

    @Test
    public void shouldHaveImmutableSpliterator() {
        assertThat(of('1', '2', '3').spliterator().characteristics() & Spliterator.IMMUTABLE).isNotZero();
    }

    @Test
    public void shouldHaveOrderedSpliterator() {
        assertThat(of('1', '2', '3').spliterator().characteristics() & Spliterator.ORDERED).isNotZero();
    }

    @Test
    public void shouldHaveSizedSpliterator() {
        assertThat(of('1', '2', '3').spliterator().characteristics() & Spliterator.SIZED).isNotZero();
    }

    @Test
    public void shouldReturnSizeWhenSpliterator() {
        assertThat(of('1', '2', '3').spliterator().getExactSizeIfKnown()).isEqualTo(3);
    }

    // -- startsWith

    @Test
    public void shouldStartsNilOfNilCalculate() {
        assertThat(empty().startsWith(empty())).isTrue();
    }

    @Test
    public void shouldStartsNilOfNonNilCalculate() {
        assertThat(empty().startsWith(of('a'))).isFalse();
    }

    @Test
    public void shouldStartsNilOfNilWithOffsetCalculate() {
        assertThat(empty().startsWith(empty(), 1)).isFalse();
    }

    @Test
    public void shouldStartsNilOfNonNilWithOffsetCalculate() {
        assertThat(empty().startsWith(of('a'), 1)).isFalse();
    }

    @Test
    public void shouldStartsNonNilOfNilCalculate() {
        assertThat(of('a', 'b', 'c').startsWith(empty())).isTrue();
    }

    @Test
    public void shouldStartsNonNilOfNonNilCalculate() {
        assertThat(of('a', 'b', 'c').startsWith(of('a', 'b'))).isTrue();
        assertThat(of('a', 'b', 'c').startsWith(of('a', 'b', 'c'))).isTrue();
        assertThat(of('a', 'b', 'c').startsWith(of('a', 'b', 'c', 'd'))).isFalse();
        assertThat(of('a', 'b', 'c').startsWith(of('a', 'c'))).isFalse();
    }

    @Test
    public void shouldStartsNonNilOfNilWithOffsetCalculate() {
        assertThat(of('a', 'b', 'c').startsWith(empty(), 1)).isTrue();
    }

    @Test
    public void shouldStartsNonNilOfNonNilWithOffsetCalculate() {
        assertThat(of('a', 'b', 'c').startsWith(of('b', 'c'), 1)).isTrue();
        assertThat(of('a', 'b', 'c').startsWith(of('b', 'c', 'd'), 1)).isFalse();
        assertThat(of('a', 'b', 'c').startsWith(of('b', 'd'), 1)).isFalse();
    }

    // -- stderr

    @Test
    public void shouldWriteToStderr() {
        of('1', '2', '3').stderr();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldHandleStderrIOException() {
        final PrintStream originalErr = System.err;
        try (PrintStream failingPrintStream = failingPrintStream()) {
            System.setErr(failingPrintStream);
            of('0').stderr();
        } finally {
            System.setErr(originalErr);
        }
    }

    // -- stdout

    @Test
    public void shouldWriteToStdout() {
        of('1', '2', '3').stdout();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldHandleStdoutIOException() {
        final PrintStream originalOut = System.out;
        try (PrintStream failingPrintStream = failingPrintStream()) {
            System.setOut(failingPrintStream);
            of('0').stdout();
        } finally {
            System.setOut(originalOut);
        }
    }

    // -- sum

    @Test
    public void shouldComputeSumOfNil() {
        assertThat(empty().sum()).isEqualTo(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenComputingSumOfStrings() {
        of('1', '2', '3').sum();
    }

    // -- take

    @Test
    public void shouldTakeNoneOnNil() {
        assertThat(empty().take(1)).isSameAs(empty());
    }

    @Test
    public void shouldTakeNoneIfCountIsNegative() {
        assertThat(of('1', '2', '3').take(-1)).isSameAs(empty());
    }

    @Test
    public void shouldTakeAsExpectedIfCountIsLessThanSize() {
        assertThat(of('1', '2', '3').take(2)).isEqualTo(of('1', '2'));
    }

    @Test
    public void shouldTakeAllIfCountExceedsSize() {
        final CharSeq t = of('1', '2', '3');
        assertThat(t.take(4)).isSameAs(t);
    }

    // -- takeRight

    @Test
    public void shouldTakeRightNoneOnNil() {
        assertThat(empty().takeRight(1)).isSameAs(empty());
    }

    @Test
    public void shouldTakeRightNoneIfCountIsNegative() {
        assertThat(of('1', '2', '3').takeRight(-1)).isSameAs(empty());
    }

    @Test
    public void shouldTakeRightAsExpectedIfCountIsLessThanSize() {
        assertThat(of('1', '2', '3').takeRight(2)).isEqualTo(of('2', '3'));
    }

    @Test
    public void shouldTakeRightAllIfCountExceedsSize() {
        final CharSeq t = of('1', '2', '3');
        assertThat(t.takeRight(4)).isSameAs(t);
    }

    // -- takeWhile

    @Test
    public void shouldTakeWhileNoneOnNil() {
        assertThat(empty().takeWhile(x -> true)).isSameAs(empty());
    }

    @Test
    public void shouldTakeWhileAllOnFalseCondition() {
        assertThat(of('1', '2', '3').takeWhile(x -> false)).isSameAs(empty());
    }

    @Test
    public void shouldTakeWhileAllOnTrueCondition() {
        final CharSeq t = of('1', '2', '3');
        assertThat(t.takeWhile(x -> true)).isSameAs(t);
    }

    @Test
    public void shouldTakeWhileAsExpected() {
        assertThat(of('2', '4', '5', '6').takeWhile(x -> x % 2 == 0)).isEqualTo(of('2', '4'));
    }

    // -- takeUntil

    @Test
    public void shouldTakeUntilNoneOnNil() {
        assertThat(empty().takeUntil(x -> true)).isSameAs(empty());
    }

    @Test
    public void shouldTakeUntilAllOnFalseCondition() {
        final CharSeq t = of('1', '2', '3');
        assertThat(t.takeUntil(x -> false)).isSameAs(t);
    }

    @Test
    public void shouldTakeUntilAllOnTrueCondition() {
        assertThat(of('1', '2', '3').takeUntil(x -> true)).isSameAs(empty());
    }

    @Test
    public void shouldTakeUntilAsExpected() {
        assertThat(of('2', '4', '5', '6').takeUntil(x -> x % 2 != 0)).isEqualTo(of('2', '4'));
    }

    // -- tail

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenTailOnNil() {
        empty().tail();
    }

    @Test
    public void shouldReturnTailOfNonNil() {
        assertThat(of('1', '2', '3').tail()).isEqualTo(of('2', '3'));
    }

    // -- tailOption

    @Test
    public void shouldReturnNoneWhenCallingTailOptionOnNil() {
        assertThat(empty().tailOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeTailWhenCallingTailOptionOnNonNil() {
        assertThat(of('1', '2', '3').tailOption()).isEqualTo(Option.some(of('2', '3')));
    }

    // -- toLowerCase

    @Test
    public void shouldConvertToLowerCase() {
        assertThat(of("JaVasLAng").toLowerCase()).isEqualTo(of("javaslang"));
        assertThat(of("JaVasLAng").toLowerCase(Locale.ENGLISH)).isEqualTo(of("javaslang"));
    }

    // -- toUpperCase

    @Test
    public void shouldConvertTotoUpperCase() {
        assertThat(of("JaVasLAng").toUpperCase()).isEqualTo(of("JAVASLANG"));
        assertThat(of("JaVasLAng").toUpperCase(Locale.ENGLISH)).isEqualTo(of("JAVASLANG"));
    }

    // -- capitalize

    @Test
    public void shouldCapitalize() {
        assertThat(of("javasLang").capitalize()).isEqualTo(of("JavasLang"));
        assertThat(of("").capitalize()).isEqualTo(of(""));
        assertThat(of("javasLang").capitalize(Locale.ENGLISH)).isEqualTo(of("JavasLang"));
        assertThat(of("").capitalize(Locale.ENGLISH)).isEqualTo(of(""));
    }

    // -- toJavaArray(Class)

    @Test
    public void shouldConvertNilToJavaArray() {
        final Character[] actual = empty().toJavaArray(Character.class);
        final Character[] expected = new Character[] {};
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConvertNonNilToJavaArray() {
        final Character[] array = of('1', '2').toJavaArray(Character.class);
        final Character[] expected = new Character[] { '1', '2' };
        assertThat(array).isEqualTo(expected);
    }

    // -- toJavaList

    @Test
    public void shouldConvertNilToArrayList() {
        assertThat(empty().toJavaList()).isEqualTo(new ArrayList<Integer>());
    }

    @Test
    public void shouldConvertNonNilToArrayList() {
        assertThat(of('1', '2', '3').toJavaList()).isEqualTo(asList('1', '2', '3'));
    }

    // -- toJavaMap(Function)

    @Test
    public void shouldConvertNilToHashMap() {
        assertThat(empty().toJavaMap(x -> Tuple.of(x, x))).isEqualTo(new java.util.HashMap<>());
    }

    @Test
    public void shouldConvertNonNilToHashMap() {
        final java.util.Map<Character, Character> expected = new java.util.HashMap<>();
        expected.put('1', '1');
        expected.put('2', '2');
        assertThat(of('1', '2').toJavaMap(x -> Tuple.of(x, x))).isEqualTo(expected);
    }

    // -- toJavaSet

    @Test
    public void shouldConvertNilToHashSet() {
        assertThat(empty().toJavaSet()).isEqualTo(new java.util.HashSet<>());
    }

    @Test
    public void shouldConvertNonNilToHashSet() {
        final java.util.Set<Character> expected = new java.util.HashSet<>();
        expected.add('2');
        expected.add('1');
        expected.add('3');
        assertThat(of('1', '2', '2', '3').toJavaSet()).isEqualTo(expected);
    }

    // -- stringPrefix

    @Test
    public void shouldReturnStringPrefix() {
        assertThat(of('1').stringPrefix()).isEqualTo("CharSeq");
    }

    // ++++++ OBJECT ++++++

    // -- equals

    @Test
    public void shouldEqualSameTraversableInstance() {
        final Traversable<?> traversable = empty();
        assertThat(traversable).isEqualTo(traversable);
    }

    @Test
    public void shouldNilNotEqualsNull() {
        assertThat(empty()).isNotNull();
    }

    @Test
    public void shouldNonNilNotEqualsNull() {
        assertThat(of('1')).isNotNull();
    }

    @Test
    public void shouldEmptyNotEqualsDifferentType() {
        assertThat(empty()).isNotEqualTo("");
    }

    @Test
    public void shouldNonEmptyNotEqualsDifferentType() {
        assertThat(of('1')).isNotEqualTo("");
    }

    @Test
    public void shouldRecognizeEqualityOfNils() {
        assertThat(empty()).isSameAs(empty());
    }

    @Test
    public void shouldRecognizeEqualityOfNonNils() {
        assertThat(of('1', '2', '3').equals(of('1', '2', '3'))).isTrue();
    }

    @Test
    public void shouldRecognizeContentEqualityOfNonNil() {
        assertThat(of('1', '2', '3').contentEquals(new StringBuffer().append("123"))).isTrue();
        assertThat(of('1', '2', '3').contentEquals("123")).isTrue();
    }

    @Test
    public void shouldRecognizeNonEqualityOfTraversablesOfSameSize() {
        assertThat(of('1', '2', '3').equals(of('1', '2', '4'))).isFalse();
    }

    @Test
    public void shouldRecognizeNonEqualityOfTraversablesOfDifferentSize() {
        assertThat(of('1', '2', '3').equals(of('1', '2'))).isFalse();
    }

    // -- hashCode

    @Test
    public void shouldCalculateHashCodeOfNil() {
        assertThat(empty().hashCode() == empty().hashCode()).isTrue();
    }

    @Test
    public void shouldCalculateHashCodeOfNonNil() {
        assertThat(of('1', '2').hashCode() == of('1', '2').hashCode()).isTrue();
    }

    @Test
    public void shouldCalculateDifferentHashCodesForDifferentTraversables() {
        assertThat(of('1', '2').hashCode() != of('2', '3').hashCode()).isTrue();
    }

    // -- Serializable interface

    @Test
    public void shouldSerializeDeserializeNil() {
        final Object actual = deserialize(serialize(empty()));
        final Object expected = empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        final boolean actual = deserialize(serialize(empty())) == empty();
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldSerializeDeserializeNonNil() {
        final Object actual = deserialize(serialize(of('1', '2', '3')));
        final Object expected = of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    // helpers

    static PrintStream failingPrintStream() {
        return new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }
        });
    }
    // -- append

    @Test
    public void shouldAppendElementToNil() {
        final CharSeq actual = empty().append('1');
        final CharSeq expected = of('1');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendElementToNonNil() {
        final CharSeq actual = of('1', '2').append('3');
        final CharSeq expected = of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    // -- appendAll

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnAppendAllOfNull() {
        empty().appendAll(null);
    }

    @Test
    public void shouldAppendAllNilToNil() {
        final CharSeq actual = empty().appendAll(empty());
        final CharSeq expected = empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNonNilToNil() {
        final CharSeq actual = empty().appendAll(of('1', '2', '3'));
        final CharSeq expected = of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNilToNonNil() {
        final CharSeq actual = of('1', '2', '3').appendAll(empty());
        final CharSeq expected = of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNonNilToNonNil() {
        final CharSeq actual = of('1', '2', '3').appendAll(of('4', '5', '6'));
        final CharSeq expected = of('1', '2', '3', '4', '5', '6');
        assertThat(actual).isEqualTo(expected);
    }

    // -- apply

    @Test
    public void shouldUseSeqAsPartialFunction() {
        assertThat(of('1', '2', '3').apply(1)).isEqualTo('2');
    }

    // -- combinations

    @Test
    public void shouldComputeCombinationsOfEmptyList() {
        assertThat(empty().combinations()).isEqualTo(Vector.of(empty()));
    }

    @Test
    public void shouldComputeCombinationsOfNonEmptyList() {
        assertThat(of("123").combinations()).isEqualTo(Vector.of(empty(), of("1"), of("2"), of("3"), of("12"), of("13"), of("23"), of("123")));
    }

    // -- combinations(k)

    @Test
    public void shouldComputeKCombinationsOfEmptyList() {
        assertThat(empty().combinations(1)).isEmpty();
    }

    @Test
    public void shouldComputeKCombinationsOfNonEmptyList() {
        assertThat(of("123").combinations(2)).isEqualTo(Vector.of(of("12"), of("13"), of("23")));
    }

    @Test
    public void shouldComputeKCombinationsOfNegativeK() {
        assertThat(of("1").combinations(-1)).isEqualTo(Vector.of(empty()));
    }

    // -- containsSlice

    @Test
    public void shouldRecognizeNilNotContainsSlice() {
        final boolean actual = empty().containsSlice(of('1', '2', '3'));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesContainSlice() {
        final boolean actual = of('1', '2', '3', '4', '5').containsSlice(of('2', '3'));
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldRecognizeNonNilDoesNotContainSlice() {
        final boolean actual = of('1', '2', '3', '4', '5').containsSlice(of('2', '1', '4'));
        assertThat(actual).isFalse();
    }

    // -- crossProduct()

    @Test
    public void shouldCalculateCrossProductOfNil() {
        final Iterator<Tuple2<Character, Character>> actual = empty().crossProduct();
        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldCalculateCrossProductOfNonNil() {
        final List<Tuple2<Character, Character>> actual = of('1', '2', '3').crossProduct().toList();
        final List<Tuple2<Character, Character>> expected = Iterator.of(Tuple.of('1', '1'), Tuple.of('1', '2'),
                Tuple.of('1', '3'), Tuple.of('2', '1'), Tuple.of('2', '2'), Tuple.of('2', '3'), Tuple.of('3', '1'),
                Tuple.of('3', '2'), Tuple.of('3', '3')).toList();
        assertThat(actual).isEqualTo(expected);
    }

    // -- crossProduct(int)

    @Test
    public void shouldCalculateCrossProductPower() {
        final List<CharSeq> actual = of("12").crossProduct(2).toList();
        final List<CharSeq> expected = Iterator.of(of('1', '1'), of('1', '2'), of('2', '1'), of('2', '2')).toList();
        assertThat(actual).isEqualTo(expected);
    }

    // -- crossProduct(Iterable)

    @Test
    public void shouldCalculateCrossProductOfNilAndNil() {
        final Traversable<Tuple2<Character, Object>> actual = empty().crossProduct(empty());
        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldCalculateCrossProductOfNilAndNonNil() {
        final Traversable<Tuple2<Character, Object>> actual = empty().crossProduct(of('1', '2', '3'));
        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldCalculateCrossProductOfNonNilAndNil() {
        final Traversable<Tuple2<Character, Character>> actual =
                of('1', '2', '3')
                        .crossProduct(empty());
        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldCalculateCrossProductOfNonNilAndNonNil() {
        final List<Tuple2<Character, Character>> actual =
                of('1', '2', '3')
                        .crossProduct(of('a', 'b'))
                        .toList();
        final List<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('1', 'b'),
                Tuple.of('2', 'a'), Tuple.of('2', 'b'), Tuple.of('3', 'a'), Tuple.of('3', 'b')).toList();
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
        of('1').get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetOnNil() {
        empty().get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetWithTooBigIndexOnNonNil() {
        of('1').get(1);
    }

    @Test
    public void shouldGetFirstElement() {
        assertThat(of('1', '2', '3').get(0)).isEqualTo('1');
    }

    @Test
    public void shouldGetLastElement() {
        assertThat(of('1', '2', '3').get(2)).isEqualTo('3');
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

    @Test
    public void shouldGroupedTraversableWithEqualSizedBlocks() {
        final List<CharSeq> actual = of('1', '2', '3', '4').grouped(2).toList();
        final List<CharSeq> expected = List.of(of('1', '2'), of('3', '4'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldGroupedTraversableWithRemainder() {
        final List<CharSeq> actual = of('1', '2', '3', '4', '5').grouped(2).toList();
        final List<CharSeq> expected = List.of(of('1', '2'), of('3', '4'), of('5'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldGroupedWhenTraversableLengthIsSmallerThanBlockSize() {
        final List<CharSeq> actual = of('1', '2', '3', '4').grouped(5).toList();
        final List<CharSeq> expected = List.of(of('1', '2', '3', '4'));
        assertThat(actual).isEqualTo(expected);
    }

    // -- indexOf

    @Test
    public void shouldNotFindIndexOfElementWhenSeqIsEmpty() {
        assertThat(empty().indexOf(1)).isEqualTo(-1);

        assertThat(empty().indexOfOption(1)).isEqualTo(Option.none());
    }

    @Test
    public void shouldNotFindIndexOfElementWhenStartIsGreater() {
        assertThat(of('1', '2', '3', '4').indexOf(2, 2)).isEqualTo(-1);

        assertThat(of('1', '2', '3', '4').indexOfOption(2, 2)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFindIndexOfFirstElement() {
        assertThat(of('1', '2', '3').indexOf('1')).isEqualTo(0);
        assertThat(of('1', '2', '3').indexOf(Character.valueOf('1'))).isEqualTo(0);

        assertThat(of('1', '2', '3').indexOfOption('1')).isEqualTo(Option.some(0));
        assertThat(of('1', '2', '3').indexOfOption(Character.valueOf('1'))).isEqualTo(Option.some(0));
    }

    @Test
    public void shouldFindIndexOfInnerElement() {
        assertThat(of('1', '2', '3').indexOf('2')).isEqualTo(1);
        assertThat(of('1', '2', '3').indexOf(Character.valueOf('2'))).isEqualTo(1);

        assertThat(of('1', '2', '3').indexOfOption('2')).isEqualTo(Option.some(1));
        assertThat(of('1', '2', '3').indexOfOption(Character.valueOf('2'))).isEqualTo(Option.some(1));
    }

    @Test
    public void shouldFindIndexOfLastElement() {
        assertThat(of('1', '2', '3').indexOf('3')).isEqualTo(2);
        assertThat(of('1', '2', '3').indexOf(Character.valueOf('3'))).isEqualTo(2);

        assertThat(of('1', '2', '3').indexOfOption('3')).isEqualTo(Option.some(2));
        assertThat(of('1', '2', '3').indexOfOption(Character.valueOf('3'))).isEqualTo(Option.some(2));
    }

    // -- indexOfSlice

    @Test
    public void shouldNotFindIndexOfSliceWhenSeqIsEmpty() {
        assertThat(empty().indexOfSlice(of('2', '3'))).isEqualTo(-1);

        assertThat(empty().indexOfSliceOption(of('2', '3'))).isEqualTo(Option.none());
    }

    @Test
    public void shouldNotFindIndexOfSliceWhenStartIsGreater() {
        assertThat(of('1', '2', '3', '4').indexOfSlice(of('2', '3'), 2)).isEqualTo(-1);

        assertThat(of('1', '2', '3', '4').indexOfSliceOption(of('2', '3'), 2)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFindIndexOfFirstSlice() {
        assertThat(of('1', '2', '3', '4').indexOfSlice(of('1', '2'))).isEqualTo(0);

        assertThat(of('1', '2', '3', '4').indexOfSliceOption(of('1', '2'))).isEqualTo(Option.some(0));
    }

    @Test
    public void shouldFindIndexOfInnerSlice() {
        assertThat(of('1', '2', '3', '4').indexOfSlice(of('2', '3'))).isEqualTo(1);

        assertThat(of('1', '2', '3', '4').indexOfSliceOption(of('2', '3'))).isEqualTo(Option.some(1));
    }

    @Test
    public void shouldFindIndexOfLastSlice() {
        assertThat(of('1', '2', '3').indexOfSlice(of('2', '3'))).isEqualTo(1);

        assertThat(of('1', '2', '3').indexOfSliceOption(of('2', '3'))).isEqualTo(Option.some(1));
    }

    // -- lastIndexOf

    @Test
    public void shouldNotFindLastIndexOfElementWhenSeqIsEmpty() {
        assertThat(empty().lastIndexOf(1)).isEqualTo(-1);

        assertThat(empty().lastIndexOfOption(1)).isEqualTo(Option.none());
    }

    @Test
    public void shouldNotFindLastIndexOfElementWhenEndIdLess() {
        assertThat(of('1', '2', '3', '4').lastIndexOf(3, 1)).isEqualTo(-1);

        assertThat(of('1', '2', '3', '4').lastIndexOfOption(3, 1)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFindLastIndexOfElement() {
        assertThat(of('1', '2', '3', '1', '2', '3').lastIndexOf('1')).isEqualTo(3);
        assertThat(of('1', '2', '3', '1', '2', '3').lastIndexOf(Character.valueOf('1'))).isEqualTo(3);

        assertThat(of('1', '2', '3', '1', '2', '3').lastIndexOfOption('1')).isEqualTo(Option.some(3));
        assertThat(of('1', '2', '3', '1', '2', '3').lastIndexOfOption(Character.valueOf('1'))).isEqualTo(Option.some(3));
    }

    @Test
    public void shouldFindLastIndexOfElementWithEnd() {
        assertThat(of('1', '2', '3', '1', '2', '3').lastIndexOf('1', 1)).isEqualTo(0);
        assertThat(of('1', '2', '3', '1', '2', '3').lastIndexOf(Character.valueOf('1'), 1)).isEqualTo(0);

        assertThat(of('1', '2', '3', '1', '2', '3').lastIndexOfOption('1', 1)).isEqualTo(Option.some(0));
        assertThat(of('1', '2', '3', '1', '2', '3').lastIndexOfOption(Character.valueOf('1'), 1)).isEqualTo(Option.some(0));
    }

    // -- lastIndexOfSlice

    @Test
    public void shouldNotFindLastIndexOfSliceWhenSeqIsEmpty() {
        assertThat(empty().lastIndexOfSlice(of('2', '3'))).isEqualTo(-1);

        assertThat(empty().lastIndexOfSliceOption(of('2', '3'))).isEqualTo(Option.none());
    }

    @Test
    public void shouldNotFindLastIndexOfSliceWhenEndIdLess() {
        assertThat(of('1', '2', '3', '4', '5').lastIndexOfSlice(of('3', '4'), 1)).isEqualTo(-1);

        assertThat(of('1', '2', '3', '4', '5').lastIndexOfSliceOption(of('3', '4'), 1)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFindLastIndexOfSlice() {
        assertThat(of('1', '2', '3', '1', '2').lastIndexOfSlice(empty())).isEqualTo(5);
        assertThat(of('1', '2', '3', '1', '2').lastIndexOfSlice(of('2'))).isEqualTo(4);
        assertThat(of('1', '2', '3', '1', '2', '3', '4').lastIndexOfSlice(of('2', '3'))).isEqualTo(4);

        assertThat(of('1', '2', '3', '1', '2').lastIndexOfSliceOption(empty())).isEqualTo(Option.some(5));
        assertThat(of('1', '2', '3', '1', '2').lastIndexOfSliceOption(of('2'))).isEqualTo(Option.some(4));
        assertThat(of('1', '2', '3', '1', '2', '3', '4').lastIndexOfSliceOption(of('2', '3'))).isEqualTo(Option.some(4));
    }

    @Test
    public void shouldFindLastIndexOfSliceWithEnd() {
        assertThat(of('1', '2', '3', '1', '2', '3').lastIndexOfSlice(empty(), 2)).isEqualTo(2);
        assertThat(of('1', '2', '3', '1', '2', '3').lastIndexOfSlice(of('2'), 2)).isEqualTo(1);
        assertThat(of('1', '2', '3', '1', '2', '3').lastIndexOfSlice(of('2', '3'), 2)).isEqualTo(1);
        assertThat(of('1', '2', '3', '1', '2', '3', '4').lastIndexOfSlice(of('2', '3'), 2)).isEqualTo(1);

        assertThat(of('1', '2', '3', '1', '2', '3').lastIndexOfSliceOption(empty(), 2)).isEqualTo(Option.some(2));
        assertThat(of('1', '2', '3', '1', '2', '3').lastIndexOfSliceOption(of('2'), 2)).isEqualTo(Option.some(1));
        assertThat(of('1', '2', '3', '1', '2', '3').lastIndexOfSliceOption(of('2', '3'), 2)).isEqualTo(Option.some(1));
        assertThat(of('1', '2', '3', '1', '2', '3', '4').lastIndexOfSliceOption(of('2', '3'), 2)).isEqualTo(Option.some(1));
    }

    // -- insert

    @Test
    public void shouldInsertIntoNil() {
        final CharSeq actual = empty().insert(0, '1');
        final CharSeq expected = of('1');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertInFrontOfElement() {
        final CharSeq actual = of('4').insert(0, '1');
        final CharSeq expected = of('1', '4');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertBehindOfElement() {
        final CharSeq actual = of('4').insert(1, '1');
        final CharSeq expected = of('4', '1');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertIntoSeq() {
        final CharSeq actual = of('1', '2', '3').insert(2, '4');
        final CharSeq expected = of('1', '2', '4', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenInsertOnNonNilWithNegativeIndex() {
        of('1').insert(-1, null);
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
        final CharSeq actual = empty().insertAll(0, of('1', '2', '3'));
        final CharSeq expected = of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllInFrontOfElement() {
        final CharSeq actual = of('4').insertAll(0, of('1', '2', '3'));
        final CharSeq expected = of('1', '2', '3', '4');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllBehindOfElement() {
        final CharSeq actual = of('4').insertAll(1, of('1', '2', '3'));
        final CharSeq expected = of('4', '1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllIntoSeq() {
        final CharSeq actual = of('1', '2', '3').insertAll(2, of('4', '5'));
        final CharSeq expected = of('1', '2', '4', '5', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnInsertAllWithNil() {
        empty().insertAll(0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenInsertOnNonNilAllWithNegativeIndex() {
        of('1').insertAll(-1, empty());
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
        assertThat(empty().intersperse(',')).isSameAs(empty());
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
        assertThat(of('1', '2', '3').iterator(1).next()).isEqualTo('2');
    }

    @Test
    public void shouldFullyIterateNonNilStartingAtIndex() {
        int actual = -1;
        for (final java.util.Iterator<Character> iter = of('1', '2', '3').iterator(1); iter.hasNext(); ) {
            actual = iter.next();
        }
        assertThat(actual).isEqualTo('3');
    }

    // -- prepend

    @Test
    public void shouldPrependElementToNil() {
        final CharSeq actual = empty().prepend('1');
        final CharSeq expected = of('1');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependElementToNonNil() {
        final CharSeq actual = of('2', '3').prepend('1');
        final CharSeq expected = of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    // -- prependAll

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnPrependAllOfNull() {
        empty().prependAll(null);
    }

    @Test
    public void shouldPrependAllNilToNil() {
        final CharSeq actual = empty().prependAll(empty());
        final CharSeq expected = empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNilToNonNil() {
        final CharSeq actual = of('1', '2', '3').prependAll(empty());
        final CharSeq expected = of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNonNilToNil() {
        final CharSeq actual = empty().prependAll(of('1', '2', '3'));
        final CharSeq expected = of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNonNilToNonNil() {
        final CharSeq actual = of('4', '5', '6').prependAll(of('1', '2', '3'));
        final CharSeq expected = of('1', '2', '3', '4', '5', '6');
        assertThat(actual).isEqualTo(expected);
    }

    // -- remove

    @Test
    public void shouldRemoveElementFromNil() {
        assertThat(empty().remove(null)).isSameAs(empty());
    }

    @Test
    public void shouldRemoveElementFromSingleton() {
        assertThat(of('1').remove('1')).isSameAs(empty());
    }

    @Test
    public void shouldRemoveFirstElement() {
        assertThat(of('1', '2', '3').remove('1')).isEqualTo(of('2', '3'));
    }

    @Test
    public void shouldRemoveLastElement() {
        assertThat(of('1', '2', '3').remove('3')).isEqualTo(of('1', '2'));
    }

    @Test
    public void shouldRemoveInnerElement() {
        assertThat(of('1', '2', '3').remove('2')).isEqualTo(of('1', '3'));
    }

    @Test
    public void shouldRemoveNonExistingElement() {
        final CharSeq t = of('1', '2', '3');
        assertThat(t.remove('4')).isSameAs(t);
    }

    // -- removeFirst(Predicate)

    @Test
    public void shouldRemoveFirstElementByPredicateFromNil() {
        assertThat(empty().removeFirst(v -> true)).isSameAs(empty());
    }

    @Test
    public void shouldRemoveFirstElementByPredicateFromSingleton() {
        assertThat(of('1').removeFirst(v -> v == '1')).isSameAs(empty());
    }

    @Test
    public void shouldRemoveFirstElementByPredicateBegin() {
        assertThat(of('1', '2', '3').removeFirst(v -> v == '1')).isEqualTo(of('2', '3'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateBeginM() {
        assertThat(of('1', '2', '1', '3').removeFirst(v -> v == '1')).isEqualTo(of('2', '1', '3'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateEnd() {
        assertThat(of('1', '2', '3').removeFirst(v -> v == '3')).isEqualTo(of('1', '2'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateInner() {
        assertThat(of('1', '2', '3', '4', '5').removeFirst(v -> v == '3'))
                .isEqualTo(of('1', '2', '4', '5'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateInnerM() {
        assertThat(of('1', '2', '3', '2', '5').removeFirst(v -> v == '2'))
                .isEqualTo(of('1', '3', '2', '5'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateNonExisting() {
        final CharSeq t = of('1', '2', '3');
        assertThat(t.removeFirst(v -> v == 4)).isSameAs(t);
    }

    // -- removeLast(Predicate)

    @Test
    public void shouldRemoveLastElementByPredicateFromNil() {
        assertThat(empty().removeLast(v -> true)).isSameAs(empty());
    }

    @Test
    public void shouldRemoveLastElementByPredicateFromSingleton() {
        assertThat(of('1').removeLast(v -> v == '1')).isSameAs(empty());
    }

    @Test
    public void shouldRemoveLastElementByPredicateBegin() {
        assertThat(of('1', '2', '3').removeLast(v -> v == '1')).isEqualTo(of('2', '3'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateEnd() {
        assertThat(of('1', '2', '3').removeLast(v -> v == '3')).isEqualTo(of('1', '2'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateEndM() {
        assertThat(of('1', '3', '2', '3').removeLast(v -> v == '3')).isEqualTo(of('1', '3', '2'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateInner() {
        assertThat(of('1', '2', '3', '4', '5').removeLast(v -> v == '3'))
                .isEqualTo(of('1', '2', '4', '5'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateInnerM() {
        assertThat(of('1', '2', '3', '2', '5').removeLast(v -> v == '2'))
                .isEqualTo(of('1', '2', '3', '5'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateNonExisting() {
        final CharSeq t = of('1', '2', '3');
        assertThat(t.removeLast(v -> v == 4)).isSameAs(t);
    }

    // -- removeAll(Iterable)

    @Test
    public void shouldRemoveAllElementsFromNil() {
        assertThat(empty().removeAll(of('1', '2', '3'))).isSameAs(empty());
    }

    @Test
    public void shouldRemoveAllExistingElementsFromNonNil() {
        assertThat(of('1', '2', '3', '1', '2', '3').removeAll(of('1', '2')))
                .isEqualTo(of('3', '3'));
    }

    @Test
    public void shouldNotRemoveAllNonExistingElementsFromNonNil() {
        final CharSeq t = of('1', '2', '3');
        assertThat(t.removeAll(of('4', '5'))).isSameAs(t);
    }

    // -- removeAll(Predicate)

    @Test
    public void shouldRemoveAllElementsByPredicateFromNil() {
        assertThat(empty().removeAll(Character::isDigit)).isSameAs(empty());
    }

    @Test
    public void shouldRemoveAllMatchedElementsFromNonNil() {
        assertThat(of('1', '2', '3', 'a', 'b', 'c').removeAll(Character::isDigit))
                .isEqualTo(of('a', 'b', 'c'));
    }

    @Test
    public void shouldNotRemoveAllNonMatchedElementsFromNonNil() {
        final CharSeq t = of('a', 'b', 'c');
        assertThat(t.removeAll(Character::isDigit)).isSameAs(t);
    }

    // -- removeAll(Object)

    @Test
    public void shouldRemoveAllObjectsFromNil() {
        assertThat(empty().removeAll('1')).isSameAs(empty());
    }

    @Test
    public void shouldRemoveAllExistingObjectsFromNonNil() {
        assertThat(of('1', '2', '3', '1', '2', '3').removeAll('1')).isEqualTo(of('2', '3', '2', '3'));
    }

    @Test
    public void shouldNotRemoveAllNonObjectsElementsFromNonNil() {
        final CharSeq t = of('1', '2', '3');
        assertThat(t.removeAll('4')).isSameAs(t);
    }

    // -- reverse

    @Test
    public void shouldReverseNil() {
        assertThat(empty().reverse()).isSameAs(empty());
    }

    @Test
    public void shouldReverseNonNil() {
        assertThat(of('1', '2', '3').reverse()).isEqualTo(of('3', '2', '1'));
    }

    // -- set

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithNegativeIndexOnNil() {
        empty().update(-1, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithNegativeIndexOnNonNil() {
        of('1').update(-1, '2');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetOnNil() {
        empty().update(0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithIndexExceedingByOneOnNonNil() {
        of('1').update(1, '2');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithIndexExceedingByTwoOnNonNil() {
        of('1').update(2, '2');
    }

    @Test
    public void shouldSetFirstElement() {
        assertThat(of('1', '2', '3').update(0, '4')).isEqualTo(of('4', '2', '3'));
    }

    @Test
    public void shouldSetLastElement() {
        assertThat(of('1', '2', '3').update(2, '4')).isEqualTo(of('1', '2', '4'));
    }

    // -- slice()

    @Test
    public void shouldSlice() {
        assertThat(empty().slice(0, 0)).isSameAs(empty());
        assertThat(of("123").slice(0, 0)).isSameAs(empty());
        assertThat(of("123").slice(1, 0)).isSameAs(empty());
        assertThat(of("123").slice(4, 5)).isSameAs(empty());
        assertThat(of("123").slice(0, 3)).isEqualTo(of("123"));
        assertThat(of("123").slice(-1, 2)).isEqualTo(of("12"));
        assertThat(of("123").slice(0, 2)).isEqualTo(of("12"));
        assertThat(of("123").slice(1, 2)).isEqualTo(of("2"));
        assertThat(of("123").slice(1, 3)).isEqualTo(of("23"));
        assertThat(of("123").slice(1, 4)).isEqualTo(of("23"));
    }

    // -- sorted()

    @Test
    public void shouldSortNil() {
        assertThat(empty().sorted()).isSameAs(empty());
    }

    @Test
    public void shouldSortNonNil() {
        assertThat(of('3', '4', '1', '2').sorted()).isEqualTo(of('1', '2', '3', '4'));
    }

    // -- sorted(Comparator)

    @Test
    public void shouldSortNilUsingComparator() {
        assertThat(empty().sorted((i, j) -> j - i)).isSameAs(empty());
    }

    @Test
    public void shouldSortNonNilUsingComparator() {
        assertThat(of('3', '4', '1', '2').sorted((i, j) -> j - i)).isEqualTo(of('4', '3', '2', '1'));
    }

    // -- sortBy()

    @Test
    public void shouldSortByFunction() {
        assertThat(of("123").sortBy(c -> -c)).isEqualTo(of("321"));
    }

    @Test
    public void shouldSortByComparator() {
        assertThat(of("123").sortBy((i, j) -> j - i, c -> c)).isEqualTo(of("321"));
    }

    // -- splitAt(index)

    @Test
    public void shouldSplitAtNil() {
        assertThat(empty().splitAt(1)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSplitAtNonNil() {
        assertThat(of('1', '2', '3').splitAt(1)).isEqualTo(Tuple.of(of('1'), of('2', '3')));
    }

    @Test
    public void shouldSplitAtBegin() {
        assertThat(of('1', '2', '3').splitAt(0)).isEqualTo(Tuple.of(empty(), of('1', '2', '3')));
    }

    @Test
    public void shouldSplitAtEnd() {
        assertThat(of('1', '2', '3').splitAt(3)).isEqualTo(Tuple.of(of('1', '2', '3'), empty()));
    }

    @Test
    public void shouldSplitAtOutOfBounds() {
        assertThat(of('1', '2', '3').splitAt(5)).isEqualTo(Tuple.of(of('1', '2', '3'), empty()));
        assertThat(of('1', '2', '3').splitAt(-1)).isEqualTo(Tuple.of(empty(), of('1', '2', '3')));
    }

    // -- splitAt(predicate)

    @Test
    public void shouldSplitPredicateAtNil() {
        assertThat(empty().splitAt(e -> true)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSplitPredicateAtNonNil() {
        assertThat(of('1', '2', '3').splitAt(e -> e == '2'))
                .isEqualTo(Tuple.of(of('1'), of('2', '3')));
    }

    @Test
    public void shouldSplitAtPredicateBegin() {
        assertThat(of('1', '2', '3').splitAt(e -> e == '1'))
                .isEqualTo(Tuple.of(empty(), of('1', '2', '3')));
    }

    @Test
    public void shouldSplitAtPredicateEnd() {
        assertThat(of('1', '2', '3').splitAt(e -> e == '3'))
                .isEqualTo(Tuple.of(of('1', '2'), of('3')));
    }

    @Test
    public void shouldSplitAtPredicateNotFound() {
        assertThat(of('1', '2', '3').splitAt(e -> e == '5'))
                .isEqualTo(Tuple.of(of('1', '2', '3'), empty()));
    }

    // -- splitAtInclusive(predicate)

    @Test
    public void shouldSplitInclusivePredicateAtNil() {
        assertThat(empty().splitAtInclusive(e -> true)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSplitInclusivePredicateAtNonNil() {
        assertThat(of('1', '2', '3').splitAtInclusive(e -> e == '2'))
                .isEqualTo(Tuple.of(of('1', '2'), of('3')));
    }

    @Test
    public void shouldSplitAtInclusivePredicateBegin() {
        assertThat(of('1', '2', '3').splitAtInclusive(e -> e == '1'))
                .isEqualTo(Tuple.of(of('1'), of('2', '3')));
    }

    @Test
    public void shouldSplitAtInclusivePredicateEnd() {
        assertThat(of('1', '2', '3').splitAtInclusive(e -> e == '3'))
                .isEqualTo(Tuple.of(of('1', '2', '3'), empty()));
    }

    @Test
    public void shouldSplitAtInclusivePredicateNotFound() {
        assertThat(of('1', '2', '3').splitAtInclusive(e -> e == '5'))
                .isEqualTo(Tuple.of(of('1', '2', '3'), empty()));
    }

    // -- removeAt(index)

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldRemoveIndexAtNil() {
        empty().removeAt(1);
    }

    @Test
    public void shouldRemoveIndexAtSingleton() {
        assertThat(of('1').removeAt(0)).isSameAs(empty());
    }

    @Test
    public void shouldRemoveIndexAtNonNil() {
        assertThat(of('1', '2', '3').removeAt(1)).isEqualTo(of('1', '3'));
    }

    @Test
    public void shouldRemoveIndexAtBegin() {
        assertThat(of('1', '2', '3').removeAt(0)).isEqualTo(of('2', '3'));
    }

    @Test
    public void shouldRemoveIndexAtEnd() {
        assertThat(of('1', '2', '3').removeAt(2)).isEqualTo(of('1', '2'));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldRemoveIndxOutOfBoundsLeft() {
        assertThat(of('1', '2', '3').removeAt(-1)).isEqualTo(of('1', '2', '3'));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldRemoveIndxOutOfBoundsRight() {
        assertThat(of('1', '2', '3').removeAt(5)).isEqualTo(of('1', '2', '3'));
    }

    // -- repeat

    @Test
    public void shouldRepeat() {
        assertThat(empty().repeat(0)).isEqualTo(empty());
        assertThat(empty().repeat(5)).isEqualTo(empty());
        assertThat(of("123").repeat(0)).isEqualTo(empty());
        assertThat(of("123").repeat(5)).isEqualTo(of("123123123123123"));
        assertThat(repeat('1', 0)).isEqualTo(empty());
        assertThat(repeat('!', 5)).isEqualTo(of("!!!!!"));
    }

    // -- transform()

    @Test
    public void shouldTransform() {
        final String transformed = of('0').transform(v -> String.valueOf(v.get()));
        assertThat(transformed).isEqualTo("0");
    }

    // -- scan, scanLeft, scanRight

    @Test
    public void shouldScan() {
        final CharSeq seq = of('1');
        final IndexedSeq<Character> result = seq.scan('0', (c1, c2) -> (char) (c1 + c2));
        assertThat(result).isEqualTo(Vector.of('0', 'a'));
    }

    @Test
    public void shouldScanLeft() {
        final CharSeq seq = of('1');
        final IndexedSeq<Character> result = seq.scanLeft('0', (c1, c2) -> (char) (c1 + c2));
        assertThat(result).isEqualTo(Vector.of('0', 'a'));
    }

    @Test
    public void shouldScanRight() {
        final CharSeq seq = of('1');
        final IndexedSeq<Character> result = seq.scanRight('0', (c1, c2) -> (char) (c1 + c2));
        assertThat(result).isEqualTo(Vector.of('a', '0'));
    }

    // -- subSequence(beginIndex)

    @Test
    public void shouldReturnNilWhenSubSequenceFrom0OnNil() {
        final CharSeq actual = empty().subSequence(0);
        assertThat(actual).isSameAs(empty());
    }

    @Test
    public void shouldReturnIdentityWhenSubSequenceFrom0OnNonNil() {
        final CharSeq actual = of('1').subSequence(0);
        assertThat(actual).isEqualTo(of('1'));
    }

    @Test
    public void shouldReturnNilWhenSubSequenceFrom1OnSeqOf1() {
        final CharSeq actual = of('1').subSequence(1);
        assertThat(actual).isSameAs(empty());
    }

    @Test
    public void shouldReturnSubSequenceWhenIndexIsWithinRange() {
        final CharSeq actual = of('1', '2', '3').subSequence(1);
        assertThat(actual).isEqualTo(of('2', '3'));
    }

    @Test
    public void shouldReturnNilWhenSubSequenceBeginningWithSize() {
        final CharSeq actual = of('1', '2', '3').subSequence(3);
        assertThat(actual).isSameAs(empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubSequenceOnNil() {
        empty().subSequence(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubSequenceWithOutOfLowerBound() {
        of('1', '2', '3').subSequence(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubSequenceWithOutOfUpperBound() {
        of('1', '2', '3').subSequence(4);
    }

    // -- subSequence(beginIndex, endIndex)

    @Test
    public void shouldReturnNilWhenSubSequenceFrom0To0OnNil() {
        final CharSeq actual = empty().subSequence(0, 0);
        assertThat(actual).isSameAs(empty());
    }

    @Test
    public void shouldReturnNilWhenSubSequenceFrom0To0OnNonNil() {
        final CharSeq actual = of('1').subSequence(0, 0);
        assertThat(actual).isSameAs(empty());
    }

    @Test
    public void shouldReturnSeqWithFirstElementWhenSubSequenceFrom0To1OnNonNil() {
        final CharSeq actual = of('1').subSequence(0, 1);
        assertThat(actual).isEqualTo(of('1'));
    }

    @Test
    public void shouldReturnNilWhenSubSequenceFrom1To1OnNonNil() {
        final CharSeq actual = of('1').subSequence(1, 1);
        assertThat(actual).isSameAs(empty());
    }

    @Test
    public void shouldReturnSubSequenceWhenIndicesAreWithinRange() {
        final CharSeq actual = of('1', '2', '3').subSequence(1, 3);
        assertThat(actual).isEqualTo(of('2', '3'));
    }

    @Test
    public void shouldReturnNilWhenIndicesBothAreUpperBound() {
        final CharSeq actual = of('1', '2', '3').subSequence(3, 3);
        assertThat(actual).isSameAs(empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubSequenceOnNonNilWhenBeginIndexIsGreaterThanEndIndex() {
        of('1', '2', '3').subSequence(1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubSequenceOnNilWhenBeginIndexIsGreaterThanEndIndex() {
        empty().subSequence(1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubSequenceOnNonNilWhenBeginIndexExceedsLowerBound() {
        of('1', '2', '3').subSequence(-'1', '2');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubSequenceOnNilWhenBeginIndexExceedsLowerBound() {
        empty().subSequence(-'1', '2');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubSequence2OnNil() {
        empty().subSequence(0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubSequenceWhenEndIndexExceedsUpperBound() {
        of('1', '2', '3').subSequence(1, 4).mkString(); // force computation of last element, e.g. because Stream is lazy
    }

    // -- unzip

    @Test
    public void shouldUnzipNil() {
        assertThat(empty().unzip(x -> Tuple.of(x, x))).isEqualTo(Tuple.of(Vector.empty(), Vector.empty()));
    }

    @Test
    public void shouldUnzipNonNil() {
        final Tuple actual = of('0', '1').unzip(i -> Tuple.of(i, i == '0' ? 'a' : 'b'));
        final Tuple expected = Tuple.of(Vector.of('0', '1'), Vector.of('a', 'b'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldUnzip3Nil() {
        assertThat(empty().unzip3(x -> Tuple.of(x, x, x))).isEqualTo(Tuple.of(Vector.empty(), Vector.empty(), Vector.empty()));
    }

    @Test
    public void shouldUnzip3NonNil() {
        final Tuple actual = of('0', '1').unzip3(i -> Tuple.of(i, i == '0' ? 'a' : 'b', i == '0' ? 'b' : 'a'));
        final Tuple expected = Tuple.of(Vector.of('0', '1'), Vector.of('a', 'b'), Vector.of('b', 'a'));
        assertThat(actual).isEqualTo(expected);
    }

    // -- zip

    @Test
    public void shouldZipNils() {
        final Seq<?> actual = empty().zip(empty());
        assertThat(actual).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldZipEmptyAndNonNil() {
        final Seq<?> actual = empty().zip(of('1'));
        assertThat(actual).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldZipNonEmptyAndNil() {
        final Seq<?> actual = of('1').zip(empty());
        assertThat(actual).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldZipNonNilsIfThisIsSmaller() {
        final IndexedSeq<Tuple2<Character, Character>> actual = of('1', '2').zip(of('a', 'b', 'c'));
        final IndexedSeq<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonNilsIfThatIsSmaller() {
        final IndexedSeq<Tuple2<Character, Character>> actual = of('1', '2', '3').zip(of('a', 'b'));
        final IndexedSeq<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonNilsOfSameSize() {
        final IndexedSeq<Tuple2<Character, Character>> actual = of('1', '2', '3').zip(of('a', 'b', 'c'));
        final IndexedSeq<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'), Tuple.of('3', 'c'));
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
        assertThat(actual).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldZipAllEmptyAndNonNil() {
        final IndexedSeq<?> actual = empty().zipAll(of('1'), null, null);
        final IndexedSeq<Tuple2<Object, Character>> expected = Vector.of(Tuple.of(null, '1'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonEmptyAndNil() {
        final IndexedSeq<?> actual = of('1').zipAll(empty(), null, null);
        final IndexedSeq<Tuple2<Character, Object>> expected = Vector.of(Tuple.of('1', null));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThisIsSmaller() {
        final IndexedSeq<Tuple2<Character, Character>> actual = of('1', '2').zipAll(of('a', 'b', 'c'), '9', 'z');
        final IndexedSeq<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'), Tuple.of('9', 'c'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThatIsSmaller() {
        final IndexedSeq<Tuple2<Character, Character>> actual = of('1', '2', '3').zipAll(of('a', 'b'), '9', 'z');
        final IndexedSeq<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'), Tuple.of('3', 'z'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsOfSameSize() {
        final IndexedSeq<Tuple2<Character, Character>> actual = of('1', '2', '3').zipAll(of('a', 'b', 'c'), '9', 'z');
        final IndexedSeq<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'), Tuple.of('3', 'c'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowIfZipAllWithThatIsNull() {
        empty().zipAll(null, null, null);
    }

    // -- zipWithIndex

    @Test
    public void shouldZipNilWithIndex() {
        assertThat(empty().zipWithIndex()).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldZipNonNilWithIndex() {
        final IndexedSeq<Tuple2<Character, Integer>> actual = of("abc").zipWithIndex();
        final IndexedSeq<Tuple2<Character, Integer>> expected = Vector.of(Tuple.of('a', 0), Tuple.of('b', 1), Tuple.of('c', 2));
        assertThat(actual).isEqualTo(expected);
    }

    // -- static collector()

    @Test
    public void shouldStreamAndCollectNil() {
        final Seq<?> actual = java.util.stream.Stream.<Character> empty().collect(collector());
        assertThat(actual).isSameAs(empty());
    }

    @Test
    public void shouldStreamAndCollectNonNil() {
        final Seq<?> actual = java.util.stream.Stream.of('1', '2', '3').collect(collector());
        assertThat(actual).isEqualTo(of('1', '2', '3'));
    }

    @Test
    public void shouldParallelStreamAndCollectNil() {
        final Seq<?> actual = java.util.stream.Stream.<Character> empty().parallel().collect(collector());
        assertThat(actual).isSameAs(empty());
    }

    @Test
    public void shouldParallelStreamAndCollectNonNil() {
        final Seq<?> actual = java.util.stream.Stream.of('1', '2', '3').parallel().collect(collector());
        assertThat(actual).isEqualTo(of('1', '2', '3'));
    }

    // -- static empty()

    @Test
    public void shouldCreateNil() {
        final Seq<?> actual = empty();
        assertThat(actual.length()).isEqualTo(0);
    }

    // -- static javaslang.String.of()

    @Test
    public void shouldWrapOtherCharSeq() {
        final CharSeq cs1 = of("123");
        final CharSeq cs2 = of(cs1);
        assertThat(cs1 == cs2).isTrue();
    }

    @Test
    public void shouldCreateSeqOfElements() {
        final CharSeq actual = of('1', '2');
        assertThat(actual.length()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualTo('1');
        assertThat(actual.get(1)).isEqualTo('2');
    }

    // -- static ofAll(Iterable)

    @Test
    public void shouldCreateListOfIterable() {
        final java.util.List<Character> arrayList = asList('1', '2');
        final CharSeq actual = ofAll(arrayList);
        assertThat(actual.length()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualTo('1');
        assertThat(actual.get(1)).isEqualTo('2');
    }

    @Test
    public void shouldTabulateTheCharSeq() {
        final Function<Number, Character> f = i -> i.toString().charAt(0);
        final CharSeq actual = tabulate(3, f);
        assertThat(actual).isEqualTo(of('0', '1', '2'));
    }

    @Test
    public void shouldTabulateTheCharSeqCallingTheFunctionInTheRightOrder() {
        final java.util.LinkedList<Character> chars = new java.util.LinkedList<>(asList('0', '1', '2'));
        final CharSeq actual = tabulate(3, i -> chars.remove());
        assertThat(actual).isEqualTo(of('0', '1', '2'));
    }

    @Test
    public void shouldTabulateTheCharSeqWith0Elements() {
        assertThat(tabulate(0, i -> 'a')).isEqualTo(empty());
    }

    @Test
    public void shouldTabulateTheCharSeqWith0ElementsWhenNIsNegative() {
        assertThat(tabulate(-1, i -> 'a')).isEqualTo(empty());
    }

    @Test
    public void shouldFillTheCharSeqCallingTheSupplierInTheRightOrder() {
        final java.util.LinkedList<Character> chars = new java.util.LinkedList<>(asList('0', '1'));
        final CharSeq actual = fill(2, () -> chars.remove());
        assertThat(actual).isEqualTo(of('0', '1'));
    }

    @Test
    public void shouldFillTheCharSeqWith0Elements() {
        assertThat(fill(0, () -> 'a')).isEqualTo(empty());
    }

    @Test
    public void shouldFillTheCharSeqWith0ElementsWhenNIsNegative() {
        assertThat(fill(-1, () -> 'a')).isEqualTo(empty());
    }

    @Test
    public void ofShouldReturnTheSingletonEmpty() {
        assertThat(of()).isSameAs(empty());
    }

    @Test
    public void ofAllShouldReturnTheSingletonEmpty() {
        assertThat(ofAll(Iterator.empty())).isSameAs(empty());
    }

    // -- unfold

    @Test
    public void shouldUnfoldRightToEmpty() {
        assertThat(CharSeq.unfoldRight(0, x -> Option.none())).isEqualTo(empty());
    }

    @Test
    public void shouldUnfoldRightSimpleCharSeq() {
        assertThat(
                CharSeq.unfoldRight('j', x -> x == 'a'
                                              ? Option.none()
                                              : Option.of(new Tuple2<>(new Character(x), (char) (x - 1)))))
                .isEqualTo(of("jihgfedcb"));
    }

    @Test
    public void shouldUnfoldLeftToEmpty() {
        assertThat(CharSeq.unfoldLeft(0, x -> Option.none())).isEqualTo(empty());
    }

    @Test
    public void shouldUnfoldLeftSimpleCharSeq() {
        assertThat(
                CharSeq.unfoldLeft('j', x -> x == 'a'
                                             ? Option.none()
                                             : Option.of(new Tuple2<>((char) (x - 1), new Character(x)))))
                .isEqualTo(of("bcdefghij"));
    }

    @Test
    public void shouldUnfoldToEmpty() {
        assertThat(CharSeq.unfold('j', x -> Option.none())).isEqualTo(empty());
    }

    @Test
    public void shouldUnfoldSimpleCharSeq() {
        assertThat(
                CharSeq.unfold('j', x -> x == 'a'
                                         ? Option.none()
                                         : Option.of(new Tuple2<>((char) (x - 1), new Character(x)))))
                .isEqualTo(of("bcdefghij"));
    }

    // -- number conversion

    // decode*

    @Test
    public void shouldDecodeByte() {
        assertThat(CharSeq.of("1").decodeByte()).isEqualTo(Byte.decode("1"));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotDecodeByteGivenEmptyCharSeq() {
        CharSeq.empty().decodeByte();
    }

    @Test
    public void shouldDecodeInteger() {
        assertThat(CharSeq.of("1").decodeInteger()).isEqualTo(Integer.decode("1"));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotDecodeIntegerGivenEmptyCharSeq() {
        CharSeq.empty().decodeInteger();
    }

    @Test
    public void shouldDecodeLong() {
        assertThat(CharSeq.of("1").decodeLong()).isEqualTo(Long.decode("1"));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotDecodeLongGivenEmptyCharSeq() {
        CharSeq.empty().decodeLong();
    }

    @Test
    public void shouldDecodeShort() {
        assertThat(CharSeq.of("1").decodeShort()).isEqualTo(Short.decode("1"));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotDecodeShortGivenEmptyCharSeq() {
        CharSeq.empty().decodeShort();
    }

    // parse*

    @Test
    public void shouldParseBooleanWhenTrue() {
        assertThat(CharSeq.of("true").parseBoolean()).isEqualTo(Boolean.parseBoolean("true"));
    }

    @Test
    public void shouldParseBooleanWhenFalse() {
        assertThat(CharSeq.of("false").parseBoolean()).isEqualTo(Boolean.parseBoolean("false"));
    }

    @Test
    public void shouldParseByte() {
        assertThat(CharSeq.of("1").parseByte()).isEqualTo((byte) 1);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseByteGivenEmptyCharSeq() {
        CharSeq.empty().parseByte();
    }

    @Test
    public void shouldParseByteUsingRadix() {
        assertThat(CharSeq.of("11").parseByte(2)).isEqualTo((byte) 3);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseByteUsingRadixGivenEmptyCharSeq() {
        CharSeq.empty().parseByte(2);
    }

    @Test
    public void shouldParseDouble() {
        assertThat(CharSeq.of("1.0").parseDouble()).isEqualTo(1.0d);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseDoubleGivenEmptyCharSeq() {
        CharSeq.empty().parseDouble();
    }

    @Test
    public void shouldParseFloat() {
        assertThat(CharSeq.of("1.0").parseFloat()).isEqualTo(1.0f);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseFloatGivenEmptyCharSeq() {
        CharSeq.empty().parseFloat();
    }

    @Test
    public void shouldParseInt() {
        assertThat(CharSeq.of("1").parseInt()).isEqualTo(1);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseIntGivenEmptyCharSeq() {
        CharSeq.empty().parseInt();
    }

    @Test
    public void shouldParseIntUsingRadix() {
        assertThat(CharSeq.of("11").parseInt(2)).isEqualTo(3);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseIntUsingRadixGivenEmptyCharSeq() {
        CharSeq.empty().parseInt(2);
    }

    @Test
    public void shouldParseUnsignedInt() {
        assertThat(CharSeq.of("+1").parseUnsignedInt()).isEqualTo(1);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseUnsignedIntGivenNegativeNumber() {
        CharSeq.of("-1").parseUnsignedInt();
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseUnsignedIntGivenEmptyCharSeq() {
        CharSeq.empty().parseUnsignedInt();
    }

    @Test
    public void shouldParseUnsignedIntUsingRadix() {
        assertThat(CharSeq.of("+11").parseUnsignedInt(2)).isEqualTo(3);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseUnsignedIntUsingRadixGivenNegativeNumber() {
        CharSeq.of("-1").parseUnsignedInt(2);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseUnsignedIntUsingRadixGivenEmptyCharSeq() {
        CharSeq.empty().parseUnsignedInt(2);
    }

    @Test
    public void shouldParseLong() {
        assertThat(CharSeq.of("1").parseLong()).isEqualTo(1L);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseLongGivenEmptyCharSeq() {
        CharSeq.empty().parseLong();
    }

    @Test
    public void shouldParseLongUsingRadix() {
        assertThat(CharSeq.of("11").parseLong(2)).isEqualTo(3L);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseLongUsingRadixGivenEmptyCharSeq() {
        CharSeq.empty().parseLong(2);
    }

    @Test
    public void shouldParseUnsignedLong() {
        assertThat(CharSeq.of("+1").parseUnsignedLong()).isEqualTo(1);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseUnsignedLongGivenNegativeNumber() {
        CharSeq.of("-1").parseUnsignedLong();
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseUnsignedLongGivenEmptyCharSeq() {
        CharSeq.empty().parseUnsignedLong();
    }

    @Test
    public void shouldParseUnsignedLongUsingRadix() {
        assertThat(CharSeq.of("+11").parseUnsignedLong(2)).isEqualTo(3);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseUnsignedLongUsingRadixGivenNegativeNumber() {
        CharSeq.of("-1").parseUnsignedLong(2);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseUnsignedLongUsingRadixGivenEmptyCharSeq() {
        CharSeq.empty().parseUnsignedLong(2);
    }

    @Test
    public void shouldParseShort() {
        assertThat(CharSeq.of("1").parseShort()).isEqualTo((short) 1);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseShortGivenEmptyCharSeq() {
        CharSeq.empty().parseShort();
    }

    @Test
    public void shouldParseShortUsingRadix() {
        assertThat(CharSeq.of("11").parseShort(2)).isEqualTo((short) 3);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotParseShortUsingRadixGivenEmptyCharSeq() {
        CharSeq.empty().parseShort(2);
    }

    // to*

    @Test
    public void shouldConvertToBooleanWhenTrue() {
        assertThat(CharSeq.of("true").toBoolean()).isEqualTo(Boolean.valueOf("true"));
    }

    @Test
    public void shouldConvertToBooleanWhenFalse() {
        assertThat(CharSeq.of("false").toBoolean()).isEqualTo(Boolean.valueOf("false"));
    }

    @Test
    public void shouldConvertToByte() {
        assertThat(CharSeq.of("1").toByte()).isEqualTo(Byte.valueOf("1"));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotConvertToByteGivenEmptyCharSeq() {
        CharSeq.empty().toByte();
    }

    @Test
    public void shouldConvertToByteUsingRadix() {
        assertThat(CharSeq.of("11").toByte(2)).isEqualTo(Byte.valueOf("11", 2));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotConvertToByteUsingRadixGivenEmptyCharSeq() {
        CharSeq.empty().toByte(2);
    }

    @Test
    public void shouldConvertToDouble() {
        assertThat(CharSeq.of("1.0").toDouble()).isEqualTo(Double.valueOf("1.0"));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotConvertToDoubleGivenEmptyCharSeq() {
        CharSeq.empty().toDouble();
    }

    @Test
    public void shouldConvertToFloat() {
        assertThat(CharSeq.of("1.0").toFloat()).isEqualTo(Float.valueOf("1.0"));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotConvertToFloatGivenEmptyCharSeq() {
        CharSeq.empty().toFloat();
    }

    @Test
    public void shouldConvertToInteger() {
        assertThat(CharSeq.of("1").toInteger()).isEqualTo(Integer.valueOf("1"));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotConvertToIntegerGivenEmptyCharSeq() {
        CharSeq.empty().toInteger();
    }

    @Test
    public void shouldConvertToIntegerUsingRadix() {
        assertThat(CharSeq.of("11").toInteger(2)).isEqualTo(Integer.valueOf("11", 2));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotConvertToIntegerUsingRadixGivenEmptyCharSeq() {
        CharSeq.empty().toInteger(2);
    }

    @Test
    public void shouldConvertToLong() {
        assertThat(CharSeq.of("1").toLong()).isEqualTo(Long.valueOf("1"));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotConvertToLongGivenEmptyCharSeq() {
        CharSeq.empty().toLong();
    }

    @Test
    public void shouldConvertToLongUsingRadix() {
        assertThat(CharSeq.of("11").toLong(2)).isEqualTo(Long.valueOf("11", 2));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotConvertToLongUsingRadixGivenEmptyCharSeq() {
        CharSeq.empty().toLong(2);
    }

    @Test
    public void shouldConvertToShort() {
        assertThat(CharSeq.of("1").toShort()).isEqualTo(Short.valueOf("1"));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotConvertToShortGivenEmptyCharSeq() {
        CharSeq.empty().toShort();
    }

    @Test
    public void shouldConvertToShortUsingRadix() {
        assertThat(CharSeq.of("11").toShort(2)).isEqualTo(Short.valueOf("11", 2));
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotConvertToShortUsingRadixGivenEmptyCharSeq() {
        CharSeq.empty().toShort(2);
    }
}
