/*
 * @(#)BitmapIndexedNode.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;


import java.util.Arrays;
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
    static final @NonNull BitmapIndexedNode<?> EMPTY_NODE = newBitmapIndexedNode(null, (0), (0), new Object[]{});

    final Object @NonNull [] mixed;
    private final int nodeMap;
    private final int dataMap;

    protected BitmapIndexedNode(int nodeMap,
                                int dataMap, @NonNull Object @NonNull [] mixed) {
        this.nodeMap = nodeMap;
        this.dataMap = dataMap;
        this.mixed = mixed;
        assert mixed.length == nodeArity() + dataArity();
    }

    @SuppressWarnings("unchecked")
    public static <K> @NonNull BitmapIndexedNode<K> emptyNode() {
        return (BitmapIndexedNode<K>) EMPTY_NODE;
    }

    @NonNull BitmapIndexedNode<D> copyAndInsertData(@Nullable IdentityObject mutator, int bitpos,
                                                    D data) {
        int idx = dataIndex(bitpos);
        Object[] dst = ListHelper.copyComponentAdd(this.mixed, idx, 1);
        dst[idx] = data;
        return newBitmapIndexedNode(mutator, nodeMap, dataMap | bitpos, dst);
    }

    @NonNull BitmapIndexedNode<D> copyAndMigrateFromDataToNode(@Nullable IdentityObject mutator,
                                                               int bitpos, Node<D> node) {

        int idxOld = dataIndex(bitpos);
        int idxNew = this.mixed.length - 1 - nodeIndex(bitpos);
        assert idxOld <= idxNew;

        // copy 'src' and remove entryLength element(s) at position 'idxOld' and
        // insert 1 element(s) at position 'idxNew'
        Object[] src = this.mixed;
        Object[] dst = new Object[src.length];
        System.arraycopy(src, 0, dst, 0, idxOld);
        System.arraycopy(src, idxOld + 1, dst, idxOld, idxNew - idxOld);
        System.arraycopy(src, idxNew + 1, dst, idxNew + 1, src.length - idxNew - 1);
        dst[idxNew] = node;
        return newBitmapIndexedNode(mutator, nodeMap | bitpos, dataMap ^ bitpos, dst);
    }

    @NonNull BitmapIndexedNode<D> copyAndMigrateFromNodeToData(@Nullable IdentityObject mutator,
                                                               int bitpos, @NonNull Node<D> node) {
        int idxOld = this.mixed.length - 1 - nodeIndex(bitpos);
        int idxNew = dataIndex(bitpos);

        // copy 'src' and remove 1 element(s) at position 'idxOld' and
        // insert entryLength element(s) at position 'idxNew'
        Object[] src = this.mixed;
        Object[] dst = new Object[src.length];
        assert idxOld >= idxNew;
        System.arraycopy(src, 0, dst, 0, idxNew);
        System.arraycopy(src, idxNew, dst, idxNew + 1, idxOld - idxNew);
        System.arraycopy(src, idxOld + 1, dst, idxOld + 1, src.length - idxOld - 1);
        dst[idxNew] = node.getData(0);
        return newBitmapIndexedNode(mutator, nodeMap ^ bitpos, dataMap | bitpos, dst);
    }

    @NonNull BitmapIndexedNode<D> copyAndSetNode(@Nullable IdentityObject mutator, int bitpos,
                                                 Node<D> node) {

        int idx = this.mixed.length - 1 - nodeIndex(bitpos);
        if (isAllowedToUpdate(mutator)) {
            // no copying if already editable
            this.mixed[idx] = node;
            return this;
        } else {
            // copy 'src' and set 1 element(s) at position 'idx'
            final Object[] dst = ListHelper.copySet(this.mixed, idx, node);
            return newBitmapIndexedNode(mutator, nodeMap, dataMap, dst);
        }
    }

    @Override
    int dataArity() {
        return Integer.bitCount(dataMap);
    }

    int dataIndex(int bitpos) {
        return Integer.bitCount(dataMap & (bitpos - 1));
    }

    public int dataMap() {
        return dataMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equivalent(@NonNull Object other) {
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
                && Arrays.equals(mixed, 0, splitAt, thatNodes, 0, splitAt)
                && Arrays.equals(mixed, splitAt, mixed.length, thatNodes, splitAt, thatNodes.length,
                (a, b) -> ((Node<D>) a).equivalent(b) ? 0 : 1);
    }


    @Override
    public @Nullable Object find(D key, int dataHash, int shift, @NonNull BiPredicate<D, D> equalsFunction) {
        int bitpos = bitpos(mask(dataHash, shift));
        if ((nodeMap & bitpos) != 0) {
            return nodeAt(bitpos).find(key, dataHash, shift + BIT_PARTITION_SIZE, equalsFunction);
        }
        if ((dataMap & bitpos) != 0) {
            D k = getData(dataIndex(bitpos));
            if (equalsFunction.test(k, key)) {
                return k;
            }
        }
        return NO_DATA;
    }


    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    D getData(int index) {
        return (D) mixed[index];
    }


    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    Node<D> getNode(int index) {
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
    @NonNull
    Node<D> nodeAt(int bitpos) {
        return (Node<D>) mixed[mixed.length - 1 - nodeIndex(bitpos)];
    }

    @SuppressWarnings("unchecked")
    @NonNull
    D dataAt(int bitpos) {
        return (D) mixed[dataIndex(bitpos)];
    }

    int nodeIndex(int bitpos) {
        return Integer.bitCount(nodeMap & (bitpos - 1));
    }

    public int nodeMap() {
        return nodeMap;
    }

    @Override
    public @NonNull BitmapIndexedNode<D> remove(@Nullable IdentityObject mutator,
                                                D data,
                                                int dataHash, int shift,
                                                @NonNull ChangeEvent<D> details, @NonNull BiPredicate<D, D> equalsFunction) {
        int mask = mask(dataHash, shift);
        int bitpos = bitpos(mask);
        if ((dataMap & bitpos) != 0) {
            return removeData(mutator, data, dataHash, shift, details, bitpos, equalsFunction);
        }
        if ((nodeMap & bitpos) != 0) {
            return removeSubNode(mutator, data, dataHash, shift, details, bitpos, equalsFunction);
        }
        return this;
    }

    private @NonNull BitmapIndexedNode<D> removeData(@Nullable IdentityObject mutator, D data, int dataHash, int shift, @NonNull ChangeEvent<D> details, int bitpos, @NonNull BiPredicate<D, D> equalsFunction) {
        int dataIndex = dataIndex(bitpos);
        int entryLength = 1;
        if (!equalsFunction.test(getData(dataIndex), data)) {
            return this;
        }
        D currentVal = getData(dataIndex);
        details.setRemoved(currentVal);
        if (dataArity() == 2 && !hasNodes()) {
            int newDataMap =
                    (shift == 0) ? (dataMap ^ bitpos) : bitpos(mask(dataHash, 0));
            Object[] nodes = {getData(dataIndex ^ 1)};
            return newBitmapIndexedNode(mutator, 0, newDataMap, nodes);
        }
        int idx = dataIndex * entryLength;
        Object[] dst = ListHelper.copyComponentRemove(this.mixed, idx, entryLength);
        return newBitmapIndexedNode(mutator, nodeMap, dataMap ^ bitpos, dst);
    }

    private @NonNull BitmapIndexedNode<D> removeSubNode(@Nullable IdentityObject mutator, D data, int dataHash, int shift,
                                                        @NonNull ChangeEvent<D> details,
                                                        int bitpos, @NonNull BiPredicate<D, D> equalsFunction) {
        Node<D> subNode = nodeAt(bitpos);
        Node<D> updatedSubNode =
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
    public @NonNull BitmapIndexedNode<D> update(@Nullable IdentityObject mutator,
                                                @Nullable D data,
                                                int dataHash, int shift,
                                                @NonNull ChangeEvent<D> details,
                                                @NonNull BiFunction<D, D, D> replaceFunction,
                                                @NonNull BiPredicate<D, D> equalsFunction,
                                                @NonNull ToIntFunction<D> hashFunction) {
        int mask = mask(dataHash, shift);
        int bitpos = bitpos(mask);
        if ((dataMap & bitpos) != 0) {
            final int dataIndex = dataIndex(bitpos);
            final D oldKey = getData(dataIndex);
            if (equalsFunction.test(oldKey, data)) {
                D updatedKey = replaceFunction.apply(oldKey, data);
                if (updatedKey == oldKey) {
                    details.found(oldKey);
                    return this;
                }
                details.setReplaced(oldKey);
                return copyAndSetData(mutator, dataIndex, updatedKey);
            }
            Node<D> updatedSubNode =
                    mergeTwoDataEntriesIntoNode(mutator,
                            oldKey, hashFunction.applyAsInt(oldKey),
                            data, dataHash, shift + BIT_PARTITION_SIZE);
            details.setAdded();
            return copyAndMigrateFromDataToNode(mutator, bitpos, updatedSubNode);
        } else if ((nodeMap & bitpos) != 0) {
            Node<D> subNode = nodeAt(bitpos);
            Node<D> updatedSubNode = subNode
                    .update(mutator, data, dataHash, shift + BIT_PARTITION_SIZE, details, replaceFunction, equalsFunction, hashFunction);
            return subNode == updatedSubNode ? this : copyAndSetNode(mutator, bitpos, updatedSubNode);
        }
        details.setAdded();
        return copyAndInsertData(mutator, bitpos, data);
    }

    @NonNull
    private BitmapIndexedNode<D> copyAndSetData(@Nullable IdentityObject mutator, int dataIndex, D updatedData) {
        if (isAllowedToUpdate(mutator)) {
            this.mixed[dataIndex] = updatedData;
            return this;
        }
        Object[] newMixed = ListHelper.copySet(this.mixed, dataIndex, updatedData);
        return newBitmapIndexedNode(mutator, nodeMap, dataMap, newMixed);
    }
}