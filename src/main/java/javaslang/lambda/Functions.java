/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lambda;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Predicate;

import javaslang.Tuples;

public interface Functions {

	/**
	 * A predicate which implements Serializable in order to obtain runtime type information about the parameter type
	 * via {@link javaslang.lambda.Lambdas#getLambdaSignature(Serializable)}.
	 *
	 * @param <T> The parameter type of the predicate.
	 */
	@FunctionalInterface
	public interface SerializablePredicate<T> extends Predicate<T>, Serializable {
	}

	/**
	 * A function with one argument which implements Serializable in order to obtain runtime type information about the
	 * lambda via {@link javaslang.lambda.Lambdas#getLambdaSignature(Serializable)}.
	 *
	 * @param <T1> The parameter type of the function.
	 * @param <R> The return type of the function.
	 */
	@FunctionalInterface
	static interface SerializableFunction1<T1, R> extends Function<T1, R>, Serializable {

		@Override
		R apply(T1 t1);

		default SerializableFunction1<T1, R> curried() {
			return t1 -> apply(t1);
		}

		default SerializableFunction1<Tuples.Tuple1<T1>, R> tupled() {
			return t -> apply(t._1);
		}
	}

	@FunctionalInterface
	static interface SerializableFunction2<T1, T2, R> extends Serializable {

		R apply(T1 t1, T2 t2);

		default SerializableFunction1<T1, SerializableFunction1<T2, R>> curried() {
			return t1 -> t2 -> apply(t1, t2);
		}

		default SerializableFunction1<Tuples.Tuple2<T1, T2>, R> tupled() {
			return t -> apply(t._1, t._2);
		}
	}

	@FunctionalInterface
	static interface SerializableFunction3<T1, T2, T3, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3);

		default SerializableFunction1<T1, SerializableFunction1<T2, SerializableFunction1<T3, R>>> curried() {
			return t1 -> t2 -> t3 -> apply(t1, t2, t3);
		}

		default SerializableFunction1<Tuples.Tuple3<T1, T2, T3>, R> tupled() {
			return t -> apply(t._1, t._2, t._3);
		}
	}

	@FunctionalInterface
	static interface SerializableFunction4<T1, T2, T3, T4, R> extends Serializable {

		R apply(T1 t1, T2 t2, T3 t3, T4 t4);

		default SerializableFunction1<T1, SerializableFunction1<T2, SerializableFunction1<T3, SerializableFunction1<T4, R>>>> curried() {
			return t1 -> t2 -> t3 -> t4 -> apply(t1, t2, t3, t4);
		}

		default SerializableFunction1<Tuples.Tuple4<T1, T2, T3, T4>, R> tupled() {
			return t -> apply(t._1, t._2, t._3, t._4);
		}
	}

	// TODO: generate SerializableFunction5 to SerializableFunction13
}
