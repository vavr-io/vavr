/*
 * ____  ______________  ________________________  __________
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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Provides iterators and spliterators for CHAMP tries.
 */
class ChampIteration {

    /**
     * Adapts a {@link Spliterator} to the {@link Iterator} interface.
     * <p>
     * References:
     * <p>
     * The code in this class has been derived from JHotDraw 8.
     * <dl>
     *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
     *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
     *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a></dd>
     * </dl>
     *
     * @param <E> the element type
     */
    static class IteratorFacade<E> implements Iterator<E>, Consumer<E> {
        private final Spliterator<E> spliterator;

        IteratorFacade(Spliterator<E> spliterator) {
            this.spliterator = spliterator;
        }

        boolean hasCurrent = false;
        E current;

        public void accept(E t) {
            hasCurrent = true;
            current = t;
        }

        @Override
        public boolean hasNext() {
            if (!hasCurrent) {
                spliterator.tryAdvance(this);
            }
            return hasCurrent;
        }

        @Override
        public E next() {
            if (!hasCurrent && !hasNext())
                throw new NoSuchElementException();
            else {
                hasCurrent = false;
                E t = current;
                current = null;
                return t;
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            if (hasCurrent) {
                hasCurrent = false;
                E t = current;
                current = null;
                action.accept(t);
            }
            spliterator.forEachRemaining(action);
        }
    }

    /**
     * Data iterator over a CHAMP trie.
     * <p>
     * XXX This iterator carefully replicates the iteration sequence of the original HAMT-based
     * HashSet class. We can not use a more performant implementation, because HashSetTest
     * requires that we use a specific iteration sequence.
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
    static class ChampSpliterator<K, E> extends Spliterators.AbstractSpliterator<E> {
        private final Function<K, E> mappingFunction;
        private final Deque<StackElement<K>> stack = new ArrayDeque<>(ChampTrie.Node.MAX_DEPTH);
        private K current;

        @SuppressWarnings("unchecked")
        public ChampSpliterator(ChampTrie.Node<K> root, Function<K, E> mappingFunction, int characteristics, long size) {
            super(size, characteristics);
            if (root.nodeArity() + root.dataArity() > 0) {
                stack.push(new StackElement<>(root));
            }
            this.mappingFunction = mappingFunction == null ? i -> (E) i : mappingFunction;
        }

        public E current() {
            return mappingFunction.apply(current);
        }


        int getNextBitpos(StackElement<K> elem) {
            return 1 << Integer.numberOfTrailingZeros(elem.map);
        }

        boolean isDone(StackElement<K> elem) {
            return elem.index >= elem.size;
        }


        int moveIndex(StackElement<K> elem) {
            return elem.index++;
        }

        boolean moveNext() {
            while (!stack.isEmpty()) {
                StackElement<K> elem = stack.peek();
                ChampTrie.Node<K> node = elem.node;

                if (node instanceof ChampTrie.HashCollisionNode) {
                    ChampTrie.HashCollisionNode<K> hcn = (ChampTrie.HashCollisionNode<K>) node;
                    current = hcn.getData(moveIndex(elem));
                    if (isDone(elem)) {
                        stack.pop();
                    }
                    return true;
                } else if (node instanceof ChampTrie.BitmapIndexedNode) {
                    ChampTrie.BitmapIndexedNode<K> bin = (ChampTrie.BitmapIndexedNode<K>) node;
                    int bitpos = getNextBitpos(elem);
                    elem.map ^= bitpos;
                    moveIndex(elem);
                    if (isDone(elem)) {
                        stack.pop();
                    }
                    if ((bin.nodeMap() & bitpos) != 0) {
                        stack.push(new StackElement<>(bin.getNode(bin.nodeIndex(bitpos))));
                    } else {
                        current = bin.getData(bin.dataIndex(bitpos));
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean tryAdvance(Consumer<? super E> action) {
            if (moveNext()) {
                action.accept(current());
                return true;
            }
            return false;
        }

        static class StackElement<K> {
            final ChampTrie.Node<K> node;
            final int size;
            int index;
            int map;

            public StackElement(ChampTrie.Node<K> node) {
                this.node = node;
                this.size = node.nodeArity() + node.dataArity();
                this.index = 0;
                this.map = (node instanceof ChampTrie.BitmapIndexedNode)
                        ? (((ChampTrie.BitmapIndexedNode<K>) node).dataMap() | ((ChampTrie.BitmapIndexedNode<K>) node).nodeMap()) : 0;
            }
        }
    }
}
