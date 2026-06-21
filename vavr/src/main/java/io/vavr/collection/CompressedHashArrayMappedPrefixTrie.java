/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

/**
 * An experimental, immutable
 * <a href="https://michael.steindorfer.name/publications/oopsla15.pdf">CHAMP</a>
 * (Compressed Hash-Array Mapped Prefix-tree) implementation of {@link HashArrayMappedTrie}.
 *
 * <p>Compared to the classic HAMT in {@link HashArrayMappedTrie}, a CHAMP node keeps two
 * bitmaps — {@code dataMap} for inline key/value payloads and {@code nodeMap} for sub-nodes —
 * and stores payloads at the head of a single backing array while storing sub-nodes, in reverse,
 * at the tail. This removes the per-entry leaf object, improves iteration/equality locality, and
 * keeps the tree in a canonical shape by inlining singleton sub-nodes back into their parent on
 * removal.
 *
 * <p>This class is a drop-in alternative behind the {@link HashArrayMappedTrie} interface; on this
 * branch {@code HashMap}/{@code HashSet} are wired to it so the two representations can be A/B
 * compared for correctness and performance.
 *
 * <p>Sizes 0 and 1 use dedicated representations: the shared {@link #EMPTY} instance and
 * {@link SingletonTrie} (a single object holding hash, key and value — matching the footprint of
 * the classic HAMT's {@code LeafSingleton}). The general node-based representation below is only
 * ever instantiated for {@code size >= 2}.
 *
 * @author Grzegorz Piwowarek
 */
