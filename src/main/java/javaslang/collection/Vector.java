/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Kind;
import javaslang.Lazy;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * TODO javadoc
 * @param <T>
 */
public interface Vector<T> extends IndexedSeq<T> {

    static <T> Vector<T> empty() {
        return Nil.instance();
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.Vector}s.
     *
     * @param <T> Component type of the Vector.
     * @return A javaslang.collection.List Collector.
     */
    static <T> Collector<T, ArrayList<T>, Vector<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, Vector<T>> finisher = Vector::ofAll;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Returns a singleton {@code Vector}, i.e. a {@code Vector} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new Vector instance containing the given element
     */
    static <T> Vector<T> of(T element) {
        return ofTrie(HashArrayMappedTrie.<Integer, T> empty().put(0, element));
    }

    /**
     * <p>
     * Creates a Vector of the given elements.
     * </p>
     * @param <T>      Component type of the Vector.
     * @param elements Zero or more elements.
     * @return A vector containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    static <T> Vector<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        HashArrayMappedTrie<Integer, T> result = HashArrayMappedTrie.empty();
        for (T element : elements) {
            result = result.put(result.size(), element);
        }
        return ofTrie(result);
    }

    /**
     * Creates a Vector of the given elements.
     *
     * The resulting vector has the same iteration order as the given iterable of elements
     * if the iteration order of the elements is stable.
     *
     * @param <T>      Component type of the Vector.
     * @param elements An Iterable of elements.
     * @return A vector containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("unchecked")
    static <T> Vector<T> ofAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof Vector) {
            return (Vector<T>) elements;
        } else {
            return new Impl<>(elements);
        }
    }

    @SuppressWarnings("unchecked")
    static <T> Vector<T> ofTrie(HashArrayMappedTrie<Integer, ? extends T> trie) {
        if(trie.isEmpty()) {
            return Nil.instance();
        } else {
            return new Impl<>((HashArrayMappedTrie<Integer, T>) trie);
        }
    }

    /**
     * Creates a Vector based on the elements of a boolean array.
     *
     * @param array a boolean array
     * @return A new Vector of Boolean values
     */
    static Vector<Boolean> ofAll(boolean[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(() -> new Iterator<Boolean>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Boolean next() {
                return array[i++];
            }
        });
    }

    /**
     * Creates a Vector based on the elements of a byte array.
     *
     * @param array a byte array
     * @return A new Vector of Byte values
     */
    static Vector<Byte> ofAll(byte[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(() -> new Iterator<Byte>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Byte next() {
                return array[i++];
            }
        });
    }

