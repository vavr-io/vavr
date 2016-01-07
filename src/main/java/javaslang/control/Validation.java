/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

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
 * Validation.Success:
 * Validation&lt;List&lt;String&gt;,Integer&gt; valS = Validation.success(5);
 *
 * Validation.Failure:
 * Validation&lt;List&lt;String&gt;,Integer&gt; valE = Validation.failure(List.of("error1","error2"));
 * </code>
 * </pre>
 *
 * @param <E> Value type in the case of failure.
 * @param <T> Value type in the case of success.
 * @author Eric Nelson
 * @since 2.0.1
 * @see <a href="http://eed3si9n.com/learning-scalaz/Validation.html">Validation</a>
 */
public interface Validation<E,T> extends Kind<Validation<E,?>, T>, Applicative<Validation<E, ?>, T> {

    static <E,T> Validation<E,T> success(T value) {
        return new Valid<>(value);
    }

    static <E,T> Validation<E,T> success(Supplier<? extends T> supplier) {
        return new Valid<>(supplier.get());
    }

    static <E,T> Validation<E,T> failure(E error) {
        return new Invalid<>(error);
    }

    static <E,T> Validation<E,T> failure(Supplier<? extends E> supplier) {
        return new Invalid<>(supplier.get());
    }

    static <E,T1,T2> ValidationBuilder<E,T1,T2> map2(Validation<E,T1> v1, Validation<E,T2> v2) {
        return new ValidationBuilder<>(v1, v2);
    }

    static <E,T1,T2,T3> ValidationBuilder.ValidationBuilder3<E,T1,T2,T3> map3(Validation<E,T1> v1, Validation<E,T2> v2, Validation<E,T3> v3) {
        return new ValidationBuilder.ValidationBuilder3<>(v1, v2, v3);
    }

    boolean isSuccess();

    boolean isFailure();

    Object get();

    void forEach(Consumer<? super T> f);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    default <U> U fold(Function<? super E,? extends U> fail, Function<? super T,? extends U> success) {
        if(isFailure()) {
            E v = ((Invalid<E,T>) this).get();
            return fail.apply(v);
        } else {
            T v = ((Valid<E,T>) this).get();
            return success.apply(v);
        }
    }

    default Validation<T,E> swap() {
        if(isFailure()) {
            E v = ((Invalid<E,T>) this).get();
            return Validation.success(v);
        } else {
            T v = ((Valid<E,T>) this).get();
            return Validation.failure(v);
        }
    }

    @Override
    default <U> Validation<E,U> map(Function<? super T,? extends U> f) {
        if(isFailure()) {
            return (Invalid<E,U>) this;
        } else {
            T v = ((Valid<E,T>) this).get();
            return Validation.success(f.apply(v));
        }
    }

    default <U,R> Validation<U,R> bimap(Function<? super E,? extends U> fail, Function<? super T,? extends R> success) {
        if(isFailure()) {
            E v = ((Invalid<E,T>) this).get();
            return Validation.failure(fail.apply(v));
        } else {
            T v = ((Valid<E,T>) this).get();
            return Validation.success(success.apply(v));
        }
    }

    default <U> Validation<U,T> leftMap(Function<? super E,? extends U> f) {
        if(isFailure()) {
            E v = ((Invalid<E,T>) this).get();
            return Validation.failure(f.apply(v));
        } else {
            return (Valid<U,T>) this;
        }
    }

    default <U> Validation<E,U> ap(Validation<E,? extends Function<? super T,? extends U>> v) {
        if(isSuccess() && v.isSuccess()) {
            return success(() -> {
                Function<? super T,? extends U> f = ((Valid<E,? extends Function<? super T,? extends U>>) v).get();
                return f.apply(((Valid<E,T>)this).get());
            });
        } else if(isSuccess() && v.isFailure()) {
            return (Invalid<E,U>) v;
        } else if(isFailure() && v.isSuccess()) {
            E e = ((Invalid<E,T>) this).get();
            return failure(e);
        } else {
            return failure(() -> {
                E e = ((Invalid<E,T>) this).get();
                return e;
            });
        }
    }

    @Override
    default <U> Validation<E,U> ap(Kind<Validation<E, ?>, ? extends Function<? super T, ? extends U>> f) {
        return ap((Validation<E,? extends Function<? super T,? extends U>>) ((Object) f));
    }

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
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isFailure() {
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
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Valid && Objects.equals(value, ((Valid<?,?>) obj).value));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "Success(" + value + ")";
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
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public void forEach(Consumer<? super T> f) {
            // Do nothing if Failure
        }

        @Override
        public E get() {
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
            return "Failure(" + error + ")";
        }

    }

}
