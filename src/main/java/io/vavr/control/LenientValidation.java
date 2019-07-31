/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
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
import io.vavr.Function0;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Function4;
import io.vavr.Function5;
import io.vavr.Function6;
import io.vavr.Function7;
import io.vavr.Function8;
import io.vavr.Value;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A lenient variant of the {@code Validation} class.
 * While a {@code Validation} can either be a {@code Valid} or an {@code Invalid}, an instance of {@code LenientValidation}
 * can contain errors and a value at the same time.
 * This is especially helpful in situations where you want to validate a sequence of values. While the validation of a single value
 * might fail, it can still be reasonable to continue with all the values of the sequence that where successfully validated.
 *
 * <pre>
 * <code>
 * <b>LenientValidation construction:</b>
 *
 * <i>Fully valid instance:</i>
 * LenientValidation&lt;String,Integer&gt; valid = LenientValidation.valid(5);
 *
 * <i>Fully invalid instance:</i>
 * LenientValidation&lt;String,Integer&gt; invalid = LenientValidation.invalid(List.of("error1","error2"));
 *
 * <i>Partially invalid instance:</i>
 * LenientValidation&lt;String,Integer&gt; partiallyValid = LenientValidation.of(List.of("error1","error2"), Option.some(5));
 *
 * <b>LenientValidation combination:</b>
 *
 * LenientValidation&lt;String,String&gt; valid1 = LenientValidation.valid("John");
 * LenientValidation&lt;String,Integer&gt; valid2 = LenientValidation.valid(5);
 * Function2&lt;String,Integer,Person&gt; f = ...;
 *
 * LenientValidation&lt;String,Person&gt; result = LenientValidation.combine(valid1, valid2).ap(f);
 * // Results in a fully valid Person instance
 *
 * <b>LenientValidation sequencing:</b>
 *
 * LenientValidation&lt;String,Integer&gt; valid1 = LenientValidation.valid(2);
 * LenientValidation&lt;String,Integer&gt; valid2 = LenientValidation.valid(3);
 * LenientValidation&lt;String,Integer&gt; valid3 = LenientValidation.invalid(List.of("error"));
 *
 * LenientValidation&lt;String,List&lt;Integer&gt;&gt; result = LenientValidation.sequence(List.of(valid1, valid2, valid3));
 * // Results in the value [2, 3] together with the occurred error ["error"]
 *
 * <b>LenientValidation traversing:</b>
 *
 * LenientValidation&lt;String,List&lt;Integer&gt;&gt; result =
 *   LenientValidation.traverse(List.of(1, 2, 3, 4), i -&gt; i % 2 == 0 ? LenientValidation.valid(i) : LenientValidation.invalid("odd number " + i));
 * // Results in the value [2, 4] together with the occurred errors ["odd number 1", "odd number 3"]
 *
 * </code>
 * </pre>
 *
 * @param <E> error type
 * @param <T> value type
 */
public final class LenientValidation<E, T> implements Value<T>, Serializable {

    public static final long serialVersionUID = 1L;

    private final Seq<E> errors;
    private final Option<T> value;

    private LenientValidation(Iterable<E> errors, Option<T> value) {
        Objects.requireNonNull(errors, "errors is null");
        Objects.requireNonNull(value, "value is null");
        this.errors = List.ofAll(errors);
        this.value = value;
    }

    /**
     * Builds a {@code LenientValidation} of a collection of errors and an
     * optional value
     * @param errors The collection of errors
     * @param value The option value
     * @param <E> error type
     * @param <T> value type
     * @return A {@code LenientValidation} instance
     */
    public static <E, T> LenientValidation<E, T> of(Iterable<E> errors, Option<T> value) {
        return new LenientValidation<>(errors, value);
    }

    /**
     * Builds a {@code LenientValidation} of a collection of errors and an
     * nullable value
     * @param errors The collection of errors
     * @param value The nullable value
     * @param <E> error type
     * @param <T> value type
     * @return A {@code LenientValidation} instance
     */
    public static <E, T> LenientValidation<E, T> fromNullable(Iterable<E> errors, T value) {
        return new LenientValidation<>(errors, Option.of(value));
    }

    /**
     * Builds a {@code LenientValidation} with an empty list of errors and an
     * option based on value
     * @param value The value used to create an option
     * @param <E> error type
     * @param <T> value type
     * @return A {@code LenientValidation} instance
     */
    public static <E, T> LenientValidation<E, T> valid(T value) {
        return of(List.empty(), Option.some(value));
    }

