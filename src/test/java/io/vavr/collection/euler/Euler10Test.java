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
package io.vavr.collection.euler;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler10Test {

    /**
     * <strong>Problem 10: Summation of primes</strong>
     * <p>
     * The sum of the primes below 10 is 2 + 3 + 5 + 7 = 17.
     * <p>
     * Find the sum of all the primes below two million.
     * <p>
     * See also <a href="https://projecteuler.net/problem=10">projecteuler.net problem 10</a>.
     */
    @Test
    public void shouldSolveProblem10() {
        assertThat(sumPrimes(10)).isEqualTo(17);
        assertThat(sumPrimes(2_000_000L)).isEqualTo(142_913_828_922L);
    }

    private long sumPrimes(long max) {
        return PrimeNumbers.primes()
                .map(Long::valueOf)
                .takeWhile(t -> t < max)
                .reduce((p1, p2) -> p1 + p2);
    }
}
