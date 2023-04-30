/* ____  ______________  ________________________  __________
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


import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

import static io.vavr.collection.ChampNodeFactory.newBitmapIndexedNode;

/**
 * Represents a bitmap-indexed node in a CHAMP trie.
 * <p>
 * References:
 * <p>
 * This class has been derived from 'The Capsule Hash Trie Collections Library'.
 * <dl>
 *      <dt>The Capsule Hash Trie Collections Library.
 *      <br>Copyright (c) Michael Steindorfer. <a href="https://github.com/usethesource/capsule/blob/3856cd65fa4735c94bcfa94ec9ecf408429b54f4/LICENSE">BSD-2-Clause License</a></dt>
 *      <dd><a href="https://github.com/usethesource/capsule">github.com</a>
 * </dl>
 *
 * @param <D> the data type
 */
 class ChampBitmapIndexedNode<D> extends ChampNode<D> {
    static final ChampBitmapIndexedNode<?> EMPTY_NODE = newBitmapIndexedNode(null, (0), (0), new Object[]{});

     final Object  [] mixed;
    private final int nodeMap;
    private final int dataMap;

    protected ChampBitmapIndexedNode(int nodeMap,
                                     int dataMap, Object  [] mixed) {
        this.nodeMap = nodeMap;
        this.dataMap = dataMap;
        this.mixed = mixed;
        assert mixed.length == nodeArity() + dataArity();
    }

    @SuppressWarnings("unchecked")
     static <K> ChampBitmapIndexedNode<K> emptyNode() {
        return (ChampBitmapIndexedNode<K>) EMPTY_NODE;
    }

     ChampBitmapIndexedNode<D> copyAndInsertData(ChampIdentityObject mutator, int bitpos,
                                                 D data) {
        int idx = dataIndex(bitpos);
        Object[] dst = ChampListHelper.copyComponentAdd(this.mixed, idx, 1);
        dst[idx] = data;
        return newBitmapIndexedNode(mutator, nodeMap, dataMap | bitpos, dst);
    }

     ChampBitmapIndexedNode<D> copyAndMigrateFromDataToNode(ChampIdentityObject mutator,
                                                            int bitpos, ChampNode<D> node) {

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

     ChampBitmapIndexedNode<D> copyAndMigrateFromNodeToData(ChampIdentityObject mutator,
                                                            int bitpos, ChampNode<D> node) {
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

     ChampBitmapIndexedNode<D> copyAndSetNode(ChampIdentityObject mutator, int bitpos,
                                              ChampNode<D> node) {

        int idx = this.mixed.length - 1 - nodeIndex(bitpos);
        if (isAllowedToUpdate(mutator)) {
            // no copying if already editable
            this.mixed[idx] = node;
            return this;
        } else {
            // copy 'src' and set 1 element(s) at position 'idx'
            final Object[] dst = ChampListHelper.copySet(this.mixed, idx, node);
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

     int dataMap() {
        return dataMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean equivalent( Object other) {
        if (this == other) {
            return true;
        }
        ChampBitmapIndexedNode<?> that = (ChampBitmapIndexedNode<?>) other;
        Object[] thatNodes = that.mixed;
        // nodes array: we compare local data from 0 to splitAt (excluded)
        // and then we compare the nested nodes from splitAt to length (excluded)
        int splitAt = dataArity();
        return nodeMap() == that.nodeMap()
                && dataMap() == that.dataMap()
                && Arrays.equals(mixed, 0, splitAt, thatNodes, 0, splitAt)
                && Arrays.equals(mixed, splitAt, mixed.length, thatNodes, splitAt, thatNodes.length,
                (a, b) -> ((ChampNode<D>) a).equivalent(b) ? 0 : 1);
    }


    @Override
    
     Object find(D key, int dataHash, int shift,  BiPredicate<D, D> equalsFunction) {
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
    
    D getData(int index) {
        return (D) mixed[index];
    }


    @Override
    @SuppressWarnings("unchecked")
    ChampNode<D> getNode(int index) {
        return (ChampNode<D>) mixed[mixed.length - 1 - index];
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
    ChampNode<D> nodeAt(int bitpos) {
        return (ChampNode<D>) mixed[mixed.length - 1 - nodeIndex(bitpos)];
    }

    @SuppressWarnings("unchecked")
    
    D dataAt(int bitpos) {
        return (D) mixed[dataIndex(bitpos)];
    }

    int nodeIndex(int bitpos) {
        return Integer.bitCount(nodeMap & (bitpos - 1));
    }

     int nodeMap() {
        return nodeMap;
    }

    @Override
    
     ChampBitmapIndexedNode<D> remove(ChampIdentityObject mutator,
                                            D data,
                                            int dataHash, int shift,
                                            ChampChangeEvent<D> details, BiPredicate<D, D> equalsFunction) {
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

    private ChampBitmapIndexedNode<D> removeData(ChampIdentityObject mutator, D data, int dataHash, int shift, ChampChangeEvent<D> details, int bitpos, BiPredicate<D, D> equalsFunction) {
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
        Object[] dst = ChampListHelper.copyComponentRemove(this.mixed, idx, entryLength);
        return newBitmapIndexedNode(mutator, nodeMap, dataMap ^ bitpos, dst);
    }

    private ChampBitmapIndexedNode<D> removeSubNode(ChampIdentityObject mutator, D data, int dataHash, int shift,
                                                    ChampChangeEvent<D> details,
                                                    int bitpos, BiPredicate<D, D> equalsFunction) {
        ChampNode<D> subNode = nodeAt(bitpos);
        ChampNode<D> updatedSubNode =
                subNode.remove(mutator, data, dataHash, shift + BIT_PARTITION_SIZE, details, equalsFunction);
        if (subNode == updatedSubNode) {
            return this;
        }
        if (!updatedSubNode.hasNodes() && updatedSubNode.hasDataArityOne()) {
            if (!hasData() && nodeArity() == 1) {
                return (ChampBitmapIndexedNode<D>) updatedSubNode;
            }
            return copyAndMigrateFromNodeToData(mutator, bitpos, updatedSubNode);
        }
        return copyAndSetNode(mutator, bitpos, updatedSubNode);
    }

    @Override
    
     ChampBitmapIndexedNode<D> update(ChampIdentityObject mutator,
                                            D newData,
                                            int dataHash, int shift,
                                            ChampChangeEvent<D> details,
                                            BiFunction<D, D, D> updateFunction,
                                            BiPredicate<D, D> equalsFunction,
                                            ToIntFunction<D> hashFunction) {
        int mask = mask(dataHash, shift);
        int bitpos = bitpos(mask);
        if ((dataMap & bitpos) != 0) {
            final int dataIndex = dataIndex(bitpos);
            final D oldData = getData(dataIndex);
            if (equalsFunction.test(oldData, newData)) {
                D updatedData = updateFunction.apply(oldData, newData);
                if (updatedData == oldData) {
                    details.found(oldData);
                    return this;
                }
                details.setReplaced(oldData, updatedData);
                return copyAndSetData(mutator, dataIndex, updatedData);
            }
            ChampNode<D> updatedSubNode =
                    mergeTwoDataEntriesIntoNode(mutator,
                            oldData, hashFunction.applyAsInt(oldData),
                            newData, dataHash, shift + BIT_PARTITION_SIZE);
            details.setAdded(newData);
            return copyAndMigrateFromDataToNode(mutator, bitpos, updatedSubNode);
        } else if ((nodeMap & bitpos) != 0) {
            ChampNode<D> subNode = nodeAt(bitpos);
            ChampNode<D> updatedSubNode = subNode
                    .update(mutator, newData, dataHash, shift + BIT_PARTITION_SIZE, details, updateFunction, equalsFunction, hashFunction);
            return subNode == updatedSubNode ? this : copyAndSetNode(mutator, bitpos, updatedSubNode);
        }
        details.setAdded(newData);
        return copyAndInsertData(mutator, bitpos, newData);
    }

    
    private ChampBitmapIndexedNode<D> copyAndSetData(ChampIdentityObject mutator, int dataIndex, D updatedData) {
        if (isAllowedToUpdate(mutator)) {
            this.mixed[dataIndex] = updatedData;
            return this;
        }
        Object[] newMixed = ChampListHelper.copySet(this.mixed, dataIndex, updatedData);
        return newBitmapIndexedNode(mutator, nodeMap, dataMap, newMixed);
    }
}