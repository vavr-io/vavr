/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

// TODO: ValueObject (unapply, equals, hashCode, toString)
public interface CheckResult<T extends Tuple> {

    static <T extends Tuple> Satisfied<T> satisfied(int count, boolean exhausted) {
        return new Satisfied<>(count, exhausted);
    }

    static <T extends Tuple> Falsified<T> falsified(int count, T sample) {
        return new Falsified<>(count, sample);
    }

    static <T extends Tuple> Erroneous<T> erroneous(int count, Error error, Option<T> sample) {
        return new Erroneous<>(count, error, sample);
    }

    boolean isSatisfied();

    boolean isFalsified();

    boolean isErroneous();

    boolean isExhausted();

    int count();

    Option<T> sample();

    Option<Error> error();

    /**
     * Represents a satisfied property check.
     * @param <T> Type of property arguments represented as Tuple.
     */
    static class Satisfied<T extends Tuple> implements CheckResult<T> {

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
        public Option<T> sample() {
            return None.instance();
        }

        @Override
        public Option<Error> error() {
            return None.instance();
        }
    }

    /**
     * Represents a falsified property check.
     * @param <T> Type of property arguments represented as Tuple.
     */
    static class Falsified<T extends Tuple> implements CheckResult<T> {

        private final int count;
        private final Some<T> sample;

        Falsified(int count, T sample) {
            this.count = count;
            this.sample = new Some<>(sample);
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
        public Option<T> sample() {
            return sample;
        }

        @Override
        public Option<Error> error() {
            return None.instance();
        }
    }

    /**
     * Represents an erroneous property check.
     * @param <T> Type of property arguments represented as Tuple.
     */
    static class Erroneous<T extends Tuple> implements CheckResult<T> {

        private final int count;
        private final Some<Error> error;
        private final Option<T> sample;

        Erroneous(int count, Error error, Option<T> sample) {
            this.count = count;
            this.error = new Some<>(error);
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
        public Option<T> sample() {
            return sample;
        }

        @Override
        public Option<Error> error() {
            return error;
        }
    }
}
