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
package io.vavr;

import java.util.Objects;
import java.util.function.Function;

/**
 * Extension of the well-known Java {@link java.lang.Iterable} in the sense that a rich
 * Vavr {@link io.vavr.collection.Iterator} is returned by {@link #iterator()}.
 *
 * @param <T> the element type
 */
public interface Iterable<T> extends java.lang.Iterable<T> {

    /**
     * Returns a rich {@code Iterator} that allows us to perform intermediate, sequential
     * operations known from Vavr's collection library.
     *
     * @return an {@link io.vavr.collection.Iterator} instance (that might be a singleton in the empty case)
     */
    @Override
    io.vavr.collection.Iterator<T> iterator();

    /**
     * A generic conversion function.
     * <p>
     * Example:
     *
     * <pre>{@code
     * List<Integer> list = Option.some(1).to(List::ofAll);
     * }</pre>
     *
     * @param fromIterable A function that converts a {@link java.lang.Iterable} to some type {@code C}.
     * @param <C> the target type of the conversion
     * @return a new instance of type {@code C} that must not be {@code null} per contract
     */
    // `Iterable<T>` must not have a generic type bound, see TraversableTest ShouldJustCompile
    default <C> C to(Function<? super java.lang.Iterable<T>, C> fromIterable) {
        Objects.requireNonNull(fromIterable, "fromIterable is null");
        return fromIterable.apply(this);
    }

}
