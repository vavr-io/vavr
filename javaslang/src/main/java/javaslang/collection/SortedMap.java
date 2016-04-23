/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An immutable {@code SortedMap} interface.
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface SortedMap<K, V> extends Map<K, V> {

    long serialVersionUID = 1L;

    /**
     * Narrows a widened {@code SortedMap<? extends K, ? extends V>} to {@code SortedMap<K, V>}
     * by performing a type safe-cast. This is eligible because immutable/read-only
     * collections are covariant.
     * <p>
     * CAUTION: If {@code K} is narrowed, the underlying {@code Comparator} might fail!
     *
     * @param sortedMap A {@code SortedMap}.
     * @param <K>       Key type
     * @param <V>       Value type
     * @return the given {@code sortedMap} instance as narrowed type {@code SortedMap<K, V>}.
     */
    @SuppressWarnings("unchecked")
    static <K, V> SortedMap<K, V> narrow(SortedMap<? extends K, ? extends V> sortedMap) {
        return (SortedMap<K, V>) sortedMap;
    }

    /**
     * Same as {@link #bimap(Function, Function)}, using a specific comparator for keys of the codomain of the given
     * {@code keyMapper}.
     *
     * @param <K2>          key's component type of the map result
     * @param <V2>          value's component type of the map result
     * @param keyComparator A comparator for keys of type K2
     * @param keyMapper     a {@code Function} that maps the keys of type {@code K} to keys of type {@code K2}
     * @param valueMapper   a {@code Function} that the values of type {@code V} to values of type {@code V2}
     * @return a new {@code SortedMap}
     * @throws NullPointerException if {@code keyMapper} or {@code valueMapper} is null
     */
    <K2, V2> SortedMap<K2, V2> bimap(Comparator<? super K2> keyComparator,
                                     Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper);

    /**
     * Same as {@link #flatMap(BiFunction)} but using a specific comparator for values of the codomain of the given
     * {@code mapper}.
     *
     * @param keyComparator A comparator for keys of type U
     * @param mapper        A function which maps key/value pairs to Iterables map entries
     * @param <K2>          New key type
     * @param <V2>          New value type
     * @return A new Map instance containing mapped entries
     */
    <K2, V2> SortedMap<K2, V2> flatMap(Comparator<? super K2> keyComparator, BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper);

    /**
     * Returns the underlying key-comparator which defines the order of the elements contained in this map.
     *
     * @return This map's key-comparator.
     */
    Comparator<K> keyComparator();

    /**
     * Same as {@link #map(BiFunction)}, using a specific comparator for keys of the codomain of the given
     * {@code mapper}.
     *
     * @param keyComparator A comparator for keys of type U
     * @param <K2>          key's component type of the map result
     * @param <V2>          value's component type of the map result
     * @param mapper        a {@code Function} that maps entries of type {@code (K, V)} to entries of type {@code (K2, V2)}
     * @return a new {@code SortedMap}
     * @throws NullPointerException if {@code mapper} is null
     */
    <K2, V2> SortedMap<K2, V2> map(Comparator<? super K2> keyComparator, BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper);

    // -- Adjusted return types of Map methods

    @Override
    <K2, V2> SortedMap<K2, V2> bimap(Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper);

    @Override
    SortedMap<K, V> distinct();

    @Override
    SortedMap<K, V> distinctBy(Comparator<? super Tuple2<K, V>> comparator);

    @Override
    <U> SortedMap<K, V> distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor);

    @Override
    SortedMap<K, V> drop(long n);

    @Override
    SortedMap<K, V> dropRight(long n);

    @Override
    SortedMap<K, V> dropUntil(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> dropWhile(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> filter(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    <K2, V2> SortedMap<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper);

    @Override
    <C> Map<C, ? extends SortedMap<K, V>> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier);

    @Override
    Iterator<? extends SortedMap<K, V>> grouped(long size);

    @Override
    SortedMap<K, V> init();

    @Override
    Option<? extends SortedMap<K, V>> initOption();

    @Override
    SortedSet<K> keySet();

    /**
     * Returns a descending iterator that iterates elements in the reversed order induced by the key Comparator.
     *
     * @return
     */
    Iterator<Tuple2<K, V>> descendingIterator();

    /**
     * Returns a reverse order {@link SortedSet} of the keys contained in this map.
     * <p>
     * The returned set has the ordering equivalent to {@link Comparator#reversed()} of the key's comparator.
     *
     * @return The keys in descending order.
     */
    SortedSet<K> descendingKeySet();

    /**
     * Returns a reverse order map with the contents of this map.
     * <p>
     * The returned set has the ordering equivalent to {@link Comparator#reversed()} of the key's comparator.
     *
     * @return a reverse order map of this map
     */
    SortedMap<K, V> descendingMap();

    /**
     * Returns a map with entries whose keys range from {@code fromKey} to {@code toKey}.
     * The {@code fromInclusive} and {@code toInclusive} flags control whether the lower and upper endpoints are
     * inclusive or exclusive.
     * <p>
     * If {@code fromKey} is {@link Option#none()}, then it assumes {@link SortedMap#min()} inclusive,
     * and the {@code fromInclusive} flag is ignored.
     * <p>
     * If {@code toKey} is {@link Option#none()}, then it assumes {@link SortedMap#max()} inclusive,
     * and the {@code toInclusive} flag is ignored.
     * <p>
     * If {@code fromKey} is equal to {@code toKey}, then an empty map is returned
     * unless both {@code fromInclusive} and {@code toInclusive} are {@code true}.
     *
     * @param fromKey       the low endpoint of the keys in the returned map.
     *                      If {@link Option#none()} then it assumes {@code fromKey} is {@link SortedMap#min()} inclusive
     *                      and ignores the {@code fromInclusive} flag.
     *                      If {@code fromKey} is lower than {@link SortedMap#min()}, then the low endpoint is {@link SortedMap#min()} inclusive.
     * @param fromInclusive {@code true} if the low endpoint is included, {@code false} if excluded
     * @param toKey         the high endpoint of the keys in the returned map.
     *                      If {@link Option#none()} then it assumes {@code toKey} is {@link SortedMap#max()} inclusive
     *                      and ignores the {@code toInclusive} flag
     *                      If {@code toKey} is lower than {@link SortedMap#max()}, then the high endpoint is {@link SortedMap#max()} inclusive.
     * @param toInclusive   {@code true} if the high endpoint is included, {@code false} if excluded
     * @return a map with entries with keys between {@code fromKey} to {@code toKey}
     * @throws IllegalArgumentException If {@code fromKey} value is greater than {@code toKey} value
     */
    SortedMap<K, V> subMap(Option<K> fromKey, boolean fromInclusive, Option<K> toKey, boolean toInclusive);

    /**
     * Returns a map with entries whose keys range from {@code fromKey} to {@code toKey}.
     * The {@code fromInclusive} and {@code toInclusive} flags control whether the lower and upper endpoints are
     * inclusive or exclusive.
     * <p>
     * If {@code fromKey} is equal to {@code toKey}, then an empty map is returned
     * unless both {@code fromInclusive} and {@code toInclusive} are {@code true}.
     *
     * @param fromKey       the low endpoint of the keys in the returned map.
     *                      If {@code fromKey} is lower than {@link SortedMap#min()}, then the low endpoint is {@link SortedMap#min()} inclusive.
     * @param fromInclusive {@code true} if the low endpoint is included, {@code false} if excluded
     * @param toKey         the high endpoint of the keys in the returned map.
     *                      If {@code toKey} is lower than {@link SortedMap#max()}, then the high endpoint is {@link SortedMap#max()} inclusive.
     * @param toInclusive   {@code true} if the high endpoint is included, {@code false} if excluded
     * @return a map with entries with keys between {@code fromKey} to {@code toKey}
     * @throws IllegalArgumentException If {@code fromKey} value is greater than {@code toKey} value
     */
    SortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive);

    /**
     * Returns a map with entries whose keys range from {@code fromKey} inclusive to {@code toKey} exclusive.
     * <p>
     * If {@code fromKey} is equal to {@code toKey}, then it returns an empty map.
     *
     * @param fromKey       the low endpoint of the keys in the returned map.
     *                      If {@code fromKey} is lower than {@link SortedMap#min()}, then the low endpoint is {@link SortedMap#min()} inclusive.
     * @param toKey         the high endpoint of the keys in the returned map.
     *                      If {@code toKey} is lower than {@link SortedMap#max()}, then the high endpoint is {@link SortedMap#max()} inclusive.
     * @return a map with entries with keys between {@code fromKey} to {@code toKey}
     * @throws IllegalArgumentException If {@code fromKey} value is greater than {@code toKey} value
     */
    SortedMap<K, V> subMap(K fromKey, K toKey);

    /**
     * Returns a map whose keys are less than or equal to the given {@code toKey} if {@code inclusive} is {@code true},
     * or excluding the {@code toKey} if {@code inclusive} is {@code false}.
     *
     * @param toKey         the high endpoint of the keys in the returned map.
     * @param inclusive     {@code true} if the high endpoint is included, {@code false} if excluded
     * @return a map with entries with keys less than or equal to the given {@code toKey}
     */
    SortedMap<K, V> headMap(K toKey, boolean inclusive);

    /**
     * Returns a map whose keys are strictly less than the given {@code toKey}.
     *
     * @param toKey         the high endpoint (exclusive) of the keys in the returned map.
     * @return a map with entries with keys less than or equal to the given {@code toKey}
     */
    SortedMap<K, V> headMap(K toKey);

    /**
     * Returns a map whose keys are greater than or equal to the given {@code fromKey} if {@code inclusive} is {@code true},
     * or excluding the {@code fromKey} if {@code inclusive} is {@code false}.
     *
     * @param fromKey       the low endpoint of the keys in the returned map.
     *                      If {@code null} then all elements are returned.
     * @return a map with entries with keys greater than or equal to the given {@code fromKey}
     */
    SortedMap<K, V> tailMap(K fromKey, boolean inclusive);

    /**
     * Returns a map whose keys are greater than or equal to the given {@code fromKey} (inclusive)
     *
     * @param fromKey       the low endpoint (inclusive) of the keys in the returned map.
     *                      If {@code null} then all elements are returned.
     * @return a map with entries with keys greater than or equal to the given {@code fromKey}
     */
    SortedMap<K, V> tailMap(K fromKey);

    /**
     * Returns the element with the highest key lower than or equal to the given {@code key}, or {@link Option#none()} if not found.
     *
     * @param key the key
     * @return the floor entry for the {@code key}, or {@link Option#none()} if not found
     */
    Option<Tuple2<K, V>> floor(K key);

    /**
     * Returns the highest key lower than or equal to the given {@code key}, or {@link Option#none()} if not found.
     *
     * @param key the key
     * @return the floor for the {@code key}, or {@link Option#none()} if not found.
     */
    Option<K> floorKey(K key);

    /**
     * Returns the element with the lowest key greater than or equal to the given {@code key}, or {@link Option#none()} if not found.
     *
     * @param key the key
     * @return the ceiling entry for the {@code key}, or {@link Option#none()} if not found.
     */
    Option<Tuple2<K, V>> ceiling(K key);

    /**
     * Returns the lowest key greater than or equal to the given {@code key}, or {@link Option#none()} if not found.
     *
     * @param key the key
     * @return the ceiling for the {@code key}, or {@link Option#none()} if not found.
     */
    Option<K> ceilingKey(K key);

    /**
     * Returns the element with the highest key lower than the given {@code key}, or {@link Option#none()} if not found.
     *
     * @param key the key
     * @return the highest entry lower than the given {@code key}, or {@link Option#none()} if not found
     */
    Option<Tuple2<K, V>> lower(K key);

    /**
     * Returns the highest key lower than the given {@code key}, or {@link Option#none()} if not found.
     *
     * @param key the key
     * @return the highest key lower thant the given {@code key}, or {@link Option#none()} if not found.
     */
    Option<K> lowerKey(K key);

    /**
     * Returns the element with the lowest key higher than the given {@code key}, or {@link Option#none()} if not found.
     *
     * @param key the key
     * @return the lowest entry higher than the given {@code key}, or {@link Option#none()} if not found
     */
    Option<Tuple2<K, V>> higher(K key);

    /**
     * Returns the lowest key higher than the given {@code key}, or {@link Option#none()} if not found.
     *
     * @param key the key
     * @return the lowest key higher thant the given {@code key}, or {@link Option#none()} if not found.
     */
    Option<K> higherKey(K key);

    @Override
    <K2, V2> SortedMap<K2, V2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper);

    @Override
    <V2> SortedMap<K, V2> mapValues(Function<? super V, ? extends V2> valueMapper);

    @Override
    SortedMap<K, V> merge(Map<? extends K, ? extends V> that);

    @Override
    <U extends V> SortedMap<K, V> merge(Map<? extends K, U> that, BiFunction<? super V, ? super U, ? extends V> collisionResolution);

    @Override
    Tuple2<? extends SortedMap<K, V>, ? extends SortedMap<K, V>> partition(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> peek(Consumer<? super Tuple2<K, V>> action);

    @Override
    SortedMap<K, V> put(K key, V value);

    @Override
    SortedMap<K, V> put(Tuple2<? extends K, ? extends V> entry);

    @Override
    SortedMap<K, V> remove(K key);

    @Override
    SortedMap<K, V> removeAll(Iterable<? extends K> keys);

    @Override
    SortedMap<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    SortedMap<K, V> replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    SortedMap<K, V> retainAll(Iterable<? extends Tuple2<K, V>> elements);

    @Override
    SortedMap<K, V> scan(Tuple2<K, V> zero, BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation);

    @Override
    Iterator<? extends SortedMap<K, V>> sliding(long size);

    @Override
    Iterator<? extends SortedMap<K, V>> sliding(long size, long step);

    @Override
    Tuple2<? extends SortedMap<K, V>, ? extends SortedMap<K, V>> span(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> tail();

    @Override
    Option<? extends SortedMap<K, V>> tailOption();

    @Override
    SortedMap<K, V> take(long n);

    @Override
    SortedMap<K, V> takeRight(long n);

    @Override
    SortedMap<K, V> takeUntil(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> takeWhile(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    java.util.SortedMap<K, V> toJavaMap();

}
