/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
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
import org.jspecify.annotations.NonNull;

/**
 * Represents a value of one of two possible types: {@link Left} or {@link Right}.
 * <p>
 * An {@code Either<L, R>} is typically used to model a computation that may result in either
 * a success (represented by {@code Right}) or a failure (represented by {@code Left}).
 * <p>
 * This implementation is <strong>right-biased</strong>, meaning that most operations such as
 * {@code map}, {@code flatMap}, {@code filter}, etc., are defined for the {@code Right} projection.
 * This makes {@code Either} behave like a monad over its {@code Right} type, and enables fluent
 * chaining of computations in the successful case.
 *
 * <h2>Example</h2>
 * <p>
 * Suppose we have a {@code compute()} function that returns an {@code Either<String, Integer>},
 * where {@code Right} represents a successful result and {@code Left} holds an error message.
 *
 * <pre>{@code
 * Either<String, Integer> result = compute().map(i -> i * 2);
 * }</pre>
 * <p>
 * If {@code compute()} returns {@code Right(1)}, the result will be {@code Right(2)}.<br>
 * If {@code compute()} returns {@code Left("error")}, the result will remain {@code Left("error")}.
 *
 * <h2>Projection Semantics</h2>
 * <ul>
 *   <li>If an {@code Either} is a {@code Right} and projected to {@code Left}, operations on {@code Left} are no-ops.</li>
 *   <li>If an {@code Either} is a {@code Left} and projected to {@code Right}, operations on {@code Right} are no-ops.</li>
 *   <li>Operations on the matching projection are applied as expected.</li>
 * </ul>
 *
 * @param <L> The type of the Left value.
 * @param <R> The type of the Right value.
 *
 * @author Daniel Dietrich, Grzegorz Piwowarek, Adam Kopeć
 */
public interface Either<L, R> extends Value<R>, Serializable {

    /**
     * Serialization version identifier.
     */
    long serialVersionUID = 1L;

    /**
     * Constructs a new {@link Right} instance containing the given value.
     *
     * @param right the value to store in the {@code Right}
     * @param <L>   the type of the left value
     * @param <R>   the type of the right value
     * @return a new {@code Right} instance
     */
    static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    /**
     * Constructs a new {@link Left} instance containing the given value.
     *
     * @param left the value to store in the {@code Left}
     * @param <L>  the type of the left value
     * @param <R>  the type of the right value
     * @return a new {@code Left} instance
     */
    static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    /**
     * Narrows a {@code Either<? extends L, ? extends R>} to {@code Either<L, R>} via a type-safe cast.
     * This is safe because immutable or read-only collections are covariant.
     *
     * @param either the {@code Either} to narrow
     * @param <L>    the type of the left value
     * @param <R>    the type of the right value
     * @return the same {@code either} instance cast to {@code Either<L, R>}
     */
    @SuppressWarnings("unchecked")
    static <L, R> Either<L, R> narrow(Either<? extends L, ? extends R> either) {
        return (Either<L, R>) either;
    }

    /**
     * Returns an {@code Either<L, R>} based on the given test condition.
     * <ul>
     *   <li>If {@code test} is {@code true}, the result is a {@link Either.Right} created from {@code right}.</li>
     *   <li>If {@code test} is {@code false}, the result is a {@link Either.Left} created from {@code left}.</li>
     * </ul>
     *
     * @param test  the boolean condition to evaluate
     * @param right a {@code Supplier<? extends R>} providing the right value if {@code test} is true
     * @param left  a {@code Supplier<? extends L>} providing the left value if {@code test} is false
     * @param <L>   the type of the left value
     * @param <R>   the type of the right value
     * @return an {@code Either<L, R>} containing the left or right value depending on {@code test}
     * @throws NullPointerException if any argument is null
     */
    static <L, R> Either<L, R> cond(boolean test, @NonNull Supplier<? extends R> right, @NonNull Supplier<? extends L> left) {
        Objects.requireNonNull(right, "right is null");
        Objects.requireNonNull(left, "left is null");

        return test ? right(right.get()) : left(left.get());
    }

