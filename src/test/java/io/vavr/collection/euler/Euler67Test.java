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

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler67Test {

    /**
     * <strong>Problem 67: Maximum path sum II</strong>
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
     * Find the maximum total from top to bottom in p067_triangle.txt, a 15K text file containing a triangle with one-hundred rows.
     * <p>
     * <strong>NOTE</strong>: This is a much more difficult version of Problem 18.
     * <p>
     * See also <a href="https://projecteuler.net/problem=67">projecteuler.net problem 67</a>.
     */
    @Test
    public void shouldSolveProblem67() {
        assertThat(solve("small_triangle.txt")).isEqualTo(23);
        assertThat(solve("p067_triangle.txt")).isEqualTo(7273);
    }

    private static int solve(String fileName) {
        return smart.apply(loadTriangle(fileName), 0, 0);
    }

    private final static Function3<Vector<Vector<Integer>>, Integer, Integer, Integer> smart = Function3.of(
            (Vector<Vector<Integer>> tr, Integer row, Integer col) -> {
                int value = tr.get(row).get(col);
                if (row == tr.length() - 1) {
                    return tr.get(row).get(col);
                } else {
                    return value + Math.max(Euler67Test.smart.apply(tr, row + 1, col), Euler67Test.smart.apply(tr, row + 1, col + 1));
                }
            }
    ).memoized();

    static Vector<Vector<Integer>> loadTriangle(String fileName) {
        return Vector.ofAll(
                Utils.readLines(Utils.file(fileName)).map(line ->
                        Arrays.stream(line.split("\\s")).map(Integer::parseInt)
                ).map(s -> Vector.ofAll(s::iterator))
        );
    }
}
