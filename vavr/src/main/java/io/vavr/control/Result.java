/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jspecify.annotations.NonNull;

/**
 * Represents the result of a computation that may either succeed with a value of type {@code T}
 * or fail with an error of type {@code E}.
 *
 * <p>{@code Result} is <em>value-biased</em>: monadic operations ({@link #map}, {@link #flatMap})
 * operate on the success value and pass errors through unchanged. This makes it natural to chain
 * operations on the happy path while propagating errors automatically.
 *
 * <p>{@code Result} differs from {@link Either} in intent: while {@code Either<L, R>} is a
 * general-purpose disjoint union, {@code Result<T, E>} explicitly models success ({@link Value})
 * and failure ({@link Error}) and provides an API shaped around that semantic. The error type
 * {@code E} is unconstrained — it can be a {@code String}, an enum, a domain error class, or
 * a {@code Throwable}.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * Result<Integer, String> parsed = parse("42");
 * int doubled = parsed
 *     .map(n -> n * 2)
 *     .getOrElse(0);
 *
 * // Constructing:
 * Result<Integer, String> ok  = Result.value(42);
 * Result<Integer, String> err = Result.error("not a number");
 * }</pre>
 *
 * @param <T> the type of the success value
 * @param <E> the type of the error
 *
 * @author Grzegorz Piwowarek
 */
public interface Result<T, E> extends io.vavr.Value<T>, Serializable {

    // -- Static factories

    /**
     * Creates a successful {@code Result} wrapping the given value.
     *
     * @param value the success value (may be {@code null})
     * @param <T>   type of the success value
     * @param <E>   type of the error
     * @return a new {@link Value}
     */
    static <T, E> Result<T, E> value(T value) {
        return new Result.Value<>(value);
    }

    /**
     * Creates a failed {@code Result} wrapping the given error.
     *
     * @param error the error (must not be {@code null})
     * @param <T>   type of the success value
     * @param <E>   type of the error
     * @return a new {@link Error}
     * @throws NullPointerException if {@code error} is null
     */
    static <T, E> Result<T, E> error(@NonNull E error) {
        return new Result.Error<>(Objects.requireNonNull(error, "error is null"));
    }

    /**
     * Narrows a widened {@code Result<? extends T, ? extends E>} to {@code Result<T, E>}
     * by performing a type-safe cast.
     *
     * @param result a {@code Result}
     * @param <T>    success component type
     * @param <E>    error component type
     * @return the given {@code result} as narrowed type {@code Result<T, E>}
     */
    @SuppressWarnings("unchecked")
    static <T, E> Result<T, E> narrow(Result<? extends T, ? extends E> result) {
        return (Result<T, E>) result;
    }

    /**
     * Reduces an {@code Iterable} of {@code Result}s into a single {@code Result} holding
     * a {@link Seq} of success values. Short-circuits on the first {@link Error} encountered.
     *
     * <pre>{@code
     * // = Value([1, 2, 3])
     * Result.sequence(List.of(Result.value(1), Result.value(2), Result.value(3)));
     *
     * // = Error("bad")
     * Result.sequence(List.of(Result.value(1), Result.error("bad"), Result.value(3)));
     * }</pre>
     *
     * @param results an {@code Iterable} of {@code Result}s
     * @param <T>     type of the success values
     * @param <E>     type of the error
     * @return a {@code Value<Seq<T>, E>} if all results are values, otherwise the first {@link Error}
     * @throws NullPointerException if {@code results} is null
     */
    @SuppressWarnings("unchecked")
    static <T, E> Result<Seq<T>, E> sequence(@NonNull Iterable<? extends Result<? extends T, ? extends E>> results) {
        Objects.requireNonNull(results, "results is null");
        Vector<T> values = Vector.empty();
        for (Result<? extends T, ? extends E> result : results) {
            if (result.isError()) {
                return Result.error(((Result.Error<?, E>) result).getError());
            }
            values = values.append(result.get());
        }
        return Result.value(values);
    }

