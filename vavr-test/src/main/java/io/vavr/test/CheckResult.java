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

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the result of a property check which is
 *
 * <ul>
 * <li>{@code Satisfied}, if all tests satisfied the given property</li>
 * <li>{@code Falsified}, if a counter-example could be discovered that falsified the given property</li>
 * <li>{@code Erroneous}, if an exception occurred executing the property check</li>
 * </ul>
 *
 * Please note that a {@code Satisfied} property check may be {@code Exhausted}, if the property is an implication
 * and no sample could be found that satisfied the pre-condition. In this case the post-condition is satisfied by
 * definition (see <a href="http://en.wikipedia.org/wiki/Principle_of_explosion">ex falso quodlibet</a>).
 *
 * @author Daniel Dietrich
 */
public interface CheckResult {

    /**
     * If this check result is satisfied as specified above.
     *
     * @return true, if this check result is satisfied, false otherwise
     */
    boolean isSatisfied();

    /**
     * If this check result is falsified as specified above.
     *
     * @return true, if this check result is falsified, false otherwise
     */
    boolean isFalsified();

    /**
     * If this check result is erroneous as specified above.
     *
     * @return true, if this check result is erroneous, false otherwise
     */
    boolean isErroneous();

    /**
     * If this check result is exhausted as specified above.
     *
     * @return true, if this check result is exhausted, false otherwise
     */
    boolean isExhausted();

    /**
     * The name of the checked property this result refers to.
     *
     * @return a property name
     */
    String propertyName();

    /**
     * The number of checks performed using random generated input data.
     *
     * @return the number of checks performed
     */
    int count();

    /**
     * An optional sample which falsified the property or which lead to an error.
     *
     * @return an optional sample
     */
    Option<Tuple> sample();

    /**
     * An optional error.
     *
     * @return an optional error
     */
    Option<Error> error();

    /**
     * Asserts that this CheckResult is satisfied.
     *
     * @throws AssertionError if this CheckResult is not satisfied.
     */
    default void assertIsSatisfied() {
        if (!isSatisfied()) {
            throw new AssertionError("Expected satisfied check result but was " + this);
        }
    }

    /**
     * Asserts that this CheckResult is satisfied with a given exhausted state.
     *
     * @param exhausted The exhausted state to be checked in the case of a satisfied CheckResult.
     * @throws AssertionError if this CheckResult is not satisfied or the exhausted state does not match.
     */
    default void assertIsSatisfiedWithExhaustion(boolean exhausted) {
        if (!isSatisfied()) {
            throw new AssertionError("Expected satisfied check result but was " + this);
        } else if (isExhausted() != exhausted) {
            throw new AssertionError("Expected satisfied check result to be " + (exhausted ? "" : "not ") + "exhausted but was: " + this);
        }
    }

    /**
     * Asserts that this CheckResult is falsified.
     *
     * @throws AssertionError if this CheckResult is not falsified.
     */
    default void assertIsFalsified() {
        if (!isFalsified()) {
            throw new AssertionError("Expected falsified check result but was " + this);
        }
    }

    /**
     * Asserts that this CheckResult is erroneous.
     *
     * @throws AssertionError if this CheckResult is not erroneous.
     */
    default void assertIsErroneous() {
        if (!isErroneous()) {
            throw new AssertionError("Expected erroneous check result but was " + this);
        }
    }

    /**
     * Represents a satisfied property check.
     */
    class Satisfied implements CheckResult, Serializable {

        private static final long serialVersionUID = 1L;

        private final String propertyName;
        private final int count;
        private final boolean exhausted;

        Satisfied(String propertyName, int count, boolean exhausted) {
            this.propertyName = propertyName;
            this.count = count;
            this.exhausted = exhausted;
        }

        @Override
        public boolean isSatisfied() {
            return true;
        }

        @Override
        public boolean isFalsified() {
            return false;
        }

        @Override
        public boolean isErroneous() {
            return false;
        }

        @Override
        public boolean isExhausted() {
            return exhausted;
        }

        @Override
        public String propertyName() {
            return propertyName;
        }

