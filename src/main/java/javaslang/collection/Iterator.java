/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.FilterMonadic;
import javaslang.Kind;
import javaslang.Kind.IterableKind;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * {@code javaslang.collection.Iterator} is a powerful replacement for {@code java.util.Iterator}.
 * Javaslang's {@code Iterator} extends Java's, so it integrates seemlessly in existing code.
 * Both are data structures whose purpose is to iterate <em>once</em> over a sequence of elements.
 * <p>
 * <strong>Note:</strong> Iterators encapsulate mutable state.
 * They are not meant to used concurrently by differnet threads.
 * <p>
 * There are two abstract methods: {@code hasNext} for checking if there is a next element available,
 * and {@code next} which removes the next element from the iterator and returns it. They can be called
 * an arbitrary amount of times. If {@code hasNext} returns false, a call of {@code next} will throw
 * a {@code NoSuchElementException}.
 * <p>
 * <strong>Caution:</strong> Other methods than {@code hasNext} and {@code next} can be called only once (exclusively).
 * More specifically, after calling a method it cannot be guaranteed that the next call will succeed.
 *
 * An Iterator that can be only used once because it is a traversal pointer into a collection, and not a collection
 * itself.
 *
 * @param <T> Component type
 * @since 2.0.0
 */
public interface Iterator<T> extends java.util.Iterator<T>, TraversableOnce<T>, FilterMonadic<IterableKind<?>, T> {

    /**
     * The empty Iterator.
     */
    Iterator<Object> EMPTY = new Impl<Object>() {

        @Override
        public boolean hsNext() {
            return false;
        }

        @Override
        public Object getNext() {
            throw new NoSuchElementException();
        }
    };

    /**
     * Returns the empty Iterator.
     *
     * @param <T> Component type
     * @return The empty Iterator
     */
    @SuppressWarnings("unchecked")
    static <T> Iterator<T> empty() {
        return (Iterator<T>) EMPTY;
    }

    /**
     * Creates an Iterator which traverses one element.
     *
     * @param element An element
     * @param <T>     Component type.
     * @return A new Iterator
     */
    static <T> Iterator<T> of(T element) {
        return new Impl<T>() {

            boolean hasNext = true;

            @Override
            public boolean hsNext() {
                return hasNext;
            }

            @Override
            public T getNext() {
                if (!hasNext) {
                    throw new NoSuchElementException();
                }
                hasNext = false;
                return element;
            }
        };
    }

