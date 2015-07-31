/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.Function1;
import javaslang.collection.Stream;

import java.util.function.Function;

public final class PrimeNumbers {

    private static final Stream<Long> PRIMES =
            Stream.of(2L).appendSelf(self -> Stream.gen(3L, i -> i + 2)
                            .filter(i -> self.takeWhile(j -> j * j <= i).forAll(k -> i % k > 0))
            );

    private static final Function<Integer, Long> MEMOIZED_PRIMES = Function1.lift(PRIMES::get).memoized();

    private PrimeNumbers() {
    }

    public static Stream<Long> primes() {
        return PRIMES;
    }

    public static long prime(int index) {
        if (index < 1) {
            throw new IllegalArgumentException("index < 1");
        }
        return MEMOIZED_PRIMES.apply(index - 1);
    }

    public static Stream<Long> primeFactors(long num) {
        return Stream.rangeClosed(2L, (int) Math.sqrt(num))
                .findFirst(d -> num % d == 0)
                .map(d -> Stream.cons(d, () -> primeFactors(num / d)))
                .orElseGet(() -> Stream.of(num));
    }
}
