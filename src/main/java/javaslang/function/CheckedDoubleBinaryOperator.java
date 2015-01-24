/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

import java.io.Serializable;

/**
 * Checked version of java.util.function.DoubleBinaryOperator.
 * Essentially the same as {@code CheckedFunction2<Double, Double, Double>}, or short {@code X2<Double, Double, Double>}.
 */
@FunctionalInterface
public interface CheckedDoubleBinaryOperator extends Serializable {

    double applyAsDouble(double left, double right) throws Throwable;
}
