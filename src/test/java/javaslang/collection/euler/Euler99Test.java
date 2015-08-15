/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.collection.Iterator;
import javaslang.collection.Stream;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler99Test {

    /**
     * <strong>Problem 99: Largest exponential</strong>
     * <p>
     * Comparing two numbers written in index form like 2<sup>11</sup> and 3<sup>7</sup> is not difficult,
     * as any calculator would confirm that 2<sup>11</sup> = 2048 &lt; 3<sup>7</sup> = 2187.
     * <p>
     * However, confirming that 632382<sup>518061</sup> &gt; 519432<sup>525806</sup> would be much more difficult,
     * as both numbers contain over three million digits.
     * <p>
     * Using p099_base_exp.txt, a 22K text file containing one thousand lines with a base/exponent pair on each line,
     * determine which line number has the greatest numerical value.
     * <p>
     * See also <a href="https://projecteuler.net/problem=99">projecteuler.net problem 99</a>.
     */
    @Test
    public void shouldSolveProblem99() {
        assertThat(solve()).isEqualTo(709);
    }

    private static int solve() {
        return readLines(file("p099_base_exp.txt"))
                .flatMap(s -> Arrays.asList(s.split(",")))
                .map(Integer::parseInt)
                .grouped(2)
                .map(t -> t.get(1) * Math.log(t.get(0)))
                .zipWithIndex()
                .reduce((t1, t2) -> t1._1 > t2._1 ? t1 : t2)
                ._2 + 1;
    }

    private static Stream<String> readLines(File file) {
        final Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            return Stream.empty();
        }
        return Stream.ofAll(new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return scanner.hasNextLine();
            }

            @Override
            public String next() {
                return scanner.nextLine();
            }
        });
    }

    private static File file(String fileName) {
        final URL resource = Euler99Test.class.getClassLoader().getResource(fileName);
        if(resource == null) {
            throw new RuntimeException("resource not found");
        }
        return new File(resource.getFile());
    }
}
