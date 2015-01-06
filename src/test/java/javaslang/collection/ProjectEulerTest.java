/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Contains high-level tests of the collection package.
 *
 * See also <a href="https://projecteuler.net">Project Euler</a> and Pavel Fatin's <a href="https://pavelfatin.com/ninety-nine">ninety-nine</a>.
 */
public class ProjectEulerTest {

    /**
     * <strong>Problem 1: Multiples of 3 and 5</strong>
     * <p>
     * If we list all the natural numbers below 10 that are multiples of 3 or 5, we get 3, 5, 6 and 9.
     * The sum of these multiples is 23.
     * </p>
     * <p>See also <a href="https://projecteuler.net/index.php?section=problems&id=1">projecteuler.net/problem1</a>.</p>
     */
    @Test
    public void shouldSolveProblem1() {
        // TODO(#75): List.range(1, 10).filter(n -> n % 3 == 0 || n % 5 == 0).sum()
        final int actual = List.range(1, 10).filter(n -> n % 3 == 0 || n % 5 == 0).reduceLeft((i,j) -> i + j);
        Assertions.assertThat(actual).isEqualTo(23);
    }
}
