/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Function1;
import javaslang.Tuple2;
import javaslang.algebra.HigherKinded1;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Interface for sequential, traversable data structures.
 * <p>Mutation:</p>
 * <ul>
 * <li>{@link #append(Object)}</li>
 * <li>{@link #appendAll(Iterable)}</li>
 * <li>{@link #insert(int, Object)}</li>
 * <li>{@link #insertAll(int, Iterable)}</li>
 * <li>{@link #prepend(Object)}</li>
 * <li>{@link #prependAll(Iterable)}</li>
 * <li>{@link #set(int, Object)}</li>
 * </ul>
 * <p>Selection:</p>
 * <ul>
 * <li>{@link #get(int)}</li>
 * <li>{@link #indexOf(Object)}</li>
 * <li>{@link #lastIndexOf(Object)}</li>
 * <li>{@link #subsequence(int)}</li>
 * <li>{@link #subsequence(int, int)}</li>
 * </ul>
 * <p>Transformation:</p>
 * <ul>
 * <li>TODO(#111): permutations()</li>
 * <li>{@link #sort()}</li>
 * <li>{@link #sort(Comparator)}</li>
 * <li>{@link #splitAt(int)}</li>
 * </ul>
 * <p>Traversion:</p>
 * <ul>
 * <li>{@link #iterator(int)}</li>
 * </ul>
 *
 * @param <T> Component type
 * @since 1.1.0
 */
public interface Seq<T> extends Traversable<T> {

    /**
     * Returns a Seq based on an Iterable. Returns the given Iterable, if it is already a Seq,
     * otherwise {@link Stream#of(Iterable)}.
     *
     * @param iterable An Iterable.
     * @param <T>      Component type
     * @return A Seq
     */
    static <T> Seq<T> of(Iterable<? extends T> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        if (iterable instanceof Seq) {
            @SuppressWarnings("unchecked")
            final Seq<T> seq = (Seq<T>) iterable;
            return seq;
        } else {
            return Stream.of(iterable);
        }
    }

    Seq<T> append(T element);

    Seq<T> appendAll(Iterable<? extends T> elements);

    T get(int index);

    int indexOf(T element);

    Seq<T> insert(int index, T element);

    Seq<T> insertAll(int index, Iterable<? extends T> elements);

    default Iterator<T> iterator(int index) {
        return subsequence(index).iterator();
    }

    int lastIndexOf(T element);

    Seq<T> prepend(T element);

    Seq<T> prependAll(Iterable<? extends T> elements);

    Seq<T> set(int index, T element);

    Seq<T> sort();

    Seq<T> sort(Comparator<? super T> c);

    /**
     * Splits a Seq at the specified index. The result of {@code splitAt(n)} is equivalent to
     * {@code Tuple.of(take(n), drop(n))}.
     *
     * @param n An index.
     * @return A Tuple containing the first n and the remaining elements.
     */
    Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAt(int n);

    Seq<T> subsequence(int beginIndex);

    Seq<T> subsequence(int beginIndex, int endIndex);

    // -- Adjusted return types of Traversable methods

    @Override
    Seq<T> clear();

    @Override
    Seq<? extends Seq<T>> combinations(int k);

    @Override
    Seq<T> distinct();

    @Override
    Seq<T> drop(int n);

    @Override
    Seq<T> dropRight(int n);

    @Override
    Seq<T> dropWhile(Predicate<? super T> predicate);

    @Override
    Seq<T> filter(Predicate<? super T> predicate);

    @Override
    Seq<T> findAll(Predicate<? super T> predicate);

    @Override
    <U, TRAVERSABLE extends HigherKinded1<U, Traversable<?>>> Seq<U> flatMap(Function1<? super T, TRAVERSABLE> mapper);

    @Override
    <U> Seq<U> flatten();

    @Override
    <U> Seq<U> flatten(Function1<T, ? extends Iterable<? extends U>> f);

    @Override
    <U> Seq<U> map(Function1<? super T, ? extends U> mapper);

    @Override
    Seq<T> init();

    @Override
    Seq<T> intersperse(T element);

    @Override
    Seq<T> remove(T element);

    @Override
    Seq<T> removeAll(T element);

    @Override
    Seq<T> removeAll(Iterable<? extends T> elements);

    @Override
    Seq<T> replace(T currentElement, T newElement);

    @Override
    Seq<T> replaceAll(T currentElement, T newElement);

    @Override
    Seq<T> replaceAll(Function1<T, T> operator);

    @Override
    Seq<T> retainAll(Iterable<? extends T> elements);

    @Override
    Seq<T> reverse();

    @Override
    Tuple2<? extends Seq<T>, ? extends Seq<T>> span(Predicate<? super T> predicate);

    @Override
    Seq<T> tail();

    @Override
    Seq<T> take(int n);

    @Override
    Seq<T> takeRight(int n);

    @Override
    Seq<T> takeWhile(Predicate<? super T> predicate);

    @Override
    <T1, T2> Tuple2<? extends Seq<T1>, ? extends Seq<T2>> unzip(Function1<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    @Override
    <U> Seq<Tuple2<T, U>> zip(Iterable<U> that);

    @Override
    <U> Seq<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem);

    @Override
    Seq<Tuple2<T, Integer>> zipWithIndex();
}
