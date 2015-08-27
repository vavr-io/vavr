/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
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

    // TODO: additional set operations (union, intersection, etc.)

    Set<T> add(T element);

    Set<T> remove(T element);

    Set<T> removeAll(java.lang.Iterable<? extends T> elements);

    <T1, T2> Tuple2<? extends Set<T1>, ? extends Set<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    <U> Set<Tuple2<T, U>> zip(java.lang.Iterable<U> that);

    <U> Set<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem);

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
    <U> Set<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper);

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
