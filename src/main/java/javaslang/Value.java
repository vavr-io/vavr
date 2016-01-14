/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.algebra.Foldable;
import javaslang.algebra.Functor;
import javaslang.algebra.Monoid;
import javaslang.collection.*;
import javaslang.control.Either;
import javaslang.control.Match;
import javaslang.control.Option;
import javaslang.control.Try;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.StreamSupport;

/**
 * Functional programming is all about values and transformation of values using functions. The {@code Value}
 * type reflects the values in a functional setting. It can be seen as the result of a partial function application.
 * Hence the result may be undefined. If a value is undefined, we say it is empty.
 * <p>
 * How the empty state is interpreted depends on the context, i.e. it may be <em>undefined</em>, <em>failed</em>,
 * <em>no elements</em>, etc.
 * <p>
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
 * <li>{@link #orElse(Object)}</li>
 * <li>{@link #orElseGet(Supplier)}</li>
 * <li>{@link #orElseThrow(Supplier)}</li>
 * <li>{@link #stringPrefix()}</li>
 * </ul>
 *
 * Checks:
 *
 * <ul>
 * <li>{@link #isDefined()}</li>
 * <li>{@link #isEmpty()}</li>
 * <li>{@link #isSingleValued()}</li>
 * </ul>
 *
 * Equality checks:
 *
 * <ul>
 * <li>{@link #corresponds(Iterable, BiPredicate)}</li>
 * <li>{@link #eq(Object)}</li>
 * </ul>
 *
 * Filtering and transformation:
 * <ul>
 * <li>{@link #filter(Predicate)}</li>
 * <li>{@link #filterNot(Predicate)}</li>
 * <li>{@link #map(Function)}</li>
 * </ul>
 *
 * Folding:
 * <ul>
 * <li>{@link #fold(Monoid)}</li>
 * <li>{@link #fold(Object, BiFunction)}</li>
 * <li>{@link #foldLeft(Monoid)}</li>
 * <li>{@link #foldLeft(Object, BiFunction)}</li>
 * <li>{@link #foldMap(Monoid, Function)}</li>
 * <li>{@link #foldRight(Object, BiFunction)}</li>
 * <li>{@link #foldRight(Monoid)}</li>
 * <li>{@link #reduce(BiFunction)}</li>
 * <li>{@link #reduceOption(BiFunction)}</li>
 * <li>{@link #reduceLeft(BiFunction)}</li>
 * <li>{@link #reduceLeftOption(BiFunction)}</li>
 * <li>{@link #reduceRight(BiFunction)}</li>
 * <li>{@link #reduceRightOption(BiFunction)}</li>
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
 * Tests:
 *
 * <ul>
 * <li>{@link #isSingleValued()}</li>
 * </ul>
 *
 * Type conversion:
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
 * <li>{@link #toLeft(Object)}</li>
 * <li>{@link #toLeft(Supplier)}</li>
 * <li>{@link #toList()}</li>
 * <li>{@link #toMap(Function)}</li>
 * <li>{@link #toOption()}</li>
 * <li>{@link #toQueue()}</li>
 * <li>{@link #toRight(Object)}</li>
 * <li>{@link #toRight(Supplier)}</li>
 * <li>{@link #toSet()}</li>
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
public interface Value<T> extends Foldable<T>, Functor<T>, Iterable<T> {

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
     *
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
     * Filters this {@code Value} by testing a predicate.
     * <p>
     * The semantics may vary from class to class, e.g. for single-valued types (like {@code Option})
     * and multi-valued types (like {@link Traversable).
     * The commonality is that filtered.isEmpty() will return true, if no element satisfied the given predicate.
     *
     * @param predicate A predicate
     * @return a new {@code Value} instance
     * @throws NullPointerException if {@code predicate} is null
     */
    Value<T> filter(Predicate<? super T> predicate);

    /**
     * Filters this {@code Value} by testing the negation of a predicate.
     * <p>
     * Shortcut for {@code filter(predicate.negate()}.
     *
     * @param predicate A predicate
     * @return a new {@code Value} instance
     * @throws NullPointerException if {@code predicate} is null
     */
    Value<T> filterNot(Predicate<? super T> predicate);

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
     * States, if this {@code Value} may contain (at most) one element or more than one element, like collections.
     *
     * @return {@code true} if this is single-valued, otherwise {@code false}.
     */
    boolean isSingleValued();

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
     * @param supplier An alternative value supplier.
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
     * @return A value of type {@code T}.
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
     * Returns the underlying value if present, otherwise returns the result of {@code Try.of(supplier).get()}.
     *
     * @param supplier An alternative value supplier.
     * @return A value of type {@code T}.
     * @throws NullPointerException  if supplier is null
     * @throws Try.NonFatalException containing the original exception if this Value was empty and the Try failed.
     */
    default T orElseTry(Try.CheckedSupplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isEmpty() ? Try.of(supplier).get() : get();
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
     * Returns the name of this Value type, which is used by toString().
     *
     * @return This type name.
     */
    String stringPrefix();

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

    // -- Adjusted return types of Iterable

    /**
     * Returns a rich {@code javaslang.collection.Iterator}.
     *
     * @return A new Iterator
     */
    @Override
    Iterator<T> iterator();

    // -- Default implementation of Foldable

    /**
     * Default implementation of {@link Foldable#foldLeft(Object, BiFunction)}
     * <strong>for single-valued types only</strong>, needs to be overridden for multi-valued types.
     *
     * @param <U>     the type to fold over
     * @param zero    A zero element to start with.
     * @param combine A function which combines elements.
     * @return a folded value
     * @throws NullPointerException if {@code combine} is null
     */
    @Override
    default <U> U foldLeft(U zero, BiFunction<? super U, ? super T, ? extends U> combine) {
        Objects.requireNonNull(combine, "combine is null");
        return isEmpty() ? zero : combine.apply(zero, get());
    }

    /**
     * Default implementation of {@link Foldable#foldRight(Object, BiFunction)}
     * <strong>for single-valued types only</strong>, needs to be overridden for multi-valued types.
     *
     * @param zero    A zero element to start with.
     * @param combine A function which combines elements.
     * @param <U>     the  type to fold over
     * @return a folded value
     * @throws NullPointerException if {@code combine} is null
     */
    @Override
    default <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> combine) {
        Objects.requireNonNull(combine, "combine is null");
        return isEmpty() ? zero : combine.apply(get(), zero);
    }

    /**
     * Default implementation of {@link Foldable#reduceLeft(BiFunction)}
     * <strong>for single-valued types only</strong>, needs to be overridden for multi-valued types.
     *
     * @param op A BiFunction of type T
     * @return a reduced value
     * @throws NullPointerException if {@code op} is null
     */
    @Override
    default T reduceLeft(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return get();
    }

    /**
     * Shortcut for {@code isEmpty() ? Option.none() : Option.some(reduceLeft(op))}, does not need to be
     * overridden by subclasses.
     *
     * @param op A BiFunction of type T
     * @return a reduced value
     * @throws NullPointerException if {@code op} is null
     */
    @Override
    default Option<T> reduceLeftOption(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return isEmpty() ? Option.none() : Option.some(reduceLeft(op));
    }

    /**
     * Default implementation of {@link Foldable#reduceRight(BiFunction)}
     * <strong>for single-valued types only</strong>, needs to be overridden for multi-valued types.
     *
     * @param op A BiFunction of type T
     * @return a reduced value
     * @throws NullPointerException if {@code op} is null
     */
    @Override
    default T reduceRight(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return get();
    }

    /**
     * Shortcut for {@code isEmpty() ? Option.none() : Option.some(reduceRight(op))}, does not need to be
     * overridden by subclasses.
     *
     * @param op An operation of type T
     * @return a reduced value
     * @throws NullPointerException if {@code op} is null
     */
    @Override
    default Option<T> reduceRightOption(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return isEmpty() ? Option.none() : Option.some(reduceRight(op));
    }

    // -- Adjusted return types of Functor

    @Override
    <U> Value<U> map(Function<? super T, ? extends U> mapper);

    // -- conversion methods

    /**
     * Provides syntactic sugar for {@link javaslang.control.Match.MatchMonad.Of}.
     * <p>
     * We write
     *
     * <pre><code>
     * value.match()
     *      .when(...).then(...)
     *      .get();
     * </code></pre>
     *
     * instead of
     *
     * <pre><code>
     * Match.of(value)
     *      .when(...).then(...)
     *      .get();
     * </code></pre>
     *
     * @return a new type-safe match builder.
     */
    Match.MatchMonad.Of<? extends Value<T>> match();

    /**
     * Converts this value to a {@link Array}.
     *
     * @return A new {@link Array}.
     */
    default Array<T> toArray() {
        return ValueModule.toTraversable(this, Array.empty(), Array::of, Array::ofAll);
    }

    /**
     * Converts this value to a {@link CharSeq}.
     *
     * @return A new {@link CharSeq}.
     */
    default CharSeq toCharSeq() {
        return CharSeq.of(toString());
    }

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
        return ValueModule.toJavaCollection(this, new ArrayList<>());
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
        if (isDefined()) {
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
        return ValueModule.toJavaCollection(this, new java.util.HashSet<>());
    }

    /**
     * Converts this value to a {@link java.util.stream.Stream}.
     *
     * @return A new {@link java.util.stream.Stream}.
     */
    default java.util.stream.Stream<T> toJavaStream() {
        return StreamSupport.stream(spliterator(), false);
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
            return isEmpty() ? Lazy.undefined() : Lazy.of(this::get);
        }
    }

    /**
     * Converts this value to a {@link Either}.
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
     * Converts this value to a {@link Either}.
     *
     * @param <R>   right type
     * @param right An instance of a right value
     * @return A new {@link Either.Right} containing the value of {@code right} if this is empty, otherwise
     * a new {@link Either.Left} containing this value.
     * @throws NullPointerException if {@code right} is null
     */
    default <R> Either<T, R> toLeft(R right) {
        return isEmpty() ? Either.right(right) : Either.left(get());
    }

    /**
     * Converts this value to a {@link List}.
     *
     * @return A new {@link List}.
     */
    default List<T> toList() {
        return ValueModule.toTraversable(this, List.empty(), List::of, List::ofAll);
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
        if (isEmpty()) {
            return HashMap.empty();
        } else if (isSingleValued()) {
            return HashMap.of(f.apply(get()));
        } else {
            return HashMap.ofEntries(Iterator.ofAll(this).map(f));
        }
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
            return isEmpty() ? Option.none() : Option.some(get());
        }
    }

    /**
     * Converts this value to a {@link Queue}.
     *
     * @return A new {@link Queue}.
     */
    default Queue<T> toQueue() {
        return ValueModule.toTraversable(this, Queue.empty(), Queue::of, Queue::ofAll);
    }

    /**
     * Converts this value to a {@link Either}.
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
     * Converts this value to a {@link Either}.
     *
     * @param <L>  left type
     * @param left An instance of a left value
     * @return A new {@link Either.Left} containing the value of {@code left} if this is empty, otherwise
     * a new {@link Either.Right} containing this value.
     * @throws NullPointerException if {@code left} is null
     */
    default <L> Either<L, T> toRight(L left) {
        return isEmpty() ? Either.left(left) : Either.right(get());
    }

    /**
     * Converts this value to a {@link Set}.
     *
     * @return A new {@link HashSet}.
     */
    default Set<T> toSet() {
        return ValueModule.toTraversable(this, HashSet.empty(), HashSet::of, HashSet::ofAll);
    }

    /**
     * Converts this value to a {@link Stack}.
     *
     * @return A new {@link List}, which is a {@link Stack}.
     */
    default Stack<T> toStack() {
        return ValueModule.toTraversable(this, Stack.empty(), Stack::of, Stack::ofAll);
    }

    /**
     * Converts this value to a {@link Stream}.
     *
     * @return A new {@link Stream}.
     */
    default Stream<T> toStream() {
        return ValueModule.toTraversable(this, Stream.empty(), Stream::of, Stream::ofAll);
    }

    /**
     * Converts this value to a {@link Try}.
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
     * Converts this value to a {@link Try}.
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
     * Converts this value to a {@link Tree}.
     *
     * @return A new {@link Tree}.
     */
    default Tree<T> toTree() {
        return ValueModule.toTraversable(this, Tree.empty(), Tree::of, Tree::ofAll);
    }

    /**
     * Converts this value to a {@link Vector}.
     *
     * @return A new {@link Vector}.
     */
    default Vector<T> toVector() {
        return ValueModule.toTraversable(this, Vector.empty(), Vector::of, Vector::ofAll);
    }

    // -- output

    /**
     * Sends the string representations of this value to the {@link PrintStream}.
     * If this value consists of multiple elements, each element is displayed in a new line.
     *
     * @param out The PrintStream to write to
     * @throws IllegalStateException if {@code PrintStream.checkError()} is true after writing to stream.
     */
    default void out(PrintStream out) {
        for (T t : this) {
            out.println(String.valueOf(t));
            if (out.checkError()) {
                throw new IllegalStateException("Error writing to PrintStream");
            }
        }
    }

    /**
     * Sends the string representations of this value to the {@link PrintWriter}.
     * If this value consists of multiple elements, each element is displayed in a new line.
     *
     * @param writer The PrintWriter to write to
     * @throws IllegalStateException if {@code PrintWriter.checkError()} is true after writing to writer.
     */
    default void out(PrintWriter writer) {
        for (T t : this) {
            writer.println(String.valueOf(t));
            if (writer.checkError()) {
                throw new IllegalStateException("Error writing to PrintWriter");
            }
        }
    }

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

interface ValueModule {

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

    static <T extends java.util.Collection<V>, V> T toJavaCollection(Value<V> value, T empty) {
        if (value.isDefined()) {
            if (value.isSingleValued()) {
                empty.add(value.get());
            } else {
                value.forEach(empty::add);
            }
        }
        return empty;
    }
}
