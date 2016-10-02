/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.collection.ArrayModule.Combinations;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

import static java.util.Arrays.*;
import static java.util.Arrays.copyOf;

/**
 * Array is a Traversable wrapper for {@code Object[]} containing elements of type {@code T}.
 *
 * @param <T> Component type
 * @author Ruslan Sennov, Daniel Dietrich
 * @since 2.0.0
 */
public final class Array<T> implements Kind1<Array<?>, T>, IndexedSeq<T>, Serializable {
    private static final long serialVersionUID = 1L;

    private static final Array<?> EMPTY = new Array<>(new Object[0]);

    private final Object[] delegate;

    private Array(Object[] delegate) {
        this.delegate = delegate;
    }

    static <T> Array<T> wrap(Object[] array) {
        return array.length == 0
               ? empty()
               : new Array<T>(array);
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.Array}.
     *
     * @param <T> Component type of the Array.
     * @return A {@link javaslang.collection.Array} Collector.
     */
    public static <T> Collector<T, ArrayList<T>, Array<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, Array<T>> finisher = Array::ofAll;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    @SuppressWarnings("unchecked")
    public static <T> Array<T> empty() {
        return (Array<T>) EMPTY;
    }

    /**
     * Narrows a widened {@code Array<? extends T>} to {@code Array<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param array An {@code Array}.
     * @param <T>   Component type of the {@code Array}.
     * @return the given {@code array} instance as narrowed type {@code Array<T>}.
     */
    @SuppressWarnings("unchecked")
    public static <T> Array<T> narrow(Array<? extends T> array) {
        return (Array<T>) array;
    }

    /**
     * Returns a singleton {@code Array}, i.e. a {@code Array} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new Array instance containing the given element
     */
    public static <T> Array<T> of(T element) {
        return wrap(new Object[] { element });
    }

    /**
     * Creates a Array of the given elements.
     *
     * @param <T>      Component type of the Array.
     * @param elements Zero or more elements.
     * @return A Array containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Array<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return wrap(copyOf(elements, elements.length));
    }

    /**
     * Creates a Array of the given elements.
     * <p>
     * The resulting Array has the same iteration order as the given iterable of elements
     * if the iteration order of the elements is stable.
     *
     * @param <T>      Component type of the Array.
     * @param elements An Iterable of elements.
     * @return A Array containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("unchecked")
    public static <T> Array<T> ofAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return elements instanceof Array
               ? (Array<T>) elements
               : wrap(toArray(elements));
    }

    /**
     * Creates a Array that contains the elements of the given {@link java.util.stream.Stream}.
     *
     * @param javaStream A {@link java.util.stream.Stream}
     * @param <T>        Component type of the Stream.
     * @return A Array containing the given elements in the same order.
     */
    public static <T> Array<T> ofAll(java.util.stream.Stream<? extends T> javaStream) {
        Objects.requireNonNull(javaStream, "javaStream is null");
        return wrap(javaStream.toArray());
    }

