/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Interface to reduce code duplication of Iterables and Traversables.
 *
 * @param <T> element type of Iterable
 * @since 2.0.0
 */
public interface TraversableOnce<T> extends Iterable<T> {

    /**
     * Indicates if this {@code TraversableOnce} is empty
     *
     * @return true, if this is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Checks, if an element exists such that the predicate holds.
     *
     * @param predicate A Predicate
     * @return true, if predicate holds for an element of this, false otherwise
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
     * Checks, if a unique elements exists such that the predicate holds.
     *
     * @param predicate A Predicate
     * @return true, if predicate holds for a unique element of this, false otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default boolean existsUnique(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        boolean exists = false;
        for (T t : this) {
            if (predicate.test(t)) {
                if (exists) {
                    return false;
                } else {
                    exists = true;
                }
            }
        }
        return exists;
    }

    /**
     * Checks, if the given predicate holds for all elements of this.
     *
     * @param predicate A Predicate
     * @return true, if the predicate holds for all elements of this, false otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default boolean forAll(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return !exists(predicate.negate());
    }

    /**
     * Performs an action on each element of this monad.
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

    default List<T> toList() {
        return List.ofAll(this);
    }

    default Queue<T> toQueue() {
        return Queue.ofAll(this);
    }

    default Stream<T> toStream() {
        return Stream.ofAll(this);
    }

    default Stack<T> toStack() {
        return Stack.ofAll(this);
    }
}
