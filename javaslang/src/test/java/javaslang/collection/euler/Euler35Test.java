/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;


import javaslang.Function1;
import javaslang.collection.CharSeq;
import javaslang.collection.List;
import javaslang.collection.Stream;
import org.junit.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 35: Circular primes</strong>
 * <p>The number, 197, is called a circular prime because all rotations of the digits: 197, 971, and 719, are themselves prime.</p>
 * <p>There are thirteen such primes below 100: 2, 3, 5, 7, 11, 13, 17, 31, 37, 71, 73, 79, and 97.</p>
 * <p>How many circular primes are there below one million?</p>
 * See also <a href="https://projecteuler.net/problem=35">projecteuler.net problem 35</a>.
 */
public class Euler35Test {

    @Test
    public void shouldSolveProblem35() {
        assertThat(rotations(197)).isEqualTo(List.of(197, 971, 719));
        assertThat(circularPrimes(100)).isEqualTo(13);
        assertThat(circularPrimes(1000000)).isEqualTo(55);
    }

    private static int circularPrimes(int n) {
        Predicate<Integer> memoizedIsPrime = Function1.of(Euler35Test::isPrime).memoized()::apply;
        return Stream.rangeClosed(2, n)
                .filter(memoizedIsPrime)
                .map(Euler35Test::rotations)
                .filter(list -> list.forAll(memoizedIsPrime))
                .length();
    }

    private static boolean isPrime(int n) {
        return n == 2 || n % 2 != 0 &&
                Stream.rangeClosedBy(3, (int) Math.sqrt(n), 2)
                        .find(x -> n % x == 0)
                        .isEmpty();
    }

    private static List<Integer> rotations(int n) {
        final CharSeq seq = CharSeq.of(String.valueOf(n));
        return Stream.range(0, seq.length())
                .map(i -> seq.drop(i).appendAll(seq.take(i)))
                .map(s -> Integer.valueOf(s.toString()))
                .toList();
    }

}
