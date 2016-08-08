/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.API;
import static javaslang.API.$;
import static javaslang.API.Case;
import javaslang.Function1;
import javaslang.collection.List;
import static javaslang.collection.euler.Utils.factorial;
import org.junit.Test;

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
                    assertThat(lexiographicPermutaionNaive(List.of("1", "0", "2"), p._2 + 1)).isEqualTo(p._1);
                    assertThat(lexiographicPermutaion(List.of("1", "0", "2"), p._2 + 1)).isEqualTo(p._1);
                });

        assertThat(lexiographicPermutaion(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"), 1_000_000)).isEqualTo("2783915460");
    }

    /**
     * Naïve version. Very readable, but not performant enough for calculating
     * "the millionth lexicographic permutation of the digits 0, 1, 2, 3, 4, 5,
     * 6, 7, 8 and 9" (takes about 40 seconds on an average laptop).
     */
    private static String lexiographicPermutaionNaive(List<String> stringsToPermutate, int ordinal) {
        return stringsToPermutate.permutations()
                .map(List::mkString)
                .sorted()
                .get(ordinal - 1);
    }

    /**
     * More performant version that uses an algorithm that calculates the number
     * of permutations achievable in each position instead of actually doing the permutations.
     */
    private static String lexiographicPermutaion(List<String> stringsToPermutate, int ordinal) {
        return API.Match(stringsToPermutate.sorted()).of(
                Case($((List<String> sx) -> sx.length() == 1), sx -> sx.mkString()),
                Case($(), sx -> {
                    final int noOfPossiblePermutationsInTail = memoizedFactorial.apply(sx.length() - 1);
                    final int headCharPosition = ((ordinal + noOfPossiblePermutationsInTail - 1) / noOfPossiblePermutationsInTail);
                    final int ordinalRest = Integer.max(0, ordinal - ((headCharPosition - 1) * noOfPossiblePermutationsInTail));
                    return List.of(sx.get(headCharPosition - 1)).mkString() + lexiographicPermutaion(sx.removeAt(headCharPosition - 1), ordinalRest);
                })
        );
    }

    private static final Function1<Integer, Integer> memoizedFactorial = Function1.of((Integer i) -> factorial(i).intValue()).memoized();
}
