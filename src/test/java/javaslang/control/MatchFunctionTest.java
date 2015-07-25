/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchFunctionTest {

    // when(Object)

    @Test
    public void shouldMatchNullByValue() {
        final boolean actual = Match
                .when(null).then(true)
                .apply(null);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchIntByValue() {
        final boolean actual = Match
                .when(1).then(false)
                .when(2).then(true)
                .apply(2);
        assertThat(actual).isTrue();
    }

    // whenIn(Onject...)

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMatchNullByValueIn() {
        final boolean actual = Match
                .whenIn((Object) null).then(true)
                .apply(null);
        assertThat(actual).isTrue();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMatchNullVarargsByValueIn() {
        Match.whenIn((Object[]) null).then(false)
                .apply(null);
    }

    @Test
    public void shouldMatchIntByValueIn() {
        final boolean actual = Match
                .whenIn(1, 2, 3).then(true)
                .apply(2);
        assertThat(actual).isTrue();
    }

    // whenTrue(Predicate)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMatchNullVarargsByPredicate() {
        Match.whenTrue(null).then(false)
                .apply(null);
    }

    @Test
    public void shouldMatchEvenIntByPredicate() {
        final String divisibility = Match
                .whenTrue((String s) -> true).then("oops")
                .whenTrue((Integer i) -> i % 2 == 0).then("even")
                .otherwise("odd")
                .apply(0);
        assertThat(divisibility).isEqualTo("even");
    }

    // whenType(Class)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMatchNullByType() {
        Match.whenType(null).then(false)
                .apply(null);
    }

    @Test
    public void shouldMatchNumberByType() {
        final String actual = Match
                .whenType(String.class).then(s -> "String " + s)
                .whenType(Number.class).then(n -> "Number " + n)
                .whenType(Integer.class).then(i -> "int " + i)
                .apply(1);
        assertThat(actual).isEqualTo("Number 1");
    }

    // whenTypeIn(Class...)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMatchNullVarargsByTypeIn() {
        Match.whenTypeIn((Class<?>[]) null).then(false)
                .apply(null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMatchNumberByTypeIn() {
        final Number number = 1;
        final String actual = Match
                .whenTypeIn(Byte.class, Integer.class).then(s -> "matched")
                .apply(number);
        assertThat(actual).isEqualTo("matched");
    }

    // whenApplicable(Function1)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMatchNullByFunction() {
        Match.whenApplicable(null).thenApply()
                .apply(null);
    }

    @Test
    public void shouldMatchIntByFunction() {
        final int actual = Match
                .whenApplicable((Integer i) -> i + 1).thenApply()
                .apply(1);
        assertThat(actual).isEqualTo(2);
    }

    // match by super-type

    @Test
    public void shouldMatchSuperTypeByValue() {
        final Option<Integer> option = Option.of(1);
        final boolean actual = Match
                .when(option).then(true)
                .apply(new Some<>(1));
        assertThat(actual).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMatchSuperTypeByValueIn() {
        final Option<Integer> option = Option.of(1);
        final boolean actual = Match
                .whenIn(None.instance(), option).then(true)
                .apply(new Some<>(1));
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchSuperTypeByPredicate() {
        final boolean actual = Match
                .whenTrue((Option<?> o) -> true).then(true)
                .apply(new Some<>(1));
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchSuperTypeByType() {
        final boolean actual = Match
                .whenType(Option.class).then(true)
                .apply(new Some<>(1));
        assertThat(actual).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMatchSuperTypeByTypeIn() {
        final boolean actual = Match
                .whenTypeIn(Boolean.class, Option.class).then(true)
                .apply(new Some<>(1));
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchSuperTypeByFunction() {
        final boolean actual = Match
                .whenApplicable((Option<?> o) -> true).thenApply()
                .apply(new Some<>(1));
        assertThat(actual).isTrue();
    }

    // match by sub-type

    @Test
    public void shouldMatchSubTypeByValue() {
        final Option<Integer> option = Option.of(1);
        final boolean actual = Match
                .when(new Some<>(1)).then(true)
                .apply(option);
        assertThat(actual).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMatchSubTypeByValueIn() {
        final Option<Integer> option = Option.of(1);
        final boolean actual = Match
                .whenIn(None.instance(), new Some<>(1)).then(true)
                .apply(option);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchSubTypeByPredicate() {
        final Option<Integer> option = Option.of(1);
        final boolean actual = Match
                .whenTrue((Some<?> o) -> true).then(true)
                .apply(option);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchSubTypeByType() {
        final Option<Integer> option = Option.of(1);
        final boolean actual = Match
                .whenType(Some.class).then(true)
                .apply(option);
        assertThat(actual).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMatchSubTypeByTypeIn() {
        final Option<Integer> option = Option.of(1);
        final boolean actual = Match
                .whenTypeIn(Boolean.class, Some.class).then(true)
                .apply(option);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchSubTypeByFunction() {
        final Option<Integer> option = Option.of(1);
        final boolean actual = Match
                .whenApplicable((Some<?> o) -> true).thenApply()
                .apply(option);
        assertThat(actual).isTrue();
    }

    // should apply then() when matched (honoring When, WhenApplicable)

    @Test
    public void shouldApplyThenValueWhenMatched() {
        final boolean actual = Match.when(1).then(true).apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldApplyThenSupplierWhenMatched() {
        final boolean actual = Match.when(1).then(() -> true).apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldApplyThenFunctionWhenMatched() {
        final boolean actual = Match.when(true).then(b -> b).apply(true);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldApplyFunctionWhenApplicableMatched() {
        final boolean actual = Match.whenApplicable((Boolean b) -> b).thenApply().apply(true);
        assertThat(actual).isTrue();
    }

    // should not apply then() when unmatched (honoring When, WhenApplicable)

    @Test
    public void shouldNotApplyThenValueWhenUnmatched() {
        final boolean actual = Match.when(1).then(false).otherwise(true).apply(0);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldNotApplyThenSupplierWhenUnmatched() {
        final boolean actual = Match.when(1).then(() -> false).otherwise(true).apply(0);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldNotApplyThenFunctionWhenUnmatched() {
        final boolean actual = Match.when(false).then(b -> b).otherwise(true).apply(true);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldNotApplyFunctionWhenApplicableUnmatched() {
        final boolean actual = Match.whenApplicable((Integer i) -> false).thenApply().otherwise(true).apply(true);
        assertThat(actual).isTrue();
    }

    // transport matched states

    @Test
    public void shouldTransportMatchedValue() {
        final boolean actual = Match
                .when(1).then(true)
                .when(1).then(false)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldTransportMatchedValueIn() {
        final boolean actual = Match
                .whenIn(1).then(true)
                .whenIn(1).then(false)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldTransportMatchedPredicate() {
        final boolean actual = Match
                .whenTrue((Integer i) -> true).then(true)
                .whenTrue((Integer i) -> true).then(false)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldTransportMatchedType() {
        final boolean actual = Match
                .whenType(Integer.class).then(true)
                .whenType(Integer.class).then(false)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldTransportMatchedTypeIn() {
        final boolean actual = Match
                .whenTypeIn(Integer.class).then(true)
                .whenTypeIn(Integer.class).then(false)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldTransportMatchedFunction() {
        final boolean actual = Match
                .whenApplicable((Integer i) -> true).thenApply()
                .whenApplicable((Integer i) -> false).thenApply()
                .apply(1);
        assertThat(actual).isTrue();
    }

    // transport unmatched states

    @Test
    public void shouldTransportUnmatchedValue() {
        final boolean actual = Match
                .when(0).then(false)
                .when(1).then(true)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldTransportUnmatchedValueIn() {
        final boolean actual = Match
                .whenIn(0).then(false)
                .whenIn(1).then(true)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldTransportUnmatchedPredicate() {
        final boolean actual = Match
                .whenTrue((Boolean b) -> false).then(false)
                .whenTrue((Integer i) -> true).then(true)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldTransportUnmatchedType() {
        final boolean actual = Match
                .whenType(Boolean.class).then(false)
                .whenType(Integer.class).then(true)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldTransportUnmatchedTypeIn() {
        final boolean actual = Match
                .whenTypeIn(Boolean.class).then(false)
                .whenTypeIn(Integer.class).then(true)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldTransportUnmatchedFunction() {
        final boolean actual = Match
                .whenApplicable((Boolean i) -> false).thenApply()
                .whenApplicable((Integer i) -> true).thenApply()
                .apply(1);
        assertThat(actual).isTrue();
    }

    // should declare common result type of multiple cases

    @Test
    public void shouldMatchTypedResultByValue() {
        final Number actual = Match.as(Number.class)
                .when(0).then(0.0d)
                .when(1).then(1)
                .apply(1);
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = MatchError.class)
    public void shouldMatchTypedResultByValueNegativeCase() {
        Match.as(Number.class)
                .when(0).then(0.0d)
                .when(1).then(1)
                .apply(-1);
    }

    @Test
    public void shouldMatchTypedResultByValueIn() {
        final Number actual = Match.as(Number.class)
                .whenIn(0).then(0.0d)
                .whenIn(1).then(1)
                .apply(1);
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = MatchError.class)
    public void shouldMatchTypedResultByValueInNegativeCase() {
        Match.as(Number.class)
                .whenIn(0).then(0.0d)
                .whenIn(1).then(1)
                .apply(-1);
    }

    @Test
    public void shouldMatchTypedResultByPredicate() {
        final Number actual = Match.as(Number.class)
                .whenTrue((Double d) -> true).then(0.0d)
                .whenTrue((Integer i) -> true).then(1)
                .apply(1);
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = MatchError.class)
    public void shouldMatchTypedResultByPredicateNegativeCase() {
        Match.as(Number.class)
                .whenTrue((Double d) -> true).then(0.0d)
                .whenTrue((Integer i) -> true).then(1)
                .apply("x");
    }

    @Test
    public void shouldMatchTypedResultByType() {
        final Number actual = Match.as(Number.class)
                .whenType(Boolean.class).then(0.0d)
                .whenType(Integer.class).then(1)
                .apply(1);
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = MatchError.class)
    public void shouldMatchTypedResultByTypeNegativeCase() {
        Match.as(Number.class)
                .whenType(Boolean.class).then(0.0d)
                .whenType(Integer.class).then(1)
                .apply("x");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMatchTypedResultByTypeIn() {
        final Number actual = Match.as(Number.class)
                .whenTypeIn(Boolean.class).then(0.0d)
                .whenTypeIn(Integer.class).then(1)
                .apply(1);
        assertThat(actual).isEqualTo(1);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = MatchError.class)
    public void shouldMatchTypedResultByTypeInNegativeCase() {
        Match.as(Number.class)
                .whenTypeIn(Boolean.class).then(0.0d)
                .whenTypeIn(Integer.class).then(1)
                .apply("x");
    }

    @Test
    public void shouldMatchTypedResultByFunction() {
        final Number actual = Match.as(Number.class)
                .whenApplicable((Double d) -> d).thenApply()
                .whenApplicable((Integer i) -> i).thenApply()
                .apply(1);
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = MatchError.class)
    public void shouldMatchTypedResultByFunctionNegativeCase() {
        Match.as(Number.class)
                .whenApplicable((Double d) -> d).thenApply()
                .whenApplicable((Integer i) -> i).thenApply()
                .apply("x");
    }

    // otherwise

    @Test
    public void shouldHandleOtherwiseValueOfMatched() {
        final boolean actual = Match
                .when(1).then(true)
                .otherwise(false)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldHandleOtherwiseValueOfUnmatched() {
        final boolean actual = Match
                .when(0).then(false)
                .otherwise(true)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldHandleOtherwiseSupplierOfMatched() {
        final boolean actual = Match
                .when(1).then(true)
                .otherwise(() -> false)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldHandleOtherwiseSupplierOfUnmatched() {
        final boolean actual = Match
                .when(0).then(false)
                .otherwise(() -> true)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldHandleOtherwiseFunctionOfMatched() {
        final boolean actual = Match
                .when(1).then(true)
                .otherwise(ignored -> false)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldHandleOtherwiseFunctionOfUnmatched() {
        final boolean actual = Match
                .when(0).then(false)
                .otherwise(ignored -> true)
                .apply(1);
        assertThat(actual).isTrue();
    }
}
