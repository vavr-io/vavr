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

import io.vavr.collection.CharSeq;
import io.vavr.collection.Stream;
import org.junit.Test;

import static io.vavr.collection.euler.Utils.file;
import static io.vavr.collection.euler.Utils.readLines;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 22: Names scores</strong>
 * <p>
 * Using names.txt (right click and 'Save Link/Target As...'), a 46K text file
 * containing over five-thousand first names, begin by sorting it into
 * alphabetical order. Then working out the alphabetical value for each name,
 * multiply this value by its alphabetical position in the list to obtain a name
 * score.
 * <p>
 * For example, when the list is sorted into alphabetical order, COLIN, which is
 * worth 3 + 15 + 12 + 9 + 14 = 53, is the 938th name in the list. So, COLIN
 * would obtain a score of 938 × 53 = 49714.
 * <p>
 * What is the total of all the name scores in the file?
 * <p>
 * See also <a href="https://projecteuler.net/problem=22">projecteuler.net
 * problem 22</a>.
 */
public class Euler22Test {

    @Test
    public void shouldSolveProblem22() {
        assertThat(nameScore("COLIN", 938)).isEqualTo(49714);
        assertThat(totalOfAllNameScores()).isEqualTo(871_198_282);
    }

    private static long nameScore(String name, long position) {
        return CharSeq.of(name)
                .map(c -> c - 'A' + 1)
                .sum().longValue() * position;
    }

    private static long totalOfAllNameScores() {
        return readLines(file("p022_names.txt"))
                .map(l -> l.replaceAll("\"", ""))
                .flatMap(l -> Stream.of(l.split(",")))
                .sorted()
                .zipWithIndex()
                .map(t -> nameScore(t._1, t._2 + 1))
                .sum().longValue();
    }
}
