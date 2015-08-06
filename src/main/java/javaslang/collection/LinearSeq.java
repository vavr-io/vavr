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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Interface for immutable, linear sequences.
 * <p>
 * Efficient {@code head()}, {@code tail()}, and {@code isEmpty()} methods are characteristic for linear sequences.
 *
 * @param <T> component type
 * @since 2.0.0
 */
public interface LinearSeq<T> extends Seq<T> {

    // -- Adjusted return types of Seq methods

    @Override
    LinearSeq<T> append(T element);

    @Override
    LinearSeq<T> appendAll(Iterable<? extends T> elements);

    @Override
    LinearSeq<T> clear();

    @Override
    LinearSeq<Tuple2<T, T>> cartesianProduct();

    @Override
    <U> LinearSeq<Tuple2<T, U>> cartesianProduct(Iterable<? extends U> that);

    @Override
    LinearSeq<? extends LinearSeq<T>> combinations();

    @Override
    LinearSeq<? extends LinearSeq<T>> combinations(int k);

    @Override
    LinearSeq<T> distinct();

    @Override
    LinearSeq<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U> LinearSeq<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    LinearSeq<T> drop(int n);

    @Override
    LinearSeq<T> dropRight(int n);

    @Override
    LinearSeq<T> dropWhile(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> filter(Predicate<? super T> predicate);

    @Override
    LinearSeq<Some<T>> filterOption(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> findAll(Predicate<? super T> predicate);

    @Override
    <U> LinearSeq<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper);

    @Override
    <U> LinearSeq<U> flatMapM(Function<? super T, ? extends Kind<? extends IterableKind<?>, ? extends U>> mapper);

    @Override
    LinearSeq<Object> flatten();

    @Override
    LinearSeq<? extends LinearSeq<T>> grouped(int size);

    @Override
    LinearSeq<T> init();

    @Override
    Option<? extends LinearSeq<T>> initOption();

    @Override
    LinearSeq<T> insert(int index, T element);

    @Override
    LinearSeq<T> insertAll(int index, Iterable<? extends T> elements);

    @Override
    LinearSeq<T> intersperse(T element);

    @Override
    <U> LinearSeq<U> map(Function<? super T, ? extends U> mapper);

    @Override
    Tuple2<? extends LinearSeq<T>, ? extends LinearSeq<T>> partition(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> peek(Consumer<? super T> action);

    @Override
    LinearSeq<? extends LinearSeq<T>> permutations();

    @Override
    LinearSeq<T> prepend(T element);

    @Override
    LinearSeq<T> prependAll(Iterable<? extends T> elements);

    @Override
    LinearSeq<T> remove(T element);

    @Override
    LinearSeq<T> removeAll(T element);

    @Override
    LinearSeq<T> removeAll(Iterable<? extends T> elements);

    @Override
    LinearSeq<T> replace(T currentElement, T newElement);

    @Override
    LinearSeq<T> replaceAll(T currentElement, T newElement);

    @Override
    LinearSeq<T> replaceAll(UnaryOperator<T> operator);

    @Override
    LinearSeq<T> retainAll(Iterable<? extends T> elements);

    @Override
    LinearSeq<T> reverse();

    @Override
    LinearSeq<T> set(int index, T element);

    @Override
    LinearSeq<? extends LinearSeq<T>> sliding(int size);

    @Override
    LinearSeq<? extends LinearSeq<T>> sliding(int size, int step);

    @Override
    LinearSeq<T> sort();

    @Override
    LinearSeq<T> sort(Comparator<? super T> comparator);

    @Override
    Tuple2<? extends LinearSeq<T>, ? extends LinearSeq<T>> span(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> subsequence(int beginIndex);

    @Override
    LinearSeq<T> subsequence(int beginIndex, int endIndex);

    @Override
    LinearSeq<T> tail();

    @Override
    Option<? extends LinearSeq<T>> tailOption();

    @Override
    LinearSeq<T> take(int n);

    @Override
    LinearSeq<T> takeRight(int n);

    @Override
    LinearSeq<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <U> LinearSeq<U> unit(Iterable<? extends U> iterable);

    @Override
    <T1, T2> Tuple2<? extends LinearSeq<T1>, ? extends LinearSeq<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <U> LinearSeq<Tuple2<T, U>> zip(Iterable<U> that);

    @Override
    <U> LinearSeq<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem);

    @Override
    LinearSeq<Tuple2<T, Integer>> zipWithIndex();

}
