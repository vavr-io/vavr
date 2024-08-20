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
package io.vavr.concurrent;

import io.vavr.control.Try;

/**
 * Represents a possibly asynchronous unit of work, called "Task".
 * <p>
 * A {@code Task} is a function that takes an instance of {@link Complete} and returns nothing.
 * <p>
 * {@code Complete} is a handler that needs to be actively called to complete the {@code Task}.
 *
 * <pre>{@code
 * Callable<T> worker = ...;
 * Future<T> result = Future.run(complete -> complete.with(Try.of(worker::call)));
 * }</pre>
 *
 * @param <T> result type
 * @deprecated Experimental API
 */
@Deprecated
@FunctionalInterface
public interface Task<T> {

    /**
     * Runs the task. Non-fatal errors are catched by a {@link Future}.
     *
     * @param complete a function that completes this task
     * @throws Throwable if an error occurs
     */
    void run(Complete<T> complete) throws Throwable;

    /**
     * Completes a task.
     * <p>
     * @param <T> result type
     */
    @FunctionalInterface
    interface Complete<T> {

        /**
         * A function that takes a {@link Try} (success or failure) and returns the state of completion.
         *
         * @param value the computation result
         * @return {@code true}, if the task could be completed, otherwise {@code false}.
         *         Successive calls will result in {@code false}.
         */
        boolean with(Try<? extends T> value);
    }
}
