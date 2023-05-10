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
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import static io.vavr.collection.ChampListHelper.arrayEquals;
import static io.vavr.collection.ChampNodeFactory.newBitmapIndexedNode;

/**
 * Represents a bitmap-indexed node in a CHAMP trie.
 * <p>
 * References:
 * <p>
 * Portions of the code in this class have been derived from 'The Capsule Hash Trie Collections Library', and from
 * 'JHotDraw 8'.
 * <dl>
 *     <dt>The Capsule Hash Trie Collections Library.
 *     <br>Copyright (c) Michael Steindorfer. <a href="https://github.com/usethesource/capsule/blob/3856cd65fa4735c94bcfa94ec9ecf408429b54f4/LICENSE">BSD-2-Clause License</a></dt>
 *     <dd><a href="https://github.com/usethesource/capsule">github.com</a>
 *     </dd>
 *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
 *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
 *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a>
 *     </dd>
 * </dl>
 *
 * @param <D> the data type
 */
 class ChampBitmapIndexedNode<D> extends ChampNode<D> {
    static final ChampBitmapIndexedNode<?> EMPTY_NODE = newBitmapIndexedNode(null, (0), (0), new Object[]{});

     final Object  [] mixed;
    private final int nodeMap;
    private final int dataMap;

     ChampBitmapIndexedNode(int nodeMap,
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

     ChampBitmapIndexedNode<D> copyAndInsertData(ChampIdentityObject owner, int bitpos,
                                                 D data) {
        int idx = dataIndex(bitpos);
        Object[] dst = ChampListHelper.copyComponentAdd(this.mixed, idx, 1);
        dst[idx] = data;
        return newBitmapIndexedNode(owner, nodeMap, dataMap | bitpos, dst);
    }

     ChampBitmapIndexedNode<D> copyAndMigrateFromDataToNode(ChampIdentityObject owner,
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
        return newBitmapIndexedNode(owner, nodeMap | bitpos, dataMap ^ bitpos, dst);
    }

     ChampBitmapIndexedNode<D> copyAndMigrateFromNodeToData(ChampIdentityObject owner,
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
        return newBitmapIndexedNode(owner, nodeMap ^ bitpos, dataMap | bitpos, dst);
    }

     ChampBitmapIndexedNode<D> copyAndSetNode(ChampIdentityObject owner, int bitpos,
                                              ChampNode<D> node) {

        int idx = this.mixed.length - 1 - nodeIndex(bitpos);
        if (isAllowedToUpdate(owner)) {
            // no copying if already editable
            this.mixed[idx] = node;
            return this;
        } else {
            // copy 'src' and set 1 element(s) at position 'idx'
            final Object[] dst = ChampListHelper.copySet(this.mixed, idx, node);
            return newBitmapIndexedNode(owner, nodeMap, dataMap, dst);
        }
    }

    @Override
    int dataArity() {
        return Integer.bitCount(dataMap);
    }

    int dataIndex(int bitpos) {
        return Integer.bitCount(dataMap & (bitpos - 1));
    }

    int index(int map, int bitpos) {
        return Integer.bitCount(map & (bitpos - 1));
    }

    int dataMap() {
        return dataMap;
    }

    @SuppressWarnings("unchecked")
    @Override
     boolean equivalent( Object other) {
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
                && arrayEquals(mixed, 0, splitAt, thatNodes, 0, splitAt)
                && arrayEquals(mixed, splitAt, mixed.length, thatNodes, splitAt, thatNodes.length,
                (a, b) -> ((ChampNode<D>) a).equivalent(b) );
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
    
     ChampBitmapIndexedNode<D> remove(ChampIdentityObject owner,
                                            D data,
                                            int dataHash, int shift,
                                            ChampChangeEvent<D> details, BiPredicate<D, D> equalsFunction) {
        int mask = mask(dataHash, shift);
        int bitpos = bitpos(mask);
        if ((dataMap & bitpos) != 0) {
            return removeData(owner, data, dataHash, shift, details, bitpos, equalsFunction);
        }
        if ((nodeMap & bitpos) != 0) {
            return removeSubNode(owner, data, dataHash, shift, details, bitpos, equalsFunction);
        }
        return this;
    }

    private ChampBitmapIndexedNode<D> removeData(ChampIdentityObject owner, D data, int dataHash, int shift, ChampChangeEvent<D> details, int bitpos, BiPredicate<D, D> equalsFunction) {
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
            return newBitmapIndexedNode(owner, 0, newDataMap, nodes);
        }
        int idx = dataIndex * entryLength;
        Object[] dst = ChampListHelper.copyComponentRemove(this.mixed, idx, entryLength);
        return newBitmapIndexedNode(owner, nodeMap, dataMap ^ bitpos, dst);
    }

    private ChampBitmapIndexedNode<D> removeSubNode(ChampIdentityObject owner, D data, int dataHash, int shift,
                                                    ChampChangeEvent<D> details,
                                                    int bitpos, BiPredicate<D, D> equalsFunction) {
        ChampNode<D> subNode = nodeAt(bitpos);
        ChampNode<D> updatedSubNode =
                subNode.remove(owner, data, dataHash, shift + BIT_PARTITION_SIZE, details, equalsFunction);
        if (subNode == updatedSubNode) {
            return this;
        }
        if (!updatedSubNode.hasNodes() && updatedSubNode.hasDataArityOne()) {
            if (!hasData() && nodeArity() == 1) {
                return (ChampBitmapIndexedNode<D>) updatedSubNode;
            }
            return copyAndMigrateFromNodeToData(owner, bitpos, updatedSubNode);
        }
        return copyAndSetNode(owner, bitpos, updatedSubNode);
    }

    @Override
    
     ChampBitmapIndexedNode<D> put(ChampIdentityObject owner,
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
                return copyAndSetData(owner, dataIndex, updatedData);
            }
            ChampNode<D> updatedSubNode =
                    mergeTwoDataEntriesIntoNode(owner,
                            oldData, hashFunction.applyAsInt(oldData),
                            newData, dataHash, shift + BIT_PARTITION_SIZE);
            details.setAdded(newData);
            return copyAndMigrateFromDataToNode(owner, bitpos, updatedSubNode);
        } else if ((nodeMap & bitpos) != 0) {
            ChampNode<D> subNode = nodeAt(bitpos);
            ChampNode<D> updatedSubNode = subNode
                    .put(owner, newData, dataHash, shift + BIT_PARTITION_SIZE, details, updateFunction, equalsFunction, hashFunction);
            return subNode == updatedSubNode ? this : copyAndSetNode(owner, bitpos, updatedSubNode);
        }
        details.setAdded(newData);
        return copyAndInsertData(owner, bitpos, newData);
    }

    
    private ChampBitmapIndexedNode<D> copyAndSetData(ChampIdentityObject owner, int dataIndex, D updatedData) {
        if (isAllowedToUpdate(owner)) {
            this.mixed[dataIndex] = updatedData;
            return this;
        }
        Object[] newMixed = ChampListHelper.copySet(this.mixed, dataIndex, updatedData);
        return newBitmapIndexedNode(owner, nodeMap, dataMap, newMixed);
    }


    @SuppressWarnings("unchecked")
    @Override
    
     ChampBitmapIndexedNode<D> putAll(ChampIdentityObject owner, ChampNode<D> other, int shift,
                                        ChampBulkChangeEvent bulkChange,
                                        BiFunction<D, D, D> updateFunction,
                                        BiPredicate<D, D> equalsFunction,
                                        ToIntFunction<D> hashFunction,
                                        ChampChangeEvent<D> details) {
        ChampBitmapIndexedNode<D> that = (ChampBitmapIndexedNode<D>) other;
        if (this == that) {
            bulkChange.inBoth += this.calculateSize();
            return this;
        }

        int newBitMap = nodeMap | dataMap | that.nodeMap | that.dataMap;
        Object[] buffer = new Object[Integer.bitCount(newBitMap)];
        int newDataMap = this.dataMap | that.dataMap;
        int newNodeMap = this.nodeMap | that.nodeMap;
        for (int mapToDo = newBitMap; mapToDo != 0; mapToDo ^= Integer.lowestOneBit(mapToDo)) {
            int mask = Integer.numberOfTrailingZeros(mapToDo);
            int bitpos = bitpos(mask);

            boolean thisIsData = (this.dataMap & bitpos) != 0;
            boolean thatIsData = (that.dataMap & bitpos) != 0;
            boolean thisIsNode = (this.nodeMap & bitpos) != 0;
            boolean thatIsNode = (that.nodeMap & bitpos) != 0;

            if (!(thisIsNode || thisIsData)) {
                // add 'mixed' (data or node) from that trie
                if (thatIsData) {
                    buffer[index(newDataMap, bitpos)] = that.getData(that.dataIndex(bitpos));
                } else {
                    buffer[buffer.length - 1 - index(newNodeMap, bitpos)] = that.getNode(that.nodeIndex(bitpos));
                }
            } else if (!(thatIsNode || thatIsData)) {
                // add 'mixed' (data or node) from this trie
                if (thisIsData) {
                    buffer[index(newDataMap, bitpos)] = this.getData(dataIndex(bitpos));
                } else {
                    buffer[buffer.length - 1 - index(newNodeMap, bitpos)] = this.getNode(nodeIndex(bitpos));
                }
            } else if (thisIsNode && thatIsNode) {
                // add a new node that joins this node and that node
                ChampNode<D> thisNode = this.getNode(this.nodeIndex(bitpos));
                ChampNode<D> thatNode = that.getNode(that.nodeIndex(bitpos));
                buffer[buffer.length - 1 - index(newNodeMap, bitpos)] = thisNode.putAll(owner, thatNode, shift + BIT_PARTITION_SIZE, bulkChange,
                        updateFunction, equalsFunction, hashFunction, details);
            } else if (thisIsData && thatIsNode) {
                // add a new node that joins this data and that node
                D thisData = this.getData(this.dataIndex(bitpos));
                ChampNode<D> thatNode = that.getNode(that.nodeIndex(bitpos));
                details.reset();
                buffer[buffer.length - 1 - index(newNodeMap, bitpos)] = thatNode.put(null, thisData, hashFunction.applyAsInt(thisData), shift + BIT_PARTITION_SIZE, details,
                        (a, b) -> updateFunction.apply(b, a),
                        equalsFunction, hashFunction);
                if (details.isUnchanged()) {
                    bulkChange.inBoth++;
                } else if (details.isReplaced()) {
                    bulkChange.replaced = true;
                    bulkChange.inBoth++;
                }
                newDataMap ^= bitpos;
            } else if (thisIsNode) {
                // add a new node that joins this node and that data
                D thatData = that.getData(that.dataIndex(bitpos));
                ChampNode<D> thisNode = this.getNode(this.nodeIndex(bitpos));
                details.reset();
                buffer[buffer.length - 1 - index(newNodeMap, bitpos)] = thisNode.put(owner, thatData, hashFunction.applyAsInt(thatData), shift + BIT_PARTITION_SIZE, details, updateFunction, equalsFunction, hashFunction);
                if (!details.isModified()) {
                    bulkChange.inBoth++;
                }
                newDataMap ^= bitpos;
            } else {
                // add a new node that joins this data and that data
                D thisData = this.getData(this.dataIndex(bitpos));
                D thatData = that.getData(that.dataIndex(bitpos));
                if (equalsFunction.test(thisData, thatData)) {
                    bulkChange.inBoth++;
                    D updated = updateFunction.apply(thisData, thatData);
                    buffer[index(newDataMap, bitpos)] = updated;
                    bulkChange.replaced |= updated != thisData;
                } else {
                    newDataMap ^= bitpos;
                    newNodeMap ^= bitpos;
                    buffer[buffer.length - 1 - index(newNodeMap, bitpos)] = mergeTwoDataEntriesIntoNode(owner, thisData, hashFunction.applyAsInt(thisData), thatData, hashFunction.applyAsInt(thatData), shift + BIT_PARTITION_SIZE);
                }
            }
        }
        return new ChampBitmapIndexedNode<>(newNodeMap, newDataMap, buffer);
    }

    @Override
    
     ChampBitmapIndexedNode<D> removeAll( ChampIdentityObject owner, ChampNode<D> other, int shift,  ChampBulkChangeEvent bulkChange,  BiFunction<D, D, D> updateFunction,  BiPredicate<D, D> equalsFunction,  ToIntFunction<D> hashFunction,  ChampChangeEvent<D> details) {
        ChampBitmapIndexedNode<D> that = (ChampBitmapIndexedNode<D>) other;
        if (this == that) {
            bulkChange.inBoth += this.calculateSize();
            return this;
        }

        int newBitMap = nodeMap | dataMap;
        Object[] buffer = new Object[Integer.bitCount(newBitMap)];
        int newDataMap = this.dataMap;
        int newNodeMap = this.nodeMap;
        for (int mapToDo = newBitMap; mapToDo != 0; mapToDo ^= Integer.lowestOneBit(mapToDo)) {
            int mask = Integer.numberOfTrailingZeros(mapToDo);
            int bitpos = bitpos(mask);

            boolean thisIsData = (this.dataMap & bitpos) != 0;
            boolean thatIsData = (that.dataMap & bitpos) != 0;
            boolean thisIsNode = (this.nodeMap & bitpos) != 0;
            boolean thatIsNode = (that.nodeMap & bitpos) != 0;

            if (!(thisIsNode || thisIsData)) {
                // programming error
                assert false;
            } else if (!(thatIsNode || thatIsData)) {
                // keep 'mixed' (data or node) from this trie
                if (thisIsData) {
                    buffer[index(newDataMap, bitpos)] = this.getData(dataIndex(bitpos));
                } else {
                    buffer[buffer.length - 1 - index(newNodeMap, bitpos)] = this.getNode(nodeIndex(bitpos));
                }
            } else if (thisIsNode && thatIsNode) {
                // remove all in that node from all in this node
                ChampNode<D> thisNode = this.getNode(this.nodeIndex(bitpos));
                ChampNode<D> thatNode = that.getNode(that.nodeIndex(bitpos));
                ChampNode<D> result = thisNode.removeAll(owner, thatNode, shift + BIT_PARTITION_SIZE, bulkChange, updateFunction, equalsFunction, hashFunction, details);
                if (result.isNodeEmpty()) {
                    newNodeMap ^= bitpos;
                } else if (result.hasMany()) {
                    buffer[buffer.length - 1 - index(newNodeMap, bitpos)] = result;
                } else {
                    newNodeMap ^= bitpos;
                    newDataMap ^= bitpos;
                    buffer[index(newDataMap, bitpos)] = result.getData(0);
                }
            } else if (thisIsData && thatIsNode) {
                // remove this data if it is contained in that node
                D thisData = this.getData(this.dataIndex(bitpos));
                ChampNode<D> thatNode = that.getNode(that.nodeIndex(bitpos));
                Object result = thatNode.find(thisData, hashFunction.applyAsInt(thisData), shift + BIT_PARTITION_SIZE, equalsFunction);
                if (result == NO_DATA) {
                    buffer[index(newDataMap, bitpos)] = thisData;
                } else {
                    newDataMap ^= bitpos;
                    bulkChange.removed++;
                }
            } else if (thisIsNode) {
                // remove that data from this node
                D thatData = that.getData(that.dataIndex(bitpos));
                ChampNode<D> thisNode = this.getNode(this.nodeIndex(bitpos));
                details.reset();
                ChampNode<D> result = thisNode.remove(owner, thatData, hashFunction.applyAsInt(thatData), shift + BIT_PARTITION_SIZE, details, equalsFunction);
                if (details.isModified()) {
                    bulkChange.removed++;
                }
                if (result.isNodeEmpty()) {
                    newNodeMap ^= bitpos;
                } else if (result.hasMany()) {
                    buffer[buffer.length - 1 - index(newNodeMap, bitpos)] = result;
                } else {
                    newDataMap ^= bitpos;
                    newNodeMap ^= bitpos;
                    buffer[index(newDataMap, bitpos)] = result.getData(0);
                }
            } else {
                // remove this data if it is equal to that data
                D thisData = this.getData(this.dataIndex(bitpos));
                D thatData = that.getData(that.dataIndex(bitpos));
                if (equalsFunction.test(thisData, thatData)) {
                    bulkChange.removed++;
                    newDataMap ^= bitpos;
                } else {
                    buffer[index(newDataMap, bitpos)] = thisData;
                }
            }
        }
        return newCroppedBitmapIndexedNode(buffer, newDataMap, newNodeMap);
    }

    
    private ChampBitmapIndexedNode<D> newCroppedBitmapIndexedNode(Object[] buffer, int newDataMap, int newNodeMap) {
        int newLength = Integer.bitCount(newNodeMap | newDataMap);
        if (newLength != buffer.length) {
            Object[] temp = buffer;
            buffer = new Object[newLength];
            int dataCount = Integer.bitCount(newDataMap);
            int nodeCount = Integer.bitCount(newNodeMap);
            System.arraycopy(temp, 0, buffer, 0, dataCount);
            System.arraycopy(temp, temp.length - nodeCount, buffer, dataCount, nodeCount);
        }
        return new ChampBitmapIndexedNode<>(newNodeMap, newDataMap, buffer);
    }

    @Override
    
     ChampBitmapIndexedNode<D> retainAll(ChampIdentityObject owner, ChampNode<D> other, int shift,  ChampBulkChangeEvent bulkChange,  BiFunction<D, D, D> updateFunction,  BiPredicate<D, D> equalsFunction,  ToIntFunction<D> hashFunction,  ChampChangeEvent<D> details) {
        ChampBitmapIndexedNode<D> that = (ChampBitmapIndexedNode<D>) other;
        if (this == that) {
            bulkChange.inBoth += this.calculateSize();
            return this;
        }

        int newBitMap = nodeMap | dataMap;
        Object[] buffer = new Object[Integer.bitCount(newBitMap)];
        int newDataMap = this.dataMap;
        int newNodeMap = this.nodeMap;
        for (int mapToDo = newBitMap; mapToDo != 0; mapToDo ^= Integer.lowestOneBit(mapToDo)) {
            int mask = Integer.numberOfTrailingZeros(mapToDo);
            int bitpos = bitpos(mask);

            boolean thisIsData = (this.dataMap & bitpos) != 0;
            boolean thatIsData = (that.dataMap & bitpos) != 0;
            boolean thisIsNode = (this.nodeMap & bitpos) != 0;
            boolean thatIsNode = (that.nodeMap & bitpos) != 0;

            if (!(thisIsNode || thisIsData)) {
                // programming error
                assert false;
            } else if (!(thatIsNode || thatIsData)) {
                // remove 'mixed' (data or node) from this trie
                if (thisIsData) {
                    newDataMap ^= bitpos;
                    bulkChange.removed++;
                } else {
                    newNodeMap ^= bitpos;
                    bulkChange.removed += this.getNode(this.nodeIndex(bitpos)).calculateSize();
                }
            } else if (thisIsNode && thatIsNode) {
                // retain all in that node from all in this node
                ChampNode<D> thisNode = this.getNode(this.nodeIndex(bitpos));
                ChampNode<D> thatNode = that.getNode(that.nodeIndex(bitpos));
                ChampNode<D> result = thisNode.retainAll(owner, thatNode, shift + BIT_PARTITION_SIZE, bulkChange, updateFunction, equalsFunction, hashFunction, details);
                if (result.isNodeEmpty()) {
                    newNodeMap ^= bitpos;
                } else if (result.hasMany()) {
                    buffer[buffer.length - 1 - index(newNodeMap, bitpos)] = result;
                } else {
                    newNodeMap ^= bitpos;
                    newDataMap ^= bitpos;
                    buffer[index(newDataMap, bitpos)] = result.getData(0);
                }
            } else if (thisIsData && thatIsNode) {
                // retain this data if it is contained in that node
                D thisData = this.getData(this.dataIndex(bitpos));
                ChampNode<D> thatNode = that.getNode(that.nodeIndex(bitpos));
                Object result = thatNode.find(thisData, hashFunction.applyAsInt(thisData), shift + BIT_PARTITION_SIZE, equalsFunction);
                if (result == NO_DATA) {
                    newDataMap ^= bitpos;
                    bulkChange.removed++;
                } else {
                    buffer[index(newDataMap, bitpos)] = thisData;
                }
            } else if (thisIsNode) {
                // retain this data if that data is contained in this node
                D thatData = that.getData(that.dataIndex(bitpos));
                ChampNode<D> thisNode = this.getNode(this.nodeIndex(bitpos));
                Object result = thisNode.find(thatData, hashFunction.applyAsInt(thatData), shift + BIT_PARTITION_SIZE, equalsFunction);
                if (result == NO_DATA) {
                    bulkChange.removed += this.getNode(this.nodeIndex(bitpos)).calculateSize();
                    newNodeMap ^= bitpos;
                } else {
                    newDataMap ^= bitpos;
                    newNodeMap ^= bitpos;
                    buffer[index(newDataMap, bitpos)] = result;
                    bulkChange.removed += this.getNode(this.nodeIndex(bitpos)).calculateSize() - 1;
                }
            } else {
                // retain this data if it is equal to that data
                D thisData = this.getData(this.dataIndex(bitpos));
                D thatData = that.getData(that.dataIndex(bitpos));
                if (equalsFunction.test(thisData, thatData)) {
                    buffer[index(newDataMap, bitpos)] = thisData;
                } else {
                    bulkChange.removed++;
                    newDataMap ^= bitpos;
                }
            }
        }
        return newCroppedBitmapIndexedNode(buffer, newDataMap, newNodeMap);
    }

    @Override
     ChampBitmapIndexedNode<D> filterAll(ChampIdentityObject owner, Predicate<? super D> predicate, int shift, ChampBulkChangeEvent bulkChange) {
        int newBitMap = nodeMap | dataMap;
        Object[] buffer = new Object[Integer.bitCount(newBitMap)];
        int newDataMap = this.dataMap;
        int newNodeMap = this.nodeMap;
        for (int mapToDo = newBitMap; mapToDo != 0; mapToDo ^= Integer.lowestOneBit(mapToDo)) {
            int mask = Integer.numberOfTrailingZeros(mapToDo);
            int bitpos = bitpos(mask);
            boolean thisIsNode = (this.nodeMap & bitpos) != 0;
            if (thisIsNode) {
                ChampNode<D> thisNode = this.getNode(this.nodeIndex(bitpos));
                ChampNode<D> result = thisNode.filterAll(owner, predicate, shift + BIT_PARTITION_SIZE, bulkChange);
                if (result.isNodeEmpty()) {
                    newNodeMap ^= bitpos;
                } else if (result.hasMany()) {
                    buffer[buffer.length - 1 - index(newNodeMap, bitpos)] = result;
                } else {
                    newNodeMap ^= bitpos;
                    newDataMap ^= bitpos;
                    buffer[index(newDataMap, bitpos)] = result.getData(0);
                }
            } else {
                D thisData = this.getData(this.dataIndex(bitpos));
                if (predicate.test(thisData)) {
                    buffer[index(newDataMap, bitpos)] = thisData;
                } else {
                    newDataMap ^= bitpos;
                    bulkChange.removed++;
                }
            }
        }
        return newCroppedBitmapIndexedNode(buffer, newDataMap, newNodeMap);
    }

     int calculateSize() {
        int size = dataArity();
        for (int i = 0, n = nodeArity(); i < n; i++) {
            ChampNode<D> node = getNode(i);
            size += node.calculateSize();
        }
        return size;
    }
}