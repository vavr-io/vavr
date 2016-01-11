/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Value;
import javaslang.algebra.Monad;
import javaslang.collection.Iterator;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Either represents a value of two possible types. An Either is either a {@link Left} or a
 * {@link Right}.
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
 * @author Daniel Dietrich
 * @since 1.0.0
 */
public interface Either<L, R> {

    /**
     * Constructs a {@link Right}
     *
     * @param right The value.
     * @param <L>   Type of left value.
     * @param <R>   Type of right value.
     * @return A new {@code Right} instance.
     */
    static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    /**
     * Constructs a {@link Left}
     *
     * @param left The value.
     * @param <L>  Type of left value.
     * @param <R>  Type of right value.
     * @return A new {@code Left} instance.
     */
    static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

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
     * @return A new Either instance
     */
    @SuppressWarnings("unchecked")
    default <X, Y> Either<X, Y> bimap(Function<? super L, ? extends X> leftMapper, Function<? super R, ? extends Y> rightMapper) {
        Objects.requireNonNull(leftMapper, "leftMapper is null");
        Objects.requireNonNull(rightMapper, "rightMapper is null");
        if (isRight()) {
            return new Right<>(rightMapper.apply((R) get()));
        } else {
            return new Left<>(leftMapper.apply((L) get()));
        }
    }

    /**
     * Folds either the left or the right side of this disjunction.
     *
     * @param leftMapper  maps the left value if this is a Left
     * @param rightMapper maps the right value if this is a Right
     * @param <U>         type of the folded value
     * @return A value of type U
     */
    @SuppressWarnings("unchecked")
    default <U> U fold(Function<? super L, ? extends U> leftMapper, Function<? super R, ? extends U> rightMapper) {
        Objects.requireNonNull(leftMapper, "leftMapper is null");
        Objects.requireNonNull(rightMapper, "rightMapper is null");
        if (isRight()) {
            return rightMapper.apply((R) get());
        } else {
            return leftMapper.apply((L) get());
        }
    }

    /**
     * Returns the left value of type {@code L} if this is a {@code Left},
     * otherwise returns the right value of type {@code R} if this is a {@code Right}.
     * <p>
     * Works well in conjunction with {@link Match}.
     *
     * @return the (left or right) value of this {@code Either}
     */
    Object get();

    /**
     * Converts a {@code Left} to a {@code Right} vice versa by wrapping the value in a new type.
     *
     * @return a new {@code Either}
     */
    @SuppressWarnings("unchecked")
    default Either<R, L> swap() {
        if (isRight()) {
            return new Left<>((R) get());
        } else {
            return new Right<>((L) get());
        }
    }

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
    final class LeftProjection<L, R> implements Monad<L>, Value<L> {

        private final Either<L, R> either;

        private LeftProjection(Either<L, R> either) {
            this.either = either;
        }

        @Override
        public boolean isEmpty() {
            return either.isRight();
        }

        /**
         * A left projection is a singleton type.
         *
         * @return {@code true}
         */
        @Override
        public boolean isSingletonType() {
            return true;
        }

        /**
         * Gets the Left value or throws.
         *
         * @return the left value, if the underlying Either is a Left
         * @throws NoSuchElementException if the underlying either of this LeftProjection is a Right
         */
        @Override
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
        @Override
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
         * Returns the underlying either of this projection.
         *
         * @return the underlying either
         */
        public Either<L, R> toEither() {
            return either;
        }

        /**
         * Returns {@code Some} value of type L if this is a left projection of a Left value and the predicate
         * applies to the underlying value.
         *
         * @param predicate A predicate
         * @return A new Option
         */
        @Override
        public Option<L> filter(Predicate<? super L> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            if (either.isLeft()) {
                final L value = asLeft();
                return predicate.test(value) ? Option.some(value) : Option.none();
            } else {
                return Option.none();
            }
        }

        @Override
        public Option<L> filterNot(Predicate<? super L> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return filter(predicate.negate());
        }

