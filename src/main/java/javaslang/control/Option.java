/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.ValueObject;
import javaslang.algebra.HigherKinded1;
import javaslang.algebra.Monad1;
import javaslang.control.Valences.Univalent;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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
 */
public interface Option<T> extends Monad1<T, Option<?>>, ValueObject, Univalent<T> {

    static <T> Option<T> of(T value) {
        return (value == null) ? None.instance() : new Some<>(value);
    }

    static <T> Option<T> none() {
        return None.instance();
    }

    boolean isPresent();

    boolean isNotPresent();

    void ifPresent(Consumer<? super T> consumer);

    Option<T> filter(Predicate<? super T> predicate);

    void forEach(Consumer<? super T> action);

    @Override
    <U> Option<U> map(Function<? super T, ? extends U> mapper);

    @Override
    <U, OPTION extends HigherKinded1<U, Option<?>>> Option<U> flatMap(Function<? super T, OPTION> mapper);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();
}
