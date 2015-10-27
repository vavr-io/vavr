/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.*;
import javaslang.control.*;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

/**
 * Functional programming is all about values and transformation of values using functions. The {@code Value}
 * type reflects the values in a functional setting. It can be seen as the result of a partial function application.
 * Hence the result may be undefined. If a value is undefined, we say it is empty.
 * <p>
 * How the empty state is interpreted depends on the context, i.e. it may be <em>undefined</em>, <em>failed</em>,
 * <em>not yet defined</em>, etc.
 * <p>
 *
 * Static methods:
 *
 * <ul>
 * <li>{@link #get(java.lang.Iterable)}</li>
 * </ul>
 *
 * Basic operations:
 *
 * <ul>
 * <li>{@link #get()}</li>
 * <li>{@link #getOption()}</li>
 * <li>{@link #ifDefined(Supplier, Supplier)}</li>
 * <li>{@link #ifDefined(Object, Object)}</li>
 * <li>{@link #ifEmpty(Supplier, Supplier)}</li>
 * <li>{@link #ifEmpty(Object, Object)}</li>
 * <li>{@link #isDefined()}</li>
 * <li>{@link #isEmpty()}</li>
 * <li>{@link #orElse(Object)}</li>
 * <li>{@link #orElseGet(Supplier)}</li>
 * <li>{@link #orElseThrow(Supplier)}</li>
 * </ul>
 *
 * Conversions:
 *
 * <ul>
 * <li>{@link #toArray()}</li>
 * <li>{@link #toCharSeq()}</li>
 * <li>{@link #toJavaArray()}</li>
 * <li>{@link #toJavaArray(Class)}</li>
 * <li>{@link #toJavaList()}</li>
 * <li>{@link #toJavaMap(Function)}</li>
 * <li>{@link #toJavaOptional()}</li>
 * <li>{@link #toJavaSet()}</li>
 * <li>{@link #toJavaStream()}</li>
 * <li>{@link #toLazy()}</li>
 * <li>{@link #toList()}</li>
 * <li>{@link #toMap(Function)}</li>
 * <li>{@link #toOption()}</li>
 * <li>{@link #toQueue()}</li>
 * <li>{@link #toSet()}</li>
 * <li>{@link #toStack()}</li>
 * <li>{@link #toStream()}</li>
 * <li>{@link #toTree()}</li>
 * <li>{@link #toTry()}</li>
 * <li>{@link #toTry(Supplier)}</li>
 * <li>{@link #toVector()}</li>
 * </ul>
 *
 * Filter-monadic operations:
 *
 * <ul>
 * <li>{@link #filter(Predicate)}</li>
 * <li>{@link #flatMap(Function)}</li>
 * <li>{@link #flatten()}</li>
 * <li>{@link #map(Function)}</li>
 * </ul>
 *
 * Side-effects:
 *
 * <ul>
 * <li>{@link #out(PrintStream)}</li>
 * <li>{@link #out(PrintWriter)}</li>
 * <li>{@link #peek(Consumer)}</li>
 * <li>{@link #stderr()}</li>
 * <li>{@link #stdout()}</li>
 * </ul>
 *
 * @param <T> The type of the wrapped value.
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Value<T> extends javaslang.Iterable<T>, Convertible<T>, FilterMonadic<T>, Printable {

    /**
     * Gets the first value of the given Iterable if exists, otherwise throws.
     *
     * @param iterable An java.lang.Iterable
     * @param <T>      Component type
     * @return An object of type T
     * @throws java.util.NoSuchElementException if the given iterable is empty
     */
    static <T> T get(java.lang.Iterable<? extends T> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        if (iterable instanceof Value) {
            return ((Value<? extends T>) iterable).get();
        } else {
            return iterable.iterator().next();
        }
    }

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

    /**
     * Performs the given {@code action} on the first element if this is an <em>eager</em> implementation.
     * Performs the given {@code action} on all elements (the first immediately, successive deferred),
     * if this is a <em>lazy</em> implementation.
     *
     * @param action The action that will be performed on the element(s).
     * @return this instance
     */
    Value<T> peek(Consumer<? super T> action);

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

    // -- Adjusted return types of FilterMonadic

    @Override
    Value<T> filter(Predicate<? super T> predicate);

    @Override
    Value<? extends Object> flatten();

    @Override
    <U> Value<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper);

    @Override
    <U> Value<U> map(Function<? super T, ? extends U> mapper);

    // -- Convertible implementation

    @Override
    default Array<T> toArray() {
        return isEmpty() ? Array.empty() : Array.ofAll(this);
    }

    @Override
    default CharSeq toCharSeq() {
        return CharSeq.of(toString());
    }

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
    default Optional<T> toJavaOptional() {
        return isEmpty() ? Optional.empty() : Optional.ofNullable(get());
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

    @Override
    default Try<T> toTry() {
        if (this instanceof Try) {
            return (Try<T>) this;
        } else {
            return Try.of(this::get);
        }
    }

    @Override
    default Try<T> toTry(Supplier<? extends Throwable> ifEmpty) {
        Objects.requireNonNull(ifEmpty, "ifEmpty is null");
        return isEmpty() ? new Failure<>(ifEmpty.get()) : toTry();
    }

    @Override
    default Tree<T> toTree() {
        return isEmpty() ? Tree.empty() : Tree.ofAll(this);
    }

    @Override
    default Vector<T> toVector() {
        return isEmpty() ? Vector.empty() : Vector.ofAll(this);
    }

    // -- Printable implementation

    @Override
    default void out(PrintStream out) {
        for (T t : this) {
            out.println(String.valueOf(t));
            if (out.checkError()) {
                throw new IllegalStateException("Error writing to PrintStream");
            }
        }
    }

    @Override
    default void out(PrintWriter writer) {
        for (T t : this) {
            writer.println(String.valueOf(t));
            if (writer.checkError()) {
                throw new IllegalStateException("Error writing to PrintWriter");
            }
        }
    }
}

