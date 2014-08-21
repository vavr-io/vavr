/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import static javaslang.Requirements.requireNonNull;
import javaslang.Lambdas;
import javaslang.Lambdas.LambdaSignature;
import javaslang.Tuples;
import javaslang.Tuples.Tuple;
import javaslang.Tuples.Tuple1;
import javaslang.Tuples.Tuple2;
import javaslang.option.Option;

// TODO: Matchs.caze(Pattern.of(decomposableOfA, any(), any()), (a, dA) -> ???)
public interface Pattern<R extends Tuple> extends Applicative<Option<R>> {

	/**
	 * Creates a Pattern for objects of type T. The Pattern matches a given object o, if {@code isApplicable(o)} returns
	 * true and {@code prototype.equals(decomposition.apply(o))} returns true.
	 * 
	 * @param <T> Type of objects to be pattern matched.
	 * @param <R> Decomposition result.
	 * @param decomposable A Decomposable which provides a Decomposition for objects of type T.
	 * @param prototype The Pattern matches, ifA prototype which will be compared
	 * @return Some(decompositionResult) if the Pattern matches, otherwise None.
	 */
	static <T, R extends Tuple> Pattern<R> of(Decomposable<T, R> decomposable, R prototype) {

		requireNonNull(decomposable, "decomposable is null");
		requireNonNull(prototype, "prototype is null");

		final Decomposition<T, R> decomposition = requireNonNull(decomposable.decomposition(), "decomposition is null");
		final LambdaSignature signature = Lambdas.getLambdaSignature(decomposition);

		return new Pattern<R>() {
			@Override
			public boolean isApplicable(Object obj) {
				return obj != null && signature.getParameterType(0).isAssignableFrom(obj.getClass());
			}

			@Override
			@SuppressWarnings("unchecked")
			public Option<R> apply(Object obj) {
				final R components = decomposition.apply((T) obj);
				if (prototype.equals(components)) {
					return Option.of(components);
				} else {
					return Option.empty();
				}
			}
		};
	}

	/**
	 * Convenience method for {@code Pattern.of(decomposable, Tuples.of(e1))}.
	 * 
	 * @param <T> Type of objects to be pattern matched.
	 * @param <E1> Type of decomposition component 1.
	 * @param decomposable A Decomposable which provides a {@link Decomposition} of arity 1 for object of type T.
	 * @param e1 Component 1 of the pattern that will be matched against component 1 of a decomposition.
	 * @return A Pattern which is applicable to objects of type T.
	 */
	static <T, E1> Pattern<Tuple1<E1>> of(Decomposable<T, Tuple1<E1>> decomposable, E1 e1) {
		return Pattern.of(decomposable, Tuples.of(e1));
	}

	/**
	 * Convenience method for {@code Pattern.of(decomposable, Tuples.of(e1, e2))}.
	 * 
	 * @param <T> Type of objects to be pattern matched.
	 * @param <E1> Type of decomposition component 1.
	 * @param <E2> Type of decomposition component 2.
	 * @param decomposable A Decomposable which provides a {@link Decomposition} of arity 2 for object of type T.
	 * @param e1 Component 1 of the pattern that will be matched against component 1 of a decomposition.
	 * @param e2 Component 2 of the pattern that will be matched against component 2 of a decomposition.
	 * @return A Pattern which is applicable to objects of type T.
	 */
	static <T, E1, E2> Pattern<Tuple2<E1, E2>> of(Decomposable<T, Tuple2<E1, E2>> decomposable, E1 e1, E2 e2) {
		return Pattern.of(decomposable, Tuples.of(e1, e2));
	}

