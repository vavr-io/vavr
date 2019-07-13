/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2019 Vavr, http://vavr.io
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
