/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.CheckedFunction1;
import javaslang.Value;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A succeeded Try.
 *
 * @param <T> component type of this Success
 * @since 1.0.0
 */
public final class Success<T> implements Try<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private final T value;

    /**
     * Constructs a Success.
     *
     * @param value The value of this Success.
     */
    public Success(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public Failure.NonFatal getCause() {
        throw new UnsupportedOperationException("getCause on Success");
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof Success && Objects.equals(value, ((Success<?>) obj).value));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Success(" + value + ")";
    }
}