    /**
     * Builds a {@code LenientValidation} based on errors with {@code Option.none()} as
     * value.
     * @param errors The collection of errors
     * @param <E> error type
     * @param <T> value type
     * @return A {@code LenientValidation} instance
     */
    public static <E, T> LenientValidation<E, T> invalid(Iterable<E> errors) {
        return fromNullable(errors, null);
    }
    /**
     * Builds a {@code LenientValidation} that consist of an empty list of
     * error, and {@code Option.none()} as value
     *
     * @param <E> error type
     * @param <T> value type
     * @return A {@code LenientValidation} instance
     */
    public static <E, T> LenientValidation<E, T> empty() {
        return fromNullable(List.empty(), null);
    }

    /**
     * Builds a {@code LenientValidation} from a {@code Validation} instance,
     * where the invalid type is a sequence. The new {@code LenientValidation}
     * instance will have as value the valid part of {@code Validation} if the
     * validation is Valid together with an empty list of errors; otherwise
     * will have null(None) as value and the invalid part of the validation
     * as errors.
     *
     * @param validation A {@code Validation} instance with a sequence type for
     * the invalid part
     * @param <E> error type
     * @param <T> value type
     * @return A {@code LenientValidation} instance
     */
    public static <E, T> LenientValidation<E, T> fromMultiErrorValidation(Validation<Seq<E>, T> validation) {
        Objects.requireNonNull(validation, "validation is null");
        return fromNullable(
            validation.isInvalid() ? validation.getError() : List.empty(),
            validation.getOrElse((T) null)
        );
    }
    /**
     * Builds a {@code LenientValidation} from a {@code Validation} instance,
     * where the invalid part is used to build the errors, and the value is
     * the result of apply {@code Validation.getOrElse()} of the validation
     * instance.
     *
     * @param validation A {@code Validation} instance
     * @param <E> error type
     * @param <T> value type
     * @return A {@code LenientValidation} instance
     */
    public static <E, T> LenientValidation<E, T> fromValidation(Validation<E, T> validation) {
        Objects.requireNonNull(validation, "validation is null");
        return fromMultiErrorValidation(
            validation.mapError(List::of)
        );
    }

    /**
     * Converts this into a strict {@code Validation} that either holds
     * errors or a value.
     *
     * @return A {@code Valid} if this holds a value and there are no errors,
     * otherwise an {@code Invalid} with the errors
     */
    public Validation<Seq<E>, T> strict() {
        return hasErrors()
            ? Validation.invalid(errors)
            : value.toValidation(List.empty());
    }

    /**
     * Reduces many {@code LenientValidation} instances into a single {@code LenientValidation} by transforming an
     * {@code Iterable<LenientValidation<? extends E, ? extends T>>} into a {@code LenientValidation<E, Seq<T>>}.
     *
     * @param <E>    type of error
     * @param <T>    type of valid
     * @param values An {@code Iterable} of {@code LenientValidation} instances.
     * @return A {@code LenientValidation} containing the accumulated list of errors of each {@code LenientValidation} instance
     * and a list of all values of those {@code LenientValidation} instances that hold a value.
     * @throws NullPointerException if values is null
     */
    public static <E, T> LenientValidation<E, Seq<T>> sequence(Iterable<? extends LenientValidation<? extends E, ? extends T>> values) {
        Objects.requireNonNull(values, "values is null");
        List<E> errors = List.empty();
        List<T> list = List.empty();
        for (LenientValidation<? extends E, ? extends T> value : values) {
            errors = errors.prependAll(value.getErrors().reverse());
            list = value.getValue().map(list::prepend).getOrElse(list);
        }
        return fromNullable(errors.reverse(), list.reverse());
    }

