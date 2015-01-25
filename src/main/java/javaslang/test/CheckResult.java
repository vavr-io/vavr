/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple;

// TODO: ValueObject (unapply, equals, hashCode, toString)
public interface CheckResult<T extends Tuple> {

    static <T extends Tuple> Satisfied<T> satisfied(int count) {
        return new Satisfied<>(count);
    }

    static <T extends Tuple> Falsified<T> falsified(int count, T counterExample) {
        return new Falsified<>(count, counterExample);
    }

    static <T extends Tuple> Erroneous<T> erroneous(int count, Error error) {
        return new Erroneous<>(count, error);
    }

    boolean isSatisfied();

    boolean isFalsified();

    boolean isErroneous();

    int count();

    T counterExample();

    Error error();

    /**
     * Represents a satisfied property check.
     * @param <T> Type of property arguments represented as Tuple.
     */
    static class Satisfied<T extends Tuple> implements CheckResult<T> {

        final int count;

        Satisfied(int count) {
            this.count = count;
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
        public int count() {
            return count;
        }

        @Override
        public T counterExample() {
            throw new UnsupportedOperationException("Satisfied.counterExcample()");
        }

        @Override
        public Error error() {
            throw new UnsupportedOperationException("Satisfied.error()");
        }
    }

    /**
     * Represents a falsified property check.
     * @param <T> Type of property arguments represented as Tuple.
     */
    static class Falsified<T extends Tuple> implements CheckResult<T> {

        final int count;
        final T counterExample;

        Falsified(int count, T counterExample) {
            this.count = count;
            this.counterExample = counterExample;
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
        public int count() {
            return count;
        }

        @Override
        public T counterExample() {
            return counterExample;
        }

        @Override
        public Error error() {
            throw new UnsupportedOperationException("Falsified.error()");
        }
    }

    /**
     * Represents an erroneous property check.
     * @param <T> Type of property arguments represented as Tuple.
     */
    static class Erroneous<T extends Tuple> implements CheckResult<T> {

        final int count;
        final Error error;

        Erroneous(int count, Error error) {
            this.count = count;
            this.error = error;
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
        public int count() {
            return count;
        }

        @Override
        public T counterExample() {
            throw new UnsupportedOperationException("Erroneous.counterExcample()");
        }

        @Override
        public Error error() {
            return error;
        }
    }
}
