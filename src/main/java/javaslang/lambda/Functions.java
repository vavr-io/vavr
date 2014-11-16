/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lambda;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import javaslang.collection.Tuple;

public interface Functions {

	@FunctionalInterface
	static interface Function0<R> extends Supplier<R>, Serializable {

		default Function0<R> curried() {
			return this;
		}

		default Function0<R> tupled() {
			return this;
		}

		default <V> Function0<V> andThen(Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return () -> after.apply(get());
		}
	}

	/**
	 * A function with one argument which implements Serializable in order to obtain runtime type information about the
	 * lambda via {@link javaslang.lambda.Lambdas#getLambdaSignature(Serializable)}.
	 *
	 * @param <T1> The parameter type of the function.
	 * @param <R> The return type of the function.
	 */
	@FunctionalInterface
	static interface Function1<T1, R> extends Function<T1, R>, Serializable {

		@Override
		R apply(T1 t1);

		default Function1<T1, R> curried() {
			return t1 -> apply(t1);
		}

		default Function1<Tuple.Tuple1<T1>, R> tupled() {
			return t -> apply(t._1);
		}

		@Override
		default <V> Function1<T1, V> andThen(Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return t1 -> after.apply(apply(t1));
		}

		@Override
		default <V> Function1<V, R> compose(Function<? super V, ? extends T1> before) {
			Objects.requireNonNull(before);
			return (V v) -> apply(before.apply(v));
		}

		static <T> Function1<T, T> identity() {
			return t -> t;
		}
	}

	@FunctionalInterface
	static interface Function2<T1, T2, R> extends BiFunction<T1, T2, R>, Serializable {

		@Override
		R apply(T1 t1, T2 t2);

		default Function1<T1, Function1<T2, R>> curried() {
			return t1 -> t2 -> apply(t1, t2);
		}

		default Function1<Tuple.Tuple2<T1, T2>, R> tupled() {
			return t -> apply(t._1, t._2);
		}

		@Override
		default <V> Function2<T1, T2, V> andThen(Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2) -> after.apply(apply(t1, t2));
		}
	}

	@FunctionalInterface
	static interface Function3<T1, T2, T3, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3);

		default Function1<T1, Function1<T2, Function1<T3, R>>> curried() {
			return t1 -> t2 -> t3 -> apply(t1, t2, t3);
		}

		default Function1<Tuple.Tuple3<T1, T2, T3>, R> tupled() {
			return t -> apply(t._1, t._2, t._3);
		}

		default <V> Function3<T1, T2, T3, V> andThen(Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3) -> after.apply(apply(t1, t2, t3));
		}
	}

	@FunctionalInterface
	static interface Function4<T1, T2, T3, T4, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, R>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> apply(t1, t2, t3, t4);
		}

		default Function1<Tuple.Tuple4<T1, T2, T3, T4>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4);
		}

		default <V> Function4<T1, T2, T3, T4, V> andThen(Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
		}
	}

	@FunctionalInterface
	static interface Function5<T1, T2, T3, T4, T5, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, R>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> apply(t1, t2, t3, t4, t5);
		}

		default Function1<Tuple.Tuple5<T1, T2, T3, T4, T5>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5);
		}

		default <V> Function5<T1, T2, T3, T4, T5, V> andThen(Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5) -> after.apply(apply(t1, t2, t3, t4, t5));
		}
	}

	@FunctionalInterface
	static interface Function6<T1, T2, T3, T4, T5, T6, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, R>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> apply(t1, t2, t3, t4, t5, t6);
		}

		default Function1<Tuple.Tuple6<T1, T2, T3, T4, T5, T6>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6);
		}

		default <V> Function6<T1, T2, T3, T4, T5, T6, V> andThen(Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6) -> after.apply(apply(t1, t2, t3, t4, t5, t6));
		}
	}

	@FunctionalInterface
	static interface Function7<T1, T2, T3, T4, T5, T6, T7, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, R>>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> apply(t1, t2, t3, t4, t5, t6, t7);
		}

		default Function1<Tuple.Tuple7<T1, T2, T3, T4, T5, T6, T7>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7);
		}

		default <V> Function7<T1, T2, T3, T4, T5, T6, T7, V> andThen(Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7));
		}
	}

	@FunctionalInterface
	static interface Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, R>>>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> apply(t1, t2, t3, t4, t5, t6, t7, t8);
		}

		default Function1<Tuple.Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8);
		}

		default <V> Function8<T1, T2, T3, T4, T5, T6, T7, T8, V> andThen(Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8));
		}
	}

	@FunctionalInterface
	static interface Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, R>>>>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9);
		}

		default Function1<Tuple.Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9);
		}

		default <V> Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, V> andThen(Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9));
		}
	}

	@FunctionalInterface
	static interface Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, Function1<T10, R>>>>>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> apply(t1, t2, t3, t4, t5, t6, t7, t8,
					t9, t10);
		}

		default Function1<Tuple.Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10);
		}

		default <V> Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, V> andThen(
				Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9,
					t10));
		}
	}

	@FunctionalInterface
	static interface Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, Function1<T10, Function1<T11, R>>>>>>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> apply(t1, t2, t3, t4, t5, t6,
					t7, t8, t9, t10, t11);
		}

		default Function1<Tuple.Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11);
		}

		default <V> Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, V> andThen(
				Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8,
					t9, t10, t11));
		}
	}

	@FunctionalInterface
	static interface Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, Function1<T10, Function1<T11, Function1<T12, R>>>>>>>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> apply(t1, t2, t3, t4, t5,
					t6, t7, t8, t9, t10, t11, t12);
		}

		default Function1<Tuple.Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12);
		}

		default <V> Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, V> andThen(
				Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12));
		}
	}

	@FunctionalInterface
	static interface Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, Function1<T10, Function1<T11, Function1<T12, Function1<T13, R>>>>>>>>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> apply(t1, t2, t3,
					t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
		}

		default Function1<Tuple.Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13);
		}

		default <V> Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, V> andThen(
				Function<? super R, ? extends V> after) {
			Objects.requireNonNull(after);
			return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) -> after.apply(apply(t1, t2, t3, t4, t5,
					t6, t7, t8, t9, t10, t11, t12, t13));
		}
	}
}
