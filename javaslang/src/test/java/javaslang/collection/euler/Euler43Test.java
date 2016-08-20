/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.collection.CharSeq;
import javaslang.Tuple;
import javaslang.collection.List;
import javaslang.collection.Seq;

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
        final Seq<Long> result = tenDigitPandigitalsWithProperty();
        assertThat(result).contains(1406357289L);

        assertThat(result.sum().longValue()).isEqualTo(16695334890L);
    }

    private static Seq<Long> tenDigitPandigitalsWithProperty() {
        final CharSeq ALL_DIGITS = CharSeq.of("0123456789");
        final List<Integer> DIVISORS = List.of(2, 3, 5, 7, 11, 13, 17);
        final char DUMMY_CHAR = Character.MIN_VALUE;

        return ALL_DIGITS
                .combinations(2)
                .flatMap(CharSeq::permutations)
                .map(cs -> Tuple.of(cs, cs.prepend(DUMMY_CHAR), Integer.valueOf(cs.mkString())))
                .flatMap(firstTwoDigits -> DIVISORS
                        .foldLeft(List.of(firstTwoDigits), (accumulator, divisor) -> accumulator
                                .flatMap(digitsSoFar -> ALL_DIGITS
                                        .removeAll(digitsSoFar._1)
                                        .map(nextDigit -> Tuple.of(digitsSoFar._1.append(nextDigit), digitsSoFar._2.tail().append(nextDigit)))
                                        .map(digitsToTest -> Tuple.of(digitsToTest._1, digitsToTest._2, Integer.valueOf(digitsToTest._2.mkString())))
                                )
                                .filter(digitsToTest -> digitsToTest._3 % divisor == 0)
                        )
                )
                .map(tailDigitsWithProperty -> tailDigitsWithProperty._1
                        .prepend(ALL_DIGITS
                                .removeAll(tailDigitsWithProperty._1)
                                .head()
                        )
                )
                .map(CharSeq::mkString)
                .map(Long::valueOf);
    }
}
