/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2018 Vavr, http://vavr.io
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
package io.vavr.control;

import java.util.Objects;

/**
 * A function that consumes an argument, returns nothing and may throw an {@link Exception}.
 * Such a function is expected to perform side-effects, i.e. altering the outer world.
 *
 * @param <T> the type of the consumed argument
 */
@FunctionalInterface
public interface CheckedConsumer<T> {

    // -- .accept(Object)

    /**
     * Consumes the given argument {@code t} by performing an action.
     *
     * @param t the input argument
     * @throws Exception if an error occurs when performing the action
     */
    void accept(T t) throws Exception;

    // -- .andThen(CheckedConsumer)

    /**
     * Returns a new {@code CheckedConsumer} that first executes this operation and then the given operation
     * {@code after}. If this operation throws an exception {@code after} is not executed.
     *
     * @param after a function that consumes a given argument after this successfully did
     * @return a new {@code CheckedConsumer} that first performs this and then {@code after}
     * @throws NullPointerException if {@code after} is null
     */
    default CheckedConsumer<T> andThen(CheckedConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return t -> { accept(t); after.accept(t); };
    }

}
