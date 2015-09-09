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
     * Adds all of the the elements of {@code that} set to this set, if not already present.
     * <p>
     * See also {@link #addAll(Iterable)}.
     *
     * @param that The set to form the union with.
     * @return A new set that contains all distinct elements of this and {@code that} set.
     */
    Set<T> union(Set<? extends T> that);

    /**
     * Unzips this elements by mapping this elements to pairs which are subsequentially split into two distinct
     * sets.
     *
     * @param unzipper a function which converts elements of this to pairs
     * @param <T1>     1st element type of a pair returned by unzipper
     * @param <T2>     2nd element type of a pair returned by unzipper
     * @return A pair of set containing elements split by unzipper
     * @throws NullPointerException if {@code unzipper} is null
     */
    <T1, T2> Tuple2<? extends Set<T1>, ? extends Set<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    /**
     * Returns a set formed from this set and another java.lang.Iterable collection by combining corresponding elements
     * in pairs. If one of the two iterables is longer than the other, its remaining elements are ignored.
     * <p>
     * The length of the returned set is the minimum of the lengths of this set and {@code that} iterable.
     *
     * @param <U>  The type of the second half of the returned pairs.
     * @param that The java.lang.Iterable providing the second half of each result pair.
     * @return a new set containing pairs consisting of corresponding elements of this set and {@code that} iterable.
     * @throws NullPointerException if {@code that} is null
     */
    <U> Set<Tuple2<T, U>> zip(java.lang.Iterable<U> that);

    /**
     * Returns a set formed from this set and another java.lang.Iterable by combining corresponding elements in
     * pairs. If one of the two collections is shorter than the other, placeholder elements are used to extend the
     * shorter collection to the length of the longer.
     * <p>
     * The length of the returned set is the maximum of the lengths of this set and {@code that} iterable. Special case:
     * if this set is shorter than that elements, and that elements contains duplicates, the resulting set may be shorter
     * than the maximum of the lengths of this and that because a set contains an element at most once.
     * <p>
     * If this Traversable is shorter than that, thisElem values are used to fill the result.
     * If that is shorter than this Traversable, thatElem values are used to fill the result.
     *
     * @param <U>      The type of the second half of the returned pairs.
     * @param that     The java.lang.Iterable providing the second half of each result pair.
     * @param thisElem The element to be used to fill up the result if this set is shorter than that.
     * @param thatElem The element to be used to fill up the result if that is shorter than this set.
     * @return A new set containing pairs consisting of corresponding elements of this set and that.
     * @throws NullPointerException if {@code that} is null
     */
    <U> Set<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem);

    /**
     * Zips this set with its indices.
     *
     * @return A new set containing all elements of this set paired with their index, starting with 0.
     */
    Set<Tuple2<T, Integer>> zipWithIndex();

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
    Set<T> takeWhile(Predicate<? super T> predicate);

}
