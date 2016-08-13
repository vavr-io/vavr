/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.collection.VectorModule.Combinations;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.*;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;

import static java.lang.System.arraycopy;
import static javaslang.collection.Arrays2.*;
import static javaslang.collection.Collections.*;
import static javaslang.collection.Vector.VectorTree.create;
import static javaslang.collection.Vector.VectorTree.emptyTree;

/**
 * Vector is the default Seq implementation that provides effectively constant time access to any element.
 * Many other operations (e.g. `tail`, `drop`, `slice`) are also effectively constant.
 * <p>
 * It's implemented using a `bit-mapped vector trie`, i.e. a tree where each node has 32 children.
 * For integers this means that the tree's depth will be ≤ 6.
 * <p>
 * For symmetrically fast beginning and end operations (e.g. `drop` vs `dropRight`) two small arrays are used as depthless staging areas.
 * The leading and trailing arrays have dynamic lengths (their max size equals the branching factor),
 * but the middle tree's leafs only contain full blocks - though the first one may be padded by an offset.
 * The leading is only empty if everything is empty; if the trailing is empty, so is the middle.
 *
 * @param <T> Component type of the Vector.
 * @author Pap Lőrinc
 * @since 3.0.0
 */
@SuppressWarnings({"SuspiciousArrayCast", "unchecked"})
public class Vector<T> implements Kind1<Vector<?>, T>, IndexedSeq<T> {
    private static final long serialVersionUID = 1L;

    static int BRANCHING_BASE = 5;
    static int branchingFactor()                   { return (1 << BRANCHING_BASE); }
    static int firstDigit(int num, int depthShift) { return num >> depthShift; }
    static int digit(int num, int depthShift)      { return lastDigit(num >> depthShift); }
    static int lastDigit(int num)                  { return (num & (-1 >>> -BRANCHING_BASE)); }

    private static final Vector<?> EMPTY = new Vector<Object>(Object.class, emptyArray(), emptyTree(), emptyArray()) {
        @Override
        public Object get(int index) { throw new NoSuchElementException("get on an empty Vector"); }
        @Override
        public Object head() { throw new NoSuchElementException("head of empty Vector"); }
        @Override
        public Iterator<Object> iterator() { return Iterator.empty(); }
    };

    final Class<?> type;
    final VectorTree<T> middle;
    final Object leading, trailing;
    final int length;

    private Vector(Class<?> type, Object leading, VectorTree<T> middle, Object trailing) {
        this.type = type;
        this.leading = leading;
        this.middle = middle;
        this.trailing = trailing;
        this.length = leadingLength() + middle.length() + getLength(type, trailing);
    }

    private int leadingLength() { return getLength(type, leading); }

    /**
     * Returns the empty Vector.
     *
     * @param <T> Component type.
     * @return The empty Vector.
     */
    public static <T> Vector<T> empty() {
        return (Vector<T>) EMPTY;
    }

    /**
     * Returns a {@link Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(Collector)} to obtain a {@link Vector}.
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
     * Narrows a widened {@code Vector<? extends T>} to {@code Vector<T>}
     * by performing a type safe-cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param vector An {@code Vector}.
     * @param <T>    Component type of the {@code Vector}.
     * @return the given {@code vector} instance as narrowed type {@code Vector<T>}.
     */
    public static <T> Vector<T> narrow(Vector<? extends T> vector) {
        return (Vector<T>) vector;
    }

