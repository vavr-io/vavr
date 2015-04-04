/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Function1;
import javaslang.ValueObject;
import javaslang.algebra.HigherKinded1;
import javaslang.algebra.Monad1;
import javaslang.control.Valences.Univalent;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <p>
 * Replacement for {@link java.util.Optional}.
 * </p>
 * <p>
 * Option is a <a href="http://stackoverflow.com/questions/13454347/monads-with-java-8">monadic</a> container type which
 * represents an optional value. Instances of Option are either an instance of {@link javaslang.control.Some} or the
 * singleton {@link javaslang.control.None}.
 * </p>
 * Most of the API is taken from {@link java.util.Optional}. A similar type can be found in <a
 * href="http://hackage.haskell.org/package/base-4.6.0.1/docs/Data-Maybe.html">Haskell</a> and <a
 * href="http://www.scala-lang.org/api/current/#scala.Option">Scala</a>.
 *
 * @param <T> The type of the optional value.
 * @since 1.0.0
 */
public interface Option<T> extends Monad1<T, Option<?>>, ValueObject, Univalent<T> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    static <T> Option<T> of(T value) {
        return (value == null) ? None.instance() : new Some<>(value);
    }

    static <T> None<T> none() {
        return None.instance();
    }

    default boolean isDefined() {
        return !isEmpty();
    }

    default Option<T> filter(Predicate<? super T> predicate) {
        if (isEmpty() || predicate.test(get())) {
            return this;
        } else {
            return None.instance();
        }
    }

    default void forEach(Consumer<? super T> action) {
        if (isDefined()) {
            action.accept(get());
        }
    }

    @Override
    default T orElse(T other) {
        return isEmpty() ? other : get();
    }

    @Override
    default T orElseGet(Supplier<? extends T> other) {
        return isEmpty() ? other.get() : get();
    }

    @Override
    default <X extends Throwable> T orElseThrow(Supplier<X> exceptionSupplier) throws X {
        if (isEmpty()) {
            throw exceptionSupplier.get();
        } else {
            return get();
        }
    }

    @Override
    <U> Option<U> map(Function1<? super T, ? extends U> mapper);

    @SuppressWarnings("unchecked")
    @Override
    default <U, OPTION extends HigherKinded1<U, Option<?>>> Option<U> flatMap(Function1<? super T, OPTION> mapper) {
        if (isEmpty()) {
            return None.instance();
        } else {
            return (Option<U>) mapper.apply(get());
        }
    }

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();
}
