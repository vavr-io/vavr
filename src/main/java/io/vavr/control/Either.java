/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2018 Vavr, http://vavr.io
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

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Either represents a value of two possible types. An Either is either a {@link Left} or a
 * {@link Right}.
 *
 * @param <L> The type of the Left value of an Either.
 * @param <R> The type of the Right value of an Either.
 * @author Daniel Dietrich
 */
public abstract class Either<L, R> implements Iterable<R>, Serializable {

    private static final long serialVersionUID = 1L;

    // sealed
    private Either() {}

    /**
     * Constructs a {@code Left}  Either.
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
     * Constructs a {@code Right} Either.
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
     * Conditionally returns either a {@code Left} or a {@code Right}, depending of the given {@code test} value.
     *
     * @param test a boolean condition
     * @param leftSupplier left value supplier
     * @param rightSupplier right value supplier
     * @param <L> type of a left value
     * @param <R> type of a right value
     * @return {@code Either.right(rightSupplier.get())} if {@code test} is true, otherwise {@code Either.left(leftSupplier.get())}
     */
    public static <L, R> Either<L, R> cond(boolean test, Supplier<? extends L> leftSupplier, Supplier<? extends R> rightSupplier) {
        Objects.requireNonNull(leftSupplier, "leftSupplier is null");
        Objects.requireNonNull(rightSupplier, "rightSupplier is null");
        return test ? Either.right(rightSupplier.get()) : Either.left(leftSupplier.get());
    }

    /**
     * Collects the underlying value (if present) using the provided {@code collector}.
     * <p>
     * Shortcut for {@code .stream().collect(collector)}.
     *
     * @param <A>       the mutable accumulation type of the reduction operation
     * @param <T>       the result type of the reduction operation
     * @param collector Collector performing reduction
     * @return the reduction result of type {@code T}
     * @throws NullPointerException if the given {@code collector} is null
     */
    public <T, A> T collect(Collector<? super R, A, T> collector) {
        return stream().collect(collector);
    }

