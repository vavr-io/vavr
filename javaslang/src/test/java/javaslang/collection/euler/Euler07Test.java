/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.collection.Array;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler07Test {

    /**
     * <strong>Problem 7: 10001st prime</strong>
     * <p>
     * By listing the first six prime numbers: 2, 3, 5, 7, 11, and 13, we can see that the 6th prime is 13.
     * <p>
     * What is the 10 001st prime number?
     * <p>
     * See also <a href="https://projecteuler.net/problem=7">projecteuler.net problem 7</a>.
     */
    @Test
    public void shouldSolveProblem7() {
        assertThat(prime(1)).isEqualTo(2);
        assertThat(prime(2)).isEqualTo(3);
        assertThat(prime(3)).isEqualTo(5);
        assertThat(prime(4)).isEqualTo(7);
        assertThat(prime(5)).isEqualTo(11);
        assertThat(prime(6)).isEqualTo(13);
        assertThat(prime(10_001)).isEqualTo(104_743);
    }

    static Array<Integer> primes = Array.ofAll(PrimeNumbers.primes().take(10_001));

    static long prime(int index) {
        return primes.get(index - 1);
    }

}
