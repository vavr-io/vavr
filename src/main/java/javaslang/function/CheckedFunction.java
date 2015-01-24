/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

/**
 * Checked version of java.util.function.Function.
 * Essentially the same as {@code CheckedFunction1<T, R>}, or short {@code X1<T, R>}.
 *
 * @param <T> Argument type
 * @param <R> Return value type
 */
@FunctionalInterface
public interface CheckedFunction<T, R> extends CheckedFunction1<T, R> {

    static <T> CheckedFunction<T, T> identity() {
        return t -> t;
    }
}
