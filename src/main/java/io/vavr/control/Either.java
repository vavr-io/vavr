/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2019 Vavr, http://vavr.io
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

import io.vavr.collection.Iterator;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;

import java.io.Serializable;
import java.lang.Iterable;
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
 */
@SuppressWarnings("deprecation")
public abstract class Either<L, R> implements io.vavr.Iterable<R>, io.vavr.Value<R>, Serializable {

    private static final long serialVersionUID = 1L;

    // sealed
    private Either() {
    }

    /**
     * Constructs a {@link Right}
     *
     * <pre>{@code
     * // Creates Either instance initiated with right value 1
     * Either<?, Integer> either = Either.right(1);
     * }</pre>
     *
     * @param right The value.
     * @param <L>   Type of left value.
     * @param <R>   Type of right value.
     * @return A new {@code Right} instance.
     */
    public static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    /**
     * Constructs a {@link Left}
     *
     * <pre>{@code
     * // Creates Either instance initiated with left value "error message"
     * Either<String, ?> either = Either.left("error message");
     * }</pre>
     *
     * @param left The value.
     * @param <L>  Type of left value.
     * @param <R>  Type of right value.
     * @return A new {@code Left} instance.
     */
    public static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    /**
     * Narrows a widened {@code Either<? extends L, ? extends R>} to {@code Either<L, R>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     * <pre>{@code
     *
     * // It's ok, Integer inherits from Number
     * Either<?, Number> answer = Either.right(42);
     *
     * // RuntimeException is an Exception
     * Either<Exception, ?> failed = Either.left(new RuntimeException("Vogon poetry recital"));
     *
     * }</pre>
     *
     * @param either A {@code Either}.
     * @param <L>    Type of left value.
     * @param <R>    Type of right value.
     * @return the given {@code either} instance as narrowed type {@code Either<L, R>}.
     */
    @SuppressWarnings("unchecked")
    public static <L, R> Either<L, R> narrow(Either<? extends L, ? extends R> either) {
        return (Either<L, R>) either;
    }

    /**
     * Returns the left value.
     *
     * <pre>{@code
     * // prints "error"
     * System.out.println(Either.left("error").getLeft());
     *
     * // throws NoSuchElementException
     * System.out.println(Either.right(42).getLeft());
     * }</pre>
     *
     * @return The left value.
     * @throws NoSuchElementException if this is a {@code Right}.
     */
    public abstract L getLeft();

    /**
     * Returns whether this Either is a Left.
     *
     * <pre>{@code
     * // prints "true"
     * System.out.println(Either.left("error").isLeft());
     *
     * // prints "false"
     * System.out.println(Either.right(42).isLeft());
     * }</pre>
     *
     * @return true, if this is a Left, false otherwise
     */
    public abstract boolean isLeft();

    /**
     * Returns whether this Either is a Right.
     *
     * <pre>{@code
     * // prints "true"
     * System.out.println(Either.right(42).isRight());
     *
     * // prints "false"
     * System.out.println(Either.left("error").isRight());
     * }</pre>
     *
     * @return true, if this is a Right, false otherwise
     */
    public abstract boolean isRight();

    /**
     * Returns a LeftProjection of this Either.
     *
     * @return a new LeftProjection of this
     * @deprecated Either is right-biased. Use {@link #swap()} instead of projections.
     */
    @Deprecated
    public final LeftProjection<L, R> left() {
        return new LeftProjection<>(this);
    }

    /**
     * Returns a RightProjection of this Either.
     *
     * @return a new RightProjection of this
     * @deprecated Either is right-biased. Use {@link #swap()} instead of projections.
     */
    @Deprecated
    public final RightProjection<L, R> right() {
        return new RightProjection<>(this);
    }

