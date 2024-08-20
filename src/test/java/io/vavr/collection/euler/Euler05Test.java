/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2024 Vavr, https://vavr.io
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

import io.vavr.collection.Stream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler05Test {

    /**
     * <strong>Problem 5: Smallest multiple</strong>
     * <p>
     * 2520 is the smallest number that can be divided by each of the numbers from 1
     * to 10 without any remainder.
     * <p>
     * What is the smallest positive number that is evenly divisible by all of the
     * numbers from 1 to 20?
     * <p>
     * See also <a href="https://projecteuler.net/problem=5">projecteuler.net problem 5</a>.
     */
    @Test
    public void shouldSolveProblem5() {
        assertThat(smallestPositiveNumberEvenlyDivisibleByAllNumbersFrom1To(10)).isEqualTo(2_520L);
        assertThat(smallestPositiveNumberEvenlyDivisibleByAllNumbersFrom1To(20)).isEqualTo(232_792_560L);
    }

    private static long smallestPositiveNumberEvenlyDivisibleByAllNumbersFrom1To(int max) {
        return Stream.rangeClosed(2, max)
                .map(PrimeNumbers::factorization)
                .reduce((m1, m2) -> m1.merge(m2, Math::max))
                .foldLeft(1L, (xs, x) -> xs * pow(x._1, x._2));
    }

    private static long pow(long a, long p) {
        return Stream.rangeClosed(1, p).fold(1L, (xs, x) -> xs * a);
    }
}
