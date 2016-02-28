/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.Tuple;
import javaslang.collection.HashMap;
import javaslang.collection.Set;
import javaslang.collection.Stream;
import javaslang.collection.TreeSet;

public final class PrimeNumbers {

    private static Set<Integer> PRIMES_2_000_000 = Sieve.fillSieve.apply(2_000_000, TreeSet.empty());

    private PrimeNumbers() {
    }

    public static Stream<Integer> primes() {
        return Stream.ofAll(PRIMES_2_000_000);
    }

    public static HashMap<Long, Long> factorization(long num) {
        if (num == 1) {
            return HashMap.empty();
        } else {
            return primeFactors(num)
                    .map(p -> HashMap.of(Tuple.of(p, 1L))
                            .merge(factorization(num / p), (a, b) -> a + b))
                    .getOrElse(HashMap::empty);
        }
    }

    public static Stream<Long> primeFactors(long num) {
        return Stream.rangeClosed(2L, (int) Math.sqrt(num))
                .find(d -> num % d == 0)
                .map(d -> Stream.cons(d, () -> primeFactors(num / d)))
                .getOrElse(() -> Stream.of(num));
    }
}
