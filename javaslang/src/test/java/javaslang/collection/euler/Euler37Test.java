/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import org.junit.Test;

import javaslang.collection.CharSeq;
import javaslang.collection.List;

import static javaslang.API.*;
import static javaslang.collection.euler.PrimeNumbers.primes;

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
        return primes()
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
                            .forAll(Utils.memoizedIsPrime::apply);
                }),
                Case($(), false)
        );
    }
}
