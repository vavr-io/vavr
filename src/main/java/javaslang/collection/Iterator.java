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

    // DEV-NOTE: we prefer returing empty() over this if !hasNext() == true in order to free memory.

    /**
     * The empty Iterator.
     */
    Iterator<Object> EMPTY = new AbstractIterator<Object>() {

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
        return new AbstractIterator<T>() {

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
        return new AbstractIterator<T>() {

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
    static <T> Iterator<T> ofIterables(java.lang.Iterable<? extends T>... iterables) {
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
    static <T> Iterator<T> ofIterables(Iterator<? extends java.lang.Iterable<? extends T>> iterables) {
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
    static <T> Iterator<T> ofIterators(java.lang.Iterable<? extends Iterator<? extends T>> iterators) {
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
    static <T> Iterator<T> ofIterables(java.lang.Iterable<? extends java.lang.Iterable<? extends T>> iterables) {
        Objects.requireNonNull(iterables, "iterables is null");
        if (!iterables.iterator().hasNext()) {
            return empty();
        }
        return new ConcatIterator<>(Stream.ofAll(iterables).map(Iterator::ofAll).iterator());
    }

    /**
     * Creates an Iterator based on the given java.lang.Iterable. This is a convenience method for
     * {@code Iterator.of(iterable.iterator()}.
     *
     * @param iterable A {@link java.lang.Iterable}
     * @param <T>      Component type.
     * @return A new {@code javaslang.collection.Iterator}
     */
    static <T> Iterator<T> ofAll(java.lang.Iterable<? extends T> iterable) {
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
        return new AbstractIterator<T>() {

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
    
    /**
     * Creates a Iterator based on the elements of a boolean array.
     *
     * @param array a boolean array
     * @return A new Iterator of Boolean values
     */
    static Iterator<Boolean> ofAll(boolean[] array) {
        Objects.requireNonNull(array, "array is null");
        return new AbstractIterator<Boolean>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Boolean next() {
                return array[i++];
            }
        };
    }

    /**
     * Creates a Iterator based on the elements of a byte array.
     *
     * @param array a byte array
     * @return A new Iterator of Byte values
     */
    static Iterator<Byte> ofAll(byte[] array) {
        Objects.requireNonNull(array, "array is null");
        return new AbstractIterator<Byte>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Byte next() {
                return array[i++];
            }
        };
    }

    /**
     * Creates a Iterator based on the elements of a char array.
     *
     * @param array a char array
     * @return A new Iterator of Character values
     */
    static Iterator<Character> ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return new AbstractIterator<Character>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Character next() {
                return array[i++];
            }
        };
    }

    /**
     * Creates a Iterator based on the elements of a double array.
     *
     * @param array a double array
     * @return A new Iterator of Double values
     */
    static Iterator<Double> ofAll(double[] array) {
        Objects.requireNonNull(array, "array is null");
        return new AbstractIterator<Double>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Double next() {
                return array[i++];
            }
        };
    }

    /**
     * Creates a Iterator based on the elements of a float array.
     *
     * @param array a float array
     * @return A new Iterator of Float values
     */
    static Iterator<Float> ofAll(float[] array) {
        Objects.requireNonNull(array, "array is null");
        return new AbstractIterator<Float>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Float next() {
                return array[i++];
            }
        };
    }

    /**
     * Creates a Iterator based on the elements of an int array.
     *
     * @param array an int array
     * @return A new Iterator of Integer values
     */
    static Iterator<Integer> ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return new AbstractIterator<Integer>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Integer next() {
                return array[i++];
            }
        };
    }

    /**
     * Creates a Iterator based on the elements of a long array.
     *
     * @param array a long array
     * @return A new Iterator of Long values
     */
    static Iterator<Long> ofAll(long[] array) {
        Objects.requireNonNull(array, "array is null");
        return new AbstractIterator<Long>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Long next() {
                return array[i++];
            }
        };
    }

    /**
     * Creates a Iterator based on the elements of a short array.
     *
     * @param array a short array
     * @return A new Iterator of Short values
     */
    static Iterator<Short> ofAll(short[] array) {
        Objects.requireNonNull(array, "array is null");
        return new AbstractIterator<Short>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Short next() {
                return array[i++];
            }
        };
    }

    /**
     * Creates a List of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.range(0, 0)  // = List()
     * List.range(2, 0)  // = List()
     * List.range(-2, 2) // = List(-2, -1, 0, 1)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or the empty range if {@code from >= toExclusive}
     */
    static Iterator<Integer> range(int from, int toExclusive) {
        return Iterator.rangeBy(from, toExclusive, 1);
    }

    /**
     * Creates a List of int numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeBy(1, 3, 1)  // = List(1, 2)
     * List.rangeBy(1, 4, 2)  // = List(1, 3)
     * List.rangeBy(4, 1, -2) // = List(4, 2)
     * List.rangeBy(4, 1, 2)  // = List()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or the empty range if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Iterator<Integer> rangeBy(int from, int toExclusive, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toExclusive || step * (from - toExclusive) > 0) {
            return Iterator.empty();
        } else {
            final int one = (from < toExclusive) ? 1 : -1;
            return Iterator.rangeClosedBy(from, toExclusive - one, step);
        }
    }

    /**
     * Creates a List of long numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.range(0L, 0L)  // = List()
     * List.range(2L, 0L)  // = List()
     * List.range(-2L, 2L) // = List(-2L, -1L, 0L, 1L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of long values as specified or the empty range if {@code from >= toExclusive}
     */
    static Iterator<Long> range(long from, long toExclusive) {
        return Iterator.rangeBy(from, toExclusive, 1);
    }

    /**
     * Creates a List of long numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeBy(1L, 3L, 1L)  // = List(1L, 2L)
     * List.rangeBy(1L, 4L, 2L)  // = List(1L, 3L)
     * List.rangeBy(4L, 1L, -2L) // = List(4L, 2L)
     * List.rangeBy(4L, 1L, 2L)  // = List()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or the empty range if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Iterator<Long> rangeBy(long from, long toExclusive, long step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toExclusive || step * (from - toExclusive) > 0) {
            return Iterator.empty();
        } else {
            final int one = (from < toExclusive) ? 1 : -1;
            return Iterator.rangeClosedBy(from, toExclusive - one, step);
        }
    }

    /**
     * Creates a List of int numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeClosed(0, 0)  // = List(0)
     * List.rangeClosed(2, 0)  // = List()
     * List.rangeClosed(-2, 2) // = List(-2, -1, 0, 1, 2)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or the empty range if {@code from > toInclusive}
     */
    static Iterator<Integer> rangeClosed(int from, int toInclusive) {
        return Iterator.rangeClosedBy(from, toInclusive, 1);
    }

    /**
     * Creates a List of int numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeClosedBy(1, 3, 1)  // = List(1, 2, 3)
     * List.rangeClosedBy(1, 4, 2)  // = List(1, 3)
     * List.rangeClosedBy(4, 1, -2) // = List(4, 2)
     * List.rangeClosedBy(4, 1, 2)  // = List()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or the empty range if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Iterator<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toInclusive) {
            return Iterator.of(from);
        } else if (step * (from - toInclusive) > 0) {
            return Iterator.empty();
        } else {
            return new AbstractIterator<Integer>() {

                int i = from;
                boolean hasNext = (step > 0) ? i <= toInclusive : i >= toInclusive;

                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                @Override
                public Integer next() {
                    if (!hasNext) {
                        EMPTY.next();
                    }
                    final int next = i;
                    if ((step > 0 && i > toInclusive - step) || (step < 0 && i < toInclusive - step)) {
                        hasNext = false;
                    } else {
                        i += step;
                    }
                    return next;
                }
            };
        }
    }

    /**
     * Creates a List of long numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeClosed(0L, 0L)  // = List(0L)
     * List.rangeClosed(2L, 0L)  // = List()
     * List.rangeClosed(-2L, 2L) // = List(-2L, -1L, 0L, 1L, 2L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of long values as specified or the empty range if {@code from > toInclusive}
     */
    static Iterator<Long> rangeClosed(long from, long toInclusive) {
        return Iterator.rangeClosedBy(from, toInclusive, 1L);
    }

    /**
     * Creates a List of long numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeClosedBy(1L, 3L, 1L)  // = List(1L, 2L, 3L)
     * List.rangeClosedBy(1L, 4L, 2L)  // = List(1L, 3L)
     * List.rangeClosedBy(4L, 1L, -2L) // = List(4L, 2L)
     * List.rangeClosedBy(4L, 1L, 2L)  // = List()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or the empty range if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Iterator<Long> rangeClosedBy(long from, long toInclusive, long step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toInclusive) {
            return Iterator.of(from);
        } else if (step * (from - toInclusive) > 0) {
            return Iterator.empty();
        } else {
            return new AbstractIterator<Long>() {

                long i = from;
                boolean hasNext = (step > 0) ? i <= toInclusive : i >= toInclusive;

                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                @Override
                public Long next() {
                    if (!hasNext) {
                        EMPTY.next();
                    }
                    final long next = i;
                    if ((step > 0 && i > toInclusive - step) || (step < 0 && i < toInclusive - step)) {
                        hasNext = false;
                    } else {
                        i += step;
                    }
                    return next;
                }
            };
        }
    }

    // TODO: add static factory methods similar to Stream.from, Stream.gen, ...

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
            return new AbstractIterator<T>() {

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
        if (n <= 0) {
            return this;
        } else if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new Iterator<T>() {
                private Queue<T> queue = Queue.empty();

                @Override
                public boolean hasNext() {
                    while (queue.length() < n && that.hasNext()) {
                        queue = queue.append(that.next());
                    }
                    return queue.length() == n && that.hasNext();
                }

                @Override
                public T next() {
                    if (!hasNext()) {
                        EMPTY.next();
                    }
                    Tuple2<T, Queue<T>> t = queue.append(that.next()).dequeue();
                    queue = t._2;
                    return t._1;
                }
            };
        }
    }

    @Override
    default Iterator<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {

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
            return new AbstractIterator<T>() {

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
    default Option<T> findLast(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        T last = null;
        while (hasNext()) {
            final T elem = next();
            if (predicate.test(elem)) {
                last = elem;
            }
        }
        return Option.of(last);
    }

    /**
     * FlatMaps the elements of this Iterator to java.lang.Iterables, which are iterated in the order of occurrence.
     *
     * @param mapper A mapper
     * @param <U>    Component type
     * @return A new java.lang.Iterable
     */
    default <U> Iterator<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<U>() {

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
     * FlatMaps the elements of this java.lang.Iterable by effectively calling
     * <pre><code>flatMap((Function&lt;? super T, ? extends Iterator&lt;? extends U&gt;&gt;) mapper)</code></pre>
     *
     * @param mapper A mapper.
     * @param <U>    Component type
     * @return A new java.lang.Iterable
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
            return flatMap(t -> () -> (t instanceof java.lang.Iterable) ? ofAll((java.lang.Iterable<?>) t).flatten() : of(t));
        }
    }

    @Override
    default <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    default T get() {
        return head();
    }

    @Override
    default <C> Map<C, Iterator<T>> groupBy(Function<? super T, ? extends C> classifier) {
        // TODO
        throw new UnsupportedOperationException("TODO");
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
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    default Option<Iterator<T>> initOption() {
        return hasNext() ? new Some<>(init()) : None.instance();
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
            return new AbstractIterator<T>() {

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
            return new AbstractIterator<U>() {

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
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    default Iterator<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {
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
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    default Iterator<T> replace(T currentElement, T newElement) {
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {

                boolean done = false;

                @Override
                public boolean hasNext() {
                    return that.hasNext();
                }

                @Override
                public T next() {
                    if (!that.hasNext()) {
                        EMPTY.next();
                    }
                    final T elem = next();
                    if (done || !Objects.equals(currentElement, elem)) {
                        return elem;
                    } else {
                        done = true;
                        return newElement;
                    }
                }
            };
        }
    }

    @Override
    default Iterator<T> replaceAll(T currentElement, T newElement) {
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {

                @Override
                public boolean hasNext() {
                    return that.hasNext();
                }

                @Override
                public T next() {
                    if (!that.hasNext()) {
                        EMPTY.next();
                    }
                    final T elem = next();
                    if (Objects.equals(currentElement, elem)) {
                        return newElement;
                    } else {
                        return elem;
                    }
                }
            };
        }
    }

    @Override
    default Iterator<T> replaceAll(UnaryOperator<T> operator) {
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {

                @Override
                public boolean hasNext() {
                    return that.hasNext();
                }

                @Override
                public T next() {
                    if (!that.hasNext()) {
                        EMPTY.next();
                    }
                    return operator.apply(next());
                }
            };
        }
    }

    @Override
    default Iterator<T> retainAll(java.lang.Iterable<? extends T> elements) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    default Iterator<IndexedSeq<T>> sliding(int size, int step) {
        if (size <= 0 || step <= 0) {
            throw new IllegalArgumentException(String.format("size: %s or step: %s not positive", size, step));
        }
        if (!hasNext()) {
            return empty();
        } else {
            final Stream<T> source = Stream.ofAll(this);
            return new AbstractIterator<IndexedSeq<T>>() {
                private Stream<T> that = source;
                private IndexedSeq<T> next = null;

                @Override
                public boolean hasNext() {
                    while (next == null && !that.isEmpty()) {
                        final Tuple2<Stream<T>, Stream<T>> split = that.splitAt(size);
                        next = split._1.toVector();
                        that = split._2.isEmpty() ? Stream.<T>empty() : that.drop(step);
                    }
                    return next != null;
                }

                @Override
                public IndexedSeq<T> next() {
                    if (!hasNext()) {
                        EMPTY.next();
                    }
                    final IndexedSeq<T> result = next;
                    next = null;
                    return result;
                }
            };
        }
    }

    @Override
    default Tuple2<Iterator<T>, Iterator<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (!hasNext()) {
            return Tuple.of(empty(), empty());
        } else {
            Stream<T> init = Stream.empty();
            T firstImproper = null;
            while (hasNext()) {
                final T element = next();
                if (predicate.test(element)) {
                    init = init.append(element);
                } else {
                    firstImproper = element;
                    break;
                }
            }
            return Tuple.of(init.iterator(), firstImproper == null ? empty() : Stream.of(firstImproper).appendAll(this).iterator());
        }
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
        if (hasNext()) {
            next();
            return new Some<>(this);
        } else {
            return None.instance();
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
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {

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
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    default Iterator<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (!hasNext()) {
            return empty();
        } else {
            final Iterator<T> that = this;
            return new AbstractIterator<T>() {

                private T next = null;
                private boolean finished = false;

                @Override
                public boolean hasNext() {
                    while (!finished && next == null && that.hasNext()) {
                        final T value = that.next();
                        if (predicate.test(value)) {
                            next = value;
                        } else {
                            finished = true;
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

    default <U> Iterator<Tuple2<T, U>> zip(java.lang.Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        if(isEmpty()) {
            return empty();
        } else {
            final Iterator<T> it1 = this;
            final java.util.Iterator<U> it2 = that.iterator();
            return new AbstractIterator<Tuple2<T, U>>() {
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

    default <U> Iterator<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        if(isEmpty()) {
            return empty();
        } else {
            final Iterator<T> it1 = this;
            final java.util.Iterator<U> it2 = that.iterator();
            return new AbstractIterator<Tuple2<T, U>>() {
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
            return new AbstractIterator<Tuple2<T, Integer>>() {
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
        if (!hasNext()) {
            return Tuple.of(empty(), empty());
        } else {
            final Stream<Tuple2<? extends T1, ? extends T2>> source = Stream.ofAll(this.map(unzipper::apply));
            return Tuple.of(source.map(t -> (T1) t._1).iterator(), source.map(t -> (T2) t._2).iterator());
        }
    }

    class ConcatIterator<T> extends AbstractIterator<T> {

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

    class DistinctIterator<T, U> extends AbstractIterator<T> {

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

    abstract class AbstractIterator<T> implements Iterator<T> {

        @Override
        public String toString() {
            return (isEmpty() ? "" : "non-") + "empty iterator";
        }
    }
}
