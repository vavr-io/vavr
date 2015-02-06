/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * Checked version of java.util.function.BiConsumer.
 * Essentially the same as {@code CheckedFunction2<T, U, Void>}, or short {@code X2<T, U, Void>}.
 *
 * @param <T> First argument type
 * @param <U> Second argument type
 */
@FunctionalInterface
public interface CheckedBiConsumer<T, U> extends Serializable {

    static final long serialVersionUID = 1L;

    void accept(T t, U u) throws Throwable;

    default CheckedBiConsumer<T,U>	andThen(CheckedBiConsumer<? super T,? super U> after) {
        Objects.requireNonNull(after);
        return (l, r) -> {
            accept(l, r);
            after.accept(l, r);
        };
    }
}