    /**
     * Creates an Iterator which traverses the given elements.
     *
     * @param elements Zero or more elements
     * @param <T>      Component type
     * @return A new Iterator
     */
    @SafeVarargs
    static <T> Iterator<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements.isNull");
        return new Impl<T>() {

            int index = 0;

            @Override
            public boolean hsNext() {
                return index < elements.length;
            }

            @Override
            public T getNext() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elements[index++];
            }
        };
    }

    /**
     * Creates a FilterMonadic Iterator based on the given Iterable. This is a convenience method for
     * {@code Iterator.of(iterable.iterator()}.
     *
     * @param iterable An Iterable
     * @param <T>      Component type.
     * @return A new {@code javaslang.collection.Iterator}
     */
    static <T> Iterator<T> ofAll(Iterable<? extends T> iterable) {
        Objects.requireNonNull(iterable, "iterator is null");
        return Iterator.ofAll(iterable.iterator());
    }

    /**
     * Creates a FilterMonadic Iterator based on the given Iterator by
     * delegating calls of {@code hasNext()} and {@code next()} to it.
     *
     * @param iterator A {@link java.util.Iterator}
     * @param <T>      Component type.
     * @return A new {@code javaslang.collection.Iterator}
     */
    static <T> Iterator<T> ofAll(java.util.Iterator<? extends T> iterator) {
        Objects.requireNonNull(iterator, "iterator is null");
        return new Impl<T>() {

            @Override
            public boolean hsNext() {
                return iterator.hasNext();
            }

            @Override
            public T getNext() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return iterator.next();
            }
        };
    }

    @Override
    Iterator<T> peek(Consumer<? super T> action);

    @Override
    default Iterator<T> iterator() {
        return this;
    }

    @Override
    default boolean isEmpty() {
        return !hasNext();
    }

    /**
     * Removes up to n elements from this iterator.
     *
     * @param n A number
     * @return The empty iterator, if {@code n <= 0} or this is empty, otherwise a new iterator without the first n elements.
     */
    default Iterator<T> drop(int n) {
        if (n <= 0) {
            return this;
        } else if (!hasNext()) {
            return Iterator.empty();
        } else {
            final Iterator<T> that = this;
            return new Impl<T>() {

                int count = n;

                @Override
                public boolean hsNext() {
                    while (count > 0 && that.hasNext()) {
                        that.next(); // discarded
                        count--;
                    }
                    return that.hasNext();
                }

                @Override
                public T getNext() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return that.next();
                }
            };
        }
    }

    /**
     * Take the first n elements from this iterator.
     *
     * @param n A number
     * @return The empty iterator, if {@code n <= 0} or this is empty, otherwise a new iterator without the first n elements.
     */
    default Iterator<T> take(int n) {
        if (n <= 0 || !hasNext()) {
            return Iterator.empty();
        } else {
            final Iterator<T> that = this;
            return new Impl<T>() {

                int count = n;

                @Override
                public boolean hsNext() {
                    return count > 0 && that.hasNext();
                }

                @Override
                public T getNext() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    count--;
                    return that.next();
                }
            };
        }
    }

    /**
     * Returns an Iterator that contains elements that satisfy the given {@code predicate}.
     *
     * @param predicate A predicate
     * @return A new Iterator
     */
    @Override
    default Iterator<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (!hasNext()) {
            return Iterator.empty();
        } else {
            final Iterator<T> that = this;
            return new Impl<T>() {

                Option<T> next = None.instance();

                @Override
                public boolean hsNext() {
                    while (next.isEmpty() && that.hasNext()) {
                        final T candidate = that.next();
                        if (predicate.test(candidate)) {
                            next = new Some<>(candidate);
                        }
                    }
                    return next.isDefined();
                }

                @Override
                public T getNext() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    T result = next.get();
                    next = None.instance();
                    return result;
                }
            };
        }
    }

    /**
     * Creates an Iterator equivalent to {@code filter(predicate).map(Some::new)}.
     *
     * @param predicate A predicate
     * @return A new Iterator
     */
    @Override
    default Iterator<Some<T>> filterOption(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (!hasNext()) {
            return Iterator.empty();
        } else {
            return filter(predicate).map(Some::new);
        }
    }

    /**
     * FlatMaps the elements of this Iterator to Iterables, which are iterated in the order of occurrence.
     *
     * @param mapper A mapper
     * @param <U>    Component type
     * @return A new Iterable
     */
    default <U> Iterator<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (!hasNext()) {
            return Iterator.empty();
        } else {
            final Iterator<T> that = this;
            return new Impl<U>() {

                final Iterator<? extends T> inputs = that;
                java.util.Iterator<? extends U> current = Collections.emptyIterator();

                @Override
                public boolean hsNext() {
                    boolean currentHasNext;
                    while (!(currentHasNext = current.hasNext()) && inputs.hasNext()) {
                        current = mapper.apply(inputs.next()).iterator();
                    }
                    return currentHasNext;
                }

                @Override
                public U getNext() {
                    return current.next();
                }
            };
        }
    }

    /**
     * FlatMaps the elements of this Iterable by effectively calling
     * <pre><code>flatMap((Function&lt;? super T, ? extends Iterator&lt;? extends U&gt;&gt;) mapper)</code></pre>
     *
     * @param mapper A mapper.
     * @param <U>    Component type
     * @return A new Iterable
     */
    @SuppressWarnings("unchecked")
    @Override
    default <U> Iterator<U> flatMapM(Function<? super T, ? extends Kind<? extends IterableKind<?>, ? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (!hasNext()) {
            return Iterator.empty();
        } else {
            return flatMap((Function<? super T, ? extends Iterable<? extends U>>) mapper);
        }
    }

    /**
     * Flattens the elements of this Iterator.
     *
     * @return A flattened Iterator
     */
    @Override
    default Iterator<Object> flatten() {
        if (!hasNext()) {
            return Iterator.empty();
        } else {
            return flatMap(t -> () -> (t instanceof Iterable) ? ofAll((Iterable<?>) t).flatten() : of(t));
        }
    }

    /**
     * Maps the elements of this Iterator lazily using the given {@code mapper}.
     *
     * @param mapper A mapper.
     * @param <U>    Component type
     * @return A new Iterator
     */
    @Override
    default <U> Iterator<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (!hasNext()) {
            return Iterator.empty();
        } else {
            final Iterator<T> that = this;
            return new Impl<U>() {

                @Override
                public boolean hsNext() {
                    return that.hasNext();
                }

                @Override
                public U getNext() {
                    if (!that.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return mapper.apply(that.next());
                }
            };
        }
    }

    abstract class Impl<T> implements Iterator<T> {

        private T next;

        public abstract T getNext();

        public abstract boolean hsNext();

        @Override
        public boolean hasNext() {
            return next != null || hsNext();
        }

        @Override
        public T next() {
            if (next == null && !hsNext()) {
                throw new NoSuchElementException();
            }
            if(next != null) {
                T result = next;
                next = null;
                return result;
            } else {
                return getNext();
            }
        }

        @Override
        public Iterator<T> peek(Consumer<? super T> action) {
            if(next != null) {
                action.accept(next);
            } else {
                if (hsNext()) {
                    next = getNext();
                    action.accept(next);
                }
            }
            return this;
        }
    }
}