    /**
     * Maps the values of an iterable to a sequence of mapped values into a single {@code LenientValidation} by
     * transforming an {@code Iterable<? extends T>} into a {@code LenientValidation<E, Seq<U>>}.
     *
     * @param values   An {@code Iterable} of values.
     * @param mapper   A mapper of values to {@code LenientValidation}'s
     * @param <T>      The type of the given values.
     * @param <E>      The mapped error type.
     * @param <U>      The mapped value type.
     * @return A {@code LenientValidation} of a {@link Seq} of results.
     * @throws NullPointerException if values or f is null.
     */
    public static <E, T, U> LenientValidation<E, Seq<U>> traverse(
        Iterable<? extends T> values,
        Function<? super T, ? extends LenientValidation<? extends E, ? extends U>> mapper)
    {
        Objects.requireNonNull(values, "values is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return sequence(Iterator.ofAll(values).map(mapper));
    }

    /**
     * Narrows a widened {@code LenientValidation<? extends E, ? extends T>}
     * to {@code LenientValidation<E, T>}
     * by performing a type-safe cast. This is eligible because immutable or
     * read-only collections are covariant.
     *
     * @param validation A {@code LenientValidation}.
     * @param <E>        type of error
     * @param <T>        type of value
     * @return the given {@code validation} instance as narrowed type {@code LenientValidation<E, T>}.
     */
    @SuppressWarnings("unchecked")
    public static <E, T> LenientValidation<E, T> narrow(LenientValidation<? extends E, ? extends T> validation) {
        return (LenientValidation<E, T>) validation;
    }

    public static <E, T1, T2> Builder2<E, T1, T2> combine(
        LenientValidation<E, T1> validation1,
        LenientValidation<E, T2> validation2)
    {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        return new Builder2<>(validation1, validation2);
    }

    public static <E, T1, T2, T3> Builder3<E, T1, T2, T3> combine(
        LenientValidation<E, T1> validation1,
        LenientValidation<E, T2> validation2,
        LenientValidation<E, T3> validation3)
    {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        return new Builder3<>(validation1, validation2, validation3);
    }

    public static <E, T1, T2, T3, T4> Builder4<E, T1, T2, T3, T4> combine(
        LenientValidation<E, T1> validation1,
        LenientValidation<E, T2> validation2,
        LenientValidation<E, T3> validation3,
        LenientValidation<E, T4> validation4)
    {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        return new Builder4<>(validation1, validation2, validation3, validation4);
    }

    public static <E, T1, T2, T3, T4, T5> Builder5<E, T1, T2, T3, T4, T5> combine(
        LenientValidation<E, T1> validation1,
        LenientValidation<E, T2> validation2,
        LenientValidation<E, T3> validation3,
        LenientValidation<E, T4> validation4,
        LenientValidation<E, T5> validation5)
    {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        return new Builder5<>(validation1, validation2, validation3, validation4, validation5);
    }

    public static <E, T1, T2, T3, T4, T5, T6> Builder6<E, T1, T2, T3, T4, T5, T6> combine(
        LenientValidation<E, T1> validation1,
        LenientValidation<E, T2> validation2,
        LenientValidation<E, T3> validation3,
        LenientValidation<E, T4> validation4,
        LenientValidation<E, T5> validation5,
        LenientValidation<E, T6> validation6)
    {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        Objects.requireNonNull(validation6, "validation6 is null");
        return new Builder6<>(validation1, validation2, validation3, validation4,
            validation5, validation6);
    }

    public static <E, T1, T2, T3, T4, T5, T6, T7> Builder7<E, T1, T2, T3, T4, T5, T6, T7> combine(
        LenientValidation<E, T1> validation1,
        LenientValidation<E, T2> validation2,
        LenientValidation<E, T3> validation3,
        LenientValidation<E, T4> validation4,
        LenientValidation<E, T5> validation5,
        LenientValidation<E, T6> validation6,
        LenientValidation<E, T7> validation7)
    {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        Objects.requireNonNull(validation6, "validation6 is null");
        Objects.requireNonNull(validation7, "validation7 is null");
        return new Builder7<>(validation1, validation2, validation3, validation4,
            validation5, validation6, validation7);
    }

    public static <E, T1, T2, T3, T4, T5, T6, T7, T8> Builder8<E, T1, T2, T3, T4, T5, T6, T7, T8> combine(
        LenientValidation<E, T1> validation1,
        LenientValidation<E, T2> validation2,
        LenientValidation<E, T3> validation3,
        LenientValidation<E, T4> validation4,
        LenientValidation<E, T5> validation5,
        LenientValidation<E, T6> validation6,
        LenientValidation<E, T7> validation7,
        LenientValidation<E, T8> validation8)
    {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        Objects.requireNonNull(validation6, "validation6 is null");
        Objects.requireNonNull(validation7, "validation7 is null");
        Objects.requireNonNull(validation8, "validation8 is null");
        return new Builder8<>(validation1, validation2, validation3, validation4,
            validation5, validation6, validation7, validation8);
    }


    /**
     * Gets a {@code Seq} of errors of this
     *
     * @return A {@code Seq} of errors
     */
    public Seq<E> getErrors() {
        return errors;
    }

    /**
     * Gets an {@code Option} maybe containing a valid value.
     *
     * @return A {@code Some} with the valid value or a {@code None} if there is no valid value
     */
    public Option<T> getValue() {
        return value;
    }

    /**
     * Checks whether this contains at least one error.
     *
     * @return true if this has at least one error, false if this does not have errors
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Check whether this holds a value.
     *
     * @return true if it holds a value, false if it does not hold a value
     */
    public boolean isValid() {
        return value.isDefined();
    }

    /**
     * Applies a function f to each error of this {@code LenientValidation}.
     *
     * @param <U> type of the error resulting from the mapping
     * @param f   a function that maps an error
     * @return an instance of {@code LenientValidation} with the errors mapped
     * @throws NullPointerException if mapping operation f is null
     */
    public <U> LenientValidation<U, T> mapError(Function1<E, U> f) {
        Objects.requireNonNull(f, "f is null");
        return of(
            getErrors().map(f),
            getValue()
        );
    }

    /**
     * Flatmaps the errors of this {@code LenientValidation}, producing multiple
     * errors for each of the errors of this {@code LenientValidation}
     *
     * @param <U> type of the error resulting from the mapping
     * @param f   a function that maps an error to an iterable of new errors
     * @return an instance of {@code LenientValidation} with the errors flatmapped
     * @throws NullPointerException if mapping operation f is null
     */
    public <U> LenientValidation<U, T> flatMapError(Function1<E, ? extends Iterable<U>> f) {
        Objects.requireNonNull(f, "f is null");
        return of(
            getErrors().flatMap(f),
            getValue()
        );
    }

    /**
     * Use a  {@code LenientValidation} and this to create a new instance.
     * The errors from the other are appended into our errors, the resulting value
     * is obtained by the mapping the other value (which is a function) over the
     * our value
     * @param validation a {@code LenientValidation} instance
     * @param <U> data type returned by the value part of the other {@code LenientValidation}
     * @return A new {@code LenientValidation} instance
     */
    public <U> LenientValidation<E, U> ap(LenientValidation<E, ? extends Function<? super T, ? extends U>> validation) {
        Objects.requireNonNull(validation, "validation is null");
        return of(
            errors.prependAll(validation.getErrors()),
            validation.getValue().flatMap(value::map)
        );
    }

    /**
     * Combines two {@code LenientValidation}s to form a {@link Builder2},
     * which can then be used to perform further
     * combines, or apply a function to it in order to transform the
     * {@link Builder2} into a {@code LenientValidation}.
     *
     * @param <U>        type of value
     * @param validation the {@code LenientValidation} object to combine this with
     * @return an instance of {@code Builder2}
     */
    public <U> Builder2<E, T, U> combine(LenientValidation<E, U> validation) {
        return new Builder2<>(this, validation);
    }

    /**
     * Filters the value of this with the predicate.
     *
     * @param predicate a predicate that decides if the value of this is valid
     * @return an instance of {@code LenientValidation} with the same errors as
     * this and the value of this if the predicate evaluates to true
     * @throws NullPointerException if predicate is null
     */
    public LenientValidation<E, T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return of(
            getErrors(),
            getValue().filter(predicate)
        );
    }

