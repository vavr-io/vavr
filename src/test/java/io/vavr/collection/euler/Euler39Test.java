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

import io.vavr.Tuple;
import io.vavr.collection.Map;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import org.junit.Test;

import static java.lang.Math.floor;
import static java.lang.Math.hypot;
import static io.vavr.control.Option.some;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler39Test {

    /**
     * <strong>Problem 39 Integer right triangles</strong>
     * <p>
     * If <i>p</i> is the perimeter of a right angle triangle with integral
     * length sides, {<i>a,b,c</i>}, there are exactly three solutions for
     * <i>p</i> = 120.
     * <p>
     * {20,48,52}, {24,45,51}, {30,40,50}
     * <p>
     * For which value of <i>p</i> ≤ 1000, is the number of solutions maximised?
     * <p>
     * See also <a href="https://projecteuler.net/problem=39">projecteuler.net
     * problem 39</a>.
     */
    @Test
    public void shouldSolveProblem39() {
        assertThat(SOLUTIONS_FOR_PERIMETERS_UP_TO_1000.get(120)).isEqualTo(some(List.of(Tuple.of(20, 48, 52), Tuple.of(24, 45, 51), Tuple.of(30, 40, 50))));

        assertThat(perimeterUpTo1000WithMaximisedNumberOfSolutions()).isEqualTo(840);
    }

    private static int perimeterUpTo1000WithMaximisedNumberOfSolutions() {
        return SOLUTIONS_FOR_PERIMETERS_UP_TO_1000
                .map((perimeter, listOfSolutions) -> Tuple.of(perimeter, listOfSolutions.length()))
                .maxBy(Tuple2::_2)
                .get()._1;
    }

    private static final Map<Integer, List<Tuple3<Integer, Integer, Integer>>> SOLUTIONS_FOR_PERIMETERS_UP_TO_1000
            = List.rangeClosed(1, 500)
            .flatMap(a -> List.rangeClosed(a, 500)
                    .map(b -> Tuple.of(a, b, hypot(a, b))))
            .filter(t -> floor(t._3) == t._3)
            .map(t -> t.map3(Double::intValue))
            .groupBy(t -> t.apply((a, b, c) -> a + b + c))
            .filterKeys(d -> d <= 1_000);
}
