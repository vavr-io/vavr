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
        final int actual = Match.when((String s) -> s.length()).when(null, o -> 1).apply(null);
        assertThat(actual).isEqualTo(1);
    }

    @Test(expected = MatchError.class)
    public void shouldNotMatchNullAsType() {
        Match.when((int i) -> false).when((Integer i) -> true).apply(null);
    }

    // -- no match

    @Test(expected = MatchError.class)
    public void shouldThrowOnNoMatchByValue() {
        Match.when("1", o -> 1).apply("2");
    }

    @Test
    public void shouldGetObjectWhenMatchErrorOccurs() {
        try {
            Match.when("1", o -> 1).apply("2");
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
        final int actual = Match.when(1, o -> 'a').when((Number n) -> 'b').when((Object o) -> 'c').apply(2.0d);
        assertThat(actual).isEqualTo('b');
    }

    // -- default case

    @Test
    public void shouldMatchDefaultCaseUsingEagerDefaultValue() {
        final int actual = Match.when(null, o -> 1).otherwise(2).apply("default");
        assertThat(actual).isEqualTo(2);
    }

    @Test
    public void shouldMatchDefaultCaseUsingLazyDefaultValue() {
        final int actual = Match.when(null, o -> 1).otherwise(() -> 2).apply("default");
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
        assertThat(Match.as(Object.class).when(null, Function1.identity())).isNotNull();
    }

    @Test
    public void shouldCreateWhenOfTypeUsingFunction() {
        assertThat(Match.as(Object.class).when((Object o) -> null)).isNotNull();
    }

    @Test
    public void shouldCreateWhenOfTypeUsingBooleanFunction() {
        assertThat(Match.as(Object.class).when((boolean b) -> null)).isNotNull();
    }

    @Test
    public void shouldCreateWhenOfTypeUsingByteFunction() {
        assertThat(Match.as(Object.class).when((byte b) -> null)).isNotNull();
    }

    @Test
    public void shouldCreateWhenOfTypeUsingCharFunction() {
        assertThat(Match.as(Object.class).when((char b) -> null)).isNotNull();
    }

    @Test
    public void shouldCreateWhenOfTypeUsingDoubleFunction() {
        assertThat(Match.as(Object.class).when((double b) -> null)).isNotNull();
    }

    @Test
    public void shouldCreateWhenOfTypeUsingFloatFunction() {
        assertThat(Match.as(Object.class).when((float b) -> null)).isNotNull();
    }

    @Test
    public void shouldCreateWhenOfTypeUsingIntFunction() {
        assertThat(Match.as(Object.class).when((int b) -> null)).isNotNull();
    }

    @Test
    public void shouldCreateWhenOfTypeUsingLongFunction() {
        assertThat(Match.as(Object.class).when((long b) -> null)).isNotNull();
    }

    @Test
    public void shouldCreateWhenOfTypeUsingShortFunction() {
        assertThat(Match.as(Object.class).when((short b) -> null)).isNotNull();
    }

    // -- primitive types vs objects

    // boolean / Boolean

    @Test
    public void shouldMatchPrimitiveBoolean() {
        final boolean actual = Match.when((boolean b) -> true).when((Boolean b) -> false).apply(true);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchBoxedPrimitiveBooleanAsBoolean() {
        final boolean actual = Match.when((Boolean b) -> true).when((boolean b) -> false).apply(true);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchBooleanAsPrimitiveBoolean() {
        final boolean actual = Match.when((boolean b) -> true).when((Boolean b) -> false).apply(Boolean.TRUE);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchBoolean() {
        final boolean actual = Match.when((Boolean b) -> true).when((boolean b) -> false).apply(Boolean.TRUE);
        assertThat(actual).isTrue();
    }

    // byte / Byte

    @Test
    public void shouldMatchPrimitiveByte() {
        final boolean actual = Match.when((byte b) -> true).when((Byte b) -> false).apply((byte) 1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchBoxedPrimitiveByteAsByte() {
        final boolean actual = Match.when((Byte b) -> true).when((byte b) -> false).apply((byte) 1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchByteAsPrimitiveByte() {
        final Byte one = 1;
        final boolean actual = Match.when((byte b) -> true).when((Byte b) -> false).apply(one);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchByte() {
        final Byte one = 1;
        final boolean actual = Match.when((Byte b) -> true).when((byte b) -> false).apply(one);
        assertThat(actual).isTrue();
    }

    // char / Character

    @Test
    public void shouldMatchPrimitiveChar() {
        final boolean actual = Match.when((char c) -> true).when((Character c) -> false).apply('#');
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchBoxedPrimitiveCharAsCharacter() {
        final boolean actual = Match.when((Character c) -> true).when((char c) -> false).apply('#');
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchCharacterAsPrimitiveChar() {
        final boolean actual = Match.when((char c) -> true).when((Character c) -> false).apply(Character.valueOf('#'));
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchCharacter() {
        final boolean actual = Match.when((Character c) -> true).when((char c) -> false).apply(Character.valueOf('#'));
        assertThat(actual).isTrue();
    }

    // double / Double

    @Test
    public void shouldMatchPrimitiveDouble() {
        final boolean actual = Match.when((double d) -> true).when((Double d) -> false).apply((double) 1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchBoxedPrimitiveDoubleAsDouble() {
        final boolean actual = Match.when((Double d) -> true).when((double d) -> false).apply((double) 1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchDoubleAsPrimitiveDouble() {
        final boolean actual = Match.when((double d) -> true).when((Double d) -> false).apply(new Double(1));
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchDouble() {
        final boolean actual = Match.when((Double d) -> true).when((double d) -> false).apply(new Double(1));
        assertThat(actual).isTrue();
    }

    // float / Float

    @Test
    public void shouldMatchPrimitiveFloat() {
        final boolean actual = Match.when((float f) -> true).when((Float f) -> false).apply((float) 1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchBoxedPrimitiveFloatAsFloat() {
        final boolean actual = Match.when((Float f) -> true).when((float f) -> false).apply((float) 1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchFloatAsPrimitiveFloat() {
        final boolean actual = Match.when((float f) -> true).when((Float f) -> false).apply(new Float(1));
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchFloat() {
        final boolean actual = Match.when((Float f) -> true).when((float f) -> false).apply(new Float(1));
        assertThat(actual).isTrue();
    }

    // int / Integer

    @Test
    public void shouldMatchPrimitiveInt() {
        final boolean actual = Match.when((int i) -> true).when((Integer i) -> false).apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchBoxedPrimitiveIntAsInteger() {
        final boolean actual = Match.when((Integer i) -> true).when((int i) -> false).apply(1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchIntegerAsPrimitiveInt() {
        final boolean actual = Match.when((int i) -> true).when((Integer i) -> false).apply(new Integer(1));
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchInteger() {
        final boolean actual = Match.when((Integer i) -> true).when((int i) -> false).apply(new Integer(1));
        assertThat(actual).isTrue();
    }

    // long / Long

    @Test
    public void shouldMatchPrimitiveLong() {
        final boolean actual = Match.when((long l) -> true).when((Long l) -> false).apply(1L);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchBoxedPrimitiveLongAsLong() {
        final boolean actual = Match.when((Long l) -> true).when((long l) -> false).apply(1L);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchLongAsPrimitiveLong() {
        final boolean actual = Match.when((long l) -> true).when((Long l) -> false).apply(new Long(1));
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchLong() {
        final boolean actual = Match.when((Long l) -> true).when((long l) -> false).apply(new Long(1));
        assertThat(actual).isTrue();
    }

    // short / Short

    @Test
    public void shouldMatchPrimitiveShort() {
        final boolean actual = Match.when((short s) -> true).when((Short s) -> false).apply((short) 1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchBoxedPrimitiveShortAsShort() {
        final boolean actual = Match.when((Short s) -> true).when((short s) -> false).apply((short) 1);
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchShortAsPrimitiveShort() {
        final boolean actual = Match.when((short s) -> true).when((Short s) -> false).apply(new Short((short) 1));
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchShort() {
        final boolean actual = Match.when((Short s) -> true).when((short s) -> false).apply(new Short((short) 1));
        assertThat(actual).isTrue();
    }

    // -- matching prototypes

    @Test
    public void shouldMatchPrimitiveBooleanValueAndApplyBooleanFunction() {
        final int actual = Match.when(true, b -> 1).when(Boolean.TRUE, b -> 2).apply(true);
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void shouldMatchPrimitiveBooleanValueAsBooleanAndApplyBooleanFunction() {
        final int actual = Match.when(Boolean.TRUE, b -> 1).when(true, b -> 2).apply(true);
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void shouldMatchByValuesUsingFunction() {
        final int actual = Match.when("1", (String s) -> 1).apply("1");
        assertThat(actual).isEqualTo(1);
    }

    @Test
    public void shouldMatchByValueOnMultipleCases() {
        final int actual = Match.when("1", o -> 1).when("2", o -> 2).when("3", o -> 3).apply("2");
        assertThat(actual).isEqualTo(2);
    }

    @Test
    public void shouldCompileObjectIntegerPrototypeCase() {
        // This does *not* compile: new Match.Builder<>().when(1, (int i) -> i);
        // Use this instead: Match.Builder<>().when(1, i -> i);
        Match.when(1, (Integer i) -> i);
    }

    @Test
    public void shouldCompileUnqualifiedIntegerPrototypeCase() {
        Match.when(1, i -> i);
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
                .when(1, (Integer i) -> i)
                .when("1", (String s) -> new BigDecimal(s));
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
                .<Integer, Number>when(1, (Integer i) -> i)
                .when("1", (String s) -> new BigDecimal(s));
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
