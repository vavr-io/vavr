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

import static io.vavr.collection.champ.LinkedChampChampSet.seqHash;

/**
 * Implements an immutable map using two Compressed Hash-Array Mapped Prefix-trees
 * (CHAMP), with predictable iteration order.
 * <p>
 * Features:
 * <ul>
 *     <li>supports up to 2<sup>30</sup> entries</li>
 *     <li>allows null keys and null values</li>
 *     <li>is immutable</li>
 *     <li>is thread-safe</li>
 *     <li>iterates in the order, in which keys were inserted</li>
 * </ul>
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>copyPut, copyPutFirst, copyPutLast: O(1) amortized, due to
 *     renumbering</li>
 *     <li>copyRemove: O(1) amortized, due to renumbering</li>
 *     <li>containsKey: O(1)</li>
 *     <li>toMutable: O(1) + O(log N) distributed across subsequent updates in
 *     the mutable copy</li>
 *     <li>clone: O(1)</li>
 *     <li>iterator creation: O(1)</li>
 *     <li>iterator.next: O(1) with bucket sort, O(log N) with heap sort</li>
 *     <li>getFirst, getLast: O(1)</li>
 * </ul>
 * <p>
 * Implementation details:
 * <p>
 * This map performs read and write operations of single elements in O(1) time,
 * and in O(1) space.
 * <p>
 * The CHAMP trie contains nodes that may be shared with other maps.
 * <p>
 * If a write operation is performed on a node, then this map creates a
 * copy of the node and of all parent nodes up to the root (copy-path-on-write).
 * Since the CHAMP trie has a fixed maximal height, the cost is O(1).
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
 * The renumbering is why the {@code put} and {@code remove} methods are
 * O(1) only in an amortized sense.
 * <p>
 * <p>
 * To support iteration, a second CHAMP trie is maintained. The second CHAMP
 * trie has the same contents as the first. However, we use the sequence number
 * for computing the hash code of an element.
 * <p>
 * In this implementation, a hash code has a length of
 * 32 bits, and is split up in little-endian order into 7 parts of
 * 5 bits (the last part contains the remaining bits).
 * <p>
 * We convert the sequence number to unsigned 32 by adding Integer.MIN_VALUE
 * to it. And then we reorder its bits from
 * 66666555554444433333222221111100 to 00111112222233333444445555566666.
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
public class LinkedChampChampMap<K, V> extends BitmapIndexedNode<SequencedEntry<K, V>>
        implements VavrMapMixin<K, V> {
    private final static long serialVersionUID = 0L;
    private static final LinkedChampChampMap<?, ?> EMPTY = new LinkedChampChampMap<>(BitmapIndexedNode.emptyNode(), BitmapIndexedNode.emptyNode(), 0, -1, 0);
    /**
     * Counter for the sequence number of the last entry.
     * The counter is incremented after a new entry is added to the end of the
     * sequence.
     */
    final int last;
    /**
     * Counter for the sequence number of the first element. The counter is
     * decrement after a new entry has been added to the start of the sequence.
     */
    final int first;
    final int size;
    /**
     * This champ trie stores the map entries by their sequence number.
     */
    final @NonNull BitmapIndexedNode<SequencedEntry<K, V>> sequenceRoot;

    LinkedChampChampMap(BitmapIndexedNode<SequencedEntry<K, V>> root,
                        BitmapIndexedNode<SequencedEntry<K, V>> sequenceRoot,
                        int size,
                        int first, int last) {
        super(root.nodeMap(), root.dataMap(), root.mixed);
        assert (long) last - first >= size : "size=" + size + " first=" + first + " last=" + last;
        this.size = size;
        this.first = first;
        this.last = last;
        this.sequenceRoot = Objects.requireNonNull(sequenceRoot);
    }

    static <K, V> BitmapIndexedNode<SequencedEntry<K, V>> buildSequenceRoot(@NonNull BitmapIndexedNode<SequencedEntry<K, V>> root, @NonNull IdentityObject mutator) {
        BitmapIndexedNode<SequencedEntry<K, V>> seqRoot = emptyNode();
        ChangeEvent<SequencedEntry<K, V>> details = new ChangeEvent<>();
        for (KeyIterator<SequencedEntry<K, V>> i = new KeyIterator<>(root, null); i.hasNext(); ) {
            SequencedEntry<K, V> elem = i.next();
            seqRoot = seqRoot.update(mutator, elem, seqHash(elem.getSequenceNumber()),
                    0, details, (oldK, newK) -> oldK, Object::equals, LinkedChampChampMap::seqHashCode);
        }
        return seqRoot;
    }

    /**
     * Returns an empty immutable map.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return an empty immutable map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> LinkedChampChampMap<K, V> empty() {
        return (LinkedChampChampMap<K, V>) LinkedChampChampMap.EMPTY;
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
    public static <K, V> LinkedChampChampMap<K, V> narrow(LinkedChampChampMap<? extends K, ? extends V> hashMap) {
        return (LinkedChampChampMap<K, V>) hashMap;
    }

    /**
     * Returns a {@code LinkedChampMap}, from a source java.util.Map.
     *
     * @param map A map
     * @param <K> The key type
     * @param <V> The value type
     * @return A new LinkedChampMap containing the given map
     */
    public static <K, V> LinkedChampChampMap<K, V> ofAll(java.util.Map<? extends K, ? extends V> map) {
        return LinkedChampChampMap.<K, V>empty().putAllEntries(map.entrySet());
    }

    /**
     * Creates a LinkedChampMap of the given entries.
     *
     * @param entries Entries
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new LinkedChampMap containing the given entries
     */
    public static <K, V> LinkedChampChampMap<K, V> ofEntries(Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> entries) {
        return LinkedChampChampMap.<K, V>empty().putAllEntries(entries);
    }

    /**
     * Creates a LinkedChampMap of the given tuples.
     *
     * @param entries Tuples
     * @param <K>     The key type
     * @param <V>     The value type
     * @return A new LinkedChampMap containing the given tuples
     */
    public static <K, V> LinkedChampChampMap<K, V> ofTuples(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        return LinkedChampChampMap.<K, V>empty().putAllTuples(entries);
    }

    @Override
    public boolean containsKey(K key) {
        Object result = find(
                new SequencedEntry<>(key),
                Objects.hashCode(key), 0, getEqualsFunction());
        return result != Node.NO_DATA;
    }

    private LinkedChampChampMap<K, V> copyPutLast(K key, V value, boolean moveToLast) {
        int keyHash = Objects.hashCode(key);
        ChangeEvent<SequencedEntry<K, V>> details = new ChangeEvent<>();
        SequencedEntry<K, V> newEntry = new SequencedEntry<>(key, value, last);
        BitmapIndexedNode<SequencedEntry<K, V>> newRoot = update(null,
                newEntry,
                keyHash, 0, details,
                moveToLast ? getUpdateAndMoveToLastFunction() : getUpdateFunction(),
                getEqualsFunction(), getHashFunction());
        var newSeqRoot = sequenceRoot;
        int newFirst = first;
        int newLast = last;
        int newSize = size;
        if (details.isModified()) {
            IdentityObject mutator = new IdentityObject();
            SequencedEntry<K, V> oldEntry = details.getData();
            boolean isUpdated = details.isReplaced();
            newSeqRoot = newSeqRoot.update(mutator,
                    newEntry, seqHash(last), 0, details,
                    getUpdateFunction(),
                    Objects::equals, LinkedChampChampMap::seqHashCode);
            if (isUpdated) {
                newSeqRoot = newSeqRoot.remove(mutator,
                        oldEntry, seqHash(oldEntry.getSequenceNumber()), 0, details,
                        Objects::equals);

                newFirst = details.getData().getSequenceNumber() == newFirst - 1 ? newFirst - 1 : newFirst;
                newLast = details.getData().getSequenceNumber() == newLast ? newLast : newLast + 1;
            } else {
                newSize++;
                newLast++;
            }
            return renumber(newRoot, newSeqRoot, newSize, newFirst, newLast);
        }
        return this;
    }

    private LinkedChampChampMap<K, V> copyRemove(K key, int newFirst, int newLast) {
        int keyHash = Objects.hashCode(key);
        ChangeEvent<SequencedEntry<K, V>> details = new ChangeEvent<>();
        BitmapIndexedNode<SequencedEntry<K, V>> newRoot =
                remove(null, new SequencedEntry<>(key), keyHash, 0, details, getEqualsFunction());
        BitmapIndexedNode<SequencedEntry<K, V>> newSeqRoot = sequenceRoot;
        if (details.isModified()) {
            var oldEntry = details.getData();
            int seq = oldEntry.getSequenceNumber();
            newSeqRoot = newSeqRoot.remove(null,
                    oldEntry,
                    seqHash(seq), 0, details, Objects::equals);
            if (seq == newFirst) {
                newFirst++;
            }
            if (seq == newLast - 1) {
                newLast--;
            }
            return renumber(newRoot, newSeqRoot, size - 1, newFirst, newLast);
        }
        return this;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <L, U> LinkedChampChampMap<L, U> create() {
        return isEmpty() ? (LinkedChampChampMap<L, U>) this : empty();
    }

    @Override
    public <L, U> Map<L, U> createFromEntries(Iterable<? extends Tuple2<? extends L, ? extends U>> entries) {
        return LinkedChampChampMap.<L, U>empty().putAllTuples(entries);
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other instanceof LinkedChampChampMap) {
            LinkedChampChampMap<?, ?> that = (LinkedChampChampMap<?, ?>) other;
            return size == that.size && equivalent(that);
        } else {
            return Collections.equals(this, other);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Option<V> get(K key) {
        Object result = find(
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
                ? new BucketSequencedIterator<>(size, first, last, this, reversed,
                null, e -> new Tuple2<>(e.getKey(), e.getValue()))
                : new HeapSequencedIterator<>(size, this, reversed,
                null, e -> new Tuple2<>(e.getKey(), e.getValue()));
    }

    @Override
    public Set<K> keySet() {
        return new VavrSetFacade<>(this);
    }

    @Override
    public LinkedChampChampMap<K, V> put(K key, V value) {
        return copyPutLast(key, value, false);
    }

    public LinkedChampChampMap<K, V> putAllEntries(Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> entries) {
        final MutableLinkedChampChampMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (java.util.Map.Entry<? extends K, ? extends V> entry : entries) {
            ChangeEvent<SequencedEntry<K, V>> details = t.putLast(entry.getKey(), entry.getValue(), false);
            modified |= details.isModified();
        }
        return modified ? t.toImmutable() : this;
    }

    public LinkedChampChampMap<K, V> putAllTuples(Iterable<? extends Tuple2<? extends K, ? extends V>> entries) {
        final MutableLinkedChampChampMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            ChangeEvent<SequencedEntry<K, V>> details = t.putLast(entry._1, entry._2, false);
            modified |= details.isModified();
        }
        return modified ? t.toImmutable() : this;

    }

    @Override
    public LinkedChampChampMap<K, V> remove(K key) {
        return copyRemove(key, first, last);
    }

    @Override
    public LinkedChampChampMap<K, V> removeAll(Iterable<? extends K> c) {
        if (this.isEmpty()) {
            return this;
        }
        final MutableLinkedChampChampMap<K, V> t = this.toMutable();
        boolean modified = false;
        for (K key : c) {
            ChangeEvent<SequencedEntry<K, V>> details = t.removeAndGiveDetails(key);
            modified |= details.isModified();
        }
        return modified ? t.toImmutable() : this;
    }

    @NonNull
    private LinkedChampChampMap<K, V> renumber(
            BitmapIndexedNode<SequencedEntry<K, V>> root,
            BitmapIndexedNode<SequencedEntry<K, V>> seqRoot,
            int size, int first, int last) {
        if (LinkedChampChampSet.mustRenumber(size, first, last)) {
            IdentityObject mutator = new IdentityObject();
            BitmapIndexedNode<SequencedEntry<K, V>> renumberedRoot = SequencedEntry.renumber(size, root, mutator, Objects::hashCode, Objects::equals);
            BitmapIndexedNode<SequencedEntry<K, V>> renumberedSeqRoot = buildSequenceRoot(renumberedRoot, mutator);
            return new LinkedChampChampMap<>(renumberedRoot, renumberedSeqRoot,
                    size, -1, size);
        }
        return new LinkedChampChampMap<>(root, seqRoot, size, first, last);
    }

    @Override
    public Map<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        // currentElement and newElem are the same => do nothing
        if (Objects.equals(currentElement, newElement)) {
            return this;
        }

        // try to remove currentElem from the 'root' trie
        final ChangeEvent<SequencedEntry<K, V>> detailsCurrent = new ChangeEvent<>();
        IdentityObject mutator = new IdentityObject();
        BitmapIndexedNode<SequencedEntry<K, V>> newRoot = remove(mutator,
                new SequencedEntry<>(currentElement._1, currentElement._2),
                Objects.hashCode(currentElement), 0, detailsCurrent, Objects::equals);
        // currentElement was not in the 'root' trie => do nothing
        if (!detailsCurrent.isModified()) {
            return this;
        }

        // currentElement was in the 'root' trie => also remove it from the 'sequenceRoot' trie
        var newSeqRoot = sequenceRoot;
        SequencedEntry<K, V> currentData = detailsCurrent.getData();
        int seq = currentData.getSequenceNumber();
        newSeqRoot = newSeqRoot.remove(mutator, currentData, seqHash(seq), 0, detailsCurrent, LinkedChampChampMap::seqEquals);

        // try to update the newElement
        ChangeEvent<SequencedEntry<K, V>> detailsNew = new ChangeEvent<>();
        SequencedEntry<K, V> newData = new SequencedEntry<>(newElement._1, newElement._2, seq);
        newRoot = newRoot.update(mutator,
                newData, Objects.hashCode(newElement), 0, detailsNew,
                getForceUpdateFunction(),
                Objects::equals, Objects::hashCode);
        boolean isReplaced = detailsNew.isReplaced();
        SequencedEntry<K, V> replacedData = detailsNew.getData();

        // the newElement was replaced => remove the replaced data from the 'sequenceRoot' trie
        if (isReplaced) {
            newSeqRoot = newSeqRoot.remove(mutator, replacedData, seqHash(replacedData.getSequenceNumber()), 0, detailsNew, LinkedChampChampMap::seqEquals);
        }

        // the newElement was inserted => insert it also in the 'sequenceRoot' trie
        newSeqRoot = newSeqRoot.update(mutator,
                newData, seqHash(seq), 0, detailsNew,
                getForceUpdateFunction(),
                LinkedChampChampMap::seqEquals, LinkedChampChampMap::seqHashCode);

        if (isReplaced) {
            // the newElement was already in the trie => renumbering may be necessary
            return renumber(newRoot, newSeqRoot, size - 1, first, last);
        } else {
            // the newElement was not in the trie => no renumbering is needed
            return new LinkedChampChampMap<>(newRoot, newSeqRoot, size, first, last);
        }
    }

    private static <V, K> boolean seqEquals(SequencedEntry<K, V> a, SequencedEntry<K, V> b) {
        return a.getSequenceNumber() == b.getSequenceNumber();
    }

    @Override
    public Map<K, V> retainAll(Iterable<? extends Tuple2<K, V>> elements) {
        if (elements == this) {
            return this;
        }
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

    public MutableLinkedChampChampMap<K, V> toMutable() {
        return new MutableLinkedChampChampMap<>(this);
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
            return LinkedChampChampMap.empty().putAllEntries(deserialized);
        }
    }

    @Override
    public boolean isSequential() {
        return true;
    }

    static <K, V> int seqHashCode(SequencedEntry<K, V> e) {
        return seqHash(e.getSequenceNumber());
    }
}
