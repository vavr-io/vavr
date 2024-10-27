/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
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
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler55Test {

    /**
     * <strong>Problem 55: Lychrel numbers</strong>
     * <p>
     * If we take 47, reverse and add, 47 + 74 = 121, which is palindromic.
     * <p>
     * Not all numbers produce palindromes so quickly. For example,
     * <ul>
     * <li>349 + 943 = 1292
     * <li>1292 + 2921 = 4213
     * <li>4213 + 3124 = 7337
     * </ul>
     * That is, 349 took three iterations to arrive at a palindrome.
     * <p>
     * Although no one has proved it yet, it is thought that some numbers, like 196, never produce a palindrome.
     * A number that never forms a palindrome through the reverse and add process is called a Lychrel number.
     * Due to the theoretical nature of these numbers, and for the purpose of this problem, we shall assume
     * that a number is Lychrel until proven otherwise. In addition you are given that for every number
     * below ten-thousand, it will either (i) become a palindrome in less than fifty iterations, or,
     * (ii) no one, with all the computing power that exists, has managed so far to map it to a palindrome.
     * In fact, 10677 is the first number to be shown to require over fifty iterations
     * before producing a palindrome: 4668731596684224866951378664 (53 iterations, 28-digits).
     * <p>
     * Surprisingly, there are palindromic numbers that are themselves Lychrel numbers;
     * the first example is 4994.
     * <p>
     * How many Lychrel numbers are there below ten-thousand?
     * <p>
     * See also <a href="https://projecteuler.net/problem=55">projecteuler.net problem 55</a>.
     */
    @Test
    public void shouldSolveProblem55() {
        assertThat(solve()).isEqualTo(249);
    }

    private static int solve() {
        return Stream.range(1, 10_000)
                .filter(Euler55Test::isLychrel)
                .length();
    }

    private static boolean isLychrel(int n) {
        return Stream.iterate(String.valueOf(n), Euler55Test::next)
                .tail()  // Surprisingly, there are palindromic numbers that are themselves Lychrel numbers
                .take(50)
                .find(Utils::isPalindrome)
                .isEmpty();
    }

    private static String next(String s) {
        return List.of(s, Utils.reverse(s))
                .map(BigInteger::new)
                .reduce(BigInteger::add)
                .toString();
    }
}
