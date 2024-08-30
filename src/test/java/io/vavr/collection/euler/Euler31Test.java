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

import io.vavr.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 31: Coin sums</strong>
 * <p>In England the currency is made up of pound, £, and pence, p, and there are eight coins in general circulation:</p>
 * <blockquote>1p, 2p, 5p, 10p, 20p, 50p, £1 (100p) and £2 (200p).</blockquote>
 * <p>It is possible to make £2 in the following way:</p>
 * <blockquote>1×£1 + 1×50p + 2×20p + 1×5p + 1×2p + 3×1p</blockquote>
 * <p>How many different ways can £2 be made using any number of coins?</p>
 * See also <a href="https://projecteuler.net/problem=31">projecteuler.net problem 31</a>.
 */
public class Euler31Test {

    @Test
    public void shouldSolveProblem31() {
        final List<Integer> coins = List.of(1, 2, 5, 10, 20, 50, 100, 200);
        assertThat(coinSums(200, coins)).isEqualTo(73682);
    }

    private static int coinSums(int n, List<Integer> coins) {
        return (n == 0) ? 1 :
               (n < 0 || coins.isEmpty()) ? 0 :
               coinSums(n, coins.tail()) + coinSums(n - coins.head(), coins);
    }

}
