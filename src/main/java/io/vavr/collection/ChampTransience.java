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

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Provides abstract base classes for transient collections.
 */
class ChampTransience {
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
     * @param <D> the data type of the CHAMP trie
     */
    abstract static class ChampAbstractTransientCollection<D> {
        /**
         * The current owner id of this map.
         * <p>
         * All nodes that have the same non-null owner id, are exclusively owned
         * by this map, and therefore can be mutated without affecting other map.
         * <p>
         * If this owner id is null, then this map does not own any nodes.
         */

         ChampTrie.IdentityObject owner;

        /**
         * The root of this CHAMP trie.
         */
         ChampTrie.BitmapIndexedNode<D> root;

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

        ChampTrie.IdentityObject makeOwner() {
            if (owner == null) {
                owner = new ChampTrie.IdentityObject();
            }
            return owner;
        }
    }

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
    abstract static class ChampAbstractTransientMap<K,V,E> extends ChampAbstractTransientCollection<E> {
        @SuppressWarnings("unchecked")
        boolean removeAll(Iterable<?> c) {
            if (isEmpty()) {
                return false;
            }
            boolean modified = false;
            for (Object key : c) {
                ChampTrie.ChangeEvent<E> details = removeKey((K)key);
                modified |= details.isModified();
            }
            return modified;
        }

        abstract ChampTrie.ChangeEvent<E> removeKey(K key);
        abstract void clear();
        abstract V put(K key, V value);

       boolean putAllTuples(Iterable<? extends Tuple2<? extends K,? extends V>> c) {
            boolean modified = false;
            for (Tuple2<? extends K,? extends V> e : c) {
                V oldValue = put(e._1,e._2);
                modified = modified || !Objects.equals(oldValue, e);
            }
            return modified;
        }

        @SuppressWarnings("unchecked")
        boolean retainAllTuples(Iterable<? extends Tuple2<K, V>> c) {
            if (isEmpty()) {
                return false;
            }
            if (c instanceof Collection<?> && ((Collection<?>) c).isEmpty()
                    || c instanceof Traversable<?> && ((Traversable<?>) c).isEmpty()) {
                clear();
                return true;
            }
            if (c instanceof Collection<?>) {
                Collection<?> that = (Collection<?>) c;
                return filterAll(e -> that.contains(e.getKey()));
            }else if (c instanceof java.util.Map<?, ?>) {
                java.util.Map<?, ?> that = (java.util.Map<?, ?>) c;
                return filterAll(e -> that.containsKey(e.getKey())&&Objects.equals(e.getValue(),that.get(e.getKey())));
            } else {
                java.util.HashSet<Object> that = new HashSet<>();
                c.forEach(t->that.add(new AbstractMap.SimpleImmutableEntry<>(t._1,t._2)));
                return filterAll(that::contains);
            }
        }

        abstract boolean filterAll(Predicate<Map.Entry<K, V>> predicate);
    }

    /**
     * Abstract base class for a transient CHAMP set.
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
     * @param <D> the data type of the CHAMP trie
     */
    abstract static class ChampAbstractTransientSet<E,D> extends ChampAbstractTransientCollection<D> {
        abstract void clear();
        abstract boolean remove(Object o);
        boolean removeAll( Iterable<?> c) {
            if (isEmpty()) {
                return false;
            }
            if (c == this) {
                clear();
                return true;
            }
            boolean modified = false;
            for (Object o : c) {
                modified |= remove(o);
            }
            return modified;
        }

        abstract java.util.Iterator<E> iterator();
        boolean retainAll( Iterable<?> c) {
            if (isEmpty()) {
                return false;
            }
            if (c instanceof Collection<?> && ((Collection<?>) c).isEmpty()) {
                Collection<?> cc = (Collection<?>) c;
                clear();
                return true;
            }
            Predicate<E> predicate;
            if (c instanceof Collection<?>) {
                Collection<?> that = (Collection<?>) c;
                predicate = that::contains;
            } else {
                HashSet<Object> that = new HashSet<>();
                c.forEach(that::add);
                predicate = that::contains;
            }
            boolean removed = false;
            for (Iterator<E> i = iterator(); i.hasNext(); ) {
                E e = i.next();
                if (!predicate.test(e)) {
                    remove(e);
                    removed = true;
                }
            }
            return removed;
        }
    }
}
