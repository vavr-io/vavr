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

import io.vavr.*;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jspecify.annotations.NonNull;

/**
 * An implementation similar to Scalaz's 
 * <a href="http://eed3si9n.com/learning-scalaz/Validation.html">Validation</a> control.
 *
 * <p>
 * The {@code Validation} type is an applicative functor, not a Monad. While a Monad
 * short-circuits on the first error, an applicative functor accumulates all errors,
 * making it particularly useful for validation scenarios where all errors should be reported.
 * </p>
 *
 * <pre>
 * <code>
 * // Validation construction:
 *
 * // Valid
 * Validation&lt;String, Integer&gt; valid = Validation.valid(5);
 *
 * // Invalid
 * Validation&lt;List&lt;String>, Integer&gt;invalid = Validation.invalid(List.of("error1", "error2"));
 *
 * // Validation combination:
 *
 * Validation&lt;String, String> valid1 = Validation.valid("John");
 * Validation&lt;String, Integer> valid2 = Validation.valid(5);
 * Validation&lt;String, Option&lt;String&gt;&gt; valid3 = Validation.valid(Option.of("123 Fake St."));
 * Function3&lt;String, Integer, Option&lt;String&gt;, Person> f = ...;
 *
 * Validation&lt;List&lt;String&gt;, String&gt; result =
 *     valid1.combine(valid2).ap((name, age) -> "Name: " + name + " Age: " + age);
 *
 * Validation&lt;List&lt;String&gt;, Person&gt; result2 =
 *     valid1.combine(valid2).combine(valid3).ap(f);
 *
 * // Another way to combine validations:
 * Validation&lt;List&lt;String&gt;, Person&gt; result3 =
 *     Validation.combine(valid1, valid2, valid3).ap(f);
 * </code>
 * </pre>
 *
 * @param <E> the type of values in the case of invalid
 * @param <T> the type of values in the case of valid
 * @author Eric Nelson
 * @see <a href="https://github.com/scalaz/scalaz/blob/series/7.3.x/core/src/main/scala/scalaz/Validation.scala">
 *     Scalaz Validation source</a>
 */
public interface Validation<E, T> extends Value<T>, Serializable {

    long serialVersionUID = 1L;

    /**
     * Creates a {@link Valid} that contains the given {@code value}.
     *
     * @param <E>   type of the error
     * @param <T>   type of the given {@code value}
     * @param value A value
     * @return {@code Valid(value)}
     */
    static <E, T> Validation<E, T> valid(T value) {
        return new Valid<>(value);
    }

    /**
     * Creates an {@link Invalid} that contains the given {@code error}.
     *
     * @param <E>   type of the given {@code error}
     * @param <T>   type of the value
     * @param error An error
     * @return {@code Invalid(error)}
     * @throws NullPointerException if error is null
     */
    static <E, T> Validation<E, T> invalid(E error) {
        Objects.requireNonNull(error, "error is null");
        return new Invalid<>(error);
    }

    /**
     * Creates a {@code Validation} of an {@code Either}.
     *
     * @param either An {@code Either}
     * @param <E>    error type
     * @param <T>    value type
     * @return A {@code Valid(either.get())} if either is a Right, otherwise {@code Invalid(either.getLeft())}.
     * @throws NullPointerException if either is null
     */
    static <E, T> Validation<E, T> fromEither(@NonNull Either<E, T> either) {
        Objects.requireNonNull(either, "either is null");
        return either.isRight() ? valid(either.get()) : invalid(either.getLeft());
    }

    /**
     * Creates a {@code Validation} of an {@code Try}.
     *
     * @param t      A {@code Try}
     * @param <T>    type of the valid value
     * @return A {@code Valid(t.get())} if t is a Success, otherwise {@code Invalid(t.getCause())}.
     * @throws NullPointerException if {@code t} is null
     */
    static <T> Validation<Throwable, T> fromTry(@NonNull Try<? extends T> t) {
        Objects.requireNonNull(t, "t is null");
        return t.isSuccess() ? valid(t.get()) : invalid(t.getCause());
    }