    /**
     * Returns a singleton {@code Vector}, i.e. a {@code Vector} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new Vector instance containing the given element
     */
    public static <T> Vector<T> of(T element) {
        final Class<?> type = toPrimitive(element.getClass());
        Object leading = asArray(type, element);
        return new Vector<T>(type, leading, emptyTree(), emptyArray());
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
    @SuppressWarnings("varargs")
    public static <T> Vector<T> of(T... elements) {
        return ofAll(List.of(elements));
    }

    /**
     * Returns a Vector containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param <T> Component type of the Vector
     * @param n   The number of elements in the Vector
     * @param f   The Function computing element values
     * @return A Vector consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    public static <T> Vector<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        return Collections.tabulate(n, f, empty(), Vector::of);
    }

    /**
     * Returns a Vector containing {@code n} values supplied by a given Supplier {@code s}.
     *
     * @param <T> Component type of the Vector
     * @param n   The number of elements in the Vector
     * @param s   The Supplier computing element values
     * @return A Vector of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    public static <T> Vector<T> fill(int n, Supplier<? extends T> s) {
        Objects.requireNonNull(s, "s is null");
        return Collections.fill(n, s, empty(), Vector::of);
    }

    /**
     * Creates a Vector of the given elements.
     * <p>
     * The resulting vector has the same iteration order as the given iterable of elements
     * if the iteration order of the elements is stable.
     *
     * @param <T>      Component type of the Vector.
     * @param elements An Iterable of elements.
     * @return A vector containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    public static <T> Vector<T> ofAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof Vector) {
            return (Vector<T>) elements;
        } else {
            final Seq<? extends T> seq = seq(elements);
            return seq.isEmpty() ? empty()
                                 : ofAll(seq, seq.size());
        }
    }

    public static <T> Vector<T> ofAll(Collection<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(elements, elements.size());
    }

    public static <T> Vector<T> ofAll(Iterable<? extends T> iterable, int size) {
        if (!iterable.iterator().hasNext()) return empty();

        final Object array = (iterable instanceof ArrayList) ? (T[]) ((ArrayList<T>) iterable).toArray()
                                                             : asArray(iterable.iterator(), size);
        return ofAll(array.getClass().getComponentType(), array, size);
    }

    static <T> Vector<T> ofAll(Class<?> type, Object array, int size) {
        assert getLength(type, array) == size;
        if (size <= branchingFactor()) {
            return new Vector<>(type, array, emptyTree(), emptyArray());
        }

        int leadingSize = Math.min(size, branchingFactor());
        final Object leading = newInstance(type, leadingSize);
        arraycopy(array, 0, leading, 0, leadingSize);

        final int remaining = size - leadingSize;
        int trailingSize = lastDigit(remaining);
        if (trailingSize == 0) { trailingSize += branchingFactor(); }

        int from = size - trailingSize;
        int trailingLength = size - from;

        final Object trailing = newInstance(type, trailingLength);
        arraycopy(array, from, trailing, 0, trailingLength);
        final int middleSize = remaining - trailingSize;
        if (middleSize == 0) {
            return new Vector<>(type, leading, emptyTree(), trailing);
        }

        Object copy = newInstance(type, middleSize);
        arraycopy(array, leadingSize, copy, 0, middleSize);
        array = copy;

        byte depthShift = 0;
        while (true) {
            final int length = getLength(array.getClass().getComponentType(), array);
            if (length <= branchingFactor()) break;

            array = Arrays2.grouped(array, length, branchingFactor());
            depthShift += BRANCHING_BASE;
        }

        final VectorTree<T> middle = create(type, array, 0, middleSize, depthShift);
        return new Vector<>(type, leading, middle, trailing);
    }

    /**
     * Creates a Vector that contains the elements of the given {@link java.util.stream.Stream}.
     *
     * @param javaStream A {@link java.util.stream.Stream}
     * @param <T>        Component type of the Stream.
     * @return A Vector containing the given elements in the same order.
     */
    public static <T> Vector<T> ofAll(java.util.stream.Stream<? extends T> javaStream) {
        Objects.requireNonNull(javaStream, "javaStream is null");
        return ofAll(Iterator.ofAll(javaStream.iterator()));
    }

    /**
     * Creates a Vector based on the elements of a boolean array.
     *
     * @param array a boolean array
     * @return A new Vector of Boolean values
     */
    public static Vector<Boolean> ofAll(boolean[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(array.getClass().getComponentType(), array, array.length);
    }

    /**
     * Creates a Vector based on the elements of a byte array.
     *
     * @param array a byte array
     * @return A new Vector of Byte values
     */
    public static Vector<Byte> ofAll(byte[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(array.getClass().getComponentType(), array, array.length);
    }

    /**
     * Creates a Vector based on the elements of a char array.
     *
     * @param array a char array
     * @return A new Vector of Character values
     */
    public static Vector<Character> ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(array.getClass().getComponentType(), array, array.length);
    }

    /**
     * Creates a Vector based on the elements of a double array.
     *
     * @param array a double array
     * @return A new Vector of Double values
     */
    public static Vector<Double> ofAll(double[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(array.getClass().getComponentType(), array, array.length);
    }

    /**
     * Creates a Vector based on the elements of a float array.
     *
     * @param array a float array
     * @return A new Vector of Float values
     */
    public static Vector<Float> ofAll(float[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(array.getClass().getComponentType(), array, array.length);
    }

    /**
     * Creates a Vector based on the elements of an int array.
     *
     * @param array an int array
     * @return A new Vector of Integer values
     */
    public static Vector<Integer> ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(array.getClass().getComponentType(), array, array.length);
    }

    /**
     * Creates a Vector based on the elements of a long array.
     *
     * @param array a long array
     * @return A new Vector of Long values
     */
    public static Vector<Long> ofAll(long[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(array.getClass().getComponentType(), array, array.length);
    }

    /**
     * Creates a Vector based on the elements of a short array.
     *
     * @param array a short array
     * @return A new Vector of Short values
     */
    public static Vector<Short> ofAll(short[] array) {
        Objects.requireNonNull(array, "array is null");
        return ofAll(array.getClass().getComponentType(), array, array.length);
    }

    public static Vector<Character> range(char from, char toExclusive) {
        return ofAll(Iterator.range(from, toExclusive));
    }

    public static Vector<Character> rangeBy(char from, char toExclusive, int step) {
        return ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @GwtIncompatible
    public static Vector<Double> rangeBy(double from, double toExclusive, double step) {
        return ofAll(Iterator.rangeBy(from, toExclusive, step));
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
        return ofAll(Iterator.range(from, toExclusive));
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
        return ofAll(Iterator.rangeBy(from, toExclusive, step));
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
        return ofAll(Iterator.range(from, toExclusive));
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
        return ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static Vector<Character> rangeClosed(char from, char toInclusive) {
        return ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    public static Vector<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @GwtIncompatible
    public static Vector<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
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
        return ofAll(Iterator.rangeClosed(from, toInclusive));
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
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
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
        return ofAll(Iterator.rangeClosed(from, toInclusive));
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
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a Vector from a seed value and a function.
     * The function takes the seed at first.
     * The function should return {@code None} when it's
     * done generating the Vector, otherwise {@code Some} {@code Tuple}
     * of the element for the next call and the value to add to the
     * resulting Vector.
     * <p>
     * Example:
     * <pre>
     * <code>
     * Vector.unfoldRight(10, x -&gt; x == 0
     *             ? Option.none()
     *             : Option.of(new Tuple2&lt;&gt;(x, x-1)));
     * // Vector(10, 9, 8, 7, 6, 5, 4, 3, 2, 1))
     * </code>
     * </pre>
     *
     * @param seed the start value for the iteration
     * @param f    the function to get the next step of the iteration
     * @return a Vector with the values built up by the iteration
     * @throws IllegalArgumentException if {@code f} is null
     */
    public static <T, U> Vector<U> unfoldRight(T seed, Function<? super T, Option<Tuple2<? extends U, ? extends T>>> f) {
        return Iterator.unfoldRight(seed, f).toVector();
    }

    /**
     * Creates a Vector from a seed value and a function.
     * The function takes the seed at first.
     * The function should return {@code None} when it's
     * done generating the Vector, otherwise {@code Some} {@code Tuple}
     * of the value to add to the resulting Vector and
     * the element for the next call.
     * <p>
     * Example:
     * <pre>
     * <code>
     * Vector.unfoldLeft(10, x -&gt; x == 0
     *             ? Option.none()
     *             : Option.of(new Tuple2&lt;&gt;(x-1, x)));
     * // Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
     * </code>
     * </pre>
     *
     * @param seed the start value for the iteration
     * @param f    the function to get the next step of the iteration
     * @return a Vector with the values built up by the iteration
     * @throws IllegalArgumentException if {@code f} is null
     */
    public static <T, U> Vector<U> unfoldLeft(T seed, Function<? super T, Option<Tuple2<? extends T, ? extends U>>> f) {
        return Iterator.unfoldLeft(seed, f).toVector();
    }

    /**
     * Creates a Vector from a seed value and a function.
     * The function takes the seed at first.
     * The function should return {@code None} when it's
     * done generating the Vector, otherwise {@code Some} {@code Tuple}
     * of the value to add to the resulting Vector and
     * the element for the next call.
     * <p>
     * Example:
     * <pre>
     * <code>
     * Vector.unfold(10, x -&gt; x == 0
     *             ? Option.none()
     *             : Option.of(new Tuple2&lt;&gt;(x-1, x)));
     * // Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
     * </code>
     * </pre>
     *
     * @param seed the start value for the iteration
     * @param f    the function to get the next step of the iteration
     * @return a Vector with the values built up by the iteration
     * @throws IllegalArgumentException if {@code f} is null
     */
    public static <T> Vector<T> unfold(T seed, Function<? super T, Option<Tuple2<? extends T, ? extends T>>> f) {
        return Iterator.unfold(seed, f).toVector();
    }

    @Override
    public Vector<T> append(T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Vector<T> appendAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (!elements.iterator().hasNext()) {
            return this;
        } else if (isEmpty()) {
            return ofAll(elements);
        } else {
            Vector<T> result = this;
            for (T element : elements) {
                result = result.append(element);
            }
            return result;
        }
    }

    @Override
    public Vector<Vector<T>> combinations() {
        return rangeClosed(0, length()).map(this::combinations).flatMap(Function.identity());
    }

    @Override
    public Vector<Vector<T>> combinations(int k) {
        return Combinations.apply(this, Math.max(k, 0));
    }

    @Override
    public Iterator<Vector<T>> crossProduct(int power) {
        return Collections.crossProduct(empty(), this, power);
    }

    @Override
    public Vector<T> distinct() {
        return distinctBy(Function.identity());
    }

    @Override
    public Vector<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        final Set<T> seen = new java.util.TreeSet<>(comparator);
        return filter(seen::add);
    }

    @Override
    public <U> Vector<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        final Set<U> seen = new java.util.HashSet<>();
        return filter(t -> seen.add(keyExtractor.apply(t)));
    }

    @Override
    public Vector<T> drop(int n) {
        if (n <= 0) {
            return this;
        } else if (n >= length()) {
            return empty();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Vector<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropUntil(predicate.negate());
    }

    @Override
    public Vector<T> dropUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (int i = 0; i < length(); i++) {
            if (predicate.test(get(i))) {
                return drop(i);
            }
        }
        return empty();
    }

    @Override
    public Vector<T> dropRight(int n) {
        if (n <= 0) {
            return this;
        } else if (n >= length()) {
            return empty();
        } else {
            return take(length() - n);
        }
    }

    @Override
    public Vector<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");

        final Vector<T> results = ofAll(iterator().filter(predicate));
        if (results.isEmpty()) {
            return empty();
        } else if (results.length() == length()) {
            return this;
        } else {
            return results;
        }
    }

    @Override
    public <U> Vector<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return empty();
        } else {
            final Iterator<? extends U> results = iterator().flatMap(mapper);
            return results.isEmpty() ? empty()
                                     : ofAll(results);
        }
    }

    @Override
    public T get(int index) {
        if ((index < 0) || (index >= length())) throw new IndexOutOfBoundsException("get(" + index + ")");

        if (index < leadingLength()) {
            return Arrays2.get(type, leading, index);
        } else if (index < trailingStartIndex()) {
            index -= leadingLength();
            final Object leaf = middle.getLeaf(index);
            return Arrays2.get(type, leaf, lastDigit(middle.offset() + index));
        } else {
            index -= trailingStartIndex();
            return Arrays2.get(type, trailing, index);
        }
    }

    Object getLeafUnsafe(int index) {
        if ((index < 0) || (index >= length())) throw new IndexOutOfBoundsException("get(" + index + ")");

        if (index < leadingLength()) {
            return leading;
        } else if (index < trailingStartIndex()) {
            return middle.getLeaf(index - leadingLength());
        } else {
            return trailing;
        }
    }

    private int trailingStartIndex() { return leadingLength() + middle.length(); }

    @Override
    public T head() {
        return Arrays2.get(type, leading, 0);
    }

    @Override
    public <C> Map<C, Vector<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return Collections.groupBy(this, classifier, Vector::ofAll);
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
            throw new UnsupportedOperationException("init of empty Vector");
        } else {
            return dropRight(1);
        }
    }

