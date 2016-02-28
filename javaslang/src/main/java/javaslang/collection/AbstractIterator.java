/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.NoSuchElementException;

/**
 * Provides a common {@link Object#toString()} implementation.
 * <p>
 * {@code equals(Object)} and {@code hashCode()} are intentionally not overridden in order to prevent this iterator
 * from being evaluated. In other words, (identity-)equals and hashCode are implemented by Object.
 *
 * @param <T> Component type
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public abstract class AbstractIterator<T> implements Iterator<T> {

    @Override
    public String toString() {
        return stringPrefix() + "(" + (isEmpty() ? "" : "?") + ")";
    }

    protected abstract T getNext();

    @Override
    public final T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("next() on empty iterator");
        }
        return getNext();
    }
}
