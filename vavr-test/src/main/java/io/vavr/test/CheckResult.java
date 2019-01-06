/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
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
 * @param <T> type of a sample
 *
 * @author Daniel Dietrich
 */
public abstract class CheckResult<T> {

    private CheckResult() {
    }

    /**
     * TODO: JAVADOC
     * 
     * @param propertyName
     * @param count
     * @param exhausted
     * @param <T>
     * @return
     */
    static <T> CheckResult<T> satisfied(String propertyName, int count, boolean exhausted) {
        return new Satisfied<>(propertyName, count, exhausted);
    }

    /**
     * TODO: JAVADOC
     * 
     * @param propertyName
     * @param count
     * @param sample
     * @param <T>
     * @return
     */
    static <T> CheckResult<T> falsified(String propertyName, int count, T sample) {
        return new Falsified<>(propertyName, count, sample);
    }

    /**
     * TODO: JAVADOC
     *
     * @param propertyName
     * @param count
     * @param error
     * @param sample
     * @param <T>
     * @return
     */
    static <T> CheckResult<T> erroneous(String propertyName, int count, CheckError error, Option<T> sample) {
        return new Erroneous<>(propertyName, count, error, sample);
    }

    /**
     * If this check result is satisfied as specified above.
     *
     * @return true, if this check result is satisfied, false otherwise
     */
    public abstract boolean isSatisfied();

    /**
     * If this check result is falsified as specified above.
     *
     * @return true, if this check result is falsified, false otherwise
     */
    public abstract boolean isFalsified();

    /**
     * If this check result is erroneous as specified above.
     *
     * @return true, if this check result is erroneous, false otherwise
     */
    public abstract boolean isErroneous();

    /**
     * If this check result is exhausted as specified above.
     *
     * @return true, if this check result is exhausted, false otherwise
     */
    public abstract boolean isExhausted();

    /**
     * The name of the checked property this result refers to.
     *
     * @return a property name
     */
    public abstract String propertyName();

    /**
     * The number of checks performed using random generated input data.
     *
     * @return the number of checks performed
     */
    public abstract int count();

    /**
     * An optional sample which falsified the property or which lead to an error.
     *
     * @return an optional sample
     */
    public abstract Option<T> sample();

    /**
     * Returns {@code Some(CheckError)} if this {@code CheckResult} is erroneous, otherwise {@code None}.
     *
     * @return an optional {@link CheckError}
     */
    public abstract Option<CheckError> error();

    /**
     * Asserts that this CheckResult is satisfied.
     *
     * @throws AssertionError if this CheckResult is not satisfied.
     */
    public void assertIsSatisfied() {
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
    public void assertIsSatisfiedWithExhaustion(boolean exhausted) {
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
    public void assertIsFalsified() {
        if (!isFalsified()) {
            throw new AssertionError("Expected falsified check result but was " + this);
        }
    }

    /**
     * Asserts that this CheckResult is erroneous.
     *
     * @throws AssertionError if this CheckResult is not erroneous.
     */
    public void assertIsErroneous() {
        if (!isErroneous()) {
            throw new AssertionError("Expected erroneous check result but was " + this);
        }
    }

    /**
     * Represents a satisfied property check.
     */
    private static final class Satisfied<T> extends CheckResult<T> implements Serializable {

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
        public Option<T> sample() {
            return Option.none();
        }

        @Override
        public Option<CheckError> error() {
            return Option.none();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Satisfied) {
                final Satisfied<?> that = (Satisfied<?>) o;
                return Objects.equals(this.propertyName, that.propertyName)
                        && this.count == that.count
                        && this.exhausted == that.exhausted;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Tuple.hash(propertyName, count, exhausted);
        }

        @Override
        public String toString() {
            return String.format("%s(propertyName = %s, count = %s, exhausted = %s)", getClass().getSimpleName(), propertyName, count, exhausted);
        }
    }

    /**
     * Represents a falsified property check.
     */
    private static final class Falsified<T> extends CheckResult<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String propertyName;
        private final int count;
        private final T sample;

        Falsified(String propertyName, int count, T sample) {
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
        public Option<T> sample() {
            return Option.some(sample);
        }

        @Override
        public Option<CheckError> error() {
            return Option.none();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Falsified) {
                final Falsified<?> that = (Falsified<?>) o;
                return Objects.equals(this.propertyName, that.propertyName)
                        && this.count == that.count
                        && Objects.equals(this.sample, that.sample);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Tuple.hash(propertyName, count, sample);
        }

        @Override
        public String toString() {
            return String.format("%s(propertyName = %s, count = %s, sample = %s)", getClass().getSimpleName(), propertyName, count, sample);
        }
    }

    /**
     * Represents an erroneous property check.
     */
    private static final class Erroneous<T> extends CheckResult<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String propertyName;
        private final int count;
        private final CheckError error;
        private final Option<T> sample;

        Erroneous(String propertyName, int count, CheckError error, Option<T> sample) {
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
        public Option<T> sample() {
            return sample;
        }

        @Override
        public Option<CheckError> error() {
            return Option.some(error);
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Erroneous) {
                final Erroneous<?> that = (Erroneous<?>) o;
                return Objects.equals(this.propertyName, that.propertyName)
                        && this.count == that.count
                        && Objects.equals(this.error, that.error)
                        && Objects.equals(this.sample, that.sample);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Tuple.hash(propertyName, count, error, sample);
        }

        @Override
        public String toString() {
            return String.format("%s(propertyName = %s, count = %s, error = %s, sample = %s)", getClass().getSimpleName(), propertyName, count, error.getMessage(), sample);
        }
    }
}
