/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.function.Function;

/**
 * An immutable {@code SortedSet} interface.
 *
 * @param <T> Component type
 * @since 2.0.0
 */
public interface SortedSet<T> /*extends Set<T>*/ {

    // TODO: additional SortedSet methods

    // -- Adjusted return types of Set methods

    // TODO: @Override
    <C> Map<C, ? extends SortedSet<T>> groupBy(Function<? super T, ? extends C> classifier);

    // TODO

}
