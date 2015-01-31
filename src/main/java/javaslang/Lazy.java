/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.control.Option;
import javaslang.control.Some;

import java.util.function.Supplier;

/**
 * Represents a lazy evaluated value. Compared to a Supplier, Lazy evaluates only once and therefore is referential transparent.
 * <pre>
 * <code>
 * final Lazy<Double> lazyDouble = Lazy.of(Math::random)
 * lazyDouble.get() // returns a random double, e.g. 0.123
 * lazyDouble.get() // returns the memoized value, e.g. 0.123
 * </code>
 * </pre>
 */
public final class Lazy<T> implements Supplier<T> {

    private Supplier<T> supplier;

    // read http://javarevisited.blogspot.de/2014/05/double-checked-locking-on-singleton-in-java.html
    private volatile Option<T> value = Option.none();

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    @Override
    public T get() {
        if (!value.isPresent()) {
            synchronized (this) {
                if (!value.isPresent()) {
                    value = new Some<>(supplier.get());
                    supplier = null; // free mem
                }
            }
        }
        return value.get();
    }
}
