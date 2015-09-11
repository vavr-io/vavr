/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Lazy;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Iterator.AbstractIterator;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * An immutable {@code Stream} is lazy sequence of elements which may be infinitely long.
 * Its immutability makes it suitable for concurrent programming.
 * <p>
 * A {@code Stream} is composed of a {@code head} element and a lazy evaluated {@code tail} {@code Stream}.
 * <p>
 * There are two implementations of the {@code Stream} interface:
 * <ul>
 * <li>{@link Nil}, which represents the empty {@code Stream}.</li>
 * <li>{@link Cons}, which represents a {@code Stream} containing one or more elements.</li>
 * </ul>
 * Methods to obtain a {@code Stream}:
 * <pre>
 * <code>
 * // factory methods
 * Stream.empty()                  // = Stream.of() = Nil.instance()
 * Stream.of(x)                    // = new Cons&lt;&gt;(x, Nil.instance())
 * Stream.of(Object...)            // e.g. Stream.of(1, 2, 3)
 * Stream.ofAll(java.lang.Iterable)          // e.g. Stream.of(List.of(1, 2, 3)) = 1, 2, 3
 * Stream.ofAll(&lt;primitive array&gt;) // e.g. List.ofAll(new int[] {1, 2, 3}) = 1, 2, 3
 *
 * // int sequences
 * Stream.from(0)                  // = 0, 1, 2, 3, ...
 * Stream.range(0, 3)              // = 0, 1, 2
 * Stream.rangeClosed(0, 3)        // = 0, 1, 2, 3
 *
 * // generators
 * Stream.cons(Object, Supplier)   // e.g. Stream.cons(current, () -&gt; next(current));
 * Stream.gen(Supplier)            // e.g. Stream.gen(Math::random);
 * Stream.gen(Object, Function)    // e.g. Stream.gen(1, i -&gt; i * 2);
 * </code>
 * </pre>
 *
 * Factory method applications:
 *
 * <pre>
 * <code>
 * Stream&lt;Integer&gt;       s1 = Stream.of(1);
 * Stream&lt;Integer&gt;       s2 = Stream.of(1, 2, 3);
 *                       // = Stream.of(new Integer[] {1, 2, 3});
 *
 * Stream&lt;int[]&gt;         s3 = Stream.of(new int[] {1, 2, 3});
 * Stream&lt;List&lt;Integer&gt;&gt; s4 = Stream.of(List.of(1, 2, 3));
 *
 * Stream&lt;Integer&gt;       s5 = Stream.ofAll(new int[] {1, 2, 3});
 * Stream&lt;Integer&gt;       s6 = Stream.ofAll(List.of(1, 2, 3));
 *
 * // cuckoo's egg
 * Stream&lt;Integer[]&gt;     s7 = Stream.&lt;Integer[]&gt; of(new Integer[] {1, 2, 3});
 *                       //!= Stream.&lt;Integer[]&gt; of(1, 2, 3);
 * </code>
 * </pre>
 *
 * Example: Generating prime numbers
 *
 * <pre>
 * <code>
 * // = Stream(2L, 3L, 5L, 7L, ...)
 * Stream.gen(2L, PrimeNumbers::nextPrimeFrom)
 *
 * // helpers
 *
 * static long nextPrimeFrom(long num) {
 *     return Stream.from(num + 1).findFirst(PrimeNumbers::isPrime).get();
 * }
 *
 * static boolean isPrime(long num) {
 *     return !Stream.rangeClosed(2L, (long) Math.sqrt(num)).exists(d -&gt; num % d == 0);
 * }
 * </code>
 * </pre>
 *
 * See Okasaki, Chris: <em>Purely Functional Data Structures</em> (p. 34 ff.). Cambridge, 2003.
 *
 * @param <T> component type of this Stream
 * @since 1.1.0
 */
