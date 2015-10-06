/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This class may move to another package in a future release and is not intended to be public API.
 */
final class Comparators {

    private Comparators() {
    }

    /**
     * Returns the natural comparator for type U, i.e. treating it as {@code Comparable<? super U>}.
     * The returned comparator is also {@code java.io.Serializable}.
     * <p>
     * Please note that this will lead to runtime exceptions, if U is not Comparable.
     *
     * @param <U> The type
     * @return The natural Comparator of type U
     */
    @SuppressWarnings("unchecked")
    static <U> SerializableComparator<U> naturalComparator() {
        return (o1, o2) -> ((Comparable<U>) o1).compareTo(o2);
    }

    /**
     * Needed for serialization of sortable collections which internally need a comparator.
     * <p>
     * In general the comparator may be
     * <ul>
     * <li>a concrete class</li>
     * <li>a lambda</li>
     * <li>a method reference</li>
     * </ul>
     *
     * @param <T> the type of objects that may be compared by this comparator
     */
    @FunctionalInterface
    interface SerializableComparator<T> extends Comparator<T>, Serializable {
    }
}
