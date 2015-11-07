/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.collection.CharSeq;
import org.junit.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler16Test {

    /**
     * <strong>Problem 16: Power digit sum</strong>
     * <p>
     * 2<sup>15</sup> = 32768 and the sum ofAll its digits is 3 + 2 + 7 + 6 + 8 = 26.
     * <p>
     * What is the sum ofAll the digits ofAll the number 2<sup>1000</sup>?
     * <p>
     * See also <a href="https://projecteuler.net/problem=16">projecteuler.net problem 16</a>.
     */
    @Test
    public void shouldSolveProblem16() {
        assertThat(solve(15)).isEqualTo(26);
        assertThat(solve(1000)).isEqualTo(1_366);
    }

    private static long solve(int n) {
        return CharSeq.of(BigInteger.valueOf(2).pow(n).toString())
                .map(c -> c - '0')
                .fold(0, (a, b) -> a + b);
    }
}
