/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.monad.Option;
import javaslang.monad.Option.Some;

import java.util.function.Supplier;

public interface Memoizer {

    static <T> Memoizer0<T> of(Supplier<T> supplier) {
        return new Memoizer0<>(supplier);
    }

    // TODO: memory footprint vs. impl of ValueObject (=> use cases?)
    static class Memoizer0<T> implements Lambda.λ0 {

        private static final long serialVersionUID = -8603286927231794237L;

        private Supplier<T> supplier;

        // read http://javarevisited.blogspot.de/2014/05/double-checked-locking-on-singleton-in-java.html
        private volatile Option<T> value = Option.none();

        public Memoizer0(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T apply() {
            if (!value.isPresent()) {
                synchronized(this) {
                    if (!value.isPresent()) {
                        value = new Some<>(supplier.get());
                        supplier = null; // free mem
                    }
                }
            }
            return value.get();
        }
    }

    // TODO: Memoizer1..Memoizer13
}
