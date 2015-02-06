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
public interface CheckResult {

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
    }

    /**
     * Represents a falsified property check.
     */
    static class Falsified implements CheckResult {

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
    }

    /**
     * Represents an erroneous property check.
     */
    static class Erroneous implements CheckResult {

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
    }
}
