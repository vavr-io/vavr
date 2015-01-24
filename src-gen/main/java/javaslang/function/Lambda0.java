/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

//
// *-- GENERATED FILE - DO NOT MODIFY --*
//

import javaslang.Tuple0;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface Lambda0<R> extends Lambda<R>, java.util.function.Supplier<R> {

    R apply();

    @Override
    default R get() {
        return apply();
    }

    @Override
    default int arity() {
        return 0;
    }

    @Override
    default Lambda1<Void, R> curried() {
        return v -> apply();
    }

    @Override
    default Lambda1<Tuple0, R> tupled() {
        return t -> apply();
    }

    @Override
    default Lambda0<R> reversed() {
        return () -> apply();
    }

    @Override
    default <V> Lambda0<V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return () -> after.apply(apply());
    }

}