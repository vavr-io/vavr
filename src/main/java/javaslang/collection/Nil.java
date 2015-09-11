/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * Representation of the singleton empty {@code List}.
 *
 * @param <T> Component type of the List.
 * @since 1.1.0
 */
final class Nil<T> implements List<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Nil<?> INSTANCE = new Nil<>();

    // hidden
    private Nil() {
    }

    /**
     * Returns the singleton instance of the liked list.
     *
     * @param <T> Component type of the List
     * @return the singleton instance of the linked list.
     */
    @SuppressWarnings("unchecked")
    public static <T> Nil<T> instance() {
        return (Nil<T>) INSTANCE;
    }

    @Override
    public T head() {
        throw new NoSuchElementException("head of empty list");
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public List<T> tail() {
        throw new UnsupportedOperationException("tail of empty list");
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    @Override
    public int hashCode() {
        return Traversable.hash(this);
    }

    @Override
    public String toString() {
        return "List()";
    }

    /**
     * Instance control for object serialization.
     *
     * @return The singleton instance of Nil.
     * @see java.io.Serializable
     */
    private Object readResolve() {
        return INSTANCE;
    }
}