/**
 * Conversion methods.
 *
 * @param <T> Component type.
 */
interface Convertible<T> {

    /**
     * Converts this value to a {@link Array}.
     *
     * @return A new {@link Array}.
     */
    Array<T> toArray();

    /**
     * Converts this value to a {@link CharSeq}.
     *
     * @return A new {@link CharSeq}.
     */
    CharSeq toCharSeq();

    /**
     * Converts this value to an untyped Java array.
     *
     * @return A new Java array.
     */
    default Object[] toJavaArray() {
        return toJavaList().toArray();
    }

    /**
     * Converts this value to a typed Java array.
     *
     * @param componentType Component type of the array
     * @return A new Java array.
     * @throws NullPointerException if componentType is null
     */
    T[] toJavaArray(Class<T> componentType);

    /**
     * Converts this value to an {@link java.util.List}.
     *
     * @return A new {@link java.util.ArrayList}.
     */
    java.util.List<T> toJavaList();

    /**
     * Converts this value to a {@link java.util.Map}.
     *
     * @param f   A function that maps an element to a key/value pair represented by Tuple2
     * @param <K> The key type
     * @param <V> The value type
     * @return A new {@link java.util.HashMap}.
     */
    <K, V> java.util.Map<K, V> toJavaMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f);

    /**
     * Converts this value to an {@link java.util.Optional}.
     *
     * @return A new {@link java.util.Optional}.
     */
    Optional<T> toJavaOptional();

    /**
     * Converts this value to a {@link java.util.Set}.
     *
     * @return A new {@link java.util.HashSet}.
     */
    java.util.Set<T> toJavaSet();

    /**
     * Converts this value to a {@link java.util.stream.Stream}.
     *
     * @return A new {@link java.util.stream.Stream}.
     */
    java.util.stream.Stream<T> toJavaStream();

    /**
     * Converts this value to a {@link Lazy}.
     *
     * @return A new {@link Lazy}.
     */
    Lazy<T> toLazy();

    /**
     * Converts this value to a {@link List}.
     *
     * @return A new {@link List}.
     */
    List<T> toList();

    /**
     * Converts this value to a {@link Map}.
     *
     * @param f   A function that maps an element to a key/value pair represented by Tuple2
     * @param <K> The key type
     * @param <V> The value type
     * @return A new {@link HashMap}.
     */
    <K, V> Map<K, V> toMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f);

    /**
     * Converts this value to an {@link Option}.
     *
     * @return A new {@link Option}.
     */
    Option<T> toOption();

    /**
     * Converts this value to a {@link Queue}.
     *
     * @return A new {@link Queue}.
     */
    Queue<T> toQueue();

    /**
     * Converts this value to a {@link Set}.
     *
     * @return A new {@link HashSet}.
     */
    Set<T> toSet();

    /**
     * Converts this value to a {@link Stack}.
     *
     * @return A new {@link List}, which is a {@link Stack}.
     */
    Stack<T> toStack();

    /**
     * Converts this value to a {@link Stream}.
     *
     * @return A new {@link Stream}.
     */
    Stream<T> toStream();

    /**
     * Converts this value to a {@link Try}.
     * <p>
     * If this value is undefined, i.e. empty, then a new {@code Failure(NoSuchElementException)} is returned,
     * otherwise a new {@code Success(value)} is returned.
     *
     * @return A new {@link Try}.
     */
    Try<T> toTry();

    /**
     * Converts this value to a {@link Try}.
     * <p>
     * If this value is undefined, i.e. empty, then a new {@code Failure(ifEmpty.get())} is returned,
     * otherwise a new {@code Success(value)} is returned.
     *
     * @param ifEmpty an exception supplier
     * @return A new {@link Try}.
     */
    Try<T> toTry(Supplier<? extends Throwable> ifEmpty);

    /**
     * Converts this value to a {@link Tree}.
     *
     * @return A new {@link Tree}.
     */
    Tree<T> toTree();

    /**
     * Converts this value to a {@link Vector}.
     *
     * @return A new {@link Vector}.
     */
    Vector<T> toVector();

}

