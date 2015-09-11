/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

/**
 * Provides a common {@link Object#toString()} implementation.
 * <p>
 * {@code equals(Object)} and {@code hashCode()} are intentionally not overridden in order to prevent this iterator
 * from being evaluated. In other words, equals and hashCode are implemented by Object.
 *
 * @param <T> Component type
 */
public abstract class AbstractIterator<T> implements Iterator<T> {

    @Override
    public String toString() {
        return (isEmpty() ? "" : "non-") + "empty iterator";
    }
}
