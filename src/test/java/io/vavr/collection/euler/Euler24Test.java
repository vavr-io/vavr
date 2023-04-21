/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
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
import io.vavr.API;
import io.vavr.collection.List;
import org.junit.Test;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.collection.euler.Utils.factorial;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 24: Lexicographic permutations</strong>
 * <p>
 * A permutation is an ordered arrangement of objects. For example, 3124 is one
 * possible permutation of the digits 1, 2, 3 and 4. If all of the permutations
 * are listed numerically or alphabetically, we call it lexicographic order. The
 * lexicographic permutations of 0, 1 and 2 are:
 * <p>
 * 012 021 102 120 201 210
 * <p>
 * What is the millionth lexicographic permutation of the digits 0, 1, 2, 3, 4,
 * 5, 6, 7, 8 and 9?
 * <p>
 * See also <a href="https://projecteuler.net/problem=24">projecteuler.net
 * problem 24</a>.
 */
public class Euler24Test {

    @Test
    public void shouldSolveProblem24() {
        List.of("012", "021", "102", "120", "201", "210").zipWithIndex()
                .forEach(p -> {
                    assertThat(lexicographicPermutationNaive(List.of("1", "0", "2"), p._2 + 1)).isEqualTo(p._1);
                    assertThat(lexicographicPermutation(List.of("1", "0", "2"), p._2 + 1)).isEqualTo(p._1);
                });

        assertThat(lexicographicPermutation(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"), 1_000_000)).isEqualTo("2783915460");
    }

    /**
     * Naïve version. Very readable, but not performant enough for calculating
     * "the millionth lexicographic permutation of the digits 0, 1, 2, 3, 4, 5,
     * 6, 7, 8 and 9" (takes about 40 seconds on an average laptop).
     */
    private static String lexicographicPermutationNaive(List<String> stringsToPermutate, int ordinal) {
        return stringsToPermutate.permutations()
                .map(List::mkString)
                .sorted()
                .get(ordinal - 1);
    }

    /**
     * More performant version that uses an algorithm that calculates the number
     * of permutations achievable in each position instead of actually doing the permutations.
     */
    private static String lexicographicPermutation(List<String> stringsToPermutate, int ordinal) {
        return API.Match(stringsToPermutate.sorted()).of(
                Case($((List<String> sx) -> sx.length() == 1), sx -> sx.mkString()),
                Case($(), sx -> {
                    final int noOfPossiblePermutationsInTail = memoizedFactorial.apply(sx.length() - 1);
                    final int headCharPosition = ((ordinal + noOfPossiblePermutationsInTail - 1) / noOfPossiblePermutationsInTail);
                    final int ordinalRest = Integer.max(0, ordinal - ((headCharPosition - 1) * noOfPossiblePermutationsInTail));
                    return List.of(sx.get(headCharPosition - 1)).mkString() + lexicographicPermutation(sx.removeAt(headCharPosition - 1), ordinalRest);
                })
        );
    }

    private static final Function1<Integer, Integer> memoizedFactorial = Function1.of((Integer i) -> factorial(i).intValue()).memoized();
}
