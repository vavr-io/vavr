/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2024 Vavr, https://vavr.io
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

import static org.assertj.core.api.Assertions.assertThat;

public class CheckedRunnableTest {

    // -- of

    @Test
    public void shouldCreateCheckedRunnableUsingLambda() {
        final CheckedRunnable runnable = CheckedRunnable.of(() -> {});
        assertThat(runnable).isNotNull();
    }

    @Test
    public void shouldCreateCheckedRunnableUsingMethodReference() {
        final CheckedRunnable runnable = CheckedRunnable.of(CheckedRunnableTest::run);
        assertThat(runnable).isNotNull();
    }

    private static void run() {
    }

    // -- unchecked

    @Test
    public void shouldApplyAnUncheckedFunctionThatDoesNotThrow() {
        final Runnable runnable = CheckedRunnable.of(() -> {}).unchecked();
        try {
            runnable.run();
        } catch(Throwable x) {
            Assertions.fail("Did not excepect an exception but received: " + x.getMessage());
        }
    }

    @Test
    public void shouldApplyAnUncheckedFunctionThatThrows() {
        final Runnable runnable = CheckedRunnable.of(() -> { throw new Error(); }).unchecked();
        try {
            runnable.run();
            Assertions.fail("Did excepect an exception.");
        } catch(Error x) {
            // ok!
        }
    }
}
