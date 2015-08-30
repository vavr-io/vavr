/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Lazy;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Value;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * An immutable {@code List} is an eager sequence of elements. Its immutability makes it suitable for concurrent programming.
 * <p>
 * A {@code List} is composed of a {@code head} element and a {@code tail} {@code List}.
 * <p>
 * There are two implementations of the {@code List} interface:
 * <ul>
 * <li>{@link Nil}, which represents the empty {@code List}.</li>
 * <li>{@link Cons}, which represents a {@code List} containing one or more elements.</li>
 * </ul>
 * Methods to obtain a {@code List}:
 * <pre>
 * <code>
 * // factory methods
 * List.empty()                  // = List.of() = Nil.instance()
 * List.of(x)                    // = new Cons&lt;&gt;(x, Nil.instance())
 * List.of(Object...)            // e.g. List.of(1, 2, 3)
 * List.ofAll(java.lang.Iterable)      // e.g. List.ofAll(Stream.of(1, 2, 3)) = 1, 2, 3
 * List.ofAll(&lt;primitive array&gt;) // e.g. List.ofAll(new int[] {1, 2, 3}) = 1, 2, 3
 *
 * // int sequences
 * List.range(0, 3)              // = 0, 1, 2
 * List.rangeClosed(0, 3)        // = 0, 1, 2, 3
 * </code>
 * </pre>
 *
 * Note: A {@code List} is primary a {@code Seq} and extends {@code Stack} for technical reasons (so {@code Stack} does not need to wrap {@code List}).
 *
 *
 * Factory method applications:
 *
 * <pre>
 * <code>
 * List&lt;Integer&gt;       s1 = List.of(1);
 * List&lt;Integer&gt;       s2 = List.of(1, 2, 3);
 *                     // = List.of(new Integer[] {1, 2, 3});
 *
 * List&lt;int[]&gt;         s3 = List.of(new int[] {1, 2, 3});
 * List&lt;List&lt;Integer&gt;&gt; s4 = List.of(List.of(1, 2, 3));
 *
 * List&lt;Integer&gt;       s5 = List.ofAll(new int[] {1, 2, 3});
 * List&lt;Integer&gt;       s6 = List.ofAll(List.of(1, 2, 3));
 *
 * // cuckoo's egg
 * List&lt;Integer[]&gt;     s7 = List.&lt;Integer[]&gt; of(new Integer[] {1, 2, 3});
 *                     //!= List.&lt;Integer[]&gt; of(1, 2, 3);
 * </code>
 * </pre>
 *
 * Example: Converting a String to digits
 *
 * <pre>
 * <code>
 * // = List(1, 2, 3)
 * List.of("123".toCharArray()).map(c -&gt; Character.digit(c, 10))
 * </code>
 * </pre>
 *
 * See Okasaki, Chris: <em>Purely Functional Data Structures</em> (p. 7 ff.). Cambridge, 2003.
 *
 * @param <T> Component type of the List
 * @since 1.1.0
 */
