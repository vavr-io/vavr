/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.control.Match;
import javaslang.control.Option;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * An immutable {@code HashSet} implementation.
 *
 * @param <T> Component type
 * @author Ruslan Sennov, Patryk Najda, Daniel Dietrich
 * @since 2.0.0
 */
public final class LinkedHashSet<T> implements Set<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final LinkedHashSet<?> EMPTY = new LinkedHashSet<>(LinkedHashMap.empty());

    private final LinkedHashMap<T, T> map;

    private LinkedHashSet(LinkedHashMap<T, T> map) {
        this.map = map;
    }

    @SuppressWarnings("unchecked")
    public static <T> LinkedHashSet<T> empty() {
        return (LinkedHashSet<T>) EMPTY;
    }

    /**
     * Returns a {@link Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(Collector)} to obtain a {@link LinkedHashSet}.
     *
     * @param <T> Component type of the LinkedHashSet.
     * @return A javaslang.collection.LinkedHashSet Collector.
     */
    public static <T> Collector<T, ArrayList<T>, LinkedHashSet<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, LinkedHashSet<T>> finisher = LinkedHashSet::ofAll;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Returns a singleton {@code LinkedHashSet}, i.e. a {@code LinkedHashSet} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new LinkedHashSet instance containing the given element
     */
    public static <T> LinkedHashSet<T> of(T element) {
        return LinkedHashSet.<T> empty().add(element);
    }

    /**
     * Creates a LinkedHashSet of the given elements.
     *
     * <pre><code>LinkedHashSet.of(1, 2, 3, 4)</code></pre>
     *
     * @param <T>      Component type of the LinkedHashSet.
     * @param elements Zero or more elements.
     * @return A set containing the given elements.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    public static <T> LinkedHashSet<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        LinkedHashMap<T, T> map = LinkedHashMap.empty();
        for (T element : elements) {
            map = map.put(element, element);
        }
        return map.isEmpty() ? LinkedHashSet.empty() : new LinkedHashSet<>(map);
    }

    /**
     * Returns a LinkedHashSet containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param <T> Component type of the LinkedHashSet
     * @param n The number of elements in the LinkedHashSet
     * @param f The Function computing element values
     * @return A LinkedHashSet consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    public static <T> LinkedHashSet<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        return Collections.tabulate(n, f, LinkedHashSet.empty(), LinkedHashSet::of);
    }

    /**
     * Returns a LinkedHashSet containing {@code n} values supplied by a given Supplier {@code s}.
     *
     * @param <T> Component type of the LinkedHashSet
     * @param n The number of elements in the LinkedHashSet
     * @param s The Supplier computing element values
     * @return A LinkedHashSet of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    public static <T> LinkedHashSet<T> fill(int n, Supplier<? extends T> s) {
        Objects.requireNonNull(s, "s is null");
        return Collections.fill(n, s, LinkedHashSet.empty(), LinkedHashSet::of);
    }

    /**
     * Creates a LinkedHashSet of the given elements.
     *
     * @param elements Set elements
     * @param <T>      The value type
     * @return A new LinkedHashSet containing the given entries
     */
    @SuppressWarnings("unchecked")
    public static <T> LinkedHashSet<T> ofAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof LinkedHashSet) {
            return (LinkedHashSet<T>) elements;
        } else {
            final LinkedHashMap<T, T> mao = addAll(LinkedHashMap.empty(), elements);
            return mao.isEmpty() ? empty() : new LinkedHashSet<>(mao);
        }
    }

    /**
     * Creates a LinkedHashSet based on the elements of a boolean array.
     *
     * @param array a boolean array
     * @return A new LinkedHashSet of Boolean values
     */
    public static LinkedHashSet<Boolean> ofAll(boolean[] array) {
        Objects.requireNonNull(array, "array is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a LinkedHashSet based on the elements of a byte array.
     *
     * @param array a byte array
     * @return A new LinkedHashSet of Byte values
     */
    public static LinkedHashSet<Byte> ofAll(byte[] array) {
        Objects.requireNonNull(array, "array is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a LinkedHashSet based on the elements of a char array.
     *
     * @param array a char array
     * @return A new LinkedHashSet of Character values
     */
    public static LinkedHashSet<Character> ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a LinkedHashSet based on the elements of a double array.
     *
     * @param array a double array
     * @return A new LinkedHashSet of Double values
     */
    public static LinkedHashSet<Double> ofAll(double[] array) {
        Objects.requireNonNull(array, "array is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a LinkedHashSet based on the elements of a float array.
     *
     * @param array a float array
     * @return A new LinkedHashSet of Float values
     */
    public static LinkedHashSet<Float> ofAll(float[] array) {
        Objects.requireNonNull(array, "array is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a LinkedHashSet based on the elements of an int array.
     *
     * @param array an int array
     * @return A new LinkedHashSet of Integer values
     */
    public static LinkedHashSet<Integer> ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a LinkedHashSet based on the elements of a long array.
     *
     * @param array a long array
     * @return A new LinkedHashSet of Long values
     */
    public static LinkedHashSet<Long> ofAll(long[] array) {
        Objects.requireNonNull(array, "array is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a LinkedHashSet based on the elements of a short array.
     *
     * @param array a short array
     * @return A new LinkedHashSet of Short values
     */
    public static LinkedHashSet<Short> ofAll(short[] array) {
        Objects.requireNonNull(array, "array is null");
        return LinkedHashSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a LinkedHashSet of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.range(0, 0)  // = LinkedHashSet()
     * LinkedHashSet.range(2, 0)  // = LinkedHashSet()
     * LinkedHashSet.range(-2, 2) // = LinkedHashSet(-2, -1, 0, 1)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or the empty range if {@code from >= toExclusive}
     */
    public static LinkedHashSet<Integer> range(int from, int toExclusive) {
        return LinkedHashSet.ofAll(Iterator.range(from, toExclusive));
    }

    public static LinkedHashSet<Character> range(char from, char toExclusive) {
        return LinkedHashSet.ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a LinkedHashSet of int numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.rangeBy(1, 3, 1)  // = LinkedHashSet(1, 2)
     * LinkedHashSet.rangeBy(1, 4, 2)  // = LinkedHashSet(1, 3)
     * LinkedHashSet.rangeBy(4, 1, -2) // = LinkedHashSet(4, 2)
     * LinkedHashSet.rangeBy(4, 1, 2)  // = LinkedHashSet()
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
    public static LinkedHashSet<Integer> rangeBy(int from, int toExclusive, int step) {
        return LinkedHashSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static LinkedHashSet<Character> rangeBy(char from, char toExclusive, int step) {
        return LinkedHashSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static LinkedHashSet<Double> rangeBy(double from, double toExclusive, double step) {
        return LinkedHashSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a LinkedHashSet of long numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.range(0L, 0L)  // = LinkedHashSet()
     * LinkedHashSet.range(2L, 0L)  // = LinkedHashSet()
     * LinkedHashSet.range(-2L, 2L) // = LinkedHashSet(-2L, -1L, 0L, 1L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of long values as specified or the empty range if {@code from >= toExclusive}
     */
    public static LinkedHashSet<Long> range(long from, long toExclusive) {
        return LinkedHashSet.ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a LinkedHashSet of long numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.rangeBy(1L, 3L, 1L)  // = LinkedHashSet(1L, 2L)
     * LinkedHashSet.rangeBy(1L, 4L, 2L)  // = LinkedHashSet(1L, 3L)
     * LinkedHashSet.rangeBy(4L, 1L, -2L) // = LinkedHashSet(4L, 2L)
     * LinkedHashSet.rangeBy(4L, 1L, 2L)  // = LinkedHashSet()
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
    public static LinkedHashSet<Long> rangeBy(long from, long toExclusive, long step) {
        return LinkedHashSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a LinkedHashSet of int numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.rangeClosed(0, 0)  // = LinkedHashSet(0)
     * LinkedHashSet.rangeClosed(2, 0)  // = LinkedHashSet()
     * LinkedHashSet.rangeClosed(-2, 2) // = LinkedHashSet(-2, -1, 0, 1, 2)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or the empty range if {@code from > toInclusive}
     */
    public static LinkedHashSet<Integer> rangeClosed(int from, int toInclusive) {
        return LinkedHashSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    public static LinkedHashSet<Character> rangeClosed(char from, char toInclusive) {
        return LinkedHashSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a LinkedHashSet of int numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.rangeClosedBy(1, 3, 1)  // = LinkedHashSet(1, 2, 3)
     * LinkedHashSet.rangeClosedBy(1, 4, 2)  // = LinkedHashSet(1, 3)
     * LinkedHashSet.rangeClosedBy(4, 1, -2) // = LinkedHashSet(4, 2)
     * LinkedHashSet.rangeClosedBy(4, 1, 2)  // = LinkedHashSet()
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
    public static LinkedHashSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return LinkedHashSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    public static LinkedHashSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return LinkedHashSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    public static LinkedHashSet<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return LinkedHashSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a LinkedHashSet of long numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.rangeClosed(0L, 0L)  // = LinkedHashSet(0L)
     * LinkedHashSet.rangeClosed(2L, 0L)  // = LinkedHashSet()
     * LinkedHashSet.rangeClosed(-2L, 2L) // = LinkedHashSet(-2L, -1L, 0L, 1L, 2L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of long values as specified or the empty range if {@code from > toInclusive}
     */
    public static LinkedHashSet<Long> rangeClosed(long from, long toInclusive) {
        return LinkedHashSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a LinkedHashSet of long numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * LinkedHashSet.rangeClosedBy(1L, 3L, 1L)  // = LinkedHashSet(1L, 2L, 3L)
     * LinkedHashSet.rangeClosedBy(1L, 4L, 2L)  // = LinkedHashSet(1L, 3L)
     * LinkedHashSet.rangeClosedBy(4L, 1L, -2L) // = LinkedHashSet(4L, 2L)
     * LinkedHashSet.rangeClosedBy(4L, 1L, 2L)  // = LinkedHashSet()
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
    public static LinkedHashSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return LinkedHashSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    public LinkedHashSet<T> add(T element) {
        return new LinkedHashSet<>(map.put(element, element));
    }

    @Override
    public LinkedHashSet<T> addAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final LinkedHashMap<T, T> that = addAll(map, elements);
        if (that.size() == map.size()) {
            return this;
        } else {
            return new LinkedHashSet<>(that);
        }
    }

    @Override
    public LinkedHashSet<T> clear() {
        return empty();
    }

    @Override
    public boolean contains(T element) {
        return map.get(element).isDefined();
    }

    @Override
    public LinkedHashSet<T> diff(Set<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (isEmpty() || elements.isEmpty()) {
            return this;
        } else {
            return removeAll(elements);
        }
    }

    @Override
    public LinkedHashSet<T> distinct() {
        return this;
    }

    @Override
    public LinkedHashSet<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return LinkedHashSet.ofAll(iterator().distinctBy(comparator));
    }

    @Override
    public <U> LinkedHashSet<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return LinkedHashSet.ofAll(iterator().distinctBy(keyExtractor));
    }

    @Override
    public LinkedHashSet<T> drop(int n) {
        if (n <= 0) {
            return this;
        } else {
            return LinkedHashSet.ofAll(iterator().drop(n));
        }
    }

    @Override
    public LinkedHashSet<T> dropRight(int n) {
        if (n <= 0) {
            return this;
        } else {
            return LinkedHashSet.ofAll(iterator().dropRight(n));
        }
    }

    @Override
    public LinkedHashSet<T> dropUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @Override
    public LinkedHashSet<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final LinkedHashSet<T> dropped = LinkedHashSet.ofAll(iterator().dropWhile(predicate));
        return dropped.length() == length() ? this : dropped;
    }

    @Override
    public LinkedHashSet<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final LinkedHashSet<T> filtered = LinkedHashSet.ofAll(iterator().filter(predicate));
        return filtered.length() == length() ? this : filtered;
    }

    @Override
    public LinkedHashSet<T> filterNot(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }

    @Override
    public <U> LinkedHashSet<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return empty();
        } else {
            final LinkedHashMap<U, U> that = foldLeft(LinkedHashMap.empty(),
                    (tree, t) -> addAll(tree, mapper.apply(t)));
            return new LinkedHashSet<>(that);
        }
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return iterator().foldRight(zero, f);
    }

    @Override
    public <C> Map<C, LinkedHashSet<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return foldLeft(HashMap.empty(), (map, t) -> {
            final C key = classifier.apply(t);
            final LinkedHashSet<T> values = map.get(key).map(ts -> ts.add(t)).orElse(LinkedHashSet.of(t));
            return map.put(key, values);
        });
    }

    @Override
    public Iterator<LinkedHashSet<T>> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    public boolean hasDefiniteSize() {
        return true;
    }

    @Override
    public T head() {
        if (map.isEmpty()) {
            throw new NoSuchElementException("head of empty set");
        }
        return iterator().next();
    }

    @Override
    public Option<T> headOption() {
        return iterator().headOption();
    }

    @Override
    public LinkedHashSet<T> init() {
        if (map.isEmpty()) {
            throw new UnsupportedOperationException("tail of empty set");
        } else {
            return new LinkedHashSet<>(map.init());
        }
    }

    @Override
    public Option<LinkedHashSet<T>> initOption() {
        if (map.isEmpty()) {
            return Option.none();
        } else {
            return Option.some(init());
        }
    }

    @Override
    public LinkedHashSet<T> intersect(Set<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (isEmpty() || elements.isEmpty()) {
            return empty();
        } else {
            return retainAll(elements);
        }
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean isTraversableAgain() {
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return map.iterator().map(t -> t._1);
    }

    @Override
    public int length() {
        return map.size();
    }

    @Override
    public <U> LinkedHashSet<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return empty();
        } else {
            final LinkedHashMap<U, U> that = foldLeft(LinkedHashMap.empty(), (tree, t) -> {
                final U u = mapper.apply(t);
                return tree.put(u, u);
            });
            return new LinkedHashSet<>(that);
        }
    }

    @Override
    public Match.MatchMonad.Of<LinkedHashSet<T>> match() {
        return Match.of(this);
    }

    @Override
    public String mkString(CharSequence prefix, CharSequence delimiter, CharSequence suffix) {
        return iterator().mkString(prefix, delimiter, suffix);
    }

    @Override
    public Tuple2<LinkedHashSet<T>, LinkedHashSet<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<T>, Iterator<T>> p = iterator().partition(predicate);
        return Tuple.of(LinkedHashSet.ofAll(p._1), LinkedHashSet.ofAll(p._2));
    }

    @Override
    public LinkedHashSet<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(iterator().head());
        }
        return this;
    }

    @Override
    public LinkedHashSet<T> remove(T element) {
        final LinkedHashMap<T, T> newMap = map.remove(element);
        return (newMap == map) ? this : new LinkedHashSet<>(newMap);
    }

    @Override
    public LinkedHashSet<T> removeAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        LinkedHashMap<T, T> that = map;
        for (T element : elements) {
            that = that.remove(element);
        }
        return (that == map) ? this : new LinkedHashSet<>(that);
    }

    @Override
    public LinkedHashSet<T> replace(T currentElement, T newElement) {
        if (map.containsKey(currentElement)) {
            return remove(currentElement).add(newElement);
        } else {
            return this;
        }
    }

    @Override
    public LinkedHashSet<T> replaceAll(T currentElement, T newElement) {
        return replace(currentElement, newElement);
    }

    @Override
    public LinkedHashSet<T> retainAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final LinkedHashMap<T, T> kept = addAll(LinkedHashMap.empty(), elements);
        LinkedHashMap<T, T> that = LinkedHashMap.empty();
        for (T element : this) {
            if (kept.containsKey(element)) {
                that = that.put(element, element);
            }
        }
        return that.isEmpty() ? empty() : that.size() == size() ? this : new LinkedHashSet<>(that);
    }

    @Override
    public LinkedHashSet<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation) {
        return scanLeft(zero, operation);
    }

    @Override
    public <U> LinkedHashSet<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanLeft(this, zero, operation, new java.util.ArrayList<>(), (c, u) -> {
            c.add(u);
            return c;
        }, LinkedHashSet::ofAll);
    }

    @Override
    public <U> LinkedHashSet<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanRight(this, zero, operation, LinkedHashSet.empty(), LinkedHashSet::add, Function.identity());
    }

    @Override
    public Iterator<LinkedHashSet<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    public Iterator<LinkedHashSet<T>> sliding(int size, int step) {
        return iterator().sliding(size, step).map(LinkedHashSet::ofAll);
    }

    @Override
    public Tuple2<LinkedHashSet<T>, LinkedHashSet<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<T>, Iterator<T>> t = iterator().span(predicate);
        return Tuple.of(LinkedHashSet.ofAll(t._1), LinkedHashSet.ofAll(t._2));
    }

    @Override
    public LinkedHashSet<T> tail() {
        if (map.isEmpty()) {
            throw new UnsupportedOperationException("tail of empty set");
        }
        return remove(head());
    }

    @Override
    public Option<LinkedHashSet<T>> tailOption() {
        if (map.isEmpty()) {
            return Option.none();
        } else {
            return Option.some(tail());
        }
    }

    @Override
    public LinkedHashSet<T> take(int n) {
        if (map.size() <= n) {
            return this;
        }
        return LinkedHashSet.ofAll(() -> iterator().take(n));
    }

    @Override
    public LinkedHashSet<T> takeRight(int n) {
        if (map.size() <= n) {
            return this;
        }
        return LinkedHashSet.ofAll(() -> iterator().takeRight(n));
    }

    @Override
    public LinkedHashSet<T> takeUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @Override
    public LinkedHashSet<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        LinkedHashSet<T> taken = LinkedHashSet.ofAll(iterator().takeWhile(predicate));
        return taken.length() == length() ? this : taken;
    }

    @SuppressWarnings("unchecked")
    @Override
    public LinkedHashSet<T> union(Set<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (isEmpty()) {
            if (elements instanceof LinkedHashSet) {
                return (LinkedHashSet<T>) elements;
            } else {
                return LinkedHashSet.ofAll(elements);
            }
        } else if (elements.isEmpty()) {
            return this;
        } else {
            final LinkedHashMap<T, T> that = addAll(map, elements);
            if (that.size() == map.size()) {
                return this;
            } else {
                return new LinkedHashSet<>(that);
            }
        }
    }
    
    @Override
    public <U> LinkedHashSet<U> unit(Iterable<? extends U> iterable) {
    	return LinkedHashSet.ofAll(iterable);
    }

    @Override
    public <T1, T2> Tuple2<LinkedHashSet<T1>, LinkedHashSet<T2>> unzip(
            Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        Tuple2<Iterator<T1>, Iterator<T2>> t = iterator().unzip(unzipper);
        return Tuple.of(LinkedHashSet.ofAll(t._1), LinkedHashSet.ofAll(t._2));
    }

    @Override
    public <T1, T2, T3> Tuple3<LinkedHashSet<T1>, LinkedHashSet<T2>, LinkedHashSet<T3>> unzip3(
            Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        Tuple3<Iterator<T1>, Iterator<T2>, Iterator<T3>> t = iterator().unzip3(unzipper);
        return Tuple.of(LinkedHashSet.ofAll(t._1), LinkedHashSet.ofAll(t._2), LinkedHashSet.ofAll(t._3));
    }

    @Override
    public <U> LinkedHashSet<Tuple2<T, U>> zip(Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        return LinkedHashSet.ofAll(iterator().zip(that));
    }

    @Override
    public <U> LinkedHashSet<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return LinkedHashSet.ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    public LinkedHashSet<Tuple2<T, Integer>> zipWithIndex() {
        return LinkedHashSet.ofAll(iterator().zipWithIndex());
    }

    // -- Object

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof LinkedHashSet) {
            final LinkedHashSet<?> that = (LinkedHashSet<?>) o;
            return this.length() == that.length() && ((LinkedHashSet<Object>) this).containsAll(that);
        } else {
            return false;
        }
    }

    @Override
    public String stringPrefix() {
        return "LinkedHashSet";
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    private static <T> LinkedHashMap<T, T> addAll(LinkedHashMap<T, T> initial,
                                                  Iterable<? extends T> additional) {
        LinkedHashMap<T, T> that = initial;
        for (T t : additional) {
            that = that.put(t, t);
        }
        return that;
    }

    // -- Serialization

    /**
     * {@code writeReplace} method for the serialization proxy pattern.
     * <p>
     * The presence of this method causes the serialization system to emit a SerializationProxy instance instead of
     * an instance of the enclosing class.
     *
     * @return A SerialiationProxy for this enclosing class.
     */
    private Object writeReplace() {
        return new SerializationProxy<>(this.map);
    }

    /**
     * {@code readObject} method for the serialization proxy pattern.
     * <p>
     * Guarantees that the serialization system will never generate a serialized instance of the enclosing class.
     *
     * @param stream An object serialization stream.
     * @throws InvalidObjectException This method will throw with the message "Proxy required".
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
        private transient LinkedHashMap<T, T> map;

        /**
         * Constructor for the case of serialization, called by {@link LinkedHashSet#writeReplace()}.
         * <p/>
         * The constructor of a SerializationProxy takes an argument that concisely represents the logical state of
         * an instance of the enclosing class.
         *
         * @param map a Cons
         */
        SerializationProxy(LinkedHashMap<T, T> map) {
            this.map = map;
        }

        /**
         * Write an object to a serialization stream.
         *
         * @param s An object serialization stream.
         * @throws IOException If an error occurs writing to the stream.
         */
        private void writeObject(ObjectOutputStream s) throws IOException {
            s.defaultWriteObject();
            s.writeInt(map.size());
            for (Tuple2<T, T> e : map) {
                s.writeObject(e._1);
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
            if (size < 0) {
                throw new InvalidObjectException("No elements");
            }
            LinkedHashMap<T, T> temp = LinkedHashMap.empty();
            for (int i = 0; i < size; i++) {
                @SuppressWarnings("unchecked")
                final T element = (T) s.readObject();
                temp = temp.put(element, element);
            }
            map = temp;
        }

        /**
         * {@code readResolve} method for the serialization proxy pattern.
         * <p>
         * Returns a logically equivalent instance of the enclosing class. The presence of this method causes the
         * serialization system to translate the serialization proxy back into an instance of the enclosing class
         * upon deserialization.
         *
         * @return A deserialized instance of the enclosing class.
         */
        private Object readResolve() {
            return map.isEmpty() ? LinkedHashSet.empty() : new LinkedHashSet<>(map);
        }
    }
}
