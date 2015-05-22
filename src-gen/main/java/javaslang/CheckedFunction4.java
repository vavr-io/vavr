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
import javaslang.control.Try;

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
public interface CheckedFunction4<T1, T2, T3, T4, R> extends λ<R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Lifts a <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method
     * reference</a> to a {@code CheckedFunction4}.
     *
     * @param methodReference (typically) a method reference, e.g. {@code Type::method}
     * @param <R> return type
     * @param <T1> 1st argument
     * @param <T2> 2nd argument
     * @param <T3> 3rd argument
     * @param <T4> 4th argument
     * @return a {@code CheckedFunction4}
     */
    static <T1, T2, T3, T4, R> CheckedFunction4<T1, T2, T3, T4, R> lift(CheckedFunction4<T1, T2, T3, T4, R> methodReference) {
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
     * @throws Throwable if something goes wrong applying this function to the given arguments
     */
    R apply(T1 t1, T2 t2, T3 t3, T4 t4) throws Throwable;

    /**
     * Applies this function partially to one argument.
     *
     * @param t1 argument 1
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction3<T2, T3, T4, R> apply(T1 t1) throws Throwable {
        return (T2 t2, T3 t3, T4 t4) -> apply(t1, t2, t3, t4);
    }

    /**
     * Applies this function partially to two arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction2<T3, T4, R> apply(T1 t1, T2 t2) throws Throwable {
        return (T3 t3, T4 t4) -> apply(t1, t2, t3, t4);
    }

    /**
     * Applies this function partially to three arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction1<T4, R> apply(T1 t1, T2 t2, T3 t3) throws Throwable {
        return (T4 t4) -> apply(t1, t2, t3, t4);
    }

    @Override
    default int arity() {
        return 4;
    }

    @Override
    default CheckedFunction1<T1, CheckedFunction1<T2, CheckedFunction1<T3, CheckedFunction1<T4, R>>>> curried() {
        return t1 -> t2 -> t3 -> t4 -> apply(t1, t2, t3, t4);
    }

    @Override
    default CheckedFunction1<Tuple4<T1, T2, T3, T4>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4);
    }

    @Override
    default CheckedFunction4<T4, T3, T2, T1, R> reversed() {
        return (t4, t3, t2, t1) -> apply(t1, t2, t3, t4);
    }

    @Override
    default CheckedFunction4<T1, T2, T3, T4, R> memoized() {
        final Map<Tuple4<T1, T2, T3, T4>, R> cache = new ConcurrentHashMap<>();
        final CheckedFunction1<Tuple4<T1, T2, T3, T4>, R> tupled = tupled();
        return (t1, t2, t3, t4) -> cache.computeIfAbsent(Tuple.of(t1, t2, t3, t4), t -> Try.of(() -> tupled.apply(t)).get());
    }

    /**
     * Returns a composed function that first applies this CheckedFunction4 to the given argument and then applies
     * {@linkplain CheckedFunction1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> CheckedFunction4<T1, T2, T3, T4, V> andThen(CheckedFunction1<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
    }

}