final class CompressedHashArrayMappedPrefixTrie<K, V> implements HashArrayMappedTrie<K, V>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int BIT_PARTITION_SIZE = 5;
    private static final int HASH_CODE_LENGTH = 32;

    private static final Object NOT_FOUND = new Object();

    private static final CompressedHashArrayMappedPrefixTrie<?, ?> EMPTY =
            new CompressedHashArrayMappedPrefixTrie<>(new BitmapIndexedNode<>(0, 0, new Object[0]), 0);

    private final Node<K, V> root;
    private final int size;

    private CompressedHashArrayMappedPrefixTrie(Node<K, V> root, int size) {
        this.root = root;
        this.size = size;
    }

    @SuppressWarnings("unchecked")
    static <K, V> HashArrayMappedTrie<K, V> empty() {
        return (CompressedHashArrayMappedPrefixTrie<K, V>) EMPTY;
    }

    @Serial
    private Object readResolve() {
        // EMPTY must stay the only size-0 instance, also across serialization
        return size == 0 ? EMPTY : this;
    }

    // -- bit twiddling shared by all nodes

    private static int mask(int hash, int shift) {
        return (hash >>> shift) & (Node.BUCKET_SIZE - 1);
    }

    private static int bitpos(int mask) {
        return 1 << mask;
    }

    private static int index(int bitmap, int bitpos) {
        return Integer.bitCount(bitmap & (bitpos - 1));
    }

    // -- HashArrayMappedTrie surface

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Option<V> get(K key) {
        final Object result = root.findValue(key, Objects.hashCode(key), 0);
        return result == NOT_FOUND ? Option.none() : Option.some((V) result);
    }

    @Override
    public Option<Tuple2<K, V>> getEntry(K key) {
        final Tuple2<K, V> entry = root.findEntry(key, Objects.hashCode(key), 0);
        return entry == null ? Option.none() : Option.some(entry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V getOrElse(K key, V defaultValue) {
        final Object result = root.findValue(key, Objects.hashCode(key), 0);
        return result == NOT_FOUND ? defaultValue : (V) result;
    }

    @Override
    public boolean containsKey(K key) {
        return root.findValue(key, Objects.hashCode(key), 0) != NOT_FOUND;
    }

    @Override
    public HashArrayMappedTrie<K, V> put(K key, V value) {
        if (size == 0) {
            return new SingletonTrie<>(Objects.hashCode(key), key, value);
        }
        final MapDetails details = new MapDetails();
        final Node<K, V> newRoot = root.updated(key, value, Objects.hashCode(key), 0, details);
        if (!details.modified) {
            return this;
        }
        return new CompressedHashArrayMappedPrefixTrie<>(newRoot, details.sizeChanged ? size + 1 : size);
    }

    @Override
    public HashArrayMappedTrie<K, V> remove(K key) {
        final MapDetails details = new MapDetails();
        final Node<K, V> newRoot = root.removed(key, Objects.hashCode(key), 0, details);
        if (!details.modified) {
            return this;
        }
        final int newSize = size - 1;
        if (newSize == 0) {
            return empty();
        }
        if (newSize == 1) {
            // canonical form guarantees a size-1 tree is a single-payload root
            final K remainingKey = newRoot.getKey(0);
            return new SingletonTrie<>(Objects.hashCode(remainingKey), remainingKey, newRoot.getValue(0));
        }
        return new CompressedHashArrayMappedPrefixTrie<>(newRoot, newSize);
    }

    @Override
    public @NonNull Iterator<Tuple2<K, V>> iterator() {
        return new EntryIterator<>(root);
    }

    @Override
    public Iterator<K> keysIterator() {
        return iterator().map(t -> t._1);
    }

    @Override
    public Iterator<V> valuesIterator() {
        return iterator().map(t -> t._2);
    }

    @Override
    public String toString() {
        return iterator().map(t -> t._1 + " -> " + t._2).mkString("CompressedHashArrayMappedPrefixTrie(", ", ", ")");
    }

    // -- bookkeeping passed down the recursion to report what changed

    private static final class MapDetails {
        private boolean modified;
        private boolean sizeChanged;

        void recordAdd() {
            modified = true;
            sizeChanged = true;
        }

        void recordReplace() {
            modified = true;
        }

        void recordRemove() {
            modified = true;
            sizeChanged = true;
        }
    }

    // -- dedicated one-entry representation, as compact as the classic HAMT's LeafSingleton

    private static final class SingletonTrie<K, V> implements HashArrayMappedTrie<K, V>, Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private final int keyHash;
        private final K key;
        private final V value;

        SingletonTrie(int keyHash, K key, V value) {
            this.keyHash = keyHash;
            this.key = key;
            this.value = value;
        }

        private boolean matches(K key) {
            return Objects.hashCode(key) == keyHash && Objects.equals(this.key, key);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public Option<V> get(K key) {
            return matches(key) ? Option.some(value) : Option.none();
        }

        @Override
        public Option<Tuple2<K, V>> getEntry(K key) {
            return matches(key) ? Option.some(Tuple.of(this.key, value)) : Option.none();
        }

        @Override
        public V getOrElse(K key, V defaultValue) {
            return matches(key) ? value : defaultValue;
        }

        @Override
        public boolean containsKey(K key) {
            return matches(key);
        }

        @Override
        public HashArrayMappedTrie<K, V> put(K key, V value) {
            final int newKeyHash = Objects.hashCode(key);
            if (newKeyHash == keyHash && Objects.equals(this.key, key)) {
                // adopt the replacing key instance, matching HashArrayMappedTrie semantics
                return new SingletonTrie<>(keyHash, key, value);
            }
            final Node<K, V> root = Node.mergeTwoKeyValPairs(
                    this.key, this.value, keyHash,
                    key, value, newKeyHash, 0);
            return new CompressedHashArrayMappedPrefixTrie<>(root, 2);
        }

        @Override
        public HashArrayMappedTrie<K, V> remove(K key) {
            return matches(key) ? empty() : this;
        }

        @Override
        public @NonNull Iterator<Tuple2<K, V>> iterator() {
            return Iterator.of(Tuple.of(key, value));
        }

        @Override
        public Iterator<K> keysIterator() {
            return Iterator.of(key);
        }

        @Override
        public Iterator<V> valuesIterator() {
            return Iterator.of(value);
        }

        @Override
        public String toString() {
            return "CompressedHashArrayMappedPrefixTrie(" + key + " -> " + value + ")";
        }
    }

    // -- node hierarchy

    private abstract static class Node<K, V> implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        static final int BUCKET_SIZE = 1 << BIT_PARTITION_SIZE;

        static final int SIZE_EMPTY = 0;
        static final int SIZE_ONE = 1;
        static final int SIZE_MORE = 2;

        abstract Object findValue(K key, int keyHash, int shift);

        abstract Tuple2<K, V> findEntry(K key, int keyHash, int shift);

        abstract Node<K, V> updated(K key, V value, int keyHash, int shift, MapDetails details);

        abstract Node<K, V> removed(K key, int keyHash, int shift, MapDetails details);

        abstract boolean hasData();

        abstract boolean hasNodes();

        abstract int payloadArity();

        abstract int nodeArity();

        abstract int sizePredicate();

        abstract K getKey(int index);

        abstract V getValue(int index);

        abstract Node<K, V> getNode(int index);

        static <K, V> Node<K, V> mergeTwoKeyValPairs(K key0, V val0, int hash0,
                                                     K key1, V val1, int hash1, int shift) {
            if (shift >= HASH_CODE_LENGTH) {
                // hashes are fully equal at this point
                return new HashCollisionNode<>(hash0, new Object[] { key0, key1 }, new Object[] { val0, val1 });
            }
            final int mask0 = mask(hash0, shift);
            final int mask1 = mask(hash1, shift);
            if (mask0 == mask1) {
                final Node<K, V> sub = mergeTwoKeyValPairs(key0, val0, hash0, key1, val1, hash1, shift + BIT_PARTITION_SIZE);
                return new BitmapIndexedNode<>(0, bitpos(mask0), new Object[] { sub });
            }
            final int dataMap = bitpos(mask0) | bitpos(mask1);
            final Object[] content = (mask0 < mask1)
                    ? new Object[] { key0, val0, key1, val1 }
                    : new Object[] { key1, val1, key0, val0 };
            return new BitmapIndexedNode<>(dataMap, 0, content);
        }

        static <K, V> Node<K, V> mergeNodeAndKeyValPair(Node<K, V> node, int nodeHash,
                                                        K key, V value, int keyHash, int shift) {
            final int mask0 = mask(nodeHash, shift);
            final int mask1 = mask(keyHash, shift);
            if (mask0 == mask1) {
                final Node<K, V> sub = mergeNodeAndKeyValPair(node, nodeHash, key, value, keyHash, shift + BIT_PARTITION_SIZE);
                return new BitmapIndexedNode<>(0, bitpos(mask0), new Object[] { sub });
            }
            return new BitmapIndexedNode<>(bitpos(mask1), bitpos(mask0), new Object[] { key, value, node });
        }
    }

    private static final class BitmapIndexedNode<K, V> extends Node<K, V> {

        @Serial
        private static final long serialVersionUID = 1L;

        private final int dataMap;
        private final int nodeMap;
        // layout: [k0, v0, k1, v1, ... payloads ascending ...][... sub-nodes stored in reverse ...]
        private final Object[] content;

        BitmapIndexedNode(int dataMap, int nodeMap, Object[] content) {
            this.dataMap = dataMap;
            this.nodeMap = nodeMap;
            this.content = content;
        }

        @Override
        boolean hasData() {
            return dataMap != 0;
        }

        @Override
        boolean hasNodes() {
            return nodeMap != 0;
        }

        @Override
        int payloadArity() {
            return Integer.bitCount(dataMap);
        }

        @Override
        int nodeArity() {
            return Integer.bitCount(nodeMap);
        }

        @Override
        int sizePredicate() {
            if (nodeMap == 0) {
                return switch (payloadArity()) {
                    case 0 -> SIZE_EMPTY;
                    case 1 -> SIZE_ONE;
                    default -> SIZE_MORE;
                };
            }
            return SIZE_MORE;
        }

        @Override
        @SuppressWarnings("unchecked")
        K getKey(int index) {
            return (K) content[2 * index];
        }

        @Override
        @SuppressWarnings("unchecked")
        V getValue(int index) {
            return (V) content[2 * index + 1];
        }

        @Override
        @SuppressWarnings("unchecked")
        Node<K, V> getNode(int index) {
            return (Node<K, V>) content[content.length - 1 - index];
        }

        private Node<K, V> nodeAt(int bitpos) {
            return getNode(index(nodeMap, bitpos));
        }

        @Override
        Object findValue(K key, int keyHash, int shift) {
            final int bitpos = bitpos(mask(keyHash, shift));
            if ((dataMap & bitpos) != 0) {
                final int i = index(dataMap, bitpos);
                return Objects.equals(getKey(i), key) ? getValue(i) : NOT_FOUND;
            }
            if ((nodeMap & bitpos) != 0) {
                return nodeAt(bitpos).findValue(key, keyHash, shift + BIT_PARTITION_SIZE);
            }
            return NOT_FOUND;
        }

        @Override
        Tuple2<K, V> findEntry(K key, int keyHash, int shift) {
            final int bitpos = bitpos(mask(keyHash, shift));
            if ((dataMap & bitpos) != 0) {
                final int i = index(dataMap, bitpos);
                return Objects.equals(getKey(i), key) ? Tuple.of(getKey(i), getValue(i)) : null;
            }
            if ((nodeMap & bitpos) != 0) {
                return nodeAt(bitpos).findEntry(key, keyHash, shift + BIT_PARTITION_SIZE);
            }
            return null;
        }

        @Override
        Node<K, V> updated(K key, V value, int keyHash, int shift, MapDetails details) {
            final int mask = mask(keyHash, shift);
            final int bitpos = bitpos(mask);
            if ((dataMap & bitpos) != 0) {
                final int i = index(dataMap, bitpos);
                final K currentKey = getKey(i);
                final V currentValue = getValue(i);
                if (Objects.equals(currentKey, key)) {
                    // adopt the replacing key instance, matching HashArrayMappedTrie semantics
                    // (an equal-but-distinct key must be surfaced after overwrite)
                    details.recordReplace();
                    return copyAndSetKeyValue(i, key, value);
                }
                final Node<K, V> sub = mergeTwoKeyValPairs(
                        currentKey, currentValue, Objects.hashCode(currentKey),
                        key, value, keyHash, shift + BIT_PARTITION_SIZE);
                details.recordAdd();
                return copyAndMigrateFromInlineToNode(bitpos, sub);
            }
            if ((nodeMap & bitpos) != 0) {
                final Node<K, V> sub = nodeAt(bitpos);
                final Node<K, V> subNew = sub.updated(key, value, keyHash, shift + BIT_PARTITION_SIZE, details);
                return subNew == sub ? this : copyAndSetNode(bitpos, subNew);
            }
            details.recordAdd();
            return copyAndInsertValue(bitpos, key, value);
        }

        @Override
        Node<K, V> removed(K key, int keyHash, int shift, MapDetails details) {
            final int mask = mask(keyHash, shift);
            final int bitpos = bitpos(mask);
            if ((dataMap & bitpos) != 0) {
                final int i = index(dataMap, bitpos);
                if (!Objects.equals(getKey(i), key)) {
                    return this;
                }
                details.recordRemove();
                if (payloadArity() == 2 && nodeArity() == 0) {
                    // collapse to a single-payload node so the parent can inline it (or it becomes the new root)
                    final int otherIndex = i == 0 ? 1 : 0;
                    final K remainingKey = getKey(otherIndex);
                    final V remainingValue = getValue(otherIndex);
                    final int newDataMap = bitpos(mask(Objects.hashCode(remainingKey), 0));
                    return new BitmapIndexedNode<>(newDataMap, 0, new Object[] { remainingKey, remainingValue });
                }
                return copyAndRemoveValue(bitpos);
            }
            if ((nodeMap & bitpos) != 0) {
                final Node<K, V> sub = nodeAt(bitpos);
                final Node<K, V> subNew = sub.removed(key, keyHash, shift + BIT_PARTITION_SIZE, details);
                if (!details.modified) {
                    return this;
                }
                return switch (subNew.sizePredicate()) {
                    case SIZE_ONE -> {
                        if (payloadArity() == 0 && nodeArity() == 1) {
                            yield subNew;
                        }
                        yield copyAndMigrateFromNodeToInline(bitpos, subNew);
                    }
                    case SIZE_MORE -> copyAndSetNode(bitpos, subNew);
                    default -> throw new IllegalStateException("unexpected empty sub-node after removal");
                };
            }
            return this;
        }

        private BitmapIndexedNode<K, V> copyAndSetKeyValue(int dataIndex, K key, V value) {
            final Object[] dst = content.clone();
            dst[2 * dataIndex] = key;
            dst[2 * dataIndex + 1] = value;
            return new BitmapIndexedNode<>(dataMap, nodeMap, dst);
        }

        private BitmapIndexedNode<K, V> copyAndSetNode(int bitpos, Node<K, V> node) {
            final Object[] dst = content.clone();
            dst[content.length - 1 - index(nodeMap, bitpos)] = node;
            return new BitmapIndexedNode<>(dataMap, nodeMap, dst);
        }

        private BitmapIndexedNode<K, V> copyAndInsertValue(int bitpos, K key, V value) {
            final int idx = 2 * index(dataMap, bitpos);
            final Object[] dst = new Object[content.length + 2];
            System.arraycopy(content, 0, dst, 0, idx);
            dst[idx] = key;
            dst[idx + 1] = value;
            System.arraycopy(content, idx, dst, idx + 2, content.length - idx);
            return new BitmapIndexedNode<>(dataMap | bitpos, nodeMap, dst);
        }

        private BitmapIndexedNode<K, V> copyAndRemoveValue(int bitpos) {
            final int idx = 2 * index(dataMap, bitpos);
            final Object[] dst = new Object[content.length - 2];
            System.arraycopy(content, 0, dst, 0, idx);
            System.arraycopy(content, idx + 2, dst, idx, content.length - idx - 2);
            return new BitmapIndexedNode<>(dataMap ^ bitpos, nodeMap, dst);
        }

        private BitmapIndexedNode<K, V> copyAndMigrateFromInlineToNode(int bitpos, Node<K, V> node) {
            final int idxOld = 2 * index(dataMap, bitpos);
            final int idxNew = content.length - 2 - index(nodeMap, bitpos);
            final Object[] dst = new Object[content.length - 1];
            System.arraycopy(content, 0, dst, 0, idxOld);
            System.arraycopy(content, idxOld + 2, dst, idxOld, idxNew - idxOld);
            dst[idxNew] = node;
            System.arraycopy(content, idxNew + 2, dst, idxNew + 1, content.length - idxNew - 2);
            return new BitmapIndexedNode<>(dataMap ^ bitpos, nodeMap | bitpos, dst);
        }

        private BitmapIndexedNode<K, V> copyAndMigrateFromNodeToInline(int bitpos, Node<K, V> node) {
            final int idxOld = content.length - 1 - index(nodeMap, bitpos);
            final int idxNew = 2 * index(dataMap, bitpos);
            final K key = node.getKey(0);
            final V value = node.getValue(0);
            final Object[] dst = new Object[content.length + 1];
            System.arraycopy(content, 0, dst, 0, idxNew);
            dst[idxNew] = key;
            dst[idxNew + 1] = value;
            System.arraycopy(content, idxNew, dst, idxNew + 2, idxOld - idxNew);
            System.arraycopy(content, idxOld + 1, dst, idxOld + 2, content.length - idxOld - 1);
            return new BitmapIndexedNode<>(dataMap | bitpos, nodeMap ^ bitpos, dst);
        }
    }

    private static final class HashCollisionNode<K, V> extends Node<K, V> {

        @Serial
        private static final long serialVersionUID = 1L;

        private final int hash;
        private final Object[] keys;
        private final Object[] vals;

        HashCollisionNode(int hash, Object[] keys, Object[] vals) {
            this.hash = hash;
            this.keys = keys;
            this.vals = vals;
        }

        @Override
        boolean hasData() {
            return true;
        }

        @Override
        boolean hasNodes() {
            return false;
        }

        @Override
        int payloadArity() {
            return keys.length;
        }

        @Override
        int nodeArity() {
            return 0;
        }

        @Override
        int sizePredicate() {
            return SIZE_MORE; // a collision node always holds at least two entries
        }

        @Override
        @SuppressWarnings("unchecked")
        K getKey(int index) {
            return (K) keys[index];
        }

        @Override
        @SuppressWarnings("unchecked")
        V getValue(int index) {
            return (V) vals[index];
        }

        @Override
        Node<K, V> getNode(int index) {
            throw new UnsupportedOperationException("collision nodes have no sub-nodes");
        }

        @Override
        Object findValue(K key, int keyHash, int shift) {
            if (keyHash == hash) {
                for (int i = 0; i < keys.length; i++) {
                    if (Objects.equals(keys[i], key)) {
                        return vals[i];
                    }
                }
            }
            return NOT_FOUND;
        }

        @Override
        Tuple2<K, V> findEntry(K key, int keyHash, int shift) {
            if (keyHash == hash) {
                for (int i = 0; i < keys.length; i++) {
                    if (Objects.equals(keys[i], key)) {
                        return Tuple.of(getKey(i), getValue(i));
                    }
                }
            }
            return null;
        }

        @Override
        Node<K, V> updated(K key, V value, int keyHash, int shift, MapDetails details) {
            if (keyHash != hash) {
                details.recordAdd();
                return mergeNodeAndKeyValPair(this, hash, key, value, keyHash, shift);
            }
            for (int i = 0; i < keys.length; i++) {
                if (Objects.equals(keys[i], key)) {
                    // adopt the replacing key instance (see BitmapIndexedNode#updated)
                    details.recordReplace();
                    final Object[] newKeys = keys.clone();
                    newKeys[i] = key;
                    final Object[] newVals = vals.clone();
                    newVals[i] = value;
                    return new HashCollisionNode<>(hash, newKeys, newVals);
                }
            }
            details.recordAdd();
            final Object[] newKeys = copyAppend(keys, key);
            final Object[] newVals = copyAppend(vals, value);
            return new HashCollisionNode<>(hash, newKeys, newVals);
        }

        @Override
        Node<K, V> removed(K key, int keyHash, int shift, MapDetails details) {
            for (int i = 0; i < keys.length; i++) {
                if (Objects.equals(keys[i], key)) {
                    details.recordRemove();
                    if (keys.length == 2) {
                        // one entry remains: become an inlineable single-payload node
                        final int other = 1 - i;
                        final int newDataMap = bitpos(mask(hash, 0));
                        return new BitmapIndexedNode<>(newDataMap, 0, new Object[] { keys[other], vals[other] });
                    }
                    return new HashCollisionNode<>(hash, copyRemove(keys, i), copyRemove(vals, i));
                }
            }
            return this;
        }

        private static Object[] copyAppend(Object[] array, Object element) {
            final Object[] result = new Object[array.length + 1];
            System.arraycopy(array, 0, result, 0, array.length);
            result[array.length] = element;
            return result;
        }

        private static Object[] copyRemove(Object[] array, int index) {
            return HashArrayMappedTrieModule.AbstractNode.remove(array, index);
        }
    }

    // -- iteration: CHAMP stack walk, yielding each node's payloads before descending

    private static final class EntryIterator<K, V> extends AbstractIterator<Tuple2<K, V>> {

        // 32-bit hashes in 5-bit chunks ⇒ at most 7 bitmap levels; collision nodes are leaves
        private static final int MAX_DEPTH = 8;

        @SuppressWarnings("unchecked")
        private final Node<K, V>[] nodeStack = new Node[MAX_DEPTH];
        private final int[] nodeCursor = new int[MAX_DEPTH];
        private final int[] nodeLength = new int[MAX_DEPTH];
        private int level = -1;

        private Node<K, V> valueNode;
        private int valueCursor;
        private int valueLength;

        EntryIterator(Node<K, V> root) {
            if (root.hasNodes()) {
                level = 0;
                nodeStack[0] = root;
                nodeCursor[0] = 0;
                nodeLength[0] = root.nodeArity();
            }
            if (root.hasData()) {
                valueNode = root;
                valueCursor = 0;
                valueLength = root.payloadArity();
            }
        }

        private boolean searchNextValueNode() {
            while (level >= 0) {
                final int cursor = nodeCursor[level];
                if (cursor < nodeLength[level]) {
                    final Node<K, V> child = nodeStack[level].getNode(cursor);
                    nodeCursor[level] = cursor + 1;
                    if (child.hasNodes()) {
                        level++;
                        nodeStack[level] = child;
                        nodeCursor[level] = 0;
                        nodeLength[level] = child.nodeArity();
                    }
                    if (child.hasData()) {
                        valueNode = child;
                        valueCursor = 0;
                        valueLength = child.payloadArity();
                        return true;
                    }
                } else {
                    level--;
                }
            }
            return false;
        }

        @Override
        public boolean hasNext() {
            return valueCursor < valueLength || searchNextValueNode();
        }

        @Override
        protected Tuple2<K, V> getNext() {
            final Tuple2<K, V> entry = Tuple.of(valueNode.getKey(valueCursor), valueNode.getValue(valueCursor));
            valueCursor++;
            return entry;
        }
    }
}
