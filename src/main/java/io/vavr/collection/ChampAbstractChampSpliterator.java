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
 * <p>
 * References:
 * <p>
 * The code in this class has been derived from JHotDraw 8.
 * <dl>
 *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
 *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
 *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a></dd>
 * </dl>
 */
abstract class ChampAbstractChampSpliterator<K, E> extends Spliterators.AbstractSpliterator<E> {

    private final  Function<K, E> mappingFunction;
    private final  Deque<StackElement<K>> stack = new ArrayDeque<>(ChampNode.MAX_DEPTH);
    private K current;
    @SuppressWarnings("unchecked")
     ChampAbstractChampSpliterator(ChampNode<K> root, Function<K, E> mappingFunction, int characteristics, long size) {
        super(size,characteristics);
        if (root.nodeArity() + root.dataArity() > 0) {
            stack.push(new StackElement<>(root, isReverse()));
        }
        this.mappingFunction = mappingFunction == null ? i -> (E) i : mappingFunction;
    }

     E current() {
        return mappingFunction.apply(current);
    }

    abstract int getNextBitpos(StackElement<K> elem);

    abstract boolean isDone( StackElement<K> elem);

    abstract boolean isReverse();

    abstract int moveIndex( StackElement<K> elem);

     boolean moveNext() {
        while (!stack.isEmpty()) {
            StackElement<K> elem = stack.peek();
            ChampNode<K> node = elem.node;

            if (node instanceof ChampHashCollisionNode) {
                ChampHashCollisionNode<K> hcn = (ChampHashCollisionNode<K>) node;
                current = hcn.getData(moveIndex(elem));
                if (isDone(elem)) {
                    stack.pop();
                }
                return true;
            } else if (node instanceof ChampBitmapIndexedNode) {
                ChampBitmapIndexedNode<K> bin = (ChampBitmapIndexedNode<K>) node;
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

         StackElement(ChampNode<K> node, boolean reverse) {
            this.node = node;
            this.size = node.nodeArity() + node.dataArity();
            this.index = reverse ? size - 1 : 0;
            this.map = (node instanceof ChampBitmapIndexedNode)
                    ? (((ChampBitmapIndexedNode<K>) node).dataMap() | ((ChampBitmapIndexedNode<K>) node).nodeMap()) : 0;
        }
    }
}
