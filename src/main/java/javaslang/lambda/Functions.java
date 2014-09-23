/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lambda;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Supplier;

import javaslang.Tuples;

public interface Functions {

	@FunctionalInterface
	static interface Function0<R> extends Supplier<R>, Serializable {

		default Function0<R> curried() {
			return this;
		}

		default Function0<R> tupled() {
			return this;
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

		default Function1<Tuples.Tuple1<T1>, R> tupled() {
			return t -> apply(t._1);
		}
	}

	@FunctionalInterface
	static interface Function2<T1, T2, R> extends Serializable {

		R apply(T1 t1, T2 t2);

		default Function1<T1, Function1<T2, R>> curried() {
			return t1 -> t2 -> apply(t1, t2);
		}

		default Function1<Tuples.Tuple2<T1, T2>, R> tupled() {
			return t -> apply(t._1, t._2);
		}
	}

	@FunctionalInterface
	static interface Function3<T1, T2, T3, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3);

		default Function1<T1, Function1<T2, Function1<T3, R>>> curried() {
			return t1 -> t2 -> t3 -> apply(t1, t2, t3);
		}

		default Function1<Tuples.Tuple3<T1, T2, T3>, R> tupled() {
			return t -> apply(t._1, t._2, t._3);
		}
	}

	@FunctionalInterface
	static interface Function4<T1, T2, T3, T4, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, R>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> apply(t1, t2, t3, t4);
		}

		default Function1<Tuples.Tuple4<T1, T2, T3, T4>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4);
		}
	}

	@FunctionalInterface
	static interface Function5<T1, T2, T3, T4, T5, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, R>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> apply(t1, t2, t3, t4, t5);
		}

		default Function1<Tuples.Tuple5<T1, T2, T3, T4, T5>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5);
		}
	}

	@FunctionalInterface
	static interface Function6<T1, T2, T3, T4, T5, T6, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, R>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> apply(t1, t2, t3, t4, t5, t6);
		}

		default Function1<Tuples.Tuple6<T1, T2, T3, T4, T5, T6>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6);
		}
	}

	@FunctionalInterface
	static interface Function7<T1, T2, T3, T4, T5, T6, T7, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, R>>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> apply(t1, t2, t3, t4, t5, t6, t7);
		}

		default Function1<Tuples.Tuple7<T1, T2, T3, T4, T5, T6, T7>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7);
		}
	}

	@FunctionalInterface
	static interface Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, R>>>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> apply(t1, t2, t3, t4, t5, t6, t7, t8);
		}

		default Function1<Tuples.Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8);
		}
	}

	@FunctionalInterface
	static interface Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, R>>>>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> apply(t1, t2, t3, t4, t5, t6, t7, t8, t9);
		}

		default Function1<Tuples.Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9);
		}
	}

	@FunctionalInterface
	static interface Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, Function1<T10, R>>>>>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> apply(t1, t2, t3, t4, t5, t6, t7, t8,
					t9, t10);
		}

		default Function1<Tuples.Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10);
		}
	}

	@FunctionalInterface
	static interface Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, Function1<T10, Function1<T11, R>>>>>>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> apply(t1, t2, t3, t4, t5, t6,
					t7, t8, t9, t10, t11);
		}

		default Function1<Tuples.Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11);
		}
	}

	@FunctionalInterface
	static interface Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, Function1<T10, Function1<T11, Function1<T12, R>>>>>>>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> apply(t1, t2, t3, t4, t5,
					t6, t7, t8, t9, t10, t11, t12);
		}

		default Function1<Tuples.Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12);
		}
	}

	@FunctionalInterface
	static interface Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13);

		default Function1<T1, Function1<T2, Function1<T3, Function1<T4, Function1<T5, Function1<T6, Function1<T7, Function1<T8, Function1<T9, Function1<T10, Function1<T11, Function1<T12, Function1<T13, R>>>>>>>>>>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> apply(t1, t2, t3,
					t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
		}

		default Function1<Tuples.Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13);
		}
	}
}
