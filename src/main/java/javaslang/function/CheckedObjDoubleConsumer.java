/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

import java.io.Serializable;

/**
 * Checked version of java.util.function.ObjDoubleConsumer.
 * Essentially the same as {@code CheckedFunction2<T, Double, Void>}, or short {@code X2<T, Double, Void>}.
 *
 * @param <T> Argument type
 */
@FunctionalInterface
public interface CheckedObjDoubleConsumer<T> extends Serializable {

    void accept(T t, double value) throws Throwable;
}