public interface Stream<T> extends LinearSeq<T> {

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.Stream}.
     *
     * @param <T> Component type of the Stream.
     * @return A javaslang.collection.Stream Collector.
     */
    static <T> Collector<T, ArrayList<T>, Stream<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, Stream<T>> finisher = Stream::ofAll;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Returns an infinitely long Stream of int values starting from {@code from}.
     * <p>
     * The {@code Stream} extends to {@code Integer.MIN_VALUE} when passing {@code Integer.MAX_VALUE}.
     *
     * @param value a start int value
     * @return a new Stream of int values starting from {@code from}
     */
    static Stream<Integer> from(int value) {
        return Stream.ofAll(Iterator.from(value));
    }

    /**
     * Returns an infinitely long Stream of long values starting from {@code from}.
     * <p>
     * The {@code Stream} extends to {@code Integer.MIN_VALUE} when passing {@code Long.MAX_VALUE}.
     *
     * @param value a start long value
     * @return a new Stream of long values starting from {@code from}
     */
    static Stream<Long> from(long value) {
        return Stream.ofAll(Iterator.from(value));
    }

    /**
     * Generates an (theoretically) infinitely long Stream using a value Supplier.
     *
     * @param supplier A Supplier of Stream values
     * @param <T>      value type
     * @return A new Stream
     */
    @SuppressWarnings("unchecked")
    static <T> Stream<T> gen(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return Stream.ofAll(Iterator.gen(supplier));
    }

    /**
     * Generates a (theoretically) infinitely long Stream using a function to calculate the next value
     * based on the previous.
     *
     * @param seed The first value in the Stream
     * @param f    A function to calculate the next value based on the previous
     * @param <T>  value type
     * @return A new Stream
     */
    static <T> Stream<T> gen(T seed, Function<? super T, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        return Stream.ofAll(Iterator.gen(seed, f));
    }

    /**
     * Constructs a Stream of a head element and a tail supplier.
     *
     * @param head         The head element of the Stream
     * @param tailSupplier A supplier of the tail values. To end the stream, return {@link Stream#empty}.
     * @param <T>          value type
     * @return A new Stream
     */
    static <T> Stream<T> cons(T head, Supplier<? extends Stream<T>> tailSupplier) {
        Objects.requireNonNull(tailSupplier, "tailSupplier is null");
        return new Cons<>(head, tailSupplier);
    }

    /**
     * Returns the single instance of Nil. Convenience method for {@code Nil.instance()}.
     * <p>
     * Note: this method intentionally returns type {@code Stream} and not {@code Nil}. This comes handy when folding.
     * If you explicitly need type {@code Nil} use {@linkplain Nil#instance()}.
     *
     * @param <T> Component type of Nil, determined by type inference in the particular context.
     * @return The empty list.
     */
    static <T> Stream<T> empty() {
        return Nil.instance();
    }

    /**
     * Returns a singleton {@code Stream}, i.e. a {@code Stream} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new Stream instance containing the given element
     */
    static <T> Stream<T> of(T element) {
        return new Cons<>(element, Nil::instance);
    }

    /**
     * <p>
     * Creates a Stream of the given elements.
     * </p>
     * <pre>
     * <code>  Stream.of(1, 2, 3, 4)
     * = Nil.instance().prepend(4).prepend(3).prepend(2).prepend(1)
     * = new Cons(1, new Cons(2, new Cons(3, new Cons(4, Nil.instance()))))</code>
     * </pre>
     *
     * @param <T>      Component type of the Stream.
     * @param elements Zero or more elements.
     * @return A list containing the given elements in the same order.
     */
    @SafeVarargs
    static <T> Stream<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return Stream.ofAll(() -> new Iterator<T>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < elements.length;
            }

            @Override
            public T next() {
                return elements[i++];
            }
        });
    }

    /**
     * Creates a Stream of the given elements.
     *
     * @param <T>      Component type of the Stream.
     * @param elements An java.lang.Iterable of elements.
     * @return A list containing the given elements in the same order.
     */
    @SuppressWarnings("unchecked")
    static <T> Stream<T> ofAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof Stream) {
            return (Stream<T>) elements;
        } else {
            class StreamFactory {
                <T> Stream<T> create(java.util.Iterator<? extends T> iterator) {
                    if (iterator.hasNext()) {
                        // we need to get the head, otherwise a tail call would get the head instead
                        final T head = iterator.next();
                        return new Cons<>(head, () -> create(iterator));
                    } else {
                        return Nil.instance();
                    }
                }
            }
            return new StreamFactory().create(elements.iterator());
        }
    }

    /**
     * Creates a Stream based on the elements of a boolean array.
     *
     * @param array a boolean array
     * @return A new Stream of Boolean values
     */
    static Stream<Boolean> ofAll(boolean[] array) {
        Objects.requireNonNull(array, "array is null");
        return Stream.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Stream based on the elements of a byte array.
     *
     * @param array a byte array
     * @return A new Stream of Byte values
     */
    static Stream<Byte> ofAll(byte[] array) {
        Objects.requireNonNull(array, "array is null");
        return Stream.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Stream based on the elements of a char array.
     *
     * @param array a char array
     * @return A new Stream of Character values
     */
    static Stream<Character> ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return Stream.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Stream based on the elements of a double array.
     *
     * @param array a double array
     * @return A new Stream of Double values
     */
    static Stream<Double> ofAll(double[] array) {
        Objects.requireNonNull(array, "array is null");
        return Stream.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Stream based on the elements of a float array.
     *
     * @param array a float array
     * @return A new Stream of Float values
     */
    static Stream<Float> ofAll(float[] array) {
        Objects.requireNonNull(array, "array is null");
        return Stream.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Stream based on the elements of an int array.
     *
     * @param array an int array
     * @return A new Stream of Integer values
     */
    static Stream<Integer> ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return Stream.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Stream based on the elements of a long array.
     *
     * @param array a long array
     * @return A new Stream of Long values
     */
    static Stream<Long> ofAll(long[] array) {
        Objects.requireNonNull(array, "array is null");
        return Stream.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Stream based on the elements of a short array.
     *
     * @param array a short array
     * @return A new Stream of Short values
     */
    static Stream<Short> ofAll(short[] array) {
        Objects.requireNonNull(array, "array is null");
        return Stream.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a Stream of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stream.range(0, 0)  // = Stream()
     * Stream.range(2, 0)  // = Stream()
     * Stream.range(-2, 2) // = Stream(-2, -1, 0, 1)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or {@code Nil} if {@code from >= toExclusive}
     */
    static Stream<Integer> range(int from, int toExclusive) {
        return Stream.ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a Stream of int numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stream.rangeBy(1, 3, 1)  // = Stream(1, 2)
     * Stream.rangeBy(1, 4, 2)  // = Stream(1, 3)
     * Stream.rangeBy(4, 1, -2) // = Stream(4, 2)
     * Stream.rangeBy(4, 1, 2)  // = Stream()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or {@code Nil} if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Stream<Integer> rangeBy(int from, int toExclusive, int step) {
        return Stream.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a Stream of long numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stream.range(0L, 0L)  // = Stream()
     * Stream.range(2L, 0L)  // = Stream()
     * Stream.range(-2L, 2L) // = Stream(-2L, -1L, 0L, 1L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of long values as specified or {@code Nil} if {@code from >= toExclusive}
     */
    static Stream<Long> range(long from, long toExclusive) {
        return Stream.ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a Stream of long numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stream.rangeBy(1L, 3L, 1L)  // = Stream(1L, 2L)
     * Stream.rangeBy(1L, 4L, 2L)  // = Stream(1L, 3L)
     * Stream.rangeBy(4L, 1L, -2L) // = Stream(4L, 2L)
     * Stream.rangeBy(4L, 1L, 2L)  // = Stream()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or {@code Nil} if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Stream<Long> rangeBy(long from, long toExclusive, long step) {
        return Stream.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a Stream of int numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stream.rangeClosed(0, 0)  // = Stream(0)
     * Stream.rangeClosed(2, 0)  // = Stream()
     * Stream.rangeClosed(-2, 2) // = Stream(-2, -1, 0, 1, 2)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or {@code Nil} if {@code from > toInclusive}
     */
    static Stream<Integer> rangeClosed(int from, int toInclusive) {
        return Stream.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a Stream of int numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stream.rangeClosedBy(1, 3, 1)  // = Stream(1, 2, 3)
     * Stream.rangeClosedBy(1, 4, 2)  // = Stream(1, 3)
     * Stream.rangeClosedBy(4, 1, -2) // = Stream(4, 2)
     * Stream.rangeClosedBy(4, 1, 2)  // = Stream()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or {@code Nil} if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Stream<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return Stream.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a Stream of long numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stream.rangeClosed(0L, 0L)  // = Stream(0L)
     * Stream.rangeClosed(2L, 0L)  // = Stream()
     * Stream.rangeClosed(-2L, 2L) // = Stream(-2L, -1L, 0L, 1L, 2L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of long values as specified or {@code Nil} if {@code from > toInclusive}
     */
    static Stream<Long> rangeClosed(long from, long toInclusive) {
        return Stream.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a Stream of long numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stream.rangeClosedBy(1L, 3L, 1L)  // = Stream(1L, 2L, 3L)
     * Stream.rangeClosedBy(1L, 4L, 2L)  // = Stream(1L, 3L)
     * Stream.rangeClosedBy(4L, 1L, -2L) // = Stream(4L, 2L)
     * Stream.rangeClosedBy(4L, 1L, 2L)  // = Stream()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or {@code Nil} if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static Stream<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return Stream.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    default Stream<T> append(T element) {
        return isEmpty() ? Stream.of(element) : new Cons<>(((Cons<T>) this).head, () -> tail().append(element));
    }

    @Override
    default Stream<T> appendAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return isEmpty() ? Stream.ofAll(elements) : new Cons<>(((Cons<T>) this).head, () -> tail().appendAll(elements));
    }

    /**
     * Appends itself to the end of stream with {@code mapper} function.
     * <p>
     * <strong>Example:</strong>
     * <p>
     * Well known scala code for Fibonacci infinite sequence
     * <pre>
     * <code>
     * val fibs:Stream[Int] = 0 #:: 1 #:: (fibs zip fibs.tail).map{ t =&gt; t._1 + t._2 }
     * </code>
     * </pre>
     * can be transformed to
     * <pre>
     * <code>
     * Stream.of(0, 1).appendSelf(self -&gt; self.zip(self.tail()).map(t -&gt; t._1 + t._2));
     * </code>
     * </pre>
     *
     * @param mapper an mapper
     * @return a new Stream
     */
    default Stream<T> appendSelf(Function<? super Stream<T>, ? extends Stream<T>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return isEmpty() ? this : new AppendSelf<>((Cons<T>) this, mapper).stream();
    }

    @Override
    default Stream<Tuple2<T, T>> crossProduct() {
        return crossProduct(this);
    }

    @Override
    default <U> Stream<Tuple2<T, U>> crossProduct(java.lang.Iterable<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        final Stream<? extends U> other = Stream.ofAll(that);
        return flatMap(a -> other.map(b -> Tuple.of(a, b)));
    }

    @Override
    default Stream<T> clear() {
        return Nil.instance();
    }

    @Override
    default Stream<Stream<T>> combinations() {
        return Stream.rangeClosed(0, length()).map(this::combinations).flatMap(Function.identity());
    }

    @Override
    default Stream<Stream<T>> combinations(int k) {
        // TODO(FIXME): move to auxilliary class Combinations
        class Recursion {
            Stream<Stream<T>> combinations(Stream<T> elements, int k) {
                return (k == 0) ? Stream.of(Stream.empty()) :
                        elements.zipWithIndex().flatMap(t ->
                                combinations(elements.drop(t._2 + 1), (k - 1))
                                        .map((Stream<T> c) -> c.prepend(t._1)));
            }
        }
        return new Recursion().combinations(this, Math.max(k, 0));
    }

    @Override
    default Stream<T> distinct() {
        return distinctBy(Function.identity());
    }

    @Override
    default Stream<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        final java.util.Set<T> seen = new java.util.TreeSet<>(comparator);
        return filter(seen::add);
    }

    @Override
    default <U> Stream<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        final java.util.Set<U> seen = new java.util.HashSet<>();
        return filter(t -> seen.add(keyExtractor.apply(t)));
    }

    @Override
    default Stream<T> drop(int n) {
        Stream<T> stream = this;
        while (n-- > 0 && !stream.isEmpty()) {
            stream = stream.tail();
        }
        return stream;
    }

    @Override
    default Stream<T> dropRight(int n) {
        // TODO(FIXME): inner class leaks reference of outer class to the outside
        // works with infinite streams by buffering elements
        class Util {
            Stream<T> dropRight(List<T> front, List<T> rear, Stream<T> remaining) {
                if (remaining.isEmpty()) {
                    return remaining;
                } else if (front.isEmpty()) {
                    return dropRight(rear.reverse(), List.empty(), remaining);
                } else {
                    return new Cons<>(front.head(), () -> dropRight(front.tail(), rear.prepend(remaining.head()), remaining.tail()));
                }
            }
        }

        if (n <= 0) {
            return this;
        } else {
            return new Util().dropRight(take(n).toList(), List.empty(), drop(n));
        }
    }

    @Override
    default Stream<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        Stream<T> stream = this;
        while (!stream.isEmpty() && predicate.test(stream.head())) {
            stream = stream.tail();
        }
        return stream;
    }

    @Override
    default Stream<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        Stream<T> stream = this;
        while (!stream.isEmpty() && !predicate.test(stream.head())) {
            stream = stream.tail();
        }
        final Stream<T> finalStream = stream;
        return stream.isEmpty() ? stream : new Cons<>(stream.head(), () -> finalStream.tail().filter(predicate));
    }

    @Override
    default <U> Stream<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return isEmpty() ? Nil.instance() : Stream.ofAll(() -> new Iterator<U>() {

            final Iterator<? extends T> inputs = Stream.this.iterator();
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
        });
    }

    @Override
    default Stream<Object> flatten() {
        if (isEmpty()) {
            return empty();
        } else {
            return flatMap(t -> {
                if (t instanceof java.lang.Iterable) {
                    return Stream.ofAll((java.lang.Iterable<?>) t).flatten();
                } else {
                    return Stream.of(t);
                }
            });
        }
    }

    @Override
    default T get(int index) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("get(" + index + ") on Nil");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("get(" + index + ")");
        }
        Stream<T> stream = this;
        for (int i = index - 1; i >= 0; i--) {
            stream = stream.tail();
            if (stream.isEmpty()) {
                throw new IndexOutOfBoundsException(String.format("get(%s) on Stream of size %s", index, index - i));
            }
        }
        return stream.head();
    }

    @Override
    default <C> Map<C, Stream<T>> groupBy(Function<? super T, ? extends C> classifier) {
        Objects.requireNonNull(classifier, "classifier is null");
        return iterator().groupBy(classifier).map((c, it) -> Map.Entry.of(c, Stream.ofAll(it)));
    }

    @Override
    default boolean hasDefiniteSize() {
        return false;
    }

    @Override
    default Option<T> headOption() {
        return isEmpty() ? None.instance() : new Some<>(head());
    }

    @Override
    default int indexOf(T element, int from) {
        int index = 0;
        for (Stream<T> stream = this; !stream.isEmpty(); stream = stream.tail(), index++) {
            if (index >= from && Objects.equals(stream.head(), element)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    default Stream<T> init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init of empty stream");
        } else {
            final Stream<T> tail = tail();
            if (tail.isEmpty()) {
                return Nil.instance();
            } else {
                return new Cons<>(((Cons<T>) this).head, tail::init);
            }
        }
    }

    @Override
    default Option<Stream<T>> initOption() {
        return isEmpty() ? None.instance() : new Some<>(init());
    }

    @Override
    default Stream<T> insert(int index, T element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        } else if (index == 0) {
            return new Cons<>(element, () -> this);
        } else if (isEmpty()) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e) on Nil");
        } else {
            return new Cons<>(((Cons<T>) this).head, () -> tail().insert(index - 1, element));
        }
    }

    @Override
    default Stream<T> insertAll(int index, java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (index < 0) {
            throw new IndexOutOfBoundsException("insertAll(" + index + ", elements)");
        } else if (index == 0) {
            return isEmpty() ? Stream.ofAll(elements) : Stream.ofAll(elements).appendAll(this);
        } else if (isEmpty()) {
            throw new IndexOutOfBoundsException("insertAll(" + index + ", elements) on Nil");
        } else {
            return new Cons<>(((Cons<T>) this).head, () -> tail().insertAll(index - 1, elements));
        }
    }

    @Override
    default Stream<T> intersperse(T element) {
        if (isEmpty()) {
            return this;
        } else {
            return new Cons<>(((Cons<T>) this).head, () -> {
                final Stream<T> tail = tail();
                return tail.isEmpty() ? tail : new Cons<>(element, () -> tail.intersperse(element));
            });
        }
    }

    @Override
    default boolean isTraversableAgain() {
        return true;
    }

    @Override
    default Iterator<T> iterator() {
        return new StreamIterator<>(this);
    }

    @Override
    default int lastIndexOf(T element, int end) {
        int result = -1, index = 0;
        for (Stream<T> stream = this; index <= end && !stream.isEmpty(); stream = stream.tail(), index++) {
            if (Objects.equals(stream.head(), element)) {
                result = index;
            }
        }
        return result;
    }

    @Override
    default int length() {
        return foldLeft(0, (n, ignored) -> n + 1);
    }

    @Override
    default <U> Stream<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return Nil.instance();
        } else {
            return new Cons<>(mapper.apply(head()), () -> tail().map(mapper));
        }
    }

    @Override
    default Stream<T> padTo(int length, T element) {
        if (length <= 0) {
            return this;
        } else if (isEmpty()) {
            return Stream.ofAll(Iterator.constant(element).take(length));
        } else {
            return new Cons<>(((Cons<T>) this).head, () -> tail().padTo(length - 1, element));
        }
    }

    @Override
    default Stream<T> patch(int from, java.lang.Iterable<? extends T> that, int replaced) {
        from = from < 0 ? 0 : from;
        replaced = replaced < 0 ? 0 : replaced;
        Stream<T> result = take(from).appendAll(that);
        from += replaced;
        result = result.appendAll(drop(from));
        return result;
    }


    @Override
    default Tuple2<Stream<T>, Stream<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return Tuple.of(filter(predicate), filter(predicate.negate()));
    }

    @Override
    default Stream<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (isEmpty()) {
            return this;
        } else {
            final T head = head();
            action.accept(head);
            return new Cons<>(head, () -> tail().peek(action));
        }
    }

    @Override
    default Stream<Stream<T>> permutations() {
        if (isEmpty()) {
            return Nil.instance();
        } else {
            final Stream<T> tail = tail();
            if (tail.isEmpty()) {
                return Stream.of(this);
            } else {
                final Stream<Stream<T>> zero = Nil.instance();
                // TODO: IntelliJ IDEA 14.1.3 needs a redundant cast here, jdk 1.8.0_40 compiles fine
                return distinct().foldLeft(zero, (xs, x) -> xs.appendAll(remove(x).permutations().map((Function<Stream<T>, Stream<T>>) l -> l.prepend(x))));
            }
        }
    }

    @Override
    default Stream<T> prepend(T element) {
        return new Cons<>(element, () -> this);
    }

    @Override
    default Stream<T> prependAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return Stream.ofAll(elements).appendAll(this);
    }

    @Override
    default Stream<T> remove(T element) {
        if (isEmpty()) {
            return this;
        } else {
            final T head = head();
            return Objects.equals(head, element) ? tail() : new Cons<>(head, () -> tail().remove(element));
        }
    }

    @Override
    default Stream<T> removeFirst(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return this;
        } else {
            final T head = head();
            return predicate.test(head) ? tail() : new Cons<>(head, () -> tail().removeFirst(predicate));
        }
    }

    @Override
    default Stream<T> removeLast(Predicate<T> predicate) {
        return isEmpty() ? this : reverse().removeFirst(predicate).reverse();
    }

    @Override
    default Stream<T> removeAt(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("removeAt(" + index + ")");
        } else if (index == 0) {
            return tail();
        } else if (isEmpty()) {
            throw new IndexOutOfBoundsException("removeAt() on Nil");
        } else {
            return new Cons<>(((Cons<T>) this).head, () -> tail().removeAt(index - 1));
        }
    }

    @Override
    default Stream<T> removeAll(T removed) {
        return filter(e -> !Objects.equals(e, removed));
    }

    @Override
    default Stream<T> removeAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final Stream<T> distinct = Stream.ofAll(elements).distinct();
        return filter(e -> !distinct.contains(e));
    }

    @Override
    default Stream<T> replace(T currentElement, T newElement) {
        if (isEmpty()) {
            return this;
        } else {
            final T head = head();
            if (Objects.equals(head, currentElement)) {
                return new Cons<>(newElement, this::tail);
            } else {
                return new Cons<>(head, () -> tail().replace(currentElement, newElement));
            }
        }
    }

    @Override
    default Stream<T> replaceAll(T currentElement, T newElement) {
        if (isEmpty()) {
            return this;
        } else {
            final T head = head();
            final T newHead = Objects.equals(head, currentElement) ? newElement : head;
            return new Cons<>(newHead, () -> tail().replaceAll(currentElement, newElement));
        }
    }

    @Override
    default Stream<T> replaceAll(UnaryOperator<T> operator) {
        if (isEmpty()) {
            return this;
        } else {
            return new Cons<>(operator.apply(head()), () -> tail().replaceAll(operator));
        }
    }

    @Override
    default Stream<T> retainAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (isEmpty()) {
            return this;
        } else {
            final Stream<T> retained = Stream.ofAll(elements).distinct();
            return filter(retained::contains);
        }
    }

    @Override
    default Stream<T> reverse() {
        return isEmpty() ? this : foldLeft(Stream.empty(), Stream::prepend);
    }

    @Override
    default Stream<T> slice(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("subsequence(" + beginIndex + ")");
        }
        Stream<T> result = this;
        for (int i = 0; i < beginIndex; i++, result = result.tail()) {
            if (result.isEmpty()) {
                throw new IndexOutOfBoundsException(String.format("subsequence(%s) on Stream of size %s", beginIndex, i));
            }
        }
        return result;
    }

    @Override
    default Stream<T> slice(int beginIndex, int endIndex) {
        if (beginIndex < 0 || beginIndex > endIndex) {
            throw new IndexOutOfBoundsException(String.format("subsequence(%s, %s)", beginIndex, endIndex));
        }
        if (beginIndex == endIndex) {
            return Nil.instance();
        } else if (isEmpty()) {
            throw new IndexOutOfBoundsException("subsequence of Nil");
        } else if (beginIndex == 0) {
            return new Cons<>(head(), () -> tail().slice(0, endIndex - 1));
        } else {
            return tail().slice(beginIndex - 1, endIndex - 1);
        }
    }

    @Override
    default Stream<T> sort() {
        return isEmpty() ? this : toJavaStream().sorted().collect(Stream.collector());
    }

    @Override
    default Stream<T> sort(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return isEmpty() ? this : toJavaStream().sorted(comparator).collect(Stream.collector());
    }

    @Override
    default Tuple2<Stream<T>, Stream<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return Tuple.of(takeWhile(predicate), dropWhile(predicate));
    }

    @Override
    default Tuple2<Stream<T>, Stream<T>> splitAt(int n) {
        return Tuple.of(take(n), drop(n));
    }

    @Override
    default Tuple2<Stream<T>, Stream<T>> splitAt(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return Tuple.of(takeWhile(predicate.negate()), dropWhile(predicate.negate()));
    }

    @Override
    default Tuple2<Stream<T>, Stream<T>> splitAtInclusive(Predicate<? super T> predicate) {
        final Tuple2<Stream<T>, Stream<T>> split = splitAt(predicate);
        if (split._2.isEmpty()) {
            return split;
        } else {
            return Tuple.of(split._1.append(split._2.head()), split._2.tail());
        }
    }

    @Override
    default Spliterator<T> spliterator() {
        // the focus of the Stream API is on random-access collections of *known size*
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    Stream<T> tail();

    @Override
    default Option<Stream<T>> tailOption() {
        return isEmpty() ? None.instance() : new Some<>(tail());
    }

    @Override
    default Stream<T> take(int n) {
        if (n < 1 || isEmpty()) {
            return Nil.instance();
        } else {
            return new Cons<>(head(), () -> tail().take(n - 1));
        }
    }

    @Override
    default Stream<T> takeRight(int n) {
        Stream<T> right = this;
        Stream<T> remaining = drop(n);
        while (!remaining.isEmpty()) {
            right = right.tail();
            remaining = remaining.tail();
        }
        return right;
    }

    @Override
    default Stream<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return Nil.instance();
        } else {
            final T head = head();
            if (predicate.test(head)) {
                return new Cons<>(head, () -> tail().takeWhile(predicate));
            } else {
                return Nil.instance();
            }
        }
    }

    @Override
    default <U> Stream<U> unit(java.lang.Iterable<? extends U> iterable) {
        return Stream.ofAll(iterable);
    }

    @Override
    default <T1, T2> Tuple2<Stream<T1>, Stream<T2>> unzip(Function<? super T, Tuple2<? extends
            T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        final Stream<Tuple2<? extends T1, ? extends T2>> stream = map(unzipper);
        final Stream<T1> stream1 = stream.map(t -> t._1);
        final Stream<T2> stream2 = stream.map(t -> t._2);
        return Tuple.of(stream1, stream2);
    }

    @Override
    default Stream<T> update(int index, T element) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("update(" + index + ", e) on Nil");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("update(" + index + ", e)");
        }
        Stream<T> preceding = Nil.instance();
        Stream<T> tail = this;
        for (int i = index; i > 0; i--, tail = tail.tail()) {
            if (tail.isEmpty()) {
                throw new IndexOutOfBoundsException("update at " + index);
            }
            preceding = preceding.prepend(tail.head());
        }
        if (tail.isEmpty()) {
            throw new IndexOutOfBoundsException("update at " + index);
        }
        // skip the current head element because it is replaced
        return preceding.reverse().appendAll(tail.tail().prepend(element));
    }

    @Override
    default <U> Stream<Tuple2<T, U>> zip(java.lang.Iterable<U> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        return Stream.ofAll(iterator().zip(iterable));
    }

    @Override
    default <U> Stream<Tuple2<T, U>> zipAll(java.lang.Iterable<U> iterable, T thisElem, U thatElem) {
        Objects.requireNonNull(iterable, "iterable is null");
        return Stream.ofAll(iterator().zipAll(iterable, thisElem, thatElem));
    }

    @Override
    default Stream<Tuple2<T, Integer>> zipWithIndex() {
        return Stream.ofAll(iterator().zipWithIndex());
    }
}

