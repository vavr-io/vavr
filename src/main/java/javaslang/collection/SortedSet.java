/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * An immutable {@code SortedSet} interface.
 *
 * @param <T> Component type
 * @since 2.0.0
 */
public interface SortedSet<T> extends Set<T> {

    // TODO: additional SortedSet methods

    // -- Adjusted return types of Set methods

    @Override
    SortedSet<T> add(T element);

    @Override
    SortedSet<T> clear();

    @Override
    SortedSet<T> distinct();

    @Override
    SortedSet<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U> SortedSet<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    SortedSet<T> drop(int n);

    @Override
    SortedSet<T> dropRight(int n);

    @Override
    SortedSet<T> dropWhile(Predicate<? super T> predicate);

    @Override
    SortedSet<T> filter(Predicate<? super T> predicate);

    @Override
    <U> SortedSet<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper);

    @Override
    <U> SortedSet<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper);

    @Override
    SortedSet<Object> flatten();

    @Override
    <C> Map<C, ? extends SortedSet<T>> groupBy(Function<? super T, ? extends C> classifier);

    @Override
    SortedSet<T> init();

    @Override
    public Option<? extends SortedSet<T>> initOption();

    @Override
    <U> SortedSet<U> map(Function<? super T, ? extends U> mapper);

    @Override
    Tuple2<? extends SortedSet<T>, ? extends SortedSet<T>> partition(Predicate<? super T> predicate);

    @Override
    SortedSet<T> peek(Consumer<? super T> action);

    @Override
    SortedSet<T> remove(T element);

    @Override
    SortedSet<T> removeAll(java.lang.Iterable<? extends T> elements);

    @Override
    SortedSet<T> replace(T currentElement, T newElement);

    @Override
    SortedSet<T> replaceAll(T currentElement, T newElement);

    @Override
    SortedSet<T> replaceAll(UnaryOperator<T> operator);

    @Override
    SortedSet<T> retainAll(java.lang.Iterable<? extends T> elements);

    @Override
    Tuple2<? extends SortedSet<T>, ? extends SortedSet<T>> span(Predicate<? super T> predicate);

    @Override
    SortedSet<T> tail();

    @Override
    Option<? extends SortedSet<T>> tailOption();

    @Override
    SortedSet<T> take(int n);

    @Override
    SortedSet<T> takeRight(int n);

    @Override
    SortedSet<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <T1, T2> Tuple2<? extends SortedSet<T1>, ? extends SortedSet<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <U> SortedSet<Tuple2<T, U>> zip(java.lang.Iterable<U> that);

    @Override
    <U> SortedSet<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem);

    @Override
    SortedSet<Tuple2<T, Integer>> zipWithIndex();

}
