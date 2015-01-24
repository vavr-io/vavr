/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

import java.io.Serializable;

/**
 * Checked version of java.util.function.ToDoubleFunction.
 * Essentially the same as {@code CheckedFunction1<T, Double>}, or short {@code X1<T, Double>}.
 *
 * @param <T> Argument type
 */
@FunctionalInterface
public interface CheckedToDoubleFunction<T> extends Serializable {

    double applyAsDouble(T value) throws Throwable;
}
