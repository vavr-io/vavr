/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
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

import io.vavr.collection.Array;
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

    private static final Array<Integer> PRIMES = Array.ofAll(PrimeNumbers.primes().take(10_001));

    private static long prime(int index) {
        return PRIMES.get(index - 1);
    }

}
