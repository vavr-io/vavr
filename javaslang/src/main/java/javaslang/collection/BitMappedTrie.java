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

import static java.util.function.Function.identity;
import static javaslang.collection.ArrayType.obj;
import static javaslang.collection.ArrayType.primitiveType;
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

    private static final BitMappedTrie<?> EMPTY = new BitMappedTrie<>(obj(), obj().empty(), 0, 0, 0);
    @SuppressWarnings("unchecked")
    static <T> BitMappedTrie<T> empty() { return (BitMappedTrie<T>) EMPTY; }

    final ArrayType<T> type;
    private final Object array;
    private final int offset, length;
    private final int depthShift;

    private BitMappedTrie(ArrayType<T> type, Object array, int offset, int length, int depthShift) {
        this.type = type;
        this.array = array;
        this.offset = offset;
        this.length = length;
        this.depthShift = depthShift;

        assert length <= treeSize(BRANCHING_FACTOR, depthShift);
        assert (EMPTY == null) || ((length > 0) && (ArrayType.of(array).lengthOf(array) > 0));
    }

    private static int treeSize(int branchCount, int depthShift) {
        final int fullBranchSize = 1 << depthShift;
        return branchCount * fullBranchSize;
    }

    static <T> BitMappedTrie<T> ofAll(Object array) {
        final ArrayType<T> type = ArrayType.of(array);
        final int size = type.lengthOf(array);
        if (size == 0) {
            return empty();
        } else {
            int shift = 0;
            for (ArrayType<T> t = type; t.lengthOf(array) > BRANCHING_FACTOR; shift += BRANCHING_BASE) {
                array = t.grouped(array, BRANCHING_FACTOR);
                t = obj();
            }
            return new BitMappedTrie<>(type, array, 0, size, shift);
        }
    }

    private BitMappedTrie<T> boxed() { return map(identity()); }

    BitMappedTrie<T> prepend(T leading) {
        if (cannotAdd(leading)) {
            return boxed().prepend(leading);
        } else {
            final int newSize = length() + 1;
            if (length() == 0) {
                return new BitMappedTrie<>(type, type.asArray(leading), offset, newSize, depthShift);
            } else {
                Object array = this.array;
                int shift = depthShift, offset = this.offset;
                if (isFullLeft()) {
                    array = obj().copyUpdate(obj().empty(), BRANCHING_FACTOR - 1, array);
                    shift += BRANCHING_BASE;
                    offset = treeSize(BRANCHING_FACTOR - 1, shift);
                }

                offset -= 1;
                array = modifyLeaf(array, shift, offset, COPY_NODE, updateLeafWith(type, leading));
                return new BitMappedTrie<>(type, array, offset, newSize, shift);
            }
        }
    }
    private boolean isFullLeft() { return offset == 0; }

    BitMappedTrie<T> append(T trailing) {
        if (cannotAdd(trailing)) {
            return boxed().append(trailing);
        } else {
            final int newSize = length() + 1;
            if (length() == 0) {
                return new BitMappedTrie<>(type, type.asArray(trailing), offset, newSize, depthShift);
            } else {
                Object array = this.array;
                int shift = depthShift;
                if (isFullRight(newSize)) {
                    array = obj().asArray(array);
                    shift += BRANCHING_BASE;
                }

                array = modifyLeaf(array, shift, offset + length(), COPY_NODE, updateLeafWith(type, trailing));
                return new BitMappedTrie<>(type, array, offset, newSize, shift);
            }
        }
    }
    private boolean isFullRight(int newSize) { return (offset + newSize) > treeSize(BRANCHING_FACTOR, depthShift); }

    BitMappedTrie<T> update(int index, T element) {
        if (cannotAdd(element)) {
            return boxed().update(index, element);
        } else {
            final Object root = modifyLeaf(array, depthShift, offset + index, COPY_NODE, updateLeafWith(type, element));
            return new BitMappedTrie<>(type, root, offset, length(), depthShift);
        }
    }

    BitMappedTrie<T> drop(int n) {
        if (n <= 0) {
            return this;
        } else if (n >= length()) {
            return empty();
        } else {
            final int index = offset + n;
            final Object root = arePointingToSameLeaf(0, n)
                                ? array
                                : modifyLeaf(array, depthShift, index, obj()::copyDrop, IDENTITY);
            return collapsed(type, root, index, length() - n, depthShift);
        }
    }

    BitMappedTrie<T> take(int n) {
        if (n >= length()) {
            return this;
        } else if (n <= 0) {
            return empty();
        } else {
            final int index = n - 1;
            final Object root = arePointingToSameLeaf(index, length() - 1)
                                ? array
                                : modifyLeaf(array, depthShift, offset + index, obj()::copyTake, IDENTITY);
            return collapsed(type, root, offset, n, depthShift);
        }
    }

    private boolean arePointingToSameLeaf(int i, int j) {
        final boolean result = firstDigit(offset + i, BRANCHING_BASE) == firstDigit(offset + j, BRANCHING_BASE);
        assert result == (getLeaf(i) == getLeaf(j));
        return result;
    }

    /* drop root node while it has a single element */
    private static <T> BitMappedTrie<T> collapsed(ArrayType<T> type, Object array, int offset, int length, int shift) {
        for (; shift > 0; shift -= BRANCHING_BASE) {
            final int skippedElements = obj().lengthOf(array) - 1;
            if (skippedElements != digit(offset, shift)) {
                break;
            }
            array = obj().getAt(array, skippedElements);
            offset -= treeSize(skippedElements, shift);
        }
        return new BitMappedTrie<>(type, array, offset, length, shift);
    }

    /* descend the tree from root to leaf, applying the given modifications along the way, returning the new root */
    private Object modifyLeaf(Object root, int depthShift, int index, NodeModifier node, NodeModifier leaf) {
        if (depthShift == 0) {
            return leaf.apply(root, index);
        } else {
            int previousIndex = firstDigit(index, depthShift);
            root = node.apply(root, previousIndex);

            Object array = root;
            for (int shift = depthShift - BRANCHING_BASE; shift >= BRANCHING_BASE; shift -= BRANCHING_BASE) {
                final int offset = digit(index, shift);

                final Object previous = obj().getAt(array, previousIndex);
                final Object newNode = node.apply(previous, offset);
                obj().setAt(array, previousIndex, newNode);

                previousIndex = offset;
                array = newNode;
            }

            obj().setAt(array, previousIndex, leaf.apply(obj().getAt(array, previousIndex), lastDigit(index)));
            return root;
        }
    }

    T get(int index) {
        final Object leaf = getLeaf(index);
        final int leafIndex = lastDigit(offset + index);
        return type.getAt(leaf, leafIndex);
    }

    /**
     * fetch the leaf, corresponding to the given index.
     * Node: the offset and length should be taken into consideration as there may be leading and trailing garbage.
     * Also, the returned array is mutable, but should not be mutated!
     */
    Object getLeaf(int index) {
        if (depthShift == 0) {
            return array;
        } else if (depthShift == BRANCHING_BASE) {
            return obj().getAt(array, firstDigit(offset + index, depthShift));
        } else {
            index += offset;
            Object leaf = obj().getAt(array, firstDigit(index, depthShift));
            for (int shift = depthShift - BRANCHING_BASE; shift > 0; shift -= BRANCHING_BASE) {
                leaf = obj().getAt(leaf, digit(index, shift));
            }
            return leaf;
        }
    }

    Iterator<T> iterator() {
        return new Iterator<T>() {
            private final int globalLength = BitMappedTrie.this.length();
            private int globalIndex = 0;

            private int index = lastDigit(offset);
            private Object leaf = getLeaf(globalIndex);
            private int length = type.lengthOf(leaf);

            @Override
            public boolean hasNext() { return globalIndex < globalLength; }

            @Override
            public T next() {
                if (index == length) { setCurrentArray(); }

                final T next = type.getAt(leaf, index);
                assert Objects.equals(next, BitMappedTrie.this.get(globalIndex));

                index++;
                globalIndex++;

                return next;
            }

            private void setCurrentArray() {
                index = 0;
                leaf = getLeaf(globalIndex);
                length = type.lengthOf(leaf);
            }
        };
    }

    @SuppressWarnings("unchecked")
    <T2> int visit(LeafVisitor<T2> visitor) {
        int globalIndex = 0, start = lastDigit(offset);
        for (int index = 0; index < length; ) {
            final T2 leaf = (T2) getLeaf(index);
            final int end = Math.min(type.lengthOf(leaf), start + length - index);

            globalIndex = visitor.visit(globalIndex, leaf, start, end);

            index += end - start;
            start = 0;
        }
        return globalIndex;
    }

    BitMappedTrie<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");

        final Object results = type.newInstance(length());
        final int length = this.<T> visit((index, leaf, start, end) -> {
            for (int i = start; i < end; i++) {
                final T value = type.getAt(leaf, i);
                if (predicate.test(value)) {
                    type.setAt(results, index++, value);
                }
            }
            return index;
        });

        return (length() == length)
               ? this
               : BitMappedTrie.ofAll(type.copyRange(results, 0, length));
    }

    <U> BitMappedTrie<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");

        final Object results = obj().newInstance(length);
        this.<T> visit((index, leaf, start, end) -> {
            for (int i = start; i < end; i++) {
                obj().setAt(results, index++, mapper.apply(type.getAt(leaf, i)));
            }
            return index;
        });

        return BitMappedTrie.ofAll(results);
    }

    int length()                         { return length; }
    private boolean cannotAdd(T element) { return (type != obj()) && (type.type() != primitiveType(element)); }
}

@FunctionalInterface
interface NodeModifier {
    Object apply(Object array, int index);

    static <T> NodeModifier updateLeafWith(ArrayType<T> type, T element) { return (a, i) -> type.copyUpdate(a, i, element); }
    NodeModifier COPY_NODE = (o, i) -> obj().copy(o, i + 1);
    NodeModifier IDENTITY = (o, i) -> o;
}

@FunctionalInterface
interface LeafVisitor<T> {
    int visit(int index, T leaf, int start, int end);
}