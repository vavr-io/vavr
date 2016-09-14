/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static javaslang.collection.Arrays.*;
import static javaslang.collection.NodeModifier.*;

/**
 * A `bit-mapped trie` is a very wide and shallow tree (for integer indices the depth will be `≤6`).
 * Each node has a maximum of `32` children (configurable).
 * Access to a given position is done by converting the index to a base 32 number and using each digit to descend down the tree.
 * Modifying the tree is done similarly, but along the way the path is copied, returning a new root every time.
 * `Append` inserts in the last leaf, or if the tree is full from the right, it adds another layer on top of it (the old root will be the first of the new one).
 * `Prepend` is done similarly, but an offset is needed, because adding a new top node (where the current root would be the last node of the new root)
 * shifts the indices by half of the current tree's full size. The `offset` shifts them back to the correct index.
 * `Slice` is done by trimming the path from the root and discarding any `leading`/`trailing` values in effectively constant time (without memory leak, as in `Java`/`Clojure`).
 *
 * @author Pap Lőrinc
 * @since 2.1.0
 */
final class BitMappedTrie<T> implements Serializable {
    static final int BRANCHING_BASE = 5;
    static final int BRANCHING_FACTOR = 1 << BRANCHING_BASE;
    static final int BRANCHING_MASK = -1 >>> -BRANCHING_BASE;

    static int firstDigit(int num, int depthShift) { return num >> depthShift; }
    static int digit(int num, int depthShift)      { return lastDigit(firstDigit(num, depthShift)); }
    static int lastDigit(int num)                  { return num & BRANCHING_MASK; }

    private static final long serialVersionUID = 1L;

    private static final BitMappedTrie<?> EMPTY = new BitMappedTrie<>(Arrays.empty(), 0, 0, 0);
    @SuppressWarnings("unchecked")
    static <T> BitMappedTrie<T> empty() { return (BitMappedTrie<T>) EMPTY; }

    private final Object[] array;
    private final int offset, length;
    private final int depthShift;

    private BitMappedTrie(Object[] array, int offset, int length, int depthShift) {
        this.array = array;
        this.offset = offset;
        this.length = length;
        this.depthShift = depthShift;

        assert length <= treeSize(BRANCHING_FACTOR, depthShift);
        assert (EMPTY == null) || ((length > 0) && (array.length > 0));
    }

    private static int treeSize(int branchCount, int depthShift) {
        final int fullBranchSize = 1 << depthShift;
        return branchCount * fullBranchSize;
    }

    static <T> BitMappedTrie<T> ofAll(Object[] array) {
        final int size = array.length;
        if (size == 0) {
            return empty();
        } else {
            int shift = 0;
            for (; array.length > BRANCHING_FACTOR; shift += BRANCHING_BASE) {
                array = grouped(array, BRANCHING_FACTOR);
            }
            return new BitMappedTrie<>(array, 0, size, shift);
        }
    }

    BitMappedTrie<T> prepend(T leading) {
        final int newSize = length() + 1;
        if (length() == 0) {
            return new BitMappedTrie<>(asArray(leading), offset, newSize, depthShift);
        } else {
            Object[] array = this.array;
            int shift = depthShift, offset = this.offset;
            if (isFullLeft()) {
                array = copyUpdate(Arrays.empty(), BRANCHING_FACTOR - 1, array);
                shift += BRANCHING_BASE;
                offset = treeSize(BRANCHING_FACTOR - 1, shift);
            }

            offset -= 1;
            array = modifyLeaf(array, shift, offset, COPY_NODE, updateLeafWith(leading));
            return new BitMappedTrie<>(array, offset, newSize, shift);
        }
    }
    private boolean isFullLeft() { return offset == 0; }

    BitMappedTrie<T> append(T trailing) {
        final int newSize = length() + 1;
        if (length() == 0) {
            return new BitMappedTrie<>(asArray(trailing), offset, newSize, depthShift);
        } else {
            Object[] array = this.array;
            int shift = depthShift;
            if (isFullRight(newSize)) {
                array = asArray(array);
                shift += BRANCHING_BASE;
            }

            array = modifyLeaf(array, shift, offset + length(), COPY_NODE, updateLeafWith(trailing));
            return new BitMappedTrie<>(array, offset, newSize, shift);
        }
    }
    private boolean isFullRight(int newSize) { return (offset + newSize) > treeSize(BRANCHING_FACTOR, depthShift); }

    BitMappedTrie<T> update(int index, T element) {
        final Object[] root = modifyLeaf(array, depthShift, offset + index, COPY_NODE, updateLeafWith(element));
        return new BitMappedTrie<>(root, offset, length(), depthShift);
    }

    BitMappedTrie<T> drop(int n) {
        if (n <= 0) {
            return this;
        } else if (n >= length()) {
            return empty();
        } else {
            final int index = offset + n;
            final Object[] root = arePointingToSameLeaf(0, n)
                                  ? array
                                  : modifyLeaf(array, depthShift, index, Arrays::copyDrop, IDENTITY);
            return collapsed(root, index, length() - n, depthShift);
        }
    }

    BitMappedTrie<T> take(int n) {
        if (n >= length()) {
            return this;
        } else if (n <= 0) {
            return empty();
        } else {
            final int index = n - 1;
            final Object[] root = arePointingToSameLeaf(index, length() - 1)
                                  ? array
                                  : modifyLeaf(array, depthShift, offset + index, Arrays::copyTake, IDENTITY);
            return collapsed(root, offset, n, depthShift);
        }
    }

