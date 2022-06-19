package io.vavr.collection;

import io.vavr.collection.champ.BitmapIndexedNode;
import io.vavr.collection.champ.ChangeEvent;
import io.vavr.collection.champ.KeyIterator;
import io.vavr.collection.champ.Node;

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
 *     <li>toMutable: O(1) + a cost distributed across subsequent updates in the mutable copy</li>
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
 * This set can create a mutable copy of itself in O(1) time and O(0) space
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
public class ChampSet<E> extends BitmapIndexedNode<E> implements SetMixin<E>, Serializable {
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

    /**
     * Returns an immutable set that contains the provided elements.
     *
     * @param iterable an iterable
     * @param <E>      the element type
     * @return an immutable set of the provided elements
     */
    @SuppressWarnings("unchecked")
    public static <E> ChampSet<E> ofAll(Iterable<? extends E> iterable) {
        return ((ChampSet<E>) ChampSet.EMPTY).addAll(iterable);
    }

    @Override
    public <R> Set<R> clear() {
        return empty();
    }

    @Override
    public <R> ChampSet<R> setAll(Iterable<? extends R> elements) {
        return ofAll(elements);
    }

    @Override
    public ChampSet<E> add(E key) {
        int keyHash = Objects.hashCode(key);
        ChangeEvent<E> changeEvent = new ChangeEvent<>();
        BitmapIndexedNode<E> newRootNode = update(null, key, keyHash, 0, changeEvent, getUpdateFunction(), getEqualsFunction(), getHashFunction());
        if (changeEvent.modified) {
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
        return findByKey(o, Objects.hashCode(o), 0, getEqualsFunction()) != Node.NO_VALUE;
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
        ChangeEvent<E> changeEvent = new ChangeEvent<>();
        BitmapIndexedNode<E> newRootNode = remove(null, key, keyHash, 0, changeEvent, getEqualsFunction());
        if (changeEvent.modified) {
            return new ChampSet<>(newRootNode, size - 1);
        }
        return this;
    }

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
        return Collections.toListAndThen(ChampSet::ofAll);
    }

    /**
     * Returns a singleton {@code HashSet}, i.e. a {@code HashSet} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new HashSet instance containing the given element
     */
    public static <T> ChampSet<T> of(T element) {
        return ChampSet.<T>empty().add(element);
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

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    private static class SerializationProxy<E> extends SetSerializationProxy<E> {
        private final static long serialVersionUID = 0L;

        protected SerializationProxy(java.util.Set<E> target) {
            super(target);
        }

        @Override
        protected Object readResolve() {
            return ChampSet.ofAll(deserialized);
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
}
