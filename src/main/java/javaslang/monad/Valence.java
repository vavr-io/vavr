/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.monad;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Valence {

    // has one (primary) value
    static interface Univalent<T> {

        T get();

        T orElse(T other);

        T orElseGet(Supplier<? extends T> other);

        <X extends Throwable> T orElseThrow(Supplier<X> exceptionSupplier) throws X;

        Option<T> toOption();
    }

    // has two values (, one is primary)
    static interface Bivalent<T, U> extends Univalent<T> {

        T orElseGet(Function<? super U, ? extends T> other);

        <X extends Throwable> T orElseThrow(Function<? super U, X> exceptionProvider) throws X;

        // order of generic parameters may vary (see Either.LeftProjection, Either.RightProjection)
        Either toEither();
    }
}
