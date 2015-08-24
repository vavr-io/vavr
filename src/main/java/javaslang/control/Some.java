/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Value;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Some represents a defined {@link javaslang.control.Option}. It contains a value which may be null. However, to
 * create an Option containing null, {@code new Some(null)} has to be called. In all other cases
 * {@link Option#of(Object)} is sufficient.
 *
 * @param <T> The type of the optional value.
 * @since 1.0.0
 */
public final class Some<T> implements Option<T>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The singleton instance of {@code Some<Void>}.
     */
    private static final Some<Void> NOTHING = new Some<>(null);

    private final T value;

    /**
     * Creates a new Some containing the given value.
     *
     * @param value A value, may be null
     */
    public Some(T value) {
        this.value = value;
    }

    /**
     * Return the singleton instance of {@code Some<Void>}.
     *
     * @return {@link #NOTHING}
     */
    public static Some<Void> nothing() {
        return NOTHING;
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
    public Option<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return predicate.test(value) ? this : None.instance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Option<U> flatMap(Function<? super T, ? extends Value<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return (Option<U>) mapper.apply(value).toOption();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Option<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper) {
        return flatMap(mapper);
    }

    @Override
    public Option<Object> flatten() {
        return flatMap(value -> (value instanceof Option) ? ((Option<?>) value).flatten() : this);
    }

    @Override
    public <U> Some<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return new Some<>(mapper.apply(get()));
    }

    @Override
    public Some<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        action.accept(get());
        return this;
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
    public String toString() {
        return "Some(" + value + ")";
    }
}
