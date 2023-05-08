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

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Predicate;

import static io.vavr.collection.ChampSequencedData.vecRemove;

/**
 * Supports efficient bulk-operations on a linked hash set through transience.
 *
 * @param <E>the element type
 */
class TransientLinkedHashSet<E> extends ChampAbstractTransientSet<E,ChampSequencedElement<E>> {
    int offset;
    Vector<Object> vector;

    TransientLinkedHashSet(LinkedHashSet<E> s) {
        root = s;
        size = s.size;
        this.vector = s.vector;
        this.offset = s.offset;
    }

    TransientLinkedHashSet() {
        this(LinkedHashSet.empty());
    }

    @Override
    void clear() {
        root = ChampBitmapIndexedNode.emptyNode();
        vector = Vector.empty();
        size = 0;
        modCount++;
        offset = -1;
    }



    public LinkedHashSet<E> toImmutable() {
        owner = null;
        return isEmpty()
                ? LinkedHashSet.empty()
                : root instanceof LinkedHashSet<E> h ? h : new LinkedHashSet<>(root, vector, size, offset);
    }

    boolean add(E element) {
        return addLast(element, false);
    }

    private boolean addLast(E e, boolean moveToLast) {
        var details = new ChampChangeEvent<ChampSequencedElement<E>>();
        var newElem = new ChampSequencedElement<E>(e, vector.size() - offset);
        root = root.put(makeOwner(), newElem,
                Objects.hashCode(e), 0, details,
                moveToLast ? ChampSequencedElement::updateAndMoveToLast : ChampSequencedElement::update,
                Objects::equals, Objects::hashCode);
        if (details.isModified()) {

            if (details.isReplaced()) {
                if (moveToLast) {
                    var oldElem = details.getOldData();
                    var result = vecRemove(vector,  oldElem,  offset);
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
    boolean addAll(Iterable<? extends E> c) {
        if (c == root) {
            return false;
        }
        if (isEmpty() && (c instanceof LinkedHashSet<?> cc)) {
            root = (ChampBitmapIndexedNode<ChampSequencedElement<E>>)(ChampBitmapIndexedNode<?>)  cc;
            size = cc.size;
            return true;
        }
        boolean modified = false;
        for (E e : c) {
            modified |= add(e);
        }
        return modified;
    }
    @Override
     Iterator<E> iterator() {
        return new ChampIteratorFacade<>(spliterator());
    }
    @SuppressWarnings("unchecked")
    Spliterator<E> spliterator() {
        return new ChampVectorSpliterator<>(vector,
                (Object o) -> ((ChampSequencedElement<E>) o).getElement(),0,
                size(), Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.ORDERED);
    }
    @SuppressWarnings("unchecked")
    @Override
    boolean remove(Object element) {
        int keyHash = Objects.hashCode(element);
        var details = new ChampChangeEvent<ChampSequencedElement<E>>();
        root = root.remove(makeOwner(),
                new ChampSequencedElement<>((E)element),
                keyHash, 0, details, Objects::equals);
        if (details.isModified()) {
            var removedElem = details.getOldDataNonNull();
            var result = vecRemove(vector,  removedElem,  offset);
            vector=result._1;
            offset=result._2;
            size--;
            renumber();
            return true;
        }
        return false;
    }



    private void renumber() {
        if (ChampSequencedData.vecMustRenumber(size, offset, vector.size())) {
            var owner = new ChampIdentityObject();
            var result = ChampSequencedData.<ChampSequencedElement<E>>vecRenumber(
                    size, root, vector, owner, Objects::hashCode, Objects::equals,
                    (e, seq) -> new ChampSequencedElement<>(e.getElement(), seq));
            root = result._1;
            vector = result._2;
            offset = 0;
        }
    }

    boolean filterAll(Predicate<? super E> predicate) {
        class VectorPredicate implements Predicate<ChampSequencedElement<E>> {
            Vector<Object> newVector = vector;
            int newOffset = offset;

            @Override
            public boolean test(ChampSequencedElement<E> e) {
                if (!predicate.test(e.getElement())) {
                    Tuple2<Vector<Object>, Integer> result = vecRemove(newVector, e, newOffset);
                    newVector = result._1;
                    newOffset = result._2;
                    return false;
                }
                return true;
            }
        }
        VectorPredicate vp = new VectorPredicate();
       ChampBulkChangeEvent bulkChange = new ChampBulkChangeEvent();
        ChampBitmapIndexedNode<ChampSequencedElement<E>> newRootNode = root.filterAll(makeOwner(), vp, 0, bulkChange);
        if (bulkChange.removed == 0) {
            return false;
        }
        root = newRootNode;
        vector = vp.newVector;
        offset = vp.newOffset;
        size -= bulkChange.removed;
        modCount++;
        return true;
    }
}
