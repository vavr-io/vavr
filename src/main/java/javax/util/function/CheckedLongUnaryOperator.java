/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction1;

import java.util.Objects;

/**
 * Checked version of java.util.function.LongUnaryOperator.
 */
@FunctionalInterface
public interface CheckedLongUnaryOperator extends CheckedFunction1<Long, Long> {

    static final long serialVersionUID = 1L;

    long applyAsLong(long operand) throws Throwable;

    @Override
    default Long apply(Long operand) throws Throwable {
        return applyAsLong(operand);
    }

    static CheckedLongUnaryOperator identity() {
        return t -> t;
    }
}
