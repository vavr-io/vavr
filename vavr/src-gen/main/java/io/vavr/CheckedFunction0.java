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

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a function with no arguments.
 *
 * @param <R> return type of the function
 * @author Daniel Dietrich
 */
@FunctionalInterface
public interface CheckedFunction0<R> extends Serializable, Callable<R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Creates a {@code CheckedFunction0} based on
     * <ul>
     * <li><a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method reference</a></li>
     * <li><a href="https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html#syntax">lambda expression</a></li>
     * </ul>
     *
     * Examples (w.l.o.g. referring to Function1):
     * <pre><code>// using a lambda expression
     * Function1&lt;Integer, Integer&gt; add1 = Function1.of(i -&gt; i + 1);
     *
     * // using a method reference (, e.g. Integer method(Integer i) { return i + 1; })
     * Function1&lt;Integer, Integer&gt; add2 = Function1.of(this::method);
     *
     * // using a lambda reference
     * Function1&lt;Integer, Integer&gt; add3 = Function1.of(add1::apply);
     * </code></pre>
     * <p>
     * <strong>Caution:</strong> Reflection loses type information of lambda references.
     * <pre><code>// type of a lambda expression
     * Type&lt;?, ?&gt; type1 = add1.getType(); // (Integer) -&gt; Integer
     *
     * // type of a method reference
     * Type&lt;?, ?&gt; type2 = add2.getType(); // (Integer) -&gt; Integer
     *
     * // type of a lambda reference
     * Type&lt;?, ?&gt; type3 = add3.getType(); // (Object) -&gt; Object
     * </code></pre>
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @param <R> return type
     * @return a {@code CheckedFunction0}
     */
    static <R> CheckedFunction0<R> of(CheckedFunction0<R> methodReference) {
        return methodReference;
    }

    /**
     * Narrows the given {@code CheckedFunction0<? extends R>} to {@code CheckedFunction0<R>}
     *
     * @param f A {@code CheckedFunction0}
     * @param <R> return type
     * @return the given {@code f} instance as narrowed type {@code CheckedFunction0<R>}
     */
    @SuppressWarnings("unchecked")
    static <R> CheckedFunction0<R> narrow(CheckedFunction0<? extends R> f) {
        return (CheckedFunction0<R>) f;
    }

    /**
     * Applies this function to no arguments and returns the result.
     *
     * @return the result of function application
     * @throws Exception if something goes wrong applying this function to the given arguments
     */
    R apply() throws Exception;

    /**
     * Implementation of {@linkplain java.util.concurrent.Callable#call()}, just calls {@linkplain #apply()}.
     *
     * @return the result of {@code apply()}
     * @throws Exception if something goes wrong when calling this function
     */
    @Override
    default R call() throws Exception {
        return apply();
    }

    /**
     * Returns a curried version of this function.
     *
     * @return a curried function equivalent to this.
     */
    default CheckedFunction0<R> curried() {
        return this;
    }

    /**
     * Returns a tupled version of this function.
     *
     * @return a tupled function equivalent to this.
     */
    default CheckedFunction1<Tuple0, R> tupled() {
        return t -> apply();
    }

    /**
     * Returns a reversed version of this function. This may be useful in a recursive context.
     *
     * @return a reversed function equivalent to this.
     */
    default CheckedFunction0<R> reversed() {
        return this;
    }

    /**
     * Checks if this function is memoizing (= caching) computed values.
     *
     * @return true, if this function is memoizing, false otherwise
     */
    default boolean isMemoized() {
        return this instanceof Memoized;
    }

    default CheckedFunction0<R> memoized() {
        if (isMemoized()) {
            return this;
        } else {
            final Lazy<R> lazy = Lazy.of(() -> {
              try {
                return apply();
              } catch (Exception x) {
                throw new RuntimeException(x);
              }
            });
            return (CheckedFunction0<R> & Memoized) () -> {
              try {
                return lazy.get();
              } catch(RuntimeException x) {
                throw (Exception) x.getCause();
              }
            };
        }
    }

    interface Memoized { /* zero abstract method (ZAM) interface */ }

    /**
     * Return a composed function that first applies this CheckedFunction0 to the given arguments and in case of throwable
     * try to get value from {@code recover} function with same arguments and throwable information.
     *
     * @param recover the function applied in case of throwable
     * @return a function composed of this and recover
     * @throws NullPointerException if recover is null
     */
    default Function0<R> recover(Function<? super Throwable, ? extends Supplier<? extends R>> recover) {
        Objects.requireNonNull(recover, "recover is null");
        return () -> {
            try {
                return this.apply();
            } catch (Throwable throwable) {
                final Supplier<? extends R> func = recover.apply(throwable);
                Objects.requireNonNull(func, () -> "recover return null for " + throwable.getClass() + ": " + throwable.getMessage());
                return func.get();
            }
        };
    }

    /**
     * Returns a composed function that first applies this CheckedFunction0 to the given argument and then applies
     * {@linkplain CheckedFunction1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> CheckedFunction0<V> andThen(CheckedFunction1<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return () -> after.apply(apply());
    }

}