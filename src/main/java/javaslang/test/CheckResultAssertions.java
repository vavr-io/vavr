/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import java.util.Objects;

public interface CheckResultAssertions {

    static CheckResultAssertion assertThat(CheckResult checkResult) {
        Objects.requireNonNull("checkResult is null");
        return new CheckResultAssertion(checkResult);
    }

    static class CheckResultAssertion {

        private final CheckResult checkResult;

        CheckResultAssertion(CheckResult checkResult) {
            this.checkResult = checkResult;
        }

        public CheckResultAssertion isSatisfied() {
            if (!checkResult.isSatisfied()) {
                throw new AssertionError("Expected satisfied check result but was " + checkResult);
            }
            return this;
        }

        public CheckResultAssertion isSatisfiedWithExhaustion(boolean exhausted) {
            if (!checkResult.isSatisfied()) {
                throw new AssertionError("Expected satisfied check result but was " + checkResult);
            } else if (checkResult.isExhausted() != exhausted) {
                throw new AssertionError("Expected satisfied check result to be " + (exhausted ? "" : "not ") + "exhausted but was: " + checkResult);
            }
            return this;
        }
    }
}
