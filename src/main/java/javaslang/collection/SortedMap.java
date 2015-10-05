/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.*;

/**
 * An immutable {@code SortedMap} interface.
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface SortedMap<K, V> extends Map<K, V> {

    /**
     * Returns the underlying key-comparator which defines the order of the elements contained in this map.
     *
     * @return This map's key-comparator.
     */
    Comparator<? super K> keyComparator();

    /**
     * Same as {@link #flatMap(BiFunction)} but using a specific comparator for values of the codomain of the given
     * {@code mapper}.
     *
     * @param keyComparator A comparator for keys of type U
     * @param mapper        A function which maps key/value pairs to Iterables map entries
     * @param <U>           New key type
     * @param <W>           New value type
     * @return A new Map instance containing mapped entries
     */
    <U, W> SortedMap<U, W> flatMap(Comparator<? super U> keyComparator, BiFunction<? super K, ? super V, ? extends java.lang.Iterable<? extends Entry<? extends U, ? extends W>>> mapper);

    /**
     * Same as {@link #map(BiFunction)} but using a specific comparator for values of the codomain of the given
     * {@code mapper}.
     *
     * @param keyComparator A comparator for keys of type U
     * @param mapper        A function which maps key/value pairs to map entries
     * @param <U>           New key type
     * @param <W>           New value type
     * @return A new Map instance containing mapped entries
     */
    <U, W> SortedMap<U, W> map(Comparator<? super U> keyComparator, BiFunction<? super K, ? super V, ? extends Entry<? extends U, ? extends W>> mapper);

    // -- Adjusted return types of Map methods

    @Override
    SortedMap<K, V> clear();

    @Override
    SortedMap<K, V> distinct();

    @Override
    SortedMap<K, V> distinctBy(Comparator<? super Entry<K, V>> comparator);

    @Override
    <U> SortedMap<K, V> distinctBy(Function<? super Entry<K, V>, ? extends U> keyExtractor);

    @Override
    SortedMap<K, V> drop(int n);

    @Override
    SortedMap<K, V> dropRight(int n);

    @Override
    SortedMap<K, V> dropWhile(Predicate<? super Entry<K, V>> predicate);

    @Override
    SortedSet<Entry<K, V>> entrySet();

    @Override
    SortedMap<K, V> filter(Predicate<? super Entry<K, V>> predicate);

    @Override
    <U> Seq<U> flatMap(Function<? super Entry<K, V>, ? extends java.lang.Iterable<? extends U>> mapper);

    @Override
    <U, W> SortedMap<U, W> flatMap(BiFunction<? super K, ? super V, ? extends java.lang.Iterable<? extends Entry<? extends U, ? extends W>>> mapper);

    @Override
    SortedMap<Object, Object> flatten();

    @Override
    <C> Map<C, ? extends SortedMap<K, V>> groupBy(Function<? super Entry<K, V>, ? extends C> classifier);

    @Override
    Entry<K, V> head();

    @Override
    SortedMap<K, V> init();

    @Override
    Option<? extends SortedMap<K, V>> initOption();

    @Override
    SortedSet<K> keySet();

    @Override
    default Entry<K, V> last() {
        return max().orElseThrow(() -> new NoSuchElementException("last on empty SortedMap"));
    }

    @Override
    <U> Seq<U> map(Function<? super Entry<K, V>, ? extends U> mapper);

    @Override
    <U, W> SortedMap<U, W> map(BiFunction<? super K, ? super V, ? extends Entry<? extends U, ? extends W>> mapper);

    @Override
    SortedMap<K, V> merge(Map<? extends K, ? extends V> that);

    @Override
    <U extends V> SortedMap<K, V> merge(Map<? extends K, U> that, BiFunction<? super V, ? super U, ? extends V> collisionResolution);

    @Override
    Tuple2<? extends SortedMap<K, V>, ? extends SortedMap<K, V>> partition(Predicate<? super Entry<K, V>> predicate);

    @Override
    SortedMap<K, V> peek(Consumer<? super Entry<K, V>> action);

    @Override
    SortedMap<K, V> put(K key, V value);

    @Override
    SortedMap<K, V> put(Entry<? extends K, ? extends V> entry);

    @Override
    SortedMap<K, V> put(Tuple2<? extends K, ? extends V> entry);

    @Override
    SortedMap<K, V> remove(K key);

    @Override
    SortedMap<K, V> removeAll(java.lang.Iterable<? extends K> keys);

    @Override
    SortedMap<K, V> replace(Entry<K, V> currentElement, Entry<K, V> newElement);

    @Override
    SortedMap<K, V> replaceAll(Entry<K, V> currentElement, Entry<K, V> newElement);

    @Override
    SortedMap<K, V> replaceAll(UnaryOperator<Entry<K, V>> operator);

    @Override
    SortedMap<K, V> retainAll(java.lang.Iterable<? extends Entry<K, V>> elements);

    @Override
    Tuple2<? extends SortedMap<K, V>, ? extends SortedMap<K, V>> span(Predicate<? super Entry<K, V>> predicate);

    @Override
    SortedMap<K, V> tail();

    @Override
    Option<? extends SortedMap<K, V>> tailOption();

    @Override
    SortedMap<K, V> take(int n);

    @Override
    SortedMap<K, V> takeRight(int n);

    @Override
    SortedMap<K, V> takeUntil(Predicate<? super Entry<K, V>> predicate);

    @Override
    SortedMap<K, V> takeWhile(Predicate<? super Entry<K, V>> predicate);

    @Override
    Seq<V> values();

}
