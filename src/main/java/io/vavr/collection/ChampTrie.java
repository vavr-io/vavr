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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import static io.vavr.collection.ChampTrie.ChampListHelper.arrayEquals;
import static io.vavr.collection.ChampTrie.NodeFactory.newBitmapIndexedNode;
import static io.vavr.collection.ChampTrie.NodeFactory.newHashCollisionNode;

/**
 * 'Compressed Hash-Array Mapped Prefix-tree' (CHAMP) trie.
 * <p>
 * References:
 * <dl>
 *      <dt>The Capsule Hash Trie Collections Library.
 *      <br>Copyright (c) Michael Steindorfer. <a href="https://github.com/usethesource/capsule/blob/3856cd65fa4735c94bcfa94ec9ecf408429b54f4/LICENSE">BSD-2-Clause License</a></dt>
 *      <dd><a href="https://github.com/usethesource/capsule">github.com</a>
 * </dl>
 *
 */
public class ChampTrie {
    /**
     * Represents a node in a 'Compressed Hash-Array Mapped Prefix-tree'
     * (CHAMP) trie.
     * <p>
     * A trie is a tree structure that stores a set of data objects; the
     * path to a data object is determined by a bit sequence derived from the data
     * object.
     * <p>
     * In a CHAMP trie, the bit sequence is derived from the hash code of a data
     * object. A hash code is a bit sequence with a fixed length. This bit sequence
     * is split up into parts. Each part is used as the index to the next child node
     * in the tree, starting from the root node of the tree.
     * <p>
     * The nodes of a CHAMP trie are compressed. Instead of allocating a node for
     * each data object, the data objects are stored directly in the ancestor node
     * at which the path to the data object starts to become unique. This means,
     * that in most cases, only a prefix of the bit sequence is needed for the
     * path to a data object in the tree.
     * <p>
     * If the hash code of a data object in the set is not unique, then it is
     * stored in a {@link HashCollisionNode}, otherwise it is stored in a
     * {@link BitmapIndexedNode}. Since the hash codes have a fixed length,
     * all {@link HashCollisionNode}s are located at the same, maximal depth
     * of the tree.
     * <p>
     * In this implementation, a hash code has a length of
     * {@value #HASH_CODE_LENGTH} bits, and is split up in little-endian order into parts of
     * {@value #BIT_PARTITION_SIZE} bits (the last part contains the remaining bits).
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
     * @param <D> the type of the data objects that are stored in this trie
     */
     abstract static class Node<D> {
        /**
         * Represents no data.
         * We can not use {@code null}, because we allow storing null-data in the
         * trie.
         */
         static final Object NO_DATA = new Object();
        static final int HASH_CODE_LENGTH = 32;
        /**
         * Bit partition size in the range [1,5].
         * <p>
         * The bit-mask must fit into the 32 bits of an int field ({@code 32 = 1<<5}).
         * (You can use a size of 6, if you replace the bit-mask fields with longs).
         */
        static final int BIT_PARTITION_SIZE = 5;
        static final int BIT_PARTITION_MASK = (1 << BIT_PARTITION_SIZE) - 1;
        static final int MAX_DEPTH = (HASH_CODE_LENGTH + BIT_PARTITION_SIZE - 1) / BIT_PARTITION_SIZE + 1;


        Node() {
        }

        /**
         * Given a masked dataHash, returns its bit-position
         * in the bit-map.
         * <p>
         * For example, if the bit partition is 5 bits, then
         * we 2^5 == 32 distinct bit-positions.
         * If the masked dataHash is 3 then the bit-position is
         * the bit with index 3. That is, 1<<3 = 0b0100.
         *
         * @param mask masked data hash
         * @return bit position
         */
        static int bitpos(int mask) {
            return 1 << mask;
        }

         static <E>  E getFirst( Node<E> node) {
            while (node instanceof BitmapIndexedNode) {
                BitmapIndexedNode<E> bxn = (BitmapIndexedNode<E>) node;
                int nodeMap = bxn.nodeMap();
                int dataMap = bxn.dataMap();
                if ((nodeMap | dataMap) == 0) {
                    break;
                }
                int firstNodeBit = Integer.numberOfTrailingZeros(nodeMap);
                int firstDataBit = Integer.numberOfTrailingZeros(dataMap);
                if (nodeMap != 0 && firstNodeBit < firstDataBit) {
                    node = node.getNode(0);
                } else {
                    return node.getData(0);
                }
            }
            if (node instanceof HashCollisionNode) {
                HashCollisionNode<E> hcn = (HashCollisionNode<E>) node;
                return hcn.getData(0);
            }
            throw new NoSuchElementException();
        }

         static <E>  E getLast( Node<E> node) {
            while (node instanceof BitmapIndexedNode) {
                BitmapIndexedNode<E> bxn = (BitmapIndexedNode<E>) node;
                int nodeMap = bxn.nodeMap();
                int dataMap = bxn.dataMap();
                if ((nodeMap | dataMap) == 0) {
                    break;
                }
                if (Integer.compareUnsigned(nodeMap, dataMap) > 0) {
                    node = node.getNode(node.nodeArity() - 1);
                } else {
                    return node.getData(node.dataArity() - 1);
                }
            }
            if (node instanceof HashCollisionNode) {
                HashCollisionNode<E> hcn = (HashCollisionNode<E>) node;
                return hcn.getData(hcn.dataArity() - 1);
            }
            throw new NoSuchElementException();
        }

        static int mask(int dataHash, int shift) {
            return (dataHash >>> shift) & BIT_PARTITION_MASK;
        }

