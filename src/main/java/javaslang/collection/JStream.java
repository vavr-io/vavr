/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.algebra.HigherKinded;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * A {@code JStream} is lazy sequence of elements which may be infinitely long. Its immutability makes it suitable for
 * concurrent programming.
 * <p>
 * A {@code JStream} is composed of a {@code head} element and a lazy evaluated {@code tail} {@code JStream}.
 * <p>
 * There are two implementations of the {@code JStream} interface:
 * <ul>
 * <li>{@link Nil}, which represents the empty {@code JStream}.</li>
 * <li>{@link Cons}, which represents a {@code JStream} containing one or more elements.</li>
 * </ul>
 * Methods to obtain a {@code JStream}:
 * <pre>
 * <code>
 * // factory methods
 * JStream.nil()              // = JStream.of() = Nil.instance()
 * JStream.of(x)              // = new Cons&lt;&gt;(x, Nil.instance())
 * JStream.of(Object...)      // e.g. JStream.of(1, 2, 3)
 * JStream.of(Iterable)       // e.g. JStream.of(List.of(1, 2, 3)) = 1, 2, 3
 * JStream.of(Iterator)       // e.g. JStream.of(Arrays.asList(1, 2, 3).iterator()) = 1, 2, 3
 *
 * // int sequences
 * JStream.from(0)            // = 0, 1, 2, 3, ...
 * JStream.range(0, 3)        // = 0, 1, 2
 * JStream.rangeClosed(0, 3)  // = 0, 1, 2, 3
 *
 * // generators
 * JStream.gen(Supplier)      // e.g. JStream.gen(Math::random);
 * </code>
 * </pre>
 *
 * @param <T> component type of this Stream
 * @since 1.1.0
 */
