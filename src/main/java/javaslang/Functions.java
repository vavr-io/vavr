/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Objects;

import javaslang.Tuple.Tuple0;
import javaslang.Tuple.Tuple1;
import javaslang.Tuple.Tuple10;
import javaslang.Tuple.Tuple11;
import javaslang.Tuple.Tuple12;
import javaslang.Tuple.Tuple13;
import javaslang.Tuple.Tuple2;
import javaslang.Tuple.Tuple3;
import javaslang.Tuple.Tuple4;
import javaslang.Tuple.Tuple5;
import javaslang.Tuple.Tuple6;
import javaslang.Tuple.Tuple7;
import javaslang.Tuple.Tuple8;
import javaslang.Tuple.Tuple9;
import javaslang.monad.Try;

/**
 * The class of reflective checked and unchecked functions with a specific return type.
 * <p/>
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
	 * The most general function type is a checked function of unknown parameters and a return value of type R.
	 * A checked function may throw an exception. The exception type cannot be specified as generic because
	 * when composing functions, we cannot say anything about the resulting exception type (in Java generics).
	 * <p/>
	 * <em>Note: X stands for 'eXception'. The lambda symbol λ is hidden in X, just leave out the upper right line.</em>
	 *
	 * @param <R> Return type of the checked function.
	 */
	public static interface X<R> extends Serializable {

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
		 * @param after
		 * @param <V>
		 * @return
		 */
		<V> CheckedFunction<V> andThen(X1<? super R, ? extends V> after);

		<V> CheckedFunction<V> andThen(java.util.function.Function<? super R, ? extends V> after);

		default MethodType getType() {
			return Functions.getLambdaSignature(this);
		}
	}

	/**
	 * Alias for {@link X}.
	 */
	public static interface CheckedFunction<R> extends X<R> {
	}

	@FunctionalInterface
	public static interface X0<R> extends CheckedFunction<R> {

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
		default <V> CheckedFunction0<V> andThen(X1<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return () -> after.apply(apply());
		}

		@Override
		default <V> CheckedFunction0<V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return () -> after.apply(apply());
		}
	}

	/**
	 * Alias for {@link X0}.
	 */
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

		@Override
		default <V> Function0<V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return () -> after.apply(apply());
		}
	}

	/**
	 * Alias for {@link λ0}.
	 */
	public static interface Function0<R> extends λ0<R> {
	}

    /**
	 * A function with one argument which implements Serializable in order to obtain reflective runtime type information
	 * via {@link javaslang.Functions#getLambdaSignature(Serializable)}.
	 *
	 * @param <T1> The parameter type of the function.
	 * @param <R> The return type of the function.
	 */
	@FunctionalInterface
	public static interface X1<T1, R> extends CheckedFunction<R> {

		R apply(T1 t1) throws Throwable;

		@Override
		default int arity() {
			return 1;
		}

		@Override
		default CheckedFunction1<T1, R> curried() {
			return t1 -> apply(t1);
		}

		@Override
		default CheckedFunction1<Tuple1<T1>, R> tupled() {
			return t -> apply(t._1);
		}

		@Override
		default <V> CheckedFunction1<T1, V> andThen(X1<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return t1 -> after.apply(apply(t1));
		}

		@Override
		default <V> CheckedFunction1<T1, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return t1 -> after.apply(apply(t1));
		}

		default <V> CheckedFunction1<V, R> compose(X1<? super V, ? extends T1> before) {
			Objects.requireNonNull(before);
			return v -> apply(before.apply(v));
		}

		default <V> CheckedFunction1<V, R> compose(java.util.function.Function<? super V, ? extends T1> before) {
			Objects.requireNonNull(before);
			return v -> apply(before.apply(v));
		}

		static <T> CheckedFunction1<T, T> identity() {
			return t -> t;
		}
	}

	/**
	 * Alias for {@link X1}.
	 */
	public static interface CheckedFunction1<T1, R> extends X1<T1, R> {

		static <T> CheckedFunction1<T, T> identity() {
			return t -> t;
		}
	}

	public static interface λ1<T1, R> extends CheckedFunction1<T1, R>, java.util.function.Function<T1, R> {

		@Override
		R apply(T1 t1) throws RuntimeException;

		@Override
		default Function1<T1, R> curried() {
			return t1 -> apply(t1);
		}

		@Override
		default Function1<Tuple1<T1>, R> tupled() {
			return t -> apply(t._1);
		}

		@Override
		default <V> Function1<T1, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return t1 -> after.apply(apply(t1));
		}

		default <V> Function1<V, R> compose(java.util.function.Function<? super V, ? extends T1> before) {
			Objects.requireNonNull(before);
			return (V v) -> apply(before.apply(v));
		}

		static <T> Function1<T, T> identity() {
			return t -> t;
		}
	}

	/**
	 * Alias for {@link λ1}.
	 */
	public static interface Function1<T1, R> extends λ1<T1, R> {

		static <T> Function1<T, T> identity() {
			return t -> t;
		}
	}

	@FunctionalInterface
	public static interface X2<T1, T2, R> extends CheckedFunction<R> {

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
		default <V> CheckedFunction2<T1, T2, V> andThen(X1<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2) -> after.apply(apply(t1, t2));
		}

		@Override
		default <V> CheckedFunction2<T1, T2, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2) -> after.apply(apply(t1, t2));
		}
	}

	/**
	 * Alias for {@link X2}.
	 */
	public static interface CheckedFunction2<T1, T2, R> extends X2<T1, T2, R> {
	}

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
		default <V> Function2<T1, T2, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2) -> after.apply(apply(t1, t2));
		}
	}

	/**
	 * Alias for {@link λ2}.
	 */
	public static interface Function2<T1, T2, R> extends λ2<T1, T2, R> {
	}

	@FunctionalInterface
	public static interface X3<T1, T2, T3, R> extends CheckedFunction<R> {

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
		default <V> CheckedFunction3<T1, T2, T3, V> andThen(X1<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3) -> after.apply(apply(t1, t2, t3));
		}

		@Override
		default <V> CheckedFunction3<T1, T2, T3, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3) -> after.apply(apply(t1, t2, t3));
		}
	}

	/**
	 * Alias for {@link X3}.
	 */
	public static interface CheckedFunction3<T1, T2, T3, R> extends X3<T1, T2, T3, R> {
	}

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
		default <V> Function3<T1, T2, T3, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3) -> after.apply(apply(t1, t2, t3));
		}
	}

	/**
	 * Alias for {@link λ3}.
	 */
	public static interface Function3<T1, T2, T3, R> extends λ3<T1, T2, T3, R> {
	}

	@FunctionalInterface
	public static interface X4<T1, T2, T3, T4, R> extends CheckedFunction<R> {

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
		default <V> CheckedFunction4<T1, T2, T3, T4, V> andThen(X1<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
		}

		@Override
		default <V> CheckedFunction4<T1, T2, T3, T4, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
		}
	}

	/**
	 * Alias for {@link X4}.
	 */
	public static interface CheckedFunction4<T1, T2, T3, T4, R> extends X4<T1, T2, T3, T4, R> {
	}

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
		default <V> Function4<T1, T2, T3, T4, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
		}
	}

	/**
	 * Alias for {@link λ4}.
	 */
	public static interface Function4<T1, T2, T3, T4, R> extends λ4<T1, T2, T3, T4, R> {
	}

	@FunctionalInterface
	public static interface X5<T1, T2, T3, T4, T5, R> extends CheckedFunction<R> {

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
		default <V> CheckedFunction5<T1, T2, T3, T4, T5, V> andThen(X1<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5) -> after.apply(apply(t1, t2, t3, t4, t5));
		}

		@Override
		default <V> CheckedFunction5<T1, T2, T3, T4, T5, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5) -> after.apply(apply(t1, t2, t3, t4, t5));
		}
	}

	/**
	 * Alias for {@link X5}.
	 */
	public static interface CheckedFunction5<T1, T2, T3, T4, T5, R> extends X5<T1, T2, T3, T4, T5, R> {
	}

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
		default <V> Function5<T1, T2, T3, T4, T5, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5) -> after.apply(apply(t1, t2, t3, t4, t5));
		}
	}

	/**
	 * Alias for {@link λ5}.
	 */
	public static interface Function5<T1, T2, T3, T4, T5, R> extends λ5<T1, T2, T3, T4, T5, R> {
	}

	@FunctionalInterface
	public static interface X6<T1, T2, T3, T4, T5, T6, R> extends CheckedFunction<R> {

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
		default <V> CheckedFunction6<T1, T2, T3, T4, T5, T6, V> andThen(X1<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6) -> after.apply(apply(t1, t2, t3, t4, t5, t6));
		}

		@Override
		default <V> CheckedFunction6<T1, T2, T3, T4, T5, T6, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6) -> after.apply(apply(t1, t2, t3, t4, t5, t6));
		}
	}

	/**
	 * Alias for {@link X6}.
	 */
	public static interface CheckedFunction6<T1, T2, T3, T4, T5, T6, R> extends X6<T1, T2, T3, T4, T5, T6, R> {
	}

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
		default <V> Function6<T1, T2, T3, T4, T5, T6, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6) -> after.apply(apply(t1, t2, t3, t4, t5, t6));
		}
	}

	/**
	 * Alias for {@link λ6}.
	 */
	public static interface Function6<T1, T2, T3, T4, T5, T6, R> extends λ6<T1, T2, T3, T4, T5, T6, R> {
	}

	@FunctionalInterface
	public static interface X7<T1, T2, T3, T4, T5, T6, T7, R> extends CheckedFunction<R> {

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
		default <V> CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, V> andThen(X1<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7));
		}

		@Override
		default <V> CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7));
		}
	}

	/**
	 * Alias for {@link X7}.
	 */
	public static interface CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, R> extends X7<T1, T2, T3, T4, T5, T6, T7, R> {
	}

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
		default <V> Function7<T1, T2, T3, T4, T5, T6, T7, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7));
		}
	}

	/**
	 * Alias for {@link λ7}.
	 */
	public static interface Function7<T1, T2, T3, T4, T5, T6, T7, R> extends λ7<T1, T2, T3, T4, T5, T6, T7, R> {
	}

	@FunctionalInterface
	public static interface X8<T1, T2, T3, T4, T5, T6, T7, T8, R> extends CheckedFunction<R> {

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
		default <V> CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, V> andThen(X1<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8));
		}

		@Override
		default <V> CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8));
		}
	}

	/**
	 * Alias for {@link X8}.
	 */
	public static interface CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, R> extends X8<T1, T2, T3, T4, T5, T6, T7, T8, R> {
	}

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
		default <V> Function8<T1, T2, T3, T4, T5, T6, T7, T8, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8));
		}
	}

	/**
	 * Alias for {@link λ8}.
	 */
	public static interface Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> extends λ8<T1, T2, T3, T4, T5, T6, T7, T8, R> {
	}

	@FunctionalInterface
	public static interface X9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> extends CheckedFunction<R> {

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
		default <V> CheckedFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, V> andThen(X1<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9));
		}

		@Override
		default <V> CheckedFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9));
		}
	}

	/**
	 * Alias for {@link X9}.
	 */
	public static interface CheckedFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> extends X9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> {
	}

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
		default <V> Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9));
		}
	}

	/**
	 * Alias for {@link λ9}.
	 */
	public static interface Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> extends λ9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> {
	}

	@FunctionalInterface
	public static interface X10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> extends CheckedFunction<R> {

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
		default <V> CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, V> andThen(X1<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
		}

		@Override
		default <V> CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9,
					t10));
		}
	}

	/**
	 * Alias for {@link X10}.
	 */
	public static interface CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> extends X10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> {
	}

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
		default <V> Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9,
					t10));
		}
	}

	/**
	 * Alias for {@link λ10}.
	 */
	public static interface Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> extends λ10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> {
	}

	@FunctionalInterface
	public static interface X11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> extends CheckedFunction<R> {

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
		default <V> CheckedFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, V> andThen(X1<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
		}

		@Override
		default <V> CheckedFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8,
					t9, t10, t11));
		}
	}

	/**
	 * Alias for {@link X11}.
	 */
	public static interface CheckedFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> extends X11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> {
	}

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
		default <V> Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8,
					t9, t10, t11));
		}
	}

	/**
	 * Alias for {@link λ11}.
	 */
	public static interface Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> extends λ11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> {
	}

	@FunctionalInterface
	public static interface X12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> extends CheckedFunction<R> {

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
		default <V> CheckedFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, V> andThen(X1<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
		}

		@Override
		default <V> CheckedFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, V> andThen(
				java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12));
		}
	}

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
		default <V> Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, V> andThen(
				java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12));
		}
	}

	/**
	 * Alias for {@link λ12}.
	 */
	public static interface Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> extends λ12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> {
	}

	/**
	 * Alias for {@link X12}.
	 */
	public static interface CheckedFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> extends X12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> {
	}

	@FunctionalInterface
	public static interface X13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> extends CheckedFunction<R> {

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
		default <V> CheckedFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, V> andThen(X1<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
		}

		@Override
		default <V> CheckedFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, V> andThen(
				java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) -> after.apply(apply(t1, t2, t3, t4, t5,
					t6, t7, t8, t9, t10, t11, t12, t13));
		}
	}

	/**
	 * Alias for {@link X13}.
	 */
	public static interface CheckedFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> extends X13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> {
	}

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
		default <V> Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, V> andThen(
				java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) -> after.apply(apply(t1, t2, t3, t4, t5,
					t6, t7, t8, t9, t10, t11, t12, t13));
		}
	}

	/**
	 * Alias for {@link λ13}.
	 */
	public static interface Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> extends λ13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> {
	}

	/**
	 * Serializes this lambda and returns the corresponding {@link java.lang.invoke.SerializedLambda}.
	 *
	 * @return The serialized lambda wrapped in a {@link javaslang.monad.Try.Success}, or a {@link javaslang.monad.Try.Failure}
	 *         if an exception occurred.
	 * @see <a
	 *      href="http://stackoverflow.com/questions/21860875/printing-debug-info-on-errors-with-java-8-lambda-expressions">printing
	 *      debug info on errors with java 8 lambda expressions</a>
	 * @see <a href="http://www.slideshare.net/hendersk/method-handles-in-java">Method Handles in Java</a>
	 */
	// TODO: Memoization / caching (using SoftReferences)
	static SerializedLambda getSerializedFunction(Serializable lambda) {
		return Try.of(() -> {
			final Method method = lambda.getClass().getDeclaredMethod("writeReplace");
			method.setAccessible(true);
			return (SerializedLambda) method.invoke(lambda);
		}).get();
	}

	/**
	 * Gets the runtime method signature of the given lambda instance. Especially this function is handy when the
	 * functional interface is generic and the parameter and/or return types cannot be determined directly.
	 * <p>
	 * Uses internally the {@link java.lang.invoke.SerializedLambda#getImplMethodSignature()} by parsing the JVM field
	 * types of the method signature. The result is a {@link java.lang.invoke.MethodType} which contains the return type
	 * and the parameter types of the given lambda.
	 *
	 * @param lambda A serializable lambda.
	 * @return The signature of the lambda as {@linkplain java.lang.invoke.MethodType}.
	 */
	// TODO: Memoization / caching (using SoftReferences)
	static MethodType getLambdaSignature(Serializable lambda) {
		final String signature = getSerializedFunction(lambda).getImplMethodSignature();
		return MethodType.fromMethodDescriptorString(signature, lambda.getClass().getClassLoader());
	}
}
