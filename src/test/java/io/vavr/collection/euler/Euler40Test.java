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

import io.vavr.collection.CharSeq;
import io.vavr.collection.Stream;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler40Test {

    /**
     * <strong>Problem 40 Champernowne's constant</strong>
     * <p>
     * An irrational decimal fraction is created by concatenating the positive
     * integers:
     * <p>
     * 0.12345678910<b>1</b>112131415161718192021...
     * <p>
     * It can be seen that the 12<sup>th</sup> digit of the fractional part is
     * 1.
     * <p>
     * If <i>d</i><sub>n</sub> represents the <i>n</i><sup>th</sup> digit of the
     * fractional part, find the value of the following expression.
     * <pre>
     * <i>d</i><sub>1</sub> × <i>d</i><sub>10</sub> × <i>d</i><sub>100</sub> × <i>d</i><sub>1000</sub> × <i>d</i><sub>10000</sub> × <i>d</i><sub>100000</sub> × <i>d</i><sub>1000000</sub>
     * </pre>
     * <p>
     * See also <a href="https://projecteuler.net/problem=40">projecteuler.net
     * problem 40</a>.
     */
    @Test
    public void shouldSolveProblem40() {
        assertThat(decimalDigitAtPosition(12)).isEqualTo('1');

        assertThat(solution()).isEqualTo(210);
    }

    private static int solution() {
        return Stream.iterate(1, i -> i * 10)
                .takeWhile(i -> i <= 1_000_000)
                .map(Euler40Test::decimalDigitAtPosition)
                .map(c -> Character.digit(c, 10))
                .fold(1, (i1, i2) -> i1 * i2);
    }

    private static char decimalDigitAtPosition(int num) {
        return FIRST_1_000_000_DECIMALS.get(num - 1);
    }

    private static final Stream<Character> FIRST_1_000_000_DECIMALS = Stream.from(1)
            .map(i -> i.toString())
            .flatMap(CharSeq::of)
            .take(1_000_000);
}
