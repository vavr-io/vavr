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
import io.vavr.collection.List;
import org.junit.Test;

import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler38Test {

    /**
     * <strong>Problem 38 Pandigital multiples</strong>
     * <p>
     * Take the number 192 and multiply it by each of 1, 2, and 3:
     * <pre>
     * 192 × 1 = 192
     * 192 × 2 = 384
     * 192 × 3 = 576
     * </pre>
     * <p>
     * By concatenating each product we get the 1 to 9 pandigital, 192384576. We
     * will call 192384576 the concatenated product of 192 and (1,2,3)
     * <p>
     * The same can be achieved by starting with 9 and multiplying by 1, 2, 3,
     * 4, and 5, giving the pandigital, 918273645, which is the concatenated
     * product of 9 and (1,2,3,4,5).
     * <p>
     * What is the largest 1 to 9 pandigital 9-digit number that can be formed
     * as the concatenated product of an integer with (1,2, ... , n) where n >
     * 1?
     * <p>
     * See also <a href="https://projecteuler.net/problem=38">projecteuler.net
     * problem 38</a>.
     */
    @Test
    public void shouldSolveProblem38() {
        assertThat(isPandigitalMultiple(CharSeq.of("192384576"))).isTrue();
        assertThat(isPandigitalMultiple(CharSeq.of("918273645"))).isTrue();

        assertThat(largest1To9PandigitalMultiple().mkString()).isEqualTo("932718654");
    }

    private static CharSeq largest1To9PandigitalMultiple() {
        return CharSeq.of("87654321")
                .permutations()
                .map(CharSeq::mkString)
                .map(Integer::valueOf)
                .sorted()
                .reverse()
                .map(i -> "9" + i) // Since 918273645 is known we don't have to investigate numbers not starting with a 9.
                .map(CharSeq::of)
                .find(Euler38Test::isPandigitalMultiple)
                .get();
    }

    private static boolean isPandigitalMultiple(CharSeq pandigital) {
        return List.rangeClosed(1, pandigital.length() - 1)
                .exists(i -> isPandigitalMultipleRest(pandigital.drop(i), Integer.valueOf(pandigital.take(i).mkString()), 2));
    }

    private static boolean isPandigitalMultipleRest(CharSeq pandigitalRest, int multiplicand, int multiplicator) {
        return Match(pandigitalRest.length()).of(
                Case($(0), true),
                Case($(), length -> List.rangeClosed(1, length)
                        .find(i -> Integer.valueOf(pandigitalRest.take(i).mkString()) == multiplicand * multiplicator)
                        .map(i -> isPandigitalMultipleRest(pandigitalRest.drop(i), multiplicand, multiplicator + 1))
                        .getOrElse(false)
                )
        );
    }
}
