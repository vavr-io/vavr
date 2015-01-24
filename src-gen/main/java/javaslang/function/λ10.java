/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
// @@ GENERATED FILE - DO NOT MODIFY @@
package javaslang.function;

import javaslang.Tuple10;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface λ10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> extends λ<R> {

    R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10);

    @Override
    default int arity() {
        return 10;
    }

    @Override
    default λ1<T1, λ1<T2, λ1<T3, λ1<T4, λ1<T5, λ1<T6, λ1<T7, λ1<T8, λ1<T9, λ1<T10, R>>>>>>>>>> curried() {
        return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    default λ1<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10);
    }

    @Override
    default λ10<T10, T9, T8, T7, T6, T5, T4, T3, T2, T1, R> reversed() {
        return (t10, t9, t8, t7, t6, t5, t4, t3, t2, t1) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    default <V> λ10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

}