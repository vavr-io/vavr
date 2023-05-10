/*
 * ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.vavr.collection;

import io.vavr.Tuple2;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static io.vavr.collection.ChampSequencedData.vecRemove;

/**
 * Supports efficient bulk-operations on a linked hash map through transience.
 *
 * @param <K>the key type
 * @param <V>the value type
 */
class TransientLinkedHashMap<K, V> extends ChampAbstractTransientMap<K, V, ChampSequencedEntry<K, V>> {
    /**
     * Offset of sequence numbers to vector indices.
     *
     * <pre>vector index = sequence number + offset</pre>
     */
    private int offset;
    /**
     * In this vector we store the elements in the order in which they were inserted.
     */
    private Vector<Object> vector;

    TransientLinkedHashMap(LinkedHashMap<K, V> m) {
        vector = m.vector;
        root = m;
        offset = m.offset;
        size = m.size;
    }

    TransientLinkedHashMap() {
        this(LinkedHashMap.empty());
    }

    public V put(K key, V value) {
        ChampSequencedEntry<K, V> oldData = putLast(key, value, false).getOldData();
        return oldData == null ? null : oldData.getValue();
    }

    boolean putAllEntries(Iterable<? extends Map.Entry<? extends K, ? extends V>> c) {
        if (c == this) {
            return false;
        }
        boolean modified = false;
        for (var e : c) {
            modified |= putLast(e.getKey(), e.getValue(), false).isModified();
        }
        return modified;
    }

    boolean putAllTuples(Iterable<? extends Tuple2<? extends K, ? extends V>> c) {
        if (c == this) {
            return false;
        }
        boolean modified = false;
        for (var e : c) {
            modified |= putLast(e._1, e._2, false).isModified();
        }
        return modified;
    }

    ChampChangeEvent<ChampSequencedEntry<K, V>> putLast(final K key, V value, boolean moveToLast) {
        ChampChangeEvent<ChampSequencedEntry<K, V>> details = new ChampChangeEvent<ChampSequencedEntry<K, V>>();
        ChampSequencedEntry<K, V> newEntry = new ChampSequencedEntry<>(key, value, vector.size() - offset);
        ChampIdentityObject owner = makeOwner();
        root = root.put(owner, newEntry,
                Objects.hashCode(key), 0, details,
                moveToLast ? ChampSequencedEntry::updateAndMoveToLast : ChampSequencedEntry::updateWithNewKey,
                ChampSequencedEntry::keyEquals, ChampSequencedEntry::entryKeyHash);
        if (details.isReplaced()
                && details.getOldDataNonNull().getSequenceNumber() == details.getNewDataNonNull().getSequenceNumber()) {
            vector = vector.update(details.getNewDataNonNull().getSequenceNumber() - offset, details.getNewDataNonNull());
            return details;
        }
        if (details.isModified()) {
            if (details.isReplaced()) {
                Tuple2<Vector<Object>, Integer> result = ChampSequencedData.vecRemove(vector, details.getOldDataNonNull(), offset);
                vector = result._1;
                offset = result._2;
            } else {
                size++;
            }
            modCount++;
            vector = vector.append(newEntry);
            renumber();
        }
        return details;
    }

    @SuppressWarnings("unchecked")
    boolean removeAll(Iterable<?> c) {
        if (isEmpty()) {
            return false;
        }
        boolean modified = false;
        for (Object key : c) {
            ChampChangeEvent<ChampSequencedEntry<K, V>> details = removeKey((K) key);
            modified |= details.isModified();
        }
        return modified;
    }

    ChampChangeEvent<ChampSequencedEntry<K, V>> removeKey(K key) {
        ChampChangeEvent<ChampSequencedEntry<K, V>> details = new ChampChangeEvent<ChampSequencedEntry<K, V>>();
        root = root.remove(null,
                new ChampSequencedEntry<>(key),
                Objects.hashCode(key), 0, details, ChampSequencedEntry::keyEquals);
        if (details.isModified()) {
            ChampSequencedEntry<K, V> oldElem = details.getOldDataNonNull();
            Tuple2<Vector<Object>, Integer> result = ChampSequencedData.vecRemove(vector, oldElem, offset);
            vector = result._1;
            offset = result._2;
            size--;
            modCount++;
            renumber();
        }
        return details;
    }

    @Override
    void clear() {
root=ChampBitmapIndexedNode.emptyNode();
vector=Vector.empty();
offset=0;
size=0;
    }

    void renumber() {
        if (ChampSequencedData.vecMustRenumber(size, offset, vector.size())) {
            ChampIdentityObject owner = makeOwner();
            Tuple2<ChampBitmapIndexedNode<ChampSequencedEntry<K, V>>, Vector<Object>> result = ChampSequencedData.vecRenumber(size, root, vector, owner,
                    ChampSequencedEntry::entryKeyHash, ChampSequencedEntry::keyEquals,
                    (e, seq) -> new ChampSequencedEntry<>(e.getKey(), e.getValue(), seq));
            root = result._1;
            vector = result._2;
            offset = 0;
        }
    }

    public LinkedHashMap<K, V> toImmutable() {
        owner = null;
        return isEmpty()
                ? LinkedHashMap.empty()
                : root instanceof LinkedHashMap<K, V> ? (LinkedHashMap<K, V>) root : new LinkedHashMap<>(root, vector, size, offset);
    }

    static class VectorSideEffectPredicate<K, V> implements Predicate<ChampSequencedEntry<K, V>> {
        Vector<Object> newVector;
        int newOffset;
        Predicate<? super Map.Entry<K, V>> predicate;

        public VectorSideEffectPredicate(Predicate<? super Map.Entry<K, V>> predicate, Vector<Object> vector, int offset) {
            this.predicate = predicate;
            this.newVector = vector;
            this.newOffset = offset;
        }

        @Override
        public boolean test(ChampSequencedEntry<K, V> e) {
            if (!predicate.test(e)) {
                Tuple2<Vector<Object>, Integer> result = vecRemove(newVector, e, newOffset);
                newVector = result._1;
                newOffset = result._2;
                return false;
            }
            return true;
        }
    }

    boolean filterAll(Predicate<Map.Entry<K, V>> predicate) {
        ChampBulkChangeEvent bulkChange = new ChampBulkChangeEvent();
        VectorSideEffectPredicate<K, V> vp = new VectorSideEffectPredicate<>(predicate, vector, offset);
        ChampBitmapIndexedNode<ChampSequencedEntry<K, V>> newRootNode = root.filterAll(makeOwner(), vp, 0, bulkChange);
        if (bulkChange.removed == 0) {
            return false;
        }
        root = newRootNode;
        vector = vp.newVector;
        offset = vector.isEmpty()?0:vp.newOffset;
        size -= bulkChange.removed;
        modCount++;
        return true;
    }
}
