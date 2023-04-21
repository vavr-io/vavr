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
