/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.Function1;
import javaslang.Function2;
import javaslang.collection.Stream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler14Test {

    /**
     * <strong>Problem 14: Longest Collatz sequence</strong>
     * <p>
     *
     * The following iterative sequence is defined for the set of positive integers:
     *
     *    n → n/2 (n is even)
     *    n → 3n + 1 (n is odd)
     *
     * Using the rule above and starting with 13, we generate the following sequence:
     *
     * 13 → 40 → 20 → 10 → 5 → 16 → 8 → 4 → 2 → 1
     * It can be seen that this sequence (starting at 13 and finishing at 1) contains 10 terms.
     * Although it has not been proved yet (Collatz Problem), it is thought that all starting numbers finish at 1.
     *
     * Which starting number, under one million, produces the longest chain?

     * NOTE: Once the chain starts the terms are allowed to go above one million.
     * </p>
     * See also <a href="https://projecteuler.net/problem=14">projecteuler.net problem 14</a>.
     */
    
    @Test
    public void shouldSolveProblem14() {
        // equivalent to from(1L).take(1_000_000)
        assertThat(Stream.from(500_000L)
                .take(500_000)
                .maxBy(collatzSequenceLength)
                .get()).isEqualTo(837799);
    }

    private final static Function2<Long, Long, Long> collatzRecursive = (len, n) -> {
        if (n == 1) return len + 1;
        else if (n % 2 == 0) {
            return Euler14Test.collatzRecursive.apply(len + 1, n / 2);
        } else {
            return Euler14Test.collatzRecursive.apply(len + 1, (3 * n) + 1);
        }
    };

    private final static Function1<Long, Long> collatzSequenceLength = collatzRecursive.memoized().curried().apply(0L);
}
