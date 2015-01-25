/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.Tuple0;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface χ0<R> extends Lambda<R> {

    R apply() throws Throwable;

    @Override
    default int arity() {
        return 0;
    }

    @Override
    default χ1<Void, R> curried() {
        return v -> apply();
    }

    @Override
    default χ1<Tuple0, R> tupled() {
        return t -> apply();
    }

    @Override
    default χ0<R> reversed() {
        return () -> apply();
    }

    @Override
    default <V> χ0<V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return () -> after.apply(apply());
    }

}