    /**
     * Map function f on the value and build a new {@code LenientValidation} based
     * on the resulting {@code LenientValidation}, the errors obtained (if exists)
     * are appended on this, the value is taken from the returned {@code LenientValidation}.
     *
     * @param f The function to flatMap with
     * @param <U> The new value type
     * @return A {@code LenientValidation} instance
     */
    public <U> LenientValidation<E, U> flatMap(Function1<? super T, LenientValidation<E, U>> f) {
        Objects.requireNonNull(f, "f is null");
        Option<LenientValidation<E, U>> validation = value.map(f);
        return of(
            validation
                .map(LenientValidation::getErrors)
                .getOrElse(List.empty())
                .prependAll(errors),
            validation
                .flatMap(LenientValidation::getValue)
        );
    }

    /**
     * Map a checked function f over our value and return a new {@code LenientValidation}
     * built using the result of the application, in case exceptions are thrown,
     * the errorProvider function is called on the exception to return an {@code invalid()}
     * LenientValidation.
     *
     * @param f The checked function to flatMap with
     * @param errorProvider A function to convert an occurred exception to an error
     * @param <U> The new value type
     * @return A {@code LenientValidation} instance
     */
    public <U> LenientValidation<E, U> flatMapTry(
        CheckedFunction1<? super T, LenientValidation<E, U>> f,
        Function1<Throwable, ? extends E> errorProvider)
    {
        Objects.requireNonNull(f, "f is null");
        Objects.requireNonNull(errorProvider, "errorProvider is null");
        return flatMap(value ->
            Try.of(() -> f.apply(value))
                .getOrElseGet(ex -> invalid(List.of(errorProvider.apply(ex))))
        );
    }

