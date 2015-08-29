/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Interface for immutable, indexed sequences.
 * <p>
 * Efficient random access is characteristic for indexed sequences.
 *
 * @param <T> component type
 * @since 2.0.0
 */
public interface IndexedSeq<T> extends Seq<T> {

    // -- Adjusted return types of Seq methods

    @Override
    IndexedSeq<T> append(T element);

    @Override
    IndexedSeq<T> appendAll(java.lang.Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> clear();

    @Override
    IndexedSeq<Tuple2<T, T>> crossProduct();

    @Override
    <U> IndexedSeq<Tuple2<T, U>> crossProduct(java.lang.Iterable<? extends U> that);

    @Override
    IndexedSeq<? extends IndexedSeq<T>> combinations();

    @Override
    IndexedSeq<? extends IndexedSeq<T>> combinations(int k);

    @Override
    IndexedSeq<T> distinct();

    @Override
    IndexedSeq<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U> IndexedSeq<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    IndexedSeq<T> drop(int n);

    @Override
    IndexedSeq<T> dropRight(int n);

    @Override
    IndexedSeq<T> dropWhile(Predicate<? super T> predicate);

    @Override
    IndexedSeq<T> filter(Predicate<? super T> predicate);

    @Override
    <U> IndexedSeq<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper);

    @Override
    <U> IndexedSeq<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper);

    @Override
    IndexedSeq<Object> flatten();

    @Override
    <C> Map<C, ? extends IndexedSeq<T>> groupBy(Function<? super T, ? extends C> classifier);

    @Override
    IndexedSeq<T> init();

    @Override
    Option<? extends IndexedSeq<T>> initOption();

    @Override
    IndexedSeq<T> insert(int index, T element);

    @Override
    IndexedSeq<T> insertAll(int index, java.lang.Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> intersperse(T element);

    @Override
    <U> IndexedSeq<U> map(Function<? super T, ? extends U> mapper);

    @Override
    IndexedSeq<T> padTo(int length, T element);

    @Override
    Tuple2<? extends IndexedSeq<T>, ? extends IndexedSeq<T>> partition(Predicate<? super T> predicate);

    @Override
    IndexedSeq<T> peek(Consumer<? super T> action);

    @Override
    IndexedSeq<? extends IndexedSeq<T>> permutations();

    @Override
    IndexedSeq<T> prepend(T element);

    @Override
    IndexedSeq<T> prependAll(java.lang.Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> remove(T element);

    @Override
    IndexedSeq<T> removeFirst(Predicate<T> predicate);

    @Override
    IndexedSeq<T> removeLast(Predicate<T> predicate);

    @Override
    IndexedSeq<T> removeAt(int indx);

    @Override
    IndexedSeq<T> removeAll(T element);

    @Override
    IndexedSeq<T> removeAll(java.lang.Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> replace(T currentElement, T newElement);

    @Override
    IndexedSeq<T> replaceAll(T currentElement, T newElement);

    @Override
    IndexedSeq<T> replaceAll(UnaryOperator<T> operator);

    @Override
    IndexedSeq<T> retainAll(java.lang.Iterable<? extends T> elements);

    @Override
    IndexedSeq<T> reverse();

    @Override
    IndexedSeq<T> set(int index, T element);

    @Override
    IndexedSeq<T> slice(int beginIndex);

    @Override
    IndexedSeq<T> slice(int beginIndex, int endIndex);

    @Override
    IndexedSeq<T> sort();

    @Override
    IndexedSeq<T> sort(Comparator<? super T> comparator);

    @Override
    Tuple2<? extends IndexedSeq<T>, ? extends IndexedSeq<T>> span(Predicate<? super T> predicate);

    @Override
    default boolean startsWith(java.lang.Iterable<? extends T> that, int offset) {
        if (offset > length()) {
            return false;
        }
        int index = offset;
        for (T t : that) {
            if (index >= length() || !Objects.equals(get(index), t)) {
                return false;
            }
            index++;
        }
        return true;
    }

    @Override
    IndexedSeq<T> tail();

    @Override
    Option<? extends IndexedSeq<T>> tailOption();

    @Override
    IndexedSeq<T> take(int n);

    @Override
    IndexedSeq<T> takeRight(int n);

    @Override
    IndexedSeq<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <U> IndexedSeq<U> unit(java.lang.Iterable<? extends U> iterable);

    @Override
    <T1, T2> Tuple2<? extends IndexedSeq<T1>, ? extends IndexedSeq<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <U> IndexedSeq<Tuple2<T, U>> zip(java.lang.Iterable<U> that);

    @Override
    <U> IndexedSeq<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem);

    @Override
    IndexedSeq<Tuple2<T, Integer>> zipWithIndex();

}
