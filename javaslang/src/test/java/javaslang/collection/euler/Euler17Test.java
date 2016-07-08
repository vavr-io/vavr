/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.API;
import static javaslang.API.*;
import javaslang.collection.Stream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler17Test {

    /**
     * <strong>Problem 17: Number letter counts</strong>
     * <p>
     * If the numbers 1 to 5 are written out in words: one, two, three, four,
     * five, then there are 3 + 3 + 5 + 4 + 4 = 19 letters used in total.
     * <p>
     * If all the numbers from 1 to 1000 (one thousand) inclusive were written
     * out in words, how many letters would be used?
     * <p>
     * NOTE: Do not count spaces or hyphens. For example, 342 (three hundred and
     * forty-two) contains 23 letters and 115 (one hundred and fifteen) contains
     * 20 letters. The use of "and" when writing out numbers is in compliance
     * with British usage.
     */
    @Test
    public void shouldSolveProblem17() {
        assertThat(letterCountInNumber(1)).isEqualTo(3);
        assertThat(letterCountInNumber(342)).isEqualTo(23);
        assertThat(letterCountInNumber(115)).isEqualTo(20);
        assertThat(letterCountInNumbersUpToAndIncluding(5)).isEqualTo(19);
        assertThat(letterCountInNumbersUpToAndIncluding(1000)).isEqualTo(21124);
    }

    private static final int THOUSAND = "thousand".length();
    private static final int HUNDRED = "hundred".length();
    private static final int AND = "and".length();
    private static final int[] LETTER_COUNT = new int[91];

    static {
        LETTER_COUNT[1] = "one".length();
        LETTER_COUNT[2] = "two".length();
        LETTER_COUNT[3] = "three".length();
        LETTER_COUNT[4] = "four".length();
        LETTER_COUNT[5] = "five".length();
        LETTER_COUNT[6] = "six".length();
        LETTER_COUNT[7] = "seven".length();
        LETTER_COUNT[8] = "eight".length();
        LETTER_COUNT[9] = "nine".length();
        LETTER_COUNT[10] = "ten".length();
        LETTER_COUNT[11] = "eleven".length();
        LETTER_COUNT[12] = "twelve".length();
        LETTER_COUNT[13] = "thirteen".length();
        LETTER_COUNT[14] = "fourteen".length();
        LETTER_COUNT[15] = "fifteen".length();
        LETTER_COUNT[16] = "sixteen".length();
        LETTER_COUNT[17] = "seventeen".length();
        LETTER_COUNT[18] = "eighteen".length();
        LETTER_COUNT[19] = "nineteen".length();
        LETTER_COUNT[20] = "twenty".length();
        LETTER_COUNT[30] = "thirty".length();
        LETTER_COUNT[40] = "forty".length();
        LETTER_COUNT[50] = "fifty".length();
        LETTER_COUNT[60] = "sixty".length();
        LETTER_COUNT[70] = "seventy".length();
        LETTER_COUNT[80] = "eighty".length();
        LETTER_COUNT[90] = "ninety".length();
    }

    private static int letterCountInNumber(int num) {
        return API.Match(num).of(
                Case($(n -> n / 1000 > 0), n -> LETTER_COUNT[n / 1000] + THOUSAND + letterCountInNumber(n % 1000)),
                Case($(n -> n / 100 > 0), n -> LETTER_COUNT[n / 100] + HUNDRED + (n % 100 > 0 ? AND + letterCountInNumber(n % 100) : 0)),
                Case($(n -> n / 10 > 1), n -> LETTER_COUNT[n / 10 * 10] + letterCountInNumber(n % 10)),
                Case($(), n -> LETTER_COUNT[n])
        );
    }

    private static int letterCountInNumbersUpToAndIncluding(int num) {
        return Stream.rangeClosed(1, num)
                .map(Euler17Test::letterCountInNumber)
                .sum().intValue();
    }
}
