/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.CheckedFunction1;
import javaslang.Value;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A succeeded Try.
 *
 * @param <T> component type of this Success
 * @since 1.0.0
 */
public final class Success<T> implements Try<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private final T value;

    /**
     * Constructs a Failure.
     *
     * @param value A value
     */
    public Success(T value) {
        this.value = value;
    }

    @Override
    public boolean isEmpty() {
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
    public Failure.NonFatal getCause() {
        throw new UnsupportedOperationException("getCause on Success");
    }

    @Override
    public Success<T> recover(Function<Throwable, ? extends T> f) {
        return this;
    }

    @Override
    public Success<T> recoverWith(Function<Throwable, Try<T>> f) {
        return this;
    }

    @Override
    public Success<T> onFailure(Consumer<Throwable> f) {
        return this;
    }

    @Override
    public Failure<Throwable> failed() {
        return new Failure<>(new UnsupportedOperationException("Success.failed()"));
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
    public Try<T> filterTry(CheckedPredicate<? super T> predicate) {
        return Try.of(() -> predicate.test(value)).flatMap(b -> filter(ignored -> b));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Try<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
        return flatMapTry((CheckedFunction<T, java.lang.Iterable<? extends U>>) mapper::apply);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Try<U> flatMapTry(CheckedFunction<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
        try {
            final Iterable<? extends U> iterable = mapper.apply(value);
            if (iterable instanceof Value) {
                return ((Value<U>) iterable).toTry();
            } else {
                return Try.of(() -> Value.get(iterable));
            }
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Override
    public Try<Object> flatten() {
        return flatMap(value -> (value instanceof Try) ? ((Try<?>) value).flatten() : this);
    }

    @Override
    public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        try {
            return new Success<>(mapper.apply(value));
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Override
    public <U> Try<U> mapTry(CheckedFunction1<? super T, ? extends U> f) {
        return Try.of(() -> f.apply(value));
    }

    @Override
    public Try<T> peek(Consumer<? super T> action) {
        try {
            action.accept(value);
            return this;
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof Success && Objects.equals(value, ((Success<?>) obj).value));
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