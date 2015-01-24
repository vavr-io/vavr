/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
// @@ GENERATED FILE - DO NOT MODIFY @@
package javaslang.function;

import javaslang.Tuple11;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface Lambda11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> extends Lambda<R> {

    R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11);

    @Override
    default int arity() {
        return 11;
    }

    @Override
    default Lambda1<T1, Lambda1<T2, Lambda1<T3, Lambda1<T4, Lambda1<T5, Lambda1<T6, Lambda1<T7, Lambda1<T8, Lambda1<T9, Lambda1<T10, Lambda1<T11, R>>>>>>>>>>> curried() {
        return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Override
    default Lambda1<Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11);
    }

    @Override
    default Lambda11<T11, T10, T9, T8, T7, T6, T5, T4, T3, T2, T1, R> reversed() {
        return (t11, t10, t9, t8, t7, t6, t5, t4, t3, t2, t1) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Override
    default <V> Lambda11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

}