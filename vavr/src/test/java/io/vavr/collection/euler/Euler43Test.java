/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
