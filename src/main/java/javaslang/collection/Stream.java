/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

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
 * A {@code Stream} is lazy sequence of elements which may be infinitely long. Its immutability makes it suitable for
 * concurrent programming.
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
 * Stream.nil()              // = Stream.of() = Nil.instance()
 * Stream.of(x)              // = new Cons&lt;&gt;(x, Nil.instance())
 * Stream.of(Object...)      // e.g. Stream.of(1, 2, 3)
 * Stream.of(Iterable)       // e.g. Stream.of(List.of(1, 2, 3)) = 1, 2, 3
 * Stream.of(Iterator)       // e.g. Stream.of(Arrays.asList(1, 2, 3).iterator()) = 1, 2, 3
 *
 * // int sequences
 * Stream.from(0)            // = 0, 1, 2, 3, ...
 * Stream.range(0, 3)        // = 0, 1, 2
 * Stream.rangeClosed(0, 3)  // = 0, 1, 2, 3
 *
 * // generators
 * Stream.gen(Supplier)          // e.g. Stream.gen(Math::random);
 * Stream.gen(Object, Function)  // e.g. Stream.gen(1, i -&gt; i * 2);
 * Stream.gen(Object, Supplier)  // e.g. Stream.gen(current, () -&gt; next(current));
 * </code>
 * </pre>
 *
 * See Okasaki, Chris: <em>Purely Functional Data Structures</em> (p. 34 ff.). Cambridge, 2003.
 *
 * @param <T> component type of this Stream
 * @since 1.1.0
 */
