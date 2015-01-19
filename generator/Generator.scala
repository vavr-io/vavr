/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.StandardOpenOption

import StringContextImplicits._

import scala.util.Properties.lineSeparator

val N = 13
val TARGET = "target/generated-sources"

// entry point
def run() {

  genJavaFile("javaslang", "Functions.java")(genFunctions)
  genJavaFile("javaslang", "Tuple.java")(genTuple)

}

def genFunctions(): String = {

  def genFunctions(i: Int): String = {

    val generics = gen(1 to i)(j => s"T$j")(", ")
    val genericsTuple = if (i > 0) s"<$generics>" else ""
    val genericsFunction = if (i > 0) s"$generics, " else ""
    val curried = if (i == 0) "v" else gen(1 to i)(j => s"t$j")(" -> ")
    val paramsDecl = gen(1 to i)(j => s"T$j t$j")(", ")
    val params = gen(1 to i)(j => s"t$j")(", ")
    val tupled = gen(1 to i)(j => s"t._$j")(", ")

    def additionalInterfaces(arity: Int): String = arity match {
      case 0 => s", java.util.function.Supplier<R>"
      case 1 => s", java.util.function.Function<$generics, R>"
      case 2 => s", java.util.function.BiFunction<$generics, R>"
      case _ => ""
    }

    def returnType(curr: Int, max: Int)(checked: Boolean): String = {
      val function = if (checked) "CheckedFunction" else "Function"
      if (curr == 1 && max == 0) {
          s"${function}1<Void, R>"
      } else {
          if (curr > max) throw new IllegalStateException("curr > max")
          val next = if (curr < max) returnType(curr + 1, max)(checked) else "R"
          val bound = if (curr < max && checked) "? extends " else ""
          s"${function}1<T$curr, $bound$next>"
      }
    }

    xs"""
    @FunctionalInterface
    public static interface X$i<${genericsFunction}R> extends λ<R> {

        ${if (i == 1) xs"""
        static <T> CheckedFunction1<T, T> identity() {
            return t -> t;
        }""" else ""}

        R apply($paramsDecl) throws Throwable;

        @Override
        default int arity() {
            return $i;
        }

        @Override
        default ${returnType(1, i)(checked = true)} curried() {
            return $curried -> apply($params);
        }

        @Override
        default CheckedFunction1<Tuple$i$genericsTuple, R> tupled() {
            return t -> apply($tupled);
        }

        @Override
        default <V> CheckedFunction$i<${genericsFunction}V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return ($params) -> after.apply(apply($params));
        }

        ${if (i == 1) xs"""
        default <V> CheckedFunction1<V, R> compose(Function<? super V, ? extends T1> before) {
            Objects.requireNonNull(before);
            return v -> apply(before.apply(v));
        }""" else ""}
    }

    /**
     * Alias for {@link X$i}.
     * ${gen(1 to i)(j => s"@param <T$j> Argument type $j")("\n      * ")}
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface CheckedFunction$i<${genericsFunction}R> extends X$i<${genericsFunction}R> {
        ${if (i == 1) xs"""
        static <T> CheckedFunction1<T, T> identity() {
            return t -> t;
        }""" else ""}
    }

    @FunctionalInterface
    public static interface λ$i<${if (i > 0) s"$generics, " else ""}R> extends CheckedFunction$i<${genericsFunction}R>${additionalInterfaces(i)} {

        ${if (i == 1) xs"""
        static <T> Function1<T, T> identity() {
            return t -> t;
        }""" else ""}

        @Override
        R apply($paramsDecl) throws RuntimeException;

        ${if (i == 0) xs"""
        @Override
        default R get() {
            return apply();
        }""" else ""}

        @Override
        default ${returnType(1, i)(checked = false)} curried() {
            return $curried -> apply($params);
        }

        @Override
        default Function1<Tuple$i$genericsTuple, R> tupled() {
            return t -> apply($tupled);
        }

        @Override
        default <V> Function$i<${genericsFunction}V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return ($params) -> Try.of(() -> after.apply(apply($params))).orElseThrow(RuntimeException::new);
        }

        ${if (i == 1) xs"""
        default <V> Function1<V, R> compose(Function<? super V, ? extends T1> before) {
            Objects.requireNonNull(before);
            return v -> Try.of(() -> apply(before.apply(v))).orElseThrow(RuntimeException::new);
        }""" else ""}
    }

    /**
     * Alias for {@link λ$i}.
     * ${gen(1 to i)(j => s"@param <T$j> Argument type $j")("\n      * ")}
     * @param <R> Return type
     */
    @FunctionalInterface
    public static interface Function$i<${genericsFunction}R> extends λ$i<${genericsFunction}R> {
        ${if (i == 1) xs"""
        static <T> Function1<T, T> identity() {
            return t -> t;
        }""" else ""}
    }
    """
  }

  xs"""
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
    public static SerializedLambda getSerializedLambda(Serializable lambda) {
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
        final String signature = getSerializedLambda(lambda).getImplMethodSignature();
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

    ${gen(0 to N)(genFunctions)("\n\n")}

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
  """
}

