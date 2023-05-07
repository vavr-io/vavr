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

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Supports efficient bulk-operations on a hash map through transience.
 * @param <K>the key type
 * @param <V>the value type
 */
class TransientHashMap<K,V> extends ChampAbstractTransientMap<K, V, AbstractMap.SimpleImmutableEntry<K, V>> {

    TransientHashMap(HashMap<K, V> m) {
        root = m;
        size = m.size;
    }

    TransientHashMap() {
        this(HashMap.empty());
    }

    public V put(K key, V value) {
        var oldData = putEntry(key, value, false).getOldData();
        return oldData == null ? null : oldData.getValue();
    }

    boolean putAllEntries(Iterable<? extends Map.Entry<? extends K, ? extends V>> c) {
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

    @SuppressWarnings("unchecked")
    boolean putAllTuples(Iterable<? extends Tuple2<? extends K, ? extends V>> c) {
        if (c instanceof HashMap<?, ?> that) {
            var bulkChange = new ChampBulkChangeEvent();
            var newRootNode = root.putAll(getOrCreateOwner(), (ChampNode<AbstractMap.SimpleImmutableEntry<K, V>>) (ChampNode<?>) that, 0, bulkChange, HashMap::updateEntry, HashMap::entryKeyEquals,
                    HashMap::entryKeyHash, new ChampChangeEvent<>());
            if (bulkChange.inBoth == that.size() && !bulkChange.replaced) {
                return false;
            }
            root = newRootNode;
            size += that.size - bulkChange.inBoth;
            modCount++;
            return true;
        }
        return super.putAllTuples(c);
    }

    ChampChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> putEntry(final K key, V value, boolean moveToLast) {
        int keyHash = HashMap.keyHash(key);
        ChampChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details = new ChampChangeEvent<>();
        root = root.put(getOrCreateOwner(), new AbstractMap.SimpleImmutableEntry<>(key, value), keyHash, 0, details,
                HashMap::updateEntry,
                HashMap::entryKeyEquals,
                HashMap::entryKeyHash);
        if (details.isModified() && !details.isReplaced()) {
            size += 1;
            modCount++;
        }
        return details;
    }



    @SuppressWarnings("unchecked")
    ChampChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> removeKey(K key) {
        int keyHash = HashMap.keyHash(key);
        ChampChangeEvent<AbstractMap.SimpleImmutableEntry<K, V>> details = new ChampChangeEvent<>();
        root = root.remove(getOrCreateOwner(), new AbstractMap.SimpleImmutableEntry<>(key, null), keyHash, 0, details,
                HashMap::entryKeyEquals);
        if (details.isModified()) {
            size = size - 1;
            modCount++;
        }
        return details;
    }

    @Override
    void clear() {
        root = ChampBitmapIndexedNode.emptyNode();
        size = 0;
        modCount++;
    }

    public HashMap<K,V> toImmutable() {
        owner = null;
        return isEmpty()
                ? HashMap.empty()
                : root instanceof HashMap<K,V> h ? h : new HashMap<>(root, size);
    }

    boolean retainAll( Iterable<?> c) {
        if (isEmpty()) {
            return false;
        }
        if ((c instanceof Collection<?> cc && cc.isEmpty())) {
            clear();
            return true;
        }
        ChampBulkChangeEvent bulkChange = new ChampBulkChangeEvent();
        ChampBitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> newRootNode;
        if (c instanceof Collection<?> that) {
            newRootNode = root.filterAll(getOrCreateOwner(), e -> that.contains(e.getKey()), 0, bulkChange);
        } else {
            java.util.HashSet<Object> that = new HashSet<>();
            c.forEach(that::add);
            newRootNode = root.filterAll(getOrCreateOwner(), that::contains, 0, bulkChange);
        }
        if (bulkChange.removed == 0) {
            return false;
        }
        root = newRootNode;
        size -= bulkChange.removed;
        modCount++;
        return true;
    }

    @SuppressWarnings("unchecked")
    boolean retainAllTuples(Iterable<? extends Tuple2<K, V>> c) {
        if (c instanceof HashMap<?, ?> that) {
            var bulkChange = new ChampBulkChangeEvent();
            var newRootNode = root.retainAll(getOrCreateOwner(),
                    (ChampNode<AbstractMap.SimpleImmutableEntry<K, V>>) (ChampNode<?>) that,
                    0, bulkChange, HashMap::updateEntry, HashMap::entryKeyEquals,
                    HashMap::entryKeyHash, new ChampChangeEvent<>());
            if (bulkChange.removed==0) {
                return false;
            }
            root = newRootNode;
            size -= bulkChange.removed;
            modCount++;
            return true;
        }
       if (isEmpty()) {
            return false;
        }
        if ((c instanceof Collection<?> cc && cc.isEmpty())) {
            clear();
            return true;
        }
        ChampBulkChangeEvent bulkChange = new ChampBulkChangeEvent();
        ChampBitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> newRootNode;
        if (c instanceof Collection<?> that) {
            return filterAll(e -> that.contains(e.getKey()));
        }else if (c instanceof Map<?,?> that) {
            return filterAll(e -> that.containsKey(e.getKey()));
        } else {
            java.util.HashSet<Object> that = new HashSet<>();
            c.forEach(that::add);
            return filterAll(that::contains);
        }
    }
    @SuppressWarnings("unchecked")
    boolean filterAll(Predicate<AbstractMap.SimpleImmutableEntry<K, V>> predicate) {
        ChampBulkChangeEvent bulkChange = new ChampBulkChangeEvent();
        ChampBitmapIndexedNode<AbstractMap.SimpleImmutableEntry<K, V>> newRootNode = root.filterAll(getOrCreateOwner(), predicate, 0, bulkChange);
        if (bulkChange.removed == 0) {
            return false;
        }
        root = newRootNode;
        size -= bulkChange.removed;
        modCount++;
        return true;
    }
}
