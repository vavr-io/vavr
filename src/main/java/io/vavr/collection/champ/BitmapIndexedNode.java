/*
 * @(#)BitmapIndexedNode.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;


import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

import static io.vavr.collection.champ.NodeFactory.newBitmapIndexedNode;


/**
 * Represents a bitmap-indexed node in a CHAMP trie.
 *
 * @param <D> the data type
 */
class BitmapIndexedNode<D> extends Node<D> {
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

    BitmapIndexedNode<D> copyAndInsertData(final UniqueId mutator, final int bitpos,
                                           final D data) {
        final int idx = dataIndex(bitpos);
        final Object[] dst = ArrayHelper.copyComponentAdd(this.mixed, idx, 1);
        dst[idx] = data;
        return newBitmapIndexedNode(mutator, nodeMap, dataMap | bitpos, dst);
    }

    BitmapIndexedNode<D> copyAndMigrateFromDataToNode(final UniqueId mutator,
                                                      final int bitpos, final Node<D> node) {

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

    BitmapIndexedNode<D> copyAndMigrateFromNodeToData(final UniqueId mutator,
                                                      final int bitpos, final Node<D> node) {
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
        dst[idxNew] = node.getData(0);
        return newBitmapIndexedNode(mutator, nodeMap ^ bitpos, dataMap | bitpos, dst);
    }

    BitmapIndexedNode<D> copyAndSetNode(final UniqueId mutator, final int bitpos,
                                        final Node<D> node) {

        final int idx = this.mixed.length - 1 - nodeIndex(bitpos);
        if (isAllowedToUpdate(mutator)) {
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
                (a, b) -> ((Node<D>) a).equivalent(b));
    }


    @Override
    public Object find(final D data, final int dataHash, final int shift, BiPredicate<D, D> equalsFunction) {
        final int bitpos = bitpos(mask(dataHash, shift));
        if ((nodeMap & bitpos) != 0) {
            return nodeAt(bitpos).find(data, dataHash, shift + BIT_PARTITION_SIZE, equalsFunction);
        }
        if ((dataMap & bitpos) != 0) {
            D k = getData(dataIndex(bitpos));
            if (equalsFunction.test(k, data)) {
                return k;
            }
        }
        return NO_DATA;
    }


    @Override
    @SuppressWarnings("unchecked")
    D getData(final int index) {
        return (D) mixed[index];
    }


    @Override
    @SuppressWarnings("unchecked")
    Node<D> getNode(final int index) {
        return (Node<D>) mixed[mixed.length - 1 - index];
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
    Node<D> nodeAt(final int bitpos) {
        return (Node<D>) mixed[mixed.length - 1 - nodeIndex(bitpos)];
    }

    int nodeIndex(final int bitpos) {
        return Integer.bitCount(nodeMap & (bitpos - 1));
    }

    public int nodeMap() {
        return nodeMap;
    }

    @Override
    public BitmapIndexedNode<D> remove(final UniqueId mutator,
                                       final D data,
                                       final int dataHash, final int shift,
                                       final ChangeEvent<D> details, BiPredicate<D, D> equalsFunction) {
        final int mask = mask(dataHash, shift);
        final int bitpos = bitpos(mask);
        if ((dataMap & bitpos) != 0) {
            return removeData(mutator, data, dataHash, shift, details, bitpos, equalsFunction);
        }
        if ((nodeMap & bitpos) != 0) {
            return removeSubNode(mutator, data, dataHash, shift, details, bitpos, equalsFunction);
        }
        return this;
    }

    private BitmapIndexedNode<D> removeData(UniqueId mutator, D data, int dataHash, int shift, ChangeEvent<D> details, int bitpos, BiPredicate<D, D> equalsFunction) {
        final int dataIndex = dataIndex(bitpos);
        int entryLength = 1;
        if (!equalsFunction.test(getData(dataIndex), data)) {
            return this;
        }
        final D currentVal = getData(dataIndex);
        details.setRemoved(currentVal);
        if (dataArity() == 2 && !hasNodes()) {
            final int newDataMap =
                    (shift == 0) ? (dataMap ^ bitpos) : bitpos(mask(dataHash, 0));
            Object[] nodes = {getData(dataIndex ^ 1)};
            return newBitmapIndexedNode(mutator, 0, newDataMap, nodes);
        }
        int idx = dataIndex * entryLength;
        final Object[] dst = ArrayHelper.copyComponentRemove(this.mixed, idx, entryLength);
        return newBitmapIndexedNode(mutator, nodeMap, dataMap ^ bitpos, dst);
    }

    private BitmapIndexedNode<D> removeSubNode(UniqueId mutator, D data, int dataHash, int shift,
                                               ChangeEvent<D> details,
                                               int bitpos, BiPredicate<D, D> equalsFunction) {
        final Node<D> subNode = nodeAt(bitpos);
        final Node<D> updatedSubNode =
                subNode.remove(mutator, data, dataHash, shift + BIT_PARTITION_SIZE, details, equalsFunction);
        if (subNode == updatedSubNode) {
            return this;
        }
        if (!updatedSubNode.hasNodes() && updatedSubNode.hasDataArityOne()) {
            if (!hasData() && nodeArity() == 1) {
                return (BitmapIndexedNode<D>) updatedSubNode;
            }
            return copyAndMigrateFromNodeToData(mutator, bitpos, updatedSubNode);
        }
        return copyAndSetNode(mutator, bitpos, updatedSubNode);
    }

    @Override
    public BitmapIndexedNode<D> update(UniqueId mutator,
                                       D data,
                                       int dataHash, int shift,
                                       ChangeEvent<D> details,
                                       BiFunction<D, D, D> updateFunction,
                                       BiPredicate<D, D> equalsFunction,
                                       ToIntFunction<D> hashFunction) {
        final int mask = mask(dataHash, shift);
        final int bitpos = bitpos(mask);
        if ((dataMap & bitpos) != 0) {
            final int dataIndex = dataIndex(bitpos);
            final D oldKey = getData(dataIndex);
            if (equalsFunction.test(oldKey, data)) {
                D updatedKey = updateFunction.apply(oldKey, data);
                if (updatedKey == oldKey) {
                    details.found(oldKey);
                    return this;
                }
                details.setReplaced(oldKey);
                return copyAndSetData(mutator, dataIndex, updatedKey);
            }
            final Node<D> updatedSubNode =
                    mergeTwoDataEntriesIntoNode(mutator,
                            oldKey, hashFunction.applyAsInt(oldKey),
                            data, dataHash, shift + BIT_PARTITION_SIZE);
            details.setAdded();
            return copyAndMigrateFromDataToNode(mutator, bitpos, updatedSubNode);
        } else if ((nodeMap & bitpos) != 0) {
            Node<D> subNode = nodeAt(bitpos);
            final Node<D> updatedSubNode = subNode
                    .update(mutator, data, dataHash, shift + BIT_PARTITION_SIZE, details, updateFunction, equalsFunction, hashFunction);
            return subNode == updatedSubNode ? this : copyAndSetNode(mutator, bitpos, updatedSubNode);
        }
        details.setAdded();
        return copyAndInsertData(mutator, bitpos, data);
    }


    private BitmapIndexedNode<D> copyAndSetData(UniqueId mutator, int dataIndex, D updatedData) {
        if (isAllowedToUpdate(mutator)) {
            this.mixed[dataIndex] = updatedData;
            return this;
        }
        final Object[] newMixed = ArrayHelper.copySet(this.mixed, dataIndex, updatedData);
        return newBitmapIndexedNode(mutator, nodeMap, dataMap, newMixed);
    }
}