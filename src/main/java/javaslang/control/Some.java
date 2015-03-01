/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Tuple;
import javaslang.Tuple1;
import javaslang.algebra.HigherKinded1;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Some represents a defined {@link javaslang.control.Option}. It contains a value which may be null. However, to
 * create an Option containing null, {@code new Some(null)} has to be called. In all other cases
 * {@link Option#of(Object)} is sufficient.
 *
 * @param <T> The type of the optional value.
 */
public final class Some<T> implements Option<T> {

    private static final long serialVersionUID = 8703728987837576700L;

    private final T value;

    public Some(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Tuple1<T> unapply() {
        return Tuple.of(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Some)) {
            return false;
        }
        final Some<?> other = (Some<?>) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return String.format("Some(%s)", value);
    }
}
