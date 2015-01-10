/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Function;
import javaslang.monad.Option;
import javaslang.monad.Option.Some;

import java.util.function.Supplier;

// internal class - subject to disappear in a future release
class Lazy {

    static <T> Lazy0<T> of(Supplier<T> supplier) {
        return new Lazy0<>(supplier);
    }

    static class Lazy0<T> implements Supplier<T> {

        private Supplier<T> supplier;

        // read http://javarevisited.blogspot.de/2014/05/double-checked-locking-on-singleton-in-java.html
        private volatile Option<T> value = Option.none();

        public Lazy0(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() {
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
}
