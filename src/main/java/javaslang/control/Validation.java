/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.*;
import javaslang.algebra.Applicative;
import javaslang.collection.List;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An implementation similar to scalaz's <a href="http://eed3si9n.com/learning-scalaz/Validation.html">Validation</a> control.
 *
 * <p>
 * The Validation type is different from a Monad type, it is an applicative
 * functor. Whereas a Monad will short circuit after the first error, the
 * applicative functor will continue on, accumulating ALL errors. This is
 * especially helpful in cases such as validation, where you want to know
 * all the validation errors that have occurred, not just the first one.
 * </p>
 *
 * <pre>
 * <code>
 * Validation.Valid:
 * Validation&lt;List&lt;String&gt;,Integer&gt; valS = Validation.valid(5);
 *
 * Validation.Invalid:
 * Validation&lt;List&lt;String&gt;,Integer&gt; valE = Validation.invalid(List.of("error1","error2"));
 * </code>
 * </pre>
 *
 * @param <E> Value type in the case of invalid.
 * @param <T> Value type in the case of valid.
 * @author Eric Nelson
 * @since 2.0.0
 * @see <a href="http://eed3si9n.com/learning-scalaz/Validation.html">Validation</a>
 */
public interface Validation<E,T> extends Kind2<Validation<?,?>, E, T>, Applicative<Validation<?,?>, E, T> {

    static <E,T> Validation<E,T> valid(T value) {
        Objects.requireNonNull(value, "value is null");
        return new Valid<>(value);
    }

    static <E,T> Validation<E,T> valid(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return new Valid<>(supplier.get());
    }

    static <E,T> Validation<E,T> invalid(E error) {
        Objects.requireNonNull(error, "error is null");
        return new Invalid<>(error);
    }

    static <E,T> Validation<E,T> invalid(Supplier<? extends E> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return new Invalid<>(supplier.get());
    }

