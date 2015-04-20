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
 *
 * <pre>
 * <code>
 * final Lazy&lt;Double&gt; lazyDouble = Lazy.of(Math::random);
 * lazyDouble.isEvaluated(); // = false
 * lazyDouble.get();         // = 0.123 (random generated)
 * lazyDouble.isEvaluated(); // = true
 * lazyDouble.get();         // = 0.123 (memoized)
 * </code>
 * </pre>
 *
 * In some situations it may also come handy to create a {@code Lazy} that is already evaluated.
 * (For example, Javaslang uses this internally for String representations of Stream.)
 *
 * <pre>
 * <code>
 * final Lazy&lt;Double&gt; lazyDouble = Lazy.eval(1);
 * lazyDouble.isEvaluated(); // = true
 * lazyDouble.get();         // = 1
 * </code>
 * </pre>
 *
 * @since 1.3.0
 */
public final class Lazy<T> implements Supplier<T>, ValueObject {

    private static final long serialVersionUID = 1L;

    private Supplier<T> supplier;

    // read http://javarevisited.blogspot.de/2014/05/double-checked-locking-on-singleton-in-java.html
    private volatile Option<T> value;

    private Lazy(Supplier<T> supplier, Option<T> value) {
        this.supplier = supplier;
        this.value = value;
    }

    /**
     * Creates a {@code Lazy} that requests its value from a given {@code Supplier}. The supplier is asked only once,
     * the value is memoized. Initially the {@code Lazy} is marked as not evaluated.
     *
     * @param <T>      type of the lazy value
     * @param supplier A supplier
     * @return A new instance of Lazy
     */
    public static <T> Lazy<T> of(Supplier<T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return new Lazy<>(supplier, Option.none());
    }

    /**
     * Creates a {@code Lazy} that contains a specific value. The value is stored directly at the time this {@code Lazy}
     * is constructed, the {@code Lazy} is accordingly marked as evaluated.
     *
     * @param value the value of the {@code Lazy}
     * @param <T> type of the value
     * @return a new {@code Lazy} containing the given value
     */
    public static <T> Lazy<T> eval(T value) {
        return new Lazy<>(null, new Some<>(value));
    }

    /**
     * Returns whether this lazy value was already evaluated.
     *
     * @return true, if this lazy value is evaluated, false otherwise
     */
    public boolean isEvaluated() {
        return value.isDefined();
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
        return String.format("Lazy(%s)", isEvaluated() ? get() : "?");
    }
}
