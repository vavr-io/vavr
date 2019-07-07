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

    public static <E, T> LenientValidation<E, T> of(Iterable<E> errors, Option<T> value) {
        return new LenientValidation<>(errors, value);
    }

    public static <E, T> LenientValidation<E, T> fromNullable(Iterable<E> errors, T value) {
        return new LenientValidation<>(errors, Option.of(value));
    }

    public static <E, T> LenientValidation<E, T> valid(T value) {
        return of(List.empty(), Option.some(value));
    }

    public static <E, T> LenientValidation<E, T> invalid(Iterable<E> errors) {
        return fromNullable(errors, null);
    }

    public static <E, T> LenientValidation<E, T> empty() {
        return fromNullable(List.empty(), null);
    }

    public static <E, T> LenientValidation<E, T> fromMultiErrorValidation(Validation<Seq<E>, T> validation) {
        Objects.requireNonNull(validation, "validation is null");
        return fromNullable(
            validation.isInvalid() ? validation.getError() : List.empty(),
            validation.getOrElse((T) null)
        );
    }

    public static <E, T> LenientValidation<E, T> fromValidation(Validation<E, T> validation) {
        Objects.requireNonNull(validation, "validation is null");
        return fromMultiErrorValidation(
            validation.mapError(List::of)
        );
    }

    /**
     * Converts this into a strict {@code Validation} that either holds errors or a value.
     *
     * @return A {@code Valid} if this holds a value and there are no errors, otherwise an {@code Invalid} with the errors
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
     * Narrows a widened {@code LenientValidation<? extends E, ? extends T>} to {@code LenientValidation<E, T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
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

    public <U> LenientValidation<E, U> ap(LenientValidation<E, ? extends Function<? super T, ? extends U>> validation) {
        Objects.requireNonNull(validation, "validation is null");
        return of(
            validation.getErrors().prependAll(errors),
            validation.getValue().flatMap(value::map)
        );
    }

    /**
     * Combines two {@code LenientValidation}s to form a {@link Builder2}, which can then be used to perform further
     * combines, or apply a function to it in order to transform the {@link Builder2} into a {@code LenientValidation}.
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
     * @return an instance of {@code LenientValidation} with the same errors as this and the value of this if the predicate evaluates to true
     * @throws NullPointerException if predicate is null
     */
    public LenientValidation<E, T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return of(
            getErrors(),
            getValue().filter(predicate)
        );
    }

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

    public <U> LenientValidation<E, U> flatMapTry(CheckedFunction1<? super T, LenientValidation<E, U>> f, Function1<Throwable, ? extends E> errorProvider) {
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