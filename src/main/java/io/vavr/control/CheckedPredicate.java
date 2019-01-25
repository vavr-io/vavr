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
package io.vavr.control;

import java.util.Objects;

/**
 * A {@linkplain java.util.function.Predicate} which may throw.
 *
 * @param <T> the type of the input to the predicate
 */
@FunctionalInterface
public interface CheckedPredicate<T> {

    /**
     * Negates a given predicate by calling {@code that.negate()}.
     *
     * @param <T>  argument type of {@code that}
     * @param that a predicate
     * @return the negation of the given predicate {@code that}
     * @throws NullPointerException if the given predicate {@code that} is null
     */
    @SuppressWarnings("unchecked")
    static <T> CheckedPredicate<T> not(CheckedPredicate<? super T> that) {
        Objects.requireNonNull(that, "that is null");
        return (CheckedPredicate<T>) that.negate();
    }

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return {@code true} if the input argument matches the predicate, otherwise {@code false}
     * @throws Throwable if an error occurs
     */
    boolean test(T t) throws Throwable;

    /**
     * Combines this predicate with {@code that} predicate using logical and (&amp;&amp;).
     * 
     * @param that a {@code CheckedPredicate}
     * @return a new {@code CheckedPredicate} with {@code p1.and(p2).test(t) == true :<=> p1.test(t) && p2.test(t) == true}
     * @throws NullPointerException if the given predicate {@code that} is null
     */
    default CheckedPredicate<T> and(CheckedPredicate<? super T> that) {
        Objects.requireNonNull(that, "that is null");
        return t -> test(t) && that.test(t);
    }

    /**
     * Negates this predicate.
     *
     * @return A new {@code CheckedPredicate} with {@code p.negate().test(t) == true :<=> p.test(t) == false}
     */
    default CheckedPredicate<T> negate() {
        return t -> !test(t);
    }

    /**
     * Combines this predicate with {@code that} predicate using logical or (||).
     *
     * @param that a {@code CheckedPredicate}
     * @return a new {@code CheckedPredicate} with {@code p1.or(p2).test(t) :<=> p1.test(t) || p2.test(t)}
     * @throws NullPointerException if the given predicate {@code that} is null
     */
    default CheckedPredicate<T> or(CheckedPredicate<? super T> that) {
        Objects.requireNonNull(that, "that is null");
        return t -> test(t) || that.test(t);
    }

}
