/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.collection.ArrayModule.Combinations;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.*;
import java.util.HashSet;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * Array is a Traversable wrapper for {@code Object[]} containing elements of type {@code T}.
 *
 * @param <T> Component type
 * @author Ruslan Sennov, Daniel Dietrich
 * @since 2.0.0
 */
public final class Array<T> implements IndexedSeq<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Array<?> EMPTY = new Array<>(new Object[0]);

    private final Object[] back;
    private final transient Lazy<Integer> hashCode = Lazy.of(() -> Traversable.hash(this));

    private Array(Object[] back) {
        this.back = back;
    }

    /* package private */
    static <T> Array<T> wrap(Object[] array) {
        if (array.length == 0) {
            return empty();
        } else {
            return new Array<>(array);
        }
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.Array}.
     *
     * @param <T> Component type of the Vector.
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
     * Returns a singleton {@code Array}, i.e. a {@code Array} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new Array instance containing the given element
     */
    @SuppressWarnings("unchecked")
    public static <T> Array<T> of(T element) {
        return wrap((T[]) new Object[] { element });
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
        return wrap(Arrays.copyOf(elements, elements.length));
    }

    /**
     * Creates a Array of the given elements.
     *
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
        if (elements instanceof Array) {
            return (Array<T>) elements;
        } else {
            return wrap(create(elements));
        }
    }

    /**
     * Creates a Array based on the elements of a boolean array.
     *
     * @param array a boolean array
     * @return A new Array of Boolean values
     */
    public static Array<Boolean> ofAll(boolean[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a byte array.
     *
     * @param array a byte array
     * @return A new Array of Byte values
     */
    public static Array<Byte> ofAll(byte[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a char array.
     *
     * @param array a char array
     * @return A new Array of Character values
     */
    public static Array<Character> ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a double array.
     *
     * @param array a double array
     * @return A new Array of Double values
     */
    public static Array<Double> ofAll(double[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a float array.
     *
     * @param array a float array
     * @return A new Array of Float values
     */
    public static Array<Float> ofAll(float[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of an int array.
     *
     * @param array an int array
     * @return A new Array of Integer values
     */
    public static Array<Integer> ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a long array.
     *
     * @param array a long array
     * @return A new Array of Long values
     */
    public static Array<Long> ofAll(long[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a short array.
     *
     * @param array a short array
     * @return A new Array of Short values
     */
    public static Array<Short> ofAll(short[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(Iterator.ofAll(array));
    }

    /**
     * Returns an Array containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param <T> Component type of the Array
     * @param n The number of elements in the Array
     * @param f The Function computing element values
     * @return An Array consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code n} or {@code f} are null
     */
    public static <T> Array<T> tabulate(Integer n, Function<Integer, ? extends T> f) {
        Objects.requireNonNull(n, "n is null");
        Objects.requireNonNull(f, "f is null");
        int nOrZero = java.lang.Math.max(n, 0);
        @SuppressWarnings("unchecked")
        T[] elements = (T[]) new Object[nOrZero];
        for (int i = 0; i < n; i++) {
            elements[i] = f.apply(i);
        }
        return wrap(elements);
    }

    /**
     * Returns an Array containing {@code n} values supplied by a given Supplier {@code s}.
     *
     * @param <T> Component type of the Array
     * @param n The number of elements in the Array
     * @param s The Supplier computing element values
     * @return An Array of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code n} or {@code s} are null
     */
    public static <T> Array<T> fill(Integer n, Supplier<? extends T> s) {
        return tabulate(n, anything -> s.get());
    }


    public static Array<Character> range(char from, char toExclusive) {
        return Array.ofAll(Iterator.range(from, toExclusive));
    }

    public static Array<Character> rangeBy(char from, char toExclusive, int step) {
        return Array.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static Array<Double> rangeBy(double from, double toExclusive, double step) {
        return Array.ofAll(Iterator.rangeBy(from, toExclusive, step));
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
        return Array.ofAll(Iterator.range(from, toExclusive));
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
        return Array.ofAll(Iterator.rangeBy(from, toExclusive, step));
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
        return Array.ofAll(Iterator.range(from, toExclusive));
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
        return Array.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static Array<Character> rangeClosed(char from, char toInclusive) {
        return Array.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    public static Array<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return Array.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    public static Array<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return Array.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
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
        return Array.ofAll(Iterator.rangeClosed(from, toInclusive));
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
        return Array.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
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
        return Array.ofAll(Iterator.rangeClosed(from, toInclusive));
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
        return Array.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    public Array<T> append(T element) {
        final Object[] arr = Arrays.copyOf(back, back.length + 1);
        arr[back.length] = element;
        return wrap(arr);
    }

    @Override
    public Array<T> appendAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final Object[] source = create(elements);
        if (source.length == 0) {
            return this;
        } else {
            Object[] arr = Arrays.copyOf(back, back.length + source.length);
            System.arraycopy(source, 0, arr, back.length, source.length);
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
                return index < back.length;
            }

            @Override
            public T getNext() {
                return (T) back[index++];
            }
        };
    }

    @Override
    public Array<T> clear() {
        return empty();
    }

    @Override
    public Array<Array<T>> combinations() {
        return Array.rangeClosed(0, length()).map(this::combinations).flatMap(Function.identity());
    }

    @Override
    public Array<Array<T>> combinations(int k) {
        return Combinations.apply(this, Math.max(k, 0));
    }

    @Override
    public Array<Tuple2<T, T>> crossProduct() {
        return crossProduct(this);
    }

    @Override
    public Array<Array<T>> crossProduct(int power) {
        return Collections.crossProduct(this, power).map(Array::ofAll).toArray();
    }

    @Override
    public <U> Array<Tuple2<T, U>> crossProduct(Iterable<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        final Array<U> other = unit(that);
        return flatMap(a -> other.map((Function<U, Tuple2<T, U>>) b -> Tuple.of(a, b)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException("get(" + index + ")");
        }
        return (T) back[index];
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
        }
        if (n >= length()) {
            return empty();
        }
        final Object[] arr = new Object[back.length - n];
        System.arraycopy(back, n, arr, 0, arr.length);
        return wrap(arr);
    }

    @Override
    public Array<T> dropRight(int n) {
        if (n <= 0) {
            return this;
        }
        if (n >= length()) {
            return empty();
        }
        return wrap(Arrays.copyOf(back, back.length - n));
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
        if (list.size() == back.length) {
            return this;
        } else {
            return list.isEmpty() ? empty() : wrap(list.toArray());
        }
    }

    @Override
    public Array<T> filterNot(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
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
            return wrap(create(list));
        }
    }

    @Override
    public <C> Map<C, Array<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return foldLeft(HashMap.empty(), (map, t) -> {
            final C key = classifier.apply(t);
            final Array<T> values = map.get(key).map(ts -> ts.append(t)).orElse(Array.of(t));
            return map.put(key, values);
        });
    }

    @Override
    public Iterator<Array<T>> grouped(int size) {
        return sliding(size, size);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head() on empty Array");
        }
        return (T) back[0];
    }

    @Override
    public Option<T> headOption() {
        return isEmpty() ? Option.none() : Option.some(head());
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
            throw new UnsupportedOperationException("init of empty vector");
        }
        final Object[] arr = new Object[length() - 1];
        System.arraycopy(back, 0, arr, 0, arr.length);
        return wrap(arr);
    }

    @Override
    public Option<Array<T>> initOption() {
        if (isEmpty()) {
            return Option.none();
        } else {
            return Option.some(init());
        }
    }

    @Override
    public boolean isEmpty() {
        return back.length == 0;
    }

    private Object readResolve() {
        if (isEmpty()) {
            return EMPTY;
        } else {
            return this;
        }
    }

    @Override
    public Array<T> insert(int index, T element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        }
        if (index > length()) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e) on Vector of length " + length());
        }
        final Object[] arr = new Object[back.length + 1];
        System.arraycopy(back, 0, arr, 0, index);
        arr[index] = element;
        System.arraycopy(back, index, arr, index + 1, back.length - index);
        return wrap(arr);
    }

    @Override
    public Array<T> insertAll(int index, Iterable<? extends T> elements) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        }
        if (index > length()) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e) on Vector of length " + length());
        }
        final Object[] list = create(elements);
        if (list.length == 0) {
            return this;
        } else {
            Object[] arr = new Object[back.length + list.length];
            System.arraycopy(back, 0, arr, 0, index);
            System.arraycopy(list, 0, arr, index, list.length);
            System.arraycopy(back, index, arr, index + list.length, back.length - index);
            return wrap(arr);
        }
    }

    @Override
    public Array<T> intersperse(T element) {
        if (back.length <= 1) {
            return this;
        } else {
            final Object[] arr = new Object[back.length * 2 - 1];
            for (int i = 0; i < back.length; i++) {
                arr[i * 2] = back[i];
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
        return back.length;
    }

    @Override
    public <U> Array<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final Object[] arr = new Object[length()];
        for (int i = 0; i < back.length; i++) {
            arr[i] = mapper.apply(get(i));
        }
        return wrap(arr);
    }

    @Override
    public Array<T> padTo(int length, T element) {
        if (length <= length()) {
            return this;
        } else {
            return appendAll(Iterator.gen(() -> element).take(length - length()));
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
        return Tuple.of(Array.ofAll(left), Array.ofAll(right));
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
        } else {
            final Array<T> tail = tail();
            if (tail.isEmpty()) {
                return Array.of(this);
            } else {
                final Array<Array<T>> zero = empty();
                return distinct().foldLeft(zero, (xs, x) -> {
                    final Function<Array<T>, Array<T>> prepend = l -> l.prepend(x);
                    return xs.appendAll(remove(x).permutations().map(prepend));
                });
            }
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
        int found = -1;
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (element.equals(value)) {
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
        }
        if (index >= length()) {
            throw new IndexOutOfBoundsException("removeAt(" + index + ")");
        }
        final Object[] arr = new Object[length() - 1];
        System.arraycopy(back, 0, arr, 0, index);
        System.arraycopy(back, index + 1, arr, index, length() - index - 1);
        return wrap(arr);
    }

    @Override
    public Array<T> removeAll(T element) {
        final java.util.List<T> list = new ArrayList<>();
        for (int i = 0; i < length(); i++) {
            T value = get(i);
            if (!element.equals(value)) {
                list.add(value);
            }
        }
        if (list.size() == length()) {
            return this;
        } else {
            return wrap(list.toArray());
        }
    }

    @Override
    public Array<T> removeAll(Iterable<? extends T> elements) {
        final java.util.Set<T> removed = new HashSet<>();
        for (T element : elements) {
            removed.add(element);
        }
        final java.util.List<T> list = new ArrayList<>();
        for (int i = 0; i < length(); i++) {
            T value = get(i);
            if (!removed.contains(value)) {
                list.add(value);
            }
        }
        if (list.size() == length()) {
            return this;
        } else {
            return wrap(list.toArray());
        }
    }

    @Override
    public Array<T> replace(T currentElement, T newElement) {
        final Object[] arr = new Object[length()];
        boolean found = false;
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (found) {
                arr[i] = back[i];
            } else {
                if (currentElement.equals(value)) {
                    arr[i] = newElement;
                    found = true;
                } else {
                    arr[i] = back[i];
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
                arr[i] = back[i];
            }
        }
        return changed ? wrap(arr) : this;
    }

    @Override
    public Array<T> retainAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final java.util.Set<T> kept = new HashSet<>();
        for (T element : elements) {
            kept.add(element);
        }
        final java.util.List<T> list = new ArrayList<>();
        for (int i = 0; i < length(); i++) {
            T value = get(i);
            if (kept.contains(value)) {
                list.add(value);
            }
        }
        if (list.size() == length()) {
            return this;
        } else {
            return wrap(list.toArray());
        }
    }

    @Override
    public Array<T> reverse() {
        final Object[] arr = new Object[back.length];
        for (int i = 0; i < back.length; i++) {
            arr[back.length - 1 - i] = back[i];
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
                }, list -> Array.<U> wrap(list.toArray()));
    }

    @Override
    public <U> Array<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanRight(this, zero, operation, List.empty(), List::prepend, list -> Array.<U> wrap(list.toJavaArray()));
    }

    @Override
    public Array<T> slice(int beginIndex, int endIndex) {
        if (beginIndex >= endIndex || beginIndex >= length() || isEmpty()) {
            return Array.empty();
        }
        if (beginIndex <= 0 && endIndex >= length()) {
            return this;
        }
        final int index = Math.max(beginIndex, 0);
        final int length = Math.min(endIndex, length()) - index;
        final Object[] arr = new Object[length];
        System.arraycopy(back, index, arr, 0, length);
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
    public Array<T> sort() {
        final Object[] arr = Arrays.copyOf(back, back.length);
        Arrays.sort(arr);
        return wrap(arr);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Array<T> sort(Comparator<? super T> comparator) {
        final Object[] arr = Arrays.copyOf(back, back.length);
        Arrays.sort(arr, (o1, o2) -> comparator.compare((T) o1, (T) o2));
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
        for (int i = 0; i < back.length; i++) {
            final T value = get(i);
            if (predicate.test(value)) {
                if (i == back.length - 1) {
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
        }
        if (beginIndex > length()) {
            throw new IndexOutOfBoundsException("subSequence(" + beginIndex + ")");
        }
        return drop(beginIndex);
    }

    @Override
    public Array<T> subSequence(int beginIndex, int endIndex) {
        if (beginIndex < 0 || beginIndex > endIndex || endIndex > length()) {
            throw new IndexOutOfBoundsException(
                    String.format("subSequence(%s, %s) on List of length %s", beginIndex, endIndex, length()));
        }
        if (beginIndex == endIndex) {
            return Array.empty();
        }
        final Object[] arr = new Object[endIndex - beginIndex];
        System.arraycopy(back, beginIndex, arr, 0, arr.length);
        return wrap(arr);
    }

    @Override
    public Array<T> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail() on empty Array");
        }
        final Object[] arr = new Object[back.length - 1];
        System.arraycopy(back, 1, arr, 0, arr.length);
        return wrap(arr);
    }

    @Override
    public Option<Array<T>> tailOption() {
        return isEmpty() ? Option.none() : Option.some(tail());
    }

    @Override
    public Array<T> take(int n) {
        if (n >= length()) {
            return this;
        }
        if (n <= 0) {
            return empty();
        }
        return wrap(Arrays.copyOf(back, n));
    }

    @Override
    public Array<T> takeRight(int n) {
        if (n >= length()) {
            return this;
        }
        if (n <= 0) {
            return empty();
        }
        final Object[] arr = new Object[n];
        System.arraycopy(back, back.length - n, arr, 0, n);
        return wrap(arr);
    }

    @Override
    public Array<T> takeUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @Override
    public Array<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (int i = 0; i < back.length; i++) {
            final T value = get(i);
            if (!predicate.test(value)) {
                return take(i);
            }
        }
        return this;
    }

    @Override
    public <U> Array<U> unit(Iterable<? extends U> iterable) {
        return wrap(create(iterable));
    }

    @Override
    public <T1, T2> Tuple2<Array<T1>, Array<T2>> unzip(
            Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        if (isEmpty()) {
            return Tuple.of(empty(), empty());
        } else {
            final Object[] xs = new Object[back.length];
            final Object[] ys = new Object[back.length];
            for (int i = 0; i < back.length; i++) {
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
            final Object[] xs = new Object[back.length];
            final Object[] ys = new Object[back.length];
            final Object[] zs = new Object[back.length];
            for (int i = 0; i < back.length; i++) {
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
        }
        if (index >= length()) {
            throw new IndexOutOfBoundsException("update(" + index + ")");
        }
        final Object[] arr = create(this);
        arr[index] = element;
        return wrap(arr);
    }

    @Override
    public <U> Array<Tuple2<T, U>> zip(Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        return Array.ofAll(iterator().zip(that));
    }

    @Override
    public <U> Array<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return Array.ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    public Array<Tuple2<T, Integer>> zipWithIndex() {
        return Array.ofAll(iterator().zipWithIndex());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Array) {
            final Object[] arr1 = back;
            final Object[] arr2 = ((Array<?>) o).back;
            return Objects.deepEquals(arr1, arr2);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return hashCode.get();
    }

    @Override
    public String stringPrefix() {
        return "Array";
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    private static <T> Object[] create(Iterable<T> elements) {
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