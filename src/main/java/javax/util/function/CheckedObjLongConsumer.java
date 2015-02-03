/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import java.io.Serializable;

/**
 * Checked version of java.util.function.ObjLongConsumer.
 * Essentially the same as {@code CheckedFunction2<T, Long, Void>}, or short {@code X2<T, Long, Void>}.
 *
 * @param <T> Argument type
 */
@FunctionalInterface
public interface CheckedObjLongConsumer<T> extends Serializable {

    void accept(T t, long value) throws Throwable;
}
