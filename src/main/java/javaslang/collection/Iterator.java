/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Value;
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
            throw new NoSuchElementException("next() on empty iterator");
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
                    EMPTY.next();
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
                    EMPTY.next();
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
        return iterators.length == 0 ? empty() : new ConcatIterator<>(Stream.of(iterators).iterator());
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
        return iterables.length == 0 ? empty() : new ConcatIterator<>(Stream.of(iterables).map(Iterator::ofAll).iterator());
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
        return iterators.isEmpty() ? empty() : new ConcatIterator<>(Stream.ofAll(iterators).iterator());
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
        return iterables.isEmpty() ? empty() : new ConcatIterator<>(Stream.ofAll(iterables).map(Iterator::ofAll).iterator());
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
            return empty();
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
            return empty();
        }
        return new ConcatIterator<>(Stream.ofAll(iterables).map(Iterator::ofAll).iterator());
    }

    /**
     * Creates an Iterator based on the given Iterable. This is a convenience method for
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
     * Creates a an Iterator based on the given Iterator by
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
                    EMPTY.next();
                }
                return iterator.next();
            }
        };
    }

    @Override
    default Iterator<T> clear() {
        return empty();
    }

    @Override
    default Iterator<T> distinct() {
        if (!hasNext()) {
            return empty();
        } else {
            return new DistinctIterator<>(this, HashSet.empty(), Function.identity());
        }
    }

    @Override
    default Iterator<T> distinctBy(Comparator<? super T> comparator) {
        if (!hasNext()) {
            return empty();
        } else {
            return new DistinctIterator<>(this, TreeSet.empty(comparator), Function.identity());
        }
    }

    @Override
    default <U> Iterator<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        if (!hasNext()) {
            return empty();
        } else {
            return new DistinctIterator<>(this, HashSet.empty(), keyExtractor);
        }
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
            return empty();
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
                        EMPTY.next();
                    }
                    return that.next();
                }
            };
        }
    }

    @Override
    default Iterator<T> dropRight(int n) {
        return null;
    }

    @Override
    default Iterator<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new Iterator<T>() {

                private T next = null;

                @Override
                public boolean hasNext() {
                    while (next == null && that.hasNext()) {
                        final T value = that.next();
                        if(!predicate.test(value)) {
                            next = value;
                        }
                    }
                    return next != null;
                }

                @Override
                public T next() {
                    if (!hasNext()) {
                        EMPTY.next();
                    }
                    final T result = next;
                    next = null;
                    return result;
                }
            };
        }
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
            return empty();
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
                        EMPTY.next();
                    }
                    T result = next.get();
                    next = None.instance();
                    return result;
                }
            };
        }
    }

    @Override
    default Iterator<T> findAll(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate);
    }

    @Override
    default Option<T> findLast(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
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
            return empty();
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
    @Override
    default <U> Iterator<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper) {
        return flatMap(mapper);
    }

    /**
     * Flattens the elements of this Iterator.
     *
     * @return A flattened Iterator
     */
    @Override
    default Iterator<Object> flatten() {
        if (!hasNext()) {
            return empty();
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
    default <C> Map<C, Iterator<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return null;
    }

    default T head() {
        if (!hasNext()) {
            EMPTY.next();
        }
        return next();
    }

    default Option<T> headOption() {
        return hasNext() ? new Some<>(next()) : None.instance();
    }

    @Override
    default Iterator<T> init() {
        return null;
    }

    @Override
    default Option<Iterator<T>> initOption() {
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
            return empty();
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
                        EMPTY.next();
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

    @Override
    default int length() {
        return foldLeft(0, (n, ignored) -> n + 1);
    }

    /**
     * Maps the elements of this Iterator lazily using the given {@code mapper}.
     *
     * @param mapper A mapper.
     * @param <U>    Component type
     * @return A new Iterator
     */
    default <U> Iterator<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (!hasNext()) {
            return empty();
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
                        EMPTY.next();
                    }
                    return mapper.apply(that.next());
                }
            };
        }
    }

    @Override
    default Tuple2<Iterator<T>, Iterator<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return null;
    }

    @Override
    default Iterator<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!hasNext()) {
            return empty();
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
                        EMPTY.next();
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
    default Iterator<T> replace(T currentElement, T newElement) {
        return null;
    }

    @Override
    default Iterator<T> replaceAll(T currentElement, T newElement) {
        return null;
    }

    @Override
    default Iterator<T> replaceAll(UnaryOperator<T> operator) {
        return null;
    }

    @Override
    default Iterator<T> retainAll(Iterable<? extends T> elements) {
        return null;
    }

    @Override
    default Iterator<Iterator<T>> sliding(int size) {
        return null;
    }

    @Override
    default Iterator<Iterator<T>> sliding(int size, int step) {
        return null;
    }

    @Override
    default Tuple2<Iterator<T>, Iterator<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
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
    default Option<Iterator<T>> tailOption() {
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
            return empty();
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
                        EMPTY.next();
                    }
                    count--;
                    return that.next();
                }
            };
        }
    }

    @Override
    default Iterator<T> takeRight(int n) {
        return null;
    }

    @Override
    default Iterator<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return null;
    }

    default <U> Iterator<Tuple2<T, U>> zip(Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        if(isEmpty()) {
            return empty();
        } else {
            final Iterator<T> it1 = this;
            final java.util.Iterator<U> it2 = that.iterator();
            return new Iterator<Tuple2<T, U>>() {
                @Override
                public boolean hasNext() {
                    return it1.hasNext() && it2.hasNext();
                }

                @Override
                public Tuple2<T, U> next() {
                    if (!hasNext()) {
                        EMPTY.next();
                    }
                    return Tuple.of(it1.next(), it2.next());
                }
            };
        }
    }

    default <U> Iterator<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        if(isEmpty()) {
            return empty();
        } else {
            final Iterator<T> it1 = this;
            final java.util.Iterator<U> it2 = that.iterator();
            return new Iterator<Tuple2<T, U>>() {
                @Override
                public boolean hasNext() {
                    return it1.hasNext() || it2.hasNext();
                }

                @Override
                public Tuple2<T, U> next() {
                    if (!hasNext()) {
                        EMPTY.next();
                    }
                    T v1 = it1.hasNext() ? it1.next() : thisElem;
                    U v2 = it2.hasNext() ? it2.next() : thatElem;
                    return Tuple.of(v1, v2);
                }
            };
        }
    }

    default Iterator<Tuple2<T, Integer>> zipWithIndex() {
        if(isEmpty()) {
            return empty();
        } else {
            final Iterator<T> it1 = this;
            return new Iterator<Tuple2<T, Integer>>() {
                private int index = 0;
                @Override
                public boolean hasNext() {
                    return it1.hasNext();
                }

                @Override
                public Tuple2<T, Integer> next() {
                    if (!hasNext()) {
                        EMPTY.next();
                    }
                    return Tuple.of(it1.next(), index++);
                }
            };
        }
    }

    default <T1, T2> Tuple2<Iterator<T1>, Iterator<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        final Stream<Tuple2<? extends T1, ? extends T2>> source = Stream.ofAll(this.map(unzipper::apply));
        return Tuple.of(source.map(t -> (T1) t._1).iterator(), source.map(t -> (T2) t._2).iterator());
    }

    class ConcatIterator<T> implements Iterator<T> {

        private final Iterator<? extends Iterator<? extends T>> iterators;
        private Iterator<? extends T> current;

        private ConcatIterator(Iterator<? extends Iterator<? extends T>> iterators) {
            this.current = empty();
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
                EMPTY.next();
            }
            return current.next();
        }
    }

    class DistinctIterator<T, U> implements Iterator<T> {

        private final Iterator<? extends T> that;
        Set<U> known;
        Function<? super T, ? extends U> keyExtractor;
        T next = null;

        private DistinctIterator(Iterator<? extends T> that, Set<U> set, Function<? super T, ? extends U> keyExtractor) {
            this.that = that;
            this.known = set;
            this.keyExtractor = keyExtractor;
        }

        @Override
        public boolean hasNext() {
            while (next == null && that.hasNext()) {
                T elem = that.next();
                U key = keyExtractor.apply(elem);
                if (!known.contains(key)) {
                    known = known.add(key);
                    next = elem;
                }
            }
            return next != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                EMPTY.next();
            }
            final T result = next;
            next = null;
            return result;
        }
    }
}
