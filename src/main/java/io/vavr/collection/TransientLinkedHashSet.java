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
 * Supports efficient bulk-operations on a linked hash set through transience.
 *
 * @param <T>the element type
 */
class TransientLinkedHashSet<T> extends ChampAbstractTransientCollection<ChampSequencedElement<T>> {
    int offset;
    Vector<Object> vector;

    TransientLinkedHashSet(LinkedHashSet<T> s) {
        root = s;
        size = s.size;
        this.vector = s.vector;
        this.offset = s.offset;
    }

    TransientLinkedHashSet() {
        this(LinkedHashSet.empty());
    }

    public LinkedHashSet<T> toImmutable() {
        mutator = null;
        return isEmpty()
                ? LinkedHashSet.empty()
                : root instanceof LinkedHashSet<T> h ? h : new LinkedHashSet<>(root, vector, size, offset);
    }

    boolean add(T element) {
        return addLast(element, false);
    }

    private boolean addLast(T e, boolean moveToLast) {
        var details = new ChampChangeEvent<ChampSequencedElement<T>>();
        var newElem = new ChampSequencedElement<T>(e, vector.size() - offset);
        root = root.update(null, newElem,
                Objects.hashCode(e), 0, details,
                moveToLast ? ChampSequencedElement::updateAndMoveToLast : ChampSequencedElement::update,
                Objects::equals, Objects::hashCode);
        if (details.isModified()) {

            if (details.isReplaced()) {
                if (moveToLast) {
                    var oldElem = details.getOldData();
                    var result = ChampSequencedData.vecRemove(vector, new ChampIdentityObject(), oldElem, details, offset);
                    vector = result._1;
                    offset = result._2;
                }
            } else {
                size++;
            }
            vector = vector.append(newElem);
            renumber();
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    boolean addAll(Iterable<? extends T> c) {
        if (c == root) {
            return false;
        }
        if (isEmpty() && (c instanceof LinkedHashSet<?> cc)) {
            root = (ChampBitmapIndexedNode<ChampSequencedElement<T>>)(ChampBitmapIndexedNode<?>)  cc;
            size = cc.size;
            return true;
        }
        boolean modified = false;
        for (T e : c) {
            modified |= add(e);
        }
        return modified;
    }

    boolean remove(T element) {
        int keyHash = Objects.hashCode(element);
        var details = new ChampChangeEvent<ChampSequencedElement<T>>();
        root = root.remove(null,
                new ChampSequencedElement<>(element),
                keyHash, 0, details, Objects::equals);
        if (details.isModified()) {
            var removedElem = details.getOldDataNonNull();
            var result = ChampSequencedData.vecRemove(vector, null, removedElem, details, offset);
            vector=result._1;
            offset=result._2;
            size--;
            renumber();
            return true;
        }
        return false;
    }

    boolean removeAll(Iterable<? extends T> c) {
        if (isEmpty() || c == root) {
            return false;
        }
        boolean modified = false;
        for (T e : c) {
            modified |= remove(e);
        }
        return modified;
    }

    private void renumber() {

        if (ChampSequencedData.vecMustRenumber(size, offset, vector.size())) {
            var mutator = new ChampIdentityObject();
            var result = ChampSequencedData.<ChampSequencedElement<T>>vecRenumber(
                    size, root, vector, mutator, Objects::hashCode, Objects::equals,
                    (e, seq) -> new ChampSequencedElement<>(e.getElement(), seq));
            root = result._1;
            vector = result._2;
            offset = 0;
        }
    }
}
