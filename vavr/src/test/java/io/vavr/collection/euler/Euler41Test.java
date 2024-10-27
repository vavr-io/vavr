/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2024 Vavr, https://vavr.io
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
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static java.util.Comparator.reverseOrder;
import static io.vavr.collection.euler.Utils.isPrime;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler41Test {

    /**
     * <strong>Problem 41 Pandigital prime</strong>
     * <p>
     * We shall say that an <i>n</i>-digit number is pandigital if it makes use
     * of all the digits 1 to <i>n</i> exactly once. For example, 2143 is a
     * 4-digit pandigital and is also prime.
     *
     * What is the largest <i>n</i>-digit pandigital prime that exists?
     * <p>
     * See also <a href="https://projecteuler.net/problem=41">projecteuler.net
     * problem 41</a>.
     */
    @Test
    public void shouldSolveProblem41() {
        assertThat(nDigitPandigitalNumbers(4)).contains(2143);
        assertThat(isPrime(2143)).isTrue();

        assertThat(largestNPandigitalPrime()).isEqualTo(7652413);
    }

    private static int largestNPandigitalPrime() {
        return Stream.rangeClosedBy(9, 1, -1)
                .flatMap(n -> nDigitPandigitalNumbers(n)
                        .filter(Utils::isPrime)
                        .sorted(reverseOrder()))
                .head();
    }

    private static List<Integer> nDigitPandigitalNumbers(int n) {
        return List.rangeClosed(1, n)
                .permutations()
                .map(List::mkString)
                .map(Integer::valueOf);
    }
}
