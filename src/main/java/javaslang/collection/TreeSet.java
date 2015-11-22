/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.collection.Comparators.naturalComparator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

/**
 * SortedSet implementation, backed by a Red/Black Tree.
 *
 * @param <T> Component type
 * @author Daniel Dietrich
 * @since 2.0.0
 */
// DEV-NOTE: it is not possible to create an EMPTY TreeSet without a Comparator type in scope
public final class TreeSet<T> implements SortedSet<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private final RedBlackTree<T> tree;

    TreeSet(RedBlackTree<T> tree) {
        this.tree = tree;
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.TreeSet}.
     *
     * @param <T> Component type of the List.
     * @return A javaslang.collection.List Collector.
     */
    public static <T> Collector<T, ArrayList<T>, TreeSet<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, TreeSet<T>> finisher = list -> TreeSet.ofAll(naturalComparator(), list);
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    public static <T extends Comparable<? super T>> TreeSet<T> empty() {
        return new TreeSet<>(RedBlackTree.<T> empty());
    }

    public static <T> TreeSet<T> empty(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return new TreeSet<>(RedBlackTree.empty(comparator));
    }

    public static <T extends Comparable<? super T>> TreeSet<T> of(T value) {
        return new TreeSet<>(RedBlackTree.of(value));
    }

    public static <T> TreeSet<T> of(Comparator<? super T> comparator, T value) {
        Objects.requireNonNull(comparator, "comparator is null");
        return new TreeSet<>(RedBlackTree.of(comparator, value));
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T extends Comparable<? super T>> TreeSet<T> ofAll(T... values) {
        Objects.requireNonNull(values, "values is null");
        return new TreeSet<>(RedBlackTree.ofAll(values));
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> TreeSet<T> ofAll(Comparator<? super T> comparator, T... values) {
        Objects.requireNonNull(comparator, "comparator is null");
        Objects.requireNonNull(values, "values is null");
        return new TreeSet<>(RedBlackTree.ofAll(comparator, values));
    }

    public static <T extends Comparable<? super T>> TreeSet<T> ofAll(java.lang.Iterable<? extends T> values) {
        Objects.requireNonNull(values, "values is null");
        return values.iterator().hasNext() ? new TreeSet<>(RedBlackTree.ofAll(values)) : empty();
    }

    @SuppressWarnings("unchecked")
    public static <T> TreeSet<T> ofAll(Comparator<? super T> comparator, java.lang.Iterable<? extends T> values) {
        Objects.requireNonNull(comparator, "comparator is null");
        Objects.requireNonNull(values, "values is null");
        return values.iterator().hasNext()
                ? new TreeSet<>(RedBlackTree.ofAll(comparator, values))
                : (TreeSet<T>) empty();
    }

    /**
     * Creates a TreeSet based on the elements of a boolean array.
     *
     * @param array a boolean array
     * @return A new TreeSet of Boolean values
     */
    public static TreeSet<Boolean> ofAll(boolean[] array) {
        Objects.requireNonNull(array, "array is null");
        return TreeSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a TreeSet based on the elements of a byte array.
     *
     * @param array a byte array
     * @return A new TreeSet of Byte values
     */
    public static TreeSet<Byte> ofAll(byte[] array) {
        Objects.requireNonNull(array, "array is null");
        return TreeSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a TreeSet based on the elements of a char array.
     *
     * @param array a char array
     * @return A new TreeSet of Character values
     */
    public static TreeSet<Character> ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return TreeSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a TreeSet based on the elements of a double array.
     *
     * @param array a double array
     * @return A new TreeSet of Double values
     */
    public static TreeSet<Double> ofAll(double[] array) {
        Objects.requireNonNull(array, "array is null");
        return TreeSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a TreeSet based on the elements of a float array.
     *
     * @param array a float array
     * @return A new TreeSet of Float values
     */
    public static TreeSet<Float> ofAll(float[] array) {
        Objects.requireNonNull(array, "array is null");
        return TreeSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a TreeSet based on the elements of an int array.
     *
     * @param array an int array
     * @return A new TreeSet of Integer values
     */
    public static TreeSet<Integer> ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return TreeSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a TreeSet based on the elements of a long array.
     *
     * @param array a long array
     * @return A new TreeSet of Long values
     */
    public static TreeSet<Long> ofAll(long[] array) {
        Objects.requireNonNull(array, "array is null");
        return TreeSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a TreeSet based on the elements of a short array.
     *
     * @param array a short array
     * @return A new TreeSet of Short values
     */
    public static TreeSet<Short> ofAll(short[] array) {
        Objects.requireNonNull(array, "array is null");
        return TreeSet.ofAll(Iterator.ofAll(array));
    }

    /**
     * Creates a TreeSet of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * TreeSet.range(0, 0)  // = TreeSet()
     * TreeSet.range(2, 0)  // = TreeSet()
     * TreeSet.range(-2, 2) // = TreeSet(-2, -1, 0, 1)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or the empty range if {@code from >= toExclusive}
     */
    public static TreeSet<Integer> range(int from, int toExclusive) {
        return TreeSet.ofAll(Iterator.range(from, toExclusive));
    }

    public static TreeSet<Character> range(char from, char toExclusive) {
        return TreeSet.ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a TreeSet of int numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * TreeSet.rangeBy(1, 3, 1)  // = TreeSet(1, 2)
     * TreeSet.rangeBy(1, 4, 2)  // = TreeSet(1, 3)
     * TreeSet.rangeBy(4, 1, -2) // = TreeSet(4, 2)
     * TreeSet.rangeBy(4, 1, 2)  // = TreeSet()
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
    public static TreeSet<Integer> rangeBy(int from, int toExclusive, int step) {
        return TreeSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static TreeSet<Character> rangeBy(char from, char toExclusive, int step) {
        return TreeSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static TreeSet<Double> rangeBy(double from, double toExclusive, double step) {
        return TreeSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a TreeSet of long numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * TreeSet.range(0L, 0L)  // = TreeSet()
     * TreeSet.range(2L, 0L)  // = TreeSet()
     * TreeSet.range(-2L, 2L) // = TreeSet(-2L, -1L, 0L, 1L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of long values as specified or the empty range if {@code from >= toExclusive}
     */
    public static TreeSet<Long> range(long from, long toExclusive) {
        return TreeSet.ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a TreeSet of long numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * TreeSet.rangeBy(1L, 3L, 1L)  // = TreeSet(1L, 2L)
     * TreeSet.rangeBy(1L, 4L, 2L)  // = TreeSet(1L, 3L)
     * TreeSet.rangeBy(4L, 1L, -2L) // = TreeSet(4L, 2L)
     * TreeSet.rangeBy(4L, 1L, 2L)  // = TreeSet()
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
    public static TreeSet<Long> rangeBy(long from, long toExclusive, long step) {
        return TreeSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a TreeSet of int numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * TreeSet.rangeClosed(0, 0)  // = TreeSet(0)
     * TreeSet.rangeClosed(2, 0)  // = TreeSet()
     * TreeSet.rangeClosed(-2, 2) // = TreeSet(-2, -1, 0, 1, 2)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or the empty range if {@code from > toInclusive}
     */
    public static TreeSet<Integer> rangeClosed(int from, int toInclusive) {
        return TreeSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    public static TreeSet<Character> rangeClosed(char from, char toInclusive) {
        return TreeSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a TreeSet of int numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * TreeSet.rangeClosedBy(1, 3, 1)  // = TreeSet(1, 2, 3)
     * TreeSet.rangeClosedBy(1, 4, 2)  // = TreeSet(1, 3)
     * TreeSet.rangeClosedBy(4, 1, -2) // = TreeSet(4, 2)
     * TreeSet.rangeClosedBy(4, 1, 2)  // = TreeSet()
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
    public static TreeSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return TreeSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    public static TreeSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return TreeSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    public static TreeSet<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return TreeSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a TreeSet of long numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * TreeSet.rangeClosed(0L, 0L)  // = TreeSet(0L)
     * TreeSet.rangeClosed(2L, 0L)  // = TreeSet()
     * TreeSet.rangeClosed(-2L, 2L) // = TreeSet(-2L, -1L, 0L, 1L, 2L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of long values as specified or the empty range if {@code from > toInclusive}
     */
    public static TreeSet<Long> rangeClosed(long from, long toInclusive) {
        return TreeSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a TreeSet of long numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * TreeSet.rangeClosedBy(1L, 3L, 1L)  // = TreeSet(1L, 2L, 3L)
     * TreeSet.rangeClosedBy(1L, 4L, 2L)  // = TreeSet(1L, 3L)
     * TreeSet.rangeClosedBy(4L, 1L, -2L) // = TreeSet(4L, 2L)
     * TreeSet.rangeClosedBy(4L, 1L, 2L)  // = TreeSet()
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
    public static TreeSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return TreeSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    public TreeSet<T> add(T element) {
        return new TreeSet<>(tree.insert(element));
    }

    @Override
    public TreeSet<T> addAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        RedBlackTree<T> that = tree;
        for (T element : elements) {
            that = that.insert(element);
        }
        if (tree == that) {
            return this;
        } else {
            return new TreeSet<>(that);
        }
    }

    @Override
    public TreeSet<T> clear() {
        return isEmpty() ? this : new TreeSet<>(tree.clear());
    }

    @Override
    public Comparator<T> comparator() {
        return tree.comparator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public TreeSet<T> diff(Set<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof TreeSet) {
            final RedBlackTree<T> that = ((TreeSet<T>) elements).tree;
            return new TreeSet<>(tree.difference(that));
        } else {
            return removeAll(elements);
        }
    }

    @Override
    public boolean contains(T element) {
        return tree.contains(element);
    }

    @Override
    public TreeSet<T> distinct() {
        return this;
    }

    @Override
    public TreeSet<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return TreeSet.ofAll(tree.comparator(), iterator().distinctBy(comparator));
    }

    @Override
    public <U> TreeSet<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return TreeSet.ofAll(tree.comparator(), iterator().distinctBy(keyExtractor));
    }

    @Override
    public TreeSet<T> drop(int n) {
        if (n <= 0) {
            return this;
        } else if (n >= length()) {
            return empty(tree.comparator());
        } else {
            return TreeSet.ofAll(tree.comparator(), iterator().drop(n));
        }
    }

    @Override
    public TreeSet<T> dropRight(int n) {
        if (n <= 0) {
            return this;
        } else if (n >= length()) {
            return empty(tree.comparator());
        } else {
            return TreeSet.ofAll(tree.comparator(), iterator().dropRight(n));
        }
    }

    @Override
    public TreeSet<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final TreeSet<T> treeSet = TreeSet.ofAll(tree.comparator(), iterator().dropWhile(predicate));
        return (treeSet.length() == length()) ? this : treeSet;
    }

    @Override
    public TreeSet<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final TreeSet<T> treeSet = TreeSet.ofAll(tree.comparator(), iterator().filter(predicate));
        return (treeSet.length() == length()) ? this : treeSet;
    }

    @Override
    public <U> TreeSet<U> flatMap(Comparator<? super U> comparator,
                                  Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return TreeSet.ofAll(comparator, iterator().flatMap(mapper));
    }

    @Override
    public <U> TreeSet<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
        return flatMap(naturalComparator(), mapper);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> TreeSet<U> flatten() {
        try {
            return ((TreeSet<? extends Iterable<U>>) this).flatMap(Function.identity());
        } catch (ClassCastException x) {
            throw new UnsupportedOperationException("flatten of non-iterable elements");
        }
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return iterator().foldRight(zero, f);
    }

    @Override
    public <C> Map<C, TreeSet<T>> groupBy(Function<? super T, ? extends C> classifier) {
        Objects.requireNonNull(classifier, "classifier is null");
        return iterator().groupBy(classifier).map(
                (key, iterator) -> Tuple.of(key, TreeSet.ofAll(tree.comparator(), iterator)));
    }

    @Override
    public boolean hasDefiniteSize() {
        return true;
    }

    @Override
    public T head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head of empty TreeSet");
        } else {
            return tree.min().get();
        }
    }

    @Override
    public Option<T> headOption() {
        return tree.min();
    }

    @Override
    public TreeSet<T> init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init of empty TreeSet");
        } else {
            return new TreeSet<>(tree.delete(tree.max().get()));
        }
    }

    @Override
    public Option<TreeSet<T>> initOption() {
        return isEmpty() ? None.instance() : new Some<>(init());
    }

    @SuppressWarnings("unchecked")
    @Override
    public TreeSet<T> intersect(Set<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof TreeSet) {
            final RedBlackTree<T> that = ((TreeSet<T>) elements).tree;
            return new TreeSet<>(tree.intersection(that));
        } else {
            return retainAll(elements);
        }
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public boolean isTraversableAgain() {
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return tree.iterator();
    }

    @Override
    public int length() {
        return tree.size();
    }

    @Override
    public <U> TreeSet<U> map(Comparator<? super U> comparator, Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return TreeSet.ofAll(comparator, iterator().map(mapper));
    }

    @Override
    public <U> TreeSet<U> map(Function<? super T, ? extends U> mapper) {
        return map(naturalComparator(), mapper);
    }

    @Override
    public Option<T> max() {
        return tree.max();
    }

    @Override
    public Option<T> min() {
        return tree.min();
    }

    @Override
    public Tuple2<TreeSet<T>, TreeSet<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return iterator().partition(predicate).map(i1 -> TreeSet.ofAll(tree.comparator(), i1),
                i2 -> TreeSet.ofAll(tree.comparator(), i2));
    }

    @Override
    public TreeSet<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(head());
        }
        return this;
    }

    @Override
    public TreeSet<T> remove(T element) {
        return new TreeSet<>(tree.delete(element));
    }

    @Override
    public TreeSet<T> removeAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (isEmpty()) {
            return this;
        } else {
            RedBlackTree<T> that = tree;
            final java.util.Iterator<? extends T> iter = elements.iterator();
            while (!that.isEmpty() && iter.hasNext()) {
                that = that.delete(iter.next());
            }
            if (that == tree) {
                return this;
            } else {
                return new TreeSet<>(that);
            }
        }
    }

    @Override
    public TreeSet<T> replace(T currentElement, T newElement) {
        if (tree.contains(currentElement)) {
            return new TreeSet<>(tree.delete(currentElement).insert(newElement));
        } else {
            return this;
        }
    }

    @Override
    public TreeSet<T> replaceAll(T currentElement, T newElement) {
        // a set has only one occurrence
        return replace(currentElement, newElement);
    }

    @Override
    public TreeSet<T> retainAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (isEmpty()) {
            return this;
        } else {
            final RedBlackTree<T> kept = RedBlackTree.ofAll(tree.comparator(), elements);
            return new TreeSet<>(tree.intersection(kept));
        }
    }

    @Override
    public TreeSet<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation) {
        return (TreeSet<T>) scanLeft(zero, operation);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <U> Set<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        if(zero instanceof Comparable) {
            List<Comparable> l = (List<Comparable>) iterator().scanLeft(zero, operation);            
            return (TreeSet<U>) TreeSet.ofAll(l);
        } else {
            List<U> l = iterator().scanLeft(zero, operation);
            return HashSet.ofAll(l);
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <U> Set<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        if(zero instanceof Comparable) {
            List<Comparable> l = (List<Comparable>) iterator().scanRight(zero, operation);            
            return (TreeSet<U>) TreeSet.ofAll(l);
        } else {
            List<U> l = iterator().scanRight(zero, operation);
            return HashSet.ofAll(l);
        }
    }
    
    @Override
    public Tuple2<TreeSet<T>, TreeSet<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return iterator().span(predicate).map(i1 -> TreeSet.ofAll(tree.comparator(), i1),
                i2 -> TreeSet.ofAll(tree.comparator(), i2));
    }

    @Override
    public TreeSet<T> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty TreeSet");
        } else {
            return new TreeSet<>(tree.delete(tree.min().get()));
        }
    }

    @Override
    public Option<TreeSet<T>> tailOption() {
        return isEmpty() ? None.instance() : new Some<>(tail());
    }

    @Override
    public TreeSet<T> take(int n) {
        if (n <= 0) {
            return empty(tree.comparator());
        } else if (n >= length()) {
            return this;
        } else {
            return TreeSet.ofAll(tree.comparator(), iterator().take(n));
        }
    }

    @Override
    public TreeSet<T> takeRight(int n) {
        if (n <= 0) {
            return empty(tree.comparator());
        } else if (n >= length()) {
            return this;
        } else {
            return TreeSet.ofAll(tree.comparator(), iterator().takeRight(n));
        }
    }

    @Override
    public TreeSet<T> takeUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final TreeSet<T> treeSet = takeWhile(predicate.negate());
        return (treeSet.length() == length()) ? this : treeSet;
    }

    @Override
    public TreeSet<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final TreeSet<T> treeSet = TreeSet.ofAll(tree.comparator(), iterator().takeWhile(predicate));
        return (treeSet.length() == length()) ? this : treeSet;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TreeSet<T> union(Set<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof TreeSet) {
            final RedBlackTree<T> that = ((TreeSet<T>) elements).tree;
            return new TreeSet<>(tree.union(that));
        } else {
            return addAll(elements);
        }
    }

    @Override
    public <T1, T2> Tuple2<TreeSet<T1>, TreeSet<T2>> unzip(
            Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip(unzipper).map(i1 -> TreeSet.ofAll(naturalComparator(), i1),
                i2 -> TreeSet.ofAll(naturalComparator(), i2));
    }

    @Override
    public <T1, T2, T3> Tuple3<TreeSet<T1>, TreeSet<T2>, TreeSet<T3>> unzip3(
            Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip3(unzipper).map(
        		i1 -> TreeSet.ofAll(naturalComparator(), i1),
                i2 -> TreeSet.ofAll(naturalComparator(), i2),
                i3 -> TreeSet.ofAll(naturalComparator(), i3));
    }

    @Override
    public <U> TreeSet<Tuple2<T, U>> zip(java.lang.Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        final Comparator<Tuple2<T, U>> tuple2Comparator = Tuple2.comparator(tree.comparator(), naturalComparator());
        return TreeSet.ofAll(tuple2Comparator, iterator().zip(that));
    }

    @Override
    public <U> TreeSet<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        final Comparator<Tuple2<T, U>> tuple2Comparator = Tuple2.comparator(tree.comparator(), naturalComparator());
        return TreeSet.ofAll(tuple2Comparator, iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    public TreeSet<Tuple2<T, Integer>> zipWithIndex() {
        final Comparator<? super T> component1Comparator = tree.comparator();
        final Comparator<Tuple2<T, Integer>> tuple2Comparator = (t1, t2) -> component1Comparator.compare(t1._1, t2._1);
        return TreeSet.ofAll(tuple2Comparator, iterator().zipWithIndex());
    }

    // -- Object

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof TreeSet) {
            final TreeSet<?> that = (TreeSet<?>) o;
            return tree.equals(that.tree);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return tree.hashCode();
    }

    @Override
    public String toString() {
        return mkString("TreeSet(", ", ", ")");
    }
}