        @Override
        public int count() {
            return count;
        }

        @Override
        public Option<Tuple> sample() {
            return Option.none();
        }

        @Override
        public Option<Error> error() {
            return Option.none();
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Satisfied) {
                final Satisfied that = (Satisfied) o;
                return Objects.equals(this.propertyName, that.propertyName)
                        && this.count == that.count
                        && this.exhausted == that.exhausted;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(propertyName, count, exhausted);
        }

        @Override
        public String toString() {
            return String.format("%s(propertyName = %s, count = %s, exhausted = %s)", getClass().getSimpleName(), propertyName, count, exhausted);
        }
    }

    /**
     * Represents a falsified property check.
     */
    class Falsified implements CheckResult, Serializable {

        private static final long serialVersionUID = 1L;

        private final String propertyName;
        private final int count;
        private final Tuple sample;

        Falsified(String propertyName, int count, Tuple sample) {
            this.propertyName = propertyName;
            this.count = count;
            this.sample = sample;
        }

        @Override
        public boolean isSatisfied() {
            return false;
        }

        @Override
        public boolean isFalsified() {
            return true;
        }

        @Override
        public boolean isErroneous() {
            return false;
        }

        @Override
        public boolean isExhausted() {
            return false;
        }

        @Override
        public String propertyName() {
            return propertyName;
        }

        @Override
        public int count() {
            return count;
        }

        @Override
        public Option<Tuple> sample() {
            return Option.some(sample);
        }

        @Override
        public Option<Error> error() {
            return Option.none();
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Falsified) {
                final Falsified that = (Falsified) o;
                return Objects.equals(this.propertyName, that.propertyName)
                        && this.count == that.count
                        && Objects.equals(this.sample, that.sample);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(propertyName, count, sample);
        }

        @Override
        public String toString() {
            return String.format("%s(propertyName = %s, count = %s, sample = %s)", getClass().getSimpleName(), propertyName, count, sample);
        }
    }

    /**
     * Represents an erroneous property check.
     */
    class Erroneous implements CheckResult, Serializable {

        private static final long serialVersionUID = 1L;

        private final String propertyName;
        private final int count;
        private final Error error;
        private final Option<Tuple> sample;

        Erroneous(String propertyName, int count, Error error, Option<Tuple> sample) {
            this.propertyName = propertyName;
            this.count = count;
            this.error = error;
            this.sample = sample;
        }

        @Override
        public boolean isSatisfied() {
            return false;
        }

        @Override
        public boolean isFalsified() {
            return false;
        }

        @Override
        public boolean isErroneous() {
            return true;
        }

        @Override
        public boolean isExhausted() {
            return false;
        }

        @Override
        public String propertyName() {
            return propertyName;
        }

        @Override
        public int count() {
            return count;
        }

        @Override
        public Option<Tuple> sample() {
            return sample;
        }

        @Override
        public Option<Error> error() {
            return Option.some(error);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Erroneous) {
                final Erroneous that = (Erroneous) o;
                return Objects.equals(this.propertyName, that.propertyName)
                        && this.count == that.count
                        && deepEquals(this.error, that.error)
                        && Objects.equals(this.sample, that.sample);
            } else {
                return false;
            }
        }

        boolean deepEquals(Throwable t1, Throwable t2) {
            return (t1 == null && t2 == null) || (
                    t1 != null && t2 != null
                            && Objects.equals(t1.getMessage(), t2.getMessage())
                            && deepEquals(t1.getCause(), t2.getCause())
            );
        }

        @Override
        public int hashCode() {
            return Objects.hash(propertyName, count, deepHashCode(error), sample);
        }

        int deepHashCode(Throwable t) {
            if (t == null) {
                return 0;
            } else {
                return Objects.hash(t.getMessage(), deepHashCode(t.getCause()));
            }
        }

        @Override
        public String toString() {
            return String.format("%s(propertyName = %s, count = %s, error = %s, sample = %s)", getClass().getSimpleName(), propertyName, count, error.getMessage(), sample);
        }
    }
}
