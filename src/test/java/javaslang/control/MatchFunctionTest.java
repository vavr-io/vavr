/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Function1;
import org.junit.Test;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class MatchFunctionTest {

    // when(Object)

    @Test
    public void shouldMatchNullByValue() {
        final boolean actual = Match
                .whenIs(null).then(true)
                .apply(null);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchIntByValue() {
        final boolean actual = Match
                .whenIs(1).then(false)
                .whenIs(2).then(true)
                .apply(2);
        assertThat(actual).isTrue();
    }

    @Test(expected = RuntimeException.class)
    public void shouldMatchIntByValueAndThenThrow() {
        Match.whenIs(1).thenThrow(RuntimeException::new).apply(1);
    }

    // whenIsIn(Onject...)

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMatchNullByValueIn() {
        final boolean actual = Match
                .whenIsIn((Object) null).then(true)
                .apply(null);
        assertThat(actual).isTrue();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMatchNullVarargsByValueIn() {
        Match.whenIsIn((Object[]) null).then(false)
                .apply(null);
    }

    @Test
    public void shouldMatchIntByValueIn() {
        final boolean actual = Match
                .whenIsIn(1, 2, 3).then(true)
                .apply(2);
        assertThat(actual).isTrue();
    }

    // when(Predicate)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMatchNullVarargsByPredicate() {
        Match.when(null).then(false)
                .apply(null);
    }

    @Test
    public void shouldMatchEvenIntByPredicate() {
        final String divisibility = Match
                .when((String s) -> true).then("oops")
                .when((Integer i) -> i % 2 == 0).then("even")
                .otherwise("odd")
                .apply(0);
        assertThat(divisibility).isEqualTo("even");
    }

    @Test
    public void shouldMatchIntByMatcher() {
        class Matcher {
            Function1<? super Object, Boolean> is(Object prototype) {
                return value -> value == prototype || (value != null && value.equals(prototype));
            }
        }
        final Matcher matcher = new Matcher();
        final boolean actual = Match
                .when(matcher.is(0)).then(true)
                .apply(0);
        assertThat(actual).isTrue();
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

    // whenApplicable(Function)

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

    // otherwise(R)

    @Test
    public void shouldMatchDefaultCaseHavingValue() {
        assertThat(Match.otherwise(true).apply(null)).isTrue();
        assertThat(Match.otherwise((Object) null).apply(null)).isNull();
    }

    // otherwise(Function)

    @Test
    public void shouldMatchDefaultCaseHavingFunction() {
        assertThat(Match.otherwise(ignored -> true).apply(null)).isTrue();
    }

    // DOES CALL Match.otherwise(R value) instead of Match.otherwise(Function)
    //    @Test(expected = NullPointerException.class)
    //    public void shouldThrowWhenMatchDefaultCaseAndFunctionIsNull() {
    //        final Function<?, ?> f = null;
    //        Match.otherwise(f).apply(null);
    //    }

    // otherwise(Supplier)

    @Test
    public void shouldMatchDefaultCaseHavingSupplier() {
        assertThat(Match.otherwise(() -> true).apply(null)).isTrue();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMatchDefaultCaseAndSupplierIsNull() {
        Match.otherwise((Supplier<?>) null).apply(null);
    }

    // otherwiseThrow(Supplier)

    @Test(expected = RuntimeException.class)
    public void shouldMatchDefaultCaseHavingExceptionSupplier() {
        Match.otherwiseThrow(RuntimeException::new).apply(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenMatchDefaultCaseAndExceptionSupplierIsNull() {
        Match.otherwiseThrow(null).apply(null);
    }

    // match by super-type

    @Test
    public void shouldMatchSuperTypeByValue() {
        final Option<Integer> option = Option.of(1);
        final boolean actual = Match
                .whenIs(option).then(true)
                .apply(new Some<>(1));
        assertThat(actual).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMatchSuperTypeByValueIn() {
        final Option<Integer> option = Option.of(1);
        final boolean actual = Match
                .whenIsIn(None.instance(), option).then(true)
                .apply(new Some<>(1));
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchSuperTypeByPredicate() {
        final boolean actual = Match
                .when((Option<?> o) -> true).then(true)
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
                .whenIs(new Some<>(1)).then(true)
                .apply(option);
        assertThat(actual).isTrue();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMatchSubTypeByValueIn() {
        final Option<Integer> option = Option.of(1);
        final boolean actual = Match
                .whenIsIn(None.instance(), new Some<>(1)).then(true)
                .apply(option);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchSubTypeByPredicate() {
        final Option<Integer> option = Option.of(1);
        final boolean actual = Match
                .when((Some<?> o) -> true).then(true)
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
        final boolean actual = Match.whenIs(1).then(true).apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldApplyThenSupplierWhenMatched() {
        final boolean actual = Match.whenIs(1).then(() -> true).apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldApplyThenFunctionWhenMatched() {
        final boolean actual = Match.whenIs(true).then(b -> b).apply(true);
        assertThat(actual).isTrue();
    }

    @Test(expected = RuntimeException.class)
    public void shouldApplyThenThrowFunctionWhenMatched() {
        Match.whenIs(true).thenThrow(RuntimeException::new).apply(true);
    }

    @Test
    public void shouldApplyFunctionWhenApplicableMatched() {
        final boolean actual = Match.whenApplicable((Boolean b) -> b).thenApply().apply(true);
        assertThat(actual).isTrue();
    }

    // should not apply then() when unmatched (honoring When, WhenApplicable)

    @Test
    public void shouldNotApplyThenValueWhenUnmatched() {
        final boolean actual = Match.whenIs(1).then(false).otherwise(true).apply(0);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldNotApplyThenSupplierWhenUnmatched() {
        final boolean actual = Match.whenIs(1).then(() -> false).otherwise(true).apply(0);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldNotApplyThenThrowFunctionWhenUnmatched() {
        final boolean actual = Match.as(Boolean.class).whenIs(false).thenThrow(RuntimeException::new).otherwise(true).apply(true);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldNotApplyThenFunctionWhenUnmatched() {
        final boolean actual = Match.whenIs(false).then(b -> b).otherwise(true).apply(true);
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
                .whenIs(1).then(true)
                .whenIs(1).then(false)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldTransportMatchedValueIn() {
        final boolean actual = Match
                .whenIsIn(1).then(true)
                .whenIsIn(1).then(false)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldTransportMatchedPredicate() {
        final boolean actual = Match
                .when((Integer i) -> true).then(true)
                .when((Integer i) -> true).then(false)
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
                .whenIs(0).then(false)
                .whenIs(1).then(true)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldTransportUnmatchedValueIn() {
        final boolean actual = Match
                .whenIsIn(0).then(false)
                .whenIsIn(1).then(true)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldTransportUnmatchedPredicate() {
        final boolean actual = Match
                .when((Boolean b) -> false).then(false)
                .when((Integer i) -> true).then(true)
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
                .whenIs(0).then(0.0d)
                .whenIs(1).then(1)
                .apply(1);
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = MatchError.class)
    public void shouldMatchTypedResultByValueNegativeCase() {
        Match.as(Number.class)
                .whenIs(0).then(0.0d)
                .whenIs(1).then(1)
                .apply(-1);
    }

    @Test
    public void shouldMatchTypedResultByValueIn() {
        final Number actual = Match.as(Number.class)
                .whenIsIn(0).then(0.0d)
                .whenIsIn(1).then(1)
                .apply(1);
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = MatchError.class)
    public void shouldMatchTypedResultByValueInNegativeCase() {
        Match.as(Number.class)
                .whenIsIn(0).then(0.0d)
                .whenIsIn(1).then(1)
                .apply(-1);
    }

    @Test
    public void shouldMatchTypedResultByPredicate() {
        final Number actual = Match.as(Number.class)
                .when((Double d) -> true).then(0.0d)
                .when((Integer i) -> true).then(1)
                .apply(1);
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = MatchError.class)
    public void shouldMatchTypedResultByPredicateNegativeCase() {
        Match.as(Number.class)
                .when((Double d) -> true).then(0.0d)
                .when((Integer i) -> true).then(1)
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

    @Test
    public void shouldMatchTypedResultByFunctionThenThrow() {
        try {
            final Number actual = Match.as(Number.class)
                    .whenApplicable((Double d) -> d).thenThrow(() -> new RuntimeException("case1"))
                    .whenApplicable((Integer i) -> i).thenThrow(() -> new RuntimeException("case2"))
                    .apply(1);
        } catch (RuntimeException x) {
            assertThat(x.getMessage()).isEqualTo("case2");
        }
    }

    @Test(expected = MatchError.class)
    public void shouldMatchTypedResultByFunctionThenThrowNegativeCase() {
        Match.as(Number.class)
                .whenApplicable((Double d) -> d).thenThrow(() -> new RuntimeException("case1"))
                .whenApplicable((Integer i) -> i).thenThrow(() -> new RuntimeException("case2"))
                .apply("x");
    }

    // otherwise

    @Test
    public void shouldHandleOtherwiseValueOfMatched() {
        final boolean actual = Match
                .whenIs(1).then(true)
                .otherwise(false)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldHandleOtherwiseValueOfUnmatched() {
        final boolean actual = Match
                .whenIs(0).then(false)
                .otherwise(true)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldHandleOtherwiseSupplierOfMatched() {
        final boolean actual = Match
                .whenIs(1).then(true)
                .otherwise(() -> false)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldHandleOtherwiseSupplierOfUnmatched() {
        final boolean actual = Match
                .whenIs(0).then(false)
                .otherwise(() -> true)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldHandleOtherwiseFunctionOfMatched() {
        final boolean actual = Match
                .whenIs(1).then(true)
                .otherwise(ignored -> false)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldHandleOtherwiseFunctionOfUnmatched() {
        final boolean actual = Match
                .whenIs(0).then(false)
                .otherwise(ignored -> true)
                .apply(1);
        assertThat(actual).isTrue();
    }

    @Test(expected = RuntimeException.class)
    public void shouldHandleOtherwiseThrowOfUnmatched() {
        Match.whenIs(0).then(false)
                .otherwiseThrow(RuntimeException::new)
                .apply(1);
    }

    // thenThrow

    @Test
    public void shouldThrowLate() {
        final Function<?, ?> match = Match.whenIs(null).thenThrow(RuntimeException::new);
        try {
            match.apply(null);
            fail("nothing thrown");
        } catch (RuntimeException x) {
            // ok
        }
    }

    @Test
    public void shouldThrowLateWhenMultipleCases() {
        final Function<?, ?> match = Match
                .whenIs(0).then(0)
                .whenIs(null).thenThrow(RuntimeException::new)
                .otherwise(0);
        try {
            match.apply(null);
            fail("nothing thrown");
        } catch (RuntimeException x) {
            // ok
        }
    }
}
