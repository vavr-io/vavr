/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * Checked version of java.util.function.IntPredicate.
 * Essentially the same as {@code CheckedFunction1<Integer, Boolean>}, or short {@code X1<Integer, Boolean>}.
 */
@FunctionalInterface
public interface CheckedIntPredicate extends Serializable {

    static final long serialVersionUID = 1L;

    boolean test(int value) throws Throwable;

    default CheckedIntPredicate and(CheckedIntPredicate other) {
        Objects.requireNonNull(other);
        return (value) -> test(value) && other.test(value);
    }

    default CheckedIntPredicate negate() {
        return (value) -> !test(value);
    }

    default CheckedIntPredicate or(CheckedIntPredicate other) {
        Objects.requireNonNull(other);
        return (value) -> test(value) || other.test(value);
    }
}
