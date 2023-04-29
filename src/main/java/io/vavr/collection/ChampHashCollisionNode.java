/*
 * @(#)HashCollisionNode.java
 * Copyright © 2023 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

import static io.vavr.collection.ChampNodeFactory.newHashCollisionNode;

/**
 * Represents a hash-collision node in a CHAMP trie.
 * <p>
 * XXX hash-collision nodes may become huge performance bottlenecks.
 * If the trie contains keys that implement {@link Comparable} then a hash-collision
 * nodes should be a sorted tree structure (for example a red-black tree).
 * Otherwise, hash-collision node should be a vector (for example a bit mapped trie).
 *
 * @param <D> the data type
 */
class ChampHashCollisionNode<D> extends ChampNode<D> {
    private final int hash;
     Object[] data;

    ChampHashCollisionNode(int hash, Object  [] data) {
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
    boolean equivalent( Object other) {
        if (this == other) {
            return true;
        }
        ChampHashCollisionNode<?> that = (ChampHashCollisionNode<?>) other;
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
    
    Object find(D key, int dataHash, int shift,  BiPredicate<D, D> equalsFunction) {
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
    ChampNode<D> getNode(int index) {
        throw new IllegalStateException("Is leaf node.");
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
    int nodeArity() {
        return 0;
    }


    @SuppressWarnings("unchecked")
    @Override
    ChampNode<D> remove(ChampIdentityObject mutator, D data,
                        int dataHash, int shift, ChampChangeEvent<D> details, BiPredicate<D, D> equalsFunction) {
        for (int idx = 0, i = 0; i < this.data.length; i += 1, idx++) {
            if (equalsFunction.test((D) this.data[i], data)) {
                @SuppressWarnings("unchecked") D currentVal = (D) this.data[i];
                details.setRemoved(currentVal);

                if (this.data.length == 1) {
                    return ChampBitmapIndexedNode.emptyNode();
                } else if (this.data.length == 2) {
                    // Create root node with singleton element.
                    // This node will be a) either be the new root
                    // returned, or b) unwrapped and inlined.
                    return ChampNodeFactory.newBitmapIndexedNode(mutator, 0, bitpos(mask(dataHash, 0)),
                            new Object[]{getData(idx ^ 1)});
                }
                // copy keys and remove 1 element at position idx
                Object[] entriesNew = ChampListHelper.copyComponentRemove(this.data, idx, 1);
                if (isAllowedToUpdate(mutator)) {
                    this.data = entriesNew;
                    return this;
                }
                return newHashCollisionNode(mutator, dataHash, entriesNew);
            }
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    ChampNode<D> update(ChampIdentityObject mutator, D newData,
                        int dataHash, int shift, ChampChangeEvent<D> details,
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
                if (isAllowedToUpdate(mutator)) {
                    this.data[i] = updatedData;
                    return this;
                }
                final Object[] newKeys = ChampListHelper.copySet(this.data, i, updatedData);
                return newHashCollisionNode(mutator, dataHash, newKeys);
            }
        }

        // copy entries and add 1 more at the end
        Object[] entriesNew = ChampListHelper.copyComponentAdd(this.data, this.data.length, 1);
        entriesNew[this.data.length] = newData;
        details.setAdded(newData);
        if (isAllowedToUpdate(mutator)) {
            this.data = entriesNew;
            return this;
        }
        return newHashCollisionNode(mutator, dataHash, entriesNew);
    }
}
