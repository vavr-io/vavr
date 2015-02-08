/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction1;

/**
 * Checked version of java.util.function.ToLongFunction.
 *
 * @param <T> Argument type
 */
@FunctionalInterface
public interface CheckedToLongFunction<T> extends CheckedFunction1<T, Long> {

    static final long serialVersionUID = 1L;

    long applyAsLong(T value) throws Throwable;

    @Override
    default Long apply(T value) throws Throwable {
        return applyAsLong(value);
    }
}
