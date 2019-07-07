/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
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

import java.util.Objects;
import java.util.function.Consumer;

import static io.vavr.CheckedConsumerModule.sneakyThrow;

/**
 * A consumer that may throw, equivalent to {@linkplain java.util.function.Consumer}.
 *
 * @param <T> the value type supplied to this consumer.
 */
@FunctionalInterface
public interface CheckedConsumer<T> {

    /**
     * Creates a {@code CheckedConsumer}.
     *
     * <pre>{@code
     * final CheckedConsumer<Value> checkedConsumer = CheckedConsumer.of(Value::stdout);
     * final Consumer<Value> consumer = checkedConsumer.unchecked();
     *
     * // prints "Hi" on the console
     * consumer.accept(CharSeq.of("Hi!"));
     *
     * // throws
     * consumer.accept(null);
     * }</pre>
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @param <T> type of values that are accepted by the consumer
     * @return a new {@code CheckedConsumer}
     * @see CheckedFunction1#of(CheckedFunction1)
     */
    static <T> CheckedConsumer<T> of(CheckedConsumer<T> methodReference) {
        return methodReference;
    }

    /**
     * Performs side-effects.
     *
     * @param t a value of type {@code T}
     * @throws Throwable if an error occurs
     */
    void accept(T t) throws Throwable;

    /**
     * Returns a chained {@code CheckedConsumer} that first executes {@code this.accept(t)}
     * and then {@code after.accept(t)}, for a given {@code t} of type {@code T}.
     *
     * @param after the action that will be executed after this action
     * @return a new {@code CheckedConsumer} that chains {@code this} and {@code after}
     * @throws NullPointerException if {@code after} is null
     */
    default CheckedConsumer<T> andThen(CheckedConsumer<? super T> after) {
        Objects.requireNonNull(after, "after is null");
        return (T t) -> { accept(t); after.accept(t); };
    }

    /**
     * Returns an unchecked {@link Consumer} that will <em>sneaky throw</em> if an exceptions occurs when accepting a value.
     *
     * @return a new {@link Consumer} that throws a {@code Throwable}.
     */
    default Consumer<T> unchecked() {
        return t -> {
            try {
                accept(t);
            } catch(Throwable x) {
                sneakyThrow(x);
            }
        };
    }
}

interface CheckedConsumerModule {

    // DEV-NOTE: we do not plan to expose this as public API
    @SuppressWarnings("unchecked")
    static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }

}
