/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.Tuple.*;
import javaslang.monad.Try;

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * The class of reflective checked and unchecked functions with a specific return types.
 * </p>
 * <p>
 * The type <strong>λ</strong> 'lambda' is the mother of all functions.
 * </p>
 * <p>
 * There are checked functions {@linkplain CheckedFunction0} to {@linkplain CheckedFunction13}, short: {@linkplain X0} to {@linkplain X13}
 * (<em>Note: X stands for 'eXception'. The lambda symbol λ is hidden in X, just leave out the upper right line</em>).
 * Checked functions throw Throwable.<br>
 * And there are unchecked functions {@linkplain Function0} to {@linkplain Function13}, short: {@linkplain λ0} to {@linkplain λ13}.
 * Unchecked functions are special checked functions, they throw a RuntimeException instead of Throwable.
 * </p>
 * <p>
 * Additionally to that there are checked versions of all functions located in the Java package java.util.function.
 * </p>
 * This class is not intended to be extended.
 */
public final class Functions {

    /**
     * This class is not intended to be instantiated.
     */
    private Functions() {
        throw new AssertionError(Functions.class.getName() + " is not intended to be instantiated.");
    }

    /**
     * Serializes a lambda and returns the corresponding {@link java.lang.invoke.SerializedLambda}.
     *
     * @param lambda A serializable lambda
     * @return The serialized lambda wrapped in a {@link javaslang.monad.Try.Success}, or a {@link javaslang.monad.Try.Failure}
     * if an exception occurred.
     * @see <a
     * href="http://stackoverflow.com/questions/21860875/printing-debug-info-on-errors-with-java-8-lambda-expressions">printing
     * debug info on errors with java 8 lambda expressions</a>
     * @see <a href="http://www.slideshare.net/hendersk/method-handles-in-java">Method Handles in Java</a>
     */
    public static SerializedLambda getSerializedFunction(Serializable lambda) {
        return Try.of(() -> {
            final Method method = lambda.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return (SerializedLambda) method.invoke(lambda);
        }).get();
    }

    /**
     * <p>
     * Gets the runtime method signature of the given lambda instance. Especially this function is handy when the
     * functional interface is generic and the parameter and/or return types cannot be determined directly.
     * </p>
     * <p>
     * Uses internally the {@link java.lang.invoke.SerializedLambda#getImplMethodSignature()} by parsing the JVM field
     * types of the method signature. The result is a {@link java.lang.invoke.MethodType} which contains the return type
     * and the parameter types of the given lambda.
     * </p>
     *
     * @param lambda A serializable lambda.
     * @return The signature of the lambda as {@linkplain java.lang.invoke.MethodType}.
     */
    public static MethodType getLambdaSignature(Serializable lambda) {
        final String signature = getSerializedFunction(lambda).getImplMethodSignature();
        return MethodType.fromMethodDescriptorString(signature, lambda.getClass().getClassLoader());
    }

    /**
     * <p>
     * This is a general definition of a checked function of unknown parameters and a return value of type R.
     * A checked function may throw an exception. The exception type is not a generic type parameter because
     * when composing functions, we cannot say anything else about the resulting type of exception than that it is
     * a Throwable.
     * </p>
     * <p>
     * This class is intended to be used internally.
     * </p>
     *
     * @param <R> Return type of the checked function.
     */
    public static interface λ<R> extends Serializable {

        /**
         * @return the numper of function arguments.
         * @see <a href="http://en.wikipedia.org/wiki/Arity">Arity</a>
         */
        int arity();

        CheckedFunction1<?, ?> curried();

        CheckedFunction1<? extends Tuple, R> tupled();

        /**
         * There can be nothing said about the type of exception (in Java), if the Function arg is also a checked function.
         * In an ideal world we could denote the appropriate bound of both exception types (this and after).
         * This is the reason why CheckedFunction throws a Throwable instead of a concrete exception.
         *
         * @param after Functions applied after this
         * @param <V> Return value of after
         * @return A Function composed of this and after
         */
        <V> λ<V> andThen(Function<? super R, ? extends V> after);

        default MethodType getType() {
            return Functions.getLambdaSignature(this);
        }
    }

    @FunctionalInterface
    public static interface X0<R> extends λ<R> {

        R apply() throws Throwable;

        @Override
        default int arity() {
            return 0;
        }

        // http://stackoverflow.com/questions/643906/uses-for-the-java-void-reference-type
        @Override
        default CheckedFunction1<Void, R> curried() {
            return v -> apply();
        }

        @Override
        default CheckedFunction1<Tuple0, R> tupled() {
            return t -> apply();
        }

