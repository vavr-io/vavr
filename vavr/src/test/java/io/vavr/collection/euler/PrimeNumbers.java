/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package io.vavr.collection.euler;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import io.vavr.collection.TreeSet;

final class PrimeNumbers {

    private static final Set<Integer> PRIMES_2_000_000 = Sieve.fillSieve(2_000_000, TreeSet.empty());

    private PrimeNumbers() {
    }

    static Stream<Integer> primes() {
        return Stream.ofAll(PRIMES_2_000_000);
    }

    static HashMap<Long, Long> factorization(long num) {
        if (num == 1) {
            return HashMap.empty();
        } else {
            return primeFactors(num)
                    .map(p -> HashMap.of(Tuple.of(p, 1L))
                            .merge(factorization(num / p), (a, b) -> a + b))
                    .getOrElse(HashMap::empty);
        }
    }

    static Stream<Long> primeFactors(long num) {
        return Stream.rangeClosed(2L, (int) Math.sqrt(num))
                .find(d -> num % d == 0)
                .map(d -> Stream.cons(d, () -> primeFactors(num / d)))
                .getOrElse(() -> Stream.of(num));
    }
}
