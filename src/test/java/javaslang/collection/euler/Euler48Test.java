/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.collection.Stream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler48Test {

    private static long MOD = 10_000_000_000L;

    /**
     * <strong>Problem 48: Self powers</strong>
     * <p>
     * The series, 1<sup>1</sup> + 2<sup>2</sup> + 3<sup>3</sup> + ... + 10<sup>10</sup> = 10405071317.
     * <p>
     * Find the last ten digits of the series, 1<sup>1</sup> + 2<sup>2</sup> + 3<sup>3</sup> + ... + 1000<sup>1000</sup>.
     * <p>
     * See also <a href="https://projecteuler.net/problem=48">projecteuler.net problem 48</a>.
     */
    @Test
    public void shouldSolveProblem48() {
        assertThat(sumPowers(10)).isEqualTo(405_071_317L);
        assertThat(sumPowers(1000)).isEqualTo(9_110_846_700L);
    }

    private static long sumPowers(int max) {
        return Stream.range(1, max)
                .map(Euler48Test::selfPower)
                .reduce(Euler48Test::sumMod);
    }

    private static long selfPower(long v) {
        Stream<Long> powers = Stream.gen(v, el -> multMod(el, el));
        return bits(v)
                .map(powers::get)
                .prepend(1L)
                .reduce(Euler48Test::multMod);
    }

    private static long multMod(long v1, long v2) {
        Stream<Long> shifts = Stream.gen(v1, el -> sumMod(el, el));
        return bits(v2)
                .map(shifts::get)
                .prepend(0L)
                .reduce(Euler48Test::sumMod);
    }

    private static long sumMod(long v1, long v2) {
        return (v1 + v2) % MOD;
    }

    private static Stream<Integer> bits(long v) {
        return Stream.from(0)
                .takeWhile(b -> (v >> b) > 0)
                .filter(b -> ((v >> b) & 1) != 0);
    }

}
