/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.Tuple;
import javaslang.collection.List;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import static javaslang.API.For;
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