	// TODO: commented out because of Eclipse JDT bug <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=442245">https://bugs.eclipse.org/bugs/show_bug.cgi?id=442245</a>
	//	static <T, E1, E2, E3> Pattern<Tuple3<E1, E2, E3>> of(Decomposable<T, Tuple3<E1, E2, E3>> decomposable, E1 e1,
	//			E2 e2, E3 e3) {
	//		return Pattern.of(decomposable, Tuples.of(e1, e2, e3));
	//	}
	//
	//	static <T, E1, E2, E3, E4> Pattern<Tuple4<E1, E2, E3, E4>> of(
	//			Decomposable<T, Tuple4<E1, E2, E3, E4>> decomposable, E1 e1, E2 e2, E3 e3, E4 e4) {
	//		return Pattern.of(decomposable, Tuples.of(e1, e2, e3, e4));
	//	}
	//
	//	static <T, E1, E2, E3, E4, E5> Pattern<Tuple5<E1, E2, E3, E4, E5>> of(
	//			Decomposable<T, Tuple5<E1, E2, E3, E4, E5>> decomposable, E1 e1, E2 e2, E3 e3, E4 e4, E5 e5) {
	//		return Pattern.of(decomposable, Tuples.of(e1, e2, e3, e4, e5));
	//	}
	//
	//	static <T, E1, E2, E3, E4, E5, E6> Pattern<Tuple6<E1, E2, E3, E4, E5, E6>> of(
	//			Decomposable<T, Tuple6<E1, E2, E3, E4, E5, E6>> decomposable, E1 e1, E2 e2, E3 e3, E4 e4, E5 e5, E6 e6) {
	//		return Pattern.of(decomposable, Tuples.of(e1, e2, e3, e4, e5, e6));
	//	}
	//
	//	static <T, E1, E2, E3, E4, E5, E6, E7> Pattern<Tuple7<E1, E2, E3, E4, E5, E6, E7>> of(
	//			Decomposable<T, Tuple7<E1, E2, E3, E4, E5, E6, E7>> decomposable, E1 e1, E2 e2, E3 e3, E4 e4, E5 e5,
	//			E6 e6, E7 e7) {
	//		return Pattern.of(decomposable, Tuples.of(e1, e2, e3, e4, e5, e6, e7));
	//	}
	//
	//	static <T, E1, E2, E3, E4, E5, E6, E7, E8> Pattern<Tuple8<E1, E2, E3, E4, E5, E6, E7, E8>> of(
	//			Decomposable<T, Tuple8<E1, E2, E3, E4, E5, E6, E7, E8>> decomposable, E1 e1, E2 e2, E3 e3, E4 e4, E5 e5,
	//			E6 e6, E7 e7, E8 e8) {
	//		return Pattern.of(decomposable, Tuples.of(e1, e2, e3, e4, e5, e6, e7, e8));
	//	}
	//
	//	static <T, E1, E2, E3, E4, E5, E6, E7, E8, E9> Pattern<Tuple9<E1, E2, E3, E4, E5, E6, E7, E8, E9>> of(
	//			Decomposable<T, Tuple9<E1, E2, E3, E4, E5, E6, E7, E8, E9>> decomposable, E1 e1, E2 e2, E3 e3, E4 e4,
	//			E5 e5, E6 e6, E7 e7, E8 e8, E9 e9) {
	//		return Pattern.of(decomposable, Tuples.of(e1, e2, e3, e4, e5, e6, e7, e8, e9));
	//	}
	//
	//	static <T, E1, E2, E3, E4, E5, E6, E7, E8, E9, E10> Pattern<Tuple10<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10>> of(
	//			Decomposable<T, Tuple10<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10>> decomposable, E1 e1, E2 e2, E3 e3,
	//			E4 e4, E5 e5, E6 e6, E7 e7, E8 e8, E9 e9, E10 e10) {
	//		return Pattern.of(decomposable, Tuples.of(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10));
	//	}
	//
	//	static <T, E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11> Pattern<Tuple11<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11>> of(
	//			Decomposable<T, Tuple11<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11>> decomposable, E1 e1, E2 e2, E3 e3,
	//			E4 e4, E5 e5, E6 e6, E7 e7, E8 e8, E9 e9, E10 e10, E11 e11) {
	//		return Pattern.of(decomposable, Tuples.of(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11));
	//	}
	//
	//	static <T, E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11, E12> Pattern<Tuple12<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11, E12>> of(
	//			Decomposable<T, Tuple12<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11, E12>> decomposable, E1 e1, E2 e2, E3 e3,
	//			E4 e4, E5 e5, E6 e6, E7 e7, E8 e8, E9 e9, E10 e10, E11 e11, E12 e12) {
	//		return Pattern.of(decomposable, Tuples.of(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12));
	//	}
	//
	//	static <T, E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11, E12, E13> Pattern<Tuple13<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11, E12, E13>> of(
	//			Decomposable<T, Tuple13<E1, E2, E3, E4, E5, E6, E7, E8, E9, E10, E11, E12, E13>> decomposable, E1 e1, E2 e2, E3 e3,
	//			E4 e4, E5 e5, E6 e6, E7 e7, E8 e8, E9 e9, E10 e10, E11 e11, E12 e12, E13 e13) {
	//		return Pattern.of(decomposable, Tuples.of(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13));
	//	}
}
