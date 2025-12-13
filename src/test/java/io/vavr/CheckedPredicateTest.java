/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2025 Vavr, https://vavr.io
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckedPredicateTest {

    @Nested
    @DisplayName("of")
    class OfTest {

        @Test
        public void shouldCreateCheckedPredicateUsingLambda() {
            final CheckedPredicate<Object> predicate = CheckedPredicate.of(obj -> true);
            assertThat(predicate).isNotNull();
        }

        @Test
        public void shouldCreateCheckedPredicateUsingMethodReference() {
            final CheckedPredicate<Object> predicate = CheckedPredicate.of(CheckedPredicateTest::test);
            assertThat(predicate).isNotNull();
        }
    }

    private static boolean test(Object obj) {
        return true;
    }


    @Nested
    @DisplayName("unchecked")
    class UncheckedTest {

        @Test
        public void shouldApplyAnUncheckedFunctionThatDoesNotThrow() {
            final Predicate<Object> predicate = CheckedPredicate.of(obj -> true).unchecked();
            try {
                predicate.test(null);
            } catch (Throwable x) {
                Assertions.fail("Did not expect an exception but received: " + x.getMessage());
            }
        }

        @Test
        public void shouldApplyAnUncheckedFunctionThatThrows() {
            final Predicate<Object> predicate = CheckedPredicate.of(obj -> {
                throw new Error();
            }).unchecked();
            try {
                predicate.test(null);
                Assertions.fail("Did expect an exception.");
            } catch (Error x) {
                // ok!
            }
        }
    }
}
