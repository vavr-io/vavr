/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction1;

import java.util.Objects;

/**
 * Checked version of java.util.function.LongPredicate.
 */
@FunctionalInterface
public interface CheckedLongPredicate extends CheckedFunction1<Long, Boolean> {

    static final long serialVersionUID = 1L;

    boolean test(long value) throws Throwable;

    @Override
    default Boolean apply(Long value) throws Throwable {
        return test(value);
    }

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
