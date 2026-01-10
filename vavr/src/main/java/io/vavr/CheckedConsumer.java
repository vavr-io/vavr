/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
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
import org.jspecify.annotations.NonNull;

import static io.vavr.CheckedConsumerModule.sneakyThrow;

/**
 * A {@linkplain java.util.function.Consumer} that is allowed to throw checked exceptions.
 *
 * @param <T> the type of the input to the consumer
 */
@FunctionalInterface
public interface CheckedConsumer<T> {

    /**
     * Creates a {@code CheckedConsumer} from the given method reference or lambda.
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * final CheckedConsumer<Value> checkedConsumer = CheckedConsumer.of(Value::stdout);
     * final Consumer<Value> consumer = checkedConsumer.unchecked();
     *
     * // prints "Hi" to the console
     * consumer.accept(CharSeq.of("Hi!"));
     *
     * // may throw an exception
     * consumer.accept(null);
     * }</pre>
     *
     * @param methodReference typically a method reference, e.g. {@code Type::method}
     * @param <T> the type of values accepted by the consumer
     * @return a new {@code CheckedConsumer} wrapping the given method reference
     * @see CheckedFunction1#of(CheckedFunction1)
     */
    static <T> CheckedConsumer<T> of(@NonNull CheckedConsumer<T> methodReference) {
        return methodReference;
    }

    /**
     * Performs an action on the given value, potentially causing side-effects.
     *
     * @param t the input value of type {@code T}
     * @throws Throwable if an error occurs during execution
     */
    void accept(T t) throws Throwable;

    /**
     * Returns a composed {@code CheckedConsumer} that performs, in sequence, 
     * {@code this.accept(t)} followed by {@code after.accept(t)} for the same input {@code t}.
     *
     * @param after the action to execute after this action
     * @return a new {@code CheckedConsumer} that chains {@code this} and {@code after}
     * @throws NullPointerException if {@code after} is null
     */
    default CheckedConsumer<T> andThen(@NonNull CheckedConsumer<? super T> after) {
        Objects.requireNonNull(after, "after is null");
        return (T t) -> { accept(t); after.accept(t); };
    }

    /**
     * Returns an unchecked {@link Consumer} that <em>sneakily throws</em> any exception 
     * encountered while accepting a value.
     *
     * @return a {@link Consumer} that may throw any {@link Throwable} without declaring it
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
