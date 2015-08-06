/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

/**
 * An immutable {@code BinarySearchTree} interface.
 *
 * @param <T> Component type
 * @since 2.0.0
 */
public interface BinarySearchTree<T> {

    boolean contains(T value);

    BinarySearchTree<T> add(T value);

    @SuppressWarnings({ "unchecked", "varargs" })
    BinarySearchTree<T> addAll(T... values);

    BinarySearchTree<T> addAll(Iterable<? extends T> values);

    boolean isEmpty();

}
