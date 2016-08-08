/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.Tuple;
import javaslang.collection.Stream;
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
