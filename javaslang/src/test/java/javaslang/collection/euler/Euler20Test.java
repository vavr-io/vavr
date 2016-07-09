/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.collection.Stream;
import org.junit.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 20:Factorial digit sum</strong>
 *
 * <p><i>n</i>! means <i>n</i> × (<i>n</i> − 1) × ... × 3 × 2 × 1</p>
 * <p>For example, 10! = 10 × 9 × ... × 3 × 2 × 1 = 3628800,<br />and the sum of the digits in the number 10! is 3 + 6 + 2 + 8 + 8 + 0 + 0 = 27.</p>
 * <p>Find the sum of the digits in the number 100!</p>
 *
 * See also <a href="https://projecteuler.net/problem=20">projecteuler.net problem 20</a>.
 */

public class Euler20Test {

    @Test
    public void shouldSolveProblem20() {
        assertThat(sumOfDigits100Factorial()).isEqualTo(648);
    }

    private static BigInteger factorial(int n) {
        return Stream.rangeClosed(1, n)
                .map(BigInteger::valueOf)
                .fold(BigInteger.ONE, BigInteger::multiply);
    }

    private static int sumOfDigits100Factorial() {
        return Stream.ofAll(factorial(100).toString().toCharArray())
                .map(x -> x - '0')
                .sum()
                .intValue();
    }
}
