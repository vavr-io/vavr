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
    @NonNull Object[] data;

    HashCollisionNode(int hash, Object @NonNull [] data) {
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
    boolean equivalent(@NonNull Object other) {
        if (this == other) {
            return true;
        }
        HashCollisionNode<?> that = (HashCollisionNode<?>) other;
        @NonNull Object[] thatEntries = that.data;
        if (hash != that.hash || thatEntries.length != data.length) {
            return false;
        }

        // Linear scan for each key, because of arbitrary element order.
        @NonNull Object[] thatEntriesCloned = thatEntries.clone();
        int remainingLength = thatEntriesCloned.length;
        outerLoop:
        for (Object key : data) {
            for (int j = 0; j < remainingLength; j += 1) {
                Object todoKey = thatEntriesCloned[j];
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
    @Nullable
    Object find(D key, int dataHash, int shift, @NonNull BiPredicate<D, D> equalsFunction) {
        for (Object entry : data) {
            if (equalsFunction.test(key, (D) entry)) {
                return entry;
            }
        }
        return NO_DATA;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    D getData(int index) {
        return (D) data[index];
    }

    @Override
    @NonNull
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
    @Nullable
    Node<D> remove(@Nullable IdentityObject mutator, D data,
                   int dataHash, int shift, @NonNull ChangeEvent<D> details, @NonNull BiPredicate<D, D> equalsFunction) {
        for (int idx = 0, i = 0; i < this.data.length; i += 1, idx++) {
            if (equalsFunction.test((D) this.data[i], data)) {
                @SuppressWarnings("unchecked") D currentVal = (D) this.data[i];
                details.setRemoved(currentVal);

                if (this.data.length == 1) {
                    return BitmapIndexedNode.emptyNode();
                } else if (this.data.length == 2) {
                    // Create root node with singleton element.
                    // This node will be a) either be the new root
                    // returned, or b) unwrapped and inlined.
                    return NodeFactory.newBitmapIndexedNode(mutator, 0, bitpos(mask(dataHash, 0)),
                            new Object[]{getData(idx ^ 1)});
                }
                // copy keys and remove 1 element at position idx
                Object[] entriesNew = ListHelper.copyComponentRemove(this.data, idx, 1);
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
    @Nullable
    Node<D> update(@Nullable IdentityObject mutator, D data,
                   int dataHash, int shift, @NonNull ChangeEvent<D> details,
                   @NonNull BiFunction<D, D, D> replaceFunction, @NonNull BiPredicate<D, D> equalsFunction,
                   @NonNull ToIntFunction<D> hashFunction) {
        assert this.hash == dataHash;

        for (int i = 0; i < this.data.length; i++) {
            D oldKey = (D) this.data[i];
            if (equalsFunction.test(oldKey, data)) {
                D updatedKey = replaceFunction.apply(oldKey, data);
                if (updatedKey == oldKey) {
                    details.found(data);
                    return this;
                }
                details.setReplaced(oldKey);
                if (isAllowedToUpdate(mutator)) {
                    this.data[i] = updatedKey;
                    return this;
                }
                final Object[] newKeys = ListHelper.copySet(this.data, i, updatedKey);
                return newHashCollisionNode(mutator, dataHash, newKeys);
            }
        }

        // copy entries and add 1 more at the end
        Object[] entriesNew = ListHelper.copyComponentAdd(this.data, this.data.length, 1);
        entriesNew[this.data.length] = data;
        details.setAdded();
        if (isAllowedToUpdate(mutator)) {
            this.data = entriesNew;
            return this;
        }
        return newHashCollisionNode(mutator, dataHash, entriesNew);
    }
}
