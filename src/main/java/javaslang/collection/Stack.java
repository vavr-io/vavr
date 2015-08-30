/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.control.Option;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * An immutable {@code Stack} stores elements allowing a last-in-first-out (LIFO) retrieval.
 * <p>
 * Stack API:
 *
 * <ul>
 * <li>{@link #peek()}</li>
 * <li>{@link #peekOption()}</li>
 * <li>{@link #pop()}</li>
 * <li>{@link #popOption()}</li>
 * <li>{@link #pop2()}</li>
 * <li>{@link #pop2Option()}</li>
 * <li>{@link #push(Object)}</li>
 * <li>{@link #push(Object[])}</li>
 * <li>{@link #pushAll(java.lang.Iterable)}</li>
 * </ul>
 *
 * See Okasaki, Chris: <em>Purely Functional Data Structures</em> (p. 7 ff.). Cambridge, 2003.
 *
 * @param <T> component type
 * @since 2.0.0
 */
public interface Stack<T> extends LinearSeq<T> {

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link javaslang.collection.Stack}
     * .
     *
     * @param <T> Component type of the Stack.
     * @return A javaslang.collection.Stack Collector.
     */
    static <T> Collector<T, ArrayList<T>, Stack<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, Stack<T>> finisher = Stack::ofAll;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Returns the empty Stack.
     *
     * @param <T> Component type
     * @return The empty Stack.
     */
    static <T> Stack<T> empty() {
        return List.empty();
    }

    /**
     * Returns a singleton {@code Stack}, i.e. a {@code Stack} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new Stack instance containing the given element
     */
    static <T> Stack<T> of(T element) {
        return List.of(element);
    }

    /**
     * <p>
     * Creates a Stack of the given elements.
     * </p>
     *
     * @param <T>      Component type of the Stack.
     * @param elements Zero or more elements.
     * @return A stack containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    @SuppressWarnings({ "unchecked", "varargs" })
    static <T> Stack<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return List.of(elements);
    }

    /**
     * Creates a Stack of the given elements.
     *
     * @param <T>      Component type of the Stack.
     * @param elements An java.lang.Iterable of elements.
     * @return A stack containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("unchecked")
    static <T> Stack<T> ofAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof Stack) {
            return (Stack<T>) elements;
        } else {
            return List.ofAll(elements);
        }
    }

    /**
     * Creates a Stack based on the elements of a boolean array.
     *
     * @param array a boolean array
     * @return A new Stack of Boolean values
     */
    static Stack<Boolean> ofAll(boolean[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(array);
    }

    /**
     * Creates a Stack based on the elements of a byte array.
     *
     * @param array a byte array
     * @return A new Stack of Byte values
     */
    static Stack<Byte> ofAll(byte[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(array);
    }

    /**
     * Creates a Stack based on the elements of a char array.
     *
     * @param array a char array
     * @return A new Stack of Character values
     */
    static Stack<Character> ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(array);
    }

    /**
     * Creates a Stack based on the elements of a double array.
     *
     * @param array a double array
     * @return A new Stack of Double values
     */
    static Stack<Double> ofAll(double[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(array);
    }

    /**
     * Creates a Stack based on the elements of a float array.
     *
     * @param array a float array
     * @return A new Stack of Float values
     */
    static Stack<Float> ofAll(float[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(array);
    }

    /**
     * Creates a Stack based on the elements of an int array.
     *
     * @param array an int array
     * @return A new Stack of Integer values
     */
    static Stack<Integer> ofAll(int[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(array);
    }

    /**
     * Creates a Stack based on the elements of a long array.
     *
     * @param array a long array
     * @return A new Stack of Long values
     */
    static Stack<Long> ofAll(long[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(array);
    }

    /**
     * Creates a Stack based on the elements of a short array.
     *
     * @param array a short array
     * @return A new Stack of Short values
     */
    static Stack<Short> ofAll(short[] array) {
        Objects.requireNonNull(array, "array is null");
        return List.ofAll(array);
    }

    /**
     * Creates a Stack of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stack.range(0, 0)  // = Stack()
     * Stack.range(2, 0)  // = Stack()
     * Stack.range(-2, 2) // = Stack(-2, -1, 0, 1)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or the empty range if {@code from >= toExclusive}
     */
    static Stack<Integer> range(int from, int toExclusive) {
        return List.range(from, toExclusive);
    }

    /**
     * Creates a Stack of int numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stack.rangeBy(1, 3, 1)  // = Stack(1, 2)
     * Stack.rangeBy(1, 4, 2)  // = Stack(1, 3)
     * Stack.rangeBy(4, 1, -2) // = Stack(4, 2)
     * Stack.rangeBy(4, 1, 2)  // = Stack()
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
    static Stack<Integer> rangeBy(int from, int toExclusive, int step) {
        return List.rangeBy(from, toExclusive, step);
    }

    /**
     * Creates a Stack of long numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stack.range(0L, 0L)  // = Stack()
     * Stack.range(2L, 0L)  // = Stack()
     * Stack.range(-2L, 2L) // = Stack(-2L, -1L, 0L, 1L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of long values as specified or the empty range if {@code from >= toExclusive}
     */
    static Stack<Long> range(long from, long toExclusive) {
        return List.range(from, toExclusive);
    }

    /**
     * Creates a Stack of long numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stack.rangeBy(1L, 3L, 1L)  // = Stack(1L, 2L)
     * Stack.rangeBy(1L, 4L, 2L)  // = Stack(1L, 3L)
     * Stack.rangeBy(4L, 1L, -2L) // = Stack(4L, 2L)
     * Stack.rangeBy(4L, 1L, 2L)  // = Stack()
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
    static Stack<Long> rangeBy(long from, long toExclusive, long step) {
        return List.rangeBy(from, toExclusive, step);
    }

    /**
     * Creates a Stack of int numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stack.rangeClosed(0, 0)  // = Stack(0)
     * Stack.rangeClosed(2, 0)  // = Stack()
     * Stack.rangeClosed(-2, 2) // = Stack(-2, -1, 0, 1, 2)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or the empty range if {@code from > toInclusive}
     */
    static Stack<Integer> rangeClosed(int from, int toInclusive) {
        return List.rangeClosed(from, toInclusive);
    }

    /**
     * Creates a Stack of int numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stack.rangeClosedBy(1, 3, 1)  // = Stack(1, 2, 3)
     * Stack.rangeClosedBy(1, 4, 2)  // = Stack(1, 3)
     * Stack.rangeClosedBy(4, 1, -2) // = Stack(4, 2)
     * Stack.rangeClosedBy(4, 1, 2)  // = Stack()
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
    static Stack<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return List.rangeClosedBy(from, toInclusive, step);
    }

    /**
     * Creates a Stack of long numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stack.rangeClosed(0L, 0L)  // = Stack(0L)
     * Stack.rangeClosed(2L, 0L)  // = Stack()
     * Stack.rangeClosed(-2L, 2L) // = Stack(-2L, -1L, 0L, 1L, 2L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of long values as specified or the empty range if {@code from > toInclusive}
     */
    static Stack<Long> rangeClosed(long from, long toInclusive) {
        return List.rangeClosed(from, toInclusive);
    }

    /**
     * Creates a Stack of long numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * Stack.rangeClosedBy(1L, 3L, 1L)  // = Stack(1L, 2L, 3L)
     * Stack.rangeClosedBy(1L, 4L, 2L)  // = Stack(1L, 3L)
     * Stack.rangeClosedBy(4L, 1L, -2L) // = Stack(4L, 2L)
     * Stack.rangeClosedBy(4L, 1L, 2L)  // = Stack()
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
    static Stack<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return List.rangeClosedBy(from, toInclusive, step);
    }

    /**
     * Returns the head element without modifying the Stack.
     *
     * @return the first element
     * @throws java.util.NoSuchElementException if this Stack is empty
     */
    T peek();

    /**
     * Returns the head element without modifying the Stack.
     *
     * @return {@code None} if this Stack is empty, otherwise a {@code Some} containing the head element
     */
    Option<T> peekOption();

    /**
     * Removes the head element from this Stack.
     *
     * @return the elements of this Stack without the head element
     * @throws java.util.NoSuchElementException if this Stack is empty
     */
    Stack<T> pop();

    /**
     * Removes the head element from this Stack.
     *
     * @return {@code None} if this Stack is empty, otherwise a {@code Some} containing the elements of this Stack without the head element
     */
    Option<? extends Stack<T>> popOption();

    /**
     * Removes the head element from this Stack.
     *
     * @return a tuple containing the head element and the remaining elements of this Stack
     * @throws java.util.NoSuchElementException if this Stack is empty
     */
    Tuple2<T, ? extends Stack<T>> pop2();

    /**
     * Removes the head element from this Stack.
     *
     * @return {@code None} if this Stack is empty, otherwise {@code Some} {@code Tuple} containing the head element and the remaining elements of this Stack
     */
    Option<? extends Tuple2<T, ? extends Stack<T>>> pop2Option();

    /**
     * Pushes a new element on top of this Stack.
     *
     * @param element The new element
     * @return a new {@code Stack} instance, containing the new element on top of this Stack
     */
    Stack<T> push(T element);

    /**
     * Pushes the given elements on top of this Stack. A Stack has LIFO order, i.e. the last of the given elements is
     * the first which will be retrieved.
     *
     * @param elements Elements, may be empty
     * @return a new {@code Stack} instance, containing the new elements on top of this Stack
     * @throws NullPointerException if elements is null
     */
    @SuppressWarnings("unchecked")
    Stack<T> push(T... elements);

    /**
     * Pushes the given elements on top of this Stack. A Stack has LIFO order, i.e. the last of the given elements is
     * the first which will be retrieved.
     *
     * @param elements An java.lang.Iterable of elements, may be empty
     * @return a new {@code Stack} instance, containing the new elements on top of this Stack
     * @throws NullPointerException if elements is null
     */
    Stack<T> pushAll(java.lang.Iterable<T> elements);

    // -- Adjusted return types of Seq methods

    @Override
    Stack<T> append(T element);

    @Override
    Stack<T> appendAll(java.lang.Iterable<? extends T> elements);

    @Override
    Stack<Tuple2<T, T>> crossProduct();

    @Override
    <U> Stack<Tuple2<T, U>> crossProduct(java.lang.Iterable<? extends U> that);

    @Override
    Stack<T> clear();

    @Override
    Stack<? extends Stack<T>> combinations();

    @Override
    Stack<? extends Stack<T>> combinations(int k);

    @Override
    Stack<T> distinct();

    @Override
    Stack<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U> Stack<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    Stack<T> drop(int n);

    @Override
    Stack<T> dropRight(int n);

    @Override
    Stack<T> dropWhile(Predicate<? super T> predicate);

    @Override
    Stack<T> filter(Predicate<? super T> predicate);

    @Override
    <U> Stack<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper);

    @Override
    <U> Stack<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper);

    @Override
    Stack<Object> flatten();

    @Override
    <C> Map<C, ? extends Stack<T>> groupBy(Function<? super T, ? extends C> classifier);

    @Override
    Stack<T> init();

    @Override
    Option<? extends Stack<T>> initOption();

    @Override
    Stack<T> insert(int index, T element);

    @Override
    Stack<T> insertAll(int index, java.lang.Iterable<? extends T> elements);

    @Override
    Stack<T> intersperse(T element);

    @Override
    <U> Stack<U> map(Function<? super T, ? extends U> mapper);

    @Override
    Stack<T> padTo(int length, T element);

    @Override
    Tuple2<? extends Stack<T>, ? extends Stack<T>> partition(Predicate<? super T> predicate);

    @Override
    Stack<T> peek(Consumer<? super T> action);

    @Override
    Stack<? extends Stack<T>> permutations();

    @Override
    Stack<T> prepend(T element);

    @Override
    Stack<T> prependAll(java.lang.Iterable<? extends T> elements);

    @Override
    Stack<T> remove(T element);

    @Override
    Stack<T> removeFirst(Predicate<T> predicate);

    @Override
    Stack<T> removeLast(Predicate<T> predicate);

    @Override
    Stack<T> removeAt(int indx);

    @Override
    Stack<T> removeAll(T element);

    @Override
    Stack<T> removeAll(java.lang.Iterable<? extends T> elements);

    @Override
    Stack<T> replace(T currentElement, T newElement);

    @Override
    Stack<T> replaceAll(T currentElement, T newElement);

    @Override
    Stack<T> replaceAll(UnaryOperator<T> operator);

    @Override
    Stack<T> retainAll(java.lang.Iterable<? extends T> elements);

    @Override
    Stack<T> reverse();

    @Override
    Stack<T> set(int index, T element);

    @Override
    Stack<T> slice(int beginIndex);

    @Override
    Stack<T> slice(int beginIndex, int endIndex);

    @Override
    Stack<T> sort();

    @Override
    Stack<T> sort(Comparator<? super T> comparator);

    @Override
    Tuple2<? extends Stack<T>, ? extends Stack<T>> span(Predicate<? super T> predicate);

    @Override
    Tuple2<? extends Stack<T>, ? extends Stack<T>> splitAt(int n);

    @Override
    Tuple2<? extends Stack<T>, ? extends Stack<T>> splitAt(Predicate<? super T> predicate);

    @Override
    Tuple2<? extends Stack<T>, ? extends Stack<T>> splitAtInclusive(Predicate<? super T> predicate);

    @Override
    Stack<T> tail();

    @Override
    Option<? extends Stack<T>> tailOption();

    @Override
    Stack<T> take(int n);

    @Override
    Stack<T> takeRight(int n);

    @Override
    Stack<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <U> Stack<U> unit(java.lang.Iterable<? extends U> iterable);

    @Override
    <T1, T2> Tuple2<? extends Stack<T1>, ? extends Stack<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <U> Stack<Tuple2<T, U>> zip(java.lang.Iterable<U> that);

    @Override
    <U> Stack<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem);

    @Override
    Stack<Tuple2<T, Integer>> zipWithIndex();
}
