/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2020 Vavr, http://vavr.io
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

import io.vavr.collection.Stream;
import org.junit.Test;

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
