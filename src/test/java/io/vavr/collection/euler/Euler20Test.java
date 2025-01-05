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

import io.vavr.collection.CharSeq;
import io.vavr.collection.Stream;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 20:Factorial digit sum</strong>
 *
 * <p><i>n</i>! means <i>n</i> × (<i>n</i> − 1) × ... × 3 × 2 × 1</p>
 * <p>For example, 10! = 10 × 9 × ... × 3 × 2 × 1 = 3628800,<br />and the sum of the digits in the number 10! is 3 + 6 + 2 + 8 + 8 + 0 + 0 = 27.</p>
 * <p>Find the sum of the digits in the number 100!</p>
 *
 * See also <a href="https://projecteuler.net/problem=20">projecteuler.net problem 20</a>.
 */

public class Euler20Test {

    @SuppressWarnings("PointlessArithmeticExpression")
    @Test
    public void shouldSolveProblem20() {
        assertThat(sumOfFactorialDigits(10)).isEqualTo(3 + 6 + 2 + 8 + 8 + 0 + 0);
        assertThat(sumOfFactorialDigits(100)).isEqualTo(9 + 3 + 3 + 2 + 6 + 2 + 1 + 5 + 4 + 4 + 3 + 9 + 4 + 4 + 1 + 5 + 2 + 6 + 8 + 1 + 6 + 9 + 9 + 2 + 3 + 8 + 8 + 5 + 6 + 2 + 6 + 6 + 7 + 0 + 0 + 4 + 9 + 0 + 7 + 1 + 5 + 9 + 6 + 8 + 2 + 6 + 4 + 3 + 8 + 1 + 6 + 2 + 1 + 4 + 6 + 8 + 5 + 9 + 2 + 9 + 6 + 3 + 8 + 9 + 5 + 2 + 1 + 7 + 5 + 9 + 9 + 9 + 9 + 3 + 2 + 2 + 9 + 9 + 1 + 5 + 6 + 0 + 8 + 9 + 4 + 1 + 4 + 6 + 3 + 9 + 7 + 6 + 1 + 5 + 6 + 5 + 1 + 8 + 2 + 8 + 6 + 2 + 5 + 3 + 6 + 9 + 7 + 9 + 2 + 0 + 8 + 2 + 7 + 2 + 2 + 3 + 7 + 5 + 8 + 2 + 5 + 1 + 1 + 8 + 5 + 2 + 1 + 0 + 9 + 1 + 6 + 8 + 6 + 4 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0);
    }

    private static BigInteger factorial(int n) {
        return Stream.rangeClosed(1, n)
                .map(BigInteger::valueOf)
                .reduce(BigInteger::multiply);
    }

    private static int sumOfFactorialDigits(int n) {
        return CharSeq.of(factorial(n).toString())
                .foldLeft(0, (sum, c) -> sum + Character.digit(c, 10));
    }

}
