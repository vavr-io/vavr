/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
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

import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * Represents a function with 4 arguments.
 *
 * @param <T1> argument 1 of the function
 * @param <T2> argument 2 of the function
 * @param <T3> argument 3 of the function
 * @param <T4> argument 4 of the function
 * @param <R> return type of the function
 * @author Daniel Dietrich
 */
@FunctionalInterface
public interface Function4<T1, T2, T3, T4, R> extends Serializable {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Returns a function that always returns the constant
     * value that you give in parameter.
     *
     * @param <T1> generic parameter type 1 of the resulting function
     * @param <T2> generic parameter type 2 of the resulting function
     * @param <T3> generic parameter type 3 of the resulting function
     * @param <T4> generic parameter type 4 of the resulting function
     * @param <R> the result type
     * @param value the value to be returned
     * @return a function always returning the given value
     */
    static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> constant(R value) {
        return (t1, t2, t3, t4) -> value;
    }

    /**
     * Creates a {@code Function4} based on
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
     * @param <T2> 2nd argument
     * @param <T3> 3rd argument
     * @param <T4> 4th argument
     * @return a {@code Function4}
     */
    static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> of(Function4<T1, T2, T3, T4, R> methodReference) {
        return methodReference;
    }

    /**
     * Lifts the given {@code partialFunction} into a total function that returns an {@code Option} result.
     *
     * @param partialFunction a function that is not defined for all values of the domain (e.g. by throwing)
     * @param <R> return type
     * @param <T1> 1st argument
     * @param <T2> 2nd argument
     * @param <T3> 3rd argument
     * @param <T4> 4th argument
     * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Some(result)}
     *         if the function is defined for the given arguments, and {@code None} otherwise.
     */
    static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, Option<R>> lift(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> partialFunction) {
        return (t1, t2, t3, t4) -> Try.<R>of(() -> partialFunction.apply(t1, t2, t3, t4)).toOption();
    }

    /**
     * Lifts the given {@code partialFunction} into a total function that returns an {@code Try} result.
     *
     * @param partialFunction a function that is not defined for all values of the domain (e.g. by throwing)
     * @param <R> return type
     * @param <T1> 1st argument
     * @param <T2> 2nd argument
     * @param <T3> 3rd argument
     * @param <T4> 4th argument
     * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Success(result)}
     *         if the function is defined for the given arguments, and {@code Failure(throwable)} otherwise.
     */
    static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, Try<R>> liftTry(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> partialFunction) {
        return (t1, t2, t3, t4) -> Try.of(() -> partialFunction.apply(t1, t2, t3, t4));
    }

    /**
     * Narrows the given {@code Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R>} to {@code Function4<T1, T2, T3, T4, R>}
     *
     * @param f A {@code Function4}
     * @param <R> return type
     * @param <T1> 1st argument
     * @param <T2> 2nd argument
     * @param <T3> 3rd argument
     * @param <T4> 4th argument
     * @return the given {@code f} instance as narrowed type {@code Function4<T1, T2, T3, T4, R>}
     */
    @SuppressWarnings("unchecked")
    static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> narrow(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
        return (Function4<T1, T2, T3, T4, R>) f;
    }

    /**
     * Applies this function to 4 arguments and returns the result.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @return the result of function application
     * 
     */
    R apply(T1 t1, T2 t2, T3 t3, T4 t4);

    /**
     * Applies this function partially to one argument.
     *
     * @param t1 argument 1
     * @return a partial application of this function
     */
    default Function3<T2, T3, T4, R> apply(T1 t1) {
        return (T2 t2, T3 t3, T4 t4) -> apply(t1, t2, t3, t4);
    }

    /**
     * Applies this function partially to two arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @return a partial application of this function
     */
    default Function2<T3, T4, R> apply(T1 t1, T2 t2) {
        return (T3 t3, T4 t4) -> apply(t1, t2, t3, t4);
    }

    /**
     * Applies this function partially to three arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @return a partial application of this function
     */
    default Function1<T4, R> apply(T1 t1, T2 t2, T3 t3) {
        return (T4 t4) -> apply(t1, t2, t3, t4);
    }

    /**
     * Returns the number of function arguments.
     * @return an int value &gt;= 0
     * @see <a href="http://en.wikipedia.org/wiki/Arity">Arity</a>
     */
    default int arity() {
        return 4;
    }

