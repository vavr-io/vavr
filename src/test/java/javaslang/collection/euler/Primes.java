/*
 *     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.collection.Stream;

final class Primes {

    private Primes() {
        throw new AssertionError(getClass().getName() + " is not intented to be instantiated.");
    }

    public static Stream<Integer> asStream() {
        return sieve(Stream.gen(2));
    }

    // TODO: prevent stack overflow
    private static Stream<Integer> sieve(Stream<Integer> numbers) {
        return new Stream.Cons<>(
                numbers.head(),
                () -> sieve(numbers.tail().filter(x -> x % numbers.head() > 0)));
    }
}
