/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.*;
import javaslang.control.Either;
import javaslang.control.Option;
import javaslang.control.Try;
import javaslang.control.Validation;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.StreamSupport;

/**
 * Functional programming is all about values and transformation of values using functions. The {@code Value}
 * type reflects the values in a functional setting. It can be seen as the result of a partial function application.
 * Hence the result may be undefined. If a value is undefined, we say it is empty.
 * <p>
 * How the empty state is interpreted depends on the context, i.e. it may be <em>undefined</em>, <em>failed</em>,
 * <em>no elements</em>, etc.
 * <p>
 * Basic operations:
 *
 * <ul>
 * <li>{@link #get()}</li>
 * <li>{@link #getOption()}</li>
 * <li>{@link #getOrElse(Object)}</li>
 * <li>{@link #getOrElse(Supplier)}</li>
 * <li>{@link #getOrElseThrow(Supplier)}</li>
 * <li>{@link #isEmpty()}</li>
 * <li>{@link #isSingleValued()}</li>
 * <li>{@link #map(Function)}</li>
 * <li>{@link #stringPrefix()}</li>
 * </ul>
 *
 * Equality checks:
 *
 * <ul>
 * <li>{@link #corresponds(Iterable, BiPredicate)}</li>
 * <li>{@link #eq(Object)}</li>
 * </ul>
 *
 * Iterable extensions:
 *
 * <ul>
 * <li>{@link #contains(Object)}</li>
 * <li>{@link #exists(Predicate)}</li>
 * <li>{@link #forAll(Predicate)}</li>
 * <li>{@link #forEach(Consumer)}</li>
 * <li>{@link #iterator()}</li>
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
 * Type conversion:
 *
 * <ul>
 * <li>{@link #toArray()}</li>
 * <li>{@link #toCharSeq()}</li>
 * <li>{@link #toJavaArray()}</li>
 * <li>{@link #toJavaArray(Class)}</li>
 * <li>{@link #toJavaCollection(Function)}</li>
 * <li>{@link #toJavaList()}</li>
 * <li>{@link #toJavaList(Function)}</li>
 * <li>{@link #toJavaMap(Function)}</li>
 * <li>{@link #toJavaMap(Supplier, Function)}</li>
 * <li>{@link #toJavaMap(Supplier, Function, Function)} </li>
 * <li>{@link #toJavaOptional()}</li>
 * <li>{@link #toJavaSet()}</li>
 * <li>{@link #toJavaSet(Function)}</li>
 * <li>{@link #toJavaStream()}</li>
 * <li>{@link #toLeft(Object)}</li>
 * <li>{@link #toLeft(Supplier)}</li>
 * <li>{@link #toLinkedMap(Function)}</li>
 * <li>{@link #toLinkedMap(Function, Function)}</li>
 * <li>{@link #toLinkedSet()}</li>
 * <li>{@link #toList()}</li>
 * <li>{@link #toMap(Function)}</li>
 * <li>{@link #toMap(Function, Function)}</li>
 * <li>{@link #toOption()}</li>
 * <li>{@link #toPriorityQueue()}</li>
 * <li>{@link #toPriorityQueue(Comparator)}</li>
 * <li>{@link #toQueue()}</li>
 * <li>{@link #toRight(Object)}</li>
 * <li>{@link #toRight(Supplier)}</li>
 * <li>{@link #toSet()}</li>
 * <li>{@link #toSortedMap(Comparator, Function)}</li>
 * <li>{@link #toSortedMap(Comparator, Function, Function)}</li>
 * <li>{@link #toSortedMap(Function)}</li>
 * <li>{@link #toSortedMap(Function, Function)}</li>
 * <li>{@link #toSortedSet()}</li>
 * <li>{@link #toSortedSet(Comparator)}</li>
 * <li>{@link #toStack()}</li>
 * <li>{@link #toStream()}</li>
 * <li>{@link #toString()}</li>
 * <li>{@link #toTree()}</li>
 * <li>{@link #toTry()}</li>
 * <li>{@link #toTry(Supplier)}</li>
 * <li>{@link #toVector()}</li>
 * </ul>
 *
 * <strong>Please note:</strong> flatMap signatures are manifold and have to be declared by subclasses of Value.
 *
 * @param <T> The type of the wrapped value.
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Value<T> extends Iterable<T> {

    /**
     * Narrows a widened {@code Value<? extends T>} to {@code Value<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param value A {@code Value}.
     * @param <T>   Component type of the {@code Value}.
     * @return the given {@code value} instance as narrowed type {@code Value<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> Value<T> narrow(Value<? extends T> value) {
        return (Value<T>) value;
    }

    /**
     * Collects the underlying value(s) (if present) using the provided {@code collector}.
     *
     * @param <A>       the mutable accumulation type of the reduction operation
     * @param <R>       the result type of the reduction operation
     * @param collector Collector performing reduction
     * @return R reduction result
     */
    default <R, A> R collect(Collector<? super T, A, R> collector) {
        return StreamSupport.stream(spliterator(), false).collect(collector);
    }

    /**
     * Collects the underlying value(s) (if present) using the given {@code supplier}, {@code accumulator} and
     * {@code combiner}.
     *
     * @param <R>         type of the result
     * @param supplier    provide unit value for reduction
     * @param accumulator perform reduction with unit value
     * @param combiner    function for combining two values, which must be
     *                    compatible with the accumulator.
     * @return R reduction result
     */
    default <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return StreamSupport.stream(spliterator(), false).collect(supplier, accumulator, combiner);
    }

    /**
     * Shortcut for {@code exists(e -> Objects.equals(e, element))}, tests if the given {@code element} is contained.
     *
     * @param element An Object of type A, may be null.
     * @return true, if element is contained, false otherwise.
     */
    default boolean contains(T element) {
        return exists(e -> Objects.equals(e, element));
    }

    /**
     * Tests whether every element of this iterable relates to the corresponding element of another iterable by
     * satisfying a test predicate.
     *
     * @param <U>       Component type of that iterable
     * @param that      the other iterable
     * @param predicate the test predicate, which relates elements from both iterables
     * @return {@code true} if both iterables have the same length and {@code predicate(x, y)}
     * is {@code true} for all corresponding elements {@code x} of this iterable and {@code y} of {@code that},
     * otherwise {@code false}.
     */
    default <U> boolean corresponds(Iterable<U> that, BiPredicate<? super T, ? super U> predicate) {
        final java.util.Iterator<T> it1 = iterator();
        final java.util.Iterator<U> it2 = that.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            if (!predicate.test(it1.next(), it2.next())) {
                return false;
            }
        }
        return !it1.hasNext() && !it2.hasNext();
    }

    /**
     * A <em>smoothing</em> replacement for {@code equals}. It is similar to Scala's {@code ==} but better in the way
     * that it is not limited to collection types, e.g. {@code Some(1) eq List(1)}, {@code None eq Failure(x)} etc.
     * <p>
     * In a nutshell: eq checks <strong>congruence of structures</strong> and <strong>equality of contained values</strong>.
     * <p>
     * Example:
     *
     * <pre><code>
     * // ((1, 2), ((3))) =&gt; structure: (()(())) values: 1, 2, 3
     * final Value&lt;?&gt; i1 = List.of(List.of(1, 2), Arrays.asList(List.of(3)));
     * final Value&lt;?&gt; i2 = Queue.of(Stream.of(1, 2), List.of(Lazy.of(() -&gt; 3)));
     * assertThat(i1.eq(i2)).isTrue();
     * </code></pre>
     * <p>
     * Semantics:
     *
     * <pre><code>
     * o == this             : true
     * o instanceof Value    : iterable elements are eq, non-iterable elements equals, for all (o1, o2) in (this, o)
     * o instanceof Iterable : this eq Iterator.of((Iterable&lt;?&gt;) o);
     * otherwise             : false
     * </code></pre>
     *
     * @param o An object
     * @return true, if this equals o according to the rules defined above, otherwise false.
     */
    default boolean eq(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Value) {
            final Value<?> that = (Value<?>) o;
            return this.iterator().corresponds(that.iterator(), (o1, o2) -> {
                if (o1 instanceof Value) {
                    return ((Value<?>) o1).eq(o2);
                } else if (o2 instanceof Value) {
                    return ((Value<?>) o2).eq(o1);
                } else {
                    return Objects.equals(o1, o2);
                }
            });
        } else if (o instanceof Iterable) {
            final Value<?> that = Iterator.ofAll((Iterable<?>) o);
            return this.eq(that);
        } else {
            return false;
        }
    }

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
    @Override
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        for (T t : this) {
            action.accept(t);
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
     * Gets the underlying value as Option.
     *
     * @return Some(value) if a value is present, None otherwise
     */
    default Option<T> getOption() {
        return isEmpty() ? Option.none() : Option.some(get());
    }

    /**
     * Returns the underlying value if present, otherwise {@code other}.
     *
     * @param other An alternative value.
     * @return A value of type {@code T}
     */
    default T getOrElse(T other) {
        return isEmpty() ? other : get();
    }

    /**
     * Returns the underlying value if present, otherwise {@code other}.
     *
     * @param supplier An alternative value supplier.
     * @return A value of type {@code T}
     * @throws NullPointerException if supplier is null
     */
    default T getOrElse(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isEmpty() ? supplier.get() : get();
    }

    /**
     * Returns the underlying value if present, otherwise throws {@code supplier.get()}.
     *
     * @param <X>      a Throwable type
     * @param supplier An exception supplier.
     * @return A value of type {@code T}.
     * @throws NullPointerException if supplier is null
     * @throws X                    if no value is present
     */
    default <X extends Throwable> T getOrElseThrow(Supplier<X> supplier) throws X {
        Objects.requireNonNull(supplier, "supplier is null");
        if (isEmpty()) {
            throw supplier.get();
        } else {
            return get();
        }
    }

    /**
     * Returns the underlying value if present, otherwise returns the result of {@code Try.of(supplier).get()}.
     *
     * @param supplier An alternative value supplier.
     * @return A value of type {@code T}.
     * @throws NullPointerException  if supplier is null
     * @throws Try.NonFatalException containing the original exception if this Value was empty and the Try failed.
     */
    default T getOrElseTry(Try.CheckedSupplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isEmpty() ? Try.of(supplier).get() : get();
    }

    /**
     * Checks, this {@code Value} is empty, i.e. if the underlying value is absent.
     *
     * @return false, if no underlying value is present, true otherwise.
     */
    boolean isEmpty();

    /**
     * States whether this is a single-valued type.
     *
     * @return {@code true} if this is single-valued, {@code false} otherwise.
     */
    boolean isSingleValued();

    /**
     * Maps the underlying value to a different component type.
     *
     * @param mapper A mapper
     * @param <U>    The new component type
     * @return A new value
     */
    <U> Value<U> map(Function<? super T, ? extends U> mapper);

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
     * Returns the name of this Value type, which is used by toString().
     *
     * @return This type name.
     */
    String stringPrefix();

    // -- output

    /**
     * Sends the string representations of this to the {@link PrintStream}.
     * If this value consists of multiple elements, each element is displayed in a new line.
     *
     * @param out The PrintStream to write to
     * @throws IllegalStateException if {@code PrintStream.checkError()} is true after writing to stream.
     */
    @GwtIncompatible("java.io.PrintStream is not implemented")
    default void out(PrintStream out) {
        for (T t : this) {
            out.println(String.valueOf(t));
            if (out.checkError()) {
                throw new IllegalStateException("Error writing to PrintStream");
            }
        }
    }

    /**
     * Sends the string representations of this to the {@link PrintWriter}.
     * If this value consists of multiple elements, each element is displayed in a new line.
     *
     * @param writer The PrintWriter to write to
     * @throws IllegalStateException if {@code PrintWriter.checkError()} is true after writing to writer.
     */
    @GwtIncompatible("java.io.PrintWriter is not implemented")
    default void out(PrintWriter writer) {
        for (T t : this) {
            writer.println(String.valueOf(t));
            if (writer.checkError()) {
                throw new IllegalStateException("Error writing to PrintWriter");
            }
        }
    }

    /**
     * Sends the string representations of this to the standard error stream {@linkplain System#err}.
     * If this value consists of multiple elements, each element is displayed in a new line.
     *
     * @throws IllegalStateException if {@code PrintStream.checkError()} is true after writing to stderr.
     */
    @GwtIncompatible("java.io.PrintStream is not implemented")
    default void stderr() {
        out(System.err);
    }

    /**
     * Sends the string representations of this to the standard output stream {@linkplain System#out}.
     * If this value consists of multiple elements, each element is displayed in a new line.
     *
     * @throws IllegalStateException if {@code PrintStream.checkError()} is true after writing to stdout.
     */
    @GwtIncompatible("java.io.PrintStream is not implemented")
    default void stdout() {
        out(System.out);
    }

    // -- Adjusted return types of Iterable

    /**
     * Returns a rich {@code javaslang.collection.Iterator}.
     *
     * @return A new Iterator
     */
    @Override
    Iterator<T> iterator();

    // -- conversion methods

    /**
     * Converts this to a {@link Array}.
     *
     * @return A new {@link Array}.
     */
    default Array<T> toArray() {
        return ValueModule.toTraversable(this, Array.empty(), Array::of, Array::ofAll);
    }

    /**
     * Converts this to a {@link CharSeq}.
     *
     * @return A new {@link CharSeq}.
     */
    default CharSeq toCharSeq() {
        return CharSeq.of(toString());
    }

    /**
     * Converts this to a {@link Validation}.
     *
     * @param <U>   value type of a {@code Valid}
     * @param value An instance of a {@code Valid} value
     * @return A new {@link Validation.Valid} containing the given {@code value} if this is empty, otherwise
     * a new {@link Validation.Invalid} containing this value.
     */
    default <U> Validation<T, U> toInvalid(U value) {
        return isEmpty() ? Validation.valid(value) : Validation.invalid(get());
    }

    /**
     * Converts this to a {@link Validation}.
     *
     * @param <U>           value type of a {@code Valid}
     * @param valueSupplier A supplier of a {@code Valid} value
     * @return A new {@link Validation.Valid} containing the result of {@code valueSupplier} if this is empty,
     * otherwise a new {@link Validation.Invalid} containing this value.
     * @throws NullPointerException if {@code valueSupplier} is null
     */
    default <U> Validation<T, U> toInvalid(Supplier<? extends U> valueSupplier) {
        Objects.requireNonNull(valueSupplier, "valueSupplier is null");
        return isEmpty() ? Validation.valid(valueSupplier.get()) : Validation.invalid(get());
    }

    /**
     * Converts this to an untyped Java array.
     *
     * @return A new Java array.
     */
    default Object[] toJavaArray() {
        return toJavaList().toArray();
    }

    /**
     * Converts this to a typed Java array.
     *
     * @param componentType Component type of the array
     * @return A new Java array.
     * @throws NullPointerException if componentType is null
     */
    @SuppressWarnings("unchecked")
    @GwtIncompatible("reflection is not supported")
    default T[] toJavaArray(Class<T> componentType) {
        Objects.requireNonNull(componentType, "componentType is null");
        if (componentType.isPrimitive()) {
            final Class<?> boxedType =
                    componentType == boolean.class ? Boolean.class :
                    componentType == byte.class ? Byte.class :
                    componentType == char.class ? Character.class :
                    componentType == double.class ? Double.class :
                    componentType == float.class ? Float.class :
                    componentType == int.class ? Integer.class :
                    componentType == long.class ? Long.class :
                    componentType == short.class ? Short.class :
                    componentType == void.class ? Void.class : null;
            componentType = (Class<T>) boxedType;
        }
        final java.util.List<T> list = toJavaList();
        return list.toArray((T[]) java.lang.reflect.Array.newInstance(componentType, list.size()));
    }

    /**
     * Converts this to a specific {@link java.util.Collection}.
     *
     * @param factory A {@code java.util.Collection} factory
     *                that returns empty collection with the specified initial capacity
     * @param <C>     a sub-type of {@code java.util.Collection}
     * @return a new {@code java.util.Collection} of type {@code C}
     */
    default <C extends java.util.Collection<T>> C toJavaCollection(Function<Integer, C> factory) {
        return ValueModule.toJavaCollection(this, factory);
    }

    /**
     * Converts this to an {@link java.util.List}.
     *
     * @return A new {@link java.util.ArrayList}.
     */
    default java.util.List<T> toJavaList() {
        return ValueModule.toJavaCollection(this, ArrayList::new);
    }

    /**
     * Converts this to a specific {@link java.util.List}.
     *
     * @param factory A {@code java.util.List} factory
     * @param <LIST>  a sub-type of {@code java.util.List}
     * @return a new {@code java.util.List} of type {@code LIST}
     */
    default <LIST extends java.util.List<T>> LIST toJavaList(Function<Integer, LIST> factory) {
        return ValueModule.toJavaCollection(this, factory);
    }

    /**
     * Converts this to a {@link java.util.Map}.
     *
     * @param f   A function that maps an element to a key/value pair represented by Tuple2
     * @param <K> The key type
     * @param <V> The value type
     * @return A new {@link java.util.HashMap}.
     */
    default <K, V> java.util.Map<K, V> toJavaMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
        return toJavaMap(java.util.HashMap::new, f);
    }

    /**
     * Converts this to a specific {@link java.util.Map}.
     *
     * @param factory     A {@code java.util.Map} factory
     * @param keyMapper   A function that maps an element to a key
     * @param valueMapper A function that maps an element to a value
     * @param <K>         The key type
     * @param <V>         The value type
     * @param <MAP>       a sub-type of {@code java.util.Map}
     * @return a new {@code java.util.Map} of type {@code MAP}
     */
    default <K, V, MAP extends java.util.Map<K, V>> MAP toJavaMap(Supplier<MAP> factory, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return toJavaMap(factory, t -> Tuple.of(keyMapper.apply(t), valueMapper.apply(t)));
    }

    /**
     * Converts this to a specific {@link java.util.Map}.
     *
     * @param factory A {@code java.util.Map} factory
     * @param f       A function that maps an element to a key/value pair represented by Tuple2
     * @param <K>     The key type
     * @param <V>     The value type
     * @param <MAP>   a sub-type of {@code java.util.Map}
     * @return a new {@code java.util.Map} of type {@code MAP}
     */
    default <K, V, MAP extends java.util.Map<K, V>> MAP toJavaMap(Supplier<MAP> factory, Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        final MAP map = factory.get();
        if (!isEmpty()) {
            if (isSingleValued()) {
                final Tuple2<? extends K, ? extends V> entry = f.apply(get());
                map.put(entry._1, entry._2);
            } else {
                for (T a : this) {
                    final Tuple2<? extends K, ? extends V> entry = f.apply(a);
                    map.put(entry._1, entry._2);
                }
            }
        }
        return map;
    }

    /**
     * Converts this to an {@link java.util.Optional}.
     *
     * @return A new {@link java.util.Optional}.
     */
    default Optional<T> toJavaOptional() {
        return isEmpty() ? Optional.empty() : Optional.ofNullable(get());
    }

    /**
     * Converts this to a {@link java.util.Set}.
     *
     * @return A new {@link java.util.HashSet}.
     */
    default java.util.Set<T> toJavaSet() {
        return ValueModule.toJavaCollection(this, java.util.HashSet::new);
    }

    /**
     * Converts this to a specific {@link java.util.Set}.
     *
     * @param factory A {@code java.util.Set} factory
     *                that returns empty set with the specified initial capacity
     * @param <SET>   a sub-type of {@code java.util.Set}
     * @return a new {@code java.util.Set} of type {@code SET}
     */
    default <SET extends java.util.Set<T>> SET toJavaSet(Function<Integer, SET> factory) {
        return ValueModule.toJavaCollection(this, factory);
    }

    /**
     * Converts this to a sequential {@link java.util.stream.Stream}.
     *
     * @return A new sequential {@link java.util.stream.Stream}.
     */
    default java.util.stream.Stream<T> toJavaStream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Converts this to a parallel {@link java.util.stream.Stream}.
     *
     * @return A new parallel {@link java.util.stream.Stream}.
     */
    default java.util.stream.Stream<T> toJavaParallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    /**
     * Converts this to a {@link Either}.
     *
     * @param <R>   right type
     * @param right An instance of a right value
     * @return A new {@link Either.Right} containing the value of {@code right} if this is empty, otherwise
     * a new {@link Either.Left} containing this value.
     */
    default <R> Either<T, R> toLeft(R right) {
        return isEmpty() ? Either.right(right) : Either.left(get());
    }

    /**
     * Converts this to a {@link Either}.
     *
     * @param <R>   right type
     * @param right A supplier of a right value
     * @return A new {@link Either.Right} containing the result of {@code right} if this is empty, otherwise
     * a new {@link Either.Left} containing this value.
     * @throws NullPointerException if {@code right} is null
     */
    default <R> Either<T, R> toLeft(Supplier<? extends R> right) {
        Objects.requireNonNull(right, "right is null");
        return isEmpty() ? Either.right(right.get()) : Either.left(get());
    }

    /**
     * Converts this to a {@link List}.
     *
     * @return A new {@link List}.
     */
    default List<T> toList() {
        return ValueModule.toTraversable(this, List.empty(), List::of, List::ofAll);
    }

    /**
     * Converts this to a {@link Map}.
     *
     * @param keyMapper   A function that maps an element to a key
     * @param valueMapper A function that maps an element to a value
     * @param <K>         The key type
     * @param <V>         The value type
     * @return A new {@link HashMap}.
     */
    default <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return toMap(t -> Tuple.of(keyMapper.apply(t), valueMapper.apply(t)));
    }

    /**
     * Converts this to a {@link Map}.
     *
     * @param f   A function that maps an element to a key/value pair represented by Tuple2
     * @param <K> The key type
     * @param <V> The value type
     * @return A new {@link HashMap}.
     */
    default <K, V> Map<K, V> toMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        final Function<Tuple2<? extends K, ? extends V>, Map<K, V>> ofElement = HashMap::of;
        final Function<Iterable<Tuple2<? extends K, ? extends V>>, Map<K, V>> ofAll = HashMap::ofEntries;
        return ValueModule.toMap(this, HashMap.empty(), ofElement, ofAll, f);
    }

    /**
     * Converts this to a {@link Map}.
     *
     * @param keyMapper   A function that maps an element to a key
     * @param valueMapper A function that maps an element to a value
     * @param <K>         The key type
     * @param <V>         The value type
     * @return A new {@link LinkedHashMap}.
     */
    default <K, V> Map<K, V> toLinkedMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return toLinkedMap(t -> Tuple.of(keyMapper.apply(t), valueMapper.apply(t)));
    }

    /**
     * Converts this to a {@link Map}.
     *
     * @param f   A function that maps an element to a key/value pair represented by Tuple2
     * @param <K> The key type
     * @param <V> The value type
     * @return A new {@link LinkedHashMap}.
     */
    default <K, V> Map<K, V> toLinkedMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        final Function<Tuple2<? extends K, ? extends V>, Map<K, V>> ofElement = LinkedHashMap::of;
        final Function<Iterable<Tuple2<? extends K, ? extends V>>, Map<K, V>> ofAll = LinkedHashMap::ofEntries;
        return ValueModule.toMap(this, LinkedHashMap.empty(), ofElement, ofAll, f);
    }

    /**
     * Converts this to a {@link Map}.
     *
     * @param keyMapper   A function that maps an element to a key
     * @param valueMapper A function that maps an element to a value
     * @param <K>         The key type
     * @param <V>         The value type
     * @return A new {@link TreeMap}.
     */
    default <K extends Comparable<? super K>, V> SortedMap<K, V> toSortedMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return toSortedMap(t -> Tuple.of(keyMapper.apply(t), valueMapper.apply(t)));
    }

    /**
     * Converts this to a {@link Map}.
     *
     * @param f   A function that maps an element to a key/value pair represented by Tuple2
     * @param <K> The key type
     * @param <V> The value type
     * @return A new {@link TreeMap}.
     */
    default <K extends Comparable<? super K>, V> SortedMap<K, V> toSortedMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        return toSortedMap((Comparator<? super K> & Serializable) K::compareTo, f);
    }

    /**
     * Converts this to a {@link Map}.
     *
     * @param comparator  A comparator that induces an order of the Map keys.
     * @param keyMapper   A function that maps an element to a key
     * @param valueMapper A function that maps an element to a value
     * @param <K>         The key type
     * @param <V>         The value type
     * @return A new {@link TreeMap}.
     */
    default <K, V> SortedMap<K, V> toSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(comparator, "comparator is null");
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return toSortedMap(comparator, t -> Tuple.of(keyMapper.apply(t), valueMapper.apply(t)));
    }

    /**
     * Converts this to a {@link Map}.
     *
     * @param comparator A comparator that induces an order of the Map keys.
     * @param f          A function that maps an element to a key/value pair represented by Tuple2
     * @param <K>        The key type
     * @param <V>        The value type
     * @return A new {@link TreeMap}.
     */
    default <K, V> SortedMap<K, V> toSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(comparator, "comparator is null");
        Objects.requireNonNull(f, "f is null");
        final Function<Tuple2<? extends K, ? extends V>, SortedMap<K, V>> ofElement = t -> TreeMap.of(comparator, t);
        final Function<Iterable<Tuple2<? extends K, ? extends V>>, SortedMap<K, V>> ofAll = t -> TreeMap.ofEntries(comparator, t);
        return ValueModule.toMap(this, TreeMap.empty(comparator), ofElement, ofAll, f);
    }

    /**
     * Converts this to an {@link Option}.
     *
     * @return A new {@link Option}.
     */
    default Option<T> toOption() {
        if (this instanceof Option) {
            return (Option<T>) this;
        } else {
            return getOption();
        }
    }

    /**
     * Converts this to a {@link Queue}.
     *
     * @return A new {@link Queue}.
     */
    default Queue<T> toQueue() {
        return ValueModule.toTraversable(this, Queue.empty(), Queue::of, Queue::ofAll);
    }

    /**
     * Converts this to a {@link PriorityQueue}.
     *
     * @return A new {@link PriorityQueue}.
     */
    @SuppressWarnings("unchecked")
    default PriorityQueue<T> toPriorityQueue() {
        if (this instanceof PriorityQueue<?>) {
            return (PriorityQueue<T>) this;
        } else {
            final Comparator<T> comparator = (Comparator<T> & Serializable) (o1, o2) -> ((Comparable<T>) o1).compareTo(o2);
            return toPriorityQueue(comparator);
        }
    }

    /**
     * Converts this to a {@link PriorityQueue}.
     *
     * @param comparator A comparator that induces an order of the PriorityQueue elements.
     * @return A new {@link PriorityQueue}.
     */
    default PriorityQueue<T> toPriorityQueue(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        final PriorityQueue<T> empty = PriorityQueue.empty(comparator);
        final Function<T, PriorityQueue<T>> of = value -> PriorityQueue.of(comparator, value);
        final Function<Iterable<T>, PriorityQueue<T>> ofAll = values -> PriorityQueue.ofAll(comparator, values);
        return ValueModule.toTraversable(this, empty, of, ofAll);
    }

    /**
     * Converts this to a {@link Either}.
     *
     * @param <L>  left type
     * @param left An instance of a left value
     * @return A new {@link Either.Left} containing the value of {@code left} if this is empty, otherwise
     * a new {@link Either.Right} containing this value.
     */
    default <L> Either<L, T> toRight(L left) {
        return isEmpty() ? Either.left(left) : Either.right(get());
    }

    /**
     * Converts this to a {@link Either}.
     *
     * @param <L>  left type
     * @param left A supplier of a left value
     * @return A new {@link Either.Left} containing the result of {@code left} if this is empty, otherwise
     * a new {@link Either.Right} containing this value.
     * @throws NullPointerException if {@code left} is null
     */
    default <L> Either<L, T> toRight(Supplier<? extends L> left) {
        Objects.requireNonNull(left, "left is null");
        return isEmpty() ? Either.left(left.get()) : Either.right(get());
    }

    /**
     * Converts this to a {@link Set}.
     *
     * @return A new {@link HashSet}.
     */
    default Set<T> toSet() {
        return ValueModule.toTraversable(this, HashSet.empty(), HashSet::of, HashSet::ofAll);
    }

    /**
     * Converts this to a {@link Set}.
     *
     * @return A new {@link LinkedHashSet}.
     */
    default Set<T> toLinkedSet() {
        return ValueModule.toTraversable(this, LinkedHashSet.empty(), LinkedHashSet::of, LinkedHashSet::ofAll);
    }

    /**
     * Converts this to a {@link SortedSet}.
     * Current items must be comparable
     *
     * @return A new {@link TreeSet}.
     * @throws ClassCastException if items are not comparable
     */
    @SuppressWarnings("unchecked")
    default SortedSet<T> toSortedSet() throws ClassCastException {
        final Comparator<T> comparator;
        if (this instanceof TreeSet<?>) {
            return (TreeSet<T>) this;
        } else if (this instanceof SortedSet<?>) {
            comparator = ((SortedSet<T>) this).comparator();
        } else {
            comparator = (Comparator<T> & Serializable) (o1, o2) -> ((Comparable<T>) o1).compareTo(o2);
        }
        return toSortedSet(comparator);
    }

    /**
     * Converts this to a {@link SortedSet}.
     *
     * @param comparator A comparator that induces an order of the SortedSet elements.
     * @return A new {@link TreeSet}.
     */
    default SortedSet<T> toSortedSet(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return ValueModule.toTraversable(this, TreeSet.empty(comparator), value -> TreeSet.of(comparator, value), values -> TreeSet.ofAll(comparator, values));
    }

    /**
     * Converts this to a {@link Stack}.
     *
     * @return A new {@link List}, which is a {@link Stack}.
     */
    default Stack<T> toStack() {
        return toList();
    }

    /**
     * Converts this to a {@link Stream}.
     *
     * @return A new {@link Stream}.
     */
    default Stream<T> toStream() {
        return ValueModule.toTraversable(this, Stream.empty(), Stream::of, Stream::ofAll);
    }

    /**
     * Converts this to a {@link Try}.
     * <p>
     * If this value is undefined, i.e. empty, then a new {@code Failure(NoSuchElementException)} is returned,
     * otherwise a new {@code Success(value)} is returned.
     *
     * @return A new {@link Try}.
     */
    default Try<T> toTry() {
        if (this instanceof Try) {
            return (Try<T>) this;
        } else {
            return Try.of(this::get);
        }
    }

    /**
     * Converts this to a {@link Try}.
     * <p>
     * If this value is undefined, i.e. empty, then a new {@code Failure(ifEmpty.get())} is returned,
     * otherwise a new {@code Success(value)} is returned.
     *
     * @param ifEmpty an exception supplier
     * @return A new {@link Try}.
     */
    default Try<T> toTry(Supplier<? extends Throwable> ifEmpty) {
        Objects.requireNonNull(ifEmpty, "ifEmpty is null");
        return isEmpty() ? Try.failure(ifEmpty.get()) : toTry();
    }

    /**
     * Converts this to a {@link Tree}.
     *
     * @return A new {@link Tree}.
     */
    default Tree<T> toTree() {
        return ValueModule.toTraversable(this, Tree.empty(), Tree::of, Tree::ofAll);
    }

    /**
     * Converts this to a {@link Validation}.
     *
     * @param <E>   error type of an {@code Invalid}
     * @param error An error
     * @return A new {@link Validation.Invalid} containing the given {@code error} if this is empty, otherwise
     * a new {@link Validation.Valid} containing this value.
     */
    default <E> Validation<E, T> toValid(E error) {
        return isEmpty() ? Validation.invalid(error) : Validation.valid(get());
    }

    /**
     * Converts this to a {@link Validation}.
     *
     * @param <E>           error type of an {@code Invalid}
     * @param errorSupplier A supplier of an error
     * @return A new {@link Validation.Invalid} containing the result of {@code errorSupplier} if this is empty,
     * otherwise a new {@link Validation.Valid} containing this value.
     * @throws NullPointerException if {@code valueSupplier} is null
     */
    default <E> Validation<E, T> toValid(Supplier<? extends E> errorSupplier) {
        Objects.requireNonNull(errorSupplier, "errorSupplier is null");
        return isEmpty() ? Validation.invalid(errorSupplier.get()) : Validation.valid(get());
    }

    /**
     * Converts this to a {@link Vector}.
     *
     * @return A new {@link Vector}.
     */
    default Vector<T> toVector() {
        return ValueModule.toTraversable(this, Vector.empty(), Vector::of, Vector::ofAll);
    }

    // -- Object

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

