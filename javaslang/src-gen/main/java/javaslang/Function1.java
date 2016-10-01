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
import java.util.function.Function;
import javaslang.control.Option;
import javaslang.control.Try;

/**
 * Represents a function with one argument.
 *
 * @param <T1> argument 1 of the function
 * @param <R> return type of the function
 * @author Daniel Dietrich
 * @since 1.1.0
 */
@FunctionalInterface
public interface Function1<T1, R> extends λ<R>, Function<T1, R> {

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
     * Lifts the given {@code partialFunction} into a total function that returns an {@code Option} result.
     *
     * @param partialFunction a function that is not defined for all values of the domain (e.g. by throwing)
     * @param <R> return type
     * @param <T1> 1st argument
     * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Some(result)}
     *         if the function is defined for the given arguments, and {@code None} otherwise.
     */
    @SuppressWarnings("RedundantTypeArguments")
    static <T1, R> Function1<T1, Option<R>> lift(Function<? super T1, ? extends R> partialFunction) {
        return t1 -> Try.<R>of(() -> partialFunction.apply(t1)).getOption();
    }

    /**
     * Lifts the given {@code partialFunction} into a total function that returns an {@code Try} result.
     *
     * @param partialFunction a function that is not defined for all values of the domain (e.g. by throwing)
     * @param <R> return type
     * @param <T1> 1st argument
     * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Success(result)}
     *         if the function is defined for the given arguments, and {@code Failure(throwable)} otherwise.
     */
    static <T1, R> Function1<T1, Try<R>> liftTry(Function<? super T1, ? extends R> partialFunction) {
        return t1 -> Try.of(() -> partialFunction.apply(t1));
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

    @Override
    default int arity() {
        return 1;
    }

    /**
     * Returns a function that always returns the constant
     * value that you give in parameter.
     *
     * @param <T1> generic parameter type 1 of the resulting function
     * @param <R> the result type
     * @param value the value to be returned
     * @return a function always returning the given value
     */
    static <T1, R> Function1<T1, R> constant(R value) {
        return (t1) -> value;
    }

    @Override
    default Function1<T1, R> curried() {
        return this;
    }

    @Override
    default Function1<Tuple1<T1>, R> tupled() {
        return t -> apply(t._1);
    }

    @Override
    default Function1<T1, R> reversed() {
        return this;
    }

    @Override
    default Function1<T1, R> memoized() {
        if (isMemoized()) {
            return this;
        } else {
            final Object lock = new Object();
            final Map<T1, R> cache = new HashMap<>();
            return (Function1<T1, R> & Memoized) t1 -> {
                synchronized (lock) {
                    return cache.computeIfAbsent(t1, this);
                }
            };
        }
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