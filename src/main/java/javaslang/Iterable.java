/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.IndexedSeq;
import javaslang.collection.Iterator;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A rich extension of {@code java.lang.Iterable} and basis of all {@link Value} types, e.g. controls, collections et al.
 *
 * @param <T> Component type
 */
public interface Iterable<T> extends java.lang.Iterable<T> {

    /**
     * Returns a rich {@code javaslang.collection.Iterator}.
     *
     * @return A new Iterator
     */
    @Override
    Iterator<T> iterator();

    /**
     * Checks, if an element exists such that the predicate holds.
     *
     * @param predicate A Predicate
     * @return true, if predicate holds for one or more elements, false otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default boolean exists(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (T t : this) {
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks, if the given predicate holds for all elements.
     *
     * @param predicate A Predicate
     * @return true, if the predicate holds for all elements, false otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default boolean forAll(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return !exists(predicate.negate());
    }

    /**
     * Performs an action on each element.
     *
     * @param action A {@code Consumer}
     * @throws NullPointerException if {@code action} is null
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        for (T t : this) {
            action.accept(t);
        }
    }

    /**
     * Groups this {@code Traversable} into fixed size blocks like so:
     * <ul>
     * <li>If {@code this.isEmpty()}, the resulting {@code Traversable} is empty.</li>
     * <li>If {@code size <= this.length()}, the resulting {@code Traversable} will contain {@code this.length() / size}
     * blocks of size {@code size} and maybe a non-empty block of size {@code this.length() % size}, if there are
     * remaining elements.</li>
     * <li>If {@code size > this.length()}, the resulting {@code Traversable} will contain one block of size
     * {@code this.length()}.</li>
     * </ul>
     * Examples:
     * <pre>
     * <code>
     * [].grouped(1) = []
     * [].grouped(0) throws
     * [].grouped(-1) throws
     * [1,2,3,4].grouped(2) = [[1,2],[3,4]]
     * [1,2,3,4,5].grouped(2) = [[1,2],[3,4],[5]]
     * [1,2,3,4].grouped(5) = [[1,2,3,4]]
     * </code>
     * </pre>
     *
     * Please note that {@code grouped(int)} is a special case of {@linkplain #sliding(int, int)}, i.e.
     * {@code grouped(size)} is the same as {@code sliding(size, size)}.
     *
     * @param size a positive block size
     * @return A new Traversable of sliced blocks of the given size
     * @throws IllegalArgumentException if {@code size} is negative or zero
     */
    default Iterator<IndexedSeq<T>> grouped(int size) {
        return sliding(size, size);
    }


    /**
     * Slides a window of a specific {@code size} and step size 1 over this {@code TraversableOnce} by calling
     * {@link #sliding(int, int)}.
     *
     * @param size a positive window size
     * @return a new TraversableOnce of windows of a specific size using step size 1
     * @throws IllegalArgumentException if {@code size} is negative or zero
     */
    default Iterator<IndexedSeq<T>> sliding(int size) {
        return sliding(size, 1);
    }

    /**
     * Slides a window of a specific {@code size} and {@code step} size over this {@code TraversableOnce}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * [].sliding(1,1) = []
     * [1,2,3,4,5].sliding(2,3) = [[1,2],[4,5]]
     * [1,2,3,4,5].sliding(2,4) = [[1,2],[5]]
     * [1,2,3,4,5].sliding(2,5) = [[1,2]]
     * [1,2,3,4].sliding(5,3) = [[1,2,3,4],[4]]
     * </code>
     * </pre>
     *
     * @param size a positive window size
     * @param step a positive step size
     * @return a new TraversableOnce of windows of a specific size using a specific step size
     * @throws IllegalArgumentException if {@code size} or {@code step} are negative or zero
     */
    default Iterator<IndexedSeq<T>> sliding(int size, int step) {
        return iterator().sliding(size, step);
    }

}
