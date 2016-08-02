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

/**
 * <strong>Problem 36: Double-base palindromes</strong>
 * <p>The decimal number, 585 = 1001001001<sub>2</sub> (binary), is palindromic in both bases.</p>
 * <p>Find the sum of all numbers, less than one million, which are palindromic in base 10 and base 2.</p>
 * <p class="info">(Please note that the palindromic number, in either base, may not include leading zeros.)</p>
 * See also <a href="https://projecteuler.net/problem=36">projecteuler.net problem 36</a>.
 */
public class Euler36Test {

    @Test
    public void shouldSolveProblem36() {
        assertThat(solve(1000000)).isEqualTo(872187);
    }

    private static int solve(int n) {
        return Stream.range(1, n)
                .filter(Euler36Test::isDoubleBasePalindrome)
                .sum().intValue();
    }

    private static boolean isPalindrome(CharSeq seq) {
        return seq.dropWhile(c -> c == '0').equals(seq.reverse().dropWhile(c -> c == '0'));
    }

    private static boolean isDoubleBasePalindrome(int x) {
        CharSeq seq = CharSeq.of(Integer.toString(x));
        CharSeq rev = CharSeq.of(Integer.toBinaryString(x));
        return isPalindrome(seq) && isPalindrome(rev);
    }

}

