package io.vavr.collection.champ;

import io.vavr.collection.Collections;
import io.vavr.collection.Iterator;
import io.vavr.collection.Set;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
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
public class HashSet<E> extends ChampPackage.BitmapIndexedNode<E> implements ChampPackage.VavrSetMixin<E, HashSet<E>>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final HashSet<?> EMPTY = new HashSet<>(ChampPackage.BitmapIndexedNode.emptyNode(), 0);
    final int size;

    HashSet(ChampPackage.BitmapIndexedNode<E> root, int size) {
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
        ChampPackage.ChangeEvent<E> details = new ChampPackage.ChangeEvent<>();
        ChampPackage.BitmapIndexedNode<E> newRootNode = update(null, key, keyHash, 0, details, getUpdateFunction(), Objects::equals, Objects::hashCode);
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
        return find(o, Objects.hashCode(o), 0, Objects::equals) != ChampPackage.Node.NO_DATA;
    }

    private BiFunction<E, E, E> getUpdateFunction() {
        return (oldk, newk) -> oldk;
    }

    @Override
    public Iterator<E> iterator() {
        return new ChampPackage.KeyIterator<E>(this, null);
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public Set<E> remove(E key) {
        int keyHash = Objects.hashCode(key);
        ChampPackage.ChangeEvent<E> details = new ChampPackage.ChangeEvent<>();
        ChampPackage.BitmapIndexedNode<E> newRootNode = remove(null, key, keyHash, 0, details, Objects::equals);
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

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    static class SerializationProxy<E> extends ChampPackage.SetSerializationProxy<E> {
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
