/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2022 Vavr, https://vavr.io
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
import io.vavr.collection.CharSeq;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler32Test {

    /**
     * <strong>Problem 23 Pandigital products</strong>
     * <p>
     * We shall say that an n-digit number is pandigital if it makes use of all
     * the digits 1 to n exactly once; for example, the 5-digit number, 15234,
     * is 1 through 5 pandigital.
     * <p>
     * The product 7254 is unusual, as the identity, 39 × 186 = 7254, containing
     * multiplicand, multiplier, and product is 1 through 9 pandigital.
     * <p>
     * Find the sum of all products whose multiplicand/multiplier/product
     * identity can be written as a 1 through 9 pandigital.
     * <p>
     * HINT: Some products can be obtained in more than one way so be sure to
     * only include it once in your sum.
     * <p>
     * See also <a href="https://projecteuler.net/problem=32">projecteuler.net
     * problem 32</a>.
     */
    @Test
    public void shouldSolveProblem32() {
        assertThat(isPandigital(1, 5, "15234")).isTrue();
        assertThat(isPandigital(1, 9, "39" + "186" + "7254")).isTrue();
        assertThat(isPandigital(1, 5, "55555")).isFalse();
        assertThat(isPandigital(1, 5, "12340")).isFalse();
        assertThat(sumOfAllProductsPandigital1Through9()).isEqualTo(45228);
    }

    private static boolean isPandigital(int from, int to, String num) {
        return num.length() == to - from + 1 && List.rangeClosed(from, to).forAll(i -> num.contains(Integer.toString(i)));
    }

    private static final CharSeq DIGITS_1_9 = CharSeq.of("123456789");

    private static long sumOfAllProductsPandigital1Through9() {
        return List.of(1, 2)
                .flatMap(i -> DIGITS_1_9.crossProduct(i)
                        .flatMap(multiplicand -> DIGITS_1_9.removeAll(multiplicand).crossProduct(5 - i)
                                .map(multiplier -> Tuple.of(multiplicand.mkString(), multiplier.mkString()))
                        )
                )
                .map(t -> Tuple.of(t._1, t._2, Long.valueOf(t._1) * Long.valueOf(t._2)))
                .filter(t -> isPandigital(1, 9, t._1 + t._2 + Long.toString(t._3)))
                .map(Tuple3::_3)
                .distinct()
                .sum().longValue();
    }
}