    private boolean arePointingToSameLeaf(int i, int j) {
        final boolean result = firstDigit(offset + i, BRANCHING_BASE) == firstDigit(offset + j, BRANCHING_BASE);
        assert result == (getLeaf(i) == getLeaf(j));
        return result;
    }

    /* drop root node while it has a single element */
    private static <T> BitMappedTrie<T> collapsed(Object[] array, int offset, int length, int shift) {
        for (; shift > 0; shift -= BRANCHING_BASE) {
            final int skippedElements = array.length - 1;
            if (skippedElements != digit(offset, shift)) {
                break;
            }
            array = getAt(array, skippedElements);
            offset -= treeSize(skippedElements, shift);
        }
        return new BitMappedTrie<>(array, offset, length, shift);
    }

    /* descend the tree from root to leaf, applying the given modifications along the way, returning the new root */
    private Object[] modifyLeaf(Object[] root, int depthShift, int index, NodeModifier node, NodeModifier leaf) {
        if (depthShift == 0) {
            return leaf.apply(root, index);
        } else {
            int previousIndex = firstDigit(index, depthShift);
            root = node.apply(root, previousIndex);

            Object[] array = root;
            for (int shift = depthShift - BRANCHING_BASE; shift >= BRANCHING_BASE; shift -= BRANCHING_BASE) {
                final int offset = digit(index, shift);

                final Object previous = array[previousIndex];
                final Object[] newNode = node.apply(previous, offset);
                array[previousIndex] = newNode;

                previousIndex = offset;
                array = newNode;
            }

            array[previousIndex] = leaf.apply(array[previousIndex], lastDigit(index));
            return root;
        }
    }

    T get(int index) {
        final T[] leaf = getLeaf(index);
        final int leafIndex = lastDigit(offset + index);
        return leaf[leafIndex];
    }

    /**
     * fetch the leaf, corresponding to the given index.
     * Node: the offset and length should be taken into consideration as there may be leading and trailing garbage.
     * Also, the returned array is mutable, but should not be mutated!
     */
    @SuppressWarnings({ "unchecked", "WeakerAccess" })
    T[] getLeaf(int index) {
        if (depthShift == 0) {
            return (T[]) array;
        } else if (depthShift == BRANCHING_BASE) {
            return getAt(array, firstDigit(offset + index, depthShift));
        } else {
            index += offset;
            Object[] leaf = getAt(array, firstDigit(index, depthShift));
            for (int shift = depthShift - BRANCHING_BASE; shift > 0; shift -= BRANCHING_BASE) {
                leaf = getAt(leaf, digit(index, shift));
            }
            return (T[]) leaf;
        }
    }

    Iterator<T> iterator() {
        return new Iterator<T>() {
            private final int globalLength = BitMappedTrie.this.length();
            private int globalIndex = 0;

            private int index = lastDigit(offset);
            private T[] leaf = getLeaf(globalIndex);
            private int length = leaf.length;

            @Override
            public boolean hasNext() { return globalIndex < globalLength; }

            @Override
            public T next() {
                if (index == length) { setCurrentArray(); }

                final T next = leaf[index];
                assert Objects.equals(next, BitMappedTrie.this.get(globalIndex));

                index++;
                globalIndex++;

                return next;
            }

            private void setCurrentArray() {
                index = 0;
                leaf = getLeaf(globalIndex);
                length = leaf.length;
            }
        };
    }

    @SuppressWarnings("unchecked")
    <T2> int visit(LeafVisitor<T2[]> visitor) {
        int globalIndex = 0, start = lastDigit(offset);
        for (int index = 0; index < length; ) {
            final T2[] leaf = (T2[]) getLeaf(index);
            final int end = Math.min(leaf.length, start + length - index);

            globalIndex = visitor.visit(globalIndex, leaf, start, end);

            index += end - start;
            start = 0;
        }
        return globalIndex;
    }

    BitMappedTrie<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");

        final Object[] results = new Object[length()];
        final int length = this.<T> visit((index, leaf, start, end) -> {
            for (int i = start; i < end; i++) {
                final T value = leaf[i];
                if (predicate.test(value)) {
                    results[index++] = value;
                }
            }
            return index;
        });

        return (length() == length)
               ? this
               : BitMappedTrie.ofAll(copyRange(results, 0, length));
    }

    <U> BitMappedTrie<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");

        final Object[] results = newInstance(length);
        this.<T> visit((index, leaf, start, end) -> {
            for (int i = start; i < end; i++) {
                results[index++] = mapper.apply(leaf[i]);
            }
            return index;
        });

        return BitMappedTrie.ofAll(results);
    }

    int length() { return length; }
}

@FunctionalInterface
interface NodeModifier {
    Object[] apply(Object arrayObject, int index);

    static <T> NodeModifier updateLeafWith(T element) { return (o, i) -> copyUpdate(o, i, element); }
    NodeModifier COPY_NODE = (o, i) -> copy(o, i + 1);
    NodeModifier IDENTITY = (o, i) -> (Object[]) o;
}

@FunctionalInterface
interface LeafVisitor<T> {
    int visit(int index, T leaf, int start, int end);
}