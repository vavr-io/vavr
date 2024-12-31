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
            Assertions.fail("Did not expect an exception but received: " + x.getMessage());
        }
    }

    @Test
    public void shouldApplyAnUncheckedFunctionThatThrows() {
        boolean thrown = false;
        final Runnable runnable = CheckedRunnable.of(() -> { throw new Error(); }).unchecked();
        try {
            runnable.run();
        } catch(Error x) {
            thrown = true;
        }
        assertThat(thrown).isTrue();
    }
}
