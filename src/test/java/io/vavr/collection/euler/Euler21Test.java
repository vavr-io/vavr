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

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.collection.Stream;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 21: Amicable numbers</strong>
 *
 * <p>Let d(<i>n</i>) be defined as the sum of proper divisors of <i>n</i> (numbers less than <i>n</i> which divide evenly into <i>n</i>).<br />
 * If d(<i>a</i>) = <i>b</i> and d(<i>b</i>) = <i>a</i>, where <i>a</i> ≠ <i>b</i>, then <i>a</i> and <i>b</i> are an amicable pair and each of <i>a</i> and <i>b</i> are called amicable numbers.</p>
 * <p>For example, the proper divisors of 220 are 1, 2, 4, 5, 10, 11, 20, 22, 44, 55 and 110; therefore d(220) = 284. The proper divisors of 284 are 1, 2, 4, 71 and 142; so d(284) = 220.</p>
 * <p>Evaluate the sum of all the amicable numbers under 10000</p>
 * See also <a href="https://projecteuler.net/problem=21">projecteuler.net problem 21</a>.
 */

public class Euler21Test {

    @Test
    public void shouldSolveProblem21() {
        assertThat(sumOfDivisors(220)).isEqualTo(1 + 2 + 4 + 5 + 10 + 11 + 20 + 22 + 44 + 55 + 110);
        assertThat(sumOfDivisors(284)).isEqualTo(1 + 2 + 4 + 71 + 142);
        assertThat(sumOfAmicablePairs(10000)).isEqualTo(31626);
    }

    private static int sumOfDivisors(int n) {
        return 1 + Stream.rangeClosed(2, (int) Math.sqrt(n))
                .map(d -> Tuple.of(d, n / d))
                .filter(t -> t._1 * t._2 == n && !Objects.equals(t._1, t._2))
                .map(t -> t._1 + t._2)
                .foldLeft(0, (sum, x) -> sum + x);
    }

    private static int sumOfAmicablePairs(int n) {
        final Function1<Integer, Integer> mSumOfDivisors = Function1.of(Euler21Test::sumOfDivisors).memoized();
        return Stream.range(1, n)
                .filter(x -> mSumOfDivisors.apply(mSumOfDivisors.apply(x)).intValue() == x && mSumOfDivisors.apply(x) > x)
                .foldLeft(0, (sum, x) -> sum + x + mSumOfDivisors.apply(x));
    }

}
