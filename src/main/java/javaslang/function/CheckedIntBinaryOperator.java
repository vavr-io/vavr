/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

import java.io.Serializable;

/**
 * Checked version of java.util.function.IntBinaryOperator.
 * Essentially the same as {@code CheckedFunction2<Integer, Integer, Integer>}, or short {@code X2<Integer, Integer, Integer>}.
 */
@FunctionalInterface
public interface CheckedIntBinaryOperator extends Serializable {

    int applyAsInt(int left, int right) throws Throwable;
}
