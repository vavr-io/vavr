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

import io.vavr.collection.CharSeq;
import io.vavr.collection.Stream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 36: Double-base palindromes</strong>
 * <p>The decimal number, 585 = 1001001001<sub>2</sub> (binary), is palindromic in both bases.</p>
 * <p>Find the sum of all numbers, less than one million, which are palindromic in base 10 and base 2.</p>
 * <p class="info">(Please note that the palindromic number, in either base, may not include leading zeros.)</p>
 * See also <a href="https://projecteuler.net/problem=36">projecteuler.net problem 36</a>.
 */
public class Euler36Test {

    @Test
    public void shouldSolveProblem36() {
        assertThat(solve(1000000)).isEqualTo(872187);
    }

    private static int solve(int n) {
        return Stream.range(1, n)
                .filter(Euler36Test::isDoubleBasePalindrome)
                .sum().intValue();
    }

    private static boolean isPalindrome(CharSeq seq) {
        return seq.dropWhile(c -> c == '0').equals(seq.reverse().dropWhile(c -> c == '0'));
    }

    private static boolean isDoubleBasePalindrome(int x) {
        final CharSeq seq = CharSeq.of(Integer.toString(x));
        final CharSeq rev = CharSeq.of(Integer.toBinaryString(x));
        return isPalindrome(seq) && isPalindrome(rev);
    }

}

