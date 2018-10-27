/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2018 Vavr, http://vavr.io
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
package io.vavr.control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckedPredicateTest {

    // -- Testees

    private static final CheckedPredicate<Object> TRUE = o -> true;
    private static final CheckedPredicate<Object> FALSE = o -> false;

    // ---- error for testing the exceptional case

    private static final Error ERROR = new Error();

    // -- static .not(CheckedPredicate)

    @Test
    void shouldApplyStaticNotToGivenPredicate() throws Exception {
        assertFalse(CheckedPredicate.not(TRUE).test(null));
        assertTrue(CheckedPredicate.not(FALSE).test(null));
    }

    @Test
    void shouldRethrowWhenUsingStaticNot() {
        final CheckedPredicate<?> p = CheckedPredicate.not(ignored -> { throw ERROR; });
        assertThrows(ERROR.getClass(), () -> p.test(null));
    }

    @Test
    void shouldThrowNPEWhenPassingNullToStaticNot() {
        assertEquals(
                "that is null",
                assertThrows(NullPointerException.class, () -> CheckedPredicate.not(null)).getMessage()
        );
    }

    // -- .test(Object)

    @Test
    void shouldBeAbleToThrowCheckedException() {
        final CheckedPredicate<?> f = ignored -> { throw ERROR; };
        assertThrows(ERROR.getClass(), () -> f.test(null));
    }

    // -- .and(CheckedPredicate)

    @Test
    void shouldBehaveLikeLogicalAnd() throws Exception {
        assertTrue(TRUE.and(TRUE).test(null));
        assertFalse(TRUE.and(FALSE).test(null));
        assertFalse(FALSE.and(TRUE).test(null));
        assertFalse(FALSE.and(FALSE).test(null));
    }

    @Test
    void shouldRethrowWhenFirstPredicateFailsUsingAnd() {
        final CheckedPredicate<Object> p = ignored -> { throw ERROR; };
        assertThrows(ERROR.getClass(), () -> p.and(TRUE).test(null));
    }

    @Test
    void shouldRethrowWhenFirstPredicateReturnsTrueSecondPredicateFailsUsingAnd() {
        final CheckedPredicate<Object> p = ignored -> { throw ERROR; };
        assertThrows(ERROR.getClass(), () -> TRUE.and(p).test(null));
    }

    @Test
    void shouldNotRethrowWhenFirstPredicateReturnsFalseSecondPredicateFailsUsingAnd() throws Exception {
        final CheckedPredicate<Object> p = ignored -> { throw ERROR; };
        assertFalse(FALSE.and(p).test(null));
    }

    @Test
    void shouldThrowNPEWhenPassingNullToAnd() {
        assertEquals(
                "that is null",
                assertThrows(NullPointerException.class, () -> TRUE.and(null)).getMessage()
        );
    }

    // -- .negate()

    @Test
    void shouldBehaveLikeLogicalNegation() throws Exception {
        assertFalse(TRUE.negate().test(null));
        assertTrue(FALSE.negate().test(null));
    }

    @Test
    void shouldRethrowWhenNegatedPredicateFails() {
        final CheckedPredicate<String> p = ignored -> { throw ERROR; };
        assertThrows(ERROR.getClass(), () -> p.negate().test(null));
    }

    // -- .or(CheckedPredicate)

    @Test
    void shouldBehaveLikeLogicalOr() throws Exception {
        assertTrue(TRUE.or(TRUE).test(null));
        assertTrue(TRUE.or(FALSE).test(null));
        assertTrue(FALSE.or(TRUE).test(null));
        assertFalse(FALSE.or(FALSE).test(null));
    }

    @Test
    void shouldRethrowWhenFirstPredicateFailsUsingOr() {
        final CheckedPredicate<Object> p = ignored -> { throw ERROR; };
        assertThrows(ERROR.getClass(), () -> p.or(TRUE).test(null));
    }

    @Test
    void shouldRethrowWhenFirstPredicateReturnsFalseAndSecondPredicateFailsUsingOr() {
        final CheckedPredicate<Object> p = ignored -> { throw ERROR; };
        assertThrows(ERROR.getClass(), () -> FALSE.or(p).test(null));
    }

    @Test
    void shouldNotRethrowWhenFirstPredicateReturnsTrueAndSecondPredicateFailsUsingOr() throws Exception {
        final CheckedPredicate<Object> p = ignored -> { throw ERROR; };
        assertTrue(TRUE.or(p).test(null));
    }

    @Test
    void shouldThrowNPEWhenPassingNullToOr() {
        assertEquals(
                "that is null",
                assertThrows(NullPointerException.class, () -> TRUE.or(null)).getMessage()
        );
    }

}
