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
import java.util.HashSet;
import java.util.function.*;
import java.util.stream.Collector;

public final class Array<T> implements IndexedSeq<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Array<?> EMPTY = new Array<>(new Object[0]);

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.Array}s.
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

    /**
     * Returns a singleton {@code Array}, i.e. a {@code Array} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new Array instance containing the given element
     */
    @SuppressWarnings("unchecked")
    public static <T> Array<T> of(T element) {
        return wrap((T[]) new Object[] {element});
    }

    /**
     * <p>
     * Creates a Array of the given elements.
     * </p>
     *
     * @param <T>      Component type of the Array.
     * @param elements Zero or more elements.
     * @return A Array containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("unchecked")
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
     * @param elements An java.lang.Iterable of elements.
     * @return A Array containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("unchecked")
    public static <T> Array<T> ofAll(java.lang.Iterable<? extends T> elements) {
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
        return Array.ofAll(() -> Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a byte array.
     *
     * @param array a byte array
     * @return A new Array of Byte values
     */
    public static Array<Byte> ofAll(byte[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(() -> Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a char array.
     *
     * @param array a char array
     * @return A new Array of Character values
     */
    public static Array<Character> ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(() -> Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a double array.
     *
     * @param array a double array
     * @return A new Array of Double values
     */
    public static Array<Double> ofAll(double[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(() -> Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a float array.
     *
     * @param array a float array
     * @return A new Array of Float values
     */
    public static Array<Float> ofAll(float[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(() -> Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of an int array.
     *
     * @param array an int array
     * @return A new Array of Integer values
     */
    public static Array<Integer> ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(() -> Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a long array.
     *
     * @param array a long array
     * @return A new Array of Long values
     */
    public static Array<Long> ofAll(long[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(() -> Iterator.ofAll(array));
    }

    /**
     * Creates a Array based on the elements of a short array.
     *
     * @param array a short array
     * @return A new Array of Short values
     */
    public static Array<Short> ofAll(short[] array) {
        Objects.requireNonNull(array, "array is null");
        return Array.ofAll(() -> Iterator.ofAll(array));
    }

    /* package */ static <T> Array<T> wrap(Object[] array) {
        if (array.length == 0) {
            return empty();
        } else {
            return new Array<>(array);
        }
    }

    private final Object[] back;
    private final transient Lazy<Integer> hashCode = Lazy.of(() -> Traversable.hash(this));

    private Array(Object[] back) {
        this.back = back;
    }

    @SuppressWarnings("unchecked")
    public static <T> Array<T> empty() {
        return (Array<T>) EMPTY;
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
    static Array<Integer> range(int from, int toExclusive) {
        return Array.rangeBy(from, toExclusive, 1);
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
    static Array<Integer> rangeBy(int from, int toExclusive, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toExclusive || step * (from - toExclusive) > 0) {
            return Array.empty();
        } else {
            final int one = (from < toExclusive) ? 1 : -1;
            return Array.rangeClosedBy(from, toExclusive - one, step);
        }
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
    static Array<Long> range(long from, long toExclusive) {
        return Array.rangeBy(from, toExclusive, 1);
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
    static Array<Long> rangeBy(long from, long toExclusive, long step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toExclusive || step * (from - toExclusive) > 0) {
            return Array.empty();
        } else {
            final int one = (from < toExclusive) ? 1 : -1;
            return Array.rangeClosedBy(from, toExclusive - one, step);
        }
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
    static Array<Integer> rangeClosed(int from, int toInclusive) {
        return Array.rangeClosedBy(from, toInclusive, 1);
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
    static Array<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toInclusive) {
            return Array.of(from);
        } else if (step * (from - toInclusive) > 0) {
            return Array.empty();
        } else {
            final Integer[] arr = new Integer[(toInclusive - from) / step + 1];
            int p = 0;
            if (step > 0) {
                int i = from;
                while (i <= toInclusive) {
                    arr[p++] = i;
                    if (Integer.MAX_VALUE - step < i) {
                        break;
                    }
                    i += step;
                }
            } else {
                int i = from;
                while (i >= toInclusive) {
                    arr[p++] = i;
                    if (Integer.MIN_VALUE - step > i) {
                        break;
                    }
                    i += step;
                }
            }
            if (p < arr.length) {
                return wrap(Arrays.copyOf(arr, p));
            } else {
                return wrap(arr);
            }
        }
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
    static Array<Long> rangeClosed(long from, long toInclusive) {
        return Array.rangeClosedBy(from, toInclusive, 1L);
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
    static Array<Long> rangeClosedBy(long from, long toInclusive, long step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toInclusive) {
            return Array.of(from);
        } else if (step * (from - toInclusive) > 0) {
            return Array.empty();
        } else {
            final Long[] arr = new Long[(int) ((toInclusive - from) / step + 1)];
            int p = 0;
            if (step > 0) {
                long i = from;
                while (i <= toInclusive) {
                    arr[p++] = i;
                    if (Long.MAX_VALUE - step < i) {
                        break;
                    }
                    i += step;
                }
            } else {
                long i = from;
                while (i >= toInclusive) {
                    arr[p++] = i;
                    if (Long.MIN_VALUE - step > i) {
                        break;
                    }
                    i += step;
                }
            }
            if (p < arr.length) {
                return wrap(Arrays.copyOf(arr, p));
            } else {
                return wrap(arr);
            }
        }
    }

    @Override
    public Array<T> append(T element) {
        final Object[] arr = Arrays.copyOf(back, back.length + 1);
        arr[back.length] = element;
        return wrap(arr);
    }

    @Override
    public Array<T> appendAll(java.lang.Iterable<? extends T> elements) {
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
        return new Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < back.length;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                } else {
                    return (T) back[index++];
                }
            }
        };
    }

    @Override
    public Array<T> clear() {
        return empty();
    }

    @Override
    public Array<Tuple2<T, T>> crossProduct() {
        return crossProduct(this);
    }

    @Override
    public <U> Array<Tuple2<T, U>> crossProduct(java.lang.Iterable<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        final Array<? extends U> other = unit(that);
        return flatMap(a -> other.map(b -> Tuple.of(a, b)));
    }

    @Override
    public Array<Array<T>> combinations() {
        return Array.rangeClosed(0, length()).map(this::combinations).flatMap(Function.identity());
    }

    @Override
    public Array<Array<T>> combinations(int k) {
        class Recursion {
            Array<Array<T>> combinations(Array<T> elements, int k) {
                return (k == 0)
                        ? Array.of(Array.empty())
                        : elements.zipWithIndex().flatMap(t -> combinations(elements.drop(t._2 + 1), (k - 1))
                        .map((Array<T> c) -> c.prepend(t._1)));
            }
        }
        return new Recursion().combinations(this, Math.max(k, 0));
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
            return list.size() == 0 ? empty() : wrap(list.toArray());
        }
    }

    @Override
    public <U> Array<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
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
    public <U> Array<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper) {
        return flatMap(mapper);
    }

    @Override
    public Array<Object> flatten() {
        return flatMap(t -> (t instanceof java.lang.Iterable) ? Array.ofAll((java.lang.Iterable<?>) t).flatten() : Array.of(t));
    }

    @Override
    public <C> Map<C, ? extends Array<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return foldLeft(HashMap.empty(), (map, t) -> {
            final C key = classifier.apply(t);
            final Array<T> values = map.get(key).map(ts -> ts.prepend(t)).orElse(Array.of(t));
            return map.put(key, values);
        });
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
        return isEmpty() ? None.instance() : new Some<>(head());
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
    public Option<? extends Array<T>> initOption() {
        if (isEmpty()) {
            return None.instance();
        } else {
            return new Some<>(init());
        }
    }

    @Override
    public boolean isEmpty() {
        return back.length == 0;
    }

    private Object readResolve() {
        if(isEmpty()) {
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
    public Array<T> insertAll(int index, java.lang.Iterable<? extends T> elements) {
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
        if(length <= length()) {
            return this;
        } else {
            return appendAll(Stream.gen(() -> element).take(length - length()));
        }
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
                // TODO: IntelliJ IDEA 14.1.1 needs a redundant cast here, jdk 1.8.0_40 compiles fine
                return distinct().foldLeft(zero, (xs, x) -> xs.appendAll(remove(x).permutations().map((Function<Array<T>, Array<T>>) l -> l.prepend(x))));
            }
        }
    }

    @Override
    public Array<T> prepend(T element) {
        return insert(0, element);
    }

    @Override
    public Array<T> prependAll(java.lang.Iterable<? extends T> elements) {
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
    public Array<T> removeAt(int indx) {
        if (indx < 0) {
            throw new IndexOutOfBoundsException("removeAt(" + indx + ")");
        }
        if (indx >= length()) {
            throw new IndexOutOfBoundsException("removeAt(" + indx + ")");
        }
        final Object[] arr = new Object[length() - 1];
        System.arraycopy(back, 0, arr, 0, indx);
        System.arraycopy(back, indx + 1, arr, indx, length() - indx - 1);
        return wrap(arr);
    }

    @Override
    public Array<T> removeAll(T element) {
        final java.util.List<T> list = new ArrayList<>();
        for (int i = 0; i < length(); i++) {
            T value = get(i);
            if(!element.equals(value)) {
                list.add(value);
            }
        }
        if(list.size() == length()) {
            return this;
        } else {
            return wrap(list.toArray());
        }
    }

    @Override
    public Array<T> removeAll(java.lang.Iterable<? extends T> elements) {
        final java.util.Set<T> removed = new HashSet<T>();
        for (T element : elements) {
            removed.add(element);
        }
        final java.util.List<T> list = new ArrayList<>();
        for (int i = 0; i < length(); i++) {
            T value = get(i);
            if(!removed.contains(value)) {
                list.add(value);
            }
        }
        if(list.size() == length()) {
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
    public Array<T> replaceAll(UnaryOperator<T> operator) {
        Objects.requireNonNull(operator, "operator is null");
        final Object[] arr = new Object[length()];
        for (int i = 0; i < length(); i++) {
            arr[i] = operator.apply(get(i));
        }
        return wrap(arr);
    }

    @Override
    public Array<T> retainAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final java.util.Set<T> keeped = new HashSet<T>();
        for (T element : elements) {
            keeped.add(element);
        }
        final java.util.List<T> list = new ArrayList<>();
        for (int i = 0; i < length(); i++) {
            T value = get(i);
            if(keeped.contains(value)) {
                list.add(value);
            }
        }
        if(list.size() == length()) {
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
    public Array<T> set(int index, T element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("set(" + index + ")");
        }
        if (index >= length()) {
            throw new IndexOutOfBoundsException("set(" + index + ")");
        }
        final Object[] arr = create(this);
        arr[index] = element;
        return wrap(arr);
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
    public Array<T> slice(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("slice(" + beginIndex + ")");
        }
        if (beginIndex > length()) {
            throw new IndexOutOfBoundsException("slice(" + beginIndex + ")");
        }
        return drop(beginIndex);
    }

    @Override
    public Array<T> slice(int beginIndex, int endIndex) {
        if (beginIndex < 0 || beginIndex > endIndex || endIndex > length()) {
            throw new IndexOutOfBoundsException(
                    String.format("slice(%s, %s) on List of length %s", beginIndex, endIndex, length()));
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
        return isEmpty() ? None.instance() : new Some<>(tail());
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
    public String toString() {
        return mkString(", ", "Array(", ")");
    }

    @Override
    public <U> Array<U> unit(java.lang.Iterable<? extends U> iterable) {
        return wrap(create(iterable));
    }

    @Override
    public <T1, T2> Tuple2<Array<T1>, Array<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
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
    public <U> Array<Tuple2<T, U>> zip(java.lang.Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        final Object[] thatArr = create(that);
        if (isEmpty() || thatArr.length == 0) {
            return empty();
        } else {
            final Object[] arr = new Object[Math.min(back.length, thatArr.length)];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = Tuple.of(back[i], thatArr[i]);
            }
            return wrap(arr);
        }
    }

    @Override
    public <U> Array<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        final Object[] thatArr = create(that);
        if (isEmpty() && thatArr.length == 0) {
            return empty();
        } else {
            final Object[] arr = new Object[Math.max(back.length, thatArr.length)];
            for (int i = 0; i < arr.length; i++) {
                final Object elem1 = i < back.length ? back[i] : thisElem;
                final Object elem2 = i < thatArr.length ? thatArr[i] : thatElem;
                arr[i] = Tuple.of(elem1, elem2);
            }
            return wrap(arr);
        }
    }

    @Override
    public Array<Tuple2<T, Integer>> zipWithIndex() {
        if (isEmpty()) {
            return empty();
        } else {
            final Object[] arr = new Object[back.length];
            for (int i = 0; i < back.length; i++) {
                arr[i] = Tuple.of(back[i], i);
            }
            return wrap(arr);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Object[] create(java.lang.Iterable<T> elements) {
        if (elements instanceof java.util.List) {
            final java.util.List<T> list = (java.util.List) elements;
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
