/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.control.Option;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An immutable {@code Map} interface.
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Daniel Dietrich, Ruslan Sennov
 * @since 2.0.0
 */
public interface Map<K, V> extends Traversable<Tuple2<K, V>>, Function<K, V> {

    @Override
    default V apply(K key) {
        return get(key).orElseThrow(NoSuchElementException::new);
    }

    boolean containsKey(K key);

    boolean containsValue(V value);

    <U, W> Map<U, W> flatMap(BiFunction<? super K, ? super V, ? extends java.lang.Iterable<? extends Tuple2<? extends U, ? extends W>>> mapper);

    Option<V> get(K key);

    Set<K> keySet();

    <U, W> Map<U, W> map(BiFunction<? super K, ? super V, ? extends Tuple2<? extends U, ? extends W>> mapper);

    Map<K, V> put(K key, V value);

    /**
     * Convenience method for {@code put(entry._1, entry._2)}.
     *
     * @param entry A Map.Tuple2
     * @return A new Map containing these elements and that entry.
     */
    Map<K, V> put(Tuple2<? extends K, ? extends V> entry);

    Map<K, V> remove(K key);

    Map<K, V> removeAll(java.lang.Iterable<? extends K> keys);

    @Override
    Map<K,V> scan(Tuple2<K, V> zero,
            BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation);
    
    int size();

    default <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(BiFunction<? super K, ? super V, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return unzip(entry -> unzipper.apply(entry._1, entry._2));
    }

    default <T1, T2, T3> Tuple3<Seq<T1>, Seq<T2>, Seq<T3>> unzip3(BiFunction<? super K, ? super V, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return unzip3(entry -> unzipper.apply(entry._1, entry._2));
    }

    Seq<V> values();

    // -- Adjusted return types of Traversable methods

    @Override
    Map<K, V> clear();

    @Override
    default boolean contains(Tuple2<K, V> element) {
        return get(element._1).map(v -> Objects.equals(v, element._2)).orElse(false);
    }

    @Override
    Map<K, V> distinct();

    @Override
    Map<K, V> distinctBy(Comparator<? super Tuple2<K, V>> comparator);

    @Override
    <U> Map<K, V> distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor);

    @Override
    Map<K, V> drop(int n);

    @Override
    Map<K, V> dropRight(int n);

    @Override
    Map<K, V> dropWhile(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Map<K, V> filter(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    <U> Seq<U> flatMap(Function<? super Tuple2<K, V>, ? extends java.lang.Iterable<? extends U>> mapper);

    /**
     * Entries of a {@code Map} are flattened to the elements of their {@code Iterable} values.
     * <p>
     * <strong>Caution:</strong> Behaves different than most other {@code Value.flatten()} implementations.
     * <p>
     * Example:
     *
     * <pre><code>// = Seq(1, 2)
     * HashMap.empty()
     *     .put(0, Option.of(1))
     *     .put(1, Option.of(2))
     *     .flatten();</code></pre>
     *
     * @param <U> the component type of the resulting sequence
     * @return an indexed sequence of elements of type U
     * @throws UnsupportedOperationException if the value of a map entry {@code (key, value)} is not {@code Iterable}.
     */
    @Override
    <U> Seq<U> flatten();

    @Override
    <C> Map<C, ? extends Map<K, V>> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier);

    @Override
    Map<K, V> init();

    @Override
    Option<? extends Map<K, V>> initOption();

    @Override
    Iterator<Tuple2<K, V>> iterator();

    @Override
    int length();

    @Override
    <U> Seq<U> map(Function<? super Tuple2<K, V>, ? extends U> mapper);

    /**
     * Creates a new map which by merging the entries of {@code this} map and {@code that} map.
     * <p>
     * If collisions occur, the value of {@code this} map is taken.
     *
     * @param that the other map
     * @return A merged map
     * @throws NullPointerException if that map is null
     */
    Map<K, V> merge(Map<? extends K, ? extends V> that);

    /**
     * Creates a new map which by merging the entries of {@code this} map and {@code that} map.
     * <p>
     * Uses the specified collision resolution function if two keys are the same.
     * The collision resolution function will always take the first argument from <code>this</code> map
     * and the second from <code>that</code> map.
     *
     * @param <U>                 value type of that Map
     * @param that                the other map
     * @param collisionResolution the collision resolution function
     * @return A merged map
     * @throws NullPointerException if that map or the given collision resolution function is null
     */
    <U extends V> Map<K, V> merge(Map<? extends K, U> that, BiFunction<? super V, ? super U, ? extends V> collisionResolution);

    @Override
    Tuple2<? extends Map<K, V>, ? extends Map<K, V>> partition(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Map<K, V> peek(Consumer<? super Tuple2<K, V>> action);

    @Override
    Map<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    Map<K, V> replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    Map<K, V> retainAll(java.lang.Iterable<? extends Tuple2<K, V>> elements);

    @Override
    Tuple2<? extends Map<K, V>, ? extends Map<K, V>> span(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    default Spliterator<Tuple2<K, V>> spliterator() {
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    Map<K, V> tail();

    @Override
    Option<? extends Map<K, V>> tailOption();

    @Override
    Map<K, V> take(int n);

    @Override
    Map<K, V> takeRight(int n);

    @Override
    Map<K, V> takeUntil(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    Map<K, V> takeWhile(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    default <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(Function<? super Tuple2<K, V>, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip(unzipper).map(Stream::ofAll, Stream::ofAll);
    }
    
    @Override
    default <T1, T2, T3> Tuple3<Seq<T1>, Seq<T2>, Seq<T3>> unzip3(
    		Function<? super Tuple2<K,V>, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip3(unzipper).map(Stream::ofAll, Stream::ofAll, Stream::ofAll);
    }

    @Override
    default <U> Seq<Tuple2<Tuple2<K, V>, U>> zip(java.lang.Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        return Stream.ofAll(iterator().zip(that));
    }

    @Override
    default <U> Seq<Tuple2<Tuple2<K, V>, U>> zipAll(java.lang.Iterable<U> that, Tuple2<K, V> thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return Stream.ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    default Seq<Tuple2<Tuple2<K, V>, Integer>> zipWithIndex() {
        return Stream.ofAll(iterator().zipWithIndex());
    }

}
