/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.collection.Stream;

public final class PrimeNumbers {

    private PrimeNumbers() {
    }

    public static Stream<Long> primes() {
        return Sieve.INSTANCE.streamOfLongs();
    }

    public static long prime(int index) {
        if (index < 1) {
            throw new IllegalArgumentException("index < 1");
        }
        return Sieve.INSTANCE.array()[index-1];
    }

    public static HashMap<Long, Long> factorization(long num) {
        if(num == 1) {
            return HashMap.empty();
        } else {
            return primeFactors(num)
                    .map(p -> HashMap.of(Map.Entry.of(p, 1L))
                            .merge(factorization(num / p), (a, b) -> a + b))
                    .orElseGet(HashMap::empty);
        }
    }

    public static Stream<Long> primeFactors(long num) {
        return Stream.rangeClosed(2L, (int) Math.sqrt(num))
                .findFirst(d -> num % d == 0)
                .map(d -> Stream.cons(d, () -> primeFactors(num / d)))
                .orElseGet(() -> Stream.of(num));
    }
}
