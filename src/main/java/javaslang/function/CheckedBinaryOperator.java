/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

/**
 * Checked version of java.util.function.BinaryOperator.
 * Essentially the same as {@code CheckedFunction2<T, T, T>}, or short {@code X2<T, T, T>}.
 *
 * @param <T> Operand type
 */
@FunctionalInterface
public interface CheckedBinaryOperator<T> extends CheckedBiFunction<T, T, T> {
}
