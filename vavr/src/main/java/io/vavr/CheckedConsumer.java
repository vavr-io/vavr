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

/**
 * A {@linkplain java.util.function.Consumer} which may throw.
 *
 * @param <T> the type of value supplied to this consumer.
 */
@FunctionalInterface
public interface CheckedConsumer<T> {

    /**
     * Performs side-effects.
     *
     * @param value a value
     * @throws Throwable if an error occurs
     */
    void accept(T value) throws Throwable;
}