    static <E,T1,T2> Builder<E,T1,T2> map2(Validation<E,T1> validation1, Validation<E,T2> validation2) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        return new Builder<>(validation1, validation2);
    }

    static <E,T1,T2,T3> Builder3<E,T1,T2,T3> map3(Validation<E,T1> validation1, Validation<E,T2> validation2, Validation<E,T3> validation3) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        return new Builder3<>(validation1, validation2, validation3);
    }

    static <E,T1,T2,T3,T4> Builder4<E,T1,T2,T3,T4> map4(Validation<E,T1> validation1, Validation<E,T2> validation2, Validation<E,T3> validation3, Validation<E,T4> validation4) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        return new Builder4<>(validation1, validation2, validation3, validation4);
    }

    static <E,T1,T2,T3,T4,T5> Builder5<E,T1,T2,T3,T4,T5> map5(Validation<E,T1> validation1, Validation<E,T2> validation2, Validation<E,T3> validation3, Validation<E,T4> validation4, Validation<E,T5> validation5) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        return new Builder5<>(validation1, validation2, validation3, validation4, validation5);
    }

    static <E,T1,T2,T3,T4,T5,T6> Builder6<E,T1,T2,T3,T4,T5,T6> map6(Validation<E,T1> validation1, Validation<E,T2> validation2, Validation<E,T3> validation3, Validation<E,T4> validation4, Validation<E,T5> validation5, Validation<E,T6> validation6) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        Objects.requireNonNull(validation6, "validation6 is null");
        return new Builder6<>(validation1, validation2, validation3, validation4, validation5, validation6);
    }

    static <E,T1,T2,T3,T4,T5,T6,T7> Builder7<E,T1,T2,T3,T4,T5,T6,T7> map7(Validation<E,T1> validation1, Validation<E,T2> validation2, Validation<E,T3> validation3, Validation<E,T4> validation4, Validation<E,T5> validation5, Validation<E,T6> validation6, Validation<E,T7> validation7) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        Objects.requireNonNull(validation6, "validation6 is null");
        Objects.requireNonNull(validation7, "validation7 is null");
        return new Builder7<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7);
    }

    static <E,T1,T2,T3,T4,T5,T6,T7,T8> Builder8<E,T1,T2,T3,T4,T5,T6,T7,T8> map8(Validation<E,T1> validation1, Validation<E,T2> validation2, Validation<E,T3> validation3, Validation<E,T4> validation4, Validation<E,T5> validation5, Validation<E,T6> validation6, Validation<E,T7> validation7, Validation<E,T8> validation8) {
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

    boolean isValid();

    boolean isInvalid();

    T get();

    E getError();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    default void forEach(Consumer<? super T> f) {
        Objects.requireNonNull(f, "function f is null");
        if(isValid())
            f.accept(get());
    }

    default <U> U fold(Function<? super E,? extends U> fInvalid, Function<? super T,? extends U> fValid) {
        Objects.requireNonNull(fInvalid, "function fInvalid null");
        Objects.requireNonNull(fValid, "function fValid null");
        if(isInvalid()) {
            E error = this.getError();
            return fInvalid.apply(error);
        } else {
            T value = this.get();
            return fValid.apply(value);
        }
    }

    default Validation<T,E> swap() {
        if(isInvalid()) {
            E error = this.getError();
            return Validation.valid(error);
        } else {
            T value = this.get();
            return Validation.invalid(value);
        }
    }

    @Override
    default <U> Validation<E,U> map(Function<? super T, ? extends U> f) {
        Objects.requireNonNull(f, "function f is null");
        if(isInvalid()) {
            return Validation.invalid(this.getError());
        } else {
            T value = this.get();
            return Validation.valid(f.apply(value));
        }
    }

    default <U,R> Validation<U,R> bimap(Function<? super E,? extends U> invalidMapper, Function<? super T,? extends R> validMapper) {
        Objects.requireNonNull(invalidMapper, "function invalidMapper is null");
        Objects.requireNonNull(validMapper, "function validMapper is null");
        if(isInvalid()) {
            E error = this.getError();
            return Validation.invalid(invalidMapper.apply(error));
        } else {
            T value = this.get();
            return Validation.valid(validMapper.apply(value));
        }
    }

    default <U> Validation<U,T> leftMap(Function<? super E,? extends U> f) {
        Objects.requireNonNull(f, "function f is null");
        if(isInvalid()) {
            E error = this.getError();
            return Validation.invalid(f.apply(error));
        } else {
            return Validation.valid(this.get());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    default <U> Validation<List<E>,U> ap(Kind2<Validation<?, ?>, List<E>, ? extends Function<? super T, ? extends U>> kind) {
        Objects.requireNonNull(kind, "kind is null");
        Validation<List<E>, ? extends Function<? super T,? extends U>> validation = (Validation<List<E>,? extends Function<? super T,? extends U>>) ((Object) kind);

        if(isValid() && validation.isValid()) {
            return valid(() -> {
                Function<? super T,? extends U> f = validation.get();
                return f.apply(this.get());
            });
        } else if(isValid() && validation.isInvalid()) {
            List<E> errors = validation.getError();
            return invalid(errors);
        } else if(isInvalid() && validation.isValid()) {
            E error = this.getError();
            return invalid(List.of(error));
        } else {
            return invalid(() -> {
                List<E> errors = validation.getError();
                E error = this.getError();
                return errors.append(error);
            });
        }
    }

    default <U> Builder<E,T,U> combine(Validation<E,U> validation) {
        return new Builder<>(this, validation);
    }


    final class Valid<E,T> implements Validation<E,T> {

        private final T value;

        /**
         * Construct a {@code Valid}
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
            return (obj == this) || (obj instanceof Valid && Objects.equals(value, ((Valid<?,?>) obj).value));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "Valid(" + value + ")";
        }

    }

    final class Invalid<E,T> implements Validation<E,T> {

        private final E error;

        /**
         * Construct an {@code Invalid}
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
            return (obj == this) || (obj instanceof Invalid && Objects.equals(error, ((Invalid<?,?>) obj).error));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(error);
        }

        @Override
        public String toString() {
            return "Invalid(" + error + ")";
        }

    }

    final class Builder<E,T1,T2> {

        private Validation<E,T1> v1;
        private Validation<E,T2> v2;

        private Builder(Validation<E,T1> v1, Validation<E,T2> v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        public <R> Validation<List<E>,R> ap(Function2<T1,T2,R> f) {
            return v2.ap(v1.ap(Validation.valid(f.curried())));
        }

        public <T3> Builder3<E,T1,T2,T3> combine(Validation<E,T3> v3) {
            return new Builder3<>(v1, v2, v3);
        }

    }

    final class Builder3<E,T1,T2,T3> {

        private Validation<E,T1> v1;
        private Validation<E,T2> v2;
        private Validation<E,T3> v3;

        private Builder3(Validation<E,T1> v1, Validation<E,T2> v2, Validation<E,T3> v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }

        public <R> Validation<List<E>,R> ap(Function3<T1,T2,T3,R> f) {
            return v3.ap(v2.ap(v1.ap(Validation.valid(f.curried()))));
        }

        public <T4> Builder4<E,T1,T2,T3,T4> combine(Validation<E,T4> v4) {
            return new Builder4<>(v1, v2, v3, v4);
        }

    }

    final class Builder4<E,T1,T2,T3,T4> {

        private Validation<E,T1> v1;
        private Validation<E,T2> v2;
        private Validation<E,T3> v3;
        private Validation<E,T4> v4;

        private Builder4(Validation<E,T1> v1, Validation<E,T2> v2, Validation<E,T3> v3, Validation<E,T4> v4) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
        }

        public <R> Validation<List<E>,R> ap(Function4<T1,T2,T3,T4,R> f) {
            return v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(f.curried())))));
        }

        public <T5> Builder5<E,T1,T2,T3,T4,T5> combine(Validation<E,T5> v5) {
            return new Builder5<>(v1, v2, v3, v4, v5);
        }

    }

    final class Builder5<E,T1,T2,T3,T4,T5> {

        private Validation<E,T1> v1;
        private Validation<E,T2> v2;
        private Validation<E,T3> v3;
        private Validation<E,T4> v4;
        private Validation<E,T5> v5;

        private Builder5(Validation<E,T1> v1, Validation<E,T2> v2, Validation<E,T3> v3, Validation<E,T4> v4, Validation<E,T5> v5) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
        }

        public <R> Validation<List<E>,R> ap(Function5<T1,T2,T3,T4,T5,R> f) {
            return v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(f.curried()))))));
        }

        public <T6> Builder6<E,T1,T2,T3,T4,T5,T6> combine(Validation<E,T6> v6) {
            return new Builder6<>(v1, v2, v3, v4, v5, v6);
        }

    }

    final class Builder6<E,T1,T2,T3,T4,T5,T6> {

        private Validation<E,T1> v1;
        private Validation<E,T2> v2;
        private Validation<E,T3> v3;
        private Validation<E,T4> v4;
        private Validation<E,T5> v5;
        private Validation<E,T6> v6;

        private Builder6(Validation<E,T1> v1, Validation<E,T2> v2, Validation<E,T3> v3, Validation<E,T4> v4, Validation<E,T5> v5, Validation<E,T6> v6) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
        }

        public <R> Validation<List<E>,R> ap(Function6<T1,T2,T3,T4,T5,T6,R> f) {
            return v6.ap(v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(f.curried())))))));
        }

        public <T7> Builder7<E,T1,T2,T3,T4,T5,T6,T7> combine(Validation<E,T7> v7) {
            return new Builder7<>(v1, v2, v3, v4, v5, v6, v7);
        }

    }

    final class Builder7<E,T1,T2,T3,T4,T5,T6,T7> {

        private Validation<E,T1> v1;
        private Validation<E,T2> v2;
        private Validation<E,T3> v3;
        private Validation<E,T4> v4;
        private Validation<E,T5> v5;
        private Validation<E,T6> v6;
        private Validation<E,T7> v7;

        private Builder7(Validation<E,T1> v1, Validation<E,T2> v2, Validation<E,T3> v3, Validation<E,T4> v4, Validation<E,T5> v5, Validation<E,T6> v6, Validation<E,T7> v7) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
        }

        public <R> Validation<List<E>,R> ap(Function7<T1,T2,T3,T4,T5,T6,T7,R> f) {
            return v7.ap(v6.ap(v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(f.curried()))))))));
        }

        public <T8> Builder8<E,T1,T2,T3,T4,T5,T6,T7,T8> combine(Validation<E,T8> v8) {
            return new Builder8<>(v1, v2, v3, v4, v5, v6, v7, v8);
        }

    }

    final class Builder8<E,T1,T2,T3,T4,T5,T6,T7,T8> {

        private Validation<E,T1> v1;
        private Validation<E,T2> v2;
        private Validation<E,T3> v3;
        private Validation<E,T4> v4;
        private Validation<E,T5> v5;
        private Validation<E,T6> v6;
        private Validation<E,T7> v7;
        private Validation<E,T8> v8;

        private Builder8(Validation<E,T1> v1, Validation<E,T2> v2, Validation<E,T3> v3, Validation<E,T4> v4, Validation<E,T5> v5, Validation<E,T6> v6, Validation<E,T7> v7, Validation<E,T8> v8) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
        }

        public <R> Validation<List<E>,R> ap(Function8<T1,T2,T3,T4,T5,T6,T7,T8,R> f) {
            return v8.ap(v7.ap(v6.ap(v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(f.curried())))))))));
        }

    }

}
