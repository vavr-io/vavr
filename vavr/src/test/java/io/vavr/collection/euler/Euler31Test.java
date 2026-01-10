/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
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

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

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
