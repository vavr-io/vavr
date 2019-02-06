/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2019 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import io.vavr.control.Option;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

public interface Iterator<T> extends java.util.Iterator<T> {

    /**
     * Returns the singleton instance of the empty {@code Iterator}.
     * <p>
     * A call to {@link #hasNext()} will always return {@code false}.
     * A call to {@link #next()} will always throw a {@link NoSuchElementException}.
     *
     * @param <T> Element type
     * @return The empty {@code Iterator}
     */
    @SuppressWarnings("unchecked")
    static <T> Iterator<T> empty() {
        return (Iterator<T>) EmptyIterator.INSTANCE;
    }

    /**
     * Creates an {@code Iterator} which iterates over the given element.
     *
     * @param element An element
     * @param <T>     Element type
     * @return A new {@code Iterator}
     */
    static <T> Iterator<T> of(T element) {
        return new SingletonIterator<>(element);
    }

    /**
     * Creates an {@code Iterator} which iterates over the given elements.
     *
     * <pre><{@code
     * Iterator<Integer> iterator = Iterator.of(1, 2, 3);
     * }</pre>
     *
     * @param elements Zero or more elements
     * @param <T>      Element type
     * @return The singleton instance of the empty {@code Iterator}, if {@code elements.length == 0},
     *         otherwise a new {@code Iterator}.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    static <T> Iterator<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return (elements.length == 0) ? empty() : new ArrayIterator<>(elements);
    }

    /**
     * A safe alternative to {@link #next()} that is equivalent to
     *
     * <pre>{@code
     * hasNext() ? Option.some(next()) : Option.none()
     * }</pre>
     *
     * @return a new instance of {@link Option}
     */
    default Option<T> nextOption() {
        return hasNext() ? Option.some(next()) : Option.none();
    }

}

final class ArrayIterator<T> implements Iterator<T> {

    private final T[] elements;
    private int index = 0;

    ArrayIterator(T[] elements) {
        this.elements = elements;
    }

    @Override
    public boolean hasNext() {
        return index < elements.length;
    }

    @Override
    public T next() {
        try {
            return elements[index++];
        } catch(IndexOutOfBoundsException x) {
            index--;
            throw new NoSuchElementException();
        }
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        while (index < elements.length) {
            action.accept(elements[index++]);
        }
    }

    @Override
    public String toString() {
        return "ArrayIterator";
    }
}

final class EmptyIterator implements Iterator<Object> {

    static final EmptyIterator INSTANCE = new EmptyIterator();

    private EmptyIterator() {}

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        throw new NoSuchElementException();
    }

    @Override
    public void forEachRemaining(Consumer<? super Object> action) {
        Objects.requireNonNull(action);
    }

    @Override
    public String toString() {
        return "EmptyIterator";
    }
}

final class SingletonIterator<T> implements Iterator<T> {

    private final T element;
    private boolean hasNext = true;

    SingletonIterator(T element) {
        this.element = element;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        hasNext = false;
        return element;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        if (hasNext) {
            action.accept(element);
            hasNext = false;
        }
    }

    @Override
    public String toString() {
        return "SingletonIterator";
    }
}
