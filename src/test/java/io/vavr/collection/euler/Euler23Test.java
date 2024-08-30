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

import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 23: Non-abundant sums</strong>
 * <p>
 * A perfect number is a number for which the sum of its proper divisors is
 * exactly equal to the number. For example, the sum of the proper divisors of
 * 28 would be 1 + 2 + 4 + 7 + 14 = 28, which means that 28 is a perfect number.
 * <p>
 * A number n is called deficient if the sum of its proper divisors is less than
 * n and it is called abundant if this sum exceeds n.
 * <p>
 * As 12 is the smallest abundant number, 1 + 2 + 3 + 4 + 6 = 16, the smallest
 * number that can be written as the sum of two abundant numbers is 24. By
 * mathematical analysis, it can be shown that all integers greater than 28123
 * can be written as the sum of two abundant numbers. However, this upper limit
 * cannot be reduced any further by analysis even though it is known that the
 * greatest number that cannot be expressed as the sum of two abundant numbers
 * is less than this limit.
 * <p>
 * Find the sum of all the positive integers which cannot be written as the sum
 * of two abundant numbers.
 * <p>
 * See also <a href="https://projecteuler.net/problem=23">projecteuler.net
 * problem 23</a>.
 */
public class Euler23Test {

    private static final long SMALLEST_ABUNDANT_NUMBER = 12;
    private static final long SMALLEST_NUMBER_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS = 2 * SMALLEST_ABUNDANT_NUMBER;
    private static final long LOWER_LIMIT_FOUND_BY_MATHEMATICAL_ANALYSIS_FOR_NUMBERS_THAT_CAN_BE_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS = 28123;

    @Test
    public void shouldSolveProblem23() {
        List.range(1, SMALLEST_ABUNDANT_NUMBER).forEach(n -> Assertions.assertThat(isAbundant.apply(n)).isFalse());
        Assertions.assertThat(isAbundant.apply(SMALLEST_ABUNDANT_NUMBER)).isTrue();
        Assertions.assertThat(isAbundant.apply(28L)).isFalse();
        assertThat(canBeWrittenAsTheSumOfTwoAbundantNumbers(SMALLEST_NUMBER_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS - 1)).isFalse();
        assertThat(canBeWrittenAsTheSumOfTwoAbundantNumbers(SMALLEST_NUMBER_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS)).isTrue();
        assertThat(canBeWrittenAsTheSumOfTwoAbundantNumbers(LOWER_LIMIT_FOUND_BY_MATHEMATICAL_ANALYSIS_FOR_NUMBERS_THAT_CAN_BE_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS + 1)).isTrue();
        assertThat(sumOfAllPositiveIntegersThatCannotBeWrittenAsTheSumOfTwoAbundantNumbers()).isEqualTo(4179871L);
    }

    private static long sumOfAllPositiveIntegersThatCannotBeWrittenAsTheSumOfTwoAbundantNumbers() {
        return Stream.rangeClosed(1, LOWER_LIMIT_FOUND_BY_MATHEMATICAL_ANALYSIS_FOR_NUMBERS_THAT_CAN_BE_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS)
                .filter(l -> !canBeWrittenAsTheSumOfTwoAbundantNumbers(l))
                .sum().longValue();
    }

    private static boolean canBeWrittenAsTheSumOfTwoAbundantNumbers(long l) {
        return Match(l).of(
                Case($(n -> n < SMALLEST_NUMBER_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS), false),
                Case($(SMALLEST_NUMBER_WRITTEN_AS_THE_SUM_OF_TO_ABUNDANT_NUMBERS), true),
                Case($(), () -> Stream.rangeClosed(SMALLEST_ABUNDANT_NUMBER, l / 2).exists(a -> isAbundant.apply(a) && isAbundant.apply(l - a)))
        );
    }

    private static final Function1<Long, Boolean> isAbundant = Function1.of((Long l) -> Utils.divisors(l).sum().longValue() > l).memoized();
}
