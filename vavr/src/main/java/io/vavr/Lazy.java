/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr;

import io.vavr.collection.Iterator;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Objects;
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
 * Example of creating a <em>real</em> lazy value (works only with interfaces):
 *
 * <pre><code>final CharSequence chars = Lazy.val(() -&gt; "Yay!", CharSequence.class);</code></pre>
 *
 * @author Daniel Dietrich
 */
public final class Lazy<T> implements Value<T>, Supplier<T>, Serializable {

    private static final long serialVersionUID = 1L;

    // read http://javarevisited.blogspot.de/2014/05/double-checked-locking-on-singleton-in-java.html
    private transient volatile Supplier<? extends T> supplier;
    private T value; // will behave as a volatile in reality, because a supplier volatile read will update all fields (see https://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html#volatile)

    // should not be called directly
    private Lazy(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Narrows a widened {@code Lazy<? extends T>} to {@code Lazy<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param lazy A {@code Lazy}.
     * @param <T>  Component type of the {@code Lazy}.
     * @return the given {@code lazy} instance as narrowed type {@code Lazy<T>}.
     */
    @SuppressWarnings("unchecked")
    public static <T> Lazy<T> narrow(Lazy<? extends T> lazy) {
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

    /**
     * Filters this lazily evaluated value. A filter call is equivalent to
     *
     * <pre>{@code map(t -> Option.some(t).filter(predicate))}</pre>
     *
     * Examples:
     *
     * <pre>{@code
     * Lazy<String> hank = Lazy.of(() -> "Hank").filter(name -> name.startsWith("H"), name -> "Nikola");
     *
     * Lazy<String> nikola = Lazy.of(() -> "Hank").filter(name -> name.startsWith("N"), name -> "Nikola");
     * }</pre>
     *
     * @param predicate a predicate that states whether the element passes the filter (true) or not (false)
     * @param defaultValue creates a default value, if this lazy value does not make it through the filter
     * @return a new, unevaluated {@code Lazy<T>} instance
     * @throws NullPointerException if {@code predicate} or {@code defaultValue} is null
     */
    public Lazy<T> filter(Predicate<? super T> predicate, Function<? super T, ? extends T> defaultValue) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(predicate, "defaultValue is null");
        return map(t -> predicate.test(t) ? t : defaultValue.apply(t));
    }

    /**
     * Returns a new {@code Lazy} instance that applies the given mapper when being evaluated,
     * e.g. by calling {@link #get()}.
     *
     * @param mapper a function that maps this underlying lazy value to a new {@code Lazy} instance.
     * @param <U> mapped value type
     * @return a new {@code Lazy} instance
     */
    public <U> Lazy<U> flatMap(Function<? super T, Lazy<? extends U>> mapper) {
        return Lazy.of(() -> mapper.apply(get()).get());
    }

    /**
     * Evaluates this lazy value and caches it, when called the first time.
     * On subsequent calls, returns the cached value.
     *
     * @return the lazy evaluated value
     */
    @Override
    public T get() {
        return (supplier == null) ? value : computeValue();
    }
    
    private synchronized T computeValue() {
        final Supplier<? extends T> s = supplier;
        if (s != null) {
            value = s.get();
            supplier = null;
        }
        return value;
    }

    /**
     * A {@code Lazy}'s value is computed synchronously.
     *
     * @return false
     */
    @Override
    public boolean isAsync() {
        return false;
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

    /**
     * A {@code Lazy}'s value is computed lazily.
     *
     * @return true
     */
    @Override
    public boolean isLazy() {
        return true;
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

    /**
     * Transforms this {@code Lazy}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U transform(Function<? super Lazy<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    @Override
    public String stringPrefix() {
        return "Lazy";
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof Lazy && Objects.equals(((Lazy<?>) o).get(), get()));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(get());
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
