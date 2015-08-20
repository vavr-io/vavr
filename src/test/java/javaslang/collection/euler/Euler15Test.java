/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import org.junit.Test;

import java.math.BigInteger;

import static javaslang.collection.euler.Utils.factorial;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler15Test {

    /**
     * <strong>Problem 15: Lattice paths</strong>
     * <p>
     * Starting in the top left corner of a 2x2 grid, and only being able to move to the right and down,
     * there are exactly 6 routes to the bottom right corner.
     * <p>
     * How many such routes are there through a 20×20 grid?
     * <p>
     * See also <a href="https://projecteuler.net/problem=15">projecteuler.net problem 15</a>.
     */
    @Test
    public void shouldSolveProblem15() {
        assertThat(solve(2)).isEqualTo(6);
        assertThat(solve(20)).isEqualTo(137_846_528_820L);
    }

    private static long solve(int n) {
        final BigInteger f = factorial(n);
        return factorial(2 * n).divide(f).divide(f).longValue();
    }
}