        static <K> Node<K> mergeTwoDataEntriesIntoNode(IdentityObject owner,
                                                       K k0, int keyHash0,
                                                       K k1, int keyHash1,
                                                       int shift) {
            if (shift >= HASH_CODE_LENGTH) {
                Object[] entries = new Object[2];
                entries[0] = k0;
                entries[1] = k1;
                return NodeFactory.newHashCollisionNode(owner, keyHash0, entries);
            }

            int mask0 = mask(keyHash0, shift);
            int mask1 = mask(keyHash1, shift);

            if (mask0 != mask1) {
                // both nodes fit on same level
                int dataMap = bitpos(mask0) | bitpos(mask1);

                Object[] entries = new Object[2];
                if (mask0 < mask1) {
                    entries[0] = k0;
                    entries[1] = k1;
                } else {
                    entries[0] = k1;
                    entries[1] = k0;
                }
                return NodeFactory.newBitmapIndexedNode(owner, (0), dataMap, entries);
            } else {
                Node<K> node = mergeTwoDataEntriesIntoNode(owner,
                        k0, keyHash0,
                        k1, keyHash1,
                        shift + BIT_PARTITION_SIZE);
                // values fit on next level

                int nodeMap = bitpos(mask0);
                return NodeFactory.newBitmapIndexedNode(owner, nodeMap, (0), new Object[]{node});
            }
        }

        abstract int dataArity();

        /**
         * Checks if this trie is equivalent to the specified other trie.
         *
         * @param other the other trie
         * @return true if equivalent
         */
        abstract boolean equivalent( Object other);

        /**
         * Finds a data object in the CHAMP trie, that matches the provided data
         * object and data hash.
         *
         * @param data           the provided data object
         * @param dataHash       the hash code of the provided data
         * @param shift          the shift for this node
         * @param equalsFunction a function that tests data objects for equality
         * @return the found data, returns {@link #NO_DATA} if no data in the trie
         * matches the provided data.
         */
        abstract Object find(D data, int dataHash, int shift,  BiPredicate<D, D> equalsFunction);

        abstract  D getData(int index);

         IdentityObject getOwner() {
            return null;
        }

        abstract Node<D> getNode(int index);

        abstract boolean hasData();

        boolean isNodeEmpty() {
            return !hasData() && !hasNodes();
        }

        boolean hasMany() {
            return hasNodes() || dataArity() > 1;
        }

        abstract boolean hasDataArityOne();

        abstract boolean hasNodes();

        boolean isAllowedToUpdate( IdentityObject y) {
            IdentityObject x = getOwner();
            return x != null && x == y;
        }

        abstract int nodeArity();

        /**
         * Removes a data object from the trie.
         *
         * @param owner        A non-null value means, that this method may update
         *                       nodes that are marked with the same unique id,
         *                       and that this method may create new mutable nodes
         *                       with this unique id.
         *                       A null value means, that this method must not update
         *                       any node and may only create new immutable nodes.
         * @param data           the data to be removed
         * @param dataHash       the hash-code of the data object
         * @param shift          the shift of the current node
         * @param details        this method reports the changes that it performed
         *                       in this object
         * @param equalsFunction a function that tests data objects for equality
         * @return the updated trie
         */
        abstract Node<D> remove(IdentityObject owner, D data,
                                int dataHash, int shift,
                                ChangeEvent<D> details,
                                BiPredicate<D, D> equalsFunction);

        /**
         * Inserts or replaces a data object in the trie.
         *
         * @param owner        A non-null value means, that this method may update
         *                       nodes that are marked with the same unique id,
         *                       and that this method may create new mutable nodes
         *                       with this unique id.
         *                       A null value means, that this method must not update
         *                       any node and may only create new immutable nodes.
         * @param newData        the data to be inserted,
         *                       or to be used for merging if there is already
         *                       a matching data object in the trie
         * @param dataHash       the hash-code of the data object
         * @param shift          the shift of the current node
         * @param details        this method reports the changes that it performed
         *                       in this object
         * @param updateFunction only used if there is a matching data object
         *                       in the trie.
         *                       Given the existing data object (first argument) and
         *                       the new data object (second argument), yields a
         *                       new data object or returns either of the two.
         *                       In all cases, the update function must return
         *                       a data object that has the same data hash
         *                       as the existing data object.
         * @param equalsFunction a function that tests data objects for equality
         * @param hashFunction   a function that computes the hash-code for a data
         *                       object
         * @return the updated trie
         */
        abstract Node<D> put(IdentityObject owner, D newData,
                             int dataHash, int shift, ChangeEvent<D> details,
                             BiFunction<D, D, D> updateFunction,
                             BiPredicate<D, D> equalsFunction,
                             ToIntFunction<D> hashFunction);
       /**
         * Inserts or replaces data elements from the specified other trie in this trie.
         *
         * @param owner
         * @param otherNode      a node with the same shift as this node from the other trie
         * @param shift          the shift of this node and the other node
         * @param bulkChange     updates the field {@link BulkChangeEvent#inBoth}
         * @param updateFunction the update function for data elements
         * @param equalsFunction the equals function for data elements
         * @param hashFunction   the hash function for data elements
         * @param details        the change event for single elements
         * @return the updated trie
         */
         abstract Node<D> putAll(IdentityObject owner, Node<D> otherNode, int shift,
                                 BulkChangeEvent bulkChange,
                                 BiFunction<D, D, D> updateFunction,
                                 BiPredicate<D, D> equalsFunction,
                                 ToIntFunction<D> hashFunction,
                                 ChangeEvent<D> details);

