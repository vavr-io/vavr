/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Tuple;
import javaslang.Tuple1;

import java.util.Objects;

/**
 * The right type of an Either.
 *
 * @param <L> left component type
 * @param <R> right component type
 * @since 1.0.0
 */
public final class Right<L, R> implements Either<L, R> {

    private static final long serialVersionUID = 1L;

    final R right;

    /**
     * Constructs a right.
     *
     * @param right The value of this Right
     */
    public Right(R right) {
        this.right = right;
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
    public Tuple1<R> unapply() {
        return Tuple.of(right);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof Right && Objects.equals(right, ((Right<?, ?>) obj).right));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(right);
    }

    @Override
    public String toString() {
        return String.format("Right(%s)", right);
    }
}
