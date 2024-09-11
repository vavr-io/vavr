/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2024 Vavr, http://vavr.io
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckedPredicateTest {

    // -- of

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

    private static boolean test(Object obj) {
        return true;
    }

    // -- unchecked

    @Test
    public void shouldApplyAnUncheckedFunctionThatDoesNotThrow() {
        final Predicate<Object> preciate = CheckedPredicate.of(obj -> true).unchecked();
        try {
            preciate.test(null);
        } catch(Throwable x) {
            Assertions.fail("Did not excepect an exception but received: " + x.getMessage());
        }
    }

    @Test
    public void shouldApplyAnUncheckedFunctionThatThrows() {
        final Predicate<Object> preciate = CheckedPredicate.of(obj -> { throw new Error(); }).unchecked();
        try {
            preciate.test(null);
            Assertions.fail("Did excepect an exception.");
        } catch(Error x) {
            // ok!
        }
    }
}