    /**
     * Defines a default for the value of this.
     *
     * @param defaultValue A default value
     * @return An instance of {@code LenientValidation} with the same errors and the default value if this is empty
     */
    public LenientValidation<E, T> orElse(T defaultValue) {
        return fromNullable(errors, value.getOrElse(defaultValue));
    }

    /**
     * Defines a default for the value of this.
     *
     * @param supplier A Supplier which produces the default value
     * @return An instance of {@code LenientValidation} with the same errors and the default value if this is empty
     */
    public LenientValidation<E, T> orElse(Supplier<? extends T> supplier) {
        return fromNullable(errors, value.getOrElse(supplier.get()));
    }

    /**
     * Converts this {@code LenientValidation} to an {@link Either}.
     *
     * @return {@code Either.right(get())} if this is valid, otherwise {@code Either.left(getError())}.
     */
    public Either<? super Seq<? extends E>, T> toEither() {
        return isValid() ? Either.right(get()) : Either.left(getErrors());
    }

    /**
     * Whereas map only performs a mapping on the value of a {@code LenientValidation},
     * and mapError performs a mapping on the errors of a {@code LenientValidation},
     * bimap allows to provide mapping actions for both.
     *
     * @param <E2>        type of the mapping result for the error
     * @param <T2>        type of the mapping result for the value
     * @param errorMapper the error mapping operation
     * @param valueMapper the value mapping operation
     * @return an instance of {@code LenientValidation}
     * @throws NullPointerException if errorMapper or valueMapper is null
     */
    public <E2, T2> LenientValidation<E2, T2> bimap(
        Function<? super Seq<? super E>, Seq<E2>> errorMapper,
        Function<Option<? super T>, Option<T2>> valueMapper)
    {
        Objects.requireNonNull(errorMapper, "errorMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return of(errorMapper.apply(errors), valueMapper.apply(value));
    }

    /**
     * Folds this into a single value.
     *
     * @param errorMapper an error mapper
     * @param valueMapper a value mapper
     * @param combiner a function combining the intermediary results from the errorMapper and valueMapper
     * @param <U1> the intermediary result type of mapping the errors
     * @param <U2> the intermediary result type of mapping the value
     * @param <U> the fold result type
     * @return A single value resulting from folding this
     */
    public <U1, U2, U> U fold(
        Function<? super Seq<? super E>, U1> errorMapper,
        Function<? super Option<? super T>, U2> valueMapper,
        Function2<? super U1, ? super U2, U> combiner)
    {
        Objects.requireNonNull(errorMapper, "errorMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        Objects.requireNonNull(combiner, "combiner is null");
        return combiner.apply(errorMapper.apply(errors), valueMapper.apply(value));
    }

    /**
     * Gets the value if it is a Valid or a value calculated from the errors.
     *
     * @param other a function which converts a sequence of errors to an alternative value
     * @return the value, if the underlying {@code LenientValidation} is valid, or else the alternative value
     * provided by {@code other} by applying the errors.
     */
    public T getOrElseGet(Function<? super Seq<? super E>, ? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        if (isValid()) {
            return get();
        } else {
            return other.apply(getErrors());
        }
    }

    /**
     * Creates a {@code LenientValidation} of an {@code Either}.
     *
     * @param either An {@code Either}
     * @param <E> error type
     * @param <T> value type
     * @return An instance of {@code LenientValidation} containing a value if either is a Right, otherwise a singleton list with Left.
     * @throws NullPointerException if either is null
     */
    public static <E, T> LenientValidation<E, T> fromEither(Either<E, T> either) {
        Objects.requireNonNull(either, "either is null");
        return either.isRight() ? valid(either.get()) : invalid(List.of(either.getLeft()));
    }

    /**
     * Creates a {@code LenientValidation} of a {@code Try}.
     *
     * @param t A {@code Try}
     * @param <T> type of the valid value
     * @return An instance of {@code LenientValidation} containing a value if t is a Success, otherwise a singleton list with the exception.
     * @throws NullPointerException if {@code t} is null
     */
    public static <T> LenientValidation<Throwable, T> fromTry(Try<? extends T> t) {
        Objects.requireNonNull(t, "t is null");
        return t.isSuccess() ? valid(t.get()) : invalid(List.of(t.getCause()));
    }

    @Override
    public String toString() {
        return stringPrefix() + "(" + errors + ", " + value + ")";
    }

    @Override
    public T get() throws RuntimeException {
        return getValue().getOrElseThrow(() -> new NoSuchElementException("get of 'invalid' LenientValidation"));
    }

    @Override
    public boolean isEmpty() {
        return getValue().isEmpty();
    }

    /**
     * A {@code LenientValidation}'s value is computed synchronously.
     *
     * @return false
     */
    @Override
    public boolean isAsync() {
        return false;
    }

    /**
     * A {@code LenientValidation}'s value is computed eagerly.
     *
     * @return false
     */
    @Override
    public boolean isLazy() {
        return false;
    }

    /**
     * A {@code LenientValidation}'s value is a single value.
     *
     * @return true
     */
    @Override
    public boolean isSingleValued() {
        return true;
    }

    @Override
    public <U> LenientValidation<E, U> map(Function<? super T, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return of(
            getErrors(),
            getValue().map(f)
        );
    }

    @Override
    public LenientValidation<E, T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        getValue().peek(action);
        return this;
    }

    @Override
    public String stringPrefix() {
        return "LenientValidation";
    }

    @Override
    public Iterator<T> iterator() {
        return getValue().iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LenientValidation<?, ?> that = (LenientValidation<?, ?>) o;
        return Objects.equals(errors, that.errors) &&
            Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errors, value);
    }

