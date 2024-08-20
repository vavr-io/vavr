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
