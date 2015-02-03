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
public interface CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, R> extends λ<R> {

    R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) throws Throwable;

    @Override
    default int arity() {
        return 7;
    }

    @Override
    default CheckedFunction1<T1, CheckedFunction1<T2, CheckedFunction1<T3, CheckedFunction1<T4, CheckedFunction1<T5, CheckedFunction1<T6, CheckedFunction1<T7, R>>>>>>> curried() {
        return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> apply(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    default CheckedFunction1<Tuple7<T1, T2, T3, T4, T5, T6, T7>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7);
    }

    @Override
    default CheckedFunction7<T7, T6, T5, T4, T3, T2, T1, R> reversed() {
        return (t7, t6, t5, t4, t3, t2, t1) -> apply(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    default <V> CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1, t2, t3, t4, t5, t6, t7) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7));
    }

}