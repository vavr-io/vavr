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

import io.vavr.Tuple;
import io.vavr.collection.Stream;
import io.vavr.Tuple2;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler57Test {

    /**
     * <strong>Problem 57: Square root convergents</strong>
     * <p>
     * It is possible to show that the square root of two can be expressed as an infinite continued fraction.
     * <p>
     * √ 2 = 1 + 1/(2 + 1/(2 + 1/(2 + ... ))) = 1.414213...
     * <p>
     * By expanding this for the first four iterations, we get:
     * <ul>
     * <li>1 + 1/2 = 3/2 = 1.5</li>
     * <li>1 + 1/(2 + 1/2) = 7/5 = 1.4</li>
     * <li>1 + 1/(2 + 1/(2 + 1/2)) = 17/12 = 1.41666...</li>
     * <li>1 + 1/(2 + 1/(2 + 1/(2 + 1/2))) = 41/29 = 1.41379...</li>
     * </ul>
     * The next three expansions are 99/70, 239/169, and 577/408, but the eighth expansion, 1393/985,
     * is the first example where the number of digits in the numerator exceeds the number of digits in the denominator.
     * <p>
     * In the first one-thousand expansions, how many fractions contain a numerator with more digits than denominator?
     * <p>
     * See also <a href="https://projecteuler.net/problem=57">projecteuler.net problem 57</a>.
     */
    @Test
    public void shouldSolveProblem57() {
        assertThat(cnt()).isEqualTo(153);
    }

    private static int cnt() {
        return fractions()
                .take(1000)
                .filter(f -> f._1.toPlainString().length() > f._2.toPlainString().length())
                .length();
    }

    private static Stream<Tuple2<BigDecimal, BigDecimal>> fractions() {
        return Stream.iterate(Tuple.of(BigDecimal.ONE, BigDecimal.ONE), Euler57Test::it);
    }

    /**
     * a/b -> 1 + 1/(1 + a/b) = (a + 2b)/(a + b)
     */
    private static Tuple2<BigDecimal, BigDecimal> it(Tuple2<BigDecimal, BigDecimal> val) {
        return Tuple.of(val._1.add(val._2.add(val._2)), val._1.add(val._2));
    }
}
