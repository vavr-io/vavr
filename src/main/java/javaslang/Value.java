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
 * @param <T> The type of the wrapped value.
 * @since 2.0.0
 */
public interface Value<T> extends MonadOps<T>, IterableOps<T>, ConversionOps<T> {

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

    @Override
    Value<T> peek(Consumer<? super T> action);

// TODO:
//    @Override
//    default Array<T> toArray() {
//        return isEmpty() ? Array.empty() : Array.ofAll(this);
//    }

    @Override
    default Lazy<T> toLazy() {
        if (this instanceof Lazy) {
            return (Lazy<T>) this;
        } else {
            return isEmpty() ? Lazy.empty() : Lazy.of(this::get);
        }
    }

    @Override
    default List<T> toList() {
        return isEmpty() ? List.empty() : List.ofAll(this);
    }

    @Override
    default <K, V> Map<K, V> toMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        Map<K, V> map = HashMap.empty();
        for (T a : this) {
            final Tuple2<? extends K, ? extends V> entry = f.apply(a);
            map = map.put(entry._1, entry._2);
        }
        return map;
    }

    @Override
    default Option<T> toOption() {
        if (this instanceof Option) {
            return (Option<T>) this;
        } else {
            return isEmpty() ? None.instance() : new Some<>(get());
        }
    }

    @Override
    default Queue<T> toQueue() {
        return isEmpty() ? Queue.empty() : Queue.ofAll(this);
    }

    @Override
    default Set<T> toSet() {
        return isEmpty() ? HashSet.empty() : HashSet.ofAll(this);
    }

    @Override
    default Stack<T> toStack() {
        return isEmpty() ? Stack.empty() : Stack.ofAll(this);
    }

    @Override
    default Stream<T> toStream() {
        return isEmpty() ? Stream.empty() : Stream.ofAll(this);
    }

// TODO:
//    @Override
//    default Vector<T> toVector() {
//        return isEmpty() ? Vector.empty() : Vector.ofAll(this);
//    }

    @SuppressWarnings("unchecked")
    @Override
    default T[] toJavaArray(Class<T> componentType) {
        Objects.requireNonNull(componentType, "componentType is null");
        final java.util.List<T> list = toJavaList();
        return list.toArray((T[]) java.lang.reflect.Array.newInstance(componentType, list.size()));
    }

    @Override
    default java.util.List<T> toJavaList() {
        final java.util.List<T> result = new java.util.ArrayList<>();
        for (T a : this) {
            result.add(a);
        }
        return result;
    }

    @Override
    default Optional<T> toJavaOptional() {
        return isEmpty() ? Optional.empty() : Optional.ofNullable(get());
    }

    @Override
    default <K, V> java.util.Map<K, V> toJavaMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        final java.util.Map<K, V> map = new java.util.HashMap<>();
        for (T a : this) {
            final Tuple2<? extends K, ? extends V> entry = f.apply(a);
            map.put(entry._1, entry._2);
        }
        return map;
    }

    @Override
    default java.util.Set<T> toJavaSet() {
        final java.util.Set<T> result = new java.util.HashSet<>();
        for (T a : this) {
            result.add(a);
        }
        return result;
    }

    @Override
    default java.util.stream.Stream<T> toJavaStream() {
        return StreamSupport.stream(spliterator(), false);
    }

}

/**
 * Internal interface.
 * <p>
 * Some essential operations for monad-like types.
 * The <code>flatMap</code> method is missing here in order to have only one generic type parameter.
 *
 * @param <T> Component type
 * @since 2.0.0
 */
interface MonadOps<T> {

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
     * Maps this value to a new value with different component type.
     *
     * @param mapper A mapper.
     * @param <U>    Component type of the mapped {@code Value}
     * @return a mapped {@code Value}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> Value<U> map(Function<? super T, ? extends U> mapper);
}

/**
 * Internal interface.
 * <p>
 * Some essential operations for iterable-like types.
 *
 * @param <T> Component type
 * @since 2.0.0
 */
interface IterableOps<T> extends Iterable<T> {

