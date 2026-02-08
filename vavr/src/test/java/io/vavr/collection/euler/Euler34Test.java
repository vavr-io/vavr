/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection.euler;

import io.vavr.collection.CharSeq;
import io.vavr.collection.Stream;
import org.junit.jupiter.api.Test;

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
