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
package io.vavr;

import io.vavr.collection.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static io.vavr.API.$;
import static io.vavr.API.*;
import static io.vavr.Predicates.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PredicatesTest {

    private static final Predicate<? super Throwable> IS_RUNTIME_EXCEPTION = instanceOf(RuntimeException.class);
    private static final Predicate<Integer> IS_GT_ONE = i -> i > 1;
    private static final Predicate<Integer> IS_GT_TWO = i -> i > 2;

    @Nested
    @DisplayName("allOf")
    class AllOfTest {

        @Test
        public void shouldTestAllOf_PositiveCase() {
            assertThat(allOf().test(1)).isTrue();
            assertThat(allOf(IS_GT_ONE, IS_GT_TWO).test(3)).isTrue();
        }

        @Test
        public void shouldTestAllOf_NegativeCase() {
            assertThat(allOf(IS_GT_ONE, IS_GT_TWO).test(2)).isFalse();
        }

    }

    @Nested
    @DisplayName("anyOf")
    class AnyOfTest {

        @Test
        public void shouldTestAnyOf_PositiveCase() {
            assertThat(anyOf(IS_GT_ONE, IS_GT_TWO).test(3)).isTrue();
            assertThat(anyOf(IS_GT_ONE, IS_GT_TWO).test(2)).isTrue();
        }

        @Test
        public void shouldTestAnyOf_NegativeCase() {
            assertThat(anyOf().test(1)).isFalse();
            assertThat(anyOf(IS_GT_ONE, IS_GT_TWO).test(1)).isFalse();
        }

    }

    @Nested
    @DisplayName("exits")
    class ExitsTest {

        @Test
        public void shouldTestExists_PositiveCase() {
            assertThat(exists(IS_GT_ONE).test(List.of(1, 3))).isTrue();
        }

        @Test
        public void shouldTestExists_NegativeCase() {
            assertThat(exists(IS_GT_ONE).test(List.of(1, 0))).isFalse();
        }

        @Test
        public void shouldCheckExistsByLiftingPredicateInContravariantPositionToPredicateInCovariantPosition() {
            final List<Integer> list = List(1, 2, 3);
            final Predicate<Number> p = n -> n.intValue() % 2 == 0;
            final boolean actual = Match(list).of(
                    Case($(exists(p)), true),
                    Case($(), false)
            );
            assertThat(actual).isTrue();
        }

    }

    @Nested
    @DisplayName("forAll")
    class ForAllTest {

        @Test
        public void shouldTestForAll_PositiveCase() {
            assertThat(forAll(IS_GT_ONE).test(List.of(2, 3))).isTrue();
        }

        @Test
        public void shouldTestForAll_NegativeCase() {
            assertThat(forAll(IS_GT_ONE).test(List.of(3, 0))).isFalse();
        }

        @Test
        public void shouldCheckForAllByLiftingPredicateInContravariantPositionToPredicateInCovariantPosition() {
            final List<Integer> list = List(1, 2, 3);
            final Predicate<Number> p = n -> n.intValue() > 0;
            final boolean actual = Match(list).of(
                    Case($(forAll(p)), true),
                    Case($(), false)
            );
            assertThat(actual).isTrue();
        }

    }

    @Nested
    @DisplayName("instanceOf")
    class InstanceOfTest {

        @Test
        public void shouldTestInstanceOf_PositiveCase() {
            assertThat(instanceOf(Number.class).test(1)).isTrue();
            assertThat(instanceOf(Number.class).test(new BigDecimal("1"))).isTrue();
            assertThat(IS_RUNTIME_EXCEPTION.test(new NullPointerException())).isTrue();
        }

        @Test
        public void shouldTestInstanceOf_NegativeCase() {
            assertThat(IS_RUNTIME_EXCEPTION.test(new Exception())).isFalse();
            assertThat(IS_RUNTIME_EXCEPTION.test(new Error("error"))).isFalse();
            assertThat(IS_RUNTIME_EXCEPTION.test(null)).isFalse();
        }

    }

    @Nested
    @DisplayName("is")
    class IsTest {

        @Test
        public void shouldTestIs_PositiveCase() {
            assertThat(is(1).test(1)).isTrue();
            assertThat(is((CharSequence) "1").test("1")).isTrue();
        }

        @Test
        public void shouldTestIs_NegativeCase() {
            assertThat(is(1).test(2)).isFalse();
            assertThat(is((CharSequence) "1").test(new StringBuilder("1"))).isFalse();
        }

    }

    @Nested
    @DisplayName("isIn")
    class IsInTest {

        @Test
        public void shouldTestIsIn_PositiveCase() {
            assertThat(isIn(1, 2, 3).test(2)).isTrue();
            assertThat(isIn((CharSequence) "1", "2", "3").test("2")).isTrue();
        }

        @Test
        public void shouldTestIsIn_NegativeCase() {
            assertThat(isIn(1, 2, 3).test(4)).isFalse();
            assertThat(isIn((CharSequence) "1", "2", "3").test("4")).isFalse();
        }

    }

    @Nested
    @DisplayName("isNull")
    class IsNullTest {

        @Test
        public void shouldTestIsNull_PositiveCase() {
            assertThat(isNull().test(null)).isTrue();
        }

        @Test
        public void shouldTestIsNull_NegativeCase() {
            assertThat(isNull().test("")).isFalse();
        }

    }

    @Nested
    @DisplayName("isNotNull")
    class IsNotNullTest {

        @Test
        public void shouldTestIsNotNull_PositiveCase() {
            assertThat(isNotNull().test("")).isTrue();
        }

        @Test
        public void shouldTestIsNotNull_NegativeCase() {
            assertThat(isNotNull().test(null)).isFalse();
        }

    }

    @Nested
    @DisplayName("noneOf")
    class NoneOfTest {

        @Test
        public void shouldTestNoneOf_PositiveCase() {
            assertThat(noneOf().test(1)).isTrue();
            assertThat(noneOf(IS_GT_ONE, IS_GT_TWO).test(1)).isTrue();
        }

        @Test
        public void shouldTestNoneOf_NegativeCase() {
            assertThat(noneOf(IS_GT_ONE).test(2)).isFalse();
            assertThat(noneOf(IS_GT_ONE, IS_GT_TWO).test(2)).isFalse();
        }

    }

    @Nested
    @DisplayName("not")
    class NotTest {

        @Test
        public void shouldThrowWhenNegatingNull() {
            assertThatThrownBy(() -> not(null)).isInstanceOf(NullPointerException.class);
        }

        @Test
        public void shouldNegate_PositiveCase() {
            assertThat(not(IS_GT_ONE).test(0)).isTrue();
        }

        @Test
        public void shouldNegate_NegativeCase() {
            assertThat(not(IS_GT_ONE).test(2)).isFalse();
        }

    }
}
