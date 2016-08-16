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

    private static final Stream<Character> FIRST_1_000_000_DECIMALS
            = Stream.from(1)
            .map(i -> i.toString())
            .flatMap(CharSeq::of)
            .take(1_000_000);
}
