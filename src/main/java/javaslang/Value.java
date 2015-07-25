/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * A representation of value which may be either <em>defined</em> or <em>empty</em>.
 * <p>
 * How the empty state is interpreted depends on the context, i.e. it may be <em>undefined</em>, <em>failed</em>,
 * <em>not yet defined</em>, etc.
 *
 * @param <T> The type of the wrapped value.
 * @since 2.0.0
 */
public interface Value<T> {

    boolean isEmpty();

    T get();

    default boolean isDefined() {
        return !isEmpty();
    }

    default T orElse(T other) {
        return isEmpty() ? other : get();
    }

    default T orElseGet(Supplier<? extends T> supplier) {
        return isEmpty() ? supplier.get() : get();
    }

    default <X extends Throwable> T orElseThrow(Supplier<X> exceptionSupplier) throws X {
        if (isEmpty()) {
            throw exceptionSupplier.get();
        } else {
            return get();
        }
    }

    default Option<T> toOption() {
        return isEmpty() ? None.instance() : new Some<>(get());
    }

    default Optional<T> toJavaOptional() {
        return isEmpty() ? Optional.empty() : Optional.ofNullable(get());
    }
}
