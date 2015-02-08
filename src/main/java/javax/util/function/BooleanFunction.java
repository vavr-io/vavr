/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.Function1;

/**
 * Unchecked boolean to R function.
 *
 * @param <R> Return value type
 */
@FunctionalInterface
public interface BooleanFunction<R> extends Function1<Boolean, R> {

    static final long serialVersionUID = 1L;

    R apply(boolean b);

    @Override
    default R apply(Boolean b) {
        return apply(b.booleanValue());
    }
}
