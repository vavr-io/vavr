/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.test;

import io.vavr.Tuple;
import io.vavr.control.Option;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckResultTest {

    static final CheckResult.Satisfied SATISFIED = new CheckResult.Satisfied("test", 0, false);
    static final CheckResult.Falsified FALSIFIED = new CheckResult.Falsified("test", 0, Tuple.of(1));
    static final CheckResult.Erroneous ERRONEOUS = new CheckResult.Erroneous("test", 0, new Error("test"), Option.none());

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
        Assertions.assertThat(new CheckResult.Satisfied("test", 0, true).isExhausted()).isTrue();
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
        final CheckResult.Satisfied satisfied = SATISFIED;
        //noinspection EqualsWithItself
        assertThat(satisfied.equals(satisfied)).isTrue();
    }

    @Test
    public void shouldIdentififyEqualSatisfiedObjectsWhenObjectsHaveSameTypeAndEqualValues() {
        final CheckResult.Satisfied satisfied1 = SATISFIED;
        final CheckResult.Satisfied satisfied2 = new CheckResult.Satisfied("test", 0, false);
        assertThat(satisfied1.equals(satisfied2)).isTrue();
    }

    @Test
    public void shouldIdentififyUnequalSatisfiedObjectsWhenTypesAreUnequal() {
        final CheckResult.Satisfied satisfied = SATISFIED;
        assertThat(satisfied.equals(new Object())).isFalse();
    }

    @Test
    public void shouldIdentififyUnequalSatisfiedObjectsWhenValuesAreUnequal() {
        final CheckResult.Satisfied satisfied = new CheckResult.Satisfied("test", 1, true);
        assertThat(satisfied.equals(new CheckResult.Satisfied("x", 1, true))).isFalse();
        assertThat(satisfied.equals(new CheckResult.Satisfied("test", -1, true))).isFalse();
        assertThat(satisfied.equals(new CheckResult.Satisfied("test", 1, false))).isFalse();
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
        final CheckResult.Falsified falsified = FALSIFIED;
        //noinspection EqualsWithItself
        assertThat(falsified.equals(falsified)).isTrue();
    }

    @Test
    public void shouldIdentififyEqualFalsifiedObjectsWhenObjectsHaveSameTypeAndEqualValues() {
        final CheckResult.Falsified falsified1 = FALSIFIED;
        final CheckResult.Falsified falsified2 = new CheckResult.Falsified("test", 0, Tuple.of(1));
        assertThat(falsified1.equals(falsified2)).isTrue();
    }

    @Test
    public void shouldIdentififyUnequalFalsifiedObjectsWhenTypesAreUnequal() {
        final CheckResult.Falsified falsified = FALSIFIED;
        assertThat(falsified.equals(new Object())).isFalse();
    }

    @Test
    public void shouldIdentififyUnequalFalsifiedObjectsWhenValuesAreUnequal() {
        final CheckResult.Falsified falsified = new CheckResult.Falsified("test", 1, Tuple.of(2));
        assertThat(falsified.equals(new CheckResult.Falsified("x", 1, Tuple.of(2)))).isFalse();
        assertThat(falsified.equals(new CheckResult.Falsified("test", -1, Tuple.of(2)))).isFalse();
        assertThat(falsified.equals(new CheckResult.Falsified("test", 1, Tuple.of(-1)))).isFalse();
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
        final CheckResult.Erroneous erroneous = new CheckResult.Erroneous("test", 1, new Error("test"), Option.of(Tuple.of(1)));
        assertThat(erroneous.sample().get()).isEqualTo(Tuple.of(1));
    }

    @Test
    public void shouldHaveAnErrorWhenIsErroneous() {
        assertThat(ERRONEOUS.error().get().getMessage()).isEqualTo("test");
    }

    @Test
    public void shouldIdentififyEqualErroneousObjectsWhenObjectsAreIdentical() {
        final CheckResult.Erroneous erroneous = ERRONEOUS;
        //noinspection EqualsWithItself
        assertThat(erroneous.equals(erroneous)).isTrue();
    }

    @Test
    public void shouldIdentififyEqualErroneousObjectsWhenObjectsHaveSameTypeAndEqualValues() {
        final CheckResult.Erroneous erroneous1 = ERRONEOUS;
        final CheckResult.Erroneous erroneous2 = new CheckResult.Erroneous("test", 0, new Error("test"), Option.none());
        assertThat(erroneous1.equals(erroneous2)).isTrue();
    }

    @Test
    public void shouldIdentififyUnequalErroneousObjectsWhenTypesAreUnequal() {
        final CheckResult.Erroneous erroneous = ERRONEOUS;
        assertThat(erroneous.equals(new Object())).isFalse();
    }

    @Test
    public void shouldIdentififyUnequalErroneousObjectsWhenValuesAreUnequal() {
        final CheckResult.Erroneous erroneous = new CheckResult.Erroneous("test", 1, new Error("error"), Option.none());
        assertThat(erroneous.equals(new CheckResult.Erroneous("x", 1, new Error("error"), Option.none()))).isFalse();
        assertThat(erroneous.equals(new CheckResult.Erroneous("test", -1, new Error("error"), Option.none()))).isFalse();
        assertThat(erroneous.equals(new CheckResult.Erroneous("test", 1, new Error("x"), Option.none()))).isFalse();
        assertThat(erroneous.equals(new CheckResult.Erroneous("test", 1, new Error("error"), Option.some(Tuple.of(1))))).isFalse();
    }

    @Test
    public void shouldCheckDeepEqualityOfErroneousErrors() {
        Assertions.assertThat(new CheckResult.Erroneous("test", 1, null, Option.none())).isEqualTo(new CheckResult.Erroneous("test", 1, null, Option.none()));
        Assertions.assertThat(new CheckResult.Erroneous("test", 1, new Error("test"), Option.none())).isNotEqualTo(new CheckResult.Erroneous("test", 1, null, Option.none()));
        Assertions.assertThat(new CheckResult.Erroneous("test", 1, null, Option.none())).isNotEqualTo(new CheckResult.Erroneous("test", 1, new Error("test"), Option.none()));
        Assertions.assertThat(new CheckResult.Erroneous("test", 1, new Error("test"), Option.none())).isEqualTo(new CheckResult.Erroneous("test", 1, new Error("test"), Option.none()));
        Assertions.assertThat(new CheckResult.Erroneous("test", 1, new Error("test"), Option.none())).isNotEqualTo(new CheckResult.Erroneous("test", 1, new Error("x"), Option.none()));
        Assertions.assertThat(new CheckResult.Erroneous("test", 1, new Error("test", new Error("test2")), Option.none())).isEqualTo(new CheckResult.Erroneous("test", 1, new Error("test", new Error("test2")), Option.none()));
        Assertions.assertThat(new CheckResult.Erroneous("test", 1, new Error("test", new Error("test2")), Option.none())).isNotEqualTo(new CheckResult.Erroneous("test", 1, new Error("test"), Option.none()));
        Assertions.assertThat(new CheckResult.Erroneous("test", 1, new Error("test", new Error("test2")), Option.none())).isNotEqualTo(new CheckResult.Erroneous("test", 1, new Error("test", new Error("x")), Option.none()));
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
        new CheckResult.Satisfied("test", 0, false).assertIsSatisfied();
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonSatisfiedCheckResultIsSatisfied() {
        new CheckResult.Falsified("test", 0, Tuple.empty()).assertIsSatisfied();
    }

    // -- satisfiedWithExhaustion

    @Test
    public void shouldAssertThatCheckResultIsSatisfiedWithExhaustionTrue() {
        new CheckResult.Satisfied("test", 0, true).assertIsSatisfiedWithExhaustion(true);
    }

    @Test
    public void shouldAssertThatCheckResultIsSatisfiedWithExhaustionFalse() {
        new CheckResult.Satisfied("test", 0, false).assertIsSatisfiedWithExhaustion(false);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonSatisfiedCheckResultIsSatisfiedWithExhaustionTrue() {
        new CheckResult.Falsified("test", 0, Tuple.empty()).assertIsSatisfiedWithExhaustion(true);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonSatisfiedCheckResultIsSatisfiedWithExhaustionFalse() {
        new CheckResult.Falsified("test", 0, Tuple.empty()).assertIsSatisfiedWithExhaustion(false);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatSatisfiedNonExhaustedCheckResultIsSatisfiedWithExhaustionTrue() {
        new CheckResult.Satisfied("test", 0, false).assertIsSatisfiedWithExhaustion(true);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatSatisfiedExhaustedCheckResultIsSatisfiedWithExhaustionFalse() {
        new CheckResult.Satisfied("test", 0, true).assertIsSatisfiedWithExhaustion(false);
    }

    // -- falsified

    @Test
    public void shouldAssertThatCheckResultIsFalsified() {
        new CheckResult.Falsified("test", 0, Tuple.empty()).assertIsFalsified();
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonFalsifiedCheckResultIsFalsified() {
        new CheckResult.Satisfied("test", 0, false).assertIsFalsified();
    }

    // -- erroneous

    @Test
    public void shouldAssertThatCheckResultIsErroneous() {
        new CheckResult.Erroneous("test", 0, new Error(), Option.none()).assertIsErroneous();
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonErroneousCheckResultIsErroneous() {
        new CheckResult.Falsified("test", 0, Tuple.empty()).assertIsErroneous();
    }

}
