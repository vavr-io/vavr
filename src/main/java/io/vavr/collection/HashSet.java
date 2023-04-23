package io.vavr.collection;

import io.vavr.Tuple2;
import io.vavr.collection.champ.BitmapIndexedNode;
import io.vavr.collection.champ.ChangeEvent;
import io.vavr.collection.champ.KeyIterator;
import io.vavr.collection.champ.Node;
import io.vavr.collection.champ.SetSerializationProxy;
import io.vavr.collection.champ.VavrSetMixin;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


/**
 * Implements an immutable set using a Compressed Hash-Array Mapped Prefix-tree
 * (CHAMP).
 * <p>
 * Features:
 * <ul>
 *     <li>supports up to 2<sup>30</sup> elements</li>
 *     <li>allows null elements</li>
 *     <li>is immutable</li>
 *     <li>is thread-safe</li>
 *     <li>does not guarantee a specific iteration order</li>
 * </ul>
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>add: O(1)</li>
 *     <li>remove: O(1)</li>
 *     <li>contains: O(1)</li>
 *     <li>toMutable: O(1) + O(log N) distributed across subsequent updates in the mutable copy</li>
 *     <li>clone: O(1)</li>
 *     <li>iterator.next(): O(1)</li>
 * </ul>
 * <p>
 * Implementation details:
 * <p>
 * This set performs read and write operations of single elements in O(1) time,
 * and in O(1) space.
 * <p>
 * The CHAMP tree contains nodes that may be shared with other sets.
 * <p>
 * If a write operation is performed on a node, then this set creates a
 * copy of the node and of all parent nodes up to the root (copy-path-on-write).
 * Since the CHAMP tree has a fixed maximal height, the cost is O(1).
 * <p>
 * This set can create a mutable copy of itself in O(1) time and O(1) space
 * using method {@code #toMutable()}}. The mutable copy shares its nodes
 * with this set, until it has gradually replaced the nodes with exclusively
 * owned nodes.
 * <p>
 * References:
 * <dl>
 *      <dt>Michael J. Steindorfer (2017).
 *      Efficient Immutable Collections.</dt>
 *      <dd><a href="https://michael.steindorfer.name/publications/phd-thesis-efficient-immutable-collections">michael.steindorfer.name</a>
 *
 *      <dt>The Capsule Hash Trie Collections Library.
 *      <br>Copyright (c) Michael Steindorfer. BSD-2-Clause License</dt>
 *      <dd><a href="https://github.com/usethesource/capsule">github.com</a>
 * </dl>
 *
 * @param <E> the element type
 */
