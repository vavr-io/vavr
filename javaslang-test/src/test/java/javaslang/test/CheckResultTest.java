/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple;
import javaslang.control.Option;
import javaslang.test.CheckResult.Erroneous;
import javaslang.test.CheckResult.Falsified;
import javaslang.test.CheckResult.Satisfied;
import org.junit.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckResultTest {

    static final Satisfied SATISFIED = new Satisfied("test", 0, false);
    static final Falsified FALSIFIED = new Falsified("test", 0, Tuple.of(1));
    static final Erroneous ERRONEOUS = new Erroneous("test", 0, new Error("test"), Option.none());

    // -- Satisfied

    @Test
    public void shouldBeSatisfiedWhenIsSatisfied() {
        assertThat(SATISFIED.isSatisfied()).isTrue();
    }

    @Test
    public void shouldNotBeFalsifiedWhenIsSatisfied() {
        assertThat(SATISFIED.isFalsified()).isFalse();
    }

    @Test
    public void shouldNotBeErroneousWhenIsSatisfied() {
        assertThat(SATISFIED.isErroneous()).isFalse();
    }

    @Test
    public void shouldBeExhaustedWhenIsSatisfiedAndExhausted() {
        assertThat(new Satisfied("test", 0, true).isExhausted()).isTrue();
    }

    @Test
    public void shouldNotBeExhaustedWhenIsSatisfiedAndNotExhausted() {
        assertThat(SATISFIED.isExhausted()).isFalse();
    }

    @Test
    public void shouldGetPropertyNameOfSatisfied() {
        assertThat(SATISFIED.propertyName()).isEqualTo("test");
    }

    @Test
    public void shouldGetCountOfSatisfied() {
        assertThat(SATISFIED.count()).isEqualTo(0);
    }

    @Test
    public void shouldHaveNoSampleWhenIsSatisfied() {
        assertThat(SATISFIED.sample()).isEqualTo(Option.none());
    }

    @Test
    public void shouldHaveNoErrorWhenIsSatisfied() {
        assertThat(SATISFIED.error()).isEqualTo(Option.none());
    }

    @Test
    public void shouldIdentififyEqualSatisfiedObjectsWhenObjectsAreIdentical() {
        final Satisfied satisfied = SATISFIED;
        //noinspection EqualsWithItself
        assertThat(satisfied.equals(satisfied)).isTrue();
    }

    @Test
    public void shouldIdentififyEqualSatisfiedObjectsWhenObjectsHaveSameTypeAndEqualValues() {
        final Satisfied satisfied1 = SATISFIED;
        final Satisfied satisfied2 = new Satisfied("test", 0, false);
        assertThat(satisfied1.equals(satisfied2)).isTrue();
    }

    @Test
    public void shouldIdentififyUnequalSatisfiedObjectsWhenTypesAreUnequal() {
        final Satisfied satisfied = SATISFIED;
        assertThat(satisfied.equals(new Object())).isFalse();
    }

    @Test
    public void shouldIdentififyUnequalSatisfiedObjectsWhenValuesAreUnequal() {
        final Satisfied satisfied = new Satisfied("test", 1, true);
        assertThat(satisfied.equals(new Satisfied("x", 1, true))).isFalse();
        assertThat(satisfied.equals(new Satisfied("test", -1, true))).isFalse();
        assertThat(satisfied.equals(new Satisfied("test", 1, false))).isFalse();
    }

    @Test
    public void shouldComputeHashCodeOfSatisfied() {
        assertThat(SATISFIED.hashCode()).isEqualTo(Objects.hash("test", 0, false));
    }

    @Test
    public void shouldComputeToStringOfSatisfied() {
        assertThat(SATISFIED.toString()).isEqualTo("Satisfied(propertyName = test, count = 0, exhausted = false)");
    }

    // -- Falsified

    @Test
    public void shouldNotBeSatisfiedWhenIsFalsified() {
        assertThat(FALSIFIED.isSatisfied()).isFalse();
    }

    @Test
    public void shouldBeFalsifiedWhenIsFalsified() {
        assertThat(FALSIFIED.isFalsified()).isTrue();
    }

    @Test
    public void shouldNotBeErroneousWhenIsFalsified() {
        assertThat(FALSIFIED.isErroneous()).isFalse();
    }

    @Test
    public void shouldNotBeExhaustedWhenIsFalsified() {
        assertThat(FALSIFIED.isExhausted()).isFalse();
    }

    @Test
    public void shouldGetPropertyNameOfFalsified() {
        assertThat(FALSIFIED.propertyName()).isEqualTo("test");
    }

    @Test
    public void shouldGetCountOfFalsified() {
        assertThat(FALSIFIED.count()).isEqualTo(0);
    }

    @Test
    public void shouldHaveASampleWhenIsFalsified() {
        assertThat(FALSIFIED.sample().get()).isEqualTo(Tuple.of(1));
    }

    @Test
    public void shouldHaveNoErrorWhenIsFalsified() {
        assertThat(FALSIFIED.error()).isEqualTo(Option.none());
    }

    @Test
    public void shouldIdentififyEqualFalsifiedObjectsWhenObjectsAreIdentical() {
        final Falsified falsified = FALSIFIED;
        //noinspection EqualsWithItself
        assertThat(falsified.equals(falsified)).isTrue();
    }

    @Test
    public void shouldIdentififyEqualFalsifiedObjectsWhenObjectsHaveSameTypeAndEqualValues() {
        final Falsified falsified1 = FALSIFIED;
        final Falsified falsified2 = new Falsified("test", 0, Tuple.of(1));
        assertThat(falsified1.equals(falsified2)).isTrue();
    }

    @Test
    public void shouldIdentififyUnequalFalsifiedObjectsWhenTypesAreUnequal() {
        final Falsified falsified = FALSIFIED;
        assertThat(falsified.equals(new Object())).isFalse();
    }

    @Test
    public void shouldIdentififyUnequalFalsifiedObjectsWhenValuesAreUnequal() {
        final Falsified falsified = new Falsified("test", 1, Tuple.of(2));
        assertThat(falsified.equals(new Falsified("x", 1, Tuple.of(2)))).isFalse();
        assertThat(falsified.equals(new Falsified("test", -1, Tuple.of(2)))).isFalse();
        assertThat(falsified.equals(new Falsified("test", 1, Tuple.of(-1)))).isFalse();
    }

    @Test
    public void shouldComputeHashCodeOfFalsified() {
        assertThat(FALSIFIED.hashCode()).isEqualTo(Objects.hash("test", 0, Tuple.of(1)));
    }

    @Test
    public void shouldComputeToStringOfFalsified() {
        assertThat(FALSIFIED.toString()).isEqualTo("Falsified(propertyName = test, count = 0, sample = (1))");
    }

    // -- Erroneous

    @Test
    public void shouldNotBeSatisfiedWhenIsErroneous() {
        assertThat(ERRONEOUS.isSatisfied()).isFalse();
    }

    @Test
    public void shouldNotBeFalsifiedWhenIsErroneous() {
        assertThat(ERRONEOUS.isFalsified()).isFalse();
    }

    @Test
    public void shouldBeErroneousWhenIsErroneous() {
        assertThat(ERRONEOUS.isErroneous()).isTrue();
    }

    @Test
    public void shouldNotBeExhaustedWhenIsErroneous() {
        assertThat(ERRONEOUS.isExhausted()).isFalse();
    }

    @Test
    public void shouldGetPropertyNameOfErroneous() {
        assertThat(ERRONEOUS.propertyName()).isEqualTo("test");
    }

    @Test
    public void shouldGetCountOfErroneous() {
        assertThat(ERRONEOUS.count()).isEqualTo(0);
    }

    @Test
    public void shouldHaveNoSampleWhenIsErroneousWithoutSample() {
        assertThat(ERRONEOUS.sample()).isEqualTo(Option.none());
    }

    @Test
    public void shouldHaveSampleWhenIsErroneousWithSample() {
        final Erroneous erroneous = new Erroneous("test", 1, new Error("test"), Option.of(Tuple.of(1)));
        assertThat(erroneous.sample().get()).isEqualTo(Tuple.of(1));
    }

    @Test
    public void shouldHaveAnErrorWhenIsErroneous() {
        assertThat(ERRONEOUS.error().get().getMessage()).isEqualTo("test");
    }

    @Test
    public void shouldIdentififyEqualErroneousObjectsWhenObjectsAreIdentical() {
        final Erroneous erroneous = ERRONEOUS;
        //noinspection EqualsWithItself
        assertThat(erroneous.equals(erroneous)).isTrue();
    }

    @Test
    public void shouldIdentififyEqualErroneousObjectsWhenObjectsHaveSameTypeAndEqualValues() {
        final Erroneous erroneous1 = ERRONEOUS;
        final Erroneous erroneous2 = new Erroneous("test", 0, new Error("test"), Option.none());
        assertThat(erroneous1.equals(erroneous2)).isTrue();
    }

    @Test
    public void shouldIdentififyUnequalErroneousObjectsWhenTypesAreUnequal() {
        final Erroneous erroneous = ERRONEOUS;
        assertThat(erroneous.equals(new Object())).isFalse();
    }

    @Test
    public void shouldIdentififyUnequalErroneousObjectsWhenValuesAreUnequal() {
        final Erroneous erroneous = new Erroneous("test", 1, new Error("error"), Option.none());
        assertThat(erroneous.equals(new Erroneous("x", 1, new Error("error"), Option.none()))).isFalse();
        assertThat(erroneous.equals(new Erroneous("test", -1, new Error("error"), Option.none()))).isFalse();
        assertThat(erroneous.equals(new Erroneous("test", 1, new Error("x"), Option.none()))).isFalse();
        assertThat(erroneous.equals(new Erroneous("test", 1, new Error("error"), Option.some(Tuple.of(1))))).isFalse();
    }

    @Test
    public void shouldCheckDeepEqualityOfErroneousErrors() {
        assertThat(new Erroneous("test", 1, null, Option.none())).isEqualTo(new Erroneous("test", 1, null, Option.none()));
        assertThat(new Erroneous("test", 1, new Error("test"), Option.none())).isNotEqualTo(new Erroneous("test", 1, null, Option.none()));
        assertThat(new Erroneous("test", 1, null, Option.none())).isNotEqualTo(new Erroneous("test", 1, new Error("test"), Option.none()));
        assertThat(new Erroneous("test", 1, new Error("test"), Option.none())).isEqualTo(new Erroneous("test", 1, new Error("test"), Option.none()));
        assertThat(new Erroneous("test", 1, new Error("test"), Option.none())).isNotEqualTo(new Erroneous("test", 1, new Error("x"), Option.none()));
        assertThat(new Erroneous("test", 1, new Error("test", new Error("test2")), Option.none())).isEqualTo(new Erroneous("test", 1, new Error("test", new Error("test2")), Option.none()));
        assertThat(new Erroneous("test", 1, new Error("test", new Error("test2")), Option.none())).isNotEqualTo(new Erroneous("test", 1, new Error("test"), Option.none()));
        assertThat(new Erroneous("test", 1, new Error("test", new Error("test2")), Option.none())).isNotEqualTo(new Erroneous("test", 1, new Error("test", new Error("x")), Option.none()));
    }

    @Test
    public void shouldComputeHashCodeOfErroneous() {
        assertThat(ERRONEOUS.hashCode()).isEqualTo(Objects.hash("test", 0, ERRONEOUS.deepHashCode(new Error("test")), Option.none()));
    }

    @Test
    public void shouldComputeToStringOfErroneous() {
        assertThat(ERRONEOUS.toString()).isEqualTo("Erroneous(propertyName = test, count = 0, error = test, sample = None)");
    }

    // Assertions

    // -- satisfied

    @Test
    public void shouldAssertThatCheckResultIsSatisfied() {
        new Satisfied("test", 0, false).assertIsSatisfied();
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonSatisfiedCheckResultIsSatisfied() {
        new Falsified("test", 0, Tuple.empty()).assertIsSatisfied();
    }

    // -- satisfiedWithExhaustion

    @Test
    public void shouldAssertThatCheckResultIsSatisfiedWithExhaustionTrue() {
        new Satisfied("test", 0, true).assertIsSatisfiedWithExhaustion(true);
    }

    @Test
    public void shouldAssertThatCheckResultIsSatisfiedWithExhaustionFalse() {
        new Satisfied("test", 0, false).assertIsSatisfiedWithExhaustion(false);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonSatisfiedCheckResultIsSatisfiedWithExhaustionTrue() {
        new Falsified("test", 0, Tuple.empty()).assertIsSatisfiedWithExhaustion(true);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonSatisfiedCheckResultIsSatisfiedWithExhaustionFalse() {
        new Falsified("test", 0, Tuple.empty()).assertIsSatisfiedWithExhaustion(false);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatSatisfiedNonExhaustedCheckResultIsSatisfiedWithExhaustionTrue() {
        new Satisfied("test", 0, false).assertIsSatisfiedWithExhaustion(true);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatSatisfiedExhaustedCheckResultIsSatisfiedWithExhaustionFalse() {
        new Satisfied("test", 0, true).assertIsSatisfiedWithExhaustion(false);
    }

    // -- falsified

    @Test
    public void shouldAssertThatCheckResultIsFalsified() {
        new Falsified("test", 0, Tuple.empty()).assertIsFalsified();
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonFalsifiedCheckResultIsFalsified() {
        new Satisfied("test", 0, false).assertIsFalsified();
    }

    // -- erroneous

    @Test
    public void shouldAssertThatCheckResultIsErroneous() {
        new Erroneous("test", 0, new Error(), Option.none()).assertIsErroneous();
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonErroneousCheckResultIsErroneous() {
        new Falsified("test", 0, Tuple.empty()).assertIsErroneous();
    }

}
