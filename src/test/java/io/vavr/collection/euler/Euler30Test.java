/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2022 Vavr, https://vavr.io
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