    /**
     * Filters this right-biased {@code Either} by testing a predicate.
     * If the {@code Either} is a {@code Right} and the predicate doesn't match, the
     * {@code Either} will be turned into a {@code Left} with contents computed by applying
     * the filterVal function to the {@code Either} value.
     *
     * @param predicate A predicate
     * @param zero a function that transforms the right value to a left value, if it does not make it through the given filter {@code predicate}
     * @return an {@code Either} instance
     * @throws NullPointerException if {@code predicate} is null
     */
    public Either<L, R> filterOrElse(Predicate<? super R> predicate, Function<? super R, ? extends L> zero) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(zero, "zero is null");
        if (isLeft() || predicate.test(get())) {
            return this;
        } else {
            return Either.left(zero.apply(get()));
        }
    }

    /**
     * FlatMaps this right-biased Either.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped right value
     * @return this as {@code Either<L, U>} if this is a Left, otherwise the right mapping result
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    public <U> Either<L, U> flatMap(Function<? super R, Either<L, ? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isRight()) {
            return (Either<L, U>) mapper.apply(get());
        } else {
            return (Either<L, U>) this;
        }
    }

    /**
     * Folds either the left or the right side of this disjunction.
     *
     * @param ifLeft  maps the left value if this is a Left
     * @param ifRight maps the right value if this is a Right
     * @param <U>         type of the folded value
     * @return A value of type U
     */
    public <U> U fold(Function<? super L, ? extends U> ifLeft, Function<? super R, ? extends U> ifRight) {
        Objects.requireNonNull(ifLeft, "ifLeft is null");
        Objects.requireNonNull(ifRight, "ifRight is null");
        if (isRight()) {
            return ifRight.apply(get());
        } else {
            return ifLeft.apply(getLeft());
        }
    }
    
    /**
     * Gets the right value if this is a {@code Right} or throws if this is a {@code Left}.
     *
     * @return the right value
     * @throws NoSuchElementException if this is a {@code Left}.
     * @deprecated TODO: description
     */
    @Deprecated
    public abstract R get();

    /**
     * Gets the left value if this is a {@code Left} or throws if this is a {@code Right}.
     *
     * @return The left value.
     * @throws NoSuchElementException if this is a {@code Right}.
     * @deprecated TODO: description
     */
    @Deprecated
    public abstract L getLeft();


    /**
     * Gets the Right value or an alternate value, if the Either is a Left.
     *
     * @param other an alternative right value
     * @return the right value, if the underlying Either is a Right or else the alternative Right value {@code other}
     */
    public R getOrElse(R other) {
        return isRight() ? get() : other;
    }

    /**
     * Gets the Right value or an alternate value, if the Either is a Left.
     *
     * @param other a function which converts a Left value to an alternative Right value
     * @return the right value, if the underlying Either is a Right or else the alternative Right value provided by
     * {@code other} by applying the Left value.
     * @throws NullPointerException if {@code other} is null
     */
    public R getOrElseGet(Function<? super L, ? extends R> other) {
        Objects.requireNonNull(other, "other is null");
        return isRight() ? get() : other.apply(getLeft());
    }
    
    /**
     * Gets the Right value or throws, if this Either is a Left.
     *
     * @param <X>               a throwable type
     * @param exceptionProvider a function which creates an exception based on a Left value
     * @return the right value, if the underlying Either is a Right or else throws the exception provided by
     * {@code exceptionProvider} by applying the Left value.
     * @throws X if this Either is a Left
     */
    public <X extends Throwable> R getOrElseThrow(Function<? super L, X> exceptionProvider) throws X {
        Objects.requireNonNull(exceptionProvider, "exceptionProvider is null");
        if (isRight()) {
            return get();
        } else {
            throw exceptionProvider.apply(getLeft());
        }
    }
    
    /**
     * Returns whether this Either is a Left.
     *
     * @return true, if this is a Left, false otherwise
     */
    public abstract boolean isLeft();

    /**
     * Returns whether this Either is a Right.
     *
     * @return true, if this is a Right, false otherwise
     */
    public abstract boolean isRight();

    @Override
    public Iterator<R> iterator() {
        return isRight() ? Collections.singleton(get()).iterator() : Collections.emptyIterator();
    }

    /**
     * Maps the value of this Either if it is a Right, performs no operation if this is a Left.
     *
     * <pre><code>
     * // = Right("A")
     * Either.right("a").map(String::toUpperCase);
     *
     * // = Left(1)
     * Either.left(1).map(String::toUpperCase);
     * </code></pre>
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped right value
     * @return a mapped {@code Monad}
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    public <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
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
     * // = Left(2)
     * Either.left(1).mapLeft(i -> i + 1);
     *
     * // = Right("a")
     * Either.right("a").mapLeft(i -> i + 1);
     * }</pre>
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped right value
     * @return a mapped {@code Monad}
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    public <U> Either<U, R> mapLeft(Function<? super L, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isLeft()) {
            return Either.left(mapper.apply(getLeft()));
        } else {
            return (Either<U, R>) this;
        }
    }

    /**
     * Applies an action to this left value if this is a {@code Left}, otherwise nothing happens.
     *
     * @param action An action that takes a left value and performs a side-effect
     * @return this {@code Either}
     * @throws NullPointerException if the given {@code action} is null
     */
    public Either<L, R> onLeft(Consumer<? super L> action) {
        Objects.requireNonNull(action, "action is null");
        if (isLeft()) {
            action.accept(getLeft());
        }
        return this;
    }

    /**
     * Applies an action to this right value if this is a {@code Right}, otherwise nothing happens.
     *
     * @param action An action that takes a right value and performs a side-effect
     * @return this {@code Either}
     * @throws NullPointerException if the given {@code action} is null
     */
    public Either<L, R> onRight(Consumer<? super R> action) {
        Objects.requireNonNull(action, "action is null");
        if (isRight()) {
            action.accept(get());
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public Either<L, R> orElse(Supplier<Either<? extends L, ? extends R>> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isRight() ? this : (Either<L, R>) supplier.get();
    }
    
    /**
     * Converts this {@code Either} to a {@link Stream}.
     *
     * @return {@code Stream.of(get()} if this is a {@code Right}, otherwise {@code Stream.empty()}
     */
    public Stream<R> stream() {
        return isRight() ? Stream.of(get()) : Stream.empty();
    }

    /**
     * Converts a {@code Left} to a {@code Right} vice versa by wrapping the value in a new type.
     *
     * @return a new {@code Either}
     */
    public Either<R, L> swap() {
        return isRight() ? left(get()): right(getLeft());
    }

    /**
     * Converts this {@code Either} to an {@link Option}.
     *
     * @return {@code Option.some(get()} if this is a {@code Right}, otherwise {@code Option.none()}
     */
    public Option<R> toOption() {
        return isRight() ? Option.some(get()) : Option.none();
    }

    /**
     * Converts this {@code Either} to an {@link Optional}.
     *
     * @return {@code Optional.ofNullable(get())} if this is a {@code Right}, otherwise {@code Optional.empty()}
     */
    public Optional<R> toOptional() {
        return isRight() ? Optional.ofNullable(get()) : Optional.empty();
    }

    /**
     * Converts this {@code Either} to a {@link Try}.
     *
     * @param leftMapper a function that maps a left value to a {@link Throwable}
     * @return {@code Try.success(get()} if this is a {@code Right}, otherwise {@code Try.failure(leftMapper.apply(getLeft())}
     * @throws NullPointerException if the given {@code leftMapper} is null
     */
    public Try<R> toTry(Function<? super L, ? extends Throwable> leftMapper) {
        Objects.requireNonNull(leftMapper, "leftMapper is null");
        return isRight() ? Try.success(get()) : Try.failure(leftMapper.apply(getLeft()));
    }

    /**
     * Transforms this {@code Either} by applying either {@code ifRight} to this right value or {@code ifLeft} to this left value.
     *
     * @param ifLeft  maps the left value if this is a {@code Left}
     * @param ifRight maps the right value if this is a {@code Right}
     * @param <U>     type of the transformed right value
     * @return A new {@code Either} instance
     * @throws NullPointerException if one of the given {@code ifRight} or {@code ifLeft} is null
     */
    @SuppressWarnings("unchecked")
    public <U> Either<L, U> transform(Function<? super L, ? extends Either<L, ? extends U>> ifLeft, Function<? super R, ? extends Either<L, ? extends U>> ifRight) {
        Objects.requireNonNull(ifLeft, "ifLeft is null");
        Objects.requireNonNull(ifRight, "ifRight is null");
        return isRight()
               ? (Either<L, U>) ifRight.apply(get())
               : (Either<L, U>) ifLeft.apply(getLeft());
    }

    /**
     * Checks if this {@code Either} is equal to the given object {@code o}.
     *
     * @param that an object, may be null
     * @return true, if {@code this} and {@code that} both are a {@code Right} and the underlying values are equal
     *         or if {@code this} and {@code that} both are a {@code Left} and the underlying values are equal.
     *         Otherwise it returns false.
     */
    @Override
    public abstract boolean equals(Object that);

    /**
     * Computes the hash of this {@code Either}.
     *
     * @return {@code 31 + Objects.hashCode(get())} if this is a {@code Right}, otherwise {@code 31 + Objects.hashCode(getLeft())}
     */
    @Override
    public abstract int hashCode();

    /**
     * Returns a string representation of this {@code Either}.
     *
     * @return {@code "Right(" + get() + ")"} if this is a {@code Right}, otherwise {@code "Left(" + getLeft() + ")"}
     */
    @Override
    public abstract String toString();
    
    
    private static final class Left<L, R> extends Either<L, R> implements Serializable {

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
            return 31 + Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "Left(" + value + ")";
        }
    }
    
    private static final class Right<L, R> extends Either<L, R> implements Serializable {

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
            return 31 + Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "Right(" + value + ")";
        }
    }
}
