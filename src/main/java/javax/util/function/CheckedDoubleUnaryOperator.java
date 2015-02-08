/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction1;

import java.util.Objects;

/**
 * Checked version of java.util.function.DoubleUnaryOperator.
 */
@FunctionalInterface
public interface CheckedDoubleUnaryOperator extends CheckedFunction1<Double, Double> {

    static final long serialVersionUID = 1L;

    double applyAsDouble(double operand) throws Throwable;

    @Override
    default Double apply(Double operand) throws Throwable {
        return applyAsDouble(operand);
    }

    static CheckedDoubleUnaryOperator identity() {
        return t -> t;
    }
}
