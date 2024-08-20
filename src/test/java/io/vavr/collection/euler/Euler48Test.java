/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2024 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.vavr.collection.euler;

import io.vavr.collection.Stream;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Euler48Test {

    private static final long MOD = 10_000_000_000L;

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
        final Stream<Long> powers = Stream.iterate(v, el -> multMod(el, el));
        return bits(v)
                .map(powers::get)
                .prepend(1L)
                .reduce(Euler48Test::multMod);
    }

    private static long multMod(long v1, long v2) {
        final Stream<Long> shifts = Stream.iterate(v1, el -> sumMod(el, el));
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
