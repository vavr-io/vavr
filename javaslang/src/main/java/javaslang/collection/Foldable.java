/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.control.Option;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Interface of foldable data structures.
 * <p>
 * <strong>Example:</strong>
 *
 * <pre><code>
 * // = "123"
 * Stream.of("1", "2", "3").fold("", (a1, a2) -&gt; a1 + a2);
 * </code></pre>
 *
 * @param <T> Component type of this foldable
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Foldable<T> {

    /**
     * Folds this elements from the left, starting with {@code zero} and successively calling {@code combine}.
     *
     * @param zero    A zero element to start with.
     * @param combine A function which combines elements.
     * @return a folded value
     * @throws NullPointerException if {@code combine} is null
     */
    default T fold(T zero, BiFunction<? super T, ? super T, ? extends T> combine) {
        Objects.requireNonNull(combine, "combine is null");
        return foldLeft(zero, combine);
    }

    /**
     * Folds this elements from the left, starting with {@code zero} and successively calling {@code combine}.
     *
     * @param <U>     the type to fold over
     * @param zero    A zero element to start with.
     * @param combine A function which combines elements.
     * @return a folded value
     * @throws NullPointerException if {@code combine} is null
     */
    <U> U foldLeft(U zero, BiFunction<? super U, ? super T, ? extends U> combine);

    /**
     * Folds this elements from the right, starting with {@code zero} and successively calling {@code combine}.
     *
     * @param <U>     the type of the folded value
     * @param zero    A zero element to start with.
     * @param combine A function which combines elements.
     * @return a folded value
     * @throws NullPointerException if {@code combine} is null
     */
    <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> combine);

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op}.
     * The order of element iteration is undetermined.
     *
     * @param op A BiFunction of type T
     * @return the reduced value.
     * @throws NoSuchElementException if this is empty
     * @throws NullPointerException   if {@code op} is null
     */
    default T reduce(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return reduceLeft(op);
    }

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op}.
     * The order of element iteration is undetermined.
     *
     * @param op A BiFunction of type T
     * @return Some of reduced value or None if the Foldable is empty.
     * @throws NullPointerException if {@code op} is null
     */
    default Option<T> reduceOption(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return reduceLeftOption(op);
    }

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op} from the left.
     *
     * @param op A BiFunction of type T
     * @return the reduced value.
     * @throws NoSuchElementException if this is empty
     * @throws NullPointerException   if {@code op} is null
     */
    T reduceLeft(BiFunction<? super T, ? super T, ? extends T> op);

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op} from the left.
     *
     * @param op A BiFunction of type T
     * @return Some of reduced value or None if the Foldable is empty.
     * @throws NullPointerException if {@code op} is null
     */
    Option<T> reduceLeftOption(BiFunction<? super T, ? super T, ? extends T> op);

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op} from the right.
     *
     * @param op An operation of type T
     * @return the reduced value.
     * @throws NoSuchElementException if this is empty
     * @throws NullPointerException   if {@code op} is null
     */
    T reduceRight(BiFunction<? super T, ? super T, ? extends T> op);

    /**
     * Accumulates the elements of this Foldable by successively calling the given operation {@code op} from the right.
     *
     * @param op An operation of type T
     * @return Some of reduced value or None.
     * @throws NullPointerException if {@code op} is null
     */
    Option<T> reduceRightOption(BiFunction<? super T, ? super T, ? extends T> op);

}
