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

import java.util.Objects;

/**
 * Supports efficient bulk-operations on a set through transience.
 *
 * @param <T>the element type
 */
class TransientHashSet<T> extends ChampAbstractTransientCollection<T> {
    TransientHashSet(HashSet<T> s) {
        root = s;
        size = s.size;
    }

    TransientHashSet() {
        this(HashSet.empty());
    }

    public HashSet<T> toImmutable() {
        mutator = null;
        return isEmpty()
                ? HashSet.empty()
                : root instanceof HashSet<T> h ? h : new HashSet<>(root, size);
    }

    boolean add(T e) {
        ChampChangeEvent<T> details = new ChampChangeEvent<>();
        root = root.update(getOrCreateIdentity(),
                e, Objects.hashCode(e), 0, details,
                (oldKey, newKey) -> oldKey,
                Objects::equals, Objects::hashCode);
        if (details.isModified()) {
            size++;
            modCount++;
        }
        return details.isModified();
    }

    @SuppressWarnings("unchecked")
    boolean addAll(Iterable<? extends T> c) {
        if (c == root) {
            return false;
        }
        if (isEmpty() && (c instanceof HashSet<?> cc)) {
            root = (ChampBitmapIndexedNode<T>) cc;
            size = cc.size;
            return true;
        }
        boolean modified = false;
        for (T e : c) {
            modified |= add(e);
        }
        return modified;
    }

    boolean remove(T key) {
        int keyHash = Objects.hashCode(key);
        ChampChangeEvent<T> details = new ChampChangeEvent<>();
        root = root.remove(mutator, key, keyHash, 0, details, Objects::equals);
        if (details.isModified()) {
            size--;
            return true;
        }
        return false;
    }

    boolean removeAll(Iterable<? extends T> c) {
        if (isEmpty()||c == root) {
            return false;
        }
        boolean modified = false;
        for (T e : c) {
            modified |= remove(e);
        }
        return modified;
    }
}
