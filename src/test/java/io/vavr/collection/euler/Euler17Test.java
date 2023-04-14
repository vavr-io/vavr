/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2023 Vavr, https://vavr.io
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

import io.vavr.Tuple;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Vector;
import org.junit.Test;

import static io.vavr.API.*;
import static io.vavr.collection.Stream.rangeClosed;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler17Test {
    /**
     * <strong>Problem 17: Number letter counts</strong>
     * <p>
     * If the numbers 1 to 5 are written out in words: one, two, three, four,
     * five, then there are 3 + 3 + 5 + 4 + 4 = 19 letters used in total.
     * <p>
     * If all the numbers from 1 to 1000 (one thousand) inclusive were written
     * out in words, how many letters would be used?
     * <p>
     * NOTE: Do not count spaces or hyphens. For example, 342 (three hundred and
     * forty-two) contains 23 letters and 115 (one hundred and fifteen) contains
     * 20 letters. The use of "and" when writing out numbers is in compliance
     * with British usage.
     */
    @Test
    public void shouldSolveProblem17() {
        runTestsFor(new SolutionA());
        runTestsFor(new SolutionB());

        // Additional capability, only for more general solution B
        assertThat(new SolutionB().letterCount(Integer.MAX_VALUE)).isEqualTo("twobilliononehundredandfortysevenmillionfourhundredandeightythreethousandsixhundredandfortyseven".length());
    }

    private static void runTestsFor(SolutionProblem17 solution) {
        List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty", "twentyone")
                .zipWithIndex()
                .forEach(t -> {
                    final int number = t._2 + 1;
                    final String numberAsString = t._1;
                    assertThat(numberAsString).hasSize(solution.letterCount(number));
                });

        assertThat(solution.letterCount(200)).isEqualTo("twohundred".length());
        assertThat(solution.letterCount(201)).isEqualTo("twohundredandone".length());

        assertThat(solution.letterCount(342)).isEqualTo(23);
        assertThat(solution.letterCount(115)).isEqualTo(20);

        assertThat(solution.letterCount(rangeClosed(1, 5))).isEqualTo(19);
        assertThat(solution.letterCount(rangeClosed(1, 1000))).isEqualTo(21124);
    }

    private interface SolutionProblem17 {
        int letterCount(int num);

        default int letterCount(Seq<Integer> range) {
            return range.map(this::letterCount)
                    .sum().intValue();
        }
    }

    static final String CONJUNCTION = "and";
    static final Map<Integer, String> LENGTHS = List.of(
            1, "one",
            2, "two",
            3, "three",
            4, "four",
            5, "five",
            6, "six",
            7, "seven",
            8, "eight",
            9, "nine",
            10, "ten",
            11, "eleven",
            12, "twelve",
            13, "thirteen",
            14, "fourteen",
            15, "fifteen",
            16, "sixteen",
            17, "seventeen",
            18, "eighteen",
            19, "nineteen",
            20, "twenty",
            30, "thirty",
            40, "forty",
            50, "fifty",
            60, "sixty",
            70, "seventy",
            80, "eighty",
            90, "ninety",
            100, "hundred",
            1_000, "thousand",
            1_000_000, "million",
            1_000_000_000, "billion"
    ).grouped(2).toSortedMap(pair -> Tuple.of((Integer) pair.get(0), (String) pair.get(1)));

    /**
     * Solution using Vavr Pattern Matching.
     */
    private static final class SolutionA implements SolutionProblem17 {
        @Override
        public int letterCount(int num) {
            return Match(num).of( /*@formatter:off*/
                    Case($(n -> n >= 1000),           n -> length(n / 1000) + length(1000) + letterCount(n % 1000)),
                    Case($(n -> n >= 100),            n -> Match(n).of(
                        Case($(n1 -> (n1 % 100) > 0), n1 -> length(n1 / 100) + length(100) + CONJUNCTION.length() + letterCount(n1 % 100)),
                        Case($(),                  length(n / 100) + length(100)))),
                    Case($(n -> n >= 20),             n -> length(n - (n % 10)) + letterCount(n % 10)),
                    Case($(0),                        0),
                    Case($(),                         n -> length(n))
            ); /*@formatter:on*/
        }

        private static int length(int number) {
            return LENGTHS.get(number).map(String::length).get();
        }
    }

    /**
     * A more general solution using functionality of the Vavr Collections.
     */
    private static final class SolutionB implements SolutionProblem17 {
        @Override
        public int letterCount(int number) {
            return asText(number).length();
        }

        private static String asText(int number) {
            return LENGTHS.foldRight(Tuple.of(Vector.<String> empty(), number), (magnitudeAndText, lengthsAndRemainder) -> {
                final int magnitude = magnitudeAndText._1;
                final int remainder = lengthsAndRemainder._2;

                return ((remainder >= magnitude) && (remainder > 0)) ? asText(magnitude, magnitudeAndText._2, lengthsAndRemainder._1, remainder)
                                                                     : lengthsAndRemainder;
            })._1.mkString();
        }

        private static Tuple2<Vector<String>, Integer> asText(int magnitude, String text, Vector<String> chunks, int remainder) {
            if (remainder >= 100) {
                text = asText(remainder / magnitude) + text;
                if ((remainder < 1000) && ((remainder % magnitude) != 0)) {
                    text += CONJUNCTION;
                }
            }
            return Tuple.of(chunks.append(text), remainder % magnitude);
        }
    }
}