def genTuple(): String = {

  def genFactoryMethod(i: Int) = {
    val generics = gen(1 to i)(j => s"T$j")(", ")
    val paramsDecl = gen(1 to i)(j => s"T$j t$j")(", ")
    val params = gen(1 to i)(j => s"t$j")(", ")
    xs"""
    static <$generics> Tuple$i<$generics> of($paramsDecl) {
        return new Tuple$i<>($params);
    }
    """
  }

  def genInnerTupleClass(i: Int) = {
    val generics = gen(1 to i)(j => s"T$j")(", ")
    val paramsDecl = gen(1 to i)(j => s"T$j t$j")(", ")
    xs"""
    /**
     * Implementation of a pair, a tuple containing $i elements.
     */
    static class Tuple$i<$generics> implements Tuple {

        private static final long serialVersionUID = 1L;

        ${gen(1 to i)(j => s"public final T$j _$j;")("\n")}

        public Tuple$i($paramsDecl) {
            ${gen(1 to i)(j => s"this._$j = t$j;")("\n")}
        }

        @Override
        public int arity() {
            return $i;
        }

        @Override
        public Tuple$i<$generics> unapply() {
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof Tuple$i)) {
                return false;
            } else {
                final Tuple$i that = (Tuple$i) o;
                return ${gen(1 to i)(j => s"Objects.equals(this._$j, that._$j)")("\n                         && ")};
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(${gen(1 to i)(j => s"_$j")(", ")});
        }

        @Override
        public String toString() {
            return String.format("(${gen(1 to i)(_ => s"%s")(", ")})", ${gen(1 to i)(j => s"_$j")(", ")});
        }
    }
    """
  }

  xs"""
  package javaslang;

  import java.util.Objects;

  public interface Tuple extends ValueObject {

      /**
       * Returns the number of elements of this tuple.
       *
       * @return The number of elements.
       */
      int arity();

      // -- factory methods

      static Tuple0 empty() {
          return Tuple0.instance();
      }

      ${gen(1 to N)(genFactoryMethod)("\n\n")}

      /**
       * Implementation of an empty tuple, a tuple containing no elements.
       */
      public static final class Tuple0 implements Tuple {

          private static final long serialVersionUID = 1L;

          /**
           * The singleton instance of Tuple0.
           */
          private static final Tuple0 INSTANCE = new Tuple0();

          /**
           * Hidden constructor.
           */
          private Tuple0() {
          }

          /**
           * Returns the singleton instance of Tuple0.
           *
           * @return The singleton instance of Tuple0.
           */
          public static Tuple0 instance() {
              return INSTANCE;
          }

          @Override
          public int arity() {
              return 0;
          }

          @Override
          public Tuple0 unapply() {
              return this;
          }

          @Override
          public boolean equals(Object o) {
              return o == this;
          }

          @Override
          public int hashCode() {
              return Objects.hash();
          }

          @Override
          public String toString() {
              return "()";
          }

          // -- Serializable implementation

          /**
           * Instance control for object serialization.
           *
           * @return The singleton instance of Tuple0.
           * @see java.io.Serializable
           */
          private Object readResolve() {
              return INSTANCE;
          }
      }

      ${gen(1 to N)(genInnerTupleClass)("\n\n")}
  }
  """
}

