package io.vavr.collection.champ;

import io.vavr.Tuple2;

import java.io.Serial;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

/**
 * Implements a mutable map using a Compressed Hash-Array Mapped Prefix-tree
 * (CHAMP).
 * <p>
 * Features:
 * <ul>
 *     <li>supports up to 2<sup>30</sup> entries</li>
 *     <li>allows null keys and null values</li>
 *     <li>is mutable</li>
 *     <li>is not thread-safe</li>
 *     <li>does not guarantee a specific iteration order</li>
 * </ul>
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>put: O(1)</li>
 *     <li>remove: O(1)</li>
 *     <li>containsKey: O(1)</li>
 *     <li>toImmutable: O(1) + O(log N) distributed across subsequent updates in
 *     this map</li>
 *     <li>clone: O(1) + O(log N) distributed across subsequent updates in this
 *     map and in the clone</li>
 *     <li>iterator.next: O(1)</li>
 * </ul>
 * <p>
 * Implementation details:
 * <p>
 * This map performs read and write operations of single elements in O(1) time,
 * and in O(1) space.
 * <p>
 * The CHAMP tree contains nodes that may be shared with other maps, and nodes
 * that are exclusively owned by this map.
 * <p>
 * If a write operation is performed on an exclusively owned node, then this
 * map is allowed to mutate the node (mutate-on-write).
 * If a write operation is performed on a potentially shared node, then this
 * map is forced to create an exclusive copy of the node and of all not (yet)
 * exclusively owned parent nodes up to the root (copy-path-on-write).
 * Since the CHAMP tree has a fixed maximal height, the cost is O(1) in either
 * case.
 * <p>
 * This map can create an immutable copy of itself in O(1) time and O(1) space
 * using method {@link #toImmutable()}. This map loses exclusive ownership of
 * all its tree nodes.
 * Thus, creating an immutable copy increases the constant cost of
 * subsequent writes, until all shared nodes have been gradually replaced by
 * exclusively owned nodes again.
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
public class MutableHashMap<K, V> extends ChampPackage.AbstractChampMap<K, V, AbstractMap.SimpleImmutableEntry<K, V>> {
    @Serial
    private final static long serialVersionUID = 0L;

    public MutableHashMap() {
        root = ChampPackage.BitmapIndexedNode.emptyNode();
    }

    public MutableHashMap(Map<? extends K, ? extends V> m) {
        if (m instanceof MutableHashMap) {
            @SuppressWarnings("unchecked")
            MutableHashMap<K, V> that = (MutableHashMap<K, V>) m;
            this.mutator = null;
            that.mutator = null;
            this.root = that.root;
            this.size = that.size;
            this.modCount = 0;
        } else {
            this.root = ChampPackage.BitmapIndexedNode.emptyNode();
            this.putAll(m);
        }
    }

    public MutableHashMap(io.vavr.collection.Map<? extends K, ? extends V> m) {
        if (m instanceof HashMap) {
            @SuppressWarnings("unchecked")
            HashMap<K, V> that = (HashMap<K, V>) m;
            this.root = that;
            this.size = that.size();
        } else {
            this.root = ChampPackage.BitmapIndexedNode.emptyNode();
            this.putAll(m);
        }
    }

    public MutableHashMap(Iterable<? extends Entry<? extends K, ? extends V>> m) {
        this.root = ChampPackage.BitmapIndexedNode.emptyNode();
        for (Entry<? extends K, ? extends V> e : m) {
            this.put(e.getKey(), e.getValue());
        }
    }

    /**
     * Removes all mappings from this map.
     */
    @Override
    public void clear() {
        root = ChampPackage.BitmapIndexedNode.emptyNode();
        size = 0;
        modCount++;
    }

    /**
     * Returns a shallow copy of this map.
     */
    @Override
    public MutableHashMap<K, V> clone() {
        return (MutableHashMap<K, V>) super.clone();
    }


    @Override
    @SuppressWarnings("unchecked")
    public boolean containsKey(Object o) {
        return root.find(new AbstractMap.SimpleImmutableEntry<>((K) o, null),
                Objects.hashCode(o), 0,
                getEqualsFunction()) != ChampPackage.Node.NO_DATA;
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     *
     * @return a view of the mappings contained in this map
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        return new ChampPackage.JavaSetFacade<>(
                () -> new ChampPackage.MappedIterator<>(new ChampPackage.FailFastIterator<>(new ChampPackage.KeyIterator<>(
                        root,
                        this::iteratorRemove),
                        () -> this.modCount),
                        e -> new ChampPackage.MutableMapEntry<>(this::iteratorPutIfPresent, e.getKey(), e.getValue())),
                MutableHashMap.this::size,
                MutableHashMap.this::containsEntry,
                MutableHashMap.this::clear,
                null,
                MutableHashMap.this::removeEntry
        );
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param o the key whose associated value is to be returned
     * @return the associated value or null
     */
    @Override
    @SuppressWarnings("unchecked")
    public V get(Object o) {
        Object result = root.find(new AbstractMap.SimpleImmutableEntry<>((K) o, null),
                Objects.hashCode(o), 0, getEqualsFunction());
        return result == ChampPackage.Node.NO_DATA || result == null ? null : ((SimpleImmutableEntry<K, V>) result).getValue();
    }


    private BiPredicate<AbstractMap.SimpleImmutableEntry<K, V>, AbstractMap.SimpleImmutableEntry<K, V>> getEqualsFunction() {
        return (a, b) -> Objects.equals(a.getKey(), b.getKey());
    }


    private ToIntFunction<AbstractMap.SimpleImmutableEntry<K, V>> getHashFunction() {
        return (a) -> Objects.hashCode(a.getKey());
    }


    private BiFunction<AbstractMap.SimpleImmutableEntry<K, V>, AbstractMap.SimpleImmutableEntry<K, V>, AbstractMap.SimpleImmutableEntry<K, V>> getUpdateFunction() {
        return (oldv, newv) -> Objects.equals(oldv.getValue(), newv.getValue()) ? oldv : newv;
    }

    private void iteratorPutIfPresent(K k, V v) {
        if (containsKey(k)) {
            mutator = null;
            put(k, v);
        }
    }

    private void iteratorRemove(AbstractMap.SimpleImmutableEntry<K, V> entry) {
        mutator = null;
        remove(entry.getKey());
    }

    @Override
    public V put(K key, V value) {
        SimpleImmutableEntry<K, V> oldValue = putAndGiveDetails(key, value).getData();
        return oldValue == null ? null : oldValue.getValue();
    }

    ChampPackage.ChangeEvent<SimpleImmutableEntry<K, V>> putAndGiveDetails(K key, V val) {
        int keyHash = Objects.hashCode(key);
        ChampPackage.ChangeEvent<SimpleImmutableEntry<K, V>> details = new ChampPackage.ChangeEvent<>();
        root = root.update(getOrCreateIdentity(), new AbstractMap.SimpleImmutableEntry<>(key, val), keyHash, 0, details,
                getUpdateFunction(),
                getEqualsFunction(),
                getHashFunction());
        if (details.isModified() && !details.isReplaced()) {
            size += 1;
            modCount++;
        }
        return details;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        // XXX We can putAll much faster if m is a MutableChampMap!
        //        if (m instanceof MutableChampMap) {
        //           newRootNode = root.updateAll(...);
        //           ...
        //           return;
        //         }
        super.putAll(m);
    }

    public void putAll(io.vavr.collection.Map<? extends K, ? extends V> m) {
        // XXX We can putAll much faster if m is a ChampMap!
        //        if (m instanceof ChampMap) {
        //           newRootNode = root.updateAll(...);
        //           ...
        //           return;
        //         }
        for (Tuple2<? extends K, ? extends V> e : m) {
            put(e._1, e._2);
        }
    }

    @Override
    public V remove(Object o) {
        @SuppressWarnings("unchecked") final K key = (K) o;
        SimpleImmutableEntry<K, V> oldValue = removeAndGiveDetails(key).getData();
        return oldValue == null ? null : oldValue.getValue();
    }

    ChampPackage.ChangeEvent<SimpleImmutableEntry<K, V>> removeAndGiveDetails(final K key) {
        final int keyHash = Objects.hashCode(key);
        final ChampPackage.ChangeEvent<SimpleImmutableEntry<K, V>> details = new ChampPackage.ChangeEvent<>();
        root = root.remove(getOrCreateIdentity(), new AbstractMap.SimpleImmutableEntry<>(key, null), keyHash, 0, details,
                getEqualsFunction());
        if (details.isModified()) {
            size = size - 1;
            modCount++;
        }
        return details;
    }

    @SuppressWarnings("unchecked")
    boolean removeEntry(final Object o) {
        if (containsEntry(o)) {
            assert o != null;
            @SuppressWarnings("unchecked") Entry<K, V> entry = (Entry<K, V>) o;
            remove(entry.getKey());
            return true;
        }
        return false;
    }

    /**
     * Returns an immutable copy of this map.
     *
     * @return an immutable copy
     */
    public HashMap<K, V> toImmutable() {
        mutator = null;
        return size == 0 ? HashMap.empty() : new HashMap<>(root, size);
    }

    @Serial
    private Object writeReplace() {
        return new SerializationProxy<>(this);
    }

    private static class SerializationProxy<K, V> extends ChampPackage.MapSerializationProxy<K, V> {
        @Serial
        private final static long serialVersionUID = 0L;

        protected SerializationProxy(Map<K, V> target) {
            super(target);
        }

        @Serial
        @Override
        protected Object readResolve() {
            return new MutableHashMap<>(deserialized);
        }
    }
}
