/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.function.Function.identity;
import static io.vavr.collection.ArrayType.obj;
import static io.vavr.collection.Collections.withSize;
import static io.vavr.collection.NodeModifier.COPY_NODE;
import static io.vavr.collection.NodeModifier.IDENTITY;

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
 */
final class BitMappedTrie<T> implements Serializable {

    static final int BRANCHING_BASE = 5;
    static final int BRANCHING_FACTOR = 1 << BRANCHING_BASE;
    static final int BRANCHING_MASK = -1 >>> -BRANCHING_BASE;

    static int firstDigit(int num, int depthShift) { return num >> depthShift; }
    static int digit(int num, int depthShift) { return lastDigit(firstDigit(num, depthShift)); }
    static int lastDigit(int num) { return num & BRANCHING_MASK; }

    private static final long serialVersionUID = 1L;

    private static final BitMappedTrie<?> EMPTY = new BitMappedTrie<>(obj(), obj().empty(), 0, 0, 0);

    @SuppressWarnings("unchecked")
    static <T> BitMappedTrie<T> empty() { return (BitMappedTrie<T>) EMPTY; }

    final ArrayType<T> type;
    @SuppressWarnings("serial") // Conditionally serializable
    private final Object array;
    private final int offset, length;
    private final int depthShift;

    private BitMappedTrie(ArrayType<T> type, Object array, int offset, int length, int depthShift) {
        this.type = type;
        this.array = array;
        this.offset = offset;
        this.length = length;
        this.depthShift = depthShift;
    }

    private static int treeSize(int branchCount, int depthShift) {
        final int fullBranchSize = 1 << depthShift;
        return branchCount * fullBranchSize;
    }

    static <T> BitMappedTrie<T> ofAll(Object array) {
        final ArrayType<T> type = ArrayType.of(array);
        final int size = type.lengthOf(array);
        return (size == 0) ? empty() : ofAll(array, type, size);
    }
    private static <T> BitMappedTrie<T> ofAll(Object array, ArrayType<T> type, int size) {
        int shift = 0;
        for (ArrayType<T> t = type; t.lengthOf(array) > BRANCHING_FACTOR; shift += BRANCHING_BASE) {
            array = t.grouped(array, BRANCHING_FACTOR);
            t = obj();
        }
        return new BitMappedTrie<>(type, array, 0, size, shift);
    }

    private BitMappedTrie<T> boxed() { return map(identity()); }

    BitMappedTrie<T> prependAll(Iterable<? extends T> iterable) {
        final Collections.IterableWithSize<? extends T> iter = withSize(iterable);
        try {
            return prepend(iter.reverseIterator(), iter.size());
        } catch (ClassCastException ignored) {
            return boxed().prepend(iter.reverseIterator(), iter.size());
        }
    }
    private BitMappedTrie<T> prepend(java.util.Iterator<? extends T> iterator, int size) {
        BitMappedTrie<T> result = this;
        while (size > 0) {
            Object array = result.array;
            int shift = result.depthShift, offset = result.offset;
            if (result.isFullLeft()) {
                array = obj().copyUpdate(obj().empty(), BRANCHING_FACTOR - 1, array);
                shift += BRANCHING_BASE;
                offset = treeSize(BRANCHING_FACTOR - 1, shift);
            }

            final int index = offset - 1;
            final int delta = Math.min(size, lastDigit(index) + 1);
            size -= delta;

            array = result.modify(array, shift, index, COPY_NODE, prependToLeaf(iterator));
            result = new BitMappedTrie<>(type, array, offset - delta, result.length + delta, shift);
        }
        return result;
    }
    private boolean isFullLeft() { return offset == 0; }
    private NodeModifier prependToLeaf(java.util.Iterator<? extends T> iterator) {
        return (array, index) -> {
            final Object copy = type.copy(array, BRANCHING_FACTOR);
            while (iterator.hasNext() && index >= 0) {
                type.setAt(copy, index--, iterator.next());
            }
            return copy;
        };
    }

    BitMappedTrie<T> appendAll(Iterable<? extends T> iterable) {
        final Collections.IterableWithSize<? extends T> iter = withSize(iterable);
        try {
            return append(iter.iterator(), iter.size());
        } catch (ClassCastException ignored) {
            return boxed().append(iter.iterator(), iter.size());
        }
    }
    private BitMappedTrie<T> append(java.util.Iterator<? extends T> iterator, int size) {
        BitMappedTrie<T> result = this;
        while (size > 0) {
            Object array = result.array;
            int shift = result.depthShift;
            if (result.isFullRight()) {
                array = obj().asArray(array);
                shift += BRANCHING_BASE;
            }

            final int index = offset + result.length;
            final int leafSpace = lastDigit(index);
            final int delta = Math.min(size, BRANCHING_FACTOR - leafSpace);
            size -= delta;

            array = result.modify(array, shift, index, COPY_NODE, appendToLeaf(iterator, leafSpace + delta));
            result = new BitMappedTrie<>(type, array, offset, result.length + delta, shift);
        }
        return result;

    }
    private boolean isFullRight() { return (offset + length + 1) > treeSize(BRANCHING_FACTOR, depthShift); }
    private NodeModifier appendToLeaf(java.util.Iterator<? extends T> iterator, int leafSize) {
        return (array, index) -> {
            final Object copy = type.copy(array, leafSize);
            while (iterator.hasNext() && index < leafSize) {
                type.setAt(copy, index++, iterator.next());
            }
            return copy;
        };
    }

