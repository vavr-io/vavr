/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Tuple;
import javaslang.Tuple1;
import javaslang.algebra.HigherKinded;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Success<T> implements Try<T> {

    private static final long serialVersionUID = 9157097743377386892L;

    private T value;

    public Success(T value) {
        this.value = value;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public T orElse(T other) {
        return value;
    }

    @Override
    public T orElseGet(Function<? super Throwable, ? extends T> other) {
        return value;
    }

    @Override
    public void orElseRun(Consumer<? super Throwable> action) {
        // nothing to do
    }

    @Override
    public <X extends Throwable> T orElseThrow(Function<? super Throwable, X> exceptionProvider) throws X {
        return value;
    }

    @Override
    public Try<T> recover(Function<Throwable, ? extends T> f) {
        return this;
    }

    @Override
    public Try<T> recoverWith(Function<Throwable, Try<T>> f) {
        return this;
    }

    @Override
    public Option<T> toOption() {
        return new Some<>(value);
    }

    @Override
    public Either<Throwable, T> toEither() {
        return new Right<>(value);
    }

    @Override
    public Try<T> filter(Predicate<? super T> predicate) {
        try {
            if (predicate.test(value)) {
                return this;
            } else {
                return new Failure<>(new NoSuchElementException("Predicate does not hold for " + value));
            }
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Override
    public Try<Throwable> failed() {
        return new Failure<>(new UnsupportedOperationException("Success.failed()"));
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        action.accept(value);
    }

    @Override
    public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        try {
            return new Success<>(mapper.apply(value));
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U, TRY extends HigherKinded<U, Try<?>>> Try<U> flatMap(Function<? super T, TRY> mapper) {
        try {
            return (Try<U>) mapper.apply(value);
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Override
    public Tuple1<T> unapply() {
        return Tuple.of(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Success)) {
            return false;
        }
        final Success<?> success = (Success<?>) obj;
        return Objects.equals(value, success.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return String.format("Success(%s)", value);
    }
}
