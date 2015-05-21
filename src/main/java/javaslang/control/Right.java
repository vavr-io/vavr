/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

/**
 * The {@code Right} version of an {@code Either}.
 *
 * @param <L> left component type
 * @param <R> right component type
 * @since 1.0.0
 */
public final class Right<L, R> implements Either<L, R>, Serializable {

    private static final long serialVersionUID = 1L;

    private final R value;

    /**
     * Constructs a {@code Right}.
     *
     * @param value a right value
     */
    public Right(R value) {
        this.value = value;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public <X, Y> Right<X, Y> bimap(Function<? super L, ? extends X> leftMapper, Function<? super R, ? extends Y> rightMapper) {
        Objects.requireNonNull(leftMapper, "leftMapper is null");
        Objects.requireNonNull(rightMapper, "rightMapper is null");
        return new Right<>(rightMapper.apply(value));
    }

    /**
     * Returns the value of this {@code Right}.
     *
     * @return the value of this {@code Right}
     */
    @Override
    public R get() {
        return value;
    }

    /**
     * Wrap the value of this {@code Right} in a new {@code Left}.
     *
     * @return a new {@code Left} containing this value
     */
    @Override
    public Left<R, L> swap() {
        return new Left<>(value);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof Right && Objects.equals(value, ((Right<?, ?>) obj).value));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Right(" + value + ")";
    }
}
