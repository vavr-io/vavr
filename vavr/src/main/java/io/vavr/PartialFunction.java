/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2017 Vavr, http://vavr.io
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

import io.vavr.control.Option;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a partial function T -&gt; R that is not necessarily defined for all input values of type T.
 * The caller is responsible for calling the method {@link #isDefinedAt(Object)} before this function is
 * applied to the value.
 * <p>
 * If the function <em>is not defined</em> for a specific value, apply() may produce an arbitrary result.
 * More specifically it is not guaranteed that the function will throw an exception.
 * <p>
 * If the function <em>is defined</em> for a specific value, {@code apply()} may still throw an exception.
 * For example
 *
 * <pre>{@code
 * PartialFunction<Integer, Integer> pf = Function1.of((Integer i) -> i / 0).partial(i -> true);
 *
 * // = true
 * pf.isDefinedAt(1);
 *
 * // = throws ArithmeticException: / by zero
 * pf.apply(1);
 * }</pre>
 *
 * @param <T> type of the function input, called <em>domain</em> of the function
 * @param <R> type of the function output, called <em>codomain</em> of the function
 * @author Daniel Dietrich
 */
public interface PartialFunction<T, R> extends Function1<T, R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Creates a new {@code PartialFunction} using the given function {@code apply} and the given
     * predicate {@code isDefinedAt}.
     *
     * @param apply a {@link Function} that is used by the returned {@code PartialFunction} to apply a given value.
     * @param isDefinedAt a {@link Predicate} that is used by the returned {@code PartialFunction} to check if a given value is defined.
     * @param <T> domain of the result
     * @param <R> codomain of the result
     * @return a new {@code PartialFunction} based on the given arguments {@code apply} and {@code isDefined}
     */
    static <T, R> PartialFunction<T, R> of(Function<T, R> apply, Predicate<? super T> isDefinedAt) {
        return new PartialFunction<T, R>() {
            private static final long serialVersionUID = 1L;
            @Override
            public R apply(T t) {
                return apply.apply(t);
            }
            @Override
            public boolean isDefinedAt(T t) {
                return isDefinedAt.test(t);
            }
        };
    }
    
    /**
     * Applies this partial function to the given argument and returns the result.
     * 
     * @param t the function argument
     * @return the result of the partial function application
     */
    @Override
    R apply(T t);

    /**
     * Tests if a value is contained in the function's domain.
     *
     * @param t a potential function argument
     * @return true, if the given value {@code t} is contained in the function's domain, false otherwise
     */
    boolean isDefinedAt(T t);

    /**
     * Chains this {@code PartialFunction} and the given {@code after} function in the way
     * that {@code after} will be applied to the result of this {@code PartialFunction}.
     *
     * @param after the function applied after this
     * @param <V> the result of the chained function call
     * @return a new {@code PartialFunction}
     */
    @Override
    default <V> PartialFunction<T, V> andThen(Function<? super R, ? extends V> after) {
        return of(t -> after.apply(this.apply(t)), this::isDefinedAt);
    }

    /**
     * Applies this {@code PartialFunction} to the given value {@code t} if this is defined at {@code t},
     * otherwise applies {@code fallback} to {@code t} and returns the result.
     *
     * @param t a value of type {@code T}
     * @param fallback a fallback function that is applied to {@code t} if this is not defined at {@code t}
     * @return either the result of {@code this.apply(t)} or the result of {@code fallback.apply(t)}
     */
    default R applyOrElse(T t, Function<? super T, ? extends R> fallback) {
        return isDefinedAt(t) ? apply(t) : fallback.apply(t);
    }

    /**
     * Lifts this partial function into a total function that returns an {@code Option} result.
     *
     * @return a function that applies arguments to this function and returns {@code Some(result)}
     *         if the function is defined for the given arguments, and {@code None} otherwise.
     */
    default Function1<T, Option<R>> lift() {
        return t -> Option.when(isDefinedAt(t), apply(t));
    }

    /* TODO: currently the name is ambiguous. uncomment after #2023 is finished!
    /**
     * Combines this {@code PartialFunction} with the given {@code fallback} function in the
     * way that the fallback is applied if this function is not defined at a specific value.
     * <p>
     * The resulting {@code PartialFunction} is defined for an input if either this function
     * is defined or the fallback is defined for the value.
     *
     * @param fallback A fallback function
     * @return A new {@code PartialFunction}
     *
    default PartialFunction<T, R> orElse(PartialFunction<? super T, ? extends R> fallback) {
        return of(t -> applyOrElse(t, fallback), t -> isDefinedAt(t) || fallback.isDefinedAt(t));
    }
    */

    /**
     * Chains this partial function with the given {@code action} that accepts the result of this partial function
     * and performs a side-effect.
     * 
     * @param action a side-effect that is performed for the result of this partial function application
     * @return a {@link Predicate} that tests if the {@code action} was performed for a given input
     */
    default Predicate<T> runWith(Consumer<? super R> action) {
        return t -> {
            if (isDefinedAt(t)) {
                action.accept(this.apply(t));
                return true;
            } else {
                return false;
            }
        };
    }
}