    /**
     * Checks, if an element exists such that the predicate holds.
     *
     * @param predicate A Predicate
     * @return true, if predicate holds for one or more elements, false otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default boolean exists(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (T t : this) {
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks, if the given predicate holds for all elements.
     *
     * @param predicate A Predicate
     * @return true, if the predicate holds for all elements, false otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default boolean forAll(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return !exists(predicate.negate());
    }

    /**
     * Performs an action on each element.
     *
     * @param action A {@code Consumer}
     * @throws NullPointerException if {@code action} is null
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        for (T t : this) {
            action.accept(t);
        }
    }

    /**
     * Performs the given {@code action} on the first element if this is an <em>eager</em> implementation.
     * Performs the given {@code action} on all elements (the first immediately, successive deferred),
     * if this is a <em>lazy</em> implementation.
     *
     * @param action The action the will be performed on the element(s).
     * @return this instance
     */
    IterableOps<T> peek(Consumer<? super T> action);
}

/**
 * Internal interface.
 * <p>
 * Inter-Javaslang type and to-Java type conversions. Currently used by Value only.
 *
 * @param <T> Component type
 * @since 2.0.0
 */
interface ConversionOps<T> {

    // -- Javaslang types

// TODO:
//    /**
//     * Converts this instance to a {@link Array}.
//     *
//     * @return A new {@link Array}.
//     */
//    Array<T> toArray();

    /**
     * Converts this instance to a {@link Lazy}.
     *
     * @return A new {@link Lazy}.
     */
    Lazy<T> toLazy();

    /**
     * Converts this instance to a {@link List}.
     *
     * @return A new {@link List}.
     */
    List<T> toList();

    /**
     * Converts this instance to a {@link Map}.
     *
     * @param f   A function that maps an element to a key/value pair represented by Tuple2
     * @param <K> The key type
     * @param <V> The value type
     * @return A new {@link HashMap}.
     */
    <K, V> Map<K, V> toMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f);

    /**
     * Converts this instance to an {@link Option}.
     *
     * @return A new {@link Option}.
     */
    Option<T> toOption();

    /**
     * Converts this instance to a {@link Queue}.
     *
     * @return A new {@link Queue}.
     */
    Queue<T> toQueue();

    /**
     * Converts this instance to a {@link Set}.
     *
     * @return A new {@link HashSet}.
     */
    Set<T> toSet();

    /**
     * Converts this instance to a {@link Stack}.
     *
     * @return A new {@link List}, which is a {@link Stack}.
     */
    Stack<T> toStack();

    /**
     * Converts this instance to a {@link Stream}.
     *
     * @return A new {@link Stream}.
     */
    Stream<T> toStream();

// TODO:
//    /**
//     * Converts this instance to a {@link Tree}.
//     *
//     * @return A new {@link Tree}.
//     */
//    Tree<T> toTree();

// TODO:
//    /**
//     * Converts this instance to a {@link Vector}.
//     *
//     * @return A new {@link Vector}.
//     */
//    Vector<T> toVector();

    // -- Java types

    /**
     * Converts this instance to a Java array.
     *
     * @param componentType Component type of the array
     * @return A new Java array.
     * @throws NullPointerException if componentType is null
     */
    T[] toJavaArray(Class<T> componentType);

    /**
     * Converts this instance to an {@link java.util.List}.
     *
     * @return A new {@link java.util.ArrayList}.
     */
    java.util.List<T> toJavaList();

    /**
     * Converts this instance to a {@link java.util.Map}.
     *
     * @param f   A function that maps an element to a key/value pair represented by Tuple2
     * @param <K> The key type
     * @param <V> The value type
     * @return A new {@link java.util.HashMap}.
     */
    <K, V> java.util.Map<K, V> toJavaMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f);

    /**
     * Converts this instance to an {@link java.util.Optional}.
     *
     * @return A new {@link java.util.Optional}.
     */
    Optional<T> toJavaOptional();

    /**
     * Converts this instance to a {@link java.util.Set}.
     *
     * @return A new {@link java.util.HashSet}.
     */
    java.util.Set<T> toJavaSet();

    /**
     * Converts this instance to a {@link java.util.stream.Stream}.
     *
     * @return A new {@link java.util.stream.Stream}.
     */
    java.util.stream.Stream<T> toJavaStream();

}
