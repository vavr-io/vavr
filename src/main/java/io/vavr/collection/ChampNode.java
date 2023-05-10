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

import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

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
 * stored in a {@link ChampHashCollisionNode}, otherwise it is stored in a
 * {@link ChampBitmapIndexedNode}. Since the hash codes have a fixed length,
 * all {@link ChampHashCollisionNode}s are located at the same, maximal depth
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
 abstract class ChampNode<D> {
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


    ChampNode() {
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

     static <E>  E getFirst( ChampNode<E> node) {
        while (node instanceof ChampBitmapIndexedNode) {
            ChampBitmapIndexedNode<E> bxn = (ChampBitmapIndexedNode<E>) node;
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
        if (node instanceof ChampHashCollisionNode) {
            ChampHashCollisionNode<E> hcn = (ChampHashCollisionNode<E>) node;
            return hcn.getData(0);
        }
        throw new NoSuchElementException();
    }

     static <E>  E getLast( ChampNode<E> node) {
        while (node instanceof ChampBitmapIndexedNode) {
            ChampBitmapIndexedNode<E> bxn = (ChampBitmapIndexedNode<E>) node;
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
        if (node instanceof ChampHashCollisionNode) {
            ChampHashCollisionNode<E> hcn = (ChampHashCollisionNode<E>) node;
            return hcn.getData(hcn.dataArity() - 1);
        }
        throw new NoSuchElementException();
    }

    static int mask(int dataHash, int shift) {
        return (dataHash >>> shift) & BIT_PARTITION_MASK;
    }

    static <K> ChampNode<K> mergeTwoDataEntriesIntoNode(ChampIdentityObject owner,
                                                        K k0, int keyHash0,
                                                        K k1, int keyHash1,
                                                        int shift) {
        if (shift >= HASH_CODE_LENGTH) {
            Object[] entries = new Object[2];
            entries[0] = k0;
            entries[1] = k1;
            return ChampNodeFactory.newHashCollisionNode(owner, keyHash0, entries);
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
            return ChampNodeFactory.newBitmapIndexedNode(owner, (0), dataMap, entries);
        } else {
            ChampNode<K> node = mergeTwoDataEntriesIntoNode(owner,
                    k0, keyHash0,
                    k1, keyHash1,
                    shift + BIT_PARTITION_SIZE);
            // values fit on next level

            int nodeMap = bitpos(mask0);
            return ChampNodeFactory.newBitmapIndexedNode(owner, nodeMap, (0), new Object[]{node});
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

     ChampIdentityObject getOwner() {
        return null;
    }

    abstract ChampNode<D> getNode(int index);

    abstract boolean hasData();

    boolean isNodeEmpty() {
        return !hasData() && !hasNodes();
    }

    boolean hasMany() {
        return hasNodes() || dataArity() > 1;
    }

    abstract boolean hasDataArityOne();

    abstract boolean hasNodes();

    boolean isAllowedToUpdate( ChampIdentityObject y) {
        ChampIdentityObject x = getOwner();
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
    abstract ChampNode<D> remove(ChampIdentityObject owner, D data,
                                 int dataHash, int shift,
                                 ChampChangeEvent<D> details,
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
    abstract ChampNode<D> put(ChampIdentityObject owner, D newData,
                              int dataHash, int shift, ChampChangeEvent<D> details,
                              BiFunction<D, D, D> updateFunction,
                              BiPredicate<D, D> equalsFunction,
                              ToIntFunction<D> hashFunction);
   /**
     * Inserts or replaces data elements from the specified other trie in this trie.
     *
     * @param owner
     * @param otherNode      a node with the same shift as this node from the other trie
     * @param shift          the shift of this node and the other node
     * @param bulkChange     updates the field {@link ChampBulkChangeEvent#inBoth}
     * @param updateFunction the update function for data elements
     * @param equalsFunction the equals function for data elements
     * @param hashFunction   the hash function for data elements
     * @param details        the change event for single elements
     * @return the updated trie
     */
     abstract  ChampNode<D> putAll( ChampIdentityObject owner,  ChampNode<D> otherNode, int shift,
                                                ChampBulkChangeEvent bulkChange,
                                                BiFunction<D, D, D> updateFunction,
                                                BiPredicate<D, D> equalsFunction,
                                                ToIntFunction<D> hashFunction,
                                                ChampChangeEvent<D> details);

    /**
     * Removes data elements in the specified other trie from this trie.
     *
     * @param owner
     * @param otherNode      a node with the same shift as this node from the other trie
     * @param shift          the shift of this node and the other node
     * @param bulkChange     updates the field {@link ChampBulkChangeEvent#removed}
     * @param updateFunction the update function for data elements
     * @param equalsFunction the equals function for data elements
     * @param hashFunction   the hash function for data elements
     * @param details        the change event for single elements
     * @return the updated trie
     */
     abstract  ChampNode<D> removeAll( ChampIdentityObject owner,  ChampNode<D> otherNode, int shift,
                                                   ChampBulkChangeEvent bulkChange,
                                                   BiFunction<D, D, D> updateFunction,
                                                   BiPredicate<D, D> equalsFunction,
                                                   ToIntFunction<D> hashFunction,
                                                   ChampChangeEvent<D> details);

    /**
     * Retains data elements in this trie that are also in the other trie - removes the rest.
     *
     * @param owner
     * @param otherNode      a node with the same shift as this node from the other trie
     * @param shift          the shift of this node and the other node
     * @param bulkChange     updates the field {@link ChampBulkChangeEvent#removed}
     * @param updateFunction the update function for data elements
     * @param equalsFunction the equals function for data elements
     * @param hashFunction   the hash function for data elements
     * @param details        the change event for single elements
     * @return the updated trie
     */
     abstract  ChampNode<D> retainAll( ChampIdentityObject owner,  ChampNode<D> otherNode, int shift,
                                                   ChampBulkChangeEvent bulkChange,
                                                   BiFunction<D, D, D> updateFunction,
                                                   BiPredicate<D, D> equalsFunction,
                                                   ToIntFunction<D> hashFunction,
                                                   ChampChangeEvent<D> details);

    /**
     * Retains data elements in this trie for which the provided predicate returns true.
     *
     * @param owner
     * @param predicate  a predicate that returns true for data elements that should be retained
     * @param shift      the shift of this node and the other node
     * @param bulkChange updates the field {@link ChampBulkChangeEvent#removed}
     * @return the updated trie
     */
     abstract  ChampNode<D> filterAll(ChampIdentityObject owner, Predicate<? super D> predicate, int shift,
                                               ChampBulkChangeEvent bulkChange);

     abstract int calculateSize();}
