/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * An immutable {@code Set} interface.
 *
 * @param <T> Component type
 * @author Daniel Dietrich, Ruslan Sennov
 * @since 2.0.0
 */
public interface Set<T> extends Traversable<T> {

    /**
     * Add the given element to this set, if it is not already contained.
     *
     * @param element The element to be added.
     * @return A new set containing all elements of this set and also {@code element}.
     */
    Set<T> add(T element);

    /**
     * Adds all of the given elements to this set, if not already contained.
     *
     * @param elements The elements to be added.
     * @return A new set containing all elements of this set and the given {@code elements}, if not already contained.
     */
    Set<T> addAll(java.lang.Iterable<? extends T> elements);

    /**
     * Calculates the difference between this set and another set.
     * <p>
     * See also {@link #removeAll(Iterable)}.
     *
     * @param that Elements to be removed from this set.
     * @return A new Set containing all elements of this set which are not located in {@code that} set.
     */
    Set<T> diff(Set<? extends T> that);

    /**
     * Computes the intersection between this set and another set.
     * <p>
     * See also {@link #retainAll(Iterable)}.
     *
     * @param that the set to intersect with.
     * @return A new Set consisting of all elements that are both in this set and in the given set {@code that}.
     */
    Set<T> intersect(Set<? extends T> that);

    /**
     * Removes a specific element from this set, if present.
     *
     * @param element The element to be removed from this set.
     * @return A new set consisting of the elements of this set, without the given {@code element}.
     */
    Set<T> remove(T element);

    /**
     * Removes all of the given elements from this set, if present.
     *
     * @param elements The elements to be removed from this set.
     * @return A new set consisting of the elements of this set, without the given {@code elements}.
     */
    Set<T> removeAll(java.lang.Iterable<? extends T> elements);

    /**
     * Adds all of the elements of {@code that} set to this set, if not already present.
     * <p>
     * See also {@link #addAll(Iterable)}.
     *
     * @param that The set to form the union with.
     * @return A new set that contains all distinct elements of this and {@code that} set.
     */
    Set<T> union(Set<? extends T> that);

    // -- Adjusted return types of Traversable methods

    @Override
    Set<T> clear();

    @Override
    boolean contains(T element);

    @Override
    Set<T> distinct();

    @Override
    Set<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U> Set<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    Set<T> drop(int n);

    @Override
    Set<T> dropRight(int n);

    @Override
    Set<T> dropWhile(Predicate<? super T> predicate);

    @Override
    Set<T> filter(Predicate<? super T> predicate);

    @Override
    <U> Set<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper);

    @Override
    Set<Object> flatten();

    @Override
    <C> Map<C, ? extends Set<T>> groupBy(Function<? super T, ? extends C> classifier);

    @Override
    Set<T> init();

    @Override
    Option<? extends Set<T>> initOption();

    @Override
    Iterator<T> iterator();

    @Override
    int length();

    @Override
    <U> Set<U> map(Function<? super T, ? extends U> mapper);

    @Override
    Tuple2<? extends Set<T>, ? extends Set<T>> partition(Predicate<? super T> predicate);

    @Override
    Set<T> peek(Consumer<? super T> action);

    @Override
    Set<T> replace(T currentElement, T newElement);

    @Override
    Set<T> replaceAll(T currentElement, T newElement);

    @Override
    Set<T> replaceAll(UnaryOperator<T> operator);

    @Override
    Set<T> retainAll(java.lang.Iterable<? extends T> elements);

    @Override
    Tuple2<? extends Set<T>, ? extends Set<T>> span(Predicate<? super T> predicate);

    @Override
    default Spliterator<T> spliterator() {
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    Set<T> tail();

    @Override
    Option<? extends Set<T>> tailOption();

    @Override
    Set<T> take(int n);

    @Override
    Set<T> takeRight(int n);

    @Override
    Set<T> takeUntil(Predicate<? super T> predicate);

    @Override
    Set<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <T1, T2> Tuple2<? extends Set<T1>, ? extends Set<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <U> Set<Tuple2<T, U>> zip(java.lang.Iterable<U> that);

    @Override
    <U> Set<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem);

    @Override
    Set<Tuple2<T, Integer>> zipWithIndex();

}
