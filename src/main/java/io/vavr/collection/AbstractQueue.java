/* ____  ______________  ________________________  __________
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

import io.vavr.*;
import io.vavr.control.Option;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

abstract class AbstractQueue<T, Q extends AbstractQueue<T, Q>> implements Traversable<T> {

    /**
     * Removes an element from this Queue.
     *
     * @return a tuple containing the first element and the remaining elements of this Queue
     * @throws NoSuchElementException if this Queue is empty
     */
    public Tuple2<T, Q> dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("dequeue of empty " + getClass().getSimpleName());
        } else {
            return Tuple.of(head(), tail());
        }
    }

    /**
     * Removes an element from this Queue.
     *
     * @return {@code None} if this Queue is empty, otherwise {@code Some} {@code Tuple} containing the first element and the remaining elements of this Queue
     */
    public Option<Tuple2<T, Q>> dequeueOption() {
        return isEmpty() ? Option.none() : Option.some(dequeue());
    }

    /**
     * Enqueues a new element.
     *
     * @param element The new element
     * @return a new {@code Queue} instance, containing the new element
     */
    public abstract Q enqueue(T element);

    /**
     * Enqueues the given elements. A queue has FIFO order, i.e. the first of the given elements is
     * the first which will be retrieved.
     *
     * @param elements Elements, may be empty
     * @return a new {@code Queue} instance, containing the new elements
     * @throws NullPointerException if elements is null
     */
    @SuppressWarnings("unchecked")
    public Q enqueue(T... elements) {
        return enqueueAll(List.of(elements));
    }

    /**
     * Enqueues the given elements. A queue has FIFO order, i.e. the first of the given elements is
     * the first which will be retrieved.
     *
     * @param elements An Iterable of elements, may be empty
     * @return a new {@code Queue} instance, containing the new elements
     * @throws NullPointerException if elements is null
     */
    public abstract Q enqueueAll(Iterable<? extends T> elements);

    /**
     * Returns the first element without modifying it.
     *
     * @return the first element
     * @throws NoSuchElementException if this Queue is empty
     */
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("peek of empty " + getClass().getSimpleName());
        } else {
            return head();
        }
    }

    /**
     * Returns the first element without modifying the Queue.
     *
     * @return {@code None} if this Queue is empty, otherwise a {@code Some} containing the first element
     */
    public Option<T> peekOption() {
        return isEmpty() ? Option.none() : Option.some(peek());
    }

    @Override
    public Q dropUntil(Predicate<? super T> predicate) {
        return dropWhile(predicate.negate());
    }

    @Override
    public abstract Q dropWhile(Predicate<? super T> predicate);

    @SuppressWarnings("unchecked")
    @Override
    public Q filterNot(Predicate<? super T> predicate) {
        return Collections.filterNot((Q) this, predicate);
    }

    /**
     * Dual of {@linkplain #tail()}, returning all elements except the last.
     *
     * @return a new instance containing all elements except the last.
     * @throws UnsupportedOperationException if this is empty
     */
    @Override
    public abstract Q init();

    /**
     * Dual of {@linkplain #tailOption()}, returning all elements except the last as {@code Option}.
     *
     * @return {@code Some(Q)} or {@code None} if this is empty.
     */
    @Override
    public Option<Q> initOption() {
        return isEmpty() ? Option.none() : Option.some(init());
    }

    /**
     * Drops the first element of a non-empty Traversable.
     *
     * @return A new instance of Traversable containing all elements except the first.
     * @throws UnsupportedOperationException if this is empty
     */
    @Override
    public abstract Q tail();

    @Override
    public Option<Q> tailOption() {
        return isEmpty() ? Option.none() : Option.some(tail());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Q retainAll(Iterable<? extends T> elements) {
        return Collections.retainAll((Q) this, elements);
    }

    @SuppressWarnings("unchecked")
    public Q removeAll(Iterable<? extends T> elements) {
        return Collections.removeAll((Q) this, elements);
    }

    @Override
    public Q takeWhile(Predicate<? super T> predicate) {
        return takeUntil(predicate.negate());
    }

    @Override
    public abstract Q takeUntil(Predicate<? super T> predicate);

    @SuppressWarnings("unchecked")
    @Override
    public Q peek(Consumer<? super T> action) {
        if (!isEmpty()) {
            action.accept(head());
        }
        return (Q) this;
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

}
