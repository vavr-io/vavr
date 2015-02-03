/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import java.io.Serializable;

/**
 * Checked version of java.util.function.LongBinaryOperator.
 * Essentially the same as {@code CheckedFunction2<Long, Long, Long>}, or short {@code X2<Long, Long, Long>}.
 */
@FunctionalInterface
public interface CheckedLongBinaryOperator extends Serializable {

    long applyAsInt(long left, long right) throws Throwable;
}
