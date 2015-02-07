/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * Checked version of java.util.function.DoubleUnaryOperator.
 * Essentially the same as {@code CheckedFunction1<Double, Double>}.
 */
@FunctionalInterface
public interface CheckedDoubleUnaryOperator extends Serializable {

    static final long serialVersionUID = 1L;

    double applyAsDouble(double operand) throws Throwable;

    default CheckedDoubleUnaryOperator compose(CheckedDoubleUnaryOperator before) {
        Objects.requireNonNull(before);
        return (double v) -> applyAsDouble(before.applyAsDouble(v));
    }

    default CheckedDoubleUnaryOperator andThen(CheckedDoubleUnaryOperator after) {
        Objects.requireNonNull(after);
        return (double t) -> after.applyAsDouble(applyAsDouble(t));
    }

    static CheckedDoubleUnaryOperator identity() {
        return t -> t;
    }
}
