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

import io.vavr.Tuple;
import io.vavr.collection.CharSeq;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 30: Digit fifth powers</strong>
 * <p>
 * Surprisingly there are only three numbers that can be written as the sum of
 * fourth powers of their digits:
 * <pre>
 * 1634 = 1^4 + 6^4 + 3^4 + 4^4
 * 8208 = 8^4 + 2^4 + 0^4 + 8^4
 * 9474 = 9^4 + 4^4 + 7^4 + 4^4
 * </pre>
 *
 * As 1 = 1^4 is not a sum it is not included.
 * <p>
 * The sum of these numbers is 1634 + 8208 + 9474 = 19316.
 * <p>
 * Find the sum of all the numbers that can be written as the sum of fifth
 * powers of their digits.
 * <p>
 * See also
 * <a href="https://projecteuler.net/problem=30">projecteuler.net problem 30
 * </a>.
 */
public class Euler30Test {

    @Test
    public void shouldSolveProblem26() {
        assertThat(sumOfAllTheNumbersThatCanBeWrittenAsTheSumOfPowersOfTheirDigits(4)).isEqualTo(19316);
        assertThat(sumOfAllTheNumbersThatCanBeWrittenAsTheSumOfPowersOfTheirDigits(5)).isEqualTo(443_839);
    }

    private static long sumOfAllTheNumbersThatCanBeWrittenAsTheSumOfPowersOfTheirDigits(int powers) {
        return List.rangeClosed(10, maximalSumForPowers(powers))
                .filter(i -> sumOfPowersOfDigits(powers, i) == i)
                .sum().longValue();
    }

    private static long maximalSumForPowers(int powers) {
        return Stream.from(1)
                .map(i -> Tuple.of((long) Math.pow(10, i) - 1, List.fill(i, () -> Math.pow(9, powers)).sum().longValue()))
                .find(t -> t._1 > t._2)
                .map(t -> t._1).get();
    }

    private static long sumOfPowersOfDigits(int powers, long num) {
        return CharSeq.of(Long.toString(num))
                .map(c -> Character.digit(c, 10))
                .map(d -> (long) Math.pow(d, powers))
                .sum().longValue();
    }
}
