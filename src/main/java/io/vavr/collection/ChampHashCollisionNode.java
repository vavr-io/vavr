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
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import static io.vavr.collection.ChampNodeFactory.newHashCollisionNode;

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
class ChampHashCollisionNode<D> extends ChampNode<D> {
    private static final ChampHashCollisionNode<?> EMPTY = new ChampHashCollisionNode<>(0, new Object[0]);
    private final int hash;
    Object[] data;

    ChampHashCollisionNode(int hash, Object[] data) {
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
    ChampNode<D> getNode(int index) {
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
    ChampNode<D> remove(ChampIdentityObject owner, D data,
                        int dataHash, int shift, ChampChangeEvent<D> details, BiPredicate<D, D> equalsFunction) {
        for (int idx = 0, i = 0; i < this.data.length; i += 1, idx++) {
            if (equalsFunction.test((D) this.data[i], data)) {
                @SuppressWarnings("unchecked") D currentVal = (D) this.data[i];
                details.setRemoved(currentVal);

                if (this.data.length == 1) {
                    return ChampBitmapIndexedNode.emptyNode();
                } else if (this.data.length == 2) {
                    // Create root node with singleton element.
                    // This node will either be the new root
                    // returned, or be unwrapped and inlined.
                    return ChampNodeFactory.newBitmapIndexedNode(owner, 0, bitpos(mask(dataHash, 0)),
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
    ChampNode<D> put(ChampIdentityObject owner, D newData,
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
    protected int calculateSize() {
        return dataArity();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ChampNode<D> putAll(ChampIdentityObject owner, ChampNode<D> otherNode, int shift, ChampBulkChangeEvent bulkChange, BiFunction<D, D, D> updateFunction, BiPredicate<D, D> equalsFunction, ToIntFunction<D> hashFunction, ChampChangeEvent<D> details) {
        if (otherNode == this) {
            bulkChange.inBoth += dataArity();
            return this;
        }
        ChampHashCollisionNode<D> that = (ChampHashCollisionNode<D>) otherNode;

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
    protected ChampNode<D> removeAll(ChampIdentityObject owner, ChampNode<D> otherNode, int shift, ChampBulkChangeEvent bulkChange, BiFunction<D, D, D> updateFunction, BiPredicate<D, D> equalsFunction, ToIntFunction<D> hashFunction, ChampChangeEvent<D> details) {
        if (otherNode == this) {
            bulkChange.removed += dataArity();
            return (ChampNode<D>) EMPTY;
        }
        ChampHashCollisionNode<D> that = (ChampHashCollisionNode<D>) otherNode;

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


    private ChampHashCollisionNode<D> newCroppedHashCollisionNode(boolean changed, Object[] buffer, int size) {
        if (changed) {
            if (buffer.length != size) {
                buffer = Arrays.copyOf(buffer, size);
            }
            return new ChampHashCollisionNode<>(hash, buffer);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ChampNode<D> retainAll(ChampIdentityObject owner, ChampNode<D> otherNode, int shift, ChampBulkChangeEvent bulkChange, BiFunction<D, D, D> updateFunction, BiPredicate<D, D> equalsFunction, ToIntFunction<D> hashFunction, ChampChangeEvent<D> details) {
        if (otherNode == this) {
            bulkChange.removed += dataArity();
            return (ChampNode<D>) EMPTY;
        }
        ChampHashCollisionNode<D> that = (ChampHashCollisionNode<D>) otherNode;

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
    protected ChampNode<D> filterAll(ChampIdentityObject owner, Predicate<D> predicate, int shift, ChampBulkChangeEvent bulkChange) {
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
