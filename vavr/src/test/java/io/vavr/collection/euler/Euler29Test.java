/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2024 Vavr, https://vavr.io
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

import io.vavr.collection.Stream;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler29Test {

    /**
     * <strong>Problem 29: Distinct powers</strong>
     * <p>
     * Consider all integer combinations of ab for 2 ≤ a ≤ 5 and 2 ≤ b ≤ 5:
     * <ul>
     * <li>2<sup>2</sup>=4, 2<sup>3</sup>=8, 2<sup>4</sup>=16, 2<sup>5</sup>=32</li>
     * <li>3<sup>2</sup>=9, 3<sup>3</sup>=27, 3<sup>4</sup>=81, 3<sup>5</sup>=243</li>
     * <li>4<sup>2</sup>=16, 4<sup>3</sup>=64, 4<sup>4</sup>=256, 4<sup>5</sup>=1024</li>
     * <li>5<sup>2</sup>=25, 5<sup>3</sup>=125, 5<sup>4</sup>=625, 5<sup>5</sup>=3125</li>
     * </ul>
     * If they are then placed in numerical order, with any repeats removed, we get the following sequence of 15 distinct terms:
     * <p>
     * 4, 8, 9, 16, 25, 27, 32, 64, 81, 125, 243, 256, 625, 1024, 3125
     * <p>
     * How many distinct terms are in the sequence generated by ab for 2 ≤ a ≤ 100 and 2 ≤ b ≤ 100?
     * <p>
     * See also <a href="https://projecteuler.net/problem=29">projecteuler.net problem 29</a>.
     */
    @Test
    public void shouldSolveProblem29() {
        assertThat(cnt(5)).isEqualTo(15);
        assertThat(cnt(100)).isEqualTo(9183);
    }

    private static int cnt(int max) {
        return Stream.rangeClosed(2, max)
                .map(BigInteger::valueOf)
                .flatMap(a -> Stream.rangeClosed(2, max).map(a::pow))
                .distinct()
                .length();
    }
}