public interface List<T> extends LinearSeq<T>, Stack<T> {

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.List}s.
     *
     * @param <T> Component type of the List.
     * @return A javaslang.collection.List Collector.
     */
    static <T> Collector<T, ArrayList<T>, List<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, List<T>> finisher = List::ofAll;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Returns the single instance of Nil. Convenience method for {@code Nil.instance()} .
     * <p>
     * Note: this method intentionally returns type {@code List} and not {@code Nil}. This comes handy when folding.
     * If you explicitly need type {@code Nil} use {@linkplain Nil#instance()}.
     *
     * @param <T> Component type of Nil, determined by type inference in the particular context.
     * @return The empty list.
     */
    static <T> List<T> empty() {
        return Nil.instance();
    }

    /**
     * Returns a singleton {@code List}, i.e. a {@code List} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new List instance containing the given element
     */
    static <T> List<T> of(T element) {
        return new Cons<>(element, Nil.instance());
    }

    /**
     * <p>
     * Creates a List of the given elements.
     * </p>
     *
     * <pre>
     * <code>  List.of(1, 2, 3, 4)
     * = Nil.instance().prepend(4).prepend(3).prepend(2).prepend(1)
     * = new Cons(1, new Cons(2, new Cons(3, new Cons(4, Nil.instance()))))</code>
     * </pre>
     *
     * @param <T>      Component type of the List.
     * @param elements Zero or more elements.
     * @return A list containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    static <T> List<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        List<T> result = Nil.<T> instance();
        for (int i = elements.length - 1; i >= 0; i--) {
            result = result.prepend(elements[i]);
        }
        return result;
    }

    /**
     * Creates a List of the given elements.
     *
     * The resulting list has the same iteration order as the given iterable of elements
     * if the iteration order of the elements is stable.
     *
     * @param <T>      Component type of the List.
     * @param elements An java.lang.Iterable of elements.
     * @return A list containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("unchecked")
    static <T> List<T> ofAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof List) {
            return (List<T>) elements;
        } else if (elements instanceof java.util.List) {
            List<T> result = Nil.instance();
            final java.util.List<T> list = (java.util.List<T>) elements;
            final ListIterator<T> iterator = list.listIterator(list.size());
            while (iterator.hasPrevious()) {
                result = result.prepend(iterator.previous());
            }
            return result;
        } else if (elements instanceof NavigableSet) {
            List<T> result = Nil.instance();
            final java.util.Iterator<T> iterator = ((NavigableSet<T>) elements).descendingIterator();
            while (iterator.hasNext()) {
                result = result.prepend(iterator.next());
            }
            return result;
        } else {
            List<T> result = Nil.instance();
            for (T element : elements) {
                result = result.prepend(element);
            }
            return result.reverse();
        }
    }

    /**
     * Creates a List based on the elements of a boolean array.
     *
     * @param array a boolean array
     * @return A new List of Boolean values
     */
    static List<Boolean> ofAll(boolean[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(() -> new Iterator<Boolean>() {
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
     * Creates a List based on the elements of a byte array.
     *
     * @param array a byte array
     * @return A new List of Byte values
     */
    static List<Byte> ofAll(byte[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(() -> new Iterator<Byte>() {
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
     * Creates a List based on the elements of a char array.
     *
     * @param array a char array
     * @return A new List of Character values
     */
    static List<Character> ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(() -> new Iterator<Character>() {
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
     * Creates a List based on the elements of a double array.
     *
     * @param array a double array
     * @return A new List of Double values
     */
    static List<Double> ofAll(double[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(() -> new Iterator<Double>() {
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
     * Creates a List based on the elements of a float array.
     *
     * @param array a float array
     * @return A new List of Float values
     */
    static List<Float> ofAll(float[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(() -> new Iterator<Float>() {
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
     * Creates a List based on the elements of an int array.
     *
     * @param array an int array
     * @return A new List of Integer values
     */
    static List<Integer> ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(() -> new Iterator<Integer>() {
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
     * Creates a List based on the elements of a long array.
     *
     * @param array a long array
     * @return A new List of Long values
     */
    static List<Long> ofAll(long[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(() -> new Iterator<Long>() {
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
     * Creates a List based on the elements of a short array.
     *
     * @param array a short array
     * @return A new List of Short values
     */
    static List<Short> ofAll(short[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(() -> new Iterator<Short>() {
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
    static List<Integer> range(int from, int toExclusive) {
        return List.rangeBy(from, toExclusive, 1);
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
    static List<Integer> rangeBy(int from, int toExclusive, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toExclusive || step * (from - toExclusive) > 0) {
            return List.empty();
        } else {
            final int one = (from < toExclusive) ? 1 : -1;
            return List.rangeClosedBy(from, toExclusive - one, step);
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
    static List<Long> range(long from, long toExclusive) {
        return List.rangeBy(from, toExclusive, 1);
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
    static List<Long> rangeBy(long from, long toExclusive, long step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toExclusive || step * (from - toExclusive) > 0) {
            return List.empty();
        } else {
            final int one = (from < toExclusive) ? 1 : -1;
            return List.rangeClosedBy(from, toExclusive - one, step);
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
    static List<Integer> rangeClosed(int from, int toInclusive) {
        return List.rangeClosedBy(from, toInclusive, 1);
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
    static List<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toInclusive) {
            return List.of(from);
        } else if (step * (from - toInclusive) > 0) {
            return List.empty();
        } else {
            final int gap = (from - toInclusive) % step;
            final int signum = (from < toInclusive) ? -1 : 1;
            final int bound = from * signum;
            List<Integer> result = List.empty();
            for (int i = toInclusive + gap; i * signum <= bound; i -= step) {
                result = result.prepend(i);
            }
            return result;
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
    static List<Long> rangeClosed(long from, long toInclusive) {
        return List.rangeClosedBy(from, toInclusive, 1L);
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
    static List<Long> rangeClosedBy(long from, long toInclusive, long step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0");
        } else if (from == toInclusive) {
            return List.of(from);
        } else if (step * (from - toInclusive) > 0) {
            return List.empty();
        } else {
            final long gap = (from - toInclusive) % step;
            final int signum = (from < toInclusive) ? -1 : 1;
            final long bound = from * signum;
            List<Long> result = List.empty();
            for (long i = toInclusive + gap; i * signum <= bound; i -= step) {
                result = result.prepend(i);
            }
            return result;
        }
    }

    @Override
    default List<T> append(T element) {
        return foldRight(List.of(element), (x, xs) -> xs.prepend(x));
    }

    @Override
    default List<T> appendAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return foldRight(List.ofAll(elements), (x, xs) -> xs.prepend(x));
    }

    @Override
    default List<Tuple2<T, T>> crossProduct() {
        return crossProduct(this);
    }

    @Override
    default <U> List<Tuple2<T, U>> crossProduct(java.lang.Iterable<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        final List<? extends U> other = unit(that);
        return flatMap(a -> other.map(b -> Tuple.of(a, b)));
    }

    @Override
    default List<T> clear() {
        return Nil.instance();
    }

    @Override
    default List<List<T>> combinations() {
        return List.rangeClosed(0, length()).map(this::combinations).flatMap(Function.identity());
    }

    @Override
    default List<List<T>> combinations(int k) {
        class Recursion {
            List<List<T>> combinations(List<T> elements, int k) {
                return (k == 0)
                        ? List.of(List.empty())
                        : elements.zipWithIndex().flatMap(t -> combinations(elements.drop(t._2 + 1), (k - 1))
                        .map((List<T> c) -> c.prepend(t._1)));
            }
        }
        return new Recursion().combinations(this, Math.max(k, 0));
    }

    @Override
    default List<T> distinct() {
        return distinctBy(Function.identity());
    }

    @Override
    default List<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        final java.util.Set<T> seen = new java.util.TreeSet<>(comparator);
        return filter(seen::add);
    }

    @Override
    default <U> List<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        final java.util.Set<U> seen = new java.util.HashSet<>();
        return filter(t -> seen.add(keyExtractor.apply(t)));
    }

    @Override
    default List<T> drop(int n) {
        List<T> list = this;
        for (int i = n; i > 0 && !list.isEmpty(); i--) {
            list = list.tail();
        }
        return list;
    }

    @Override
    default List<T> dropRight(int n) {
        if (n <= 0) {
            return this;
        }
        if (n >= length()) {
            return empty();
        }
        return List.ofAll(iterator().dropRight(n));
    }

    @Override
    default List<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        List<T> list = this;
        while (!list.isEmpty() && predicate.test(list.head())) {
            list = list.tail();
        }
        return list;
    }

    @Override
    default List<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final List<T> filtered = foldLeft(List.<T> empty(), (xs, x) -> predicate.test(x) ? xs.prepend(x) : xs);
        return this.length() == filtered.length() ? this : filtered.reverse();
    }

    @Override
    default <U> List<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return empty();
        } else {
            List<U> list = empty();
            for (T t : this) {
                for (U u : mapper.apply(t)) {
                    list = list.prepend(u);
                }
            }
            return list.reverse();
        }
    }

    @Override
    default <U> List<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper) {
        return flatMap(mapper);
    }

    @Override
    List<Object> flatten();

    @Override
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        Stack.super.forEach(action);
    }

    @Override
    default boolean forAll(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return Stack.super.forAll(predicate);
    }

    @Override
    default T get(int index) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("get(" + index + ") on Nil");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("get(" + index + ")");
        }
        List<T> list = this;
        for (int i = index - 1; i >= 0; i--) {
            list = list.tail();
            if (list.isEmpty()) {
                throw new IndexOutOfBoundsException(String.format("get(%s) on List of length %s", index, index - i));
            }
        }
        return list.head();
    }

    @Override
    default <C> Map<C, List<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return foldLeft(HashMap.empty(), (map, t) -> {
            final C key = classifier.apply(t);
            final List<T> values = map.get(key).map(ts -> ts.prepend(t)).orElse(List.of(t));
            return map.put(key, values);
        });
    }

    @Override
    default int indexOf(T element, int from) {
        int index = 0;
        for (List<T> list = this; !list.isEmpty(); list = list.tail(), index++) {
            if (index >= from && Objects.equals(list.head(), element)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    List<T> init();

    @Override
    Option<List<T>> initOption();

    @Override
    int length();

    @Override
    default List<T> insert(int index, T element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        }
        List<T> preceding = Nil.instance();
        List<T> tail = this;
        for (int i = index; i > 0; i--, tail = tail.tail()) {
            if (tail.isEmpty()) {
                throw new IndexOutOfBoundsException("insert(" + index + ", e) on List of length " + length());
            }
            preceding = preceding.prepend(tail.head());
        }
        List<T> result = tail.prepend(element);
        for (T next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<T> insertAll(int index, java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (index < 0) {
            throw new IndexOutOfBoundsException("insertAll(" + index + ", elements)");
        }
        List<T> preceding = Nil.instance();
        List<T> tail = this;
        for (int i = index; i > 0; i--, tail = tail.tail()) {
            if (tail.isEmpty()) {
                throw new IndexOutOfBoundsException("insertAll(" + index + ", elements) on List of length " + length());
            }
            preceding = preceding.prepend(tail.head());
        }
        List<T> result = tail.prependAll(elements);
        for (T next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<T> intersperse(T element) {
        return isEmpty() ? Nil.instance() : foldRight(empty(), (x, xs) -> xs.isEmpty() ? xs.prepend(x) : xs.prepend(element).prepend(x));
    }

    @Override
    default int lastIndexOf(T element, int end) {
        int result = -1, index = 0;
        for (List<T> list = this; index <= end && !list.isEmpty(); list = list.tail(), index++) {
            if (Objects.equals(list.head(), element)) {
                result = index;
            }
        }
        return result;
    }

    @Override
    default <U> List<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        List<U> list = empty();
        for (T t : this) {
            list = list.prepend(mapper.apply(t));
        }
        return list.reverse();
    }

    @Override
    default List<T> padTo(int length, T element) {
        if(length <= length()) {
            return this;
        } else {
            return appendAll(Stream.gen(() -> element).take(length - length()));
        }
    }

    @Override
    default Tuple2<List<T>, List<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final java.util.List<T> left = new ArrayList<>(), right = new ArrayList<>();
        for (T t : this) {
            (predicate.test(t) ? left : right).add(t);
        }
        return Tuple.of(List.ofAll(left), List.ofAll(right));
    }

    @Override
    default T peek() {
        return head();
    }

    /**
     * Performs an action on the head element of this {@code List}.
     *
     * @param action A {@code Consumer}
     * @return this {@code List}
     */
    @Override
    default List<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(head());
        }
        return this;
    }

    @Override
    default List<List<T>> permutations() {
        if (isEmpty()) {
            return Nil.instance();
        } else {
            final List<T> tail = tail();
            if (tail.isEmpty()) {
                return List.of(this);
            } else {
                final List<List<T>> zero = Nil.instance();
                // TODO: IntelliJ IDEA 14.1.1 needs a redundant cast here, jdk 1.8.0_40 compiles fine
                return distinct().foldLeft(zero, (xs, x) -> xs.appendAll(remove(x).permutations().map((Function<List<T>, List<T>>) l -> l.prepend(x))));
            }
        }
    }

    @Override
    default List<T> pop() {
        return tail();
    }

    @Override
    Option<List<T>> popOption();

    @Override
    default Tuple2<T, List<T>> pop2() {
        return Tuple.of(head(), tail());
    }

    @Override
    Option<Tuple2<T, List<T>>> pop2Option();

    @Override
    default List<T> prepend(T element) {
        return new Cons<>(element, this);
    }

    @Override
    default List<T> prependAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return isEmpty() ? List.ofAll(elements) : List.ofAll(elements).reverse().foldLeft(this, List::prepend);
    }

    @Override
    default List<T> push(T element) {
        return new Cons<>(element, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    default List<T> push(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        List<T> result = Nil.<T> instance();
        for (T element : elements) {
            result = result.prepend(element);
        }
        return result;
    }

    @Override
    default List<T> pushAll(java.lang.Iterable<T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        List<T> result = Nil.<T> instance();
        for (T element : elements) {
            result = result.prepend(element);
        }
        return result;
    }

    @Override
    default List<T> remove(T element) {
        List<T> preceding = Nil.instance();
        List<T> tail = this;
        boolean found = false;
        while (!found && !tail.isEmpty()) {
            final T head = tail.head();
            if (head.equals(element)) {
                found = true;
            } else {
                preceding = preceding.prepend(head);
            }
            tail = tail.tail();
        }
        if (!found) {
            return this;
        }
        List<T> result = tail;
        for (T next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<T> removeFirst(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        List<T> init = List.empty();
        List<T> tail = this;
        while (!tail.isEmpty() && !predicate.test(tail.head())) {
            init = init.prepend(tail.head());
            tail = tail.tail();
        }
        if (tail.isEmpty()) {
            return this;
        } else {
            return init.foldLeft(tail.tail(), List::prepend);
        }
    }

    @Override
    default List<T> removeLast(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final List<T> removedAndReversed = reverse().removeFirst(predicate);
        return removedAndReversed.length() == length() ? this : removedAndReversed.reverse();
    }

    @Override
    List<T> removeAt(int indx);

    @Override
    default List<T> removeAll(T removed) {
        List<T> result = Nil.instance();
        boolean found = false;
        for (T element : this) {
            if (element.equals(removed)) {
                found = true;
            } else {
                result = result.prepend(element);
            }
        }
        return found ? result.reverse() : this;
    }

    @Override
    default List<T> removeAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        List<T> removed = List.ofAll(elements).distinct();
        List<T> result = Nil.instance();
        boolean found = false;
        for (T element : this) {
            if (removed.contains(element)) {
                found = true;
            } else {
                result = result.prepend(element);
            }
        }
        return found ? result.reverse() : this;
    }

    @Override
    default List<T> replace(T currentElement, T newElement) {
        List<T> preceding = Nil.instance();
        List<T> tail = this;
        while (!tail.isEmpty() && !Objects.equals(tail.head(), currentElement)) {
            preceding = preceding.prepend(tail.head());
            tail = tail.tail();
        }
        if (tail.isEmpty()) {
            return this;
        }
        // skip the current head element because it is replaced
        List<T> result = tail.tail().prepend(newElement);
        for (T next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<T> replaceAll(T currentElement, T newElement) {
        List<T> result = Nil.instance();
        for (List<T> list = this; !list.isEmpty(); list = list.tail()) {
            final T head = list.head();
            final T elem = Objects.equals(head, currentElement) ? newElement : head;
            result = result.prepend(elem);
        }
        return result.reverse();
    }

    @Override
    default List<T> replaceAll(UnaryOperator<T> operator) {
        Objects.requireNonNull(operator, "operator is null");
        List<T> result = Nil.instance();
        for (T element : this) {
            result = result.prepend(operator.apply(element));
        }
        return result.reverse();
    }

    @Override
    default List<T> retainAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final List<T> keeped = List.ofAll(elements).distinct();
        List<T> result = Nil.instance();
        for (T element : this) {
            if (keeped.contains(element)) {
                result = result.prepend(element);
            }
        }
        return result.reverse();
    }

    @Override
    default List<T> reverse() {
        return isEmpty() ? this : foldLeft(empty(), List::prepend);
    }

    @Override
    default List<T> set(int index, T element) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("set(" + index + ", e) on Nil");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("set(" + index + ", e)");
        }
        List<T> preceding = Nil.instance();
        List<T> tail = this;
        for (int i = index; i > 0; i--, tail = tail.tail()) {
            if (tail.isEmpty()) {
                throw new IndexOutOfBoundsException("set(" + index + ", e) on List of length " + length());
            }
            preceding = preceding.prepend(tail.head());
        }
        if (tail.isEmpty()) {
            throw new IndexOutOfBoundsException("set(" + index + ", e) on List of length " + length());
        }
        // skip the current head element because it is replaced
        List<T> result = tail.tail().prepend(element);
        for (T next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<T> slice(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("slice(" + beginIndex + ")");
        }
        List<T> result = this;
        for (int i = 0; i < beginIndex; i++, result = result.tail()) {
            if (result.isEmpty()) {
                throw new IndexOutOfBoundsException(
                        String.format("slice(%s) on List of length %s", beginIndex, i));
            }
        }
        return result;
    }

    @Override
    default List<T> slice(int beginIndex, int endIndex) {
        if (beginIndex < 0 || beginIndex > endIndex) {
            throw new IndexOutOfBoundsException(
                    String.format("slice(%s, %s) on List of length %s", beginIndex, endIndex, length()));
        }
        List<T> result = Nil.instance();
        List<T> list = this;
        for (int i = 0; i < endIndex; i++, list = list.tail()) {
            if (list.isEmpty()) {
                throw new IndexOutOfBoundsException(
                        String.format("slice(%s, %s) on List of length %s", beginIndex, endIndex, i));
            }
            if (i >= beginIndex) {
                result = result.prepend(list.head());
            }
        }
        return result.reverse();
    }

    @Override
    default List<T> sort() {
        return isEmpty() ? this : toJavaStream().sorted().collect(List.collector());
    }

    @Override
    default List<T> sort(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return isEmpty() ? this : toJavaStream().sorted(comparator).collect(List.collector());
    }

    @Override
    default Tuple2<List<T>, List<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<T>, Iterator<T>> itt = iterator().span(predicate);
        return Tuple.of(List.ofAll(itt._1), List.ofAll(itt._2));
    }

    @Override
    Tuple2<List<T>, List<T>> splitAt(int n);

    @Override
    Tuple2<List<T>, List<T>> splitAt(Predicate<? super T> predicate);

    @Override
    Tuple2<List<T>, List<T>> splitAtInclusive(Predicate<? super T> predicate);

    @Override
    default Spliterator<T> spliterator() {
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    List<T> tail();

    @Override
    Option<List<T>> tailOption();

    @Override
    default List<T> take(int n) {
        if (n >= length()) {
            return this;
        }
        if (n <= 0) {
            return empty();
        }
        List<T> result = Nil.instance();
        List<T> list = this;
        for (int i = 0; i < n && !list.isEmpty(); i++, list = list.tail()) {
            result = result.prepend(list.head());
        }
        return result.reverse();
    }

    @Override
    default List<T> takeRight(int n) {
        if (n >= length()) {
            return this;
        }
        if (n <= 0) {
            return empty();
        }
        return reverse().take(n).reverse();
    }

    @Override
    default List<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        List<T> result = Nil.instance();
        for (List<T> list = this; !list.isEmpty() && predicate.test(list.head()); list = list.tail()) {
            result = result.prepend(list.head());
        }
        return result.length() == length() ? this : result.reverse();
    }

    @Override
    default <U> List<U> unit(java.lang.Iterable<? extends U> iterable) {
        return List.ofAll(iterable);
    }

    @Override
    default <T1, T2> Tuple2<List<T1>, List<T2>> unzip(
            Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        List<T1> xs = Nil.instance();
        List<T2> ys = Nil.instance();
        for (T element : this) {
            final Tuple2<? extends T1, ? extends T2> t = unzipper.apply(element);
            xs = xs.prepend(t._1);
            ys = ys.prepend(t._2);
        }
        return Tuple.of(xs.reverse(), ys.reverse());
    }

    @Override
    default <U> List<Tuple2<T, U>> zip(java.lang.Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        List<Tuple2<T, U>> result = Nil.instance();
        List<T> list1 = this;
        java.util.Iterator<U> list2 = that.iterator();
        while (!list1.isEmpty() && list2.hasNext()) {
            result = result.prepend(Tuple.of(list1.head(), list2.next()));
            list1 = list1.tail();
        }
        return result.reverse();
    }

    @Override
    default <U> List<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        List<Tuple2<T, U>> result = Nil.instance();
        Iterator<T> list1 = this.iterator();
        java.util.Iterator<U> list2 = that.iterator();
        while (list1.hasNext() || list2.hasNext()) {
            final T elem1 = list1.hasNext() ? list1.next() : thisElem;
            final U elem2 = list2.hasNext() ? list2.next() : thatElem;
            result = result.prepend(Tuple.of(elem1, elem2));
        }
        return result.reverse();
    }

    @Override
    default List<Tuple2<T, Integer>> zipWithIndex() {
        List<Tuple2<T, Integer>> result = Nil.instance();
        int index = 0;
        for (List<T> list = this; !list.isEmpty(); list = list.tail()) {
            result = result.prepend(Tuple.of(list.head(), index++));
        }
        return result.reverse();
    }

    /**
     * Non-empty {@code List}, consisting of a {@code head} and a {@code tail}.
     *
     * @param <T> Component type of the List.
     * @since 1.1.0
     */
    // DEV NOTE: class declared final because of serialization proxy pattern (see Effective Java, 2nd ed., p. 315)
    final class Cons<T> implements List<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private final T head;
        private final List<T> tail;
        private final int length;

        private final transient Lazy<Integer> hashCode = Lazy.of(() -> Traversable.hash(this));

        /**
         * Creates a List consisting of a head value and a trailing List.
         *
         * @param head The head
         * @param tail The tail
         */
        public Cons(T head, List<T> tail) {
            this.head = head;
            this.tail = tail;
            this.length = 1 + tail.length();
        }

        @Override
        public List<Object> flatten() {
            return flatMap(t -> (t instanceof java.lang.Iterable) ? List.ofAll((java.lang.Iterable<?>) t).flatten() : List.of(t));
        }

        @Override
        public boolean hasDefiniteSize() {
            return true;
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public Some<T> headOption() {
            return new Some<>(head);
        }

        @Override
        public List<T> init() {
            return dropRight(1);
        }

        @Override
        public Some<List<T>> initOption() {
            return new Some<>(init());
        }

        @Override
        public boolean isTraversableAgain() {
            return true;
        }

        @Override
        public int length() {
            return length;
        }

        @Override
        public Tuple2<List<T>, List<T>> splitAt(int n) {
            List<T> init = Nil.instance();
            List<T> tail = this;
            while (n > 0 && !tail.isEmpty()) {
                init = init.prepend(tail.head());
                tail = tail.tail();
                n--;
            }
            return Tuple.of(init.reverse(), tail);
        }

        @Override
        public Tuple2<List<T>, List<T>> splitAt(Predicate<? super T> predicate) {
            final Tuple2<List<T>, List<T>> t = splitByPredicateReversed(this, predicate);
            if (t._2.isEmpty()) {
                return Tuple.of(this, empty());
            } else {
                return Tuple.of(t._1.reverse(), t._2);
            }
        }

        @Override
        public Tuple2<List<T>, List<T>> splitAtInclusive(Predicate<? super T> predicate) {
            final Tuple2<List<T>, List<T>> t = splitByPredicateReversed(this, predicate);
            if (t._2.isEmpty() || t._2.tail().isEmpty()) {
                return Tuple.of(this, empty());
            } else {
                return Tuple.of(t._1.prepend(t._2.head()).reverse(), t._2.tail());
            }
        }

        @Override
        public boolean startsWith(Iterable<? extends T> that, int offset) {
            if (offset > 0) {
                if (offset > length()) {
                    return false;
                } else {
                    return drop(offset).startsWith(that);
                }
            }
            final java.util.Iterator<? extends T> it = that.iterator();
            List<T> list = this;
            while (it.hasNext() && !list.isEmpty()) {
                if (Objects.equals(it.next(), list.head())) {
                    list = list.tail();
                } else {
                    return false;
                }
            }
            return !it.hasNext();
        }

        private static <T> Tuple2<List<T>, List<T>> splitByPredicateReversed(List<T> source, Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            List<T> init = Nil.instance();
            List<T> tail = source;
            while (!tail.isEmpty() && !predicate.test(tail.head())) {
                init = init.prepend(tail.head());
                tail = tail.tail();
            }
            return Tuple.of(init, tail);
        }

        @Override
        public Some<T> peekOption() {
            return new Some<>(head());
        }

        @Override
        public Some<List<T>> popOption() {
            return new Some<>(tail());
        }

        @Override
        public Some<Tuple2<T, List<T>>> pop2Option() {
            return new Some<>(Tuple.of(head(), tail()));
        }

        @Override
        public List<T> removeAt(int indx) {
            if(indx < 0) {
                throw new IndexOutOfBoundsException("removeAt(" + indx + ")");
            }
            List<T> init = Nil.instance();
            List<T> tail = this;
            while (indx > 0 && !tail.isEmpty()) {
                init = init.prepend(tail.head());
                tail = tail.tail();
                indx--;
            }
            if(indx > 0 && tail.isEmpty()) {
                throw new IndexOutOfBoundsException("removeAt() on Nil");
            }
            return init.reverse().appendAll(tail.tail());
        }

        @Override
        public List<T> tail() {
            return tail;
        }

        @Override
        public Some<List<T>> tailOption() {
            return new Some<>(tail);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof List) {
                List<?> list1 = this;
                List<?> list2 = (List<?>) o;
                while (!list1.isEmpty() && !list2.isEmpty()) {
                    final boolean isEqual = Objects.equals(list1.head(), list2.head());
                    if (!isEqual) {
                        return false;
                    }
                    list1 = list1.tail();
                    list2 = list2.tail();
                }
                return list1.isEmpty() && list2.isEmpty();
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
            return mkString(", ", "List(", ")");
        }

        /**
         * <p>
         * {@code writeReplace} method for the serialization proxy pattern.
         * </p>
         * <p>
         * The presence of this method causes the serialization system to emit a SerializationProxy instance instead of
         * an instance of the enclosing class.
         * </p>
         *
         * @return A SerialiationProxy for this enclosing class.
         */
        private Object writeReplace() {
            return new SerializationProxy<>(this);
        }

        /**
         * <p>
         * {@code readObject} method for the serialization proxy pattern.
         * </p>
         * Guarantees that the serialization system will never generate a serialized instance of the enclosing class.
         *
         * @param stream An object serialization stream.
         * @throws java.io.InvalidObjectException This method will throw with the message "Proxy required".
         */
        private void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        /**
         * A serialization proxy which, in this context, is used to deserialize immutable, linked Lists with final
         * instance fields.
         *
         * @param <T> The component type of the underlying list.
         */
        // DEV NOTE: The serialization proxy pattern is not compatible with non-final, i.e. extendable,
        // classes. Also, it may not be compatible with circular object graphs.
        private static final class SerializationProxy<T> implements Serializable {

            private static final long serialVersionUID = 1L;

            // the instance to be serialized/deserialized
            private transient Cons<T> list;

            /**
             * Constructor for the case of serialization, called by {@link Cons#writeReplace()}.
             * <p/>
             * The constructor of a SerializationProxy takes an argument that concisely represents the logical state of
             * an instance of the enclosing class.
             *
             * @param list a Cons
             */
            SerializationProxy(Cons<T> list) {
                this.list = list;
            }

            /**
             * Write an object to a serialization stream.
             *
             * @param s An object serialization stream.
             * @throws java.io.IOException If an error occurs writing to the stream.
             */
            private void writeObject(ObjectOutputStream s) throws IOException {
                s.defaultWriteObject();
                s.writeInt(list.length());
                for (List<T> l = list; !l.isEmpty(); l = l.tail()) {
                    s.writeObject(l.head());
                }
            }

            /**
             * Read an object from a deserialization stream.
             *
             * @param s An object deserialization stream.
             * @throws ClassNotFoundException If the object's class read from the stream cannot be found.
             * @throws InvalidObjectException If the stream contains no list elements.
             * @throws IOException            If an error occurs reading from the stream.
             */
            private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
                s.defaultReadObject();
                final int size = s.readInt();
                if (size <= 0) {
                    throw new InvalidObjectException("No elements");
                }
                List<T> temp = Nil.instance();
                for (int i = 0; i < size; i++) {
                    @SuppressWarnings("unchecked")
                    final T element = (T) s.readObject();
                    temp = temp.prepend(element);
                }
                list = (Cons<T>) temp.reverse();
            }

            /**
             * <p>
             * {@code readResolve} method for the serialization proxy pattern.
             * </p>
             * Returns a logically equivalent instance of the enclosing class. The presence of this method causes the
             * serialization system to translate the serialization proxy back into an instance of the enclosing class
             * upon deserialization.
             *
             * @return A deserialized instance of the enclosing class.
             */
            private Object readResolve() {
                return list;
            }
        }
    }

    /**
     * Representation of the singleton empty {@code List}.
     *
     * @param <T> Component type of the List.
     * @since 1.1.0
     */
    final class Nil<T> implements List<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private static final Nil<?> INSTANCE = new Nil<>();

        // hidden
        private Nil() {
        }

        /**
         * Returns the singleton instance of the liked list.
         *
         * @param <T> Component type of the List
         * @return the singleton instance of the linked list.
         */
        @SuppressWarnings("unchecked")
        public static <T> Nil<T> instance() {
            return (Nil<T>) INSTANCE;
        }

        @Override
        public Nil<Object> flatten() {
            return Nil.instance();
        }

        @Override
        public boolean hasDefiniteSize() {
            return true;
        }

        @Override
        public T head() {
            throw new NoSuchElementException("head of empty list");
        }

        @Override
        public None<T> headOption() {
            return None.instance();
        }

        @Override
        public List<T> init() {
            throw new UnsupportedOperationException("init of empty list");
        }

        @Override
        public None<List<T>> initOption() {
            return None.instance();
        }

        @Override
        public boolean isTraversableAgain() {
            return true;
        }

        @Override
        public int length() {
            return 0;
        }

        @Override
        public Tuple2<List<T>, List<T>> splitAt(int n) {
            return Tuple.of(empty(), empty());
        }

        @Override
        public Tuple2<List<T>, List<T>> splitAt(Predicate<? super T> predicate) {
            return Tuple.of(empty(), empty());
        }

        @Override
        public Tuple2<List<T>, List<T>> splitAtInclusive(Predicate<? super T> predicate) {
            return Tuple.of(empty(), empty());
        }

        @Override
        public boolean startsWith(Iterable<? extends T> that, int offset) {
            return offset == 0 && !that.iterator().hasNext();
        }

        @Override
        public None<T> peekOption() {
            return None.instance();
        }

        @Override
        public None<List<T>> popOption() {
            return None.instance();
        }

        @Override
        public None<Tuple2<T, List<T>>> pop2Option() {
            return None.instance();
        }

        @Override
        public List<T> removeAt(int indx) {
            throw new IndexOutOfBoundsException("removeAt() on Nil");
        }

        @Override
        public List<T> tail() {
            throw new UnsupportedOperationException("tail of empty list");
        }

        @Override
        public None<List<T>> tailOption() {
            return None.instance();
        }

        @Override
        public boolean isEmpty() {
            return true;
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
            return "List()";
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
