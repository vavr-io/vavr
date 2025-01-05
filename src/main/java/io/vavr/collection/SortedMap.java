/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2025 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.vavr.collection;

import io.vavr.Tuple2;
import io.vavr.control.Option;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.*;

/**
 * An immutable {@code SortedMap} interface.
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public interface SortedMap<K, V> extends Map<K, V>, Ordered<K> {

    long serialVersionUID = 1L;

    /**
     * Narrows a widened {@code SortedMap<? extends K, ? extends V>} to {@code SortedMap<K, V>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
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
    Tuple2<V, ? extends SortedMap<K, V>> computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction);

    @Override
    Tuple2<Option<V>, ? extends SortedMap<K, V>> computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction);

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
    SortedMap<K, V> filterNot(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> filter(BiPredicate<? super K, ? super V> predicate);

    @Override
    SortedMap<K, V> filterNot(BiPredicate<? super K, ? super V> predicate);

    @Override
    SortedMap<K, V> filterKeys(Predicate<? super K> predicate);

    @Override
    SortedMap<K, V> filterNotKeys(Predicate<? super K> predicate);

    @Override
    SortedMap<K, V> filterValues(Predicate<? super V> predicate);

    @Override
    SortedMap<K, V> filterNotValues(Predicate<? super V> predicate);

    @Override
    <K2, V2> SortedMap<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper);

    @Override
    <C> Map<C, ? extends SortedMap<K, V>> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier);

    @Override
    Iterator<? extends SortedMap<K, V>> grouped(int size);

    @Override
    SortedMap<K, V> init();

    @Override
    Option<? extends SortedMap<K, V>> initOption();

    @Override
    default boolean isOrdered() {
        return true;
    }

    @Override
    SortedSet<K> keySet();

    @Override
    default Tuple2<K, V> last() {
        return max().getOrElseThrow(() -> new NoSuchElementException("last on empty SortedMap"));
    }

    @Override
    <K2, V2> SortedMap<K2, V2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper);

    @Override
    <K2> SortedMap<K2, V> mapKeys(Function<? super K, ? extends K2> keyMapper);

    @Override
    <K2> SortedMap<K2, V> mapKeys(Function<? super K, ? extends K2> keyMapper, BiFunction<? super V, ? super V, ? extends V> valueMerge);

    @Override
    <V2> SortedMap<K, V2> mapValues(Function<? super V, ? extends V2> valueMapper);

    @Override
    SortedMap<K, V> merge(Map<? extends K, ? extends V> that);

    @Override
    <U extends V> SortedMap<K, V> merge(Map<? extends K, U> that, BiFunction<? super V, ? super U, ? extends V> collisionResolution);

    @Override
    SortedMap<K, V> orElse(Iterable<? extends Tuple2<K, V>> other);

    @Override
    SortedMap<K, V> orElse(Supplier<? extends Iterable<? extends Tuple2<K, V>>> supplier);

    @Override
    Tuple2<? extends SortedMap<K, V>, ? extends SortedMap<K, V>> partition(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> peek(Consumer<? super Tuple2<K, V>> action);

    @Override
    SortedMap<K, V> put(K key, V value);

    @Override
    SortedMap<K, V> put(Tuple2<? extends K, ? extends V> entry);

    @Override
    <U extends V> SortedMap<K, V> put(K key, U value, BiFunction<? super V, ? super U, ? extends V> merge);

    @Override
    <U extends V> SortedMap<K, V> put(Tuple2<? extends K, U> entry, BiFunction<? super V, ? super U, ? extends V> merge);

    @Override
    SortedMap<K, V> remove(K key);

    @Override
    SortedMap<K, V> removeAll(Iterable<? extends K> keys);

    @Override
    SortedMap<K, V> replace(K key, V oldValue, V newValue);

    @Override
    SortedMap<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    SortedMap<K, V> replaceValue(K key, V value);

    @Override
    SortedMap<K, V> replaceAll(BiFunction<? super K, ? super V, ? extends V> function);

    @Override
    SortedMap<K, V> replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    SortedMap<K, V> retainAll(Iterable<? extends Tuple2<K, V>> elements);

    @Override
    SortedMap<K, V> scan(Tuple2<K, V> zero, BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation);

    @Override
    Iterator<? extends SortedMap<K, V>> slideBy(Function<? super Tuple2<K, V>, ?> classifier);

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

    @Override
    java.util.SortedMap<K, V> toJavaMap();

}
