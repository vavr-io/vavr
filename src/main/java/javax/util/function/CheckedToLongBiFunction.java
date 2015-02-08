/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction2;

/**
 * Checked version of java.util.function.ToLongBiFunction.
 *
 * @param <T> First argument type
 * @param <U> Second argument type
 */
@FunctionalInterface
public interface CheckedToLongBiFunction<T, U> extends CheckedFunction2<T, U, Long> {

    static final long serialVersionUID = 1L;

    long applyAsLong(T t, U u) throws Throwable;

    @Override
    default Long apply(T t, U u) throws Throwable {
        return applyAsLong(t, u);
    }
}
