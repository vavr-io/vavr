/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.control.Option;
import javaslang.control.Some;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a lazy evaluated value. Compared to a Supplier, Lazy is memoizing, i.e. it evaluates only once and
 * therefore is referential transparent.
 * <pre>
 * <code>
 * final Lazy&lt;Double&gt; lazyDouble = Lazy.of(Math::random)
 * lazyDouble.get() // returns a random double, e.g. 0.123
 * lazyDouble.get() // returns the memoized value, e.g. 0.123
 * </code>
 * </pre>
 * @since 1.3.0
 */
public final class Lazy<T> implements Supplier<T>, ValueObject {

    private static final long serialVersionUID = 1L;

    private Supplier<T> supplier;

    // read http://javarevisited.blogspot.de/2014/05/double-checked-locking-on-singleton-in-java.html
    private volatile Option<T> value = Option.none();

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Creates a Lazy, because it is easier to write {@code Lazy.of(xxx)} than {@code new Lazy<>(xxx)}.
     *
     * @param <T> type of the lazy value
     * @param supplier A supplier
     * @return A new instance of Lazy
     */
    public static <T> Lazy<T> of(Supplier<T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return new Lazy<>(supplier);
    }

    /**
     * Evaluates this lazy value and caches it, when called the first type.
     * On subsequent calls, returns the cached value.
     *
     * @return the lazy evaluated value
     */
    @Override
    public T get() {
        if (value.isEmpty()) {
            synchronized (this) {
                if (value.isEmpty()) {
                    value = new Some<>(supplier.get());
                    supplier = null; // free mem
                }
            }
        }
        return value.get();
    }

    @Override
    public Tuple1<T> unapply() {
        return Tuple.of(get());
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Lazy && ((Lazy) o).get().equals(get()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(get());
    }

    @Override
    public String toString() {
        return String.format("Lazy(%s)", get());
    }
}
