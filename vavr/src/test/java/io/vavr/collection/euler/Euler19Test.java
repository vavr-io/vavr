/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection.euler;

import io.vavr.Tuple;
import io.vavr.collection.List;
import org.junit.Test;

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
