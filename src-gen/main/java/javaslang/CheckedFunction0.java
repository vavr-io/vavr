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
import javaslang.control.Try;

/**
 * Represents a function with no arguments.
 *
 * @param <R> return type of the function
 * @since 1.1.0
 */
@FunctionalInterface
public interface CheckedFunction0<R> extends λ<R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Lifts a <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method
     * reference</a> to a {@code CheckedFunction0}.
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @param <R> return type
     * @return a {@code CheckedFunction0}
     */
    static <R> CheckedFunction0<R> lift(CheckedFunction0<R> methodReference) {
        return methodReference;
    }

    /**
     * Applies this function to no arguments and returns the result.
     *
     * @return the result of function application
     * @throws Throwable if something goes wrong applying this function to the given arguments
     */
    R apply() throws Throwable;

    @Override
    default int arity() {
        return 0;
    }

    @Override
    default CheckedFunction0<R> curried() {
        return this;
    }

    @Override
    default CheckedFunction1<Tuple0, R> tupled() {
        return t -> apply();
    }

    @Override
    default CheckedFunction0<R> reversed() {
        return this;
    }

    @Override
    default CheckedFunction0<R> memoized() {
        return Lazy.of(() -> Try.of(this::apply).get())::get;
    }

    /**
     * Returns a composed function that first applies this CheckedFunction0 to the given argument and then applies
     * {@linkplain CheckedFunction1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> CheckedFunction0<V> andThen(CheckedFunction1<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return () -> after.apply(apply());
    }

}