    /**
     * Creates a Array based on the elements of a boolean array.
     *
     * @param array a boolean array
     * @return A new Array of Boolean values
     */
    public static Array<Boolean> ofAll(boolean[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a byte array.
     *
     * @param array a byte array
     * @return A new Array of Byte values
     */
    public static Array<Byte> ofAll(byte[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a char array.
     *
     * @param array a char array
     * @return A new Array of Character values
     */
    public static Array<Character> ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a double array.
     *
     * @param array a double array
     * @return A new Array of Double values
     */
    public static Array<Double> ofAll(double[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a float array.
     *
     * @param array a float array
     * @return A new Array of Float values
     */
    public static Array<Float> ofAll(float[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of an int array.
     *
     * @param array an int array
     * @return A new Array of Integer values
     */
    public static Array<Integer> ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a long array.
     *
     * @param array a long array
     * @return A new Array of Long values
     */
    public static Array<Long> ofAll(long[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a short array.
     *
     * @param array a short array
     * @return A new Array of Short values
     */
    public static Array<Short> ofAll(short[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(Iterator.ofAll(array));
    }

    /**
     * Returns an Array containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param <T> Component type of the Array
     * @param n   The number of elements in the Array
     * @param f   The Function computing element values
     * @return An Array consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    public static <T> Array<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        return Collections.tabulate(n, f, empty(), Array::of);
    }

    /**
     * Returns an Array containing {@code n} values supplied by a given Supplier {@code s}.
     *
     * @param <T> Component type of the Array
     * @param n   The number of elements in the Array
     * @param s   The Supplier computing element values
     * @return An Array of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    public static <T> Array<T> fill(int n, Supplier<? extends T> s) {
        Objects.requireNonNull(s, "s is null");
        return Collections.fill(n, s, empty(), Array::of);
    }

    public static Array<Character> range(char from, char toExclusive) {
        return ofAll(Iterator.range(from, toExclusive));
    }

    public static Array<Character> rangeBy(char from, char toExclusive, int step) {
        return ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @GwtIncompatible
    public static Array<Double> rangeBy(double from, double toExclusive, double step) {
        return ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a Array of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Array.range(0, 0)  // = Array()
     * Array.range(2, 0)  // = Array()
     * Array.range(-2, 2) // = Array(-2, -1, 0, 1)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or the empty range if {@code from >= toExclusive}
     */
    public static Array<Integer> range(int from, int toExclusive) {
        return ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a Array of int numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Array.rangeBy(1, 3, 1)  // = Array(1, 2)
     * Array.rangeBy(1, 4, 2)  // = Array(1, 3)
     * Array.rangeBy(4, 1, -2) // = Array(4, 2)
     * Array.rangeBy(4, 1, 2)  // = Array()
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
    public static Array<Integer> rangeBy(int from, int toExclusive, int step) {
        return ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a Array of long numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Array.range(0L, 0L)  // = Array()
     * Array.range(2L, 0L)  // = Array()
     * Array.range(-2L, 2L) // = Array(-2L, -1L, 0L, 1L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of long values as specified or the empty range if {@code from >= toExclusive}
     */
    public static Array<Long> range(long from, long toExclusive) {
        return ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a Array of long numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Array.rangeBy(1L, 3L, 1L)  // = Array(1L, 2L)
     * Array.rangeBy(1L, 4L, 2L)  // = Array(1L, 3L)
     * Array.rangeBy(4L, 1L, -2L) // = Array(4L, 2L)
     * Array.rangeBy(4L, 1L, 2L)  // = Array()
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
    public static Array<Long> rangeBy(long from, long toExclusive, long step) {
        return ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static Array<Character> rangeClosed(char from, char toInclusive) {
        return ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    public static Array<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @GwtIncompatible
    public static Array<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a Array of int numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Array.rangeClosed(0, 0)  // = Array(0)
     * Array.rangeClosed(2, 0)  // = Array()
     * Array.rangeClosed(-2, 2) // = Array(-2, -1, 0, 1, 2)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or the empty range if {@code from > toInclusive}
     */
    public static Array<Integer> rangeClosed(int from, int toInclusive) {
        return ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a Array of int numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Array.rangeClosedBy(1, 3, 1)  // = Array(1, 2, 3)
     * Array.rangeClosedBy(1, 4, 2)  // = Array(1, 3)
     * Array.rangeClosedBy(4, 1, -2) // = Array(4, 2)
     * Array.rangeClosedBy(4, 1, 2)  // = Array()
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
    public static Array<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a Array of long numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Array.rangeClosed(0L, 0L)  // = Array(0L)
     * Array.rangeClosed(2L, 0L)  // = Array()
     * Array.rangeClosed(-2L, 2L) // = Array(-2L, -1L, 0L, 1L, 2L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of long values as specified or the empty range if {@code from > toInclusive}
     */
    public static Array<Long> rangeClosed(long from, long toInclusive) {
        return ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a Array of long numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Array.rangeClosedBy(1L, 3L, 1L)  // = Array(1L, 2L, 3L)
     * Array.rangeClosedBy(1L, 4L, 2L)  // = Array(1L, 3L)
     * Array.rangeClosedBy(4L, 1L, -2L) // = Array(4L, 2L)
     * Array.rangeClosedBy(4L, 1L, 2L)  // = Array()
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
    public static Array<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a Array from a seed value and a function.
     * The function takes the seed at first.
     * The function should return {@code None} when it's
     * done generating the Array, otherwise {@code Some} {@code Tuple}
     * of the element for the next call and the value to add to the
     * resulting Array.
     * <p>
     * Example:
     * <pre>
     * <code>
     * Array.unfoldRight(10, x -&gt; x == 0
     *             ? Option.none()
     *             : Option.of(new Tuple2&lt;gt;(x, x-1)));
     * // Array(10, 9, 8, 7, 6, 5, 4, 3, 2, 1))
     * </code>
     * </pre>
     *
     * @param <T>  type of seeds
     * @param <U>  type of unfolded values
     * @param seed the start value for the iteration
     * @param f    the function to get the next step of the iteration
     * @return an Array with the values built up by the iteration
     * @throws NullPointerException if {@code f} is null
     */
    public static <T, U> Array<U> unfoldRight(T seed, Function<? super T, Option<Tuple2<? extends U, ? extends T>>> f) {
        return Iterator.unfoldRight(seed, f).toArray();
    }

    /**
     * Creates an Array from a seed value and a function.
     * The function takes the seed at first.
     * The function should return {@code None} when it's
     * done generating the list, otherwise {@code Some} {@code Tuple}
     * of the value to add to the resulting list and
     * the element for the next call.
     * <p>
     * Example:
     * <pre>
     * <code>
     * Array.unfoldLeft(10, x -&gt; x == 0
     *             ? Option.none()
     *             : Option.of(new Tuple2&lt;gt;(x-1, x)));
     * // Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
     * </code>
     * </pre>
     *
     * @param <T>  type of seeds
     * @param <U>  type of unfolded values
     * @param seed the start value for the iteration
     * @param f    the function to get the next step of the iteration
     * @return an Array with the values built up by the iteration
     * @throws NullPointerException if {@code f} is null
     */
    public static <T, U> Array<U> unfoldLeft(T seed, Function<? super T, Option<Tuple2<? extends T, ? extends U>>> f) {
        return Iterator.unfoldLeft(seed, f).toArray();
    }

    /**
     * Creates an Array from a seed value and a function.
     * The function takes the seed at first.
     * The function should return {@code None} when it's
     * done generating the list, otherwise {@code Some} {@code Tuple}
     * of the value to add to the resulting list and
     * the element for the next call.
     * <p>
     * Example:
     * <pre>
     * <code>
     * Array.unfold(10, x -&gt; x == 0
     *             ? Option.none()
     *             : Option.of(new Tuple2&lt;gt;(x-1, x)));
     * // Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
     * </code>
     * </pre>
     *
     * @param <T>  type of seeds and unfolded values
     * @param seed the start value for the iteration
     * @param f    the function to get the next step of the iteration
     * @return an Array with the values built up by the iteration
     * @throws NullPointerException if {@code f} is null
     */
    public static <T> Array<T> unfold(T seed, Function<? super T, Option<Tuple2<? extends T, ? extends T>>> f) {
        return Iterator.unfold(seed, f).toArray();
    }

    @Override
    public Array<T> append(T element) {
        final Object[] copy = copyOf(delegate, delegate.length + 1);
        copy[delegate.length] = element;
        return wrap(copy);
    }

    @Override
    public Array<T> appendAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final Object[] source = toArray(elements);
        if (source.length == 0) {
            return this;
        } else {
            final Object[] arr = copyOf(delegate, delegate.length + source.length);
            System.arraycopy(source, 0, arr, delegate.length, source.length);
            return wrap(arr);
        }
    }

    @Override
    public boolean hasDefiniteSize() {
        return true;
    }

    @Override
    public boolean isTraversableAgain() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<T> iterator() {
        return new AbstractIterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < delegate.length;
            }

            @Override
            public T getNext() {
                return (T) delegate[index++];
            }
        };
    }

    @Override
    public Array<Array<T>> combinations() {
        return rangeClosed(0, length()).map(this::combinations).flatMap(Function.identity());
    }

    @Override
    public Array<Array<T>> combinations(int k) {
        return Combinations.apply(this, Math.max(k, 0));
    }

    @Override
    public Iterator<Array<T>> crossProduct(int power) {
        return Collections.crossProduct(empty(), this, power);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException("get(" + index + ")");
        }
        return (T) delegate[index];
    }

    @Override
    public Array<T> distinct() {
        return distinctBy(Function.identity());
    }

    @Override
    public Array<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        final java.util.Set<T> seen = new java.util.TreeSet<>(comparator);
        return filter(seen::add);
    }

    @Override
    public <U> Array<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        final java.util.Set<U> seen = new java.util.HashSet<>();
        return filter(t -> seen.add(keyExtractor.apply(t)));
    }

    @Override
    public Array<T> drop(int n) {
        if (n <= 0) {
            return this;
        } else if (n >= length()) {
            return empty();
        } else {
            final Object[] arr = new Object[delegate.length - n];
            System.arraycopy(delegate, n, arr, 0, arr.length);
            return wrap(arr);
        }
    }

    @Override
    public Array<T> dropRight(int n) {
        if (n <= 0) {
            return this;
        } else if (n >= length()) {
            return empty();
        } else {
            return wrap(copyOf(delegate, delegate.length - n));
        }
    }

    @Override
    public Array<T> dropUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @Override
    public Array<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (int i = 0; i < length(); i++) {
            if (!predicate.test(get(i))) {
                return drop(i);
            }
        }
        return empty();
    }

    @Override
    public Array<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final java.util.List<T> list = new ArrayList<>();
        for (T t : this) {
            if (predicate.test(t)) {
                list.add(t);
            }
        }
        if (list.isEmpty()) {
            return empty();
        } else if (list.size() == size()) {
            return this;
        } else {
            return wrap(list.toArray());
        }
    }

    @Override
    public <U> Array<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return empty();
        } else {
            final java.util.List<U> list = new ArrayList<>();
            for (T t : this) {
                for (U u : mapper.apply(t)) {
                    list.add(u);
                }
            }
            return wrap(toArray(list));
        }
    }

    @Override
    public <C> Map<C, Array<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return Collections.groupBy(this, classifier, Array::ofAll);
    }

    @Override
    public Iterator<Array<T>> grouped(int size) {
        return sliding(size, size);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head on empty Array");
        } else {
            return (T) delegate[0];
        }
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
    public Array<T> init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init of empty Array");
        }
        return dropRight(1);
    }

    @Override
    public Option<Array<T>> initOption() {
        return isEmpty() ? Option.none() : Option.some(init());
    }

    @Override
    public boolean isEmpty() {
        return delegate.length == 0;
    }

    private Object readResolve() {
        return isEmpty() ? EMPTY : this;
    }

    @Override
    public Array<T> insert(int index, T element) {
        if (index < 0 || index > length()) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e) on Array of length " + length());
        }
        final Object[] arr = new Object[delegate.length + 1];
        System.arraycopy(delegate, 0, arr, 0, index);
        arr[index] = element;
        System.arraycopy(delegate, index, arr, index + 1, delegate.length - index);
        return wrap(arr);
    }

    @Override
    public Array<T> insertAll(int index, Iterable<? extends T> elements) {
        if (index < 0 || index > length()) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e) on Array of length " + length());
        }
        final Object[] list = toArray(elements);
        if (list.length == 0) {
            return this;
        } else {
            final Object[] arr = new Object[delegate.length + list.length];
            System.arraycopy(delegate, 0, arr, 0, index);
            System.arraycopy(list, 0, arr, index, list.length);
            System.arraycopy(delegate, index, arr, index + list.length, delegate.length - index);
            return wrap(arr);
        }
    }

    @Override
    public Array<T> intersperse(T element) {
        if (delegate.length <= 1) {
            return this;
        } else {
            final Object[] arr = new Object[delegate.length * 2 - 1];
            for (int i = 0; i < delegate.length; i++) {
                arr[i * 2] = delegate[i];
                if (i > 0) {
                    arr[i * 2 - 1] = element;
                }
            }
            return wrap(arr);
        }
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
        return delegate.length;
    }

    @Override
    public <U> Array<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final Object[] arr = new Object[length()];
        for (int i = 0; i < delegate.length; i++) {
            arr[i] = mapper.apply(get(i));
        }
        return wrap(arr);
    }