public interface Stream<T> extends Seq<T> {

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
        return new Cons<>(() -> value, () -> from(value + 1));
    }

    /**
     * Generates an (theoretically) infinitely long Stream using a value Supplier.
     *
     * @param supplier A Supplier of Stream values
     * @param <T>      value type
     * @return A new Stream
     */
    static <T> Stream<T> gen(Supplier<T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return new Cons<>(supplier, () -> gen(supplier));
    }

    /**
     * Generates an (theoretically) infinitely long Stream using a function to calculate the next value
     * based on the previous.
     *
     * @param seed The first value in the Stream
     * @param f    A function to calculate the next value based on the previous
     * @param <T>  value type
     * @return A new Stream
     */
    static <T> Stream<T> gen(T seed, Function<T, T> f) {
        Objects.requireNonNull(f, "f is null");
        return new Stream.Cons<>(() -> seed, () -> gen(f.apply(seed), f));
    }

    /**
     * Generates an (theoretically) infinitely long Stream using a function to calculate the tail of the stream
     * based on the head.
     *
     * @param head         The first value in the Stream
     * @param tailSupplier A function to calculate the tail values. To end the stream, return {@link Stream#nil}.
     * @param <T>          value type
     * @return A new Stream
     */
    static <T> Stream<T> gen(T head, Supplier<Stream<T>> tailSupplier) {
        Objects.requireNonNull(tailSupplier, "tailSupplier is null");
        return new Stream.Cons<>(() -> head, tailSupplier);
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
    static <T> Stream<T> nil() {
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
        return new Cons<>(() -> element, Nil::instance);
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
        return Stream.ofAll(new Iterator<T>() {
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
     * @param elements An Iterable of elements.
     * @return A list containing the given elements in the same order.
     */
    @SuppressWarnings("unchecked")
    static <T> Stream<T> ofAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof Stream) {
            return (Stream<T>) elements;
        } else {
            return Stream.ofAll(elements.iterator());
        }
    }

    /**
     * Creates a Stream based on an Iterator.
     *
     * @param iterator An Iterator
     * @param <T>      Component type
     * @return A new Stream
     */
    // providing this method to save resources creating a Stream - makes no sense for collections in general
    static <T> Stream<T> ofAll(Iterator<? extends T> iterator) {
        Objects.requireNonNull(iterator, "iterator is null");
        if (iterator.hasNext()) {
            // we need to get the head, otherwise a tail call would get the head instead
            final T head = iterator.next();
            return new Cons<>(() -> head, () -> Stream.ofAll(iterator));
        } else {
            return Nil.instance();
        }
    }

    /**
     * Creates a Stream of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or {@code Nil} if {@code from >= toExclusive}
     */
    static Stream<Integer> range(int from, int toExclusive) {
        if (from >= toExclusive) {
            return Nil.instance();
        } else {
            return Stream.rangeClosed(from, toExclusive - 1);
        }
    }

    /**
     * Creates a Stream of int numbers starting from {@code from}, extending to {@code toInclusive}.
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or {@code Nil} if {@code from > toInclusive}
     */
    static Stream<Integer> rangeClosed(int from, int toInclusive) {
        if (from > toInclusive) {
            return Nil.instance();
        } else if (from == Integer.MAX_VALUE) {
            return Stream.of(Integer.MAX_VALUE);
        } else {
            return new Cons<>(() -> from, () -> rangeClosed(from + 1, toInclusive));
        }
    }

    @Override
    Stream<T> append(T element);

    @Override
    Stream<T> appendAll(Iterable<? extends T> elements);

    @Override
    default Stream<T> clear() {
        return Nil.instance();
    }

    @Override
    default Stream<Stream<T>> combinations() {
        return Stream.rangeClosed(0, length()).map(this::combinations).flatten(Function.identity());
    }

    @Override
    default Stream<Stream<T>> combinations(int k) {
        class Recursion {
            Stream<Stream<T>> combinations(Stream<T> elements, int k) {
                return (k == 0) ? Stream.of(Stream.nil()) :
                        elements.zipWithIndex().flatMap(t ->
                                combinations(elements.drop(t._2 + 1), (k - 1))
                                        .map((Stream<T> c) -> c.prepend(t._1)));
            }
        }
        return new Recursion().combinations(this, Math.max(k, 0));
    }

    @Override
    default Stream<T> distinct() {
        return distinct(Function.identity());
    }

    @Override
    default <U> Stream<T> distinct(Function<? super T, ? extends U> keyExtractor) {
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
        return reverse().drop(n).reverse();
    }

    @Override
    default Stream<T> dropWhile(Predicate<? super T> predicate) {
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
        return stream.isEmpty() ? stream : new Cons<>(stream::head, () -> finalStream.tail().filter(predicate));
    }

    @Override
    default Stream<T> findAll(Predicate<? super T> predicate) {
        return filter(predicate);
    }

    @Override
    default <U> Stream<U> flatMap(Function<? super T, ? extends Iterable<U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return isEmpty() ? Nil.instance() : map(mapper).flatten(Function.identity());
    }

    /**
     * Flattens a {@code Stream} using a function. A common use case is to use the identity
     * {@code stream.flatten(Function::identity)} to flatten a {@code Stream} of {@code Stream}s.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Match&lt;Stream&lt;U&gt;&gt; f = Match
     *    .when((Stream&lt;U&gt; l) -&gt; l)
     *    .when((U u) -&gt; Stream.of(u));
     * Stream.of(1).flatten(f);              // = Stream(1)
     * Stream.of(Stream.of(1)).flatten(f);   // = Stream(1)
     * Stream.of(Nil.instance()).flatten(f); // = Nil
     * Nil.instance().flatten(f);            // = Nil
     * </code>
     * </pre>
     *
     * @param <U> component type of the result {@code Stream}
     * @param f   a function which maps elements of this {@code Stream} to {@code Stream}s
     * @return a new {@code Stream}
     * @throws NullPointerException if {@code f} is null
     */
    @SuppressWarnings("unchecked")
    @Override
    default <U> Stream<U> flatten(Function<? super T, ? extends Iterable<U>> f) {
        Objects.requireNonNull(f, "f is null");
        return isEmpty() ? Nil.instance() : Stream.ofAll(new Iterator<U>() {

            final Iterator<? extends T> inputs = Stream.this.iterator();
            Iterator<? extends U> current = Collections.emptyIterator();

            @Override
            public boolean hasNext() {
                boolean currentHasNext;
                while (!(currentHasNext = current.hasNext()) && inputs.hasNext()) {
                    current = f.apply(inputs.next()).iterator();
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
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        Seq.super.forEach(action);
    }

    @Override
    default boolean forAll(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return Seq.super.forAll(predicate);
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
    default Stream<Stream<T>> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    default int indexOf(T element) {
        int index = 0;
        for (Stream<T> stream = this; !stream.isEmpty(); stream = stream.tail(), index++) {
            if (Objects.equals(stream.head(), element)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    Stream<T> init();

    @Override
    Option<Stream<T>> initOption();

    @Override
    Stream<T> insert(int index, T element);

    @Override
    Stream<T> insertAll(int index, Iterable<? extends T> elements);

    @Override
    Stream<T> intersperse(T element);

    @Override
    default int lastIndexOf(T element) {
        int result = -1, index = 0;
        for (Stream<T> stream = this; !stream.isEmpty(); stream = stream.tail(), index++) {
            if (Objects.equals(stream.head(), element)) {
                result = index;
            }
        }
        return result;
    }

    @Override
    default <U> Stream<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return Nil.instance();
        } else {
            return new Cons<>(() -> mapper.apply(head()), () -> tail().map(mapper));
        }
    }

    @Override
    default Tuple2<Stream<T>, Stream<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return Tuple.of(filter(predicate), filter(predicate.negate()));
    }

    @Override
    Stream<T> peek(Consumer<? super T> action);

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
        return new Cons<>(() -> element, () -> this);
    }

    @Override
    default Stream<T> prependAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return Stream.ofAll(elements).appendAll(this);
    }

    @Override
    Stream<T> remove(T element);

    @Override
    default Stream<T> removeAll(T removed) {
        return filter(e -> !Objects.equals(e, removed));
    }

    @Override
    default Stream<T> removeAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final Stream<T> distinct = Stream.ofAll(elements).distinct();
        return filter(e -> !distinct.contains(e));
    }

    @Override
    Stream<T> replace(T currentElement, T newElement);

    @Override
    default Stream<T> replaceAll(T currentElement, T newElement) {
        if (isEmpty()) {
            return this;
        } else {
            final Supplier<T> newHead = () -> {
                final T head = head();
                return Objects.equals(head, currentElement) ? newElement : head;
            };
            return new Cons<>(newHead, () -> tail().replaceAll(currentElement, newElement));
        }
    }

    @Override
    default Stream<T> replaceAll(UnaryOperator<T> operator) {
        if (isEmpty()) {
            return this;
        } else {
            return new Cons<>(() -> operator.apply(head()), () -> tail().replaceAll(operator));
        }
    }

    @Override
    default Stream<T> retainAll(Iterable<? extends T> elements) {
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
        return isEmpty() ? this : foldLeft(Stream.nil(), Stream::prepend);
    }

    @Override
    default Stream<T> set(int index, T element) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("set(" + index + ", e) on Nil");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("set(" + index + ", e)");
        }
        Stream<T> preceding = Nil.instance();
        Stream<T> tail = this;
        for (int i = index; i > 0; i--, tail = tail.tail()) {
            if (tail.isEmpty()) {
                throw new IndexOutOfBoundsException("set(" + index + ", e) on Stream of size " + length());
            }
            preceding = preceding.prepend(tail.head());
        }
        if (tail.isEmpty()) {
            throw new IndexOutOfBoundsException("set(" + index + ", e) on Stream of size " + length());
        }
        // skip the current head element because it is replaced
        return preceding.reverse().appendAll(tail.tail().prepend(element));
    }

    @Override
    default Stream<Stream<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    default Stream<Stream<T>> sliding(int size, int step) {
        if (size <= 0 || step <= 0) {
            throw new IllegalArgumentException(String.format("size: %s or step: %s not positive", size, step));
        }
        if (isEmpty()) {
            return Nil.instance();
        } else {
            final Tuple2<Stream<T>, Stream<T>> split = splitAt(size);
            return new Cons<>(() -> split._1, () -> split._2.isEmpty() ? nil() : drop(step).sliding(size, step));
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
    default Spliterator<T> spliterator() {
        // the focus of the Stream API is on random-access collections of *known size*
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    default Stream<T> subsequence(int beginIndex) {
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
    Stream<T> subsequence(int beginIndex, int endIndex);

    @Override
    Stream<T> tail();

    @Override
    Option<Stream<T>> tailOption();

    @Override
    Stream<T> take(int n);

    @Override
    default Stream<T> takeRight(int n) {
        return reverse().take(n).reverse();
    }

    @Override
    Stream<T> takeWhile(Predicate<? super T> predicate);

    @Override
    default <T1, T2> Tuple2<Stream<T1>, Stream<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        final Stream<Tuple2<? extends T1, ? extends T2>> stream = map(unzipper);
        final Stream<T1> stream1 = stream.map(t -> t._1);
        final Stream<T2> stream2 = stream.map(t -> t._2);
        return Tuple.of(stream1, stream2);
    }

    @Override
    default <U> Stream<Tuple2<T, U>> zip(Iterable<U> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        final Stream<U> that = Stream.ofAll(iterable);
        if (this.isEmpty() || that.isEmpty()) {
            return Nil.instance();
        } else {
            return new Cons<>(() -> Tuple.of(this.head(), that.head()), () -> this.tail().zip(that.tail()));
        }
    }

    @Override
    default <U> Stream<Tuple2<T, U>> zipAll(Iterable<U> iterable, T thisElem, U thatElem) {
        Objects.requireNonNull(iterable, "iterable is null");
        final Stream<U> that = Stream.ofAll(iterable);
        final boolean isThisEmpty = this.isEmpty();
        final boolean isThatEmpty = that.isEmpty();
        if (isThisEmpty && isThatEmpty) {
            return Nil.instance();
        } else {
            final Supplier<Tuple2<T, U>> zippedHead = () -> Tuple.of(
                    isThisEmpty ? thisElem : this.head(),
                    isThatEmpty ? thatElem : that.head()
            );
            final Supplier<Stream<Tuple2<T, U>>> zippedTail = () -> {
                final Stream<T> tail1 = isThisEmpty ? this : this.tail();
                final Stream<U> tail2 = isThatEmpty ? that : that.tail();
                return tail1.zipAll(tail2, thisElem, thatElem);
            };
            return new Cons<>(zippedHead, zippedTail);
        }
    }

    @Override
    default Stream<Tuple2<T, Integer>> zipWithIndex() {
        return zip(Stream.from(0));
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
    final class Cons<T> extends AbstractStream<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final Lazy<T> head;
        private final Lazy<Stream<T>> tail;

        /**
         * Creates a new {@code Stream} consisting of a head element and a lazy trailing {@code Stream}.
         *
         * @param head A head element
         * @param tail A tail {@code Stream} supplier, {@linkplain Nil} denotes the end of the {@code Stream}
         */
        Cons(Supplier<T> head, Supplier<Stream<T>> tail) {
            this.head = Lazy.of(head);
            this.tail = Lazy.of(Objects.requireNonNull(tail, "tail is null"));
        }

        @Override
        public Stream<T> append(T element) {
            return new Cons<>(head, () -> tail().append(element));
        }

        @Override
        public Stream<T> appendAll(Iterable<? extends T> elements) {
            Objects.requireNonNull(elements, "elements is null");
            return new Cons<>(head, () -> tail().appendAll(elements));
        }

        @Override
        public T head() {
            return head.get();
        }

        @Override
        public Some<T> headOption() {
            return new Some<>(head.get());
        }

        @Override
        public Stream<T> init() {
            final Stream<T> tail = tail();
            if (tail.isEmpty()) {
                return Nil.instance();
            } else {
                return new Cons<>(head, tail::init);
            }
        }

        @Override
        public Some<Stream<T>> initOption() {
            return new Some<>(init());
        }

        @Override
        public Stream<T> insert(int index, T element) {
            if (index < 0) {
                throw new IndexOutOfBoundsException("insert(" + index + ", e)");
            }
            if (index == 0) {
                return new Cons<>(() -> element, () -> this);
            } else {
                return new Cons<>(head, () -> tail().insert(index - 1, element));
            }
        }

        @Override
        public Stream<T> insertAll(int index, Iterable<? extends T> elements) {
            Objects.requireNonNull(elements, "elements is null");
            if (index < 0) {
                throw new IndexOutOfBoundsException("insertAll(" + index + ", elements)");
            }
            if (index == 0) {
                return Stream.ofAll(elements).appendAll(this);
            } else {
                return new Cons<>(head, () -> tail().insertAll(index - 1, elements));
            }
        }

        @Override
        public Stream<T> intersperse(T element) {
            return new Cons<>(head, () -> {
                final Stream<T> tail = tail();
                return tail.isEmpty() ? tail : new Cons<>(() -> element, () -> tail.intersperse(element));
            });
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Stream<T> peek(Consumer<? super T> action) {
            action.accept(head.get());
            return new Cons<>(head, () -> tail().peek(action));
        }

        @Override
        public Stream<T> remove(T element) {
            return Objects.equals(head.get(), element) ? tail() : new Cons<>(head, () -> tail().remove(element));
        }

        @Override
        public Stream<T> replace(T currentElement, T newElement) {
            if (Objects.equals(head.get(), currentElement)) {
                return new Cons<>(() -> newElement, this::tail);
            } else {
                return new Cons<>(head, () -> tail().replace(currentElement, newElement));
            }
        }

        @Override
        public Stream<T> subsequence(int beginIndex, int endIndex) {
            if (beginIndex < 0 || beginIndex > endIndex) {
                throw new IndexOutOfBoundsException(String.format("subsequence(%s, %s)", beginIndex, endIndex));
            }
            if (beginIndex == endIndex) {
                return Nil.instance();
            }
            if (beginIndex == 0) {
                return new Cons<>(head, () -> tail().subsequence(0, endIndex - 1));
            } else {
                return tail().subsequence(beginIndex - 1, endIndex - 1);
            }
        }

        @Override
        public Stream<T> tail() {
            return tail.get();
        }

        @Override
        public Some<Stream<T>> tailOption() {
            return new Some<>(tail.get());
        }

        @Override
        public Stream<T> take(int n) {
            if (n < 1) {
                return Nil.instance();
            } else {
                return new Cons<>(head, () -> tail().take(n - 1));
            }
        }

        @Override
        public Stream<T> takeWhile(Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            if (predicate.test(head.get())) {
                return new Cons<>(head, () -> tail().takeWhile(predicate));
            } else {
                return Nil.instance();
            }
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

    /**
     * The empty Stream.
     * <p>
     * This is a singleton, i.e. not Cloneable.
     *
     * @param <T> Component type of the Stream.
     * @since 1.1.0
     */
    final class Nil<T> extends AbstractStream<T> implements Serializable {

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
        public Stream<T> append(T element) {
            return Stream.of(element);
        }

        @Override
        public Stream<T> appendAll(Iterable<? extends T> elements) {
            Objects.requireNonNull(elements, "elements is null");
            return Stream.ofAll(elements);
        }

        @Override
        public T head() {
            throw new NoSuchElementException("head of empty stream");
        }

        @Override
        public None<T> headOption() {
            return None.instance();
        }

        @Override
        public Stream<T> init() {
            throw new UnsupportedOperationException("init of empty stream");
        }

        @Override
        public None<Stream<T>> initOption() {
            return None.instance();
        }

        @Override
        public Stream<T> insert(int index, T element) {
            if (index != 0) {
                throw new IndexOutOfBoundsException("insert(" + index + ", e) on Nil");
            } else {
                return new Cons<>(() -> element, Nil::instance);
            }
        }

        @Override
        public Stream<T> insertAll(int index, Iterable<? extends T> elements) {
            Objects.requireNonNull(elements, "elements is null");
            if (index != 0) {
                throw new IndexOutOfBoundsException("insertAll(" + index + ", elements) on Nil");
            } else {
                return Stream.ofAll(elements);
            }
        }

        @Override
        public Stream<T> intersperse(T element) {
            return Nil.instance();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public Stream<T> peek(Consumer<? super T> action) {
            return this;
        }

        @Override
        public Stream<T> remove(T element) {
            return this;
        }

        @Override
        public Stream<T> replace(T currentElement, T newElement) {
            return this;
        }

        @Override
        public Stream<T> subsequence(int beginIndex, int endIndex) {
            if (beginIndex < 0 || beginIndex > endIndex) {
                throw new IndexOutOfBoundsException(String.format("subsequence(%s, %s)", beginIndex, endIndex));
            }
            if (beginIndex == endIndex) {
                return this;
            }
            throw new IndexOutOfBoundsException("subsequence of Nil");
        }

        @Override
        public Stream<T> tail() {
            throw new UnsupportedOperationException("tail of empty stream");
        }

        @Override
        public None<Stream<T>> tailOption() {
            return None.instance();
        }

        @Override
        public Stream<T> take(int n) {
            return this;
        }

        @Override
        public Stream<T> takeWhile(Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return this;
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
     * <p>
     * This class is needed because the interface {@link Stream} cannot use default methods to override Object's non-final
     * methods equals, hashCode and toString.
     * </p>
     * See <a href="http://mail.openjdk.java.net/pipermail/lambda-dev/2013-March/008435.html">Allow default methods to
     * override Object's methods</a>.
     *
     * @param <T> Component type of the Stream.
     * @since 1.1.0
     */
    abstract class AbstractStream<T> implements Stream<T> {

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
            int hashCode = 1;
            for (T element : this) {
                hashCode = 31 * hashCode + Objects.hashCode(element);
            }
            return hashCode;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder("Stream(");
            Stream<T> stream = this;
            while (stream != null && !stream.isEmpty()) {
                final Cons<T> cons = (Cons<T>) stream;
                builder.append(cons.head.get());
                if (cons.tail.isDefined()) {
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
    }
}
