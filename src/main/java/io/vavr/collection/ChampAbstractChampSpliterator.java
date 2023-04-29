/*
 * @(#)AbstractKeySpliterator.java
 * Copyright © 2023 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Key iterator over a CHAMP trie.
 * <p>
 * Uses a stack with a fixed maximal depth.
 * Iterates over keys in preorder sequence.
 * <p>
 * Supports the {@code remove} operation. The remove function must
 * create a new version of the trie, so that iterator does not have
 * to deal with structural changes of the trie.
 */
abstract class ChampAbstractChampSpliterator<K, E> extends Spliterators.AbstractSpliterator<E> {

    private final  Function<K, E> mappingFunction;
    private final  Deque<StackElement<K>> stack = new ArrayDeque<>(ChampNode.MAX_DEPTH);
    private K current;
    @SuppressWarnings("unchecked")
    public ChampAbstractChampSpliterator(ChampNode<K> root, Function<K, E> mappingFunction, int characteristics, long size) {
        super(size,characteristics);
        if (root.nodeArity() + root.dataArity() > 0) {
            stack.push(new StackElement<>(root, isReverse()));
        }
        this.mappingFunction = mappingFunction == null ? i -> (E) i : mappingFunction;
    }

    public E current() {
        return mappingFunction.apply(current);
    }

    abstract int getNextBitpos(StackElement<K> elem);

    abstract boolean isDone( StackElement<K> elem);

    abstract boolean isReverse();

    abstract int moveIndex( StackElement<K> elem);

    public boolean moveNext() {
        while (!stack.isEmpty()) {
            StackElement<K> elem = stack.peek();
            ChampNode<K> node = elem.node;

            if (node instanceof ChampHashCollisionNode<K> hcn) {
                current = hcn.getData(moveIndex(elem));
                if (isDone(elem)) {
                    stack.pop();
                }
                return true;
            } else if (node instanceof ChampBitmapIndexedNode<K> bin) {
                int bitpos = getNextBitpos(elem);
                elem.map ^= bitpos;
                moveIndex(elem);
                if (isDone(elem)) {
                    stack.pop();
                }
                if ((bin.nodeMap() & bitpos) != 0) {
                    stack.push(new StackElement<>(bin.nodeAt(bitpos), isReverse()));
                } else {
                    current = bin.dataAt(bitpos);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean tryAdvance( Consumer<? super E> action) {
        if (moveNext()) {
            action.accept(current());
            return true;
        }
        return false;
    }

    static class StackElement<K> {
        final ChampNode<K> node;
        final int size;
        int index;
        int map;

        public StackElement(ChampNode<K> node, boolean reverse) {
            this.node = node;
            this.size = node.nodeArity() + node.dataArity();
            this.index = reverse ? size - 1 : 0;
            this.map = (node instanceof ChampBitmapIndexedNode<K> bin)
                    ? (bin.dataMap() | bin.nodeMap()) : 0;
        }
    }
}
