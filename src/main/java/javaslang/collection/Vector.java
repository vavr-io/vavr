/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.collection.VectorModule.Combinations;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * Vector is the default Seq implementation. It provides the best performance in between Array (with constant time element access)
 * and List (with constant time element addition).
 *
 * @param <T> Component type of the Vector.
 * @author Ruslan Sennov
 * @since 2.0.0
 */
public final class Vector<T> implements IndexedSeq<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Vector<?> EMPTY = new Vector<>(HashArrayMappedTrie.empty());

    private final HashArrayMappedTrie<Integer, T> trie;
    private final int indexShift;
    private final transient Lazy<Integer> hashCode = Lazy.of(() -> Traversable.hash(this));

    private Vector(HashArrayMappedTrie<Integer, T> trie) {
        this(0, trie);
    }

    private Vector(int indexShift, HashArrayMappedTrie<Integer, T> trie) {
        this.trie = trie;
        this.indexShift = indexShift;
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
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.Vector}.
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
     * Creates a Vector of the given elements.
     *
     * @param <T>      Component type of the Vector.
     * @param elements Zero or more elements.
     * @return A vector containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    public static <T> Vector<T> ofAll(T... elements) {
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
        return Vector.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Vector based on the elements of a byte array.
     *
     * @param array a byte array
     * @return A new Vector of Byte values
     */
    public static Vector<Byte> ofAll(byte[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Vector based on the elements of a char array.
     *
     * @param array a char array
     * @return A new Vector of Character values
     */
    public static Vector<Character> ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Vector based on the elements of a double array.
     *
     * @param array a double array
     * @return A new Vector of Double values
     */
    public static Vector<Double> ofAll(double[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Vector based on the elements of a float array.
     *
     * @param array a float array
     * @return A new Vector of Float values
     */
    public static Vector<Float> ofAll(float[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Vector based on the elements of an int array.
     *
     * @param array an int array
     * @return A new Vector of Integer values
     */
    public static Vector<Integer> ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Vector based on the elements of a long array.
     *
     * @param array a long array
     * @return A new Vector of Long values
     */
    public static Vector<Long> ofAll(long[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Vector based on the elements of a short array.
     *
     * @param array a short array
     * @return A new Vector of Short values
     */
    public static Vector<Short> ofAll(short[] array) {
        Objects.requireNonNull(array, "array is null");
        return Vector.ofAll(Iterator.ofAll(array));
    }

    public static Vector<Character> range(char from, char toExclusive) {
        return Vector.ofAll(Iterator.range(from, toExclusive));
    }

    public static Vector<Character> rangeBy(char from, char toExclusive, int step) {
        return Vector.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static Vector<Double> rangeBy(double from, double toExclusive, double step) {
        return Vector.ofAll(Iterator.rangeBy(from, toExclusive, step));
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
        return Vector.ofAll(Iterator.range(from, toExclusive));
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
        return Vector.ofAll(Iterator.rangeBy(from, toExclusive, step));
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
        return Vector.ofAll(Iterator.range(from, toExclusive));
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
        return Vector.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static Vector<Character> rangeClosed(char from, char toInclusive) {
        return Vector.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    public static Vector<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return Vector.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    public static Vector<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return Vector.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
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
        return Vector.ofAll(Iterator.rangeClosed(from, toInclusive));
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
        return Vector.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
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
        return Vector.ofAll(Iterator.rangeClosed(from, toInclusive));
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
        return Vector.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    public Vector<T> append(T element) {
        return new Vector<>(indexShift, trie.put(length() + indexShift, element));
    }

    @Override
    public Vector<T> appendAll(java.lang.Iterable<? extends T> elements) {
        HashArrayMappedTrie<Integer, T> result = trie;
        for (T element : elements) {
            result = result.put(result.size() + indexShift, element);
        }
        return new Vector<>(indexShift, result);
    }

    @Override
    public Vector<T> clear() {
        return Vector.empty();
    }

    @Override
    public Vector<Vector<T>> combinations() {
        return Vector.rangeClosed(0, length()).map(this::combinations).flatMap(Function.identity());
    }

    @Override
    public Vector<Vector<T>> combinations(int k) {
        return Combinations.apply(this, Math.max(k, 0));
    }

    @Override
    public Vector<Tuple2<T, T>> crossProduct() {
        return crossProduct(this);
    }

    @Override
    public Vector<Vector<T>> crossProduct(int power) {
        return Collections.crossProduct(this, power).map(Vector::ofAll).toVector();
    }

    @Override
    public <U> Vector<Tuple2<T, U>> crossProduct(java.lang.Iterable<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        final Vector<U> other = Vector.ofAll(that);
        return flatMap(a -> other.map((Function<U, Tuple2<T, U>>) b -> Tuple.of(a, b)));
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
        return trie.isEmpty() ? empty() : new Vector<>(trie);
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
    public Vector<T> dropUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
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
            return trie.isEmpty() ? empty() : new Vector<>(trie);
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
            return trie.isEmpty() ? empty() : new Vector<>(trie);
        }
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException("get(" + index + ")");
        }
        return trie.get(index + indexShift).get();
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
        Objects.requireNonNull(classifier, "classifier is null");
        return iterator().groupBy(classifier).map((c, it) -> Tuple.of(c, Vector.ofAll(it)));
    }

    @Override
    public Iterator<Vector<T>> grouped(int size) {
        return sliding(size, size);
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
        return new Vector<>(indexShift, trie.remove(length() + indexShift - 1));
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
        return trie.isEmpty() ? empty() : new Vector<>(trie);
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
        return new AbstractIterator<T>() {
            private int index = indexShift;
            private final int size = trie.size() + indexShift;

            @Override
            public boolean hasNext() {
                return index < size;
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
        return trie.isEmpty() ? empty() : new Vector<>(trie);
    }

    @Override
    public Vector<T> padTo(int length, T element) {
        if (length <= length()) {
            return this;
        } else {
            return appendAll(Iterator.gen(() -> element).take(length - length()));
        }
    }

    @Override
    public Vector<T> patch(int from, java.lang.Iterable<? extends T> that, int replaced) {
        from = from < 0 ? 0 : from;
        replaced = replaced < 0 ? 0 : replaced;
        Vector<T> result = take(from).appendAll(that);
        from += replaced;
        result = result.appendAll(drop(from));
        return result;
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
                return distinct().foldLeft(zero, (xs, x) -> {
                    final Function<Vector<T>, Vector<T>> prepend = l -> l.prepend(x);
                    return xs.appendAll(remove(x).permutations().map(prepend));
                });
            }
        }
    }

    @Override
    public Vector<T> prepend(T element) {
        final int newIndexShift = indexShift - 1;
        return new Vector<>(newIndexShift, trie.put(newIndexShift, element));
    }

    @Override
    public Vector<T> prependAll(java.lang.Iterable<? extends T> elements) {
        List<T> list = List.ofAll(elements);
        final int newIndexShift = indexShift - list.length();
        HashArrayMappedTrie<Integer, T> newTrie = trie;
        for (int i = newIndexShift; !list.isEmpty(); i++) {
            newTrie = newTrie.put(i, list.head());
            list = list.tail();
        }
        return new Vector<>(newIndexShift, newTrie);
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
        return trie.size() == length() ? this : trie.isEmpty() ? empty() : new Vector<>(trie);
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
        return trie.size() == length() ? this : trie.isEmpty() ? empty() : new Vector<>(trie);
    }

    @Override
    public Vector<T> removeLast(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (int i = length() - 1; i >= 0; i--) {
            if (predicate.test(get(i))) {
                return removeAt(i);
            }
        }
        return this;
    }

    @Override
    public Vector<T> removeAt(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("removeAt(" + index + ")");
        }
        if (index >= length()) {
            throw new IndexOutOfBoundsException("removeAt(" + index + ")");
        }
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            if (i != index) {
                trie = trie.put(trie.size(), get(i));
            }
        }
        return trie.isEmpty() ? empty() : new Vector<>(trie);
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
        return result.size() == length() ? this : (result.isEmpty() ? empty() : new Vector<>(result));

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
        return found ? (result.isEmpty() ? empty() : new Vector<>(result)) : this;
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
        return changed ? (trie.isEmpty() ? empty() : new Vector<>(trie)) : this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Vector<T> retainAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        // TODO(Eclipse bug): remove cast + SuppressWarnings
        final Vector<T> kept = (Vector<T>) (Object) Vector.ofAll(elements).distinct();
        HashArrayMappedTrie<Integer, T> result = HashArrayMappedTrie.empty();
        for (T element : this) {
            if (kept.contains(element)) {
                result = result.put(result.size(), element);
            }
        }
        return result.isEmpty() ? empty() : new Vector<>(result);
    }

    @Override
    public Vector<T> reverse() {
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = 0; i < length(); i++) {
            trie = trie.put(i, get(length() - 1 - i));
        }
        return trie.isEmpty() ? empty() : new Vector<>(trie);
    }

    @Override
    public Vector<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation) {
        return scanLeft(zero, operation);
    }

    @Override
    public <U> Vector<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanLeft(this, zero, operation, Vector.empty(), Vector::append, Function.identity());
    }

    @Override
    public <U> Vector<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanRight(this, zero, operation, Vector.empty(), Vector::prepend, Function.identity());
    }

    @Override
    public Vector<T> slice(int beginIndex, int endIndex) {
        if (beginIndex >= endIndex || beginIndex >= length() || isEmpty()) {
            return Vector.empty();
        }
        if (beginIndex <= 0 && endIndex >= length()) {
            return this;
        }
        final int index = Math.max(beginIndex, 0);
        final int length = Math.min(endIndex, length());
        HashArrayMappedTrie<Integer, T> trie = HashArrayMappedTrie.empty();
        for (int i = index; i < length; i++) {
            trie = trie.put(trie.size(), get(i));
        }
        return trie.isEmpty() ? empty() : new Vector<>(trie);
    }

    @Override
    public Iterator<Vector<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    public Iterator<Vector<T>> sliding(int size, int step) {
        return iterator().sliding(size, step).map(Vector::ofAll);
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
    public <U extends Comparable<? super U>> Vector<T> sortBy(Function<? super T, ? extends U> mapper) {
        return sortBy(U::compareTo, mapper);
    }

    @Override
    public <U> Vector<T> sortBy(Comparator<? super U> comparator, Function<? super T, ? extends U> mapper) {
        final Function<? super T, ? extends U> domain = Function1.of(mapper::apply).memoized();
        return toJavaStream()
                .sorted((e1, e2) -> comparator.compare(domain.apply(e1), domain.apply(e2)))
                .collect(collector());
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
    public Vector<T> subSequence(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("slice(" + beginIndex + ")");
        }
        if (beginIndex > length()) {
            throw new IndexOutOfBoundsException("slice(" + beginIndex + ")");
        }
        return drop(beginIndex);
    }

    @Override
    public Vector<T> subSequence(int beginIndex, int endIndex) {
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
        return trie.isEmpty() ? empty() : new Vector<>(trie);
    }

    @Override
    public Vector<T> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty vector");
        }
        if (length() == 1) {
            return empty();
        } else {
            final int newIndexShift = indexShift + 1;
            return new Vector<>(newIndexShift, trie.remove(indexShift));
        }
    }

    @Override
    public Option<Vector<T>> tailOption() {
        if (isEmpty()) {
            return None.instance();
        }
        if (length() == 1) {
            return new Some<>(empty());
        } else {
            return new Some<>(tail());
        }
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
    public Vector<T> takeUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
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
        return trie.size() == length() ? this : trie.isEmpty() ? empty() : new Vector<>(trie);
    }

    @Override
    public <U> Vector<U> unit(java.lang.Iterable<? extends U> iterable) {
        return Vector.ofAll(iterable);
    }

    @Override
    public <T1, T2> Tuple2<Vector<T1>, Vector<T2>> unzip(
            Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        HashArrayMappedTrie<Integer, T1> xs = HashArrayMappedTrie.empty();
        HashArrayMappedTrie<Integer, T2> ys = HashArrayMappedTrie.empty();
        for (T element : this) {
            final Tuple2<? extends T1, ? extends T2> t = unzipper.apply(element);
            xs = xs.put(xs.size(), t._1);
            ys = ys.put(ys.size(), t._2);
        }
        return Tuple.of(new Vector<>(xs), new Vector<>(ys));
    }

    @Override
    public <T1, T2, T3> Tuple3<Vector<T1>, Vector<T2>, Vector<T3>> unzip3(Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        HashArrayMappedTrie<Integer, T1> xs = HashArrayMappedTrie.empty();
        HashArrayMappedTrie<Integer, T2> ys = HashArrayMappedTrie.empty();
        HashArrayMappedTrie<Integer, T3> zs = HashArrayMappedTrie.empty();
        for (T element : this) {
            final Tuple3<? extends T1, ? extends T2, ? extends T3> t = unzipper.apply(element);
            xs = xs.put(xs.size(), t._1);
            ys = ys.put(ys.size(), t._2);
            zs = zs.put(zs.size(), t._3);
        }
        return Tuple.of(new Vector<>(xs), new Vector<>(ys), new Vector<>(zs));
    }

    @Override
    public Vector<T> update(int index, T element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("update(" + index + ")");
        }
        if (index >= length()) {
            throw new IndexOutOfBoundsException("update(" + index + ")");
        }
        return new Vector<>(indexShift, trie.put(index + indexShift, element));
    }

    @Override
    public <U> Vector<Tuple2<T, U>> zip(java.lang.Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        return Vector.ofAll(iterator().zip(that));
    }

    @Override
    public <U> Vector<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return Vector.ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    public Vector<Tuple2<T, Integer>> zipWithIndex() {
        return Vector.ofAll(iterator().zipWithIndex());
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
        return mkString("Vector(", ", ", ")");
    }
}

interface VectorModule {

    final class Combinations {

        static <T> Vector<Vector<T>> apply(Vector<T> elements, int k) {
            if (k == 0) {
                return Vector.of(Vector.empty());
            } else {
                return elements.zipWithIndex().flatMap(t -> apply(elements.drop(t._2 + 1), (k - 1))
                        .map((Vector<T> c) -> c.prepend(t._1))
                );
            }
        }
    }
}