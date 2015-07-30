/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.util.Objects;
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

    /**
     * Gets the underlying value of throws if no value is present.
     *
     * @return the underlying value
     * @throws java.util.NoSuchElementException if no value is defined
     */
    T get();

    /**
     * Checks, this {@code Value} is empty, i.e. if the underlying value is absent.
     *
     * @return false, if no underlying value is present, true otherwise.
     */
    boolean isEmpty();

    /**
     * Checks, this {@code Value} is defined, i.e. if the underlying value is present.
     *
     * @return true, if an underlying value is present, false otherwise.
     */
    default boolean isDefined() {
        return !isEmpty();
    }

    /**
     * Returns the underlying value if present, otherwise {@code other}.
     *
     * @param other An alternative value.
     * @return A value of type {@code T}
     */
    default T orElse(T other) {
        return isEmpty() ? other : get();
    }

    /**
     * Returns the underlying value if present, otherwise {@code other}.
     *
     * @param supplier An alternative value.
     * @return A value of type {@code T}
     * @throws NullPointerException if supplier is null
     */
    default T orElseGet(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isEmpty() ? supplier.get() : get();
    }

    /**
     * Returns the underlying value if present, otherwise throws {@code supplier.get()}.
     *
     * @param <X>      a Throwable type
     * @param supplier An exception supplier.
     * @return A value of type {@code T}
     * @throws NullPointerException if supplier is null
     * @throws X                    if no value is present
     */
    default <X extends Throwable> T orElseThrow(Supplier<X> supplier) throws X {
        Objects.requireNonNull(supplier, "supplier is null");
        if (isEmpty()) {
            throw supplier.get();
        } else {
            return get();
        }
    }

    /**
     * Converts this value to an {@link javaslang.control.Option}.
     *
     * @return {@code Some(value)} if a value is present <strong>(may be null)</strong>, {@code None} otherwise.
     */
    default Option<T> toOption() {
        return isEmpty() ? None.instance() : new Some<>(get());
    }

    /**
     * Converts this value to an {@link java.util.Optional}.
     *
     * @return An empty {@code Optional}, if no value is present <strong>or the value is null</strong>,
     * otherwise a non-empty {@code Option} containing the value.
     */
    default Optional<T> toJavaOptional() {
        return isEmpty() ? Optional.empty() : Optional.ofNullable(get());
    }
}
