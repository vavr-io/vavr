/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2019 Vavr, http://vavr.io
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

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface SortedMap<K, V> extends Map<K, V> {

    @Override
    <U> Traversable<U> flatMap(Function<? super Tuple2<K, V>, ? extends Iterable<? extends U>> mapper);

    @Override
    <K2, V2> Map<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<? extends Tuple2<? extends K2, ? extends V2>>> mapper);

    <K2, V2> SortedMap<K2, V2> flatMap(Comparator<? super K2> keyComparator, BiFunction<? super K, ? super V, ? extends Iterable<? extends Tuple2<? extends K2, ? extends V2>>> mapper);

    @Override
    <U> Traversable<U> map(Function<? super Tuple2<K, V>, ? extends U> mapper);

    @Override
    <K2, V2> Map<K2, V2> map(BiFunction<? super K, ? super V, ? extends Tuple2<? extends K2, ? extends V2>> mapper);

    <K2, V2> SortedMap<K2, V2> map(Comparator<? super K2> keyComparator, BiFunction<? super K, ? super V, ? extends Tuple2<? extends K2, ? extends V2>> mapper);

}
