/*
 * @(#)BitmapIndexedNode.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;


import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

import static io.vavr.collection.champ.ChampTrie.newBitmapIndexedNode;
import static io.vavr.collection.champ.ChampTrie.newHashCollisionNode;


/**
 * Represents a bitmap-indexed node in a CHAMP trie.
 *
 * @param <K> the key type
 */
public class BitmapIndexedNode<K> extends Node<K> {
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

    /**
     * Creates a copy of this trie with all elements of the specified
     * trie added to it.
     * <p>
     *
     * @param o            the trie to be added to this trie
     * @param shift        the shift for both tries
     * @param bulkChange   Reports data about the bulk change.
     * @param mutator      the mutator
     * @param hashFunction a function that computes a hash code for a key
     * @return a node that contains all the added key-value pairs
     */
    public BitmapIndexedNode<K> updateAll(Node<K> o, int shift, ChangeEvent<K> bulkChange,
                                          UniqueId mutator,
                                          BiFunction<K, K, K> updateFunction,
                                          BiFunction<K, K, K> inverseUpdateFunction,
                                          BiPredicate<K, K> equalsFunction,
                                          ToIntFunction<K> hashFunction) {
        // Given the same bit-position in this and that:
        //                              this    this    that    that
        // case                         dataMap nodeMap dataMap nodeMap
        // ---------------------------------------------------------------------------
        // 0    illegal                   -       -       -       -
        // 1    put "a" in dataMap        "a"     -       -       -
        // 2    put x in nodeMap          -       x       -       -
        // 3    illegal                   "a"     x       -       -
        // 4    put "b" in dataMap        -       -       "b"     -
        // 5.1  put "a" in dataMap        "a"     -       "a"     -   values are equal
        // 5.2  put {"a","b"} in nodeMap  "a"     -       "b"     -   values are not equal
        // 6    put x ∪ {"b"} in nodeMap  -       x       "b"     -
        // 7    illegal                   "a"     x       "b"     -
        // 8    put y in nodeMap          -       -       -       y
        // 9    put {"a"} ∪ y in nodeMap  "a"     -       -       y
        // 10.1 put x in nodeMap          -       x       -       x   nodes are equivalent
        // 10.2 put x ∪ y in nodeMap      -       x       -       y   nodes are not equivalent
        // 11   illegal                   "a"     x       -       y
        // 12   illegal                   -       -       "b"     y
        // 13   illegal                   "a"     -       "b"     y
        // 14   illegal                   -       x       "b"     y
        // 15   illegal                   "a"     x       "b"     y

        if (o == this) {
            return this;
        }
        BitmapIndexedNode<K> that = (BitmapIndexedNode<K>) o;

        int newNodeLength = Integer.bitCount(this.nodeMap | this.dataMap | that.nodeMap | that.dataMap);
        Object[] newMixed = new Object[newNodeLength];
        int newNodeMap = this.nodeMap | that.nodeMap;
        int newDataMap = this.dataMap | that.dataMap;
        int thisNodeMapToDo = this.nodeMap;
        int thatNodeMapToDo = that.nodeMap;

        ChangeEvent<K> subDetails = new ChangeEvent<>();
        boolean changed = false;


        // Step 1: Merge that.dataMap and this.dataMap into newDataMap.
        //         We may have to merge data nodes into sub-nodes.
        // -------
        // iterate over all bit-positions in dataMapNew which have a non-zero bit
        int dataIndex = 0;
        for (int mapToDo = newDataMap; mapToDo != 0; mapToDo ^= Integer.lowestOneBit(mapToDo)) {
            int mask = Integer.numberOfTrailingZeros(mapToDo);
            int bitpos = bitpos(mask);
            boolean thisHasData = (this.dataMap & bitpos) != 0;
            boolean thatHasData = (that.dataMap & bitpos) != 0;
            if (thisHasData && thatHasData) {
                K thisKey = this.getKey(index(this.dataMap, bitpos));
                K thatKey = that.getKey(index(that.dataMap, bitpos));
                if (Objects.equals(thisKey, thatKey)) {
                    // case 5.1:
                    newMixed[dataIndex++] = thisKey;
                    bulkChange.numInBothCollections++;
                } else {
                    // case 5.2:
                    newDataMap ^= bitpos;
                    newNodeMap |= bitpos;
                    int thatKeyHash = hashFunction.applyAsInt(thatKey);
                    Node<K> subNodeNew = mergeTwoKeyValPairs(mutator, thisKey, hashFunction.applyAsInt(thisKey), thatKey, thatKeyHash, shift + BIT_PARTITION_SIZE);
                    newMixed[nodeIndexAt(newMixed, newNodeMap, bitpos)] = subNodeNew;
                    changed = true;
                }
            } else if (thisHasData) {
                K thisKey = this.getKey(index(this.dataMap, bitpos));
                boolean thatHasNode = (that.nodeMap & bitpos) != 0;
                if (thatHasNode) {
                    // case 9:
                    newDataMap ^= bitpos;
                    thatNodeMapToDo ^= bitpos;
                    int thisKeyHash = hashFunction.applyAsInt(thisKey);
                    subDetails.modified = false;
                    subDetails.updated = false;
                    Node<K> subNode = that.nodeAt(bitpos);
                    Node<K> subNodeNew = subNode.update(mutator, thisKey, thisKeyHash, shift + BIT_PARTITION_SIZE, subDetails, updateFunction, equalsFunction, hashFunction);
                    newMixed[nodeIndexAt(newMixed, newNodeMap, bitpos)] = subNodeNew;
                    changed = true;
                    if (!subDetails.modified || subDetails.updated) {
                        bulkChange.numInBothCollections++;
                    }
                } else {
                    // case 1:
                    newMixed[dataIndex++] = thisKey;
                }
            } else {
                assert thatHasData;
                K thatKey = that.getKey(index(that.dataMap, bitpos));
                int thatKeyHash = hashFunction.applyAsInt(thatKey);
                boolean thisHasNode = (this.nodeMap & bitpos) != 0;
                if (thisHasNode) {
                    // case 6:
                    newDataMap ^= bitpos;
                    thisNodeMapToDo ^= bitpos;
                    subDetails.modified = false;
                    subDetails.updated = false;
                    Node<K> subNode = this.getNode(index(this.nodeMap, bitpos));
                    Node<K> subNodeNew = subNode.update(mutator, thatKey, thatKeyHash, shift + BIT_PARTITION_SIZE, subDetails,
                            updateFunction, equalsFunction, hashFunction);
                    newMixed[nodeIndexAt(newMixed, newNodeMap, bitpos)] = subNodeNew;
                    if (!subDetails.modified || subDetails.updated) {
                        bulkChange.numInBothCollections++;
                    } else {
                        changed = true;
                    }
                } else {
                    // case 4:
                    changed = true;
                    newMixed[dataIndex++] = thatKey;
                }
            }
        }

        // Step 2: Merge remaining sub-nodes
        // -------
        int nodeMapToDo = thisNodeMapToDo | thatNodeMapToDo;
        for (int mapToDo = nodeMapToDo; mapToDo != 0; mapToDo ^= Integer.lowestOneBit(mapToDo)) {
            int mask = Integer.numberOfTrailingZeros(mapToDo);
            int bitpos = bitpos(mask);
            boolean thisHasNodeToDo = (thisNodeMapToDo & bitpos) != 0;
            boolean thatHasNodeToDo = (thatNodeMapToDo & bitpos) != 0;
            if (thisHasNodeToDo && thatHasNodeToDo) {
                //cases 10.1 and 10.2
                Node<K> thisSubNode = this.getNode(index(this.nodeMap, bitpos));
                Node<K> thatSubNode = that.getNode(index(that.nodeMap, bitpos));
                Node<K> newSubNode = thisSubNode.updateAll(thatSubNode, shift + BIT_PARTITION_SIZE, bulkChange, mutator,
                        updateFunction, inverseUpdateFunction, equalsFunction, hashFunction);
                changed |= newSubNode != thisSubNode;
                newMixed[nodeIndexAt(newMixed, newNodeMap, bitpos)] = newSubNode;

            } else if (thatHasNodeToDo) {
                // case 8
                Node<K> thatSubNode = that.getNode(index(that.nodeMap, bitpos));
                newMixed[nodeIndexAt(newMixed, newNodeMap, bitpos)] = thatSubNode;
                changed = true;
            } else {
                // case 2
                assert thisHasNodeToDo;
                Node<K> thisSubNode = this.getNode(index(this.nodeMap, bitpos));
                newMixed[nodeIndexAt(newMixed, newNodeMap, bitpos)] = thisSubNode;
            }
        }

        // Step 3: create new node if it has changed
        // ------
        if (changed) {
            bulkChange.setValueAdded();
            return newBitmapIndexedNode(mutator, newNodeMap, newDataMap, newMixed);
        }

        return this;
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