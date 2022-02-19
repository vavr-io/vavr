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
package io.vavr;

import java.util.*;

final class JavaCollections {

    private JavaCollections() {
    }

    @SuppressWarnings("unchecked")
    static <K, V> Map<K, V> javaMap(Object... pairs) {
        Objects.requireNonNull(pairs, "pairs is null");
        if ((pairs.length & 1) != 0) {
            throw new IllegalArgumentException("Odd length of key-value pairs list");
        }
        final Map<K, V> map = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            map.put((K) pairs[i], (V) pairs[i + 1]);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    static <T> Set<T> javaSet(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        final Set<T> set = new HashSet<>();
        Collections.addAll(set, elements);
        return set;
    }
}
