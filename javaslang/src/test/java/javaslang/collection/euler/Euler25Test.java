/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import org.junit.Test;

import static javaslang.collection.euler.Utils.fibonacci;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 25: 1000-digit Fibonacci number</strong>
 * <p>
 * The Fibonacci sequence is defined by the recurrence relation:
 * <p>
 * Fn = Fn−1 + Fn−2, where F1 = 1 and F2 = 1. Hence the first 12 terms will be:
 * <pre>
 * F1 = 1
 * F2 = 1
 * F3 = 2
 * F4 = 3
 * F5 = 5
 * F6 = 8
 * F7 = 13
 * F8 = 21
 * F9 = 34
 * F10 = 55
 * F11 = 89
 * F12 = 144
 * </pre>
 * <p>
 * The 12th term, F12, is the first term to contain three digits.
 * <p>
 * What is the first term in the Fibonacci sequence to contain 1000 digits?
 * <p>
 * See also <a href="https://projecteuler.net/problem=25">projecteuler.net
 * problem 25</a>.
 */
public class Euler25Test {

    @Test
    public void shouldSolveProblem25() {
        assertThat(firstFibonacciTermContaining(3)).isEqualTo(12);
        assertThat(firstFibonacciTermContaining(1000)).isEqualTo(4782);
    }

    private static int firstFibonacciTermContaining(int digits) {
        return fibonacci()
                .zipWithIndex()
                .find(t -> t._1.toString().length() == digits)
                .get()._2;
    }
}
