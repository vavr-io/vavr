/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import java.util.Objects;

/**
 * Assertions targeting on unit tests.
 *
 * @since 1.2.0
 */
public interface CheckResultAssertions {

    /**
     * Returns a {@linkplain javaslang.test.CheckResultAssertions.CheckResultAssertion} which provides a fluent API
     * to call further assertions on the given checkResult.
     *
     * @param checkResult A CheckResult
     * @return A new CheckResultAssertion
     */
    static CheckResultAssertion assertThat(CheckResult checkResult) {
        Objects.requireNonNull("checkResult is null");
        return new CheckResultAssertion(checkResult);
    }

    /**
     * Assertions for {@linkplain javaslang.test.CheckResult}.
     */
    class CheckResultAssertion {

        private final CheckResult checkResult;

        CheckResultAssertion(CheckResult checkResult) {
            this.checkResult = checkResult;
        }

        /**
         * Asserts that the underlying CheckResult is satisfied.
         *
         * @return this CheckResultAssertion
         * @throws AssertionError if the underlying CheckResult is not satisfied.
         */
        public CheckResultAssertion isSatisfied() {
            if (!checkResult.isSatisfied()) {
                throw new AssertionError("Expected satisfied check result but was " + checkResult);
            }
            return this;
        }

        /**
         * Asserts that the underlying CheckResult is satisfied with a given exhausted state.
         *
         * @param exhausted The exhausted state to be checked in the case of a satisfied CheckResult.
         * @return this CheckResultAssertion
         * @throws AssertionError if the underlying CheckResult is not satisfied or the exhausted state does not match.
         */
        public CheckResultAssertion isSatisfiedWithExhaustion(boolean exhausted) {
            if (!checkResult.isSatisfied()) {
                throw new AssertionError("Expected satisfied check result but was " + checkResult);
            } else if (checkResult.isExhausted() != exhausted) {
                throw new AssertionError("Expected satisfied check result to be " + (exhausted ? "" : "not ") + "exhausted but was: " + checkResult);
            }
            return this;
        }

        /**
         * Asserts that the underlying CheckResult is falsified.
         *
         * @return this CheckResultAssertion
         * @throws AssertionError if the underlying CheckResult is not falsified.
         */
        public CheckResultAssertion isFalsified() {
            if (!checkResult.isFalsified()) {
                throw new AssertionError("Expected falsified check result but was " + checkResult);
            }
            return this;
        }

        /**
         * Asserts that the underlying CheckResult is erroneous.
         *
         * @return this CheckResultAssertion
         * @throws AssertionError if the underlying CheckResult is not erroneous.
         */
        public CheckResultAssertion isErroneous() {
            if (!checkResult.isErroneous()) {
                throw new AssertionError("Expected erroneous check result but was " + checkResult);
            }
            return this;
        }
    }
}