    @Override
    public Array<T> padTo(int length, T element) {
        final int actualLength = length();
        if (length <= actualLength) {
            return this;
        } else {
            return appendAll(Iterator.continually(element).take(length - actualLength));
        }
    }

    @Override
    public Array<T> leftPadTo(int length, T element) {
        final int actualLength = length();
        if (length <= actualLength) {
            return this;
        } else {
            return prependAll(Iterator.continually(element).take(length - actualLength));
        }
    }

    @Override
    public Array<T> patch(int from, Iterable<? extends T> that, int replaced) {
        from = from < 0 ? 0 : from;
        replaced = replaced < 0 ? 0 : replaced;
        Array<T> result = take(from).appendAll(that);
        from += replaced;
        result = result.appendAll(drop(from));
        return result;
    }

    @Override
    public Tuple2<Array<T>, Array<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final java.util.List<T> left = new ArrayList<>(), right = new ArrayList<>();
        for (T t : this) {
            (predicate.test(t) ? left : right).add(t);
        }
        return Tuple.of(ofAll(left), ofAll(right));
    }

    @Override
    public Array<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(head());
        }
        return this;
    }

    @Override
    public Array<Array<T>> permutations() {
        if (isEmpty()) {
            return empty();
        } else if (delegate.length == 1) {
            return of(this);
        } else {
            Array<Array<T>> results = empty();
            for (T t : distinct()) {
                for (Array<T> ts : remove(t).permutations()) {
                    results = results.append(of(t).appendAll(ts));
                }
            }
            return results;
        }
    }

    @Override
    public Array<T> prepend(T element) {
        return insert(0, element);
    }

    @Override
    public Array<T> prependAll(Iterable<? extends T> elements) {
        return insertAll(0, elements);
    }

    @Override
    public Array<T> remove(T element) {
        int index = -1;
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (element.equals(value)) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            return this;
        } else {
            return removeAt(index);
        }
    }

    @Override
    public Array<T> removeFirst(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        int found = -1;
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (predicate.test(value)) {
                found = i;
                break;
            }
        }
        if (found < 0) {
            return this;
        } else {
            return removeAt(found);
        }
    }

    @Override
    public Array<T> removeLast(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        int found = -1;
        for (int i = length() - 1; i >= 0; i--) {
            final T value = get(i);
            if (predicate.test(value)) {
                found = i;
                break;
            }
        }
        if (found < 0) {
            return this;
        } else {
            return removeAt(found);
        }
    }

    @Override
    public Array<T> removeAt(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("removeAt(" + index + ")");
        } else if (index >= length()) {
            throw new IndexOutOfBoundsException("removeAt(" + index + ")");
        } else {
            final Object[] arr = new Object[length() - 1];
            System.arraycopy(delegate, 0, arr, 0, index);
            System.arraycopy(delegate, index + 1, arr, index, length() - index - 1);
            return wrap(arr);
        }
    }

    @Override
    public Array<T> removeAll(T element) {
        return Collections.removeAll(this, element);
    }

    @Override
    public Array<T> removeAll(Iterable<? extends T> elements) {
        return Collections.removeAll(this, elements);
    }

    @Override
    public Array<T> removeAll(Predicate<? super T> predicate) {
        return Collections.removeAll(this, predicate);
    }

    @Override
    public Array<T> replace(T currentElement, T newElement) {
        final Object[] arr = new Object[length()];
        boolean found = false;
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (found) {
                arr[i] = delegate[i];
            } else {
                if (currentElement.equals(value)) {
                    arr[i] = newElement;
                    found = true;
                } else {
                    arr[i] = delegate[i];
                }
            }
        }
        return found ? wrap(arr) : this;
    }

    @Override
    public Array<T> replaceAll(T currentElement, T newElement) {
        final Object[] arr = new Object[length()];
        boolean changed = false;
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (currentElement.equals(value)) {
                arr[i] = newElement;
                changed = true;
            } else {
                arr[i] = delegate[i];
            }
        }
        return changed ? wrap(arr) : this;
    }

    @Override
    public Array<T> retainAll(Iterable<? extends T> elements) {
        return Collections.retainAll(this, elements);
    }

    @Override
    public Array<T> reverse() {
        final Object[] arr = new Object[delegate.length];
        for (int i = 0; i < delegate.length; i++) {
            arr[delegate.length - 1 - i] = delegate[i];
        }
        return wrap(arr);
    }

    @Override
    public Array<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation) {
        return scanLeft(zero, operation);
    }

    @Override
    public <U> Array<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanLeft(this, zero, operation,
                new java.util.ArrayList<>(), (c, u) -> {
                    c.add(u);
                    return c;
                }, Array::ofAll);
    }

    @Override
    public <U> Array<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanRight(this, zero, operation, List.empty(), List::prepend, Value::toArray);
    }

    @Override
    public Array<T> slice(int beginIndex, int endIndex) {
        if (beginIndex >= endIndex || beginIndex >= length() || isEmpty()) {
            return empty();
        }
        if (beginIndex <= 0 && endIndex >= length()) {
            return this;
        }
        final int index = Math.max(beginIndex, 0);
        final int length = Math.min(endIndex, length()) - index;
        final Object[] arr = new Object[length];
        System.arraycopy(delegate, index, arr, 0, length);
        return wrap(arr);
    }

    @Override
    public Iterator<Array<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    public Iterator<Array<T>> sliding(int size, int step) {
        return iterator().sliding(size, step).map(Array::ofAll);
    }

    @Override
    public Array<T> sorted() {
        final Object[] arr = copyOf(delegate, delegate.length);
        sort(arr);
        return wrap(arr);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Array<T> sorted(Comparator<? super T> comparator) {
        final Object[] arr = copyOf(delegate, delegate.length);
        sort(arr, (o1, o2) -> comparator.compare((T) o1, (T) o2));
        return wrap(arr);
    }

    @Override
    public <U extends Comparable<? super U>> Array<T> sortBy(Function<? super T, ? extends U> mapper) {
        return sortBy(U::compareTo, mapper);
    }

    @Override
    public <U> Array<T> sortBy(Comparator<? super U> comparator, Function<? super T, ? extends U> mapper) {
        final Function<? super T, ? extends U> domain = Function1.of(mapper::apply).memoized();
        return toJavaStream()
                .sorted((e1, e2) -> comparator.compare(domain.apply(e1), domain.apply(e2)))
                .collect(collector());
    }

    @Override
    public Tuple2<Array<T>, Array<T>> splitAt(int n) {
        return Tuple.of(take(n), drop(n));
    }

    @Override
    public Tuple2<Array<T>, Array<T>> splitAt(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Array<T> init = takeWhile(predicate.negate());
        return Tuple.of(init, drop(init.length()));
    }

    @Override
    public Tuple2<Array<T>, Array<T>> splitAtInclusive(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (int i = 0; i < delegate.length; i++) {
            final T value = get(i);
            if (predicate.test(value)) {
                if (i == delegate.length - 1) {
                    return Tuple.of(this, empty());
                } else {
                    return Tuple.of(take(i + 1), drop(i + 1));
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
    public Tuple2<Array<T>, Array<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return Tuple.of(takeWhile(predicate), dropWhile(predicate));
    }

    @Override
    public Array<T> subSequence(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("subSequence(" + beginIndex + ")");
        } else if (beginIndex > length()) {
            throw new IndexOutOfBoundsException("subSequence(" + beginIndex + ")");
        } else {
            return drop(beginIndex);
        }
    }

    @Override
    public Array<T> subSequence(int beginIndex, int endIndex) {
        if (beginIndex < 0 || beginIndex > endIndex || endIndex > length()) {
            throw new IndexOutOfBoundsException("subSequence(" + beginIndex + ", " + endIndex + ") on List of length " + length());
        } else if (beginIndex == endIndex) {
            return empty();
        } else {
            final Object[] arr = new Object[endIndex - beginIndex];
            System.arraycopy(delegate, beginIndex, arr, 0, arr.length);
            return wrap(arr);
        }
    }

    @Override
    public Array<T> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail() on empty Array");
        } else {
            final Object[] arr = new Object[delegate.length - 1];
            System.arraycopy(delegate, 1, arr, 0, arr.length);
            return wrap(arr);
        }
    }

    @Override
    public Option<Array<T>> tailOption() {
        return isEmpty() ? Option.none() : Option.some(tail());
    }

    @Override
    public Array<T> take(int n) {
        if (n >= length()) {
            return this;
        } else if (n <= 0) {
            return empty();
        } else {
            return wrap(copyOf(delegate, n));
        }
    }

    @Override
    public Array<T> takeRight(int n) {
        if (n >= length()) {
            return this;
        } else if (n <= 0) {
            return empty();
        } else {
            final Object[] arr = new Object[n];
            System.arraycopy(delegate, delegate.length - n, arr, 0, n);
            return wrap(arr);
        }
    }

    @Override
    public Array<T> takeUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @Override
    public Array<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (int i = 0; i < delegate.length; i++) {
            final T value = get(i);
            if (!predicate.test(value)) {
                return take(i);
            }
        }
        return this;
    }

    /**
     * Transforms this {@code Array}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U transform(Function<? super Array<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    @Override
    public <U> Array<U> unit(Iterable<? extends U> iterable) {
        return ofAll(iterable);
    }

    @Override
    public <T1, T2> Tuple2<Array<T1>, Array<T2>> unzip(
            Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        if (isEmpty()) {
            return Tuple.of(empty(), empty());
        } else {
            final Object[] xs = new Object[delegate.length];
            final Object[] ys = new Object[delegate.length];
            for (int i = 0; i < delegate.length; i++) {
                final Tuple2<? extends T1, ? extends T2> t = unzipper.apply(get(i));
                xs[i] = t._1;
                ys[i] = t._2;
            }
            return Tuple.of(wrap(xs), wrap(ys));
        }
    }

    @Override
    public <T1, T2, T3> Tuple3<Array<T1>, Array<T2>, Array<T3>> unzip3(Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        if (isEmpty()) {
            return Tuple.of(empty(), empty(), empty());
        } else {
            final Object[] xs = new Object[delegate.length];
            final Object[] ys = new Object[delegate.length];
            final Object[] zs = new Object[delegate.length];
            for (int i = 0; i < delegate.length; i++) {
                final Tuple3<? extends T1, ? extends T2, ? extends T3> t = unzipper.apply(get(i));
                xs[i] = t._1;
                ys[i] = t._2;
                zs[i] = t._3;
            }
            return Tuple.of(wrap(xs), wrap(ys), wrap(zs));
        }
    }

    @Override
    public Array<T> update(int index, T element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("update(" + index + ")");
        } else if (index >= length()) {
            throw new IndexOutOfBoundsException("update(" + index + ")");
        } else {
            final Object[] arr = toArray(this);
            arr[index] = element;
            return wrap(arr);
        }
    }

    @Override
    public <U> Array<Tuple2<T, U>> zip(Iterable<? extends U> that) {
        return zipWith(that, Tuple::of);
    }

    @Override
    public <U, R> Array<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return ofAll(iterator().zipWith(that, mapper));
    }

    @Override
    public <U> Array<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    public Array<Tuple2<T, Integer>> zipWithIndex() {
        return ofAll(iterator().zipWithIndex());
    }

    @Override
    public <U> Array<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return ofAll(iterator().zipWithIndex(mapper));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Array) {
            final Object[] arr1 = delegate;
            final Object[] arr2 = ((Array<?>) o).delegate;
            return Objects.deepEquals(arr1, arr2);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(delegate);
    }

    @Override
    public String stringPrefix() {
        return "Array";
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    private static <T> Object[] toArray(Iterable<T> elements) {
        if (elements instanceof java.util.List) {
            final java.util.List<T> list = (java.util.List<T>) elements;
            return list.toArray();
        } else {
            final java.util.Iterator<? extends T> it = elements.iterator();
            final java.util.List<T> list = new java.util.ArrayList<>();
            while (it.hasNext()) {
                list.add(it.next());
            }
            return list.toArray();
        }
    }
}

interface ArrayModule {

    final class Combinations {

        static <T> Array<Array<T>> apply(Array<T> elements, int k) {
            if (k == 0) {
                return Array.of(Array.empty());
            } else {
                return elements.zipWithIndex().flatMap(
                        t -> apply(elements.drop(t._2 + 1), (k - 1)).map(c -> c.prepend(t._1))
                );
            }
        }
    }
}
