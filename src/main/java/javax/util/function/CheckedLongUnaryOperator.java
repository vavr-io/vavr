/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * Checked version of java.util.function.LongUnaryOperator.
 * Essentially the same as {@code CheckedFunction1<Long, Long>}, or short {@code X1<Long, Long>}.
 */
@FunctionalInterface
public interface CheckedLongUnaryOperator extends Serializable {

    static final long serialVersionUID = 1L;

    long applyAsLong(long operand) throws Throwable;

    default CheckedLongUnaryOperator compose(CheckedLongUnaryOperator before) {
        Objects.requireNonNull(before);
        return (long v) -> applyAsLong(before.applyAsLong(v));
    }

    default CheckedLongUnaryOperator andThen(CheckedLongUnaryOperator after) {
        Objects.requireNonNull(after);
        return (long t) -> after.applyAsLong(applyAsLong(t));
    }

    static CheckedLongUnaryOperator identity() {
        return t -> t;
    }
}
