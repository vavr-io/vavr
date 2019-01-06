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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a function with one argument.
 *
 * @param <T1> argument 1 of the function
 * @param <R> return type of the function
 * @author Daniel Dietrich
 */
@FunctionalInterface
public interface Function1<T1, R> extends Serializable, Function<T1, R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Creates a {@code Function1} based on
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
     * @param <T1> 1st argument
     * @return a {@code Function1}
     */
    static <T1, R> Function1<T1, R> of(Function1<T1, R> methodReference) {
        return methodReference;
    }

    /**
     * Narrows the given {@code Function1<? super T1, ? extends R>} to {@code Function1<T1, R>}
     *
     * @param f A {@code Function1}
     * @param <R> return type
     * @param <T1> 1st argument
     * @return the given {@code f} instance as narrowed type {@code Function1<T1, R>}
     */
    @SuppressWarnings("unchecked")
    static <T1, R> Function1<T1, R> narrow(Function1<? super T1, ? extends R> f) {
        return (Function1<T1, R>) f;
    }

    /**
     * Returns the identity Function1, i.e. the function that returns its input.
     *
     * @param <T> argument type (and return type) of the identity function
     * @return the identity Function1
     */
    static <T> Function1<T, T> identity() {
        return t -> t;
    }

    /**
     * Applies this function to one argument and returns the result.
     *
     * @param t1 argument 1
     * @return the result of function application
     * 
     */
    R apply(T1 t1);

    /**
     * Returns a curried version of this function.
     *
     * @return a curried function equivalent to this.
     */
    default Function1<T1, R> curried() {
        return this;
    }

    /**
     * Returns a tupled version of this function.
     *
     * @return a tupled function equivalent to this.
     */
    default Function1<Tuple1<T1>, R> tupled() {
        return t -> apply(t._1);
    }

    /**
     * Returns a reversed version of this function. This may be useful in a recursive context.
     *
     * @return a reversed function equivalent to this.
     */
    default Function1<T1, R> reversed() {
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

    default Function1<T1, R> memoized() {
        if (isMemoized()) {
            return this;
        } else {
            final Map<T1, R> cache = new HashMap<>();
            return (Function1<T1, R> & Memoized) (t1) -> {
                synchronized (cache) {
                    if (cache.containsKey(t1)) {
                        return cache.get(t1);
                    } else {
                        final R value = apply(t1);
                        cache.put(t1, value);
                        return value;
                    }
                }
            };
        }
    }

    interface Memoized { /* zero abstract method (ZAM) interface */ }

    /**
     * Converts this {@code Function1} to a {@link PartialFunction} by adding an {@code isDefinedAt} condition.
     * <p>
     * @param isDefinedAt a predicate that states if an element is in the domain of the returned {@code PartialFunction}.
     * @return a new {@code PartialFunction} that has the same behavior like this function but is defined only for those elements that make it through the given {@code Predicate}
     * @throws NullPointerException if {@code isDefinedAt} is null
     */
    default PartialFunction<T1, R> partial(Predicate<? super T1> isDefinedAt) {
        Objects.requireNonNull(isDefinedAt, "isDefinedAt is null");
        final Function1<T1, R> self = this;
        return new PartialFunction<T1, R>() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isDefinedAt(T1 t1) {
                return isDefinedAt.test(t1);
            }

            @Override
            public R apply(T1 t1) {
              return self.apply(t1);
            }
        };
    }

    /**
     * Returns a composed function that first applies this Function1 to the given argument and then applies
     * {@linkplain Function} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> Function1<T1, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1) -> after.apply(apply(t1));
    }

    /**
     * Returns a composed function that first applies the {@linkplain Function} {@code before} the
     * given argument and then applies this Function1 to the result.
     *
     * @param <V> argument type of before
     * @param before the function applied before this
     * @return a function composed of before and this
     * @throws NullPointerException if before is null
     */
    default <V> Function1<V, R> compose(Function<? super V, ? extends T1> before) {
        Objects.requireNonNull(before, "before is null");
        return v -> apply(before.apply(v));
    }
}