    /**
     * Returns an {@code Either<L, R>} based on the given test condition.
     * <ul>
     *   <li>If {@code test} is {@code true}, the result is a {@link Either.Right} containing {@code right}.</li>
     *   <li>If {@code test} is {@code false}, the result is a {@link Either.Left} containing {@code left}.</li>
     * </ul>
     *
     * @param test  the boolean condition to evaluate
     * @param right the {@code R} value to return if {@code test} is true
     * @param left  the {@code L} value to return if {@code test} is false
     * @param <L>   the type of the left value
     * @param <R>   the type of the right value
     * @return an {@code Either<L, R>} containing either the left or right value depending on {@code test}
     * @throws NullPointerException if any argument is null
     */
    static <L, R> Either<L, R> cond(boolean test, @NonNull R right, @NonNull L left) {
        Objects.requireNonNull(right, "right is null");
        Objects.requireNonNull(left, "left is null");

        return cond(test, () -> right, () -> left);
    }

    /**
     * Returns the left value of this {@code Either}.
     *
     * @return the left value
     * @throws NoSuchElementException if this {@code Either} is a {@link Either.Right}
     */
    L getLeft();

    /**
     * Checks whether this {@code Either} is a {@link Either.Left}.
     *
     * @return {@code true} if this is a {@code Left}, {@code false} otherwise
     */
    boolean isLeft();

    /**
     * Checks whether this {@code Either} is a {@link Either.Right}.
     *
     * @return {@code true} if this is a {@code Right}, {@code false} otherwise
     */
    boolean isRight();

    /**
     * Returns a LeftProjection of this Either.
     *
     * @return a new LeftProjection of this
     *
     * @deprecated Either is right-biased. Use {@link #swap()} instead of projections.
     */
    @Deprecated
    default LeftProjection<L, R> left() {
        return new LeftProjection<>(this);
    }

    /**
     * Returns a RightProjection of this Either.
     *
     * @return a new RightProjection of this
     *
     * @deprecated Either is right-biased. Use {@link #swap()} instead of projections.
     */
    @Deprecated
    default RightProjection<L, R> right() {
        return new RightProjection<>(this);
    }

    /**
     * Transforms the value of this {@code Either} by applying one of the given mapping functions.
     * <ul>
     *   <li>If this is a {@link Either.Left}, {@code leftMapper} is applied to the left value.</li>
     *   <li>If this is a {@link Either.Right}, {@code rightMapper} is applied to the right value.</li>
     * </ul>
     *
     * @param leftMapper  function to transform the left value if this is a {@code Left}
     * @param rightMapper function to transform the right value if this is a {@code Right}
     * @param <X>         the type of the left value in the resulting {@code Either}
     * @param <Y>         the type of the right value in the resulting {@code Either}
     * @return a new {@code Either} instance with the transformed value
     */
    default <X, Y> Either<X, Y> bimap(@NonNull Function<? super L, ? extends X> leftMapper, @NonNull Function<? super R, ? extends Y> rightMapper) {
        Objects.requireNonNull(leftMapper, "leftMapper is null");
        Objects.requireNonNull(rightMapper, "rightMapper is null");
        if (isRight()) {
            return new Right<>(rightMapper.apply(get()));
        } else {
            return new Left<>(leftMapper.apply(getLeft()));
        }
    }

    /**
     * Reduces this {@code Either} to a single value by applying one of the given functions.
     * <ul>
     *   <li>If this is a {@link Either.Left}, {@code leftMapper} is applied to the left value.</li>
     *   <li>If this is a {@link Either.Right}, {@code rightMapper} is applied to the right value.</li>
     * </ul>
     *
     * @param leftMapper  function to transform the left value if this is a {@code Left}
     * @param rightMapper function to transform the right value if this is a {@code Right}
     * @param <U>         the type of the resulting value
     * @return a value of type {@code U} obtained by applying the appropriate function
     */
    default <U> U fold(@NonNull Function<? super L, ? extends U> leftMapper, @NonNull Function<? super R, ? extends U> rightMapper) {
        Objects.requireNonNull(leftMapper, "leftMapper is null");
        Objects.requireNonNull(rightMapper, "rightMapper is null");
        if (isRight()) {
            return rightMapper.apply(get());
        } else {
            return leftMapper.apply(getLeft());
        }
    }

