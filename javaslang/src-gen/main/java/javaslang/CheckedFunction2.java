/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import javaslang.control.Option;
import javaslang.control.Try;

/**
 * Represents a function with two arguments.
 *
 * @param <T1> argument 1 of the function
 * @param <T2> argument 2 of the function
 * @param <R> return type of the function
 * @author Daniel Dietrich
 * @since 1.1.0
 */
@FunctionalInterface
public interface CheckedFunction2<T1, T2, R> extends λ<R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Creates a {@code CheckedFunction2} based on
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
     * @return a {@code CheckedFunction2}
     */
    static <T1, T2, R> CheckedFunction2<T1, T2, R> of(CheckedFunction2<T1, T2, R> methodReference) {
        return methodReference;
    }

    /**
     * Lifts the given {@code partialFunction} into a total function that returns an {@code Option} result.
     *
     * @param partialFunction a function that is not defined for all values of the domain (e.g. by throwing)
     * @param <R> return type
     * @param <T1> 1st argument
     * @param <T2> 2nd argument
     * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Some(result)}
     *         if the function is defined for the given arguments, and {@code None} otherwise.
     */
    @SuppressWarnings("RedundantTypeArguments")
    static <T1, T2, R> Function2<T1, T2, Option<R>> lift(CheckedFunction2<? super T1, ? super T2, ? extends R> partialFunction) {
        return (t1, t2) -> Try.<R>of(() -> partialFunction.apply(t1, t2)).getOption();
    }

    /**
     * Lifts the given {@code partialFunction} into a total function that returns an {@code Try} result.
     *
     * @param partialFunction a function that is not defined for all values of the domain (e.g. by throwing)
     * @param <R> return type
     * @param <T1> 1st argument
     * @param <T2> 2nd argument
     * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Success(result)}
     *         if the function is defined for the given arguments, and {@code Failure(throwable)} otherwise.
     */
    static <T1, T2, R> Function2<T1, T2, Try<R>> liftTry(CheckedFunction2<? super T1, ? super T2, ? extends R> partialFunction) {
        return (t1, t2) -> Try.of(() -> partialFunction.apply(t1, t2));
    }

    /**
     * Applies this function to two arguments and returns the result.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @return the result of function application
     * @throws Throwable if something goes wrong applying this function to the given arguments
     */
    R apply(T1 t1, T2 t2) throws Throwable;

    /**
     * Applies this function partially to one argument.
     *
     * @param t1 argument 1
     * @return a partial application of this function
     */
    default CheckedFunction1<T2, R> apply(T1 t1) {
        return (T2 t2) -> apply(t1, t2);
    }

    @Override
    default int arity() {
        return 2;
    }

    /**
     * Returns a function that always returns the constant
     * value that you give in parameter.
     *
     * @param <T1> generic parameter type 1 of the resulting function
     * @param <T2> generic parameter type 2 of the resulting function
     * @param <R> the result type
     * @param value the value to be returned
     * @return a function always returning the given value
     */
    static <T1, T2, R> CheckedFunction2<T1, T2, R> constant(R value) {
        return (t1, t2) -> value;
    }

    @Override
    default Function1<T1, CheckedFunction1<T2, R>> curried() {
        return t1 -> t2 -> apply(t1, t2);
    }

    @Override
    default CheckedFunction1<Tuple2<T1, T2>, R> tupled() {
        return t -> apply(t._1, t._2);
    }

    @Override
    default CheckedFunction2<T2, T1, R> reversed() {
        return (t2, t1) -> apply(t1, t2);
    }

    @Override
    default CheckedFunction2<T1, T2, R> memoized() {
        if (isMemoized()) {
            return this;
        } else {
            final Object lock = new Object();
            final Map<Tuple2<T1, T2>, R> cache = new HashMap<>();
            final CheckedFunction1<Tuple2<T1, T2>, R> tupled = tupled();
            return (CheckedFunction2<T1, T2, R> & Memoized) (t1, t2) -> {
                synchronized (lock) {
                    return cache.computeIfAbsent(Tuple.of(t1, t2), t -> Try.of(() -> tupled.apply(t)).get());
                }
            };
        }
    }

    /**
     * Return a composed function that first applies this CheckedFunction2 to the given arguments and in case of throwable
     * try to get value from {@code recover} function with same arguments and throwable information.
     *
     * @param recover the function applied in case of throwable
     * @return a function composed of this and recover
     * @throws NullPointerException if recover is null
     */
    default Function2<T1, T2, R> recover(Function<? super Throwable, ? extends BiFunction<? super T1, ? super T2, ? extends R>> recover) {
        Objects.requireNonNull(recover, "recover is null");
        return (t1, t2) -> {
            try {
                return this.apply(t1, t2);
            } catch (Throwable throwable) {
                final BiFunction<? super T1, ? super T2, ? extends R> func = recover.apply(throwable);
                Objects.requireNonNull(func, () -> "recover return null for " + throwable.getClass() + ": " + throwable.getMessage());
                return func.apply(t1, t2);
            }
        };
    }

    /**
     * Return unchecked function that will return this CheckedFunction2 result in correct case and throw runtime exception
     * wrapped by {@code exceptionMapper} in case of throwable
     *
     * @param exceptionMapper the function that convert function {@link Throwable} into subclass of {@link RuntimeException}
     * @return a new Function2 that wraps this CheckedFunction2 by throwing a {@code RuntimeException} issued by the given {@code exceptionMapper} in the case of a failure
     */
    default Function2<T1, T2, R> unchecked(Function<? super Throwable, ? extends RuntimeException> exceptionMapper) {
        return recover(throwable -> {
            throw exceptionMapper.apply(throwable);
        });
    }

    /**
     * Return unchecked function that will return this CheckedFunction2 result in correct case and throw exception
     * wrapped by {@link IllegalStateException} in case of throwable.
     *
     * @return a new Function2 that wraps this CheckedFunction2 by throwing an {@code IllegalStateException} in the case of a failure
     */
    default Function2<T1, T2, R> unchecked() {
        return unchecked(IllegalStateException::new);
    }

    /**
     * Returns a composed function that first applies this CheckedFunction2 to the given argument and then applies
     * {@linkplain CheckedFunction1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> CheckedFunction2<T1, T2, V> andThen(CheckedFunction1<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1, t2) -> after.apply(apply(t1, t2));
    }

}