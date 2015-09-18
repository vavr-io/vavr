/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.Function3;
import javaslang.collection.Vector;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler18Test {

    /**
     * <strong>Problem 18: Maximum path sum I</strong>
     * <p>
     * By starting at the top of the triangle below and moving to adjacent numbers on the row below, the maximum total from top to bottom is 23.
     * <pre>
     *        3
     *       7 4
     *      2 4 6
     *     8 5 9 3
     * </pre>
     * That is, 3 + 7 + 4 + 9 = 23.
     * <p>
     * Find the maximum total from top to bottom in p018_triangle.txt.
     * <p>
     * <strong>NOTE</strong>: As there are only 16384 routes, it is possible to solve this problem by trying every route.
     * However, Problem 67, is the same challenge with a triangle containing one-hundred rows;
     * it cannot be solved by brute force, and requires a clever method! ;o).
     * <p>
     * See also <a href="https://projecteuler.net/problem=18">projecteuler.net problem 18</a>.
     */
    @Test
    public void shouldSolveProblem18() {
        assertThat(solve("small_triangle.txt")).isEqualTo(23);
        assertThat(solve("p018_triangle.txt")).isEqualTo(1074);
    }

    private static int solve(String fileName) {
        return naive.apply(Euler67Test.loadTriangle(fileName), 0, 0);
    }

    private final static Function3<Vector<Vector<Integer>>, Integer, Integer, Integer> naive = Function3.lift(
            (Vector<Vector<Integer>> tr, Integer row, Integer col) -> {
                int value = tr.get(row).get(col);
                if (row == tr.length() - 1) {
                    return tr.get(row).get(col);
                } else {
                    return value + Math.max(Euler18Test.naive.apply(tr, row + 1, col), Euler18Test.naive.apply(tr, row + 1, col + 1));
                }
            }
    );
}
