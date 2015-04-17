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
 * The {@code Left} version of an {@code Either}.
 *
 * @param <L> left component type
 * @param <R> right component type
 * @since 1.0.0
 */
public interface Left<L, R> extends Either<L, R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    static <L, R> Left<L, R> of(L left) {
        return new LeftImpl<>(left);
    }

    L get();

    @Override
    <X, Y> Left<X, Y> bimap(Function<? super L, ? extends X> leftMapper, Function<? super R, ? extends Y> rightMapper);

    /**
     * Internal implementation of {@code Left}. Users of {@code Either} should rely only on {@code Left}.
     *
     * @param <L> left component type
     * @param <R> right component type
     * @since 1.3.0
     */
    final class LeftImpl<L, R> implements Left<L, R> {

        private static final long serialVersionUID = 1L;

        private final L left;

        private LeftImpl(L left) {
            this.left = left;
        }

        @Override
        public L get() {
            return left;
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
            return new LeftImpl<>(leftMapper.apply(left().get()));
        }

        @Override
        public Tuple1<L> unapply() {
            return Tuple.of(left);
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof LeftImpl && Objects.equals(left, ((LeftImpl<?, ?>) obj).get()));
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
}
