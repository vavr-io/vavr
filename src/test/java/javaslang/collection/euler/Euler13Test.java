/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import org.junit.Test;

import java.math.BigInteger;

import static javaslang.collection.euler.Utils.file;
import static javaslang.collection.euler.Utils.readLines;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler13Test {

    /**
     * <strong>Problem 13: Large sum</strong>
     * <p>
     * Work out the first ten digits of the sum of the following one-hundred 50-digit numbers.
     * <p>
     * See also <a href="https://projecteuler.net/problem=13">projecteuler.net problem 13</a>.
     */
    @Test
    public void shouldSolveProblem13() {
        assertThat(solve()).isEqualTo("5537376230");
    }

    private static String solve() {
        return readLines(file("p013_numbers.txt"))
                .map(BigInteger::new)
                .fold(BigInteger.ZERO, BigInteger::add)
                .toString().substring(0,10);
    }
}
