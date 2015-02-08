/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction1;

import java.util.Objects;

/**
 * Checked version of java.util.function.IntUnaryOperator.
 */
@FunctionalInterface
public interface CheckedIntUnaryOperator extends CheckedFunction1<Integer, Integer> {

    static final long serialVersionUID = 1L;

    int applyAsInt(int operand) throws Throwable;

    @Override
    default Integer apply(Integer operand) throws Throwable {
        return applyAsInt(operand);
    }

    static CheckedIntUnaryOperator identity() {
        return t -> t;
    }
}
