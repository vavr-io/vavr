/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

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

    public long sumPrimes(long max) {
        return PrimeNumbers.primes()
                .map(Long::valueOf)
                .takeWhile(t -> t < max)
                .reduce((p1, p2) -> p1 + p2);
    }
}
