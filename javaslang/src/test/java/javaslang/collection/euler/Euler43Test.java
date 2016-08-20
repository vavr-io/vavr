/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.collection.CharSeq;
import javaslang.collection.Stream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler43Test {

    /**
     * <strong>Problem 43 Sub-string divisibility</strong>
     * <p>
     * The number, 1406357289, is a 0 to 9 pandigital number because it is made
     * up of each of the digits 0 to 9 in some order, but it also has a rather
     * interesting sub-string divisibility property.</p>
     * <p>
     * Let <i>d</i><sub>1</sub> be the 1<sup>st</sup> digit,
     * <i>d</i><sub>2</sub> be the 2<sup>nd</sup> digit, and so on. In this way,
     * we note the following:</p>
     * <pre>
     * <ul>
     * <li><i>d</i><sub>2</sub><i>d</i><sub>3</sub><i>d</i><sub>4</sub>=406 is divisible by 2</li>
     * <li><i>d</i><sub>3</sub><i>d</i><sub>4</sub><i>d</i><sub>5</sub>=063 is divisible by 3</li>
     * <li><i>d</i><sub>4</sub><i>d</i><sub>5</sub><i>d</i><sub>6</sub>=635 is divisible by 5</li>
     * <li><i>d</i><sub>5</sub><i>d</i><sub>6</sub><i>d</i><sub>7</sub>=357 is divisible by 7</li>
     * <li><i>d</i><sub>6</sub><i>d</i><sub>7</sub><i>d</i><sub>8</sub>=572 is divisible by 11</li>
     * <li><i>d</i><sub>7</sub><i>d</i><sub>8</sub><i>d</i><sub>9</sub>=728 is divisible by 13</li>
     * <li><i>d</i><sub>8</sub><i>d</i><sub>9</sub><i>d</i><sub>10</sub>=289 is divisible by 17</li>
     * </ul>
     * </pre>
     * <p>
     * Find the sum of all 0 to 9 pandigital numbers with this property.
     * <p>
     * See also <a href="https://projecteuler.net/problem=43">projecteuler.net
     * problem 43</a>.
     */
    @Test
    public void shouldSolveProblem43() {
        assertThat(hasSubStringDivisibilityProperty(CharSeq.of("1406357289"))).isTrue();

        assertThat(sumOfAllTenDigitPandigitalsWithProperty()).isEqualTo(16695334890L);
    }

    private static long sumOfAllTenDigitPandigitalsWithProperty() {
        return CharSeq.of("0123456789")
                .permutations()
                .filter(Euler43Test::hasSubStringDivisibilityProperty)
                .map(CharSeq::mkString)
                .map(Long::valueOf)
                .sum().longValue();
    }

    private static boolean hasSubStringDivisibilityProperty(CharSeq tenDigitPandigital) {
        return Stream.rangeClosed(2, 8)
                .map(i -> tenDigitPandigital.subSequence(i - 1, i + 2))
                .map(CharSeq::mkString)
                .map(Integer::valueOf)
                .zip(Stream.of(2, 3, 5, 7, 11, 13, 17))
                .forAll(t -> t._1 % t._2 == 0);
    }
}
