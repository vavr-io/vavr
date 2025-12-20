/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
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
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jspecify.annotations.NonNull;

/**
 * Represents a lazily evaluated value. Unlike a standard {@link java.util.function.Supplier}, 
 * {@code Lazy} is memoizing: the computation is performed at most once, ensuring referential transparency.
 *
 * <p>This type behaves more like a <em>Functor</em> than a <em>Monad</em>: it represents a value rather than capturing
 * a specific state. Therefore, it does not provide operations like {@code flatMap} or {@code orElse}.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * final Lazy&lt;Double&gt; l = Lazy.of(Math::random);
 * l.isEvaluated(); // false
 * double value = l.get(); // evaluates and returns a random number, e.g., 0.123
 * l.isEvaluated(); // true
 * double memoizedValue = l.get(); // returns the same value as before, e.g., 0.123
 * }</pre>
 *
 * <p>Creating a <em>truly lazy</em> value for an interface type:</p>
 * <pre>{@code
 * final CharSequence chars = Lazy.val(() -&gt; "Yay!", CharSequence.class);
 * }</pre>
 *
 * @param <T> the type of the lazily evaluated value
 * @author Daniel Dietrich
 */
public final class Lazy<T> implements Value<T>, Supplier<T>, Serializable {

    /**
     * The serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;
    private final ReentrantLock lock = new ReentrantLock();

    // read http://javarevisited.blogspot.de/2014/05/double-checked-locking-on-singleton-in-java.html
    private transient volatile Supplier<? extends T> supplier;

    @SuppressWarnings("serial") // Conditionally serializable
    private T value; // will behave as a volatile in reality, because a supplier volatile read will update all fields (see https://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html#volatile)

    // should not be called directly
    private Lazy(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Narrows a {@code Lazy<? extends T>} to {@code Lazy<T>} via a
     * type-safe cast. Safe here because the lazy value is immutable and no elements
     * can be added that would violate the type (covariance)
     *
     * @param lazy the lazy value to narrow
     * @param <T>  the target element type
     * @return the same lazy value viewed as {@code Lazy<T>}
     */
    @SuppressWarnings("unchecked")
    public static <T> Lazy<T> narrow(Lazy<? extends T> lazy) {
        return (Lazy<T>) lazy;
    }

    /**
     * Creates a {@code Lazy} instance that obtains its value from the given {@code Supplier}.
     * The supplier is invoked at most once, and the result is cached for subsequent calls.
     *
     * @param <T>      the type of the lazy value
     * @param supplier the supplier providing the value
     * @return a new {@code Lazy} instance
     */
    @SuppressWarnings("unchecked")
    public static <T> Lazy<T> of(@NonNull Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        if (supplier instanceof Lazy) {
            return (Lazy<T>) supplier;
        } else {
            return new Lazy<>(supplier);
        }
    }

    /**
     * Combines multiple {@code Lazy} instances into a single {@code Lazy} containing a sequence of their evaluated values.
     *
     * <p>Transforms an {@code Iterable<Lazy<? extends T>>} into a {@code Lazy<Seq<T>>}, evaluating each value lazily
     * when the resulting {@code Lazy} is accessed.</p>
     *
     * @param <T>    the type of the lazy values
     * @param values an {@code Iterable} of lazy values
     * @return a {@code Lazy} containing a sequence of the evaluated values
     * @throws NullPointerException if {@code values} is null
     */
    @SuppressWarnings("Convert2MethodRef") // TODO should be fixed in JDK 9 and Idea
    public static <T> Lazy<Seq<T>> sequence(@NonNull Iterable<? extends Lazy<? extends T>> values) {
        Objects.requireNonNull(values, "values is null");
        return Lazy.of(() -> Vector.ofAll(values).map(lazy -> lazy.get()));
    }

    /**
     * Creates a true <em>lazy value</em> of type {@code T}, implemented using a {@linkplain java.lang.reflect.Proxy}
     * that delegates to a {@code Lazy} instance.
     *
     * @param supplier the supplier providing the value when needed
     * @param type     the interface class that the proxy should implement
     * @param <T>      the type of the lazy value
     * @return a new proxy instance of type {@code T} that evaluates lazily
     */
    @SuppressWarnings("unchecked")
    public static <T> T val(@NonNull Supplier<? extends T> supplier, @NonNull Class<T> type) {
        Objects.requireNonNull(supplier, "supplier is null");
        Objects.requireNonNull(type, "type is null");
        if (!type.isInterface()) {
            throw new IllegalArgumentException("type has to be an interface");
        }
        final Lazy<T> lazy = Lazy.of(supplier);
        final InvocationHandler handler = (proxy, method, args) -> method.invoke(lazy.get(), args);
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, handler);
    }

    public Option<T> filter(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final T v = get();
        return predicate.test(v) ? Option.some(v) : Option.none();
    }

    /**
     * Evaluates this lazy value on the first call and caches the result.
     * Subsequent calls return the cached value without recomputation.
     *
     * @return the evaluated value
     */
    @Override
    public T get() {
        return (supplier == null) ? value : computeValue();
    }
    
    private T computeValue() {
        lock.lock();
        try {
            final Supplier<? extends T> s = supplier;
            if (s != null) {
                value = s.get();
                supplier = null;
            }
        } finally {
            lock.unlock();
        }
        return value;
    }

    /**
     * Indicates that this {@code Lazy} value is computed synchronously.
     *
     * @return {@code false}
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
     * Checks whether this lazy value has been evaluated.
     *
     * <p>Note: The value is evaluated internally (at most once) when {@link #get()} is called.</p>
     *
     * @return {@code true} if the value has been evaluated, {@code false} otherwise
     * @throws UnsupportedOperationException if this lazy value is undefined
     */
    public boolean isEvaluated() {
        return supplier == null;
    }

    /**
     * Indicates that this {@code Lazy} value is computed lazily.
     *
     * @return {@code true}
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
    public @NonNull Iterator<T> iterator() {
        return Iterator.of(get());
    }

    @Override
    public <U> Lazy<U> map(@NonNull Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return Lazy.of(() -> mapper.apply(get()));
    }

    @Override
    public <U> Lazy<U> mapTo(U value) {
        return map(ignored -> value);
    }

    @Override
    public Lazy<Void> mapToVoid() {
        return map(ignored -> null);
    }

    @Override
    public Lazy<T> peek(@NonNull Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        action.accept(get());
        return this;
    }

    /**
     * Applies a transformation function to the value contained in this {@code Lazy}, producing a new {@code Lazy} instance
     * of the transformed value.
     *
     * @param f   the function to transform the value
     * @param <U> the type of the result of the transformation
     * @return a new {@code Lazy} instance containing the transformed value
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U transform(@NonNull Function<? super Lazy<T>, ? extends U> f) {
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
     * Forces the lazy value to be evaluated before it is serialized.
     *
     * @param s the object output stream to write to
     * @throws java.io.IOException if an I/O error occurs during serialization
     */
    private void writeObject(ObjectOutputStream s) throws IOException {
        get(); // evaluates the lazy value if it isn't evaluated yet!
        s.defaultWriteObject();
    }
}
