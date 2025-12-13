/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2025 Vavr, https://vavr.io
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

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static io.vavr.collection.euler.Utils.factorial;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler15Test {

    /**
     * <strong>Problem 15: Lattice paths</strong>
     * <p>
     * Starting in the top left corner of a 2x2 grid, and only being able to move to the right and down,
     * there are exactly 6 routes to the bottom right corner.
     * <p>
     * How many such routes are there through a 20×20 grid?
     * <p>
     * See also <a href="https://projecteuler.net/problem=15">projecteuler.net problem 15</a>.
     */
    @Test
    public void shouldSolveProblem15() {
        assertThat(solve(2)).isEqualTo(6);
        assertThat(solve(20)).isEqualTo(137_846_528_820L);
    }

    private static long solve(int n) {
        final BigInteger f = factorial(n);
        return factorial(2 * n).divide(f).divide(f).longValue();
    }
}
