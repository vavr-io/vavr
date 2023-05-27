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
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler06Test {

    /**
     * <strong>Problem 6: Sum square difference</strong>
     * <p>
     * The sum of the squares of the first ten natural numbers is,
     * <p>
     * 1² + 2² + ... + 10² = 385
     * <p>
     * The square of the sum of the first ten natural
     * numbers is,
     * <p>
     * (1 + 2 + ... + 10)² = 55² = 3025
     * <p>
     * Hence the difference between the sum of the
     * squares of the first ten natural numbers and the square of the sum is
     * <p>
     * 3025 − 385 = 2640.
     * <p>
     * Find the difference between the sum of the squares of the first one hundred
     * natural numbers and the square of the sum.
     * <p>
     * See also <a href="https://projecteuler.net/problem=6">projecteuler.net problem 6</a>.
     */
    @Test
    public void shouldSolveProblem6() {
        assertThat(differenceBetweenSumOfTheSquaresAndSquareOfTheSumFrom1UpTo(10)).isEqualTo(2640L);
        assertThat(differenceBetweenSumOfTheSquaresAndSquareOfTheSumFrom1UpTo(100)).isEqualTo(25164150L);
    }

    private static long differenceBetweenSumOfTheSquaresAndSquareOfTheSumFrom1UpTo(int max) {
        return squareOfSumFrom1UpTo(max) - sumOfSquaresFrom1UpTo(max);
    }

    private static long squareOfSumFrom1UpTo(int max) {
        return (long) Math.pow(Stream.rangeClosed(1, max).sum().longValue(), 2);
    }

    private static long sumOfSquaresFrom1UpTo(int max) {
        return Stream.rangeClosed(1, max)
                .map(i -> (long) Math.pow(i, 2))
                .sum().longValue();
    }
}
