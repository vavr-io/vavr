/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchMonadTest {

    // when(Object)

    @Test
    public void shouldMatchByValue() {
        final int i = 2;
        final boolean actual = Match.of(i)
                .when(1).then(false)
                .when(2).then(true)
                .get();
        assertThat(actual).isTrue();
    }

    // whenIn(Onject...)

    @Test
    public void shouldMatchByValues() {
        final int i = 2;
        final boolean actual = Match.of(i)
                .whenIn(1, 2, 3).then(true)
                .get();
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldMatchOrElseByValues() {
        final int i = 4;
        final boolean actual = Match.of(i)
                .whenIn(1, 2, 3).then(false)
                .orElse(true);
        assertThat(actual).isTrue();
    }

    // whenApplicable(Function1)

    @Test
    public void shouldMatchByFunction() {
        final int actual = Match.of(1)
                .whenApplicable((Integer i) -> i + 1).thenApply()
                .get();
        assertThat(actual).isEqualTo(2);
    }

    @Test
    public void shouldMatchOrElseByFunction() {
        final int i = 4;
        final boolean actual = Match.of(i)
                .whenIn(1, 2, 3).then(false)
                .orElse(true);
        assertThat(actual).isTrue();
    }

    // whenTrue(Predicate)

    @Test
    public void shouldMatchByPredicate() {
        final String divisibility = Match.of(0)
                .whenTrue((String s) -> true).then("oops")
                .whenTrue((Integer i) -> i % 2 == 0).then("even")
                .orElse("odd");
        assertThat(divisibility).isEqualTo("even");
    }

    @Test
    public void shouldMatchOrElseByPredicate() {
        final String divisibility = Match.of(1)
                .whenTrue((String s) -> true).then("oops")
                .whenTrue((Integer i) -> i % 2 == 0).then("even")
                .orElse("odd");
        assertThat(divisibility).isEqualTo("odd");
    }

    // whenType(Class)

    @Test
    public void shouldMatchByType() {
        final String actual = Match.of(1)
                .whenType(String.class).then(s -> "String " + s)
                .whenType(Number.class).then(n -> "Number " + n)
                .whenType(Integer.class).then(i -> "int " + i)
                .orElse("unknown");
        assertThat(actual).isEqualTo("Number 1");
    }

    @Test
    public void shouldMatchOrElseByType() {
        final String actual = Match.of(1)
                .whenType(String.class).then(s -> "String " + s)
                .whenType(Short.class).then(s -> "Short " + s)
                .orElse("unknown");
        assertThat(actual).isEqualTo("unknown");
    }

    // whenTypeIn(Class...)

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMatchByTypeIn() {
        final Number number = 1;
        final String actual = Match.of(number)
                .whenTypeIn(Byte.class, Integer.class).then(s -> "matched")
                .orElse("unknown");
        assertThat(actual).isEqualTo("matched");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMatchOrElseByTypeIn() {
        final Number number = 1;
        final String actual = Match.of(number)
                .whenTypeIn(Byte.class, Short.class).then(s -> "matched")
                .orElse("unknown");
        assertThat(actual).isEqualTo("unknown");
    }
}
