/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.control;

import io.vavr.Value;
import io.vavr.collection.Iterator;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
 * If the result of compute() is Left("error"), the value is Left("error").
 *
 * @param <L> The type of the Left value of an Either.
 * @param <R> The type of the Right value of an Either.
 * @author Daniel Dietrich
 */
public interface Either<L, R> extends Value<R>, Serializable {

    long serialVersionUID = 1L;

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
     * Narrows a widened {@code Either<? extends L, ? extends R>} to {@code Either<L, R>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param either A {@code Either}.
     * @param <L>    Type of left value.
     * @param <R>    Type of right value.
     * @return the given {@code either} instance as narrowed type {@code Either<L, R>}.
     */
    @SuppressWarnings("unchecked")
    static <L, R> Either<L, R> narrow(Either<? extends L, ? extends R> either) {
        return (Either<L, R>) either;
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
     * Reduces many {@code Either}s into a single {@code Either} by transforming an
     * {@code Iterable<Either<L, R>>} into a {@code Either<Seq<L>, Seq<R>>}.
     * <p>
     * If any of the given {@code Either}s is a {@link Either.Left} then {@code sequence} returns a
     * {@link Either.Left} containing a non-empty {@link Seq} of all left values.
     * <p>
     * If none of the given {@code Either}s is a {@link Either.Left} then {@code sequence} returns a
     * {@link Either.Right} containing a (possibly empty) {@link Seq} of all right values.
     *
     * <pre>{@code
     * // = Right(Seq())
     * Either.sequence(List.empty())
     *
     * // = Right(Seq(1, 2))
     * Either.sequence(List.of(Either.right(1), Either.right(2)))
     *
     * // = Left(Seq("x"))
     * Either.sequence(List.of(Either.right(1), Either.left("x")))
     * }</pre>
     *
     * @param eithers An {@link Iterable} of {@code Either}s
     * @param <L>     closure of all left types of the given {@code Either}s
     * @param <R>     closure of all right types of the given {@code Either}s
     * @return An {@code Either} of a {@link Seq} of left or right values
     * @throws NullPointerException if {@code eithers} is null
     */
    @SuppressWarnings("unchecked")
    static <L,R> Either<Seq<L>, Seq<R>> sequence(Iterable<? extends Either<? extends L, ? extends R>> eithers) {
        Objects.requireNonNull(eithers, "eithers is null");
        return Iterator.ofAll((Iterable<Either<L, R>>) eithers)
                .partition(Either::isLeft)
                .apply((leftPartition, rightPartition) -> leftPartition.hasNext()
                    ? Either.left(leftPartition.map(Either::getLeft).toVector())
                    : Either.right(rightPartition.map(Either::get).toVector())
                );
    }

    /**
     * Maps the values of an iterable to a sequence of mapped values into a single {@code Either} by
     * transforming an {@code Iterable<? extends T>} into a {@code Either<Seq<U>>}.
     * <p>
     *
     * @param values   An {@code Iterable} of values.
     * @param mapper   A mapper of values to Eithers
     * @param <L>      The mapped left value type.
     * @param <R>      The mapped right value type.
     * @param <T>      The type of the given values.
     * @return A {@code Either} of a {@link Seq} of results.
     * @throws NullPointerException if values or f is null.
     */
    static <L, R, T> Either<Seq<L>, Seq<R>> traverse(Iterable<? extends T> values, Function<? super T, ? extends Either<? extends L, ? extends R>> mapper) {
        Objects.requireNonNull(values, "values is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return sequence(Iterator.ofAll(values).map(mapper));
    }

    /**
     * Reduces many {@code Either}s into a single {@code Either} by transforming an
     * {@code Iterable<Either<L, R>>} into a {@code Either<L, Seq<R>>}.
     * <p>
     * If any of the given {@code Either}s is a {@link Either.Left} then {@code sequenceRight} returns a
     * {@link Either.Left} containing the first left value (in iteration order).
     * <p>
     * If none of the given {@code Either}s is a {@link Either.Left} then {@code sequenceRight} returns a
     * {@link Either.Right} containing a (possibly empty) {@link Seq} of all right values.
     *
     * <pre>{@code
     * // = Right(Seq())
     * Either.sequenceRight(List.empty())
     *
     * // = Right(Seq(1, 2))
     * Either.sequenceRight(List.of(Either.right(1), Either.right(2)))
     *
     * // = Left("x1")
     * Either.sequenceRight(List.of(Either.right(1), Either.left("x1"), Either.left("x2")))
     * }</pre>
     *
     * @param eithers An {@link Iterable} of {@code Either}s
     * @param <L>     closure of all left types of the given {@code Either}s
     * @param <R>     closure of all right types of the given {@code Either}s
     * @return An {@code Either} of either a {@link Seq} of right values or the first left value, if present.
     * @throws NullPointerException if {@code eithers} is null
     */
    static <L,R> Either<L, Seq<R>> sequenceRight(Iterable<? extends Either<? extends L, ? extends R>> eithers) {
        Objects.requireNonNull(eithers, "eithers is null");
        Vector<R> rightValues = Vector.empty();
        for (Either<? extends L, ? extends R> either : eithers) {
            if (either.isRight()) {
                rightValues = rightValues.append(either.get());
            } else {
                return Either.left(either.getLeft());
            }
        }
        return Either.right(rightValues);
    }

    /**
     * Maps the values of an iterable to a sequence of mapped values into a single {@code Either} by
     * transforming an {@code Iterable<? extends T>} into a {@code Either<Seq<U>>}.
     * <p>
     *
     * @param values   An {@code Iterable} of values.
     * @param mapper   A mapper of values to Eithers
     * @param <L>      The mapped left value type.
     * @param <R>      The mapped right value type.
     * @param <T>      The type of the given values.
     * @return A {@code Either} of a {@link Seq} of results.
     * @throws NullPointerException if values or f is null.
     */
    static <L, R, T> Either<L, Seq<R>> traverseRight(Iterable<? extends T> values, Function<? super T, ? extends Either<? extends L, ? extends R>> mapper) {
        Objects.requireNonNull(values, "values is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return sequenceRight(Iterator.ofAll(values).map(mapper));
    }

    /**
     * Gets the Right value or an alternate value, if the projected Either is a Left.
     *
     * @param other a function which converts a Left value to an alternative Right value
     * @return the right value, if the underlying Either is a Right or else the alternative Right value provided by
     * {@code other} by applying the Left value.
     */
    default R getOrElseGet(Function<? super L, ? extends R> other) {
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
    default <X extends Throwable> R getOrElseThrow(Function<? super L, X> exceptionFunction) throws X {
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

    // -- Adjusted return types of Monad methods

    /**
     * FlatMaps this right-biased Either.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped right value
     * @return this as {@code Either<L, U>} if this is a Left, otherwise the right mapping result
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
     * Maps the value of this Either if it is a Right, performs no operation if this is a Left.
     *
     * <pre><code>
     * import static io.vavr.API.*;
     *
     * // = Right("A")
     * Right("a").map(String::toUpperCase);
     *
     * // = Left(1)
     * Left(1).map(String::toUpperCase);
     * </code></pre>
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
     * Maps the value of this Either if it is a Left, performs no operation if this is a Right.
     *
     * <pre>{@code
     * import static io.vavr.API.*;
     *
     * // = Left(2)
     * Left(1).mapLeft(i -> i + 1);
     *
     * // = Right("a")
     * Right("a").mapLeft(i -> i + 1);
     * }</pre>
     *
     * @param leftMapper A mapper
     * @param <U>        Component type of the mapped right value
     * @return a mapped {@code Monad}
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    default <U> Either<U, R> mapLeft(Function<? super L, ? extends U> leftMapper) {
        Objects.requireNonNull(leftMapper, "leftMapper is null");
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
    default Option<Either<L, R>> filter(Predicate<? super R> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return isLeft() || predicate.test(get()) ? Option.some(this) : Option.none();
    }

    /**
     * Filters this right-biased {@code Either} by testing a predicate.
     * If the {@code Either} is a {@code Right} and the predicate doesn't match, the
     * {@code Either} will be turned into a {@code Left} with contents computed by applying
     * the filterVal function to the {@code Either} value.
     *
     * <pre>{@code
     * import static io.vavr.API.*;
     *
     * // = Left("bad: a")
     * Right("a").filterOrElse(i -> false, val -> "bad: " + val);
     *
     * // = Right("a")
     * Right("a").filterOrElse(i -> true, val -> "bad: " + val);
     * }</pre>
     *
     * @param predicate A predicate
     * @param zero a function that transforms the right value to a left value, if it does not make it through the given filter {@code predicate}
     * @return an {@code Either} instance
     * @throws NullPointerException if {@code predicate} is null
     */
    default Either<L,R> filterOrElse(Predicate<? super R> predicate, Function<? super R, ? extends L> zero) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(zero, "zero is null");
        if (isLeft() || predicate.test(get())) {
            return this;
        } else {
            return Either.left(zero.apply(get()));
        }
    }

    /**
     * Gets the right value if this is a {@code Right} or throws if this is a {@code Left}.
     *
     * @return the right value
     * @throws NoSuchElementException if this is a {@code Left}.
     */
    @Override
    R get();

    @Override
    default boolean isEmpty() {
        return isLeft();
    }

    @SuppressWarnings("unchecked")
    default Either<L, R> orElse(Either<? extends L, ? extends R> other) {
        Objects.requireNonNull(other, "other is null");
        return isRight() ? this : (Either<L, R>) other;
    }

    @SuppressWarnings("unchecked")
    default Either<L, R> orElse(Supplier<? extends Either<? extends L, ? extends R>> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isRight() ? this : (Either<L, R>) supplier.get();
    }

    /**
     * A right-biased {@code Either}'s value is computed synchronously.
     *
     * @return false
     */
    @Override
    default boolean isAsync() {
        return false;
    }

    /**
     * A right-biased {@code Either}'s value is computed eagerly.
     *
     * @return false
     */
    @Override
    default boolean isLazy() {
        return false;
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

    default Either<L, R> peekLeft(Consumer<? super L> action) {
        Objects.requireNonNull(action, "action is null");
        if (isLeft()) {
            action.accept(getLeft());
        }
        return this;
    }

    /**
     * Returns this as {@code Validation}.
     *
     * @return {@code Validation.valid(get())} if this is right, otherwise {@code Validation.invalid(getLeft())}.
     */
    default Validation<L, R> toValidation() {
        return isRight() ? Validation.valid(get()) : Validation.invalid(getLeft());
    }

    // -- Object.*

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    /**
     * The {@code Left} version of an {@code Either}.
     *
     * @param <L> left component type
     * @param <R> right component type
     * @author Daniel Dietrich
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
