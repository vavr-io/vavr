/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2024 Vavr, http://vavr.io
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

import java.util.function.Predicate;

import static io.vavr.CheckedPredicateModule.sneakyThrow;

/**
 * A {@linkplain java.util.function.Predicate} which may throw.
 *
 * @param <T> the type of the input to the predicate
 */
@FunctionalInterface
public interface CheckedPredicate<T> {

    /**
     * Creates a {@code CheckedPredicate}.
     *
     * <pre>{@code
     * final CheckedPredicate<Boolean> checkedPredicate = CheckedPredicate.of(Boolean::booleanValue);
     * final Predicate<Boolean> predicate = checkedPredicate.unchecked();
     *
     * // = true
     * predicate.test(Boolean.TRUE);
     *
     * // throws
     * predicate.test(null);
     * }</pre>
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @param <T> type of values that are tested by the predicate
     * @return a new {@code CheckedPredicate}
     * @see CheckedFunction1#of(CheckedFunction1)
     */
    static <T> CheckedPredicate<T> of(CheckedPredicate<T> methodReference) {
        return methodReference;
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
     * Negates this predicate.
     *
     * @return A new CheckedPredicate.
     */
    default CheckedPredicate<T> negate() {
        return t -> !test(t);
    }

    /**
     * Returns an unchecked {@link Predicate} that will <em>sneaky throw</em> if an exceptions occurs when testing a value.
     *
     * @return a new {@link Predicate} that throws a {@code Throwable}.
     */
    default Predicate<T> unchecked() {
        return t -> {
            try {
                return test(t);
            } catch(Throwable x) {
                return sneakyThrow(x);
            }
        };
    }
}

interface CheckedPredicateModule {

    // DEV-NOTE: we do not plan to expose this as public API
    @SuppressWarnings("unchecked")
    static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }

}
