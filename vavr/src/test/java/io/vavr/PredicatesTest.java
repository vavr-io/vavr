/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
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
package io.vavr;

import io.vavr.collection.List;
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
    
    // -- allOf

    @Test
    public void shouldTestAllOf_PositiveCase() {
        assertThat(allOf().test(1)).isTrue();
        assertThat(allOf(IS_GT_ONE, IS_GT_TWO).test(3)).isTrue();
    }

    @Test
    public void shouldTestAllOf_NegativeCase() {
        assertThat(allOf(IS_GT_ONE, IS_GT_TWO).test(2)).isFalse();
    }

    // -- anyOf

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

    // -- exits

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

    // -- forAll

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

    // -- instanceOf

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

    // -- is

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

    // -- isIn

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

    // -- isNull

    @Test
    public void shouldTestIsNull_PositiveCase() {
        assertThat(isNull().test(null)).isTrue();
    }

    @Test
    public void shouldTestIsNull_NegativeCase() {
        assertThat(isNull().test("")).isFalse();
    }

    // -- isNotNull

    @Test
    public void shouldTestIsNotNull_PositiveCase() {
        assertThat(isNotNull().test("")).isTrue();
    }

    @Test
    public void shouldTestIsNotNull_NegativeCase() {
        assertThat(isNotNull().test(null)).isFalse();
    }

    // -- noneOf

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

    // -- not

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
