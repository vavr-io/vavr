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

import io.vavr.Function3;
import io.vavr.collection.Vector;
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

    private final static Function3<Vector<Vector<Integer>>, Integer, Integer, Integer> naive = Function3.of(
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
