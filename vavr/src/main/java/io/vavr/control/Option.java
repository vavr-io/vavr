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

import io.vavr.CheckedFunction1;
import io.vavr.PartialFunction;
import io.vavr.Value;
import io.vavr.collection.Iterator;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jspecify.annotations.NonNull;

/**
 * A replacement for {@link java.util.Optional}.
 * <p>
 * {@code Option} is a monadic container type representing the presence or absence of a value.
 * An instance is either a {@link Some} holding a value or the singleton {@link None}.
 * <p>
 * The design is similar to {@link java.util.Optional} and related types in
 * <a href="http://hackage.haskell.org/package/base-4.6.0.1/docs/Data-Maybe.html">Haskell</a> and
 * <a href="http://www.scala-lang.org/api/current/#scala.Option">Scala</a>.
 *
 * @param <T> the type of the optional value
 */
public interface Option<T> extends Value<T>, Serializable {

    long serialVersionUID = 1L;

    /**
     * Creates an {@code Option} from the given value.
     *
     * @param value the value to wrap
     * @param <T>   the value type
     * @return {@code Some(value)} if the value is non-null, otherwise {@code None}
     */
    static <T> Option<T> of(T value) {
        return (value == null) ? none() : some(value);
    }

    /**
     * Reduces multiple {@code Option} values into a single {@code Option} by transforming
     * an {@code Iterable<Option<? extends T>>} into an {@code Option<Seq<T>>}.
     * <p>
     * If any element is {@link Option.None}, the result is {@code None}.
     * Otherwise, all contained values are collected into a {@link Seq} wrapped in {@code Some}.
     *
     * @param values an iterable of {@code Option} values
     * @param <T>    the element type
     * @return an {@code Option} containing a {@code Seq} of all values, or {@code None} if any value is empty
     * @throws NullPointerException if {@code values} is null
     */
    static <T> Option<Seq<T>> sequence(@NonNull Iterable<? extends Option<? extends T>> values) {
        Objects.requireNonNull(values, "values is null");
        Vector<T> vector = Vector.empty();
        for (Option<? extends T> value : values) {
            if (value.isEmpty()) {
                return Option.none();
            }
            vector = vector.append(value.get());
        }
        return Option.some(vector);
    }

