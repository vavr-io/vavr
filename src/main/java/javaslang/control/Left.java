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
 * The {@code Left} version of an {@code Either}.
 *
 * @param <L> left component type
 * @param <R> right component type
 * @since 1.0.0
 */
public final class Left<L, R> implements Either<L, R>, Serializable {

    private static final long serialVersionUID = 1L;

    private final L value;

    /**
     * Constructs a {@code Left}.
     *
     * @param value a left value
     */
    public Left(L value) {
        this.value = value;
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public <X, Y> Left<X, Y> bimap(Function<? super L, ? extends X> leftMapper, Function<? super R, ? extends Y> rightMapper) {
        Objects.requireNonNull(leftMapper, "leftMapper is null");
        Objects.requireNonNull(rightMapper, "rightMapper is null");
        return new Left<>(leftMapper.apply(value));
    }

    /**
     * Returns the value of this {@code Left}.
     *
     * @return the value of this {@code Left}
     */
    @Override
    public L get() {
        return value;
    }

    /**
     * Wrap the value of this {@code Left} in a new {@code Right}.
     *
     * @return a new {@code Right} containing this value
     */
    @Override
    public Right<R, L> swap() {
        return new Right<>(value);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof Left && Objects.equals(value, ((Left<?, ?>) obj).value));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Left(" + value + ")";
    }
}
