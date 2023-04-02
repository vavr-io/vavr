package io.vavr.collection.champ;

import io.vavr.Tuple2;
import io.vavr.collection.Collections;
import io.vavr.collection.Iterator;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import io.vavr.control.Option;

import java.io.ObjectStreamException;
import java.util.AbstractMap;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Implements an immutable map using a Compressed Hash-Array Mapped Prefix-tree
 * (CHAMP).
 * <p>
 * Features:
 * <ul>
 *     <li>supports up to 2<sup>30</sup> entries</li>
 *     <li>allows null keys and null values</li>
 *     <li>is immutable</li>
 *     <li>is thread-safe</li>
 *     <li>does not guarantee a specific iteration order</li>
 * </ul>
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>copyPut: O(1)</li>
 *     <li>copyRemove: O(1)</li>
 *     <li>containsKey: O(1)</li>
 *     <li>toMutable: O(1) + O(log N) distributed across subsequent updates in the mutable copy</li>
 *     <li>clone: O(1)</li>
 *     <li>iterator.next(): O(1)</li>
 * </ul>
 * <p>
 * Implementation details:
 * <p>
 * This map performs read and write operations of single elements in O(1) time,
 * and in O(1) space.
 * <p>
 * The CHAMP tree contains nodes that may be shared with other maps.
 * <p>
 * If a write operation is performed on a node, then this map creates a
 * copy of the node and of all parent nodes up to the root (copy-path-on-write).
 * Since the CHAMP tree has a fixed maximal height, the cost is O(1).
 * <p>
 * This map can create a mutable copy of itself in O(1) time and O(1) space
 * using method {@link #toMutable()}}. The mutable copy shares its nodes
 * with this map, until it has gradually replaced the nodes with exclusively
 * owned nodes.
 * <p>
 * All operations on this set can be performed concurrently, without a need for
 * synchronisation.
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
 * @param <K> the key type
 * @param <V> the value type
 */
public class ChampMap<K, V> extends BitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>>
        implements VavrMapMixin<K, V> {
    private static final ChampMap<?, ?> EMPTY = new ChampMap<>(BitmapIndexedNode.emptyNode(), 0);
    private final static long serialVersionUID = 0L;
    private final int size;

    ChampMap(BitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> root, int size) {
        super(root.nodeMap(), root.dataMap(), root.mixed);
        this.size = size;
    }

    /**
     * Returns an empty immutable map.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return an empty immutable map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> ChampMap<K, V> empty() {
        return (ChampMap<K, V>) ChampMap.EMPTY;
    }

    static <V, K> boolean keyEquals(AbstractMap.SimpleImmutableEntry<K, V> a, AbstractMap.SimpleImmutableEntry<K, V> b) {
        return Objects.equals(a.getKey(), b.getKey());
    }

    static <V, K> int keyHash(AbstractMap.SimpleImmutableEntry<K, V> e) {
        return Objects.hashCode(e.getKey());
    }

    /**
     * Narrows a widened {@code HashMap<? extends K, ? extends V>} to {@code ChampMap<K, V>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param hashMap A {@code HashMap}.
     * @param <K>     Key type
     * @param <V>     Value type
     * @return the given {@code hashMap} instance as narrowed type {@code ChampMap<K, V>}.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> ChampMap<K, V> narrow(ChampMap<? extends K, ? extends V> hashMap) {
        return (ChampMap<K, V>) hashMap;
    }

    /**
     * Returns a {@code ChampMap}, from a source java.util.Map.
     *
     * @param map A map
     * @param <K> The key type
     * @param <V> The value type
     * @return A new ChampMap containing the given map
     */
    public static <K, V> ChampMap<K, V> ofAll(java.util.Map<? extends K, ? extends V> map) {
        return ChampMap.<K, V>empty().putAllEntries(map.entrySet());
    }

    /**
     * Creates a ChampMap of the given entries.
     *
     * @param entries Entries
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new ChampMap containing the given entries
     */
    public static <K, V> ChampMap<K, V> ofEntries(Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> entries) {
        return ChampMap.<K, V>empty().putAllEntries(entries);
    }

    /**
     * Creates a ChampMap of the given tuples.
     *
     * @param entries Tuples
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new ChampMap containing the given tuples
     */
    public static <K, V> ChampMap<K, V> ofTuples(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        return ChampMap.<K, V>empty().putAllTuples(entries);
    }

    @Override
    public boolean containsKey(K key) {
        return find(new AbstractMap.SimpleImmutableEntry<>(key, null), Objects.hashCode(key), 0,
                ChampMap::keyEquals) != Node.NO_DATA;
    }

    /**
     * Creates an empty map of the specified key and value types.
     *
     * @param <L> the key type of the map
     * @param <U> the value type of the map
     * @return a new empty map.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <L, U> ChampMap<L, U> create() {
        return isEmpty() ? (ChampMap<L, U>) this : empty();
    }

    /**
     * Creates an empty map of the specified key and value types,
     * and adds all the specified entries.
     *
     * @param entries the entries
     * @param <L>     the key type of the map
     * @param <U>     the value type of the map
     * @return a new map contains the specified entries.
     */
    @Override
    public <L, U> Map<L, U> createFromEntries(Iterable<? extends Tuple2<? extends L, ? extends U>> entries) {
        return ChampMap.<L, U>empty().putAllTuples(entries);
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other instanceof ChampMap) {
            ChampMap<?, ?> that = (ChampMap<?, ?>) other;
            return size == that.size && equivalent(that);
        } else {
            return Collections.equals(this, other);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Option<V> get(K key) {
        Object result = find(new AbstractMap.SimpleImmutableEntry<>(key, null), Objects.hashCode(key), 0, ChampMap::keyEquals);
        return result == Node.NO_DATA || result == null
                ? Option.none()
                : Option.some(((AbstractMap.SimpleImmutableEntry<K, V>) result).getValue());
    }

    private BiFunction<AbstractMap.SimpleImmutableEntry<K, V>, AbstractMap.SimpleImmutableEntry<K, V>, AbstractMap.SimpleImmutableEntry<K, V>> getUpdateFunction() {
        // XXX ChampMapTest.shouldPutExistingKeyAndEqualValue wants us to replace the existing key,
        //        if it is not the same as the new key. This behavior is different from java.util.Map collections!
        return (oldv, newv) -> oldv.getKey() == newv.getKey() && Objects.equals(oldv.getValue(), newv.getValue()) ? oldv : newv;
    }

    @Override
    public int hashCode() {
        return Collections.hashUnordered(this);
    }

    @Override
    public Iterator<Tuple2<K, V>> iterator() {
        return new MappedIterator<>(new KeyIterator<>(this, null),
                e -> new Tuple2<>(e.getKey(), e.getValue()));
    }

    @Override
    public Set<K> keySet() {
        return new VavrSetFacade<>(this);
    }

    @Override
    public ChampMap<K, V> put(K key, V value) {
        final int keyHash = Objects.hashCode(key);
        final ChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details = new ChangeEvent<>();
        final BitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> newRootNode = update(null, new AbstractMap.SimpleImmutableEntry<>(key, value),
                keyHash, 0, details,
                getUpdateFunction(), ChampMap::keyEquals, ChampMap::keyHash);
        if (details.isModified()) {
            if (details.isReplaced()) {
                return new ChampMap<>(newRootNode, size);
            }
            return new ChampMap<>(newRootNode, size + 1);
        }
        return this;
    }

    private ChampMap<K, V> putAllEntries(Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> entries) {
        final MutableChampMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (java.util.Map.Entry<? extends K, ? extends V> entry : entries) {
            ChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details =
                    t.putAndGiveDetails(entry.getKey(), entry.getValue());
            modified |= details.isModified();
        }
        return modified ? t.toImmutable() : this;
    }

    private ChampMap<K, V> putAllTuples(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        final MutableChampMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            ChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details =
                    t.putAndGiveDetails(entry._1(), entry._2());
            modified |= details.isModified();
        }
        return modified ? t.toImmutable() : this;
    }

    @Override
    public ChampMap<K, V> remove(K key) {
        final int keyHash = Objects.hashCode(key);
        final ChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details = new ChangeEvent<>();
        final BitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> newRootNode =
                remove(null, new AbstractMap.SimpleImmutableEntry<>(key, null), keyHash, 0, details,
                        ChampMap::keyEquals);
        if (details.isModified()) {
            return new ChampMap<>(newRootNode, size - 1);
        }
        return this;
    }

    @Override
    public ChampMap<K, V> removeAll(Iterable<? extends K> keys) {
        if (this.isEmpty()) {
            return this;
        }
        final MutableChampMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (K key : keys) {
            ChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details = t.removeAndGiveDetails(key);
            modified |= details.isModified();
        }
        return modified ? t.toImmutable() : this;
    }

    @Override
    public Map<K, V> retainAll(Iterable<? extends Tuple2<K, V>> elements) {
        Objects.requireNonNull(elements, "elements is null");
        MutableChampMap<K, V> m = new MutableChampMap<>();
        for (Tuple2<K, V> entry : elements) {
            if (contains(entry)) {
                m.put(entry._1, entry._2);
            }
        }
        return m.toImmutable();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ChampMap<K, V> tail() {
        // XXX ChampMapTest.shouldThrowWhenTailEmpty wants us to throw
        //       UnsupportedOperationException instead of NoSuchElementException.
        if (isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return remove(iterator().next()._1);
    }

    @Override
    public MutableChampMap<K, V> toJavaMap() {
        return toMutable();
    }

    /**
     * Creates a mutable copy of this map.
     *
     * @return a mutable CHAMP map
     */
    public MutableChampMap<K, V> toMutable() {
        return new MutableChampMap<>(this);
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    @Override
    public Stream<V> values() {
        return new MappedIterator<>(iterator(), Tuple2::_2).toStream();
    }

    private Object writeReplace() throws ObjectStreamException {
        return new SerializationProxy<>(this.toMutable());
    }

    static class SerializationProxy<K, V> extends MapSerializationProxy<K, V> {
        private final static long serialVersionUID = 0L;

        SerializationProxy(java.util.Map<K, V> target) {
            super(target);
        }

        @Override
        protected Object readResolve() {
            return ChampMap.empty().putAllEntries(deserialized);
        }
    }
}
