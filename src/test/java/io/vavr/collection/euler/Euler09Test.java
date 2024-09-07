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
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler09Test {

    /**
     * <strong>Problem 9: Special Pythagorean triplet</strong>
     * <p>
     * A Pythagorean triplet is a set of three natural numbers, a < b < c, for which,
     * a<sup>2</sup> + b<sup>2</sup> = c<sup>2</sup>
     * <p>
     * For example, 3<sup>2</sup> + 4<sup>2</sup> = 9 + 16 = 25 = 5<sup>2</sup>.
     * <p>
     * There exists exactly one Pythagorean triplet for which a + b + c = 1000.
     * Find the product abc.
     * <p>
     * See also <a href="https://projecteuler.net/problem=9">projecteuler.net problem 9</a>.
     */
    @Test
    public void shouldSolveProblem9() {
        assertThat(abc(1_000)).isEqualTo(31_875_000);
    }

    public int abc(int sum) {
        return List.rangeClosed(1, sum)
                .crossProduct()
                .filter(t -> t._1 + t._2 < sum)
                .map(t -> Tuple.of(t._1, t._2, sum - t._1 - t._2))
                .filter(t -> t._1 * t._1 + t._2 * t._2 == t._3 * t._3)
                .map(t -> t._1 * t._2 * t._3)
                .head();
    }
}
