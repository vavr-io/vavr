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

import io.vavr.control.Option;
import java.util.function.Function;
import org.jspecify.annotations.NonNull;

/**
 * Represents a partial function {@code T -> R} that may not be defined for all input values of type {@code T}.
 * The caller is responsible for checking {@link #isDefinedAt(Object)} before applying this function.
 *
 * <p>If the function <em>is not defined</em> for a given value, {@link #apply(Object)} may produce an arbitrary result.
 * There is no guarantee that an exception will be thrown in this case.</p>
 *
 * <p>If the function <em>is defined</em> for a given value, {@link #apply(Object)} may still throw an exception during execution.</p>
 *
 * @param <T> the type of the function input (the <em>domain</em>)
 * @param <R> the type of the function output (the <em>codomain</em>)
 * @author Daniel Dietrich
 */
public interface PartialFunction<T, R> extends Function1<T, R> {

    /**
     * The serial version UID for serialization.
     */
    long serialVersionUID = 1L;

    /**
     * Converts (or "unlifts") a {@code totalFunction} that returns an {@code Option} into a partial function.
     * <p>
     * The provided {@code totalFunction} should be side-effect-free, because it may be invoked twice:
     * once when checking if the resulting partial function is defined at a value, and once when applying
     * the partial function to that value.
     *
     * @param totalFunction the function returning an {@code Option} result
     * @param <T> the type of the function input (the <em>domain</em>)
     * @param <R> the type of the function output (the <em>codomain</em>)
     * @return a partial function that is defined only for inputs for which the {@code totalFunction} returns a defined {@code Option}
     */
    static <T, R> PartialFunction<T, R> unlift(@NonNull Function<? super T, ? extends Option<? extends R>> totalFunction) {
        return new PartialFunction<T, R>() {

            private static final long serialVersionUID = 1L;

            @Override
            public R apply(T t) {
                return totalFunction.apply(t).get();
            }

            @Override
            public boolean isDefinedAt(T value) {
                return totalFunction.apply(value).isDefined();
            }

        };
    }

    /**
     * Creates a partial function that maps a given {@code Value} to its underlying value.
     * <p>
     * The resulting partial function is defined for an input {@code Value} if and only if the {@code Value} is not empty.
     * For defined inputs, the partial function returns the underlying value contained in the {@code Value}.
     *
     * @param <T> the type of the underlying value
     * @param <V> the type of the input {@code Value} (the <em>domain</em> of the function)
     * @return a partial function that maps a non-empty {@code Value} to its underlying value
     */
    static <T, V extends Value<T>> PartialFunction<V, T> getIfDefined() {
        return new PartialFunction<V, T>() {

            private static final long serialVersionUID = 1L;

            @Override
            public T apply(V v) {
                return v.get();
            }

            @Override
            public boolean isDefinedAt(V v) {
                return !v.isEmpty();
            }

        };
    }

    /**
     * Applies this function to the given argument and returns the result.
     *
     * @param t the input argument
     * @return the result of applying this function to the input
     */
    R apply(T t);

    /**
     * Tests whether a value is contained in the function's domain.
     *
     * @param value a potential input to the function
     * @return {@code true} if the given value is contained in the function's domain, {@code false} otherwise
     */
    boolean isDefinedAt(T value);

    /**
     * Lifts this partial function into a total function that returns an {@code Option} result.
     *
     * <p>The resulting function applies an argument to this partial function and returns
     * {@code Some(result)} if the function is defined for that argument, or {@code None} if it is not defined.</p>
     *
     * @return a total function that returns an {@code Option} containing the result if defined, or {@code None} otherwise
     */
    default Function1<T, Option<R>> lift() {
        return t -> Option.when(isDefinedAt(t), () -> apply(t));
    }

}
