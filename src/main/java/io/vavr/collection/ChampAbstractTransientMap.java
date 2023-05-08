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

import io.vavr.Tuple2;

import java.util.Objects;

/**
 * Abstract base class for a transient CHAMP map.
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
abstract class ChampAbstractTransientMap<K,V,E> extends ChampAbstractTransientCollection<E>{
    @SuppressWarnings("unchecked")
    boolean removeAll(Iterable<?> c) {
        if (isEmpty()) {
            return false;
        }
        boolean modified = false;
        for (Object key : c) {
            var details = removeKey((K)key);
            modified |= details.isModified();
        }
        return modified;
    }

    abstract ChampChangeEvent<E> removeKey(K key);
    abstract void clear();
    abstract V put(K key, V value);

   boolean putAllTuples(Iterable<? extends Tuple2<? extends K,? extends V>> c) {
        boolean modified = false;
        for (var e : c) {
            var oldValue = put(e._1,e._2);
            modified = modified || !Objects.equals(oldValue, e);
        }
        return modified;

    }
}
