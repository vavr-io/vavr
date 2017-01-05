/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.control.Option;

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
 * @since 1.2.0
 */
public interface CheckResult<T> {

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
    Option<T> sample();

    /**
     * An optional error.
     *
     * @return an optional error
     * @since 1.2.0
     */
    Option<Error> error();

    /**
     * The number of shrinks performed.
     *
     * @return the number of shrinks performed
     */
    int shrinks();

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
    class Satisfied<T> implements CheckResult<T>, Serializable {

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
        public Option<Error> error() {
            return Option.none();
        }

        @Override
        public int shrinks() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Satisfied) {
                final Satisfied<?> that = (Satisfied) o;
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
            return String.format("%s(propertyName = %s, count = %s, exhausted = %s)",
                    getClass().getSimpleName(), propertyName, count, exhausted);
        }
    }

    /**
     * Represents a falsified property check.
     *
     * @since 1.2.0
     */
    class Falsified<T> implements CheckResult<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private final String propertyName;
        private final int count;
        private final T sample;
        private final int shrinks;

        Falsified(String propertyName, int count, T sample, int shrinks) {
            this.propertyName = propertyName;
            this.count = count;
            this.sample = sample;
            this.shrinks = shrinks;
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
        public Option<Error> error() {
            return Option.none();
        }

        @Override
        public int shrinks() {
            return shrinks;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Falsified) {
                final Falsified<?> that = (Falsified) o;
                return Objects.equals(this.propertyName, that.propertyName)
                        && this.count == that.count
                        && this.shrinks == that.shrinks
                        && Objects.equals(this.sample, that.sample);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(propertyName, count, sample, shrinks);
        }

        @Override
        public String toString() {
            return String.format("%s(propertyName = %s, count = %s, sample = %s, shrinks = %s)",
                    getClass().getSimpleName(), propertyName, count, sample, shrinks);
        }
    }

    /**
     * Represents an erroneous property check.
     *
     * @since 1.2.0
     */
    class Erroneous<T> implements CheckResult<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private final String propertyName;
        private final int count;
        private final Error error;
        private final Option<T> sample;
        private final int shrinks;

        Erroneous(String propertyName, int count, Error error, Option<T> sample, int shrinks) {
            this.propertyName = propertyName;
            this.count = count;
            this.error = error;
            this.sample = sample;
            this.shrinks = shrinks;
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
        public Option<Error> error() {
            return Option.some(error);
        }

        @Override
        public int shrinks() {
            return shrinks;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Erroneous) {
                final Erroneous<?> that = (Erroneous) o;
                return Objects.equals(this.propertyName, that.propertyName)
                        && this.count == that.count
                        && this.shrinks == that.shrinks
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
            return Objects.hash(propertyName, count, deepHashCode(error), sample, shrinks);
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
            return String.format("%s(propertyName = %s, count = %s, error = %s, sample = %s, shrinks = %s)",
                    getClass().getSimpleName(), propertyName, count, error.getMessage(), sample, shrinks);
        }
    }
}
