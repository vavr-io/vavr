/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

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
 * A {@code List} is an eager sequence of elements. Its immutability makes it suitable for concurrent programming.
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
 * List.nil()              // = List.of() = Nil.instance()
 * List.of(x)              // = new Cons&lt;&gt;(x, Nil.instance())
 * List.of(Object...)      // e.g. List.of(1, 2, 3)
 * List.ofAll(Iterable)    // e.g. List.ofAll(Stream.of(1, 2, 3)) = 1, 2, 3
 *
 * // int sequences
 * List.range(0, 3)        // = 0, 1, 2
 * List.rangeClosed(0, 3)  // = 0, 1, 2, 3
 * </code>
 * </pre>
 *
 * Note: A {@code List} is primary a {@code Seq} and extends {@code Stack} for technical reasons (so {@code Stack} does not need to wrap {@code List}).
 * <p>
 * See Okasaki, Chris: <em>Purely Functional Data Structures</em> (p. 7 ff.). Cambridge, 2003.
 *
 * @param <T> Component type of the List
 * @since 1.1.0
 */
public interface List<T> extends Seq<T>, Stack<T> {

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.List}
     * .
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
    static <T> List<T> nil() {
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
        List<T> result = Nil.<T>instance();
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
     * @param elements An Iterable of elements.
     * @return A list containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    static <T> List<T> ofAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof List) {
            @SuppressWarnings("unchecked")
            final List<T> list = (List<T>) elements;
            return list;
        } else if (elements instanceof java.util.List<?>) {
            List<T> result = Nil.instance();
            final java.util.List<? extends T> list = (java.util.List<? extends T>) elements;
            final ListIterator<? extends T> iter = list.listIterator(list.size());
            while (iter.hasPrevious()) {
                result = result.prepend(iter.previous());
            }
            return result;
        } else if (elements instanceof NavigableSet<?>) {
            List<T> result = Nil.instance();
            final Iterator<? extends T> iter = ((NavigableSet<? extends T>) elements).descendingIterator();
            while (iter.hasNext()) {
                result = result.prepend(iter.next());
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
     * Creates a List of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or {@code Nil} if {@code from >= toExclusive}
     */
    static List<Integer> range(int from, int toExclusive) {
        if (from >= toExclusive) {
            return Nil.instance();
        } else {
            return List.rangeClosed(from, toExclusive - 1);
        }
    }

    /**
     * Creates a List of int numbers starting from {@code from}, extending to {@code toInclusive}.
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or {@code Nil} if {@code from > toInclusive}
     */
    static List<Integer> rangeClosed(int from, int toInclusive) {
        if (from > toInclusive) {
            return Nil.instance();
        } else if (toInclusive == Integer.MIN_VALUE) {
            return List.of(Integer.MIN_VALUE);
        } else {
            List<Integer> result = Nil.instance();
            for (int i = toInclusive; i >= from; i--) {
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
    default List<T> appendAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return foldRight(List.ofAll(elements), (x, xs) -> xs.prepend(x));
    }

    @Override
    default List<T> clear() {
        return Nil.instance();
    }

    @Override
    default List<List<T>> combinations() {
        return List.rangeClosed(0, length()).map(this::combinations).flatten(Function.identity());
    }

    @Override
    default List<List<T>> combinations(int k) {
        class Recursion {
            List<List<T>> combinations(List<T> elements, int k) {
                return (k == 0)
                        ? List.of(List.nil())
                        : elements.zipWithIndex().flatMap(t -> combinations(elements.drop(t._2 + 1), (k - 1))
                        .map((List<T> c) -> c.prepend(t._1)));
            }
        }
        return new Recursion().combinations(this, Math.max(k, 0));
    }

    @Override
    default List<T> distinct() {
        return distinct(Function.identity());
    }

    @Override
    default <U> List<T> distinct(Function<? super T, ? extends U> keyExtractor) {
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
        return reverse().drop(n).reverse();
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
        return isEmpty() ? this : foldLeft(List.<T>nil(), (xs, x) -> predicate.test(x) ? xs.prepend(x) : xs).reverse();
    }

    @Override
    default List<T> findAll(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate);
    }

    @Override
    default <U> List<U> flatMap(Function<? super T, ? extends Iterable<U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return nil();
        } else {
            List<U> list = nil();
            for (T t : this) {
                for (U u : mapper.apply(t)) {
                    list = list.prepend(u);
                }
            }
            return list.reverse();
        }
    }

    /**
     * Flattens a {@code List} using a function {@code f}. A common use case is to use the identity
     * {@code list.flatten(Function::identity)} to flatten a {@code List} of {@code List}s.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Match&lt;List&lt;U&gt;&gt; f = Match
     *    .when((List&lt;U&gt; l) -&gt; l)
     *    .when((U u) -&gt; List.of(u));
     * List.of(1).flatten(f);              // = List(1)
     * List.of(List.of(1)).flatten(f);     // = List(1)
     * List.of(Nil.instance()).flatten(f); // = Nil
     * Nil.instance().flatten(f);          // = Nil
     * </code>
     * </pre>
     *
     * @param <U> component type of the result {@code List}
     * @param f   a function which maps elements of this {@code List} to {@code List}s
     * @return a new {@code List}
     * @throws NullPointerException if {@code f} is null
     */
    @Override
    default <U> List<U> flatten(Function<? super T, ? extends Iterable<U>> f) {
        Objects.requireNonNull(f, "f is null");
        return isEmpty() ? Nil.instance() : foldRight(nil(), (t, xs) -> xs.prependAll(f.apply(t)));
    }

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
    default List<List<T>> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    default int indexOf(T element) {
        int index = 0;
        for (List<T> list = this; !list.isEmpty(); list = list.tail(), index++) {
            if (Objects.equals(list.head(), element)) {
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
    default List<T> insertAll(int index, Iterable<? extends T> elements) {
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
        return isEmpty() ? Nil.instance() : foldRight(nil(), (x, xs) -> xs.isEmpty() ? xs.prepend(x) : xs.prepend(element).prepend(x));
    }

    @Override
    default int lastIndexOf(T element) {
        int result = -1, index = 0;
        for (List<T> list = this; !list.isEmpty(); list = list.tail(), index++) {
            if (Objects.equals(list.head(), element)) {
                result = index;
            }
        }
        return result;
    }

    @Override
    default <U> List<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        List<U> list = nil();
        for (T t : this) {
            list = list.prepend(mapper.apply(t));
        }
        return list.reverse();
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
    default List<T> prependAll(Iterable<? extends T> elements) {
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
        List<T> result = Nil.<T>instance();
        for (T element : elements) {
            result = result.prepend(element);
        }
        return result;
    }

    @Override
    default List<T> pushAll(Iterable<T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        List<T> result = Nil.<T>instance();
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
        List<T> result = tail;
        for (T next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<T> removeAll(T removed) {
        List<T> result = Nil.instance();
        for (T element : this) {
            if (!element.equals(removed)) {
                result = result.prepend(element);
            }
        }
        return result.reverse();
    }

    @Override
    default List<T> removeAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        List<T> removed = List.ofAll(elements).distinct();
        List<T> result = Nil.instance();
        for (T element : this) {
            if (!removed.contains(element)) {
                result = result.prepend(element);
            }
        }
        return result.reverse();
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
    default List<T> retainAll(Iterable<? extends T> elements) {
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
        return isEmpty() ? this : foldLeft(nil(), List::prepend);
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
    default List<List<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    default List<List<T>> sliding(int size, int step) {
        if (size <= 0 || step <= 0) {
            throw new IllegalArgumentException(String.format("size: %s or step: %s not positive", size, step));
        }
        List<List<T>> result = Nil.instance();
        List<T> list = this;
        while (!list.isEmpty()) {
            final Tuple2<List<T>, List<T>> split = list.splitAt(size);
            result = result.prepend(split._1);
            list = split._2.isEmpty() ? Nil.instance() : list.drop(step);
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
        return Tuple.of(takeWhile(predicate), dropWhile(predicate));
    }

    @Override
    default Tuple2<List<T>, List<T>> splitAt(int n) {
        return Tuple.of(take(n), drop(n));
    }

    @Override
    default Spliterator<T> spliterator() {
        // the focus of the Stream API is on random-access collections of *known size*
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    default List<T> subsequence(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("subsequence(" + beginIndex + ")");
        }
        List<T> result = this;
        for (int i = 0; i < beginIndex; i++, result = result.tail()) {
            if (result.isEmpty()) {
                throw new IndexOutOfBoundsException(
                        String.format("subsequence(%s) on List of length %s", beginIndex, i));
            }
        }
        return result;
    }

    @Override
    default List<T> subsequence(int beginIndex, int endIndex) {
        if (beginIndex < 0 || beginIndex > endIndex) {
            throw new IndexOutOfBoundsException(
                    String.format("subsequence(%s, %s) on List of length %s", beginIndex, endIndex, length()));
        }
        List<T> result = Nil.instance();
        List<T> list = this;
        for (int i = 0; i < endIndex; i++, list = list.tail()) {
            if (list.isEmpty()) {
                throw new IndexOutOfBoundsException(
                        String.format("subsequence(%s, %s) on List of length %s", beginIndex, endIndex, i));
            }
            if (i >= beginIndex) {
                result = result.prepend(list.head());
            }
        }
        return result.reverse();
    }

    @Override
    List<T> tail();

    @Override
    Option<List<T>> tailOption();

    @Override
    default List<T> take(int n) {
        List<T> result = Nil.instance();
        List<T> list = this;
        for (int i = 0; i < n && !list.isEmpty(); i++, list = list.tail()) {
            result = result.prepend(list.head());
        }
        return result.reverse();
    }

    @Override
    default List<T> takeRight(int n) {
        return reverse().take(n).reverse();
    }

    @Override
    default List<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        List<T> result = Nil.instance();
        for (List<T> list = this; !list.isEmpty() && predicate.test(list.head()); list = list.tail()) {
            result = result.prepend(list.head());
        }
        return result.reverse();
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
    default <U> List<Tuple2<T, U>> zip(Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        List<Tuple2<T, U>> result = Nil.instance();
        List<T> list1 = this;
        Iterator<U> list2 = that.iterator();
        while (!list1.isEmpty() && list2.hasNext()) {
            result = result.prepend(Tuple.of(list1.head(), list2.next()));
            list1 = list1.tail();
        }
        return result.reverse();
    }

    @Override
    default <U> List<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        List<Tuple2<T, U>> result = Nil.instance();
        Iterator<T> list1 = this.iterator();
        Iterator<U> list2 = that.iterator();
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
    final class Cons<T> extends AbstractList<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final T head;
        private final List<T> tail;

        /**
         * Creates a List consisting of a head value and a trailing List.
         *
         * @param head The head
         * @param tail The tail
         */
        Cons(T head, List<T> tail) {
            this.head = head;
            this.tail = tail;
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
    final class Nil<T> extends AbstractList<T> implements Serializable {

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
     * This class is needed because the interface {@link List} cannot use default methods to override Object's non-final
     * methods equals, hashCode and toString.
     * </p>
     * See <a href="http://mail.openjdk.java.net/pipermail/lambda-dev/2013-March/008435.html">Allow default methods to
     * override Object's methods</a>.
     *
     * @param <T> Component type of the List.
     * @since 1.1.0
     */
    abstract class AbstractList<T> implements List<T> {

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
            int hashCode = 1;
            for (T element : this) {
                hashCode = 31 * hashCode + Objects.hashCode(element);
            }
            return hashCode;
        }

        @Override
        public String toString() {
            return map(String::valueOf).join(", ", "List(", ")");
        }
    }
}
