/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2024 Vavr, http://vavr.io
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
import io.vavr.collection.List;
import org.junit.Test;

import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler37Test {

    /**
     * <strong>Problem 37 Truncatable primes</strong>
     * <p>
     * The number 3797 has an interesting property. Being prime itself, it is
     * possible to continuously remove digits from left to right, and remain
     * prime at each stage: 3797, 797, 97, and 7. Similarly we can work from
     * right to left: 3797, 379, 37, and 3.
     * <p>
     * Find the sum of the only eleven primes that are both truncatable from
     * left to right and right to left.
     * <p>
     * NOTE: 2, 3, 5, and 7 are not considered to be truncatable primes.
     * <p>
     * See also <a href="https://projecteuler.net/problem=37">projecteuler.net
     * problem 37</a>.
     */
    @Test
    public void shouldSolveProblem37() {
        assertThat(isTruncatablePrime(3797)).isTrue();
        List.of(2, 3, 5, 7).forEach(i -> assertThat(isTruncatablePrime(7)).isFalse());

        assertThat(sumOfTheElevenTruncatablePrimes()).isEqualTo(748_317);
    }

    private static int sumOfTheElevenTruncatablePrimes() {
        return PrimeNumbers.primes()
                .filter(Euler37Test::isTruncatablePrime)
                .take(11)
                .sum().intValue();
    }

    private static boolean isTruncatablePrime(int prime) {
        return Match(prime).of(
                Case($(p -> p > 7), p -> {
                    final CharSeq primeSeq = CharSeq.of(Integer.toString(p));
                    return List.rangeClosed(1, primeSeq.length() - 1)
                            .flatMap(i -> List.of(primeSeq.drop(i), primeSeq.dropRight(i)))
                            .map(CharSeq::mkString)
                            .map(Long::valueOf)
                            .forAll(Utils.MEMOIZED_IS_PRIME::apply);
                }),
                Case($(), false)
        );
    }
}
