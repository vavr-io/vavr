/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * Checked version of java.util.function.DoublePredicate.
 * Essentially the same as {@code CheckedFunction1<Double, Boolean>}, or short {@code X1<Double, Boolean>}.
 */
@FunctionalInterface
public interface CheckedDoublePredicate extends Serializable {

    boolean test(double value) throws Throwable;

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
