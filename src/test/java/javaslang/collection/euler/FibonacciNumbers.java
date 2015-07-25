/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.Function1;
import javaslang.control.Match;

import java.util.function.Function;

public final class FibonacciNumbers {

    private static final Function<Integer, Long> MEMOIZED_FIBONACCI = Function1.lift(FibonacciNumbers::compute).memoized();

    private FibonacciNumbers() {
    }

    public static long fibonacci(int order) {
        return MEMOIZED_FIBONACCI.apply(order);
    }

    private static long compute(int order) {
        return Match
                .when(0).then(() -> 0L)
                .whenIn(1, 2).then(() -> 1L)
                .otherwise(() -> MEMOIZED_FIBONACCI.apply(order - 2) + MEMOIZED_FIBONACCI.apply(order - 1))
                .apply(order);
    }
}
