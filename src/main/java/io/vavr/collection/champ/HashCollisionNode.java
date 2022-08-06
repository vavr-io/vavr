/*
 * @(#)HashCollisionNode.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;


import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

import static io.vavr.collection.champ.NodeFactory.newHashCollisionNode;


/**
 * Represents a hash-collision node in a CHAMP trie.
 *
 * @param <D> the data type
 */
class HashCollisionNode<D> extends Node<D> {
    private final int hash;
    Object[] keys;

    HashCollisionNode(final int hash, final Object[] keys) {
        this.keys = keys;
        this.hash = hash;
    }

    @Override
    int dataArity() {
        return keys.length;
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
        Object[] thatEntries = that.keys;
        if (hash != that.hash || thatEntries.length != keys.length) {
            return false;
        }

        // Linear scan for each key, because of arbitrary element order.
        Object[] thatEntriesCloned = thatEntries.clone();
        int remainingLength = thatEntriesCloned.length;
        outerLoop:
        for (final Object key : keys) {
            for (int j = 0; j < remainingLength; j += 1) {
                final Object todoKey = thatEntriesCloned[j];
                if (Objects.equals((D) todoKey, (D) key)) {
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
    Object findByData(final D data, final int dataHash, final int shift, BiPredicate<D, D> equalsFunction) {
        for (Object entry : keys) {
            if (equalsFunction.test(data, (D) entry)) {
                return entry;
            }
        }
        return NO_DATA;
    }

    @Override
    @SuppressWarnings("unchecked")
    D getData(final int index) {
        return (D) keys[index];
    }

    @Override
    Node<D> getNode(int index) {
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
    Node<D> remove(final UniqueId mutator, final D data,
                   final int dataHash, final int shift, final ChangeEvent<D> details, BiPredicate<D, D> equalsFunction) {
        for (int idx = 0, i = 0; i < keys.length; i += 1, idx++) {
            if (equalsFunction.test((D) keys[i], data)) {
                @SuppressWarnings("unchecked") final D currentVal = (D) keys[i];
                details.setRemoved(currentVal);

                if (keys.length == 1) {
                    return BitmapIndexedNode.emptyNode();
                } else if (keys.length == 2) {
                    // Create root node with singleton element.
                    // This node will be a) either be the new root
                    // returned, or b) unwrapped and inlined.
                    final Object[] theOtherEntry = {getData(idx ^ 1)};
                    return NodeFactory.newBitmapIndexedNode(mutator, 0, bitpos(mask(dataHash, 0)), theOtherEntry);
                }
                // copy keys and vals and remove entryLength elements at position idx
                final Object[] entriesNew = ArrayHelper.copyComponentRemove(this.keys, idx, 1);
                if (isAllowedToEdit(mutator)) {
                    this.keys = entriesNew;
                    return this;
                }
                return newHashCollisionNode(mutator, dataHash, entriesNew);
            }
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    Node<D> update(final UniqueId mutator, final D data,
                   final int dataHash, final int shift, final ChangeEvent<D> details,
                   final BiFunction<D, D, D> updateFunction, BiPredicate<D, D> equalsFunction,
                   ToIntFunction<D> hashFunction) {
        assert this.hash == dataHash;

        for (int i = 0; i < keys.length; i++) {
            D oldKey = (D) keys[i];
            if (equalsFunction.test(oldKey, data)) {
                D updatedKey = updateFunction.apply(oldKey, data);
                if (updatedKey == oldKey) {
                    details.found(data);
                    return this;
                }
                details.setUpdated(oldKey);
                if (isAllowedToEdit(mutator)) {
                    this.keys[i] = updatedKey;
                    return this;
                }
                final Object[] newKeys = ArrayHelper.copySet(this.keys, i, updatedKey);
                return newHashCollisionNode(mutator, dataHash, newKeys);
            }
        }

        // copy entries and add 1 more at the end
        final Object[] entriesNew = ArrayHelper.copyComponentAdd(this.keys, this.keys.length, 1);
        entriesNew[this.keys.length] = data;
        details.setAdded();
        if (isAllowedToEdit(mutator)) {
            this.keys = entriesNew;
            return this;
        }
        return newHashCollisionNode(mutator, dataHash, entriesNew);
    }
}
