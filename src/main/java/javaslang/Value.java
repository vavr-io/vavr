/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.*;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

/**
 * A representation of value which may be either <em>defined</em> or <em>undefined</em>. If a value is undefined, we say
 * it is empty.
 * <p>
 * In a functional setting, a value can be seen as the result of a partial function application. Hence the result may
 * be undefined.
 * <p>
 * How the empty state is interpreted depends on the context, i.e. it may be <em>undefined</em>, <em>failed</em>,
 * <em>not yet defined</em>, etc.
 *
 * <p>
 *
 * Side-effects:
 *
 * <ul>
 * <li>{@link #forEach(Consumer)}</li>
 * <li>{@link #peek(Consumer)}</li>
 * <li>{@link #stderr()}</li>
 * <li>{@link #stdout()}</li>
 * </ul>
 *
 * Tests:
 *
 * <ul>
 * <li>{@link #exists(Predicate)}</li>
 * <li>{@link #forAll(Predicate)}</li>
 * </ul>
 *
 * @param <T> The type of the wrapped value.
 * @since 2.0.0
 */
public interface Value<T> extends javaslang.Iterable<T> {

    /**
     * Gets the underlying value or throws if no value is present.
     *
     * @return the underlying value
     * @throws java.util.NoSuchElementException if no value is defined
     */
    T get();

    /**
     * Gets the underlying as Option.
     *
     * @return Some(value) if a value is present, None otherwise
     */
    default Option<T> getOption() {
        return isEmpty() ? None.instance() : new Some<>(get());
    }

    /**
     * A fluent if-expression for this value. If this is defined (i.e. not empty) trueVal is returned,
     * otherwise falseVal is returned.
     *
     * @param trueVal  The result, if this is defined.
     * @param falseVal The result, if this is not defined.
     * @return trueVal if this.isDefined(), otherwise falseVal.
     */
    default T ifDefined(T trueVal, T falseVal) {
        return isDefined() ? trueVal : falseVal;
    }

    /**
     * A fluent if-expression for this value. If this is defined (i.e. not empty) trueSupplier.get() is returned,
     * otherwise falseSupplier.get() is returned.
     *
     * @param trueSupplier  The result, if this is defined.
     * @param falseSupplier The result, if this is not defined.
     * @return trueSupplier.get() if this.isDefined(), otherwise falseSupplier.get().
     */
    default T ifDefined(Supplier<? extends T> trueSupplier, Supplier<? extends T> falseSupplier) {
        return isDefined() ? trueSupplier.get() : falseSupplier.get();
    }

    /**
     * A fluent if-expression for this value. If this is empty (i.e. not defined) trueVal is returned,
     * otherwise falseVal is returned.
     *
     * @param trueVal  The result, if this is empty.
     * @param falseVal The result, if this is not empty.
     * @return trueVal if this.isEmpty(), otherwise falseVal.
     */
    default T ifEmpty(T trueVal, T falseVal) {
        return isEmpty() ? trueVal : falseVal;
    }

    /**
     * A fluent if-expression for this value. If this is empty (i.e. not defined) trueSupplier.get() is returned,
     * otherwise falseSupplier.get() is returned.
     *
     * @param trueSupplier  The result, if this is defined.
     * @param falseSupplier The result, if this is not defined.
     * @return trueSupplier.get() if this.isEmpty(), otherwise falseSupplier.get().
     */
    default T ifEmpty(Supplier<? extends T> trueSupplier, Supplier<? extends T> falseSupplier) {
        return isEmpty() ? trueSupplier.get() : falseSupplier.get();
    }

    /**
     * Checks, this {@code Value} is empty, i.e. if the underlying value is absent.
     *
     * @return false, if no underlying value is present, true otherwise.
     */
    boolean isEmpty();

    /**
     * Checks, this {@code Value} is defined, i.e. if the underlying value is present.
     *
     * @return true, if an underlying value is present, false otherwise.
     */
    default boolean isDefined() {
        return !isEmpty();
    }

    /**
     * Returns the underlying value if present, otherwise {@code other}.
     *
     * @param other An alternative value.
     * @return A value of type {@code T}
     */
    default T orElse(T other) {
        return isEmpty() ? other : get();
    }

    /**
     * Returns the underlying value if present, otherwise {@code other}.
     *
     * @param supplier An alternative value.
     * @return A value of type {@code T}
     * @throws NullPointerException if supplier is null
     */
    default T orElseGet(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isEmpty() ? supplier.get() : get();
    }

