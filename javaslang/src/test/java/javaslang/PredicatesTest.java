/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static javaslang.Predicates.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PredicatesTest {

    static final Predicate<? super Throwable> IS_RUNTIME_EXCEPTION = instanceOf(RuntimeException.class);

    // -- instanceOf

    @Test
    public void shouldTestInstanceOf_PositiveCase() {
        assertThat(instanceOf(Number.class).test(1)).isTrue();
        assertThat(instanceOf(Number.class).test(new BigDecimal("1"))).isTrue();
        assertThat(IS_RUNTIME_EXCEPTION.test(new NullPointerException())).isTrue();
    }

    @Test
    public void shouldTestInstanceOf_NegativeCase() {
        assertThat(IS_RUNTIME_EXCEPTION.test(new Exception())).isFalse();
        assertThat(IS_RUNTIME_EXCEPTION.test(new Error("error"))).isFalse();
        assertThat(IS_RUNTIME_EXCEPTION.test(null)).isFalse();
    }

    // -- is

    @Test
    public void shouldTestIs_PositiveCase() {
        assertThat(is(1).test(1)).isTrue();
        assertThat(is((CharSequence) "1").test("1")).isTrue();
    }

    @Test
    public void shouldTestIs_NegativeCase() {
        assertThat(is(1).test(2)).isFalse();
        assertThat(is((CharSequence) "1").test(new StringBuilder("1"))).isFalse();
    }

    // -- isIn

    @Test
    public void shouldTestIsIn_PositiveCase() {
        assertThat(isIn(1, 2, 3).test(2)).isTrue();
        assertThat(isIn((CharSequence) "1", "2", "3").test("2")).isTrue();
    }

    @Test
    public void shouldTestIsIn_NegativeCase() {
        assertThat(isIn(1, 2, 3).test(4)).isFalse();
        assertThat(isIn((CharSequence) "1", "2", "3").test("4")).isFalse();
    }

    // Predicate Combinators

    // -- allOf

    static final Predicate<Integer> P1 = i -> i > 1;
    static final Predicate<? super Integer> P2 = i -> i > 2;

    @Test
    public void shouldTestAllOf_PositiveCase() {
        assertThat(allOf().test(1)).isTrue();
        assertThat(allOf(P1, P2).test(3)).isTrue();
    }

    @Test
    public void shouldTestAllOf_NegativeCase() {
        assertThat(allOf(P1, P2).test(2)).isFalse();
    }

    // -- anyOf

    @Test
    public void shouldTestAnyOf_PositiveCase() {
        assertThat(anyOf(P1, P2).test(3)).isTrue();
        assertThat(anyOf(P1, P2).test(2)).isTrue();
    }

    @Test
    public void shouldTestAnyOf_NegativeCase() {
        assertThat(anyOf().test(1)).isFalse();
        assertThat(anyOf(P1, P2).test(1)).isFalse();
    }

    // -- noneOf

    @Test
    public void shouldTestNoneOf_PositiveCase() {
        assertThat(noneOf().test(1)).isTrue();
        assertThat(noneOf(P1, P2).test(1)).isTrue();
    }

    @Test
    public void shouldTestNoneOf_NegativeCase() {
        assertThat(noneOf(P1).test(2)).isFalse();
        assertThat(noneOf(P1, P2).test(2)).isFalse();
    }

    // -- isNull

    @Test
    public void shouldTestIsNull_PositiveCase() {
        assertThat(isNull().test(null)).isTrue();
    }

    @Test
    public void shouldTestIsNull_NegativeCase() {
        assertThat(isNull().test("")).isFalse();
    }

    // -- isNotNull

    @Test
    public void shouldTestIsNotNull_PositiveCase() {
        assertThat(isNotNull().test("")).isTrue();
    }

    @Test
    public void shouldTestIsNotNull_NegativeCase() {
        assertThat(isNotNull().test(null)).isFalse();
    }

}
