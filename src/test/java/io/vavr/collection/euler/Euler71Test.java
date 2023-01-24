/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2023 Vavr, https://vavr.io
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

import io.vavr.Tuple;
import io.vavr.Tuple4;
import io.vavr.collection.Stream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler71Test {

    /**
     * <strong>Problem 71: Ordered fractions</strong>
     * <p>
     * Consider the fraction, n/d, where n and d are positive integers. If n<d and HCF(n,d)=1, it is called a reduced proper fraction.
     * <p>
     * If we list the set of reduced proper fractions for d ≤ 8 in ascending order of size, we get:
     * <p>
     * 1/8, 1/7, 1/6, 1/5, 1/4, 2/7, 1/3, 3/8, 2/5, 3/7, 1/2, 4/7, 3/5, 5/8, 2/3, 5/7, 3/4, 4/5, 5/6, 6/7, 7/8
     * <p>
     * It can be seen that 2/5 is the fraction immediately to the left of 3/7.
     * <p>
     * By listing the set of reduced proper fractions for d ≤ 1,000,000 in ascending order of size, find the numerator of the fraction immediately to the left of 3/7.
     * <p>
     * See also <a href="https://projecteuler.net/problem=71">projecteuler.net problem 71</a>.
     */
    @Test
    public void shouldSolveProblem71() {
        assertThat(left37(8)).isEqualTo(2);
        assertThat(left37(1_000_000)).isEqualTo(428_570);
    }

    private static int left37(int maxDenominator) {
        return Stream.iterate(Tuple.of(0, 1, 1, 1), (Tuple4<Integer, Integer, Integer, Integer> t) -> {
            final int m1 = t._1 + t._3;
            final int m2 = t._2 + t._4;
            if (m1 * 7 >= m2 * 3) {
                return Tuple.of(t._1, t._2, m1, m2);
            } else {
                return Tuple.of(m1, m2, t._3, t._4);
            }
        }).takeWhile(t -> t._2 <= maxDenominator && t._4 <= maxDenominator).last()._1;
    }
}
