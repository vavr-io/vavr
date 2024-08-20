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
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 28: Number spiral diagonals</strong>
 * <p>
 * Starting with the number 1 and moving to the right in a clockwise direction a
 * 5 by 5 spiral is formed as follows:
 * <pre>
 *             21 22 23 24 25
 *             20  7  8  9 10
 *             19  6  1  2 11
 *             18  5  4  3 12
 *             17 16 15 14 13
 * </pre>
 *
 * It can be verified that the sum of the numbers on the diagonals is 101.
 * <p>
 * What is the sum of the numbers on the diagonals in a 1001 by 1001 spiral
 * formed in the same way?
 * <p>
 * See also
 * <a href="https://projecteuler.net/problem=28">projecteuler.net problem 28
 * </a>.
 */
public class Euler28Test {

    @Test
    public void shouldSolveProblem28() {
        assertThat(sumOfDiagonalInSpiralWithSide(5)).isEqualTo(101);
        assertThat(sumOfDiagonalInSpiralWithSide(1001)).isEqualTo(669_171_001);
    }

    private static long sumOfDiagonalInSpiralWithSide(long maxSideLength) {
        return diagonalNumbersInSpiralWithSide(maxSideLength).sum().longValue();
    }

    private static Stream<Long> diagonalNumbersInSpiralWithSide(long maxSideLength) {
        return Stream.iterate(Tuple.of(1, center()), t -> Tuple.of(nextSideLength(t._1), nextRoundOfCorners(t._2.last(), nextSideLength(t._1))))
                .takeWhile(t -> t._1 <= maxSideLength)
                .flatMap(t -> t._2);
    }

    private static Stream<Long> center() {
        return Stream.of(1L);
    }

    private static int nextSideLength(int currentSideLength) {
        return currentSideLength + 2;
    }

    private static Stream<Long> nextRoundOfCorners(long previousCorner, int currentSideLength) {
        return Stream.iterate(previousCorner, n -> n + currentSideLength - 1)
                .drop(1)
                .take(4);
    }
}