    /**
     * Returns a curried version of this function.
     *
     * @return a curried function equivalent to this.
     */
    default Function1<T1, Function1<T2, Function1<T3, Function1<T4, R>>>> curried() {
        return t1 -> t2 -> t3 -> t4 -> apply(t1, t2, t3, t4);
    }

    /**
     * Returns a tupled version of this function.
     *
     * @return a tupled function equivalent to this.
     */
    default Function1<Tuple4<T1, T2, T3, T4>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4);
    }

    /**
     * Returns a reversed version of this function. This may be useful in a recursive context.
     *
     * @return a reversed function equivalent to this.
     */
    default Function4<T4, T3, T2, T1, R> reversed() {
        return (t4, t3, t2, t1) -> apply(t1, t2, t3, t4);
    }

    /**
     * Returns a memoizing version of this function, which computes the return value for given arguments only one time.
     * On subsequent calls given the same arguments the memoized value is returned.
     * <p>
     * Please note that memoizing functions do not permit {@code null} as single argument or return value.
     *
     * @return a memoizing function equivalent to this.
     */
    default Function4<T1, T2, T3, T4, R> memoized() {
        if (isMemoized()) {
            return this;
        } else {
            final Map<Tuple4<T1, T2, T3, T4>, R> cache = new HashMap<>();
            final ReentrantLock lock = new ReentrantLock();
            return (Function4<T1, T2, T3, T4, R> & Memoized) (t1, t2, t3, t4) -> {
                final Tuple4<T1, T2, T3, T4> key = Tuple.of(t1, t2, t3, t4);
                lock.lock();
                try {
                    if (cache.containsKey(key)) {
                        return cache.get(key);
                    } else {
                        final R value = tupled().apply(key);
                        cache.put(key, value);
                        return value;
                    }
                } finally {
                    lock.unlock();
                }
            };
        }
    }

    /**
     * Checks if this function is memoizing (= caching) computed values.
     *
     * @return true, if this function is memoizing, false otherwise
     */
    default boolean isMemoized() {
        return this instanceof Memoized;
    }

    /**
     * Returns a composed function that first applies this Function4 to the given argument and then applies
     * {@linkplain Function} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> Function4<T1, T2, T3, T4, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
    }

    /**
     * Returns a composed function that first applies the {@linkplain Function} {@code before} to the
     * 1st argument and then applies this Function4 to the result and the other arguments.
     *
     * @param <S> argument type of before
     * @param before the function applied before this
     * @return a function composed of before and this
     * @throws NullPointerException if before is null
     */
    default <S> Function4<S, T2, T3, T4, R> compose1(Function1<? super S, ? extends T1> before) {
        Objects.requireNonNull(before, "before is null");
        return (S s, T2 t2, T3 t3, T4 t4) -> apply(before.apply(s), t2, t3, t4);
    }

    /**
     * Returns a composed function that first applies the {@linkplain Function} {@code before} to the
     * 2nd argument and then applies this Function4 to the result and the other arguments.
     *
     * @param <S> argument type of before
     * @param before the function applied before this
     * @return a function composed of before and this
     * @throws NullPointerException if before is null
     */
    default <S> Function4<T1, S, T3, T4, R> compose2(Function1<? super S, ? extends T2> before) {
        Objects.requireNonNull(before, "before is null");
        return (T1 t1, S s, T3 t3, T4 t4) -> apply(t1, before.apply(s), t3, t4);
    }

    /**
     * Returns a composed function that first applies the {@linkplain Function} {@code before} to the
     * 3rd argument and then applies this Function4 to the result and the other arguments.
     *
     * @param <S> argument type of before
     * @param before the function applied before this
     * @return a function composed of before and this
     * @throws NullPointerException if before is null
     */
    default <S> Function4<T1, T2, S, T4, R> compose3(Function1<? super S, ? extends T3> before) {
        Objects.requireNonNull(before, "before is null");
        return (T1 t1, T2 t2, S s, T4 t4) -> apply(t1, t2, before.apply(s), t4);
    }

    /**
     * Returns a composed function that first applies the {@linkplain Function} {@code before} to the
     * 4th argument and then applies this Function4 to the result and the other arguments.
     *
     * @param <S> argument type of before
     * @param before the function applied before this
     * @return a function composed of before and this
     * @throws NullPointerException if before is null
     */
    default <S> Function4<T1, T2, T3, S, R> compose4(Function1<? super S, ? extends T4> before) {
        Objects.requireNonNull(before, "before is null");
        return (T1 t1, T2 t2, T3 t3, S s) -> apply(t1, t2, t3, before.apply(s));
    }
}