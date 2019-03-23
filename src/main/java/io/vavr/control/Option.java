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

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Replacement for {@link Optional}.
 * <p>
 * Option is a <a href="http://stackoverflow.com/questions/13454347/monads-with-java-8">monadic</a> container type which
 * represents an optional value.
 * <p>
 * Most of the API is taken from {@link Optional}. A similar type can be found in <a
 * href="http://hackage.haskell.org/package/base-4.6.0.1/docs/Data-Maybe.html">Haskell</a> and <a
 * href="http://www.scala-lang.org/api/current/#scala.Option">Scala</a>.
 *
 * @param <T> The type of the optional value.
 * @author Daniel Dietrich
 */
public abstract class Option<T> implements io.vavr.Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    // sealed
    private Option() {}

    /**
     * Creates a new {@code Option} of a given value.
     *
     * @param value A value
     * @param <T>   type of the value
     * @return A new {@code Option} containing the given {@code value} if value is not {@code null}, otherwise the empty {@code Option}.
     */
    public static <T> Option<T> of(T value) {
        return (value == null) ? none() : some(value);
    }

    /**
     * Creates a new {@code Some} of a given value.
     * <p>
     * The only difference to {@link Option#of(Object)} is, when called with argument {@code null}.
     * <pre>
     * <code>
     * Option.of(null);   // = None
     * Option.some(null); // = Some(null)
     * </code>
     * </pre>
     *
     * @param value A value
     * @param <T>   type of the value
     * @return {@code Some(value)}
     */
    public static <T> Option<T> some(T value) {
        return new Some<>(value);
    }

    /**
     * Returns the single instance of {@code None}
     *
     * @param <T> component type
     * @return the single instance of {@code None}
     */
    @SuppressWarnings("unchecked")
    public static <T> Option<T> none() {
        return (None<T>) None.INSTANCE;
    }

    /**
     * When the given {@code condition} is true, {@code Some(supplier.get())} is returned.
     * Otherwise, {@code None} is returned.
     * <p>
     * Same as {@code Option.unless(!condition, supplier)}.
     *
     * @param <T>       type of the optional value
     * @param condition A boolean value
     * @param supplier  An optional value supplier, may supply {@code null}
     * @return a new {@code Option}
     * @throws NullPointerException if the given {@code supplier} is null
     * @see #unless(boolean, Supplier)
     */
    public static <T> Option<T> when(boolean condition, Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return condition ? some(supplier.get()) : none();
    }

    /**
     * Unless the given {@code condition} is true, {@code Some(supplier.get())} is returned.
     * Otherwise, {@code None} is returned.
     * <p>
     * Same as {@code Option.when(!condition, supplier)}.
     *
     * @param <T>       type of the optional value
     * @param condition A boolean value
     * @param supplier  An optional value supplier, may supply {@code null}
     * @return a new {@code Option}
     * @throws NullPointerException if the given {@code supplier} is null
     * @see #when(boolean, Supplier)
     */
    public static <T> Option<T> unless(boolean condition, Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return condition ? none() : some(supplier.get());
    }

    /**
     * Wraps a Java Optional to a new Option
     *
     * @param optional a given optional to wrap in {@code Option}
     * @param <T>      type of the value
     * @return {@code Some(optional.get())} if value is Java {@code Optional} is present, {@code None} otherwise
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> Option<T> ofOptional(Optional<? extends T> optional) {
        Objects.requireNonNull(optional, "optional is null");
        return optional.<Option<T>>map(Option::of).orElseGet(Option::none);
    }

    /**
     * Collects the underlying value (if present) using the provided {@code collector}.
     * <p>
     * Shortcut for {@code .stream().collect(collector)}.
     *
     * @param <A>       the mutable accumulation type of the reduction operation
     * @param <R>       the result type of the reduction operation
     * @param collector Collector performing reduction
     * @return the reduction result of type {@code R}
     * @throws NullPointerException if the given {@code collector} is null
     */
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return stream().collect(collector);
    }

    /**
     * Returns {@code Some(value)} if this is a {@code Some} and the value satisfies the given predicate.
     * Otherwise {@code None} is returned.
     *
     * @param predicate A predicate which is used to test an optional value
     * @return {@code Some(value)} or {@code None} as specified
     */
    public Option<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return isEmpty() || predicate.test(get()) ? this : none();
    }

    /**
     * Maps the value to a new {@code Option} if this is a {@code Some}, otherwise returns {@code None}.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the resulting Option
     * @return a new {@code Option}
     */
    @SuppressWarnings("unchecked")
    public <U> Option<U> flatMap(Function<? super T, Option<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return isDefined() ? (Option<U>) mapper.apply(get()) : none();
    }
    
    /**
     * Folds either the {@code None} or the {@code Some} side of the Option value.
     *
     * @param ifEmpty maps the left value if this is a None
     * @param ifDefined maps the value if this is a Some
     * @param <U>         type of the folded value
     * @return A value of type U
     * @throws NullPointerException if one of the given {@code ifEmpty} or {@code ifDefined} is null
     */
    public <U> U fold(Supplier<? extends U> ifEmpty, Function<? super T, ? extends U> ifDefined) {
        Objects.requireNonNull(ifEmpty, "ifEmpty is null");
        Objects.requireNonNull(ifDefined, "ifDefined is null");
        return isDefined() ? ifDefined.apply(get()) : ifEmpty.get();
    }

    /**
     * Gets the value if this is a {@code Some} or throws if this is a {@code None}.
     * <p>
     * <strong>Warning:</strong> Please note that this operation is considered unsafe.
     * Alternatives are {@link #getOrElse(Object)}, {@link #getOrElseGet(Supplier)} or {@link #getOrElseThrow(Supplier)}.
     * Other alternatives are {@link #onDefined(Consumer)}, {@link #forEach(Consumer)} or iteration using a for-loop.
     *
     * @return the value
     * @throws NoSuchElementException if this is a {@code None}.
     */
    public abstract T get() throws NoSuchElementException;

    /**
     * Returns the value if this is a {@code Some} or the {@code other} value if this is a {@code None}.
     * <p>
     * Please note, that the other value is eagerly evaluated.
     *
     * @param other An alternative value
     * @return This value, if this Option is defined or the {@code other} value, if this Option is empty.
     */
    public T getOrElse(T other) {
        return isEmpty() ? other : get();
    }

    /**
     * Returns the value if this is a {@code Some}, otherwise the {@code other} value is returned,
     * if this is a {@code None}.
     * <p>
     * Please note, that the other value is lazily evaluated.
     *
     * @param supplier An alternative value supplier
     * @return This value, if this Option is defined or the {@code other} value, if this Option is empty.
     */
    public T getOrElseGet(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isEmpty() ? supplier.get() : get();
    }

    /**
     * Returns the value if this is a {@code Some}, otherwise throws an exception.
     *
     * @param exceptionProvider An exception provider
     * @param <X>               A throwable
     * @return This value, if this {@code Option} is defined, otherwise throws X
     * @throws X if this {@code Option} is empty
     * @throws NullPointerException if the given {@code exceptionProvider} is null
     */
    public <X extends Throwable> T getOrElseThrow(Supplier<X> exceptionProvider) throws X {
        Objects.requireNonNull(exceptionProvider, "exceptionProvider is null");
        if (isDefined()) {
            return get();
        } else {
            throw exceptionProvider.get();
        }
    }

    /**
     * Returns true, if this is {@code Some}, otherwise false, if this is {@code None}.
     * <p>
     * Please note that it is possible to create {@code new Some(null)}, which is defined.
     *
     * @return true, if this {@code Option} has a defined value, false otherwise
     */
    public abstract boolean isDefined();

    /**
     * Returns true, if this is {@code None}, otherwise false, if this is {@code Some}.
     *
     * @return true, if this {@code Option} is empty, false otherwise
     */
    public abstract boolean isEmpty();

    @Override
    public Iterator<T> iterator() {
        return isDefined() ? Iterator.of(get()) : Iterator.empty();
    }

    /**
     * Maps the value and wraps it in a new {@code Some} if this is a {@code Some}, returns {@code None}.
     *
     * @param mapper A value mapper
     * @param <U>    The new value type
     * @return a new {@code Some} containing the mapped value if this Option is defined, otherwise {@code None}, if this is empty.
     */
    public <U> Option<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return isDefined() ? some(mapper.apply(get())) : none();
    }

    /**
     * Runs a Java Runnable passed as parameter if this {@code Option} is empty.
     *
     * @param action a given Runnable to be run
     * @return this {@code Option}
     * @throws NullPointerException if the given {@code action} is null
     */
    public Option<T> onEmpty(Runnable action) {
        Objects.requireNonNull(action, "action is null");
        if (isEmpty()) {
            action.run();
        }
        return this;
    }

    /**
     * Applies an action to this value if this is defined, otherwise nothing happens.
     *
     * @param action An action which can be applied to an optional value
     * @return this {@code Option}
     * @throws NullPointerException if the given {@code action} is null
     */
    public Option<T> onDefined(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (isDefined()) {
            action.accept(get());
        }
        return this;
    }

    /**
     * Returns this {@code Option} if it is nonempty, otherwise return the result of evaluating supplier.
     *
     * @param supplier An alternative {@code Option} supplier
     * @return this {@code Option} if it is nonempty, otherwise return the result of evaluating supplier.
     */
    @SuppressWarnings("unchecked")
    public Option<T> orElse(Supplier<? extends Option<? extends T>> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isEmpty() ? (Option<T>) supplier.get() : this;
    }
    
    /**
     * Converts this {@code Option} to a {@link Stream}.
     *
     * @return {@code Stream.of(get()} if this is a {@code Some}, otherwise {@code Stream.empty()}
     */
    public Stream<T> stream() {
        return isDefined() ? Stream.of(get()) : Stream.empty();
    }

    /**
     * Converts this {@code Option} to an {@link Either}.
     *
     * @param <U> the left type of the {@code Either}
     * @param leftSupplier a left value supplier
     * @return {@code Either.right(get()} if this is a defined {@code Option}, otherwise {@code Either.left(leftSupplier.get())}
     * @throws NullPointerException if the given {@code leftSupplier} is null
     */
    public <U> Either<U, T> toEither(Supplier<? extends U> leftSupplier) {
        Objects.requireNonNull(leftSupplier, "leftSupplier is null");
        return isDefined() ? Either.right(get()) : Either.left(leftSupplier.get());
    }

    /**
     * Converts this {@code Option} to an {@link Optional}.
     *
     * @return {@code Optional.ofNullable(get())} if this is defined, otherwise {@code Optional.empty()}
     */
    public Optional<T> toOptional() {
        return isDefined() ? Optional.ofNullable(get()) : Optional.empty();
    }

    /**
     * Converts this {@code Option} to a {@link Try}.
     *
     * @param ifEmpty supplies a {@code Throwable} if this {@code Option} is empty
     * @return {@code Try.success(get()} if this is a defined {@code Option}, otherwise {@code Try.failure(ifEmpty.get())}
     * @throws NullPointerException if the given {@code ifEmpty} is null
     */
    public Try<T> toTry(Supplier<? extends Throwable> ifEmpty) {
        Objects.requireNonNull(ifEmpty, "ifEmpty is null");
        return isDefined() ? Try.success(get()) : Try.failure(ifEmpty.get());
    }

    /**
     * Transforms this {@code Option} by applying either {@code ifDefined} to this value or by calling {@code ifEmpty}.
     *
     * @param ifEmpty supplies an {@code Option} if this {@code Option} is empty
     * @param ifDefined maps the value if this {@code Option} is defined
     * @param <U>    type of the transformed value
     * @return A new {@code Option} instance
     * @throws NullPointerException if one of the given {@code ifDefined} or {@code ifEmpty} is null
     */
    @SuppressWarnings("unchecked")
    public <U> Option<U> transform(Supplier<? extends Option<? extends U>> ifEmpty, Function<? super T, ? extends Option<? extends U>> ifDefined) {
        Objects.requireNonNull(ifEmpty, "ifEmpty is null");
        Objects.requireNonNull(ifDefined, "ifDefined is null");
        return isDefined()
               ? (Option<U>) ifDefined.apply(get())
               : (Option<U>) ifEmpty.get();
    }

    /**
     * Checks if this {@code Option} is equal to the given object {@code o}.
     *
     * @param that an object, may be null
     * @return true, if {@code this} and {@code that} both are a defined {@code Option} and the underlying values are
     *         equal or if {@code this} and {@code that} both are an empty {@code Option}. Otherwise it returns false.
     */
    @Override
    public abstract boolean equals(Object that);

    /**
     * Computes the hash of this {@code Option}.
     *
     * @return {@code 31 + Objects.hashCode(get())} if this is a {@code Some}, otherwise {@code 1}
     */
    @Override
    public abstract int hashCode();

    /**
     * Returns a string representation of this {@code Option}.
     *
     * @return {@code "Some(" + get() + ")"} if this {@code Option} is defined, otherwise {@code "None"}
     */
    @Override
    public abstract String toString();

    private static final class Some<T> extends Option<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final T value;
        
        private Some(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public boolean isDefined() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Some && Objects.equals(value, ((Some<?>) obj).value));
        }

        @Override
        public int hashCode() {
            return 31 + Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "Some(" + value + ")";
        }
    }

    private static final class None<T> extends Option<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private static final None<?> INSTANCE = new None<>();

        private None() {
        }

        @Override
        public T get() {
            throw new NoSuchElementException("get() on None");
        }

        @Override
        public boolean isDefined() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean equals(Object o) {
            return o == this;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public String toString() {
            return "None";
        }

        // -- Serializable implementation

        /**
         * Instance control for object serialization.
         *
         * @return The singleton instance of None.
         * @see Serializable
         */
        private Object readResolve() {
            return INSTANCE;
        }
    }
}
