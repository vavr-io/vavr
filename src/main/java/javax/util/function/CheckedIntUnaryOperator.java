/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * Checked version of java.util.function.IntUnaryOperator.
 * Essentially the same as {@code CheckedFunction1<Integer, Integer>}, or short {@code X1<Integer, Integer>}.
 */
@FunctionalInterface
public interface CheckedIntUnaryOperator extends Serializable {

    static final long serialVersionUID = 1L;

    int applyAsInt(int operand) throws Throwable;

    default CheckedIntUnaryOperator compose(CheckedIntUnaryOperator before) {
        Objects.requireNonNull(before);
        return (int v) -> applyAsInt(before.applyAsInt(v));
    }

    default CheckedIntUnaryOperator andThen(CheckedIntUnaryOperator after) {
        Objects.requireNonNull(after);
        return (int t) -> after.applyAsInt(applyAsInt(t));
    }

    static CheckedIntUnaryOperator identity() {
        return t -> t;
    }
}
