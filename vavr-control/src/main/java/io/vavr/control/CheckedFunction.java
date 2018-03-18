/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
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
package io.vavr.control;

import java.util.Objects;

/**
 * A {@linkplain java.util.function.Function} which may throw.
 *
 * @param <T> the type of this function's domain
 * @param <R> the type of this function's codomain, i.e. the return type
 */
@FunctionalInterface
public interface CheckedFunction<T, R> {

    /**
     * Returns the identity {@code CheckedFunction}, i.e. the function that returns its input.
     *
     * @param <T> argument type (and return type) of the identity function
     * @return the identity {@code CheckedFunction}
     */
    static <T> CheckedFunction<T, T> identity() {
        return t -> t;
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param t argument of type {@code T}
     * @return the result of the function application
     * @throws Exception if something goes wrong applying this function to the given argument
     */
    R apply(T t) throws Exception;

    /**
     * Returns a composed function that first applies this to the given argument and then applies
     * {@code after} to the result.
     *
     * @param <U> return type of after
     * @param after the function applied after this
     * @return a function composed of this and {@code after}
     * @throws NullPointerException if {@code after} is null
     */
    default <U> CheckedFunction<T, U> andThen(CheckedFunction<? super R, ? extends U> after) {
        Objects.requireNonNull(after);
        return t -> after.apply(apply(t));
    }

    /**
     * Returns a composed function that first applies {@code before} to the given argument and then applies this
     * to the result.
     *
     * @param <U> argument type of before
     * @param before the function applied before this
     * @return a function composed of {@code before} and this
     * @throws NullPointerException if {@code before} is null
     */
    default <U> CheckedFunction<U, R> compose(CheckedFunction<? super U, ? extends T> before) {
        Objects.requireNonNull(before);
        return u -> apply(before.apply(u));
    }

}
