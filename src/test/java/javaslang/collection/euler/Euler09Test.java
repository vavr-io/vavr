/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.Tuple;
import javaslang.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler09Test {

    /**
     * <strong>Problem 9: Special Pythagorean triplet</strong>
     * <p>
     * A Pythagorean triplet is a set of three natural numbers, a < b < c, for which,
     * a<sup>2</sup> + b<sup>2</sup> = c<sup>2</sup>
     * <p>
     * For example, 3<sup>2</sup> + 4<sup>2</sup> = 9 + 16 = 25 = 5<sup>2</sup>.
     * <p>
     * There exists exactly one Pythagorean triplet for which a + b + c = 1000.
     * Find the product abc.
     * <p>
     * See also <a href="https://projecteuler.net/problem=9">projecteuler.net problem 9</a>.
     */
    @Test
    public void shouldSolveProblem9() {
        assertThat(abc(1_000)).isEqualTo(31_875_000);
    }

    public int abc(int sum) {
        return List.rangeClosed(1, sum)
                .crossProduct()
                .filter(t -> t._1 + t._2 < sum)
                .map(t -> Tuple.of(t._1, t._2, sum - t._1 - t._2))
                .filter(t -> t._1 * t._1 + t._2 * t._2 == t._3 * t._3)
                .map(t -> t._1 * t._2 * t._3)
                .head();
    }
}