/**
 * Monadic and filter operations.
 *
 * @param <T> Component type.
 */
interface FilterMonadic<T> {

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
    FilterMonadic<T> filter(Predicate<? super T> predicate);

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
    // DEV_NOTE: needs to be <? extends Object> because of Map.flatten() of type Map<Object, Object>.
    FilterMonadic<? extends Object> flatten();

    /**
     * FlatMaps this value to a new value with different component type.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped {@code Value}
     * @return a mapped {@code Value}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> FilterMonadic<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper);

    /**
     * Maps this value to a new value with different component type.
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped {@code Value}
     * @return a mapped {@code Value}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> FilterMonadic<U> map(Function<? super T, ? extends U> mapper);

}

/**
 * Print operations.
 */
interface Printable {

    /**
     * Sends the string representations of this value to the {@link PrintStream}.
     * If this value consists of multiple elements, each element is displayed in a new line.
     *
     * @param out The PrintStream to write to
     * @throws IllegalStateException if {@code PrintStream.checkError()} is true after writing to stream.
     */
    void out(PrintStream out);

    /**
     * Sends the string representations of this value to the {@link PrintWriter}.
     * If this value consists of multiple elements, each element is displayed in a new line.
     *
     * @param writer The PrintWriter to write to
     * @throws IllegalStateException if {@code PrintWriter.checkError()} is true after writing to writer.
     */
    void out(PrintWriter writer);

    /**
     * Sends the string representations of this value to the standard error stream {@linkplain System#err}.
     * If this value consists of multiple elements, each element is displayed in a new line.
     *
     * @throws IllegalStateException if {@code PrintStream.checkError()} is true after writing to stderr.
     */
    default void stderr() {
        out(System.err);
    }

    /**
     * Sends the string representations of this value to the standard output stream {@linkplain System#out}.
     * If this value consists of multiple elements, each element is displayed in a new line.
     *
     * @throws IllegalStateException if {@code PrintStream.checkError()} is true after writing to stdout.
     */
    default void stdout() {
        out(System.out);
    }

}