    /**
     * Reduces many {@code Validation} instances into a single {@code Validation} by transforming an
     * {@code Iterable<Validation<? extends T>>} into a {@code Validation<Seq<T>>}.
     *
     * @param <E>    value type in the case of invalid
     * @param <T>    value type in the case of valid
     * @param values An iterable of Validation instances.
     * @return A valid Validation of a sequence of values if all Validation instances are valid
     * or an invalid Validation containing an accumulated List of errors.
     * @throws NullPointerException if values is null
     */
    static <E, T> Validation<Seq<E>, Seq<T>> sequence(@NonNull Iterable<? extends Validation<? extends Seq<? extends E>, ? extends T>> values) {
        Objects.requireNonNull(values, "values is null");
        List<E> errors = List.empty();
        List<T> list = List.empty();
        for (Validation<? extends Seq<? extends E>, ? extends T> value : values) {
            if (value.isInvalid()) {
                errors = errors.prependAll(value.getError().reverse());
            } else if (errors.isEmpty()) {
                list = list.prepend(value.get());
            }
        }
        return errors.isEmpty() ? valid(list.reverse()) : invalid(errors.reverse());
    }

    /**
     * Maps the values of an iterable to a sequence of mapped values into a single {@code Validation} by
     * transforming an {@code Iterable<? extends T>} into a {@code Validation<Seq<U>>}.
     *
     * @param values   An {@code Iterable} of values.
     * @param mapper   A mapper of values to Validations
     * @param <T>      The type of the given values.
     * @param <E>      The mapped error value type.
     * @param <U>      The mapped valid value type.
     * @return A {@code Validation} of a {@link Seq} of results.
     * @throws NullPointerException if values or f is null.
     */
    static <E, T, U> Validation<Seq<E>, Seq<U>> traverse(
      @NonNull Iterable<? extends T> values, 
      @NonNull Function<? super T, ? extends Validation<? extends Seq<? extends E>, ? extends U>> mapper) {
        Objects.requireNonNull(values, "values is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return sequence(Iterator.ofAll(values).map(mapper));
    }

    /**
     * Narrows a widened {@code Validation<? extends E, ? extends T>} to {@code Validation<E, T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param validation A {@code Validation}.
     * @param <E>        type of error
     * @param <T>        type of valid value
     * @return the given {@code validation} instance as narrowed type {@code Validation<E, T>}.
     */
    @SuppressWarnings("unchecked")
    static <E, T> Validation<E, T> narrow(Validation<? extends E, ? extends T> validation) {
        return (Validation<E, T>) validation;
    }

    /**
     * Decides which {@code Validation<E, T>} to return, depending on the test value -
     * if it's true, the result will be a {@link Validation.Valid},
     * if it's false - the result will be a {@link Validation.Invalid}
     *
     * @param test   A {@code boolean} value to evaluate
     * @param valid  A {@code Supplier<? extends T>} supplier of valid value, called if test is true
     * @param error  A {@code Supplier<? extends E>} supplier of error, called if test is false
     * @param <E>    Type of error
     * @param <T>    Type of valid value
     *
     * @return {@code Validation<E, T>} with valid value or error, depending on the test condition evaluation
     *
     * @throws NullPointerException if any of the arguments is null
     */
    static <E, T> Validation<E, T> cond(boolean test, @NonNull Supplier<? extends T> valid, @NonNull Supplier<? extends E> error) {
        Objects.requireNonNull(valid, "valid is null");
        Objects.requireNonNull(error, "error is null");

        return test ? valid(valid.get()) : invalid(error.get());
    }

    /**
     * Decides which {@code Validation<E, T>} to return, depending on the test value -
     * if it's true, the result will be a {@link Validation.Valid},
     * if it's false - the result will be a {@link Validation.Invalid}
     *
     * @param test   A {@code boolean} value to evaluate
     * @param valid  A {@code T} valid value, called if test is true
     * @param error  An {@code E} error value, called if test is false
     * @param <E>    Type of error
     * @param <T>    Type of valid value
     *
     * @return {@code Validation<E, T>} with valid value or error, depending on the test condition evaluation
     *
     * @throws NullPointerException if any of the arguments is null
     */
    static <E, T> Validation<E, T> cond(boolean test, @NonNull T valid, @NonNull E error) {
        Objects.requireNonNull(valid, "valid is null");
        Objects.requireNonNull(error, "error is null");

        return test ? valid(valid) : invalid(error);
    }

    /**
     * Combines two {@code Validation}s into a {@link Builder}.
     *
     * @param <E>         type of error
     * @param <T1>        type of first valid value
     * @param <T2>        type of second valid value
     * @param validation1 first validation
     * @param validation2 second validation
     * @return an instance of Builder&lt;E,T1,T2&gt;
     * @throws NullPointerException if validation1 or validation2 is null
     */
    static <E, T1, T2> Builder<E, T1, T2> combine(@NonNull Validation<E, T1> validation1, @NonNull Validation<E, T2> validation2) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        return new Builder<>(validation1, validation2);
    }

