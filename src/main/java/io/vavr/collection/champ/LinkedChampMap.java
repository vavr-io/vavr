package io.vavr.collection.champ;

import io.vavr.Tuple2;
import io.vavr.collection.Collections;
import io.vavr.collection.Iterator;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import io.vavr.control.Option;

import java.io.ObjectStreamException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

/**
 * Implements an immutable map using a Compressed Hash-Array Mapped Prefix-tree
 * (CHAMP), with predictable iteration order.
 * <p>
 * Features:
 * <ul>
 *     <li>allows null keys and null values</li>
 *     <li>is immutable</li>
 *     <li>is thread-safe</li>
 *     <li>iterates in the order, in which keys were inserted</li>
 * </ul>
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>copyPut, copyPutFirst, copyPutLast: O(1) amortized due to
 *     renumbering</li>
 *     <li>copyRemove: O(1) amortized due to renumbering</li>
 *     <li>containsKey: O(1)</li>
 *     <li>toMutable: O(1) + a cost distributed across subsequent updates in
 *     the mutable copy</li>
 *     <li>clone: O(1)</li>
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
public class LinkedChampMap<K, V> extends BitmapIndexedNode<SequencedEntry<K, V>>
        implements MapMixin<K, V> {
    private final static long serialVersionUID = 0L;
    private static final LinkedChampMap<?, ?> EMPTY = new LinkedChampMap<>(BitmapIndexedNode.emptyNode(), 0, -1, 0);
    /**
     * Counter for the sequence number of the last entry.
     * The counter is incremented after a new entry is added to the end of the
     * sequence.
     */
    protected transient final int last;
    /**
     * Counter for the sequence number of the first element. The counter is
     * decrement after a new entry has been added to the start of the sequence.
     */
    protected transient final int first;
    final transient int size;

    LinkedChampMap(BitmapIndexedNode<SequencedEntry<K, V>> root, int size,
                   int first, int last) {
        super(root.nodeMap(), root.dataMap(), root.mixed);
        assert (long) last - first >= size : "size=" + size + " first=" + first + " last=" + last;
        this.size = size;
        this.first = first;
        this.last = last;
    }

    /**
     * Returns an empty immutable map.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return an empty immutable map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> LinkedChampMap<K, V> empty() {
        return (LinkedChampMap<K, V>) LinkedChampMap.EMPTY;
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
    public static <K, V> LinkedChampMap<K, V> narrow(LinkedChampMap<? extends K, ? extends V> hashMap) {
        return (LinkedChampMap<K, V>) hashMap;
    }

    /**
     * Returns a {@code LinkedChampMap}, from a source java.util.Map.
     *
     * @param map A map
     * @param <K> The key type
     * @param <V> The value type
     * @return A new LinkedChampMap containing the given map
     */
    public static <K, V> LinkedChampMap<K, V> ofAll(java.util.Map<? extends K, ? extends V> map) {
        return LinkedChampMap.<K, V>empty().putAllEntries(map.entrySet());
    }

    /**
     * Creates a LinkedChampMap of the given entries.
     *
     * @param entries Entries
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new LinkedChampMap containing the given entries
     */
    public static <K, V> LinkedChampMap<K, V> ofEntries(Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> entries) {
        return LinkedChampMap.<K, V>empty().putAllEntries(entries);
    }

    /**
     * Creates a LinkedChampMap of the given tuples.
     *
     * @param entries Tuples
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new LinkedChampMap containing the given tuples
     */
    public static <K, V> LinkedChampMap<K, V> ofTuples(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        return LinkedChampMap.<K, V>empty().putAllTuples(entries);
    }

    @Override
    public boolean containsKey(K key) {
        Object result = findByKey(
                new SequencedEntry<>(key),
                Objects.hashCode(key), 0, getEqualsFunction());
        return result != Node.NO_VALUE;
    }

    private LinkedChampMap<K, V> copyPutFirst(K key, V value, boolean moveToFirst) {
        final int keyHash = Objects.hashCode(key);
        final ChangeEvent<SequencedEntry<K, V>> details = new ChangeEvent<>();
        BitmapIndexedNode<SequencedEntry<K, V>> newRootNode = update(null,
                new SequencedEntry<>(key, value, first),
                keyHash, 0, details,
                moveToFirst ? getUpdateAndMoveToFirstFunction() : getUpdateFunction(),
                getEqualsFunction(), getHashFunction());
        if (details.updated) {
            return moveToFirst
                    ? renumber(newRootNode, size,
                    details.getOldValue().getSequenceNumber() == first ? first : first - 1,
                    details.getOldValue().getSequenceNumber() == last ? last - 1 : last)
                    : new LinkedChampMap<>(newRootNode, size, first - 1, last);
        }
        return details.modified ? renumber(newRootNode, size + 1, first - 1, last) : this;
    }

    private LinkedChampMap<K, V> copyPutLast(K key, V value) {
        return copyPutLast(key, value, true);
    }

    private LinkedChampMap<K, V> copyPutLast(K key, V value, boolean moveToLast) {
        final int keyHash = Objects.hashCode(key);
        final ChangeEvent<SequencedEntry<K, V>> details = new ChangeEvent<>();
        BitmapIndexedNode<SequencedEntry<K, V>> newRootNode = update(null,
                new SequencedEntry<>(key, value, last),
                keyHash, 0, details,
                moveToLast ? getUpdateAndMoveToLastFunction() : getUpdateFunction(),
                getEqualsFunction(), getHashFunction());
        if (details.updated) {
            return moveToLast
                    ? renumber(newRootNode, size,
                    details.getOldValue().getSequenceNumber() == first ? first + 1 : first,
                    details.getOldValue().getSequenceNumber() == last ? last : last + 1)
                    : new LinkedChampMap<>(newRootNode, size, first, last + 1);
        }
        return details.modified ? renumber(newRootNode, size + 1, first, last + 1) : this;
    }

    private LinkedChampMap<K, V> copyRemove(K key, int newFirst, int newLast) {
        final int keyHash = Objects.hashCode(key);
        final ChangeEvent<SequencedEntry<K, V>> details = new ChangeEvent<>();
        final BitmapIndexedNode<SequencedEntry<K, V>> newRootNode =
                remove(null, new SequencedEntry<>(key), keyHash, 0, details, getEqualsFunction());
        if (details.isModified()) {
            int seq = details.getOldValue().getSequenceNumber();
            if (seq == newFirst) {
                newFirst++;
            }
            if (seq == newLast) {
                newLast--;
            }
            return renumber(newRootNode, size - 1, newFirst, newLast);
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <L, U> LinkedChampMap<L, U> create() {
        return isEmpty() ? (LinkedChampMap<L, U>) this : empty();
    }

    @Override
    public <L, U> Map<L, U> createFromEntries(Iterable<? extends Tuple2<? extends L, ? extends U>> entries) {
        return LinkedChampMap.<L, U>empty().putAllTuples(entries);
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other instanceof LinkedChampMap) {
            LinkedChampMap<?, ?> that = (LinkedChampMap<?, ?>) other;
            return size == that.size && equivalent(that);
        } else {
            return Collections.equals(this, other);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Option<V> get(K key) {
        Object result = findByKey(
                new SequencedEntry<>(key),
                Objects.hashCode(key), 0, getEqualsFunction());
        return (result instanceof SequencedEntry<?, ?>)
                ? Option.some(((SequencedEntry<K, V>) result).getValue())
                : Option.none();
    }

    private BiPredicate<SequencedEntry<K, V>, SequencedEntry<K, V>> getEqualsFunction() {
        return (a, b) -> Objects.equals(a.getKey(), b.getKey());
    }

    private BiFunction<SequencedEntry<K, V>, SequencedEntry<K, V>, SequencedEntry<K, V>> getForceUpdateFunction() {
        return (oldK, newK) -> newK;
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
        return iterator(false);
    }

    public Iterator<Tuple2<K, V>> iterator(boolean reversed) {
        return BucketSequencedIterator.isSupported(size, first, last)
                ? new BucketSequencedIterator<SequencedEntry<K, V>, Tuple2<K, V>>(size, first, last, this, reversed,
                null, e -> new Tuple2<K, V>(e.getKey(), e.getValue()))
                : new HeapSequencedIterator<SequencedEntry<K, V>, Tuple2<K, V>>(size, this, reversed,
                null, e -> new Tuple2<K, V>(e.getKey(), e.getValue()));
    }

    @Override
    public Set<K> keySet() {
        return new VavrSetFacade<>(this);
    }

    @Override
    public LinkedChampMap<K, V> put(K key, V value) {
        return copyPutLast(key, value, false);
    }

    public LinkedChampMap<K, V> putAllEntries(Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> entries) {
        final MutableLinkedChampMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (java.util.Map.Entry<? extends K, ? extends V> entry : entries) {
            ChangeEvent<SequencedEntry<K, V>> details = t.putLast(entry.getKey(), entry.getValue(), false);
            modified |= details.modified;
        }
        return modified ? t.toImmutable() : this;
    }

    public LinkedChampMap<K, V> putAllTuples(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        final MutableLinkedChampMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            ChangeEvent<SequencedEntry<K, V>> details = t.putLast(entry._1, entry._2, false);
            modified |= details.modified;
        }
        return modified ? t.toImmutable() : this;

    }

    @Override
    public LinkedChampMap<K, V> remove(K key) {
        return copyRemove(key, first, last);
    }

    @Override
    public LinkedChampMap<K, V> removeAll(Iterable<? extends K> c) {
        if (this.isEmpty()) {
            return this;
        }
        final MutableLinkedChampMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (K key : c) {
            ChangeEvent<SequencedEntry<K, V>> details = t.removeAndGiveDetails(key);
            modified |= details.modified;
        }
        return modified ? t.toImmutable() : this;
    }

    private LinkedChampMap<K, V> renumber(BitmapIndexedNode<SequencedEntry<K, V>> root, int size, int first, int last) {
        if (size == 0) {
            return empty();
        }
        if (Sequenced.mustRenumber(size, first, last)) {
            root = SequencedEntry.renumber(size, root, new UniqueId(), Objects::hashCode, Objects::equals);
            return new LinkedChampMap<>(root, size, -1, size);
        }
        return new LinkedChampMap<>(root, size, first, last);
    }

    @Override
    public Map<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        if (Objects.equals(currentElement, newElement)) {
            return this;
        }
        final ChangeEvent<SequencedEntry<K, V>> detailsCurrent = new ChangeEvent<>();
        BitmapIndexedNode<SequencedEntry<K, V>> newRootNode = remove(null,
                new SequencedEntry<>(currentElement._1, currentElement._2),
                Objects.hashCode(currentElement._1), 0, detailsCurrent,
                (a, b) -> Objects.equals(a.getKey(), b.getKey())
                        && Objects.equals(a.getValue(), b.getValue()));
        if (!detailsCurrent.modified) {
            return this;
        }
        int seq = detailsCurrent.getOldValue().getSequenceNumber();
        ChangeEvent<SequencedEntry<K, V>> detailsNew = new ChangeEvent<>();
        newRootNode = newRootNode.update(null,
                new SequencedEntry<>(newElement._1, newElement._2, seq),
                Objects.hashCode(newElement._1), 0, detailsNew,
                getForceUpdateFunction(), getEqualsFunction(), getHashFunction());
        if (detailsNew.updated) {
            return renumber(newRootNode, size - 1, first, last);
        } else {
            return new LinkedChampMap<>(newRootNode, size, first, last);
        }
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
    public Map<K, V> tail() {
        // XXX ChampMapTest.shouldThrowWhenTailEmpty wants us to throw
        //       UnsupportedOperationException instead of NoSuchElementException.
        if (isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return remove(iterator().next()._1);
    }

    @Override
    public java.util.Map<K, V> toJavaMap() {
        return toMutable();
    }

    public MutableLinkedChampMap<K, V> toMutable() {
        return new MutableLinkedChampMap<>(this);
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
            return LinkedChampMap.empty().putAllEntries(deserialized);
        }
    }

    @Override
    public boolean isSequential() {
        return true;
    }
}
