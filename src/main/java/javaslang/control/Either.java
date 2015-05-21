/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.collection.TraversableOnce;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Either represents a value of two possible types. An Either is either a {@link javaslang.control.Left} or a
 * {@link javaslang.control.Right}.
 * <p>
 * If the given Either is a Right and projected to a Left, the Left operations have no effect on the Right value.<br>
 * If the given Either is a Left and projected to a Right, the Right operations have no effect on the Left value.<br>
 * If a Left is projected to a Left or a Right is projected to a Right, the operations have an effect.
 * <p>
 * <strong>Example:</strong> A compute() function, which results either in an Integer value (in the case of success) or
 * in an error message of type String (in the case of failure). By convention the success case is Right and the failure
 * is Left.
 *
 * <pre>
 * <code>
 * Either&lt;String,Integer&gt; value = compute().right().map(i -&gt; i * 2).toEither();
 * </code>
 * </pre>
 *
 * If the result of compute() is Right(1), the value is Right(2).<br>
 * If the result of compute() is Left("error), the value is Left("error").
 *
 * @param <L> The type of the Left value of an Either.
 * @param <R> The type of the Right value of an Either.
 * @since 1.0.0
 */
public interface Either<L, R> {

    /**
     * Returns whether this Either is a Left.
     *
     * @return true, if this is a Left, false otherwise
     */
    boolean isLeft();

    /**
     * Returns whether this Either is a Right.
     *
     * @return true, if this is a Right, false otherwise
     */
    boolean isRight();

    /**
     * Returns a LeftProjection of this Either.
     *
     * @return a new LeftProjection of this
     */
    default LeftProjection<L, R> left() {
        return new LeftProjection<>(this);
    }

    /**
     * Returns a RightProjection of this Either.
     *
     * @return a new RightProjection of this
     */
    default RightProjection<L, R> right() {
        return new RightProjection<>(this);
    }

    /**
     * Maps either the left or the right side of this disjunction.
     *
     * @param leftMapper  maps the left value if this is a Left
     * @param rightMapper maps the right value if this is a Right
     * @param <X>         The new left type of the resulting Either
     * @param <Y>         The new right type of the resulting Either
     * @return A new either instance
     */
    <X, Y> Either<X, Y> bimap(Function<? super L, ? extends X> leftMapper, Function<? super R, ? extends Y> rightMapper);

    /**
     * Returns the left value of type {@code L} if this is a {@code Left},
     * otherwise returns the right value of type {@code R} if this is a {@code Right}.
     *
     * @return the value of this {@code Either}
     */
    Object get();

    /**
     * Converts a {@code Left} to a {@code Right} vice versa by wrapping the value in a new type.
     *
     * @return a new {@code Either}
     */
    Either<R, L> swap();

    // -- Object.*

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    // -- Left/Right projections

    /**
     * A left projection of an either.
     *
     * @param <L> The type of the Left value of an Either.
     * @param <R> The type of the Right value of an Either.
     * @since 1.0.0
     */
    final class LeftProjection<L, R> implements TraversableOnce<L> {

        private final Either<L, R> either;

        private LeftProjection(Either<L, R> either) {
            this.either = either;
        }

        /**
         * Gets the Left value or throws.
         *
         * @return the left value, if the underlying Either is a Left
         * @throws NoSuchElementException if the underlying either of this LeftProjection is a Right
         */
        public L get() {
            if (either.isLeft()) {
                return asLeft();
            } else {
                throw new NoSuchElementException("Either.left().get() on Right");
            }
        }

        /**
         * Gets the Left value or an alternate value, if the projected Either is a Right.
         *
         * @param other an alternative value
         * @return the left value, if the underlying Either is a Left or else {@code other}
         * @throws NoSuchElementException if the underlying either of this LeftProjection is a Right
         */
        public L orElse(L other) {
            return either.isLeft() ? asLeft() : other;
        }

        /**
         * Gets the Left value or an alternate value, if the projected Either is a Right.
         *
         * @param other a function which converts a Right value to an alternative Left value
         * @return the left value, if the underlying Either is a Left or else the alternative Left value provided by
         * {@code other} by applying the Right value.
         */
        public L orElseGet(Function<? super R, ? extends L> other) {
            Objects.requireNonNull(other, "other is null");
            if (either.isLeft()) {
                return asLeft();
            } else {
                return other.apply(asRight());
            }
        }

        /**
         * Runs an action in the case this is a projection on a Right value.
         *
         * @param action an action which consumes a Right value
         */
        public void orElseRun(Consumer<? super R> action) {
            Objects.requireNonNull(action, "action is null");
            if (either.isRight()) {
                action.accept(asRight());
            }
        }

        /**
         * Gets the Left value or throws, if the projected Either is a Right.
         *
         * @param <X>               a throwable type
         * @param exceptionFunction a function which creates an exception based on a Right value
         * @return the left value, if the underlying Either is a Left or else throws the exception provided by
         * {@code exceptionFunction} by applying the Right value.
         * @throws X if the projected Either is a Right
         */
        public <X extends Throwable> L orElseThrow(Function<? super R, X> exceptionFunction) throws X {
            Objects.requireNonNull(exceptionFunction, "exceptionFunction is null");
            if (either.isLeft()) {
                return asLeft();
            } else {
                throw exceptionFunction.apply(asRight());
            }
        }

        /**
         * Converts this Either to an {@linkplain javaslang.control.Option}.
         *
         * @return {@linkplain javaslang.control.Some} of the left value if this is a projection of a Left,
         * {@linkplain javaslang.control.None} otherwise.
         */
        public Option<L> toOption() {
            if (either.isLeft()) {
                return new Some<>(asLeft());
            } else {
                return None.instance();
            }
        }

        /**
         * Returns the underlying either of this projection.
         *
         * @return the underlying either
         */
        public Either<L, R> toEither() {
            return either;
        }

        /**
         * Converts this Either to a {@linkplain java.util.Optional}.
         *
         * @return {@code Optional.ofNullable(leftValue)} if this is a projection of a Left,
         * {@code Optional.empty()} otherwise.
         */
        public Optional<L> toJavaOptional() {
            if (either.isLeft()) {
                return Optional.ofNullable(asLeft());
            } else {
                return Optional.empty();
            }
        }

        /**
         * Returns
         * <ul>
         * <li>{@code LeftProjection(Left(Some(value)))}, if the underlying {@code Either} of this projection is a
         * {@code Left} and the left value satisfies the given predicate</li>
         * <li>{@code LeftProjection(Left(None)))} if the underlying {@code Either} of this projection
         * is a {@code Left} and the left value does <em>not</em> satisfy the given predicate</li>
         * <li>{@code LeftProjection(Right(Some(value)))} otherwise, i.e. if the underlying {@code Either} of this
         * projection is a {@code Right}</li>
         * </ul>
         *
         * @param predicate A predicate
         * @return a LeftProjection of an {@code Either} with an optional value
         */
        public LeftProjection<Option<L>, Option<R>> filter(Predicate<? super L> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            if (either.isRight() || (either.isLeft() && predicate.test(asLeft()))) {
                return new LeftProjection<>(either.bimap(Some::new, Some::new));
            } else {
                return new LeftProjection<>(new Left<>(None.instance()));
            }
        }

        /**
         * Flattens a {@code LeftProjection} using a function.
         *
         * @param <U> the new type of the left value
         * @param f   a function which maps elements of this LeftProjection to LeftProjections
         * @return a {@code LeftProjection}
         * @throws NullPointerException if {@code f} is null
         */
        @SuppressWarnings("unchecked")
        public <U> LeftProjection<U, R> flatten(Function<? super L, ? extends LeftProjection<U, R>> f) {
            Objects.requireNonNull(f, "f is null");
            if (either.isRight()) {
                return (LeftProjection<U, R>) this;
            } else {
                return f.apply(get());
            }
        }

        /**
         * Applies the given action to the value if the projected either is a Left. Otherwise nothing happens.
         *
         * @param action An action which takes a left value
         * @return this LeftProjection
         */
        public LeftProjection<L, R> peek(Consumer<? super L> action) {
            Objects.requireNonNull(action, "action is null");
            if (either.isLeft()) {
                action.accept(asLeft());
            }
            return this;
        }

        /**
         * Maps the left value if the projected Either is a Left.
         *
         * @param mapper A mapper which takes a left value and returns a value of type U
         * @param <U>    The new type of a Left value
         * @return A new LeftProjection
         */
        @SuppressWarnings("unchecked")
        public <U> LeftProjection<U, R> map(Function<? super L, ? extends U> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            if (either.isLeft())
                return new Left<U, R>(mapper.apply(asLeft())).left();
            else {
                return (LeftProjection<U, R>) this;
            }
        }

        /**
         * FlatMaps the left value if the projected Either is a Left.
         *
         * @param mapper A mapper which takes a left value and returns a new Either
         * @param <U>    The new type of a Left value
         * @return A new LeftProjection
         */
        @SuppressWarnings("unchecked")
        public <U> LeftProjection<U, R> flatMap(Function<? super L, ? extends LeftProjection<U, R>> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            if (either.isLeft()) {
                return mapper.apply(asLeft());
            } else {
                return (LeftProjection<U, R>) this;
            }
        }

        @Override
        public Iterator<L> iterator() {
            if (either.isLeft()) {
                return Collections.singleton(asLeft()).iterator();
            } else {
                return Collections.emptyIterator();
            }
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof LeftProjection && Objects.equals(either, ((LeftProjection<?, ?>) obj).either));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(either);
        }

        @Override
        public String toString() {
            return "LeftProjection(" + either + ")";
        }

        private L asLeft() {
            return ((Left<L, R>) either).get();
        }

        private R asRight() {
            return ((Right<L, R>) either).get();
        }
    }

    /**
     * A right projection of an either.
     *
     * @param <L> The type of the Left value of an Either.
     * @param <R> The type of the Right value of an Either.
     * @since 1.0.0
     */
    final class RightProjection<L, R> implements TraversableOnce<R> {

        private final Either<L, R> either;

        private RightProjection(Either<L, R> either) {
            this.either = either;
        }

        /**
         * Gets the Right value or throws.
         *
         * @return the left value, if the underlying Either is a Right
         * @throws NoSuchElementException if the underlying either of this RightProjection is a Left
         */
        public R get() {
            if (either.isRight()) {
                return asRight();
            } else {
                throw new NoSuchElementException("Either.right().get() on Left");
            }
        }

        /**
         * Gets the Right value or an alternate value, if the projected Either is a Left.
         *
         * @param other an alternative value
         * @return the right value, if the underlying Either is a Right or else {@code other}
         * @throws NoSuchElementException if the underlying either of this RightProjection is a Left
         */
        public R orElse(R other) {
            return either.isRight() ? asRight() : other;
        }

        /**
         * Gets the Right value or an alternate value, if the projected Either is a Left.
         *
         * @param other a function which converts a Left value to an alternative Right value
         * @return the right value, if the underlying Either is a Right or else the alternative Right value provided by
         * {@code other} by applying the Left value.
         */
        public R orElseGet(Function<? super L, ? extends R> other) {
            Objects.requireNonNull(other, "other is null");
            if (either.isRight()) {
                return asRight();
            } else {
                return other.apply(asLeft());
            }
        }

        /**
         * Runs an action in the case this is a projection on a Left value.
         *
         * @param action an action which consumes a Left value
         */
        public void orElseRun(Consumer<? super L> action) {
            Objects.requireNonNull(action, "action is null");
            if (either.isLeft()) {
                action.accept(asLeft());
            }
        }

        /**
         * Gets the Right value or throws, if the projected Either is a Left.
         *
         * @param <X>               a throwable type
         * @param exceptionFunction a function which creates an exception based on a Left value
         * @return the right value, if the underlying Either is a Right or else throws the exception provided by
         * {@code exceptionFunction} by applying the Left value.
         * @throws X if the projected Either is a Left
         */
        public <X extends Throwable> R orElseThrow(Function<? super L, X> exceptionFunction) throws X {
            Objects.requireNonNull(exceptionFunction, "exceptionFunction is null");
            if (either.isRight()) {
                return asRight();
            } else {
                throw exceptionFunction.apply(asLeft());
            }
        }

        /**
         * Converts this Either to an {@linkplain javaslang.control.Option}.
         *
         * @return {@linkplain javaslang.control.Some} of the right value if this is a projection of a Right,
         * {@linkplain javaslang.control.None} otherwise.
         */
        public Option<R> toOption() {
            if (either.isRight()) {
                return new Some<>(asRight());
            } else {
                return None.instance();
            }
        }

        /**
         * Returns the underlying either of this projection.
         *
         * @return the underlying either
         */
        public Either<L, R> toEither() {
            return either;
        }

        /**
         * Converts this Either to a {@linkplain java.util.Optional}.
         *
         * @return {@code Optional.ofNullable(rightValue)} if this is a projection of a Right,
         * {@code Optional.empty()} otherwise.
         */
        public Optional<R> toJavaOptional() {
            if (either.isRight()) {
                return Optional.ofNullable(asRight());
            } else {
                return Optional.empty();
            }
        }

        /**
         * Returns
         * <ul>
         * <li>{@code RightProjection(Right(Some(value)))}, if the underlying {@code Either} of this projection is a
         * {@code Right} and the right value satisfies the given predicate</li>
         * <li>{@code RightProjection(Right(None)))} if the underlying {@code Either} of this projection
         * is a {@code Right} and the right value does <em>not</em> satisfy the given predicate</li>
         * <li>{@code RightProjection(Left(Some(value)))} otherwise, i.e. if the underlying {@code Either} of this
         * projection is a {@code Left}</li>
         * </ul>
         *
         * @param predicate A predicate
         * @return a RightProjection of an {@code Either} with an optional value
         */
        public RightProjection<Option<L>, Option<R>> filter(Predicate<? super R> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            if (either.isLeft() || (either.isRight() && predicate.test(asRight()))) {
                return new RightProjection<>(either.bimap(Some::new, Some::new));
            } else {
                return new RightProjection<>(new Right<>(None.instance()));
            }
        }

        /**
         * Flattens a {@code RightProjection} using a function.
         *
         * @param <U> the new type of the right value
         * @param f   a function which maps elements of this RightProjection to RightProjections
         * @return a {@code RightProjection}
         * @throws NullPointerException if {@code f} is null
         */
        @SuppressWarnings("unchecked")
        public <U> RightProjection<L, U> flatten(Function<? super R, ? extends RightProjection<L, U>> f) {
            Objects.requireNonNull(f, "f is null");
            if (either.isLeft()) {
                return (RightProjection<L, U>) this;
            } else {
                return f.apply(get());
            }
        }

        /**
         * Applies the given action to the value if the projected either is a Right. Otherwise nothing happens.
         *
         * @param action An action which takes a right value
         * @return this {@code Either} instance
         */
        public RightProjection<L, R> peek(Consumer<? super R> action) {
            Objects.requireNonNull(action, "action is null");
            if (either.isRight()) {
                action.accept(asRight());
            }
            return this;
        }

        /**
         * Maps the right value if the projected Either is a Right.
         *
         * @param mapper A mapper which takes a right value and returns a value of type U
         * @param <U>    The new type of a Right value
         * @return A new RightProjection
         */
        @SuppressWarnings("unchecked")
        public <U> RightProjection<L, U> map(Function<? super R, ? extends U> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            if (either.isRight())
                return new Right<L, U>(mapper.apply(asRight())).right();
            else {
                return (RightProjection<L, U>) this;
            }
        }

        /**
         * FlatMaps the right value if the projected Either is a Right.
         *
         * @param mapper A mapper which takes a right value and returns a new Either
         * @param <U>    The new type of a Right value
         * @return A new RightProjection
         */
        @SuppressWarnings("unchecked")
        public <U> RightProjection<L, U> flatMap(Function<? super R, ? extends RightProjection<L, U>> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            if (either.isRight()) {
                return mapper.apply(asRight());
            } else {
                return (RightProjection<L, U>) this;
            }
        }

        @Override
        public Iterator<R> iterator() {
            if (either.isRight()) {
                return Collections.singleton(asRight()).iterator();
            } else {
                return Collections.emptyIterator();
            }
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof RightProjection && Objects.equals(either, ((RightProjection<?, ?>) obj).either));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(either);
        }

        @Override
        public String toString() {
            return "RightProjection(" + either + ")";
        }

        private L asLeft() {
            return ((Left<L, R>) either).get();
        }

        private R asRight() {
            return ((Right<L, R>) either).get();
        }
    }
}
