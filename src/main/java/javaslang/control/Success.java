/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import java.io.Serializable;
import java.util.Objects;

/**
 * A succeeded Try.
 *
 * @param <T> component type of this Success
 * @author Daniel Dietrich
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
