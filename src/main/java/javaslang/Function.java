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
import java.util.function.BiFunction;
import java.util.function.Supplier;

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
 * The class of reflective functions with a specific return type.
 */
public interface Function<R> extends Serializable {

	/**
	 * @return the numper of function arguments.
	 * @see <a href="http://en.wikipedia.org/wiki/Arity">Arity</a>
	 */
	int arity();

	Function1<?, ?> curried();

	Function1<? extends Tuple, R> tupled();

	<V> Function<V> andThen(java.util.function.Function<? super R, ? extends V> after);

	default MethodType getType() {
		return Function.getLambdaSignature(this);
	}

	@FunctionalInterface
	static interface Function0<R> extends Function<R>, Supplier<R> {

		R apply();

		@Override
		default R get() {
			return apply();
		}

		@Override
		default int arity() {
			return 0;
		}

		// http://stackoverflow.com/questions/643906/uses-for-the-java-void-reference-type
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
	 * A function with one argument which implements Serializable in order to obtain reflective runtime type information
	 * via {@link javaslang.Function#getLambdaSignature(Serializable)}.
	 *
	 * @param <T1> The parameter type of the function.
	 * @param <R> The return type of the function.
	 */
	@FunctionalInterface
	static interface Function1<T1, R> extends Function<R>, java.util.function.Function<T1, R> {

		@Override
		R apply(T1 t1);

		@Override
		default int arity() {
			return 1;
		}

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

		@Override
		default <V> Function1<V, R> compose(java.util.function.Function<? super V, ? extends T1> before) {
			Objects.requireNonNull(before);
			return (V v) -> apply(before.apply(v));
		}

		static <T> Function1<T, T> identity() {
			return t -> t;
		}
	}

	@FunctionalInterface
	static interface Function2<T1, T2, R> extends Function<R>, BiFunction<T1, T2, R> {

		@Override
		default int arity() {
			return 2;
		}

		@Override
		R apply(T1 t1, T2 t2);

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

	@FunctionalInterface
	static interface Function3<T1, T2, T3, R> extends Function<R> {

		R apply(T1 t1, T2 t2, T3 t3);

		@Override
		default int arity() {
			return 3;
		}

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

	@FunctionalInterface
	static interface Function4<T1, T2, T3, T4, R> extends Function<R> {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4);

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
		default <V> Function4<T1, T2, T3, T4, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
		}
	}

	@FunctionalInterface
	static interface Function5<T1, T2, T3, T4, T5, R> extends Function<R> {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);

		@Override
		default int arity() {
			return 5;
		}

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

	@FunctionalInterface
	static interface Function6<T1, T2, T3, T4, T5, T6, R> extends Function<R> {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

		@Override
		default int arity() {
			return 6;
		}

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

	@FunctionalInterface
	static interface Function7<T1, T2, T3, T4, T5, T6, T7, R> extends Function<R> {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7);

		@Override
		default int arity() {
			return 7;
		}

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

	@FunctionalInterface
	static interface Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> extends Function<R> {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8);

		@Override
		default int arity() {
			return 8;
		}

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

	@FunctionalInterface
	static interface Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> extends Function<R> {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9);

		@Override
		default int arity() {
			return 9;
		}

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

	@FunctionalInterface
	static interface Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> extends Function<R> {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10);

		@Override
		default int arity() {
			return 10;
		}

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

	@FunctionalInterface
	static interface Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> extends Function<R> {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11);

		@Override
		default int arity() {
			return 11;
		}

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

	@FunctionalInterface
	static interface Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> extends Function<R> {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12);

		@Override
		default int arity() {
			return 12;
		}

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

	@FunctionalInterface
	static interface Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> extends Function<R> {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

		@Override
		default int arity() {
			return 13;
		}

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
