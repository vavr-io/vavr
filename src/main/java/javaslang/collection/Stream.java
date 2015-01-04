/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.Memoizer.Memoizer0;
import javaslang.Algebra.Monad;
import javaslang.Algebra.Monoid;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

public interface Stream<T> extends Seq<T>, Monad<T, Traversable<?>>, Monoid<Stream<T>>, ValueObject {

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
                if (!hasNext) {
                    throw new NoSuchElementException();
                }
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
     * Creates a Stream of the given elements.
     * <p/>
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
        Require.nonNull(elements, "elements is null");
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
        Require.nonNull(elements, "elements is null");
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
        Require.nonNull(iterator, "iterator is null");
        if (iterator.hasNext()) {
            return new Cons<>(iterator.next(), () -> Stream.of(iterator));
        } else {
            return Nil.instance();
        }
    }

    static Stream<Integer> range(int from, int to) {
        if (from > to) {
            return Nil.instance();
        } else if (from == Integer.MIN_VALUE && to == Integer.MIN_VALUE) {
            return new Cons<>(Integer.MIN_VALUE, Nil::instance);
        } else {
            return Stream.of(new Iterator<Integer>() {
                int i = from;

                @Override
                public boolean hasNext() {
                    return i <= to;
                }

                @Override
                public Integer next() {
                    return i++;
                }
            });
        }
    }

    static Stream<Integer> until(int from, int to) {
        if (to == Integer.MIN_VALUE) {
            return Nil.instance();
        } else {
            return Stream.range(from, to - 1);
        }
    }

    @Override
    default Stream<T> append(T element) {
        return isEmpty() ? new Cons<>(element, Nil::instance) : new Cons<>(head(), () -> tail().append(element));
    }

    @Override
    default Stream<T> appendAll(Iterable<? extends T> elements) {
        Require.nonNull(elements, "elements is null");
        return isEmpty() ? Stream.of(elements) : new Cons<>(head(), () -> tail().appendAll(elements));
    }

    @Override
    default Stream<T> clear() {
        return Nil.instance();
    }

    @Override
    default Stream<T> combine(Stream<T> list1, Stream<T> list2) {
        Require.nonNull(list1, "list1 is null");
        Require.nonNull(list2, "list2 is null");
        return list1.appendAll(list2);
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
        Require.nonNull(predicate, "predicate is null");
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
    default <U, TRAVERSABLE extends Manifest<U, Traversable<?>>> Stream<U> flatMap(Function<? super T, TRAVERSABLE> mapper) {
        Require.nonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return Nil.instance();
        } else {
            @SuppressWarnings("unchecked")
            final Traversable<U> mapped = (Traversable<U>) mapper.apply(head());
            return Nil.<U>instance().appendAll(mapped).appendAll(tail().flatMap(mapper));
        }
    }

    @Override
    default T fold(T zero, BiFunction<? super T, ? super T, ? extends T> op) {
        return foldLeft(zero, op);
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
        Require.nonNull(elements, "elements is null");
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

            Stream<T> stream = Stream.this;

            @Override
            public boolean hasNext() {
                return !stream.isEmpty();
            }

            @Override
            public T next() {
                if (stream.isEmpty()) {
                    throw new NoSuchElementException();
                } else {
                    final T result = stream.head();
                    stream = stream.tail();
                    return result;
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
    default <U> Stream<U> map(Function<? super T, ? extends U> mapper) {
        Require.nonNull(mapper, "mapper is null");
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
        Require.nonNull(elements, "elements is null");
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
        Require.nonNull(elements, "elements is null");
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
    default Stream<T> replaceAll(UnaryOperator<T> operator) {
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
        Require.nonNull(c, "comparator is null");
        return toJavaStream().sorted(c).collect(Stream.collector());
    }

    @Override
    default Tuple.Tuple2<Stream<T>, Stream<T>> span(Predicate<? super T> predicate) {
        Require.nonNull(predicate, "predicate is null");
        return Tuple.of(takeWhile(predicate), dropWhile(predicate));
    }

    @Override
    default Tuple.Tuple2<Stream<T>, Stream<T>> splitAt(int n) {
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
    default <T1, T2> Tuple.Tuple2<Stream<T1>, Stream<T2>> unzip(Function<? super T, Tuple.Tuple2<T1, T2>> unzipper) {
        Require.nonNull(unzipper, "unzipper is null");
        final Stream<Tuple.Tuple2<T1, T2>> stream = map(unzipper);
        return Tuple.of(stream.map(t -> t._1), stream.map(t -> t._2));
    }

    @Override
    default Stream<T> zero() {
        return Nil.instance();
    }

    @Override
    default <U> Stream<Tuple.Tuple2<T, U>> zip(Iterable<U> iterable) {
        Require.nonNull(iterable, "iterable is null");
        final Stream<U> that = Stream.of(iterable);
        if (this.isEmpty() || that.isEmpty()) {
            return Nil.instance();
        } else {
            return new Cons<>(Tuple.of(this.head(), that.head()), () -> this.tail().zip(that.tail()));
        }
    }

    @Override
    default <U> Stream<Tuple.Tuple2<T, U>> zipAll(Iterable<U> iterable, T thisElem, U thatElem) {
        Require.nonNull(iterable, "iterable is null");
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
    default Stream<Tuple.Tuple2<T, Integer>> zipWithIndex() {
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
    static final class Cons<T> extends AbstractStream<T> {

        private static final long serialVersionUID = 53595355464228669L;

        private final T head;
        private final Memoizer0<Stream<T>> tail;

        public Cons(T head, Supplier<Stream<T>> tail) {
            this.head = head;
            this.tail = Memoizer.of(tail);
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public Stream<T> tail() {
            return tail.apply();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Tuple.Tuple2<T, Stream<T>> unapply() {
            return Tuple.of(head, tail());
        }

        /**
         * {@code writeReplace} method for the serialization proxy pattern.
         * <p/>
         * The presence of this method causes the serialization system to emit a SerializationProxy instance instead of
         * an instance of the enclosing class.
         *
         * @return A SerialiationProxy for this enclosing class.
         */
        private Object writeReplace() {
            return new SerializationProxy<>(this);
        }

        /**
         * {@code readObject} method for the serialization proxy pattern.
         * <p/>
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

            private static final long serialVersionUID = 3851894487210781138L;

            // the instance to be serialized/deserialized
            private transient Cons<T> stream;

            /**
             * Constructor for the case of serialization, called by {@link Cons#writeReplace()}.
             * <p/>
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
             * {@code readResolve} method for the serialization proxy pattern.
             * <p/>
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
     * <p/>
     * This is a singleton, i.e. not Cloneable.
     *
     * @param <T> Component type of the Stream.
     */
    static final class Nil<T> extends AbstractStream<T> {

        private static final long serialVersionUID = 809473773619488283L;

        private static final Nil<?> INSTANCE = new Nil<>();

        // hidden
        private Nil() {
        }

        public static <T> Nil<T> instance() {
            @SuppressWarnings("unchecked")
            final Nil<T> instance = (Nil<T>) INSTANCE;
            return instance;
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
        public Tuple.Tuple0 unapply() {
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
     * This class is needed because the interface {@link Stream} cannot use default methods to override Object's non-final
     * methods equals, hashCode and toString.
     * <p/>
     * See <a href="http://mail.openjdk.java.net/pipermail/lambda-dev/2013-March/008435.html">Allow default methods to
     * override Object's methods</a>.
     *
     * @param <T> Component type of the Stream.
     */
    static abstract class AbstractStream<T> implements Stream<T> {

        private static final long serialVersionUID = 5433763348296234013L;

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof Stream)) {
                return false;
            } else {
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
            return Stream.class.getSimpleName() + map(Strings::toString).join(", ", "(", ")");
        }
    }
}