    /**
     * Maps each element of {@code items} to a {@code Result} and then reduces using
     * {@link #sequence}. Short-circuits on the first {@link Error}.
     *
     * @param items  elements to map
     * @param mapper function mapping each element to a {@code Result}
     * @param <S>    source element type
     * @param <T>    type of the success values
     * @param <E>    type of the error
     * @return a {@code Value<Seq<T>, E>} or the first {@link Error}
     * @throws NullPointerException if {@code items} or {@code mapper} is null
     */
    static <S, T, E> Result<Seq<T>, E> traverse(
            @NonNull Iterable<? extends S> items,
            @NonNull Function<? super S, ? extends Result<? extends T, ? extends E>> mapper) {
        Objects.requireNonNull(items, "items is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return sequence(Vector.ofAll(items).map(mapper));
    }

    // -- Query

    /**
     * Returns {@code true} if this is a {@link Value} (success), {@code false} otherwise.
     *
     * @return whether this result is a success
     */
    boolean isValue();

    /**
     * Returns {@code true} if this is an {@link Error} (failure), {@code false} otherwise.
     *
     * @return whether this result is an error
     */
    boolean isError();

    /**
     * Returns {@code true} if this is an {@link Error}. Satisfies {@link io.vavr.Value}'s contract:
     * the "empty" state of a {@code Result} is the error case.
     *
     * @return {@code true} if this is an {@link Error}, {@code false} if it is a {@link Value}
     */
    @Override
    default boolean isEmpty() {
        return isError();
    }

    // -- Access

    /**
     * Returns the success value.
     *
     * @return the success value
     * @throws NoSuchElementException if this is an {@link Error}
     */
    @Override
    T get();

    /**
     * Returns the error value.
     *
     * @return the error value
     * @throws NoSuchElementException if this is a {@link Value}
     */
    E getError();

    // -- Transforms (value-biased)

    /**
     * Maps the success value using {@code mapper}. If this is an {@link Error}, returns
     * {@code this} unchanged (type-erased).
     *
     * @param mapper a function to apply to the success value
     * @param <U>    the new success type
     * @return a new {@code Result<U, E>}
     * @throws NullPointerException if {@code mapper} is null
     */
    @Override
    <U> Result<U, E> map(@NonNull Function<? super T, ? extends U> mapper);

    /**
     * FlatMaps the success value using {@code mapper}. If this is an {@link Error}, returns
     * {@code this} unchanged.
     *
     * @param mapper a function returning a new {@code Result}
     * @param <U>    the new success type
     * @return the mapped {@code Result}
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    default <U> Result<U, E> flatMap(@NonNull Function<? super T, ? extends Result<? extends U, ? extends E>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isError()) {
            return (Result<U, E>) this;
        }
        return Result.narrow(mapper.apply(get()));
    }

    /**
     * Maps the error value using {@code mapper}. If this is a {@link Value}, returns
     * {@code this} unchanged.
     *
     * @param mapper a function to apply to the error value
     * @param <F>    the new error type
     * @return a new {@code Result<T, F>}
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    default <F> Result<T, F> mapError(@NonNull Function<? super E, ? extends F> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isValue()) {
            return (Result<T, F>) this;
        }
        return Result.error(mapper.apply(getError()));
    }

    /**
     * Applies {@code errorMapper} to the error value or {@code valueMapper} to the success value,
     * returning both sides mapped to a common type {@code U}.
     *
     * @param errorMapper maps the error when this is an {@link Error}
     * @param valueMapper maps the value when this is a {@link Value}
     * @param <X>         new error type
     * @param <Y>         new success type
     * @return a new {@code Result<Y, X>}
     * @throws NullPointerException if either mapper is null
     */
    default <X, Y> Result<Y, X> bimap(
            @NonNull Function<? super E, ? extends X> errorMapper,
            @NonNull Function<? super T, ? extends Y> valueMapper) {
        Objects.requireNonNull(errorMapper, "errorMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        if (isError()) {
            return Result.error(errorMapper.apply(getError()));
        }
        return Result.value(valueMapper.apply(get()));
    }

    // -- Fold / pattern match

    /**
     * Reduces this {@code Result} to a single value by applying {@code errorMapper} to the error
     * or {@code valueMapper} to the success value.
     *
     * <pre>{@code
     * String msg = result.fold(
     *     err -> "Error: " + err,
     *     val -> "Value: " + val
     * );
     * }</pre>
     *
     * @param errorMapper function applied when this is an {@link Error}
     * @param valueMapper function applied when this is a {@link Value}
     * @param <U>         result type
     * @return the folded value
     * @throws NullPointerException if either mapper is null
     */
    default <U> U fold(
            @NonNull Function<? super E, ? extends U> errorMapper,
            @NonNull Function<? super T, ? extends U> valueMapper) {
        Objects.requireNonNull(errorMapper, "errorMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return isError() ? errorMapper.apply(getError()) : valueMapper.apply(get());
    }

    // -- Fallbacks

    /**
     * Returns the success value, or {@code other} if this is an {@link Error}.
     *
     * @param other alternative value
     * @return the success value or {@code other}
     */
    @Override
    default T getOrElse(T other) {
        return isError() ? other : get();
    }

    /**
     * Returns the success value, or the result of {@code supplier} if this is an {@link Error}.
     *
     * @param supplier alternative value supplier
     * @return the success value or the supplied alternative
     * @throws NullPointerException if {@code supplier} is null
     */
    @Override
    default T getOrElse(@NonNull Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isError() ? supplier.get() : get();
    }

    /**
     * Returns the success value, or throws an exception produced by {@code exceptionMapper}
     * applied to the error.
     *
     * <pre>{@code
     * int value = result.getOrElseThrow(err -> new IllegalStateException(err.toString()));
     * }</pre>
     *
     * @param exceptionMapper maps the error to a throwable
     * @param <X>             the exception type
     * @return the success value
     * @throws X                    if this is an {@link Error}
     * @throws NullPointerException if {@code exceptionMapper} is null
     */
    default <X extends Throwable> T getOrElseThrow(@NonNull Function<? super E, ? extends X> exceptionMapper) throws X {
        Objects.requireNonNull(exceptionMapper, "exceptionMapper is null");
        if (isError()) {
            throw exceptionMapper.apply(getError());
        }
        return get();
    }

    /**
     * Returns this {@code Result} if it is a {@link Value}, otherwise returns the result of
     * calling {@code other}.
     *
     * @param other supplier of an alternative {@code Result}
     * @return this or the supplied alternative
     * @throws NullPointerException if {@code other} is null
     */
    @SuppressWarnings("unchecked")
    default Result<T, E> orElse(@NonNull Supplier<? extends Result<? extends T, ? extends E>> other) {
        Objects.requireNonNull(other, "other is null");
        return isValue() ? this : Result.narrow(other.get());
    }

    /**
     * Recovers from an error by applying {@code recovery} to the error value to produce a
     * success value.
     *
     * <pre>{@code
     * int value = Result.<Integer, String>error("bad").recover(e -> -1); // = Value(-1)
     * }</pre>
     *
     * @param recovery function mapping the error to a success value
     * @return a {@link Value} (either the original or the recovered one)
     * @throws NullPointerException if {@code recovery} is null
     */
    default Result<T, E> recover(@NonNull Function<? super E, ? extends T> recovery) {
        Objects.requireNonNull(recovery, "recovery is null");
        return isError() ? Result.value(recovery.apply(getError())) : this;
    }

    /**
     * Recovers from an error by applying {@code recovery} to the error value to produce a new
     * {@code Result}. Analogous to {@link #flatMap} but on the error branch.
     *
     * @param recovery function mapping the error to a new {@code Result}
     * @return this if a {@link Value}, otherwise the result of applying {@code recovery}
     * @throws NullPointerException if {@code recovery} is null
     */
    @SuppressWarnings("unchecked")
    default Result<T, E> recoverWith(@NonNull Function<? super E, ? extends Result<? extends T, ? extends E>> recovery) {
        Objects.requireNonNull(recovery, "recovery is null");
        return isValue() ? this : Result.narrow(recovery.apply(getError()));
    }

    // -- Side effects

    /**
     * Executes {@code action} on the success value if this is a {@link Value}, then returns
     * {@code this} unchanged.
     *
     * @param action side-effecting consumer
     * @return this {@code Result}
     * @throws NullPointerException if {@code action} is null
     */
    @Override
    Result<T, E> peek(@NonNull Consumer<? super T> action);

    /**
     * Executes {@code action} on the error value if this is an {@link Error}, then returns
     * {@code this} unchanged.
     *
     * @param action side-effecting consumer
     * @return this {@code Result}
     * @throws NullPointerException if {@code action} is null
     */
    default Result<T, E> peekError(@NonNull Consumer<? super E> action) {
        Objects.requireNonNull(action, "action is null");
        if (isError()) {
            action.accept(getError());
        }
        return this;
    }

    // -- Filter

    /**
     * If this is a {@link Value} and the success value satisfies {@code predicate}, returns
     * {@code this}. Otherwise returns {@code error(errorSupplier.get())}.
     *
     * @param predicate     predicate to test the success value
     * @param errorSupplier supplier of an error when the predicate fails
     * @return this or an {@link Error}
     * @throws NullPointerException if {@code predicate} or {@code errorSupplier} is null
     */
    default Result<T, E> filter(@NonNull Predicate<? super T> predicate, @NonNull Supplier<? extends E> errorSupplier) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(errorSupplier, "errorSupplier is null");
        if (isError() || predicate.test(get())) {
            return this;
        }
        return Result.error(errorSupplier.get());
    }

    // -- Swap

    /**
     * Swaps success and error: a {@link Value} becomes an {@link Error} and vice versa.
     *
     * @return a {@code Result<E, T>} with the sides exchanged
     */
    default Result<E, T> swap() {
        return isValue() ? Result.error(get()) : Result.value(getError());
    }

    // -- Transforms

    /**
     * Applies {@code f} to {@code this} and returns the result. Useful for applying
     * a transformation to the {@code Result} container itself rather than its contents.
     *
     * @param f   a function that takes this {@code Result} and returns a value
     * @param <U> result type of {@code f}
     * @return the result of applying {@code f} to this
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(@NonNull Function<? super Result<T, E>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    // -- Conversions

    /**
     * Converts this {@code Result<T, E>} to an {@code Either<E, T>}: a {@link Value} becomes
     * a {@link Either.Right} and an {@link Error} becomes a {@link Either.Left}.
     *
     * @return an {@link Either} representation of this {@code Result}
     */
    default Either<E, T> toEither() {
        return isValue() ? Either.right(get()) : Either.left(getError());
    }

    // -- Inner classes

    /**
     * The success variant of {@link Result}. Wraps a value of type {@code T}.
     *
     * @param <T> type of the success value
     * @param <E> type of the error
     */
    final class Value<T, E> implements Result<T, E>, Serializable {

        private static final long serialVersionUID = 1L;

        private final T value;

        private Value(T value) {
            this.value = value;
        }

        @Override
        public boolean isValue() {
            return true;
        }

        @Override
        public boolean isError() {
            return false;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public E getError() {
            throw new NoSuchElementException("getError() on Result.Value");
        }

        @Override
        public <U> Result<U, E> map(@NonNull Function<? super T, ? extends U> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return Result.value(mapper.apply(value));
        }

        @Override
        public Result<T, E> peek(@NonNull Consumer<? super T> action) {
            Objects.requireNonNull(action, "action is null");
            action.accept(value);
            return this;
        }

        @Override
        public Iterator<T> iterator() {
            return Iterator.of(value);
        }

        @Override
        public boolean isAsync() {
            return false;
        }

        @Override
        public boolean isLazy() {
            return false;
        }

        @Override
        public boolean isSingleValued() {
            return true;
        }

        @Override
        public String stringPrefix() {
            return "Value";
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + value + ")";
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Result.Value && Objects.equals(value, ((Result.Value<?, ?>) obj).value));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }
    }

    /**
     * The error variant of {@link Result}. Wraps an error of type {@code E}.
     *
     * @param <T> type of the success value
     * @param <E> type of the error
     */
    final class Error<T, E> implements Result<T, E>, Serializable {

        private static final long serialVersionUID = 1L;

        private final E error;

        private Error(E error) {
            this.error = error;
        }

        @Override
        public boolean isValue() {
            return false;
        }

        @Override
        public boolean isError() {
            return true;
        }

        @Override
        public T get() {
            throw new NoSuchElementException("get() on Result.Error");
        }

        @Override
        public E getError() {
            return error;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> Result<U, E> map(@NonNull Function<? super T, ? extends U> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return (Result<U, E>) this;
        }

        @Override
        public Result<T, E> peek(@NonNull Consumer<? super T> action) {
            Objects.requireNonNull(action, "action is null");
            return this;
        }

        @Override
        public Iterator<T> iterator() {
            return Iterator.empty();
        }

        @Override
        public boolean isAsync() {
            return false;
        }

        @Override
        public boolean isLazy() {
            return false;
        }

        @Override
        public boolean isSingleValued() {
            return true;
        }

        @Override
        public String stringPrefix() {
            return "Error";
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + error + ")";
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Result.Error && Objects.equals(error, ((Result.Error<?, ?>) obj).error));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(error);
        }
    }
}
