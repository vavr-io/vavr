/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.algebra.Foldable;
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
 * <em>not yet defined</em>, etc.
 * <p>
 *
 * Static methods:
 *
 * <ul>
 * <li>{@link #get(Iterable)}</li>
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
 * Tests:
 *
 * <ul>
 * <li>{@link #isSingletonType()}</li>
 * </ul>
 *
 * @param <T> The type of the wrapped value.
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Value<T> extends Convertible<T>, Foldable<T>, ValueModule.Iterable<T>, ValueModule.Printable {

    /**
     * Gets the first value of the given Iterable if exists.
     *
     * @param iterable An Iterable
     * @param <T>      Component type
     * @return {@code Some(value)} if iterable is not empty, otherwise {@code None}.
     * @throws java.lang.NullPointerException if iterable is null
     */
    @SuppressWarnings("unchecked")
    static <T> Option<T> getOption(Iterable<? extends T> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        if (iterable instanceof Value) {
            final Value<T> value = (Value<T>) iterable;
            return value.getOption();
        } else {
            final java.util.Iterator<? extends T> iterator = iterable.iterator();
            if (iterator.hasNext()) {
                return Option.some(iterator.next());
            } else {
                return Option.none();
            }
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
     * States, if this value may contain (at most) one element or more than one element, like collections.
     * <p>
     * We call a type <em>singleton type</em>, which may contain at most one element.
     *
     * @return {@code true}, if this is a singleton type, {@code false} otherwise.
     */
    boolean isSingletonType();

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

    // -- Adjusted return types of Foldable

    // DEV-NOTE: default implementations for singleton types, needs to be overridden by multi valued types
    @Override
    default <U> U foldLeft(U zero, BiFunction<? super U, ? super T, ? extends U> combine) {
        return isEmpty() ? zero : combine.apply(zero, get());
    }

    // DEV-NOTE: default implementations for singleton types, needs to be overridden by multi valued types
    @Override
    default <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> combine) {
        return isEmpty() ? zero : combine.apply(get(), zero);
    }

    @Override
    default T reduceLeft(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return get();
    }

    @Override
    default Option<T> reduceLeftOption(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return getOption();
    }

    @Override
    default T reduceRight(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return get();
    }

    @Override
    default Option<T> reduceRightOption(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return getOption();
    }

    // -- Convertible implementation

    @Override
    Match.MatchMonad.Of<? extends Value<T>> match();

    @Override
    default Array<T> toArray() {
        return ValueModule.toTraversable(this, Array.empty(), Array::of, Array::ofAll);
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
        return ValueModule.toJavaCollection(this, new ArrayList<>());
    }

    @Override
    default <K, V> java.util.Map<K, V> toJavaMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        final java.util.Map<K, V> map = new java.util.HashMap<>();
        if (isDefined()) {
            if (isSingletonType()) {
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

    @Override
    default Optional<T> toJavaOptional() {
        return isEmpty() ? Optional.empty() : Optional.ofNullable(get());
    }

    @Override
    default java.util.Set<T> toJavaSet() {
        return ValueModule.toJavaCollection(this, new java.util.HashSet<>());
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
            return isEmpty() ? Lazy.undefined() : Lazy.of(this::get);
        }
    }

    @Override
    default <R> Either<T, R> toLeft(Supplier<? extends R> right) {
        Objects.requireNonNull(right, "right is null");
        return isEmpty() ? Either.right(right.get()) : Either.left(get());
    }

    @Override
    default <R> Either<T, R> toLeft(R right) {
        return isEmpty() ? Either.right(right) : Either.left(get());
    }

    @Override
    default List<T> toList() {
        return ValueModule.toTraversable(this, List.empty(), List::of, List::ofAll);
    }

    @Override
    default <K, V> Map<K, V> toMap(Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
        Objects.requireNonNull(f, "f is null");
        if (isEmpty()) {
            return HashMap.empty();
        } else if (isSingletonType()) {
            return HashMap.of(f.apply(get()));
        } else {
            return HashMap.ofEntries(Iterator.ofAll(this).map(f));
        }
    }

    @Override
    default Option<T> toOption() {
        if (this instanceof Option) {
            return (Option<T>) this;
        } else {
            return isEmpty() ? Option.none() : Option.some(get());
        }
    }

    @Override
    default Queue<T> toQueue() {
        return ValueModule.toTraversable(this, Queue.empty(), Queue::of, Queue::ofAll);
    }

    @Override
    default <L> Either<L, T> toRight(Supplier<? extends L> left) {
        Objects.requireNonNull(left, "left is null");
        return isEmpty() ? Either.left(left.get()) : Either.right(get());
    }

    @Override
    default <L> Either<L, T> toRight(L left) {
        return isEmpty() ? Either.left(left) : Either.right(get());
    }

    @Override
    default Set<T> toSet() {
        return ValueModule.toTraversable(this, HashSet.empty(), HashSet::of, HashSet::ofAll);
    }

    @Override
    default Stack<T> toStack() {
        return ValueModule.toTraversable(this, Stack.empty(), Stack::of, Stack::ofAll);
    }

    @Override
    default Stream<T> toStream() {
        return ValueModule.toTraversable(this, Stream.empty(), Stream::of, Stream::ofAll);
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
        return isEmpty() ? Try.failure(ifEmpty.get()) : toTry();
    }

    @Override
    default Tree<T> toTree() {
        return ValueModule.toTraversable(this, Tree.empty(), Tree::of, Tree::ofAll);
    }

    @Override
    default Vector<T> toVector() {
        return ValueModule.toTraversable(this, Vector.empty(), Vector::of, Vector::ofAll);
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

interface ValueModule {

    static <T extends Traversable<V>, V> T toTraversable(Value<V> value, T empty,
                                                         Function<V, T> ofElement,
                                                         Function<java.lang.Iterable<V>, T> ofAll) {
        if (value.isEmpty()) {
            return empty;
        } else if (value.isSingletonType()) {
            return ofElement.apply(value.get());
        } else {
            return ofAll.apply(value);
        }
    }

    static <T extends java.util.Collection<V>, V> T toJavaCollection(Value<V> value, T empty) {
        if (value.isDefined()) {
            if (value.isSingletonType()) {
                empty.add(value.get());
            } else {
                value.forEach(empty::add);
            }
        }
        return empty;
    }

    /**
     * A rich extension of {@code java.lang.Iterable}.
     */
    interface Iterable<T> extends java.lang.Iterable<T> {

        /**
         * Returns a rich {@code javaslang.collection.Iterator}.
         *
         * @return A new Iterator
         */
        @Override
        Iterator<T> iterator();

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
        default <U> boolean corresponds(java.lang.Iterable<U> that, BiPredicate<? super T, ? super U> predicate) {
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
         * o == this                         : true
         * o instanceof ValueModule.Iterable : iterable elements are eq, non-iterable elements equals, for all (o1, o2) in (this, o)
         * o instanceof java.lang.Iterable   : this eq Iterator.of((java.lang.Iterable&lt;?&gt;) o);
         * otherwise                         : false
         * </code></pre>
         *
         * @param o An object
         * @return true, if this equals o according to the rules defined above, otherwise false.
         */
        default boolean eq(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof ValueModule.Iterable) {
                final ValueModule.Iterable<?> that = (ValueModule.Iterable<?>) o;
                return this.iterator().corresponds(that.iterator(), (o1, o2) -> {
                    if (o1 instanceof ValueModule.Iterable) {
                        return ((ValueModule.Iterable<?>) o1).eq(o2);
                    } else if (o2 instanceof ValueModule.Iterable) {
                        return ((ValueModule.Iterable<?>) o2).eq(o1);
                    } else {
                        return Objects.equals(o1, o2);
                    }
                });
            } else if (o instanceof java.lang.Iterable) {
                final ValueModule.Iterable<?> that = Iterator.ofAll((java.lang.Iterable<?>) o);
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
        default void forEach(Consumer<? super T> action) {
            Objects.requireNonNull(action, "action is null");
            for (T t : this) {
                action.accept(t);
            }
        }
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
}
