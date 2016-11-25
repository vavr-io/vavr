/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;
import java.util.Comparator;

/**
 * INTERNAL: Common {@code Comparator} related functions (not intended to be public).
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
final class Comparators {

    private Comparators() {
    }

    /**
     * Returns the natural comparator for type U, i.e. treating it as {@code Comparable<U>}.
     * The returned comparator is also {@code java.io.Serializable}.
     * <p>
     * Please note that this will lead to runtime exceptions, if U is not Comparable.
     *
     * @param <U> The type
     * @return The natural Comparator of type U
     */
    @SuppressWarnings("unchecked")
    static <U> Comparator<U> naturalComparator() {
        return NaturalComparator.instance();
    }

}

final class NaturalComparator<T> implements Comparator<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final NaturalComparator<?> INSTANCE = new NaturalComparator<>();

    private NaturalComparator() {
    }

    @SuppressWarnings("unchecked")
    static <T> NaturalComparator<T> instance() {
        return (NaturalComparator<T>) INSTANCE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(T o1, T o2) {
        return ((Comparable<T>) o1).compareTo(o2);
    }

    /**
     * Instance control for object serialization.
     *
     * @return The singleton instance of NaturalComparator.
     * @see java.io.Serializable
     */
    private Object readResolve() {
        return INSTANCE;
    }

}
