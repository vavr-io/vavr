/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javaslang.control.Option;
import javaslang.control.Try;

/**
 * Represents a function with 10 arguments.
 *
 * @param <T1> argument 1 of the function
 * @param <T2> argument 2 of the function
 * @param <T3> argument 3 of the function
 * @param <T4> argument 4 of the function
 * @param <T5> argument 5 of the function
 * @param <T6> argument 6 of the function
 * @param <T7> argument 7 of the function
 * @param <T8> argument 8 of the function
 * @param <T9> argument 9 of the function
 * @param <T10> argument 10 of the function
 * @param <R> return type of the function
 * @author Daniel Dietrich
 * @since 1.1.0
 */
@FunctionalInterface
public interface CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> extends λ<R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Creates a {@code CheckedFunction10} based on
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
     * @param <T5> 5th argument
     * @param <T6> 6th argument
     * @param <T7> 7th argument
     * @param <T8> 8th argument
     * @param <T9> 9th argument
     * @param <T10> 10th argument
     * @return a {@code CheckedFunction10}
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> of(CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> methodReference) {
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
     * @param <T7> 7th argument
     * @param <T8> 8th argument
     * @param <T9> 9th argument
     * @param <T10> 10th argument
     * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Some(result)}
     *         if the function is defined for the given arguments, and {@code None} otherwise.
     */
    @SuppressWarnings("RedundantTypeArguments")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Option<R>> lift(CheckedFunction10<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? extends R> partialFunction) {
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> Try.<R>of(() -> partialFunction.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10)).getOption();
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
     * @param <T7> 7th argument
     * @param <T8> 8th argument
     * @param <T9> 9th argument
     * @param <T10> 10th argument
     * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Success(result)}
     *         if the function is defined for the given arguments, and {@code Failure(throwable)} otherwise.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Try<R>> liftTry(CheckedFunction10<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? extends R> partialFunction) {
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> Try.of(() -> partialFunction.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    /**
     * Narrows the given {@code CheckedFunction10<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? extends R>} to {@code CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>}
     *
     * @param f A {@code CheckedFunction10}
     * @param <R> return type
     * @param <T1> 1st argument
     * @param <T2> 2nd argument
     * @param <T3> 3rd argument
     * @param <T4> 4th argument
     * @param <T5> 5th argument
     * @param <T6> 6th argument
     * @param <T7> 7th argument
     * @param <T8> 8th argument
     * @param <T9> 9th argument
     * @param <T10> 10th argument
     * @return the given {@code f} instance as narrowed type {@code CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>}
     */
    @SuppressWarnings("unchecked")
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> narrow(CheckedFunction10<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? extends R> f) {
        return (CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>) f;
    }

    /**
     * Applies this function to 10 arguments and returns the result.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @param t5 argument 5
     * @param t6 argument 6
     * @param t7 argument 7
     * @param t8 argument 8
     * @param t9 argument 9
     * @param t10 argument 10
     * @return the result of function application
     * @throws Throwable if something goes wrong applying this function to the given arguments
     */
    R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) throws Throwable;

    /**
     * Applies this function partially to one argument.
     *
     * @param t1 argument 1
     * @return a partial application of this function
     */
    default CheckedFunction9<T2, T3, T4, T5, T6, T7, T8, T9, T10, R> apply(T1 t1) {
        return (T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    /**
     * Applies this function partially to two arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @return a partial application of this function
     */
    default CheckedFunction8<T3, T4, T5, T6, T7, T8, T9, T10, R> apply(T1 t1, T2 t2) {
        return (T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    /**
     * Applies this function partially to three arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @return a partial application of this function
     */
    default CheckedFunction7<T4, T5, T6, T7, T8, T9, T10, R> apply(T1 t1, T2 t2, T3 t3) {
        return (T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
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
    default CheckedFunction6<T5, T6, T7, T8, T9, T10, R> apply(T1 t1, T2 t2, T3 t3, T4 t4) {
        return (T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
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
    default CheckedFunction5<T6, T7, T8, T9, T10, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return (T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    /**
     * Applies this function partially to 6 arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @param t5 argument 5
     * @param t6 argument 6
     * @return a partial application of this function
     */
    default CheckedFunction4<T7, T8, T9, T10, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return (T7 t7, T8 t8, T9 t9, T10 t10) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    /**
     * Applies this function partially to 7 arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @param t5 argument 5
     * @param t6 argument 6
     * @param t7 argument 7
     * @return a partial application of this function
     */
    default CheckedFunction3<T8, T9, T10, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return (T8 t8, T9 t9, T10 t10) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    /**
     * Applies this function partially to 8 arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @param t5 argument 5
     * @param t6 argument 6
     * @param t7 argument 7
     * @param t8 argument 8
     * @return a partial application of this function
     */
    default CheckedFunction2<T9, T10, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return (T9 t9, T10 t10) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    /**
     * Applies this function partially to 9 arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @param t5 argument 5
     * @param t6 argument 6
     * @param t7 argument 7
     * @param t8 argument 8
     * @param t9 argument 9
     * @return a partial application of this function
     */
    default CheckedFunction1<T10, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return (T10 t10) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    default int arity() {
        return 10;
    }

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
     * @param <T7> generic parameter type 7 of the resulting function
     * @param <T8> generic parameter type 8 of the resulting function
     * @param <T9> generic parameter type 9 of the resulting function
     * @param <T10> generic parameter type 10 of the resulting function
     * @param <R> the result type
     * @param value the value to be returned
     * @return a function always returning the given value
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> constant(R value) {
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> value;
    }

    @Override
    default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, CheckedFunction1<T10, R>>>>>>>>>> curried() {
        return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    default CheckedFunction1<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10);
    }

    @Override
    default CheckedFunction10<T10, T9, T8, T7, T6, T5, T4, T3, T2, T1, R> reversed() {
        return (t10, t9, t8, t7, t6, t5, t4, t3, t2, t1) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    default CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> memoized() {
        if (isMemoized()) {
            return this;
        } else {
            final Map<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R> cache = new HashMap<>();
            return (CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> & Memoized) (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10)
                    -> Memoized.of(cache, Tuple.of(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10), t -> Try.of(() -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10)).get());
        }
    }

    /**
     * Return a composed function that first applies this CheckedFunction10 to the given arguments and in case of throwable
     * try to get value from {@code recover} function with same arguments and throwable information.
     *
     * @param recover the function applied in case of throwable
     * @return a function composed of this and recover
     * @throws NullPointerException if recover is null
     */
    default Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> recover(Function<? super Throwable, ? extends Function10<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? extends R>> recover) {
        Objects.requireNonNull(recover, "recover is null");
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> {
            try {
                return this.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
            } catch (Throwable throwable) {
                final Function10<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? extends R> func = recover.apply(throwable);
                Objects.requireNonNull(func, () -> "recover return null for " + throwable.getClass() + ": " + throwable.getMessage());
                return func.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
            }
        };
    }

    /**
     * Return unchecked function that will return this CheckedFunction10 result in correct case and throw runtime exception
     * wrapped by {@code exceptionMapper} in case of throwable
     *
     * @param exceptionMapper the function that convert function {@link Throwable} into subclass of {@link RuntimeException}
     * @return a new Function10 that wraps this CheckedFunction10 by throwing a {@code RuntimeException} issued by the given {@code exceptionMapper} in the case of a failure
     */
    default Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> unchecked(Function<? super Throwable, ? extends RuntimeException> exceptionMapper) {
        return recover(throwable -> {
            throw exceptionMapper.apply(throwable);
        });
    }

    /**
     * Return unchecked function that will return this CheckedFunction10 result in correct case and throw exception
     * wrapped by {@link IllegalStateException} in case of throwable.
     *
     * @return a new Function10 that wraps this CheckedFunction10 by throwing an {@code IllegalStateException} in the case of a failure
     */
    default Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> unchecked() {
        return unchecked(IllegalStateException::new);
    }

    /**
     * Returns a composed function that first applies this CheckedFunction10 to the given argument and then applies
     * {@linkplain CheckedFunction1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, V> andThen(CheckedFunction1<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

}