    /**
     * Transforms an {@link Iterable} of {@code Either<L, R>} into a single {@code Either<Seq<L>, Seq<R>>}.
     * <p>
     * If any of the given {@code Either}s is a {@link Either.Left}, the result is a {@link Either.Left}
     * containing a non-empty {@link Seq} of all left values.
     * <p>
     * If all of the given {@code Either}s are {@link Either.Right}, the result is a {@link Either.Right}
     * containing a (possibly empty) {@link Seq} of all right values.
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
     * @param eithers an {@link Iterable} of {@code Either} instances
     * @param <L>     the common type of left values
     * @param <R>     the common type of right values
     * @return an {@code Either} containing a {@link Seq} of left values if any {@code Either} was a {@link Either.Left}, 
     *         otherwise a {@link Seq} of right values
     * @throws NullPointerException if {@code eithers} is null
     */
    @SuppressWarnings("unchecked")
    static <L, R> Either<Seq<L>, Seq<R>> sequence(@NonNull Iterable<? extends Either<? extends L, ? extends R>> eithers) {
        Objects.requireNonNull(eithers, "eithers is null");
        return Iterator.ofAll((Iterable<Either<L, R>>) eithers)
          .partition(Either::isLeft)
          .apply((leftPartition, rightPartition) -> leftPartition.hasNext()
            ? Either.left(leftPartition.map(Either::getLeft).toVector())
            : Either.right(rightPartition.map(Either::get).toVector())
          );
    }