    BitMappedTrie<T> update(int index, T element) {
        try {
            final Object root = modify(array, depthShift, offset + index, COPY_NODE, updateLeafWith(type, element));
            return new BitMappedTrie<>(type, root, offset, length, depthShift);
        } catch (ClassCastException ignored) {
            return boxed().update(index, element);
        }
    }
    private NodeModifier updateLeafWith(ArrayType<T> type, T element) { return (a, i) -> type.copyUpdate(a, i, element); }

    BitMappedTrie<T> drop(int n) {
        if (n <= 0) {
            return this;
        } else if (n >= length) {
            return empty();
        } else {
            final int index = offset + n;
            final Object root = arePointingToSameLeaf(0, n)
                                ? array
                                : modify(array, depthShift, index, obj()::copyDrop, IDENTITY);
            return collapsed(type, root, index, length - n, depthShift);
        }
    }

    BitMappedTrie<T> take(int n) {
        if (n >= length) {
            return this;
        } else if (n <= 0) {
            return empty();
        } else {
            final int index = n - 1;
            final Object root = arePointingToSameLeaf(index, length - 1)
                                ? array
                                : modify(array, depthShift, offset + index, obj()::copyTake, IDENTITY);
            return collapsed(type, root, offset, n, depthShift);
        }
    }

    private boolean arePointingToSameLeaf(int i, int j) {
        return firstDigit(offset + i, BRANCHING_BASE) == firstDigit(offset + j, BRANCHING_BASE);
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
    private Object modify(Object root, int depthShift, int index, NodeModifier node, NodeModifier leaf) {
        return (depthShift == 0)
               ? leaf.apply(root, index)
               : modifyNonLeaf(root, depthShift, index, node, leaf);
    }
    private Object modifyNonLeaf(Object root, int depthShift, int index, NodeModifier node, NodeModifier leaf) {
        int previousIndex = firstDigit(index, depthShift);
        root = node.apply(root, previousIndex);

        Object array = root;
        for (int shift = depthShift - BRANCHING_BASE; shift >= BRANCHING_BASE; shift -= BRANCHING_BASE) {
            final int prev = previousIndex;
            previousIndex = digit(index, shift);
            array = setNewNode(node, prev, array, previousIndex);
        }

        final Object newLeaf = leaf.apply(obj().getAt(array, previousIndex), lastDigit(index));
        obj().setAt(array, previousIndex, newLeaf);
        return root;
    }
    private Object setNewNode(NodeModifier node, int previousIndex, Object array, int offset) {
        final Object previous = obj().getAt(array, previousIndex);
        final Object newNode = node.apply(previous, offset);
        obj().setAt(array, previousIndex, newNode);
        return newNode;
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
    @SuppressWarnings("WeakerAccess")
    Object getLeaf(int index) {
        if (depthShift == 0) {
            return array;
        } else {
            return getLeafGeneral(index);
        }
    }
    private Object getLeafGeneral(int index) {
        index += offset;
        Object leaf = obj().getAt(array, firstDigit(index, depthShift));
        for (int shift = depthShift - BRANCHING_BASE; shift > 0; shift -= BRANCHING_BASE) {
            leaf = obj().getAt(leaf, digit(index, shift));
        }
        return leaf;
    }

    Iterator<T> iterator() {
        return new Iterator<T>() {
            private final int globalLength = BitMappedTrie.this.length;
            private int globalIndex = 0;

            private int index = lastDigit(offset);
            private Object leaf = getLeaf(globalIndex);
            private int length = type.lengthOf(leaf);

            @Override
            public boolean hasNext() { return globalIndex < globalLength; }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("next() on empty iterator");
                }
                
                if (index == length) { setCurrentArray(); }
                final T next = type.getAt(leaf, index);

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
            final int end = getMin(start, index, leaf);

            globalIndex = visitor.visit(globalIndex, leaf, start, end);

            index += end - start;
            start = 0;
        }
        return globalIndex;
    }
    private int getMin(int start, int index, Object leaf) { return Math.min(type.lengthOf(leaf), start + length - index); }

    BitMappedTrie<T> filter(Predicate<? super T> predicate) {
        final Object results = type.newInstance(length());
        final int length = this.<T> visit((index, leaf, start, end) -> filter(predicate, results, index, leaf, start, end));
        return (this.length == length)
               ? this
               : BitMappedTrie.ofAll(type.copyRange(results, 0, length));
    }
    private int filter(Predicate<? super T> predicate, Object results, int index, T leaf, int start, int end) {
        for (int i = start; i < end; i++) {
            final T value = type.getAt(leaf, i);
            if (predicate.test(value)) {
                type.setAt(results, index++, value);
            }
        }
        return index;
    }

    <U> BitMappedTrie<U> map(Function<? super T, ? extends U> mapper) {
        final Object results = obj().newInstance(length);
        this.<T> visit((index, leaf, start, end) -> map(mapper, results, index, leaf, start, end));
        return BitMappedTrie.ofAll(results);
    }
    private <U> int map(Function<? super T, ? extends U> mapper, Object results, int index, T leaf, int start, int end) {
        for (int i = start; i < end; i++) {
            obj().setAt(results, index++, mapper.apply(type.getAt(leaf, i)));
        }
        return index;
    }

    int length() { return length; }
}

@FunctionalInterface
interface NodeModifier {
    Object apply(Object array, int index);

    NodeModifier COPY_NODE = (o, i) -> obj().copy(o, i + 1);
    NodeModifier IDENTITY = (o, i) -> o;
}

@FunctionalInterface
interface LeafVisitor<T> {
    int visit(int index, T leaf, int start, int end);
}
