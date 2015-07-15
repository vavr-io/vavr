/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Function1;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class MatchTest {

    // -- type hint

    @Test
    public void shouldSpecifyMatchExpressionType() {
        final Match<Number> toNumber = Match.as(Number.class)
                .whenType(Integer.class).then(i -> i)
                .whenType(String.class).then(BigDecimal::new);
        assertThat(toNumber.apply("1")).isNotNull();
    }

    // -- null handling

    @Test
    public void shouldMatchNullAsPrototype() {
        final int actual = Match
                .whenType(String.class).then(String::length)
                .when(null).then(1)
                .apply(null);
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatchNullAsType() {
        Match
                .when(Integer.class).then(() -> false)
                .when(Integer.class).then(() -> true)
                .apply(null);
    }

    // -- no match

    @Test(expected = MatchError.class)
    public void shouldThrowOnNoMatchByValue() {
        Match.when("1").then(() -> 1).apply("2");
    }

    @Test
    public void shouldGetObjectWhenMatchErrorOccurs() {
        try {
            Match.when("1").then(o -> 1).apply("2");
            fail("No MatchError thrown");
        } catch (MatchError x) {
            assertThat(x.getObject()).isEqualTo("2");
        }
    }

    // -- match by type of function

    @Test
    public void shouldMatchByDoubleOnMultipleCasesUsingTypedParameter() {
        final int actual = Match
                .whenType(Byte.class).then(() -> 1)
                .whenType(Double.class).then(() -> 2)
                .whenType(Integer.class).then(() -> 3)
                .apply(1.0d);
        assertThat(actual).isEqualTo(2);
    }

    @Test
    public void shouldMatchByIntOnMultipleCasesUsingTypedParameter() {
        final int actual = Match
                .whenType(Byte.class).then(b -> (int) b)
                .whenType(Double.class).then(Double::intValue)
                .whenType(Integer.class).then(i -> i)
                .apply(Integer.MAX_VALUE);
        assertThat(actual).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void shouldMatchByAssignableTypeOnMultipleCases() {
        final int actual = Match
                .when(1).then(() -> 'a')
                .whenType(Number.class).then(() -> 'b')
                .otherwise(() -> 'c')
                .apply(2.0d);
        assertThat(actual).isEqualTo('b');
    }

    // -- default case

    @Test
    public void shouldMatchDefaultCaseUsingEagerDefaultValue() {
        final int actual = Match
                .when("x").then(() -> 1)
                .otherwise(() -> 2)
                .apply("default");
        assertThat(actual).isEqualTo(2);
    }

    @Test
    public void shouldMatchDefaultCaseUsingLazyDefaultValue() {
        final int actual = Match
                .when("x").then(() -> 1)
                .otherwise(() -> 2)
                .apply("default");
        assertThat(actual).isEqualTo(2);
    }

    // -- as()

    @Test
    public void shouldCreateWhenOfTypeUsingPrototype() {
        assertThat(Match.as(Object.class).when(1).then(Function1.identity())).isNotNull();
    }

    @Test
    public void shouldCreateWhenOfTypeUsingFunction() {
        assertThat(Match.as(Object.class).whenType(Object.class).then(() -> null)).isNotNull();
    }

    // -- whenIn

    @Test
    public void shouldAllowValidMatchWithMultiPrototypes() {
        final String result = Match
                .whenIn(1, 3, 5, 7).then("true")
                .whenIn(2, 4, 6, 8).then("false")
                .apply(6);

        assertThat(result).isEqualTo("false");
    }

    @Test
    public void shouldAllowInvalidMatchWithMultiPrototypes() {
        final String result = Match
                .whenIn(1, 3, 5, 7).then("true")
                .whenIn(2, 4, 6, 8).then("false")
                .otherwise("Not Found")
                .apply(9);

        assertThat(result).isEqualTo("Not Found");
    }

    // -- primitive types vs objects

    @Test
    public void shouldMatchBoxedPrimitiveBooleanAsBoolean() {
        final boolean actual = Match.whenType(Boolean.class).then(true).apply(true);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchBoolean() {
        final boolean actual = Match.whenType(Boolean.class).then(true).apply(Boolean.TRUE);
        assertThat(actual).isTrue();
    }

    // -- matching prototypes

    @Test
    public void shouldMatchPrimitiveBooleanValueAndApplyBooleanFunction() {
        final int actual = Match
                .when(true).then(b -> 1)
                .when(Boolean.TRUE).then(b -> 2)
                .apply(true);
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void shouldMatchPrimitiveBooleanValueAsBooleanAndApplyBooleanFunction() {
        final int actual = Match
                .when(Boolean.TRUE).then(b -> 1)
                .when(true).then(b -> 2)
                .apply(true);
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void shouldMatchByValuesUsingFunction() {
        final int actual = Match
                .when("1").then((String s) -> 1)
                .apply("1");
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void shouldMatchByValueOnMultipleCases() {
        final int actual = Match
                .when("1").then(o -> 1)
                .when("2").then(o -> 2)
                .when("3").then(o -> 3)
                .apply("2");
        assertThat(actual).isEqualTo(2);
    }

    @Test
    public void shouldCompileObjectIntegerPrototypeCase() {
        // This does *not* compile: new Match.Builder<>().when(1, (int i) -> i);
        // Use this instead: Match.Builder<>().when(1, i -> i);
        Match.when(1).then((Integer i) -> i);
    }

    @Test
    public void shouldCompileUnqualifiedIntegerPrototypeCase() {
        Match.when(1).then(i -> i);
    }

    // -- matching arrays

    @Test
    public void shouldMatchBooleanArray() {
        final int actual = Match.whenType(boolean[].class).then(1).apply(new boolean[]{true});
        assertThat(actual).isEqualTo(1);
    }

    // -- return type of match

    @Test
    public void shouldAllowCommonReturnTypeUsingBuilder() {
        final Match<Number> toNumber = Match
                .whenType(Integer.class).<Number> then(i -> i)
                .whenType(String.class).then(BigDecimal::new);
        final Number number = toNumber.apply("1.0E10");
        assertThat(number).isEqualTo(new BigDecimal("1.0E10"));
    }

    @Test
    public void shouldAllowCommonReturnTypeUsingBuilderAndPrototype() {
        final Match<Number> toNumber = Match.as(Number.class)
                .when(1).then((Integer i) -> i)
                .when("1").then(BigDecimal::new);
        final Number number = toNumber.apply("1");
        assertThat(number).isEqualTo(new BigDecimal("1"));
    }

    @Test
    public void shouldAllowCommonReturnTypeUsingMatchs() {
        final Match<Number> toNumber = Match.as(Number.class)
                .whenType(Integer.class).then(i -> i)
                .whenType(String.class).then(BigDecimal::new);
        final Number number = toNumber.apply("1");
        assertThat(number).isEqualTo(new BigDecimal("1"));
    }

    @Test
    public void shouldAllowCommonReturnTypeUsingMatchsWithPrototype() {
        final Match<Number> toNumber = Match
                .when(1).<Number>then((Integer i) -> i)
                .when("1").then(BigDecimal::new);
        final Number number = toNumber.apply("1");
        assertThat(number).isEqualTo(new BigDecimal("1"));
    }

    // -- lambda type

    @Test
    public void shouldMatchLambdaConsideringTypeHierarchy() {
        final SpecialFunction lambda = String::valueOf;
        final String actual = Match
                .whenType(SameSignatureAsSpecialFunction.class).then(f -> f.apply(1))
                .whenApplicable((Function1<Integer, String> f) -> f.apply(2)).thenApply()
                .apply(lambda);
        assertThat(actual).isEqualTo("2");
    }

    @FunctionalInterface
    interface SpecialFunction extends Function1<Integer, String> {
        @Override
        String apply(Integer i);
    }

    @FunctionalInterface
    interface SameSignatureAsSpecialFunction extends Function1<Integer, String> {
        @Override
        String apply(Integer i);
    }
}
