/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction1;

/**
 * Checked version of BooleanFunction.
 *
 * @param <R> Return value type
 */
@FunctionalInterface
public interface CheckedBooleanFunction<R> extends CheckedFunction1<Boolean, R> {

    static final long serialVersionUID = 1L;

    R apply(boolean b) throws Throwable;

    @Override
    default R apply(Boolean b) throws Throwable {
        return apply(b.booleanValue());
    }
}
