package io.vavr.collection.champ;

import io.vavr.collection.Collections;
import io.vavr.collection.Iterator;
import io.vavr.collection.Set;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;
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
public class ChampSet<E> extends BitmapIndexedNode<E> implements SetMixin<E, ChampSet<E>>, Serializable {
    private static final long serialVersionUID = 1L;
    private static final ChampSet<?> EMPTY = new ChampSet<>(BitmapIndexedNode.emptyNode(), 0);
    final int size;

    ChampSet(BitmapIndexedNode<E> root, int size) {
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
    public static <E> ChampSet<E> empty() {
        return ((ChampSet<E>) ChampSet.EMPTY);
    }

    @Override
    public <R> Set<R> create() {
        return empty();
    }

    @Override
    public <R> ChampSet<R> createFromElements(Iterable<? extends R> elements) {
        return ChampSet.<R>empty().addAll(elements);
    }

    @Override
    public ChampSet<E> add(E key) {
        int keyHash = Objects.hashCode(key);
        ChangeEvent<E> details = new ChangeEvent<>();
        BitmapIndexedNode<E> newRootNode = update(null, key, keyHash, 0, details, getUpdateFunction(), getEqualsFunction(), getHashFunction());
        if (details.isModified()) {
            return new ChampSet<>(newRootNode, size + 1);
        }
        return this;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public ChampSet<E> addAll(Iterable<? extends E> set) {
        if (set == this || isEmpty() && (set instanceof ChampSet<?>)) {
            return (ChampSet<E>) set;
        }
        if (isEmpty() && (set instanceof MutableChampSet)) {
            return ((MutableChampSet<E>) set).toImmutable();
        }
        MutableChampSet<E> t = toMutable();
        boolean modified = false;
        for (E key : set) {
            modified |= t.add(key);
        }
        return modified ? t.toImmutable() : this;
    }

    @Override
    public boolean contains(E o) {
        return findByData(o, Objects.hashCode(o), 0, getEqualsFunction()) != Node.NO_DATA;
    }

    private BiPredicate<E, E> getEqualsFunction() {
        return Objects::equals;
    }

    private ToIntFunction<E> getHashFunction() {
        return Objects::hashCode;
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
        BitmapIndexedNode<E> newRootNode = remove(null, key, keyHash, 0, details, getEqualsFunction());
        if (details.isModified()) {
            return new ChampSet<>(newRootNode, size - 1);
        }
        return this;
    }

    /**
     * Creates a mutable copy of this set.
     *
     * @return a mutable copy of this set.
     */
    MutableChampSet<E> toMutable() {
        return new MutableChampSet<>(this);
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link ChampSet}.
     *
     * @param <T> Component type of the HashSet.
     * @return A io.vavr.collection.ChampSet Collector.
     */
    public static <T> Collector<T, ArrayList<T>, ChampSet<T>> collector() {
        return Collections.toListAndThen(iterable -> ChampSet.<T>empty().addAll(iterable));
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other instanceof ChampSet) {
            ChampSet<?> that = (ChampSet<?>) other;
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
    public static <T> ChampSet<T> of(T... elements) {
        //Arrays.asList throws a NullPointerException for us.
        return ChampSet.<T>empty().addAll(Arrays.asList(elements));
    }

    /**
     * Creates a ChampSet of the given elements.
     *
     * @param elements Set elements
     * @param <T>      The value type
     * @return A new ChampSet containing the given entries
     */
    @SuppressWarnings("unchecked")
    public static <T> ChampSet<T> ofAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof ChampSet) {
            return (ChampSet<T>) elements;
        } else {
            return ChampSet.<T>of().addAll(elements);
        }
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    public static class SerializationProxy<E> extends SetSerializationProxy<E> {
        private final static long serialVersionUID = 0L;

        public SerializationProxy(java.util.Set<E> target) {
            super(target);
        }

        @Override
        protected Object readResolve() {
            return ChampSet.<E>empty().addAll(deserialized);
        }
    }

    private Object writeReplace() {
        return new SerializationProxy<E>(this.toMutable());
    }

    @Override
    public Set<E> dropRight(int n) {
        return drop(n);
    }

    @Override
    public Set<E> takeRight(int n) {
        return take(n);
    }

    @Override
    public Set<E> init() {
        return tail();
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super E, ? super U, ? extends U> combine) {
        Objects.requireNonNull(combine, "combine is null");
        return foldLeft(zero, (u, t) -> combine.apply(t, u));
    }

    /**
     * Creates a mutable copy of this set.
     * The copy is an instance of {@link MutableChampSet}.
     *
     * @return a mutable copy of this set.
     */
    @Override
    public MutableChampSet<E> toJavaSet() {
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
    public static <T> ChampSet<T> narrow(ChampSet<? extends T> hashSet) {
        return (ChampSet<T>) hashSet;
    }
}
