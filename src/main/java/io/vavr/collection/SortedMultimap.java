/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2022 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

import java.util.Collection;
import java.util.Comparator;
import java.util.function.*;

/**
 * An immutable {@code SortedMultimap} interface.
 *
 * @param <K> Key type
 * @param <V> Value type
 * @deprecated marked for removal from vavr core lib, might be moved to an extended collections module
 */
@Deprecated
public interface SortedMultimap<K, V> extends Multimap<K, V>, Ordered<K> {

    long serialVersionUID = 1L;

    /**
     * Narrows a widened {@code SortedMultimap<? extends K, ? extends V>} to {@code SortedMultimap<K, V>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param map A {@code SortedMultimap}.
     * @param <K> Key type
     * @param <V> Value type
     * @return the given {@code multimap} instance as narrowed type {@code SortedMultimap<K, V>}.
     */
    @SuppressWarnings("unchecked")
    static <K, V> SortedMultimap<K, V> narrow(SortedMultimap<? extends K, ? extends V> map) {
        return (SortedMultimap<K, V>) map;
    }

    @Override
    SortedMultimap<K, V> filter(BiPredicate<? super K, ? super V> predicate);

    @Override
    SortedMultimap<K, V> filterNot(BiPredicate<? super K, ? super V> predicate);

    @Override
    SortedMultimap<K, V> filterKeys(Predicate<? super K> predicate);

    @Override
    SortedMultimap<K, V> filterNotKeys(Predicate<? super K> predicate);

    @Override
    SortedMultimap<K, V> filterValues(Predicate<? super V> predicate);

    @Override
    SortedMultimap<K, V> filterNotValues(Predicate<? super V> predicate);

    @Override
    SortedSet<K> keySet();

    @Override
    SortedMultimap<K, V> merge(Multimap<? extends K, ? extends V> that);

    @Override
    <K2 extends K, V2 extends V> SortedMultimap<K, V> merge(Multimap<K2, V2> that, BiFunction<Traversable<V>, Traversable<V2>, Traversable<V>> collisionResolution);

    @Override
    SortedMultimap<K, V> put(K key, V value);

    @Override
    SortedMultimap<K, V> put(Tuple2<? extends K, ? extends V> entry);

    @Override
    SortedMultimap<K, V> remove(K key);

    @Override
    SortedMultimap<K, V> remove(K key, V value);

    @Override
    SortedMultimap<K, V> removeAll(Iterable<? extends K> keys);

    @Override
    java.util.SortedMap<K, Collection<V>> toJavaMap();

    @Override
    SortedMultimap<K, V> distinct();

    @Override
    SortedMultimap<K, V> distinctBy(Comparator<? super Tuple2<K, V>> comparator);

    @Override
    <U> SortedMultimap<K, V> distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor);

    @Override
    SortedMultimap<K, V> drop(int n);

    @Override
    SortedMultimap<K, V> dropRight(int n);

    @Override
    SortedMultimap<K, V> dropUntil(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMultimap<K, V> dropWhile(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMultimap<K, V> filter(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMultimap<K, V> filterNot(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    <C> Map<C, ? extends SortedMultimap<K, V>> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier);

    @Override
    Iterator<? extends SortedMultimap<K, V>> grouped(int size);

    @Override
    SortedMultimap<K, V> init();

    @Override
    Option<? extends SortedMultimap<K, V>> initOption();

    @Override
    SortedMultimap<K, V> orElse(Iterable<? extends Tuple2<K, V>> other);

    @Override
    SortedMultimap<K, V> orElse(Supplier<? extends Iterable<? extends Tuple2<K, V>>> supplier);

    @Override
    Tuple2<? extends SortedMultimap<K, V>, ? extends SortedMultimap<K, V>> partition(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMultimap<K, V> peek(Consumer<? super Tuple2<K, V>> action);

    @Override
    SortedMultimap<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    SortedMultimap<K, V> replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement);

    @Override
    SortedMultimap<K, V> replaceValue(K key, V value);

    @Override
    SortedMultimap<K, V> replace(K key, V oldValue, V newValue);

    @Override
    SortedMultimap<K, V> replaceAll(BiFunction<? super K, ? super V, ? extends V> function);

    @Override
    SortedMultimap<K, V> retainAll(Iterable<? extends Tuple2<K, V>> elements);

    @Override
    SortedMultimap<K, V> scan(Tuple2<K, V> zero,
                              BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation);

    @Override
    Iterator<? extends SortedMultimap<K, V>> slideBy(Function<? super Tuple2<K, V>, ?> classifier);

    @Override
    Iterator<? extends SortedMultimap<K, V>> sliding(int size);

    @Override
    Iterator<? extends SortedMultimap<K, V>> sliding(int size, int step);

    @Override
    Tuple2<? extends SortedMultimap<K, V>, ? extends SortedMultimap<K, V>> span(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMultimap<K, V> tail();

    @Override
    Option<? extends SortedMultimap<K, V>> tailOption();

    @Override
    SortedMultimap<K, V> take(int n);

    @Override
    SortedMultimap<K, V> takeRight(int n);

    @Override
    SortedMultimap<K, V> takeUntil(Predicate<? super Tuple2<K, V>> predicate);

    @Override
    SortedMultimap<K, V> takeWhile(Predicate<? super Tuple2<K, V>> predicate);

}
