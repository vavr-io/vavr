/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
// @@ GENERATED FILE - DO NOT MODIFY @@
package javaslang.function;

import javaslang.Tuple.*;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface Function0<R> extends λ<R>, java.util.function.Supplier<R> {

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
    default Function1<Void, R> curried() {
        return v -> apply();
    }

    @Override
    default Function1<Tuple0, R> tupled() {
        return t -> apply();
    }

    @Override
    default Function0<R> reversed() {
        return () -> apply();
    }

    @Override
    default <V> Function0<V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return () -> after.apply(apply());
    }

}