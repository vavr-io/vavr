/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
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
    static <U> Comparator<U> naturalComparator() {
        return (Comparator<U> & Serializable) (o1, o2) -> ((Comparable<U>) o1).compareTo(o2);
    }
}
