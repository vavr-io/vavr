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
public interface Function3<T1, T2, T3, R> extends λ<R> {

    long serialVersionUID = 1L;

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

    default <V> Function3<T1, T2, T3, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1, t2, t3) -> after.apply(apply(t1, t2, t3));
    }

}