    public static final class Builder2<E, T1, T2> {

        private final LenientValidation<E, T1> v1;
        private final LenientValidation<E, T2> v2;

        private Builder2(LenientValidation<E, T1> v1, LenientValidation<E, T2> v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        public <R> LenientValidation<E, R> ap(Function2<T1, T2, R> f) {
            return v2.ap(v1.ap(LenientValidation.valid(f.curried())));
        }

        public <T3> Builder3<E, T1, T2, T3> combine(LenientValidation<E, T3> v3) {
            return new Builder3<>(v1, v2, v3);
        }

    }

    public static final class Builder3<E, T1, T2, T3> {

        private final LenientValidation<E, T1> v1;
        private final LenientValidation<E, T2> v2;
        private final LenientValidation<E, T3> v3;

        private Builder3(LenientValidation<E, T1> v1, LenientValidation<E, T2> v2, LenientValidation<E, T3> v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }

        public <R> LenientValidation<E, R> ap(Function3<T1, T2, T3, R> f) {
            return v3.ap(v2.ap(v1.ap(LenientValidation.valid(f.curried()))));
        }

        public <T4> Builder4<E, T1, T2, T3, T4> combine(LenientValidation<E, T4> v4) {
            return new Builder4<>(v1, v2, v3, v4);
        }

    }

    public static final class Builder4<E, T1, T2, T3, T4> {

        private final LenientValidation<E, T1> v1;
        private final LenientValidation<E, T2> v2;
        private final LenientValidation<E, T3> v3;
        private final LenientValidation<E, T4> v4;

        private Builder4(
            LenientValidation<E, T1> v1,
            LenientValidation<E, T2> v2,
            LenientValidation<E, T3> v3,
            LenientValidation<E, T4> v4)
        {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
        }

        public <R> LenientValidation<E, R> ap(Function4<T1, T2, T3, T4, R> f) {
            return v4.ap(v3.ap(v2.ap(v1.ap(LenientValidation.valid(f.curried())))));
        }

        public <T5> Builder5<E, T1, T2, T3, T4, T5> combine(LenientValidation<E, T5> v5) {
            return new Builder5<>(v1, v2, v3, v4, v5);
        }

    }

