/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.algebra.HigherKinded1;
import javaslang.algebra.Monad1;
import javaslang.control.Try;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * A Lazy linked list implementation.
 *
 * @param <T> component type of this Stream
 * @since 1.1.0
 */
// DEV-NOTE: Beware of serializing IO streams.
public interface Stream<T> extends Seq<T>, Monad1<T, Traversable<?>>, ValueObject {

    long serialVersionUID = 1L;

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
        final Function<ArrayList<T>, Stream<T>> finisher = Stream::of;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    static Stream<Integer> gen(int from) {
        return Stream.of(new Iterator<Integer>() {
            int i = from;
            boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Integer next() {
                if (i == Integer.MAX_VALUE) {
                    hasNext = false;
                }
                return i++;
            }
        });
    }

    static Stream<BigInteger> gen(BigInteger from) {
        return Stream.of(new Iterator<BigInteger>() {
            BigInteger i = from;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public BigInteger next() {
                final BigInteger value = i;
                i = i.add(BigInteger.ONE);
                return value;
            }
        });
    }

    // Supplier is not referential transparent in general. Example: Stream.gen(Math::random).take(10)
    static <T> Stream<T> gen(Supplier<T> supplier) {
        return Stream.of(new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return supplier.get();
            }
        });
    }


    static Stream<String> stdin() {
        return lines(System.in);
    }

    static Stream<String> stdin(Charset charset) {
        return lines(System.in, charset);
    }

    static Stream<String> lines(InputStream in) {
        return lines(in, Charset.defaultCharset());
    }

    static Stream<String> lines(InputStream in, Charset charset) {
        return Stream.of(new Iterator<String>() {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
            String next;

            @Override
            public boolean hasNext() {
                final boolean hasNext = (next = Try.<String>of(reader::readLine).orElse(null)) != null;
                if (!hasNext) {
                    Try.run(reader::close);
                }
                return hasNext;
            }

            @Override
            public String next() {
                // user can never call this Iterator.next() directly => no check of hasNext here
                return next;
            }
        });
    }

    static Stream<Character> chars(InputStream in) {
        return chars(in, Charset.defaultCharset());
    }

    static Stream<Character> chars(InputStream in, Charset charset) {
        return Stream.of(new Iterator<Character>() {
            final InputStreamReader reader = new InputStreamReader(in, charset);
            int next;

            @Override
            public boolean hasNext() {
                final boolean hasNext = (next = Try.of(reader::read).orElse(-1)) != -1;
                if (!hasNext) {
                    Try.run(reader::close);
                }
                return hasNext;
            }

            @Override
            public Character next() {
                // user can never call this Iterator.next() directly => no check of hasNext here
                return (char) next;
            }
        });
    }

    static Stream<Byte> bytes(InputStream in) {
        return Stream.of(new Iterator<Byte>() {
            int next;

            @Override
            public boolean hasNext() {
                final boolean hasNext = (next = Try.of(in::read).orElse(-1)) != -1;
                if (!hasNext) {
                    Try.run(in::close);
                }
                return hasNext;
            }

            @Override
            public Byte next() {
                // user can never call this Iterator.next() directly => no check of hasNext here
                return (byte) next;
            }
        });
    }

    static Stream<Integer> ints(InputStream in) {
        return Stream.of(new Iterator<Integer>() {
            int next;

            @Override
            public boolean hasNext() {
                final boolean hasNext = (next = Try.of(in::read).orElse(-1)) != -1;
                if (!hasNext) {
                    Try.run(in::close);
                }
                return hasNext;
            }

            @Override
            public Integer next() {
                // user can never call this Iterator.next() directly => no check of hasNext here
                return next;
            }
        });
    }

    /**
     * Returns the single instance of Nil. Convenience method for {@code Nil.instance()} .
     *
     * @param <T> Component type of Nil, determined by type inference in the particular context.
     * @return The empty list.
     */
    static <T> Stream<T> nil() {
        return Nil.instance();
    }

    /**
     * <p>
     * Use {@linkplain Stream#cons(Object)} instead of {@linkplain Stream#of(Iterable)} in order to create nested structures of
     * the form {@code Stream<Stream<T>>}.
     * </p>
     * <p>
     * {@linkplain Stream#cons(Object)} produces the same result as {@linkplain Stream#of(Iterable)} if T is not Iterable and
     * the Iterable contains only one element.
     * </p>
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new Stream instance containing the given element
     */
    static <T> Stream<T> cons(T element) {
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
        return Stream.of(new Iterator<T>() {
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
    static <T> Stream<T> of(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof Stream) {
            @SuppressWarnings("unchecked")
            final Stream<T> stream = (Stream<T>) elements;
            return stream;
        } else {
            return Stream.of(elements.iterator());
        }
    }

    // providing this method to save resources creating a Stream - makes no sense for collections in general
    static <T> Stream<T> of(Iterator<? extends T> iterator) {
        Objects.requireNonNull(iterator, "iterator is null");
        return new Deferred<>(() -> {
            if (iterator.hasNext()) {
                return new Cons<>(iterator.next(), () -> Stream.of(iterator));
            } else {
                return Nil.instance();
            }
        });
    }

    static Stream<Integer> range(int from, int toExclusive) {
        if (toExclusive == Integer.MIN_VALUE) {
            return Nil.instance();
        } else {
            return Stream.rangeClosed(from, toExclusive - 1);
        }
    }

    static Stream<Integer> rangeClosed(int from, int toInclusive) {
        if (from > toInclusive) {
            return Nil.instance();
        } else if (from == Integer.MIN_VALUE && toInclusive == Integer.MIN_VALUE) {
            return new Cons<>(Integer.MIN_VALUE, Nil::instance);
        } else {
            return Stream.of(new Iterator<Integer>() {
                int i = from;

                @Override
                public boolean hasNext() {
                    return i <= toInclusive;
                }

                @Override
                public Integer next() {
                    return i++;
                }
            });
        }
    }

    @Override
    default Stream<T> append(T element) {
        return isEmpty() ? new Cons<>(element, Nil::instance) : new Cons<>(head(), () -> tail().append(element));
    }

    @Override
    default Stream<T> appendAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return isEmpty() ? Stream.of(elements) : new Cons<>(head(), () -> tail().appendAll(elements));
    }

    @Override
    default Stream<T> clear() {
        return Nil.instance();
    }

    @Override
    default Stream<Stream<T>> combinations(int k) {
        class Recursion {
            Stream<Stream<T>> combinations(Stream<T> elements, int k) {
                return (k == 0) ? Stream.cons(Stream.nil()) :
                        elements.zipWithIndex().flatMap(t ->
                                combinations(elements.drop(t._2 + 1), (k - 1))
                                        .map((Stream<T> c) -> c.prepend(t._1)));
            }
        }
        return new Recursion().combinations(this, Math.max(k, 0));
    }

    @Override
    default Stream<T> distinct() {
        // TODO: better solution?
        return Stream.of(List.of(this).distinct());
    }

    @Override
    default Stream<T> drop(int n) {
        return (Stream<T>) Seq.super.drop(n);
    }

    @Override
    default Stream<T> dropRight(int n) {
        return (Stream<T>) Seq.super.dropRight(n);
    }

    @Override
    default Stream<T> dropWhile(Predicate<? super T> predicate) {
        return (Stream<T>) Seq.super.dropWhile(predicate);
    }

    @Override
    default Stream<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return Nil.instance();
        } else {
            final T head = head();
            if (predicate.test(head)) {
                return new Cons<>(head, () -> tail().filter(predicate));
            } else {
                return tail().filter(predicate);
            }
        }
    }

    @Override
    default <U, TRAVERSABLE extends HigherKinded1<U, Traversable<?>>> Stream<U> flatMap(Function1<? super T, TRAVERSABLE> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return Nil.instance();
        } else {
            @SuppressWarnings("unchecked")
            final Traversable<U> mapped = (Traversable<U>) mapper.apply(head());
            return Nil.<U>instance().appendAll(mapped).appendAll(tail().flatMap(mapper));
        }
    }

    @Override
    default <U> Stream<U> flatten() {
        final Seq<U> seq = Seq.super.flatten();
        return (Stream<U>) seq;
    }

    @Override
    default <U> Stream<U> flatten(Function1<T, ? extends Iterable<? extends U>> f) {
        Objects.requireNonNull(f, "f is null");
        if (isEmpty()) {
            return Nil.instance();
        } else {
            final Iterable<? extends U> mapped = f.apply(head());
            return Nil.<U>instance().appendAll(mapped).appendAll(tail().flatten(f));
        }
    }

    @Override
    default T get(int index) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("get(" + index + ") on empty stream");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("get(" + index + ")");
        }
        Stream<T> stream = this;
        for (int i = index - 1; i >= 0; i--) {
            stream = stream.tail();
            if (stream.isEmpty()) {
                throw new IndexOutOfBoundsException(String.format("get(%s) on stream of size %s", index, index - i));
            }
        }
        return stream.head();
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
    default Stream<T> init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init on empty Stream");
        } else {
            final Stream<T> tail = tail();
            if (tail.isEmpty()) {
                return Nil.instance();
            } else {
                return new Cons<>(head(), tail::init);
            }
        }
    }

    @Override
    default Stream<T> insert(int index, T element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        }
        if (index > 0 && isEmpty()) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e) on empty stream");
        }
        if (index == 0) {
            return new Cons<>(element, () -> this);
        } else {
            return new Cons<>(head(), () -> tail().insert(index - 1, element));
        }
    }

    @Override
    default Stream<T> insertAll(int index, Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (index < 0) {
            throw new IndexOutOfBoundsException("insertAll(" + index + ", elements)");
        }
        if (index > 0 && isEmpty()) {
            throw new IndexOutOfBoundsException("insertAll(" + index + ", elements) on empty stream");
        }
        if (index == 0) {
            return Stream.of(elements).appendAll(this);
        } else {
            return new Cons<>(head(), () -> tail().insertAll(index - 1, elements));
        }
    }

    @Override
    default Stream<T> intersperse(T element) {
        if (isEmpty()) {
            return Nil.instance();
        } else {
            return new Cons<>(head(), () -> {
                final Stream<T> tail = tail();
                return tail.isEmpty() ? tail : new Cons<>(element, () -> tail.intersperse(element));
            });
        }
    }

    @Override
    default Iterator<T> iterator() {

        final class StreamIterator implements Iterator<T> {

            Supplier<Stream<T>> streamSupplier = () -> Stream.this;

            @Override
            public boolean hasNext() {
                return !streamSupplier.get().isEmpty();
            }

            @Override
            public T next() {
                final Stream<T> stream = streamSupplier.get();
                if (stream.isEmpty()) {
                    throw new NoSuchElementException();
                } else {
                    // defer computation of stream = stream.tail() because computation of new head may be blocking!
                    streamSupplier = stream::tail;
                    return stream.head();
                }
            }
        }

        return new StreamIterator();
    }

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
    default <U> Stream<U> map(Function1<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return Nil.instance();
        } else {
            return new Cons<>(mapper.apply(head()), () -> tail().map(mapper));
        }
    }

    @Override
    default Stream<T> prepend(T element) {
        return new Cons<>(element, () -> this);
    }

    @Override
    default Stream<T> prependAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return Stream.of(elements).appendAll(this);
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
    default Stream<T> removeAll(T removed) {
        return filter(e -> !Objects.equals(e, removed));
    }

    @Override
    default Stream<T> removeAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final Stream<T> distinct = Stream.of(elements).distinct();
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
    default Stream<T> replaceAll(Function1<T, T> operator) {
        if (isEmpty()) {
            return this;
        } else {
            return new Cons<>(operator.apply(head()), () -> tail().replaceAll(operator));
        }
    }

    @Override
    default Stream<T> retainAll(Iterable<? extends T> elements) {
        final Stream<T> retained = Stream.of(elements).distinct();
        return filter(retained::contains);
    }

    @Override
    default Stream<T> reverse() {
        return foldLeft(nil(), Stream::prepend);
    }

    @Override
    default Stream<T> set(int index, T element) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("set(" + index + ", e) on empty stream");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("set(" + index + ", e)");
        }
        Stream<T> preceding = Nil.instance();
        Stream<T> tail = this;
        for (int i = index; i > 0; i--, tail = tail.tail()) {
            if (tail.isEmpty()) {
                throw new IndexOutOfBoundsException("set(" + index + ", e) on stream of size " + length());
            }
            preceding = preceding.prepend(tail.head());
        }
        if (tail.isEmpty()) {
            throw new IndexOutOfBoundsException("set(" + index + ", e) on stream of size " + length());
        }
        // skip the current head element because it is replaced
        return preceding.reverse().appendAll(tail.tail().prepend(element));
    }

    @Override
    default Stream<T> sort() {
        return toJavaStream().sorted().collect(Stream.collector());
    }

    @Override
    default Stream<T> sort(Comparator<? super T> c) {
        Objects.requireNonNull(c, "comparator is null");
        return toJavaStream().sorted(c).collect(Stream.collector());
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
                throw new IndexOutOfBoundsException(String.format("subsequence(%s) on stream of size %s", beginIndex, i));
            }
        }
        return result;
    }

    @Override
    default Stream<T> subsequence(int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex - beginIndex < 0) {
            throw new IndexOutOfBoundsException(String.format("subsequence(%s, %s)", beginIndex, endIndex));
        }
        if (endIndex - beginIndex == 0) {
            return Nil.instance();
        }
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("subsequence of empty stream");
        }
        if (beginIndex == 0) {
            return new Cons<>(head(), () -> tail().subsequence(0, endIndex - 1));
        } else {
            return tail().subsequence(beginIndex - 1, endIndex - 1);
        }
    }

    @Override
    Stream<T> tail();

    @Override
    default Stream<T> take(int n) {
        if (isEmpty()) {
            return this;
        } else if (n < 1) {
            return Nil.instance();
        } else {
            return new Cons<>(head(), () -> tail().take(n - 1));
        }
    }

    @Override
    default Stream<T> takeRight(int n) {
        return (Stream<T>) Seq.super.takeRight(n);
    }

    @Override
    default Stream<T> takeWhile(Predicate<? super T> predicate) {
        if (isEmpty()) {
            return this;
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
    default <T1, T2> Tuple2<Stream<T1>, Stream<T2>> unzip(Function1<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        final Stream<Tuple2<? extends T1, ? extends T2>> stream = map(unzipper);
        final Stream<T1> stream1 = stream.map(t -> t._1);
        final Stream<T2> stream2 = stream.map(t -> t._2);
        return Tuple.of(stream1, stream2);
    }

    @Override
    default <U> Stream<Tuple2<T, U>> zip(Iterable<U> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        final Stream<U> that = Stream.of(iterable);
        if (this.isEmpty() || that.isEmpty()) {
            return Nil.instance();
        } else {
            return new Cons<>(Tuple.of(this.head(), that.head()), () -> this.tail().zip(that.tail()));
        }
    }

    @Override
    default <U> Stream<Tuple2<T, U>> zipAll(Iterable<U> iterable, T thisElem, U thatElem) {
        Objects.requireNonNull(iterable, "iterable is null");
        final Stream<U> that = Stream.of(iterable);
        final boolean isThisEmpty = this.isEmpty();
        final boolean isThatEmpty = that.isEmpty();
        if (isThisEmpty && isThatEmpty) {
            return Nil.instance();
        } else {
            final T head1 = isThisEmpty ? thisElem : this.head();
            final U head2 = isThatEmpty ? thatElem : that.head();
            final Stream<T> tail1 = isThisEmpty ? this : this.tail();
            final Stream<U> tail2 = isThatEmpty ? that : that.tail();
            return new Cons<>(Tuple.of(head1, head2), () -> tail1.zipAll(tail2, thisElem, thatElem));
        }
    }

    @Override
    default Stream<Tuple2<T, Integer>> zipWithIndex() {
        return zip(() -> new Iterator<Integer>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                return i++;
            }
        });
    }

    /**
     * Non-empty Stream.
     *
     * @param <T> Component type of the Stream.
     */
    // DEV NOTE: class declared final because of serialization proxy pattern.
    // (see Effective Java, 2nd ed., p. 315)
    final class Cons<T> extends AbstractStream<T> {

        private static final long serialVersionUID = 1L;

        private final T head;
        private final Lazy<Stream<T>> tail;

        public Cons(T head, Supplier<Stream<T>> tail) {
            this.head = head;
            this.tail = Lazy.of(tail);
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public Stream<T> tail() {
            return tail.get();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Tuple2<T, Stream<T>> unapply() {
            return Tuple.of(head(), tail());
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
     * <p>
     * The empty Stream.
     * </p>
     * This is a singleton, i.e. not Cloneable.
     *
     * @param <T> Component type of the Stream.
     */
    final class Nil<T> extends AbstractStream<T> {

        private static final long serialVersionUID = 1L;

        private static final Nil<?> INSTANCE = new Nil<>();

        // hidden
        private Nil() {
        }

        @SuppressWarnings("unchecked")
        public static <T> Nil<T> instance() {
            return (Nil<T>) INSTANCE;
        }

        @Override
        public T head() {
            throw new UnsupportedOperationException("head of empty stream");
        }

        @Override
        public Stream<T> tail() {
            throw new UnsupportedOperationException("tail of empty stream");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public Tuple0 unapply() {
            return Tuple.empty();
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
     * Deferred Stream for lazy evaluation of blocking input.
     *
     * @param <T> Component type of the Stream.
     */
    final class Deferred<T> extends AbstractStream<T> {

        private static final long serialVersionUID = 1L;

        private final Lazy<Stream<T>> stream;

        public Deferred(Supplier<Stream<T>> streamSupplier) {
            this.stream = Lazy.of(streamSupplier);
        }

        @Override
        public T head() {
            return stream.get().head();
        }

        @Override
        public Stream<T> tail() {
            return stream.get().tail();
        }

        @Override
        public boolean isEmpty() {
            return stream.get().isEmpty();
        }

        @Override
        public Tuple2<T, Stream<T>> unapply() {
            return Tuple.of(head(), tail());
        }

        /**
         * <p>
         * {@code writeReplace} method for serializing wrapped Stream.
         * </p>
         * The presence of this method causes the serialization system to delegate tp the wrapped Stream instance
         * instead of an instance of the enclosing class.
         *
         * @return A SerialiationProxy for this enclosing class.
         */
        private Object writeReplace() {
            return stream.get();
        }

        /**
         * <p>
         * {@code readObject} method for preventing serialization of the enclosing class.
         * </p>
         * Guarantees that the serialization system will never generate a serialized instance of the enclosing class.
         *
         * @param stream An object serialization stream.
         * @throws java.io.InvalidObjectException This method will throw with the message "Not deserializable".
         */
        private void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("No direct serialization");
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
     */
    abstract class AbstractStream<T> implements Stream<T> {

        private static final long serialVersionUID = 1L;

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
            for (Stream<T> stream = this; !stream.isEmpty(); stream = stream.tail()) {
                final T element = stream.head();
                hashCode = 31 * hashCode + Objects.hashCode(element);
            }
            return hashCode;
        }

        @Override
        public String toString() {
            return Stream.class.getSimpleName() + map(String::valueOf).join(", ", "(", ")");
        }
    }
}