        /**
         * FlatMaps the left value if the projected Either is a Left.
         *
         * @param mapper A mapper which takes a left value and returns a Iterable
         * @param <U>    The new type of a Left value
         * @return A new LeftProjection
         */
        @SuppressWarnings("unchecked")
        @Override
        public <U> LeftProjection<U, R> flatMap(Function<? super L, ? extends Iterable<? extends U>> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            if (either.isLeft()) {
                final Iterable<? extends U> mapped = mapper.apply(asLeft());
                if (mapped instanceof LeftProjection) {
                    return (LeftProjection<U, R>) mapped;
                } else {
                    return new Left<U, R>(Value.get(mapped)).left();
                }
            } else {
                return (LeftProjection<U, R>) this;
            }
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

        @Override
        public Match.MatchMonad.Of<LeftProjection<L, R>> match() {
            return Match.of(this);
        }

        /**
         * Applies the given action to the value if the projected either is a Left. Otherwise nothing happens.
         *
         * @param action An action which takes a left value
         * @return this LeftProjection
         */
        @Override
        public LeftProjection<L, R> peek(Consumer<? super L> action) {
            Objects.requireNonNull(action, "action is null");
            if (either.isLeft()) {
                action.accept(asLeft());
            }
            return this;
        }

        /**
         * Transforms this {@code LeftProjection}.
         *
         * @param f   A transformation
         * @param <U> Type of transformation result
         * @return An instance of type {@code U}
         * @throws NullPointerException if {@code f} is null
         */
        public <U> U transform(Function<? super LeftProjection<? super L, ? super R>, ? extends U> f) {
            Objects.requireNonNull(f, "f is null");
            return f.apply(this);
        }

        @Override
        public Iterator<L> iterator() {
            if (either.isLeft()) {
                return Iterator.of(asLeft());
            } else {
                return Iterator.empty();
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
        public String stringPrefix() {
            return "LeftProjection";
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + either + ")";
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
    final class RightProjection<L, R> implements Monad<R>, Value<R> {

        private final Either<L, R> either;

        private RightProjection(Either<L, R> either) {
            this.either = either;
        }

        @Override
        public boolean isEmpty() {
            return either.isLeft();
        }

        /**
         * A right projection is a singleton type.
         *
         * @return {@code true}
         */
        @Override
        public boolean isSingletonType() {
            return true;
        }

        /**
         * Gets the Right value or throws.
         *
         * @return the left value, if the underlying Either is a Right
         * @throws NoSuchElementException if the underlying either of this RightProjection is a Left
         */
        @Override
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
        @Override
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
         * Returns the underlying either of this projection.
         *
         * @return the underlying either
         */
        public Either<L, R> toEither() {
            return either;
        }

        /**
         * Returns {@code Some} value of type R if this is a right projection of a Right value and the predicate
         * applies to the underlying value.
         *
         * @param predicate A predicate
         * @return A new Option
         */
        @Override
        public Option<R> filter(Predicate<? super R> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            if (either.isRight()) {
                final R value = asRight();
                return predicate.test(value) ? Option.some(value) : Option.none();
            } else {
                return Option.none();
            }
        }

        @Override
        public Option<R> filterNot(Predicate<? super R> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return filter(predicate.negate());
        }

        /**
         * FlatMaps the right value if the projected Either is a Right.
         *
         * @param mapper A mapper which takes a right value and returns a Iterable
         * @param <U>    The new type of a Right value
         * @return A new RightProjection
         */
        @SuppressWarnings("unchecked")
        @Override
        public <U> RightProjection<L, U> flatMap(Function<? super R, ? extends Iterable<? extends U>> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            if (either.isRight()) {
                final Iterable<? extends U> mapped = mapper.apply(asRight());
                if (mapped instanceof RightProjection) {
                    return (RightProjection<L, U>) mapped;
                } else {
                    return new Right<L, U>(Value.get(mapped)).right();
                }
            } else {
                return (RightProjection<L, U>) this;
            }
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

        @Override
        public Match.MatchMonad.Of<RightProjection<L, R>> match() {
            return Match.of(this);
        }

        /**
         * Applies the given action to the value if the projected either is a Right. Otherwise nothing happens.
         *
         * @param action An action which takes a right value
         * @return this {@code Either} instance
         */
        @Override
        public RightProjection<L, R> peek(Consumer<? super R> action) {
            Objects.requireNonNull(action, "action is null");
            if (either.isRight()) {
                action.accept(asRight());
            }
            return this;
        }

        /**
         * Transforms this {@code RightProjection}.
         *
         * @param f   A transformation
         * @param <U> Type of transformation result
         * @return An instance of type {@code U}
         * @throws NullPointerException if {@code f} is null
         */
        public <U> U transform(Function<? super RightProjection<? super L, ? super R>, ? extends U> f) {
            Objects.requireNonNull(f, "f is null");
            return f.apply(this);
        }

        @Override
        public Iterator<R> iterator() {
            if (either.isRight()) {
                return Iterator.of(asRight());
            } else {
                return Iterator.empty();
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
        public String stringPrefix() {
            return "RightProjection";
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + either + ")";
        }

        private L asLeft() {
            return ((Left<L, R>) either).get();
        }

        private R asRight() {
            return ((Right<L, R>) either).get();
        }
    }

    /**
     * The {@code Left} version of an {@code Either}.
     *
     * @param <L> left component type
     * @param <R> right component type
     * @author Daniel Dietrich
     * @since 1.0.0
     */
    final class Left<L, R> implements Either<L, R>, Serializable {

        private static final long serialVersionUID = 1L;

        private final L value;

        /**
         * Constructs a {@code Left}.
         *
         * @param value a left value
         */
        private Left(L value) {
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

        /**
         * Returns the value of this {@code Left}.
         *
         * @return the value of this {@code Left}
         */
        @Override
        public L get() {
            return value;
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

    /**
     * The {@code Right} version of an {@code Either}.
     *
     * @param <L> left component type
     * @param <R> right component type
     * @author Daniel Dietrich
     * @since 1.0.0
     */
    final class Right<L, R> implements Either<L, R>, Serializable {

        private static final long serialVersionUID = 1L;

        private final R value;

        /**
         * Constructs a {@code Right}.
         *
         * @param value a right value
         */
        private Right(R value) {
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

        /**
         * Returns the value of this {@code Right}.
         *
         * @return the value of this {@code Right}
         */
        @Override
        public R get() {
            return value;
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
}