    @Override
    public Option<? extends Vector<T>> initOption() {
        return isEmpty() ? Option.none() : Option.some(init());
    }

    @Override
    public Vector<T> insert(int index, T element) {
        return insertAll(index, List.of(element));
    }

    @Override
    public Vector<T> insertAll(int index, Iterable<? extends T> elements) {
        if ((index < 0) || (index > length())) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e) on Vector of length " + length());
        }

        final Vector<T> begin = take(index);
        final Vector<T> end = drop(index);
        return begin.appendAll(elements).appendAll(end);
    }

    @Override
    public Vector<T> intersperse(T element) {
        return ofAll(iterator().intersperse(element));
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public boolean isTraversableAgain() {
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int i = 0;

            @Override
            public boolean hasNext() { return i < Vector.this.length(); }

            @Override
            public T next() { return Vector.this.get(i++); }
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
        return length;
    }

    @Override
    public <U> Vector<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");

        if (isEmpty()) {
            return empty();
        } else {
            final Iterator<? extends U> results = iterator().map(mapper);
            return results.isEmpty() ? empty()
                                     : ofAll(results);
        }
    }

    @Override
    public Vector<T> padTo(int length, T element) {
        final int actualLength = length();
        return (length <= actualLength) ? this
                                        : appendAll(Iterator.continually(element)
                                                            .take(length - actualLength));
    }

    @Override
    public Vector<T> leftPadTo(int length, T element) {
        if (length <= length()) {
            return this;
        } else {
            final Iterator<T> prefix = Iterator.continually(element).take(length - length());
            return prependAll(prefix);
        }
    }

    @Override
    public Vector<T> patch(int from, Iterable<? extends T> that, int replaced) {
        from = Math.max(from, 0);
        replaced = Math.max(replaced, 0);

        Vector<T> result = take(from).appendAll(that);
        from += replaced;
        result = result.appendAll(drop(from));
        return result;
    }

    @Override
    public Tuple2<Vector<T>, Vector<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final ArrayList<T> left = new ArrayList<>(), right = new ArrayList<>();
        for (int i = 0; i < length(); i++) {
            final T t = get(i);
            (predicate.test(t) ? left : right).add(t);
        }
        return Tuple.of(ofAll(left), ofAll(right));
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
            return empty();
        } else if (length() == 1) {
            return of(this);
        } else {
            Vector<Vector<T>> results = empty();
            for (T t : distinct()) {
                for (Vector<T> ts : remove(t).permutations()) {
                    results = results.append(of(t).appendAll(ts));
                }
            }
            return results;
        }
    }

    @Override
    public Vector<T> prepend(T element) {
        if (isEmpty()) {
            return of(element);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Vector<T> prependAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (!elements.iterator().hasNext()) {
            return this;
        } else if (isEmpty()) {
            return ofAll(elements);
        } else {
            Vector<T> result = this;
            for (T element : seq(elements).reverse()) {
                result = result.prepend(element);
            }
            return result;
        }
    }

    @Override
    public Vector<T> remove(T element) {
        for (int i = 0; i < length(); i++) {
            if (Objects.equals(get(i), element)) {
                return removeAt(i);
            }
        }
        return this;
    }

    @Override
    public Vector<T> removeFirst(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (int i = 0; i < length(); i++) {
            if (predicate.test(get(i))) {
                return removeAt(i);
            }
        }
        return this;
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
        if ((index < 0) || (index >= length())) {
            throw new IndexOutOfBoundsException("removeAt(" + index + ")");
        } else {
            final Vector<T> begin = take(index);
            final Vector<T> end = drop(index + 1);
            return begin.appendAll(end);
        }
    }

    @Override
    public Vector<T> removeAll(T element) {
        return Collections.removeAll(this, element);
    }

    @Override
    public Vector<T> removeAll(Iterable<? extends T> elements) {
        return Collections.removeAll(this, elements);
    }

    @Override
    public Vector<T> removeAll(Predicate<? super T> predicate) {
        return Collections.removeAll(this, predicate);
    }

    @Override
    public Vector<T> replace(T currentElement, T newElement) {
        return indexOfOption(currentElement).map(i -> update(i, newElement))
                                            .getOrElse(this);
    }

    @Override
    public Vector<T> replaceAll(T currentElement, T newElement) {
        Vector<T> result = this;
        int index = 0;
        for (T value : iterator()) {
            if (Objects.equals(value, currentElement)) {
                result = result.update(index, newElement);
            }
            index++;
        }
        return result;
    }

    @Override
    public Vector<T> retainAll(Iterable<? extends T> elements) {
        return Collections.retainAll(this, elements);
    }

    @Override
    public Vector<T> reverse() {
        return (length() <= 1) ? this : ofAll(reverseIterator());
    }

    @Override
    public Vector<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation) {
        return scanLeft(zero, operation);
    }

    @Override
    public <U> Vector<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanLeft(this, zero, operation, empty(), Vector::append, Function.identity());
    }

    @Override
    public <U> Vector<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanRight(this, zero, operation, empty(), Vector::prepend, Function.identity());
    }

    @Override
    public Vector<T> slice(int beginIndex, int endIndex) {
        if ((beginIndex >= endIndex) || (beginIndex >= size()) || isEmpty()) {
            return empty();
        } else if ((beginIndex <= 0) && (endIndex >= length())) {
            return this;
        } else {
            return take(endIndex).drop(beginIndex);
        }
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
    public Vector<T> sorted() {
        return isEmpty() ? this : toJavaStream().sorted().collect(collector());
    }

    @Override
    public Vector<T> sorted(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return isEmpty() ? this : toJavaStream().sorted(comparator).collect(collector());
    }

    @Override
    public <U extends Comparable<? super U>> Vector<T> sortBy(Function<? super T, ? extends U> mapper) {
        return sortBy(U::compareTo, mapper);
    }

    @Override
    public <U> Vector<T> sortBy(Comparator<? super U> comparator, Function<? super T, ? extends U> mapper) {
        final Function<? super T, ? extends U> domain = Function1.of(mapper::apply).memoized();
        return toJavaStream().sorted((e1, e2) -> comparator.compare(domain.apply(e1), domain.apply(e2)))
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
        return Tuple.of(init, drop(init.size()));
    }

    @Override
    public Tuple2<Vector<T>, Vector<T>> splitAtInclusive(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (predicate.test(value)) {
                return (i == (length() - 1)) ? Tuple.of(this, empty())
                                             : Tuple.of(take(i + 1), drop(i + 1));
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
        if ((beginIndex < 0) || (beginIndex > length())) {
            throw new IndexOutOfBoundsException("subSequence(" + beginIndex + ")");
        } else {
            return drop(beginIndex);
        }
    }

    @Override
    public Vector<T> subSequence(int beginIndex, int endIndex) {
        if ((beginIndex < 0) || (beginIndex > endIndex) || (endIndex > length())) {
            throw new IndexOutOfBoundsException("subSequence(" + beginIndex + ", " + endIndex + ") on Vector of size " + length());
        } else {
            return slice(beginIndex, endIndex);
        }
    }

    @Override
    public Vector<T> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty Vector");
        } else {
            return drop(1);
        }
    }

    @Override
    public Option<Vector<T>> tailOption() {
        return isEmpty() ? Option.none() : Option.some(tail());
    }

    @Override
    public Vector<T> take(int n) {
        if (n >= length()) {
            return this;
        } else if (n <= 0) {
            return empty();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Vector<T> takeRight(int n) {
        if (n >= length()) {
            return this;
        } else if (n <= 0) {
            return empty();
        } else {
            return drop(length() - n);
        }
    }

    @Override
    public Vector<T> takeUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @Override
    public Vector<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (int i = 0; i < length(); i++) {
            final T value = get(i);
            if (!predicate.test(value)) {
                return take(i);
            }
        }
        return this;
    }

    /**
     * Transforms this {@code Vector}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U transform(Function<? super Vector<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    @Override
    public <U> Vector<U> unit(Iterable<? extends U> iterable) {
        return ofAll(iterable);
    }

    @Override
    public <T1, T2> Tuple2<Vector<T1>, Vector<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        Vector<T1> xs = empty();
        Vector<T2> ys = empty();
        for (int i = 0; i < length(); i++) {
            final Tuple2<? extends T1, ? extends T2> t = unzipper.apply(get(i));
            xs = xs.append(t._1);
            ys = ys.append(t._2);
        }
        return Tuple.of(xs, ys);
    }

    @Override
    public <T1, T2, T3> Tuple3<Vector<T1>, Vector<T2>, Vector<T3>> unzip3(Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        Vector<T1> xs = empty();
        Vector<T2> ys = empty();
        Vector<T3> zs = empty();
        for (int i = 0; i < length(); i++) {
            final Tuple3<? extends T1, ? extends T2, ? extends T3> t = unzipper.apply(get(i));
            xs = xs.append(t._1);
            ys = ys.append(t._2);
            zs = zs.append(t._3);
        }
        return Tuple.of(xs, ys, zs);
    }

    @Override
    public Vector<T> update(int index, T element) {
        if ((index < 0) || (index >= length())) {
            throw new IndexOutOfBoundsException("update(" + index + ")");
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public <U> Vector<Tuple2<T, U>> zip(Iterable<? extends U> that) {
        return zipWith(that, Tuple::of);
    }

    @Override
    public <U, R> Vector<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return ofAll(iterator().zipWith(that, mapper));
    }

    @Override
    public <U> Vector<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    public Vector<Tuple2<T, Integer>> zipWithIndex() {
        return zipWithIndex(Tuple::of);
    }

    @Override
    public <U> Vector<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return ofAll(iterator().zipWithIndex(mapper));
    }

    private Object readResolve() { return isEmpty() ? EMPTY : this; }

    @Override
    public boolean equals(Object that) {
        return (that == this) || ((that instanceof Vector) && areEqual(this, (Vector<?>) that));
    }

    @Override
    public int hashCode() {
        return hash(this);
    }

    @Override
    public String stringPrefix() {
        return "Vector";
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    static final class VectorTree<T> implements Serializable {
        private static final long serialVersionUID = 1L;

        private static final VectorTree<?> EMPTY = new VectorTree<>(null, emptyArray(), 0, 0, 0);
        static <T> VectorTree<T> emptyTree() { return (VectorTree<T>) EMPTY; }

        private final Class<?> type;
        private final Object array;
        private final int offset, length;
        private final int depthShift;

        static <T> VectorTree<T> create(Class<?> type, Object array, int offset, int length, int depthShift) {
            if (length == 0) {
                assert (offset == 0) && (depthShift == 0);
                return (VectorTree<T>) EMPTY;
            } else {
                return new VectorTree<T>(type, array, offset, length, depthShift);
            }
        }

        private VectorTree(Class<?> type, Object array, int offset, int length, int depthShift) {
            this.type = type;
            this.array = array;
            this.offset = offset;
            this.length = length;
            this.depthShift = depthShift;

            assert length() <= treeSize(branchingFactor(), depthShift);
        }

        static int treeSize(int branchCount, int depthShift) {
            final int fullBranchSize = 1 << depthShift;
            return branchCount * fullBranchSize;
        }

        int offset() { return offset; }

        Object getLeaf(int index) {
            if (depthShift == 0) {
                return array;
            } else {
                index += offset;
                assert index >= 0;

                if (depthShift == BRANCHING_BASE) {
                    return ((Object[]) array)[firstDigit(index, depthShift)];
                } else {
                    Object root = ((Object[]) array)[firstDigit(index, depthShift)];

                    int depthShift = this.depthShift - BRANCHING_BASE;
                    root = ((Object[]) root)[digit(index, depthShift)];

                    while (depthShift > BRANCHING_BASE) {
                        depthShift -= BRANCHING_BASE;
                        root = ((Object[]) root)[digit(index, depthShift)];
                    }

                    assert root != null;
                    return root;
                }
            }
        }

        int length() { return length; }
    }
}

interface VectorModule {
    final class Combinations {
        static <T> Vector<Vector<T>> apply(Vector<T> elements, int k) {
            return (k == 0)
                   ? Vector.of(Vector.empty())
                   : elements.zipWithIndex().flatMap(
                    t -> apply(elements.drop(t._2 + 1), (k - 1)).map((Vector<T> c) -> c.prepend(t._1)));
        }
    }
}