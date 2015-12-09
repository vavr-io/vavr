/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Value;
import javaslang.collection.Iterator;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Replacement for {@link java.util.Optional}.
 * <p>
 * Option is a <a href="http://stackoverflow.com/questions/13454347/monads-with-java-8">monadic</a> container type which
 * represents an optional value. Instances of Option are either an instance of {@link javaslang.control.Some} or the
 * singleton {@link javaslang.control.None}.
 * <p>
 * Most of the API is taken from {@link java.util.Optional}. A similar type can be found in <a
 * href="http://hackage.haskell.org/package/base-4.6.0.1/docs/Data-Maybe.html">Haskell</a> and <a
 * href="http://www.scala-lang.org/api/current/#scala.Option">Scala</a>.
 *
 * @param <T> The type of the optional value.
 * @author Daniel Dietrich
 * @since 1.0.0
 */
public interface Option<T> extends Value<T> {

    /**
     * Creates a new {@code Option} of a given value.
     *
     * @param value A value
     * @param <T>   type of the value
     * @return {@code Some(value)} if value is not {@code null}, {@code None} otherwise
     */
    static <T> Option<T> of(T value) {
        return (value == null) ? None.instance() : new Some<>(value);
    }

    /**
     * Creates a new {@code Some} of a given value.
     * <p>
     * The only difference to {@link Option#of(Object)} is, when called with argument {@code null}.
     * <pre>
     * <code>
     * Option.of(null);   // = None
     * Option.some(null); // = Some(null)
     * </code>
     * </pre>
     *
     * @param value A value
     * @param <T>   type of the value
     * @return {@code Some(value)}
     */
    static <T> Option<T> some(T value) {
        return new Some<>(value);
    }

    /**
     * Returns the single instance of {@code None}
     *
     * @param <T> component type
     * @return the single instance of {@code None}
     */
    static <T> Option<T> none() {
        return None.instance();
    }

    /**
     * Creates {@code Some} of suppliers value if condition is true, or {@code None} in other case
     *
     * @param <T>       type of the optional value
     * @param condition A boolean value
     * @param supplier  An optional value supplier, may supply {@code null}
     * @return return {@code Some} of supplier's value if condition is true, or {@code None} in other case
     * @throws NullPointerException if the given {@code supplier} is null
     */
    static <T> Option<T> when(boolean condition, Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return condition ? of(supplier.get()) : none();
    }

    /**
     * Wraps a Java Optional to a new Option
     *
     * @param optional a given optional to wrap in {@code Option}
     * @param <T>      type of the value
     * @return {@code Some(optional.get())} if value is Java {@code Optional} is present, {@code None} otherwise
     */
    static <T> Option<T> ofOptional(Optional<? extends T> optional) {
        Objects.requireNonNull(optional, "optional is null");
        return optional.isPresent() ? of(optional.get()) : none();
    }


    /**
     * Returns true, if this is {@code None}, otherwise false, if this is {@code Some}.
     *
     * @return true, if this {@code Option} is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Returns true, if this is {@code Some}, otherwise false, if this is {@code None}.
     * <p>
     * Please note that it is possible to create {@code new Some(null)}, which is defined.
     *
     * @return true, if this {@code Option} has a defined value, false otherwise
     */
    default boolean isDefined() {
        return !isEmpty();
    }

    /**
     * An option is a singleton type.
     *
     * @return {@code true}
     */
    @Override
    default boolean isSingletonType() {
        return true;
    }

    T get();

    @Override
    default Option<T> getOption() {
        return this;
    }

    /**
     * Returns the value if this is a {@code Some} or the {@code other} value if this is a {@code None}.
     * <p>
     * Please note, that the other value is eagerly evaluated.
     *
     * @param other An alternative value
     * @return This value, if this Option is defined or the {@code other} value, if this Option is empty.
     */
    default T orElse(T other) {
        return isEmpty() ? other : get();
    }

    /**
     * Returns the value if this is a {@code Some}, otherwise the {@code other} value is returned,
     * if this is a {@code None}.
     * <p>
     * Please note, that the other value is lazily evaluated.
     *
     * @param supplier An alternative value supplier
     * @return This value, if this Option is defined or the {@code other} value, if this Option is empty.
     */
    default T orElseGet(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isEmpty() ? supplier.get() : get();
    }

    /**
     * Returns the value if this is a {@code Some}, otherwise throws an exception.
     *
     * @param exceptionSupplier An exception supplier
     * @param <X>               A throwable
     * @return This value, if this Option is defined, otherwise throws X
     * @throws X a throwable
     */
    default <X extends Throwable> T orElseThrow(Supplier<X> exceptionSupplier) throws X {
        Objects.requireNonNull(exceptionSupplier, "exceptionSupplier is null");
        if (isEmpty()) {
            throw exceptionSupplier.get();
        } else {
            return get();
        }
    }

    /**
     * Returns {@code Some(value)} if this is a {@code Some} and the value satisfies the given predicate.
     * Otherwise {@code None} is returned.
     *
     * @param predicate A predicate which is used to test an optional value
     * @return {@code Some(value)} or {@code None} as specified
     */
    @Override
    default Option<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return (isEmpty() || predicate.test(get())) ? this : None.instance();
    }

    /**
     * Maps the value to a new {@code Option} if this is a {@code Some}, otherwise returns {@code None}.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the resulting Option
     * @return a new {@code Option}
     */
    @SuppressWarnings("unchecked")
    @Override
    default <U> Option<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return None.instance();
        } else {
            final Iterable<? extends U> iterable = mapper.apply(get());
            if (iterable instanceof Value) {
                return ((Value<U>) iterable).toOption();
            } else {
                final java.util.Iterator<? extends U> iterator = iterable.iterator();
                if (iterator.hasNext()) {
                    return new Some<>(iterator.next());
                } else {
                    return None.instance();
                }
            }
        }
    }

    /**
     * Maps the value and wraps it in a new {@code Some} if this is a {@code Some}, returns {@code None}.
     *
     * @param mapper A value mapper
     * @param <U>    The new value type
     * @return a new {@code Some} containing the mapped value if this Option is defined, otherwise {@code None}, if this is empty.
     */
    @Override
    default <U> Option<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return None.instance();
        } else {
            return new Some<>(mapper.apply(get()));
        }
    }

    /**
     * Applies an action to this value, if this option is defined, otherwise does nothing.
     *
     * @param action An action which can be applied to an optional value
     * @return this {@code Option}
     */
    @Override
    default Option<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (isDefined()) {
            action.accept(get());
        }
        return this;
    }

    /**
     * Transforms this {@code Option}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(Function<? super Option<? super T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    @Override
    default Iterator<T> iterator() {
        return isEmpty() ? Iterator.empty() : Iterator.of(get());
    }

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();
}
