/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.Objects;

/**
 * Represents a function with three arguments.
 *
 * @param <T1> argument 1 of the function
 * @param <T2> argument 2 of the function
 * @param <T3> argument 3 of the function
 * @param <R> return type of the function
 * @since 1.1.0
 */
@FunctionalInterface
public interface Function3<T1, T2, T3, R> extends λ<R> {

    long serialVersionUID = 1L;

    /**
     * Applies this function to three arguments and returns the result.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @return the result of function application
     * 
     */
    R apply(T1 t1, T2 t2, T3 t3);

    @Override
    default int arity() {
        return 3;
    }

    @Override
    default Function1<T1, Function1<T2, Function1<T3, R>>> curried() {
        return t1 -> t2 -> t3 -> apply(t1, t2, t3);
    }

    @Override
    default Function1<Tuple3<T1, T2, T3>, R> tupled() {
        return t -> apply(t._1, t._2, t._3);
    }

    @Override
    default Function3<T3, T2, T1, R> reversed() {
        return (t3, t2, t1) -> apply(t1, t2, t3);
    }

    /**
     * Returns a composed function that first applies this Function3 to the given argument and then applies
     * {@linkplain Function1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> Function3<T1, T2, T3, V> andThen(Function1<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1, t2, t3) -> after.apply(apply(t1, t2, t3));
    }

}