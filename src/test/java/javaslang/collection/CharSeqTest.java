/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
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

import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;

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

    protected StringAssert assertThat(java.lang.String actual) {
        return new StringAssert(actual) {};
    }

    private CharSeq empty() {
        return CharSeq.empty();
    }

    // -- exists

    @Test
    public void shouldBeAwareOfExistingElement() {
        assertThat(CharSeq.of('1', '2').exists(i -> i == '2')).isTrue();
    }

    @Test
    public void shouldBeAwareOfNonExistingElement() {
        assertThat(empty().exists(i -> i == 1)).isFalse();
    }

    // -- forAll

    @Test
    public void shouldBeAwareOfPropertyThatHoldsForAll() {
        assertThat(CharSeq.of('2', '4').forAll(i -> i % 2 == 0)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsForAll() {
        assertThat(CharSeq.of('2', '3').forAll(i -> i % 2 == 0)).isFalse();
    }

    // -- padTo

    @Test
    public void shouldPadEmptyToEmpty() {
        assertThat(empty().padTo(0, 'a')).isSameAs(empty());
    }

    @Test
    public void shouldPadEmptyToNonEmpty() {
        assertThat(empty().padTo(2, 'a')).isEqualTo(CharSeq.of('a', 'a'));
    }

    @Test
    public void shouldPadNonEmptyZeroLen() {
        CharSeq seq = CharSeq.of('a');
        assertThat(seq.padTo(0, 'b')).isSameAs(seq);
    }

    @Test
    public void shouldPadNonEmpty() {
        assertThat(CharSeq.of('a').padTo(2, 'a')).isEqualTo(CharSeq.of('a', 'a'));
        assertThat(CharSeq.of('a').padTo(2, 'b')).isEqualTo(CharSeq.of('a', 'b'));
        assertThat(CharSeq.of('a').padTo(3, 'b')).isEqualTo(CharSeq.of('a', 'b', 'b'));
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
        Seq<Character> s = CharSeq.of('1', '2', '3');
        assertThat(empty().patch(0, s, 0)).isEqualTo(s);
        assertThat(empty().patch(-1, s, -1)).isEqualTo(s);
        assertThat(empty().patch(-1, s, 1)).isEqualTo(s);
        assertThat(empty().patch(1, s, -1)).isEqualTo(s);
        assertThat(empty().patch(1, s, 1)).isEqualTo(s);
    }

    @Test
    public void shouldPatchNonEmptyByEmpty() {
        Seq<Character> s = CharSeq.of('1', '2', '3');
        assertThat(s.patch(-1, empty(), -1)).isEqualTo(CharSeq.of('1', '2', '3'));
        assertThat(s.patch(-1, empty(), 0)).isEqualTo(CharSeq.of('1', '2', '3'));
        assertThat(s.patch(-1, empty(), 1)).isEqualTo(CharSeq.of('2', '3'));
        assertThat(s.patch(-1, empty(), 3)).isSameAs(empty());
        assertThat(s.patch(0, empty(), -1)).isEqualTo(CharSeq.of('1', '2', '3'));
        assertThat(s.patch(0, empty(), 0)).isEqualTo(CharSeq.of('1', '2', '3'));
        assertThat(s.patch(0, empty(), 1)).isEqualTo(CharSeq.of('2', '3'));
        assertThat(s.patch(0, empty(), 3)).isSameAs(empty());
        assertThat(s.patch(1, empty(), -1)).isEqualTo(CharSeq.of('1', '2', '3'));
        assertThat(s.patch(1, empty(), 0)).isEqualTo(CharSeq.of('1', '2', '3'));
        assertThat(s.patch(1, empty(), 1)).isEqualTo(CharSeq.of('1', '3'));
        assertThat(s.patch(1, empty(), 3)).isEqualTo(CharSeq.of('1'));
        assertThat(s.patch(4, empty(), -1)).isEqualTo(CharSeq.of('1', '2', '3'));
        assertThat(s.patch(4, empty(), 0)).isEqualTo(CharSeq.of('1', '2', '3'));
        assertThat(s.patch(4, empty(), 1)).isEqualTo(CharSeq.of('1', '2', '3'));
        assertThat(s.patch(4, empty(), 3)).isEqualTo(CharSeq.of('1', '2', '3'));
    }

    @Test
    public void shouldPatchNonEmptyByNonEmpty() {
        Seq<Character> s = CharSeq.of('1', '2', '3');
        Seq<Character> d = CharSeq.of('4', '5', '6');
        assertThat(s.patch(-1, d, -1)).isEqualTo(CharSeq.of('4', '5', '6', '1', '2', '3'));
        assertThat(s.patch(-1, d, 0)).isEqualTo(CharSeq.of('4', '5', '6', '1', '2', '3'));
        assertThat(s.patch(-1, d, 1)).isEqualTo(CharSeq.of('4', '5', '6', '2', '3'));
        assertThat(s.patch(-1, d, 3)).isEqualTo(CharSeq.of('4', '5', '6'));
        assertThat(s.patch(0, d, -1)).isEqualTo(CharSeq.of('4', '5', '6', '1', '2', '3'));
        assertThat(s.patch(0, d, 0)).isEqualTo(CharSeq.of('4', '5', '6', '1', '2', '3'));
        assertThat(s.patch(0, d, 1)).isEqualTo(CharSeq.of('4', '5', '6', '2', '3'));
        assertThat(s.patch(0, d, 3)).isEqualTo(CharSeq.of('4', '5', '6'));
        assertThat(s.patch(1, d, -1)).isEqualTo(CharSeq.of('1', '4', '5', '6', '2', '3'));
        assertThat(s.patch(1, d, 0)).isEqualTo(CharSeq.of('1', '4', '5', '6', '2', '3'));
        assertThat(s.patch(1, d, 1)).isEqualTo(CharSeq.of('1', '4', '5', '6', '3'));
        assertThat(s.patch(1, d, 3)).isEqualTo(CharSeq.of('1', '4', '5', '6'));
        assertThat(s.patch(4, d, -1)).isEqualTo(CharSeq.of('1', '2', '3', '4', '5', '6'));
        assertThat(s.patch(4, d, 0)).isEqualTo(CharSeq.of('1', '2', '3', '4', '5', '6'));
        assertThat(s.patch(4, d, 1)).isEqualTo(CharSeq.of('1', '2', '3', '4', '5', '6'));
        assertThat(s.patch(4, d, 3)).isEqualTo(CharSeq.of('1', '2', '3', '4', '5', '6'));
    }

    // -- peek

    @Test
    public void shouldPeekNil() {
        assertThat(empty().peek(t -> {
        })).isSameAs(empty());
    }

    @Test
    public void shouldPeekNonNilPerformingNoAction() {
        assertThat(CharSeq.of('1').peek(t -> {
        })).isEqualTo(CharSeq.of('1'));
    }

    @Test
    public void shouldPeekSingleValuePerformingAnAction() {
        final char[] effect = { 0 };
        final CharSeq actual = CharSeq.of('1').peek(i -> effect[0] = i);
        assertThat(actual).isEqualTo(CharSeq.of('1'));
        assertThat(effect[0]).isEqualTo('1');
    }

    // -- static rangeClosed()

    @Test
    public void shouldCreateRangeClosedWhereFromIsGreaterThanTo() {
        assertThat(CharSeq.rangeClosed('b', 'a')).isEmpty();
    }

    @Test
    public void shouldCreateRangeClosedWhereFromEqualsTo() {
        assertThat(CharSeq.rangeClosed('a', 'a')).isEqualTo(CharSeq.of('a'));
    }

    @Test
    public void shouldCreateRangeClosedWhereFromIsLessThanTo() {
        assertThat(CharSeq.rangeClosed('a', 'c')).isEqualTo(CharSeq.of('a', 'b', 'c'));
    }

    @Test
    public void shouldCreateRangeClosedWhereFromAndToEqualMIN_VALUE() {
        assertThat(CharSeq.rangeClosed(Character.MIN_VALUE, Character.MIN_VALUE)).isEqualTo(CharSeq.of(Character.MIN_VALUE));
    }

    @Test
    public void shouldCreateRangeClosedWhereFromAndToEqualMAX_VALUE() {
        assertThat(CharSeq.rangeClosed(Character.MAX_VALUE, Character.MAX_VALUE)).isEqualTo(CharSeq.of(Character.MAX_VALUE));
    }

    // -- static rangeClosedBy()

    @Test
    public void shouldCreateRangeClosedByWhereFromIsGreaterThanToAndStepWrongDirection() {
        assertThat(CharSeq.rangeClosedBy('b', 'a', 1)).isEmpty();
        assertThat(CharSeq.rangeClosedBy('b', 'a', 3)).isEmpty();
        assertThat(CharSeq.rangeClosedBy('a', 'b', -1)).isEmpty();
        assertThat(CharSeq.rangeClosedBy('a', 'b', -3)).isEmpty();
    }

    @Test
    public void shouldCreateRangeClosedByWhereFromEqualsTo() {
        assertThat(CharSeq.rangeClosedBy('a', 'a', 1)).isEqualTo(CharSeq.of('a'));
        assertThat(CharSeq.rangeClosedBy('a', 'a', 3)).isEqualTo(CharSeq.of('a'));
        assertThat(CharSeq.rangeClosedBy('a', 'a', -1)).isEqualTo(CharSeq.of('a'));
        assertThat(CharSeq.rangeClosedBy('a', 'a', -3)).isEqualTo(CharSeq.of('a'));
    }

    @Test
    public void shouldCreateRangeClosedByWhereFromIsLessThanToAndStepCorrectDirection() {
        assertThat(CharSeq.rangeClosedBy('a', 'c', 1)).isEqualTo(CharSeq.of('a', 'b', 'c'));
        assertThat(CharSeq.rangeClosedBy('a', 'e', 2)).isEqualTo(CharSeq.of('a', 'c', 'e'));
        assertThat(CharSeq.rangeClosedBy('a', 'f', 2)).isEqualTo(CharSeq.of('a', 'c', 'e'));
        assertThat(CharSeq.rangeClosedBy((char) (Character.MAX_VALUE - 2), Character.MAX_VALUE, 3)).isEqualTo(CharSeq.of((char) (Character.MAX_VALUE - 2)));
        assertThat(CharSeq.rangeClosedBy((char) (Character.MAX_VALUE - 3), Character.MAX_VALUE, 3)).isEqualTo(CharSeq.of((char) (Character.MAX_VALUE - 3), Character.MAX_VALUE));
        assertThat(CharSeq.rangeClosedBy('c', 'a', -1)).isEqualTo(CharSeq.of('c', 'b', 'a'));
        assertThat(CharSeq.rangeClosedBy('e', 'a', -2)).isEqualTo(CharSeq.of('e', 'c', 'a'));
        assertThat(CharSeq.rangeClosedBy('e', (char) ('a' - 1), -2)).isEqualTo(CharSeq.of('e', 'c', 'a'));
        assertThat(CharSeq.rangeClosedBy((char) (Character.MIN_VALUE + 2), Character.MIN_VALUE, -3)).isEqualTo(CharSeq.of((char) (Character.MIN_VALUE + 2)));
        assertThat(CharSeq.rangeClosedBy((char) (Character.MIN_VALUE + 3), Character.MIN_VALUE, -3)).isEqualTo(CharSeq.of((char) (Character.MIN_VALUE + 3), Character.MIN_VALUE));
    }

    @Test
    public void shouldCreateRangeClosedByWhereFromAndToEqualMIN_VALUE() {
        assertThat(CharSeq.rangeClosedBy(Character.MIN_VALUE, Character.MIN_VALUE, 1)).isEqualTo(CharSeq.of(Character.MIN_VALUE));
        assertThat(CharSeq.rangeClosedBy(Character.MIN_VALUE, Character.MIN_VALUE, 3)).isEqualTo(CharSeq.of(Character.MIN_VALUE));
        assertThat(CharSeq.rangeClosedBy(Character.MIN_VALUE, Character.MIN_VALUE, -1)).isEqualTo(CharSeq.of(Character.MIN_VALUE));
        assertThat(CharSeq.rangeClosedBy(Character.MIN_VALUE, Character.MIN_VALUE, -3)).isEqualTo(CharSeq.of(Character.MIN_VALUE));
    }

    @Test
    public void shouldCreateRangeClosedByWhereFromAndToEqualMAX_VALUE() {
        assertThat(CharSeq.rangeClosedBy(Character.MAX_VALUE, Character.MAX_VALUE, 1)).isEqualTo(CharSeq.of(Character.MAX_VALUE));
        assertThat(CharSeq.rangeClosedBy(Character.MAX_VALUE, Character.MAX_VALUE, 3)).isEqualTo(CharSeq.of(Character.MAX_VALUE));
        assertThat(CharSeq.rangeClosedBy(Character.MAX_VALUE, Character.MAX_VALUE, -1)).isEqualTo(CharSeq.of(Character.MAX_VALUE));
        assertThat(CharSeq.rangeClosedBy(Character.MAX_VALUE, Character.MAX_VALUE, -3)).isEqualTo(CharSeq.of(Character.MAX_VALUE));
    }

    // -- static range()

    @Test
    public void shouldCreateRangeWhereFromIsGreaterThanTo() {
        assertThat(CharSeq.range('b', 'a').isEmpty());
    }

    @Test
    public void shouldCreateRangeWhereFromEqualsTo() {
        assertThat(CharSeq.range('a', 'a')).isEmpty();
    }

    @Test
    public void shouldCreateRangeWhereFromIsLessThanTo() {
        assertThat(CharSeq.range('a', 'c')).isEqualTo(CharSeq.of('a', 'b'));
    }

    @Test
    public void shouldCreateRangeWhereFromAndToEqualMIN_VALUE() {
        assertThat(CharSeq.range(Character.MIN_VALUE, Character.MIN_VALUE)).isEmpty();
    }

    @Test
    public void shouldCreateRangeWhereFromAndToEqualMAX_VALUE() {
        assertThat(CharSeq.range(Character.MAX_VALUE, Character.MAX_VALUE)).isEmpty();
    }

    // -- static rangeBy()

    @Test
    public void shouldCreateRangeByWhereFromIsGreaterThanToAndStepWrongDirection() {
        assertThat(CharSeq.rangeBy('b', 'a', 1)).isEmpty();
        assertThat(CharSeq.rangeBy('b', 'a', 3)).isEmpty();
        assertThat(CharSeq.rangeBy('a', 'b', -1)).isEmpty();
        assertThat(CharSeq.rangeBy('a', 'b', -3)).isEmpty();
    }

    @Test
    public void shouldCreateRangeByWhereFromEqualsTo() {
        assertThat(CharSeq.rangeBy('a', 'a', 1)).isEmpty();
        assertThat(CharSeq.rangeBy('a', 'a', 3)).isEmpty();
        assertThat(CharSeq.rangeBy('a', 'a', -1)).isEmpty();
        assertThat(CharSeq.rangeBy('a', 'a', -3)).isEmpty();
    }

    @Test
    public void shouldCreateRangeByWhereFromIsLessThanToAndStepCorrectDirection() {
        assertThat(CharSeq.rangeBy('a', 'c', 1)).isEqualTo(CharSeq.of('a', 'b'));
        assertThat(CharSeq.rangeBy('a', 'd', 2)).isEqualTo(CharSeq.of('a', 'c'));
        assertThat(CharSeq.rangeBy('c', 'a', -1)).isEqualTo(CharSeq.of('c', 'b'));
        assertThat(CharSeq.rangeBy('d', 'a', -2)).isEqualTo(CharSeq.of('d', 'b'));
        assertThat(CharSeq.rangeBy((char) (Character.MAX_VALUE - 3), Character.MAX_VALUE, 3)).isEqualTo(CharSeq.of((char) (Character.MAX_VALUE - 3)));
        assertThat(CharSeq.rangeBy((char) (Character.MAX_VALUE - 4), Character.MAX_VALUE, 3)).isEqualTo(CharSeq.of((char) (Character.MAX_VALUE - 4), (char) (Character.MAX_VALUE - 1)));
        assertThat(CharSeq.rangeBy((char) (Character.MIN_VALUE + 3), Character.MIN_VALUE, -3)).isEqualTo(CharSeq.of((char) (Character.MIN_VALUE + 3)));
        assertThat(CharSeq.rangeBy((char) (Character.MIN_VALUE + 4), Character.MIN_VALUE, -3)).isEqualTo(CharSeq.of((char) (Character.MIN_VALUE + 4), (char) (Character.MIN_VALUE + 1)));
    }

    @Test
    public void shouldCreateRangeByWhereFromAndToEqualMIN_VALUE() {
        assertThat(CharSeq.rangeBy(Character.MIN_VALUE, Character.MIN_VALUE, 1)).isEmpty();
        assertThat(CharSeq.rangeBy(Character.MIN_VALUE, Character.MIN_VALUE, 3)).isEmpty();
        assertThat(CharSeq.rangeBy(Character.MIN_VALUE, Character.MIN_VALUE, -1)).isEmpty();
        assertThat(CharSeq.rangeBy(Character.MIN_VALUE, Character.MIN_VALUE, -3)).isEmpty();
    }

    @Test
    public void shouldCreateRangeByWhereFromAndToEqualMAX_VALUE() {
        assertThat(CharSeq.rangeBy(Character.MAX_VALUE, Character.MAX_VALUE, 1)).isEmpty();
        assertThat(CharSeq.rangeBy(Character.MAX_VALUE, Character.MAX_VALUE, 3)).isEmpty();
        assertThat(CharSeq.rangeBy(Character.MAX_VALUE, Character.MAX_VALUE, -1)).isEmpty();
        assertThat(CharSeq.rangeBy(Character.MAX_VALUE, Character.MAX_VALUE, -3)).isEmpty();
    }

    // step == 0

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitCharRangeByStepZero() {
        CharSeq.rangeBy('a', 'b', 0);
    }

    public void shouldProhibitCharRangeClosedByStepZero() {
        CharSeq.rangeClosedBy('a', 'b', 0);
    }

    // -- average

    @Test
    public void shouldReturnNoneWhenComputingAverageOfNil() {
        assertThat(empty().average()).isEqualTo(Option.none());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenComputingAverageOfStrings() {
        CharSeq.of('1', '2', '3').average();
    }

    // -- clear

    @Test

    public void shouldClearNil() {
        assertThat(empty().clear()).isSameAs(empty());
    }

    @Test
    public void shouldClearNonNil() {
        assertThat(CharSeq.of('1', '2', '3').clear()).isSameAs(empty());
    }

    // -- contains

    @Test
    public void shouldRecognizeNilContainsNoElement() {
        final boolean actual = empty().contains((Character) null);
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesNotContainElement() {
        final boolean actual = CharSeq.of('1', '2', '3').contains('0');
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesContainElement() {
        final boolean actual = CharSeq.of('1', '2', '3').contains('2');
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldRecognizeNonNilDoesNotContainCharSequence() {
        final boolean actual = CharSeq.of('1', '2', '3').contains("13");
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesContainCharSequence() {
        final boolean actual = CharSeq.of('1', '2', '3').contains("23");
        assertThat(actual).isTrue();
    }

    // -- containsAll

    @Test
    public void shouldRecognizeNilNotContainsAllElements() {
        final boolean actual = empty().containsAll(CharSeq.of('1', '2', '3'));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilNotContainsAllOverlappingElements() {
        final boolean actual = CharSeq.of('1', '2', '3').containsAll(CharSeq.of('2', '3', '4'));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilContainsAllOnSelf() {
        final boolean actual = CharSeq.of('1', '2', '3').containsAll(CharSeq.of('1', '2', '3'));
        assertThat(actual).isTrue();
    }

    // -- distinct

    @Test
    public void shouldComputeDistinctOfEmptyTraversable() {
        assertThat(empty().distinct()).isSameAs(empty());
    }

    @Test
    public void shouldComputeDistinctOfNonEmptyTraversable() {
        assertThat(CharSeq.of('1', '1', '2', '2', '3', '3').distinct()).isEqualTo(CharSeq.of('1', '2', '3'));
    }

    // -- distinct(Comparator)

    @Test
    public void shouldComputeDistinctByOfEmptyTraversableUsingComparator() {
        final Comparator<Character> comparator = (i1, i2) -> i1 - i2;
        assertThat(CharSeq.empty().distinctBy(comparator)).isSameAs(empty());
    }

    @Test
    public void shouldComputeDistinctByOfNonEmptyTraversableUsingComparator() {
        final Comparator<Character> comparator = (s1, s2) -> (s1 - s2);
        assertThat(CharSeq.of('1', '2', '3', '3', '4', '5').distinctBy(comparator))
                .isEqualTo(CharSeq.of('1', '2', '3', '4', '5'));
    }

    // -- distinct(Function)

    @Test
    public void shouldComputeDistinctByOfEmptyTraversableUsingKeyExtractor() {
        assertThat(empty().distinctBy(Function.identity())).isSameAs(empty());
    }

    @Test
    public void shouldComputeDistinctByOfNonEmptyTraversableUsingKeyExtractor() {
        assertThat(CharSeq.of('1', '2', '3', '3', '4', '5').distinctBy(c -> c))
                .isEqualTo(CharSeq.of('1', '2', '3', '4', '5'));
    }

    // -- drop

    @Test
    public void shouldDropNoneOnNil() {
        assertThat(empty().drop(1)).isSameAs(empty());
    }

    @Test
    public void shouldDropNoneIfCountIsNegative() {
        final CharSeq t = CharSeq.of('1', '2', '3');
        assertThat(t.drop(-1)).isSameAs(t);
    }

    @Test
    public void shouldDropAsExpectedIfCountIsLessThanSize() {
        assertThat(CharSeq.of('1', '2', '3').drop(2)).isEqualTo(CharSeq.of('3'));
    }

    @Test
    public void shouldDropAllIfCountExceedsSize() {
        assertThat(CharSeq.of('1', '2', '3').drop('4')).isSameAs(empty());
    }

    // -- dropRight

    @Test
    public void shouldDropRightNoneOnNil() {
        assertThat(empty().dropRight(1)).isSameAs(empty());
    }

    @Test
    public void shouldDropRightNoneIfCountIsNegative() {
        final CharSeq t = CharSeq.of('1', '2', '3');
        assertThat(t.dropRight(-1)).isSameAs(t);
    }

    @Test
    public void shouldDropRightAsExpectedIfCountIsLessThanSize() {
        assertThat(CharSeq.of('1', '2', '3').dropRight(2)).isEqualTo(CharSeq.of('1'));
    }

    @Test
    public void shouldDropRightAllIfCountExceedsSize() {
        assertThat(CharSeq.of('1', '2', '3').dropRight(4)).isSameAs(empty());
    }

    // -- dropUntil

    @Test
    public void shouldDropUntilNoneOnNil() {
        assertThat(empty().dropUntil(ignored -> true)).isSameAs(empty());
    }

    @Test
    public void shouldDropUntilNoneIfPredicateIsTrue() {
        assertThat(CharSeq.of('1', '2', '3').dropUntil(ignored -> true)).isEqualTo(CharSeq.of('1', '2', '3'));
    }

    @Test
    public void shouldDropUntilAllIfPredicateIsFalse() {
        assertThat(CharSeq.of('1', '2', '3').dropUntil(ignored -> false)).isSameAs(empty());
    }

    @Test
    public void shouldDropUntilCorrect() {
        assertThat(CharSeq.of('1', '2', '3').dropUntil(i -> i >= '2')).isEqualTo(CharSeq.of('2', '3'));
    }

    // -- dropWhile

    @Test
    public void shouldDropWhileNoneOnNil() {
        assertThat(empty().dropWhile(ignored -> true)).isSameAs(empty());
    }

    @Test
    public void shouldDropWhileNoneIfPredicateIsFalse() {
        CharSeq t = CharSeq.of('1', '2', '3');
        assertThat(t.dropWhile(ignored -> false)).isSameAs(t);
    }

    @Test
    public void shouldDropWhileAllIfPredicateIsTrue() {
        assertThat(CharSeq.of('1', '2', '3').dropWhile(ignored -> true)).isSameAs(empty());
    }

    @Test
    public void shouldDropWhileCorrect() {
        assertThat(CharSeq.of('1', '2', '3').dropWhile(i -> i == '1')).isEqualTo(CharSeq.of('2', '3'));
    }

    // -- existsUnique

    @Test
    public void shouldBeAwareOfExistingUniqueElement() {
        assertThat(CharSeq.of('1', '2').existsUnique(i -> i == '1')).isTrue();
    }

    @Test
    public void shouldBeAwareOfNonExistingUniqueElement() {
        assertThat(CharSeq.empty().existsUnique(i -> i == '1')).isFalse();
    }

    @Test
    public void shouldBeAwareOfExistingNonUniqueElement() {
        assertThat(CharSeq.of('1', '1', '2').existsUnique(i -> i == '1')).isFalse();
    }

    // -- filter

    @Test
    public void shouldFilterEmptyTraversable() {
        assertThat(empty().filter(ignored -> true)).isSameAs(empty());
    }

    @Test
    public void shouldFilterNonEmptyTraversable() {
        assertThat(CharSeq.of('1', '2', '3', '4').filter(i -> i == '2' || i == '4')).isEqualTo(CharSeq.of('2', '4'));
    }

    @Test
    public void shouldFilterNonEmptyTraversableAllMatch() {
        final CharSeq t = CharSeq.of('1', '2', '3', '4');
        assertThat(t.filter(i -> true)).isSameAs(t);
    }

    // -- filterNot

    @Test
    public void shouldFilterNotEmptyTraversable() {
        assertThat(empty().filterNot(ignored -> true)).isSameAs(empty());
    }

    @Test
    public void shouldFilterNotNonEmptyTraversable() {
        assertThat(CharSeq.of('1', '2', '3', '4').filterNot(i -> i == '2' || i == '4')).isEqualTo(CharSeq.of('1', '3'));
    }

    @Test
    public void shouldFilterNotNonEmptyTraversableAllMatch() {
        final CharSeq t = CharSeq.of('1', '2', '3', '4');
        assertThat(t.filterNot(i -> false)).isSameAs(t);
    }

    // -- findFirst

    @Test
    public void shouldFindFirstOfNil() {
        assertThat(empty().findFirst(ignored -> true)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFindFirstOfNonNil() {
        assertThat(CharSeq.of('1', '2', '3', '4').findFirst(i -> i % 2 == 0)).isEqualTo(Option.of('2'));
    }

    // -- findLast

    @Test
    public void shouldFindLastOfNil() {
        assertThat(empty().findLast(ignored -> true)).isEqualTo(Option.none());
    }

    @Test
    public void shouldFindLastOfNonNil() {
        assertThat(CharSeq.of('1', '2', '3', '4').findLast(i -> i % 2 == 0)).isEqualTo(Option.of('4'));
    }

    // -- flatMap

    @Test
    public void shouldFlatMapEmptyTraversable() {
        assertThat(empty().flatMap(CharSeq::of)).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldFlatMapNonEmptyTraversable() {
        assertThat(CharSeq.of('1', '2', '3').flatMap(CharSeq::of)).isEqualTo(Vector.of('1', '2', '3'));
    }

    @Test
    public void shouldFlatMapTraversableByExpandingElements() {
        assertThat(CharSeq.of('1', '2', '3').flatMap(i -> {
            if (i == '1') {
                return CharSeq.of('1', '2', '3');
            } else if (i == '2') {
                return CharSeq.of('4', '5');
            } else {
                return CharSeq.of('6');
            }
        })).isEqualTo(Vector.of('1', '2', '3', '4', '5', '6'));
    }

    @Test
    public void shouldFlatMapElementsToSequentialValuesInTheRightOrder() {
        final AtomicInteger seq = new AtomicInteger('0');
        final IndexedSeq<Character> actualInts = CharSeq.of('0', '1', '2').flatMap(
                ignored -> Vector.of((char) seq.getAndIncrement(), (char) seq.getAndIncrement()));
        final IndexedSeq<Character> expectedInts = Vector.of('0', '1', '2', '3', '4', '5');
        assertThat(actualInts).isEqualTo(expectedInts);
    }

    // -- flatMapChars()

    @Test
    public void sholdFlatMapChars() {
        assertThat(CharSeq.empty().flatMapChars(c -> "X")).isEqualTo(CharSeq.empty());
        assertThat(CharSeq.of('1', '2', '3').flatMapChars(c -> c == '1' ? "*" : "-")).isEqualTo(CharSeq.of("*--"));
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
        assertThat(CharSeq.of('1', '2', '3').fold('0', (a, b) -> b)).isEqualTo('3');
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
        assertThat(CharSeq.of('a', 'b', 'c').foldLeft("", (xs, x) -> xs + x)).isEqualTo("abc");
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
        assertThat(CharSeq.of('a', 'b', 'c').foldRight("", (x, xs) -> x + xs)).isEqualTo("abc");
    }

    // -- head

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowWhenHeadOnNil() {
        empty().head();
    }

    @Test
    public void shouldReturnHeadOfNonNil() {
        assertThat(CharSeq.of('1', '2', '3').head()).isEqualTo('1');
    }

    // -- headOption

    @Test
    public void shouldReturnNoneWhenCallingHeadOptionOnNil() {
        assertThat(empty().headOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeHeadWhenCallingHeadOptionOnNonNil() {
        assertThat(CharSeq.of('1', '2', '3').headOption()).isEqualTo(Option.some('1'));
    }

    // -- hasDefiniteSize

    @Test
    public void shouldReturnSomethingOnHasDefiniteSize() {
        assertThat(empty().hasDefiniteSize()).isTrue();
    }

    // -- groupBy

    @Test
    public void shouldNilGroupBy() {
        assertThat(CharSeq.empty().groupBy(Function.identity())).isEqualTo(HashMap.empty());
    }

    @Test
    public void shouldNonNilGroupByIdentity() {
        Map<?, ?> actual = CharSeq.of('a', 'b', 'c').groupBy(Function.identity());
        Map<?, ?> expected = HashMap.empty().put('a', CharSeq.of('a')).put('b', CharSeq.of('b')).put('c', CharSeq.of('c'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldNonNilGroupByEqual() {
        Map<?, ?> actual = CharSeq.of('a', 'b', 'c').groupBy(c -> 1);
        Map<?, ?> expected = HashMap.empty().put(1, CharSeq.of('a', 'b', 'c'));
        assertThat(actual).isEqualTo(expected);
    }

    // -- init

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenInitOfNil() {
        empty().init();
    }

    @Test
    public void shouldGetInitOfNonNil() {
        assertThat(CharSeq.of('1', '2', '3').init()).isEqualTo(CharSeq.of('1', '2'));
    }

    // -- initOption

    @Test
    public void shouldReturnNoneWhenCallingInitOptionOnNil() {
        assertThat(empty().initOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeInitWhenCallingInitOptionOnNonNil() {
        assertThat(CharSeq.of('1', '2', '3').initOption()).isEqualTo(Option.some(CharSeq.of('1', '2')));
    }

    // -- isEmpty

    @Test
    public void shouldRecognizeNil() {
        assertThat(empty().isEmpty()).isTrue();
    }

    @Test
    public void shouldRecognizeNonNil() {
        assertThat(CharSeq.of('1').isEmpty()).isFalse();
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
        assertThat(CharSeq.of('1', '2', '3').iterator().next()).isEqualTo('1');
    }

    @Test
    public void shouldFullyIterateNonNil() {
        final Iterator<Character> iterator = CharSeq.of('1', '2', '3').iterator();
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
        assertThat(CharSeq.of('a', 'b', 'c').mkString()).isEqualTo("abc");
    }

    // -- mkString(delimiter)

    @Test
    public void shouldMkStringWithDelimiterNil() {
        assertThat(empty().mkString(",")).isEqualTo("");
    }

    @Test
    public void shouldMkStringWithDelimiterNonNil() {
        assertThat(CharSeq.of('a', 'b', 'c').mkString(",")).isEqualTo("a,b,c");
    }

    // -- mkString(delimiter, prefix, suffix)

    @Test
    public void shouldMkStringWithDelimiterAndPrefixAndSuffixNil() {
        assertThat(empty().mkString("[", ",", "]")).isEqualTo("[]");
    }

    @Test
    public void shouldMkStringWithDelimiterAndPrefixAndSuffixNonNil() {
        assertThat(CharSeq.of('a', 'b', 'c').mkString("[", ",", "]")).isEqualTo("[a,b,c]");
    }

    // -- last

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowWhenLastOnNil() {
        empty().last();
    }

    @Test
    public void shouldReturnLastOfNonNil() {
        assertThat(CharSeq.of('1', '2', '3').last()).isEqualTo('3');
    }

    // -- lastOption

    @Test
    public void shouldReturnNoneWhenCallingLastOptionOnNil() {
        assertThat(empty().lastOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeLastWhenCallingLastOptionOnNonNil() {
        assertThat(CharSeq.of('1', '2', '3').lastOption()).isEqualTo(Option.some('3'));
    }

    // -- length

    @Test
    public void shouldComputeLengthOfNil() {
        assertThat(empty().length()).isEqualTo(0);
    }

    @Test
    public void shouldComputeLengthOfNonNil() {
        assertThat(CharSeq.of('1', '2', '3').length()).isEqualTo(3);
    }

    // -- map

    @Test
    public void shouldMapNil() {
        assertThat(empty().map(i -> i + 1)).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldMapNonNil() {
        assertThat(CharSeq.of('1', '2', '3').map(i -> (char) (i + 1))).isEqualTo(Vector.of('2', '3', '4'));
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
        assertThat(CharSeq.of('a', 'b', 'c').map(Integer::valueOf)).isInstanceOf(Vector.class);
    }

    @Test
    public void shouldMapToCharSeqWhenMapCharsIsUsed() {
        assertThat(CharSeq.empty().mapChars(c -> (char) (c + 1))).isEqualTo(CharSeq.empty());
        assertThat(CharSeq.of('a', 'b', 'c').mapChars(c -> (char) (c + 1))).isEqualTo(CharSeq.of("bcd"));
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
        assertThat(CharSeq.of('1', '2', '3', '4').partition(i -> i % 2 != 0))
                .isEqualTo(Tuple.of(CharSeq.of('1', '3'), CharSeq.of('2', '4')));
    }

    @Test
    public void shouldPartitionIntsInOddAndEvenHavingOnlyOddNumbers() {
        assertThat(CharSeq.of('1', '3').partition(i -> i % 2 != 0)).isEqualTo(Tuple.of(CharSeq.of('1', '3'), empty()));
    }

    @Test
    public void shouldPartitionIntsInOddAndEvenHavingOnlyEvenNumbers() {
        assertThat(CharSeq.of('2', '4').partition(i -> i % 2 != 0)).isEqualTo(Tuple.of(empty(), CharSeq.of('2', '4')));
    }

    // -- permutations

    @Test
    public void shouldComputePermutationsOfEmptyList() {
        assertThat(empty().permutations()).isEmpty();
    }

    @Test
    public void shouldComputePermutationsOfNonEmpty() {
        assertThat(CharSeq.of("123").permutations())
                .isEqualTo(Vector.of(CharSeq.of("123"), CharSeq.of("132"), CharSeq.of("213"), CharSeq.of("231"), CharSeq.of("312"), CharSeq.of("321")));
    }

    // -- max

    @Test
    public void shouldReturnNoneWhenComputingMaxOfNil() {
        assertThat(empty().max()).isEqualTo(Option.none());
    }

    @Test
    public void shouldComputeMaxOfChar() {
        assertThat(CharSeq.of('a', 'b', 'c').max()).isEqualTo(Option.some('c'));
    }

    // -- maxBy(Comparator)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMaxByWithNullComparator() {
        CharSeq.of('1').maxBy((Comparator<Character>) null);
    }

    @Test
    public void shouldThrowWhenMaxByOfNil() {
        assertThat(empty().maxBy((o1, o2) -> 0)).isEqualTo(Option.none());
    }

    @Test
    public void shouldCalculateMaxByOfInts() {
        assertThat(CharSeq.of('1', '2', '3').maxBy((i1, i2) -> i1 - i2)).isEqualTo(Option.some('3'));
    }

    @Test
    public void shouldCalculateInverseMaxByOfInts() {
        assertThat(CharSeq.of('1', '2', '3').maxBy((i1, i2) -> i2 - i1)).isEqualTo(Option.some('1'));
    }

    // -- maxBy(Function)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMaxByWithNullFunction() {
        CharSeq.of('1').maxBy((Function<Character, Character>) null);
    }

    @Test
    public void shouldThrowWhenMaxByFunctionOfNil() {
        assertThat(empty().maxBy(i -> i)).isEqualTo(Option.none());
    }

    @Test
    public void shouldCalculateMaxByFunctionOfInts() {
        assertThat(CharSeq.of('1', '2', '3').maxBy(i -> i)).isEqualTo(Option.some('3'));
    }

    @Test
    public void shouldCalculateInverseMaxByFunctionOfInts() {
        assertThat(CharSeq.of('1', '2', '3').maxBy(i -> -i)).isEqualTo(Option.some('1'));
    }

    // -- min

    @Test
    public void shouldReturnNoneWhenComputingMinOfNil() {
        assertThat(empty().min()).isEqualTo(Option.none());
    }

    @Test
    public void shouldComputeMinOfChar() {
        assertThat(CharSeq.of('a', 'b', 'c').min()).isEqualTo(Option.some('a'));
    }

    // -- minBy(Comparator)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMinByWithNullComparator() {
        CharSeq.of('1').minBy((Comparator<Character>) null);
    }

    @Test
    public void shouldThrowWhenMinByOfNil() {
        assertThat(empty().minBy((o1, o2) -> 0)).isEqualTo(Option.none());
    }

    @Test
    public void shouldCalculateMinByOfInts() {
        assertThat(CharSeq.of('1', '2', '3').minBy((i1, i2) -> i1 - i2)).isEqualTo(Option.some('1'));
    }

    @Test
    public void shouldCalculateInverseMinByOfInts() {
        assertThat(CharSeq.of('1', '2', '3').minBy((i1, i2) -> i2 - i1)).isEqualTo(Option.some('3'));
    }

    // -- minBy(Function)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMinByWithNullFunction() {
        CharSeq.of('1').minBy((Function<Character, Character>) null);
    }

    @Test
    public void shouldThrowWhenMinByFunctionOfNil() {
        assertThat(empty().minBy(i -> i)).isEqualTo(Option.none());
    }

    @Test
    public void shouldCalculateMinByFunctionOfInts() {
        assertThat(CharSeq.of('1', '2', '3').minBy(i -> i)).isEqualTo(Option.some('1'));
    }

    @Test
    public void shouldCalculateInverseMinByFunctionOfInts() {
        assertThat(CharSeq.of('1', '2', '3').minBy(i -> -i)).isEqualTo(Option.some('3'));
    }

    // -- peek

    @Test
    public void shouldPeekNonNilPerformingAnAction() {
        final char[] effect = { 0 };
        final CharSeq actual = CharSeq.of('1', '2', '3').peek(i -> effect[0] = i);
        assertThat(actual).isEqualTo(CharSeq.of('1', '2', '3')); // traverses all elements in the lazy case
        assertThat(effect[0]).isEqualTo('1');
    }

    // -- product

    @Test
    public void shouldComputeProductOfNil() {
        assertThat(empty().product()).isEqualTo(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenComputingProductOfStrings() {
        CharSeq.of('1', '2', '3').product();
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
        assertThat(CharSeq.of('1', '2', '3').reduce((a, b) -> b)).isEqualTo('3');
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
        assertThat(CharSeq.of('a', 'b', 'c').reduceLeft((xs, x) -> x)).isEqualTo('c');
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
        assertThat(CharSeq.of('a', 'b', 'c').reduceRight((x, xs) -> x)).isEqualTo('a');
    }

    // -- replace(curr, new)

    @Test
    public void shouldReplaceElementOfNilUsingCurrNew() {
        assertThat(empty().replace('1', '2')).isSameAs(empty());
    }

    @Test
    public void shouldReplaceElementOfNonNilUsingCurrNew() {
        assertThat(CharSeq.of('0', '1', '2', '1').replace('1', '3')).isEqualTo(CharSeq.of('0', '3', '2', '1'));
    }

    // -- replaceAll(curr, new)

    @Test
    public void shouldReplaceAllElementsOfNilUsingCurrNew() {
        assertThat(empty().replaceAll('1', '2')).isSameAs(empty());
    }

    @Test
    public void shouldReplaceAllElementsOfNonNilUsingCurrNew() {
        assertThat(CharSeq.of('0', '1', '2', '1').replaceAll('1', '3')).isEqualTo(CharSeq.of('0', '3', '2', '3'));
    }

    // -- retainAll

    @Test
    public void shouldRetainAllElementsFromNil() {
        assertThat(empty().retainAll(CharSeq.of('1', '2', '3'))).isSameAs(empty());
    }

    @Test
    public void shouldRetainAllExistingElementsFromNonNil() {
        assertThat(CharSeq.of('1', '2', '3', '1', '2', '3').retainAll(CharSeq.of('1', '2')))
                .isEqualTo(CharSeq.of('1', '2', '1', '2'));
    }

    @Test
    public void shouldNotRetainAllNonExistingElementsFromNonNil() {
        assertThat(CharSeq.of('1', '2', '3').retainAll(CharSeq.of('4', '5'))).isSameAs(empty());
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
        CharSeq.of('1').sliding(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenSlidingNonNilByNegativeSize() {
        CharSeq.of('1').sliding(-1);
    }

    @Test
    public void shouldSlideNilBySize() {
        assertThat(empty().sliding(1).isEmpty()).isTrue();
    }

    @Test
    public void shouldSlideNonNilBySize1() {
        assertThat(CharSeq.of('1', '2', '3').sliding(1).toList())
                .isEqualTo(List.of(CharSeq.of('1'), CharSeq.of('2'), CharSeq.of('3')));
    }

    @Test // #201
    public void shouldSlideNonNilBySize2() {
        assertThat(CharSeq.of('1', '2', '3', '4', '5').sliding(2).toList())
                .isEqualTo(List.of(CharSeq.of('1', '2'), CharSeq.of('2', '3'), CharSeq.of('3', '4'), CharSeq.of('4', '5')));
    }

    // -- sliding(size, step)

    @Test
    public void shouldSlideNilBySizeAndStep() {
        assertThat(empty().sliding(1, 1).isEmpty()).isTrue();
    }

    @Test
    public void shouldSlide5ElementsBySize2AndStep3() {
        assertThat(CharSeq.of('1', '2', '3', '4', '5').sliding(2, 3).toList())
                .isEqualTo(List.of(CharSeq.of('1', '2'), CharSeq.of('4', '5')));
    }

    @Test
    public void shouldSlide5ElementsBySize2AndStep4() {
        assertThat(CharSeq.of('1', '2', '3', '4', '5').sliding(2, 4).toList())
                .isEqualTo(List.of(CharSeq.of('1', '2'), CharSeq.of('5')));
    }

    @Test
    public void shouldSlide5ElementsBySize2AndStep5() {
        assertThat(CharSeq.of('1', '2', '3', '4', '5').sliding(2, 5).toList()).isEqualTo(List.of(CharSeq.of('1', '2')));
    }

    @Test
    public void shouldSlide4ElementsBySize5AndStep3() {
        assertThat(CharSeq.of('1', '2', '3', '4').sliding(5, 3).toList())
                .isEqualTo(List.of(CharSeq.of('1', '2', '3', '4')));
    }

    // -- span

    @Test
    public void shouldSpanNil() {
        assertThat(empty().span(i -> i < 2)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSpanNonNil() {
        CharSeq cs = CharSeq.of('0', '1', '2', '3');
        assertThat(cs.span(i -> i == '0' || i == '1'))
                .isEqualTo(Tuple.of(CharSeq.of('0', '1'), CharSeq.of('2', '3')));
        assertThat(cs.span(i -> false))
                .isEqualTo(Tuple.of(empty(), cs));
        assertThat(cs.span(i -> true))
                .isEqualTo(Tuple.of(cs, empty()));
    }

    // -- spliterator

    @Test
    public void shouldSplitNil() {
        final java.util.List<Character> actual = new java.util.ArrayList<>();
        CharSeq.empty().spliterator().forEachRemaining(actual::add);
        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldSplitNonNil() {
        final java.util.List<Character> actual = new java.util.ArrayList<>();
        CharSeq.of('1', '2', '3').spliterator().forEachRemaining(actual::add);
        assertThat(actual).isEqualTo(Arrays.asList('1', '2', '3'));
    }

    @Test
    public void shouldHaveImmutableSpliterator() {
        assertThat(CharSeq.of('1', '2', '3').spliterator().characteristics() & Spliterator.IMMUTABLE).isNotZero();
    }

    @Test
    public void shouldHaveOrderedSpliterator() {
        assertThat(CharSeq.of('1', '2', '3').spliterator().characteristics() & Spliterator.ORDERED).isNotZero();
    }

    @Test
    public void shouldHaveSizedSpliterator() {
        assertThat(CharSeq.of('1', '2', '3').spliterator().characteristics() & Spliterator.SIZED).isNotZero();
    }

    @Test
    public void shouldReturnSizeWhenSpliterator() {
        assertThat(CharSeq.of('1', '2', '3').spliterator().getExactSizeIfKnown()).isEqualTo(3);
    }

    // -- startsWith

    @Test
    public void shouldStartsNilOfNilCalculate() {
        assertThat(empty().startsWith(empty())).isTrue();
    }

    @Test
    public void shouldStartsNilOfNonNilCalculate() {
        assertThat(empty().startsWith(CharSeq.of('a'))).isFalse();
    }

    @Test
    public void shouldStartsNilOfNilWithOffsetCalculate() {
        assertThat(empty().startsWith(empty(), 1)).isFalse();
    }

    @Test
    public void shouldStartsNilOfNonNilWithOffsetCalculate() {
        assertThat(empty().startsWith(CharSeq.of('a'), 1)).isFalse();
    }

    @Test
    public void shouldStartsNonNilOfNilCalculate() {
        assertThat(CharSeq.of('a', 'b', 'c').startsWith(empty())).isTrue();
    }

    @Test
    public void shouldStartsNonNilOfNonNilCalculate() {
        assertThat(CharSeq.of('a', 'b', 'c').startsWith(CharSeq.of('a', 'b'))).isTrue();
        assertThat(CharSeq.of('a', 'b', 'c').startsWith(CharSeq.of('a', 'b', 'c'))).isTrue();
        assertThat(CharSeq.of('a', 'b', 'c').startsWith(CharSeq.of('a', 'b', 'c', 'd'))).isFalse();
        assertThat(CharSeq.of('a', 'b', 'c').startsWith(CharSeq.of('a', 'c'))).isFalse();
    }

    @Test
    public void shouldStartsNonNilOfNilWithOffsetCalculate() {
        assertThat(CharSeq.of('a', 'b', 'c').startsWith(empty(), 1)).isTrue();
    }

    @Test
    public void shouldStartsNonNilOfNonNilWithOffsetCalculate() {
        assertThat(CharSeq.of('a', 'b', 'c').startsWith(CharSeq.of('b', 'c'), 1)).isTrue();
        assertThat(CharSeq.of('a', 'b', 'c').startsWith(CharSeq.of('b', 'c', 'd'), 1)).isFalse();
        assertThat(CharSeq.of('a', 'b', 'c').startsWith(CharSeq.of('b', 'd'), 1)).isFalse();
    }

    // -- stderr

    @Test
    public void shouldWriteToStderr() {
        CharSeq.of('1', '2', '3').stderr();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldHandleStderrIOException() {
        final PrintStream originalErr = System.err;
        try (PrintStream failingPrintStream = failingPrintStream()) {
            System.setErr(failingPrintStream);
            CharSeq.of('0').stderr();
        } finally {
            System.setErr(originalErr);
        }
    }

    // -- stdout

    @Test
    public void shouldWriteToStdout() {
        CharSeq.of('1', '2', '3').stdout();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldHandleStdoutIOException() {
        final PrintStream originalOut = System.out;
        try (PrintStream failingPrintStream = failingPrintStream()) {
            System.setOut(failingPrintStream);
            CharSeq.of('0').stdout();
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
        CharSeq.of('1', '2', '3').sum();
    }

    // -- take

    @Test
    public void shouldTakeNoneOnNil() {
        assertThat(empty().take(1)).isSameAs(empty());
    }

    @Test
    public void shouldTakeNoneIfCountIsNegative() {
        assertThat(CharSeq.of('1', '2', '3').take(-1)).isSameAs(empty());
    }

    @Test
    public void shouldTakeAsExpectedIfCountIsLessThanSize() {
        assertThat(CharSeq.of('1', '2', '3').take(2)).isEqualTo(CharSeq.of('1', '2'));
    }

    @Test
    public void shouldTakeAllIfCountExceedsSize() {
        final CharSeq t = CharSeq.of('1', '2', '3');
        assertThat(t.take(4)).isSameAs(t);
    }

    // -- takeRight

    @Test
    public void shouldTakeRightNoneOnNil() {
        assertThat(empty().takeRight(1)).isSameAs(empty());
    }

    @Test
    public void shouldTakeRightNoneIfCountIsNegative() {
        assertThat(CharSeq.of('1', '2', '3').takeRight(-1)).isSameAs(empty());
    }

    @Test
    public void shouldTakeRightAsExpectedIfCountIsLessThanSize() {
        assertThat(CharSeq.of('1', '2', '3').takeRight(2)).isEqualTo(CharSeq.of('2', '3'));
    }

    @Test
    public void shouldTakeRightAllIfCountExceedsSize() {
        final CharSeq t = CharSeq.of('1', '2', '3');
        assertThat(t.takeRight(4)).isSameAs(t);
    }

    // -- takeWhile

    @Test
    public void shouldTakeWhileNoneOnNil() {
        assertThat(empty().takeWhile(x -> true)).isSameAs(empty());
    }

    @Test
    public void shouldTakeWhileAllOnFalseCondition() {
        assertThat(CharSeq.of('1', '2', '3').takeWhile(x -> false)).isSameAs(empty());
    }

    @Test
    public void shouldTakeWhileAllOnTrueCondition() {
        final CharSeq t = CharSeq.of('1', '2', '3');
        assertThat(t.takeWhile(x -> true)).isSameAs(t);
    }

    @Test
    public void shouldTakeWhileAsExpected() {
        assertThat(CharSeq.of('2', '4', '5', '6').takeWhile(x -> x % 2 == 0)).isEqualTo(CharSeq.of('2', '4'));
    }

    // -- takeUntil

    @Test
    public void shouldTakeUntilNoneOnNil() {
        assertThat(empty().takeUntil(x -> true)).isSameAs(empty());
    }

    @Test
    public void shouldTakeUntilAllOnFalseCondition() {
        final CharSeq t = CharSeq.of('1', '2', '3');
        assertThat(t.takeUntil(x -> false)).isSameAs(t);
    }

    @Test
    public void shouldTakeUntilAllOnTrueCondition() {
        assertThat(CharSeq.of('1', '2', '3').takeUntil(x -> true)).isSameAs(empty());
    }

    @Test
    public void shouldTakeUntilAsExpected() {
        assertThat(CharSeq.of('2', '4', '5', '6').takeUntil(x -> x % 2 != 0)).isEqualTo(CharSeq.of('2', '4'));
    }

    // -- tail

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenTailOnNil() {
        empty().tail();
    }

    @Test
    public void shouldReturnTailOfNonNil() {
        assertThat(CharSeq.of('1', '2', '3').tail()).isEqualTo(CharSeq.of('2', '3'));
    }

    // -- tailOption

    @Test
    public void shouldReturnNoneWhenCallingTailOptionOnNil() {
        assertThat(empty().tailOption().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnSomeTailWhenCallingTailOptionOnNonNil() {
        assertThat(CharSeq.of('1', '2', '3').tailOption()).isEqualTo(Option.some(CharSeq.of('2', '3')));
    }

    // -- toLowerCase

    @Test
    public void shouldConvertToLowerCase() {
        assertThat(CharSeq.of("JaVasLAng").toLowerCase()).isEqualTo(CharSeq.of("javaslang"));
        assertThat(CharSeq.of("JaVasLAng").toLowerCase(Locale.ENGLISH)).isEqualTo(CharSeq.of("javaslang"));
    }

    // -- toUpperCase

    @Test
    public void shouldConvertTotoUpperCase() {
        assertThat(CharSeq.of("JaVasLAng").toUpperCase()).isEqualTo(CharSeq.of("JAVASLANG"));
        assertThat(CharSeq.of("JaVasLAng").toUpperCase(Locale.ENGLISH)).isEqualTo(CharSeq.of("JAVASLANG"));
    }

    // -- toJavaArray(Class)

    @Test
    public void shouldConvertNilToJavaArray() {
        final Character[] actual = CharSeq.empty().toJavaArray(Character.class);
        final Character[] expected = new Character[] {};
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConvertNonNilToJavaArray() {
        final Character[] array = CharSeq.of('1', '2').toJavaArray(Character.class);
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
        assertThat(CharSeq.of('1', '2', '3').toJavaList()).isEqualTo(Arrays.asList('1', '2', '3'));
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
        assertThat(CharSeq.of('1', '2').toJavaMap(x -> Tuple.of(x, x))).isEqualTo(expected);
    }

    // -- toJavaSet

    @Test
    public void shouldConvertNilToHashSet() {
        assertThat(CharSeq.empty().toJavaSet()).isEqualTo(new java.util.HashSet<>());
    }

    @Test
    public void shouldConvertNonNilToHashSet() {
        final java.util.Set<Character> expected = new java.util.HashSet<>();
        expected.add('2');
        expected.add('1');
        expected.add('3');
        assertThat(CharSeq.of('1', '2', '2', '3').toJavaSet()).isEqualTo(expected);
    }

    // -- stringPrefix

    @Test
    public void shouldReturnStringPrefix() {
        assertThat(CharSeq.of('1').stringPrefix()).isEqualTo("CharSeq");
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
        assertThat(CharSeq.of('1')).isNotNull();
    }

    @Test
    public void shouldEmptyNotEqualsDifferentType() {
        assertThat(empty()).isNotEqualTo("");
    }

    @Test
    public void shouldNonEmptyNotEqualsDifferentType() {
        assertThat(CharSeq.of('1')).isNotEqualTo("");
    }

    @Test
    public void shouldRecognizeEqualityOfNils() {
        assertThat(empty()).isSameAs(empty());
    }

    @Test
    public void shouldRecognizeEqualityOfNonNils() {
        assertThat(CharSeq.of('1', '2', '3').equals(CharSeq.of('1', '2', '3'))).isTrue();
    }

    @Test
    public void shouldRecognizeContentEqualityOfNonNil() {
        assertThat(CharSeq.of('1', '2', '3').contentEquals(new StringBuffer().append("123"))).isTrue();
        assertThat(CharSeq.of('1', '2', '3').contentEquals("123")).isTrue();
    }

    @Test
    public void shouldRecognizeNonEqualityOfTraversablesOfSameSize() {
        assertThat(CharSeq.of('1', '2', '3').equals(CharSeq.of('1', '2', '4'))).isFalse();
    }

    @Test
    public void shouldRecognizeNonEqualityOfTraversablesOfDifferentSize() {
        assertThat(CharSeq.of('1', '2', '3').equals(CharSeq.of('1', '2'))).isFalse();
    }

    // -- hashCode

    @Test
    public void shouldCalculateHashCodeOfNil() {
        assertThat(empty().hashCode() == empty().hashCode()).isTrue();
    }

    @Test
    public void shouldCalculateHashCodeOfNonNil() {
        assertThat(CharSeq.of('1', '2').hashCode() == CharSeq.of('1', '2').hashCode()).isTrue();
    }

    @Test
    public void shouldCalculateDifferentHashCodesForDifferentTraversables() {
        assertThat(CharSeq.of('1', '2').hashCode() != CharSeq.of('2', '3').hashCode()).isTrue();
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
        final Object actual = deserialize(serialize(CharSeq.of('1', '2', '3')));
        final Object expected = CharSeq.of('1', '2', '3');
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
        final CharSeq expected = CharSeq.of('1');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendElementToNonNil() {
        final CharSeq actual = CharSeq.of('1', '2').append('3');
        final CharSeq expected = CharSeq.of('1', '2', '3');
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
        final CharSeq actual = empty().appendAll(CharSeq.of('1', '2', '3'));
        final CharSeq expected = CharSeq.of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNilToNonNil() {
        final CharSeq actual = CharSeq.of('1', '2', '3').appendAll(empty());
        final CharSeq expected = CharSeq.of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendAllNonNilToNonNil() {
        final CharSeq actual = CharSeq.of('1', '2', '3').appendAll(CharSeq.of('4', '5', '6'));
        final CharSeq expected = CharSeq.of('1', '2', '3', '4', '5', '6');
        assertThat(actual).isEqualTo(expected);
    }

    // -- apply

    @Test
    public void shouldUseSeqAsPartialFunction() {
        assertThat(CharSeq.of('1', '2', '3').apply(1)).isEqualTo('2');
    }

    // -- combinations

    @Test
    public void shouldComputeCombinationsOfEmptyList() {
        assertThat(empty().combinations()).isEqualTo(Vector.of(empty()));
    }

    @Test
    public void shouldComputeCombinationsOfNonEmptyList() {
        assertThat(CharSeq.of("123").combinations()).isEqualTo(Vector.of(empty(), CharSeq.of("1"), CharSeq.of("2"), CharSeq.of("3"), CharSeq.of("12"), CharSeq.of("13"), CharSeq.of("23"), CharSeq.of("123")));
    }

    // -- combinations(k)

    @Test
    public void shouldComputeKCombinationsOfEmptyList() {
        assertThat(empty().combinations(1)).isEmpty();
    }

    @Test
    public void shouldComputeKCombinationsOfNonEmptyList() {
        assertThat(CharSeq.of("123").combinations(2)).isEqualTo(Vector.of(CharSeq.of("12"), CharSeq.of("13"), CharSeq.of("23")));
    }

    @Test
    public void shouldComputeKCombinationsOfNegativeK() {
        assertThat(CharSeq.of("1").combinations(-1)).isEqualTo(Vector.of(CharSeq.empty()));
    }

    // -- containsSlice

    @Test
    public void shouldRecognizeNilNotContainsSlice() {
        final boolean actual = empty().containsSlice(CharSeq.of('1', '2', '3'));
        assertThat(actual).isFalse();
    }

    @Test
    public void shouldRecognizeNonNilDoesContainSlice() {
        final boolean actual = CharSeq.of('1', '2', '3', '4', '5').containsSlice(CharSeq.of('2', '3'));
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldRecognizeNonNilDoesNotContainSlice() {
        final boolean actual = CharSeq.of('1', '2', '3', '4', '5').containsSlice(CharSeq.of('2', '1', '4'));
        assertThat(actual).isFalse();
    }

    // -- crossProduct()

    @Test
    public void shouldCalculateCrossProductOfNil() {
        final IndexedSeq<Tuple2<Character, Character>> actual = empty().crossProduct();
        assertThat(actual).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldCalculateCrossProductOfNonNil() {
        final IndexedSeq<Tuple2<Character, Character>> actual = CharSeq.of('1', '2', '3').crossProduct();
        final Vector<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', '1'), Tuple.of('1', '2'),
                Tuple.of('1', '3'), Tuple.of('2', '1'), Tuple.of('2', '2'), Tuple.of('2', '3'), Tuple.of('3', '1'),
                Tuple.of('3', '2'), Tuple.of('3', '3'));
        assertThat(actual).isEqualTo(expected);
    }

    // -- crossProduct(int)

    @Test
    public void shouldCalculateCrossProductPower() {
        final IndexedSeq<CharSeq> actual = CharSeq.of("12").crossProduct(2);
        final Vector<CharSeq> expected = Vector.of(CharSeq.of('1', '1'), CharSeq.of('1', '2'), CharSeq.of('2', '1'), CharSeq.of('2', '2'));
        assertThat(actual).isEqualTo(expected);
    }

    // -- crossProduct(Iterable)

    @Test
    public void shouldCalculateCrossProductOfNilAndNil() {
        final Traversable<Tuple2<Character, Object>> actual = empty().crossProduct(empty());
        assertThat(actual).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldCalculateCrossProductOfNilAndNonNil() {
        final Traversable<Tuple2<Character, Object>> actual = empty().crossProduct(CharSeq.of('1', '2', '3'));
        assertThat(actual).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldCalculateCrossProductOfNonNilAndNil() {
        final Traversable<Tuple2<Character, Character>> actual = CharSeq
                .of('1', '2', '3')
                .crossProduct(CharSeq.empty());
        assertThat(actual).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldCalculateCrossProductOfNonNilAndNonNil() {
        final IndexedSeq<Tuple2<Character, Character>> actual = CharSeq
                .of('1', '2', '3')
                .crossProduct(CharSeq.of('a', 'b'));
        final IndexedSeq<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('1', 'b'),
                Tuple.of('2', 'a'), Tuple.of('2', 'b'), Tuple.of('3', 'a'), Tuple.of('3', 'b'));
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
        CharSeq.of('1').get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetOnNil() {
        empty().get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenGetWithTooBigIndexOnNonNil() {
        CharSeq.of('1').get(1);
    }

    @Test
    public void shouldGetFirstElement() {
        assertThat(CharSeq.of('1', '2', '3').get(0)).isEqualTo('1');
    }

    @Test
    public void shouldGetLastElement() {
        assertThat(CharSeq.of('1', '2', '3').get(2)).isEqualTo('3');
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
        final List<CharSeq> actual = CharSeq.of('1', '2', '3', '4').grouped(2).toList();
        final List<CharSeq> expected = List.of(CharSeq.of('1', '2'), CharSeq.of('3', '4'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldGroupedTraversableWithRemainder() {
        final List<CharSeq> actual = CharSeq.of('1', '2', '3', '4', '5').grouped(2).toList();
        final List<CharSeq> expected = List.of(CharSeq.of('1', '2'), CharSeq.of('3', '4'), CharSeq.of('5'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldGroupedWhenTraversableLengthIsSmallerThanBlockSize() {
        final List<CharSeq> actual = CharSeq.of('1', '2', '3', '4').grouped(5).toList();
        final List<CharSeq> expected = List.of(CharSeq.of('1', '2', '3', '4'));
        assertThat(actual).isEqualTo(expected);
    }

    // -- indexOf

    @Test
    public void shouldNotFindIndexOfElementWhenSeqIsEmpty() {
        assertThat(empty().indexOf(1)).isEqualTo(-1);
    }

    @Test
    public void shouldNotFindIndexOfElementWhenStartIsGreater() {
        assertThat(CharSeq.of('1', '2', '3', '4').indexOf(2, 2)).isEqualTo(-1);
    }

    @Test
    public void shouldFindIndexOfFirstElement() {
        assertThat(CharSeq.of('1', '2', '3').indexOf('1')).isEqualTo(0);
        assertThat(CharSeq.of('1', '2', '3').indexOf(Character.valueOf('1'))).isEqualTo(0);
    }

    @Test
    public void shouldFindIndexOfInnerElement() {
        assertThat(CharSeq.of('1', '2', '3').indexOf('2')).isEqualTo(1);
        assertThat(CharSeq.of('1', '2', '3').indexOf(Character.valueOf('2'))).isEqualTo(1);
    }

    @Test
    public void shouldFindIndexOfLastElement() {
        assertThat(CharSeq.of('1', '2', '3').indexOf('3')).isEqualTo(2);
        assertThat(CharSeq.of('1', '2', '3').indexOf(Character.valueOf('3'))).isEqualTo(2);
    }

    // -- indexOfSlice

    @Test
    public void shouldNotFindIndexOfSliceWhenSeqIsEmpty() {
        assertThat(empty().indexOfSlice(CharSeq.of('2', '3'))).isEqualTo(-1);
    }

    @Test
    public void shouldNotFindIndexOfSliceWhenStartIsGreater() {
        assertThat(CharSeq.of('1', '2', '3', '4').indexOfSlice(CharSeq.of('2', '3'), 2)).isEqualTo(-1);
    }

    @Test
    public void shouldFindIndexOfFirstSlice() {
        assertThat(CharSeq.of('1', '2', '3', '4').indexOfSlice(CharSeq.of('1', '2'))).isEqualTo(0);
    }

    @Test
    public void shouldFindIndexOfInnerSlice() {
        assertThat(CharSeq.of('1', '2', '3', '4').indexOfSlice(CharSeq.of('2', '3'))).isEqualTo(1);
    }

    @Test
    public void shouldFindIndexOfLastSlice() {
        assertThat(CharSeq.of('1', '2', '3').indexOfSlice(CharSeq.of('2', '3'))).isEqualTo(1);
    }

    // -- lastIndexOf

    @Test
    public void shouldNotFindLastIndexOfElementWhenSeqIsEmpty() {
        assertThat(empty().lastIndexOf(1)).isEqualTo(-1);
    }

    @Test
    public void shouldNotFindLastIndexOfElementWhenEndIdLess() {
        assertThat(CharSeq.of('1', '2', '3', '4').lastIndexOf(3, 1)).isEqualTo(-1);
    }

    @Test
    public void shouldFindLastIndexOfElement() {
        assertThat(CharSeq.of('1', '2', '3', '1', '2', '3').lastIndexOf('1')).isEqualTo(3);
        assertThat(CharSeq.of('1', '2', '3', '1', '2', '3').lastIndexOf(Character.valueOf('1'))).isEqualTo(3);
    }

    @Test
    public void shouldFindLastIndexOfElementWithEnd() {
        assertThat(CharSeq.of('1', '2', '3', '1', '2', '3').lastIndexOf('1', 1)).isEqualTo(0);
        assertThat(CharSeq.of('1', '2', '3', '1', '2', '3').lastIndexOf(Character.valueOf('1'), 1)).isEqualTo(0);
    }

    // -- lastIndexOfSlice

    @Test
    public void shouldNotFindLastIndexOfSliceWhenSeqIsEmpty() {
        assertThat(empty().lastIndexOfSlice(CharSeq.of('2', '3'))).isEqualTo(-1);
    }

    @Test
    public void shouldNotFindLastIndexOfSliceWhenEndIdLess() {
        assertThat(CharSeq.of('1', '2', '3', '4', '5').lastIndexOfSlice(CharSeq.of('3', '4'), 1)).isEqualTo(-1);
    }

    @Test
    public void shouldFindLastIndexOfSlice() {
        assertThat(CharSeq.of('1', '2', '3', '1', '2').lastIndexOfSlice(empty())).isEqualTo(5);
        assertThat(CharSeq.of('1', '2', '3', '1', '2').lastIndexOfSlice(CharSeq.of('2'))).isEqualTo(4);
        assertThat(CharSeq.of('1', '2', '3', '1', '2', '3', '4').lastIndexOfSlice(CharSeq.of('2', '3'))).isEqualTo(4);
    }

    @Test
    public void shouldFindLastIndexOfSliceWithEnd() {
        assertThat(CharSeq.of('1', '2', '3', '1', '2', '3').lastIndexOfSlice(empty(), 2)).isEqualTo(2);
        assertThat(CharSeq.of('1', '2', '3', '1', '2', '3').lastIndexOfSlice(CharSeq.of('2'), 2)).isEqualTo(1);
        assertThat(CharSeq.of('1', '2', '3', '1', '2', '3').lastIndexOfSlice(CharSeq.of('2', '3'), 2)).isEqualTo(1);
        assertThat(CharSeq.of('1', '2', '3', '1', '2', '3', '4').lastIndexOfSlice(CharSeq.of('2', '3'), 2))
                .isEqualTo(1);
    }

    // -- insert

    @Test
    public void shouldInsertIntoNil() {
        final CharSeq actual = empty().insert(0, '1');
        final CharSeq expected = CharSeq.of('1');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertInFrontOfElement() {
        final CharSeq actual = CharSeq.of('4').insert(0, '1');
        final CharSeq expected = CharSeq.of('1', '4');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertBehindOfElement() {
        final CharSeq actual = CharSeq.of('4').insert(1, '1');
        final CharSeq expected = CharSeq.of('4', '1');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertIntoSeq() {
        final CharSeq actual = CharSeq.of('1', '2', '3').insert(2, '4');
        final CharSeq expected = CharSeq.of('1', '2', '4', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenInsertOnNonNilWithNegativeIndex() {
        CharSeq.of('1').insert(-1, null);
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
        final CharSeq actual = empty().insertAll(0, CharSeq.of('1', '2', '3'));
        final CharSeq expected = CharSeq.of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllInFrontOfElement() {
        final CharSeq actual = CharSeq.of('4').insertAll(0, CharSeq.of('1', '2', '3'));
        final CharSeq expected = CharSeq.of('1', '2', '3', '4');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllBehindOfElement() {
        final CharSeq actual = CharSeq.of('4').insertAll(1, CharSeq.of('1', '2', '3'));
        final CharSeq expected = CharSeq.of('4', '1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInsertAllIntoSeq() {
        final CharSeq actual = CharSeq.of('1', '2', '3').insertAll(2, CharSeq.of('4', '5'));
        final CharSeq expected = CharSeq.of('1', '2', '4', '5', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnInsertAllWithNil() {
        empty().insertAll(0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenInsertOnNonNilAllWithNegativeIndex() {
        CharSeq.of('1').insertAll(-1, empty());
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
        assertThat(CharSeq.of('a').intersperse(',')).isEqualTo(CharSeq.of('a'));
    }

    @Test
    public void shouldIntersperseMultipleElements() {
        assertThat(CharSeq.of('a', 'b').intersperse(',')).isEqualTo(CharSeq.of('a', ',', 'b'));
    }

    // -- iterator(int)

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenNilIteratorStartingAtIndex() {
        empty().iterator(1);
    }

    @Test
    public void shouldIterateFirstElementOfNonNilStartingAtIndex() {
        assertThat(CharSeq.of('1', '2', '3').iterator(1).next()).isEqualTo('2');
    }

    @Test
    public void shouldFullyIterateNonNilStartingAtIndex() {
        int actual = -1;
        for (java.util.Iterator<Character> iter = CharSeq.of('1', '2', '3').iterator(1); iter.hasNext(); ) {
            actual = iter.next();
        }
        assertThat(actual).isEqualTo('3');
    }

    // -- prepend

    @Test
    public void shouldPrependElementToNil() {
        final CharSeq actual = empty().prepend('1');
        final CharSeq expected = CharSeq.of('1');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependElementToNonNil() {
        final CharSeq actual = CharSeq.of('2', '3').prepend('1');
        final CharSeq expected = CharSeq.of('1', '2', '3');
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
        final CharSeq actual = CharSeq.of('1', '2', '3').prependAll(empty());
        final CharSeq expected = CharSeq.of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNonNilToNil() {
        final CharSeq actual = empty().prependAll(CharSeq.of('1', '2', '3'));
        final CharSeq expected = CharSeq.of('1', '2', '3');
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPrependAllNonNilToNonNil() {
        final CharSeq actual = CharSeq.of('4', '5', '6').prependAll(CharSeq.of('1', '2', '3'));
        final CharSeq expected = CharSeq.of('1', '2', '3', '4', '5', '6');
        assertThat(actual).isEqualTo(expected);
    }

    // -- remove

    @Test
    public void shouldRemoveElementFromNil() {
        assertThat(empty().remove(null)).isSameAs(empty());
    }

    @Test
    public void shouldRemoveElementFromSingleton() {
        assertThat(CharSeq.of('1').remove('1')).isSameAs(empty());
    }

    @Test
    public void shouldRemoveFirstElement() {
        assertThat(CharSeq.of('1', '2', '3').remove('1')).isEqualTo(CharSeq.of('2', '3'));
    }

    @Test
    public void shouldRemoveLastElement() {
        assertThat(CharSeq.of('1', '2', '3').remove('3')).isEqualTo(CharSeq.of('1', '2'));
    }

    @Test
    public void shouldRemoveInnerElement() {
        assertThat(CharSeq.of('1', '2', '3').remove('2')).isEqualTo(CharSeq.of('1', '3'));
    }

    @Test
    public void shouldRemoveNonExistingElement() {
        final CharSeq t = CharSeq.of('1', '2', '3');
        assertThat(t.remove('4')).isSameAs(t);
    }

    // -- removeFirst(Predicate)

    @Test
    public void shouldRemoveFirstElementByPredicateFromNil() {
        assertThat(empty().removeFirst(v -> true)).isSameAs(empty());
    }

    @Test
    public void shouldRemoveFirstElementByPredicateFromSingleton() {
        assertThat(CharSeq.of('1').removeFirst(v -> v == '1')).isSameAs(empty());
    }

    @Test
    public void shouldRemoveFirstElementByPredicateBegin() {
        assertThat(CharSeq.of('1', '2', '3').removeFirst(v -> v == '1')).isEqualTo(CharSeq.of('2', '3'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateBeginM() {
        assertThat(CharSeq.of('1', '2', '1', '3').removeFirst(v -> v == '1')).isEqualTo(CharSeq.of('2', '1', '3'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateEnd() {
        assertThat(CharSeq.of('1', '2', '3').removeFirst(v -> v == '3')).isEqualTo(CharSeq.of('1', '2'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateInner() {
        assertThat(CharSeq.of('1', '2', '3', '4', '5').removeFirst(v -> v == '3'))
                .isEqualTo(CharSeq.of('1', '2', '4', '5'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateInnerM() {
        assertThat(CharSeq.of('1', '2', '3', '2', '5').removeFirst(v -> v == '2'))
                .isEqualTo(CharSeq.of('1', '3', '2', '5'));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateNonExisting() {
        final CharSeq t = CharSeq.of('1', '2', '3');
        assertThat(t.removeFirst(v -> v == 4)).isSameAs(t);
    }

    // -- removeLast(Predicate)

    @Test
    public void shouldRemoveLastElementByPredicateFromNil() {
        assertThat(empty().removeLast(v -> true)).isSameAs(empty());
    }

    @Test
    public void shouldRemoveLastElementByPredicateFromSingleton() {
        assertThat(CharSeq.of('1').removeLast(v -> v == '1')).isSameAs(empty());
    }

    @Test
    public void shouldRemoveLastElementByPredicateBegin() {
        assertThat(CharSeq.of('1', '2', '3').removeLast(v -> v == '1')).isEqualTo(CharSeq.of('2', '3'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateEnd() {
        assertThat(CharSeq.of('1', '2', '3').removeLast(v -> v == '3')).isEqualTo(CharSeq.of('1', '2'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateEndM() {
        assertThat(CharSeq.of('1', '3', '2', '3').removeLast(v -> v == '3')).isEqualTo(CharSeq.of('1', '3', '2'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateInner() {
        assertThat(CharSeq.of('1', '2', '3', '4', '5').removeLast(v -> v == '3'))
                .isEqualTo(CharSeq.of('1', '2', '4', '5'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateInnerM() {
        assertThat(CharSeq.of('1', '2', '3', '2', '5').removeLast(v -> v == '2'))
                .isEqualTo(CharSeq.of('1', '2', '3', '5'));
    }

    @Test
    public void shouldRemoveLastElementByPredicateNonExisting() {
        final CharSeq t = CharSeq.of('1', '2', '3');
        assertThat(t.removeLast(v -> v == 4)).isSameAs(t);
    }

    // -- removeAll(Iterable)

    @Test
    public void shouldRemoveAllElementsFromNil() {
        assertThat(empty().removeAll(CharSeq.of('1', '2', '3'))).isSameAs(empty());
    }

    @Test
    public void shouldRemoveAllExistingElementsFromNonNil() {
        assertThat(CharSeq.of('1', '2', '3', '1', '2', '3').removeAll(CharSeq.of('1', '2')))
                .isEqualTo(CharSeq.of('3', '3'));
    }

    @Test
    public void shouldNotRemoveAllNonExistingElementsFromNonNil() {
        final CharSeq t = CharSeq.of('1', '2', '3');
        assertThat(t.removeAll(CharSeq.of('4', '5'))).isSameAs(t);
    }

    // -- removeAll(Object)

    @Test
    public void shouldRemoveAllObjectsFromNil() {
        assertThat(empty().removeAll('1')).isSameAs(empty());
    }

    @Test
    public void shouldRemoveAllExistingObjectsFromNonNil() {
        assertThat(CharSeq.of('1', '2', '3', '1', '2', '3').removeAll('1')).isEqualTo(CharSeq.of('2', '3', '2', '3'));
    }

    @Test
    public void shouldNotRemoveAllNonObjectsElementsFromNonNil() {
        final CharSeq t = CharSeq.of('1', '2', '3');
        assertThat(t.removeAll('4')).isSameAs(t);
    }

    // -- reverse

    @Test
    public void shouldReverseNil() {
        assertThat(empty().reverse()).isSameAs(empty());
    }

    @Test
    public void shouldReverseNonNil() {
        assertThat(CharSeq.of('1', '2', '3').reverse()).isEqualTo(CharSeq.of('3', '2', '1'));
    }

    // -- set

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithNegativeIndexOnNil() {
        empty().update(-1, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithNegativeIndexOnNonNil() {
        CharSeq.of('1').update(-1, '2');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetOnNil() {
        empty().update(0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithIndexExceedingByOneOnNonNil() {
        CharSeq.of('1').update(1, '2');
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSetWithIndexExceedingByTwoOnNonNil() {
        CharSeq.of('1').update(2, '2');
    }

    @Test
    public void shouldSetFirstElement() {
        assertThat(CharSeq.of('1', '2', '3').update(0, '4')).isEqualTo(CharSeq.of('4', '2', '3'));
    }

    @Test
    public void shouldSetLastElement() {
        assertThat(CharSeq.of('1', '2', '3').update(2, '4')).isEqualTo(CharSeq.of('1', '2', '4'));
    }

    // -- slice()

    @Test
    public void shouldSlice() {
        assertThat(CharSeq.empty().slice(0, 0)).isSameAs(empty());
        assertThat(CharSeq.of("123").slice(0, 0)).isSameAs(empty());
        assertThat(CharSeq.of("123").slice(1, 0)).isSameAs(empty());
        assertThat(CharSeq.of("123").slice(4, 5)).isSameAs(empty());
        assertThat(CharSeq.of("123").slice(0, 3)).isEqualTo(CharSeq.of("123"));
        assertThat(CharSeq.of("123").slice(-1, 2)).isEqualTo(CharSeq.of("12"));
        assertThat(CharSeq.of("123").slice(0, 2)).isEqualTo(CharSeq.of("12"));
        assertThat(CharSeq.of("123").slice(1, 2)).isEqualTo(CharSeq.of("2"));
        assertThat(CharSeq.of("123").slice(1, 3)).isEqualTo(CharSeq.of("23"));
        assertThat(CharSeq.of("123").slice(1, 4)).isEqualTo(CharSeq.of("23"));
    }

    // -- sort()

    @Test
    public void shouldSortNil() {
        assertThat(empty().sort()).isSameAs(empty());
    }

    @Test
    public void shouldSortNonNil() {
        assertThat(CharSeq.of('3', '4', '1', '2').sort()).isEqualTo(CharSeq.of('1', '2', '3', '4'));
    }

    // -- sort(Comparator)

    @Test
    public void shouldSortNilUsingComparator() {
        assertThat(empty().sort((i, j) -> j - i)).isSameAs(empty());
    }

    @Test
    public void shouldSortNonNilUsingComparator() {
        assertThat(CharSeq.of('3', '4', '1', '2').sort((i, j) -> j - i)).isEqualTo(CharSeq.of('4', '3', '2', '1'));
    }

    // -- sortBy()

    @Test
    public void shouldSortByFunction() {
        assertThat(CharSeq.of("123").sortBy(c -> -c)).isEqualTo(CharSeq.of("321"));
    }

    @Test
    public void shouldSortByComparator() {
        assertThat(CharSeq.of("123").sortBy((i, j) -> j - i, c -> c)).isEqualTo(CharSeq.of("321"));
    }

    // -- splitAt(index)

    @Test
    public void shouldSplitAtNil() {
        assertThat(empty().splitAt(1)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSplitAtNonNil() {
        assertThat(CharSeq.of('1', '2', '3').splitAt(1)).isEqualTo(Tuple.of(CharSeq.of('1'), CharSeq.of('2', '3')));
    }

    @Test
    public void shouldSplitAtBegin() {
        assertThat(CharSeq.of('1', '2', '3').splitAt(0)).isEqualTo(Tuple.of(empty(), CharSeq.of('1', '2', '3')));
    }

    @Test
    public void shouldSplitAtEnd() {
        assertThat(CharSeq.of('1', '2', '3').splitAt(3)).isEqualTo(Tuple.of(CharSeq.of('1', '2', '3'), empty()));
    }

    @Test
    public void shouldSplitAtOutOfBounds() {
        assertThat(CharSeq.of('1', '2', '3').splitAt(5)).isEqualTo(Tuple.of(CharSeq.of('1', '2', '3'), empty()));
        assertThat(CharSeq.of('1', '2', '3').splitAt(-1)).isEqualTo(Tuple.of(empty(), CharSeq.of('1', '2', '3')));
    }

    // -- splitAt(predicate)

    @Test
    public void shouldSplitPredicateAtNil() {
        assertThat(empty().splitAt(e -> true)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSplitPredicateAtNonNil() {
        assertThat(CharSeq.of('1', '2', '3').splitAt(e -> e == '2'))
                .isEqualTo(Tuple.of(CharSeq.of('1'), CharSeq.of('2', '3')));
    }

    @Test
    public void shouldSplitAtPredicateBegin() {
        assertThat(CharSeq.of('1', '2', '3').splitAt(e -> e == '1'))
                .isEqualTo(Tuple.of(empty(), CharSeq.of('1', '2', '3')));
    }

    @Test
    public void shouldSplitAtPredicateEnd() {
        assertThat(CharSeq.of('1', '2', '3').splitAt(e -> e == '3'))
                .isEqualTo(Tuple.of(CharSeq.of('1', '2'), CharSeq.of('3')));
    }

    @Test
    public void shouldSplitAtPredicateNotFound() {
        assertThat(CharSeq.of('1', '2', '3').splitAt(e -> e == '5'))
                .isEqualTo(Tuple.of(CharSeq.of('1', '2', '3'), empty()));
    }

    // -- splitAtInclusive(predicate)

    @Test
    public void shouldSplitInclusivePredicateAtNil() {
        assertThat(empty().splitAtInclusive(e -> true)).isEqualTo(Tuple.of(empty(), empty()));
    }

    @Test
    public void shouldSplitInclusivePredicateAtNonNil() {
        assertThat(CharSeq.of('1', '2', '3').splitAtInclusive(e -> e == '2'))
                .isEqualTo(Tuple.of(CharSeq.of('1', '2'), CharSeq.of('3')));
    }

    @Test
    public void shouldSplitAtInclusivePredicateBegin() {
        assertThat(CharSeq.of('1', '2', '3').splitAtInclusive(e -> e == '1'))
                .isEqualTo(Tuple.of(CharSeq.of('1'), CharSeq.of('2', '3')));
    }

    @Test
    public void shouldSplitAtInclusivePredicateEnd() {
        assertThat(CharSeq.of('1', '2', '3').splitAtInclusive(e -> e == '3'))
                .isEqualTo(Tuple.of(CharSeq.of('1', '2', '3'), empty()));
    }

    @Test
    public void shouldSplitAtInclusivePredicateNotFound() {
        assertThat(CharSeq.of('1', '2', '3').splitAtInclusive(e -> e == '5'))
                .isEqualTo(Tuple.of(CharSeq.of('1', '2', '3'), empty()));
    }

    // -- removeAt(index)

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldRemoveIndxAtNil() {
        empty().removeAt(1);
    }

    @Test
    public void shouldRemoveIndxAtSingleton() {
        assertThat(CharSeq.of('1').removeAt(0)).isSameAs(empty());
    }

    @Test
    public void shouldRemoveIndxAtNonNil() {
        assertThat(CharSeq.of('1', '2', '3').removeAt(1)).isEqualTo(CharSeq.of('1', '3'));
    }

    @Test
    public void shouldRemoveIndxAtBegin() {
        assertThat(CharSeq.of('1', '2', '3').removeAt(0)).isEqualTo(CharSeq.of('2', '3'));
    }

    @Test
    public void shouldRemoveIndxAtEnd() {
        assertThat(CharSeq.of('1', '2', '3').removeAt(2)).isEqualTo(CharSeq.of('1', '2'));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldRemoveIndxOutOfBoundsLeft() {
        assertThat(CharSeq.of('1', '2', '3').removeAt(-1)).isEqualTo(CharSeq.of('1', '2', '3'));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldRemoveIndxOutOfBoundsRight() {
        assertThat(CharSeq.of('1', '2', '3').removeAt(5)).isEqualTo(CharSeq.of('1', '2', '3'));
    }

    // -- repeat

    @Test
    public void shouldRepeat() {
        assertThat(CharSeq.empty().repeat(0)).isEqualTo(CharSeq.empty());
        assertThat(CharSeq.empty().repeat(5)).isEqualTo(CharSeq.empty());
        assertThat(CharSeq.of("123").repeat(0)).isEqualTo(CharSeq.empty());
        assertThat(CharSeq.of("123").repeat(5)).isEqualTo(CharSeq.of("123123123123123"));
        assertThat(CharSeq.repeat('1', 0)).isEqualTo(CharSeq.empty());
        assertThat(CharSeq.repeat('!', 5)).isEqualTo(CharSeq.of("!!!!!"));
    }


    // -- scan, scanLeft, scanRight

    @Test
    public void shouldScan() {
        final CharSeq seq = CharSeq.of('1');
        final IndexedSeq<Character> result = seq.scan('0', (c1, c2) -> (char) (c1 + c2));
        assertThat(result).isEqualTo(Vector.of('0', 'a'));
    }

    @Test
    public void shouldScanLeft() {
        final CharSeq seq = CharSeq.of('1');
        final IndexedSeq<Character> result = seq.scanLeft('0', (c1, c2) -> (char) (c1 + c2));
        assertThat(result).isEqualTo(Vector.of('0', 'a'));
    }

    @Test
    public void shouldScanRight() {
        final CharSeq seq = CharSeq.of('1');
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
        final CharSeq actual = CharSeq.of('1').subSequence(0);
        assertThat(actual).isEqualTo(CharSeq.of('1'));
    }

    @Test
    public void shouldReturnNilWhenSubSequenceFrom1OnSeqOf1() {
        final CharSeq actual = CharSeq.of('1').subSequence(1);
        assertThat(actual).isSameAs(empty());
    }

    @Test
    public void shouldReturnSubSequenceWhenIndexIsWithinRange() {
        final CharSeq actual = CharSeq.of('1', '2', '3').subSequence(1);
        assertThat(actual).isEqualTo(CharSeq.of('2', '3'));
    }

    @Test
    public void shouldReturnNilWhenSubSequenceBeginningWithSize() {
        final CharSeq actual = CharSeq.of('1', '2', '3').subSequence(3);
        assertThat(actual).isSameAs(empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubSequenceOnNil() {
        empty().subSequence(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubSequenceWithOutOfLowerBound() {
        CharSeq.of('1', '2', '3').subSequence(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowWhenSubSequenceWithOutOfUpperBound() {
        CharSeq.of('1', '2', '3').subSequence(4);
    }

    // -- subSequence(beginIndex, endIndex)

    @Test
    public void shouldReturnNilWhenSubSequenceFrom0To0OnNil() {
        final CharSeq actual = empty().subSequence(0, 0);
        assertThat(actual).isSameAs(empty());
    }

    @Test
    public void shouldReturnNilWhenSubSequenceFrom0To0OnNonNil() {
        final CharSeq actual = CharSeq.of('1').subSequence(0, 0);
        assertThat(actual).isSameAs(empty());
    }

    @Test
    public void shouldReturnSeqWithFirstElementWhenSubSequenceFrom0To1OnNonNil() {
        final CharSeq actual = CharSeq.of('1').subSequence(0, 1);
        assertThat(actual).isEqualTo(CharSeq.of('1'));
    }

    @Test
    public void shouldReturnNilWhenSubSequenceFrom1To1OnNonNil() {
        final CharSeq actual = CharSeq.of('1').subSequence(1, 1);
        assertThat(actual).isSameAs(empty());
    }

    @Test
    public void shouldReturnSubSequenceWhenIndicesAreWithinRange() {
        final CharSeq actual = CharSeq.of('1', '2', '3').subSequence(1, 3);
        assertThat(actual).isEqualTo(CharSeq.of('2', '3'));
    }

    @Test
    public void shouldReturnNilWhenIndicesBothAreUpperBound() {
        final CharSeq actual = CharSeq.of('1', '2', '3').subSequence(3, 3);
        assertThat(actual).isSameAs(empty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubSequenceOnNonNilWhenBeginIndexIsGreaterThanEndIndex() {
        CharSeq.of('1', '2', '3').subSequence(1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubSequenceOnNilWhenBeginIndexIsGreaterThanEndIndex() {
        empty().subSequence(1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowOnSubSequenceOnNonNilWhenBeginIndexExceedsLowerBound() {
        CharSeq.of('1', '2', '3').subSequence(-'1', '2');
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
        CharSeq.of('1', '2', '3').subSequence(1, 4).mkString(); // force computation of last element, e.g. because Stream is lazy
    }

    // -- unzip

    @Test
    public void shouldUnzipNil() {
        assertThat(empty().unzip(x -> Tuple.of(x, x))).isEqualTo(Tuple.of(Vector.empty(), Vector.empty()));
    }

    @Test
    public void shouldUnzipNonNil() {
        final Tuple actual = CharSeq.of('0', '1').unzip(i -> Tuple.of(i, i == '0' ? 'a' : 'b'));
        final Tuple expected = Tuple.of(Vector.of('0', '1'), Vector.of('a', 'b'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldUnzip3Nil() {
        assertThat(empty().unzip3(x -> Tuple.of(x, x, x))).isEqualTo(Tuple.of(Vector.empty(), Vector.empty(), Vector.empty()));
    }

    @Test
    public void shouldUnzip3NonNil() {
        final Tuple actual = CharSeq.of('0', '1').unzip3(i -> Tuple.of(i, i == '0' ? 'a' : 'b', i == '0' ? 'b' : 'a'));
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
        final Seq<?> actual = empty().zip(CharSeq.of('1'));
        assertThat(actual).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldZipNonEmptyAndNil() {
        final Seq<?> actual = CharSeq.of('1').zip(empty());
        assertThat(actual).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldZipNonNilsIfThisIsSmaller() {
        final IndexedSeq<Tuple2<Character, Character>> actual = CharSeq.of('1', '2').zip(CharSeq.of('a', 'b', 'c'));
        final IndexedSeq<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonNilsIfThatIsSmaller() {
        final IndexedSeq<Tuple2<Character, Character>> actual = CharSeq.of('1', '2', '3').zip(CharSeq.of('a', 'b'));
        final IndexedSeq<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonNilsOfSameSize() {
        final IndexedSeq<Tuple2<Character, Character>> actual = CharSeq.of('1', '2', '3').zip(CharSeq.of('a', 'b', 'c'));
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
        final IndexedSeq<?> actual = empty().zipAll(CharSeq.of('1'), null, null);
        final IndexedSeq<Tuple2<Object, Character>> expected = Vector.of(Tuple.of(null, '1'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonEmptyAndNil() {
        final IndexedSeq<?> actual = CharSeq.of('1').zipAll(empty(), null, null);
        final IndexedSeq<Tuple2<Character, Object>> expected = Vector.of(Tuple.of('1', null));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThisIsSmaller() {
        final IndexedSeq<Tuple2<Character, Character>> actual = CharSeq.of('1', '2').zipAll(CharSeq.of('a', 'b', 'c'), '9', 'z');
        final IndexedSeq<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'), Tuple.of('9', 'c'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThatIsSmaller() {
        final IndexedSeq<Tuple2<Character, Character>> actual = CharSeq.of('1', '2', '3').zipAll(CharSeq.of('a', 'b'), '9', 'z');
        final IndexedSeq<Tuple2<Character, Character>> expected = Vector.of(Tuple.of('1', 'a'), Tuple.of('2', 'b'), Tuple.of('3', 'z'));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsOfSameSize() {
        final IndexedSeq<Tuple2<Character, Character>> actual = CharSeq.of('1', '2', '3').zipAll(CharSeq.of('a', 'b', 'c'), '9', 'z');
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
        final IndexedSeq<Tuple2<Character, Integer>> actual = CharSeq.of("abc").zipWithIndex();
        final IndexedSeq<Tuple2<Character, Integer>> expected = Vector.of(Tuple.of('a', 0), Tuple.of('b', 1), Tuple.of('c', 2));
        assertThat(actual).isEqualTo(expected);
    }

    // -- static collector()

    @Test
    public void shouldStreamAndCollectNil() {
        final Seq<?> actual = java.util.stream.Stream.<Character> empty().collect(CharSeq.collector());
        assertThat(actual).isSameAs(empty());
    }

    @Test
    public void shouldStreamAndCollectNonNil() {
        final Seq<?> actual = java.util.stream.Stream.of('1', '2', '3').collect(CharSeq.collector());
        assertThat(actual).isEqualTo(CharSeq.of('1', '2', '3'));
    }

    @Test
    public void shouldParallelStreamAndCollectNil() {
        final Seq<?> actual = java.util.stream.Stream.<Character> empty().parallel().collect(CharSeq.collector());
        assertThat(actual).isSameAs(empty());
    }

    @Test
    public void shouldParallelStreamAndCollectNonNil() {
        final Seq<?> actual = java.util.stream.Stream.of('1', '2', '3').parallel().collect(CharSeq.collector());
        assertThat(actual).isEqualTo(CharSeq.of('1', '2', '3'));
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
        CharSeq cs1 = CharSeq.of("123");
        CharSeq cs2 = CharSeq.of(cs1);
        assertThat(cs1 == cs2).isTrue();
    }

    @Test
    public void shouldCreateSeqOfElements() {
        final CharSeq actual = CharSeq.of('1', '2');
        assertThat(actual.length()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualTo('1');
        assertThat(actual.get(1)).isEqualTo('2');
    }

    // -- static ofAll(Iterable)

    @Test
    public void shouldCreateListOfIterable() {
        final java.util.List<Character> arrayList = Arrays.asList('1', '2');
        final CharSeq actual = CharSeq.ofAll(arrayList);
        assertThat(actual.length()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualTo('1');
        assertThat(actual.get(1)).isEqualTo('2');
    }

    @Test
    public void shouldTabulateTheCharSeq() {
        Function<Number, Character> f = i -> i.toString().charAt(0);
        CharSeq actual = CharSeq.tabulate(3, f);
        assertThat(actual).isEqualTo(CharSeq.of('0', '1', '2'));
    }

    @Test
    public void shouldTabulateTheCharSeqCallingTheFunctionInTheRightOrder() {
        java.util.LinkedList<Character> chars = new java.util.LinkedList<>(Arrays.asList('0', '1', '2'));
        CharSeq actual = CharSeq.tabulate(3, i -> chars.remove());
        assertThat(actual).isEqualTo(CharSeq.of('0', '1', '2'));
    }

    @Test
    public void shouldTabulateTheCharSeqWith0Elements() {
        assertThat(CharSeq.tabulate(0, i -> 'a')).isEqualTo(empty());
    }

    @Test
    public void shouldTabulateTheCharSeqWith0ElementsWhenNIsNegative() {
        assertThat(CharSeq.tabulate(-1, i -> 'a')).isEqualTo(empty());
    }

    @Test
    public void shouldFillTheCharSeqCallingTheSupplierInTheRightOrder() {
        java.util.LinkedList<Character> chars = new java.util.LinkedList<>(Arrays.asList('0', '1'));
        CharSeq actual = CharSeq.fill(2, () -> chars.remove());
        assertThat(actual).isEqualTo(CharSeq.of('0', '1'));
    }

    @Test
    public void shouldFillTheCharSeqWith0Elements() {
        assertThat(CharSeq.fill(0, () -> 'a')).isEqualTo(empty());
    }

    @Test
    public void shouldFillTheCharSeqWith0ElementsWhenNIsNegative() {
        assertThat(CharSeq.fill(-1, () -> 'a')).isEqualTo(empty());
    }

    @Test
    public void ofShouldReturnTheSingletonEmpty() {
        assertThat(CharSeq.of()).isSameAs(empty());
    }

    @Test
    public void ofAllShouldReturnTheSingletonEmpty() {
        assertThat(CharSeq.ofAll(Iterator.empty())).isSameAs(empty());
    }
}
