/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction1;

import java.util.Objects;

/**
 * Checked version of java.util.function.Consumer.
 *
 * @param <T> Argument type
 */
@FunctionalInterface
public interface CheckedConsumer<T> extends CheckedFunction1<T, Void> {

    static final long serialVersionUID = 1L;

    void accept(T t) throws Throwable;

    @Override
    default Void apply(T t) throws Throwable {
        accept(t);
        return null;
    }
}
