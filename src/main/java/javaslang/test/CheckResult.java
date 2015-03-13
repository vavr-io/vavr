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

public interface CheckResult extends ValueObject {

    static final long serialVersionUID = 1L;

    static Satisfied satisfied(int count, boolean exhausted) {
        return new Satisfied(count, exhausted);
    }

    static Falsified falsified(int count, Tuple sample) {
        return new Falsified(count, sample);
    }

    static Erroneous erroneous(int count, Error error, Option<Tuple> sample) {
        return new Erroneous(count, error, sample);
    }

    boolean isSatisfied();

    boolean isFalsified();

    boolean isErroneous();

    boolean isExhausted();

    int count();

    Option<Tuple> sample();

    Option<Error> error();

    /**
     * Represents a satisfied property check.
     */
    static class Satisfied implements CheckResult {

        private static final long serialVersionUID = 1L;

        final int count;
        final boolean exhausted;

        Satisfied(int count, boolean exhausted) {
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
                return this.count == that.count && this.exhausted == that.exhausted;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(count, exhausted);
        }

        @Override
        public String toString() {
            return String.format("%s(count = %s, exhausted = %s)", getClass().getSimpleName(), count, exhausted);
        }
    }

    /**
     * Represents a falsified property check.
     */
    static class Falsified implements CheckResult {

        private static final long serialVersionUID = 1L;

        private final int count;
        private final Tuple sample;

        Falsified(int count, Tuple sample) {
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
                return this.count == that.count && Objects.equals(this.sample, that.sample);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(count, sample);
        }

        @Override
        public String toString() {
            return String.format("%s(count = %s, sample = %s)", getClass().getSimpleName(), count, sample);
        }
    }

    /**
     * Represents an erroneous property check.
     */
    static class Erroneous implements CheckResult {

        private static final long serialVersionUID = 1L;

        private final int count;
        private final Error error;
        private final Option<Tuple> sample;

        Erroneous(int count, Error error, Option<Tuple> sample) {
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
                return this.count == that.count
                        && Objects.equals(this.error, that.error)
                        && Objects.equals(this.sample, that.sample);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(count, error, sample);
        }

        @Override
        public String toString() {
            return String.format("%s(count = %s, error = %s, sample = %s)", getClass().getSimpleName(), count, error, sample);
        }
    }
}
