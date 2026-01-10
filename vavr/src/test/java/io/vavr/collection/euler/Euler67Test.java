/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
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

import io.vavr.Function3;
import io.vavr.collection.Vector;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

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