    /**
     * Transforms an {@link Iterable} of values into a single {@code Either<Seq<L>, Seq<R>>} by applying a mapping function 
     * that returns an {@code Either} for each value.
     * <p>
     * If the mapper returns any {@link Either.Left}, the resulting {@code Either} is a {@link Either.Left}
     * containing a {@link Seq} of all left values. Otherwise, the result is a {@link Either.Right} containing 
     * a {@link Seq} of all right values.
     *
     * @param values an {@code Iterable} of values to map
     * @param mapper a function mapping each value to an {@code Either<L, R>}
     * @param <L>    the type of left values
     * @param <R>    the type of right values
     * @param <T>    the type of the input values
     * @return a single {@code Either} containing a {@link Seq} of left or right results
     * @throws NullPointerException if {@code values} or {@code mapper} is null
     */
    static <L, R, T> Either<Seq<L>, Seq<R>> traverse(@NonNull Iterable<? extends T> values, @NonNull Function<? super T, ? extends Either<? extends L, ? extends R>> mapper) {
        Objects.requireNonNull(values, "values is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return sequence(Iterator.ofAll(values).map(mapper));
    }

    /**
     * Transforms an {@link Iterable} of {@code Either<L, R>} into a single {@code Either<L, Seq<R>>}.
     * <p>
     * If any of the given {@code Either}s is a {@link Either.Left}, the result is a {@link Either.Left}
     * containing the first left value encountered in iteration order.
     * <p>
     * If all of the given {@code Either}s are {@link Either.Right}, the result is a {@link Either.Right}
     * containing a (possibly empty) {@link Seq} of all right values.
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
     * @param eithers an {@link Iterable} of {@code Either} instances
     * @param <L>     the type of left values
     * @param <R>     the type of right values
     * @return an {@code Either} containing either the first left value if present, or a {@link Seq} of all right values
     * @throws NullPointerException if {@code eithers} is null
     */
    static <L, R> Either<L, Seq<R>> sequenceRight(@NonNull Iterable<? extends Either<? extends L, ? extends R>> eithers) {
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
     * Transforms an {@link Iterable} of values into a single {@code Either<Seq<L>, Seq<R>>} by applying a mapping 
     * function that returns an {@code Either} for each element.
     * <p>
     * If the mapper returns any {@link Either.Left}, the resulting {@code Either} is a {@link Either.Left}
     * containing a {@link Seq} of all left values. Otherwise, the result is a {@link Either.Right} containing 
     * a {@link Seq} of all right values.
     *
     * @param values an {@code Iterable} of values to map
     * @param mapper a function mapping each value to an {@code Either<L, R>}
     * @param <L>    the type of left values
     * @param <R>    the type of right values
     * @param <T>    the type of input values
     * @return a single {@code Either} containing a {@link Seq} of left or right results
     * @throws NullPointerException if {@code values} or {@code mapper} is null
     */
    static <L, R, T> Either<L, Seq<R>> traverseRight(@NonNull Iterable<? extends T> values, @NonNull Function<? super T, ? extends Either<? extends L, ? extends R>> mapper) {
        Objects.requireNonNull(values, "values is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return sequenceRight(Iterator.ofAll(values).map(mapper));
    }

    /**
     * Returns the right value of this {@code Either}, or an alternative value if this is a {@link Either.Left}.
     *
     * @param other a function that converts a left value to an alternative right value
     * @return the right value if present, otherwise the alternative value produced by applying {@code other} to the left value
     */
    default R getOrElseGet(@NonNull Function<? super L, ? extends R> other) {
        Objects.requireNonNull(other, "other is null");
        if (isRight()) {
            return get();
        } else {
            return other.apply(getLeft());
        }
    }

    /**
     * Executes the given action if this projection represents a {@link Either.Left} value.
     *
     * @param action a consumer that processes the left value
     */
    default void orElseRun(@NonNull Consumer<? super L> action) {
        Objects.requireNonNull(action, "action is null");
        if (isLeft()) {
            action.accept(getLeft());
        }
    }

    /**
     * Returns the right value of this {@code Either}, or throws an exception if it is a {@link Either.Left}.
     *
     * @param <X>               the type of exception to be thrown
     * @param exceptionFunction a function that produces an exception from the left value
     * @return the right value if present
     * @throws X if this {@code Either} is a {@link Either.Left}, using the exception produced by {@code exceptionFunction}
     */
    default <X extends Throwable> R getOrElseThrow(@NonNull Function<? super L, X> exceptionFunction) throws X {
        Objects.requireNonNull(exceptionFunction, "exceptionFunction is null");
        if (isRight()) {
            return get();
        } else {
            throw exceptionFunction.apply(getLeft());
        }
    }

    /**
     * Swaps the sides of this {@code Either}, converting a {@link Either.Left} to a {@link Either.Right}
     * and vice versa.
     *
     * @return a new {@code Either} with the left and right values swapped
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
     * Applies a flat-mapping function to the right value of this right-biased {@code Either}.
     * <p>
     * If this {@code Either} is a {@link Either.Left}, it is returned unchanged. 
     * Otherwise, the {@code mapper} function is applied to the right value, and its result is returned.
     *
     * @param mapper a function that maps the right value to another {@code Either<L, U>}
     * @param <U>    the type of the right value in the resulting {@code Either}
     * @return this {@code Either} unchanged if it is a {@link Either.Left}, or the result of applying {@code mapper} if it is a {@link Either.Right}
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    default <U> Either<L, U> flatMap(@NonNull Function<? super R, ? extends Either<L, ? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isRight()) {
            return (Either<L, U>) mapper.apply(get());
        } else {
            return (Either<L, U>) this;
        }
    }

    /**
     * Transforms the right value of this {@code Either} using the given mapping function.
     * <p>
     * If this {@code Either} is a {@link Either.Left}, no operation is performed and it is returned unchanged.
     *
     * <pre>{@code
     * import static io.vavr.API.*;
     *
     * // = Right("A")
     * Right("a").map(String::toUpperCase);
     *
     * // = Left(1)
     * Left(1).map(String::toUpperCase);
     * }</pre>
     *
     * @param mapper a function to transform the right value
     * @param <U>    the type of the right value in the resulting {@code Either}
     * @return a new {@code Either} with the right value transformed, or the original left value
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    @Override
    default <U> Either<L, U> map(@NonNull Function<? super R, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isRight()) {
            return Either.right(mapper.apply(get()));
        } else {
            return (Either<L, U>) this;
        }
    }

    /**
     * Transforms the left value of this {@code Either} using the given mapping function.
     * <p>
     * If this {@code Either} is a {@link Either.Right}, no operation is performed and it is returned unchanged.
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
     * @param leftMapper a function to transform the left value
     * @param <U>        the type of the left value in the resulting {@code Either}
     * @return a new {@code Either} with the left value transformed, or the original right value
     * @throws NullPointerException if {@code leftMapper} is null
     */
    @SuppressWarnings("unchecked")
    default <U> Either<U, R> mapLeft(@NonNull Function<? super L, ? extends U> leftMapper) {
        Objects.requireNonNull(leftMapper, "leftMapper is null");
        if (isLeft()) {
            return Either.left(leftMapper.apply(getLeft()));
        } else {
            return (Either<U, R>) this;
        }
    }

    // -- Adjusted return types of Value methods

    /**
     * Returns an {@link Option} describing the right value of this right-biased {@code Either}
     * if it satisfies the given predicate.
     * <p>
     * If this {@code Either} is a {@link Either.Left} or the predicate does not match, {@link Option#none()} is returned.
     *
     * @param predicate a predicate to test the right value
     * @return an {@link Option} containing the right value if it satisfies the predicate, or {@link Option#none()} otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default Option<Either<L, R>> filter(@NonNull Predicate<? super R> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return isLeft() || predicate.test(get()) ? Option.some(this) : Option.none();
    }

    /**
     * Filters this right-biased {@code Either} using the given predicate. 
     * <p>
     * If this {@code Either} is a {@link Either.Right} and the predicate evaluates to {@code false}, 
     * the result is a {@link Either.Left} obtained by applying the {@code zero} function to the right value.
     * If the predicate evaluates to {@code true}, the {@code Either.Right} is returned unchanged.
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
     * @param predicate a predicate to test the right value
     * @param zero      a function that converts a right value to a left value if the predicate fails
     * @return an {@code Either} containing the right value if the predicate matches, or a left value otherwise
     * @throws NullPointerException if {@code predicate} or {@code zero} is null
     */
    default Either<L, R> filterOrElse(@NonNull Predicate<? super R> predicate, @NonNull Function<? super R, ? extends L> zero) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(zero, "zero is null");
        if (isLeft() || predicate.test(get())) {
            return this;
        } else {
            return Either.left(zero.apply(get()));
        }
    }

    /**
     * Returns the right value if this is a {@code Right}; otherwise throws.
     *
     * @return the right value
     * @throws NoSuchElementException if this is a {@code Left}
     */
    @Override
    R get();

    @Override
    default boolean isEmpty() {
        return isLeft();
    }

    /**
     * Returns this {@code Either} if it is a {@link Either.Right}, otherwise returns the given {@code other} Either.
     *
     * @param other an alternative {@code Either}
     * @return this {@code Either} if it is a {@code Right}, otherwise {@code other}
     */
    @SuppressWarnings("unchecked")
    default Either<L, R> orElse(@NonNull Either<? extends L, ? extends R> other) {
        Objects.requireNonNull(other, "other is null");
        return isRight() ? this : (Either<L, R>) other;
    }

    /**
     * Returns this {@code Either} if it is a {@link Either.Right}, otherwise returns the result of evaluating the given {@code supplier}.
     *
     * @param supplier a supplier of an alternative {@code Either}
     * @return this {@code Either} if it is a {@code Right}, otherwise the result of {@code supplier}
     */
    @SuppressWarnings("unchecked")
    default Either<L, R> orElse(@NonNull Supplier<? extends Either<? extends L, ? extends R>> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isRight() ? this : (Either<L, R>) supplier.get();
    }

    /**
     * Indicates that a right-biased {@code Either} computes its value synchronously.
     *
     * @return {@code false}
     */
    @Override
    default boolean isAsync() {
        return false;
    }

    /**
     * Indicates that a right-biased {@code Either} computes its value eagerly.
     *
     * @return {@code false}
     */
    @Override
    default boolean isLazy() {
        return false;
    }

    /**
     * Indicates that a right-biased {@code Either} contains exactly one value.
     *
     * @return {@code true}
     */
    @Override
    default boolean isSingleValued() {
        return true;
    }

    @Override
    default @NonNull Iterator<R> iterator() {
        if (isRight()) {
            return Iterator.of(get());
        } else {
            return Iterator.empty();
        }
    }

    @Override
    default Either<L, R> peek(@NonNull Consumer<? super R> action) {
        Objects.requireNonNull(action, "action is null");
        if (isRight()) {
            action.accept(get());
        }
        return this;
    }

    /**
     * Performs the given action on the left value if this is a {@link Either.Left}.
     * <p>
     * If this is a {@link Either.Right}, no action is performed.
     *
     * @param action a consumer that processes the left value
     * @return this {@code Either}
     */
    default Either<L, R> peekLeft(@NonNull Consumer<? super L> action) {
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

    @Override
    default Try<R> toTry() {
        return isRight() 
          ? Try.success(get()) 
          : Try.failure(new Failure(getLeft()));
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
     *
     * @deprecated Either is right-biased. Use {@link #swap()} instead of projections.
     */
    @Deprecated
    final class LeftProjection<L, R> implements Value<L> {

        private final Either<L, R> either;

        private LeftProjection(Either<L, R> either) {
            this.either = either;
        }

        /**
         * Applies a transformation to both left and right values of the projected {@code Either} and returns a new {@code LeftProjection}.
         *
         * @param leftMapper  function to transform the left value
         * @param rightMapper function to transform the right value
         * @param <L2>        the type of the left value in the resulting projection
         * @param <R2>        the type of the right value in the resulting projection
         * @return a new {@code LeftProjection} with the transformed values
         */
        public <L2, R2> LeftProjection<L2, R2> bimap(Function<? super L, ? extends L2> leftMapper, @NonNull Function<? super R, ? extends R2> rightMapper) {
            return either.<L2, R2>bimap(leftMapper, rightMapper).left();
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
         *
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

        /**
         * Returns this {@code LeftProjection} if the underlying {@code Either} is a {@link Either.Left}, otherwise returns the given {@code other} projection.
         *
         * @param other an alternative {@code LeftProjection}
         * @return this projection if the underlying {@code Either} is a {@code Left}, otherwise {@code other}
         */
        @SuppressWarnings("unchecked")
        public LeftProjection<L, R> orElse(@NonNull LeftProjection<? extends L, ? extends R> other) {
            Objects.requireNonNull(other, "other is null");
            return either.isLeft() ? this : (LeftProjection<L, R>) other;
        }

        /**
         * Returns this {@code LeftProjection} if the underlying {@code Either} is a {@link Either.Left}, otherwise returns the result of evaluating the given {@code supplier}.
         *
         * @param supplier a supplier of an alternative {@code LeftProjection}
         * @return this projection if the underlying {@code Either} is a {@code Left}, otherwise the result of {@code supplier}
         */
        @SuppressWarnings("unchecked")
        public LeftProjection<L, R> orElse(@NonNull Supplier<? extends LeftProjection<? extends L, ? extends R>> supplier) {
            Objects.requireNonNull(supplier, "supplier is null");
            return either.isLeft() ? this : (LeftProjection<L, R>) supplier.get();
        }

        /**
         * Gets the Left value or an alternate value, if the projected Either is a Right.
         *
         * @param other an alternative value
         *
         * @return the left value, if the underlying Either is a Left or else {@code other}
         *
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
         *
         * @return the left value, if the underlying Either is a Left or else the alternative Left value provided by
         * {@code other} by applying the Right value.
         */
        public L getOrElseGet(@NonNull Function<? super R, ? extends L> other) {
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
        public void orElseRun(@NonNull Consumer<? super R> action) {
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
         *
         * @return the left value, if the underlying Either is a Left or else throws the exception provided by
         * {@code exceptionFunction} by applying the Right value.
         *
         * @throws X if the projected Either is a Right
         */
        public <X extends Throwable> L getOrElseThrow(@NonNull Function<? super R, X> exceptionFunction) throws X {
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
         *
         * @return A new Option
         */
        public Option<LeftProjection<L, R>> filter(@NonNull Predicate<? super L> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return either.isRight() || predicate.test(either.getLeft()) ? Option.some(this) : Option.none();
        }

        /**
         * FlatMaps this LeftProjection.
         *
         * @param mapper A mapper
         * @param <U>    Component type of the mapped left value
         *
         * @return this as {@code LeftProjection<L, U>} if a Right is underlying, otherwise a the mapping result of the left value.
         *
         * @throws NullPointerException if {@code mapper} is null
         */
        @SuppressWarnings("unchecked")
        public <U> LeftProjection<U, R> flatMap(@NonNull Function<? super L, ? extends LeftProjection<? extends U, R>> mapper) {
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
         *
         * @return A new LeftProjection
         */
        @SuppressWarnings("unchecked")
        @Override
        public <U> LeftProjection<U, R> map(@NonNull Function<? super L, ? extends U> mapper) {
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
         *
         * @return this LeftProjection
         */
        @Override
        public LeftProjection<L, R> peek(@NonNull Consumer<? super L> action) {
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
         *
         * @return An instance of type {@code U}
         *
         * @throws NullPointerException if {@code f} is null
         */
        public <U> U transform(@NonNull Function<? super LeftProjection<L, R>, ? extends U> f) {
            Objects.requireNonNull(f, "f is null");
            return f.apply(this);
        }

        @Override
        public @NonNull Iterator<L> iterator() {
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
     *
     * @deprecated Either is right-biased. Use {@link #swap()} instead of projections.
     */
    @Deprecated
    final class RightProjection<L, R> implements Value<R> {

        private final Either<L, R> either;

        private RightProjection(Either<L, R> either) {
            this.either = either;
        }

        /**
         * Applies a transformation to both left and right values of the projected {@code Either} and returns a new {@code RightProjection}.
         *
         * @param leftMapper  function to transform the left value
         * @param rightMapper function to transform the right value
         * @param <L2>        the type of the left value in the resulting projection
         * @param <R2>        the type of the right value in the resulting projection
         * @return a new {@code RightProjection} with the transformed values
         */
        public <L2, R2> RightProjection<L2, R2> bimap(@NonNull Function<? super L, ? extends L2> leftMapper, @NonNull Function<? super R, ? extends R2> rightMapper) {
            return either.<L2, R2>bimap(leftMapper, rightMapper).right();
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
         *
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

        /**
         * Returns this {@code RightProjection} if the underlying {@code Either} is a {@link Either.Right}, otherwise returns the given {@code other} projection.
         *
         * @param other an alternative {@code RightProjection}
         * @return this projection if the underlying {@code Either} is a {@code Right}, otherwise {@code other}
         */
        @SuppressWarnings("unchecked")
        public RightProjection<L, R> orElse(@NonNull RightProjection<? extends L, ? extends R> other) {
            Objects.requireNonNull(other, "other is null");
            return either.isRight() ? this : (RightProjection<L, R>) other;
        }

        /**
         * Returns this {@code RightProjection} if the underlying {@code Either} is a {@link Either.Right}, otherwise returns the result of evaluating the given {@code supplier}.
         *
         * @param supplier a supplier of an alternative {@code RightProjection}
         * @return this projection if the underlying {@code Either} is a {@code Right}, otherwise the result of {@code supplier}
         */
        @SuppressWarnings("unchecked")
        public RightProjection<L, R> orElse(@NonNull Supplier<? extends RightProjection<? extends L, ? extends R>> supplier) {
            Objects.requireNonNull(supplier, "supplier is null");
            return either.isRight() ? this : (RightProjection<L, R>) supplier.get();
        }

        /**
         * Gets the Right value or an alternate value, if the projected Either is a Left.
         *
         * @param other an alternative value
         *
         * @return the right value, if the underlying Either is a Right or else {@code other}
         *
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
         *
         * @return the right value, if the underlying Either is a Right or else the alternative Right value provided by
         * {@code other} by applying the Left value.
         */
        public R getOrElseGet(@NonNull Function<? super L, ? extends R> other) {
            Objects.requireNonNull(other, "other is null");
            return either.getOrElseGet(other);
        }

        /**
         * Runs an action in the case this is a projection on a Left value.
         *
         * @param action an action which consumes a Left value
         */
        public void orElseRun(@NonNull Consumer<? super L> action) {
            Objects.requireNonNull(action, "action is null");
            either.orElseRun(action);
        }

        /**
         * Gets the Right value or throws, if the projected Either is a Left.
         *
         * @param <X>               a throwable type
         * @param exceptionFunction a function which creates an exception based on a Left value
         *
         * @return the right value, if the underlying Either is a Right or else throws the exception provided by
         * {@code exceptionFunction} by applying the Left value.
         *
         * @throws X if the projected Either is a Left
         */
        public <X extends Throwable> R getOrElseThrow(@NonNull Function<? super L, X> exceptionFunction) throws X {
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
         *
         * @return A new Option
         */
        public Option<RightProjection<L, R>> filter(@NonNull Predicate<? super R> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return either.isLeft() || predicate.test(either.get()) ? Option.some(this) : Option.none();
        }

        /**
         * FlatMaps this RightProjection.
         *
         * @param mapper A mapper
         * @param <U>    Component type of the mapped right value
         *
         * @return this as {@code RightProjection<L, U>} if a Left is underlying, otherwise a the mapping result of the right value.
         *
         * @throws NullPointerException if {@code mapper} is null
         */
        @SuppressWarnings("unchecked")
        public <U> RightProjection<L, U> flatMap(@NonNull Function<? super R, ? extends RightProjection<L, ? extends U>> mapper) {
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
         *
         * @return A new RightProjection
         */
        @SuppressWarnings("unchecked")
        @Override
        public <U> RightProjection<L, U> map(@NonNull Function<? super R, ? extends U> mapper) {
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
         *
         * @return this {@code Either} instance
         */
        @Override
        public RightProjection<L, R> peek(@NonNull Consumer<? super R> action) {
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
         *
         * @return An instance of type {@code U}
         *
         * @throws NullPointerException if {@code f} is null
         */
        public <U> U transform(@NonNull Function<? super RightProjection<L, R>, ? extends U> f) {
            Objects.requireNonNull(f, "f is null");
            return f.apply(this);
        }

        @Override
        public @NonNull Iterator<R> iterator() {
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
     *
     * @author Daniel Dietrich
     */
    final class Left<L, R> implements Either<L, R>, Serializable {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("serial") // Conditionally serializable
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
     *
     * @author Daniel Dietrich
     */
    final class Right<L, R> implements Either<L, R>, Serializable {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("serial") // Conditionally serializable
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

    /**
     * An exception wrapper used to propagate values through exception handling mechanisms.
     * <p>
     * This class wraps a value in an exception to enable using exception-based control flow
     * when working with Either. It is not possible to use a generic type parameter for the
     * exception type due to Java type system limitations.
     */
    // it's not possible to use a generic type parameter for the exception type
    class Failure extends Exception {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("serial") // Conditionally serializable
        private final Object value;

        /**
         * Constructs a new {@code Failure} wrapping the given value.
         *
         * @param value the value to wrap
         */
        public Failure(Object value) {
            super("wrapped value representing a failure");
            this.value = value;
        }

        /**
         * Returns the wrapped value.
         *
         * @return the wrapped value
         */
        public Object getValue() {
            return value;
        }
    }
}
