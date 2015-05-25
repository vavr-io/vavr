/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a function with 4 arguments.
 *
 * @param <T1> argument 1 of the function
 * @param <T2> argument 2 of the function
 * @param <T3> argument 3 of the function
 * @param <T4> argument 4 of the function
 * @param <R> return type of the function
 * @since 1.1.0
 */
@FunctionalInterface
public interface Function4<T1, T2, T3, T4, R> extends λ<R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Lifts a <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method
     * reference</a> to a {@code Function4}.
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @param <R> return type
     * @param <T1> 1st argument
     * @param <T2> 2nd argument
     * @param <T3> 3rd argument
     * @param <T4> 4th argument
     * @return a {@code Function4}
     */
    static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> lift(Function4<T1, T2, T3, T4, R> methodReference) {
        return methodReference;
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
     * 
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
     * 
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
     * 
     */
    default Function1<T4, R> apply(T1 t1, T2 t2, T3 t3) {
        return (T4 t4) -> apply(t1, t2, t3, t4);
    }

    @Override
    default int arity() {
        return 4;
    }

    @Override
    default Function1<T1, Function1<T2, Function1<T3, Function1<T4, R>>>> curried() {
        return t1 -> t2 -> t3 -> t4 -> apply(t1, t2, t3, t4);
    }

    @Override
    default Function1<Tuple4<T1, T2, T3, T4>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4);
    }

    @Override
    default Function4<T4, T3, T2, T1, R> reversed() {
        return (t4, t3, t2, t1) -> apply(t1, t2, t3, t4);
    }

    @Override
    default Function4<T1, T2, T3, T4, R> memoized() {
        final Map<Tuple4<T1, T2, T3, T4>, R> cache = new ConcurrentHashMap<>();
        final Function1<Tuple4<T1, T2, T3, T4>, R> tupled = tupled();
        return (t1, t2, t3, t4) -> cache.computeIfAbsent(Tuple.of(t1, t2, t3, t4), tupled::apply);
    }

    /**
     * Returns a composed function that first applies this Function4 to the given argument and then applies
     * {@linkplain Function1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> Function4<T1, T2, T3, T4, V> andThen(Function1<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
    }

}