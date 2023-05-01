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

/**
 * Supports efficient bulk-operations on a linked hash map through transience.
 *
 * @param <K>the key type
 * @param <V>the value type
 */
class TransientLinkedHashMap<K, V> extends ChampAbstractTransientCollection<ChampSequencedEntry<K, V>> {
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
        var oldData = putLast(key, value, false).getOldData();
        return oldData == null ? null : oldData.getValue();
    }

    boolean putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> c) {
        if (c == this) {
            return false;
        }
        boolean modified = false;
        for (var e : c) {
            var oldValue = put(e.getKey(), e.getValue());
            modified = modified || !Objects.equals(oldValue, e);
        }
        return modified;
    }

    boolean putAllTuples(Iterable<? extends Tuple2<? extends K, ? extends V>> c) {
        if (c == this) {
            return false;
        }
        boolean modified = false;
        for (var e : c) {
            var oldValue = put(e._1, e._2);
            modified = modified || !Objects.equals(oldValue, e);
        }
        return modified;
    }

    ChampChangeEvent<ChampSequencedEntry<K, V>> putLast(final K key, V value, boolean moveToLast) {
        var details = new ChampChangeEvent<ChampSequencedEntry<K, V>>();
        var newEntry = new ChampSequencedEntry<>(key, value, vector.size() - offset);
        var mutator = getOrCreateIdentity();
        root = root.update(mutator, newEntry,
                Objects.hashCode(key), 0, details,
                moveToLast ? ChampSequencedEntry::updateAndMoveToLast : ChampSequencedEntry::updateWithNewKey,
                ChampSequencedEntry::keyEquals, ChampSequencedEntry::keyHash);
        if (details.isReplaced()
                && details.getOldDataNonNull().getSequenceNumber() == details.getNewDataNonNull().getSequenceNumber()) {
            vector = vector.update(details.getNewDataNonNull().getSequenceNumber() - offset, details.getNewDataNonNull());
            return details;
        }
        if (details.isModified()) {
            if (details.isReplaced()) {
                var result = ChampSequencedData.vecRemove(vector, mutator, details.getOldDataNonNull(), new ChampChangeEvent<ChampSequencedEntry<K, V>>(), offset);
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

    boolean removeAll(Iterable<? extends K> c) {
        if (isEmpty()) {
            return false;
        }
        boolean modified = false;
        for (K key : c) {
            ChampChangeEvent<ChampSequencedEntry<K, V>> details = removeAndGiveDetails(key);
            modified |= details.isModified();
        }
        return modified;
    }

    ChampChangeEvent<ChampSequencedEntry<K, V>> removeAndGiveDetails(K key) {
        var details = new ChampChangeEvent<ChampSequencedEntry<K, V>>();
        root = root.remove(null,
                new ChampSequencedEntry<>(key),
                Objects.hashCode(key), 0, details, ChampSequencedEntry::keyEquals);
        if (details.isModified()) {
            var oldElem = details.getOldDataNonNull();
            var result = ChampSequencedData.vecRemove(vector, null, oldElem, new ChampChangeEvent<>(), offset);
            vector = result._1;
            offset = result._2;
            size--;
            modCount++;
            renumber();
        }
        return details;
    }

    void renumber() {
        if (ChampSequencedData.vecMustRenumber(size, offset, vector.size())) {
            ChampIdentityObject mutator = getOrCreateIdentity();
            var result = ChampSequencedData.vecRenumber(size, root, vector, mutator,
                    ChampSequencedEntry::keyHash, ChampSequencedEntry::keyEquals,
                    (e, seq) -> new ChampSequencedEntry<>(e.getKey(), e.getValue(), seq));
            root = result._1;
            vector = result._2;
            offset = 0;
        }
    }

    public LinkedHashMap<K, V> toImmutable() {
        mutator = null;
        return isEmpty()?LinkedHashMap.empty():new LinkedHashMap<>(root, vector, size, offset);
    }
}
