/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.collection.Stream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 24: Lexicographic permutations</strong>
 *
 * <p>A permutation is an ordered arrangement of objects. For example, 3124 is one possible permutation of the digits 1, 2, 3 and 4. If all of the permutations are listed numerically or alphabetically, we call it lexicographic order. The lexicographic permutations of 0, 1 and 2 are:</p>
 * <p style="text-align:center;">012   021   102   120   201   210</p>
 * <p>What is the millionth lexicographic permutation of the digits 0, 1, 2, 3, 4, 5, 6, 7, 8 and 9?</p>
 *
 * See also <a href="https://projecteuler.net/problem=24">projecteuler.net problem 24</a>.
 */

public class Euler24Test {

    @Test
    public void shouldSolveProblem24() {
        List<Integer> listOf012 = List.rangeClosed(0, 2);
        List<List<Integer>> allPermutationsOf012 = Stream.rangeClosed(1, 6).
                map(n -> getNthPermutation(listOf012, n))
                .foldLeft(List.empty(), List::append);
        assertThat(allPermutationsOf012).isEqualTo(listOf012.permutations());
        List<Integer> permutedList = getNthPermutation(List.rangeClosed(0, 9), 1000000);
        assertThat(permutedList.map(String::valueOf).fold("", String::concat)).isEqualTo("2783915460");
    }

    static <T extends Comparable<? super T>> List<T> nextPermutation(List<T> list) {
        int pivot = list.zip(list.tail()).lastIndexWhere((a) -> a._1.compareTo(a._2) < 0);
        if (pivot == -1) {
            return list;
        }
        T elementAtPivot = list.get(pivot);
        int endIdx = list.lastIndexWhere((a) -> a.compareTo(elementAtPivot) > 0);
        list = list.update(pivot, list.get(endIdx)).update(endIdx, elementAtPivot);
        Tuple2<List<T>, List<T>> tuple2 = list.splitAt(pivot + 1);
        return tuple2._1.appendAll(tuple2._2.reverse());
    }

    static List<Integer> getNthPermutation(List<Integer> list, int n) {
        for (int i = 1; i < n; ++i) {
            list = nextPermutation(list);
        }
        return list;
    }

}