        /**
         * Removes data elements in the specified other trie from this trie.
         *
         * @param owner
         * @param otherNode      a node with the same shift as this node from the other trie
         * @param shift          the shift of this node and the other node
         * @param bulkChange     updates the field {@link BulkChangeEvent#removed}
         * @param updateFunction the update function for data elements
         * @param equalsFunction the equals function for data elements
         * @param hashFunction   the hash function for data elements
         * @param details        the change event for single elements
         * @return the updated trie
         */
         abstract Node<D> removeAll(IdentityObject owner, Node<D> otherNode, int shift,
                                    BulkChangeEvent bulkChange,
                                    BiFunction<D, D, D> updateFunction,
                                    BiPredicate<D, D> equalsFunction,
                                    ToIntFunction<D> hashFunction,
                                    ChangeEvent<D> details);

        /**
         * Retains data elements in this trie that are also in the other trie - removes the rest.
         *
         * @param owner
         * @param otherNode      a node with the same shift as this node from the other trie
         * @param shift          the shift of this node and the other node
         * @param bulkChange     updates the field {@link BulkChangeEvent#removed}
         * @param updateFunction the update function for data elements
         * @param equalsFunction the equals function for data elements
         * @param hashFunction   the hash function for data elements
         * @param details        the change event for single elements
         * @return the updated trie
         */
         abstract Node<D> retainAll(IdentityObject owner, Node<D> otherNode, int shift,
                                    BulkChangeEvent bulkChange,
                                    BiFunction<D, D, D> updateFunction,
                                    BiPredicate<D, D> equalsFunction,
                                    ToIntFunction<D> hashFunction,
                                    ChangeEvent<D> details);

        /**
         * Retains data elements in this trie for which the provided predicate returns true.
         *
         * @param owner
         * @param predicate  a predicate that returns true for data elements that should be retained
         * @param shift      the shift of this node and the other node
         * @param bulkChange updates the field {@link BulkChangeEvent#removed}
         * @return the updated trie
         */
         abstract Node<D> filterAll(IdentityObject owner, Predicate<? super D> predicate, int shift,
                                    BulkChangeEvent bulkChange);

         abstract int calculateSize();}

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
    static class BitmapIndexedNode<D> extends Node<D> {
        static final BitmapIndexedNode<?> EMPTY_NODE = newBitmapIndexedNode(null, (0), (0), new Object[]{});

         final Object  [] mixed;
        private final int nodeMap;
        private final int dataMap;

         BitmapIndexedNode(int nodeMap,
                           int dataMap, Object  [] mixed) {
            this.nodeMap = nodeMap;
            this.dataMap = dataMap;
            this.mixed = mixed;
            assert mixed.length == nodeArity() + dataArity();
        }

        @SuppressWarnings("unchecked")
         static <K> BitmapIndexedNode<K> emptyNode() {
            return (BitmapIndexedNode<K>) EMPTY_NODE;
        }

         BitmapIndexedNode<D> copyAndInsertData(IdentityObject owner, int bitpos,
                                                D data) {
            int idx = dataIndex(bitpos);
            Object[] dst = ChampListHelper.copyComponentAdd(this.mixed, idx, 1);
            dst[idx] = data;
            return newBitmapIndexedNode(owner, nodeMap, dataMap | bitpos, dst);
        }

         BitmapIndexedNode<D> copyAndMigrateFromDataToNode(IdentityObject owner,
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
            return newBitmapIndexedNode(owner, nodeMap | bitpos, dataMap ^ bitpos, dst);
        }

