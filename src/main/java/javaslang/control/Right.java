/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Tuple;
import javaslang.Tuple1;

import java.util.Objects;
import java.util.function.Function;

/**
 * The {@code Right} version of an {@code Either}.
 *
 * @param <L> left component type
 * @param <R> right component type
 * @since 1.0.0
 */
public interface Right<L, R> extends Either<L, R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    static <L, R> Right<L, R> of(R right) {
        return new RightImpl<>(right);
    }

    R get();

    @Override
    <X, Y> Right<X, Y> bimap(Function<? super L, ? extends X> leftMapper, Function<? super R, ? extends Y> rightMapper);

    /**
     * Internal implementation of {@code Right}. Users of {@code Either} should rely only on {@code Right}.
     *
     * @param <L> left component type
     * @param <R> right component type
     * @since 1.3.0
     */
    final class RightImpl<L, R> implements Right<L, R> {

        private static final long serialVersionUID = 1L;

        private final R right;

        private RightImpl(R right) {
            this.right = right;
        }

        @Override
        public R get() {
            return right;
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
            return new RightImpl<>(rightMapper.apply(right().get()));
        }

        @Override
        public Tuple1<R> unapply() {
            return Tuple.of(right);
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof RightImpl && Objects.equals(right, ((RightImpl<?, ?>) obj).get()));
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
}
