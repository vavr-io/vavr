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

public class Euler04Test {

    /**
     * <strong>Problem 4: Largest palindrome product</strong>
     * <p>
     * A palindromic number reads the same both ways. The largest palindrome made
     * from the product of two 2-digit numbers is 9009 = 91 × 99.
     * <p>
     * Find the largest palindrome made from the product of two 3-digit numbers.
     * <p>
     * See also <a href="https://projecteuler.net/problem=4">projecteuler.net problem 4</a>.
     */
    @Test
    public void shouldSolveProblem4() {
        assertThat(largestPalindromeOfProductsFromFactorsInRange(10, 99)).isEqualTo(9009);
        assertThat(largestPalindromeOfProductsFromFactorsInRange(100, 999)).isEqualTo(906609);
    }

    private static int largestPalindromeOfProductsFromFactorsInRange(final int min, final int max) {
        return List.rangeClosed(min, max)
                .crossProduct()
                .filter(t -> t._1 <= t._2)
                .map(t -> t._1 * t._2)
                .filter(Utils::isPalindrome)
                .max().get();
    }
}