// DEV-NOTE: Beware of serializing IO streams.
public interface JStream<T> extends JSeq<T>, ValueObject {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link JStream}.
     *
     * @param <T> Component type of the Stream.
     * @return A javaslang.collection.Stream Collector.
     */
    static <T> Collector<T, ArrayList<T>, JStream<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, JStream<T>> finisher = JStream::ofAll;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Returns an infinitely long JStream of int values starting from {@code from}.
     * <p>
     * The {@code JStream} extends to {@code Integer.MIN_VALUE} when passing {@code Integer.MAX_VALUE}.
     *
     * @param value a start int value
     * @return a new JStream of int values starting from {@code from}
     */
    static JStream<Integer> from(int value) {
        return new Cons<>(value, () -> from(value + 1));
    }

    /**
     * Generates an (theoretically) infinitely long JStream using a value Supplier.
     *
     * @param supplier A Supplier of JStream values
     * @param <T>      value type
     * @return A new JStream
     */
    static <T> JStream<T> gen(Supplier<T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return new Cons<>(supplier.get(), () -> gen(supplier));
    }

    /**
     * Returns the single instance of Nil. Convenience method for {@code Nil.instance()}.
     * <p>
     * Note: this method intentionally returns type {@code JStream} and not {@code Nil}. This comes handy when folding.
     * If you explicitely need type {@code Nil} use {@linkplain Nil#instance()}.
     *
     * @param <T> Component type of Nil, determined by type inference in the particular context.
     * @return The empty list.
     */
    static <T> JStream<T> nil() {
        return Nil.instance();
    }

    /**
     * Returns a singleton {@code JStream}, i.e. a {@code JStream} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new JStream instance containing the given element
     */
    static <T> JStream<T> of(T element) {
        return new Cons<>(element, Nil::instance);
    }

    /**
     * <p>
     * Creates a JStream of the given elements.
     * </p>
     * <pre>
     * <code>  JStream.of(1, 2, 3, 4)
     * = Nil.instance().prepend(4).prepend(3).prepend(2).prepend(1)
     * = new Cons(1, new Cons(2, new Cons(3, new Cons(4, Nil.instance()))))</code>
     * </pre>
     *
     * @param <T>      Component type of the JStream.
     * @param elements Zero or more elements.
     * @return A list containing the given elements in the same order.
     */
    @SafeVarargs
    static <T> JStream<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return JStream.ofAll(new Iterator<T>() {
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
     * Creates a JStream of the given elements.
     *
     * @param <T>      Component type of the JStream.
     * @param elements An Iterable of elements.
     * @return A list containing the given elements in the same order.
     */
    @SuppressWarnings("unchecked")
    static <T> JStream<T> ofAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof JStream) {
            return (JStream<T>) elements;
        } else {
            return JStream.ofAll(elements.iterator());
        }
    }

    /**
     * Creates a JStream based on an Iterator.
     *
     * @param iterator An Iterator
     * @param <T>      Component type
     * @return A new JStream
     */
    // providing this method to save resources creating a JStream - makes no sense for collections in general
    static <T> JStream<T> ofAll(Iterator<? extends T> iterator) {
        Objects.requireNonNull(iterator, "iterator is null");
        if (iterator.hasNext()) {
            return new Cons<>(iterator.next(), () -> JStream.ofAll(iterator));
        } else {
            return Nil.instance();
        }
    }

    /**
     * Creates a JStream of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or {@code Nil} if {@code from >= toExclusive}
     */
    static JStream<Integer> range(int from, int toExclusive) {
        if (from >= toExclusive) {
            return Nil.instance();
        } else {
            return JStream.rangeClosed(from, toExclusive - 1);
        }
    }

    /**
     * Creates a JStream of int numbers starting from {@code from}, extending to {@code toInclusive}.
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or {@code Nil} if {@code from > toInclusive}
     */
    static JStream<Integer> rangeClosed(int from, int toInclusive) {
        if (from > toInclusive) {
            return Nil.instance();
        } else if (from == Integer.MAX_VALUE) {
            return JStream.of(Integer.MAX_VALUE);
        } else {
            return new Cons<>(from, () -> rangeClosed(from + 1, toInclusive));
        }
    }

    @Override
    default JStream<T> append(T element) {
        return isEmpty() ? JStream.of(element) : new Cons<>(head(), () -> tail().append(element));
    }

    @Override
    default JStream<T> appendAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return isEmpty() ? JStream.ofAll(elements) : new Cons<>(head(), () -> tail().appendAll(elements));
    }

    @Override
    default JStream<T> clear() {
        return Nil.instance();
    }

    @Override
    default JStream<? extends JStream<T>> combinations() {
        return JStream.rangeClosed(0, length()).map(this::combinations).flatten(Function.identity());
    }

    @Override
    default JStream<JStream<T>> combinations(int k) {
        class Recursion {
            JStream<JStream<T>> combinations(JStream<T> elements, int k) {
                return (k == 0) ? JStream.of(JStream.nil()) :
                        elements.zipWithIndex().flatMap(t ->
                                combinations(elements.drop(t._2 + 1), (k - 1))
                                        .map((JStream<T> c) -> c.prepend(t._1)));
            }
        }
        return new Recursion().combinations(this, Math.max(k, 0));
    }

    @Override
    default JStream<T> distinct() {
        return distinct(Function.identity());
    }

    @Override
    default <U> JStream<T> distinct(Function<? super T, ? extends U> keyExtractor) {
        final Set<U> seen = new HashSet<>();
        return filter(t -> seen.add(keyExtractor.apply(t)));
    }

    @Override
    default JStream<T> drop(int n) {
        JStream<T> stream = this;
        while (n-- > 0 && !stream.isEmpty()) {
            stream = stream.tail();
        }
        return stream;
    }

    @Override
    default JStream<T> dropRight(int n) {
        return reverse().drop(n).reverse();
    }

    @Override
    default JStream<T> dropWhile(Predicate<? super T> predicate) {
        JStream<T> stream = this;
        while (!stream.isEmpty() && predicate.test(stream.head())) {
            stream = stream.tail();
        }
        return stream;
    }

    @Override
    default JStream<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        JStream<T> stream = this;
        while (!stream.isEmpty() && !predicate.test(stream.head())) {
            stream = stream.tail();
        }
        final JStream<T> finalStream = stream;
        return stream.isEmpty() ? stream : new Cons<>(stream.head(), () -> finalStream.tail().filter(predicate));
    }

    @Override
    default JStream<T> findAll(Predicate<? super T> predicate) {
        return filter(predicate);
    }

    @Override
    default <U, TRAVERSABLE extends HigherKinded<U, JTraversable<?>>> JStream<U> flatMap(Function<? super T, ? extends TRAVERSABLE> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return isEmpty() ? Nil.instance() : map(mapper).flatten(Function.identity());
    }

    /**
     * Flattens a {@code JStream} using a function. A common use case is to use the identity
     * {@code stream.flatten(Function::identity)} to flatten a {@code JStream} of {@code JStream}s.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Match&lt;JStream&lt;U&gt;&gt; f = Match
     *    .when((JStream&lt;U&gt; l) -&gt; l)
     *    .when((U u) -&gt; JStream.of(u));
     * JStream.of(1).flatten(f);              // = JStream(1)
     * JStream.of(JStream.of(1)).flatten(f);  // = JStream(1)
     * JStream.of(Nil.instance()).flatten(f); // = Nil
     * Nil.instance().flatten(f);             // = Nil
     * </code>
     * </pre>
     *
     * @param <U>           component type of the result {@code JStream}
     * @param <TRAVERSABLE> a {@code Traversable&lt;U&gt;}
     * @param f             a function which maps elements of this {@code JStream} to {@code JStream}s
     * @return a new {@code JStream}
     * @throws NullPointerException if {@code f} is null
     */
    @SuppressWarnings("unchecked")
    @Override
    default <U, TRAVERSABLE extends HigherKinded<U, JTraversable<?>>> JStream<U> flatten(Function<? super T, ? extends TRAVERSABLE> f) {
        Objects.requireNonNull(f, "f is null");
        return isEmpty() ? Nil.instance() : JStream.ofAll(new Iterator<U>() {

            final Iterator<? extends T> inputs = JStream.this.iterator();
            Iterator<? extends U> current = Collections.emptyIterator();

            @Override
            public boolean hasNext() {
                boolean currentHasNext;
                while (!(currentHasNext = current.hasNext()) && inputs.hasNext()) {
                    current = ((JTraversable<U>) f.apply(inputs.next())).iterator();
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
    default T get(int index) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("get(" + index + ") on empty stream");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("get(" + index + ")");
        }
        JStream<T> stream = this;
        for (int i = index - 1; i >= 0; i--) {
            stream = stream.tail();
            if (stream.isEmpty()) {
                throw new IndexOutOfBoundsException(String.format("get(%s) on stream of size %s", index, index - i));
            }
        }
        return stream.head();
    }

    @Override
    default JStream<JStream<T>> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    default int indexOf(T element) {
        int index = 0;
        for (JStream<T> stream = this; !stream.isEmpty(); stream = stream.tail(), index++) {
            if (Objects.equals(stream.head(), element)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    default JStream<T> init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init on empty Stream");
        } else {
            final JStream<T> tail = tail();
            if (tail.isEmpty()) {
                return Nil.instance();
            } else {
                return new Cons<>(head(), tail::init);
            }
        }
    }

    @Override
    default Option<JStream<T>> initOption() {
        return isEmpty() ? None.instance() : new Some<>(init());
    }

    @Override
    default JStream<T> insert(int index, T element) {
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
    default JStream<T> insertAll(int index, Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (index < 0) {
            throw new IndexOutOfBoundsException("insertAll(" + index + ", elements)");
        }
        if (index > 0 && isEmpty()) {
            throw new IndexOutOfBoundsException("insertAll(" + index + ", elements) on empty stream");
        }
        if (index == 0) {
            return JStream.ofAll(elements).appendAll(this);
        } else {
            return new Cons<>(head(), () -> tail().insertAll(index - 1, elements));
        }
    }

    @Override
    default JStream<T> intersperse(T element) {
        if (isEmpty()) {
            return Nil.instance();
        } else {
            return new Cons<>(head(), () -> {
                final JStream<T> tail = tail();
                return tail.isEmpty() ? tail : new Cons<>(element, () -> tail.intersperse(element));
            });
        }
    }

    @Override
    default int lastIndexOf(T element) {
        int result = -1, index = 0;
        for (JStream<T> stream = this; !stream.isEmpty(); stream = stream.tail(), index++) {
            if (Objects.equals(stream.head(), element)) {
                result = index;
            }
        }
        return result;
    }

    @Override
    default <U> JStream<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return Nil.instance();
        } else {
            return new Cons<>(mapper.apply(head()), () -> tail().map(mapper));
        }
    }

    @Override
    default Tuple2<JStream<T>, JStream<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return Tuple.of(filter(predicate), filter(predicate.negate()));
    }

    @Override
    default JStream<T> peek(Consumer<? super T> action) {
        if (isEmpty()) {
            return this;
        } else {
            final T head = head();
            action.accept(head);
            return new Cons<>(head, () -> tail().peek(action));
        }
    }

    @Override
    default JStream<JStream<T>> permutations() {
        if (isEmpty()) {
            return Nil.instance();
        } else {
            final JStream<T> tail = tail();
            if (tail.isEmpty()) {
                return JStream.of(this);
            } else {
                final JStream<JStream<T>> zero = Nil.instance();
                // TODO: IntelliJ IDEA 14.1.1 needs a redundant cast here, jdk 1.8.0_40 compiles fine
                return distinct().foldLeft(zero, (xs, x) -> xs.appendAll(remove(x).permutations().map((Function<JStream<T>, JStream<T>>) l -> l.prepend(x))));
            }
        }
    }

    @Override
    default JStream<T> prepend(T element) {
        return new Cons<>(element, () -> this);
    }

    @Override
    default JStream<T> prependAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return JStream.ofAll(elements).appendAll(this);
    }

    @Override
    default JStream<T> remove(T element) {
        if (isEmpty()) {
            return this;
        } else {
            final T head = head();
            return Objects.equals(head, element) ? tail() : new Cons<>(head, () -> tail().remove(element));
        }
    }

    @Override
    default JStream<T> removeAll(T removed) {
        return filter(e -> !Objects.equals(e, removed));
    }

    @Override
    default JStream<T> removeAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final JStream<T> distinct = JStream.ofAll(elements).distinct();
        return filter(e -> !distinct.contains(e));
    }

    @Override
    default JStream<T> replace(T currentElement, T newElement) {
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
    default JStream<T> replaceAll(T currentElement, T newElement) {
        if (isEmpty()) {
            return this;
        } else {
            final T head = head();
            final T newHead = Objects.equals(head, currentElement) ? newElement : head;
            return new Cons<>(newHead, () -> tail().replaceAll(currentElement, newElement));
        }
    }

    @Override
    default JStream<T> replaceAll(UnaryOperator<T> operator) {
        if (isEmpty()) {
            return this;
        } else {
            return new Cons<>(operator.apply(head()), () -> tail().replaceAll(operator));
        }
    }

    @Override
    default JStream<T> retainAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (isEmpty()) {
            return this;
        } else {
            final JStream<T> retained = JStream.ofAll(elements).distinct();
            return filter(retained::contains);
        }
    }

    @Override
    default JStream<T> reverse() {
        return isEmpty() ? this : foldLeft(JStream.nil(), JStream::prepend);
    }

    @Override
    default JStream<T> set(int index, T element) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("set(" + index + ", e) on empty stream");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("set(" + index + ", e)");
        }
        JStream<T> preceding = Nil.instance();
        JStream<T> tail = this;
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
    default JStream<JStream<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    default JStream<JStream<T>> sliding(int size, int step) {
        if (size <= 0 || step <= 0) {
            throw new IllegalArgumentException(String.format("size: %s or step: %s not positive", size, step));
        }
        if (isEmpty()) {
            return Nil.instance();
        } else {
            final Tuple2<JStream<T>, JStream<T>> split = splitAt(size);
            return new Cons<>(split._1, () -> split._2.isEmpty() ? Nil.instance() : drop(step).sliding(size, step));
        }
    }

    @Override
    default JStream<T> sort() {
        return isEmpty() ? this : toJavaStream().sorted().collect(JStream.collector());
    }

    @Override
    default JStream<T> sort(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return isEmpty() ? this : toJavaStream().sorted(comparator).collect(JStream.collector());
    }

    @Override
    default Tuple2<JStream<T>, JStream<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return Tuple.of(takeWhile(predicate), dropWhile(predicate));
    }

    @Override
    default Tuple2<JStream<T>, JStream<T>> splitAt(int n) {
        return Tuple.of(take(n), drop(n));
    }

    @Override
    default Spliterator<T> spliterator() {
        // the focus of the Stream API is on random-access collections of *known size*
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    default JStream<T> subsequence(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("subsequence(" + beginIndex + ")");
        }
        JStream<T> result = this;
        for (int i = 0; i < beginIndex; i++, result = result.tail()) {
            if (result.isEmpty()) {
                throw new IndexOutOfBoundsException(String.format("subsequence(%s) on stream of size %s", beginIndex, i));
            }
        }
        return result;
    }

    @Override
    default JStream<T> subsequence(int beginIndex, int endIndex) {
        if (beginIndex < 0 || beginIndex > endIndex) {
            throw new IndexOutOfBoundsException(String.format("subsequence(%s, %s)", beginIndex, endIndex));
        }
        if (beginIndex == endIndex) {
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
    JStream<T> tail();

    @Override
    default JStream<T> take(int n) {
        if (isEmpty()) {
            return this;
        } else if (n < 1) {
            return Nil.instance();
        } else {
            return new Cons<>(head(), () -> tail().take(n - 1));
        }
    }

    @Override
    default JStream<T> takeRight(int n) {
        return reverse().take(n).reverse();
    }

    @Override
    default JStream<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
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
    default <T1, T2> Tuple2<JStream<T1>, JStream<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        final JStream<Tuple2<? extends T1, ? extends T2>> stream = map(unzipper);
        final JStream<T1> stream1 = stream.map(t -> t._1);
        final JStream<T2> stream2 = stream.map(t -> t._2);
        return Tuple.of(stream1, stream2);
    }

    @Override
    default <U> JStream<Tuple2<T, U>> zip(Iterable<U> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        final JStream<U> that = JStream.ofAll(iterable);
        if (this.isEmpty() || that.isEmpty()) {
            return Nil.instance();
        } else {
            return new Cons<>(Tuple.of(this.head(), that.head()), () -> this.tail().zip(that.tail()));
        }
    }

    @Override
    default <U> JStream<Tuple2<T, U>> zipAll(Iterable<U> iterable, T thisElem, U thatElem) {
        Objects.requireNonNull(iterable, "iterable is null");
        final JStream<U> that = JStream.ofAll(iterable);
        final boolean isThisEmpty = this.isEmpty();
        final boolean isThatEmpty = that.isEmpty();
        if (isThisEmpty && isThatEmpty) {
            return Nil.instance();
        } else {
            final T head1 = isThisEmpty ? thisElem : this.head();
            final U head2 = isThatEmpty ? thatElem : that.head();
            final JStream<T> tail1 = isThisEmpty ? this : this.tail();
            final JStream<U> tail2 = isThatEmpty ? that : that.tail();
            return new Cons<>(Tuple.of(head1, head2), () -> tail1.zipAll(tail2, thisElem, thatElem));
        }
    }

    @Override
    default JStream<Tuple2<T, Integer>> zipWithIndex() {
        return zip(JStream.from(0));
    }

    /**
     * Non-empty {@code JStream}, consisting of a {@code head}, a {@code tail} and an optional
     * {@link java.lang.AutoCloseable}.
     *
     * @param <T> Component type of the JStream.
     * @since 1.1.0
     */
    // DEV NOTE: class declared final because of serialization proxy pattern.
    // (see Effective Java, 2nd ed., p. 315)
    final class Cons<T> extends AbstractStream<T> {

        private static final long serialVersionUID = 1L;

        private final T head;
        private final Lazy<JStream<T>> tail;

        /**
         * Creates a new {@code JStream} consisting of a head element and a lazy trailing {@code JStream}.
         *
         * @param head A head element
         * @param tail A tail {@code JStream} supplier, {@linkplain Nil} denotes the end of the {@code JStream}
         */
        public Cons(T head, Supplier<JStream<T>> tail) {
            this.head = head;
            this.tail = Lazy.of(Objects.requireNonNull(tail, "tail is null"));
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public Option<T> headOption() {
            return new Some<>(head);
        }

        @Override
        public JStream<T> tail() {
            return tail.get();
        }

        @Override
        public Option<JStream<T>> tailOption() {
            return new Some<>(tail.get());
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Tuple2<T, JStream<T>> unapply() {
            return Tuple.of(head, tail.get());
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
                for (JStream<T> l = stream; !l.isEmpty(); l = l.tail()) {
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
                JStream<T> temp = Nil.instance();
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
     * The empty JStream.
     * <p>
     * This is a singleton, i.e. not Cloneable.
     *
     * @param <T> Component type of the JStream.
     * @since 1.1.0
     */
    final class Nil<T> extends AbstractStream<T> {

        private static final long serialVersionUID = 1L;

        private static final Nil<?> INSTANCE = new Nil<>();

        // hidden
        private Nil() {
        }

        /**
         * Returns the singleton empty JStream instance.
         *
         * @param <T> Component type of the JStream
         * @return The empty JStream
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
        public Option<T> headOption() {
            return None.instance();
        }

        @Override
        public JStream<T> tail() {
            throw new UnsupportedOperationException("tail of empty stream");
        }

        @Override
        public Option<JStream<T>> tailOption() {
            return None.instance();
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
     * <p>
     * This class is needed because the interface {@link JStream} cannot use default methods to override Object's non-final
     * methods equals, hashCode and toString.
     * </p>
     * See <a href="http://mail.openjdk.java.net/pipermail/lambda-dev/2013-March/008435.html">Allow default methods to
     * override Object's methods</a>.
     *
     * @param <T> Component type of the JStream.
     * @since 1.1.0
     */
    abstract class AbstractStream<T> implements JStream<T> {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof JStream) {
                JStream<?> stream1 = this;
                JStream<?> stream2 = (JStream<?>) o;
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
            for (JStream<T> stream = this; !stream.isEmpty(); stream = stream.tail()) {
                final T element = stream.head();
                hashCode = 31 * hashCode + Objects.hashCode(element);
            }
            return hashCode;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder("JStream(");
            JStream<T> stream = this;
            while (stream != null && !stream.isEmpty()) {
                final Cons<T> cons = (Cons<T>) stream;
                builder.append(cons.head);
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
