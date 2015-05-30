/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * A {@code Stack} interface which sits between {@code Seq} and {@code List} for technical reasons.
 *
 * <ul>
 * <li>{@link #peek()}</li>
 * <li>{@link #pop()}</li>
 * <li>{@link #popOption()}</li>
 * <li>{@link #peek()}</li>
 * <li>{@link #peekOption()}</li>
 * <li>{@link #push(Object)}</li>
 * </ul>
 *
 * @param <T> component type
 */
public interface Stack<T> extends Seq<T> {

    /**
     * Returns the head element without modifying the stack.
     *
     * @return the first element
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    T peek();

    /**
     * Returns the head element without modifying the stack.
     *
     * @return {@code None} if this stack is empty, otherwise a {@code Some} containing the head element
     */
    Option<T> peekOption();

    /**
     * Removes the head element from this stack.
     *
     * @return the elements of this stack without the head element
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    Stack<T> pop();

    /**
     * Removes the head element from this stack.
     *
     * @return {@code None} if this stack is empty, otherwise a {@code Some} containing the elements of this stack without the head element
     */
    Option<? extends Stack<T>> popOption();

    /**
     * Removes the head element from this stack.
     *
     * @return a tuple containing the head element and the remaining elements of this stack
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    Tuple2<T, ? extends Stack<T>> pop2();

    /**
     * Removes the head element from this stack.
     *
     * @return {@code None} if this stack is empty, otherwise {@code Some} {@code Tuple} containing the head element and the remaining elements of this stack
     */
    Option<? extends Tuple2<T, ? extends Stack<T>>> pop2Option();

    /**
     * Pushes a new element on top of this stack.
     *
     * @param element The new element
     * @return a new {@code Stack} instance, containing the new element on top of this elements
     */
    Stack<T> push(T element);

    // -- Adjusted return types of Seq methods

    @Override
    Stack<T> append(T element);

    @Override
    Stack<T> appendAll(Iterable<? extends T> elements);

    @Override
    Stack<T> clear();

    @Override
    Stack<? extends Stack<T>> combinations();

    @Override
    Stack<? extends Stack<T>> combinations(int k);

    @Override
    Stack<T> distinct();

    @Override
    <U> Stack<T> distinct(Function<? super T, ? extends U> keyExtractor);

    @Override
    Stack<T> drop(int n);

    @Override
    Stack<T> dropRight(int n);

    @Override
    Stack<T> dropWhile(Predicate<? super T> predicate);

    @Override
    Stack<T> filter(Predicate<? super T> predicate);

    @Override
    Stack<T> findAll(Predicate<? super T> predicate);

    @Override
    <U> Stack<U> flatMap(Function<? super T, ? extends Iterable<U>> mapper);

    @Override
    <U> Stack<U> flatten(Function<? super T, ? extends Iterable<U>> f);

    @Override
    Stack<? extends Stack<T>> grouped(int size);

    @Override
    Stack<T> init();

    @Override
    Option<? extends Stack<T>> initOption();

    @Override
    Stack<T> insert(int index, T element);

    @Override
    Stack<T> insertAll(int index, Iterable<? extends T> elements);

    @Override
    Stack<T> intersperse(T element);

    @Override
    <U> Stack<U> map(Function<? super T, ? extends U> mapper);

    @Override
    Tuple2<? extends Stack<T>, ? extends Stack<T>> partition(Predicate<? super T> predicate);

    @Override
    Stack<T> peek(Consumer<? super T> action);

    @Override
    Stack<? extends Stack<T>> permutations();

    @Override
    Stack<T> prepend(T element);

    @Override
    Stack<T> prependAll(Iterable<? extends T> elements);

    @Override
    Stack<T> remove(T element);

    @Override
    Stack<T> removeAll(T element);

    @Override
    Stack<T> removeAll(Iterable<? extends T> elements);

    @Override
    Stack<T> replace(T currentElement, T newElement);

    @Override
    Stack<T> replaceAll(T currentElement, T newElement);

    @Override
    Stack<T> replaceAll(UnaryOperator<T> operator);

    @Override
    Stack<T> retainAll(Iterable<? extends T> elements);

    @Override
    Stack<T> reverse();

    @Override
    Stack<T> set(int index, T element);

    @Override
    Stack<? extends Stack<T>> sliding(int size);

    @Override
    Stack<? extends Stack<T>> sliding(int size, int step);

    @Override
    Stack<T> sort();

    @Override
    Stack<T> sort(Comparator<? super T> comparator);

    @Override
    Tuple2<? extends Stack<T>, ? extends Stack<T>> span(Predicate<? super T> predicate);

    @Override
    Tuple2<? extends Stack<T>, ? extends Stack<T>> splitAt(int n);

    @Override
    Stack<T> subsequence(int beginIndex);

    @Override
    Stack<T> subsequence(int beginIndex, int endIndex);

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
    <T1, T2> Tuple2<? extends Stack<T1>, ? extends Stack<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <U> Stack<Tuple2<T, U>> zip(Iterable<U> that);

    @Override
    <U> Stack<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem);

    @Override
    Stack<Tuple2<T, Integer>> zipWithIndex();
}