    /**
     * Returns the underlying value if present, otherwise throws {@code supplier.get()}.
     *
     * @param <X>      a Throwable type
     * @param supplier An exception supplier.
     * @return A value of type {@code T}
     * @throws NullPointerException if supplier is null
     * @throws X                    if no value is present
     */
    default <X extends Throwable> T orElseThrow(Supplier<X> supplier) throws X {
        Objects.requireNonNull(supplier, "supplier is null");
        if (isEmpty()) {
            throw supplier.get();
        } else {
            return get();
        }
    }

    // --
    // --
    // -- Monadic operations
    // --
    // --

    /**
     * Filters this {@code Value} by testing a predicate.
     * <p>
     * The semantics may vary from class to class, e.g. for single-valued type (like Option) and multi-values types
     * (like Traversable). The commonality is, that filtered.isEmpty() will return true, if no element satisfied
     * the given predicate.
     * <p>
     * Also, an implementation may throw {@code NoSuchElementException}, if no element makes it through the filter
     * and this state cannot be reflected. E.g. this is the case for {@link javaslang.control.Either.LeftProjection} and
     * {@link javaslang.control.Either.RightProjection}.
     *
     * @param predicate A predicate
     * @return a new Value instance
     * @throws NullPointerException if {@code predicate} is null
     */
    Value<T> filter(Predicate<? super T> predicate);

    /**
     * Flattens this {@code Value}.
     * <p>
     * The semantics may vary from class to class. The commonality is,
     * that some kind of wrapped state is recursively unwrapped.
     * <p>
     * Example:
     * <pre><code>(((1))).flatten() = (1)</code></pre>
     *
     * @return A flattened version of this {@code Value}.
     */
    Value<Object> flatten();

    /**
     * FlatMaps this value to a new value with different component type.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped {@code Value}
     * @return a mapped {@code Value}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> Value<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper);

    /**
     * Maps this value to a new value with different component type.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped {@code Value}
     * @return a mapped {@code Value}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> Value<U> map(Function<? super T, ? extends U> mapper);

    /**
     * Performs the given {@code action} on the first element if this is an <em>eager</em> implementation.
     * Performs the given {@code action} on all elements (the first immediately, successive deferred),
     * if this is a <em>lazy</em> implementation.
     *
     * @param action The action the will be performed on the element(s).
     * @return this instance
     */
    Value<T> peek(Consumer<? super T> action);

    // --
    // --
    // -- Conversion operations
    // --
    // --

    // -- Javaslang types

// TODO:
//    /**
//     * Converts this value to a {@link Array}.
//     *
//     * @return A new {@link Array}.
//     */
//    default Array<T> toArray() {
//        return isEmpty() ? Array.empty() : Array.ofAll(this);
//    }

    /**
     * Converts this value to a {@link CharSeq}.
     *
     * @return A new {@link CharSeq}.
     */
    default CharSeq toCharSeq() {
        return CharSeq.of(toString());
    }

    /**
     * Converts this value to a {@link Lazy}.
     *
     * @return A new {@link Lazy}.
     */
    default Lazy<T> toLazy() {
        if (this instanceof Lazy) {
            return (Lazy<T>) this;
        } else {
            return isEmpty() ? Lazy.empty() : Lazy.of(this::get);
        }
    }

    /**
     * Converts this value to a {@link List}.
     *
     * @return A new {@link List}.
     */
    default List<T> toList() {
        return isEmpty() ? List.empty() : List.ofAll(this);
    }

    /**
     * Converts this value to a {@link Map}.
     *
     * @param f   A function that maps an element to a key/value pair represented by Tuple2
     * @param <K> The key type
     * @param <V> The value type
     * @return A new {@link HashMap}.
     */
    default <K, V> Map<K, V> toMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        Map<K, V> map = HashMap.empty();
        for (T a : this) {
            final Tuple2<? extends K, ? extends V> entry = f.apply(a);
            map = map.put(entry._1, entry._2);
        }
        return map;
    }

    /**
     * Converts this value to an {@link Option}.
     *
     * @return A new {@link Option}.
     */
    default Option<T> toOption() {
        if (this instanceof Option) {
            return (Option<T>) this;
        } else {
            return isEmpty() ? None.instance() : new Some<>(get());
        }
    }

    /**
     * Converts this value to a {@link Queue}.
     *
     * @return A new {@link Queue}.
     */
    default Queue<T> toQueue() {
        return isEmpty() ? Queue.empty() : Queue.ofAll(this);
    }

    /**
     * Converts this value to a {@link Set}.
     *
     * @return A new {@link HashSet}.
     */
    default Set<T> toSet() {
        return isEmpty() ? HashSet.empty() : HashSet.ofAll(this);
    }

    /**
     * Converts this value to a {@link Stack}.
     *
     * @return A new {@link List}, which is a {@link Stack}.
     */
    default Stack<T> toStack() {
        return isEmpty() ? Stack.empty() : Stack.ofAll(this);
    }

    /**
     * Converts this value to a {@link Stream}.
     *
     * @return A new {@link Stream}.
     */
    default Stream<T> toStream() {
        return isEmpty() ? Stream.empty() : Stream.ofAll(this);
    }

