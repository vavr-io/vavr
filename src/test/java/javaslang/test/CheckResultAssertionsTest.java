/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple;
import javaslang.control.None;
import org.junit.Test;

public class CheckResultAssertionsTest {

    // -- satisfied

    @Test
    public void shouldAssertThatCheckResultIsSatisfied() {
        CheckResultAssertions.assertThat(new CheckResult.Satisfied("test", 0, false)).isSatisfied();
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonSatisfiedCheckResultIsSatisfied() {
        CheckResultAssertions.assertThat(new CheckResult.Falsified("test", 0, Tuple.empty())).isSatisfied();
    }

    // -- satisfiedWithExhaustion

    @Test
    public void shouldAssertThatCheckResultIsSatisfiedWithExhaustionTrue() {
        CheckResultAssertions.assertThat(new CheckResult.Satisfied("test", 0, true)).isSatisfiedWithExhaustion(true);
    }

    @Test
    public void shouldAssertThatCheckResultIsSatisfiedWithExhaustionFalse() {
        CheckResultAssertions.assertThat(new CheckResult.Satisfied("test", 0, false)).isSatisfiedWithExhaustion(false);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonSatisfiedCheckResultIsSatisfiedWithExhaustionTrue() {
        CheckResultAssertions.assertThat(new CheckResult.Falsified("test", 0, Tuple.empty())).isSatisfiedWithExhaustion(true);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonSatisfiedCheckResultIsSatisfiedWithExhaustionFalse() {
        CheckResultAssertions.assertThat(new CheckResult.Falsified("test", 0, Tuple.empty())).isSatisfiedWithExhaustion(false);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatSatisfiedNonExhaustedCheckResultIsSatisfiedWithExhaustionTrue() {
        CheckResultAssertions.assertThat(new CheckResult.Satisfied("test", 0, false)).isSatisfiedWithExhaustion(true);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatSatisfiedExhaustedCheckResultIsSatisfiedWithExhaustionFalse() {
        CheckResultAssertions.assertThat(new CheckResult.Satisfied("test", 0, true)).isSatisfiedWithExhaustion(false);
    }

    // -- falsified

    @Test
    public void shouldAssertThatCheckResultIsFalsified() {
        CheckResultAssertions.assertThat(new CheckResult.Falsified("test", 0, Tuple.empty())).isFalsified();
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonFalsifiedCheckResultIsFalsified() {
        CheckResultAssertions.assertThat(new CheckResult.Satisfied("test", 0, false)).isFalsified();
    }

    // -- erroneous

    @Test
    public void shouldAssertThatCheckResultIsErroneous() {
        CheckResultAssertions.assertThat(new CheckResult.Erroneous("test", 0, new Error(), None.instance())).isErroneous();
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonErroneousCheckResultIsErroneous() {
        CheckResultAssertions.assertThat(new CheckResult.Falsified("test", 0, Tuple.empty())).isErroneous();
    }
}
