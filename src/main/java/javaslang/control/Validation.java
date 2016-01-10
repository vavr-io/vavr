/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

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
 * @since 2.0.1
 * @see <a href="http://eed3si9n.com/learning-scalaz/Validation.html">Validation</a>
 */
public interface Validation<E,T> extends Kind<Validation<?,?>, E, T>, Applicative<Validation<?,?>, E, T> {

    static <E,T> Validation<E,T> valid(T value) {
        return new Valid<>(value);
    }

    static <E,T> Validation<E,T> valid(Supplier<? extends T> supplier) {
        return new Valid<>(supplier.get());
    }

    static <E,T> Validation<E,T> invalid(E error) {
        return new Invalid<>(error);
    }

    static <E,T> Validation<E,T> invalid(Supplier<? extends E> supplier) {
        return new Invalid<>(supplier.get());
    }

    static <E,T1,T2> ValidationBuilder<E,T1,T2> map2(Validation<E,T1> v1, Validation<E,T2> v2) {
        return new ValidationBuilder<>(v1, v2);
    }

    static <E,T1,T2,T3> ValidationBuilder.ValidationBuilder3<E,T1,T2,T3> map3(Validation<E,T1> v1, Validation<E,T2> v2, Validation<E,T3> v3) {
        return new ValidationBuilder.ValidationBuilder3<>(v1, v2, v3);
    }

    boolean isValid();

    boolean isInvalid();

    T get();

    E error();

    void forEach(Consumer<? super T> f);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    default <U> U fold(Function<? super E,? extends U> fail, Function<? super T,? extends U> success) {
        if(isInvalid()) {
            E v = this.error();
            return fail.apply(v);
        } else {
            T v = this.get();
            return success.apply(v);
        }
    }

    default Validation<T,E> swap() {
        if(isInvalid()) {
            E v = this.error();
            return Validation.valid(v);
        } else {
            T v = this.get();
            return Validation.invalid(v);
        }
    }

    @Override
    default <U> Validation<E,U> map(Function<? super T, ? extends U> f) {
        if(isInvalid()) {
            return (Invalid<E,U>) this;
        } else {
            T v = this.get();
            return Validation.valid(f.apply(v));
        }
    }

    default <U,R> Validation<U,R> bimap(Function<? super E,? extends U> fail, Function<? super T,? extends R> success) {
        if(isInvalid()) {
            E v = this.error();
            return Validation.invalid(fail.apply(v));
        } else {
            T v = this.get();
            return Validation.valid(success.apply(v));
        }
    }

    default <U> Validation<U,T> leftMap(Function<? super E,? extends U> f) {
        if(isInvalid()) {
            E v = this.error();
            return Validation.invalid(f.apply(v));
        } else {
            return (Valid<U,T>) this;
        }
    }

    default <U> Validation<List<E>,U> ap(Validation<List<E>, ? extends Function<? super T,? extends U>> v) {
        if(isValid() && v.isValid()) {
            return valid(() -> {
                Function<? super T,? extends U> f = ((Valid<E,? extends Function<? super T,? extends U>>) v).get();
                return f.apply(this.get());
            });
        } else if(isValid() && v.isInvalid()) {
            List<E> errors = v.error();
            return invalid(errors);
        } else if(isInvalid() && v.isValid()) {
            E error = this.error();
            return invalid(List.of(error));
        } else {
            return invalid(() -> {
                List<E> errors = v.error();
                E e = this.error();
                return errors.append(e);
            });
        }
    }

    @Override
    default <U> Validation<List<E>,U> ap(Kind<Validation<?, ?>, List<E>, ? extends Function<? super T, ? extends U>> f) {
        return ap((Validation<List<E>,? extends Function<? super T,? extends U>>) ((Object) f));
    }

    //    @Override
//    default <U> Validation<E,U> ap(Kind<Validation<E, ?>, ? extends Function<? super T, ? extends U>> f) {
//        return ap((Validation<E,? extends Function<? super T,? extends U>>) ((Object) f));
//    }

    default <U> ValidationBuilder<E,T,U> combine(Validation<E,U> v) {
        return new ValidationBuilder<>(this, v);
    }


    final class Valid<E,T> implements Validation<E,T> {

        private final T value;

        /**
         * Construct a {@code Valid}
         * @param value The value of this success
         */
        public Valid(T value) {
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
        public void forEach(Consumer<? super T> f) {
            f.accept(get());
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public E error() throws RuntimeException {
            throw new NoSuchElementException("errors of 'valid' Validation");
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
        public Invalid(E error) {
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
        public void forEach(Consumer<? super T> f) {
            // Do nothing if Invalid
        }

        @Override
        public T get() throws RuntimeException {
            throw new NoSuchElementException("get of 'invalid' Validation");
        }

        @Override
        public E error() {
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

}
