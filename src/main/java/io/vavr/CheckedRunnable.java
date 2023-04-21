/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
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

import static io.vavr.CheckedRunnableModule.sneakyThrow;

/**
 * A {@linkplain Runnable} which may throw.
 */
@FunctionalInterface
public interface CheckedRunnable {

    /**
     * Creates a {@code CheckedRunnable}.
     *
     * <pre>{@code
     * // class Evil { static void sideEffect() { ... } }
     * final CheckedRunnable checkedRunnable = CheckedRunnable.of(Evil::sideEffect);
     * final Runnable runnable = checkedRunnable.unchecked();
     *
     * // may or may not perform a side-effect while not throwing
     * runnable.run();
     *
     * // may or may not perform a side-effect while throwing
     * runnable.run();
     * }</pre>
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @return a new {@code CheckedRunnable}
     * @see CheckedFunction1#of(CheckedFunction1)
     */
    static CheckedRunnable of(CheckedRunnable methodReference) {
        return methodReference;
    }

    /**
     * Performs side-effects.
     *
     * @throws Throwable if an error occurs
     */
    void run() throws Throwable;

    /**
     * Returns an unchecked {@link Runnable} that will <em>sneaky throw</em> if an exceptions occurs when running the unit of work.
     *
     * @return a new {@link Runnable} that throws a {@code Throwable}.
     */
    default Runnable unchecked() {
        return () -> {
            try {
                run();
            } catch(Throwable x) {
                sneakyThrow(x);
            }
        };
    }
}

interface CheckedRunnableModule {

    // DEV-NOTE: we do not plan to expose this as public API
    @SuppressWarnings("unchecked")
    static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }

}
