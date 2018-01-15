/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
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

/**
 * A consumer that may throw, equivalent to {@linkplain java.util.function.Consumer}.
 *
 * @param <T> the value type supplied to this consumer.
 */
@FunctionalInterface
public interface CheckedConsumer<T> {

    /**
     * Performs side-effects.
     *
     * @param t a value of type {@code T}
     * @throws Exception if an error occurs
     */
    void accept(T t) throws Exception;

    /**
     * Returns a chained {@code CheckedConsumer} that first executes {@code this.accept(t)}
     * and then {@code after.accept(t)}, for a given {@code t} of type {@code T}.
     *
     * @param after the action that will be executed after this action
     * @return a new {@code CheckedConsumer} that chains {@code this} and {@code after}
     * @throws NullPointerException if {@code after} is null
     */
    default CheckedConsumer<T> andThen(CheckedConsumer<? super T> after) {
        Objects.requireNonNull(after, "after is null");
        return (T t) -> { accept(t); after.accept(t); };
    }
}
