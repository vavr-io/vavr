/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

//
// *-- GENERATED FILE - DO NOT MODIFY --*
//

import javaslang.Tuple6;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface CheckedLambda6<T1, T2, T3, T4, T5, T6, R> extends Lambda<R> {

    R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) throws Throwable;

    @Override
    default int arity() {
        return 6;
    }

    @Override
    default CheckedLambda1<T1, CheckedLambda1<T2, CheckedLambda1<T3, CheckedLambda1<T4, CheckedLambda1<T5, CheckedLambda1<T6, R>>>>>> curried() {
        return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> apply(t1, t2, t3, t4, t5, t6);
    }

    @Override
    default CheckedLambda1<Tuple6<T1, T2, T3, T4, T5, T6>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6);
    }

    @Override
    default CheckedLambda6<T6, T5, T4, T3, T2, T1, R> reversed() {
        return (t6, t5, t4, t3, t2, t1) -> apply(t1, t2, t3, t4, t5, t6);
    }

    @Override
    default <V> CheckedLambda6<T1, T2, T3, T4, T5, T6, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1, t2, t3, t4, t5, t6) -> after.apply(apply(t1, t2, t3, t4, t5, t6));
    }

}