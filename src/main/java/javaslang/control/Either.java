/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Value;
import javaslang.algebra.Kind1;
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
public interface Either<L, R> extends Monad<Either<L, ?>, R>, Value<R> {

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
     * Returns the left value.
     *
     * @return The left value.
     * @throws NoSuchElementException if this is a {@code Right}.
     */
    L getLeft();

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
    default <X, Y> Either<X, Y> bimap(Function<? super L, ? extends X> leftMapper, Function<? super R, ? extends Y> rightMapper) {
        Objects.requireNonNull(leftMapper, "leftMapper is null");
        Objects.requireNonNull(rightMapper, "rightMapper is null");
        if (isRight()) {
            return new Right<>(rightMapper.apply(get()));
        } else {
            return new Left<>(leftMapper.apply(getLeft()));
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
    default <U> U fold(Function<? super L, ? extends U> leftMapper, Function<? super R, ? extends U> rightMapper) {
        Objects.requireNonNull(leftMapper, "leftMapper is null");
        Objects.requireNonNull(rightMapper, "rightMapper is null");
        if (isRight()) {
            return rightMapper.apply(get());
        } else {
            return leftMapper.apply(getLeft());
        }
    }

    /**
     * Gets the Right value or an alternate value, if the projected Either is a Left.
     *
     * @param other a function which converts a Left value to an alternative Right value
     * @return the right value, if the underlying Either is a Right or else the alternative Right value provided by
     * {@code other} by applying the Left value.
     */
    default R orElseGet(Function<? super L, ? extends R> other) {
        Objects.requireNonNull(other, "other is null");
        if (isRight()) {
            return get();
        } else {
            return other.apply(getLeft());
        }
    }

    /**
     * Runs an action in the case this is a projection on a Left value.
     *
     * @param action an action which consumes a Left value
     */
    default void orElseRun(Consumer<? super L> action) {
        Objects.requireNonNull(action, "action is null");
        if (isLeft()) {
            action.accept(getLeft());
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
    default <X extends Throwable> R orElseThrow(Function<? super L, X> exceptionFunction) throws X {
        Objects.requireNonNull(exceptionFunction, "exceptionFunction is null");
        if (isRight()) {
            return get();
        } else {
            throw exceptionFunction.apply(getLeft());
        }
    }

    /**
     * Converts a {@code Left} to a {@code Right} vice versa by wrapping the value in a new type.
     *
     * @return a new {@code Either}
     */
    default Either<R, L> swap() {
        if (isRight()) {
            return new Left<>(get());
        } else {
            return new Right<>(getLeft());
        }
    }

    // -- Adjusted return types of Convertible methods

    @Override
    default Match.MatchMonad.Of<Either<L, R>> match() {
        return Match.of(this);
    }

    // -- Adjusted return types of Monad methods

    /**
     * FlatMaps this right-biased Either.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped right value
     * @return this as {@code Either<L, U>} if this is a Left, otherwise a the mapping result of the right value.
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    default <U> Either<L, U> flatMap(Function<? super R, ? extends Either<L, ? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isRight()) {
            return (Either<L, U>) mapper.apply(get());
        } else {
            return (Either<L, U>) this;
        }
    }

    /**
     * Calls {@link Either#flatMap(Function)}.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped right value
     * @return this as {@code Either<L, U>} if this is a Left, otherwise a the mapping result of the right value.
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    @Override
    default <U> Either<L, U> flatMapM(Function<? super R, ? extends Kind1<Either<L, ?>, U>> mapper) {
        return flatMap((Function<R, Either<L, U>>) mapper);
    }

    /**
     * Maps this right-biased Either.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped right value
     * @return a mapped {@code Monad}
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    @Override
    default <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isRight()) {
            return Either.right(mapper.apply(get()));
        } else {
            return (Either<L, U>) this;
        }
    }
    
    /**
     * Maps this right-biased Either.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped right value
     * @return a mapped {@code Monad}
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    default <U> Either<U, R> mapLeft(Function<? super L, ? extends U> leftMapper) {
        Objects.requireNonNull(leftMapper, "mapper is null");
        if (isLeft()) {
            return Either.left(leftMapper.apply(getLeft()));
        } else {
            return (Either<U, R>) this;
        }
    }

    // -- Adjusted return types of Value methods

    /**
     * Filters this right-biased {@code Either} by testing a predicate.
     * <p>
     *
     * @param predicate A predicate
     * @return a new {@code Option} instance
     * @throws NullPointerException if {@code predicate} is null
     */
    @Override
    default Option<R> filter(Predicate<? super R> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return getOption().filter(predicate);
    }

    /**
     * Filters this right-biased {@code Either} by testing the negation of a predicate.
     * <p>
     * Shortcut for {@code filter(predicate.negate()}.
     *
     * @param predicate A predicate
     * @return a new {@code Option} instance
     * @throws NullPointerException if {@code predicate} is null
     */
    @Override
    default Option<R> filterNot(Predicate<? super R> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return getOption().filterNot(predicate);
    }

    /**
     * Gets the right value if this is a {@code Right} or throws if this is a {@code Left}.
     *
     * @return the right value
     * @throws NoSuchElementException if this is a Left.
     */
    @Override
    R get();

    @Override
    default boolean isEmpty() {
        return isLeft();
    }

    /**
     * A right-biased {@code Either} is single-valued.
     *
     * @return {@code true}
     */
    @Override
    default boolean isSingleValued() {
        return true;
    }

    @Override
    default Iterator<R> iterator() {
        if (isRight()) {
            return Iterator.of(get());
        } else {
            return Iterator.empty();
        }
    }

    @Override
    default Either<L, R> peek(Consumer<? super R> action) {
        Objects.requireNonNull(action, "action is null");
        if (isRight()) {
            action.accept(get());
        }
        return this;
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
     * A left projection of an Either.
     *
     * @param <L> The type of the Left value of an Either.
     * @param <R> The type of the Right value of an Either.
     * @since 1.0.0
     */
    final class LeftProjection<L, R> implements Monad<LeftProjection<?, R>, L>, Value<L> {

        private final Either<L, R> either;

        private LeftProjection(Either<L, R> either) {
            this.either = either;
        }

        @Override
        public boolean isEmpty() {
            return either.isRight();
        }

        /**
         * A {@code LeftProjection} is single-valued.
         *
         * @return {@code true}
         */
        @Override
        public boolean isSingleValued() {
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
                return either.getLeft();
            } else {
                throw new NoSuchElementException("LeftProjection.get() on Right");
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
            return either.isLeft() ? either.getLeft() : other;
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
                return either.getLeft();
            } else {
                return other.apply(either.get());
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
                action.accept(either.get());
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
                return either.getLeft();
            } else {
                throw exceptionFunction.apply(either.get());
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
                final L value = either.getLeft();
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
         * FlatMaps this LeftProjection.
         *
         * @param mapper A mapper
         * @param <U>    Component type of the mapped left value
         * @return this as {@code LeftProjection<L, U>} if a Right is underlying, otherwise a the mapping result of the left value.
         * @throws NullPointerException if {@code mapper} is null
         */
        @SuppressWarnings("unchecked")
        public <U> LeftProjection<U, R> flatMap(Function<? super L, ? extends LeftProjection<? extends U, R>> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            if (either.isLeft()) {
                return (LeftProjection<U, R>) mapper.apply(either.getLeft());
            } else {
                return (LeftProjection<U, R>) this;
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> LeftProjection<U, R> flatMapM(Function<? super L, ? extends Kind1<LeftProjection<?, R>, U>> mapper) {
            return flatMap((Function<L, LeftProjection<U, R>>) mapper);
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
            if (either.isLeft()) {
            	return either.mapLeft(mapper).left();
            } else {
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
                action.accept(either.getLeft());
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
                return Iterator.of(either.getLeft());
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
    }

    /**
     * A right projection of an Either.
     *
     * @param <L> The type of the Left value of an Either.
     * @param <R> The type of the Right value of an Either.
     * @since 1.0.0
     */
    final class RightProjection<L, R> implements Monad<RightProjection<L, ?>, R>, Value<R> {

        private final Either<L, R> either;

        private RightProjection(Either<L, R> either) {
            this.either = either;
        }

        @Override
        public boolean isEmpty() {
            return either.isLeft();
        }

        /**
         * A {@code RightProjection} is single-valued.
         *
         * @return {@code true}
         */
        @Override
        public boolean isSingleValued() {
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
                return either.get();
            } else {
                throw new NoSuchElementException("RightProjection.get() on Left");
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
            return either.orElse(other);
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
            return either.orElseGet(other);
        }

        /**
         * Runs an action in the case this is a projection on a Left value.
         *
         * @param action an action which consumes a Left value
         */
        public void orElseRun(Consumer<? super L> action) {
            Objects.requireNonNull(action, "action is null");
            either.orElseRun(action);
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
            return either.orElseThrow(exceptionFunction);
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
            return either.filter(predicate);
        }

        @Override
        public Option<R> filterNot(Predicate<? super R> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return either.filterNot(predicate);
        }

        /**
         * FlatMaps this RightProjection.
         *
         * @param mapper A mapper
         * @param <U>    Component type of the mapped right value
         * @return this as {@code RightProjection<L, U>} if a Left is underlying, otherwise a the mapping result of the right value.
         * @throws NullPointerException if {@code mapper} is null
         */
        @SuppressWarnings("unchecked")
        public <U> RightProjection<L, U> flatMap(Function<? super R, ? extends RightProjection<L, ? extends U>> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            if (either.isRight()) {
                return (RightProjection<L, U>) mapper.apply(either.get());
            } else {
                return (RightProjection<L, U>) this;
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> RightProjection<L, U> flatMapM(Function<? super R, ? extends Kind1<RightProjection<L, ?>, U>> mapper) {
            return flatMap((Function<R, RightProjection<L, U>>) mapper);
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
            if (either.isRight()) {
                return either.map(mapper).right();
            } else {
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
                action.accept(either.get());
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
            return either.iterator();
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
        public R get() {
            throw new NoSuchElementException("get() on Left");
        }

        @Override
        public L getLeft() {
            return value;
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
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Left && Objects.equals(value, ((Left<?, ?>) obj).value));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String stringPrefix() {
            return "Left";
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + value + ")";
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
        public R get() {
            return value;
        }

        @Override
        public L getLeft() {
            throw new NoSuchElementException("getLeft() on Right");
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
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Right && Objects.equals(value, ((Right<?, ?>) obj).value));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String stringPrefix() {
            return "Right";
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + value + ")";
        }
    }
}
