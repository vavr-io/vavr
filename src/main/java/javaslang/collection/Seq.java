/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

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
     * Splits a Traversable at the specified index. The result of {@code splitAt(n)} is equivalent to
     * {@code Tuple.of(take(n), drop(n))}.
     *
     * @param n An index.
     * @return A Tuple containing the first n and the remaining elements.
     */
    Tuple2<? extends Traversable<T>, ? extends Traversable<T>> splitAt(int n);

    Seq<T> subsequence(int beginIndex);

    Seq<T> subsequence(int beginIndex, int endIndex);
}
