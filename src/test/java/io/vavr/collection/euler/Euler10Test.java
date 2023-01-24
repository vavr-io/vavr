/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2023 Vavr, https://vavr.io
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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler10Test {

    /**
     * <strong>Problem 10: Summation of primes</strong>
     * <p>
     * The sum of the primes below 10 is 2 + 3 + 5 + 7 = 17.
     * <p>
     * Find the sum of all the primes below two million.
     * <p>
     * See also <a href="https://projecteuler.net/problem=10">projecteuler.net problem 10</a>.
     */
    @Test
    public void shouldSolveProblem10() {
        assertThat(sumPrimes(10)).isEqualTo(17);
        assertThat(sumPrimes(2_000_000L)).isEqualTo(142_913_828_922L);
    }

    private long sumPrimes(long max) {
        return PrimeNumbers.primes()
                .map(Long::valueOf)
                .takeWhile(t -> t < max)
                .reduce((p1, p2) -> p1 + p2);
    }
}
