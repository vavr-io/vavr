/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction2;

/**
 * Checked version of java.util.function.LongBinaryOperator.
 */
@FunctionalInterface
public interface CheckedLongBinaryOperator extends CheckedFunction2<Long, Long, Long> {

    static final long serialVersionUID = 1L;

    long applyAsLong(long left, long right) throws Throwable;

    @Override
    default Long apply(Long left, Long right) throws Throwable {
        return applyAsLong(left, right);
    }
}
