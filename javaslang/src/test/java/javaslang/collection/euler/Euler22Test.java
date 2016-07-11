/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.collection.CharSeq;
import javaslang.collection.Stream;
import static javaslang.collection.euler.Utils.file;
import static javaslang.collection.euler.Utils.readLines;
import org.junit.Test;

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
