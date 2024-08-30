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

import io.vavr.collection.CharSeq;
import io.vavr.collection.Stream;
import org.junit.Test;

import static io.vavr.collection.euler.Utils.MEMOIZED_FACTORIAL;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler34Test {

    /**
     * <strong>Problem 34 Digit factorials</strong>
     * <p>
     * 145 is a curious number, as 1! + 4! + 5! = 1 + 24 + 120 = 145.
     * <p>
     * Find the sum of all numbers which are equal to the sum of the factorial
     * of their digits.
     * <p>
     * F
     * Note: as 1! = 1 and 2! = 2 are not sums they are not included.
     * <p>
     * See also <a href="https://projecteuler.net/problem=34">projecteuler.net
     * problem 34</a>.
     */
    @Test
    public void shouldSolveProblem34() {
        assertThat(sumOfDigitFactorial(145)).isEqualTo(145);
        assertThat(sumOfOfAllNumbersWhichAreEqualToSumOfDigitFactorial()).isEqualTo(40730);
    }

    private static int sumOfOfAllNumbersWhichAreEqualToSumOfDigitFactorial() {
        return Stream.rangeClosed(3, 2_540_160) // 9! * 7 = 2 540 160 is a seven digit number, as is 9! * 8, therefor 9! * 7 is the definitive upper limit we have to investigate.
                .filter(i -> i == sumOfDigitFactorial(i))
                .sum().intValue();
    }

    private static int sumOfDigitFactorial(int num) {
        return CharSeq.of(Integer.toString(num))
                .map(c -> Character.digit(c, 10))
                .map(MEMOIZED_FACTORIAL)
                .sum().intValue();
    }
}
