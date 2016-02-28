/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

public class MatchFunctionTest {

// TODO
//    // when(Object)
//
//    @Test
//    public void shouldMatchNullByValue() {
//        final boolean actual = Match.whenIs(null).then(true).apply(null);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldMatchIntByValue() {
//        final boolean actual = Match.whenIs(1).then(false).whenIs(2).then(true).apply(2);
//        assertThat(actual).isTrue();
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void shouldMatchIntByValueAndThenThrow() {
//        Match.whenIs(1).thenThrow(RuntimeException::new).apply(1);
//    }
//
//    // whenIsIn(Onject...)
//
//    @Test
//    public void shouldMatchNullByValueIn() {
//        final boolean actual = Match.whenIsIn((Object) null).then(true).apply(null);
//        assertThat(actual).isTrue();
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void shouldThrowWhenMatchNullVarargsByValueIn() {
//        Match.whenIsIn((Object[]) null).then(false).apply(null);
//    }
//
//    @Test
//    public void shouldMatchIntByValueIn() {
//        final boolean actual = Match.whenIsIn(1, 2, 3).then(true).apply(2);
//        assertThat(actual).isTrue();
//    }
//
//    // when(Predicate)
//
//    @Test(expected = NullPointerException.class)
//    public void shouldThrowWhenMatchNullVarargsByPredicate() {
//        Match.when(null).then(false).apply(null);
//    }
//
//    @Test
//    public void shouldMatchEvenIntByPredicate() {
//        final String divisibility = Match
//                .when((String s) -> true).then("oops")
//                .when((Integer i) -> i % 2 == 0).then("even")
//                .otherwise("odd")
//                .apply(0);
//        assertThat(divisibility).isEqualTo("even");
//    }
//
//    @Test
//    public void shouldMatchIntByMatcher() {
//        class Matcher {
//            SerializablePredicate<? super Object> is(Object prototype) {
//                return value -> value == prototype || (value != null && value.equals(prototype));
//            }
//        }
//        final Matcher matcher = new Matcher();
//        final boolean actual = Match.when(matcher.is(0)).then(true).apply(0);
//        assertThat(actual).isTrue();
//    }
//
//    // whenType(Class)
//
//    @Test(expected = NullPointerException.class)
//    public void shouldThrowWhenMatchNullByType() {
//        Match.whenType(null).then(false).apply(null);
//    }
//
//    @Test
//    public void shouldMatchNumberByType() {
//        final String actual = Match
//                .whenType(String.class)
//                .then(s -> "String " + s)
//                .whenType(Number.class)
//                .then(n -> "Number " + n)
//                .whenType(Integer.class)
//                .then(i -> "int " + i)
//                .apply(1);
//        assertThat(actual).isEqualTo("Number 1");
//    }
//
//    // whenTypeIn(Class...)
//
//    @Test(expected = NullPointerException.class)
//    public void shouldThrowWhenMatchNullVarargsByTypeIn() {
//        Match.whenTypeIn((Class<?>[]) null).then(false).apply(null);
//    }
//
//    @Test
//    public void shouldMatchNumberByTypeIn() {
//        final Number number = 1;
//        final String actual = Match.whenTypeIn(Byte.class, Integer.class).then(s -> "matched").apply(number);
//        assertThat(actual).isEqualTo("matched");
//    }
//
//    // whenApplicable(Function)
//
//    @Test(expected = NullPointerException.class)
//    public void shouldThrowWhenMatchNullByFunction() {
//        Match.whenApplicable(null).thenApply().apply(null);
//    }
//
//    @Test
//    public void shouldMatchIntByFunction() {
//        final int actual = Match.whenApplicable((Integer i) -> i + 1).thenApply().apply(1);
//        assertThat(actual).isEqualTo(2);
//    }
//
//    // otherwise(R)
//
//    @Test
//    public void shouldMatchDefaultCaseHavingValue() {
//        assertThat(Match.otherwise(true).apply(null)).isTrue();
//        assertThat(Match.otherwise((Object) null).apply(null)).isNull();
//    }
//
//    // otherwise(Function)
//
//    @Test
//    public void shouldMatchDefaultCaseHavingFunction() {
//        assertThat(Match.otherwise(ignored -> true).apply(null)).isTrue();
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void shouldThrowWhenMatchDefaultCaseAndFunctionIsNull() {
//        final Function<Object, String> f = null;
//        Match.otherwise(f).apply(1);
//    }
//
//    // otherwise(Supplier)
//
//    @Test
//    public void shouldMatchDefaultCaseHavingSupplier() {
//        assertThat(Match.otherwise(() -> true).apply(null)).isTrue();
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void shouldThrowWhenMatchDefaultCaseAndSupplierIsNull() {
//        Match.otherwise((Supplier<?>) null).apply(null);
//    }
//
//    // otherwiseRun(Consumer)
//
//    @Test(expected = NullPointerException.class)
//    public void shouldThrowOnOtherwiseRunWhenConsumerIsNull() {
//        Match.otherwiseRun((Consumer<Object>) null);
//    }
//
//    @Test
//    public void shouldPerformActionWhenOtherwiseRunAndConsumerIsNotNull() {
//        final int[] check = { 0 };
//        Match.otherwiseRun(o -> check[0] = 1).accept(null);
//        assertThat(check[0]).isEqualTo(1);
//    }
//
//    // otherwiseRun(Runnable)
//
//    @Test(expected = NullPointerException.class)
//    public void shouldThrowOnOtherwiseRunWhenRunnableIsNull() {
//        Match.otherwiseRun((Runnable) null);
//    }
//
//    @Test
//    public void shouldPerformActionWhenOtherwiseRunAndRunnableIsNotNull() {
//        final int[] check = { 0 };
//        Match.otherwiseRun(() -> check[0] = 1).accept(null);
//        assertThat(check[0]).isEqualTo(1);
//    }
//
//    // otherwiseThrow(Supplier)
//
//    @Test(expected = RuntimeException.class)
//    public void shouldMatchDefaultCaseHavingExceptionSupplier() {
//        Match.otherwiseThrow(RuntimeException::new).apply(null);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void shouldThrowWhenMatchDefaultCaseAndExceptionSupplierIsNull() {
//        Match.otherwiseThrow(null).apply(null);
//    }
//
//    // match by super-type
//
//    @Test
//    public void shouldMatchSuperTypeByValue() {
//        final Option<Integer> option = Option.of(1);
//        final boolean actual = Match.whenIs(option).then(true).apply(Option.some(1));
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldMatchSuperTypeByValueIn() {
//        final Option<Integer> option = Option.of(1);
//        final boolean actual = Match.whenIsIn(Option.none(), option).then(true).apply(Option.some(1));
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldMatchSuperTypeByPredicate() {
//        final boolean actual = Match.when((Option<?> o) -> true).then(true).apply(Option.some(1));
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldMatchSuperTypeByType() {
//        final boolean actual = Match.whenType(Option.class).then(true).apply(Option.some(1));
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldMatchSuperTypeByTypeIn() {
//        final boolean actual = Match.whenTypeIn(Boolean.class, Option.class).then(true).apply(Option.some(1));
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldMatchSuperTypeByFunction() {
//        final boolean actual = Match.whenApplicable((Option<?> o) -> true).thenApply().apply(Option.some(1));
//        assertThat(actual).isTrue();
//    }
//
//    // match by sub-type
//
//    @Test
//    public void shouldMatchSubTypeByValue() {
//        final Option<Integer> option = Option.of(1);
//        final boolean actual = Match.whenIs(Option.some(1)).then(true).apply(option);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldMatchSubTypeByValueIn() {
//        final Option<Integer> option = Option.of(1);
//        final boolean actual = Match.whenIsIn(Option.none(), Option.some(1)).then(true).apply(option);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldMatchSubTypeByPredicate() {
//        final Option<Integer> option = Option.of(1);
//        final boolean actual = Match.when((Option.Some<?> o) -> true).then(true).apply(option);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldMatchSubTypeByType() {
//        final Option<Integer> option = Option.of(1);
//        final boolean actual = Match.whenType(Option.Some.class).then(true).apply(option);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldMatchSubTypeByTypeIn() {
//        final Option<Integer> option = Option.of(1);
//        final boolean actual = Match.whenTypeIn(Boolean.class, Option.Some.class).then(true).apply(option);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldMatchSubTypeByFunction() {
//        final Option<Integer> option = Option.of(1);
//        final boolean actual = Match.whenApplicable((Option.Some<?> o) -> true).thenApply().apply(option);
//        assertThat(actual).isTrue();
//    }
//
//    // should apply then() when matched (honoring When, WhenApplicable)
//
//    @Test
//    public void shouldApplyThenValueWhenMatched() {
//        MatchFunction<Boolean> match = Match.whenIs(1).then(true).whenIs(2).then(false);
//        assertThat(match.apply(1)).isTrue();
//        assertThat(match.apply(2)).isFalse();
//    }
//
//    @Test
//    public void shouldApplyThenSupplierWhenMatched() {
//        MatchFunction<Boolean> match = Match.whenIs(1).then(() -> true).whenIs(2).then(() -> false);
//        assertThat(match.apply(1)).isTrue();
//        assertThat(match.apply(2)).isFalse();
//    }
//
//    @Test
//    public void shouldApplyThenFunctionWhenMatched() {
//        final boolean actual = Match.whenIs(true).then(b -> b).apply(true);
//        assertThat(actual).isTrue();
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void shouldApplyThenThrowFunctionWhenMatched() {
//        Match.whenIs(true).thenThrow(RuntimeException::new).apply(true);
//    }
//
//    @Test
//    public void shouldApplyFunctionWhenApplicableMatched() {
//        final boolean actual = Match.whenApplicable((Boolean b) -> b).thenApply().apply(true);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldRunConsumerWhenMatchedAsFunction() {
//        final int[] check = { 0 };
//        Match.whenIs(1).thenRun(i -> check[0] = i).accept(1);
//        assertThat(check[0]).isEqualTo(1);
//    }
//
//    @Test
//    public void shouldRunRunnableWhenMatchedAsFunction() {
//        final int[] check = { 0 };
//        Match.whenIs(1).thenRun(() -> check[0] = 1).accept(1);
//        assertThat(check[0]).isEqualTo(1);
//    }
//
//    // should not apply then() when unmatched (honoring When, WhenApplicable)
//
//    @Test
//    public void shouldNotApplyThenValueWhenUnmatched() {
//        final boolean actual = Match.whenIs(1).then(false).otherwise(true).apply(0);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldNotApplyThenSupplierWhenUnmatched() {
//        final boolean actual = Match.whenIs(1).then(() -> false).otherwise(true).apply(0);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldNotApplyThenThrowFunctionWhenUnmatched() {
//        final boolean actual = Match
//                .as(Boolean.class)
//                .whenIs(false)
//                .thenThrow(RuntimeException::new)
//                .otherwise(true)
//                .apply(true);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldNotApplyThenFunctionWhenUnmatched() {
//        final boolean actual = Match.whenIs(false).then(b -> b).otherwise(true).apply(true);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldNotApplyFunctionWhenApplicableUnmatched() {
//        final boolean actual = Match.whenApplicable((Integer i) -> false).thenApply().otherwise(true).apply(true);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldNotRunConsumerWhenUnmatchedAsFunction() {
//        final int[] check = { 0 };
//        Match.whenIs(1).thenRun(i -> check[0] = i).accept(-1);
//        assertThat(check[0]).isEqualTo(0);
//    }
//
//    @Test
//    public void shouldNotRunRunnableWhenUnmatchedAsFunction() {
//        final int[] check = { 0 };
//        Match.whenIs(1).thenRun(() -> check[0] = 1).accept(-1);
//        assertThat(check[0]).isEqualTo(0);
//    }
//
//    // transport matched states
//
//    @Test
//    public void shouldTransportMatchedValue() {
//        final boolean actual = Match.whenIs(1).then(true).whenIs(1).then(false).apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldTransportMatchedValueIn() {
//        final boolean actual = Match.whenIsIn(1).then(true).whenIsIn(1).then(false).apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldTransportMatchedPredicate() {
//        final boolean actual = Match
//                .when((Integer i) -> true)
//                .then(true)
//                .when((Integer i) -> true)
//                .then(false)
//                .apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldTransportMatchedType() {
//        final boolean actual = Match.whenType(Integer.class).then(true).whenType(Integer.class).then(false).apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldTransportMatchedTypeIn() {
//        final boolean actual = Match
//                .whenTypeIn(Integer.class)
//                .then(true)
//                .whenTypeIn(Integer.class)
//                .then(false)
//                .apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldTransportMatchedFunction() {
//        final boolean actual = Match
//                .whenApplicable((Integer i) -> true)
//                .thenApply()
//                .whenApplicable((Integer i) -> false)
//                .thenApply()
//                .apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    // transport unmatched states
//
//    @Test
//    public void shouldTransportUnmatchedValue() {
//        final boolean actual = Match.whenIs(0).then(false).whenIs(1).then(true).apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldTransportUnmatchedValueIn() {
//        final boolean actual = Match.whenIsIn(0).then(false).whenIsIn(1).then(true).apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldTransportUnmatchedPredicate() {
//        final boolean actual = Match
//                .when((Boolean b) -> false)
//                .then(false)
//                .when((Integer i) -> true)
//                .then(true)
//                .apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldTransportUnmatchedType() {
//        final boolean actual = Match.whenType(Boolean.class).then(false).whenType(Integer.class).then(true).apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldTransportUnmatchedTypeIn() {
//        final boolean actual = Match
//                .whenTypeIn(Boolean.class)
//                .then(false)
//                .whenTypeIn(Integer.class)
//                .then(true)
//                .apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldTransportUnmatchedFunction() {
//        final boolean actual = Match
//                .whenApplicable((Boolean i) -> false)
//                .thenApply()
//                .whenApplicable((Integer i) -> true)
//                .thenApply()
//                .apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    // should declare common result type of multiple cases
//
//    @Test
//    public void shouldMatchTypedResultByValue() {
//        final Number actual = Match.as(Number.class).whenIs(0).then(0.0d).whenIs(1).then(1).apply(1);
//        assertThat(actual).isEqualTo(1);
//    }
//
//    @Test(expected = MatchError.class)
//    public void shouldMatchTypedResultByValueNegativeCase() {
//        Match.as(Number.class).whenIs(0).then(0.0d).whenIs(1).then(1).apply(-1);
//    }
//
//    @Test
//    public void shouldMatchTypedResultByValueIn() {
//        final Number actual = Match.as(Number.class).whenIsIn(0).then(0.0d).whenIsIn(1).then(1).apply(1);
//        assertThat(actual).isEqualTo(1);
//    }
//
//    @Test(expected = MatchError.class)
//    public void shouldMatchTypedResultByValueInNegativeCase() {
//        Match.as(Number.class).whenIsIn(0).then(0.0d).whenIsIn(1).then(1).apply(-1);
//    }
//
//    @Test
//    public void shouldMatchTypedResultByPredicate() {
//        final Number actual = Match
//                .as(Number.class)
//                .when((Double d) -> true)
//                .then(0.0d)
//                .when((Integer i) -> true)
//                .then(1)
//                .apply(1);
//        assertThat(actual).isEqualTo(1);
//    }
//
//    @Test(expected = MatchError.class)
//    public void shouldMatchTypedResultByPredicateNegativeCase() {
//        Match.as(Number.class).when((Double d) -> true).then(0.0d).when((Integer i) -> true).then(1).apply("x");
//    }
//
//    @Test
//    public void shouldMatchTypedResultByType() {
//        final Number actual = Match
//                .as(Number.class)
//                .whenType(Boolean.class)
//                .then(0.0d)
//                .whenType(Integer.class)
//                .then(1)
//                .apply(1);
//        assertThat(actual).isEqualTo(1);
//    }
//
//    @Test(expected = MatchError.class)
//    public void shouldMatchTypedResultByTypeNegativeCase() {
//        Match.as(Number.class).whenType(Boolean.class).then(0.0d).whenType(Integer.class).then(1).apply("x");
//    }
//
//    @Test
//    public void shouldMatchTypedResultByFunction() {
//        final Number actual = Match
//                .as(Number.class)
//                .whenApplicable((Double d) -> d)
//                .thenApply()
//                .whenApplicable((Integer i) -> i)
//                .thenApply()
//                .apply(1);
//        assertThat(actual).isEqualTo(1);
//    }
//
//    @Test(expected = MatchError.class)
//    public void shouldMatchTypedResultByFunctionNegativeCase() {
//        Match
//                .as(Number.class)
//                .whenApplicable((Double d) -> d)
//                .thenApply()
//                .whenApplicable((Integer i) -> i)
//                .thenApply()
//                .apply("x");
//    }
//
//    @Test
//    public void shouldMatchTypedResultByFunctionThenThrow() {
//        try {
//            Match.as(Number.class)
//                    .whenApplicable((Double d) -> d)
//                    .thenThrow(() -> new RuntimeException("case1"))
//                    .whenApplicable((Integer i) -> i)
//                    .thenThrow(() -> new RuntimeException("case2"))
//                    .apply(1);
//        } catch (RuntimeException x) {
//            assertThat(x.getMessage()).isEqualTo("case2");
//        }
//    }
//
//    @Test(expected = MatchError.class)
//    public void shouldMatchTypedResultByFunctionThenThrowNegativeCase() {
//        Match
//                .as(Number.class)
//                .whenApplicable((Double d) -> d)
//                .thenThrow(() -> new RuntimeException("case1"))
//                .whenApplicable((Integer i) -> i)
//                .thenThrow(() -> new RuntimeException("case2"))
//                .apply("x");
//    }
//
//    // otherwise
//
//    @Test
//    public void shouldHandleOtherwiseValueOfMatched() {
//        final boolean actual = Match.whenIs(1).then(true).otherwise(false).apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldHandleOtherwiseValueOfUnmatched() {
//        final boolean actual = Match.whenIs(0).then(false).otherwise(true).apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldHandleOtherwiseSupplierOfMatched() {
//        final boolean actual = Match.whenIs(1).then(true).otherwise(() -> false).apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldHandleOtherwiseSupplierOfUnmatched() {
//        final boolean actual = Match.whenIs(0).then(false).otherwise(() -> true).apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldHandleOtherwiseFunctionOfMatched() {
//        final boolean actual = Match.whenIs(1).then(true).otherwise(ignored -> false).apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test
//    public void shouldHandleOtherwiseFunctionOfUnmatched() {
//        final boolean actual = Match.whenIs(0).then(false).otherwise(ignored -> true).apply(1);
//        assertThat(actual).isTrue();
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void shouldHandleOtherwiseThrowOfUnmatched() {
//        Match.whenIs(0).then(false).otherwiseThrow(RuntimeException::new).apply(1);
//    }
//
//    // thenThrow
//
//    @Test
//    public void shouldThrowLate() {
//        final Function<?, ?> match = Match.whenIs(null).thenThrow(RuntimeException::new);
//        try {
//            match.apply(null);
//            fail("nothing thrown");
//        } catch (RuntimeException x) {
//            // ok
//        }
//    }
//
//    @Test
//    public void shouldThrowLateWhenMultipleCases() {
//        final Function<?, ?> match = Match.whenIs(0).then(0).whenIs(null).thenThrow(RuntimeException::new).otherwise(0);
//        try {
//            match.apply(null);
//            fail("nothing thrown");
//        } catch (RuntimeException x) {
//            // ok
//        }
//    }
//
//    @Test
//    public void shouldConsumeDefaultCaseWhenNoMatchFound() {
//        final IntegerConsumer integerConsumer = new IntegerConsumer();
//        Match.whenIs(1).thenRun(integerConsumer)
//                .otherwiseRun(i -> {integerConsumer.accept(2);})
//                .accept(2);
//        assertThat(integerConsumer.value).isEqualTo(2);
//    }
//
//    @Test
//    public void shouldConsumeFirstMatchingCase() {
//        final IntegerConsumer integerConsumer = new IntegerConsumer();
//        Consumer<Object> match = Match
//                .when(String::isEmpty).thenRun(i -> {integerConsumer.accept(1);})
//                .whenApplicable((Double i) -> {integerConsumer.accept(2);}).thenRun()
//                .whenIs(3).thenRun(integerConsumer)
//                .whenIsIn(4, 5).thenRun(integerConsumer)
//                .whenType(Float.class).thenRun(i -> {integerConsumer.accept(6);})
//                .whenTypeIn(Boolean.class, Long.class).thenRun(i -> {integerConsumer.accept(7);})
//                .otherwiseRun(i -> {integerConsumer.accept(8);});
//        match.accept("");
//        assertThat(integerConsumer.value).isEqualTo(1);
//        match.accept(1d);
//        assertThat(integerConsumer.value).isEqualTo(2);
//        match.accept(3);
//        assertThat(integerConsumer.value).isEqualTo(3);
//        match.accept(5);
//        assertThat(integerConsumer.value).isEqualTo(5);
//        match.accept(1f);
//        assertThat(integerConsumer.value).isEqualTo(6);
//        match.accept(false);
//        assertThat(integerConsumer.value).isEqualTo(7);
//        match.accept("javaslang");
//        assertThat(integerConsumer.value).isEqualTo(8);
//    }
//
//    @Test
//    public void shouldRunFirstMatchingCase() {
//        final int[] integerConsumer = new int[] { -1 };
//        Consumer<Object> match = Match
//                .when(String::isEmpty).thenRun(() -> integerConsumer[0] = 1)
//                .whenIs(3).thenRun(() -> integerConsumer[0] = 3)
//                .whenIsIn(4, 5).thenRun(() -> integerConsumer[0] = 5)
//                .whenType(Float.class).thenRun(() -> integerConsumer[0] = 6)
//                .whenTypeIn(Boolean.class, Long.class).thenRun(() -> integerConsumer[0] = 7)
//                .otherwiseRun(() -> integerConsumer[0] = 8);
//        match.accept("");
//        assertThat(integerConsumer[0]).isEqualTo(1);
//        match.accept(3);
//        assertThat(integerConsumer[0]).isEqualTo(3);
//        match.accept(5);
//        assertThat(integerConsumer[0]).isEqualTo(5);
//        match.accept(1f);
//        assertThat(integerConsumer[0]).isEqualTo(6);
//        match.accept(false);
//        assertThat(integerConsumer[0]).isEqualTo(7);
//        match.accept("javaslang");
//        assertThat(integerConsumer[0]).isEqualTo(8);
//    }
//
//    @Test
//    public void shouldThrowWhenApplicable() {
//        final SimpleRunnable simpleRunnable = new SimpleRunnable();
//        final Consumer<Object> match = Match
//                .whenIs(1).thenRun(simpleRunnable)
//                .whenApplicable((Double d) -> {}).thenThrow(() -> new IllegalStateException("runnable not consumed"));
//        assertThatThrownBy(() -> match.accept(3d))
//                .isInstanceOf(IllegalStateException.class)
//                .hasMessage("runnable not consumed");
//    }
//
//    @Test
//    public void shouldNotConsumeWhenNoMatchFoundAndNoDefaultCase() {
//        final IntegerConsumer integerConsumer = new IntegerConsumer();
//        Match.whenIs(1).thenRun(integerConsumer)
//                .whenIs(2).thenRun(integerConsumer)
//                .whenType(String.class).thenRun(i -> integerConsumer.accept(3))
//                .accept(10);
//        assertThat(integerConsumer.value).isEqualTo(0);
//    }
//
//    @Test
//    public void shouldOtherwiseThrowWhenNoMatchFoundAndNoDefaultCase() {
//        final SimpleRunnable simpleRunnable = new SimpleRunnable();
//        final Consumer<Object> match = Match
//                .whenIs(1).thenRun(simpleRunnable)
//                .whenIs(2).thenRun(simpleRunnable)
//                .otherwiseThrow(() -> new IllegalStateException("runnable not consumed"));
//        assertThatThrownBy(() -> match.accept(3))
//                .isInstanceOf(IllegalStateException.class)
//                .hasMessage("runnable not consumed");
//    }
//
//    @Test
//    public void shouldAllowToDefineRunnableAsDefaultCase() {
//        final SimpleRunnable simpleRunnable = new SimpleRunnable();
//        Match.whenIs(1).thenRun(simpleRunnable)
//                .otherwiseRun(simpleRunnable)
//                .accept(2);
//        assertThat(simpleRunnable.executed).isEqualTo(true);
//    }
//
//    @Test
//    public void shouldAllowToDefineRunnableAsMatchingAction() {
//        final SimpleRunnable simpleRunnable = new SimpleRunnable();
//        final Consumer<Object> match = Match
//                .whenIs(1).thenRun(simpleRunnable)
//                .whenIs(2).thenRun(simpleRunnable)
//                .when(o -> !simpleRunnable.executed).thenThrow(() -> new IllegalStateException("runnable not consumed"));
//        assertThatThrownBy(() -> match.accept(3))
//                .isInstanceOf(IllegalStateException.class)
//                .hasMessage("runnable not consumed");
//    }
}
