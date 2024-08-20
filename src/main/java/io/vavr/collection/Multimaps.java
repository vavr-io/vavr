/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2024 Vavr, https://vavr.io
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

import java.util.Objects;
import java.util.function.Function;

/**
 * INTERNAL: Common {@code Multimap} functions (not intended to be public).
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
