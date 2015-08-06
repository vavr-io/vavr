/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Kind;
import javaslang.Tuple2;
import javaslang.control.Option;
import javaslang.control.Some;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.*;

/**
 * An immutable {@code Set} interface.
 *
 * @param <T>
 * @since 2.0.0
 */
public interface Set<T> extends Traversable<T> {

    // TODO: set operations

    Set<T> add(T element);

    @Override
    Set<T> clear();

    @Override
    Set<Tuple2<T, T>> cartesianProduct();

    @Override
    <U> Set<Tuple2<T, U>> cartesianProduct(Iterable<? extends U> that);

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
    Set<Some<T>> filterOption(Predicate<? super T> predicate);

    @Override
    Set<T> findAll(Predicate<? super T> predicate);

    @Override
    <U> Set<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper);

    @Override
    <U> Set<U> flatMapM(Function<? super T, ? extends Kind<? extends IterableKind<?>, ? extends U>> mapper);

    @Override
    Set<Object> flatten();

    @Override
    Set<? extends Set<T>> grouped(int size);

    @Override
    Set<T> init();

    @Override
    Option<? extends Set<T>> initOption();

    @Override
    Set<T> intersperse(T element);

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
    Set<T> remove(T element);

    @Override
    Set<T> removeAll(T element);

    @Override
    Set<T> removeAll(Iterable<? extends T> elements);

    @Override
    Set<T> replace(T currentElement, T newElement);

    @Override
    Set<T> replaceAll(T currentElement, T newElement);

    @Override
    Set<T> replaceAll(UnaryOperator<T> operator);

    @Override
    Set<T> retainAll(Iterable<? extends T> elements);

    @Override
    Set<T> reverse();

    @Override
    Set<? extends Set<T>> sliding(int size);

    @Override
    Set<? extends Set<T>> sliding(int size, int step);

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

    @Override
    <U> Set<U> unit(Iterable<? extends U> iterable);

    @Override
    <T1, T2> Tuple2<? extends Set<T1>, ? extends Set<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <U> Set<Tuple2<T, U>> zip(Iterable<U> that);

    @Override
    <U> Set<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem);

    @Override
    Set<Tuple2<T, Integer>> zipWithIndex();

}