         BitmapIndexedNode<D> copyAndMigrateFromNodeToData(IdentityObject owner,
                                                           int bitpos, Node<D> node) {
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

         BitmapIndexedNode<D> copyAndSetNode(IdentityObject owner, int bitpos,
                                             Node<D> node) {

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
            BitmapIndexedNode<?> that = (BitmapIndexedNode<?>) other;
            Object[] thatNodes = that.mixed;
            // nodes array: we compare local data from 0 to splitAt (excluded)
            // and then we compare the nested nodes from splitAt to length (excluded)
            int splitAt = dataArity();
            return nodeMap() == that.nodeMap()
                    && dataMap() == that.dataMap()
                    && arrayEquals(mixed, 0, splitAt, thatNodes, 0, splitAt)
                    && arrayEquals(mixed, splitAt, mixed.length, thatNodes, splitAt, thatNodes.length,
                    (a, b) -> ((Node<D>) a).equivalent(b) );
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
        Node<D> nodeAt(int bitpos) {
            return (Node<D>) mixed[mixed.length - 1 - nodeIndex(bitpos)];
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
        BitmapIndexedNode<D> remove(IdentityObject owner,
                                    D data,
                                    int dataHash, int shift,
                                    ChangeEvent<D> details, BiPredicate<D, D> equalsFunction) {
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

        private BitmapIndexedNode<D> removeData(IdentityObject owner, D data, int dataHash, int shift, ChangeEvent<D> details, int bitpos, BiPredicate<D, D> equalsFunction) {
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

        private BitmapIndexedNode<D> removeSubNode(IdentityObject owner, D data, int dataHash, int shift,
                                                   ChangeEvent<D> details,
                                                   int bitpos, BiPredicate<D, D> equalsFunction) {
            Node<D> subNode = nodeAt(bitpos);
            Node<D> updatedSubNode =
                    subNode.remove(owner, data, dataHash, shift + BIT_PARTITION_SIZE, details, equalsFunction);
            if (subNode == updatedSubNode) {
                return this;
            }
            if (!updatedSubNode.hasNodes() && updatedSubNode.hasDataArityOne()) {
                if (!hasData() && nodeArity() == 1) {
                    return (BitmapIndexedNode<D>) updatedSubNode;
                }
                return copyAndMigrateFromNodeToData(owner, bitpos, updatedSubNode);
            }
            return copyAndSetNode(owner, bitpos, updatedSubNode);
        }

        @Override
        BitmapIndexedNode<D> put(IdentityObject owner,
                                 D newData,
                                 int dataHash, int shift,
                                 ChangeEvent<D> details,
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
                Node<D> updatedSubNode =
                        mergeTwoDataEntriesIntoNode(owner,
                                oldData, hashFunction.applyAsInt(oldData),
                                newData, dataHash, shift + BIT_PARTITION_SIZE);
                details.setAdded(newData);
                return copyAndMigrateFromDataToNode(owner, bitpos, updatedSubNode);
            } else if ((nodeMap & bitpos) != 0) {
                Node<D> subNode = nodeAt(bitpos);
                Node<D> updatedSubNode = subNode
                        .put(owner, newData, dataHash, shift + BIT_PARTITION_SIZE, details, updateFunction, equalsFunction, hashFunction);
                return subNode == updatedSubNode ? this : copyAndSetNode(owner, bitpos, updatedSubNode);
            }
            details.setAdded(newData);
            return copyAndInsertData(owner, bitpos, newData);
        }


        private BitmapIndexedNode<D> copyAndSetData(IdentityObject owner, int dataIndex, D updatedData) {
            if (isAllowedToUpdate(owner)) {
                this.mixed[dataIndex] = updatedData;
                return this;
            }
            Object[] newMixed = ChampListHelper.copySet(this.mixed, dataIndex, updatedData);
            return newBitmapIndexedNode(owner, nodeMap, dataMap, newMixed);
        }


        @SuppressWarnings("unchecked")
        @Override
        BitmapIndexedNode<D> putAll(IdentityObject owner, Node<D> other, int shift,
                                    BulkChangeEvent bulkChange,
                                    BiFunction<D, D, D> updateFunction,
                                    BiPredicate<D, D> equalsFunction,
                                    ToIntFunction<D> hashFunction,
                                    ChangeEvent<D> details) {
            BitmapIndexedNode<D> that = (BitmapIndexedNode<D>) other;
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
                    Node<D> thisNode = this.getNode(this.nodeIndex(bitpos));
                    Node<D> thatNode = that.getNode(that.nodeIndex(bitpos));
                    buffer[buffer.length - 1 - index(newNodeMap, bitpos)] = thisNode.putAll(owner, thatNode, shift + BIT_PARTITION_SIZE, bulkChange,
                            updateFunction, equalsFunction, hashFunction, details);
                } else if (thisIsData && thatIsNode) {
                    // add a new node that joins this data and that node
                    D thisData = this.getData(this.dataIndex(bitpos));
                    Node<D> thatNode = that.getNode(that.nodeIndex(bitpos));
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
                    Node<D> thisNode = this.getNode(this.nodeIndex(bitpos));
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
            return new BitmapIndexedNode<>(newNodeMap, newDataMap, buffer);
        }

        @Override
        BitmapIndexedNode<D> removeAll(IdentityObject owner, Node<D> other, int shift, BulkChangeEvent bulkChange, BiFunction<D, D, D> updateFunction, BiPredicate<D, D> equalsFunction, ToIntFunction<D> hashFunction, ChangeEvent<D> details) {
            BitmapIndexedNode<D> that = (BitmapIndexedNode<D>) other;
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
                    Node<D> thisNode = this.getNode(this.nodeIndex(bitpos));
                    Node<D> thatNode = that.getNode(that.nodeIndex(bitpos));
                    Node<D> result = thisNode.removeAll(owner, thatNode, shift + BIT_PARTITION_SIZE, bulkChange, updateFunction, equalsFunction, hashFunction, details);
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
                    Node<D> thatNode = that.getNode(that.nodeIndex(bitpos));
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
                    Node<D> thisNode = this.getNode(this.nodeIndex(bitpos));
                    details.reset();
                    Node<D> result = thisNode.remove(owner, thatData, hashFunction.applyAsInt(thatData), shift + BIT_PARTITION_SIZE, details, equalsFunction);
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


        private BitmapIndexedNode<D> newCroppedBitmapIndexedNode(Object[] buffer, int newDataMap, int newNodeMap) {
            int newLength = Integer.bitCount(newNodeMap | newDataMap);
            if (newLength != buffer.length) {
                Object[] temp = buffer;
                buffer = new Object[newLength];
                int dataCount = Integer.bitCount(newDataMap);
                int nodeCount = Integer.bitCount(newNodeMap);
                System.arraycopy(temp, 0, buffer, 0, dataCount);
                System.arraycopy(temp, temp.length - nodeCount, buffer, dataCount, nodeCount);
            }
            return new BitmapIndexedNode<>(newNodeMap, newDataMap, buffer);
        }

        @Override
        BitmapIndexedNode<D> retainAll(IdentityObject owner, Node<D> other, int shift, BulkChangeEvent bulkChange, BiFunction<D, D, D> updateFunction, BiPredicate<D, D> equalsFunction, ToIntFunction<D> hashFunction, ChangeEvent<D> details) {
            BitmapIndexedNode<D> that = (BitmapIndexedNode<D>) other;
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
                    Node<D> thisNode = this.getNode(this.nodeIndex(bitpos));
                    Node<D> thatNode = that.getNode(that.nodeIndex(bitpos));
                    Node<D> result = thisNode.retainAll(owner, thatNode, shift + BIT_PARTITION_SIZE, bulkChange, updateFunction, equalsFunction, hashFunction, details);
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
                    Node<D> thatNode = that.getNode(that.nodeIndex(bitpos));
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
                    Node<D> thisNode = this.getNode(this.nodeIndex(bitpos));
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
        BitmapIndexedNode<D> filterAll(IdentityObject owner, Predicate<? super D> predicate, int shift, BulkChangeEvent bulkChange) {
            int newBitMap = nodeMap | dataMap;
            Object[] buffer = new Object[Integer.bitCount(newBitMap)];
            int newDataMap = this.dataMap;
            int newNodeMap = this.nodeMap;
            for (int mapToDo = newBitMap; mapToDo != 0; mapToDo ^= Integer.lowestOneBit(mapToDo)) {
                int mask = Integer.numberOfTrailingZeros(mapToDo);
                int bitpos = bitpos(mask);
                boolean thisIsNode = (this.nodeMap & bitpos) != 0;
                if (thisIsNode) {
                    Node<D> thisNode = this.getNode(this.nodeIndex(bitpos));
                    Node<D> result = thisNode.filterAll(owner, predicate, shift + BIT_PARTITION_SIZE, bulkChange);
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
                Node<D> node = getNode(i);
                size += node.calculateSize();
            }
            return size;
        }
    }

    /**
     * Represents a hash-collision node in a CHAMP trie.
     * <p>
     * XXX hash-collision nodes may become huge performance bottlenecks.
     * If the trie contains keys that implement {@link Comparable} then a hash-collision
     * nodes should be a sorted tree structure (for example a red-black tree).
     * Otherwise, hash-collision node should be a vector (for example a bit mapped trie).
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
    static class HashCollisionNode<D> extends Node<D> {
        private static final HashCollisionNode<?> EMPTY = new HashCollisionNode<>(0, new Object[0]);
        private final int hash;
        Object[] data;

        HashCollisionNode(int hash, Object[] data) {
            this.data = data;
            this.hash = hash;
        }

        @Override
        int dataArity() {
            return data.length;
        }

        @Override
        boolean hasDataArityOne() {
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        boolean equivalent(Object other) {
            if (this == other) {
                return true;
            }
            HashCollisionNode<?> that = (HashCollisionNode<?>) other;
            Object[] thatEntries = that.data;
            if (hash != that.hash || thatEntries.length != data.length) {
                return false;
            }

            // Linear scan for each key, because of arbitrary element order.
            Object[] thatEntriesCloned = thatEntries.clone();
            int remainingLength = thatEntriesCloned.length;
            outerLoop:
            for (Object key : data) {
                for (int j = 0; j < remainingLength; j += 1) {
                    Object todoKey = thatEntriesCloned[j];
                    if (Objects.equals(todoKey, key)) {
                        // We have found an equal entry. We do not need to compare
                        // this entry again. So we replace it with the last entry
                        // from the array and reduce the remaining length.
                        System.arraycopy(thatEntriesCloned, remainingLength - 1, thatEntriesCloned, j, 1);
                        remainingLength -= 1;

                        continue outerLoop;
                    }
                }
                return false;
            }

            return true;
        }

        @SuppressWarnings("unchecked")
        @Override
        Object find(D key, int dataHash, int shift, BiPredicate<D, D> equalsFunction) {
            for (Object entry : data) {
                if (equalsFunction.test(key, (D) entry)) {
                    return entry;
                }
            }
            return NO_DATA;
        }

        @Override
        @SuppressWarnings("unchecked")
        D getData(int index) {
            return (D) data[index];
        }

        @Override
        Node<D> getNode(int index) {
            throw new IllegalStateException("Is leaf node.");
        }


        @Override
        boolean hasData() {
            return data.length > 0;
        }

        @Override
        boolean hasNodes() {
            return false;
        }

        @Override
        int nodeArity() {
            return 0;
        }


        @SuppressWarnings("unchecked")
        @Override
        Node<D> remove(IdentityObject owner, D data,
                       int dataHash, int shift, ChangeEvent<D> details, BiPredicate<D, D> equalsFunction) {
            for (int idx = 0, i = 0; i < this.data.length; i += 1, idx++) {
                if (equalsFunction.test((D) this.data[i], data)) {
                    @SuppressWarnings("unchecked") D currentVal = (D) this.data[i];
                    details.setRemoved(currentVal);

                    if (this.data.length == 1) {
                        return BitmapIndexedNode.emptyNode();
                    } else if (this.data.length == 2) {
                        // Create root node with singleton element.
                        // This node will either be the new root
                        // returned, or be unwrapped and inlined.
                        return newBitmapIndexedNode(owner, 0, bitpos(mask(dataHash, 0)),
                                new Object[]{getData(idx ^ 1)});
                    }
                    // copy keys and remove 1 element at position idx
                    Object[] entriesNew = ChampListHelper.copyComponentRemove(this.data, idx, 1);
                    if (isAllowedToUpdate(owner)) {
                        this.data = entriesNew;
                        return this;
                    }
                    return newHashCollisionNode(owner, dataHash, entriesNew);
                }
            }
            return this;
        }

        @SuppressWarnings("unchecked")
        @Override
        Node<D> put(IdentityObject owner, D newData,
                    int dataHash, int shift, ChangeEvent<D> details,
                    BiFunction<D, D, D> updateFunction, BiPredicate<D, D> equalsFunction,
                    ToIntFunction<D> hashFunction) {
            assert this.hash == dataHash;

            for (int i = 0; i < this.data.length; i++) {
                D oldData = (D) this.data[i];
                if (equalsFunction.test(oldData, newData)) {
                    D updatedData = updateFunction.apply(oldData, newData);
                    if (updatedData == oldData) {
                        details.found(oldData);
                        return this;
                    }
                    details.setReplaced(oldData, updatedData);
                    if (isAllowedToUpdate(owner)) {
                        this.data[i] = updatedData;
                        return this;
                    }
                    final Object[] newKeys = ChampListHelper.copySet(this.data, i, updatedData);
                    return newHashCollisionNode(owner, dataHash, newKeys);
                }
            }

            // copy entries and add 1 more at the end
            Object[] entriesNew = ChampListHelper.copyComponentAdd(this.data, this.data.length, 1);
            entriesNew[this.data.length] = newData;
            details.setAdded(newData);
            if (isAllowedToUpdate(owner)) {
                this.data = entriesNew;
                return this;
            }
            return newHashCollisionNode(owner, dataHash, entriesNew);
        }

        @Override
         int calculateSize() {
            return dataArity();
        }

        @SuppressWarnings("unchecked")
        @Override
        Node<D> putAll(IdentityObject owner, Node<D> otherNode, int shift, BulkChangeEvent bulkChange, BiFunction<D, D, D> updateFunction, BiPredicate<D, D> equalsFunction, ToIntFunction<D> hashFunction, ChangeEvent<D> details) {
            if (otherNode == this) {
                bulkChange.inBoth += dataArity();
                return this;
            }
            HashCollisionNode<D> that = (HashCollisionNode<D>) otherNode;

            // XXX HashSetTest requires that we use a specific iteration sequence. We could use a more performant
            //     algorithm, if we would not have to care about the iteration sequence.

            // The buffer initially contains all data elements from this node.
            // Every time we find a data element in that node, that is not in this node, we add it to the end
            // of the buffer.
            // Buffer content:
            // 0..thisSize-1 = data elements from this node
            // thisSize..resultSize-1 = data elements from that node that are not also contained in this node
            final int thisSize = this.dataArity();
            final int thatSize = that.dataArity();
            Object[] buffer = Arrays.copyOf(this.data, thisSize + thatSize);
            System.arraycopy(this.data, 0, buffer, 0, this.data.length);
            Object[] thatArray = that.data;
            int resultSize = thisSize;
            boolean updated = false;
            outer:
            for (int i = 0; i < thatSize; i++) {
                D thatData = (D) thatArray[i];
                for (int j = 0; j < thisSize; j++) {
                    D thisData = (D) buffer[j];
                    if (equalsFunction.test(thatData, thisData)) {
                        D updatedData = updateFunction.apply(thisData, thatData);
                        buffer[j] = updatedData;
                        updated |= updatedData != thisData;
                        bulkChange.inBoth++;
                        continue outer;
                    }
                }
                buffer[resultSize++] = thatData;
            }
            return newCroppedHashCollisionNode(updated | resultSize != thisSize, buffer, resultSize);
        }

        @SuppressWarnings("unchecked")
        @Override
        Node<D> removeAll(IdentityObject owner, Node<D> otherNode, int shift, BulkChangeEvent bulkChange, BiFunction<D, D, D> updateFunction, BiPredicate<D, D> equalsFunction, ToIntFunction<D> hashFunction, ChangeEvent<D> details) {
            if (otherNode == this) {
                bulkChange.removed += dataArity();
                return (Node<D>) EMPTY;
            }
            HashCollisionNode<D> that = (HashCollisionNode<D>) otherNode;

            // XXX HashSetTest requires that we use a specific iteration sequence. We could use a more performant
            //     algorithm, if we would not have to care about the iteration sequence.

            // The buffer initially contains all data elements from this node.
            // Every time we find a data element that must be removed, we remove it from the buffer.
            // Buffer content:
            // 0..resultSize-1 = data elements from this node that have not been removed
            final int thisSize = this.dataArity();
            final int thatSize = that.dataArity();
            int resultSize = thisSize;
            Object[] buffer = this.data.clone();
            Object[] thatArray = that.data;
            outer:
            for (int i = 0; i < thatSize && resultSize > 0; i++) {
                D thatData = (D) thatArray[i];
                for (int j = 0; j < resultSize; j++) {
                    D thisData = (D) buffer[j];
                    if (equalsFunction.test(thatData, thisData)) {
                        System.arraycopy(buffer, j + 1, buffer, j, resultSize - j - 1);
                        resultSize--;
                        bulkChange.removed++;
                        continue outer;
                    }
                }
            }
            return newCroppedHashCollisionNode(thisSize != resultSize, buffer, resultSize);
        }


        private HashCollisionNode<D> newCroppedHashCollisionNode(boolean changed, Object[] buffer, int size) {
            if (changed) {
                if (buffer.length != size) {
                    buffer = Arrays.copyOf(buffer, size);
                }
                return new HashCollisionNode<>(hash, buffer);
            }
            return this;
        }

        @SuppressWarnings("unchecked")
        @Override
        Node<D> retainAll(IdentityObject owner, Node<D> otherNode, int shift, BulkChangeEvent bulkChange, BiFunction<D, D, D> updateFunction, BiPredicate<D, D> equalsFunction, ToIntFunction<D> hashFunction, ChangeEvent<D> details) {
            if (otherNode == this) {
                bulkChange.removed += dataArity();
                return (Node<D>) EMPTY;
            }
            HashCollisionNode<D> that = (HashCollisionNode<D>) otherNode;

            // XXX HashSetTest requires that we use a specific iteration sequence. We could use a more performant
            //     algorithm, if we would not have to care about the iteration sequence.

            // The buffer initially contains all data elements from this node.
            // Every time we find a data element that must be retained, we add it to the buffer.
            final int thisSize = this.dataArity();
            final int thatSize = that.dataArity();
            int resultSize = 0;
            Object[] buffer = this.data.clone();
            Object[] thatArray = that.data;
            Object[] thisArray = this.data;
            outer:
            for (int i = 0; i < thatSize; i++) {
                D thatData = (D) thatArray[i];
                for (int j = resultSize; j < thisSize; j++) {
                    D thisData = (D) thisArray[j];
                    if (equalsFunction.test(thatData, thisData)) {
                        buffer[resultSize++] = thisData;
                        continue outer;
                    }
                }
                bulkChange.removed++;
            }
            return newCroppedHashCollisionNode(thisSize != resultSize, buffer, resultSize);
        }

        @SuppressWarnings("unchecked")
        @Override
        Node<D> filterAll(IdentityObject owner, Predicate<? super D> predicate, int shift, BulkChangeEvent bulkChange) {
            final int thisSize = this.dataArity();
            int resultSize = 0;
            Object[] buffer = new Object[thisSize];
            Object[] thisArray = this.data;
            outer:
            for (int i = 0; i < thisSize; i++) {
                D thisData = (D) thisArray[i];
                if (predicate.test(thisData)) {
                    buffer[resultSize++] = thisData;
                } else {
                    bulkChange.removed++;
                }
            }
            return newCroppedHashCollisionNode(thisSize != resultSize, buffer, resultSize);
        }
    }

    /**
     * A {@link BitmapIndexedNode} that provides storage space for a 'owner' identity.
     * <p>
     * References:
     * <p>
     * This class has been derived from 'The Capsule Hash Trie Collections Library'.
     * <dl>
     *      <dt>The Capsule Hash Trie Collections Library.
     *      <br>Copyright (c) Michael Steindorfer. <a href="https://github.com/usethesource/capsule/blob/3856cd65fa4735c94bcfa94ec9ecf408429b54f4/LICENSE">BSD-2-Clause License</a></dt>
     *      <dd><a href="https://github.com/usethesource/capsule">github.com</a>
     * </dl>
     * @param <K> the key type
     */
    static class MutableBitmapIndexedNode<K> extends BitmapIndexedNode<K> {
        private static final long serialVersionUID = 0L;
        private final IdentityObject owner;

        MutableBitmapIndexedNode(IdentityObject owner, int nodeMap, int dataMap, Object  [] nodes) {
            super(nodeMap, dataMap, nodes);
            this.owner = owner;
        }

        @Override
        IdentityObject getOwner() {
            return owner;
        }
    }

    /**
     * A {@link HashCollisionNode} that provides storage space for a 'owner' identity..
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
     * @param <K> the key type
     */
    static class MutableHashCollisionNode<K> extends HashCollisionNode<K> {
        private static final long serialVersionUID = 0L;
        private final IdentityObject owner;

        MutableHashCollisionNode(IdentityObject owner, int hash, Object  [] entries) {
            super(hash, entries);
            this.owner = owner;
        }

        @Override
        IdentityObject getOwner() {
            return owner;
        }
    }

    /**
     * Provides factory methods for {@link Node}s.
     * <p>
     * References:
     * <p>
     * The code in this class has been derived from JHotDraw 8.
     * <dl>
     *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
     *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
     *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a></dd>
     * </dl>
     */
    static class NodeFactory {

        /**
         * Don't let anyone instantiate this class.
         */
        private NodeFactory() {
        }

        static <K> BitmapIndexedNode<K> newBitmapIndexedNode(
                IdentityObject owner, int nodeMap,
                int dataMap, Object[] nodes) {
            return owner == null
                    ? new BitmapIndexedNode<>(nodeMap, dataMap, nodes)
                    : new MutableBitmapIndexedNode<>(owner, nodeMap, dataMap, nodes);
        }

        static <K> HashCollisionNode<K> newHashCollisionNode(
                IdentityObject owner, int hash, Object  [] entries) {
            return owner == null
                    ? new HashCollisionNode<>(hash, entries)
                    : new MutableHashCollisionNode<>(owner, hash, entries);
        }
    }

    /**
     * This class is used to report a change (or no changes) of data in a CHAMP trie.
     * <p>
     * References:
     * <p>
     * The code in this class has been derived from JHotDraw 8.
     * <dl>
     *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
     *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
     *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a></dd>
     * </dl>
     *
     * @param <D> the data type
     */
    static class ChangeEvent<D> {
        private Type type = Type.UNCHANGED;
        private  D oldData;
        private  D newData;

         ChangeEvent() {
        }

         boolean isUnchanged() {
            return type == Type.UNCHANGED;
        }

         boolean isAdded() {
            return type== Type.ADDED;
        }

        /**
         * Call this method to indicate that a data element has been added.
         */
        void setAdded( D newData) {
            this.newData = newData;
            this.type = Type.ADDED;
        }

        void found(D data) {
            this.oldData = data;
        }

          D getOldData() {
            return oldData;
        }

          D getNewData() {
            return newData;
        }

          D getOldDataNonNull() {
            return Objects.requireNonNull(oldData);
        }

          D getNewDataNonNull() {
            return Objects.requireNonNull(newData);
        }

        /**
         * Call this method to indicate that the value of an element has changed.
         *
         * @param oldData the old value of the element
         * @param newData the new value of the element
         */
        void setReplaced( D oldData,  D newData) {
            this.oldData = oldData;
            this.newData = newData;
            this.type = Type.REPLACED;
        }

        /**
         * Call this method to indicate that an element has been removed.
         *
         * @param oldData the value of the removed element
         */
        void setRemoved( D oldData) {
            this.oldData = oldData;
            this.type = Type.REMOVED;
        }

        /**
         * Returns true if the CHAMP trie has been modified.
         */
         boolean isModified() {
            return type != Type.UNCHANGED;
        }

        /**
         * Returns true if the data element has been replaced.
         */
         boolean isReplaced() {
            return type == Type.REPLACED;
        }

        void reset() {
            type = Type.UNCHANGED;
            oldData = null;
            newData = null;
        }

        enum Type {
            UNCHANGED,
            ADDED,
            REMOVED,
            REPLACED
        }
    }

    static class BulkChangeEvent {
        int inBoth;
        boolean replaced;
        int removed;
   }

    /**
     * An object with a unique identity within this VM.
     * <p>
     * References:
     * <p>
     * The code in this class has been derived from JHotDraw 8.
     * <dl>
     *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
     *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
     *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a></dd>
     * </dl>
     */
    static class IdentityObject implements Serializable {

        private static final long serialVersionUID = 0L;

         IdentityObject() {
        }
    }

    /**
     * Provides helper methods for lists that are based on arrays.
     * <p>
     * References:
     * <p>
     * The code in this class has been derived from JHotDraw 8.
     * <dl>
     *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
     *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
     *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a></dd>
     * </dl>
     *
     * @author Werner Randelshofer
     */
    static class ChampListHelper {
        /**
         * Don't let anyone instantiate this class.
         */
        private ChampListHelper() {

        }


        /**
         * Copies 'src' and inserts 'numComponents' at position 'index'.
         * <p>
         * The new components will have a null value.
         *
         * @param src           an array
         * @param index         an index
         * @param numComponents the number of array components to be added
         * @param <T>           the array type
         * @return a new array
         */
        static <T> T[] copyComponentAdd(T[] src, int index, int numComponents) {
            if (index == src.length) {
                return Arrays.copyOf(src, src.length + numComponents);
            }
            @SuppressWarnings("unchecked") final T[] dst = (T[]) java.lang.reflect.Array.newInstance(src.getClass().getComponentType(), src.length + numComponents);
            System.arraycopy(src, 0, dst, 0, index);
            System.arraycopy(src, index, dst, index + numComponents, src.length - index);
            return dst;
        }

        /**
         * Copies 'src' and removes 'numComponents' at position 'index'.
         *
         * @param src           an array
         * @param index         an index
         * @param numComponents the number of array components to be removed
         * @param <T>           the array type
         * @return a new array
         */
        static <T> T[] copyComponentRemove(T[] src, int index, int numComponents) {
            if (index == src.length - numComponents) {
                return Arrays.copyOf(src, src.length - numComponents);
            }
            @SuppressWarnings("unchecked") final T[] dst = (T[]) Array.newInstance(src.getClass().getComponentType(), src.length - numComponents);
            System.arraycopy(src, 0, dst, 0, index);
            System.arraycopy(src, index + numComponents, dst, index, src.length - index - numComponents);
            return dst;
        }

        /**
         * Copies 'src' and sets 'value' at position 'index'.
         *
         * @param src   an array
         * @param index an index
         * @param value a value
         * @param <T>   the array type
         * @return a new array
         */
        static <T> T[] copySet(T[] src, int index, T value) {
            final T[] dst = Arrays.copyOf(src, src.length);
            dst[index] = value;
            return dst;
        }

        /**
         * Checks if the specified array ranges are equal.
         *
         * @param a     array a
         * @param aFrom from index in array a
         * @param aTo   to index in array a
         * @param b     array b
         * @param bFrom from index in array b
         * @param bTo   to index in array b
         * @return true if equal
         */
        static boolean arrayEquals(Object[] a, int aFrom, int aTo,
                                   Object[] b, int bFrom, int bTo) {
            if (aTo - aFrom != bTo - bFrom) return false;
            int bOffset = bFrom - aFrom;
            for (int i = aFrom; i < aTo; i++) {
                if (!Objects.equals(a[i], b[i + bOffset])) {
                    return false;
                }
            }
            return true;
        }
        /**
         * Checks if the specified array ranges are equal.
         *
         * @param a     array a
         * @param aFrom from index in array a
         * @param aTo   to index in array a
         * @param b     array b
         * @param bFrom from index in array b
         * @param bTo   to index in array b
         * @return true if equal
         */
        static boolean arrayEquals(Object[] a, int aFrom, int aTo,
                                   Object[] b, int bFrom, int bTo,
                                   BiPredicate<Object,Object> c) {
            if (aTo - aFrom != bTo - bFrom) return false;
            int bOffset = bFrom - aFrom;
            for (int i = aFrom; i < aTo; i++) {
                if (!c.test(a[i], b[i + bOffset])) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Checks if the provided index is {@literal >= 0} and {@literal <=} size;
         *
         * @param index the index
         * @param size  the size
         * @throws IndexOutOfBoundsException if index is out of bounds
         */
        static void checkIndex(int index, int size) {
            if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index=" + index + " size=" + size);
        }
    }
}
