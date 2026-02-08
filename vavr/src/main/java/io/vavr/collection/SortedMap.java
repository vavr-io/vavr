/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import io.vavr.Tuple2;
import io.vavr.control.Option;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.*;
import org.jspecify.annotations.NonNull;

/**
 * An immutable {@code SortedMap} interface.
 *
 * @param <K> Key type
 * @param <V> Value type
 * @author Daniel Dietrich
 */
public interface SortedMap<K, V> extends Map<K, V>, Ordered<K> {

    /**
     * The serial version UID for serialization.
     */
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
    <K2, V2> SortedMap<K2, V2> bimap(@NonNull Comparator<? super K2> keyComparator,
                                     @NonNull Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper);

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
    <K2, V2> SortedMap<K2, V2> flatMap(@NonNull Comparator<? super K2> keyComparator, @NonNull BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper);

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
    <K2, V2> SortedMap<K2, V2> map(@NonNull Comparator<? super K2> keyComparator, @NonNull BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper);

    // -- Adjusted return types of Map methods

    @Override
    <K2, V2> SortedMap<K2, V2> bimap(@NonNull Function<? super K, ? extends K2> keyMapper, @NonNull Function<? super V, ? extends V2> valueMapper);

    @Override
    Tuple2<V, ? extends SortedMap<K, V>> computeIfAbsent(K key, @NonNull Function<? super K, ? extends V> mappingFunction);

    @Override
    Tuple2<Option<V>, ? extends SortedMap<K, V>> computeIfPresent(K key, @NonNull BiFunction<? super K, ? super V, ? extends V> remappingFunction);

    @Override
    SortedMap<K, V> distinct();

    @Override
    SortedMap<K, V> distinctBy(@NonNull Comparator<? super Tuple2<K, V>> comparator);

    @Override
    <U> SortedMap<K, V> distinctBy(@NonNull Function<? super Tuple2<K, V>, ? extends U> keyExtractor);

    @Override
    SortedMap<K, V> drop(int n);

    @Override
    SortedMap<K, V> dropRight(int n);

    @Override
    SortedMap<K, V> dropUntil(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> dropWhile(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> filter(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> reject(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> filter(@NonNull BiPredicate<? super K, ? super V> predicate);

    @Override
    SortedMap<K, V> reject(@NonNull BiPredicate<? super K, ? super V> predicate);

    @Override
    SortedMap<K, V> filterKeys(@NonNull Predicate<? super K> predicate);

    @Override
    SortedMap<K, V> rejectKeys(@NonNull Predicate<? super K> predicate);

    @Override
    SortedMap<K, V> filterValues(@NonNull Predicate<? super V> predicate);

    @Override
    SortedMap<K, V> rejectValues(@NonNull Predicate<? super V> predicate);

    @Override
    @Deprecated
    SortedMap<K, V> removeAll(@NonNull BiPredicate<? super K, ? super V> predicate);

    @Override
    @Deprecated
    SortedMap<K, V> removeKeys(@NonNull Predicate<? super K> predicate);

    @Override
    @Deprecated
    SortedMap<K, V> removeValues(@NonNull Predicate<? super V> predicate);

    @Override
    <K2, V2> SortedMap<K2, V2> flatMap(@NonNull BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper);

    @Override
    <C> Map<C, ? extends SortedMap<K, V>> groupBy(@NonNull Function<? super Tuple2<K, V>, ? extends C> classifier);

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
    <K2, V2> SortedMap<K2, V2> map(@NonNull BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper);

    @Override
    <K2> SortedMap<K2, V> mapKeys(@NonNull Function<? super K, ? extends K2> keyMapper);

    @Override
    <K2> SortedMap<K2, V> mapKeys(@NonNull Function<? super K, ? extends K2> keyMapper, @NonNull BiFunction<? super V, ? super V, ? extends V> valueMerge);

    @Override
    <V2> SortedMap<K, V2> mapValues(@NonNull Function<? super V, ? extends V2> valueMapper);

    @Override
    SortedMap<K, V> merge(@NonNull Map<? extends K, ? extends V> that);

    @Override
    <U extends V> SortedMap<K, V> merge(@NonNull Map<? extends K, U> that, @NonNull BiFunction<? super V, ? super U, ? extends V> collisionResolution);

    @Override
    SortedMap<K, V> orElse(Iterable<? extends Tuple2<K, V>> other);

    @Override
    SortedMap<K, V> orElse(@NonNull Supplier<? extends Iterable<? extends Tuple2<K, V>>> supplier);

    @Override
    Tuple2<? extends SortedMap<K, V>, ? extends SortedMap<K, V>> partition(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> peek(@NonNull Consumer<? super Tuple2<K, V>> action);

    @Override
    SortedMap<K, V> put(K key, V value);

    @Override
    SortedMap<K, V> put(@NonNull Tuple2<? extends K, ? extends V> entry);

    @Override
    <U extends V> SortedMap<K, V> put(K key, U value, @NonNull BiFunction<? super V, ? super U, ? extends V> merge);

    @Override
    <U extends V> SortedMap<K, V> put(@NonNull Tuple2<? extends K, U> entry, @NonNull BiFunction<? super V, ? super U, ? extends V> merge);

    @Override
    SortedMap<K, V> remove(K key);

    @Override
    SortedMap<K, V> removeAll(@NonNull Iterable<? extends K> keys);

    @Override
    SortedMap<K, V> replace(K key, V oldValue, V newValue);

    @Override
    SortedMap<K, V> replace(@NonNull Tuple2<K, V> currentElement, @NonNull Tuple2<K, V> newElement);

    @Override
    SortedMap<K, V> replaceValue(K key, V value);

    @Override
    SortedMap<K, V> replaceAll(@NonNull BiFunction<? super K, ? super V, ? extends V> function);

    @Override
    SortedMap<K, V> replaceAll(@NonNull Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    SortedMap<K, V> retainAll(@NonNull Iterable<? extends Tuple2<K, V>> elements);

    @Override
    SortedMap<K, V> scan(@NonNull Tuple2<K, V> zero, @NonNull BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation);

    @Override
    Iterator<? extends SortedMap<K, V>> slideBy(@NonNull Function<? super Tuple2<K, V>, ?> classifier);

    @Override
    Iterator<? extends SortedMap<K, V>> sliding(int size);

    @Override
    Iterator<? extends SortedMap<K, V>> sliding(int size, int step);

    @Override
    Tuple2<? extends SortedMap<K, V>, ? extends SortedMap<K, V>> span(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> tail();

    @Override
    Option<? extends SortedMap<K, V>> tailOption();

    @Override
    SortedMap<K, V> take(int n);

    @Override
    SortedMap<K, V> takeRight(int n);

    @Override
    SortedMap<K, V> takeUntil(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMap<K, V> takeWhile(@NonNull Predicate<? super Tuple2<K, V>> predicate);

    @Override
    java.util.SortedMap<K, V> toJavaMap();

}
