/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
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
import io.vavr.collection.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler45Test {

    /**
     * <strong>Problem 45 Triangular, pentagonal, and hexagonal</strong>
     *
     * <p>Triangle, pentagonal, and hexagonal numbers are generated by the following formulae:</p>
     * <p>Triangle      Tn=n(n+1)/2     1, 3,  6, 10, 15, ...</p>
     * <p>Pentagonal    Pn=n(3n−1)/2    1, 5, 12, 22, 35, ...</p>
     * <p>Hexagonal     Hn=n(2n−1)      1, 6, 15, 28, 45, ...</p>
     *
     * <p>
     * It can be verified that T285 = P165 = H143 = 40755.
     * Find the next triangle number that is also pentagonal and hexagonal.
     * </p>
     *
     * See also <a href="https://projecteuler.net/problem=45">projecteuler.net
     * problem 45</a>.
     */

    @Test
    public void shouldSolveProblem45() {
        assertThat(List.of(5, 12, 22, 35)).allMatch(Euler45Test::isPentagonal);
        assertThat(List.of(3, 11, 21, 36)).allMatch(i -> !isPentagonal(i));
        Assertions.assertThat(HEXAGONAL.take(5)).containsExactly(6L, 15L, 28L, 45L, 66L);

        assertThat(HEXAGONAL
                .filter(Euler45Test::isPentagonal)
                .head())
                .isEqualTo(40755);

        assertThat(HEXAGONAL
                .filter(Euler45Test::isPentagonal)
                .tail()
                .head())
                .isEqualTo(1533776805L);
    }

    private static final Stream<Long> HEXAGONAL = Stream.from(2L).map(i -> i*(2*i -1));

    private static boolean isPentagonal(long i) {
        // If a number k is pentagonal then n(3n−1)/2 = k; for some integer n
        // by the quadratic formula (1+sqrt(1+4*3*2k))/6 = n
        long discriminant = 1+24*i;
        return isPerfectSquare(discriminant) && (1 + flooredRoot(discriminant)) % 6 == 0;
    }

    private static boolean isPerfectSquare(long i) {
        long sqrtFloor = flooredRoot(i);
        return sqrtFloor*sqrtFloor == i;
    }

    private static long flooredRoot(long i) {
        return (long)Math.sqrt(i+0.5);
    }
}
