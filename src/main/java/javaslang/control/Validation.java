/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Function1;
import javaslang.algebra.Functor;
import javaslang.collection.List;

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
public interface Validation<E,T> extends Functor<T> {

    static <E,T> Validation<E,T> success(T value) {
        return new Success<>(value);
    }

    static <E,T> Validation<E,T> success(Supplier<T> supplier) {
        return new Success<>(supplier.get());
    }

    static <E,T> Validation<E,T> failure(E error) {
        return new Failure<>(error);
    }

    static <E,T> Validation<E,T> failure(Supplier<E> supplier) {
        return new Failure<>(supplier.get());
    }

    boolean isSuccess();

    boolean isFailure();

    Object get();

    void foreach(Consumer<T> f);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    default <U> U fold(Function<? super E,? extends U> fail, Function<? super T,? extends U> success) {
        if(isFailure()) {
            E v = ((Failure<E,T>) this).get();
            return fail.apply(v);
        } else {
            T v = ((Success<E,T>) this).get();
            return success.apply(v);
        }
    }

    default Validation<T,E> swap() {
        if(isFailure()) {
            E v = ((Failure<E,T>) this).get();
            return Validation.success(v);
        } else {
            T v = ((Success<E,T>) this).get();
            return Validation.failure(v);
        }
    }

    @Override
    default <U> Validation<E,U> map(Function<? super T,? extends U> mapper) {
        if(isFailure()) {
            E v = ((Failure<E,T>) this).get();
            return Validation.failure(v);
        } else {
            T v = ((Success<E,T>) this).get();
            return Validation.success(mapper.apply(v));
        }
    }

    default <U,R> Validation<U,R> bimap(Function<? super E,? extends U> fail, Function<? super T,? extends R> success) {
        if(isFailure()) {
            E v = ((Failure<E,T>) this).get();
            return Validation.failure(fail.apply(v));
        } else {
            T v = ((Success<E,T>) this).get();
            return Validation.success(success.apply(v));
        }
    }

    default <U> Validation<U,T> leftMap(Function<? super E,? extends U> f) {
        if(isFailure()) {
            E v = ((Failure<E,T>) this).get();
            return Validation.failure(f.apply(v));
        } else {
            T v = ((Success<E,T>) this).get();
            return Validation.success(v);
        }
    }

    default <U> Validation<List<E>,U> ap(Validation<List<E>,Function1<T,U>> vv) {
        if(isSuccess() && vv.isSuccess()) {
            return success(() -> {
                Function<T,U> f = ((Success<List<E>,Function1<T,U>>) vv).get();
                return f.apply(((Success<E,T>)this).get());
            });
        } else if(isSuccess() && vv.isFailure()) {
            return (Failure<List<E>,U>) vv;
        } else if(isFailure() && vv.isSuccess()) {
            E e = ((Failure<E,T>) this).get();
            return failure(List.of(e));
        } else {
            return failure(() -> {
                List<E> e  = ((Failure<List<E>,Function1<T,U>>) vv).get();
                E  e2 = ((Failure<E,T>) this).get();
                return e.append(e2);
            });
        }
    }

    default <U> ValidationBuilder<E,T,U> bld(Validation<E,U> v) {
        return new ValidationBuilder<>(this, v);
    }


    final class Success<E,T> implements Validation<E,T> {

        private final T value;

        /**
         * Construct a Success
         * @param value The value of this success
         */
        public Success(T value) {
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
        public void foreach(Consumer<T> f) {
            f.accept(get());
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Success && Objects.equals(value, ((Success<?,?>) obj).value));
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

    final class Failure<E,T> implements Validation<E,T> {

        private final E error;

        /**
         * Construct a Failure
         * @param error The value of this error
         */
        public Failure(E error) {
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
        public void foreach(Consumer<T> f) {
            // Do nothing if Failure
        }

        @Override
        public E get() {
            return error;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Failure && Objects.equals(error, ((Failure<?,?>) obj).error));
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