interface ValueModule {

    @SuppressWarnings("unchecked")
    static <T extends Traversable<V>, V> T toTraversable(Value<V> value, T empty,
            Function<V, T> ofElement,
            Function<Iterable<V>, T> ofAll) {
        if (value.isEmpty()) {
            return empty;
        } else if (value.isSingleValued()) {
            return ofElement.apply(value.get());
        } else {
            return ofAll.apply(value);
        }
    }

    static <T, K, V, M extends Map<K, V>, TT extends Tuple2<? extends K, ? extends V>> M toMap(
            Value<T> value, M empty,
            Function<TT, M> ofElement,
            Function<Iterable<TT>, M> ofAll,
            Function<? super T, ? extends TT> f
    ) {
        if (value.isEmpty()) {
            return empty;
        } else if (value.isSingleValued()) {
            return ofElement.apply(f.apply(value.get()));
        } else {
            return ofAll.apply(Iterator.ofAll(value).map(f));
        }
    }

    static <T extends java.util.Collection<V>, V> T toJavaCollection(Value<V> value, Function<Integer, T> containerSupplier) {
        final T container;
        if (value.isEmpty()) {
            container = containerSupplier.apply(0);
        } else {
            if (value.isSingleValued()) {
                container = containerSupplier.apply(1);
                container.add(value.get());
            } else {
                final int size = value instanceof Traversable && ((Traversable) value).isTraversableAgain()
                                 ? ((Traversable<V>) value).size()
                                 : 0;
                container = containerSupplier.apply(size);
                value.forEach(container::add);
            }
        }
        return container;
    }
}
