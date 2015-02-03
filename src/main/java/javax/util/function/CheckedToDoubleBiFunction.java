/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import java.io.Serializable;

/**
 * Checked version of java.util.function.ToDoubleBiFunction.
 * Essentially the same as {@code CheckedFunction2<T, U, Double>}, or short {@code X2<T, U, Double>}.
 *
 * @param <T> First argument type
 * @param <U> Second argument type
 */
@FunctionalInterface
public interface CheckedToDoubleBiFunction<T, U> extends Serializable {

    double applyAsDouble(T t, U u) throws Throwable;
}
