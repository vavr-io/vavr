/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javaslang.control.Try;

/**
 * Represents a function with one argument.
 *
 * @param <T1> argument 1 of the function
 * @param <R> return type of the function
 * @since 1.1.0
 */
@FunctionalInterface
public interface CheckedFunction1<T1, R> extends λ<R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Lifts a <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method
     * reference</a> to a {@code CheckedFunction1}.
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @param <R> return type
     * @param <T1> 1st argument
     * @return a {@code CheckedFunction1}
     */
    static <T1, R> CheckedFunction1<T1, R> lift(CheckedFunction1<T1, R> methodReference) {
        return methodReference;
    }

    /**
     * Returns the identity CheckedFunction1, i.e. the function that returns its input.
     *
     * @param <T> argument type (and return type) of the identity function
     * @return the identity CheckedFunction1
     */
    static <T> CheckedFunction1<T, T> identity() {
        return t -> t;
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param t1 argument 1
     * @return the result of function application
     * @throws Throwable if something goes wrong applying this function to the given arguments
     */
    R apply(T1 t1) throws Throwable;

    @Override
    default int arity() {
        return 1;
    }

    @Override
    default CheckedFunction1<T1, R> curried() {
        return this;
    }

    @Override
    default CheckedFunction1<Tuple1<T1>, R> tupled() {
        return t -> apply(t._1);
    }

    @Override
    default CheckedFunction1<T1, R> reversed() {
        return this;
    }

    @Override
    default CheckedFunction1<T1, R> memoized() {
        final Map<T1, R> cache = new ConcurrentHashMap<>();
        return t1 -> cache.computeIfAbsent(t1, t -> Try.of(() -> this.apply(t)).get());
    }

    /**
     * Returns a composed function that first applies this CheckedFunction1 to the given argument and then applies
     * {@linkplain CheckedFunction1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> CheckedFunction1<T1, V> andThen(CheckedFunction1<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1) -> after.apply(apply(t1));
    }

    /**
     * Returns a composed function that first applies the {@linkplain CheckedFunction1} {@code before} the
     * given argument and then applies this CheckedFunction1 to the result.
     *
     * @param <V> argument type of before
     * @param before the function applied before this
     * @return a function composed of before and this
     * @throws NullPointerException if before is null
     */
    default <V> CheckedFunction1<V, R> compose(CheckedFunction1<? super V, ? extends T1> before) {
        Objects.requireNonNull(before, "before is null");
        return v -> apply(before.apply(v));
    }
}