    public static final class Builder5<E, T1, T2, T3, T4, T5> {

        private final LenientValidation<E, T1> v1;
        private final LenientValidation<E, T2> v2;
        private final LenientValidation<E, T3> v3;
        private final LenientValidation<E, T4> v4;
        private final LenientValidation<E, T5> v5;

        private Builder5(
            LenientValidation<E, T1> v1,
            LenientValidation<E, T2> v2,
            LenientValidation<E, T3> v3,
            LenientValidation<E, T4> v4,
            LenientValidation<E, T5> v5)
        {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
        }

        public <R> LenientValidation<E, R> ap(Function5<T1, T2, T3, T4, T5, R> f) {
            return v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(LenientValidation.valid(f.curried()))))));
        }

        public <T6> Builder6<E, T1, T2, T3, T4, T5, T6> combine(LenientValidation<E, T6> v6) {
            return new Builder6<>(v1, v2, v3, v4, v5, v6);
        }

    }

    public static final class Builder6<E, T1, T2, T3, T4, T5, T6> {

        private final LenientValidation<E, T1> v1;
        private final LenientValidation<E, T2> v2;
        private final LenientValidation<E, T3> v3;
        private final LenientValidation<E, T4> v4;
        private final LenientValidation<E, T5> v5;
        private final LenientValidation<E, T6> v6;

        private Builder6(
            LenientValidation<E, T1> v1,
            LenientValidation<E, T2> v2,
            LenientValidation<E, T3> v3,
            LenientValidation<E, T4> v4,
            LenientValidation<E, T5> v5,
            LenientValidation<E, T6> v6)
        {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
        }

        public <R> LenientValidation<E, R> ap(Function6<T1, T2, T3, T4, T5, T6, R> f) {
            return v6.ap(v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(LenientValidation.valid(f.curried())))))));
        }

        public <T7> Builder7<E, T1, T2, T3, T4, T5, T6, T7> combine(LenientValidation<E, T7> v7) {
            return new Builder7<>(v1, v2, v3, v4, v5, v6, v7);
        }

    }

    public static final class Builder7<E, T1, T2, T3, T4, T5, T6, T7> {

        private final LenientValidation<E, T1> v1;
        private final LenientValidation<E, T2> v2;
        private final LenientValidation<E, T3> v3;
        private final LenientValidation<E, T4> v4;
        private final LenientValidation<E, T5> v5;
        private final LenientValidation<E, T6> v6;
        private final LenientValidation<E, T7> v7;

        private Builder7(
            LenientValidation<E, T1> v1,
            LenientValidation<E, T2> v2,
            LenientValidation<E, T3> v3,
            LenientValidation<E, T4> v4,
            LenientValidation<E, T5> v5,
            LenientValidation<E, T6> v6,
            LenientValidation<E, T7> v7)
        {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
        }

        public <R> LenientValidation<E, R> ap(Function7<T1, T2, T3, T4, T5, T6, T7, R> f) {
            return v7.ap(v6.ap(v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(LenientValidation.valid(f.curried()))))))));
        }

        public <T8> Builder8<E, T1, T2, T3, T4, T5, T6, T7, T8> combine(LenientValidation<E, T8> v8) {
            return new Builder8<>(v1, v2, v3, v4, v5, v6, v7, v8);
        }

    }

    public static final class Builder8<E, T1, T2, T3, T4, T5, T6, T7, T8> {

        private final LenientValidation<E, T1> v1;
        private final LenientValidation<E, T2> v2;
        private final LenientValidation<E, T3> v3;
        private final LenientValidation<E, T4> v4;
        private final LenientValidation<E, T5> v5;
        private final LenientValidation<E, T6> v6;
        private final LenientValidation<E, T7> v7;
        private final LenientValidation<E, T8> v8;

        private Builder8(
            LenientValidation<E, T1> v1,
            LenientValidation<E, T2> v2,
            LenientValidation<E, T3> v3,
            LenientValidation<E, T4> v4,
            LenientValidation<E, T5> v5,
            LenientValidation<E, T6> v6,
            LenientValidation<E, T7> v7,
            LenientValidation<E, T8> v8)
        {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
        }

        public <R> LenientValidation<E, R> ap(Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> f) {
            return v8.ap(v7.ap(v6.ap(v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(LenientValidation.valid(f.curried())))))))));
        }

    }

}