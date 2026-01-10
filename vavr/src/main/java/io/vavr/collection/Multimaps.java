/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
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
import java.util.Objects;
import java.util.function.Function;

/**
 * INTERNAL: Common {@code Multimap} functions (not intended to be public).
 *
 * @author Ruslan Sennov, Daniel Dietrich
 */
class Multimaps {

    @SuppressWarnings("unchecked")
    static <K, V, M extends Multimap<K, V>> M ofJavaMap(M source, java.util.Map<? extends K, ? extends V> map) {
        Objects.requireNonNull(map, "map is null");
        return Stream.ofAll(map.entrySet()).foldLeft(source, (m, el) -> (M) m.put(el.getKey(), el.getValue()));
    }

    @SuppressWarnings("unchecked")
    static <T, K, V, M extends Multimap<K, V>> M ofStream(M source, java.util.stream.Stream<? extends T> stream,
                                                     Function<? super T, ? extends K> keyMapper,
                                                     Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(stream, "stream is null");
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return Stream.ofAll(stream).foldLeft(source, (m, el) -> (M) m.put(keyMapper.apply(el), valueMapper.apply(el)));
    }

    @SuppressWarnings("unchecked")
    static <T, K, V, M extends Multimap<K, V>> M ofStream(M source, java.util.stream.Stream<? extends T> stream,
                                                     Function<? super T, Tuple2<? extends K, ? extends V>> entryMapper) {
        Objects.requireNonNull(stream, "stream is null");
        Objects.requireNonNull(entryMapper, "entryMapper is null");
        return Stream.ofAll(stream).foldLeft(source, (m, el) -> (M) m.put(entryMapper.apply(el)));
    }
}
