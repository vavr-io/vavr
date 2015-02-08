/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction1;

import java.util.Objects;

/**
 * Checked version of java.util.function.DoublePredicate.
 */
@FunctionalInterface
public interface CheckedDoublePredicate extends CheckedFunction1<Double, Boolean> {

    static final long serialVersionUID = 1L;

    boolean test(double value) throws Throwable;

    @Override
    default Boolean apply(Double value) throws Throwable {
        return test(value);
    }

    default CheckedDoublePredicate and(CheckedDoublePredicate other) {
        Objects.requireNonNull(other);
        return (value) -> test(value) && other.test(value);
    }

    default CheckedDoublePredicate negate() {
        return (value) -> !test(value);
    }

    default CheckedDoublePredicate or(CheckedDoublePredicate other) {
        Objects.requireNonNull(other);
        return (value) -> test(value) || other.test(value);
    }
}
