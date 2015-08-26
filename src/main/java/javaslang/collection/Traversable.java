/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * An interface for inherently recursive data structures. The order of elements is determined by
 * {@link java.lang.Iterable#iterator()}, which may vary each time it is called.
 * <p>
 * Implementations of {@code Traversable} should calculate the {@code hashCode} via {@link #hash(java.lang.Iterable)}.
 * <p>
 * Additionally to {@link TraversableOnce} this interface has the following methods:
 *
 * <ul>
 * <li>{@link #hasDefiniteSize()}</li>
 * <li>{@link #isTraversableAgain()}</li>
 * </ul>
 *
 * @param <T> Component type
 * @since 1.1.0
 */
public interface Traversable<T> extends TraversableOnce<T> {

    /**
     * Used by collections to compute the hashCode only once.
     * <p>
     * Idiom:
     * <pre>
     * <code>
     * class MyCollection implements Serializable {
     *
     *     // Not allowed to be serialized!
     *     private final transient Lazy&lt;Integer&gt; hashCode = Lazy.of(() -&gt; Traversable.hash(this));
     *
     *     &#64;Override
     *     public int hashCode() {
     *         return hashCode.get();
     *     }
     * }
     * </code>
     * </pre>
     *
     * <strong>Note:</strong> In the case of an empty collection, such as {@code List.Nil} it is recommended to
     * directly return {@code Traversable.hash(this)} instead of asking a {@code Lazy} value:
     * <pre>
     * <code>
     * interface List&lt;T&gt; {
     *
     *     class Nil&lt;T&gt; {
     *
     *         &#64;Override
     *         public int hashCode() {
     *             return Traversable.hash(this);
     *         }
     *     }
     * }
     * </code>
     * </pre>
     *
     * @param <T>     Component type
     * @param objects An java.lang.Iterable
     * @return The hashCode of the given java.lang.Iterable
     * @throws NullPointerException if objects is null
     */
    static <T> int hash(java.lang.Iterable<? extends T> objects) {
        int hashCode = 1;
        for (Object o : objects) {
            hashCode = 31 * hashCode + Objects.hashCode(o);
        }
        return hashCode;
    }

    /**
     * Checks if this Traversable is known to have a finite size.
     * <p>
     * This method should be implemented by classes only, i.e. not by interfaces.
     *
     * @return true, if this Traversable is known to hafe a finite size, false otherwise.
     */
    boolean hasDefiniteSize();

    /**
     * Checks if this Traversable can be repeatedly traversed.
     * <p>
     * This method should be implemented by classes only, i.e. not by interfaces.
     *
     * @return true, if this Traversable is known to be traversable repeatedly, false otherwise.
     */
    boolean isTraversableAgain();


    // -- Adjusted return types of TraversableOnce

    @Override
    Traversable<T> clear();

    @Override
    Traversable<T> distinct();

    @Override
    Traversable<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U> Traversable<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    Traversable<T> drop(int n);

    @Override
    Traversable<T> dropRight(int n);

    @Override
    Traversable<T> dropWhile(Predicate<? super T> predicate);

    @Override
    Traversable<T> filter(Predicate<? super T> predicate);

    @Override
    <U> Traversable<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper);

    @Override
    <U> Traversable<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper);

    @Override
    Traversable<Object> flatten();

    @Override
    <C> Map<C, ? extends Traversable<T>> groupBy(Function<? super T, ? extends C> classifier);

    @Override
    Traversable<T> init();

    @Override
    Option<? extends Traversable<T>> initOption();

    @Override
    <U> Traversable<U> map(Function<? super T, ? extends U> mapper);

    @Override
    Tuple2<? extends Traversable<T>, ? extends Traversable<T>> partition(Predicate<? super T> predicate);

    @Override
    Traversable<T> peek(Consumer<? super T> action);

    @Override
    Traversable<T> replace(T currentElement, T newElement);

    @Override
    Traversable<T> replaceAll(T currentElement, T newElement);

    @Override
    Traversable<T> replaceAll(UnaryOperator<T> operator);

    @Override
    Traversable<T> retainAll(java.lang.Iterable<? extends T> elements);

    @Override
    Tuple2<? extends Traversable<T>, ? extends Traversable<T>> span(Predicate<? super T> predicate);

    @Override
    Traversable<T> tail();

    @Override
    Option<? extends Traversable<T>> tailOption();

    @Override
    Traversable<T> take(int n);

    @Override
    Traversable<T> takeRight(int n);

    @Override
    Traversable<T> takeWhile(Predicate<? super T> predicate);

}
