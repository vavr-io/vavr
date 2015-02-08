/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction2;

/**
 * Checked version of java.util.function.ObjIntConsumer.
 *
 * @param <T> Argument type
 */
@FunctionalInterface
public interface CheckedObjIntConsumer<T> extends CheckedFunction2<T, Integer, Void> {

    static final long serialVersionUID = 1L;

    void accept(T t, int value) throws Throwable;

    @Override
    default Void apply(T t, Integer value) throws Throwable {
        accept(t, value);
        return null;
    }
}
