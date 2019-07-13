/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2019 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection.euler;

import org.junit.Test;

import static java.util.Arrays.asList;
import static io.vavr.collection.euler.Utils.file;
import static io.vavr.collection.euler.Utils.readLines;
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

    private static long solve() {
        return readLines(file("p099_base_exp.txt"))
                .flatMap(s -> asList(s.split(",")))
                .map(Integer::parseInt)
                .grouped(2)
                .map(t -> t.get(1) * Math.log(t.get(0)))
                .zipWithIndex()
                .reduce((t1, t2) -> t1._1 > t2._1 ? t1 : t2)
                ._2 + 1;
    }
}
