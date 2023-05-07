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

/**
 * Abstract base class for a transient CHAMP collection. 
 * 
 * @param <E> the element type
 */
abstract class ChampAbstractTransientCollection<E> {
    /**
     * The current owner id of this map.
     * <p>
     * All nodes that have the same non-null owner id, are exclusively owned
     * by this map, and therefore can be mutated without affecting other map.
     * <p>
     * If this owner id is null, then this map does not own any nodes.
     */

    protected ChampIdentityObject owner;

    /**
     * The root of this CHAMP trie.
     */
    protected ChampBitmapIndexedNode<E> root;

    /**
     * The number of entries in this map.
     */
    protected int size;

    /**
     * The number of times this map has been structurally modified.
     */
    protected int modCount;

    int size() {
        return size;
    }
boolean isEmpty(){return size==0;}

    ChampIdentityObject getOrCreateOwner() {
        if (owner == null) {
            owner = new ChampIdentityObject();
        }
        return owner;
    }
}
