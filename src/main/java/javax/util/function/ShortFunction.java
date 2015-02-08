/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.Function1;

/**
 * Unchecked short to R function.
 *
 * @param <R> Return value type
 */
@FunctionalInterface
public interface ShortFunction<R> extends Function1<Short, R> {

    static final long serialVersionUID = 1L;

    R apply(short s);

    @Override
    default R apply(Short s) {
        return apply(s.shortValue());
    }
}