/**
 * The empty Stream.
 * <p>
 * This is a singleton, i.e. not Cloneable.
 *
 * @param <T> Component type of the Stream.
 * @since 1.1.0
 */
final class Nil<T> implements Stream<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Nil<?> INSTANCE = new Nil<>();

    // hidden
    private Nil() {
    }

    /**
     * Returns the singleton empty Stream instance.
     *
     * @param <T> Component type of the Stream
     * @return The empty Stream
     */
    @SuppressWarnings("unchecked")
    public static <T> Nil<T> instance() {
        return (Nil<T>) INSTANCE;
    }

    @Override
    public T head() {
        throw new NoSuchElementException("head of empty stream");
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Stream<T> tail() {
        throw new UnsupportedOperationException("tail of empty stream");
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
        return "Stream()";
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

/**
 * Non-empty {@code Stream}, consisting of a {@code head}, a {@code tail} and an optional
 * {@link java.lang.AutoCloseable}.
 *
 * @param <T> Component type of the Stream.
 * @since 1.1.0
 */
// DEV NOTE: class declared final because of serialization proxy pattern.
// (see Effective Java, 2nd ed., p. 315)
final class Cons<T> implements Stream<T>, Serializable {

    private static final long serialVersionUID = 1L;

    final T head;
    final Lazy<Stream<T>> tail;

    /**
     * Creates a new {@code Stream} consisting of a head element and a lazy trailing {@code Stream}.
     *
     * @param head A head element
     * @param tail A tail {@code Stream} supplier, {@linkplain Nil} denotes the end of the {@code Stream}
     */
    public Cons(T head, Supplier<? extends Stream<T>> tail) {
        this.head = head;
        this.tail = Lazy.of(Objects.requireNonNull(tail, "tail is null"));
    }

    @Override
    public T head() {
        return head;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Stream<T> tail() {
        return tail.get();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Stream) {
            Stream<?> stream1 = this;
            Stream<?> stream2 = (Stream<?>) o;
            while (!stream1.isEmpty() && !stream2.isEmpty()) {
                final boolean isEqual = Objects.equals(stream1.head(), stream2.head());
                if (!isEqual) {
                    return false;
                }
                stream1 = stream1.tail();
                stream2 = stream2.tail();
            }
            return stream1.isEmpty() && stream2.isEmpty();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Traversable.hash(this);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("Stream(");
        Stream<T> stream = this;
        while (stream != null && !stream.isEmpty()) {
            final Cons<T> cons = (Cons<T>) stream;
            builder.append(cons.head);
            if (cons.tail.isEvaluated()) {
                stream = cons.tail.get();
                if (!stream.isEmpty()) {
                    builder.append(", ");
                }
            } else {
                builder.append(", ?");
                stream = null;
            }
        }
        return builder.append(")").toString();
    }

    /**
     * <p>
     * {@code writeReplace} method for the serialization proxy pattern.
     * </p>
     * The presence of this method causes the serialization system to emit a SerializationProxy instance instead of
     * an instance of the enclosing class.
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
     * A serialization proxy which, in this context, is used to deserialize immutable, linked Streams with final
     * instance fields.
     *
     * @param <T> The component type of the underlying stream.
     */
    // DEV NOTE: The serialization proxy pattern is not compatible with non-final, i.e. extendable,
    // classes. Also, it may not be compatible with circular object graphs.
    private static final class SerializationProxy<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        // the instance to be serialized/deserialized
        private transient Cons<T> stream;

        /**
         * <p>
         * Constructor for the case of serialization, called by {@link Cons#writeReplace()}.
         * </p>
         * The constructor of a SerializationProxy takes an argument that concisely represents the logical state of
         * an instance of the enclosing class.
         *
         * @param stream a Cons
         */
        SerializationProxy(Cons<T> stream) {
            this.stream = stream;
        }

        /**
         * Write an object to a serialization stream.
         *
         * @param s An object serialization stream.
         * @throws java.io.IOException If an error occurs writing to the stream.
         */
        private void writeObject(ObjectOutputStream s) throws IOException {
            s.defaultWriteObject();
            s.writeInt(stream.length());
            for (Stream<T> l = stream; !l.isEmpty(); l = l.tail()) {
                s.writeObject(l.head());
            }
        }

        /**
         * Read an object from a deserialization stream.
         *
         * @param s An object deserialization stream.
         * @throws ClassNotFoundException If the object's class read from the stream cannot be found.
         * @throws InvalidObjectException If the stream contains no stream elements.
         * @throws IOException            If an error occurs reading from the stream.
         */
        @SuppressWarnings("ConstantConditions")
        private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
            s.defaultReadObject();
            final int size = s.readInt();
            if (size <= 0) {
                throw new InvalidObjectException("No elements");
            }
            Stream<T> temp = Nil.instance();
            for (int i = 0; i < size; i++) {
                @SuppressWarnings("unchecked")
                final T element = (T) s.readObject();
                temp = temp.append(element);
            }
            // DEV-NOTE: Cons is deserialized
            stream = (Cons<T>) temp;
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
            return stream;
        }
    }
}

final class AppendSelf<T> {

    private final Cons<T> self;

    AppendSelf(Cons<T> self, Function<? super Stream<T>, ? extends Stream<T>> mapper) {
        this.self = appendAll(self, mapper);
    }

    private Cons<T> appendAll(Cons<T> stream, Function<? super Stream<T>, ? extends Stream<T>> mapper) {
        return new Cons<>(stream.head, () -> {
            final Stream<T> tail = stream.tail();
            return tail.isEmpty() ? mapper.apply(self) : appendAll((Cons<T>) tail, mapper);
        });
    }

    Cons<T> stream() {
        return self;
    }
}

final class StreamIterator<T> extends AbstractIterator<T> {

    Lazy<Stream<T>> stream;

    StreamIterator(Stream<T> stream) {
        this.stream = Lazy.of(() -> stream);
    }

    @Override
    public boolean hasNext() {
        return stream.get().isDefined();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            Iterator.empty().next();
        }
        final Stream<T> current = stream.get();
        stream = Lazy.of(current::tail);
        return current.head();
    }
}