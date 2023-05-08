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

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * Supports efficient bulk-operations on a set through transience.
 *
 * @param <E>the element type
 */
class TransientHashSet<E> extends ChampAbstractTransientSet<E, E> {
    TransientHashSet(HashSet<E> s) {
        root = s;
        size = s.size;
    }

    TransientHashSet() {
        this(HashSet.empty());
    }

    public HashSet<E> toImmutable() {
        owner = null;
        return isEmpty()
                ? HashSet.empty()
                : root instanceof HashSet<E> h ? h : new HashSet<>(root, size);
    }

    boolean add(E e) {
        ChampChangeEvent<E> details = new ChampChangeEvent<>();
        root = root.put(makeOwner(),
                e, HashSet.keyHash(e), 0, details,
                (oldKey, newKey) -> oldKey,
                Objects::equals, HashSet::keyHash);
        if (details.isModified()) {
            size++;
            modCount++;
        }
        return details.isModified();
    }

    @SuppressWarnings("unchecked")
    boolean addAll(Iterable<? extends E> c) {
        if (c == root) {
            return false;
        }
        if (isEmpty() && (c instanceof HashSet<?> cc)) {
            root = (ChampBitmapIndexedNode<E>) cc;
            size = cc.size;
            return true;
        }
        if (c instanceof HashSet<?> that) {
            var bulkChange = new ChampBulkChangeEvent();
            var newRootNode = root.putAll(makeOwner(), (ChampNode<E>) that, 0, bulkChange, HashSet::updateElement, Objects::equals, HashSet::keyHash, new ChampChangeEvent<>());
            if (bulkChange.inBoth == that.size()) {
                return false;
            }
            root = newRootNode;
            size += that.size - bulkChange.inBoth;
            modCount++;
            return true;
        }
        boolean added = false;
        for (E e : c) {
            added |= add(e);
        }
        return added;
    }

    @Override
    public Iterator<E> iterator() {
        return new ChampIteratorFacade<>(spliterator());
    }


    public Spliterator<E> spliterator() {
        return new ChampSpliterator<>(root, Function.identity(), Spliterator.DISTINCT | Spliterator.SIZED, size);
    }

    @SuppressWarnings("unchecked")
    @Override
    boolean remove(Object key) {
        int keyHash = HashSet.keyHash(key);
        ChampChangeEvent<E> details = new ChampChangeEvent<>();
        root = root.remove(owner, (E) key, keyHash, 0, details, Objects::equals);
        if (details.isModified()) {
            size--;
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    boolean removeAll(Iterable<?> c) {
        if (isEmpty()
                || (c instanceof Collection<?> cc) && cc.isEmpty()) {
            return false;
        }
        if (c instanceof HashSet<?> that) {
            ChampBulkChangeEvent bulkChange = new ChampBulkChangeEvent();
            ChampBitmapIndexedNode<E> newRootNode = root.removeAll(makeOwner(), (ChampBitmapIndexedNode<E>) that, 0, bulkChange, HashSet::updateElement, Objects::equals, HashSet::keyHash, new ChampChangeEvent<>());
            if (bulkChange.removed == 0) {
                return false;
            }
            root = newRootNode;
            size -= bulkChange.removed;
            modCount++;
            return true;
        }
        return super.removeAll(c);
    }

    void clear() {
        root = ChampBitmapIndexedNode.emptyNode();
        size = 0;
        modCount++;
    }

    @SuppressWarnings("unchecked")
    boolean retainAll(Iterable<?> c) {
        if (isEmpty()) {
            return false;
        }
        if ((c instanceof Collection<?> cc && cc.isEmpty())) {
            clear();
            return true;
        }
        ChampBulkChangeEvent bulkChange = new ChampBulkChangeEvent();
        ChampBitmapIndexedNode<E> newRootNode;
        if (c instanceof HashSet<?> that) {
            newRootNode = root.retainAll(makeOwner(), (ChampBitmapIndexedNode<E>) that, 0, bulkChange, HashSet::updateElement, Objects::equals, HashSet::keyHash, new ChampChangeEvent<>());
        } else if (c instanceof Collection<?> that) {
            newRootNode = root.filterAll(makeOwner(), that::contains, 0, bulkChange);
        } else {
            java.util.HashSet<Object> that = new java.util.HashSet<>();
            c.forEach(that::add);
            newRootNode = root.filterAll(makeOwner(), that::contains, 0, bulkChange);
        }
        if (bulkChange.removed == 0) {
            return false;
        }
        root = newRootNode;
        size -= bulkChange.removed;
        modCount++;
        return true;
    }

    public boolean filterAll(Predicate<? super E> predicate) {
        ChampBulkChangeEvent bulkChange = new ChampBulkChangeEvent();
        ChampBitmapIndexedNode<E> newRootNode
            = root.filterAll(makeOwner(),predicate,0,bulkChange);
        if (bulkChange.removed == 0) {
            return false;
        }
        root = newRootNode;
        size -= bulkChange.removed;
        modCount++;
        return true;

    }
}
