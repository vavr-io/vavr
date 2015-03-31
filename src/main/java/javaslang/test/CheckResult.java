/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.ValueObject;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.util.Objects;

/**
 * @since 1.2.0
 */
public interface CheckResult extends ValueObject {

    long serialVersionUID = 1L;

    boolean isSatisfied();

    boolean isFalsified();

    boolean isErroneous();

    boolean isExhausted();

    String propertyName();

    int count();

    Option<Tuple> sample();

    Option<Error> error();

    /**
     * Represents a satisfied property check.
     */
    class Satisfied implements CheckResult {

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
        public None<Tuple> sample() {
            return None.instance();
        }

        @Override
        public None<Error> error() {
            return None.instance();
        }

        @Override
        public Tuple2<Integer, Boolean> unapply() {
            return Tuple.of(count, exhausted);
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
    class Falsified implements CheckResult {

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
        public Some<Tuple> sample() {
            return new Some<>(sample);
        }

        @Override
        public None<Error> error() {
            return None.instance();
        }

        @Override
        public Tuple2<Integer, Tuple> unapply() {
            return Tuple.of(count, sample);
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
    class Erroneous implements CheckResult {

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
        public Some<Error> error() {
            return new Some<>(error);
        }

        @Override
        public Tuple3<Integer, Error, Option<Tuple>> unapply() {
            return Tuple.of(count, error, sample);
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
