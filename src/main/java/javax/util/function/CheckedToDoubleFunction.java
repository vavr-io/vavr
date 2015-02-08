/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction1;

/**
 * Checked version of java.util.function.ToDoubleFunction.
 *
 * @param <T> Argument type
 */
@FunctionalInterface
public interface CheckedToDoubleFunction<T> extends CheckedFunction1<T, Double> {

    static final long serialVersionUID = 1L;

    double applyAsDouble(T value) throws Throwable;

    @Override
    default Double apply(T value) throws Throwable {
        return applyAsDouble(value);
    }
}
