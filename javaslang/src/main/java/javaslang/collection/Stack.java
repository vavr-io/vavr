/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.control.Option;

/**
 * An immutable {@code Stack} stores elements allowing a last-in-first-out (LIFO) retrieval.
 * <p>
 * {@link List} extends {@code Stack}, so use one of {@code List}'s static factory methods to obtain a {@code Stack}.
 * <p>
 * Stack API:
 *
 * <ul>
 * <li>{@link #peek()}</li>
 * <li>{@link #peekOption()}</li>
 * <li>{@link #pop()}</li>
 * <li>{@link #popOption()}</li>
 * <li>{@link #pop2()}</li>
 * <li>{@link #pop2Option()}</li>
 * <li>{@link #push(Object)}</li>
 * <li>{@link #push(Object[])}</li>
 * <li>{@link #pushAll(Iterable)}</li>
 * </ul>
 *
 * See Okasaki, Chris: <em>Purely Functional Data Structures</em> (p. 7 ff.). Cambridge, 2003.
 *
 * @param <T> component type
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Stack<T> {

    /**
     * Narrows a widened {@code Stack<? extends T>} to {@code Stack<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param stack A {@code Stack}.
     * @param <T>   Component type of the {@code Stack}.
     * @return the given {@code stack} instance as narrowed type {@code Stack<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> Stack<T> narrow(Stack<? extends T> stack) {
        return (Stack<T>) stack;
    }

    /**
     * Checks if this Stack is empty.
     *
     * @return true, if this is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Returns the head element without modifying the Stack.
     *
     * @return the first element
     * @throws java.util.NoSuchElementException if this Stack is empty
     */
    T peek();

    /**
     * Returns the head element without modifying the Stack.
     *
     * @return {@code None} if this Stack is empty, otherwise a {@code Some} containing the head element
     */
    Option<T> peekOption();

    /**
     * Removes the head element from this Stack.
     *
     * @return the elements of this Stack without the head element
     * @throws java.util.NoSuchElementException if this Stack is empty
     */
    Stack<T> pop();

    /**
     * Removes the head element from this Stack.
     *
     * @return {@code None} if this Stack is empty, otherwise a {@code Some} containing the elements of this Stack without the head element
     */
    Option<? extends Stack<T>> popOption();

    /**
     * Removes the head element from this Stack.
     *
     * @return a tuple containing the head element and the remaining elements of this Stack
     * @throws java.util.NoSuchElementException if this Stack is empty
     */
    Tuple2<T, ? extends Stack<T>> pop2();

    /**
     * Removes the head element from this Stack.
     *
     * @return {@code None} if this Stack is empty, otherwise {@code Some} {@code Tuple} containing the head element and the remaining elements of this Stack
     */
    Option<? extends Tuple2<T, ? extends Stack<T>>> pop2Option();

    /**
     * Pushes a new element on top of this Stack.
     *
     * @param element The new element
     * @return a new {@code Stack} instance, containing the new element on top of this Stack
     */
    Stack<T> push(T element);

    /**
     * Pushes the given elements on top of this Stack. A Stack has LIFO order, i.e. the last of the given elements is
     * the first which will be retrieved.
     *
     * @param elements Elements, may be empty
     * @return a new {@code Stack} instance, containing the new elements on top of this Stack
     * @throws NullPointerException if elements is null
     */
    @SuppressWarnings("unchecked")
    Stack<T> push(T... elements);

    /**
     * Pushes the given elements on top of this Stack. A Stack has LIFO order, i.e. the last of the given elements is
     * the first which will be retrieved.
     *
     * @param elements An Iterable of elements, may be empty
     * @return a new {@code Stack} instance, containing the new elements on top of this Stack
     * @throws NullPointerException if elements is null
     */
    Stack<T> pushAll(Iterable<T> elements);

}
