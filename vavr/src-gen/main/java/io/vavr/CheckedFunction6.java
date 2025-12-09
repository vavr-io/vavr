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

import static io.vavr.CheckedFunction6Module.sneakyThrow;

import io.vavr.control.Option;
import io.vavr.control.Try;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import org.jspecify.annotations.NonNull;

/**
 * Represents a function with 6 arguments.
 *
 * @param <T1> argument 1 of the function
 * @param <T2> argument 2 of the function
 * @param <T3> argument 3 of the function
 * @param <T4> argument 4 of the function
 * @param <T5> argument 5 of the function
 * @param <T6> argument 6 of the function
 * @param <R> return type of the function
 * @author Daniel Dietrich
 */
@FunctionalInterface
public interface CheckedFunction6<T1, T2, T3, T4, T5, T6, R> extends Serializable {

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
     * @param <T5> generic parameter type 5 of the resulting function
     * @param <T6> generic parameter type 6 of the resulting function
     * @param <R> the result type
     * @param value the value to be returned
     * @return a function always returning the given value
     */
    static <T1, T2, T3, T4, T5, T6, R> CheckedFunction6<T1, T2, T3, T4, T5, T6, R> constant(R value) {
        return (t1, t2, t3, t4, t5, t6) -> value;
    }

    /**
     * Creates a {@code CheckedFunction6} based on
     * <ul>
     * <li><a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method reference</a></li>
     * <li><a href="https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html#syntax">lambda expression</a></li>
     * </ul>
     *
     * Examples (w.l.o.g. referring to Function1):
     * <pre>{@code // using a lambda expression
     * Function1&lt;Integer, Integer&gt; add1 = Function1.of(i -&gt; i + 1);
     *
     * // using a method reference (, e.g. Integer method(Integer i) { return i + 1; })
     * Function1&lt;Integer, Integer&gt; add2 = Function1.of(this::method);
     *
     * // using a lambda reference
     * Function1&lt;Integer, Integer&gt; add3 = Function1.of(add1::apply);
     * }</pre>
     * <p>
     * <strong>Caution:</strong> Reflection loses type information of lambda references.
     * <pre>{@code // type of a lambda expression
     * Type&lt;?, ?&gt; type1 = add1.getType(); // (Integer) -&gt; Integer
     *
     * // type of a method reference
     * Type&lt;?, ?&gt; type2 = add2.getType(); // (Integer) -&gt; Integer
     *
     * // type of a lambda reference
     * Type&lt;?, ?&gt; type3 = add3.getType(); // (Object) -&gt; Object
     * }</pre>
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @param <R> return type
     * @param <T1> 1st argument
     * @param <T2> 2nd argument
     * @param <T3> 3rd argument
     * @param <T4> 4th argument
     * @param <T5> 5th argument
     * @param <T6> 6th argument
     * @return a {@code CheckedFunction6}
     */
    static <T1, T2, T3, T4, T5, T6, R> CheckedFunction6<T1, T2, T3, T4, T5, T6, R> of(@NonNull CheckedFunction6<T1, T2, T3, T4, T5, T6, R> methodReference) {
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
     * @param <T5> 5th argument
     * @param <T6> 6th argument
     * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Some(result)}
     *         if the function is defined for the given arguments, and {@code None} otherwise.
     */
    static <T1, T2, T3, T4, T5, T6, R> Function6<T1, T2, T3, T4, T5, T6, Option<R>> lift(@NonNull CheckedFunction6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> partialFunction) {
        return (t1, t2, t3, t4, t5, t6) -> Try.<R>of(() -> partialFunction.apply(t1, t2, t3, t4, t5, t6)).toOption();
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
     * @param <T5> 5th argument
     * @param <T6> 6th argument
     * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Success(result)}
     *         if the function is defined for the given arguments, and {@code Failure(throwable)} otherwise.
     */
    static <T1, T2, T3, T4, T5, T6, R> Function6<T1, T2, T3, T4, T5, T6, Try<R>> liftTry(@NonNull CheckedFunction6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> partialFunction) {
        return (t1, t2, t3, t4, t5, t6) -> Try.of(() -> partialFunction.apply(t1, t2, t3, t4, t5, t6));
    }

    /**
     * Narrows the given {@code CheckedFunction6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R>} to {@code CheckedFunction6<T1, T2, T3, T4, T5, T6, R>}
     *
     * @param f A {@code CheckedFunction6}
     * @param <R> return type
     * @param <T1> 1st argument
     * @param <T2> 2nd argument
     * @param <T3> 3rd argument
     * @param <T4> 4th argument
     * @param <T5> 5th argument
     * @param <T6> 6th argument
     * @return the given {@code f} instance as narrowed type {@code CheckedFunction6<T1, T2, T3, T4, T5, T6, R>}
     */
    @SuppressWarnings("unchecked")
    static <T1, T2, T3, T4, T5, T6, R> CheckedFunction6<T1, T2, T3, T4, T5, T6, R> narrow(CheckedFunction6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
        return (CheckedFunction6<T1, T2, T3, T4, T5, T6, R>) f;
    }

