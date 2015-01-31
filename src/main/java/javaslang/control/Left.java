/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Tuple;
import javaslang.Tuple1;
import javaslang.Tuple2;
import javaslang.algebra.Functor2;
import javaslang.algebra.HigherKinded2;
import javaslang.algebra.Monad2;

import java.util.Objects;
import java.util.function.BiFunction;

public final class Left<L, R> implements Either<L, R> {

    private static final long serialVersionUID = 3297057402720487673L;

    final L left;

    public Left(L left) {
        this.left = left;
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
    public Tuple1<L> unapply() {
        return Tuple.of(left);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof Left && Objects.equals(left, ((Left<?, ?>) obj).left));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(left);
    }

    @Override
    public String toString() {
        return String.format("Left(%s)", left);
    }
}
