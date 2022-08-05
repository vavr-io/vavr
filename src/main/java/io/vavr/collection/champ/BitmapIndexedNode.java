/*
 * @(#)BitmapIndexedNode.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;


import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

import static io.vavr.collection.champ.NodeFactory.newBitmapIndexedNode;
import static io.vavr.collection.champ.NodeFactory.newHashCollisionNode;


/**
 * Represents a bitmap-indexed node in a CHAMP trie.
 *
 * @param <K> the key type
 */
class BitmapIndexedNode<K> extends Node<K> {
    static final BitmapIndexedNode<?> EMPTY_NODE = newBitmapIndexedNode(null, (0), (0), new Object[]{});

    public final Object[] mixed;
    final int nodeMap;
    final int dataMap;

    protected BitmapIndexedNode(final int nodeMap,
                                final int dataMap, final Object[] mixed) {
        this.nodeMap = nodeMap;
        this.dataMap = dataMap;
        this.mixed = mixed;
        assert mixed.length == nodeArity() + dataArity();
    }

    @SuppressWarnings("unchecked")
    public static <K> BitmapIndexedNode<K> emptyNode() {
        return (BitmapIndexedNode<K>) EMPTY_NODE;
    }

    BitmapIndexedNode<K> copyAndInsertValue(final UniqueId mutator, final int bitpos,
                                            final K key) {
        final int idx = dataIndex(bitpos);
        final Object[] dst = ArrayHelper.copyComponentAdd(this.mixed, idx, 1);
        dst[idx] = key;
        return newBitmapIndexedNode(mutator, nodeMap, dataMap | bitpos, dst);
    }

    BitmapIndexedNode<K> copyAndMigrateFromDataToNode(final UniqueId mutator,
                                                      final int bitpos, final Node<K> node) {

        final int idxOld = dataIndex(bitpos);
        final int idxNew = this.mixed.length - 1 - nodeIndex(bitpos);
        assert idxOld <= idxNew;

        // copy 'src' and remove entryLength element(s) at position 'idxOld' and
        // insert 1 element(s) at position 'idxNew'
        final Object[] src = this.mixed;
        final Object[] dst = new Object[src.length];
        System.arraycopy(src, 0, dst, 0, idxOld);
        System.arraycopy(src, idxOld + 1, dst, idxOld, idxNew - idxOld);
        System.arraycopy(src, idxNew + 1, dst, idxNew + 1, src.length - idxNew - 1);
        dst[idxNew] = node;
        return newBitmapIndexedNode(mutator, nodeMap | bitpos, dataMap ^ bitpos, dst);
    }

    BitmapIndexedNode<K> copyAndMigrateFromNodeToData(final UniqueId mutator,
                                                      final int bitpos, final Node<K> node) {
        final int idxOld = this.mixed.length - 1 - nodeIndex(bitpos);
        final int idxNew = dataIndex(bitpos);

        // copy 'src' and remove 1 element(s) at position 'idxOld' and
        // insert entryLength element(s) at position 'idxNew'
        final Object[] src = this.mixed;
        final Object[] dst = new Object[src.length];
        assert idxOld >= idxNew;
        System.arraycopy(src, 0, dst, 0, idxNew);
        System.arraycopy(src, idxNew, dst, idxNew + 1, idxOld - idxNew);
        System.arraycopy(src, idxOld + 1, dst, idxOld + 1, src.length - idxOld - 1);
        dst[idxNew] = node.getKey(0);
        return newBitmapIndexedNode(mutator, nodeMap ^ bitpos, dataMap | bitpos, dst);
    }

    BitmapIndexedNode<K> copyAndSetNode(final UniqueId mutator, final int bitpos,
                                        final Node<K> node) {

        final int idx = this.mixed.length - 1 - nodeIndex(bitpos);
        if (isAllowedToEdit(mutator)) {
            // no copying if already editable
            this.mixed[idx] = node;
            return this;
        } else {
            // copy 'src' and set 1 element(s) at position 'idx'
            final Object[] dst = ArrayHelper.copySet(this.mixed, idx, node);
            return newBitmapIndexedNode(mutator, nodeMap, dataMap, dst);
        }
    }

    @Override
    int dataArity() {
        return Integer.bitCount(dataMap);
    }

    int dataIndex(final int bitpos) {
        return Integer.bitCount(dataMap & (bitpos - 1));
    }

