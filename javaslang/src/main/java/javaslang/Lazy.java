/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.Iterator;
import javaslang.collection.List;
import javaslang.collection.Seq;
import javaslang.control.Match;
import javaslang.control.Option;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents a lazy evaluated value. Compared to a Supplier, Lazy is memoizing, i.e. it evaluates only once and
 * therefore is referential transparent.
 *
 * <pre>
 * <code>
 * final Lazy&lt;Double&gt; l = Lazy.of(Math::random);
 * l.isEvaluated(); // = false
 * l.get();         // = 0.123 (random generated)
 * l.isEvaluated(); // = true
 * l.get();         // = 0.123 (memoized)
 * </code>
 * </pre>
 *
 * Since 2.0.0 you may also create a <em>real</em> lazy value (works only with interfaces):
 *
 * <pre><code>final CharSequence chars = Lazy.of(() -&gt; "Yay!", CharSequence.class);</code></pre>
 *
 * @author Daniel Dietrich
 * @since 1.2.1
 */
// DEV-NOTE: No flatMap and orElse because this more like a Functor than a Monad.
//           It represents a value rather than capturing a specific state.
public final class Lazy<T> implements Value<T>, Supplier<T>, Serializable {

    private static final long serialVersionUID = 1L;

    // read http://javarevisited.blogspot.de/2014/05/double-checked-locking-on-singleton-in-java.html
    private transient volatile Supplier<? extends T> supplier;
    private volatile T value;

    // should not be called directly
    private Lazy(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Narrows a widened {@code Lazy<? extends T>} to {@code Lazy<T>}
     * by performing a type safe-cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param lazy A {@code Lazy}.
     * @param <T>  Component type of the {@code Lazy}.
     * @return the given {@code lazy} instance as narrowed type {@code Lazy<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> Lazy<T> narrow(Lazy<? extends T> lazy) {
        return (Lazy<T>) lazy;
    }

    /**
     * Creates a {@code Lazy} that requests its value from a given {@code Supplier}. The supplier is asked only once,
     * the value is memoized.
     *
     * @param <T>      type of the lazy value
     * @param supplier A supplier
     * @return A new instance of Lazy
     */
    @SuppressWarnings("unchecked")
    public static <T> Lazy<T> of(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        if (supplier instanceof Lazy) {
            return (Lazy<T>) supplier;
        } else {
            return new Lazy<>(supplier);
        }
    }

    /**
     * Reduces many {@code Lazy} values into a single {@code Lazy} by transforming an
     * {@code Iterable<Lazy<? extends T>>} into a {@code Lazy<Seq<T>>}.
     *
     * @param <T>    Type of the lazy values.
     * @param values An iterable of lazy values.
     * @return A lazy sequence of values.
     * @throws NullPointerException if values is null
     */
    public static <T> Lazy<Seq<T>> sequence(Iterable<? extends Lazy<? extends T>> values) {
        Objects.requireNonNull(values, "values is null");
        return Lazy.of(() -> List.ofAll(values).map(Lazy::get));
    }

    /**
     * Creates a real _lazy value_ of type {@code T}, backed by a {@linkplain java.lang.reflect.Proxy} which delegates
     * to a {@code Lazy} instance.
     *
     * @param supplier A supplier
     * @param type     An interface
     * @param <T>      type of the lazy value
     * @return A new instance of T
     */
    @SuppressWarnings("unchecked")
    public static <T> T val(Supplier<? extends T> supplier, Class<T> type) {
        Objects.requireNonNull(supplier, "supplier is null");
        Objects.requireNonNull(type, "type is null");
        if (!type.isInterface()) {
            throw new IllegalArgumentException("type has to be an interface");
        }
        final Lazy<T> lazy = Lazy.of(supplier);
        final InvocationHandler handler = (proxy, method, args) -> method.invoke(lazy.get(), args);
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, handler);
    }

    public Option<T> filter(Predicate<? super T> predicate) {
        return predicate.test(value) ? Option.some(value) : Option.none();
    }

    /**
     * Evaluates this lazy value and caches it, when called the first time.
     * On subsequent calls, returns the cached value.
     *
     * @return the lazy evaluated value
     * @throws NoSuchElementException if this value is undefined
     */
    @Override
    public T get() {
        if (!isEvaluated()) {
            synchronized (this) {
                if (!isEvaluated()) {
                    value = supplier.get();
                    supplier = null; // free mem
                }
            }
        }
        return value;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Checks, if this lazy value is evaluated.
     * <p>
     * Note: A value is internally evaluated (once) by calling {@link #get()}.
     *
     * @return true, if the value is evaluated, false otherwise.
     * @throws UnsupportedOperationException if this value is undefined
     */
    public boolean isEvaluated() {
        return supplier == null;
    }

    @Override
    public boolean isSingleValued() {
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return Iterator.of(get());
    }

    @Override
    public <U> Lazy<U> map(Function<? super T, ? extends U> mapper) {
        return Lazy.of(() -> mapper.apply(get()));
    }

    @Override
    public Match.MatchValue.Of<Lazy<T>> match() {
        return Match.of(this);
    }

    @Override
    public Lazy<T> peek(Consumer<? super T> action) {
        action.accept(get());
        return this;
    }

    /**
     * Transforms this {@code Lazy}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U transform(Function<? super Lazy<? super T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    public String stringPrefix() {
        return "Lazy";
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof Lazy && Objects.equals(((Lazy<?>) o).get(), get()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(get());
    }

    @Override
    public String toString() {
        return stringPrefix() + "(" + (!isEvaluated() ? "?" : value) + ")";
    }

    /**
     * Ensures that the value is evaluated before serialization.
     *
     * @param s An object serialization stream.
     * @throws java.io.IOException If an error occurs writing to the stream.
     */
    private void writeObject(ObjectOutputStream s) throws IOException {
        get(); // evaluates the lazy value if it isn't evaluated yet!
        s.defaultWriteObject();
    }
}
