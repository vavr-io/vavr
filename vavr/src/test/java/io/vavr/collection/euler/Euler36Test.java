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

import io.vavr.collection.CharSeq;
import io.vavr.collection.Stream;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 36: Double-base palindromes</strong>
 *
 * <p>The decimal number, 585 = 1001001001<sub>2</sub> (binary), is palindromic in both bases.
 *
 * <p>Find the sum of all numbers, less than one million, which are palindromic in base 10 and base
 * 2.
 *
 * <p class="info">(Please note that the palindromic number, in either base, may not include leading
 * zeros.) See also <a href="https://projecteuler.net/problem=36">projecteuler.net problem 36</a>.
 */
public class Euler36Test {

  @Test
  public void shouldSolveProblem36() {
    assertThat(solve(1000000)).isEqualTo(872187);
  }

  private static int solve(int n) {
    return Stream.range(1, n).filter(Euler36Test::isDoubleBasePalindrome).sum().intValue();
  }

  private static boolean isPalindrome(CharSeq seq) {
    return seq.dropWhile(c -> c == '0').equals(seq.reverse().dropWhile(c -> c == '0'));
  }

  private static boolean isDoubleBasePalindrome(int x) {
    final CharSeq seq = CharSeq.of(Integer.toString(x));
    final CharSeq rev = CharSeq.of(Integer.toBinaryString(x));
    return isPalindrome(seq) && isPalindrome(rev);
  }
}
