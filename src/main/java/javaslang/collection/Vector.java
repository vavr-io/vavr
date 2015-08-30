/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * TODO javadoc
 *
 * @param <T> Component type of the Vector.
 */
public final class Vector<T> implements IndexedSeq<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Vector<?> EMPTY = new Vector<>(HashArrayMappedTrie.empty());

    private final HashArrayMappedTrie<Integer, T> trie;
    private final transient Lazy<Integer> hashCode = Lazy.of(() -> Traversable.hash(this));

    private Vector(HashArrayMappedTrie<Integer, T> trie) {
        this.trie = trie;
    }

    /**
     * Returns the empty Vector.
     *
     * @param <T> Component type.
     * @return The empty Vector.
     */
    @SuppressWarnings("unchecked")
    public static <T> Vector<T> empty() {
        return (Vector<T>) EMPTY;
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.Vector}s.
     *
     * @param <T> Component type of the Vector.
     * @return A javaslang.collection.List Collector.
     */
    public static <T> Collector<T, ArrayList<T>, Vector<T>> collector() {
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
    public static <T> Vector<T> of(T element) {
        return new Vector<>(HashArrayMappedTrie.<Integer, T> empty().put(0, element));
    }

    /**
     * <p>
     * Creates a Vector of the given elements.
     * </p>
     *
     * @param <T>      Component type of the Vector.
     * @param elements Zero or more elements.
     * @return A vector containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    public static <T> Vector<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        HashArrayMappedTrie<Integer, T> result = HashArrayMappedTrie.empty();
        for (T element : elements) {
            result = result.put(result.size(), element);
        }
        return elements.length == 0 ? empty() : new Vector<>(result);
    }

    /**
     * Creates a Vector of the given elements.
     *
     * The resulting vector has the same iteration order as the given iterable of elements
     * if the iteration order of the elements is stable.
     *
     * @param <T>      Component type of the Vector.
     * @param elements An java.lang.Iterable of elements.
     * @return A vector containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("unchecked")
    public static <T> Vector<T> ofAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof Vector) {
            return (Vector<T>) elements;
        } else {
            HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
            for (T element : elements) {
                trie = trie.put(trie.size(), element);
            }
            return new Vector<>(trie);
        }
    }

    /**
     * Creates a Vector based on the elements of a boolean array.
     *
     * @param array a boolean array
     * @return A new Vector of Boolean values
     */
    public static Vector<Boolean> ofAll(boolean[] array) {
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
    public static Vector<Byte> ofAll(byte[] array) {
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
    public static Vector<Character> ofAll(char[] array) {
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
    public static Vector<Double> ofAll(double[] array) {
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
    public static Vector<Float> ofAll(float[] array) {
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
    public static Vector<Integer> ofAll(int[] array) {
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
    public static Vector<Long> ofAll(long[] array) {
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
    public static Vector<Short> ofAll(short[] array) {
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
    public static Vector<Integer> range(int from, int toExclusive) {
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
    public static Vector<Integer> rangeBy(int from, int toExclusive, int step) {
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
    public static Vector<Long> range(long from, long toExclusive) {
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
    public static Vector<Long> rangeBy(long from, long toExclusive, long step) {
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
    public static Vector<Integer> rangeClosed(int from, int toInclusive) {
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
    public static Vector<Integer> rangeClosedBy(int from, int toInclusive, int step) {
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
                    if (Integer.MAX_VALUE - step < i) {
                        break;
                    }
                    i += step;
                }
            } else {
                int i = from;
                while (i >= toInclusive) {
                    trie = trie.put(trie.size(), i);
                    if (Integer.MIN_VALUE - step > i) {
                        break;
                    }
                    i += step;
                }
            }
            return trie.size() == 0 ? empty() : new Vector<>(trie);
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
    public static Vector<Long> rangeClosed(long from, long toInclusive) {
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
    public static Vector<Long> rangeClosedBy(long from, long toInclusive, long step) {
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
                    if (Long.MAX_VALUE - step < i) {
                        break;
                    }
                    i += step;
                }
            } else {
                long i = from;
                while (i >= toInclusive) {
                    trie = trie.put(trie.size(), i);
                    if (Long.MIN_VALUE - step > i) {
                        break;
                    }
                    i += step;
                }
            }
            return trie.size() == 0 ? empty() : new Vector<>(trie);
        }
    }

    @Override
    public Vector<T> append(T element) {
        return new Vector<>(trie.put(trie.size(), element));
    }

    @Override
    public Vector<T> appendAll(java.lang.Iterable<? extends T> elements) {
        HashArrayMappedTrie<Integer, T> result = trie;
        for (T element : elements) {
            result = result.put(result.size(), element);
        }
        return new Vector<>(result);
    }

    @Override
    public Vector<T> clear() {
        return Vector.empty();
    }

    @Override
    public Vector<Tuple2<T, T>> crossProduct() {
        return crossProduct(this);
    }

    @Override
    public <U> Vector<Tuple2<T, U>> crossProduct(java.lang.Iterable<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        final Vector<? extends U> other = Vector.ofAll(that);
        return flatMap(a -> other.map(b -> Tuple.of(a, b)));
    }

    @Override
    public Vector<Vector<T>> combinations() {
        return Vector.rangeClosed(0, length()).map(this::combinations).flatMap(Function.identity());
    }

    @Override
    public Vector<Vector<T>> combinations(int k) {
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
    public Vector<T> distinct() {
        return distinctBy(Function.identity());
    }

    @Override
    public Vector<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        final java.util.Set<T> seen = new java.util.TreeSet<>(comparator);
        return filter(seen::add);
    }

    @Override
    public <U> Vector<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        final java.util.Set<U> seen = new java.util.HashSet<>();
        return filter(t -> seen.add(keyExtractor.apply(t)));
    }

    @Override
    public Vector<T> drop(int n) {
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
        return trie.size() == 0 ? empty() : new Vector<>(trie);
    }

    @Override
    public Vector<T> dropRight(int n) {
        if (n <= 0) {
            return this;
        }
        if (n >= length()) {
            return empty();
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length() - n; i++) {
            trie = trie.put(trie.size(), get(i));
        }
        return new Vector<>(trie);
    }

    @Override
    public Vector<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (int i = 0; i < length(); i++) {
            if (!predicate.test(get(i))) {
                return drop(i);
            }
        }
        return empty();
    }

    @Override
    public Vector<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (T t : this) {
            if (predicate.test(t)) {
                trie = trie.put(trie.size(), t);
            }
        }
        if (trie.size() == length()) {
            return this;
        } else {
            return trie.size() == 0 ? empty() : new Vector<>(trie);
        }
    }

    @Override
    public <U> Vector<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
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
            return trie.size() == 0 ? empty() : new Vector<>(trie);
        }
    }

    @Override
    public <U> Vector<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper) {
        return flatMap(mapper);
    }

    @Override
    public Vector<Object> flatten() {
        return flatMap(t -> (t instanceof java.lang.Iterable) ? Vector.ofAll((java.lang.Iterable<?>) t).flatten() : Vector.of(t));
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException("get(" + index + ")");
        }
        return trie.get(index).get();
    }

    @Override
    public T head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head of empty vector");
        } else {
            return get(0);
        }
    }

    @Override
    public Option<T> headOption() {
        if (isEmpty()) {
            return None.instance();
        } else {
            return new Some<>(get(0));
        }
    }

    @Override
    public <C> Map<C, Vector<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return foldLeft(HashMap.empty(), (map, t) -> {
            final C key = classifier.apply(t);
            final Vector<T> values = map.get(key).map(ts -> ts.append(t)).orElse(Vector.of(t));
            return map.put(key, values);
        });
    }

    @Override
    public boolean hasDefiniteSize() {
        return true;
    }

    @Override
    public int indexOf(T element, int from) {
        for (int i = from; i < length(); i++) {
            if (Objects.equals(get(i), element)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Vector<T> init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init of empty vector");
        }
        return new Vector<>(trie.remove(length() - 1));
    }

    @Override
    public Option<? extends Vector<T>> initOption() {
        if (isEmpty()) {
            return None.instance();
        } else {
            return new Some<>(init());
        }
    }

    @Override
    public Vector<T> insert(int index, T element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        }
        if (index > length()) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e) on Vector of length " + length());
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i <= length(); i++) {
            if (i == index) {
                trie = trie.put(trie.size(), element);
            }
            if (i < length()) {
                trie = trie.put(trie.size(), get(i));
            }
        }
        return new Vector<>(trie);
    }

    @Override
    public Vector<T> insertAll(int index, java.lang.Iterable<? extends T> elements) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        }
        if (index > length()) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e) on Vector of length " + length());
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i <= length(); i++) {
            if (i == index) {
                for (T element : elements) {
                    trie = trie.put(trie.size(), element);
                }
            }
            if (i < length()) {
                trie = trie.put(trie.size(), get(i));
            }
        }
        return new Vector<>(trie);
    }

    @Override
    public Vector<T> intersperse(T element) {
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            if (i > 0) {
                trie = trie.put(trie.size(), element);
            }
            trie = trie.put(trie.size(), get(i));
        }
        return trie.size() == 0 ? empty() : new Vector<>(trie);
    }

    @Override
    public boolean isEmpty() {
        return trie.isEmpty();
    }

    @Override
    public boolean isTraversableAgain() {
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
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
    public int lastIndexOf(T element, int end) {
        for (int i = Math.min(end, length() - 1); i >= 0; i--) {
            if (Objects.equals(get(i), element)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int length() {
        return trie.size();
    }

    @Override
    public <U> Vector<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        HashArrayMappedTrie<Integer, U> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            trie = trie.put(i, mapper.apply(get(i)));
        }
        return trie.size() == 0 ? empty() : new Vector<>(trie);
    }

    @Override
    public Vector<T> padTo(int length, T element) {
        if(length <= length()) {
            return this;
        } else {
            return appendAll(Stream.gen(() -> element).take(length - length()));
        }
    }

    @Override
    public Tuple2<Vector<T>, Vector<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final java.util.List<T> left = new ArrayList<>(), right = new ArrayList<>();
        for (int i = 0; i < length(); i++) {
            T t = get(i);
            (predicate.test(t) ? left : right).add(t);
        }
        return Tuple.of(Vector.ofAll(left), Vector.ofAll(right));
    }

    @Override
    public Vector<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(head());
        }
        return this;
    }

    @Override
    public Vector<Vector<T>> permutations() {
        if (isEmpty()) {
            return Vector.empty();
        } else {
            final Vector<T> tail = tail();
            if (tail.isEmpty()) {
                return Vector.of(this);
            } else {
                final Vector<Vector<T>> zero = empty();
                // TODO: IntelliJ IDEA 14.1.1 needs a redundant cast here, jdk 1.8.0_40 compiles fine
                return distinct().foldLeft(zero, (xs, x) -> xs.appendAll(remove(x).permutations().map((Function<Vector<T>, Vector<T>>) l -> l.prepend(x))));
            }
        }
    }

    @Override
    public Vector<T> prepend(T element) {
        return insert(0, element);
    }

    @Override
    public Vector<T> prependAll(java.lang.Iterable<? extends T> elements) {
        return insertAll(0, elements);
    }

    @Override
    public Vector<T> remove(T element) {
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        boolean found = false;
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (found) {
                trie = trie.put(trie.size(), value);
            } else {
                if (element.equals(value)) {
                    found = true;
                } else {
                    trie = trie.put(trie.size(), value);
                }
            }
        }
        return trie.size() == length() ? this : trie.size() == 0 ? empty() : new Vector<>(trie);
    }

    @Override
    public Vector<T> removeFirst(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        boolean found = false;
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (found) {
                trie = trie.put(trie.size(), value);
            } else {
                if (predicate.test(value)) {
                    found = true;
                } else {
                    trie = trie.put(trie.size(), value);
                }
            }
        }
        return trie.size() == length() ? this : trie.size() == 0 ? empty() : new Vector<>(trie);
    }

    @Override
    public Vector<T> removeLast(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = length() - 1; i >= 0; i--) {
            if (predicate.test(get(i))) {
                return removeAt(i);
            }
        }
        return this;
    }

    @Override
    public Vector<T> removeAt(int indx) {
        if (indx < 0) {
            throw new IndexOutOfBoundsException("removeAt(" + indx + ")");
        }
        if (indx >= length()) {
            throw new IndexOutOfBoundsException("removeAt(" + indx + ")");
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            if (i != indx) {
                trie = trie.put(trie.size(), get(i));
            }
        }
        return trie.size() == 0 ? empty() : new Vector<>(trie);
    }

    @Override
    public Vector<T> removeAll(T element) {
        HashArrayMappedTrie<Integer, T> result = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (!element.equals(value)) {
                result = result.put(result.size(), value);
            }
        }
        return result.size() == length() ? this : (result.size() == 0 ? empty() : new Vector<>(result));

    }

    @Override
    public Vector<T> removeAll(java.lang.Iterable<? extends T> elements) {
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
        return found ? (result.size() == 0 ? empty() : new Vector<>(result)) : this;
    }

    @Override
    public Vector<T> replace(T currentElement, T newElement) {
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        boolean found = false;
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (found) {
                trie = trie.put(trie.size(), value);
            } else {
                if (currentElement.equals(value)) {
                    trie = trie.put(trie.size(), newElement);
                    found = true;
                } else {
                    trie = trie.put(trie.size(), value);
                }
            }
        }
        return found ? new Vector<>(trie) : this;
    }

    @Override
    public Vector<T> replaceAll(T currentElement, T newElement) {
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        boolean changed = false;
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (currentElement.equals(value)) {
                trie = trie.put(trie.size(), newElement);
                changed = true;
            } else {
                trie = trie.put(trie.size(), value);
            }
        }
        return changed ? (trie.size() == 0 ? empty() : new Vector<>(trie)) : this;
    }

    @Override
    public Vector<T> replaceAll(UnaryOperator<T> operator) {
        Objects.requireNonNull(operator, "operator is null");
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            trie = trie.put(trie.size(), operator.apply(get(i)));
        }
        return trie.size() == 0 ? empty() : new Vector<>(trie);
    }

    @Override
    public Vector<T> retainAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final Vector<T> keeped = Vector.ofAll(elements).distinct();
        HashArrayMappedTrie<Integer, T> result = HashArrayMappedTrie.empty();
        for (T element : this) {
            if (keeped.contains(element)) {
                result = result.put(result.size(), element);
            }
        }
        return result.size() == 0 ? empty() : new Vector<>(result);
    }

    @Override
    public Vector<T> reverse() {
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            trie = trie.put(i, get(length() - 1 - i));
        }
        return trie.size() == 0 ? empty() : new Vector<>(trie);
    }

    @Override
    public Vector<T> slice(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("slice(" + beginIndex + ")");
        }
        if (beginIndex > length()) {
            throw new IndexOutOfBoundsException("slice(" + beginIndex + ")");
        }
        return drop(beginIndex);
    }

    @Override
    public Vector<T> slice(int beginIndex, int endIndex) {
        if (beginIndex < 0 || beginIndex > endIndex || endIndex > length()) {
            throw new IndexOutOfBoundsException(
                    String.format("slice(%s, %s) on List of length %s", beginIndex, endIndex, length()));
        }
        if (beginIndex == endIndex) {
            return Vector.empty();
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = beginIndex; i < endIndex; i++) {
            trie = trie.put(trie.size(), get(i));
        }
        return trie.size() == 0 ? empty() : new Vector<>(trie);
    }

    @Override
    public Vector<T> sort() {
        return isEmpty() ? this : toJavaStream().sorted().collect(Vector.collector());
    }

    @Override
    public Vector<T> sort(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return isEmpty() ? this : toJavaStream().sorted(comparator).collect(Vector.collector());
    }

    @Override
    public Tuple2<Vector<T>, Vector<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return Tuple.of(takeWhile(predicate), dropWhile(predicate));
    }

    @Override
    public Tuple2<Vector<T>, Vector<T>> splitAt(int n) {
        return Tuple.of(take(n), drop(n));
    }

    @Override
    public Tuple2<Vector<T>, Vector<T>> splitAt(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Vector<T> init = takeWhile(predicate.negate());
        return Tuple.of(init, drop(init.length()));
    }

    @Override
    public Tuple2<Vector<T>, Vector<T>> splitAtInclusive(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        HashArrayMappedTrie<Integer, T> init = HashArrayMappedTrie.empty();
        for (T t : this) {
            init = init.put(init.size(), t);
            if (predicate.test(t)) {
                if (init.size() == length()) {
                    Tuple.of(this, empty());
                } else {
                    return Tuple.of(new Vector<>(init), drop(init.size()));
                }
            }
        }
        return Tuple.of(this, empty());
    }

    @Override
    public Spliterator<T> spliterator() {
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    public Vector<T> set(int index, T element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("set(" + index + ")");
        }
        if (index >= length()) {
            throw new IndexOutOfBoundsException("set(" + index + ")");
        }
        return new Vector<>(trie.put(index, element));
    }

    @Override
    public Vector<T> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty vector");
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 1; i < length(); i++) {
            trie = trie.put(i - 1, get(i));
        }
        return trie.size() == 0 ? empty() : new Vector<>(trie);
    }

    @Override
    public Option<Vector<T>> tailOption() {
        if (isEmpty()) {
            return None.instance();
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 1; i < length(); i++) {
            trie = trie.put(i - 1, get(i));
        }
        return new Some<>(trie.size() == 0 ? empty() : new Vector<>(trie));
    }

    @Override
    public Vector<T> take(int n) {
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
        return new Vector<>(trie);
    }

    @Override
    public Vector<T> takeRight(int n) {
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
        return new Vector<>(trie);
    }

    @Override
    public Vector<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            T value = get(i);
            if (!predicate.test(value)) {
                break;
            }
            trie = trie.put(i, get(i));
        }
        return trie.size() == length() ? this : trie.size() == 0 ? empty() : new Vector<>(trie);
    }

    @Override
    public <U> Vector<U> unit(java.lang.Iterable<? extends U> iterable) {
        return Vector.ofAll(iterable);
    }

    @Override
    public <T1, T2> Tuple2<Vector<T1>, Vector<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        HashArrayMappedTrie<Integer, T1> xs = HashArrayMappedTrie.empty();
        HashArrayMappedTrie<Integer, T2> ys = HashArrayMappedTrie.empty();
        for (T element : this) {
            final Tuple2<? extends T1, ? extends T2> t = unzipper.apply(element);
            xs = xs.put(xs.size(), t._1);
            ys = ys.put(ys.size(), t._2);
        }
        return Tuple.of(xs.size() == 0 ? empty() : new Vector<>(xs), ys.size() == 0 ? empty() : new Vector<>(ys));
    }

    @Override
    public <U> Vector<Tuple2<T, U>> zip(java.lang.Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        HashArrayMappedTrie<Integer, Tuple2<T, U>> result = HashArrayMappedTrie.empty();
        Iterator<T> list1 = iterator();
        java.util.Iterator<U> list2 = that.iterator();
        while (list1.hasNext() && list2.hasNext()) {
            result = result.put(result.size(), Tuple.of(list1.next(), list2.next()));
        }
        return result.size() == 0 ? empty() : new Vector<>(result);
    }

    @Override
    public <U> Vector<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        HashArrayMappedTrie<Integer, Tuple2<T, U>> result = HashArrayMappedTrie.empty();
        Iterator<T> list1 = iterator();
        java.util.Iterator<U> list2 = that.iterator();
        while (list1.hasNext() || list2.hasNext()) {
            final T elem1 = list1.hasNext() ? list1.next() : thisElem;
            final U elem2 = list2.hasNext() ? list2.next() : thatElem;
            result = result.put(result.size(), Tuple.of(elem1, elem2));
        }
        return result.size() == 0 ? empty() : new Vector<>(result);
    }

    @Override
    public Vector<Tuple2<T, Integer>> zipWithIndex() {
        HashArrayMappedTrie<Integer, Tuple2<T, Integer>> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            trie = trie.put(i, Tuple.of(get(i), i));
        }
        return trie.size() == 0 ? empty() : new Vector<>(trie);
    }

    private Object readResolve() {
        return isEmpty() ? EMPTY : this;
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
        return mkString(", ", "Vector(", ")");
    }
}
