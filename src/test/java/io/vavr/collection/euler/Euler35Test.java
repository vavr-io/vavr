/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2024 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.vavr.collection.euler;

import io.vavr.Function1;
import io.vavr.collection.CharSeq;
import io.vavr.collection.Stream;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 35: Circular primes</strong>
 * <p>The number, 197, is called a circular prime because all rotations of the digits: 197, 971, and 719, are themselves prime.</p>
 * <p>There are thirteen such primes below 100: 2, 3, 5, 7, 11, 13, 17, 31, 37, 71, 73, 79, and 97.</p>
 * <p>How many circular primes are there below one million?</p>
 * See also <a href="https://projecteuler.net/problem=35">projecteuler.net problem 35</a>.
 */
public class Euler35Test {

    @Test
    public void shouldSolveProblem35() {
        assertThat(rotations(197)).isEqualTo(List.of(197, 971, 719));
        assertThat(circularPrimes(100)).isEqualTo(13);
        assertThat(circularPrimes(1000000)).isEqualTo(55);
    }

    private static int circularPrimes(int n) {
        final Predicate<Integer> memoizedIsPrime = Function1.of(Euler35Test::isPrime).memoized()::apply;
        return Stream.rangeClosed(2, n)
                .filter(memoizedIsPrime)
                .map(Euler35Test::rotations)
                .filter(list -> list.forAll(memoizedIsPrime))
                .length();
    }

    private static boolean isPrime(int n) {
        return n == 2 || n % 2 != 0 &&
                Stream.rangeClosedBy(3, (int) Math.sqrt(n), 2)
                        .find(x -> n % x == 0)
                        .isEmpty();
    }

    private static List<Integer> rotations(int n) {
        final CharSeq seq = CharSeq.of(String.valueOf(n));
        return Stream.range(0, seq.length())
                .map(i -> seq.drop(i).appendAll(seq.take(i)))
                .map(s -> Integer.valueOf(s.toString()))
                .toList();
    }

}
