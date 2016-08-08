/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import static javaslang.API.For;
import javaslang.Function1;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.CharSeq;
import javaslang.collection.List;
import javaslang.collection.Stream;
import org.junit.Test;

import javaslang.control.Option;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 27: Quadratic primes</strong>
 * <p>
 * Euler discovered the remarkable quadratic formula:
 * <p>
 * n² + n + 41
 * <p>
 * It turns out that the formula will produce 40 primes for the consecutive
 * values n = 0 to 39. However, when n = 40, 40^2 + 40 + 41 = 40(40 + 1) + 41 is
 * divisible by 41, and certainly when n = 41, 41² + 41 + 41 is clearly
 * divisible by 41.
 * <p>
 * The incredible formula n² − 79n + 1601 was discovered, which produces 80
 * primes for the consecutive values n = 0 to 79. The product of the
 * coefficients, −79 and 1601, is −126479.
 * <p>
 * Considering quadratics of the form:
 * <p>
 * n² + an + b, where |a| < 1000 and |b| < 1000 <p>
 * where |n| is the modulus/absolute value of n e.g. |11| = 11 and |−4| = 4
 * <p>
 * Find the product of the coefficients, a and b, for the quadratic expression
 * that produces the maximum number of primes for consecutive values of n,
 * starting with n = 0.
 * <p>
 * See also
 * <a href="https://projecteuler.net/problem=27">projecteuler.net problem 27
 * </a>.
 */
public class Euler27Test {

    @Test
    public void shouldSolveProblem27() {
        assertThat(numberOfConsecutivePrimesProducedByFormulaWithCoefficients(1, 41)).isEqualTo(40);
        assertThat(numberOfConsecutivePrimesProducedByFormulaWithCoefficients(-79, 1601)).isEqualTo(80);

        assertThat(productOfCoefficientsWithMostConsecutivePrimes(-999, 999)).isEqualTo(-59231);
    }

    private static int productOfCoefficientsWithMostConsecutivePrimes(int coefficientsLowerBound, int coefficientsUpperBound) {
        final List<Integer> coefficients = List.rangeClosed(coefficientsLowerBound, coefficientsUpperBound);
        return For(coefficients, coefficients).yield(Tuple::of)
                .map(c -> Tuple.of(c._1, c._2, numberOfConsecutivePrimesProducedByFormulaWithCoefficients(c._1, c._2)))
                .fold(Tuple.of(0, 0, -1), (n, m) -> n._3 >= m._3 ? n : m)
                .apply((a, b, p) -> a * b);
    }

    private static int numberOfConsecutivePrimesProducedByFormulaWithCoefficients(int a, int b) {
        return Stream.from(0L)
                .map(n -> (long) Math.pow(n, 2) + a * n + b)
                .takeWhile(Utils.memoizedIsPrime::apply)
                .length();
    }
}
