/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction2;

/**
 * Checked version of java.util.function.ObjDoubleConsumer.
 *
 * @param <T> Argument type
 */
@FunctionalInterface
public interface CheckedObjDoubleConsumer<T> extends CheckedFunction2<T, Double, Void> {

    static final long serialVersionUID = 1L;

    void accept(T t, double value) throws Throwable;

    @Override
    default Void apply(T t, Double value) throws Throwable {
        accept(t, value);
        return null;
    }
}
