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

import io.vavr.Tuple;
import io.vavr.collection.CharSeq;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler32Test {

    /**
     * <strong>Problem 23 Pandigital products</strong>
     * <p>
     * We shall say that an n-digit number is pandigital if it makes use of all
     * the digits 1 to n exactly once; for example, the 5-digit number, 15234,
     * is 1 through 5 pandigital.
     * <p>
     * The product 7254 is unusual, as the identity, 39 × 186 = 7254, containing
     * multiplicand, multiplier, and product is 1 through 9 pandigital.
     * <p>
     * Find the sum of all products whose multiplicand/multiplier/product
     * identity can be written as a 1 through 9 pandigital.
     * <p>
     * HINT: Some products can be obtained in more than one way so be sure to
     * only include it once in your sum.
     * <p>
     * See also <a href="https://projecteuler.net/problem=32">projecteuler.net
     * problem 32</a>.
     */
    @Test
    public void shouldSolveProblem32() {
        assertThat(isPandigital(1, 5, "15234")).isTrue();
        assertThat(isPandigital(1, 9, "39" + "186" + "7254")).isTrue();
        assertThat(isPandigital(1, 5, "55555")).isFalse();
        assertThat(isPandigital(1, 5, "12340")).isFalse();
        assertThat(sumOfAllProductsPandigital1Through9()).isEqualTo(45228);
    }

    private static boolean isPandigital(int from, int to, String num) {
        return num.length() == to - from + 1 && List.rangeClosed(from, to).forAll(i -> num.contains(Integer.toString(i)));
    }

    private static final CharSeq DIGITS_1_9 = CharSeq.of("123456789");

    private static long sumOfAllProductsPandigital1Through9() {
        return List.of(1, 2)
                .flatMap(i -> DIGITS_1_9.crossProduct(i)
                        .flatMap(multiplicand -> DIGITS_1_9.removeAll(multiplicand).crossProduct(5 - i)
                                .map(multiplier -> Tuple.of(multiplicand.mkString(), multiplier.mkString()))
                        )
                )
                .map(t -> Tuple.of(t._1, t._2, Long.valueOf(t._1) * Long.valueOf(t._2)))
                .filter(t -> isPandigital(1, 9, t._1 + t._2 + Long.toString(t._3)))
                .map(Tuple3::_3)
                .distinct()
                .sum().longValue();
    }
}
