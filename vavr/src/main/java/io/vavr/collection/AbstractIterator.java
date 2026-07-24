/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import java.util.NoSuchElementException;

/**
 * Provides a common {@link Object#toString()} implementation.
 * <p>
 * {@code equals(Object)} and {@code hashCode()} are intentionally not overridden in order to prevent this iterator
 * from being evaluated. In other words, (identity-)equals and hashCode are implemented by Object.
 *
 * @param <T> Component type
 * @author Daniel Dietrich
 */
abstract class AbstractIterator<T> implements Iterator<T> {

    /**
     * Peek buffer: when {@code hasPeeked} is true, {@code peeked} holds an element that
     * was read via {@link #getNext()} but not yet consumed by an external {@link #next()}
     * call. This allows {@link #head()} to be non-destructive.
     */
    private T peeked;
    private boolean hasPeeked = false;

    @Override
    public String toString() {
        return stringPrefix() + "(" + (isEmpty() ? "" : "?") + ")";
    }

    protected abstract T getNext();

    @Override
    public final T next() {
        if (hasPeeked) {
            T result = peeked;
            peeked = null;
            hasPeeked = false;
            return result;
        }
        if (!hasNext()) {
            throw new NoSuchElementException("next() on empty iterator");
        }
        return getNext();
    }

    /**
     * Returns the first element of this iterator without advancing it, so that a
     * subsequent call to {@link #tail()} correctly skips only the first element and
     * not the second.
     *
     * <p>Fixes the {@code Traversable} contract violation where:
     * <pre>{@code
     *   Iterator<Integer> it = Iterator.of(0, 1, 2, 3);
     *   it.head();              // must return 0
     *   it = it.tail();         // must position iterator at 1
     *   it.head();              // must return 1
     * }</pre>
     */
    @Override
    public T head() {
        if (!hasNext()) {
            throw new NoSuchElementException("head() on empty iterator");
        }
        if (!hasPeeked) {
            peeked = getNext();
            hasPeeked = true;
        }
        return peeked;
    }

    /**
     * Returns this iterator positioned after the first element. If {@link #head()} was
     * previously called (peeked), the peeked value is discarded; otherwise the first
     * element is consumed by calling {@link #next()}.
     */
    @Override
    public Iterator<T> tail() {
        if (!hasNext()) {
            throw new UnsupportedOperationException("tail() on empty iterator");
        }
        next(); // drains either the peek buffer or the real next element
        return this;
    }
}