    /**
     * Creates a Vector based on the elements of a char array.
     *
     * @param array a char array
     * @return A new Vector of Character values
     */
    static Vector<Character> ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(() -> new Iterator<Character>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Character next() {
                return array[i++];
            }
        });
    }

    /**
     * Creates a Vector based on the elements of a double array.
     *
     * @param array a double array
     * @return A new Vector of Double values
     */
    static Vector<Double> ofAll(double[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(() -> new Iterator<Double>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Double next() {
                return array[i++];
            }
        });
    }

    /**
     * Creates a Vector based on the elements of a float array.
     *
     * @param array a float array
     * @return A new Vector of Float values
     */
    static Vector<Float> ofAll(float[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(() -> new Iterator<Float>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Float next() {
                return array[i++];
            }
        });
    }

    /**
     * Creates a Vector based on the elements of an int array.
     *
     * @param array an int array
     * @return A new Vector of Integer values
     */
    static Vector<Integer> ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(() -> new Iterator<Integer>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Integer next() {
                return array[i++];
            }
        });
    }

    /**
     * Creates a Vector based on the elements of a long array.
     *
     * @param array a long array
     * @return A new Vector of Long values
     */
    static Vector<Long> ofAll(long[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(() -> new Iterator<Long>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Long next() {
                return array[i++];
            }
        });
    }

    /**
     * Creates a Vector based on the elements of a short array.
     *
     * @param array a short array
     * @return A new Vector of Short values
     */
    static Vector<Short> ofAll(short[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(() -> new Iterator<Short>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Short next() {
                return array[i++];
            }
        });
    }

    /**
     * Creates a Vector of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Vector.range(0, 0)  // = Vector()
     * Vector.range(2, 0)  // = Vector()
     * Vector.range(-2, 2) // = Vector(-2, -1, 0, 1)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or the empty range if {@code from >= toExclusive}
     */
    static Vector<Integer> range(int from, int toExclusive) {
        return Vector.rangeBy(from, toExclusive, 1);
    }

    /**
     * Creates a Vector of int numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Vector.rangeBy(1, 3, 1)  // = Vector(1, 2)
     * Vector.rangeBy(1, 4, 2)  // = Vector(1, 3)
     * Vector.rangeBy(4, 1, -2) // = Vector(4, 2)
     * Vector.rangeBy(4, 1, 2)  // = Vector()
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
    static Vector<Integer> rangeBy(int from, int toExclusive, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toExclusive || step * (from - toExclusive) > 0) {
            return Vector.empty();
        } else {
            final int one = (from < toExclusive) ? 1 : -1;
            return Vector.rangeClosedBy(from, toExclusive - one, step);
        }
    }

    /**
     * Creates a Vector of long numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Vector.range(0L, 0L)  // = Vector()
     * Vector.range(2L, 0L)  // = Vector()
     * Vector.range(-2L, 2L) // = Vector(-2L, -1L, 0L, 1L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of long values as specified or the empty range if {@code from >= toExclusive}
     */
    static Vector<Long> range(long from, long toExclusive) {
        return Vector.rangeBy(from, toExclusive, 1);
    }

    /**
     * Creates a Vector of long numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Vector.rangeBy(1L, 3L, 1L)  // = Vector(1L, 2L)
     * Vector.rangeBy(1L, 4L, 2L)  // = Vector(1L, 3L)
     * Vector.rangeBy(4L, 1L, -2L) // = Vector(4L, 2L)
     * Vector.rangeBy(4L, 1L, 2L)  // = Vector()
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
    static Vector<Long> rangeBy(long from, long toExclusive, long step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toExclusive || step * (from - toExclusive) > 0) {
            return Vector.empty();
        } else {
            final int one = (from < toExclusive) ? 1 : -1;
            return Vector.rangeClosedBy(from, toExclusive - one, step);
        }
    }

    /**
     * Creates a Vector of int numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Vector.rangeClosed(0, 0)  // = Vector(0)
     * Vector.rangeClosed(2, 0)  // = Vector()
     * Vector.rangeClosed(-2, 2) // = Vector(-2, -1, 0, 1, 2)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or the empty range if {@code from > toInclusive}
     */
    static Vector<Integer> rangeClosed(int from, int toInclusive) {
        return Vector.rangeClosedBy(from, toInclusive, 1);
    }

    /**
     * Creates a Vector of int numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Vector.rangeClosedBy(1, 3, 1)  // = Vector(1, 2, 3)
     * Vector.rangeClosedBy(1, 4, 2)  // = Vector(1, 3)
     * Vector.rangeClosedBy(4, 1, -2) // = Vector(4, 2)
     * Vector.rangeClosedBy(4, 1, 2)  // = Vector()
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
    static Vector<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toInclusive) {
            return Vector.of(from);
        } else if (step * (from - toInclusive) > 0) {
            return Vector.empty();
        } else {
            HashArrayMappedTrie<Integer, Integer> trie = HashArrayMappedTrie.empty();
            if (step > 0) {
                int i = from;
                while (i <= toInclusive) {
                    trie = trie.put(trie.size(), i);
                    if(Integer.MAX_VALUE - step < i) {
                        break;
                    }
                    i += step;
                }
            } else {
                int i = from;
                while (i >= toInclusive) {
                    trie = trie.put(trie.size(), i);
                    if(Integer.MIN_VALUE - step > i) {
                        break;
                    }
                    i += step;
                }
            }
            return ofTrie(trie);
        }
    }

    /**
     * Creates a Vector of long numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Vector.rangeClosed(0L, 0L)  // = Vector(0L)
     * Vector.rangeClosed(2L, 0L)  // = Vector()
     * Vector.rangeClosed(-2L, 2L) // = Vector(-2L, -1L, 0L, 1L, 2L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of long values as specified or the empty range if {@code from > toInclusive}
     */
    static Vector<Long> rangeClosed(long from, long toInclusive) {
        return Vector.rangeClosedBy(from, toInclusive, 1L);
    }

    /**
     * Creates a Vector of long numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Vector.rangeClosedBy(1L, 3L, 1L)  // = Vector(1L, 2L, 3L)
     * Vector.rangeClosedBy(1L, 4L, 2L)  // = Vector(1L, 3L)
     * Vector.rangeClosedBy(4L, 1L, -2L) // = Vector(4L, 2L)
     * Vector.rangeClosedBy(4L, 1L, 2L)  // = Vector()
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
    static Vector<Long> rangeClosedBy(long from, long toInclusive, long step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toInclusive) {
            return Vector.of(from);
        } else if (step * (from - toInclusive) > 0) {
            return Vector.empty();
        } else {
            HashArrayMappedTrie<Integer, Long> trie = HashArrayMappedTrie.empty();
            if (step > 0) {
                long i = from;
                while (i <= toInclusive) {
                    trie = trie.put(trie.size(), i);
                    if(Long.MAX_VALUE - step < i) {
                        break;
                    }
                    i += step;
                }
            } else {
                long i = from;
                while (i >= toInclusive) {
                    trie = trie.put(trie.size(), i);
                    if(Long.MIN_VALUE - step > i) {
                        break;
                    }
                    i += step;
                }
            }
            return ofTrie(trie);
        }
    }

    @Override
    default Vector<T> append(T element) {
        return ofTrie(toHashArrayMappedTrie().put(length(), element));
    }

    @Override
    default Vector<T> appendAll(Iterable<? extends T> elements) {
        HashArrayMappedTrie<Integer, T> trie = toHashArrayMappedTrie();
        for (T element : elements) {
            trie = trie.put(trie.size(), element);
        }
        return ofTrie(trie);
    }

    @Override
    default Vector<T> clear() {
        return Nil.instance();
    }

    @Override
    default Vector<Tuple2<T, T>> crossProduct() {
        return crossProduct(this);
    }

    @Override
    default <U> Vector<Tuple2<T, U>> crossProduct(Iterable<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        final Vector<? extends U> other = Vector.ofAll(that);
        return flatMap(a -> other.map(b -> Tuple.of(a, b)));
    }

    @Override
    default Vector<Vector<T>> combinations() {
        return Vector.rangeClosed(0, length()).map(this::combinations).flatMap(Function.identity());
    }

    @Override
    default Vector<Vector<T>> combinations(int k) {
        class Recursion {
            Vector<Vector<T>> combinations(Vector<T> elements, int k) {
                return (k == 0)
                        ? Vector.of(Vector.empty())
                        : elements.zipWithIndex().flatMap(t -> combinations(elements.drop(t._2 + 1), (k - 1))
                        .map((Vector<T> c) -> c.prepend(t._1)));
            }
        }
        return new Recursion().combinations(this, Math.max(k, 0));
    }

    @Override
    default Vector<T> distinct() {
        return distinctBy(Function.identity());
    }

    @Override
    default Vector<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        final java.util.Set<T> seen = new java.util.TreeSet<>(comparator);
        return filter(seen::add);
    }

    @Override
    default <U> Vector<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        final java.util.Set<U> seen = new java.util.HashSet<>();
        return filter(t -> seen.add(keyExtractor.apply(t)));
    }

    @Override
    default Vector<T> drop(int n) {
        if (n <= 0) {
            return this;
        }
        if (n >= length()) {
            return empty();
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = n; i < length(); i++) {
            trie = trie.put(i - n, get(i));
        }
        return ofTrie(trie);
    }

    @Override
    default Vector<T> dropRight(int n) {
        if (n <= 0) {
            return this;
        }
        if (n >= length()) {
            return empty();
        }
        HashArrayMappedTrie<Integer, T> trie = toHashArrayMappedTrie();
        for (int i = length() - n; i < length(); i++) {
            trie = trie.remove(i);
        }
        return ofTrie(trie);
    }

    @Override
    default Vector<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (int i = 0; i < length(); i++) {
            if(!predicate.test(get(i))) {
                return drop(i);
            }
        }
        return empty();
    }

    @Override
    default Vector<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (T t: this) {
            if (predicate.test(t)) {
                trie = trie.put(trie.size(), t);
            }
        }
        if (trie.size() == length()) {
            return this;
        } else {
            return ofTrie(trie);
        }
    }

    @Override
    default Vector<T> findAll(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate);
    }

    @Override
    default <U> Vector<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return empty();
        } else {
            HashArrayMappedTrie<Integer, U> trie = HashArrayMappedTrie.empty();
            for (int i = 0; i < length(); i++) {
                for (U u : mapper.apply(get(i))) {
                    trie = trie.put(trie.size(), u);
                }
            }
            return ofTrie(trie);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    default <U> Vector<U> flatMapM(Function<? super T, ? extends Kind<? extends IterableKind<?>, ? extends U>> mapper) {
        return flatMap((Function<? super T, ? extends Iterable<? extends U>>) mapper);
    }

    @Override
    default Vector<Object> flatten() {
        return flatMap(t -> (t instanceof Iterable) ? Vector.ofAll((Iterable<?>) t).flatten() : Vector.of(t));
    }

    @Override
    default T get(int index) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("get(" + index + ") on Nil");
        }
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException("get(" + index + ")");
        }
        return toHashArrayMappedTrie().get(index).get();
    }

    @Override
    default <C> Map<C, ? extends Vector<T>> groupBy(Function<? super T, ? extends C> classifier) {
        Map<C, Vector<T>> result = HashMap.empty();
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            final C key = classifier.apply(value);
            final Vector<T> list = result.get(key);
            result = result.put(key, (list == null) ? Vector.of(value) : list.append(value));
        }
        return result;
    }

    @Override
    default Vector<? extends Vector<T>> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    default int indexOf(T element, int from) {
        for (int i = from; i < length(); i++) {
            if (Objects.equals(get(i), element)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    default Vector<T> init() {
        if(isEmpty()) {
            throw new UnsupportedOperationException("init of empty vector");
        } else {
            return ofTrie(toHashArrayMappedTrie().remove(length() - 1));
        }
    }

    @Override
    default Option<? extends Vector<T>> initOption() {
        if(isEmpty()) {
            return None.instance();
        } else {
            return new Some<>(ofTrie(toHashArrayMappedTrie().remove(length() - 1)));
        }
    }

    @Override
    default Vector<T> insert(int index, T element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        }
        if (index > length()) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e) on Vector of length " + length());
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i <= length(); i++) {
            if(i == index) {
                trie = trie.put(trie.size(), element);
            }
            if(i < length()) {
                trie = trie.put(trie.size(), get(i));
            }
        }
        return ofTrie(trie);
    }

    @Override
    default Vector<T> insertAll(int index, Iterable<? extends T> elements) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        }
        if (index > length()) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e) on Vector of length " + length());
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i <= length(); i++) {
            if(i == index) {
                for (T element : elements) {
                    trie = trie.put(trie.size(), element);
                }
            }
            if(i < length()) {
                trie = trie.put(trie.size(), get(i));
            }
        }
        return ofTrie(trie);
    }

    @Override
    default Vector<T> intersperse(T element) {
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            if(i > 0) {
                trie = trie.put(trie.size(), element);
            }
            trie = trie.put(trie.size(), get(i));
        }
        return ofTrie(trie);
    }

    @Override
    default boolean isEmpty() {
        return length() == 0;
    }

    @Override
    default Iterator<T> iterator() {
        return new Iterator<T>() {
            private final HashArrayMappedTrie<Integer, T> trie = toHashArrayMappedTrie();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < trie.size();
            }

            @Override
            public T next() {
                return trie.get(index++).get();
            }
        };
    }

    @Override
    default int length() {
        return toHashArrayMappedTrie().size();
    }

    @Override
    default int lastIndexOf(T element, int end) {
        for (int i = Math.min(end, length() - 1); i >= 0; i--) {
            if (Objects.equals(get(i), element)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    default <U> Vector<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        HashArrayMappedTrie<Integer, U> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            trie = trie.put(i, mapper.apply(get(i)));
        }
        return ofTrie(trie);
    }

    @Override
    default Tuple2<Vector<T>, Vector<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final java.util.List<T> left = new ArrayList<>(), right = new ArrayList<>();
        for (int i = 0; i < length(); i++) {
            T t = get(i);
            (predicate.test(t) ? left : right).add(t);
        }
        return Tuple.of(Vector.ofAll(left), Vector.ofAll(right));
    }

    @Override
    default Vector<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(head());
        }
        return this;
    }

    @Override
    default Vector<Vector<T>> permutations() {
        if (isEmpty()) {
            return Nil.instance();
        } else {
            if (length() == 1) {
                return Vector.of(this);
            } else {
                HashArrayMappedTrie<Integer, Vector<T>> trie = HashArrayMappedTrie.empty();
                for (T t : distinct()) {
                    for (Vector<T> ts : remove(t).permutations()) {
                        trie = trie.put(trie.size(), ts);
                    }
                }
                return ofTrie(trie);
            }
        }
    }

    @Override
    default Vector<T> prepend(T element) {
        return insert(0, element);
    }

    @Override
    default Vector<T> prependAll(Iterable<? extends T> elements) {
        return insertAll(0, elements);
    }

    @Override
    default Vector<T> remove(T element) {
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        boolean found = false;
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (found) {
                trie = trie.put(trie.size(), value);
            } else {
                if(element.equals(value)) {
                    found = true;
                } else {
                    trie = trie.put(trie.size(), value);
                }
            }
        }
        return trie.size() == toHashArrayMappedTrie().size() ? this : ofTrie(trie);
    }

    @Override
    default Vector<T> removeFirst(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        boolean found = false;
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (found) {
                trie = trie.put(trie.size(), value);
            } else {
                if(predicate.test(value)) {
                    found = true;
                } else {
                    trie = trie.put(trie.size(), value);
                }
            }
        }
        return trie.size() == toHashArrayMappedTrie().size() ? this : ofTrie(trie);
    }

    @Override
    default Vector<T> removeLast(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = length() - 1; i >= 0; i--) {
            if(predicate.test(get(i))) {
                return removeAt(i);
            }
        }
        return this;
    }

    @Override
    default Vector<T> removeAt(int indx) {
        if(indx < 0) {
            throw new IndexOutOfBoundsException("removeAt(" + indx + ")");
        }
        if(indx >= length()) {
            throw new IndexOutOfBoundsException("removeAt(" + indx + ")");
        }
        HashArrayMappedTrie<Integer, T> trie = toHashArrayMappedTrie().remove(indx);
        for (int i = indx + 1; i < length(); i++) {
            final T value = get(i);
            trie = trie.remove(i).put(i - 1, value);
        }
        return ofTrie(trie);
    }

    @Override
    default Vector<T> removeAll(T element) {
        HashArrayMappedTrie<Integer, T> result = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (!element.equals(value)) {
                result = result.put(result.size(), value);
            }
        }
        return result.size() == length() ? this : ofTrie(result);

    }

    @Override
    default Vector<T> removeAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        HashArrayMappedTrie<T, T> removed = HashArrayMappedTrie.empty();
        for (T element : elements) {
            removed = removed.put(element, element);
        }
        HashArrayMappedTrie<Integer, T> result = HashArrayMappedTrie.empty();
        boolean found = false;
        for (int i = 0; i < length(); i++) {
            T element = get(i);
            if (removed.get(element).isDefined()) {
                found = true;
            } else {
                result = result.put(result.size(), element);
            }
        }
        return found ? ofTrie(result) : this;
    }

    @Override
    default Vector<T> replace(T currentElement, T newElement) {
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        boolean found = false;
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (found) {
                trie = trie.put(trie.size(), value);
            } else {
                if(currentElement.equals(value)) {
                    trie = trie.put(trie.size(), newElement);
                    found = true;
                } else {
                    trie = trie.put(trie.size(), value);
                }
            }
        }
        return found ? ofTrie(trie) : this;
    }

    @Override
    default Vector<T> replaceAll(T currentElement, T newElement) {
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        boolean changed = false;
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if(currentElement.equals(value)) {
                trie = trie.put(trie.size(), newElement);
                changed = true;
            } else {
                trie = trie.put(trie.size(), value);
            }
        }
        return changed ? ofTrie(trie) : this;
    }

    @Override
    default Vector<T> replaceAll(UnaryOperator<T> operator) {
        Objects.requireNonNull(operator, "operator is null");
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            trie = trie.put(trie.size(), operator.apply(get(i)));
        }
        return ofTrie(trie);

    }

    @Override
    default Vector<T> retainAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final Vector<T> keeped = Vector.ofAll(elements).distinct();
        HashArrayMappedTrie<Integer, T> result = HashArrayMappedTrie.empty();
        for (T element : this) {
            if (keeped.contains(element)) {
                result = result.put(result.size(), element);
            }
        }
        return ofTrie(result);
    }

    @Override
    default Vector<T> reverse() {
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            trie = trie.put(i, get(length() - 1 - i));
        }
        return ofTrie(trie);
    }

    @Override
    default Vector<T> set(int index, T element) {
        if(index < 0) {
            throw new IndexOutOfBoundsException("set(" + index + ")");
        }
        if(index >= length()) {
            throw new IndexOutOfBoundsException("set(" + index + ")");
        }
        return ofTrie(toHashArrayMappedTrie().put(index, element));
    }

    @Override
    default Vector<Vector<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    default Vector<Vector<T>> sliding(int size, int step) {
        if (size <= 0 || step <= 0) {
            throw new IllegalArgumentException(String.format("size: %s or step: %s not positive", size, step));
        }
        HashArrayMappedTrie<Integer, Vector<T>> result = HashArrayMappedTrie.empty();
        Vector<T> list = this;
        while (!list.isEmpty()) {
            final Tuple2<Vector<T>, Vector<T>> split = list.splitAt(size);
            result = result.put(result.size(), split._1);
            list = split._2.isEmpty() ? Nil.instance() : list.drop(step);
        }
        return ofTrie(result);

    }

    @Override
    default Vector<T> sort() {
        return isEmpty() ? this : toJavaStream().sorted().collect(Vector.collector());
    }

    @Override
    default Vector<T> sort(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return isEmpty() ? this : toJavaStream().sorted(comparator).collect(Vector.collector());
    }

    @Override
    default Tuple2<Vector<T>, Vector<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return Tuple.of(takeWhile(predicate), dropWhile(predicate));
    }

    @Override
    default Tuple2<Vector<T>, Vector<T>> splitAt(int n) {
        return Tuple.of(take(n), drop(n));
    }

    @Override
    default Tuple2<Vector<T>, Vector<T>> splitAt(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Vector<T> init = takeWhile(predicate.negate());
        return Tuple.of(init, drop(init.length()));
    }

    @Override
    default Tuple2<Vector<T>, Vector<T>> splitAtInclusive(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        HashArrayMappedTrie<Integer, T> init = HashArrayMappedTrie.empty();
        for (T t : this) {
            init = init.put(init.size(), t);
            if (predicate.test(t)) {
                if (init.size() == length()) {
                    Tuple.of(this, empty());
                } else {
                    return Tuple.of(ofTrie(init), drop(init.size()));
                }
            }
        }
        return Tuple.of(this, empty());
    }

    @Override
    default Spliterator<T> spliterator() {
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    default Vector<T> subsequence(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("subsequence(" + beginIndex + ")");
        }
        if (beginIndex > length()) {
            throw new IndexOutOfBoundsException("subsequence(" + beginIndex + ")");
        }
        return drop(beginIndex);
    }

    @Override
    default Vector<T> subsequence(int beginIndex, int endIndex) {
        if (beginIndex < 0 || beginIndex > endIndex || endIndex > length()) {
            throw new IndexOutOfBoundsException(
                    String.format("subsequence(%s, %s) on List of length %s", beginIndex, endIndex, length()));
        }
        if(beginIndex == endIndex) {
            return Vector.empty();
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = beginIndex; i < endIndex; i++) {
            trie = trie.put(trie.size(), get(i));
        }
        return ofTrie(trie);
    }

    @Override
    default T head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head of empty vector");
        } else {
            return get(0);
        }
    }

    @Override
    default Option<T> headOption() {
        if (isEmpty()) {
            return None.instance();
        } else {
            return new Some<>(get(0));
        }
    }

    @Override
    default Vector<T> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty vector");
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 1; i < length(); i++) {
            trie = trie.put(i - 1, get(i));
        }
        return ofTrie(trie);
    }

    @Override
    default Option<Vector<T>> tailOption() {
        if(isEmpty()) {
            return None.instance();
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 1; i < length(); i++) {
            trie = trie.put(i - 1, get(i));
        }
        return new Some<>(ofTrie(trie));
    }

    @Override
    default Vector<T> take(int n) {
        if (n >= length()) {
            return this;
        }
        if (n <= 0) {
            return empty();
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < n; i++) {
            trie = trie.put(i, get(i));
        }
        return ofTrie(trie);
    }

    @Override
    default Vector<T> takeRight(int n) {
        if (n >= length()) {
            return this;
        }
        if (n <= 0) {
            return empty();
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < n; i++) {
            trie = trie.put(i, get(length() - n + i));
        }
        return ofTrie(trie);
    }

    @Override
    default Vector<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            T value = get(i);
            if (!predicate.test(value)) {
                break;
            }
            trie = trie.put(i, get(i));
        }
        return trie.size() == length() ? this : ofTrie(trie);
    }

    @Override
    default <U> Vector<U> unit(Iterable<? extends U> iterable) {
        return Vector.ofAll(iterable);
    }

    @Override
    default <T1, T2> Tuple2<Vector<T1>, Vector<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        HashArrayMappedTrie<Integer, T1> xs = HashArrayMappedTrie.empty();
        HashArrayMappedTrie<Integer, T2> ys = HashArrayMappedTrie.empty();
        for (T element : this) {
            final Tuple2<? extends T1, ? extends T2> t = unzipper.apply(element);
            xs = xs.put(xs.size(), t._1);
            ys = ys.put(ys.size(), t._2);
        }
        return Tuple.of(ofTrie(xs), ofTrie(ys));
    }

    @Override
    default <U> Vector<Tuple2<T, U>> zip(Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        HashArrayMappedTrie<Integer, Tuple2<T, U>> result = HashArrayMappedTrie.empty();
        Iterator<T> list1 = iterator();
        java.util.Iterator<U> list2 = that.iterator();
        while (list1.hasNext() && list2.hasNext()) {
            result = result.put(result.size(), Tuple.of(list1.next(), list2.next()));
        }
        return ofTrie(result);
    }

    @Override
    default <U> Vector<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        HashArrayMappedTrie<Integer, Tuple2<T, U>> result = HashArrayMappedTrie.empty();
        Iterator<T> list1 = iterator();
        java.util.Iterator<U> list2 = that.iterator();
        while (list1.hasNext() || list2.hasNext()) {
            final T elem1 = list1.hasNext() ? list1.next() : thisElem;
            final U elem2 = list2.hasNext() ? list2.next() : thatElem;
            result = result.put(result.size(), Tuple.of(elem1, elem2));
        }
        return ofTrie(result);
    }

    @Override
    default Vector<Tuple2<T, Integer>> zipWithIndex() {
        HashArrayMappedTrie<Integer, Tuple2<T, Integer>> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            trie = trie.put(i, Tuple.of(get(i), i));
        }
        return ofTrie(trie);
    }

    HashArrayMappedTrie<Integer, T> toHashArrayMappedTrie();

    final class Impl<T> implements Vector<T>, Serializable {
        private static final long serialVersionUID = 1L;

        private final HashArrayMappedTrie<Integer, T> trie;
        private final transient Lazy<Integer> hashCode = Lazy.of(() -> Traversable.hash(this));

        private Impl(HashArrayMappedTrie<Integer, T> trie) {
            this.trie = trie;
        }

        private Impl(Iterable<? extends T> elements) {
            HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
            for (T element : elements) {
                trie = trie.put(trie.size(), element);
            }
            this.trie = trie;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Vector) {
                Vector<?> vector1 = this;
                Vector<?> vector2 = (Vector<?>) o;
                while (!vector1.isEmpty() && !vector2.isEmpty()) {
                    final boolean isEqual = Objects.equals(vector1.head(), vector2.head());
                    if (!isEqual) {
                        return false;
                    }
                    vector1 = vector1.tail();
                    vector2 = vector2.tail();
                }
                return vector1.isEmpty() && vector2.isEmpty();
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return hashCode.get();
        }

        @Override
        public String toString() {
            return map(String::valueOf).join(", ", "Vector(", ")");
        }

        @Override
        public HashArrayMappedTrie<Integer, T> toHashArrayMappedTrie() {
            return trie;
        }

        private Object writeReplace() {
            return new SerializationProxy<>(this);
        }

        private void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        /**
         * A serialization proxy.
         * See {@link List.Cons.SerializationProxy}
         */
        private static final class SerializationProxy<T> implements Serializable {

            private static final long serialVersionUID = 1L;

            private transient Impl<T> vector;

            SerializationProxy(Impl<T> vector) {
                this.vector = vector;
            }

            private void writeObject(ObjectOutputStream s) throws IOException {
                s.defaultWriteObject();
                s.writeInt(vector.length());
                for (T t: vector) {
                    s.writeObject(t);
                }
            }

            private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
                s.defaultReadObject();
                final int size = s.readInt();
                if (size <= 0) {
                    throw new InvalidObjectException("No elements");
                }
                HashArrayMappedTrie<Integer, T> temp = HashArrayMappedTrie.empty();
                for (int i = 0; i < size; i++) {
                    @SuppressWarnings("unchecked")
                    final T element = (T) s.readObject();
                    temp = temp.put(temp.size(), element);
                }
                vector = new Impl<>(temp);
            }

            private Object readResolve() {
                return vector;
            }
        }
    }

    class Nil<T> implements Vector<T>, Serializable {
        private static final long serialVersionUID = 1L;
        private static final Vector<?> INSTANCE = new Nil<>();

        private final HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();

        private Nil() {
        }

        @SuppressWarnings("unchecked")
        public static <T> Nil<T> instance() {
            return (Nil<T>) INSTANCE;
        }

        @Override
        public boolean equals(Object o) {
            return o == this;
        }

        @Override
        public int hashCode() {
            return Traversable.hash(this);
        }

        @Override
        public String toString() {
            return "Vector()";
        }

        @Override
        public HashArrayMappedTrie<Integer, T> toHashArrayMappedTrie() {
            return trie;
        }

        /**
         * Instance control for object serialization.
         *
         * @return The singleton instance of Nil.
         * @see java.io.Serializable
         */
        private Object readResolve() {
            return INSTANCE;
        }
    }
}
