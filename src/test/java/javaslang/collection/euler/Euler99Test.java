/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import org.junit.Test;

import java.util.Arrays;

import static javaslang.collection.euler.Utils.file;
import static javaslang.collection.euler.Utils.readLines;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler99Test {

    /**
     * <strong>Problem 99: Largest exponential</strong>
     * <p>
     * Comparing two numbers written in index form like 2<sup>11</sup> and 3<sup>7</sup> is not difficult,
     * as any calculator would confirm that 2<sup>11</sup> = 2048 &lt; 3<sup>7</sup> = 2187.
     * <p>
     * However, confirming that 632382<sup>518061</sup> &gt; 519432<sup>525806</sup> would be much more difficult,
     * as both numbers contain over three million digits.
     * <p>
     * Using p099_base_exp.txt, a 22K text file containing one thousand lines with a base/exponent pair on each line,
     * determine which line number has the greatest numerical value.
     * <p>
     * See also <a href="https://projecteuler.net/problem=99">projecteuler.net problem 99</a>.
     */
    @Test
    public void shouldSolveProblem99() {
        assertThat(solve()).isEqualTo(709);
    }

    private static int solve() {
        return readLines(file("p099_base_exp.txt"))
                .flatMap(s -> Arrays.asList(s.split(",")))
                .map(Integer::parseInt)
                .grouped(2)
                .map(t -> t.get(1) * Math.log(t.get(0)))
                .zipWithIndex()
                .reduce((t1, t2) -> t1._1 > t2._1 ? t1 : t2)
                ._2 + 1;
    }
}
