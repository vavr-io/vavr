/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.Objects;

/**
 * Represents a function with 16 arguments.
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
 * @param <T11> argument 11 of the function
 * @param <T12> argument 12 of the function
 * @param <T13> argument 13 of the function
 * @param <T14> argument 14 of the function
 * @param <T15> argument 15 of the function
 * @param <T16> argument 16 of the function
 * @param <R> return type of the function
 * @since 1.1.0
 */
@FunctionalInterface
public interface CheckedFunction16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> extends λ<R> {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Lifts a <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method
     * reference</a> to a {@code CheckedFunction16}.
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
     * @param <T11> 11th argument
     * @param <T12> 12th argument
     * @param <T13> 13th argument
     * @param <T14> 14th argument
     * @param <T15> 15th argument
     * @param <T16> 16th argument
     * @return a {@code CheckedFunction16}
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> CheckedFunction16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> lift(CheckedFunction16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> methodReference) {
        return methodReference;
    }

    /**
     * Applies this function to 16 arguments and returns the result.
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
     * @param t11 argument 11
     * @param t12 argument 12
     * @param t13 argument 13
     * @param t14 argument 14
     * @param t15 argument 15
     * @param t16 argument 16
     * @return the result of function application
     * @throws Throwable if something goes wrong applying this function to the given arguments
     */
    R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) throws Throwable;

    /**
     * Applies this function partially to one argument.
     *
     * @param t1 argument 1
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction15<T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> apply(T1 t1) throws Throwable {
        return (T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    /**
     * Applies this function partially to two arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction14<T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> apply(T1 t1, T2 t2) throws Throwable {
        return (T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
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
    default CheckedFunction13<T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> apply(T1 t1, T2 t2, T3 t3) throws Throwable {
        return (T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    /**
     * Applies this function partially to 4 arguments.
     *
     * @param t1 argument 1
     * @param t2 argument 2
     * @param t3 argument 3
     * @param t4 argument 4
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction12<T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> apply(T1 t1, T2 t2, T3 t3, T4 t4) throws Throwable {
        return (T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
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
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction11<T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) throws Throwable {
        return (T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
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
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction10<T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) throws Throwable {
        return (T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
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
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction9<T8, T9, T10, T11, T12, T13, T14, T15, T16, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) throws Throwable {
        return (T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
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
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction8<T9, T10, T11, T12, T13, T14, T15, T16, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) throws Throwable {
        return (T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
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
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction7<T10, T11, T12, T13, T14, T15, T16, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) throws Throwable {
        return (T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    /**
     * Applies this function partially to 10 arguments.
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
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction6<T11, T12, T13, T14, T15, T16, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) throws Throwable {
        return (T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    /**
     * Applies this function partially to 11 arguments.
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
     * @param t11 argument 11
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction5<T12, T13, T14, T15, T16, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) throws Throwable {
        return (T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    /**
     * Applies this function partially to 12 arguments.
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
     * @param t11 argument 11
     * @param t12 argument 12
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction4<T13, T14, T15, T16, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) throws Throwable {
        return (T13 t13, T14 t14, T15 t15, T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    /**
     * Applies this function partially to 13 arguments.
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
     * @param t11 argument 11
     * @param t12 argument 12
     * @param t13 argument 13
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction3<T14, T15, T16, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) throws Throwable {
        return (T14 t14, T15 t15, T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    /**
     * Applies this function partially to 14 arguments.
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
     * @param t11 argument 11
     * @param t12 argument 12
     * @param t13 argument 13
     * @param t14 argument 14
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction2<T15, T16, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) throws Throwable {
        return (T15 t15, T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    /**
     * Applies this function partially to 15 arguments.
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
     * @param t11 argument 11
     * @param t12 argument 12
     * @param t13 argument 13
     * @param t14 argument 14
     * @param t15 argument 15
     * @return a partial application of this function
     * @throws Throwable if something goes wrong partially applying this function to the given arguments
     */
    default CheckedFunction1<T16, R> apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) throws Throwable {
        return (T16 t16) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    @Override
    default int arity() {
        return 16;
    }

    @Override
    default CheckedFunction1<T1, CheckedFunction1<T2, CheckedFunction1<T3, CheckedFunction1<T4, CheckedFunction1<T5, CheckedFunction1<T6, CheckedFunction1<T7, CheckedFunction1<T8, CheckedFunction1<T9, CheckedFunction1<T10, CheckedFunction1<T11, CheckedFunction1<T12, CheckedFunction1<T13, CheckedFunction1<T14, CheckedFunction1<T15, CheckedFunction1<T16, R>>>>>>>>>>>>>>>> curried() {
        return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> t14 -> t15 -> t16 -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    @Override
    default CheckedFunction1<Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16);
    }

    @Override
    default CheckedFunction16<T16, T15, T14, T13, T12, T11, T10, T9, T8, T7, T6, T5, T4, T3, T2, T1, R> reversed() {
        return (t16, t15, t14, t13, t12, t11, t10, t9, t8, t7, t6, t5, t4, t3, t2, t1) -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    /**
     * Returns a composed function that first applies this CheckedFunction16 to the given argument and then applies
     * {@linkplain CheckedFunction1} {@code after} to the result.
     *
     * @param <V> return type of after
     * @param after the function applied after this
     * @return a function composed of this and after
     * @throws NullPointerException if after is null
     */
    default <V> CheckedFunction16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, V> andThen(CheckedFunction1<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after is null");
        return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

}