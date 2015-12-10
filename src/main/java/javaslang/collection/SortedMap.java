/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.NoSuchElementException;
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

    /**
     * Returns the underlying key-comparator which defines the order of the elements contained in this map.
     *
     * @return This map's key-comparator.
     */
    Comparator<K> keyComparator();

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
    <U, W> SortedMap<U, W> flatMap(Comparator<? super U> keyComparator, BiFunction<? super K, ? super V, ? extends Iterable<? extends Tuple2<? extends U, ? extends W>>> mapper);

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
    <U, W> SortedMap<U, W> map(Comparator<? super U> keyComparator, BiFunction<? super K, ? super V, ? extends Tuple2<? extends U, ? extends W>> mapper);

    // -- Adjusted return types of Map methods

    @Override
    SortedMap<K, V> clear();

    @Override
    SortedMap<K, V> distinct();

    @Override
    SortedMap<K, V> distinctBy(Comparator<? super Tuple2<K, V>> comparator);

    @Override
    <U> SortedMap<K, V> distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor);

    @Override
    SortedMap<K, V> drop(int n);

    @Override
    SortedMap<K, V> dropRight(int n);

    @Override
    SortedMap<K, V> dropUntil(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> dropWhile(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> filter(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    <U, W> SortedMap<U, W> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<? extends Tuple2<? extends U, ? extends W>>> mapper);

    @Override
    <C> Map<C, ? extends SortedMap<K, V>> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier);

    @Override
    Iterator<? extends SortedMap<K, V>> grouped(int size);

    @Override
    SortedMap<K, V> init();

    @Override
    Option<? extends SortedMap<K, V>> initOption();

    @Override
    SortedSet<K> keySet();

    @Override
    default Tuple2<K, V> last() {
        return max().orElseThrow(() -> new NoSuchElementException("last on empty SortedMap"));
    }

    @Override
    <U> Seq<U> map(Function<? super Tuple2<K, V>, ? extends U> mapper);

    @Override
    <U, W> SortedMap<U, W> map(BiFunction<? super K, ? super V, ? extends Tuple2<? extends U, ? extends W>> mapper);

    @Override
    <W> SortedMap<K, W> mapValues(Function<? super V, ? extends W> mapper);

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
    Iterator<? extends SortedMap<K, V>> sliding(int size);

    @Override
    Iterator<? extends SortedMap<K, V>> sliding(int size, int step);

    @Override
    Tuple2<? extends SortedMap<K, V>, ? extends SortedMap<K, V>> span(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> tail();

    @Override
    Option<? extends SortedMap<K, V>> tailOption();

    @Override
    SortedMap<K, V> take(int n);

    @Override
    SortedMap<K, V> takeRight(int n);

    @Override
    SortedMap<K, V> takeUntil(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> takeWhile(Predicate<? super Tuple2<K, V>> predicate);

}
