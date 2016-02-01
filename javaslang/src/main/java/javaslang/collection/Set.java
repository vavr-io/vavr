/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Function1;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.control.Match;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An immutable {@code Set} interface.
 *
 * @param <T> Component type
 * @author Daniel Dietrich, Ruslan Sennov
 * @since 2.0.0
 */
public interface Set<T> extends Traversable<T>, Function1<T, Boolean> {

    long serialVersionUID = 1L;

    /**
     * Narrows a widened {@code Set<? extends T>} to {@code Set<T>}
     * by performing a type safe-cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param set A {@code Set}.
     * @param <T> Component type of the {@code Set}.
     * @return the given {@code set} instance as narrowed type {@code Set<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> Set<T> narrow(Set<? extends T> set) {
        return (Set<T>) set;
    }

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
    Set<T> addAll(Iterable<? extends T> elements);

    /**
     * Tests if a given {@code element} is contained in this {@code Set}.
     * <p>
     * This method is equivalent to {@link #contains(Object)}.
     *
     * @param element the element to test for membership.
     * @return {@code true} if the given {@code element} is contained, {@code false} otherwise.
     */
    @Override
    default Boolean apply(T element) {
        return contains(element);
    }

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
    Set<T> removeAll(Iterable<? extends T> elements);

    /**
     * Transforms this {@code Set}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(Function<? super Set<? super T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

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
    Set<T> drop(long n);

    @Override
    Set<T> dropRight(long n);

    @Override
    Set<T> dropUntil(Predicate<? super T> predicate);

    @Override
    Set<T> dropWhile(Predicate<? super T> predicate);

    @Override
    Set<T> filter(Predicate<? super T> predicate);

    @Override
    <U> Set<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper);

    @Override
    <C> Map<C, ? extends Set<T>> groupBy(Function<? super T, ? extends C> classifier);

    @Override
    Iterator<? extends Set<T>> grouped(long size);

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
    Match.MatchValue.Of<? extends Set<T>> match();

    @Override
    Tuple2<? extends Set<T>, ? extends Set<T>> partition(Predicate<? super T> predicate);

    @Override
    Set<T> peek(Consumer<? super T> action);

    @Override
    Set<T> replace(T currentElement, T newElement);

    @Override
    Set<T> replaceAll(T currentElement, T newElement);

    @Override
    Set<T> retainAll(Iterable<? extends T> elements);

    @Override
    Set<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation);

    @Override
    <U> Set<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation);

    @Override
    <U> Set<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation);

    @Override
    Iterator<? extends Set<T>> sliding(long size);

    @Override
    Iterator<? extends Set<T>> sliding(long size, long step);

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
    Set<T> take(long n);

    @Override
    Set<T> takeRight(long n);

    @Override
    Set<T> takeUntil(Predicate<? super T> predicate);

    @Override
    Set<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <T1, T2> Tuple2<? extends Set<T1>, ? extends Set<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <T1, T2, T3> Tuple3<? extends Set<T1>, ? extends Set<T2>, ? extends Set<T3>> unzip3(Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper);

    @Override
    <U> Set<Tuple2<T, U>> zip(Iterable<U> that);

    @Override
    <U> Set<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem);

    @Override
    Set<Tuple2<T, Long>> zipWithIndex();

}
