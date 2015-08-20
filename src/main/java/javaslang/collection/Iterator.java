/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Kind;
import javaslang.Tuple2;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;

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
public interface Iterator<T> extends java.util.Iterator<T>, TraversableOnce<T> {

    /**
     * The empty Iterator.
     */
    Iterator<Object> EMPTY = new Iterator<Object>() {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
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
        return new Iterator<T>() {

            boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public T next() {
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
        return new Iterator<T>() {

            int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elements[index++];
            }
        };
    }

    /**
     * Creates an Iterator which traverses along all given iterators.
     *
     * @param iterators The list of iterators
     * @param <T>       Component type.
     * @return A new {@code javaslang.collection.Iterator}
     */
    @SafeVarargs
    @SuppressWarnings({ "unchecked", "varargs" })
    static <T> Iterator<T> ofIterators(Iterator<? extends T>... iterators) {
        Objects.requireNonNull(iterators, "iterators is null");
        return iterators.length == 0 ? Iterator.empty() : new ConcatIterator<>(Stream.of(iterators).iterator());
    }

    /**
     * Creates an Iterator which traverses along all given iterables.
     *
     * @param iterables The list of iterables
     * @param <T>       Component type.
     * @return A new {@code javaslang.collection.Iterator}
     */
    @SafeVarargs
    @SuppressWarnings({ "unchecked", "varargs" })
    static <T> Iterator<T> ofIterables(Iterable<? extends T>... iterables) {
        Objects.requireNonNull(iterables, "iterables is null");
        return iterables.length == 0 ? Iterator.empty() : new ConcatIterator<>(Stream.of(iterables).map(Iterator::ofAll).iterator());
    }

    /**
     * Creates an Iterator which traverses along all given iterators.
     *
     * @param iterators The iterator over iterators
     * @param <T>       Component type.
     * @return A new {@code javaslang.collection.Iterator}
     */
    static <T> Iterator<T> ofIterators(Iterator<? extends Iterator<? extends T>> iterators) {
        Objects.requireNonNull(iterators, "iterators is null");
        return iterators.isEmpty() ? Iterator.empty() : new ConcatIterator<>(Stream.ofAll(iterators).iterator());
    }

    /**
     * Creates an Iterator which traverses along all given iterables.
     *
     * @param iterables The iterator over iterables
     * @param <T>       Component type.
     * @return A new {@code javaslang.collection.Iterator}
     */
    static <T> Iterator<T> ofIterables(Iterator<? extends Iterable<? extends T>> iterables) {
        Objects.requireNonNull(iterables, "iterables is null");
        return iterables.isEmpty() ? Iterator.empty() : new ConcatIterator<>(Stream.ofAll(iterables).map(Iterator::ofAll).iterator());
    }

    /**
     * Creates an Iterator which traverses along all given iterators.
     *
     * @param iterators The iterable of iterators
     * @param <T>       Component type.
     * @return A new {@code javaslang.collection.Iterator}
     */
    static <T> Iterator<T> ofIterators(Iterable<? extends Iterator<? extends T>> iterators) {
        Objects.requireNonNull(iterators, "iterators is null");
        if (!iterators.iterator().hasNext()) {
            return Iterator.empty();
        }
        return new ConcatIterator<>(Stream.ofAll(iterators).iterator());
    }

    /**
     * Creates an Iterator which traverses along all given iterables.
     *
     * @param iterables The iterable of iterables
     * @param <T>       Component type.
     * @return A new {@code javaslang.collection.Iterator}
     */
    static <T> Iterator<T> ofIterables(Iterable<? extends Iterable<? extends T>> iterables) {
        Objects.requireNonNull(iterables, "iterables is null");
        if (!iterables.iterator().hasNext()) {
            return Iterator.empty();
        }
        return new ConcatIterator<>(Stream.ofAll(iterables).map(Iterator::ofAll).iterator());
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
        Objects.requireNonNull(iterable, "iterable is null");
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
        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return iterator.next();
            }
        };
    }

    @Override
    default TraversableOnce<T> clear() {
        return null;
    }

    @Override
    default TraversableOnce<T> distinct() {
        return null;
    }

    @Override
    default TraversableOnce<T> distinctBy(Comparator<? super T> comparator) {
        return null;
    }

    @Override
    default <U> TraversableOnce<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        return null;
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
            return new Iterator<T>() {

                int count = n;

                @Override
                public boolean hasNext() {
                    while (count > 0 && that.hasNext()) {
                        that.next(); // discarded
                        count--;
                    }
                    return that.hasNext();
                }

                @Override
                public T next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return that.next();
                }
            };
        }
    }

    @Override
    default TraversableOnce<T> dropRight(int n) {
        return null;
    }

    @Override
    default TraversableOnce<T> dropWhile(Predicate<? super T> predicate) {
        return null;
    }

    default boolean equals(Iterator<? extends T> that) {
        while (this.hasNext() && that.hasNext()) {
            if (!Objects.equals(this.next(), that.next())) {
                return false;
            }
        }
        return this.hasNext() == that.hasNext();
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
            return new Iterator<T>() {

                Option<T> next = None.instance();

                @Override
                public boolean hasNext() {
                    while (next.isEmpty() && that.hasNext()) {
                        final T candidate = that.next();
                        if (predicate.test(candidate)) {
                            next = new Some<>(candidate);
                        }
                    }
                    return next.isDefined();
                }

                @Override
                public T next() {
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

    @Override
    default TraversableOnce<T> findAll(Predicate<? super T> predicate) {
        return null;
    }

    @Override
    default Option<T> findLast(Predicate<? super T> predicate) {
        return null;
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
            return new Iterator<U>() {

                final Iterator<? extends T> inputs = that;
                java.util.Iterator<? extends U> current = Collections.emptyIterator();

                @Override
                public boolean hasNext() {
                    boolean currentHasNext;
                    while (!(currentHasNext = current.hasNext()) && inputs.hasNext()) {
                        current = mapper.apply(inputs.next()).iterator();
                    }
                    return currentHasNext;
                }

                @Override
                public U next() {
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

    @Override
    default <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        return null;
    }

    @Override
    default T get() {
        return head();
    }

    @Override
    default <C> Map<C, ? extends TraversableOnce<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return null;
    }

    default T head() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return next();
    }

    default Option<T> headOption() {
        return hasNext() ? new Some<>(next()) : None.instance();
    }

    @Override
    default TraversableOnce<T> init() {
        return null;
    }

    @Override
    default Option<? extends TraversableOnce<T>> initOption() {
        return null;
    }

    /**
     * Inserts an element between all elements of this Iterator.
     *
     * @param element An element.
     * @return an interspersed version of this
     */
    default Iterator<T> intersperse(T element) {
        if (!hasNext()) {
            return Iterator.empty();
        } else {
            final Iterator<T> that = this;
            return new Iterator<T>() {

                boolean insertElement = false;

                @Override
                public boolean hasNext() {
                    return that.hasNext();
                }

                @Override
                public T next() {
                    if (!that.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    if (insertElement) {
                        insertElement = false;
                        return element;
                    } else {
                        insertElement = true;
                        return that.next();
                    }
                }
            };
        }
    }

    @Override
    default boolean isEmpty() {
        return !hasNext();
    }

    @Override
    default Iterator<T> iterator() {
        return this;
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
            return new Iterator<U>() {

                @Override
                public boolean hasNext() {
                    return that.hasNext();
                }

                @Override
                public U next() {
                    if (!that.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return mapper.apply(that.next());
                }
            };
        }
    }

    @Override
    default Tuple2<? extends TraversableOnce<T>, ? extends TraversableOnce<T>> partition(Predicate<? super T> predicate) {
        return null;
    }

    @Override
    default Iterator<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!hasNext()) {
            return Iterator.empty();
        } else {
            final Iterator<T> that = this;
            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return that.hasNext();
                }

                @Override
                public T next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final T next = that.next();
                    action.accept(next);
                    return next;
                }
            };
        }
    }

    @Override
    default T reduceRight(BiFunction<? super T, ? super T, ? extends T> op) {
        return null;
    }

    @Override
    default TraversableOnce<T> replace(T currentElement, T newElement) {
        return null;
    }

    @Override
    default TraversableOnce<T> replaceAll(T currentElement, T newElement) {
        return null;
    }

    @Override
    default TraversableOnce<T> replaceAll(UnaryOperator<T> operator) {
        return null;
    }

    @Override
    default TraversableOnce<T> retainAll(Iterable<? extends T> elements) {
        return null;
    }

    @Override
    default TraversableOnce<? extends TraversableOnce<T>> sliding(int size) {
        return null;
    }

    @Override
    default TraversableOnce<? extends TraversableOnce<T>> sliding(int size, int step) {
        return null;
    }

    @Override
    default Tuple2<? extends TraversableOnce<T>, ? extends TraversableOnce<T>> span(Predicate<? super T> predicate) {
        return null;
    }

    default Iterator<T> tail() {
        if (!hasNext()) {
            throw new UnsupportedOperationException();
        } else {
            next(); // remove first element
            return this;
        }
    }

    @Override
    default Option<? extends TraversableOnce<T>> tailOption() {
        return null;
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
            return new Iterator<T>() {

                int count = n;

                @Override
                public boolean hasNext() {
                    return count > 0 && that.hasNext();
                }

                @Override
                public T next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    count--;
                    return that.next();
                }
            };
        }
    }

    @Override
    default TraversableOnce<T> takeRight(int n) {
        return null;
    }

    @Override
    default TraversableOnce<T> takeWhile(Predicate<? super T> predicate) {
        return null;
    }

    class ConcatIterator<T> implements Iterator<T> {

        private final Iterator<? extends Iterator<? extends T>> iterators;
        private Iterator<? extends T> current;

        private ConcatIterator(Iterator<? extends Iterator<? extends T>> iterators) {
            this.current = Iterator.empty();
            this.iterators = iterators;
        }

        @Override
        public boolean hasNext() {
            while (!current.hasNext() && !iterators.isEmpty()) {
                current = iterators.next();
            }
            return current.hasNext();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return current.next();
        }
    }
}
