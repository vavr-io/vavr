/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * Checked version of java.util.function.LongPredicate.
 * Essentially the same as {@code CheckedFunction1<Long, Boolean>}, or short {@code X1<Long, Boolean>}.
 */
@FunctionalInterface
public interface CheckedLongPredicate extends Serializable {

    boolean test(long value) throws Throwable;

    default CheckedLongPredicate and(CheckedLongPredicate other) {
        Objects.requireNonNull(other);
        return (value) -> test(value) && other.test(value);
    }

    default CheckedLongPredicate negate() {
        return (value) -> !test(value);
    }

    default CheckedLongPredicate or(CheckedLongPredicate other) {
        Objects.requireNonNull(other);
        return (value) -> test(value) || other.test(value);
    }
}