    /**
     * Applies this function to 6 arguments and returns the result.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @param t5 argument 5
     * @param t6 argument 6
     * @return the result of function application
     * @throws Throwable if something goes wrong applying this function to the given arguments
     */
    R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) throws Throwable;

    /**
     * Applies this function partially to one argument.
     *
     * @param t1 argument 1
     * @return a partial application of this function
     */
    default CheckedFunction5<T2, T3, T4, T5, T6, R> apply(T1 t1) {
        return (T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) -> apply(t1, t2, t3, t4, t5, t6);
    }

    /**
     * Applies this function partially to two arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @return a partial application of this function
     */
    default CheckedFunction4<T3, T4, T5, T6, R> apply(T1 t1, T2 t2) {
        return (T3 t3, T4 t4, T5 t5, T6 t6) -> apply(t1, t2, t3, t4, t5, t6);
    }

    /**
     * Applies this function partially to three arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @return a partial application of this function
     */
    default CheckedFunction3<T4, T5, T6, R> apply(T1 t1, T2 t2, T3 t3) {
        return (T4 t4, T5 t5, T6 t6) -> apply(t1, t2, t3, t4, t5, t6);
    }

    /**
     * Applies this function partially to 4 arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @return a partial application of this function
     */
    default CheckedFunction2<T5, T6, R> apply(T1 t1, T2 t2, T3 t3, T4 t4) {
        return (T5 t5, T6 t6) -> apply(t1, t2, t3, t4, t5, t6);
    }

    /**
     * Applies this function partially to 5 arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @param t5 argument 5
     * @return a partial application of this function
     */
    default CheckedFunction1<T6, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return (T6 t6) -> apply(t1, t2, t3, t4, t5, t6);
    }

    /**
     * Returns the number of function arguments.
     * @return an int value &gt;= 0
     * @see <a href="http://en.wikipedia.org/wiki/Arity">Arity</a>
     */
    default int arity() {
        return 6;
    }

    /**
     * Returns a curried version of this function.
     *
     * @return a curried function equivalent to this.
     */
    default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, CheckedFunction1<T6, R>>>>>> curried() {
        return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> apply(t1, t2, t3, t4, t5, t6);
    }

    /**
     * Returns a tupled version of this function.
     *
     * @return a tupled function equivalent to this.
     */
    default CheckedFunction1<Tuple6<T1, T2, T3, T4, T5, T6>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6);
    }

    /**
     * Returns a reversed version of this function. This may be useful in a recursive context.
     *
     * @return a reversed function equivalent to this.
     */
    default CheckedFunction6<T6, T5, T4, T3, T2, T1, R> reversed() {
        return (t6, t5, t4, t3, t2, t1) -> apply(t1, t2, t3, t4, t5, t6);
    }

    /**
     * Returns a memoizing version of this function, which computes the return value for given arguments only one time.
     * On subsequent calls given the same arguments the memoized value is returned.
     * <p>
     * Please note that memoizing functions do not permit {@code null} as single argument or return value.
     *
     * @return a memoizing function equivalent to this.
     */
    default CheckedFunction6<T1, T2, T3, T4, T5, T6, R> memoized() {
        if (isMemoized()) {
            return this;
        } else {
            final Map<Tuple6<T1, T2, T3, T4, T5, T6>, R> cache = new HashMap<>();
            final ReentrantLock lock = new ReentrantLock();
            return (CheckedFunction6<T1, T2, T3, T4, T5, T6, R> & Memoized) (t1, t2, t3, t4, t5, t6) -> {
                final Tuple6<T1, T2, T3, T4, T5, T6> key = Tuple.of(t1, t2, t3, t4, t5, t6);
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
     * Return a composed function that first applies this CheckedFunction6 to the given arguments and in case of throwable
     * try to get value from {@code recover} function with same arguments and throwable information.
     *
     * @param recover the function applied in case of throwable
     * @return a function composed of this and recover
     * @throws NullPointerException if recover is null
     */
    default Function6<T1, T2, T3, T4, T5, T6, R> recover(@NonNull Function<? super Throwable, ? extends Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R>> recover) {
        Objects.requireNonNull(recover, "recover is null");
        return (t1, t2, t3, t4, t5, t6) -> {
            try {
                return this.apply(t1, t2, t3, t4, t5, t6);
            } catch (Throwable throwable) {
                final Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> func = recover.apply(throwable);
                Objects.requireNonNull(func, () -> "recover return null for " + throwable.getClass() + ": " + throwable.getMessage());
                return func.apply(t1, t2, t3, t4, t5, t6);
            }
        };
    }

    /**
     * Returns an unchecked function that will <em>sneaky throw</em> if an exceptions occurs when applying the function.
     *
     * @return a new Function6 that throws a {@code Throwable}.
     */
    default Function6<T1, T2, T3, T4, T5, T6, R> unchecked() {
        return (t1, t2, t3, t4, t5, t6) -> {
            try {
                return apply(t1, t2, t3, t4, t5, t6);
            } catch(Throwable t) {
                return sneakyThrow(t);
            }
        };
    }

    /**
     * Returns a composed function that first applies this CheckedFunction6 to the given argument and then applies
     * {@linkplain CheckedFunction1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> CheckedFunction6<T1, T2, T3, T4, T5, T6, V> andThen(@NonNull CheckedFunction1<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1, t2, t3, t4, t5, t6) -> after.apply(apply(t1, t2, t3, t4, t5, t6));
    }

    /**
     * Returns a composed function that first applies the {@linkplain Function} {@code before} to the
     * 1st argument and then applies this CheckedFunction6 to the result and the other arguments.
     *
     * @param <S> argument type of before
     * @param before the function applied before this
     * @return a function composed of before and this
     * @throws NullPointerException if before is null
     */
    default <S> CheckedFunction6<S, T2, T3, T4, T5, T6, R> compose1(@NonNull Function1<? super S, ? extends T1> before) {
        Objects.requireNonNull(before, "before is null");
        return (S s, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) -> apply(before.apply(s), t2, t3, t4, t5, t6);
    }

    /**
     * Returns a composed function that first applies the {@linkplain Function} {@code before} to the
     * 2nd argument and then applies this CheckedFunction6 to the result and the other arguments.
     *
     * @param <S> argument type of before
     * @param before the function applied before this
     * @return a function composed of before and this
     * @throws NullPointerException if before is null
     */
    default <S> CheckedFunction6<T1, S, T3, T4, T5, T6, R> compose2(@NonNull Function1<? super S, ? extends T2> before) {
        Objects.requireNonNull(before, "before is null");
        return (T1 t1, S s, T3 t3, T4 t4, T5 t5, T6 t6) -> apply(t1, before.apply(s), t3, t4, t5, t6);
    }

    /**
     * Returns a composed function that first applies the {@linkplain Function} {@code before} to the
     * 3rd argument and then applies this CheckedFunction6 to the result and the other arguments.
     *
     * @param <S> argument type of before
     * @param before the function applied before this
     * @return a function composed of before and this
     * @throws NullPointerException if before is null
     */
    default <S> CheckedFunction6<T1, T2, S, T4, T5, T6, R> compose3(@NonNull Function1<? super S, ? extends T3> before) {
        Objects.requireNonNull(before, "before is null");
        return (T1 t1, T2 t2, S s, T4 t4, T5 t5, T6 t6) -> apply(t1, t2, before.apply(s), t4, t5, t6);
    }

    /**
     * Returns a composed function that first applies the {@linkplain Function} {@code before} to the
     * 4th argument and then applies this CheckedFunction6 to the result and the other arguments.
     *
     * @param <S> argument type of before
     * @param before the function applied before this
     * @return a function composed of before and this
     * @throws NullPointerException if before is null
     */
    default <S> CheckedFunction6<T1, T2, T3, S, T5, T6, R> compose4(@NonNull Function1<? super S, ? extends T4> before) {
        Objects.requireNonNull(before, "before is null");
        return (T1 t1, T2 t2, T3 t3, S s, T5 t5, T6 t6) -> apply(t1, t2, t3, before.apply(s), t5, t6);
    }

    /**
     * Returns a composed function that first applies the {@linkplain Function} {@code before} to the
     * 5th argument and then applies this CheckedFunction6 to the result and the other arguments.
     *
     * @param <S> argument type of before
     * @param before the function applied before this
     * @return a function composed of before and this
     * @throws NullPointerException if before is null
     */
    default <S> CheckedFunction6<T1, T2, T3, T4, S, T6, R> compose5(@NonNull Function1<? super S, ? extends T5> before) {
        Objects.requireNonNull(before, "before is null");
        return (T1 t1, T2 t2, T3 t3, T4 t4, S s, T6 t6) -> apply(t1, t2, t3, t4, before.apply(s), t6);
    }

    /**
     * Returns a composed function that first applies the {@linkplain Function} {@code before} to the
     * 6th argument and then applies this CheckedFunction6 to the result and the other arguments.
     *
     * @param <S> argument type of before
     * @param before the function applied before this
     * @return a function composed of before and this
     * @throws NullPointerException if before is null
     */
    default <S> CheckedFunction6<T1, T2, T3, T4, T5, S, R> compose6(@NonNull Function1<? super S, ? extends T6> before) {
        Objects.requireNonNull(before, "before is null");
        return (T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, S s) -> apply(t1, t2, t3, t4, t5, before.apply(s));
    }
}

interface CheckedFunction6Module {

    // DEV-NOTE: we do not plan to expose this as public API
    @SuppressWarnings("unchecked")
    static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}