// TODO:
//    /**
//     * Converts this value to a {@link Tree}.
//     *
//     * @return A new {@link Tree}.
//     */
//    Tree<T> toTree();

    /**
     * Converts this value to a {@link Vector}.
     *
     * @return A new {@link Vector}.
     */
    default Vector<T> toVector() {
        return isEmpty() ? Vector.empty() : Vector.ofAll(this);
    }


    // -- Java types

    /**
     * Converts this value to a Java array.
     *
     * @param componentType Component type of the array
     * @return A new Java array.
     * @throws NullPointerException if componentType is null
     */
    @SuppressWarnings("unchecked")
    default T[] toJavaArray(Class<T> componentType) {
        Objects.requireNonNull(componentType, "componentType is null");
        final java.util.List<T> list = toJavaList();
        return list.toArray((T[]) java.lang.reflect.Array.newInstance(componentType, list.size()));
    }

    /**
     * Converts this value to an {@link java.util.List}.
     *
     * @return A new {@link java.util.ArrayList}.
     */
    default java.util.List<T> toJavaList() {
        final java.util.List<T> result = new java.util.ArrayList<>();
        for (T a : this) {
            result.add(a);
        }
        return result;
    }

    /**
     * Converts this value to a {@link java.util.Map}.
     *
     * @param f   A function that maps an element to a key/value pair represented by Tuple2
     * @param <K> The key type
     * @param <V> The value type
     * @return A new {@link java.util.HashMap}.
     */
    default <K, V> java.util.Map<K, V> toJavaMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        final java.util.Map<K, V> map = new java.util.HashMap<>();
        for (T a : this) {
            final Tuple2<? extends K, ? extends V> entry = f.apply(a);
            map.put(entry._1, entry._2);
        }
        return map;
    }

    /**
     * Converts this value to an {@link java.util.Optional}.
     *
     * @return A new {@link java.util.Optional}.
     */
    default Optional<T> toJavaOptional() {
        return isEmpty() ? Optional.empty() : Optional.ofNullable(get());
    }

    /**
     * Converts this value to a {@link java.util.Set}.
     *
     * @return A new {@link java.util.HashSet}.
     */
    default java.util.Set<T> toJavaSet() {
        final java.util.Set<T> result = new java.util.HashSet<>();
        for (T a : this) {
            result.add(a);
        }
        return result;
    }

    /**
     * Converts this value to a {@link java.util.stream.Stream}.
     *
     * @return A new {@link java.util.stream.Stream}.
     */
    default java.util.stream.Stream<T> toJavaStream() {
        return StreamSupport.stream(spliterator(), false);
    }

    // --
    // --
    // -- Console output
    // --
    // --

    /**
     * Sends the string representations of this value to the standard error stream {@linkplain System#err}.
     * If this value consists of multiple elements, each element is displayed in a new line.
     *
     * @throws IllegalStateException if {@code PrintStream.checkError()} is true after writing to stderr.
     */
    default void stderr() {
        for (T t : this) {
            System.err.println(String.valueOf(t));
            if (System.err.checkError()) {
                throw new IllegalStateException("Error writing to stderr");
            }
        }
    }

    /**
     * Sends the string representations of this value to the standard output stream {@linkplain System#out}.
     * If this value consists of multiple elements, each element is displayed in a new line.
     *
     * @throws IllegalStateException if {@code PrintStream.checkError()} is true after writing to stdout.
     */
    default void stdout() {
        for (T t : this) {
            System.out.println(String.valueOf(t));
            if (System.out.checkError()) {
                throw new IllegalStateException("Error writing to stdout");
            }
        }
    }

    // --
    // --
    // -- Object
    // --
    // --

    /**
     * Clarifies that values have a proper equals() method implemented.
     * <p>
     * See <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#equals-java.lang.Object-">Object.equals(Object)</a>.
     *
     * @param o An object
     * @return true, if this equals o, false otherwise
     */
    @Override
    boolean equals(Object o);

    /**
     * Clarifies that values have a proper hashCode() method implemented.
     * <p>
     * See <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#hashCode--">Object.hashCode()</a>.
     *
     * @return The hashcode of this object
     */
    @Override
    int hashCode();

    /**
     * Clarifies that values have a proper toString() method implemented.
     * <p>
     * See <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#toString--">Object.toString()</a>.
     *
     * @return A String representation of this object
     */
    @Override
    String toString();

}
