/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.Tuple2;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface Lambda2<T1, T2, R> extends Lambda<R>, java.util.function.BiFunction<T1, T2, R> {

    @Override
    R apply(T1 t1, T2 t2);

    @Override
    default int arity() {
        return 2;
    }

    @Override
    default Lambda1<T1, Lambda1<T2, R>> curried() {
        return t1 -> t2 -> apply(t1, t2);
    }

    @Override
    default Lambda1<Tuple2<T1, T2>, R> tupled() {
        return t -> apply(t._1, t._2);
    }

    @Override
    default Lambda2<T2, T1, R> reversed() {
        return (t2, t1) -> apply(t1, t2);
    }

    @Override
    default <V> Lambda2<T1, T2, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1, t2) -> after.apply(apply(t1, t2));
    }

}