        @Override
        default <V> CheckedFunction0<V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return () -> after.apply(apply());
        }
    }

    /**
     * Alias for {@link X0}.
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction0<R> extends X0<R> {
    }

    @FunctionalInterface
    public static interface λ0<R> extends CheckedFunction0<R>, java.util.function.Supplier<R> {

        @Override
        R apply() throws RuntimeException;

        @Override
        default R get() {
            return apply();
        }

        @Override
        default Function1<Void, R> curried() {
            return v -> apply();
        }

        @Override
        default Function1<Tuple0, R> tupled() {
            return t -> apply();
        }
    }

    /**
     * Alias for {@link λ0}.
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function0<R> extends λ0<R> {
    }

    /**
     * A function with one argument which implements Serializable in order to obtain reflective runtime type information
     * via {@link javaslang.Functions#getLambdaSignature(Serializable)}.
     *
     * @param <T1> The parameter type of the function.
     * @param <R>  The return type of the function.
     */
    @FunctionalInterface
    public static interface X1<T1, R> extends λ<R> {

        static <T> CheckedFunction1<T, T> identity() {
            return t -> t;
        }

        R apply(T1 t1) throws Throwable;

        @Override
        default int arity() {
            return 1;
        }

        @Override
        default CheckedFunction1<T1, R> curried() {
            //noinspection Convert2MethodRef
            return t1 -> apply(t1);
        }

        @Override
        default CheckedFunction1<Tuple1<T1>, R> tupled() {
            return t -> apply(t._1);
        }

        @Override
        default <V> CheckedFunction1<T1, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return t1 -> after.apply(apply(t1));
        }

        default <V> CheckedFunction1<V, R> compose(Function<? super V, ? extends T1> before) {
            Objects.requireNonNull(before);
            return v -> apply(before.apply(v));
        }
    }

    /**
     * Alias for {@link X1}.
     * @param <T1> Argument type 1
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction1<T1, R> extends X1<T1, R> {

        static <T> CheckedFunction1<T, T> identity() {
            return t -> t;
        }
    }

    @FunctionalInterface
    public static interface λ1<T1, R> extends CheckedFunction1<T1, R>, java.util.function.Function<T1, R> {

        static <T> Function1<T, T> identity() {
            return t -> t;
        }

        @Override
        R apply(T1 t1) throws RuntimeException;

        @Override
        default Function1<T1, R> curried() {
            //noinspection Convert2MethodRef
            return t1 -> apply(t1);
        }

        @Override
        default Function1<Tuple1<T1>, R> tupled() {
            return t -> apply(t._1);
        }

        @Override
        default <V> Function1<T1, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return t1 -> Try.of(() -> after.apply(apply(t1))).orElseThrow(RuntimeException::new);
        }

        @Override
        default <V> Function1<V, R> compose(Function<? super V, ? extends T1> before) {
            Objects.requireNonNull(before);
            return v -> Try.of(() -> apply(before.apply(v))).orElseThrow(RuntimeException::new);
        }
    }

    /**
     * Alias for {@link λ1}.
     * @param <T1> Argument type 1
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function1<T1, R> extends λ1<T1, R> {

        static <T> Function1<T, T> identity() {
            return t -> t;
        }
    }

    @FunctionalInterface
    public static interface X2<T1, T2, R> extends λ<R> {

        R apply(T1 t1, T2 t2) throws Throwable;

        @Override
        default int arity() {
            return 2;
        }

        @Override
        default CheckedFunction1<T1, ? extends CheckedFunction1<T2, R>> curried() {
            return t1 -> t2 -> apply(t1, t2);
        }

        @Override
        default CheckedFunction1<Tuple2<T1, T2>, R> tupled() {
            return t -> apply(t._1, t._2);
        }

        @Override
        default <V> CheckedFunction2<T1, T2, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2) -> after.apply(apply(t1, t2));
        }
    }

    /**
     * Alias for {@link X2}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction2<T1, T2, R> extends X2<T1, T2, R> {
    }

    @FunctionalInterface
    public static interface λ2<T1, T2, R> extends CheckedFunction2<T1, T2, R>, java.util.function.BiFunction<T1, T2, R> {

        @Override
        R apply(T1 t1, T2 t2) throws RuntimeException;

        @Override
        default Function1<T1, Function1<T2, R>> curried() {
            return t1 -> t2 -> apply(t1, t2);
        }

        @Override
        default Function1<Tuple2<T1, T2>, R> tupled() {
            return t -> apply(t._1, t._2);
        }

        @Override
        default <V> Function2<T1, T2, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2) -> Try.of(() -> after.apply(apply(t1, t2))).orElseThrow(RuntimeException::new);
        }
    }

    /**
     * Alias for {@link λ2}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function2<T1, T2, R> extends λ2<T1, T2, R> {
    }

    @FunctionalInterface
    public static interface X3<T1, T2, T3, R> extends λ<R> {

        R apply(T1 t1, T2 t2, T3 t3) throws Throwable;

        @Override
        default int arity() {
            return 3;
        }

        @Override
        default CheckedFunction1<T1, ? extends CheckedFunction1<T2, ? extends CheckedFunction1<T3, R>>> curried() {
            return t1 -> t2 -> t3 -> apply(t1, t2, t3);
        }

        @Override
        default CheckedFunction1<Tuple3<T1, T2, T3>, R> tupled() {
            return t -> apply(t._1, t._2, t._3);
        }

        @Override
        default <V> CheckedFunction3<T1, T2, T3, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3) -> after.apply(apply(t1, t2, t3));
        }
    }

    /**
     * Alias for {@link X3}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction3<T1, T2, T3, R> extends X3<T1, T2, T3, R> {
    }

    @FunctionalInterface
    public static interface λ3<T1, T2, T3, R> extends CheckedFunction3<T1, T2, T3, R> {

        @Override
        R apply(T1 t1, T2 t2, T3 t3) throws RuntimeException;

        @Override
        default Function1<T1, Function1<T2, Function1<T3, R>>> curried() {
            return t1 -> t2 -> t3 -> apply(t1, t2, t3);
        }

        @Override
        default Function1<Tuple3<T1, T2, T3>, R> tupled() {
            return t -> apply(t._1, t._2, t._3);
        }

        @Override
        default <V> Function3<T1, T2, T3, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3) -> Try.of(() -> after.apply(apply(t1, t2, t3))).orElseThrow(RuntimeException::new);
        }
    }

    /**
     * Alias for {@link λ3}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function3<T1, T2, T3, R> extends λ3<T1, T2, T3, R> {
    }

    @FunctionalInterface
    public static interface X4<T1, T2, T3, T4, R> extends λ<R> {

        R apply(T1 t1, T2 t2, T3 t3, T4 t4) throws Throwable;

        @Override
        default int arity() {
            return 4;
        }

        @Override
        default CheckedFunction1<T1, ? extends CheckedFunction1<T2, ? extends CheckedFunction1<T3, ? extends CheckedFunction1<T4, R>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> apply(t1, t2, t3, t4);
        }

        @Override
        default CheckedFunction1<Tuple4<T1, T2, T3, T4>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4);
        }

        @Override
        default <V> CheckedFunction4<T1, T2, T3, T4, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
        }
    }

    /**
     * Alias for {@link X4}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction4<T1, T2, T3, T4, R> extends X4<T1, T2, T3, T4, R> {
    }

    @FunctionalInterface
    public static interface λ4<T1, T2, T3, T4, R> extends CheckedFunction4<T1, T2, T3, T4, R> {

        @Override
        R apply(T1 t1, T2 t2, T3 t3, T4 t4) throws RuntimeException;

        @Override
        default Function1<T1, Function1<T2, Function1<T3, Function1<T4, R>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> apply(t1, t2, t3, t4);
        }

        @Override
        default Function1<Tuple4<T1, T2, T3, T4>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4);
        }

        @Override
        default <V> Function4<T1, T2, T3, T4, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4) -> Try.of(() -> after.apply(apply(t1, t2, t3, t4))).orElseThrow(RuntimeException::new);
        }
    }

    /**
     * Alias for {@link λ4}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function4<T1, T2, T3, T4, R> extends λ4<T1, T2, T3, T4, R> {
    }

    @FunctionalInterface
    public static interface X5<T1, T2, T3, T4, T5, R> extends λ<R> {

        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) throws Throwable;

        @Override
        default int arity() {
            return 5;
        }

        @Override
        default CheckedFunction1<T1, ? extends CheckedFunction1<T2, ? extends CheckedFunction1<T3, ? extends CheckedFunction1<T4, ? extends CheckedFunction1<T5, R>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> apply(t1, t2, t3, t4, t5);
        }

        @Override
        default CheckedFunction1<Tuple5<T1, T2, T3, T4, T5>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5);
        }

        @Override
        default <V> CheckedFunction5<T1, T2, T3, T4, T5, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5) -> after.apply(apply(t1, t2, t3, t4, t5));
        }
    }

    /**
     * Alias for {@link X5}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction5<T1, T2, T3, T4, T5, R> extends X5<T1, T2, T3, T4, T5, R> {
    }

    @FunctionalInterface
    public static interface λ5<T1, T2, T3, T4, T5, R> extends CheckedFunction5<T1, T2, T3, T4, T5, R> {

        @Override
        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) throws RuntimeException;

        @Override
        default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, R>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> apply(t1, t2, t3, t4, t5);
        }

        @Override
        default Function1<Tuple5<T1, T2, T3, T4, T5>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5);
        }

        @Override
        default <V> Function5<T1, T2, T3, T4, T5, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5) -> Try.of(() -> after.apply(apply(t1, t2, t3, t4, t5))).orElseThrow(RuntimeException::new);
        }
    }

    /**
     * Alias for {@link λ5}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function5<T1, T2, T3, T4, T5, R> extends λ5<T1, T2, T3, T4, T5, R> {
    }

    @FunctionalInterface
    public static interface X6<T1, T2, T3, T4, T5, T6, R> extends λ<R> {

        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) throws Throwable;

        @Override
        default int arity() {
            return 6;
        }

        @Override
        default CheckedFunction1<T1, ? extends CheckedFunction1<T2, ? extends CheckedFunction1<T3, ? extends CheckedFunction1<T4, ? extends CheckedFunction1<T5, ? extends CheckedFunction1<T6, R>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> apply(t1, t2, t3, t4, t5, t6);
        }

        @Override
        default CheckedFunction1<Tuple6<T1, T2, T3, T4, T5, T6>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6);
        }

        @Override
        default <V> CheckedFunction6<T1, T2, T3, T4, T5, T6, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6) -> after.apply(apply(t1, t2, t3, t4, t5, t6));
        }
    }

    /**
     * Alias for {@link X6}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction6<T1, T2, T3, T4, T5, T6, R> extends X6<T1, T2, T3, T4, T5, T6, R> {
    }

    @FunctionalInterface
    public static interface λ6<T1, T2, T3, T4, T5, T6, R> extends CheckedFunction6<T1, T2, T3, T4, T5, T6, R> {

        @Override
        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) throws RuntimeException;

        @Override
        default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, R>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> apply(t1, t2, t3, t4, t5, t6);
        }

        @Override
        default Function1<Tuple6<T1, T2, T3, T4, T5, T6>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6);
        }

        @Override
        default <V> Function6<T1, T2, T3, T4, T5, T6, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6) -> Try.of(() -> after.apply(apply(t1, t2, t3, t4, t5, t6))).orElseThrow(RuntimeException::new);
        }
    }

    /**
     * Alias for {@link λ6}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function6<T1, T2, T3, T4, T5, T6, R> extends λ6<T1, T2, T3, T4, T5, T6, R> {
    }

    @FunctionalInterface
    public static interface X7<T1, T2, T3, T4, T5, T6, T7, R> extends λ<R> {

        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) throws Throwable;

        @Override
        default int arity() {
            return 7;
        }

        @Override
        default CheckedFunction1<T1, ? extends CheckedFunction1<T2, ? extends CheckedFunction1<T3, ? extends CheckedFunction1<T4, ? extends CheckedFunction1<T5, ? extends CheckedFunction1<T6, ? extends CheckedFunction1<T7, R>>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> apply(t1, t2, t3, t4, t5, t6, t7);
        }

        @Override
        default CheckedFunction1<Tuple7<T1, T2, T3, T4, T5, T6, T7>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7);
        }

        @Override
        default <V> CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6, t7) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7));
        }
    }

    /**
     * Alias for {@link X7}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <T7> Argument type 7
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, R> extends X7<T1, T2, T3, T4, T5, T6, T7, R> {
    }

    @FunctionalInterface
    public static interface λ7<T1, T2, T3, T4, T5, T6, T7, R> extends CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, R> {

        @Override
        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) throws RuntimeException;

        @Override
        default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, R>>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> apply(t1, t2, t3, t4, t5, t6, t7);
        }

        @Override
        default Function1<Tuple7<T1, T2, T3, T4, T5, T6, T7>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7);
        }

        @Override
        default <V> Function7<T1, T2, T3, T4, T5, T6, T7, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6, t7) -> Try.of(() -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7))).orElseThrow(RuntimeException::new);
        }
    }

    /**
     * Alias for {@link λ7}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <T7> Argument type 7
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function7<T1, T2, T3, T4, T5, T6, T7, R> extends λ7<T1, T2, T3, T4, T5, T6, T7, R> {
    }

    @FunctionalInterface
    public static interface X8<T1, T2, T3, T4, T5, T6, T7, T8, R> extends λ<R> {

        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) throws Throwable;

        @Override
        default int arity() {
            return 8;
        }

        @Override
        default CheckedFunction1<T1, ? extends CheckedFunction1<T2, ? extends CheckedFunction1<T3, ? extends CheckedFunction1<T4, ? extends CheckedFunction1<T5, ? extends CheckedFunction1<T6, ? extends CheckedFunction1<T7, ? extends CheckedFunction1<T8, R>>>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> apply(t1, t2, t3, t4, t5, t6, t7, t8);
        }

        @Override
        default CheckedFunction1<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8);
        }

        @Override
        default <V> CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6, t7, t8) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8));
        }
    }

    /**
     * Alias for {@link X8}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <T7> Argument type 7
     * @param <T8> Argument type 8
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, R> extends X8<T1, T2, T3, T4, T5, T6, T7, T8, R> {
    }

    @FunctionalInterface
    public static interface λ8<T1, T2, T3, T4, T5, T6, T7, T8, R> extends CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, R> {

        @Override
        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) throws RuntimeException;

        @Override
        default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, R>>>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> apply(t1, t2, t3, t4, t5, t6, t7, t8);
        }

        @Override
        default Function1<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8);
        }

        @Override
        default <V> Function8<T1, T2, T3, T4, T5, T6, T7, T8, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6, t7, t8) -> Try.of(() -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8))).orElseThrow(RuntimeException::new);
        }
    }

    /**
     * Alias for {@link λ8}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <T7> Argument type 7
     * @param <T8> Argument type 8
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> extends λ8<T1, T2, T3, T4, T5, T6, T7, T8, R> {
    }

    @FunctionalInterface
    public static interface X9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> extends λ<R> {

        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) throws Throwable;

        @Override
        default int arity() {
            return 9;
        }

        @Override
        default CheckedFunction1<T1, ? extends CheckedFunction1<T2, ? extends CheckedFunction1<T3, ? extends CheckedFunction1<T4, ? extends CheckedFunction1<T5, ? extends CheckedFunction1<T6, ? extends CheckedFunction1<T7, ? extends CheckedFunction1<T8, ? extends CheckedFunction1<T9, R>>>>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9);
        }

        @Override
        default CheckedFunction1<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9);
        }

        @Override
        default <V> CheckedFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9));
        }
    }

    /**
     * Alias for {@link X9}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <T7> Argument type 7
     * @param <T8> Argument type 8
     * @param <T9> Argument type 9
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> extends X9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> {
    }

    @FunctionalInterface
    public static interface λ9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> extends CheckedFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> {

        @Override
        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) throws RuntimeException;

        @Override
        default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, R>>>>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9);
        }

        @Override
        default Function1<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9);
        }

        @Override
        default <V> Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> Try.of(() -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9))).orElseThrow(RuntimeException::new);
        }
    }

    /**
     * Alias for {@link λ9}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <T7> Argument type 7
     * @param <T8> Argument type 8
     * @param <T9> Argument type 9
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> extends λ9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> {
    }

    @FunctionalInterface
    public static interface X10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> extends λ<R> {

        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) throws Throwable;

        @Override
        default int arity() {
            return 10;
        }

        @Override
        default CheckedFunction1<T1, ? extends CheckedFunction1<T2, ? extends CheckedFunction1<T3, ? extends CheckedFunction1<T4, ? extends CheckedFunction1<T5, ? extends CheckedFunction1<T6, ? extends CheckedFunction1<T7, ? extends CheckedFunction1<T8, ? extends CheckedFunction1<T9, ? extends CheckedFunction1<T10, R>>>>>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> apply(t1, t2, t3, t4, t5, t6, t7, t8,
                    t9, t10);
        }

        @Override
        default CheckedFunction1<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10);
        }

        @Override
        default <V> CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
        }
    }

    /**
     * Alias for {@link X10}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <T7> Argument type 7
     * @param <T8> Argument type 8
     * @param <T9> Argument type 9
     * @param <T10> Argument type 10
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> extends X10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> {
    }

    @FunctionalInterface
    public static interface λ10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> extends CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> {

        @Override
        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) throws RuntimeException;

        @Override
        default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, Function1<T10, R>>>>>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> apply(t1, t2, t3, t4, t5, t6, t7, t8,
                    t9, t10);
        }

        @Override
        default Function1<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10);
        }

        @Override
        default <V> Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> Try.of(() -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10))).orElseThrow(RuntimeException::new);
        }
    }

    /**
     * Alias for {@link λ10}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <T7> Argument type 7
     * @param <T8> Argument type 8
     * @param <T9> Argument type 9
     * @param <T10> Argument type 10
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> extends λ10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> {
    }

    @FunctionalInterface
    public static interface X11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> extends λ<R> {

        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) throws Throwable;

        @Override
        default int arity() {
            return 11;
        }

        @Override
        default CheckedFunction1<T1, ? extends CheckedFunction1<T2, ? extends CheckedFunction1<T3, ? extends CheckedFunction1<T4, ? extends CheckedFunction1<T5, ? extends CheckedFunction1<T6, ? extends CheckedFunction1<T7, ? extends CheckedFunction1<T8, ? extends CheckedFunction1<T9, ? extends CheckedFunction1<T10, ? extends CheckedFunction1<T11, R>>>>>>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> apply(t1, t2, t3, t4, t5, t6,
                    t7, t8, t9, t10, t11);
        }

        @Override
        default CheckedFunction1<Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11);
        }

        @Override
        default <V> CheckedFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
        }
    }

    /**
     * Alias for {@link X11}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <T7> Argument type 7
     * @param <T8> Argument type 8
     * @param <T9> Argument type 9
     * @param <T10> Argument type 10
     * @param <T11> Argument type 11
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> extends X11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> {
    }

    @FunctionalInterface
    public static interface λ11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> extends CheckedFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> {

        @Override
        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) throws RuntimeException;

        @Override
        default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, Function1<T10, Function1<T11, R>>>>>>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> apply(t1, t2, t3, t4, t5, t6,
                    t7, t8, t9, t10, t11);
        }

        @Override
        default Function1<Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11);
        }

        @Override
        default <V> Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> Try.of(() -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11))).orElseThrow(RuntimeException::new);
        }
    }

    /**
     * Alias for {@link λ11}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <T7> Argument type 7
     * @param <T8> Argument type 8
     * @param <T9> Argument type 9
     * @param <T10> Argument type 10
     * @param <T11> Argument type 11
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> extends λ11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> {
    }

    @FunctionalInterface
    public static interface X12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> extends λ<R> {

        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) throws Throwable;

        @Override
        default int arity() {
            return 12;
        }

        @Override
        default CheckedFunction1<T1, ? extends CheckedFunction1<T2, ? extends CheckedFunction1<T3, ? extends CheckedFunction1<T4, ? extends CheckedFunction1<T5, ? extends CheckedFunction1<T6, ? extends CheckedFunction1<T7, ? extends CheckedFunction1<T8, ? extends CheckedFunction1<T9, ? extends CheckedFunction1<T10, ? extends CheckedFunction1<T11, ? extends CheckedFunction1<T12, R>>>>>>>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> apply(t1, t2, t3, t4, t5,
                    t6, t7, t8, t9, t10, t11, t12);
        }

        @Override
        default CheckedFunction1<Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12);
        }

        @Override
        default <V> CheckedFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
        }
    }

    @FunctionalInterface
    public static interface λ12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> extends CheckedFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> {

        @Override
        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) throws RuntimeException;

        @Override
        default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, Function1<T10, Function1<T11, Function1<T12, R>>>>>>>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> apply(t1, t2, t3, t4, t5,
                    t6, t7, t8, t9, t10, t11, t12);
        }

        @Override
        default Function1<Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12);
        }

        @Override
        default <V> Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> Try.of(() -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12))).orElseThrow(RuntimeException::new);
        }
    }

    /**
     * Alias for {@link λ12}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <T7> Argument type 7
     * @param <T8> Argument type 8
     * @param <T9> Argument type 9
     * @param <T10> Argument type 10
     * @param <T11> Argument type 11
     * @param <T12> Argument type 12
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> extends λ12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> {
    }

    /**
     * Alias for {@link X12}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <T7> Argument type 7
     * @param <T8> Argument type 8
     * @param <T9> Argument type 9
     * @param <T10> Argument type 10
     * @param <T11> Argument type 11
     * @param <T12> Argument type 12
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> extends X12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> {
    }

    @FunctionalInterface
    public static interface X13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> extends λ<R> {

        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) throws Throwable;

        @Override
        default int arity() {
            return 13;
        }

        @Override
        default CheckedFunction1<T1, ? extends CheckedFunction1<T2, ? extends CheckedFunction1<T3, ? extends CheckedFunction1<T4, ? extends CheckedFunction1<T5, ? extends CheckedFunction1<T6, ? extends CheckedFunction1<T7, ? extends CheckedFunction1<T8, ? extends CheckedFunction1<T9, ? extends CheckedFunction1<T10, ? extends CheckedFunction1<T11, ? extends CheckedFunction1<T12, ? extends CheckedFunction1<T13, R>>>>>>>>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> apply(t1, t2, t3,
                    t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
        }

        @Override
        default CheckedFunction1<Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13);
        }

        @Override
        default <V> CheckedFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
        }
    }

    /**
     * Alias for {@link X13}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <T7> Argument type 7
     * @param <T8> Argument type 8
     * @param <T9> Argument type 9
     * @param <T10> Argument type 10
     * @param <T11> Argument type 11
     * @param <T12> Argument type 12
     * @param <T13> Argument type 13
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> extends X13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> {
    }

    @FunctionalInterface
    public static interface λ13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> extends CheckedFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> {

        @Override
        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) throws RuntimeException;

        @Override
        default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, Function1<T10, Function1<T11, Function1<T12, Function1<T13, R>>>>>>>>>>>>> curried() {
            return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> apply(t1, t2, t3,
                    t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
        }

        @Override
        default Function1<Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>, R> tupled() {
            return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13);
        }

        @Override
        default <V> Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) -> Try.of(() -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13))).orElseThrow(RuntimeException::new);
        }
    }

    /**
     * Alias for {@link λ13}.
     * @param <T1> Argument type 1
     * @param <T2> Argument type 2
     * @param <T3> Argument type 3
     * @param <T4> Argument type 4
     * @param <T5> Argument type 5
     * @param <T6> Argument type 6
     * @param <T7> Argument type 7
     * @param <T8> Argument type 8
     * @param <T9> Argument type 9
     * @param <T10> Argument type 10
     * @param <T11> Argument type 11
     * @param <T12> Argument type 12
     * @param <T13> Argument type 13
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> extends λ13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> {
    }

    /**
     * Checked version of java.util.function.BiConsumer.
     * Essentially the same as {@code CheckedFunction2<T, U, Void>}, or short {@code X2<T, U, Void>}.
     *
     * @param <T> First argument type
     * @param <U> Second argument type
     */
    @FunctionalInterface
    public static interface CheckedBiConsumer<T, U> extends Serializable {

        void accept(T t, U u) throws Throwable;

        default CheckedBiConsumer<T,U>	andThen(CheckedBiConsumer<? super T,? super U> after) {
            Objects.requireNonNull(after);
            return (l, r) -> {
                accept(l, r);
                after.accept(l, r);
            };
        }
    }

    /**
     * Checked version of java.util.function.BiFunction.
     * Essentially the same as {@code CheckedFunction2<T, U, R>}, or short {@code X2<T, U, R>}.
     *
     * @param <T> First argument type
     * @param <U> Second argument type
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface CheckedBiFunction<T, U, R> extends CheckedFunction2<T, U, R> {
    }

    /**
     * Checked version of java.util.function.BinaryOperator.
     * Essentially the same as {@code CheckedFunction2<T, T, T>}, or short {@code X2<T, T, T>}.
     *
     * @param <T> Operand type
     */
    @FunctionalInterface
    public static interface CheckedBinaryOperator<T> extends CheckedBiFunction<T, T, T> {
    }

    /**
     * Checked version of java.util.function.BiPredicate.
     * Essentially the same as {@code CheckedFunction2<T, U, Boolean>}, or short {@code X2<T, U, Boolean>}.
     *
     * @param <T> First argument type
     * @param <U> Second argument type
     */
    @FunctionalInterface
    public static interface CheckedBiPredicate<T, U> extends Serializable {

        boolean test(T t, U u) throws Throwable;

        default CheckedBiPredicate<T, U> and(CheckedBiPredicate<? super T, ? super U> other) {
            Objects.requireNonNull(other);
            return (T t, U u) -> test(t, u) && other.test(t, u);
        }

        default CheckedBiPredicate<T, U> negate() {
            return (T t, U u) -> !test(t, u);
        }

        default CheckedBiPredicate<T, U> or(CheckedBiPredicate<? super T, ? super U> other) {
            Objects.requireNonNull(other);
            return (T t, U u) -> test(t, u) || other.test(t, u);
        }
    }

    /**
     * Checked version of java.util.function.BooleanSupplier.
     * Essentially the same as {@code CheckedFunction0<Boolean>}, or short {@code X0<Boolean>}.
     */
    @FunctionalInterface
    public static interface CheckedBooleanSupplier extends Serializable {

        boolean getAsBoolean() throws Throwable;
    }

    /**
     * Checked version of java.util.function.Consumer.
     * Essentially the same as {@code CheckedFunction1<T, Void>}, or short {@code X1<T, Void>}.
     *
     * @param <T> Argument type
     */
    @FunctionalInterface
    public static interface CheckedConsumer<T> extends Serializable {

        void accept(T t) throws Throwable;

        default CheckedConsumer<T> andThen(CheckedConsumer<? super T> after) {
            Objects.requireNonNull(after);
            return (T t) -> { accept(t); after.accept(t); };
        }
    }

    /**
     * Checked version of java.util.function.DoubleBinaryOperator.
     * Essentially the same as {@code CheckedFunction2<Double, Double, Double>}, or short {@code X2<Double, Double, Double>}.
     */
    @FunctionalInterface
    public static interface CheckedDoubleBinaryOperator extends Serializable {

        double applyAsDouble(double left, double right) throws Throwable;
    }

    /**
     * Checked version of java.util.function.DoubleConsumer.
     * Essentially the same as {@code CheckedFunction1<Double, Void>}, or short {@code X1<Double, Void>}.
     */
    @FunctionalInterface
    public static interface CheckedDoubleConsumer extends Serializable {

        void accept(double value) throws Throwable;

        default CheckedDoubleConsumer andThen(CheckedDoubleConsumer after) {
            Objects.requireNonNull(after);
            return (double t) -> { accept(t); after.accept(t); };
        }
    }

    /**
     * Checked version of java.util.function.DoubleFunction.
     * Essentially the same as {@code CheckedFunction1<Double, R>}, or short {@code X1<Double, R>}.
     *
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface CheckedDoubleFunction<R> extends Serializable {

        R apply(double value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.DoublePredicate.
     * Essentially the same as {@code CheckedFunction1<Double, Boolean>}, or short {@code X1<Double, Boolean>}.
     */
    @FunctionalInterface
    public static interface CheckedDoublePredicate extends Serializable {

        boolean test(double value) throws Throwable;

        default CheckedDoublePredicate and(CheckedDoublePredicate other) {
            Objects.requireNonNull(other);
            return (value) -> test(value) && other.test(value);
        }

        default CheckedDoublePredicate negate() {
            return (value) -> !test(value);
        }

        default CheckedDoublePredicate or(CheckedDoublePredicate other) {
            Objects.requireNonNull(other);
            return (value) -> test(value) || other.test(value);
        }
    }

    /**
     * Checked version of java.util.function.DoubleSupplier.
     * Essentially the same as {@code CheckedFunction0<Double>}, or short {@code X0<Double>}.
     */
    @FunctionalInterface
    public static interface CheckedDoubleSupplier extends Serializable {

        double getAsDouble() throws Throwable;
    }

    /**
     * Checked version of java.util.function.DoubleToIntFunction.
     * Essentially the same as {@code CheckedFunction1<Double, Integer>}, or short {@code X1<Double, Integer>}.
     */
    @FunctionalInterface
    public static interface CheckedDoubleToIntFunction extends Serializable {

        int applyAsInt(double value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.DoubleToLongFunction.
     * Essentially the same as {@code CheckedFunction1<Double, Long>}, or short {@code X1<Double, Long>}.
     */
    @FunctionalInterface
    public static interface CheckedDoubleToLongFunction extends Serializable {

        long applyAsLong(double value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.DoubleUnaryOperator.
     * Essentially the same as {@code CheckedFunction1<Double, Double>}, or short {@code X1<Double, Double>}.
     */
    @FunctionalInterface
    public static interface CheckedDoubleUnaryOperator extends Serializable {

        double applyAsDouble(double operand) throws Throwable;

        default CheckedDoubleUnaryOperator compose(CheckedDoubleUnaryOperator before) {
            Objects.requireNonNull(before);
            return (double v) -> applyAsDouble(before.applyAsDouble(v));
        }

        default CheckedDoubleUnaryOperator andThen(CheckedDoubleUnaryOperator after) {
            Objects.requireNonNull(after);
            return (double t) -> after.applyAsDouble(applyAsDouble(t));
        }

        static CheckedDoubleUnaryOperator identity() {
            return t -> t;
        }
    }

    /**
     * Checked version of java.util.function.Function.
     * Essentially the same as {@code CheckedFunction1<T, R>}, or short {@code X1<T, R>}.
     *
     * @param <T> Argument type
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface CheckedFunction<T, R> extends CheckedFunction1<T, R> {

        static <T> CheckedFunction<T, T> identity() {
            return t -> t;
        }
    }

    /**
     * Checked version of java.util.function.IntBinaryOperator.
     * Essentially the same as {@code CheckedFunction2<Integer, Integer, Integer>}, or short {@code X2<Integer, Integer, Integer>}.
     */
    @FunctionalInterface
    public static interface CheckedIntBinaryOperator extends Serializable {

        int applyAsInt(int left, int right) throws Throwable;
    }

    /**
     * Checked version of java.util.function.IntConsumer.
     * Essentially the same as {@code CheckedFunction1<Integer, Void>}, or short {@code X1<Integer, Void>}.
     */
    @FunctionalInterface
    public static interface CheckedIntConsumer extends Serializable {

        void accept(int value) throws Throwable;

        default CheckedIntConsumer andThen(CheckedIntConsumer after) {
            Objects.requireNonNull(after);
            return (int t) -> { accept(t); after.accept(t); };
        }
    }

    /**
     * Checked version of java.util.function.IntFunction.
     * Essentially the same as {@code CheckedFunction1<Integer, R>}, or short {@code X1<Integer, R>}.
     *
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface CheckedIntFunction<R> extends Serializable {

        R apply(int value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.IntPredicate.
     * Essentially the same as {@code CheckedFunction1<Integer, Boolean>}, or short {@code X1<Integer, Boolean>}.
     */
    @FunctionalInterface
    public static interface CheckedIntPredicate extends Serializable {

        boolean test(int value) throws Throwable;

        default CheckedIntPredicate and(CheckedIntPredicate other) {
            Objects.requireNonNull(other);
            return (value) -> test(value) && other.test(value);
        }

        default CheckedIntPredicate negate() {
            return (value) -> !test(value);
        }

        default CheckedIntPredicate or(CheckedIntPredicate other) {
            Objects.requireNonNull(other);
            return (value) -> test(value) || other.test(value);
        }
    }

    /**
     * Checked version of java.util.function.IntSupplier.
     * Essentially the same as {@code CheckedFunction0<Integer>}, or short {@code X0<Integer>}.
     */
    @FunctionalInterface
    public static interface CheckedIntSupplier extends Serializable {

        int getAsInt() throws Throwable;
    }

    /**
     * Checked version of java.util.function.IntToDoubleFunction.
     * Essentially the same as {@code CheckedFunction1<Integer, Double>}, or short {@code X1<Integer, Double>}.
     */
    @FunctionalInterface
    public static interface CheckedIntToDoubleFunction extends Serializable {

        double applyAsDouble(int value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.IntToLongFunction.
     * Essentially the same as {@code CheckedFunction1<Integer, Long>}, or short {@code X1<Integer, Long>}.
     */
    @FunctionalInterface
    public static interface CheckedIntToLongFunction extends Serializable {

        long applyAsLong(int value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.IntUnaryOperator.
     * Essentially the same as {@code CheckedFunction1<Integer, Integer>}, or short {@code X1<Integer, Integer>}.
     */
    @FunctionalInterface
    public static interface CheckedIntUnaryOperator extends Serializable {

        int applyAsInt(int operand) throws Throwable;

        default CheckedIntUnaryOperator compose(CheckedIntUnaryOperator before) {
            Objects.requireNonNull(before);
            return (int v) -> applyAsInt(before.applyAsInt(v));
        }

        default CheckedIntUnaryOperator andThen(CheckedIntUnaryOperator after) {
            Objects.requireNonNull(after);
            return (int t) -> after.applyAsInt(applyAsInt(t));
        }

        static CheckedIntUnaryOperator identity() {
            return t -> t;
        }
    }

    /**
     * Checked version of java.util.function.LongBinaryOperator.
     * Essentially the same as {@code CheckedFunction2<Long, Long, Long>}, or short {@code X2<Long, Long, Long>}.
     */
    @FunctionalInterface
    public static interface CheckedLongBinaryOperator extends Serializable {

        long applyAsInt(long left, long right) throws Throwable;
    }

    /**
     * Checked version of java.util.function.LongConsumer.
     * Essentially the same as {@code CheckedFunction1<Long, Void>}, or short {@code X1<Long, Void>}.
     */
    @FunctionalInterface
    public static interface CheckedLongConsumer extends Serializable {

        void accept(long value) throws Throwable;

        default CheckedLongConsumer andThen(CheckedLongConsumer after) {
            Objects.requireNonNull(after);
            return (long t) -> { accept(t); after.accept(t); };
        }
    }

    /**
     * Checked version of java.util.function.LongFunction.
     * Essentially the same as {@code CheckedFunction1<Long, R>}, or short {@code X1<Long, R>}.
     *
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface CheckedLongFunction<R> extends Serializable {

        R apply(long value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.LongPredicate.
     * Essentially the same as {@code CheckedFunction1<Long, Boolean>}, or short {@code X1<Long, Boolean>}.
     */
    @FunctionalInterface
    public static interface CheckedLongPredicate extends Serializable {

        boolean test(long value) throws Throwable;

        default CheckedLongPredicate and(CheckedLongPredicate other) {
            Objects.requireNonNull(other);
            return (value) -> test(value) && other.test(value);
        }

        default CheckedLongPredicate negate() {
            return (value) -> !test(value);
        }

        default CheckedLongPredicate or(CheckedLongPredicate other) {
            Objects.requireNonNull(other);
            return (value) -> test(value) || other.test(value);
        }
    }

    /**
     * Checked version of java.util.function.LongSupplier.
     * Essentially the same as {@code CheckedFunction0<Long>}, or short {@code X0<Long>}.
     */
    @FunctionalInterface
    public static interface CheckedLongSupplier extends Serializable {

        long getAsLong() throws Throwable;
    }

    /**
     * Checked version of java.util.function.LongToDoubleFunction.
     * Essentially the same as {@code CheckedFunction1<Long, Double>}, or short {@code X1<Long, Double>}.
     */
    @FunctionalInterface
    public static interface CheckedLongToDoubleFunction extends Serializable {

        double applyAsDouble(long value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.LongToIntFunction.
     * Essentially the same as {@code CheckedFunction1<Long, Integer>}, or short {@code X1<Long, Integer>}.
     */
    @FunctionalInterface
    public static interface CheckedLongToIntFunction extends Serializable {

        int applyAsInt(long value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.LongUnaryOperator.
     * Essentially the same as {@code CheckedFunction1<Long, Long>}, or short {@code X1<Long, Long>}.
     */
    @FunctionalInterface
    public static interface CheckedLongUnaryOperator extends Serializable {

        long applyAsLong(long operand) throws Throwable;

        default CheckedLongUnaryOperator compose(CheckedLongUnaryOperator before) {
            Objects.requireNonNull(before);
            return (long v) -> applyAsLong(before.applyAsLong(v));
        }

        default CheckedLongUnaryOperator andThen(CheckedLongUnaryOperator after) {
            Objects.requireNonNull(after);
            return (long t) -> after.applyAsLong(applyAsLong(t));
        }

        static CheckedLongUnaryOperator identity() {
            return t -> t;
        }
    }

    /**
     * Checked version of java.util.function.ObjDoubleConsumer.
     * Essentially the same as {@code CheckedFunction2<T, Double, Void>}, or short {@code X2<T, Double, Void>}.
     *
     * @param <T> Argument type
     */
    @FunctionalInterface
    public static interface CheckedObjDoubleConsumer<T> extends Serializable {

        void accept(T t, double value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.ObjIntConsumer.
     * Essentially the same as {@code CheckedFunction2<T, Integer, Void>}, or short {@code X2<T, Integer, Void>}.
     *
     * @param <T> Argument type
     */
    @FunctionalInterface
    public static interface CheckedObjIntConsumer<T> extends Serializable {

        void accept(T t, int value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.ObjLongConsumer.
     * Essentially the same as {@code CheckedFunction2<T, Long, Void>}, or short {@code X2<T, Long, Void>}.
     *
     * @param <T> Argument type
     */
    @FunctionalInterface
    public static interface CheckedObjLongConsumer<T> extends Serializable {

        void accept(T t, long value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.Predicate.
     * Essentially the same as {@code CheckedFunction1<T, Boolean>}, or short {@code X1<T, Boolean>}.
     *
     * @param <T> Argument type
     */
    @FunctionalInterface
    public static interface CheckedPredicate<T> extends Serializable {

        static <T> CheckedPredicate<T> isEqual(Object targetRef) {
            return (null == targetRef)
                    ? Objects::isNull
                    : targetRef::equals;
        }

        boolean test(T t) throws Throwable;

        default CheckedPredicate<T> and(CheckedPredicate<? super T> other) {
            Objects.requireNonNull(other);
            return (t) -> test(t) && other.test(t);
        }

        default CheckedPredicate<T> negate() {
            return (t) -> !test(t);
        }

        default CheckedPredicate<T> or(CheckedPredicate<? super T> other) {
            Objects.requireNonNull(other);
            return (t) -> test(t) || other.test(t);
        }
    }

    /**
     * Checked version of java.util.function.Supplier.
     * Essentially the same as {@code CheckedFunction0<R>}, or short {@code X0<R>}.
     *
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedSupplier<R> extends Serializable {

        R get() throws Throwable;
    }

    /**
     * Checked version of java.util.function.ToDoubleBiFunction.
     * Essentially the same as {@code CheckedFunction2<T, U, Double>}, or short {@code X2<T, U, Double>}.
     *
     * @param <T> First argument type
     * @param <U> Second argument type
     */
    @FunctionalInterface
    public static interface CheckedToDoubleBiFunction<T, U> extends Serializable {

        double applyAsDouble(T t, U u) throws Throwable;
    }

    /**
     * Checked version of java.util.function.ToDoubleFunction.
     * Essentially the same as {@code CheckedFunction1<T, Double>}, or short {@code X1<T, Double>}.
     *
     * @param <T> Argument type
     */
    @FunctionalInterface
    public static interface CheckedToDoubleFunction<T> extends Serializable {

        double applyAsDouble(T value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.ToIntBiFunction.
     * Essentially the same as {@code CheckedFunction2<T, U, Integer>}, or short {@code X2<T, U, Integer>}.
     *
     * @param <T> First argument type
     * @param <U> Second argument type
     */
    @FunctionalInterface
    public static interface CheckedToIntBiFunction<T, U> extends Serializable {

        int applyAsInt(T t, U u) throws Throwable;
    }

    /**
     * Checked version of java.util.function.ToIntFunction.
     * Essentially the same as {@code CheckedFunction1<T, Integer>}, or short {@code X1<T, Integer>}.
     *
     * @param <T> Argument type
     */
    @FunctionalInterface
    public static interface CheckedToIntFunction<T> extends Serializable {

        int applyAsInt(T value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.ToLongBiFunction.
     * Essentially the same as {@code CheckedFunction2<T, U, Long>}, or short {@code X2<T, U, Long>}.
     *
     * @param <T> First argument type
     * @param <U> Second argument type
     */
    @FunctionalInterface
    public static interface CheckedToLongBiFunction<T, U> extends Serializable {

        long applyAsLong(T t, U u) throws Throwable;
    }

    /**
     * Checked version of java.util.function.ToLongFunction.
     * Essentially the same as {@code CheckedFunction1<T, Long>}, or short {@code X1<T, Long>}.
     *
     * @param <T> Argument type
     */
    @FunctionalInterface
    public static interface CheckedToLongFunction<T> extends Serializable {

        long applyAsLong(T value) throws Throwable;
    }

    /**
     * Checked version of java.util.function.UnaryOperator.
     * Essentially the same as {@code CheckedFunction1<T, T>}, or short {@code X1<T, T>}.
     *
     * @param <T> Operand type
     */
    @FunctionalInterface
    public static interface CheckedUnaryOperator<T> extends CheckedFunction1<T, T> {

        static <T> CheckedUnaryOperator<T> identity() {
            return t -> t;
        }
    }

    /**
     * Checked version of java.lang.Runnable.
     * Essentially the same as {@code CheckedFunction0<Void>}, or short {@code X0<Void>}.
     */
    @FunctionalInterface
    public static interface CheckedRunnable extends Serializable {

        void run() throws Throwable;
    }

    /**
     * Unchecked boolean to R function.
     * Essentially the same as {@code Function1<Boolean, R>}, or short {@code λ1<Boolean, R>}.
     *
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface BooleanFunction<R> extends Serializable {

        R apply(boolean b);
    }

    /**
     * Checked version of java.util.function.BooleanFunction.
     * Essentially the same as {@code CheckedFunction1<Boolean, R>}, or short {@code X1<Boolean, R>}.
     *
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface CheckedBooleanFunction<R> extends Serializable {

        R apply(boolean b) throws Throwable;
    }

    /**
     * Unchecked byte to R function.
     * Essentially the same as {@code Function1<Byte, R>}, or short {@code λ1<Byte, R>}.
     *
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface ByteFunction<R> extends Serializable {

        R apply(byte b);
    }

    /**
     * Checked version of java.util.function.ByteFunction.
     * Essentially the same as {@code CheckedFunction1<Byte, R>}, or short {@code X1<Byte, R>}.
     *
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface CheckedByteFunction<R> extends Serializable {

        R apply(boolean b) throws Throwable;
    }

    /**
     * Unchecked char to R function.
     * Essentially the same as {@code Function1<Character, R>}, or short {@code λ1<Character, R>}.
     *
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface CharFunction<R> extends Serializable {

        R apply(char c);
    }

    /**
     * Checked version of java.util.function.CharFunction.
     * Essentially the same as {@code CheckedFunction1<Character, R>}, or short {@code X1<Character, R>}.
     *
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface CheckedCharFunction<R> extends Serializable {

        R apply(char c) throws Throwable;
    }

    /**
     * Unchecked float to R function.
     * Essentially the same as {@code Function1<Float, R>}, or short {@code λ1<Float, R>}.
     *
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface FloatFunction<R> extends Serializable {
        R apply(float f);
    }

    /**
     * Checked version of java.util.function.FloatFunction.
     * Essentially the same as {@code CheckedFunction1<Float, R>}, or short {@code X1<Float, R>}.
     *
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface CheckedFloatFunction<R> extends Serializable {

        R apply(float f) throws Throwable;
    }

    /**
     * Unchecked short to R function.
     * Essentially the same as {@code Function1<Short, R>}, or short {@code λ1<Short, R>}.
     *
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface ShortFunction<R> extends Serializable {
        R apply(short s);
    }

    /**
     * Checked version of java.util.function.ShortFunction.
     * Essentially the same as {@code CheckedFunction1<Short, R>}, or short {@code X1<Short, R>}.
     *
     * @param <R> Return value type
     */
    @FunctionalInterface
    public static interface CheckedShortFunction<R> extends Serializable {

        R apply(short s) throws Throwable;
    }
}
