/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a function with no arguments.
 *
 * @param <R> return type of the function
 * @since 1.1.0
 */
@FunctionalInterface
public interface Function0<R> extends λ<R>, Supplier<R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Lifts a <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method
     * reference</a> to a {@code Function0}.
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @param <R> return type
     * @return a {@code Function0}
     */
    static <R> Function0<R> lift(Function0<R> methodReference) {
        return methodReference;
    }

    /**
     * Applies this function to no arguments and returns the result.
     *
     * @return the result of function application
     * 
     */
    R apply();

    /**
     * Implementation of {@linkplain java.util.function.Supplier#get()}, just calls {@linkplain #apply()}.
     *
     * @return the result of {@code apply()}
     */
    @Override
    default R get() {
        return apply();
    }

    @Override
    default int arity() {
        return 0;
    }

    @Override
    default Function0<R> curried() {
        return this;
    }

    @Override
    default Function1<Tuple0, R> tupled() {
        return t -> apply();
    }

    @Override
    default Function0<R> reversed() {
        return this;
    }

    @Override
    default Function0<R> memoized() {
        return Lazy.of(this::apply)::get;
    }

    /**
     * Returns a composed function that first applies this Function0 to the given argument and then applies
     * {@linkplain Function1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> Function0<V> andThen(Function1<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return () -> after.apply(apply());
    }

}