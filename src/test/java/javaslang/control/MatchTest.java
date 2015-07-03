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
                .when((Integer i) -> i)
                .when((String s) -> new BigDecimal(s));
        assertThat(toNumber.apply("1")).isNotNull();
    }

    // -- null handling

    @Test
    public void shouldMatchNullAsPrototype() {
        final int actual = Match.when((String s) -> s.length()).whenNull().then(o -> 1).apply(null);
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatchNullAsType() {
        Match.when((Integer i) -> false).when((Integer i) -> true).apply(null);
    }

    // -- no match

    @Test(expected = MatchError.class)
    public void shouldThrowOnNoMatchByValue() {
        Match.when("1").then(o -> 1).apply("2");
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
        final int actual = Match.when((Byte b) -> 1).when((Double d) -> 2).when((Integer i) -> 3).apply(1.0d);
        assertThat(actual).isEqualTo(2);
    }

    @Test
    public void shouldMatchByIntOnMultipleCasesUsingTypedParameter() {
        final int actual = Match
                .when((Byte b) -> (int) b)
                .when((Double d) -> d.intValue())
                .when((Integer i) -> i)
                .apply(Integer.MAX_VALUE);
        assertThat(actual).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void shouldMatchByAssignableTypeOnMultipleCases() {
        final int actual = Match.when(1).then(o -> 'a').when((Number n) -> 'b').when((Object o) -> 'c').apply(2.0d);
        assertThat(actual).isEqualTo('b');
    }

    // -- default case

    @Test
    public void shouldMatchDefaultCaseUsingEagerDefaultValue() {
        final int actual = Match.when("x").then(o -> 1).otherwise(2).apply("default");
        assertThat(actual).isEqualTo(2);
    }

    @Test
    public void shouldMatchDefaultCaseUsingLazyDefaultValue() {
        final int actual = Match.when("x").then(o -> 1).otherwise(() -> 2).apply("default");
        assertThat(actual).isEqualTo(2);
    }

    // -- generics vs type erasure

    @Test
    public void shouldClarifyHereThatTypeErasureIsPresent() {
        final int actual = Match
                .when((Some<Integer> some) -> 1)
                .when((Some<String> some) -> Integer.parseInt(some.get()))
                .apply(new Some<>("123"));
        assertThat(actual).isEqualTo(1);
    }

    // -- as()

    @Test
    public void shouldCreateWhenOfTypeUsingPrototype() {
        assertThat(Match.as(Object.class).when(1).then(Function1.identity())).isNotNull();
    }

    @Test
    public void shouldCreateWhenOfTypeUsingFunction() {
        assertThat(Match.as(Object.class).when((Object o) -> null)).isNotNull();
    }

    // -- whenIn

    @Test
    public void shouldAllowValidMatchWithMultiPrototypes() {
        final String result = Match
                .whenIn(1, 3, 5, 7).then(() -> "true")
                .whenIn(2, 4, 6, 8).then(() -> "false")
                .apply(6);

        assertThat(result).isEqualTo("false");
    }

    @Test
    public void shouldAllowInvalidMatchWithMultiPrototypes() {
        final String result = Match
                .whenIn(1, 3, 5, 7).then(() -> "true")
                .whenIn(2, 4, 6, 8).then(() -> "false")
                .otherwise("Not Found")
                .apply(9);

        assertThat(result).isEqualTo("Not Found");
    }

    // -- primitive types vs objects

    // boolean / Boolean

    @Test
    public void shouldMatchBoxedPrimitiveBooleanAsBoolean() {
        final boolean actual = Match.when((Boolean b) -> true).apply(true);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchBoolean() {
        final boolean actual = Match.when((Boolean b) -> true).apply(Boolean.TRUE);
        assertThat(actual).isTrue();
    }

    // byte / Byte

    @Test
    public void shouldMatchBoxedPrimitiveByteAsByte() {
        final boolean actual = Match.when((Byte b) -> true).apply((byte) 1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchByte() {
        final Byte one = 1;
        final boolean actual = Match.when((Byte b) -> true).apply(one);
        assertThat(actual).isTrue();
    }

    // char / Character

    @Test
    public void shouldMatchBoxedPrimitiveCharAsCharacter() {
        final boolean actual = Match.when((Character c) -> true).apply('#');
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchCharacter() {
        final boolean actual = Match.when((Character c) -> true).apply(Character.valueOf('#'));
        assertThat(actual).isTrue();
    }

    // double / Double

    @Test
    public void shouldMatchBoxedPrimitiveDoubleAsDouble() {
        final boolean actual = Match.when((Double d) -> true).apply((double) 1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchDouble() {
        final boolean actual = Match.when((Double d) -> true).apply(new Double(1));
        assertThat(actual).isTrue();
    }

    // float / Float

    @Test
    public void shouldMatchBoxedPrimitiveFloatAsFloat() {
        final boolean actual = Match.when((Float f) -> true).apply((float) 1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchFloat() {
        final boolean actual = Match.when((Float f) -> true).apply(new Float(1));
        assertThat(actual).isTrue();
    }

    // int / Integer

    @Test
    public void shouldMatchBoxedPrimitiveIntAsInteger() {
        final boolean actual = Match.when((Integer i) -> true).apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchInteger() {
        final boolean actual = Match.when((Integer i) -> true).apply(new Integer(1));
        assertThat(actual).isTrue();
    }

    // long / Long

    @Test
    public void shouldMatchBoxedPrimitiveLongAsLong() {
        final boolean actual = Match.when((Long l) -> true).apply(1L);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchLong() {
        final boolean actual = Match.when((Long l) -> true).apply(new Long(1));
        assertThat(actual).isTrue();
    }

    // short / Short

    @Test
    public void shouldMatchBoxedPrimitiveShortAsShort() {
        final boolean actual = Match.when((Short s) -> true).apply((short) 1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchShort() {
        final boolean actual = Match.when((Short s) -> true).apply(new Short((short) 1));
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
        final int actual = Match.when((boolean[] b) -> 1).apply(new boolean[]{true});
        assertThat(actual).isEqualTo(1);
    }

    // -- return type of match

    @Test
    public void shouldAllowCommonReturnTypeUsingBuilder() {
        final Match<Number> toNumber = Match.as(Number.class)
                .when((Integer i) -> i)
                .when((String s) -> new BigDecimal(s));
        final Number number = toNumber.apply("1.0E10");
        assertThat(number).isEqualTo(new BigDecimal("1.0E10"));
    }

    @Test
    public void shouldAllowCommonReturnTypeUsingBuilderAndPrototype() {
        final Match<Number> toNumber = Match.as(Number.class)
                .when(1).then((Integer i) -> i)
                .when("1").then((String s) -> new BigDecimal(s));
        final Number number = toNumber.apply("1");
        assertThat(number).isEqualTo(new BigDecimal("1"));
    }

    @Test
    public void shouldAllowCommonReturnTypeUsingMatchs() {
        final Match<Number> toNumber = Match
                .<Number>when((Integer i) -> i)
                .when((String s) -> new BigDecimal(s));
        final Number number = toNumber.apply("1");
        assertThat(number).isEqualTo(new BigDecimal("1"));
    }

    @Test
    public void shouldAllowCommonReturnTypeUsingMatchsWithPrototype() {
        final Match<Number> toNumber = Match
                .when(1).<Number> then((Integer i) -> i)
                .when("1").then((String s) -> new BigDecimal(s));
        final Number number = toNumber.apply("1");
        assertThat(number).isEqualTo(new BigDecimal("1"));
    }

    // -- lambda type

    @Test
    public void shouldMatchLambdaConsideringTypeHierarchy() {
        final SpecialFunction lambda = i -> String.valueOf(i);
        final String actual = Match
                .when((SameSignatureAsSpecialFunction f) -> f.apply(1))
                .when((Function1<Integer, String> f) -> f.apply(2))
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
