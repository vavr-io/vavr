/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.collection.Stream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler05Test {

    /**
     * <strong>Problem 5: Smallest multiple</strong>
     * <p>
     * 2520 is the smallest number that can be divided by each of the numbers from 1
     * to 10 without any remainder.
     * <p>
     * What is the smallest positive number that is evenly divisible by all of the
     * numbers from 1 to 20?
     * <p>
     * See also <a href="https://projecteuler.net/problem=5">projecteuler.net problem 5</a>.
     */
    @Test
    public void shouldSolveProblem5() {
        assertThat(smallestPositiveNumberEvenlyDivisibleByAllNumbersFrom1To(10)).isEqualTo(2_520L);
        assertThat(smallestPositiveNumberEvenlyDivisibleByAllNumbersFrom1To(20)).isEqualTo(232_792_560L);
    }

    private static long smallestPositiveNumberEvenlyDivisibleByAllNumbersFrom1To(int max) {
        return Stream.rangeClosed(2, max)
                .map(PrimeNumbers::factorization)
                .reduce((m1, m2) -> m1.merge(m2, Math::max))
                .foldLeft(1L, (xs, x) -> xs * pow(x.key, x.value));
    }

    private static long pow(long a, long p) {
        return Stream.rangeClosed(1, p).fold(1L, (xs, x) -> xs * a);
    }
}
