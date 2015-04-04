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

/**
 * Represents a function with 10 arguments.
 *
 * @param <T1> argument 1 of the function
 * @param <T2> argument 2 of the function
 * @param <T3> argument 3 of the function
 * @param <T4> argument 4 of the function
 * @param <T5> argument 5 of the function
 * @param <T6> argument 6 of the function
 * @param <T7> argument 7 of the function
 * @param <T8> argument 8 of the function
 * @param <T9> argument 9 of the function
 * @param <T10> argument 10 of the function
 * @param <R> return type of the function
 * @since 1.1.0
 */
@FunctionalInterface
public interface CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> extends λ<R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Applies this function to 10 arguments and returns the result.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @param t5 argument 5
     * @param t6 argument 6
     * @param t7 argument 7
     * @param t8 argument 8
     * @param t9 argument 9
     * @param t10 argument 10
     * @return the result of function application
     * @throws Throwable if something goes wrong applying this function to the given arguments
     */
    R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) throws Throwable;

    @Override
    default int arity() {
        return 10;
    }

    @Override
    default CheckedFunction1<T1, CheckedFunction1<T2, CheckedFunction1<T3, CheckedFunction1<T4, CheckedFunction1<T5, CheckedFunction1<T6, CheckedFunction1<T7, CheckedFunction1<T8, CheckedFunction1<T9, CheckedFunction1<T10, R>>>>>>>>>> curried() {
        return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    default CheckedFunction1<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10);
    }

    @Override
    default CheckedFunction10<T10, T9, T8, T7, T6, T5, T4, T3, T2, T1, R> reversed() {
        return (t10, t9, t8, t7, t6, t5, t4, t3, t2, t1) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    /**
     * Returns a composed function that first applies this CheckedFunction10 to the given argument and then applies
     * {@linkplain CheckedFunction1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, V> andThen(CheckedFunction1<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

}