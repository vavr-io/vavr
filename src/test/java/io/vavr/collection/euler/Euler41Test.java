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

import io.vavr.collection.Stream;
import io.vavr.collection.List;
import org.junit.Test;

import static java.util.Comparator.reverseOrder;
import static io.vavr.collection.euler.Utils.isPrime;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler41Test {

    /**
     * <strong>Problem 41 Pandigital prime</strong>
     * <p>
     * We shall say that an <i>n</i>-digit number is pandigital if it makes use
     * of all the digits 1 to <i>n</i> exactly once. For example, 2143 is a
     * 4-digit pandigital and is also prime.
     *
     * What is the largest <i>n</i>-digit pandigital prime that exists?
     * <p>
     * See also <a href="https://projecteuler.net/problem=41">projecteuler.net
     * problem 41</a>.
     */
    @Test
    public void shouldSolveProblem41() {
        assertThat(nDigitPandigitalNumbers(4)).contains(2143);
        assertThat(isPrime(2143)).isTrue();

        assertThat(largestNPandigitalPrime()).isEqualTo(7652413);
    }

    private static int largestNPandigitalPrime() {
        return Stream.rangeClosedBy(9, 1, -1)
                .flatMap(n -> nDigitPandigitalNumbers(n)
                        .filter(Utils::isPrime)
                        .sorted(reverseOrder()))
                .head();
    }

    private static List<Integer> nDigitPandigitalNumbers(int n) {
        return List.rangeClosed(1, n)
                .permutations()
                .map(List::mkString)
                .map(Integer::valueOf);
    }
}
