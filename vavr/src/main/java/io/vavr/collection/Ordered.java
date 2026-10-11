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

import java.util.Comparator;
import org.jspecify.annotations.Nullable;

/**
 * A collection whose elements are arranged according to a well-defined order.
 * Implementations expose the {@link Comparator} used to determine that order.
 *
 * @param <T> the type the ordering is defined on: the element type for element-ordered collections
 *            (e.g. {@code SortedSet}, {@code PriorityQueue}), or the key type for key-ordered
 *            collections (e.g. {@code SortedMap}, {@code SortedMultimap})
 * @author Ruslan Sennov, Daniel Dietrich
 */
public interface Ordered<T extends @Nullable Object> {

    /**
     * Returns the comparator that governs the ordering of this collection
     * (by element, or by key for key-value collections such as sorted maps).
     * The returned comparator must be consistent with the collection's iteration order.
     *
     * @return the comparator defining the order
     */
    Comparator<T> comparator();
}
