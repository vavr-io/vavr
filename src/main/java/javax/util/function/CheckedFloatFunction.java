/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import java.io.Serializable;

/**
 * Checked version of FloatFunction.
 * Essentially the same as {@code CheckedFunction1<Float, R>}, or short {@code X1<Float, R>}.
 *
 * @param <R> Return value type
 */
@FunctionalInterface
public interface CheckedFloatFunction<R> extends Serializable {

    R apply(float f) throws Throwable;
}
