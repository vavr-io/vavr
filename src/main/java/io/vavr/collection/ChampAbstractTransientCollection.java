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
abstract class ChampAbstractTransientCollection<E> {
    /**
     * The current owner id of this map.
     * <p>
     * All nodes that have the same non-null owner id, are exclusively owned
     * by this map, and therefore can be mutated without affecting other map.
     * <p>
     * If this owner id is null, then this map does not own any nodes.
     */

     ChampIdentityObject owner;

    /**
     * The root of this CHAMP trie.
     */
     ChampBitmapIndexedNode<E> root;

    /**
     * The number of entries in this map.
     */
     int size;

    /**
     * The number of times this map has been structurally modified.
     */
     int modCount;

    int size() {
        return size;
    }

    boolean isEmpty() {
        return size == 0;
    }

    ChampIdentityObject getOrCreateOwner() {
        if (owner == null) {
            owner = new ChampIdentityObject();
        }
        return owner;
    }
}
