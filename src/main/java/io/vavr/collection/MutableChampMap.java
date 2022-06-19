package io.vavr.collection;

import io.vavr.Tuple2;
import io.vavr.collection.champ.BitmapIndexedNode;
import io.vavr.collection.champ.ChangeEvent;
import io.vavr.collection.champ.FailFastIterator;
import io.vavr.collection.champ.KeyIterator;
import io.vavr.collection.champ.MappedIterator;
import io.vavr.collection.champ.MutableMapEntry;
import io.vavr.collection.champ.Node;
import io.vavr.collection.champ.WrappedSet;

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
 *     <li>toImmutable: O(1) + a cost distributed across subsequent updates in
 *     this map</li>
 *     <li>clone: O(1) + a cost distributed across subsequent updates in this
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
public class MutableChampMap<K, V> extends AbstractChampMap<K, V, AbstractMap.SimpleImmutableEntry<K, V>> {
    private final static long serialVersionUID = 0L;

    public MutableChampMap() {
        root = BitmapIndexedNode.emptyNode();
    }

    public MutableChampMap(Map<? extends K, ? extends V> m) {
        if (m instanceof MutableChampMap) {
            @SuppressWarnings("unchecked")
            MutableChampMap<K, V> that = (MutableChampMap<K, V>) m;
            this.mutator = null;
            that.mutator = null;
            this.root = that.root;
            this.size = that.size;
            this.modCount = 0;
        } else {
            this.root = BitmapIndexedNode.emptyNode();
            this.putAll(m);
        }
    }

    public MutableChampMap(io.vavr.collection.Map<? extends K, ? extends V> m) {
        if (m instanceof ChampMap) {
            @SuppressWarnings("unchecked")
            ChampMap<K, V> that = (ChampMap<K, V>) m;
            this.root = that;
            this.size = that.size();
        } else {
            this.root = BitmapIndexedNode.emptyNode();
            this.putAll(m);
        }
    }

    public MutableChampMap(Iterable<? extends Entry<? extends K, ? extends V>> m) {
        this.root = BitmapIndexedNode.emptyNode();
        for (Entry<? extends K, ? extends V> e : m) {
            this.put(e.getKey(), e.getValue());
        }
    }


    @Override
    public void clear() {
        root = BitmapIndexedNode.emptyNode();
        size = 0;
        modCount++;
    }

    /**
     * Returns a shallow copy of this map.
     */
    @Override
    public MutableChampMap<K, V> clone() {
        return (MutableChampMap<K, V>) super.clone();
    }


    @Override
    @SuppressWarnings("unchecked")
    public boolean containsKey(Object o) {
        return root.findByKey(new AbstractMap.SimpleImmutableEntry<>((K) o, null),
                Objects.hashCode(o), 0,
                getEqualsFunction()) != Node.NO_VALUE;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new WrappedSet<>(
                () -> new MappedIterator<>(new FailFastIterator<>(new KeyIterator<>(
                        root,
                        this::iteratorRemove),
                        () -> this.modCount),
                        e -> new MutableMapEntry<>(this::iteratorPutIfPresent, e.getKey(), e.getValue())),
                MutableChampMap.this::size,
                MutableChampMap.this::containsEntry,
                MutableChampMap.this::clear,
                null,
                MutableChampMap.this::removeEntry
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(Object o) {
        Object result = root.findByKey(new AbstractMap.SimpleImmutableEntry<>((K) o, null),
                Objects.hashCode(o), 0, getEqualsFunction());
        return result == Node.NO_VALUE || result == null ? null : ((SimpleImmutableEntry<K, V>) result).getValue();
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
        SimpleImmutableEntry<K, V> oldValue = putAndGiveDetails(key, value).getOldValue();
        return oldValue == null ? null : oldValue.getValue();
    }

    ChangeEvent<SimpleImmutableEntry<K, V>> putAndGiveDetails(K key, V val) {
        int keyHash = Objects.hashCode(key);
        ChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details = new ChangeEvent<>();
        BitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> newRootNode = root
                .update(getOrCreateMutator(), new AbstractMap.SimpleImmutableEntry<>(key, val), keyHash, 0, details,
                        getUpdateFunction(),
                        getEqualsFunction(),
                        getHashFunction());
        if (details.isModified()) {
            if (details.isUpdated()) {
                root = newRootNode;
            } else {
                root = newRootNode;
                size += 1;
                modCount++;
            }
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
        SimpleImmutableEntry<K, V> oldValue = removeAndGiveDetails(key).getOldValue();
        return oldValue == null ? null : oldValue.getValue();
    }

    ChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> removeAndGiveDetails(final K key) {
        final int keyHash = Objects.hashCode(key);
        final ChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details = new ChangeEvent<>();
        final BitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> newRootNode =
                root.remove(getOrCreateMutator(), new AbstractMap.SimpleImmutableEntry<>(key, null), keyHash, 0, details,
                        getEqualsFunction());
        if (details.isModified()) {
            root = newRootNode;
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
    public ChampMap<K, V> toImmutable() {
        mutator = null;
        return size == 0 ? ChampMap.empty() : new ChampMap<>(root, size);
    }

    private Object writeReplace() {
        return new SerializationProxy<>(this);
    }

    private static class SerializationProxy<K, V> extends MapSerializationProxy<K, V> {
        private final static long serialVersionUID = 0L;

        protected SerializationProxy(Map<K, V> target) {
            super(target);
        }

        @Override
        protected Object readResolve() {
            return new MutableChampMap<>(deserialized);
        }
    }
}
