/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.Function1;

/**
 * Unchecked float to R function.
 *
 * @param <R> Return value type
 */
@FunctionalInterface
public interface FloatFunction<R> extends Function1<Float, R> {

    static final long serialVersionUID = 1L;

    R apply(float f);

    @Override
    default R apply(Float f) {
        return apply(f.floatValue());
    }
}
