/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

import java.io.Serializable;

/**
 * Checked version of java.util.function.ToLongBiFunction.
 * Essentially the same as {@code CheckedFunction2<T, U, Long>}, or short {@code X2<T, U, Long>}.
 *
 * @param <T> First argument type
 * @param <U> Second argument type
 */
@FunctionalInterface
public interface CheckedToLongBiFunction<T, U> extends Serializable {

    long applyAsLong(T t, U u) throws Throwable;
}
