/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2025 Vavr, https://vavr.io
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
package io.vavr.collection.euler;

import io.vavr.Function1;
import io.vavr.collection.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class Euler14Test {

    /**
     * <strong>Problem 14: Longest Collatz sequence</strong>
     * <p>
     * The following iterative sequence is defined for the set of positive integers:
     * <pre>
     * <code>
     * n → n/2 (n is even)
     * n → 3n + 1 (n is odd)
     * </code>
     * </pre>
     * Using the rule above and starting with 13, we generate the following sequence:
     * <pre>
     * <code>
     * 13 → 40 → 20 → 10 → 5 → 16 → 8 → 4 → 2 → 1
     * </code>
     * </pre>
     * It can be seen that this sequence (starting at 13 and finishing at 1) contains 10 terms.
     * Although it has not been proved yet (Collatz Problem), it is thought that all starting numbers finish at 1.
     * <p>
     * Which starting number, under one million, produces the longest chain?
     * <p>
     * NOTE: Once the chain starts the terms are allowed to go above one million.
     * <p>
     * See also <a href="https://projecteuler.net/problem=14">projecteuler.net problem 14</a>.
     */
    @Test
    public void shouldSolveProblem14() {
        // equivalent to from(1L).take(1_000_000)
        Assertions.assertThat(Stream.from(500_000L)
                .take(500_000)
                .maxBy(collatzSequenceLength)
                .get()).isEqualTo(837799);
    }

    private final static Function1<Long, Long> collatzRecursive = n -> {
        if (n == 1) {
            return 1L;
        } else {
            if (n % 2 == 0) {
                return Euler14Test.collatzRecursive.apply(n / 2) + 1;
            } else {
                return Euler14Test.collatzRecursive.apply(3 * n + 1) + 1;
            }
        }
    };

    private final static Function1<Long, Long> collatzSequenceLength = collatzRecursive.memoized();
}
