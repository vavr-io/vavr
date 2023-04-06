/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