    /**
     * Maps the elements of an iterable into {@code Option} values and collects the results
     * into a single {@code Option}.
     * <p>
     * Each element is transformed using {@code mapper}.
     * If any mapped value is {@link Option.None}, the result is {@code None}.
     * Otherwise, all mapped values are accumulated into a {@link Seq} wrapped in {@code Some}.
     *
     * @param values an iterable of input values
     * @param mapper a function mapping each value to an {@code Option}
     * @param <T>    the input element type
     * @param <U>    the mapped element type
     * @return an {@code Option} containing a {@code Seq} of mapped values, or {@code None} if any mapping yields {@code None}
     * @throws NullPointerException if {@code values} or {@code mapper} is null
     */
    static <T, U> Option<Seq<U>> traverse(@NonNull Iterable<? extends T> values, @NonNull Function<? super T, ? extends Option<? extends U>> mapper) {
        Objects.requireNonNull(values, "values is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return sequence(Iterator.ofAll(values).map(mapper));
    }

    /**
     * Creates a {@code Some} containing the given value.
     * <p>
     * Unlike {@link Option#of(Object)}, this method preserves {@code null}:
     * <pre>
     * Option.of(null);   // yields None
     * Option.some(null); // yields Some(null)
     * </pre>
     *
     * @param value the value to wrap, possibly {@code null}
     * @param <T>   the value type
     * @return a {@code Some} containing {@code value}
     */
    static <T> Option<T> some(T value) {
        return new Some<>(value);
    }

    /**
     * Returns the singleton {@code None} instance.
     *
     * @param <T> the option's component type
     * @return the singleton {@code None}
     */
    static <T> Option<T> none() {
        @SuppressWarnings("unchecked")
        final None<T> none = (None<T>) None.INSTANCE;
        return none;
    }

    /**
     * Narrows a widened {@code Option<? extends T>} to {@code Option<T>} via a type-safe cast.
     * <p>
     * This is safe because immutable/read-only types are covariant.
     *
     * @param option the {@code Option} to narrow
     * @param <T>    the component type of the {@code Option}
     * @return the same {@code Option} instance, cast to {@code Option<T>}
     */
    @SuppressWarnings("unchecked")
    static <T> Option<T> narrow(@NonNull Option<? extends T> option) {
        return (Option<T>) option;
    }

    /**
     * Returns {@code Some} of the value supplied by {@code supplier} if {@code condition} is true,
     * or {@code None} if {@code condition} is false.
     *
     * @param <T>       the type of the optional value
     * @param condition the condition to test
     * @param supplier  a supplier of the value, may return {@code null}
     * @return {@code Some} of the supplied value if {@code condition} is true, otherwise {@code None}
     * @throws NullPointerException if {@code supplier} is null
     */
    static <T> Option<T> when(boolean condition, @NonNull Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return condition ? some(supplier.get()) : none();
    }

    /**
     * Returns {@code Some} of the given {@code value} if {@code condition} is true, or {@code None} otherwise.
     *
     * @param <T>       the type of the optional value
     * @param condition the condition to test
     * @param value     the value to wrap, may be {@code null}
     * @return {@code Some} of {@code value} if {@code condition} is true, otherwise {@code None}
     */
    static <T> Option<T> when(boolean condition, T value) {
        return condition ? some(value) : none();
    }

    /**
     * Wraps a {@link java.util.Optional} in a new {@code Option}.
     *
     * @param optional the Java {@code Optional} to wrap
     * @param <T>      the type of the contained value
     * @return {@code Some(optional.get())} if the {@code Optional} is present, otherwise {@code None}
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static <T> Option<T> ofOptional(@NonNull Optional<? extends T> optional) {
        Objects.requireNonNull(optional, "optional is null");
        return optional.<Option<T>>map(Option::of).orElseGet(Option::none);
    }

    /**
     * Applies a {@code partialFunction} to the value of this {@code Option} if it is defined for that value,
     * and wraps the result in a new {@code Option}.
     * <p>
     * If the {@code partialFunction} is not defined for the value, {@code None} is returned.
     *
     * <pre>{@code
     * if (partialFunction.isDefinedAt(value)) {
     *     R newValue = partialFunction.apply(value);
     *     // wrapped in Some(newValue)
     * }
     * }</pre>
     *
     * @param partialFunction a function that may not be defined for all input values
     * @param <R>             the type of the mapped value
     * @return a new {@code Option} containing the mapped value if defined, otherwise {@code None}
     * @throws NullPointerException if {@code partialFunction} is null
     */
    default <R> Option<R> collect(@NonNull PartialFunction<? super T, ? extends R> partialFunction) {
        Objects.requireNonNull(partialFunction, "partialFunction is null");
        return flatMap(partialFunction.lift()::apply);
    }

    /**
     * Checks whether this {@code Option} is empty.
     *
     * @return {@code true} if this is {@code None}, {@code false} if this is {@code Some}
     */
    @Override
    boolean isEmpty();

    /**
     * Executes the given {@link Runnable} if this {@code Option} is empty ({@code None}).
     *
     * @param action a {@code Runnable} to execute
     * @return this {@code Option}
     */
    default Option<T> onEmpty(@NonNull Runnable action) {
        Objects.requireNonNull(action, "action is null");
        if (isEmpty()) {
            action.run();
        }
        return this;
    }

    /**
     * Indicates that an {@code Option}'s value is computed synchronously.
     *
     * @return {@code false}
     */
    @Override
    default boolean isAsync() {
        return false;
    }

    /**
     * Checks whether this {@code Option} contains a value.
     * <p>
     * Note that {@code Some(null)} is considered defined.
     *
     * @return {@code true} if this is {@code Some}, {@code false} if this is {@code None}
     */
    default boolean isDefined() {
        return !isEmpty();
    }

    /**
     * Indicates that an {@code Option}'s value is computed eagerly.
     *
     * @return {@code false}
     */
    @Override
    default boolean isLazy() {
        return false;
    }

    /**
     * Indicates that an {@code Option} contains exactly one value.
     *
     * @return {@code true}
     */
    @Override
    default boolean isSingleValued() {
        return true;
    }

    /**
     * Returns the value contained in this {@code Some}, or throws if this is {@code None}.
     *
     * @return the contained value
     * @throws NoSuchElementException if this is {@code None}
     */
    @Override
    T get();

    /**
     * Returns the value contained in this {@code Some}, or the provided {@code other} value if this is {@code None}.
     * <p>
     * Note that {@code other} is evaluated eagerly.
     *
     * @param other an alternative value to return if this is {@code None}
     * @return the contained value if defined, otherwise {@code other}
     */
    @Override
    default T getOrElse(T other) {
        return isEmpty() ? other : get();
    }

    /**
     * Returns this {@code Option} if it is non-empty, otherwise returns the provided alternative {@code Option}.
     *
     * @param other an alternative {@code Option} to return if this is {@code None}
     * @return this {@code Option} if defined, otherwise {@code other}
     */
    @SuppressWarnings("unchecked")
    default Option<T> orElse(@NonNull Option<? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        return isEmpty() ? (Option<T>) other : this;
    }

    /**
     * Returns this {@code Option} if it is non-empty; otherwise, returns the {@code Option} provided by the supplier.
     *
     * @param supplier a supplier of an alternative {@code Option} if this is {@code None}
     * @return this {@code Option} if defined, otherwise the result of {@code supplier.get()}
     * @throws NullPointerException if {@code supplier} is null
     */
    @SuppressWarnings("unchecked")
    default Option<T> orElse(@NonNull Supplier<? extends Option<? extends T>> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isEmpty() ? (Option<T>) supplier.get() : this;
    }

    /**
     * Returns the value contained in this {@code Some}, or the value supplied by {@code supplier} if this is {@code None}.
     * <p>
     * The alternative value is evaluated lazily.
     *
     * @param supplier a supplier of an alternative value if this is {@code None}
     * @return the contained value if defined, otherwise the value returned by {@code supplier}
     * @throws NullPointerException if {@code supplier} is null
     */
    @Override
    default T getOrElse(@NonNull Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isEmpty() ? supplier.get() : get();
    }

    /**
     * Returns the value contained in this {@code Some}, or throws an exception provided by {@code exceptionSupplier} if this is {@code None}.
     *
     * @param exceptionSupplier a supplier of the exception to throw if this is {@code None}
     * @param <X>               the type of the exception
     * @return the contained value if defined
     * @throws X if this {@code Option} is {@code None}
     * @throws NullPointerException if {@code exceptionSupplier} is null
     */
    @Override
    default <X extends Throwable> T getOrElseThrow(@NonNull Supplier<X> exceptionSupplier) throws X {
        Objects.requireNonNull(exceptionSupplier, "exceptionSupplier is null");
        if (isEmpty()) {
            throw exceptionSupplier.get();
        } else {
            return get();
        }
    }

    /**
     * Returns {@code Some(value)} if this {@code Option} is a {@code Some} and the contained value satisfies the given predicate.
     * Otherwise, returns {@code None}.
     *
     * @param predicate a predicate to test the contained value
     * @return {@code Some(value)} if the value satisfies the predicate, otherwise {@code None}
     * @throws NullPointerException if {@code predicate} is null
     */
    default Option<T> filter(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return isEmpty() || predicate.test(get()) ? this : none();
    }

    /**
     * Transforms the value of this {@code Option} using the given mapper if it is a {@code Some}.
     * Returns {@code None} if this is {@code None}.
     *
     * @param mapper a function to transform the contained value
     * @param <U>    the type of the resulting {@code Option}'s value
     * @return a new {@code Option} containing the mapped value, or {@code None}
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    default <U> Option<U> flatMap(@NonNull Function<? super T, ? extends Option<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return isEmpty() ? none() : (Option<U>) mapper.apply(get());
    }

    /**
     * Transforms the value of this {@code Some} using the given mapper and wraps it in a new {@code Some}.
     * Returns {@code None} if this is {@code None}.
     *
     * @param mapper a function to transform the contained value
     * @param <U>    the type of the resulting {@code Some}'s value
     * @return a new {@code Some} with the mapped value if this is defined, otherwise {@code None}
     * @throws NullPointerException if {@code mapper} is null
     */
    @Override
    default <U> Option<U> map(@NonNull Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return isEmpty() ? none() : some(mapper.apply(get()));
    }

    @Override
    default <U> Option<U> mapTo(U value) {
        return map(ignored -> value);
    }

    @Override
    default Option<Void> mapToVoid() {
        return map(ignored -> null);
    }

    /**
     * Converts this {@code Option} to a {@link Try}, then applies the given checked function if this is a {@link Try.Success},
     * passing the contained value to it.
     *
     * @param <U>    the type of the resulting {@code Try}'s value
     * @param mapper a checked function to transform the contained value
     * @return a {@code Try} containing the mapped value if this {@code Option} is defined, otherwise a {@link Try.Failure}
     * @throws NullPointerException if {@code mapper} is null
     */
    default <U> Try<U> mapTry(@NonNull CheckedFunction1<? super T, ? extends U> mapper) {
        return toTry().mapTry(mapper);
    }

    /**
     * Folds this {@code Option} into a single value by applying one of two functions:
     * <ul>
     *     <li>{@code ifNone} is applied if this is {@code None}</li>
     *     <li>{@code f} is applied to the contained value if this is {@code Some}</li>
     * </ul>
     *
     * @param ifNone a function to produce a value if this is {@code None}
     * @param f      a function to transform the contained value if this is {@code Some}
     * @param <U>    the type of the folded result
     * @return the result of applying {@code f} or {@code ifNone} depending on whether this is {@code Some} or {@code None}
     * @throws NullPointerException if {@code ifNone} or {@code f} is null
     */
    default <U> U fold(@NonNull Supplier<? extends U> ifNone, @NonNull Function<? super T, ? extends U> f) {
        return this.<U>map(f).getOrElse(ifNone);
    }

    /**
     * Executes the given action on the contained value if this {@code Option} is defined ({@code Some}),
     * otherwise does nothing.
     *
     * @param action a consumer to apply to the contained value
     * @return this {@code Option}
     * @throws NullPointerException if {@code action} is null
     */
    @Override
    default Option<T> peek(@NonNull Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (isDefined()) {
            action.accept(get());
        }
        return this;
    }

    /**
     * Transforms this {@code Option} into a value of type {@code U} using the given function.
     *
     * @param f   a function to transform this {@code Option}
     * @param <U> the type of the result
     * @return the result of applying {@code f} to this {@code Option}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(@NonNull Function<? super Option<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    @Override
    default @NonNull Iterator<T> iterator() {
        return isEmpty() ? Iterator.empty() : Iterator.of(get());
    }

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    /**
     * Some represents a defined {@link Option}. It contains a value which may be null. However, to
     * create an Option containing null, {@code new Some(null)} has to be called. In all other cases
     * {@link Option#of(Object)} is sufficient.
     *
     * @param <T> The type of the optional value.
     * @author Daniel Dietrich
     */
    final class Some<T> implements Option<T>, Serializable {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("serial") // Conditionally serializable
        private final T value;

        /**
         * Creates a new Some containing the given value.
         *
         * @param value A value, may be null
         */
        private Some(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
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
            return Objects.hashCode(value);
        }

        @Override
        public String stringPrefix() {
            return "Some";
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + value + ")";
        }
    }

    /**
     * None is a singleton representation of the undefined {@link Option}.
     *
     * @param <T> The type of the optional value.
     * @author Daniel Dietrich
     */
    final class None<T> implements Option<T>, Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The singleton instance of None.
         */
        private static final None<?> INSTANCE = new None<>();

        /**
         * Hidden constructor.
         */
        private None() {
        }

        @Override
        public T get() {
            throw new NoSuchElementException("No value present");
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
        public String stringPrefix() {
            return "None";
        }

        @Override
        public String toString() {
            return stringPrefix();
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
