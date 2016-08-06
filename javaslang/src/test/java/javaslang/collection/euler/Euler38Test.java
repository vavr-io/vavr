/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import org.junit.Test;

import javaslang.collection.CharSeq;
import javaslang.collection.List;

import static javaslang.API.*;

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

        assertThat(largets1To9PandigitalMultiple().mkString()).isEqualTo("932718654");
    }

    private static CharSeq largets1To9PandigitalMultiple() {
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
