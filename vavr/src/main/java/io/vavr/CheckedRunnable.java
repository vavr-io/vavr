/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
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