public class HashSet<E> extends BitmapIndexedNode<E> implements VavrSetMixin<E, HashSet<E>>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final HashSet<?> EMPTY = new HashSet<>(BitmapIndexedNode.emptyNode(), 0);
    final int size;

    HashSet(BitmapIndexedNode<E> root, int size) {
        super(root.nodeMap(), root.dataMap(), root.mixed);
        this.size = size;
    }

    /**
     * Returns an empty immutable set.
     *
     * @param <E> the element type
     * @return an empty immutable set
     */
    @SuppressWarnings("unchecked")
    public static <E> HashSet<E> empty() {
        return ((HashSet<E>) HashSet.EMPTY);
    }

    /**
     * Creates an empty set of the specified element type.
     *
     * @param <R> the element type
     * @return a new empty set.
     */
    @Override
    public <R> HashSet<R> create() {
        return empty();
    }

    /**
     * Creates an empty set of the specified element type, and adds all
     * the specified elements.
     *
     * @param elements the elements
     * @param <R>      the element type
     * @return a new set that contains the specified elements.
     */
    @Override
    public <R> HashSet<R> createFromElements(Iterable<? extends R> elements) {
        return HashSet.<R>empty().addAll(elements);
    }

    @Override
    public HashSet<E> add(E key) {
        int keyHash = Objects.hashCode(key);
        ChangeEvent<E> details = new ChangeEvent<>();
        BitmapIndexedNode<E> newRootNode = update(null, key, keyHash, 0, details, getUpdateFunction(), Objects::equals, Objects::hashCode);
        if (details.isModified()) {
            return new HashSet<>(newRootNode, size + 1);
        }
        return this;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public HashSet<E> addAll(Iterable<? extends E> set) {
        if (set == this || isEmpty() && (set instanceof HashSet<?>)) {
            return (HashSet<E>) set;
        }
        if (isEmpty() && (set instanceof MutableHashSet)) {
            return ((MutableHashSet<E>) set).toImmutable();
        }
        MutableHashSet<E> t = toMutable();
        boolean modified = false;
        for (E key : set) {
            modified |= t.add(key);
        }
        return modified ? t.toImmutable() : this;
    }

    @Override
    public boolean contains(E o) {
        return find(o, Objects.hashCode(o), 0, Objects::equals) != Node.NO_DATA;
    }

    private BiFunction<E, E, E> getUpdateFunction() {
        return (oldk, newk) -> oldk;
    }

    @Override
    public Iterator<E> iterator() {
        return new KeyIterator<E>(this, null);
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public Set<E> remove(E key) {
        int keyHash = Objects.hashCode(key);
        ChangeEvent<E> details = new ChangeEvent<>();
        BitmapIndexedNode<E> newRootNode = remove(null, key, keyHash, 0, details, Objects::equals);
        if (details.isModified()) {
            return new HashSet<>(newRootNode, size - 1);
        }
        return this;
    }

    /**
     * Creates a mutable copy of this set.
     *
     * @return a mutable copy of this set.
     */
    MutableHashSet<E> toMutable() {
        return new MutableHashSet<>(this);
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link HashSet}.
     *
     * @param <T> Component type of the HashSet.
     * @return A io.vavr.collection.ChampSet Collector.
     */
    public static <T> Collector<T, ArrayList<T>, HashSet<T>> collector() {
        return Collections.toListAndThen(iterable -> HashSet.<T>empty().addAll(iterable));
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other instanceof HashSet) {
            HashSet<?> that = (HashSet<?>) other;
            return size == that.size && equivalent(that);
        }
        return Collections.equals(this, other);
    }

    @Override
    public int hashCode() {
        return Collections.hashUnordered(iterator());
    }

    /**
     * Creates a ChampSet of the given elements.
     *
     * <pre><code>ChampSet.of(1, 2, 3, 4)</code></pre>
     *
     * @param <T>      Component type of the ChampSet.
     * @param elements Zero or more elements.
     * @return A set containing the given elements.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> HashSet<T> of(T... elements) {
        //Arrays.asList throws a NullPointerException for us.
        return HashSet.<T>empty().addAll(Arrays.asList(elements));
    }
    /**
     * Creates a HashSet from boolean values.
     *
     * @param elements boolean values
     * @return A new HashSet of Boolean values
     * @throws NullPointerException if elements is null
     */
    public static HashSet<Boolean> ofAll(boolean... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return HashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a HashSet from byte values.
     *
     * @param elements byte values
     * @return A new HashSet of Byte values
     * @throws NullPointerException if elements is null
     */
    public static HashSet<Byte> ofAll(byte... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return HashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a HashSet from char values.
     *
     * @param elements char values
     * @return A new HashSet of Character values
     * @throws NullPointerException if elements is null
     */
    public static HashSet<Character> ofAll(char... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return HashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a HashSet from double values.
     *
     * @param elements double values
     * @return A new HashSet of Double values
     * @throws NullPointerException if elements is null
     */
    public static HashSet<Double> ofAll(double... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return HashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a HashSet from float values.
     *
     * @param elements float values
     * @return A new HashSet of Float values
     * @throws NullPointerException if elements is null
     */
    public static HashSet<Float> ofAll(float... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return HashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a HashSet from int values.
     *
     * @param elements int values
     * @return A new HashSet of Integer values
     * @throws NullPointerException if elements is null
     */
    public static HashSet<Integer> ofAll(int... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return HashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a HashSet from long values.
     *
     * @param elements long values
     * @return A new HashSet of Long values
     * @throws NullPointerException if elements is null
     */
    public static HashSet<Long> ofAll(long... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return HashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a HashSet from short values.
     *
     * @param elements short values
     * @return A new HashSet of Short values
     * @throws NullPointerException if elements is null
     */
    public static HashSet<Short> ofAll(short... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return HashSet.ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a ChampSet of the given elements.
     *
     * @param elements Set elements
     * @param <T>      The value type
     * @return A new ChampSet containing the given entries
     */
    @SuppressWarnings("unchecked")
    public static <T> HashSet<T> ofAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof HashSet) {
            return (HashSet<T>) elements;
        } else {
            return HashSet.<T>of().addAll(elements);
        }
    }

    /**
     * Creates a HashSet that contains the elements of the given {@link java.util.stream.Stream}.
     *
     * @param javaStream A {@link java.util.stream.Stream}
     * @param <T>        Component type of the Stream.
     * @return A HashSet containing the given elements in the same order.
     */
    public static <T> HashSet<T> ofAll(java.util.stream.Stream<? extends T> javaStream) {
        Objects.requireNonNull(javaStream, "javaStream is null");
        return HashSet.ofAll(Iterator.ofAll(javaStream.iterator()));
    }

    /**
     * Returns an HashSet containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param <T> Component type of the HashSet
     * @param n   The number of elements in the HashSet
     * @param f   The Function computing element values
     * @return An HashSet consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    public static <T> HashSet<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        return Collections.tabulate(n, f, HashSet.empty(), HashSet::of);
    }

    /**
     * Returns a HashSet containing tuples returned by {@code n} calls to a given Supplier {@code s}.
     *
     * @param <T> Component type of the HashSet
     * @param n   The number of elements in the HashSet
     * @param s   The Supplier computing element values
     * @return An HashSet of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    public static <T> HashSet<T> fill(int n, Supplier<? extends T> s) {
        Objects.requireNonNull(s, "s is null");
        return Collections.fill(n, s, HashSet.empty(), HashSet::of);
    }

    /**
     * Creates a HashSet of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * HashSet.range(0, 0)  // = HashSet()
     * HashSet.range(2, 0)  // = HashSet()
     * HashSet.range(-2, 2) // = HashSet(-2, -1, 0, 1)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or the empty range if {@code from >= toExclusive}
     */
    public static HashSet<Integer> range(int from, int toExclusive) {
        return HashSet.ofAll(Iterator.range(from, toExclusive));
    }

    public static HashSet<Character> range(char from, char toExclusive) {
        return HashSet.ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a HashSet of int numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * HashSet.rangeBy(1, 3, 1)  // = HashSet(1, 2)
     * HashSet.rangeBy(1, 4, 2)  // = HashSet(1, 3)
     * HashSet.rangeBy(4, 1, -2) // = HashSet(4, 2)
     * HashSet.rangeBy(4, 1, 2)  // = HashSet()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or the empty range if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static HashSet<Integer> rangeBy(int from, int toExclusive, int step) {
        return HashSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static HashSet<Character> rangeBy(char from, char toExclusive, int step) {
        return HashSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static HashSet<Double> rangeBy(double from, double toExclusive, double step) {
        return HashSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a HashSet of long numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * HashSet.range(0L, 0L)  // = HashSet()
     * HashSet.range(2L, 0L)  // = HashSet()
     * HashSet.range(-2L, 2L) // = HashSet(-2L, -1L, 0L, 1L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of long values as specified or the empty range if {@code from >= toExclusive}
     */
    public static HashSet<Long> range(long from, long toExclusive) {
        return HashSet.ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a HashSet of long numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * HashSet.rangeBy(1L, 3L, 1L)  // = HashSet(1L, 2L)
     * HashSet.rangeBy(1L, 4L, 2L)  // = HashSet(1L, 3L)
     * HashSet.rangeBy(4L, 1L, -2L) // = HashSet(4L, 2L)
     * HashSet.rangeBy(4L, 1L, 2L)  // = HashSet()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or the empty range if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static HashSet<Long> rangeBy(long from, long toExclusive, long step) {
        return HashSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a HashSet of int numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * HashSet.rangeClosed(0, 0)  // = HashSet(0)
     * HashSet.rangeClosed(2, 0)  // = HashSet()
     * HashSet.rangeClosed(-2, 2) // = HashSet(-2, -1, 0, 1, 2)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or the empty range if {@code from > toInclusive}
     */
    public static HashSet<Integer> rangeClosed(int from, int toInclusive) {
        return HashSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    public static HashSet<Character> rangeClosed(char from, char toInclusive) {
        return HashSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a HashSet of int numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * HashSet.rangeClosedBy(1, 3, 1)  // = HashSet(1, 2, 3)
     * HashSet.rangeClosedBy(1, 4, 2)  // = HashSet(1, 3)
     * HashSet.rangeClosedBy(4, 1, -2) // = HashSet(4, 2)
     * HashSet.rangeClosedBy(4, 1, 2)  // = HashSet()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or the empty range if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static HashSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return HashSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    public static HashSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return HashSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    public static HashSet<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return HashSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a HashSet of long numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * HashSet.rangeClosed(0L, 0L)  // = HashSet(0L)
     * HashSet.rangeClosed(2L, 0L)  // = HashSet()
     * HashSet.rangeClosed(-2L, 2L) // = HashSet(-2L, -1L, 0L, 1L, 2L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of long values as specified or the empty range if {@code from > toInclusive}
     */
    public static HashSet<Long> rangeClosed(long from, long toInclusive) {
        return HashSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a HashSet of long numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * HashSet.rangeClosedBy(1L, 3L, 1L)  // = HashSet(1L, 2L, 3L)
     * HashSet.rangeClosedBy(1L, 4L, 2L)  // = HashSet(1L, 3L)
     * HashSet.rangeClosedBy(4L, 1L, -2L) // = HashSet(4L, 2L)
     * HashSet.rangeClosedBy(4L, 1L, 2L)  // = HashSet()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or the empty range if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static HashSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return HashSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> HashSet<Tuple2<E, U>> zip(Iterable<? extends U> that) {
        return (HashSet<Tuple2<E, U>>) (HashSet<?>) VavrSetMixin.super.zip(that);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> HashSet<Tuple2<E, U>> zipAll(Iterable<? extends U> that, E thisElem, U thatElem) {
        return (HashSet<Tuple2<E, U>>) (HashSet<?>) VavrSetMixin.super.zipAll(that, thisElem, thatElem);
    }

    @SuppressWarnings("unchecked")
    @Override
    public HashSet<Tuple2<E, Integer>> zipWithIndex() {
        return (HashSet<Tuple2<E, Integer>>) (HashSet<?>) VavrSetMixin.super.zipWithIndex();
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    static class SerializationProxy<E> extends SetSerializationProxy<E> {
        @Serial
        private final static long serialVersionUID = 0L;

        public SerializationProxy(java.util.Set<E> target) {
            super(target);
        }

        @Serial
        @Override
        protected Object readResolve() {
            return HashSet.<E>empty().addAll(deserialized);
        }
    }

    @Serial
    private Object writeReplace() {
        return new SerializationProxy<E>(this.toMutable());
    }

    @Override
    public HashSet<E> dropRight(int n) {
        return drop(n);
    }

    @Override
    public HashSet<E> takeRight(int n) {
        return take(n);
    }

    @Override
    public HashSet<E> init() {
        return tail();
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super E, ? super U, ? extends U> combine) {
        Objects.requireNonNull(combine, "combine is null");
        return foldLeft(zero, (u, t) -> combine.apply(t, u));
    }

    /**
     * Creates a mutable copy of this set.
     * The copy is an instance of {@link MutableHashSet}.
     *
     * @return a mutable copy of this set.
     */
    @Override
    public MutableHashSet<E> toJavaSet() {
        return toMutable();
    }

    /**
     * Narrows a widened {@code ChampSet<? extends T>} to {@code ChampSet<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param hashSet A {@code ChampSet}.
     * @param <T>     Component type of the {@code ChampSet}.
     * @return the given {@code ChampSet} instance as narrowed type {@code HashSet<T>}.
     */
    @SuppressWarnings("unchecked")
    public static <T> HashSet<T> narrow(HashSet<? extends T> hashSet) {
        return (HashSet<T>) hashSet;
    }
}