    /**
     * Maps either the left or the right side of this disjunction.
     *
     * <pre>{@code
     * Either<?, AtomicInteger> success = Either.right(new AtomicInteger(42));
     *
     * // prints "Right(42)"
     * System.out.println(success.bimap(Function1.identity(), AtomicInteger::get));
     *
     * Either<Exception, ?> failure = Either.left(new Exception("error"));
     *
     * // prints "Left(error)"
     * System.out.println(failure.bimap(Exception::getMessage, Function1.identity()));
     * }</pre>
     *
     * @param leftMapper  maps the left value if this is a Left
     * @param rightMapper maps the right value if this is a Right
     * @param <X>         The new left type of the resulting Either
     * @param <Y>         The new right type of the resulting Either
     * @return A new Either instance
     */
    public final <X, Y> Either<X, Y> bimap(Function<? super L, ? extends X> leftMapper, Function<? super R, ? extends Y> rightMapper) {
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
     * <pre>{@code
     * Either<Exception, Integer> success = Either.right(3);
     *
     * // prints "Users updated: 3"
     * System.out.println(success.fold(Exception::getMessage, count -> "Users updated: " + count));
     *
     * Either<Exception, Integer> failure = Either.left(new Exception("Failed to update users"));
     *
     * // prints "Failed to update users"
     * System.out.println(failure.fold(Exception::getMessage, count -> "Users updated: " + count));
     * }</pre>
     *
     * @param leftMapper  maps the left value if this is a Left
     * @param rightMapper maps the right value if this is a Right
     * @param <U>         type of the folded value
     * @return A value of type U
     */
    public final <U> U fold(Function<? super L, ? extends U> leftMapper, Function<? super R, ? extends U> rightMapper) {
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
    public static <L,R> Either<Seq<L>, Seq<R>> sequence(Iterable<? extends Either<? extends L, ? extends R>> eithers) {
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
     * <pre>{@code
     * Function<Integer, Either<Exception, Double>> validatingMapper =
     *         i -> i < 0 ? Either.left(new Exception("invalid value")) :
     *                         Either.right(Math.sqrt(i));
     *
     * // prints "Right(Vector(2.0, 0.0, 3.0))"
     * System.out.println(Either.traverse(Seq(4, 0, 9), validatingMapper));
     *
     * // prints "Left(Vector(java.lang.Exception: invalid value))"
     * System.out.println(Either.traverse(Seq(4, 0, -12), validatingMapper));
     * }</pre>
     *
     * @param values   An {@code Iterable} of values.
     * @param mapper   A mapper of values to Eithers
     * @param <L>      The mapped left value type.
     * @param <R>      The mapped right value type.
     * @param <T>      The type of the given values.
     * @return A {@code Either} of a {@link Seq} of results.
     * @throws NullPointerException if values or f is null.
     */
    public static <L, R, T> Either<Seq<L>, Seq<R>> traverse(Iterable<? extends T> values, Function<? super T, ? extends Either<? extends L, ? extends R>> mapper) {
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
    public static <L,R> Either<L, Seq<R>> sequenceRight(Iterable<? extends Either<? extends L, ? extends R>> eithers) {
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
    public static <L, R, T> Either<L, Seq<R>> traverseRight(Iterable<? extends T> values, Function<? super T, ? extends Either<? extends L, ? extends R>> mapper) {
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
    public final R getOrElseGet(Function<? super L, ? extends R> other) {
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
    public final void orElseRun(Consumer<? super L> action) {
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
    public final <X extends Throwable> R getOrElseThrow(Function<? super L, X> exceptionFunction) throws X {
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
    public final Either<R, L> swap() {
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
    public final <U> Either<L, U> flatMap(Function<? super R, ? extends Either<L, ? extends U>> mapper) {
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
    public final <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
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
    public final <U> Either<U, R> mapLeft(Function<? super L, ? extends U> leftMapper) {
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
    public final Option<Either<L, R>> filter(Predicate<? super R> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return isLeft() || predicate.test(get()) ? Option.some(this) : Option.none();
    }

    /**
     * Filters this right-biased {@code Either} by testing a predicate.
     *
     * @param predicate A predicate
     * @return a new {@code Either}
     * @throws NullPointerException if {@code predicate} is null
     *
     */
    public final Option<Either<L, R>> filterNot(Predicate<? super R> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
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
     * @param zero      A function that turns a right value into a left value if the right value does not make it through the filter.
     * @return an {@code Either} instance
     * @throws NullPointerException if {@code predicate} is null
     */
    public final Either<L,R> filterOrElse(Predicate<? super R> predicate, Function<? super R, ? extends L> zero) {
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
    public abstract R get();

    @Override
    public final boolean isEmpty() {
        return isLeft();
    }

    @SuppressWarnings("unchecked")
    public final Either<L, R> orElse(Either<? extends L, ? extends R> other) {
        Objects.requireNonNull(other, "other is null");
        return isRight() ? this : (Either<L, R>) other;
    }

    @SuppressWarnings("unchecked")
    public final Either<L, R> orElse(Supplier<? extends Either<? extends L, ? extends R>> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isRight() ? this : (Either<L, R>) supplier.get();
    }

    /**
     * A right-biased {@code Either}'s value is computed synchronously.
     *
     * @return false
     */
    @Override
    public final boolean isAsync() {
        return false;
    }

    /**
     * A right-biased {@code Either}'s value is computed eagerly.
     *
     * @return false
     */
    @Override
    public final boolean isLazy() {
        return false;
    }

    /**
     * A right-biased {@code Either} is single-valued.
     *
     * @return {@code true}
     */
    @Override
    public final boolean isSingleValued() {
        return true;
    }

    @Override
    public final Iterator<R> iterator() {
        if (isRight()) {
            return Iterator.of(get());
        } else {
            return Iterator.empty();
        }
    }

    @Override
    public final Either<L, R> peek(Consumer<? super R> action) {
        Objects.requireNonNull(action, "action is null");
        if (isRight()) {
            action.accept(get());
        }
        return this;
    }

    public final Either<L, R> peekLeft(Consumer<? super L> action) {
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
    public final Validation<L, R> toValidation() {
        return isRight() ? Validation.valid(get()) : Validation.invalid(getLeft());
    }

    // -- Left/Right projections

    /**
     * A left projection of an Either.
     *
     * @param <L> The type of the Left value of an Either.
     * @param <R> The type of the Right value of an Either.
     * @deprecated Either is right-biased. Use {@link #swap()} instead of projections.
     */
    @Deprecated
    public static final class LeftProjection<L, R> implements io.vavr.Value<L> {

        private final Either<L, R> either;

        private LeftProjection(Either<L, R> either) {
            this.either = either;
        }

        public <L2, R2> LeftProjection<L2, R2> bimap(Function<? super L, ? extends L2> leftMapper, Function<? super R, ? extends R2> rightMapper) {
            return either.<L2, R2> bimap(leftMapper, rightMapper).left();
        }

        /**
         * A {@code LeftProjection}'s value is computed synchronously.
         *
         * @return false
         */
        @Override
        public boolean isAsync() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return either.isRight();
        }

        /**
         * A {@code LeftProjection}'s value is computed eagerly.
         *
         * @return false
         */
        @Override
        public boolean isLazy() {
            return false;
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
         * Gets the {@code Left} value or throws.
         *
         * @return the left value, if the underlying {@code Either} is a {@code Left}
         * @throws NoSuchElementException if the underlying {@code Either} of this {@code LeftProjection} is a {@code Right}
         */
        @Override
        public L get() {
            if (either.isLeft()) {
                return either.getLeft();
            } else {
                throw new NoSuchElementException("LeftProjection.get() on Right");
            }
        }

        @SuppressWarnings("unchecked")
        public LeftProjection<L, R> orElse(LeftProjection<? extends L, ? extends R> other) {
            Objects.requireNonNull(other, "other is null");
            return either.isLeft() ? this : (LeftProjection<L, R>) other;
        }

        @SuppressWarnings("unchecked")
        public LeftProjection<L, R> orElse(Supplier<? extends LeftProjection<? extends L, ? extends R>> supplier) {
            Objects.requireNonNull(supplier, "supplier is null");
            return either.isLeft() ? this : (LeftProjection<L, R>) supplier.get();
        }

        /**
         * Gets the Left value or an alternate value, if the projected Either is a Right.
         *
         * @param other an alternative value
         * @return the left value, if the underlying Either is a Left or else {@code other}
         * @throws NoSuchElementException if the underlying either of this LeftProjection is a Right
         */
        @Override
        public L getOrElse(L other) {
            return either.isLeft() ? either.getLeft() : other;
        }

        /**
         * Gets the Left value or an alternate value, if the projected Either is a Right.
         *
         * @param other a function which converts a Right value to an alternative Left value
         * @return the left value, if the underlying Either is a Left or else the alternative Left value provided by
         * {@code other} by applying the Right value.
         */
        public L getOrElseGet(Function<? super R, ? extends L> other) {
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
        public <X extends Throwable> L getOrElseThrow(Function<? super R, X> exceptionFunction) throws X {
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
        public Option<LeftProjection<L, R>> filter(Predicate<? super L> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return either.isRight() || predicate.test(either.getLeft()) ? Option.some(this) : Option.none();
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

        /**
         * Maps the left value if the projected Either is a Left.
         *
         * @param mapper A mapper which takes a left value and returns a value of type U
         * @param <U>    The new type of a Left value
         * @return A new LeftProjection
         */
        @SuppressWarnings("unchecked")
        @Override
        public <U> LeftProjection<U, R> map(Function<? super L, ? extends U> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            if (either.isLeft()) {
                return either.mapLeft((Function<L, U>) mapper).left();
            } else {
                return (LeftProjection<U, R>) this;
            }
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
        public <U> U transform(Function<? super LeftProjection<L, R>, ? extends U> f) {
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
            return either.hashCode();
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
     * @deprecated Either is right-biased. Use {@link #swap()} instead of projections.
     */
    @Deprecated
    public static final class RightProjection<L, R> implements io.vavr.Value<R> {

        private final Either<L, R> either;

        private RightProjection(Either<L, R> either) {
            this.either = either;
        }

        public <L2, R2> RightProjection<L2, R2> bimap(Function<? super L, ? extends L2> leftMapper, Function<? super R, ? extends R2> rightMapper) {
            return either.<L2, R2> bimap(leftMapper, rightMapper).right();
        }

        /**
         * A {@code RightProjection}'s value is computed synchronously.
         *
         * @return false
         */
        @Override
        public boolean isAsync() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return either.isLeft();
        }

        /**
         * A {@code RightProjection}'s value is computed eagerly.
         *
         * @return false
         */
        @Override
        public boolean isLazy() {
            return false;
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
         * Gets the {@code Right} value or throws.
         *
         * @return the right value, if the underlying {@code Either} is a {@code Right}
         * @throws NoSuchElementException if the underlying {@code Either} of this {@code RightProjection} is a {@code Left}
         */
        @Override
        public R get() {
            if (either.isRight()) {
                return either.get();
            } else {
                throw new NoSuchElementException("RightProjection.get() on Left");
            }
        }

        @SuppressWarnings("unchecked")
        public RightProjection<L, R> orElse(RightProjection<? extends L, ? extends R> other) {
            Objects.requireNonNull(other, "other is null");
            return either.isRight() ? this : (RightProjection<L, R>) other;
        }

        @SuppressWarnings("unchecked")
        public RightProjection<L, R> orElse(Supplier<? extends RightProjection<? extends L, ? extends R>> supplier) {
            Objects.requireNonNull(supplier, "supplier is null");
            return either.isRight() ? this : (RightProjection<L, R>) supplier.get();
        }

        /**
         * Gets the Right value or an alternate value, if the projected Either is a Left.
         *
         * @param other an alternative value
         * @return the right value, if the underlying Either is a Right or else {@code other}
         * @throws NoSuchElementException if the underlying either of this RightProjection is a Left
         */
        @Override
        public R getOrElse(R other) {
            return either.getOrElse(other);
        }

        /**
         * Gets the Right value or an alternate value, if the projected Either is a Left.
         *
         * @param other a function which converts a Left value to an alternative Right value
         * @return the right value, if the underlying Either is a Right or else the alternative Right value provided by
         * {@code other} by applying the Left value.
         */
        public R getOrElseGet(Function<? super L, ? extends R> other) {
            Objects.requireNonNull(other, "other is null");
            return either.getOrElseGet(other);
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
        public <X extends Throwable> R getOrElseThrow(Function<? super L, X> exceptionFunction) throws X {
            Objects.requireNonNull(exceptionFunction, "exceptionFunction is null");
            return either.getOrElseThrow(exceptionFunction);
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
        public Option<RightProjection<L, R>> filter(Predicate<? super R> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return either.isLeft() || predicate.test(either.get()) ? Option.some(this) : Option.none();
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

        /**
         * Maps the right value if the projected Either is a Right.
         *
         * @param mapper A mapper which takes a right value and returns a value of type U
         * @param <U>    The new type of a Right value
         * @return A new RightProjection
         */
        @SuppressWarnings("unchecked")
        @Override
        public <U> RightProjection<L, U> map(Function<? super R, ? extends U> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            if (either.isRight()) {
                return either.map((Function<R, U>) mapper).right();
            } else {
                return (RightProjection<L, U>) this;
            }
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
        public <U> U transform(Function<? super RightProjection<L, R>, ? extends U> f) {
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
            return either.hashCode();
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
     * @deprecated will be removed from the public API
     */
    @Deprecated
    public static final class Left<L, R> extends Either<L, R> implements Serializable {

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
     * @deprecated will be removed from the public API
     */
    @Deprecated
    public static final class Right<L, R> extends Either<L, R> implements Serializable {

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