    public int dataMap() {
        return dataMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equivalent(final Object other) {
        if (this == other) {
            return true;
        }
        BitmapIndexedNode<?> that = (BitmapIndexedNode<?>) other;
        Object[] thatNodes = that.mixed;
        // nodes array: we compare local data from 0 to splitAt (excluded)
        // and then we compare the nested nodes from splitAt to length (excluded)
        int splitAt = dataArity();
        return nodeMap() == that.nodeMap()
                && dataMap() == that.dataMap()
                && ArrayHelper.equals(mixed, 0, splitAt, thatNodes, 0, splitAt)
                && ArrayHelper.equals(mixed, splitAt, mixed.length, thatNodes, splitAt, thatNodes.length,
                (a, b) -> ((Node<K>) a).equivalent(b));
    }


    @Override
    public Object findByKey(final K key, final int keyHash, final int shift, BiPredicate<K, K> equalsFunction) {
        final int bitpos = bitpos(mask(keyHash, shift));
        if ((nodeMap & bitpos) != 0) {
            return nodeAt(bitpos).findByKey(key, keyHash, shift + BIT_PARTITION_SIZE, equalsFunction);
        }
        if ((dataMap & bitpos) != 0) {
            K k = getKey(dataIndex(bitpos));
            if (equalsFunction.test(k, key)) {
                return k;
            }
        }
        return NO_VALUE;
    }


    @Override
    @SuppressWarnings("unchecked")
    K getKey(final int index) {
        return (K) mixed[index];
    }


    @Override
    @SuppressWarnings("unchecked")
    Node<K> getNode(final int index) {
        return (Node<K>) mixed[mixed.length - 1 - index];
    }

    @Override
    boolean hasData() {
        return dataMap != 0;
    }

    @Override
    boolean hasDataArityOne() {
        return Integer.bitCount(dataMap) == 1;
    }

    @Override
    boolean hasNodes() {
        return nodeMap != 0;
    }

    @Override
    int nodeArity() {
        return Integer.bitCount(nodeMap);
    }

    @SuppressWarnings("unchecked")
    Node<K> nodeAt(final int bitpos) {
        return (Node<K>) mixed[mixed.length - 1 - nodeIndex(bitpos)];
    }

    int nodeIndex(final int bitpos) {
        return Integer.bitCount(nodeMap & (bitpos - 1));
    }

    public int nodeMap() {
        return nodeMap;
    }

    @Override
    public BitmapIndexedNode<K> remove(final UniqueId mutator,
                                       final K key,
                                       final int keyHash, final int shift,
                                       final ChangeEvent<K> details, BiPredicate<K, K> equalsFunction) {
        final int mask = mask(keyHash, shift);
        final int bitpos = bitpos(mask);
        if ((dataMap & bitpos) != 0) {
            return removeData(mutator, key, keyHash, shift, details, bitpos, equalsFunction);
        }
        if ((nodeMap & bitpos) != 0) {
            return removeSubNode(mutator, key, keyHash, shift, details, bitpos, equalsFunction);
        }
        return this;
    }

    private BitmapIndexedNode<K> removeData(UniqueId mutator, K key, int keyHash, int shift, ChangeEvent<K> details, int bitpos, BiPredicate<K, K> equalsFunction) {
        final int dataIndex = dataIndex(bitpos);
        int entryLength = 1;
        if (!equalsFunction.test(getKey(dataIndex), key)) {
            return this;
        }
        final K currentVal = getKey(dataIndex);
        details.setValueRemoved(currentVal);
        if (dataArity() == 2 && !hasNodes()) {
            final int newDataMap =
                    (shift == 0) ? (dataMap ^ bitpos) : bitpos(mask(keyHash, 0));
            Object[] nodes = {getKey(dataIndex ^ 1)};
            return newBitmapIndexedNode(mutator, 0, newDataMap, nodes);
        }
        int idx = dataIndex * entryLength;
        final Object[] dst = ArrayHelper.copyComponentRemove(this.mixed, idx, entryLength);
        return newBitmapIndexedNode(mutator, nodeMap, dataMap ^ bitpos, dst);
    }

    private BitmapIndexedNode<K> removeSubNode(UniqueId mutator, K key, int keyHash, int shift,
                                               ChangeEvent<K> details,
                                               int bitpos, BiPredicate<K, K> equalsFunction) {
        final Node<K> subNode = nodeAt(bitpos);
        final Node<K> updatedSubNode =
                subNode.remove(mutator, key, keyHash, shift + BIT_PARTITION_SIZE, details, equalsFunction);
        if (subNode == updatedSubNode) {
            return this;
        }
        if (!updatedSubNode.hasNodes() && updatedSubNode.hasDataArityOne()) {
            if (!hasData() && nodeArity() == 1) {
                return (BitmapIndexedNode<K>) updatedSubNode;
            }
            return copyAndMigrateFromNodeToData(mutator, bitpos, updatedSubNode);
        }
        return copyAndSetNode(mutator, bitpos, updatedSubNode);
    }

    @Override
    public BitmapIndexedNode<K> update(UniqueId mutator,
                                       K key,
                                       int keyHash, int shift,
                                       ChangeEvent<K> details,
                                       BiFunction<K, K, K> updateFunction,
                                       BiPredicate<K, K> equalsFunction,
                                       ToIntFunction<K> hashFunction) {
        final int mask = mask(keyHash, shift);
        final int bitpos = bitpos(mask);
        if ((dataMap & bitpos) != 0) {
            final int dataIndex = dataIndex(bitpos);
            final K oldKey = getKey(dataIndex);
            if (equalsFunction.test(oldKey, key)) {
                K updatedKey = updateFunction.apply(oldKey, key);
                if (updatedKey == oldKey) {
                    details.found(oldKey);
                    return this;
                }
                details.setValueUpdated(oldKey);
                return copyAndSetValue(mutator, dataIndex, updatedKey);
            }
            final Node<K> updatedSubNode =
                    mergeTwoDataEntriesIntoNode(mutator,
                            oldKey, hashFunction.applyAsInt(oldKey),
                            key, keyHash, shift + BIT_PARTITION_SIZE);
            details.setValueAdded();
            return copyAndMigrateFromDataToNode(mutator, bitpos, updatedSubNode);
        } else if ((nodeMap & bitpos) != 0) {
            Node<K> subNode = nodeAt(bitpos);
            final Node<K> updatedSubNode = subNode
                    .update(mutator, key, keyHash, shift + BIT_PARTITION_SIZE, details, updateFunction, equalsFunction, hashFunction);
            return subNode == updatedSubNode ? this : copyAndSetNode(mutator, bitpos, updatedSubNode);
        }
        details.setValueAdded();
        return copyAndInsertValue(mutator, bitpos, key);
    }


    private BitmapIndexedNode<K> copyAndSetValue(UniqueId mutator, int dataIndex, K updatedKey) {
        if (isAllowedToEdit(mutator)) {
            this.mixed[dataIndex] = updatedKey;
            return this;
        }
        final Object[] newMixed = ArrayHelper.copySet(this.mixed, dataIndex, updatedKey);
        return newBitmapIndexedNode(mutator, nodeMap, dataMap, newMixed);
    }

    private int nodeIndexAt(Object[] array, int nodeMap, final int bitpos) {
        return array.length - 1 - Integer.bitCount(nodeMap & (bitpos - 1));
    }

    private Node<K> mergeTwoKeyValPairs(UniqueId mutator,
                                        final K key0, final int keyHash0,
                                        final K key1, final int keyHash1,
                                        final int shift) {

        assert !(key0.equals(key1));

        if (shift >= HASH_CODE_LENGTH) {
            @SuppressWarnings("unchecked")
            HashCollisionNode<K> unchecked = newHashCollisionNode(mutator, keyHash0, new Object[]{key0, key1});
            return unchecked;
        }

        final int mask0 = mask(keyHash0, shift);
        final int mask1 = mask(keyHash1, shift);

        if (mask0 != mask1) {
            // both nodes fit on same level
            final int dataMap = bitpos(mask0) | bitpos(mask1);
            if (mask0 < mask1) {
                return newBitmapIndexedNode(mutator, 0, dataMap, new Object[]{key0, key1});
            } else {
                return newBitmapIndexedNode(mutator, 0, dataMap, new Object[]{key1, key0});
            }
        } else {
            final Node<K> node = mergeTwoKeyValPairs(mutator, key0, keyHash0, key1, keyHash1, shift + BIT_PARTITION_SIZE);
            // values fit on next level
            final int nodeMap = bitpos(mask0);
            return newBitmapIndexedNode(mutator, nodeMap, 0, new Object[]{node});
        }
    }
}