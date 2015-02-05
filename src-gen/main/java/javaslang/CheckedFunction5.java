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
import java.util.function.Function;

@FunctionalInterface
public interface CheckedFunction5<T1, T2, T3, T4, T5, R> extends λ<R> {

    R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) throws Throwable;

    @Override
    default int arity() {
        return 5;
    }

    @Override
    default CheckedFunction1<T1, CheckedFunction1<T2, CheckedFunction1<T3, CheckedFunction1<T4, CheckedFunction1<T5, R>>>>> curried() {

        return t1 -> t2 -> t3 -> t4 -> t5 -> apply(t1, t2, t3, t4, t5);
    }

    @Override
    default CheckedFunction1<Tuple5<T1, T2, T3, T4, T5>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4, t._5);
    }

    @Override
    default CheckedFunction5<T5, T4, T3, T2, T1, R> reversed() {

        return (t5, t4, t3, t2, t1) -> apply(t1, t2, t3, t4, t5);
    }

    @Override
    default <V> CheckedFunction5<T1, T2, T3, T4, T5, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1, t2, t3, t4, t5) -> after.apply(apply(t1, t2, t3, t4, t5));
    }

}