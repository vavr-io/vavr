/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.collection.List;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>Contains high-level tests of the collection package.</p>
 * <p>See also <a href="https://projecteuler.net">Project Euler</a> and Pavel Fatin's
 * <a href="https://pavelfatin.com/ninety-nine">ninety-nine</a>.</p>
 */
public class ProjectEulerTest {

    /**
     * <strong>Problem 1: Multiples of 3 and 5</strong>
     * <p>If we list all the natural numbers below 10 that are multiples of 3 or 5, we get 3, 5, 6 and 9.
     * The sum of these multiples is 23.</p>
     * <p>Find the sum of all the multiples of 3 or 5 below 1000.</p>
     * <p>See also <a href="https://projecteuler.net/problem=1">projecteuler.net problem 1</a>.</p>
     */
    @Test
    public void shouldSolveProblem1() {
        assertThat(sumOfMultiplesOf3and5Below(10)).isEqualTo(23);
        assertThat(sumOfMultiplesOf3and5Below(1000)).isEqualTo(233168);
    }

    private static int sumOfMultiplesOf3and5Below(int limit) {
        return List.range(0, limit).filter(ProjectEulerTest::isMultipleOf3or5).sum().intValue();
    }

    private static boolean isMultipleOf3or5(int num) {
        return num != 0 && (num % 3 == 0 || num % 5 == 0);
    }


    /**
     * <strong>Problem 7: 10001st prime</strong>
     * <p>By listing the first six prime numbers: 2, 3, 5, 7, 11, and 13, we can see that the 6th prime is 13.</p>
     * <p>What is the 10 001st prime number?</p>
     * <p>See also <a href="https://projecteuler.net/problem=7">projecteuler.net problem 7</a>.</p>
     */
    @Test
    @Ignore
    public void shouldSolveProblem7() {
        final int actual = Primes.asStream().drop(10_000).head();
        assertThat(actual).isEqualTo(0); // TODO, see Primes#sieve(Stream)
    }
}
