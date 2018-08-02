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

    private static final CheckError CHECK_ERROR = new CheckError("test", new Error("test"));
    private static final CheckResult<?> SATISFIED = CheckResult.satisfied("test", 0, false);
    private static final CheckResult<?> FALSIFIED = CheckResult.falsified("test", 0, Tuple.of(1));
    private static final CheckResult<?> ERRONEOUS = CheckResult.erroneous("test", 0, CHECK_ERROR, Option.none());

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
        Assertions.assertThat(CheckResult.satisfied("test", 0, true).isExhausted()).isTrue();
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
    public void shouldIdentifyEqualSatisfiedObjectsWhenObjectsAreIdentical() {
        //noinspection EqualsWithItself
        assertThat(SATISFIED.equals(SATISFIED)).isTrue();
    }

    @Test
    public void shouldIdentifyEqualSatisfiedObjectsWhenObjectsHaveSameTypeAndEqualValues() {
        final CheckResult<?> satisfied = CheckResult.satisfied("test", 0, false);
        assertThat(SATISFIED.equals(satisfied)).isTrue();
    }

    @Test
    public void shouldIdentifyUnequalSatisfiedObjectsWhenTypesAreUnequal() {
        assertThat(SATISFIED.equals(new Object())).isFalse();
    }

    @Test
    public void shouldIdentifyUnequalSatisfiedObjectsWhenValuesAreUnequal() {
        final CheckResult<?> satisfied = CheckResult.satisfied("test", 1, true);
        assertThat(satisfied.equals(CheckResult.satisfied("x", 1, true))).isFalse();
        assertThat(satisfied.equals(CheckResult.satisfied("test", -1, true))).isFalse();
        assertThat(satisfied.equals(CheckResult.satisfied("test", 1, false))).isFalse();
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
    public void shouldIdentifyEqualFalsifiedObjectsWhenObjectsAreIdentical() {
        final CheckResult<?> falsified = FALSIFIED;
        //noinspection EqualsWithItself
        assertThat(falsified.equals(falsified)).isTrue();
    }

    @Test
    public void shouldIdentifyEqualFalsifiedObjectsWhenObjectsHaveSameTypeAndEqualValues() {
        final CheckResult<?> falsified = CheckResult.falsified("test", 0, Tuple.of(1));
        assertThat(FALSIFIED.equals(falsified)).isTrue();
    }

    @Test
    public void shouldIdentifyUnequalFalsifiedObjectsWhenTypesAreUnequal() {
        assertThat(FALSIFIED.equals(new Object())).isFalse();
    }

    @Test
    public void shouldIdentifyUnequalFalsifiedObjectsWhenValuesAreUnequal() {
        final CheckResult<?> falsified = CheckResult.falsified("test", 1, Tuple.of(2));
        assertThat(falsified.equals(CheckResult.falsified("x", 1, Tuple.of(2)))).isFalse();
        assertThat(falsified.equals(CheckResult.falsified("test", -1, Tuple.of(2)))).isFalse();
        assertThat(falsified.equals(CheckResult.falsified("test", 1, Tuple.of(-1)))).isFalse();
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
        final CheckResult<?> erroneous = CheckResult.erroneous("test", 1, CHECK_ERROR, Option.of(Tuple.of(1)));
        assertThat(erroneous.sample().get()).isEqualTo(Tuple.of(1));
    }

    @Test
    public void shouldHaveAnErrorWhenIsErroneous() {
        assertThat(ERRONEOUS.error().get().getMessage()).isEqualTo("test");
    }

    @Test
    public void shouldIdentifyEqualErroneousObjectsWhenObjectsAreIdentical() {
        final CheckResult<?> erroneous = ERRONEOUS;
        //noinspection EqualsWithItself
        assertThat(erroneous.equals(erroneous)).isTrue();
    }

    @Test
    public void shouldIdentifyEqualErroneousObjectsWhenObjectsHaveSameTypeAndEqualValues() {
        final CheckResult<?> erroneous = CheckResult.erroneous("test", 0, CHECK_ERROR, Option.none());
        assertThat(ERRONEOUS.equals(erroneous)).isTrue();
    }

    @Test
    public void shouldIdentifyUnequalErroneousObjectsWhenTypesAreUnequal() {
        assertThat(ERRONEOUS.equals(new Object())).isFalse();
    }

    @Test
    public void shouldIdentifyUnequalErroneousObjectsWhenValuesAreUnequal() {
        final CheckResult<?> erroneous = CheckResult.erroneous("test", 1, CHECK_ERROR, Option.none());
        assertThat(erroneous.equals(CheckResult.erroneous("x", 1, CHECK_ERROR, Option.none()))).isFalse();
        assertThat(erroneous.equals(CheckResult.erroneous("test", -1, CHECK_ERROR, Option.none()))).isFalse();
        assertThat(erroneous.equals(CheckResult.erroneous("test", 1, new CheckError("unique error", new Error("unique error")), Option.none()))).isFalse();
        assertThat(erroneous.equals(CheckResult.erroneous("test", 1, CHECK_ERROR, Option.some(Tuple.of(1))))).isFalse();
    }

    @Test
    public void shouldComputeHashCodeOfErroneous() {
        assertThat(ERRONEOUS.hashCode()).isNotNull();
    }

    @Test
    public void shouldComputeToStringOfErroneous() {
        assertThat(ERRONEOUS.toString()).isEqualTo("Erroneous(propertyName = test, count = 0, error = test, sample = None)");
    }

    // Assertions

    // -- satisfied

    @Test
    public void shouldAssertThatCheckResultIsSatisfied() {
        CheckResult.satisfied("test", 0, false).assertIsSatisfied();
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonSatisfiedCheckResultIsSatisfied() {
        CheckResult.falsified("test", 0, Tuple.empty()).assertIsSatisfied();
    }

    // -- satisfiedWithExhaustion

    @Test
    public void shouldAssertThatCheckResultIsSatisfiedWithExhaustionTrue() {
        CheckResult.satisfied("test", 0, true).assertIsSatisfiedWithExhaustion(true);
    }

    @Test
    public void shouldAssertThatCheckResultIsSatisfiedWithExhaustionFalse() {
        CheckResult.satisfied("test", 0, false).assertIsSatisfiedWithExhaustion(false);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonSatisfiedCheckResultIsSatisfiedWithExhaustionTrue() {
        CheckResult.falsified("test", 0, Tuple.empty()).assertIsSatisfiedWithExhaustion(true);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonSatisfiedCheckResultIsSatisfiedWithExhaustionFalse() {
        CheckResult.falsified("test", 0, Tuple.empty()).assertIsSatisfiedWithExhaustion(false);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatSatisfiedNonExhaustedCheckResultIsSatisfiedWithExhaustionTrue() {
        CheckResult.satisfied("test", 0, false).assertIsSatisfiedWithExhaustion(true);
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatSatisfiedExhaustedCheckResultIsSatisfiedWithExhaustionFalse() {
        CheckResult.satisfied("test", 0, true).assertIsSatisfiedWithExhaustion(false);
    }

    // -- falsified

    @Test
    public void shouldAssertThatCheckResultIsFalsified() {
        CheckResult.falsified("test", 0, Tuple.empty()).assertIsFalsified();
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonFalsifiedCheckResultIsFalsified() {
        CheckResult.satisfied("test", 0, false).assertIsFalsified();
    }

    // -- erroneous

    @Test
    public void shouldAssertThatCheckResultIsErroneous() {
        CheckResult.erroneous("test", 0, CHECK_ERROR, Option.none()).assertIsErroneous();
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowWhenAssertThatNonErroneousCheckResultIsErroneous() {
        CheckResult.falsified("test", 0, Tuple.empty()).assertIsErroneous();
    }

}