    /**
     * Combines three {@code Validation}s into a {@link Builder3}.
     *
     * @param <E>         type of error
     * @param <T1>        type of first valid value
     * @param <T2>        type of second valid value
     * @param <T3>        type of third valid value
     * @param validation1 first validation
     * @param validation2 second validation
     * @param validation3 third validation
     * @return an instance of Builder3&lt;E,T1,T2,T3&gt;
     * @throws NullPointerException if validation1, validation2 or validation3 is null
     */
    static <E, T1, T2, T3> Builder3<E, T1, T2, T3> combine(@NonNull Validation<E, T1> validation1, @NonNull Validation<E, T2> validation2, @NonNull Validation<E, T3> validation3) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        return new Builder3<>(validation1, validation2, validation3);
    }

    /**
     * Combines four {@code Validation}s into a {@link Builder4}.
     *
     * @param <E>         type of error
     * @param <T1>        type of first valid value
     * @param <T2>        type of second valid value
     * @param <T3>        type of third valid value
     * @param <T4>        type of fourth valid value
     * @param validation1 first validation
     * @param validation2 second validation
     * @param validation3 third validation
     * @param validation4 fourth validation
     * @return an instance of Builder3&lt;E,T1,T2,T3,T4&gt;
     * @throws NullPointerException if validation1, validation2, validation3 or validation4 is null
     */
    static <E, T1, T2, T3, T4> Builder4<E, T1, T2, T3, T4> combine(@NonNull Validation<E, T1> validation1, @NonNull Validation<E, T2> validation2, @NonNull Validation<E, T3> validation3, @NonNull Validation<E, T4> validation4) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        return new Builder4<>(validation1, validation2, validation3, validation4);
    }

    /**
     * Combines five {@code Validation}s into a {@link Builder5}.
     *
     * @param <E>         type of error
     * @param <T1>        type of first valid value
     * @param <T2>        type of second valid value
     * @param <T3>        type of third valid value
     * @param <T4>        type of fourth valid value
     * @param <T5>        type of fifth valid value
     * @param validation1 first validation
     * @param validation2 second validation
     * @param validation3 third validation
     * @param validation4 fourth validation
     * @param validation5 fifth validation
     * @return an instance of Builder3&lt;E,T1,T2,T3,T4,T5&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4 or validation5 is null
     */
    static <E, T1, T2, T3, T4, T5> Builder5<E, T1, T2, T3, T4, T5> combine(@NonNull Validation<E, T1> validation1, @NonNull Validation<E, T2> validation2, @NonNull Validation<E, T3> validation3, @NonNull Validation<E, T4> validation4, @NonNull Validation<E, T5> validation5) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        return new Builder5<>(validation1, validation2, validation3, validation4, validation5);
    }

    /**
     * Combines six {@code Validation}s into a {@link Builder6}.
     *
     * @param <E>         type of error
     * @param <T1>        type of first valid value
     * @param <T2>        type of second valid value
     * @param <T3>        type of third valid value
     * @param <T4>        type of fourth valid value
     * @param <T5>        type of fifth valid value
     * @param <T6>        type of sixth valid value
     * @param validation1 first validation
     * @param validation2 second validation
     * @param validation3 third validation
     * @param validation4 fourth validation
     * @param validation5 fifth validation
     * @param validation6 sixth validation
     * @return an instance of Builder3&lt;E,T1,T2,T3,T4,T5,T6&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5 or validation6 is null
     */
    static <E, T1, T2, T3, T4, T5, T6> Builder6<E, T1, T2, T3, T4, T5, T6> combine(@NonNull Validation<E, T1> validation1, @NonNull Validation<E, T2> validation2, @NonNull Validation<E, T3> validation3, @NonNull Validation<E, T4> validation4, @NonNull Validation<E, T5> validation5, @NonNull Validation<E, T6> validation6) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        Objects.requireNonNull(validation6, "validation6 is null");
        return new Builder6<>(validation1, validation2, validation3, validation4, validation5, validation6);
    }

    /**
     * Combines seven {@code Validation}s into a {@link Builder7}.
     *
     * @param <E>         type of error
     * @param <T1>        type of first valid value
     * @param <T2>        type of second valid value
     * @param <T3>        type of third valid value
     * @param <T4>        type of fourth valid value
     * @param <T5>        type of fifth valid value
     * @param <T6>        type of sixth valid value
     * @param <T7>        type of seventh valid value
     * @param validation1 first validation
     * @param validation2 second validation
     * @param validation3 third validation
     * @param validation4 fourth validation
     * @param validation5 fifth validation
     * @param validation6 sixth validation
     * @param validation7 seventh validation
     * @return an instance of Builder3&lt;E,T1,T2,T3,T4,T5,T6,T7&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6 or validation7 is null
     */
    static <E, T1, T2, T3, T4, T5, T6, T7> Builder7<E, T1, T2, T3, T4, T5, T6, T7> combine(@NonNull Validation<E, T1> validation1, @NonNull Validation<E, T2> validation2, @NonNull Validation<E, T3> validation3, @NonNull Validation<E, T4> validation4, @NonNull Validation<E, T5> validation5, @NonNull Validation<E, T6> validation6, @NonNull Validation<E, T7> validation7) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        Objects.requireNonNull(validation6, "validation6 is null");
        Objects.requireNonNull(validation7, "validation7 is null");
        return new Builder7<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7);
    }

    /**
     * Combines eight {@code Validation}s into a {@link Builder8}.
     *
     * @param <E>         type of error
     * @param <T1>        type of first valid value
     * @param <T2>        type of second valid value
     * @param <T3>        type of third valid value
     * @param <T4>        type of fourth valid value
     * @param <T5>        type of fifth valid value
     * @param <T6>        type of sixth valid value
     * @param <T7>        type of seventh valid value
     * @param <T8>        type of eighth valid value
     * @param validation1 first validation
     * @param validation2 second validation
     * @param validation3 third validation
     * @param validation4 fourth validation
     * @param validation5 fifth validation
     * @param validation6 sixth validation
     * @param validation7 seventh validation
     * @param validation8 eighth validation
     * @return an instance of Builder3&lt;E,T1,T2,T3,T4,T5,T6,T7,T8&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7 or validation8 is null
     */
    static <E, T1, T2, T3, T4, T5, T6, T7, T8> Builder8<E, T1, T2, T3, T4, T5, T6, T7, T8> combine(@NonNull Validation<E, T1> validation1, @NonNull Validation<E, T2> validation2, @NonNull Validation<E, T3> validation3, @NonNull Validation<E, T4> validation4, @NonNull Validation<E, T5> validation5, @NonNull Validation<E, T6> validation6, @NonNull Validation<E, T7> validation7, @NonNull Validation<E, T8> validation8) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        Objects.requireNonNull(validation6, "validation6 is null");
        Objects.requireNonNull(validation7, "validation7 is null");
        Objects.requireNonNull(validation8, "validation8 is null");
        return new Builder8<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8);
    }

    /**
     * Check whether this is of type {@code Valid}
     *
     * @return true if is a Valid, false if is an Invalid
     */
    boolean isValid();

    /**
     * Check whether this is of type {@code Invalid}
     *
     * @return true if is an Invalid, false if is a Valid
     */
    boolean isInvalid();

    /**
     * Returns this {@code Validation} if it is valid, otherwise return the alternative.
     *
     * @param other An alternative {@code Validation}
     * @return this {@code Validation} if it is valid, otherwise return the alternative.
     */
    @SuppressWarnings("unchecked")
    default Validation<E, T> orElse(@NonNull Validation<? extends E, ? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        return isValid() ? this : (Validation<E, T>) other;
    }

    /**
     * Returns this {@code Validation} if it is valid, otherwise return the result of evaluating supplier.
     *
     * @param supplier An alternative {@code Validation} supplier
     * @return this {@code Validation} if it is valid, otherwise return the result of evaluating supplier.
     */
    @SuppressWarnings("unchecked")
    default Validation<E, T> orElse(@NonNull Supplier<Validation<? extends E, ? extends T>> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isValid() ? this : (Validation<E, T>) supplier.get();
    }

    @Override
    default boolean isEmpty() {
        return isInvalid();
    }

    /**
     * Gets the value of this {@code Validation} if is a {@code Valid} or throws if this is an {@code Invalid}.
     *
     * @return The value of this {@code Validation}
     * @throws NoSuchElementException if this is an {@code Invalid}
     */
    @Override
    T get();

    /**
     * Gets the value if it is a Valid or an value calculated from the error.
     *
     * @param other a function which converts an error to an alternative value
     * @return the value, if the underlying Validation is a Valid, or else the alternative value
     * provided by {@code other} by applying the error.
     */
    default T getOrElseGet(@NonNull Function<? super E, ? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        if (isValid()) {
            return get();
        } else {
            return other.apply(getError());
        }
    }

    /**
     * Gets the error of this Validation if it is an {@code Invalid} or throws if this is a {@code Valid}.
     *
     * @return The error, if present
     * @throws RuntimeException if this is a {@code Valid}
     */
    E getError();

    /**
     * Converts this Validation to an {@link Either}.
     *
     * @return {@code Either.right(get())} if this is valid, otherwise {@code Either.left(getError())}.
     */
    default Either<E, T> toEither() {
        return isValid() ? Either.right(get()) : Either.left(getError());
    }

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    /**
     * Performs the given action for the value contained in {@code Valid}, or does nothing
     * if this is an {@code Invalid}.
     *
     * @param action the action to be performed on the contained value
     * @throws NullPointerException if action is null
     */
    @Override
    default void forEach(@NonNull Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (isValid()) {
            action.accept(get());
        }
    }

    /**
     * Transforms this {@code Validation} to a value of type {@code U}.
     * <p>
     * Example:
     * <pre>{@code
     * Validation<List<String>, String> valid = ...;<br>
     * int i = valid.fold(List::length, String::length);
     * }</pre>
     *
     * @param <U>       the fold result type
     * @param ifInvalid an error mapper
     * @param ifValid   an mapper for a valid value
     * @return {@code ifValid.apply(get())} if this is valid, otherwise {@code ifInvalid.apply(getError())}.
     * @throws NullPointerException if one of the given mappers {@code ifInvalid} or {@code ifValid} is null
     */
    default <U> U fold(@NonNull Function<? super E, ? extends U> ifInvalid, @NonNull Function<? super T, ? extends U> ifValid) {
        Objects.requireNonNull(ifInvalid, "ifInvalid is null");
        Objects.requireNonNull(ifValid, "ifValid is null");
        return isValid() ? ifValid.apply(get()) : ifInvalid.apply(getError());
    }

    /**
     * Flip the valid/invalid values for this Validation. If this is a Valid&lt;E,T&gt;, returns Invalid&lt;T,E&gt;.
     * Or if this is an Invalid&lt;E,T&gt;, return a Valid&lt;T,E&gt;.
     *
     * @return a flipped instance of Validation
     */
    default Validation<T, E> swap() {
        if (isInvalid()) {
            final E error = this.getError();
            return Validation.valid(error);
        } else {
            final T value = this.get();
            return Validation.invalid(value);
        }
    }

    @Override
    default <U> Validation<E, U> map(@NonNull Function<? super T, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        if (isInvalid()) {
            return Validation.invalid(this.getError());
        } else {
            final T value = this.get();
            return Validation.valid(f.apply(value));
        }
    }

    /**
     * Whereas map only performs a mapping on a valid Validation, and mapError performs a mapping on an invalid
     * Validation, bimap allows you to provide mapping actions for both, and will give you the result based
     * on what type of Validation this is. Without this, you would have to do something like:
     *
     * validation.map(...).mapError(...);
     *
     * @param <E2>        type of the mapping result if this is an invalid
     * @param <T2>        type of the mapping result if this is a valid
     * @param errorMapper the invalid mapping operation
     * @param valueMapper the valid mapping operation
     * @return an instance of Validation&lt;U,R&gt;
     * @throws NullPointerException if invalidMapper or validMapper is null
     */
    default <E2, T2> Validation<E2, T2> bimap(@NonNull Function<? super E, ? extends E2> errorMapper, @NonNull Function<? super T, ? extends T2> valueMapper) {
        Objects.requireNonNull(errorMapper, "errorMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        if (isInvalid()) {
            final E error = this.getError();
            return Validation.invalid(errorMapper.apply(error));
        } else {
            final T value = this.get();
            return Validation.valid(valueMapper.apply(value));
        }
    }

    /**
     * Applies a function f to the error of this Validation if this is an Invalid. Otherwise does nothing
     * if this is a Valid.
     *
     * @param <U> type of the error resulting from the mapping
     * @param f   a function that maps the error in this Invalid
     * @return an instance of Validation&lt;U,T&gt;
     * @throws NullPointerException if mapping operation f is null
     */
    default <U> Validation<U, T> mapError(@NonNull Function<? super E, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        if (isInvalid()) {
            final E error = this.getError();
            return Validation.invalid(f.apply(error));
        } else {
            return Validation.valid(this.get());
        }
    }

    default <U> Validation<Seq<E>, U> ap(@NonNull Validation<Seq<E>, ? extends Function<? super T, ? extends U>> validation) {
        Objects.requireNonNull(validation, "validation is null");
        if (isValid()) {
            if (validation.isValid()) {
                final Function<? super T, ? extends U> f = validation.get();
                final U u = f.apply(this.get());
                return valid(u);
            } else {
                final Seq<E> errors = validation.getError();
                return invalid(errors);
            }
        } else {
            if (validation.isValid()) {
                final E error = this.getError();
                return invalid(List.of(error));
            } else {
                final Seq<E> errors = validation.getError();
                final E error = this.getError();
                return invalid(errors.append(error));
            }
        }
    }

    /**
     * Combines two {@code Validation}s to form a {@link Builder}, which can then be used to perform further
     * combines, or apply a function to it in order to transform the {@link Builder} into a {@code Validation}.
     *
     * @param <U>        type of the value contained in validation
     * @param validation the validation object to combine this with
     * @return an instance of Builder
     */
    default <U> Builder<E, T, U> combine(Validation<E, U> validation) {
        return new Builder<>(this, validation);
    }

    // -- Implementation of Value

    default Option<Validation<E, T>> filter(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return isInvalid() || predicate.test(get()) ? Option.some(this) : Option.none();
    }

    @SuppressWarnings("unchecked")
    default <U> Validation<E, U> flatMap(@NonNull Function<? super T, ? extends Validation<E, ? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return isInvalid() ? (Validation<E, U>) this : (Validation<E, U>) mapper.apply(get());
    }

    @Override
    default Validation<E, T> peek(@NonNull Consumer<? super T> action) {
        if (isValid()) {
            action.accept(get());
        }
        return this;
    }

    /**
     * A {@code Validation}'s value is computed synchronously.
     *
     * @return false
     */
    @Override
    default boolean isAsync() {
        return false;
    }

    /**
     * A {@code Validation}'s value is computed eagerly.
     *
     * @return false
     */
    @Override
    default boolean isLazy() {
        return false;
    }

    @Override
    default boolean isSingleValued() {
        return true;
    }

    @Override
    default @NonNull Iterator<T> iterator() {
        return isValid() ? Iterator.of(get()) : Iterator.empty();
    }

    /**
     * A valid Validation
     *
     * @param <E> type of the error of this Validation
     * @param <T> type of the value of this Validation
     */
    final class Valid<E, T> implements Validation<E, T>, Serializable {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("serial") // Conditionally serializable
        private final T value;

        /**
         * Construct a {@code Valid}
         *
         * @param value The value of this success
         */
        private Valid(T value) {
            this.value = value;
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public E getError() throws RuntimeException {
            throw new NoSuchElementException("error of 'valid' Validation");
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Valid && Objects.equals(value, ((Valid<?, ?>) obj).value));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String stringPrefix() {
            return "Valid";
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + value + ")";
        }

    }

    /**
     * An invalid Validation
     *
     * @param <E> type of the error of this Validation
     * @param <T> type of the value of this Validation
     */
    final class Invalid<E, T> implements Validation<E, T>, Serializable {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("serial") // Conditionally serializable
        private final E error;

        /**
         * Construct an {@code Invalid}
         *
         * @param error The value of this error
         */
        private Invalid(E error) {
            this.error = error;
        }

        @Override
        public boolean isValid() {
            return false;
        }

        @Override
        public boolean isInvalid() {
            return true;
        }

        @Override
        public T get() throws RuntimeException {
            throw new NoSuchElementException("get of 'invalid' Validation");
        }

        @Override
        public E getError() {
            return error;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Invalid && Objects.equals(error, ((Invalid<?, ?>) obj).error));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(error);
        }

        @Override
        public String stringPrefix() {
            return "Invalid";
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + error + ")";
        }

    }

    final class Builder<E, T1, T2> {

        private Validation<E, T1> v1;
        private Validation<E, T2> v2;

        private Builder(Validation<E, T1> v1, Validation<E, T2> v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        public <R> Validation<Seq<E>, R> ap(Function2<T1, T2, R> f) {
            return v2.ap(v1.ap(Validation.valid(f.curried())));
        }

        public <T3> Builder3<E, T1, T2, T3> combine(Validation<E, T3> v3) {
            return new Builder3<>(v1, v2, v3);
        }

    }

    final class Builder3<E, T1, T2, T3> {

        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;

        private Builder3(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }

        public <R> Validation<Seq<E>, R> ap(Function3<T1, T2, T3, R> f) {
            return v3.ap(v2.ap(v1.ap(Validation.valid(f.curried()))));
        }

        public <T4> Builder4<E, T1, T2, T3, T4> combine(Validation<E, T4> v4) {
            return new Builder4<>(v1, v2, v3, v4);
        }

    }

    final class Builder4<E, T1, T2, T3, T4> {

        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;

        private Builder4(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
        }

        public <R> Validation<Seq<E>, R> ap(Function4<T1, T2, T3, T4, R> f) {
            return v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(f.curried())))));
        }

        public <T5> Builder5<E, T1, T2, T3, T4, T5> combine(Validation<E, T5> v5) {
            return new Builder5<>(v1, v2, v3, v4, v5);
        }

    }

    final class Builder5<E, T1, T2, T3, T4, T5> {

        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;

        private Builder5(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
        }

        public <R> Validation<Seq<E>, R> ap(Function5<T1, T2, T3, T4, T5, R> f) {
            return v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(f.curried()))))));
        }

        public <T6> Builder6<E, T1, T2, T3, T4, T5, T6> combine(Validation<E, T6> v6) {
            return new Builder6<>(v1, v2, v3, v4, v5, v6);
        }

    }

    final class Builder6<E, T1, T2, T3, T4, T5, T6> {

        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;

        private Builder6(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
        }

        public <R> Validation<Seq<E>, R> ap(Function6<T1, T2, T3, T4, T5, T6, R> f) {
            return v6.ap(v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(f.curried())))))));
        }

        public <T7> Builder7<E, T1, T2, T3, T4, T5, T6, T7> combine(Validation<E, T7> v7) {
            return new Builder7<>(v1, v2, v3, v4, v5, v6, v7);
        }

    }

    final class Builder7<E, T1, T2, T3, T4, T5, T6, T7> {

        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;

        private Builder7(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
        }

        public <R> Validation<Seq<E>, R> ap(Function7<T1, T2, T3, T4, T5, T6, T7, R> f) {
            return v7.ap(v6.ap(v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(f.curried()))))))));
        }

        public <T8> Builder8<E, T1, T2, T3, T4, T5, T6, T7, T8> combine(Validation<E, T8> v8) {
            return new Builder8<>(v1, v2, v3, v4, v5, v6, v7, v8);
        }

    }

    final class Builder8<E, T1, T2, T3, T4, T5, T6, T7, T8> {

        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;

        private Builder8(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
        }

        public <R> Validation<Seq<E>, R> ap(Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> f) {
            return v8.ap(v7.ap(v6.ap(v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(f.curried())))))))));
        }
    }
}
