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
import io.vavr.collection.Seq;
import io.vavr.collection.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler43Test {

    /**
     * <strong>Problem 43 Sub-string divisibility</strong>
     * <p>
     * The number, 1406357289, is a 0 to 9 pandigital number because it is made
     * up of each of the digits 0 to 9 in some order, but it also has a rather
     * interesting sub-string divisibility property.</p>
     * <p>
     * Let <i>d</i><sub>1</sub> be the 1<sup>st</sup> digit,
     * <i>d</i><sub>2</sub> be the 2<sup>nd</sup> digit, and so on. In this way,
     * we note the following:</p>
     * <pre>
     * <ul>
     * <li><i>d</i><sub>2</sub><i>d</i><sub>3</sub><i>d</i><sub>4</sub>=406 is divisible by 2</li>
     * <li><i>d</i><sub>3</sub><i>d</i><sub>4</sub><i>d</i><sub>5</sub>=063 is divisible by 3</li>
     * <li><i>d</i><sub>4</sub><i>d</i><sub>5</sub><i>d</i><sub>6</sub>=635 is divisible by 5</li>
     * <li><i>d</i><sub>5</sub><i>d</i><sub>6</sub><i>d</i><sub>7</sub>=357 is divisible by 7</li>
     * <li><i>d</i><sub>6</sub><i>d</i><sub>7</sub><i>d</i><sub>8</sub>=572 is divisible by 11</li>
     * <li><i>d</i><sub>7</sub><i>d</i><sub>8</sub><i>d</i><sub>9</sub>=728 is divisible by 13</li>
     * <li><i>d</i><sub>8</sub><i>d</i><sub>9</sub><i>d</i><sub>10</sub>=289 is divisible by 17</li>
     * </ul>
     * </pre>
     * <p>
     * Find the sum of all 0 to 9 pandigital numbers with this property.
     * <p>
     * See also <a href="https://projecteuler.net/problem=43">projecteuler.net
     * problem 43</a>.
     */
    @Test
    public void shouldSolveProblem43() {
        final Seq<Long> result = tenDigitPandigitalsWithProperty();
        Assertions.assertThat(result).contains(1406357289L);

        assertThat(result.sum().longValue()).isEqualTo(16695334890L);
    }

    private static Seq<Long> tenDigitPandigitalsWithProperty() {
        final CharSeq ALL_DIGITS = CharSeq.of("0123456789");
        final List<Integer> DIVISORS = List.of(2, 3, 5, 7, 11, 13, 17);

        return ALL_DIGITS
                .combinations(2)
                .flatMap(CharSeq::permutations)
                .flatMap(firstTwoDigits -> DIVISORS
                        .foldLeft(List.of(firstTwoDigits), (accumulator, divisor) -> accumulator
                                .flatMap(digitsSoFar -> ALL_DIGITS
                                        .removeAll(digitsSoFar)
                                        .map(nextDigit -> digitsSoFar.append(nextDigit))
                                )
                                .filter(digitsToTest -> digitsToTest.takeRight(3).parseInt() % divisor == 0)
                        )
                )
                .map(tailDigitsWithProperty -> tailDigitsWithProperty
                        .prepend(ALL_DIGITS
                                .removeAll(tailDigitsWithProperty)
                                .head()
                        )
                )
                .map(CharSeq::parseLong);
    }
}
