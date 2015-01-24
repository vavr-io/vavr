/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

import java.io.Serializable;

/**
 * Checked version of java.util.function.ToIntBiFunction.
 * Essentially the same as {@code CheckedFunction2<T, U, Integer>}, or short {@code X2<T, U, Integer>}.
 *
 * @param <T> First argument type
 * @param <U> Second argument type
 */
@FunctionalInterface
public interface CheckedToIntBiFunction<T, U> extends Serializable {

    int applyAsInt(T t, U u) throws Throwable;
}
