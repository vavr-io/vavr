/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
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

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a partial function T -&gt; R that is not necessarily defined for all input values of type T.
 * The caller is responsible for calling the method isDefinedAt() before this function is applied to the value.
 * <p>
 * If the function <em>is not defined</em> for a specific value, apply() may produce an arbitrary result.
 * More specifically it is not guaranteed that the function will throw an exception.
 * <p>
 * If the function <em>is defined</em> for a specific value, apply() may still throw an exception.
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
     * Applies this function to the given argument and returns the result.
     *
     * @param t the argument
     * @return the result of function application
     *
     */
    @Override
    R apply(T t);

    /**
     * Tests if a value is contained in the function's domain.
     *
     * @param value a potential function argument
     * @return true, if the given value is contained in the function's domain, false otherwise
     */
    boolean isDefinedAt(T value);

    /**
     * Applies this {@code PartialFunction} to the given {@code value} if it in its domain, i.e.
     * {@code this.isDefinedAt(value) == true}. Otherwise the given function {@code orElse} is
     * applied to the given {@code value}.
     * <p>
     * This is a shortcut for
     * <pre>{@code
     * this.isDefinedAt(value) ? this.apply(value) : orElse.apply(value)
     * }</pre>
     *
     * @param value a value
     * @param fallback a function that returns a default result for a given input value
     * @return the result of applying this partial function or the given {@code fallback} function
     */
    default R applyOrElse(T value, Function<? super T, ? extends R> fallback) {
        return isDefinedAt(value) ? apply(value) : fallback.apply(value);
    }

    /**
     * Composes this partial function with then given function {@code after} that gets applied to results of this
     * partial function.
     *
     * @param after the function applied after this
     * @param <V> the result type the given function {@code after}
     * @return a new {@code PartialFunction} {@code after.apply(this.apply(t))} which is defined for the same input as this.
     */
    @Override
    default <V> PartialFunction<T, V> andThen(Function<? super R, ? extends V> after) {
        final PartialFunction<T, R> self = this;
        return new PartialFunction<T, V>() {
            private static final long serialVersionUID = 1L;
            @Override
            public V apply(T t) {
                return after.apply(self.apply(t));
            }
            @Override
            public boolean isDefinedAt(T value) {
                return self.isDefinedAt(value);
            }
        };
    }

    /**
     * Same as {@link Function1#compose(Function)} because Java's type system does not allow to specialize this method.
     * <p>
     * <strong>Caution!</strong> The resulting function may be undefined for certain inputs. Applying it might lead to
     * arbitrary behavior, including throwing runtime exceptions.
     *
     * @param <V> argument type of before
     * @param before the function applied before this
     * @return a function composed of before and this
     * @throws NullPointerException if before is null
     */
    @Override
    default <V> Function1<V, R> compose(Function<? super V, ? extends T> before) {
        return Function1.super.compose(before);
    }

    /**
     * Turns this {@code PartialFunction} into a new {@code PartialFunction} using a partial {@code fallback} function.
     * 
     * @param fallback a fallback function
     * @return a new {@code PartialFunction} that is defined for a given {@code value} if {@code this.isDefined(value) || fallback.isDefined(value)}.
     */
    default PartialFunction<T, R> orElse(PartialFunction<? super T, ? extends R> fallback) {
        final PartialFunction<T, R> self = this;
        return new PartialFunction<T, R>() {
            private static final long serialVersionUID = 1L;
            @Override
            public R apply(T t) {
                return self.isDefinedAt(t) ? self.apply(t) : fallback.apply(t);
            }
            @Override
            public boolean isDefinedAt(T value) {
                return self.isDefinedAt(value) || fallback.isDefinedAt(value);
            }
        };
    }

    /**
     * Composes this {@code PartialFunction} with an {@code action} which receives results of this partial function.
     *
     * @param action a {@link Consumer} that receives values of this codomain
     * @return true, if the this is defined for a given value (and the action ran), false otherwise.
     */
    default Function<T, Boolean> runWith(Consumer<? super R> action) {
        return t -> {
            if (isDefinedAt(t)) {
                action.accept(apply(t));
                return true;
            } else {
                return false;
            }
        };
    }
}
