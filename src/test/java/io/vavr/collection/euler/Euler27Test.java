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

import io.vavr.API;
import io.vavr.Tuple;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.junit.Test;

import static io.vavr.API.For;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 27: Quadratic primes</strong>
 * <p>
 * Euler discovered the remarkable quadratic formula:
 * <p>
 * n² + n + 41
 * <p>
 * It turns out that the formula will produce 40 primes for the consecutive
 * values n = 0 to 39. However, when n = 40, 40^2 + 40 + 41 = 40(40 + 1) + 41 is
 * divisible by 41, and certainly when n = 41, 41² + 41 + 41 is clearly
 * divisible by 41.
 * <p>
 * The incredible formula n² − 79n + 1601 was discovered, which produces 80
 * primes for the consecutive values n = 0 to 79. The product of the
 * coefficients, −79 and 1601, is −126479.
 * <p>
 * Considering quadratics of the form:
 * <p>
 * n² + an + b, where |a| < 1000 and |b| < 1000 <p>
 * where |n| is the modulus/absolute value of n e.g. |11| = 11 and |−4| = 4
 * <p>
 * Find the product of the coefficients, a and b, for the quadratic expression
 * that produces the maximum number of primes for consecutive values of n,
 * starting with n = 0.
 * <p>
 * See also
 * <a href="https://projecteuler.net/problem=27">projecteuler.net problem 27
 * </a>.
 */
public class Euler27Test {

    @Test
    public void shouldSolveProblem27() {
        assertThat(numberOfConsecutivePrimesProducedByFormulaWithCoefficients(1, 41)).isEqualTo(40);
        assertThat(numberOfConsecutivePrimesProducedByFormulaWithCoefficients(-79, 1601)).isEqualTo(80);

        assertThat(productOfCoefficientsWithMostConsecutivePrimes(-999, 999)).isEqualTo(-59231);
    }

    private static int productOfCoefficientsWithMostConsecutivePrimes(int coefficientsLowerBound, int coefficientsUpperBound) {
        final Iterable<Integer> coefficients = List.rangeClosed(coefficientsLowerBound, coefficientsUpperBound);
        return API.For(coefficients, coefficients)
                .yield(Tuple::of)
                .map(c -> Tuple.of(c._1, c._2, numberOfConsecutivePrimesProducedByFormulaWithCoefficients(c._1, c._2)))
                .fold(Tuple.of(0, 0, -1), (n, m) -> n._3 >= m._3 ? n : m)
                .apply((a, b, p) -> a * b);
    }

    private static int numberOfConsecutivePrimesProducedByFormulaWithCoefficients(int a, int b) {
        return Iterator.from(0L)
                .map(n -> (long) Math.pow(n, 2) + a * n + b)
                .takeWhile(Utils.MEMOIZED_IS_PRIME::apply)
                .length();
    }
}
