package io.vavr.collection.champ;

import io.vavr.Tuple2;
import io.vavr.collection.Iterator;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

/**
 * Implements a mutable map using a Compressed Hash-Array Mapped Prefix-tree
 * (CHAMP), with predictable iteration order.
 * <p>
 * Features:
 * <ul>
 *     <li>allows null keys and null values</li>
 *     <li>is mutable</li>
 *     <li>is not thread-safe</li>
 *     <li>iterates in the order, in which keys were inserted</li>
 * </ul>
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>put, putFirst, putLast: O(1) amortized due to renumbering</li>
 *     <li>remove: O(1)</li>
 *     <li>containsKey: O(1)</li>
 *     <li>toImmutable: O(1) + a cost distributed across subsequent updates in
 *     this mutable map</li>
 *     <li>clone: O(1) + a cost distributed across subsequent updates in this
 *     mutable map and in the clone</li>
 *     <li>iterator creation: O(N)</li>
 *     <li>iterator.next: O(1) with bucket sort or O(log N) with a heap</li>
 *     <li>getFirst, getLast: O(N)</li>
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
 * Insertion Order:
 * <p>
 * This map uses a counter to keep track of the insertion order.
 * It stores the current value of the counter in the sequence number
 * field of each data entry. If the counter wraps around, it must renumber all
 * sequence numbers.
 * <p>
 * The renumbering is why the {@code copyPut} is O(1) only in an amortized sense.
 * <p>
 * The iterator of the map is a priority queue, that orders the entries by
 * their stored insertion counter value. This is why {@code iterator.next()}
 * is O(log n).
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
public class MutableLinkedChampMap<K, V> extends AbstractChampMap<K, V, SequencedEntry<K, V>> {
    private final static long serialVersionUID = 0L;
    /**
     * Counter for the sequence number of the last element. The counter is
     * incremented after a new entry is added to the end of the sequence.
     */
    private transient int last = 0;

    /**
     * Counter for the sequence number of the first element. The counter is
     * decrement after a new entry has been added to the start of the sequence.
     */
    private int first = -1;

    public MutableLinkedChampMap() {
        root = BitmapIndexedNode.emptyNode();
    }

    /**
     * Constructs a map containing the same mappings as in the specified
     * {@link Map}.
     *
     * @param m a map
     */
    public MutableLinkedChampMap(java.util.Map<? extends K, ? extends V> m) {
        if (m instanceof MutableLinkedChampMap<?, ?>) {
            @SuppressWarnings("unchecked")
            MutableLinkedChampMap<K, V> that = (MutableLinkedChampMap<K, V>) m;
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

    /**
     * Constructs a map containing the same mappings as in the specified
     * {@link Iterable}.
     *
     * @param m an iterable
     */
    public MutableLinkedChampMap(io.vavr.collection.Map<? extends K, ? extends V> m) {
        if (m instanceof LinkedChampMap) {
            @SuppressWarnings("unchecked")
            LinkedChampMap<K, V> that = (LinkedChampMap<K, V>) m;
            this.root = that;
            this.size = that.size();
            this.first = that.first;
            this.last = that.last;
        } else {
            this.root = BitmapIndexedNode.emptyNode();
            this.putAll(m);
        }

    }

    public MutableLinkedChampMap(Iterable<? extends Entry<? extends K, ? extends V>> m) {
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
        first = -1;
        last = 0;
    }

    /**
     * Returns a shallow copy of this map.
     */
    @Override
    public MutableLinkedChampMap<K, V> clone() {
        return (MutableLinkedChampMap<K, V>) super.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean containsKey(final Object o) {
        K key = (K) o;
        return Node.NO_VALUE != root.findByKey(new SequencedEntry<>(key),
                Objects.hashCode(key), 0,
                getEqualsFunction());
    }

    private Iterator<Entry<K, V>> entryIterator(boolean reversed) {
        return new FailFastIterator<>(new HeapSequencedIterator<SequencedEntry<K, V>, Entry<K, V>>(
                size, root, reversed,
                this::iteratorRemove,
                e -> new MutableMapEntry<>(this::iteratorPutIfPresent, e.getKey(), e.getValue())),
                () -> this.modCount);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new SetFacade<>(
                () -> entryIterator(false),
                this::size,
                this::containsEntry,
                this::clear,
                null,
                this::removeEntry
        );
    }

    //@Override
    public Entry<K, V> firstEntry() {
        return isEmpty() ? null : HeapSequencedIterator.getFirst(root, first, last);
    }

    //@Override
    public K firstKey() {
        return HeapSequencedIterator.getFirst(root, first, last).getKey();
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(final Object o) {
        Object result = root.findByKey(
                new SequencedEntry<>((K) o),
                Objects.hashCode(o), 0, getEqualsFunction());
        return (result instanceof SequencedEntry<?, ?>) ? ((SequencedEntry<K, V>) result).getValue() : null;
    }


    private BiPredicate<SequencedEntry<K, V>, SequencedEntry<K, V>> getEqualsFunction() {
        return (a, b) -> Objects.equals(a.getKey(), b.getKey());
    }


    private ToIntFunction<SequencedEntry<K, V>> getHashFunction() {
        return (a) -> Objects.hashCode(a.getKey());
    }


    private BiFunction<SequencedEntry<K, V>, SequencedEntry<K, V>, SequencedEntry<K, V>> getUpdateAndMoveToFirstFunction() {
        return (oldK, newK) -> (Objects.equals(oldK.getValue(), newK.getValue())
                && oldK.getSequenceNumber() == newK.getSequenceNumber() + 1) ? oldK : newK;
    }


    private BiFunction<SequencedEntry<K, V>, SequencedEntry<K, V>, SequencedEntry<K, V>> getUpdateAndMoveToLastFunction() {
        return (oldK, newK) -> (Objects.equals(oldK.getValue(), newK.getValue())
                && oldK.getSequenceNumber() == newK.getSequenceNumber() - 1) ? oldK : newK;
    }


    private BiFunction<SequencedEntry<K, V>, SequencedEntry<K, V>, SequencedEntry<K, V>> getUpdateFunction() {
        return (oldv, newv) -> Objects.equals(oldv.getValue(), newv.getValue()) ? oldv : newv;
    }

    private void iteratorPutIfPresent(K k, V v) {
        if (containsKey(k)) {
            put(k, v);
        }
    }

    private void iteratorRemove(SequencedEntry<K, V> entry) {
        remove(entry.getKey());
    }

    //@Override
    public Entry<K, V> lastEntry() {
        return isEmpty() ? null : HeapSequencedIterator.getLast(root, first, last);
    }

    //@Override
    public K lastKey() {
        return HeapSequencedIterator.getLast(root, first, last).getKey();
    }

    //@Override
    public Map.Entry<K, V> pollFirstEntry() {
        if (isEmpty()) {
            return null;
        }
        SequencedEntry<K, V> entry = HeapSequencedIterator.getFirst(root, first, last);
        remove(entry.getKey());
        first = entry.getSequenceNumber();
        renumber();
        return entry;
    }

    //@Override
    public Map.Entry<K, V> pollLastEntry() {
        if (isEmpty()) {
            return null;
        }
        SequencedEntry<K, V> entry = HeapSequencedIterator.getLast(root, first, last);
        remove(entry.getKey());
        last = entry.getSequenceNumber();
        renumber();
        return entry;
    }

    @Override
    public V put(K key, V value) {
        SequencedEntry<K, V> oldValue = this.putLast(key, value, false).getOldValue();
        return oldValue == null ? null : oldValue.getValue();
    }

    //@Override
    public V putFirst(K key, V value) {
        SequencedEntry<K, V> oldValue = putFirst(key, value, true).getOldValue();
        return oldValue == null ? null : oldValue.getValue();
    }

    private ChangeEvent<SequencedEntry<K, V>> putFirst(final K key, final V val,
                                                       boolean moveToFirst) {
        final int keyHash = Objects.hashCode(key);
        final ChangeEvent<SequencedEntry<K, V>> details = new ChangeEvent<>();
        final BitmapIndexedNode<SequencedEntry<K, V>> newRootNode =
                root.update(getOrCreateMutator(),
                        new SequencedEntry<>(key, val, first), keyHash, 0, details,
                        moveToFirst ? getUpdateAndMoveToFirstFunction() : getUpdateFunction(),
                        getEqualsFunction(), getHashFunction());
        if (details.isModified()) {
            root = newRootNode;
            if (details.isUpdated()) {
                first = details.getOldValue().getSequenceNumber() == first ? first : first - 1;
                last = details.getOldValue().getSequenceNumber() == last ? last - 1 : last;
            } else {
                modCount++;
                size++;
                first--;
            }
            renumber();
        }
        return details;
    }

    //@Override
    public V putLast(K key, V value) {
        SequencedEntry<K, V> oldValue = putLast(key, value, true).getOldValue();
        return oldValue == null ? null : oldValue.getValue();
    }

    ChangeEvent<SequencedEntry<K, V>> putLast(
            final K key, final V val, boolean moveToLast) {
        final ChangeEvent<SequencedEntry<K, V>> details = new ChangeEvent<>();
        final BitmapIndexedNode<SequencedEntry<K, V>> newRoot =
                root.update(getOrCreateMutator(),
                        new SequencedEntry<>(key, val, last), Objects.hashCode(key), 0, details,
                        moveToLast ? getUpdateAndMoveToLastFunction() : getUpdateFunction(),
                        getEqualsFunction(), getHashFunction());

        if (details.isModified()) {
            root = newRoot;
            if (details.isUpdated()) {
                first = details.getOldValue().getSequenceNumber() == first - 1 ? first - 1 : first;
                last = details.getOldValue().getSequenceNumber() == last ? last : last + 1;
            } else {
                modCount++;
                size++;
                last++;
            }
            renumber();
        }
        return details;
    }


    @Override
    public V remove(Object o) {
        @SuppressWarnings("unchecked") final K key = (K) o;
        ChangeEvent<SequencedEntry<K, V>> details = removeAndGiveDetails(key);
        if (details.modified) {
            return details.getOldValue().getValue();
        }
        return null;
    }

    ChangeEvent<SequencedEntry<K, V>> removeAndGiveDetails(final K key) {
        final int keyHash = Objects.hashCode(key);
        final ChangeEvent<SequencedEntry<K, V>> details = new ChangeEvent<>();
        final BitmapIndexedNode<SequencedEntry<K, V>> newRootNode =
                root.remove(getOrCreateMutator(),
                        new SequencedEntry<>(key), keyHash, 0, details,
                        getEqualsFunction());
        if (details.isModified()) {
            root = newRootNode;
            size = size - 1;
            modCount++;
            int seq = details.getOldValue().getSequenceNumber();
            if (seq == last - 1) {
                last--;
            }
            if (seq == first + 1) {
                first++;
            }
            renumber();
        }
        return details;
    }


    /**
     * Renumbers the sequence numbers if they have overflown,
     * or if the extent of the sequence numbers is more than
     * 4 times the size of the set.
     */
    private void renumber() {
        if (Sequenced.mustRenumber(size, first, last)) {
            root = SequencedEntry.renumber(size, root, getOrCreateMutator(),
                    getHashFunction(), getEqualsFunction());
            last = size;
            first = -1;
        }
    }


    /**
     * Returns an immutable copy of this map.
     *
     * @return an immutable copy
     */
    public LinkedChampMap<K, V> toImmutable() {
        mutator = null;
        return size == 0 ? LinkedChampMap.empty() : new LinkedChampMap<>(root, size, first, last);
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
            return new MutableLinkedChampMap<>(deserialized);
        }
    }
}