/**
 * Generates a Java file.
 * @param pkg A path of the java package
 * @param fileName A file name, may contain path segments.
 * @param gen A generator which produces a String.
 */
def genJavaFile(pkg: String, fileName: String)(gen: () => String)(implicit charset: Charset = StandardCharsets.UTF_8): Unit = {

  println(s"Generating $pkg/$fileName")

  val fileContents =   xs"""
    ${classHeader()}
    ${gen.apply()}
  """

  import java.nio.file.{Paths, Files}

  Files.write(
    Files.createDirectories(Paths.get(TARGET, pkg)).resolve(fileName),
    fileContents.getBytes(charset),
    StandardOpenOption.CREATE, StandardOpenOption.WRITE)
}

/**
 * Applies f for a range of Ints using delimiter to mkString the output.
 * @param range A range of Ints
 * @param f A generator which takes an Int and produces a String
 * @param delimiter The delimiter of the strings parts
 * @return Generated String
 */
def gen(range: Range)(f: Int => String)(implicit delimiter: String = "") = range.map(i => f.apply(i)) mkString delimiter

/**
 * The header for Java files.
 * @return A header as String
 */
def classHeader() = xs"""
  /**    / \\____  _    ______   _____ / \\____   ____  _____
   *    /  \\__  \\/ \\  / \\__  \\ /  __//  \\__  \\ /    \\/ __  \\   Javaslang
   *  _/  // _\\  \\  \\/  / _\\  \\\\_  \\/  // _\\  \\  /\\  \\__/  /   Copyright 2014-2015 Daniel Dietrich
   * /___/ \\_____/\\____/\\_____/____/\\___\\_____/_/  \\_/____/    Licensed under the Apache License, Version 2.0
   */
  """

/**
 * Indentation of cascaded rich strings.
 * @see https://gist.github.com/danieldietrich/5174348
 */
object StringContextImplicits {

  implicit class StringContextExtension(sc: StringContext) {

    def xs(args: Any*): String = align(sc.s, args)

    def xraw(args: Any*): String = align(sc.raw, args)

    /**
     * Indenting a rich string, removing first and last newline.
     * A rich string consists of arguments surrounded by text parts.
     */
    private def align(interpolator: Seq[Any] => String, args: Seq[Any]) = {

      // indent embedded strings, invariant: parts.length = args.length + 1
      val indentedArgs = for {
        (part, arg) <- sc.parts zip args.map(s => if (s == null) "" else s.toString)
      } yield {
        // get the leading space of last line of current part
        val space = """([ \t]*)[^\s]*$""".r.findFirstMatchIn(part).map(_.group(1)).getOrElse("")
        // add this leading space to each line (except the first) of current arg
        arg.split("\r?\n") match {
            case lines: Array[String] if lines.length > 0 => lines reduce (_ + lineSeparator + space + _)
            case whitespace => whitespace mkString ""
        }
      }

      // remove first and last newline and split string into separate lines
      // adding termination symbol \u0000 in order to preserve empty strings between last newlines when splitting
      val split = (interpolator(indentedArgs).replaceAll( """(^[ \t]*\r?\n)|(\r?\n[ \t]*$)""", "") + '\u0000').split("\r?\n")

      // find smallest indentation
      val prefix = split filter (!_.trim().isEmpty) map { s =>
        """^\s+""".r.findFirstIn(s).getOrElse("")
      } match {
        case prefixes: Array[String] if prefixes.length > 0 => prefixes reduce { (s1, s2) =>
          if (s1.length <= s2.length) s1 else s2
        }
        case _ => ""
      }

      // align all lines
      split map { s =>
        if (s.startsWith(prefix)) s.substring(prefix.length) else s
      } mkString lineSeparator dropRight 1 // dropping termination character \u0000
    }
  }

}
