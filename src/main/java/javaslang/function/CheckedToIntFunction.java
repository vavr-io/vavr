/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

import java.io.Serializable;

/**
 * Checked version of java.util.function.ToIntFunction.
 * Essentially the same as {@code CheckedFunction1<T, Integer>}, or short {@code X1<T, Integer>}.
 *
 * @param <T> Argument type
 */
@FunctionalInterface
public interface CheckedToIntFunction<T> extends Serializable {

    int applyAsInt(T value) throws Throwable;
}
