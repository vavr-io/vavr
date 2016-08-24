/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;

import static javaslang.collection.Arrays.*;
import static javaslang.collection.NodeModifier.copyLeaf;
import static javaslang.collection.NodeModifier.updateLeafWith;

/**
 * A `bit-mapped trie` is a very wide and shallow tree (for integer indices the depth will be ≤ 6).
 * Each node has a maximum of `32` children (configurable).
 * Access to a given position is done by converting the index to a base 32 number and using each digit to descend down the tree.
 * Modifying the tree is done similarly, but along the way the path is copied, returning a new root every time.
 * Append inserts in the last leaf, or if the tree is full from the right, it adds another layer on top of it (the old root will be the first of the new one).
 * Prepend is done similarly, but an offset is needed, because adding a new top node (where the current root would be the last node of the new root)
 * shifts the indices by half of the current tree's full size. The `offset` shifts them back to the correct index.
 *
 * @author Pap Lőrinc
 * @since 2.1.0
 */
final class BitMappedTrie<T> implements Serializable {
    static int BRANCHING_BASE = 5;
    static int branchingFactor()                   { return 1 << BRANCHING_BASE; }
    static int firstDigit(int num, int depthShift) { return num >> depthShift; }
    static int digit(int num, int depthShift)      { return lastDigit(firstDigit(num, depthShift)); }
    static int lastDigit(int num)                  { return num & (-1 >>> -BRANCHING_BASE); }

    private static final long serialVersionUID = 1L;

    private static final BitMappedTrie<?> EMPTY = new BitMappedTrie<>(Arrays.empty(), 0, 0, 0);
    @SuppressWarnings("unchecked")
    static <T> BitMappedTrie<T> empty() { return (BitMappedTrie<T>) EMPTY; }

    private final Object[] array;
    private final int offset, length;
    private final int depthShift;

    BitMappedTrie(Object[] array, int offset, int length, int depthShift) {
        this.array = array;
        this.offset = offset;
        this.length = length;
        this.depthShift = depthShift;

        assert length <= treeSize(branchingFactor(), depthShift);
        assert (EMPTY == null) || ((length > 0) && (array.length > 0));
    }

    private static int treeSize(int branchCount, int depthShift) {
        final int fullBranchSize = 1 << depthShift;
        return branchCount * fullBranchSize;
    }

    BitMappedTrie<T> prepend(T leading) {
        final int newSize = length() + 1;
        if (length() == 0) {
            return new BitMappedTrie<>(asArray(leading), offset, newSize, depthShift);
        } else {
            Object[] array = this.array;
            int shift = depthShift, offset = this.offset;
            if (isFullLeft()) {
                array = copyUpdate(Arrays.empty(), branchingFactor() - 1, array);
                shift += BRANCHING_BASE;
                offset = treeSize(branchingFactor() - 1, shift);
            }

            offset -= 1;
            array = modifyLeaf(array, shift, offset, copyLeaf, updateLeafWith(leading));
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

            array = modifyLeaf(array, shift, offset + length(), copyLeaf, updateLeafWith(trailing));
            return new BitMappedTrie<>(array, offset, newSize, shift);
        }
    }
    private boolean isFullRight(int newSize) { return (offset + newSize) > treeSize(branchingFactor(), depthShift); }

    BitMappedTrie<T> update(int index, T element) {
        final Object[] root = modifyLeaf(array, depthShift, offset + index, copyLeaf, updateLeafWith(element));
        return new BitMappedTrie<>(root, offset, length(), depthShift);
    }

    BitMappedTrie<T> drop(int n) {
        if (n <= 0) {
            return this;
        } else if (n >= length()) {
            return empty();
        } else {
            final NodeModifier drop = Arrays::copyDrop;
            final Object[] root = modifyLeaf(array, depthShift, offset + n, drop, drop);
            return new BitMappedTrie<>(root, offset + n, length() - n, depthShift);
        }
    }

    BitMappedTrie<T> take(int n) {
        if (n >= length()) {
            return this;
        } else if (n <= 0) {
            return empty();
        } else {
            final NodeModifier take = Arrays::copyTake;
            final Object[] root = modifyLeaf(array, depthShift, (offset + n) - 1, take, take);
            return new BitMappedTrie<>(root, offset, n, depthShift);
        }
    }

    /* descend the tree from root to leaf, applying the given modifications along the way, returning the new root */
    private static Object[] modifyLeaf(Object[] root, int depthShift, int index, NodeModifier node, NodeModifier leaf) {
        if (depthShift == 0) {
            return leaf.apply(root, index);
        } else {
            int previousIndex = firstDigit(index, depthShift);
            root = node.apply(root, previousIndex);

            Object[] array = root;
            for (int shift = depthShift - BRANCHING_BASE; shift >= BRANCHING_BASE; shift -= BRANCHING_BASE) {
                final int offset = digit(index, shift);

                final Object[] newNode = node.apply(array[previousIndex], offset);
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
        final int leafIndex = lastDigit(offset() + index);
        return leaf[leafIndex];
    }

    /**
     * fetch the leaf, corresponding to the given index.
     * Node: the offset and length should be taken into consideration as there may be leading and trailing garbage.
     */
    @SuppressWarnings("unchecked")
    T[] getLeaf(int index) {
        Object[] leaf = array;
        for (int shift = depthShift; shift > 0; shift -= BRANCHING_BASE) {
            leaf = getAt(leaf, digit(offset + index, shift));
        }
        return (T[]) leaf;
    }

    Iterator<T> iterator() {
        return new Iterator<T>() {
            final int globalLength = BitMappedTrie.this.length();
            int globalIndex = 0;

            int index = lastDigit(offset());
            T[] leaf = getLeaf(globalIndex);
            int length = leaf.length;

            @Override
            public boolean hasNext() { return globalIndex < globalLength; }

            @Override
            public T next() {
                if (index == length) { setCurrentArray(); }

                final T next = leaf[index];

                index++;
                globalIndex++;

                return next;
            }

            void setCurrentArray() {
                index = 0;
                leaf = getLeaf(globalIndex);
                length = leaf.length;
                assert leaf != null;
            }
        };
    }

    int offset() { return offset; }
    int length() { return length; }
}

@FunctionalInterface
interface NodeModifier {
    Object[] apply(Object arrayObject, int index);

    static NodeModifier copyLeaf = (o, i) -> copy(o, i + 1);
    static NodeModifier updateLeafWith(Object element) { return (o, i) -> copyUpdate(o, i, element); }
}