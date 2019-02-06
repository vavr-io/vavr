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

import io.vavr.collection.Iterator;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Extension of the well-known Java {@link java.lang.Iterable} in the sense that a rich
 * Vavr {@link io.vavr.collection.Iterator} is returned by {@link #iterator()}.
 *
 * @param <T> the type of Iterator elements
 */
public interface Iterable<T> extends java.lang.Iterable<T> {

    /**
     * Returns an {@link Iterator} that allows us to perform intermediate, sequential
     * operations known from Vavr's collection library.
     *
     * @return an {@link Iterator} instance. It is not necessarily a new instance, like that returned by {@link Iterator#empty()}.
     */
    @Override
    Iterator<T> iterator();

    /**
     * Checks, if an element exists such that the predicate holds.
     *
     * @param predicate A Predicate
     * @return true, if predicate holds for one or more elements, false otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default boolean exists(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (T t : this) {
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks, if the given predicate holds for all elements.
     *
     * @param predicate A Predicate
     * @return true, if the predicate holds for all elements, false otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default boolean forAll(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return !exists(predicate.negate());
    }

    // `Iterable<T>` must not have a generic type bound, see TraversableTest ShouldJustCompile
    default <C> C to(Function<? super java.lang.Iterable<T>, C> fromIterable) {
        Objects.requireNonNull(fromIterable, "fromIterable is null");
        return fromIterable.apply(this);
    }

}
