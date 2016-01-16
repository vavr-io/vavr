/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.algebra.Monad;
import javaslang.collection.Iterator;
import javaslang.collection.List;
import javaslang.collection.Seq;
import javaslang.control.Match;

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
public interface Lazy<T> extends Monad<T>, Supplier<T>, Value<T> {

    /**
     * Creates a {@code Lazy} that requests its value from a given {@code Supplier}. The supplier is asked only once,
     * the value is memoized.
     *
     * @param <T>      type of the lazy value
     * @param supplier A supplier
     * @return A new instance of Lazy
     */
    @SuppressWarnings("unchecked")
    static <T> Lazy<T> of(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        if (supplier instanceof Lazy) {
            return (Lazy<T>) supplier;
        } else {
            return new Defined<>(supplier);
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
    static <T> Lazy<Seq<T>> sequence(Iterable<? extends Lazy<? extends T>> values) {
        Objects.requireNonNull(values, "values is null");
        return Lazy.of(() -> List.ofAll(values).map(Lazy::get));
    }

    /**
     * Returns the singleton {@code undefined} lazy value.
     * <p>
     * The undefined lazy value is by definition empty and throws a {@code NoSuchElementException} on {@code get()}.
     *
     * @param <T> Component type
     * @return The undefined lazy value.
     */
    static <T> Lazy<T> undefined() {
        return Undefined.instance();
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
    static <T> T val(Supplier<? extends T> supplier, Class<T> type) {
        Objects.requireNonNull(supplier, "supplier is null");
        Objects.requireNonNull(type, "type is null");
        if (!type.isInterface()) {
            throw new IllegalArgumentException("type has to be an interface");
        }
        final Lazy<T> lazy = Lazy.of(supplier);
        final InvocationHandler handler = (proxy, method, args) -> method.invoke(lazy.get(), args);
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, handler);
    }

    /**
     * Filters this value. If the filter result is empty, {@code None} is returned, otherwise Some of this lazy value
     * is returned.
     *
     * @param predicate A predicate
     * @return A new Option instance
     * @throws NullPointerException if {@code predicate} is null.
     */
    @Override
    default Lazy<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return this;
        } else {
            return predicate.test(get()) ? this : Undefined.instance();
        }
    }

    @Override
    default Lazy<T> filterNot(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }

    @SuppressWarnings("unchecked")
    @Override
    default <U> Lazy<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return (Lazy<U>) this;
        } else {
            return unit(mapper.apply(get()));
        }
    }

    /**
     * Evaluates this lazy value and caches it, when called the first time.
     * On subsequent calls, returns the cached value.
     *
     * @return the lazy evaluated value
     * @throws NoSuchElementException if this value is undefined
     */
    @Override
    T get();

    /**
     * Checks, if this lazy value is evaluated.
     * <p>
     * Note: A value is internally evaluated (once) by calling {@link #get()}.
     *
     * @return true, if the value is evaluated, false otherwise.
     * @throws UnsupportedOperationException if this value is undefined
     */
    boolean isEvaluated();

    /**
     * A {@code Lazy} is single-valued.
     *
     * @return {@code true}
     */
    @Override
    default boolean isSingleValued() {
        return true;
    }

    @Override
    default <U> Lazy<U> map(Function<? super T, ? extends U> mapper) {
        return isEmpty() ? Lazy.undefined() : Lazy.of(() -> mapper.apply(get()));
    }

    @Override
    default Match.MatchMonad.Of<Lazy<T>> match() {
        return Match.of(this);
    }

    @Override
    default Lazy<T> peek(Consumer<? super T> action) {
        if (!isEmpty()) {
            action.accept(get());
        }
        return this;
    }

    @Override
    default String stringPrefix() {
        return "Lazy";
    }

    /**
     * Transforms this {@code Lazy}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(Function<? super Lazy<? super T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    default <U> Lazy<U> unit(Iterable<? extends U> iterable) {
    	if (iterable instanceof Lazy) {
    		return (Lazy<U>) iterable;
    	} else if (iterable instanceof Value) {
    		final Value<U> value = (Value<U>) iterable;
    		return value.isEmpty() ? Lazy.undefined() : Lazy.of(value::get);
    	} else {
    		final java.util.Iterator<? extends U> iterator = iterable.iterator();
    		if (iterator.hasNext()) {
    			return Lazy.of(() -> iterator.next());
    		} else {
    			return Lazy.undefined();
    		}
        }
    }

    /**
     * Lazy value implementation.
     *
     * @param <T> Type of the value.
     */
    final class Defined<T> implements Lazy<T>, Serializable {

        private static final long serialVersionUID = 1L;

        // read http://javarevisited.blogspot.de/2014/05/double-checked-locking-on-singleton-in-java.html
        private transient volatile Supplier<? extends T> supplier;
        private volatile T value;

        // should not be called directly
        private Defined(Supplier<? extends T> supplier) {
            this.supplier = supplier;
        }

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

        @Override
        public boolean isEvaluated() {
            return supplier == null;
        }

        @Override
        public Iterator<T> iterator() {
            return Iterator.of(get());
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

    /**
     * The singleton undefined lazy value.
     *
     * @param <T> Type of the value.
     */
    final class Undefined<T> implements Lazy<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private static final Undefined<?> INSTANCE = new Undefined<>();

        // hidden
        private Undefined() {
        }

        @SuppressWarnings("unchecked")
        static <T> Undefined<T> instance() {
            return (Undefined<T>) INSTANCE;
        }

        @Override
        public T get() {
            throw new NoSuchElementException("get on Undefined");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean isEvaluated() {
            throw new UnsupportedOperationException("isEvaluated on Undefined");
        }

        @Override
        public Iterator<T> iterator() {
            return Iterator.empty();
        }

        @Override
        public boolean equals(Object o) {
            return o == this;
        }

        @Override
        public int hashCode() {
            return -13;
        }

        @Override
        public String toString() {
            return stringPrefix() + "()";
        }

        /**
         * Instance control for object serialization.
         *
         * @return The singleton instance of Nil.
         * @see java.io.Serializable
         */
        private Object readResolve() {
            return INSTANCE;
        }
    }
}
