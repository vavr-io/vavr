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

import io.vavr.Tuple;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import static io.vavr.API.For;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 19:Counting Sundays</strong>
 *
 * <p>You are given the following information, but you may prefer to do some research for yourself.</p>
 * <ul>
 * <li>1 Jan 1900 was a Monday.</li>
 * <li>Thirty days has September,<br />
 * April, June and November.<br />
 * All the rest have thirty-one,<br />
 * Saving February alone,<br />
 * Which has twenty-eight, rain or shine.<br />
 * And on leap years, twenty-nine.</li>
 * <li>A leap year occurs on any year evenly divisible by 4, but not on a century unless it is divisible by 400.</li>
 * </ul>
 * <p>How many Sundays fell on the first of the month during the twentieth century (1 Jan 1901 to 31 Dec 2000)?</p>
 *
 * See also <a href="https://projecteuler.net/problem=19">projecteuler.net problem 19</a>.
 */
public class Euler19Test {

    @Test
    public void shouldSolveProblem19() {
        assertThat(findNumberOfFirstMonthDaysOnSunday(1901, 2000)).isEqualTo(171);
    }

    private static boolean isFirstDayOfMonthSunday(int year, Month month) {
        return LocalDate.of(year, month, 1).getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private static int findNumberOfFirstMonthDaysOnSunday(int startYear, int endYear) {
        return For(List.rangeClosed(startYear, endYear), List.of(Month.values()))
                .yield(Tuple::of)
                .filter(t -> isFirstDayOfMonthSunday(t._1, t._2))
                .length();
    }

}
